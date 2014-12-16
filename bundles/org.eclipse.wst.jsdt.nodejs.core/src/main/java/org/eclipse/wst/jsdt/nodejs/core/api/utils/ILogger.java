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
package org.eclipse.wst.jsdt.nodejs.core.api.utils;

/**
 * This interface will be used to log errors without requiring a dependency to org.eclipse.core.runtime. The
 * error code and their value are the same as in IStatus in order to simplify the integration with Eclipse log
 * system.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public interface ILogger {
	/**
	 * OK.
	 */
	int OK = 0;

	/**
	 * INFO.
	 */
	int INFO = 1;

	/**
	 * WARNING.
	 */
	int WARNING = 2;

	/**
	 * ERROR.
	 */
	int ERROR = 4;

	/**
	 * CANCEL.
	 */
	int CANCEL = 8;

	/**
	 * Logs the given exception.
	 *
	 * @param bundle
	 *            The name of the bundle
	 * @param severity
	 *            The severity
	 * @param exception
	 *            The exception to log
	 */
	void log(String bundle, int severity, Exception exception);

	/**
	 * Logs the given message.
	 * 
	 * @param bundle
	 *            The identifier of the bundle
	 * @param severity
	 *            The severity
	 * @param message
	 *            The message to log
	 */
	void log(String bundle, int severity, String message);
}
