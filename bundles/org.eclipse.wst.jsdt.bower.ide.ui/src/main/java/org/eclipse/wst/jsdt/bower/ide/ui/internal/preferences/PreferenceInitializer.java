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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.wst.jsdt.bower.core.api.utils.IBowerConstants;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.BowerUiPlugin;

/**
 * Class used to initialize default preference values.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = BowerUiPlugin.getInstance().getPreferenceStore();
		store.setDefault(IPreferenceConstants.BOWER_SERVER_URL, IBowerConstants.DEFAULT_BOWER_SERVER_URL);
	}

}
