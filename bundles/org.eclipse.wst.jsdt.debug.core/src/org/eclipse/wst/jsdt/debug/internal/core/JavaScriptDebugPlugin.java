/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.core;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.jsdt.debug.internal.core.launching.ConnectorsManager;
import org.eclipse.wst.jsdt.debug.internal.core.model.BreakpointParticipantManager;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 * @since 1.0
 */
public class JavaScriptDebugPlugin extends Plugin {

	/**
	 * Status code indicating an unexpected internal error.
	 */
	public static final int INTERNAL_ERROR = 120;
	/**
	 * The ID of the bundle<br>
	 * <br>
	 * value is: <code>org.eclipse.wst.jsdt.debug.core</code>
	 */
	public static final String PLUGIN_ID = "org.eclipse.wst.jsdt.debug.core"; //$NON-NLS-1$

	/**
	 * The singleton instance
	 */
	private static JavaScriptDebugPlugin plugin;

	/**
	 * The singleton {@link ConnectorsManager}
	 */
	private static ConnectorsManager connectionmanager = null;
	/**
	 * Singleton {@link BreakpointParticipantManager}
	 */
	private static BreakpointParticipantManager participantmanager = null;
	
	private static JavaScriptPreferencesManager prefmanager = null;
	private static Map externalScriptPaths = new HashMap();
	
	/**
	 * Returns the singleton {@link ConnectorsManager} instance
	 * 
	 * @return the {@link ConnectorsManager}
	 */
	public static synchronized ConnectorsManager getConnectionsManager() {
		if (connectionmanager == null) {
			connectionmanager = new ConnectorsManager();
		}
		return connectionmanager;
	}
	
	/**
	 * Returns the singleton {@link BreakpointParticipantManager}
	 * 
	 * @return the {@link BreakpointParticipantManager}
	 */
	public static synchronized BreakpointParticipantManager getParticipantManager() {
		if(participantmanager == null) {
			participantmanager = new BreakpointParticipantManager();
		}
		return participantmanager;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		prefmanager = new JavaScriptPreferencesManager();
		prefmanager.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		try {
			plugin = null;
			if(connectionmanager != null) {
				connectionmanager.dispose();
			}
			if(participantmanager != null) {
				participantmanager.dispose();
			}
			if(prefmanager != null) {
				prefmanager.stop();
			}
		}
		finally {
			super.stop(context);
		}
	}

	/**
	 * Returns the singleton instance
	 *
	 * @return the singleton instance
	 */
	public static JavaScriptDebugPlugin getDefault() {
		return plugin;
	}
	
	/**
	 * Logs the specified status with this plug-in's log.
	 * 
	 * @param status status to log
	 */
	public static void log(IStatus status) {
		if (plugin != null) {
			plugin.getLog().log(status);
		}
	}

	/**
	 * Logs the specified throwable with this plug-in's log.
	 * 
	 * @param t throwable to log 
	 */
	public static void log(Throwable t) {
		log(newErrorStatus("Error logged from JSDT Debug Core: ", t)); //$NON-NLS-1$
	}
	
	/**
	 * Logs an internal error with the specified message.
	 * 
	 * @param message the error message to log
	 */
	public static void logErrorMessage(String message) {
		log(newErrorStatus("Internal message logged from JavaScript Debug Core: " + message, null)); //$NON-NLS-1$	
	}
	
	/**
	 * Returns a new error status for this plug-in with the given message
	 * @param message the message to be included in the status
	 * @param exception the exception to be included in the status or <code>null</code> if none
	 * @return a new error status
	 */
	public static IStatus newErrorStatus(String message, Throwable exception) {
		return new Status(IStatus.ERROR, PLUGIN_ID, INTERNAL_ERROR, message, exception);
	}

	public static synchronized void addExternalScriptPath(String sourceName, String scriptPath) {
		externalScriptPaths.put(sourceName, scriptPath);		
	}
	
	public static synchronized String getExternalScriptPath(String sourceName) {
		return (String) externalScriptPaths.get(sourceName);		
	}
}
