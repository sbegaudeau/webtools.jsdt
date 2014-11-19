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
package org.eclipse.wst.jsdt.webapp.core.api.commands;

import java.io.IOException;

/**
 * This command will be used to run Gulp tasks.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class Gulp {
	/**
	 * The name of the gulp executable.
	 */
	private static final String GULP = "gulp"; //$NON-NLS-1$

	/**
	 * The location of gulp.
	 */
	private String location;

	/**
	 * The constructor.
	 */
	public Gulp() {
		this.location = GULP;
	}

	/**
	 * The constructor.
	 *
	 * @param location
	 *            The location of gulp on the computer.
	 */
	public Gulp(String location) {
		this.location = location;
	}

	/**
	 * Runs the gulp command.
	 *
	 * @param commands
	 *            The commands to run
	 * @return The exit status of the command
	 */
	public int call(String... commands) {
		String[] commandsToRun = new String[commands.length + 1];
		commandsToRun[0] = this.location;
		System.arraycopy(commands, 0, commandsToRun, 1, commands.length);

		ProcessBuilder processBuilder = new ProcessBuilder(commandsToRun);
		processBuilder.redirectErrorStream(true);
		try {
			Process process = processBuilder.start();
			// BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

			if (process.isAlive()) {
				process.destroy();
			}

			int exitValue = process.exitValue();
			return exitValue;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 1;
	}
}
