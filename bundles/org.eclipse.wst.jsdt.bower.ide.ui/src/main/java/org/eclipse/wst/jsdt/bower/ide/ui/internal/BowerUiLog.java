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
package org.eclipse.wst.jsdt.bower.ide.ui.internal;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.jsdt.nodejs.core.api.utils.ILogger;

/**
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class BowerUiLog implements ILogger {
	/**
	 * The Eclipse logger.
	 */
	private ILog log;

	/**
	 * The constructor.
	 *
	 * @param log
	 *            The Eclipse logger of the bundle.
	 */
	public BowerUiLog(ILog log) {
		this.log = log;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.wst.jsdt.nodejs.core.api.utils.ILogger#log(java.lang.String, int, java.lang.Exception)
	 */
	@Override
	public void log(String bundle, int severity, Exception exception) {
		IStatus status = new Status(severity, bundle, exception.getMessage(), exception);
		this.log.log(status);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.wst.jsdt.nodejs.core.api.utils.ILogger#log(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void log(String bundle, int severity, String message) {
		IStatus status = new Status(severity, bundle, message);
		this.log.log(status);
	}

}
