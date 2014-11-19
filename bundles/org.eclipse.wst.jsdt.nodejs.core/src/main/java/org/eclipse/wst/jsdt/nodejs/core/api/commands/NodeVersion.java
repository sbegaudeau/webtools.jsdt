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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.wst.jsdt.nodejs.core.api.INodeJsConstants;

/**
 * This command will return the version of Node.js installed.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class NodeVersion {
	/**
	 * The parameter of the command.
	 */
	private static final String PARAMETER = "--version"; //$NON-NLS-1$

	/**
	 * The location of node.
	 */
	private String location;

	/**
	 * The constructor.
	 */
	public NodeVersion() {
		this.location = INodeJsConstants.NODE;
	}

	/**
	 * The constructor.
	 *
	 * @param location
	 *            The location of Node.js on the computer.
	 */
	public NodeVersion(String location) {
		this.location = location;
	}

	/**
	 * Returns the version of node installed.
	 *
	 * @return The version of node installed if found, <code>null</code> otherwise.
	 */
	public String call() {
		String version = null;
		ProcessBuilder processBuilder = new ProcessBuilder(location, PARAMETER);
		processBuilder.redirectErrorStream(true);
		try {
			Process process = processBuilder.start();
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

			StringBuilder builder = new StringBuilder();
			String line = ""; //$NON-NLS-1$
			while (line != null) {
				line = input.readLine();
				if (line != null) {
					builder.append(line);
				}
			}

			if (process.isAlive()) {
				process.destroy();
			}

			int exitValue = process.exitValue();
			if (exitValue == 0) {
				version = builder.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return version;
	}
}
