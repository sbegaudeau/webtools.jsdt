/*******************************************************************************
 * Copyright (c) 2014 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.nodejs.ide.ui.internal.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.NodeJsIdeUiActivator;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.utils.I18n;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.utils.I18nKeys;

/**
 * The preference page.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class NodeJsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * The constructor.
	 */
	public NodeJsPreferencePage() {
		super(GRID);
		this.setDescription(I18n.getString(I18nKeys.PREFERENCE_PAGE_DESCRIPTION));
		this.setPreferenceStore(NodeJsIdeUiActivator.getInstance().getPreferenceStore());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	@Override
	public void createFieldEditors() {
		this.addField(new FileFieldEditor(INodeJsPreferenceConstants.NODE_LOCATION, I18n
				.getString(I18nKeys.PREFERENCES_PAGE_NODE_LOCATION), getFieldEditorParent()));
		this.addField(new FileFieldEditor(INodeJsPreferenceConstants.NPM_LOCATION, I18n
				.getString(I18nKeys.PREFERENCES_PAGE_NPM_LOCATION), getFieldEditorParent()));
	}
}
