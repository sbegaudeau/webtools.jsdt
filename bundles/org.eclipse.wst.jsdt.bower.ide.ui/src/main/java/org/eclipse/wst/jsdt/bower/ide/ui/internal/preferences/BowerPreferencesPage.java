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
package org.eclipse.wst.jsdt.bower.ide.ui.internal.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.BowerUiPlugin;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.utils.I18n;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.utils.I18nKeys;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By subclassing
 * <samp>FieldEditorPreferencePage</samp>, we can use the field support built into JFace that allows us to
 * create a page that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that belongs to the
 * main plug-in class. That way, preferences can be accessed directly via the preference store.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */

public class BowerPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * The constructor.
	 */
	public BowerPreferencesPage() {
		super(GRID);
		this.setPreferenceStore(BowerUiPlugin.getInstance().getPreferenceStore());
		this.setDescription(I18n.getString(I18nKeys.BOWER_PREFERENCES_DESCRIPTION));
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
		addField(new StringFieldEditor(IPreferenceConstants.BOWER_SERVER_URL, I18n
				.getString(I18nKeys.BOWER_PREFERENCES_SERVER_URL_LABEL), getFieldEditorParent()));
	}

}
