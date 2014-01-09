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
package org.eclipse.wst.jsdt.debug.internal.ui.actions;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.wst.jsdt.debug.internal.core.model.JavaScriptValue;
import org.eclipse.wst.jsdt.debug.internal.core.model.JavaScriptVariable;
import org.eclipse.wst.jsdt.debug.internal.ui.Constants;

/**
 * Viewer filter action for the <code>this</code> variable
 * 
 * @since 1.1
 */
public class ThisFilterAction extends ViewFilterAction {

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.ui.actions.ViewFilterAction#getPreferenceKey()
	 */
	protected String getPreferenceKey() {
		return Constants.SHOW_THIS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(element instanceof JavaScriptVariable){
			JavaScriptVariable var = (JavaScriptVariable) element;
			if(JavaScriptValue.THIS.equals(var.getName())) {
				return getValue();
			}
		}
		return true;
	}
}
