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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.eclipse.wst.jsdt.nodejs.core.api.INodeJsConstants;

/**
 * This command will be used to run npm commands.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class Npm {

	/**
	 * The location of npm.
	 */
	private String npmLocation;

	/**
	 * The current directory.
	 */
	private File directory;

	/**
	 * The location of node.
	 */
	private String nodeLocation;

	/**
	 * The constructor.
	 *
	 * @param npmLocation
	 *            The location of npm on the computer.
	 * @param directory
	 *            The directory in which npm will be launched
	 * @param nodeLocation
	 *            The location of the node executable
	 */
	public Npm(String npmLocation, File directory, String nodeLocation) {
		this.npmLocation = npmLocation;
		this.directory = directory;
		this.nodeLocation = nodeLocation;
	}

	/**
	 * Runs the npm command.
	 *
	 * @param commands
	 *            The commands to run
	 * @return The result of the command
	 */
	public Result call(String... commands) {
		String[] commandsToRun = new String[commands.length + 1];
		commandsToRun[0] = this.npmLocation;
		System.arraycopy(commands, 0, commandsToRun, 1, commands.length);

		ProcessBuilder processBuilder = new ProcessBuilder(commandsToRun);
		processBuilder.directory(this.directory);

		Map<String, String> environment = processBuilder.environment();
		String path = environment.get(INodeJsConstants.PATH);
		String nodeParentPath = new File(this.nodeLocation).getParent();
		if (!path.contains(nodeParentPath)) {
			path = path + INodeJsConstants.PATH_SEPARATOR + nodeParentPath;
			environment.put(INodeJsConstants.PATH, path);
		}

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
					builder.append(System.getProperty("line.separator")); //$NON-NLS-1$
				}
			}

			try {
				process.waitFor(5, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			int exitValue = process.exitValue();
			return new Result(exitValue, builder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Result(1, ""); //$NON-NLS-1$
	}
}
