/*******************************************************************************
 * Copyright (c) 2010, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Peter Rybin - Bug 389133 - Allow enablement and properties ruler actions for third-party breakpoints in JavaScript Editor
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.ui.breakpoints;

import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.ui.actions.RulerBreakpointAction;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.IUpdate;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpoint;

/**
 * Ruler action for the Breakpoint Properties action
 * 
 * @since 1.0
 */
public class BreakpointPropertiesRulerAction extends RulerBreakpointAction implements IUpdate {

	private IBreakpoint breakpoint = null;
	
	/**
	 * Constructor
	 * @param editor
	 * @param info
	 */
	public BreakpointPropertiesRulerAction(ITextEditor editor, IVerticalRulerInfo info) {
		super(editor, info);
		setText(Messages.breakpoint_properties);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		if(this.breakpoint != null) {
			String pageId = null;
			if (this.breakpoint instanceof IJavaScriptBreakpoint) {
				pageId = JavaScriptBreakpointPropertyPage.PAGE_ID;
			}
			PreferencesUtil.createPropertyDialogOn(
					getEditor().getSite().getShell(), 
					this.breakpoint, 
					pageId, 
					null, 
					null, 
					0).open();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.IUpdate#update()
	 */
	public void update() {
		this.breakpoint = getBreakpoint();
		setEnabled(this.breakpoint != null);
	}

}
