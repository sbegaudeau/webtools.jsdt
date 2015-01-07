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
package org.eclipse.wst.jsdt.bower.core.internal.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Utility class used to retrieve the messages for internationalization.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class I18n {
	/**
	 * The path of the resources files.
	 */
	private static final String BUNDLE_NAME = "OSGI-INF/i18n/messages"; //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	private I18n() {
		// prevent instantiation
	}

	/**
	 * Returns the value for the given key in the given locale with the given arguments.
	 *
	 * @param key
	 *            The key of the message
	 * @param arguments
	 *            The arguments of the message
	 * @return The message
	 */
	public static String getString(String key, Object... arguments) {
		try {
			return MessageFormat.format(ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString(
					key), arguments);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
