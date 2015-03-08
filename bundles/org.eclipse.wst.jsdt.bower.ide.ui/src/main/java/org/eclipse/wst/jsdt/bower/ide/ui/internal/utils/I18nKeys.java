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
package org.eclipse.wst.jsdt.bower.ide.ui.internal.utils;

/**
 * Utility interface holding the keys used by the internationalization.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public interface I18nKeys {
	/**
	 * Bower-related preferences.
	 */
	String BOWER_PREFERENCES_DESCRIPTION = "Bower-Preferences-Description"; //$NON-NLS-1$

	/**
	 * Bower server URL:.
	 */
	String BOWER_PREFERENCES_SERVER_URL_LABEL = "Bower-Preferences-Server-Url-Label"; //$NON-NLS-1$

	/**
	 * bower.json.
	 */
	String BOWER_EDITOR_TITLE = "Bower-Editor-Title"; //$NON-NLS-1$

	/**
	 * The name of the job "Bower Install".
	 */
	String BOWER_INSTALL_JOB_NAME = "BowerInstallJobName"; //$NON-NLS-1$

	/**
	 * The name of the job "Bower Update".
	 */
	String BOWER_UPDATE_JOB_NAME = "BowerUpdateJobName"; //$NON-NLS-1$

	/**
	 * The name of the job "Bower Clear".
	 */
	String BOWER_CLEAR_JOB_NAME = "BowerClearJobName"; //$NON-NLS-1$
}
