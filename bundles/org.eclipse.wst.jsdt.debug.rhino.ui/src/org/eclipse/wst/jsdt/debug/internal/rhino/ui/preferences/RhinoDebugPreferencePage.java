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
package org.eclipse.wst.jsdt.debug.internal.rhino.ui.preferences;

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
import org.eclipse.wst.jsdt.debug.internal.rhino.Constants;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.IHelpConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.RhinoUIPlugin;
import org.eclipse.wst.jsdt.debug.internal.ui.SWTFactory;

/**
 * Default preference page for Rhino preferences
 * 
 * @since 1.0
 */
public class RhinoDebugPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String PAGE_ID = "org.eclipse.wst.jsdt.debug.rhino.ui.debug.pref.page"; //$NON-NLS-1$
	
	/**
	 * Constructor
	 */
	public RhinoDebugPreferencePage() {
		super(GRID);
		setDescription(Messages.rhino_pref_page_desc);
		setPreferenceStore(RhinoUIPlugin.getCorePreferenceStore());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IHelpConstants.RHINO_PREFERENCE_PAGE);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite comp = SWTFactory.createComposite(parent, parent.getFont(), 1, 1, GridData.FILL_HORIZONTAL, 0, 0);
		Group group = SWTFactory.createGroup(comp, Messages.suspend_execution, 1, 1, GridData.FILL_HORIZONTAL);
		BooleanFieldEditor editor = new BooleanFieldEditor(Constants.SUSPEND_ON_STDIN_LOAD, Messages.suspend_for_stdin, group);
		initEditor(editor, RhinoUIPlugin.getCorePreferenceStore());
		//reset the group layout after we add the editors, since they toast the padding
		GridLayout lo = (GridLayout) group.getLayout();
		lo.marginWidth = 5;
		lo.marginHeight = 5;
		return comp;
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
}
