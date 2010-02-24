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
package org.eclipse.wst.jsdt.debug.internal.ui.breakpoints;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptLineBreakpoint;
import org.eclipse.wst.jsdt.debug.internal.ui.JavaScriptDebugUIPlugin;
import org.eclipse.wst.jsdt.debug.internal.ui.SWTFactory;


/**
 * Property page for line breakpoints
 * 
 * @since 1.0
 */
public class LineBreakpointPropertyPage extends JavaScriptBreakpointPropertyPage {
	
	/* (non-Javadoc)
	 * @see org.eclipse.e4.languages.internal.javascript.debug.ui.breakpoints.BreakpointPropertyPage#createTypeSpecificLabels(org.eclipse.swt.widgets.Composite)
	 */
	protected void createTypeSpecificLabels(Composite parent) {
		StringBuffer lineNumber = new StringBuffer(4);
		try {
			int lNumber = ((IJavaScriptLineBreakpoint)getBreakpoint()).getLineNumber();
			if (lNumber > 0) {
				lineNumber.append(lNumber);
			}
		} catch (CoreException ce) {
			JavaScriptDebugUIPlugin.log(ce);
		}
		if (lineNumber.length() > 0) {
			SWTFactory.createLabel(parent, Messages.line_number, 1); 
			Text text = SWTFactory.createText(parent, SWT.READ_ONLY | SWT.SINGLE, 1);
			text.setText(lineNumber.toString());
			text.setBackground(parent.getBackground());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.e4.languages.internal.javascript.debug.ui.breakpoints.BreakpointPropertyPage#createTypeSpecificEditors(org.eclipse.swt.widgets.Composite)
	 */
	protected void createTypeSpecificEditors(Composite parent) throws CoreException {
		setTitle(Messages.line_number_bp);
	}
}
