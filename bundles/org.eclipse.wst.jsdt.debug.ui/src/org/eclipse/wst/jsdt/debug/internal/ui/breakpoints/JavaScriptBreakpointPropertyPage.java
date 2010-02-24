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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpoint;
import org.eclipse.wst.jsdt.debug.internal.core.launching.Constants;
import org.eclipse.wst.jsdt.debug.internal.ui.IHelpContextIds;
import org.eclipse.wst.jsdt.debug.internal.ui.JavaScriptDebugUIPlugin;
import org.eclipse.wst.jsdt.debug.internal.ui.SWTFactory;

/**
 * Abstract class for breakpoint property pages
 * 
 * @since 1.0
 */
public class JavaScriptBreakpointPropertyPage extends PropertyPage {

	protected Button enabledbutton;
	protected Button hitcountbutton;
	protected Text hitcounttext;
	protected Combo suspendpolicycombo;
	protected List errors = new ArrayList();
	
	/**
	 * @return the underlying {@link IJavaScriptBreakpoint} element this page was opened on
	 */
	protected IJavaScriptBreakpoint getBreakpoint() {
		return (IJavaScriptBreakpoint) getElement();
	}
	
	/**
	 * Allows subclasses to add type specific labels to the common Java
	 * breakpoint page.
	 * @param parent
	 */
	protected void createTypeSpecificLabels(Composite parent) {}
	
	/**
	 * Allows subclasses to add type specific editors to the common Java
	 * breakpoint page.
	 * @param parent
	 * @throws CoreException
	 */
	protected void createTypeSpecificEditors(Composite parent) throws CoreException {}
	
	/**
	 * Stores the values configured in this page. This method
	 * should be called from within a workspace runnable to
	 * reduce the number of resource deltas.
	 */
	protected void doStore() throws CoreException {
		IJavaScriptBreakpoint breakpoint = getBreakpoint();
		//store hit count
		int hitCount = -1;
		if (hitcountbutton.getSelection()) {
			try {
				hitCount = Integer.parseInt(hitcounttext.getText());
			} 
			catch (NumberFormatException e) {
				JavaScriptDebugUIPlugin.log(e);
			}
			breakpoint.setHitCount(hitCount);
		}
		else {
			breakpoint.setHitCount(0);
		}
		breakpoint.setEnabled(enabledbutton.getSelection());
		breakpoint.setSuspendPolicy(suspendpolicycombo.getSelectionIndex()+1);
	}
	
	/**
	 * Creates the button to toggle enablement of the breakpoint
	 * @param parent
	 * @throws CoreException
	 */
	protected void createEnabledButton(Composite parent) throws CoreException {
		enabledbutton = SWTFactory.createCheckButton(parent, Messages.enabled, null, false, 1); 
		enabledbutton.setSelection(getBreakpoint().isEnabled());
	}
	
	/**
	 * Creates a default hit count editor
	 * @param parent 
	 * @throws CoreException
	 */
	protected void createHitCountEditor(Composite parent) throws CoreException {
		Composite hitCountComposite = SWTFactory.createComposite(parent, parent.getFont(), 2, 1, GridData.FILL_HORIZONTAL, 0, 0);
		hitcountbutton = SWTFactory.createCheckButton(hitCountComposite, Messages.hit_count, null, false, 1); 
		hitcountbutton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				hitcounttext.setEnabled(hitcountbutton.getSelection());
				hitCountChanged();
			}
		});
		int hitCount = getBreakpoint().getHitCount();
		String hitCountString = Constants.EMPTY_STRING;
		if (hitCount > 0) {
			hitCountString = new Integer(hitCount).toString();
			hitcountbutton.setSelection(true);
		} else {
			hitcountbutton.setSelection(false);
		}
		hitcounttext = SWTFactory.createSingleText(hitCountComposite, 1);
		hitcounttext.setText(hitCountString);
		if (hitCount <= 0) {
			hitcounttext.setEnabled(false);
		}
		hitcounttext.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				hitCountChanged();
			}
		});
	}
	
	/**
	 * Validates the current state of the hit count editor.
	 * Hit count value must be a positive integer.
	 */
	void hitCountChanged() {
		if (!hitcountbutton.getSelection()) {
			removeErrorMessage(Messages.hit_count_must_be_positive);
			return;
		}
		String hitCountText= hitcounttext.getText();
		int hitCount= -1;
		try {
			hitCount = Integer.parseInt(hitCountText);
		} 
		catch (NumberFormatException e1) {
			addErrorMessage(Messages.hit_count_must_be_positive);
			return;
		}
		if (hitCount < 1) {
			addErrorMessage(Messages.hit_count_must_be_positive);
		} else {
			removeErrorMessage(Messages.hit_count_must_be_positive);
		}
	}

	/**
	 * Creates the editor for configuring the suspend policy (suspend target or suspend thread) of the breakpoint.
	 * @param parent 
	 */
	protected void createSuspendPolicyEditor(Composite parent) throws CoreException {
		Composite comp = SWTFactory.createComposite(parent, 2, 1, GridData.FILL_HORIZONTAL);
		SWTFactory.createLabel(comp, Messages.suspend_policy, 1); 
		boolean suspendThread = getBreakpoint().getSuspendPolicy() == IJavaScriptBreakpoint.SUSPEND_THREAD;
		suspendpolicycombo = new Combo(comp, SWT.BORDER | SWT.READ_ONLY);
		suspendpolicycombo.add(Messages.suspend_thread_option);
		suspendpolicycombo.add(Messages.suspend_target);
		suspendpolicycombo.select(1);
		if(suspendThread) {
			suspendpolicycombo.select(0);
		}
	}
	
	/**
	 * Creates the labels displayed for the breakpoint.
	 * @param parent
	 */
	protected void createLabels(Composite parent) {
		Composite labelComposite = SWTFactory.createComposite(parent, 2, 1, GridData.FILL_HORIZONTAL);
		try {
			String name = getBreakpoint().getTypeName();
			if (name != null) {
				SWTFactory.createLabel(labelComposite, Messages.type_name, 1); 
				Text text = SWTFactory.createText(labelComposite, SWT.READ_ONLY | SWT.SINGLE, 1, GridData.FILL_HORIZONTAL);
				text.setText(name);
				text.setBackground(parent.getBackground());
			}
			name = getBreakpoint().getScriptPath();
			if(name != null) {
				SWTFactory.createLabel(labelComposite, Messages.script_path, 1);
				Text text = SWTFactory.createText(labelComposite, SWT.READ_ONLY, 1, GridData.FILL_HORIZONTAL);
				text.setText(name);
				text.setBackground(parent.getBackground());
			}
			createTypeSpecificLabels(labelComposite);
		} catch (CoreException ce) {
			JavaScriptDebugUIPlugin.log(ce);
		}
	}
	
	/**
	 * Adds the given error message to the errors currently displayed on this page.
	 * The page displays the most recently added error message.
	 * Clients should retain messages that are passed into this method as the
	 * message should later be passed into removeErrorMessage(String) to clear the error.
	 * This method should be used instead of setErrorMessage(String).
	 * @param message the error message to display on this page.
	 */
	protected void addErrorMessage(String message) {
		errors.remove(message);
		errors.add(message);
		setErrorMessage(message);
		setValid(message == null);
	}
	
	/**
	 * Removes the given error message from the errors currently displayed on this page.
	 * When an error message is removed, the page displays the error that was added
	 * before the given message. This is akin to popping the message from a stack.
	 * Clients should call this method instead of setErrorMessage(null).
	 * @param message the error message to clear
	 */
	protected void removeErrorMessage(String message) {
		errors.remove(message);
		if (errors.isEmpty()) {
			addErrorMessage(null);
		} else {
			addErrorMessage((String) errors.get(errors.size() - 1));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		setTitle(Messages.script_load_bp);
		noDefaultAndApplyButton();
		Composite comp = SWTFactory.createComposite(parent, 1, 1, GridData.FILL_BOTH);
		createLabels(comp);
		try {
			createEnabledButton(comp);
			createHitCountEditor(comp);
			createTypeSpecificEditors(comp);
			createSuspendPolicyEditor(comp); // Suspend policy is considered uncommon. Add it last.
		} 
		catch (CoreException e) {
			JavaScriptDebugUIPlugin.log(e);
		}
		setValid(true);
		setControl(parent);
		return comp;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	public boolean performOk() {
		IWorkspaceRunnable wr = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				doStore();
			}
		};
		try {
			ResourcesPlugin.getWorkspace().run(wr, null, IWorkspace.AVOID_UPDATE, null);
		} 
		catch (CoreException e) {
			JavaScriptDebugUIPlugin.log(e);
		}
		return super.performOk();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IHelpContextIds.JAVASCRIPT_BREAKPOINT_PROPERTY_PAGE);
	}
}
