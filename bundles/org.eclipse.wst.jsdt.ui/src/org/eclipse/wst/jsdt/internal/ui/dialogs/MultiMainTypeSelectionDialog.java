/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.dialogs;


import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchScope;
import org.eclipse.wst.jsdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.wst.jsdt.internal.ui.JavaUIMessages;
import org.eclipse.wst.jsdt.internal.ui.util.ExceptionHandler;
import org.eclipse.wst.jsdt.internal.ui.util.MainMethodSearchEngine;
import org.eclipse.wst.jsdt.ui.JavaScriptElementLabelProvider;

/**
 * A dialog to select a type from a list of types. The dialog allows
 * multiple selections.
 */
public class MultiMainTypeSelectionDialog extends ElementListSelectionDialog {

	private IRunnableContext fRunnableContext;
	private IJavaScriptSearchScope fScope;
	private int fStyle;
		
	/**
	 * Constructor.
	 */
	public MultiMainTypeSelectionDialog(Shell shell, IRunnableContext context,
		IJavaScriptSearchScope scope, int style)
	{
		super(shell, new JavaScriptElementLabelProvider(
			JavaScriptElementLabelProvider.SHOW_PARAMETERS | JavaScriptElementLabelProvider.SHOW_POST_QUALIFIED | JavaScriptElementLabelProvider.SHOW_ROOT));

		setMultipleSelection(true);

		Assert.isNotNull(context);
		Assert.isNotNull(scope);
		
		fRunnableContext= context;
		fScope= scope;
		fStyle= style;
	}

	/*
	 * @see Window#open()
	 */
	public int open() {
		MainMethodSearchEngine engine= new MainMethodSearchEngine();
		IType[] types;
		try {
			types= engine.searchMainMethods(fRunnableContext, fScope, fStyle);
		} catch (InterruptedException e) {
			return CANCEL;
		} catch (InvocationTargetException e) {
			//XX: to do
			ExceptionHandler.handle(e, JavaUIMessages.MultiMainTypeSelectionDialog_errorTitle, e.getMessage()); 
			return CANCEL;
		}
		
		setElements(types);
		return super.open();
	}
	
	/*
	 * @see org.eclipse.jface.window.Window#configureShell(Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(newShell, IJavaHelpContextIds.MULTI_MAIN_TYPE_SELECTION_DIALOG);		
	}


}
