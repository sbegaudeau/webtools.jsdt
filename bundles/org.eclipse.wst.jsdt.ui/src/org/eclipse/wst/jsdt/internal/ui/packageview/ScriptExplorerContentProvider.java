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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.wst.jsdt.internal.ui.navigator.JavaNavigatorContentProvider;

public class ScriptExplorerContentProvider extends JavaNavigatorContentProvider {
	private WorkbenchContentProvider fResourceProvider = new WorkbenchContentProvider();

	public ScriptExplorerContentProvider(boolean provideMembers) {
		super(provideMembers);
	}

	public void dispose() {
		fResourceProvider.dispose();
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

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		super.inputChanged(viewer, oldInput, newInput);
		fResourceProvider.inputChanged(viewer, oldInput != null ? ResourcesPlugin.getWorkspace() : null, newInput != null ? ResourcesPlugin.getWorkspace() : null);
	}
}
