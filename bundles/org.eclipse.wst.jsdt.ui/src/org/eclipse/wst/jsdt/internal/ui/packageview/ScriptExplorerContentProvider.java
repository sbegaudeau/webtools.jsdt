/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.packageview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.internal.ui.navigator.JavaNavigatorContentProvider;

public class ScriptExplorerContentProvider extends JavaNavigatorContentProvider {
	class RefreshListener implements IResourceDeltaVisitor, IResourceChangeListener {
		private List refreshQueue = new ArrayList();

		public void resourceChanged(IResourceChangeEvent event) {
			if (getProvideMembers())
				try {
					event.getDelta().accept(fRefreshListener);
					if (!refreshQueue.isEmpty()) {
						Collection runnables = new ArrayList();
						postRefresh(refreshQueue, false, runnables);
						executeRunnables(runnables);
					}
				}
				catch (CoreException e) {
				}
				finally {
					refreshQueue.clear();
				}
		}

		public boolean visit(IResourceDelta delta) throws CoreException {
			if ((delta.getFlags() & IResourceDelta.CONTENT) != 0) {
				IResource resource = delta.getResource();
				if (resource.getType() == IResource.FILE && JavaScriptCore.isJavaScriptLikeFileName(resource.getName())) {
					refreshQueue.add(resource);
				}
			}
			return true;
		}
	}

	private WorkbenchContentProvider fResourceProvider = new WorkbenchContentProvider();

	private RefreshListener fRefreshListener = new RefreshListener();

	public ScriptExplorerContentProvider(boolean provideMembers) {
		super(provideMembers);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(fRefreshListener, IResourceChangeEvent.POST_CHANGE);
	}

	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(fRefreshListener);
		super.dispose();
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IResource) {
			switch (((IResource) parentElement).getType()) {
				case IResource.PROJECT :
				case IResource.FILE :
					return concatenate(super.getChildren(parentElement), fResourceProvider.getChildren(parentElement));
				case IResource.FOLDER :
					return fResourceProvider.getChildren(parentElement);
			}
		}
		return super.getChildren(parentElement);
	}

	public Object getParent(Object element) {
		if (element instanceof IResource) {
			return fResourceProvider.getParent(element);
		}
		return super.getParent(element);
	}

}
