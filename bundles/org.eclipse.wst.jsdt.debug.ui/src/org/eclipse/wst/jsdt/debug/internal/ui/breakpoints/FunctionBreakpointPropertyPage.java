/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptFunctionBreakpoint;
import org.eclipse.wst.jsdt.debug.internal.ui.SWTFactory;


/**
 * Property page for function breakpoints
 * 
 * @since 0.9
 */
public class FunctionBreakpointPropertyPage extends JavaScriptBreakpointPropertyPage {

	private Button entry = null, exit = null; 

	/* (non-Javadoc)
	 * @see org.eclipse.e4.languages.internal.javascript.debug.ui.breakpoints.BreakpointPropertyPage#createTypeSpecificEditors(org.eclipse.swt.widgets.Composite)
	 */
	protected void createTypeSpecificEditors(Composite parent) throws CoreException {
		setTitle(Messages.function_bp);
		this.entry = SWTFactory.createCheckButton(parent, 
				Messages.suspend_when_entering, 
				null, 
				getFunctionBreakpoint().isEntry(), 
				1);
		this.exit = SWTFactory.createCheckButton(parent, 
				Messages.suspend_when_exiting, 
				null, 
				getFunctionBreakpoint().isExit(), 
				1);
	}

	/**
	 * @return the {@link IJavaScriptFunctionBreakpoint} this page is opened on
	 */
	IJavaScriptFunctionBreakpoint getFunctionBreakpoint() {
		return (IJavaScriptFunctionBreakpoint) getBreakpoint();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.e4.languages.internal.javascript.debug.ui.breakpoints.BreakpointPropertyPage#doStore()
	 */
	protected void doStore() throws CoreException {
		IJavaScriptFunctionBreakpoint breakpoint = getFunctionBreakpoint();
		breakpoint.setEntry(this.entry.getSelection());
		breakpoint.setExit(this.exit.getSelection());
		super.doStore();
	}
}
