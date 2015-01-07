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
package org.eclipse.wst.jsdt.nodejs.core.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This command let you find where a specific executable is.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class Which {
	/**
	 * The command to return the location of SmartEA on an unix machine.
	 */
	private static final String UNIX_COMMAND = "which"; //$NON-NLS-1$

	/**
	 * The command to return the location of SmartEA on a windows machine.
	 */
	private static final String WINDOWS_COMMAND = "where"; //$NON-NLS-1$

	/**
	 * The executable to find.
	 */
	private String executable;

	/**
	 * The constructor.
	 *
	 * @param executable
	 *            The executable to find
	 */
	public Which(String executable) {
		this.executable = executable;
	}

	/**
	 * Returns the location of node.
	 *
	 * @return The location of SmartEA if successful, <code>null</code> otherwise.
	 */
	public String call() {
		String command = UNIX_COMMAND;
		String operatingSystem = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$
		if (operatingSystem.indexOf("win") >= 0) { //$NON-NLS-1$
			command = WINDOWS_COMMAND;
		}

		ProcessBuilder processBuilder = new ProcessBuilder(command, this.executable);
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

			int exitValue = process.waitFor();
			if (exitValue == 0) {
				return builder.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
