/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.chrome;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.jsdt.debug.internal.chrome.event.EventQueueImpl;
import org.eclipse.wst.jsdt.debug.internal.chrome.jsdi.MirrorImpl;
import org.eclipse.wst.jsdt.debug.internal.chrome.transport.PacketImpl;
import org.eclipse.wst.jsdt.debug.internal.chrome.transport.JSON;
import org.osgi.framework.BundleContext;

public class ChromePlugin extends Plugin {

	/**
	 * Id of the bundle
	 */
	public static final String PLUGIN_ID = "org.eclipse.wst.jsdt.debug.chrome"; //$NON-NLS-1$
	/**
	 * PacketImpl tracing option name
	 */
	public static final String TRC_PACKETS = PLUGIN_ID + "/packets"; //$NON-NLS-1$
	/**
	 * ChromeEvent queue tracing option name
	 */
	public static final String TRC_EVENTQUEUE = PLUGIN_ID + "/eventqueue"; //$NON-NLS-1$
	/**
	 * JSDI implementation tracing option name
	 */
	public static final String TRC_JSDI = PLUGIN_ID + "/jsdi"; //$NON-NLS-1$
	/**
	 * JSON parser tracing option
	 */
	public static final String TRC_JSON = PLUGIN_ID + "/json"; //$NON-NLS-1$
	/**
	 * Status code indicating an unexpected internal error.
	 */
	public static final int INTERNAL_ERROR = 120;
	
	/**
	 * Singleton instance
	 */
	private static ChromePlugin plugin = null;
	
	/**
	 * @return the singleton plugin instance
	 */
	public static ChromePlugin getDefault() {
		return plugin;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		plugin = this;
		configureTracing();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		try {
			plugin = null;
		}
		finally {
			super.stop(bundleContext);
		}
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
		log(newErrorStatus("Error logged from Google Chrome Debug: ", t)); //$NON-NLS-1$
	}
	
	/**
	 * Logs an internal error with the specified message.
	 * 
	 * @param message the error message to log
	 */
	public static void logErrorMessage(String message) {
		log(newErrorStatus("Internal message logged from Google Chrome Debug: " + message, null)); //$NON-NLS-1$	
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

	/**
	 * Turns on / off any tracing options
	 */
	public void configureTracing() {
		if(ChromePlugin.getDefault().isDebugging()) {
			String option = Platform.getDebugOption(TRC_PACKETS);
			if(option != null) {
				PacketImpl.setTracing(Boolean.valueOf(option).booleanValue());
			}
			option = Platform.getDebugOption(TRC_EVENTQUEUE);
			if(option != null) {
				EventQueueImpl.setTracing(Boolean.valueOf(option).booleanValue());
			}
			option = Platform.getDebugOption(TRC_JSDI);
			if(option != null) {
				MirrorImpl.setTracing(Boolean.valueOf(option).booleanValue());
			}
			option = Platform.getDebugOption(TRC_JSON);
			if(option != null) {
				JSON.setTracing(Boolean.valueOf(option).booleanValue());
			}
		}
	}
}
