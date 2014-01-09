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
package org.eclipse.wst.jsdt.debug.internal.rhino.ui;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;

/**
 * Adapter factory for Rhino UI
 * 
 * @since 1.0
 */
public class RhinoAdapterFactory implements IAdapterFactory {

	static WorkbenchAdapter wsAdapter = null;
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	 */
	public synchronized Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType.equals(IWorkbenchAdapter.class)) {
			if(wsAdapter == null) {
				wsAdapter = new WorkbenchAdapter();
			}
			return wsAdapter;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	public Class[] getAdapterList() {
		return new Class[] {IWorkbenchAdapter.class};
	}
}

/**
 * A workbench adapter
 * 
 * @since 1.0
 */
class WorkbenchAdapter implements IWorkbenchAdapter {

	/* (non-Javadoc)
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object o) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getImageDescriptor(java.lang.Object)
	 */
	public ImageDescriptor getImageDescriptor(Object object) {
		
		return null;
	}

	/**
	 * Returns an image descriptor for a JavaScript element, or <code>null</code>
	 * if none.
	 * 
	 * @param element JavaScript element
	 * @return an image descriptor for a JavaScript element, or <code>null</code>
	 * if none
	 */
	protected ImageDescriptor getImageDescriptor(IJavaScriptElement element) {
		IWorkbenchAdapter adapter = (IWorkbenchAdapter)element.getAdapter(IWorkbenchAdapter.class);
		if (adapter != null) {
			return adapter.getImageDescriptor(element);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getLabel(java.lang.Object)
	 */
	public String getLabel(Object o) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getParent(java.lang.Object)
	 */
	public Object getParent(Object o) {
		return null;
	}
}
