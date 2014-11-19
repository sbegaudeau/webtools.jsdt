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
package org.eclipse.wst.jsdt.nodejs.ide.ui.internal.console;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.wst.jsdt.nodejs.core.api.INodeJsConstants;
import org.eclipse.wst.jsdt.nodejs.core.api.commands.Result;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.NodeJsIdeUiActivator;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.preferences.INodeJsPreferenceConstants;

/**
 * Utility class used to launch npm commands and display the result in the console.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class NpmConsole {
	/**
	 * The current directory.
	 */
	private File directory;

	/**
	 * The constructor.
	 *
	 * @param directory
	 *            The directory in which npm will be launched
	 */
	public NpmConsole(File directory) {
		this.directory = directory;
	}

	/**
	 * Runs the npm command.
	 *
	 * @param commands
	 *            The commands to run
	 * @return The result of the command
	 */
	public Result call(String... commands) {
		IPreferenceStore preferenceStore = NodeJsIdeUiActivator.getInstance().getPreferenceStore();
		String nodeLocation = preferenceStore.getString(INodeJsPreferenceConstants.NODE_LOCATION);
		String npmLocation = preferenceStore.getString(INodeJsPreferenceConstants.NPM_LOCATION);

		String[] commandsToRun = new String[commands.length + 1];
		commandsToRun[0] = npmLocation;
		System.arraycopy(commands, 0, commandsToRun, 1, commands.length);

		MessageConsole console = this.findConsole("npm");
		console.clearConsole();

		MessageConsoleStream newMessageStream = console.newMessageStream();

		ProcessBuilder processBuilder = new ProcessBuilder(commandsToRun);
		processBuilder.directory(this.directory);

		Map<String, String> environment = processBuilder.environment();
		String path = environment.get(INodeJsConstants.PATH);
		String nodeParentPath = new File(nodeLocation).getParent();
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
					newMessageStream.println(line);
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
		} finally {
			try {
				newMessageStream.close();
			} catch (IOException e) {
				NodeJsIdeUiActivator.log(e, true);
			}
		}
		return new Result(1, ""); //$NON-NLS-1$
	}

	/**
	 * Finds a message console in which the messages can be displayed.
	 *
	 * @param name
	 *            The name of the console
	 * @return The message console
	 */
	private MessageConsole findConsole(String name) {
		ConsolePlugin consolePlugin = ConsolePlugin.getDefault();
		IConsoleManager consoleManager = consolePlugin.getConsoleManager();
		IConsole[] existingConsoles = consoleManager.getConsoles();
		for (IConsole console : existingConsoles) {
			if (name.equals(console.getName()) && console instanceof MessageConsole) {
				return (MessageConsole)console;
			}
		}

		// no console found, so create a new one
		ImageDescriptor imageDescriptor = null;
		MessageConsole console = new MessageConsole(name, imageDescriptor);
		consoleManager.addConsoles(new IConsole[] {console });
		return console;
	}
}
