/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.rhino.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.wst.jsdt.debug.internal.rhino.RhinoDebugPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class RhinoUIPlugin extends AbstractUIPlugin {

	/**
	 * Status code indicating an unexpected internal error.
	 */
	public static final int INTERNAL_ERROR = 120;
	
	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.wst.jsdt.debug.rhino.ui"; //$NON-NLS-1$

	// The shared instance
	private static RhinoUIPlugin plugin;
	private static IPreferenceStore corestore = new ScopedPreferenceStore(InstanceScope.INSTANCE, RhinoDebugPlugin.PLUGIN_ID);
	
	/**
	 * The constructor
	 */
	public RhinoUIPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static RhinoUIPlugin getDefault() {
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
		log(newErrorStatus("Error logged from Rhino Debug: ", t)); //$NON-NLS-1$
	}
	
	/**
	 * Logs an internal error with the specified message.
	 * 
	 * @param message the error message to log
	 */
	public static void logErrorMessage(String message) {
		log(newErrorStatus("Internal message logged from Rhino Debug: " + message, null)); //$NON-NLS-1$	
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
	 * Returns the active workbench window or <code>null</code> if there is no active window
	 * 
	 * @see IWorkbench
	 * @see IWorkbenchWindow
	 * @return the active workbench window or <code>null</code>
	 */
	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}	
	
	/**
	 * Returns the active workbench page or <code>null</code> if it cannot be resolved
	 * 
	 * @see IWorkbench
	 * @see IWorkbenchWindow
	 * @see IWorkbenchPage
	 * @return the active workbench page or <code>null</code>
	 */
	public static IWorkbenchPage getActivePage() {
		IWorkbenchWindow w = getActiveWorkbenchWindow();
		if (w != null) {
			return w.getActivePage();
		}
		return null;
	}
	
	/**
	 * Returns the {@link IPreferenceStore} wrapper for the {@link RhinoDebugPlugin} preferences
	 * 
	 * @return the {@link IPreferenceStore} for the Rhino core preferences
	 */
	public static IPreferenceStore getCorePreferenceStore() {
		return corestore;
	}
}
