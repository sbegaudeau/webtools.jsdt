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
package org.eclipse.wst.jsdt.debug.internal.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.debug.internal.core.Constants;
import org.eclipse.wst.jsdt.debug.internal.ui.IHelpContextIds;
import org.eclipse.wst.jsdt.debug.internal.ui.JavaScriptDebugUIPlugin;
import org.eclipse.wst.jsdt.debug.internal.ui.SWTFactory;

/**
 * Default preference page for JavaScript debugging
 * @since 1.1
 */
public class JavaScriptDebugPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String PAGE_ID = "org.eclipse.wst.jsdt.debug.ui.debug.page"; //$NON-NLS-1$
	
	/**
	 * Constructor
	 */
	public JavaScriptDebugPreferencePage() {
		super(GRID);
		setPreferenceStore(JavaScriptDebugUIPlugin.getCorePreferenceStore());
		setDescription(Messages.js_debug_pref_page_desc);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IHelpContextIds.DEBUG_PREFERENCE_PAGE);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite comp = SWTFactory.createComposite(parent, parent.getFont(), 1, 1, GridData.FILL_HORIZONTAL, 0, 0);
		//General
		Group group = SWTFactory.createGroup(comp, Messages.general, 1, 1, GridData.FILL_HORIZONTAL);
		BooleanFieldEditor editor = new BooleanFieldEditor(Constants.DELETE_EXT_PROJECT_ON_EXIT, Messages.delete_ext_project_on_exit, group);
		initEditor(editor, JavaScriptDebugUIPlugin.getCorePreferenceStore());
		initGroup(group);
		//Suspend Execution
		group = SWTFactory.createGroup(comp, Messages.suspend_execution, 1, 1, GridData.FILL_HORIZONTAL);
		editor = new BooleanFieldEditor(Constants.SUSPEND_ON_ALL_SCRIPT_LOADS, Messages.suspend_for_all_script_loads, group);
		initEditor(editor, JavaScriptDebugUIPlugin.getCorePreferenceStore());
		editor = new BooleanFieldEditor(Constants.SUSPEND_ON_THROWN_EXCEPTION, Messages.suspend_on_exceptions, group);
		initEditor(editor, JavaScriptDebugUIPlugin.getCorePreferenceStore());
		initGroup(group);
		return comp;
	}

	/**
	 * Refreshes the specified group to re-set the default spacings that have been 
	 * trashed by the field editors
	 * 
	 * @param group
	 */
	void initGroup(Group group) {
		GridData gd = (GridData) group.getLayoutData();
		gd.grabExcessHorizontalSpace = true;
		GridLayout lo = (GridLayout) group.getLayout();
		lo.marginWidth = 5;
		lo.marginHeight = 5;
	}
	
	/**
	 * Initializes and sets up the given editor
	 * 
	 * @param editor
	 * @param store
	 */
	void initEditor(FieldEditor editor, IPreferenceStore store) {
		addField(editor);
		editor.setPage(this);
		editor.setPropertyChangeListener(this);
		editor.setPreferenceStore(store);
		editor.load();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {}
}
