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

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * Manager to handle updating preferences and providing 
 * delegation for common preference calls.
 * 
 * @since 1.0
 */
public class PreferencesManager implements IPropertyChangeListener {

	/**
	 * Preference controlling if loaded scripts should be shown in the Debug view
	 */
	public static final String PREF_SHOW_LOADED_SCRIPTS = JavaScriptDebugUIPlugin.PLUGIN_ID + ".show_loaded_scripts"; //$NON-NLS-1$

	private static PreferencesManager fgManager = null;
	
	/**
	 * Constructor
	 */
	private PreferencesManager() {
		/*no instantiation*/
	}
	
	/**
	 * Returns the singleton instance of the manager
	 * @return the manager
	 */
	public static synchronized PreferencesManager getManager() {
		if(fgManager == null) {
			fgManager = new PreferencesManager();
		}
		return fgManager;
	}
	
	/**
	 * If loaded scripts should be shown in the Debug view
	 * @return <code>true</code> if loaded scripts should be shown in the Debug view
	 */
	public boolean showLoadedScripts() {
		return JavaScriptDebugUIPlugin.getDefault().getPreferenceStore().getBoolean(PREF_SHOW_LOADED_SCRIPTS);
	}
	
	/**
	 * Sets if loaded scripts should be shown in the Debug or not
	 * @param show <code>true</code> if loaded scripts should be shown <code>false</code> otherwise
	 */
	public void showLoadedScripts(boolean show) {
		JavaScriptDebugUIPlugin.getDefault().getPreferenceStore().setValue(PREF_SHOW_LOADED_SCRIPTS, show);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
	}
}
