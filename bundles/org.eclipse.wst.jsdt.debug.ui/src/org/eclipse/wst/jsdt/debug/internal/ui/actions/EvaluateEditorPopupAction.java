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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.debug.ui.InspectPopupDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptValue;
import org.eclipse.wst.jsdt.debug.internal.ui.eval.JavaScriptInspectExpression;


/**
 * Default handler for the {@link EvaluateAction}
 * 
 * @since 1.0
 */
public class EvaluateEditorPopupAction extends EvaluateAction {

	/**
	 * Constructor
	 */
	public EvaluateEditorPopupAction() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		run(null);
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.ui.actions.EvaluateAction#showResult(org.eclipse.wst.jsdt.debug.core.model.IJavaScriptValue)
	 */
	protected void showResult(IJavaScriptValue value) {
		InspectPopupDialog dialog = new InspectPopupDialog(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
				getPopupAnchor(getStyledText(getTargetPart())), 
				null, 
				new JavaScriptInspectExpression(getValue()));
		dialog.open();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#isEnabled()
	 */
	public boolean isEnabled() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		setWindow(window);
		if(window != null) {
			IWorkbenchPage page = window.getActivePage();
			if(page != null) {
				setTargetpart(page.getActivePart());
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#isHandled()
	 */
	public boolean isHandled() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#removeHandlerListener(org.eclipse.core.commands.IHandlerListener)
	 */
	public void removeHandlerListener(IHandlerListener handlerListener) {}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#addHandlerListener(org.eclipse.core.commands.IHandlerListener)
	 */
	public void addHandlerListener(IHandlerListener handlerListener) {}
}
