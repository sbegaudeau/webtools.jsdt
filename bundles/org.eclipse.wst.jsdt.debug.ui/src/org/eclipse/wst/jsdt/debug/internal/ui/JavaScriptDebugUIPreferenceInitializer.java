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
package org.eclipse.wst.jsdt.debug.internal.ui;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Initialize common preferences for {@link JavaScriptDebugUIPlugin}
 * 
 * @since 1.1
 */
public class JavaScriptDebugUIPreferenceInitializer extends AbstractPreferenceInitializer {

	/**
	 * Constructor
	 */
	public JavaScriptDebugUIPreferenceInitializer() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = JavaScriptDebugUIPlugin.getDefault().getPreferenceStore();
		store.setDefault(Constants.SHOW_FUNCTIONS, true);
		store.setDefault(Constants.SHOW_PROTOTYPES, true);
		store.setDefault(Constants.SHOW_THIS, true);
	}
}
