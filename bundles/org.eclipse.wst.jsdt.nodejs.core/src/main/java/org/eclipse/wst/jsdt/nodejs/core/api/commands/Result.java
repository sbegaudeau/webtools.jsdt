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
package org.eclipse.wst.jsdt.nodejs.core.api.commands;

/**
 * The result of the execution of a command.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class Result {
	/**
	 * The exit code (0 == success).
	 */
	private int exitCode;

	/**
	 * The body of the result.
	 */
	private String body;

	/**
	 * The constructor.
	 *
	 * @param exitCode
	 *            The exit code
	 * @param body
	 *            The body
	 */
	public Result(int exitCode, String body) {
		this.exitCode = exitCode;
		this.body = body;
	}

	/**
	 * Returns the body.
	 *
	 * @return The body
	 */
	public String getBody() {
		return this.body;
	}

	/**
	 * Returns the exit code.
	 *
	 * @return The exit code
	 */
	public int getExitCode() {
		return this.exitCode;
	}
}
