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
package org.eclipse.wst.jsdt.bower.core.api.utils;

/**
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public interface ILog {

	/**
	 * The severity of the message to be logged.
	 *
	 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
	 */
	public enum Severity {
		/**
		 * Info.
		 */
		INFO,
		/**
		 * Warning.
		 */
		WARNING,
		/**
		 * Error.
		 */
		ERROR;
	}

	/**
	 * Logs a message with the given severity for the bundle with the given bundle id.
	 *
	 * @param severity
	 *            The severity
	 * @param bundleId
	 *            The bundle id
	 * @param message
	 *            The message
	 */
	public void log(Severity severity, String bundleId, String message);
}
