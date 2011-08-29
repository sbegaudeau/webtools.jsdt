/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.rhino.ui;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;

/**
 * Property tester for launching, etc
 * <br><br>
 * Provides the following tests:
 * <ol>
 * <li><code>isContainer</code> - if the selected item is a container</li>
 * <li><code>isScript</code> - if the selected element is a script</li>
 * </ol>
 * @since 1.0
 */
public class RhinoPropertyTester extends PropertyTester {

	static final String IS_SCRIPT = "isScript"; //$NON-NLS-1$
	
	/**
	 * Constructor
	 */
	public RhinoPropertyTester() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object, java.lang.String, java.lang.Object[], java.lang.Object)
	 */
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if(receiver instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) receiver;
			if(IS_SCRIPT.equals(property)) {
				return isScript(adaptable);
			}
		}
		return false;
	}
	
	/**
	 * Returns if the element is a container or not
	 * @param element
	 * @return <code>true</code> if the element is a script <code>false</code> otherwise
	 */
	boolean isScript(IAdaptable element) {
		IJavaScriptElement jselement = (IJavaScriptElement) element.getAdapter(IJavaScriptElement.class);
		if(jselement != null) {
			int type = jselement.getElementType();
			return type == IJavaScriptElement.JAVASCRIPT_UNIT;
		}
		IFile file = (IFile) element.getAdapter(IFile.class);
		if(file != null) {
			return "js".equals(file.getFileExtension()); //$NON-NLS-1$
		}
		return false;
	}
}
