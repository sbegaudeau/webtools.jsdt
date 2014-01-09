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
package org.eclipse.wst.jsdt.debug.internal.core;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpoint;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptLoadBreakpoint;
import org.eclipse.wst.jsdt.debug.core.model.JavaScriptDebugModel;
import org.eclipse.wst.jsdt.debug.internal.core.breakpoints.JavaScriptExceptionBreakpoint;
import org.eclipse.wst.jsdt.debug.internal.core.breakpoints.JavaScriptLoadBreakpoint;
import org.eclipse.wst.jsdt.debug.internal.core.model.JavaScriptDebugTarget;

/**
 * Manager to handle various preferences.<br>
 * <br>
 * Specifically this manager handles the preferences for:
 * <ul>
 * <li>Suspend on all script loads</li>
 * </ul>
 * 
 * @since 1.0
 */
public class JavaScriptPreferencesManager implements IPreferenceChangeListener {

	/**
	 * The invisible "suspend on all script loads" breakpoint
	 */
	private static IJavaScriptLoadBreakpoint allLoadsBreakpoint = null;
	
	/**
	 * The invisible "suspend on exception" breakpoint
	 * @since 1.1
	 */
	private static JavaScriptExceptionBreakpoint allExceptions = null;
	
	class StartJob extends Job {
		private boolean loads = false;
		private boolean exceptions = false;
		/**
		 * Constructor
		 */
		public StartJob(boolean loads, boolean exceptions) {
			super(Constants.EMPTY_STRING);
			this.loads = loads;
			this.exceptions = exceptions;
		}
		/* (non-Javadoc)
		 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
		 */
		protected IStatus run(IProgressMonitor monitor) {
			if(loads) {
				allLoadsBreakpoint = createSuspendOnAllLoads();
			}
			if(exceptions) {
				allExceptions = createSuspendOnException();
			}
			return Status.OK_STATUS;
		}
		
	}
	
	/**
	 * Starts the manager
	 */
	public void start() {
		IEclipsePreferences node = InstanceScope.INSTANCE.getNode(JavaScriptDebugPlugin.PLUGIN_ID);
		node.addPreferenceChangeListener(this);
		StartJob job = new StartJob(node.getBoolean(Constants.SUSPEND_ON_ALL_SCRIPT_LOADS, false), 
				node.getBoolean(Constants.SUSPEND_ON_THROWN_EXCEPTION, true));
		job.setSystem(true);
		job.setPriority(Job.INTERACTIVE);
		job.schedule();
	}
	
	/**
	 * Stops the manager and clean up
	 */
	public void stop() {
		IEclipsePreferences node = InstanceScope.INSTANCE.getNode(JavaScriptDebugPlugin.PLUGIN_ID);
		if(node != null) {
			node.removePreferenceChangeListener(this);
		}
		try {
			if(allLoadsBreakpoint != null) {
				allLoadsBreakpoint.delete();
			}
			if(allExceptions != null) {
				allExceptions.delete();
			}
		} catch (CoreException e) {
			JavaScriptDebugPlugin.log(e);
		}
		finally {
			try {
				//confirm they are all gone
				//https://bugs.eclipse.org/bugs/show_bug.cgi?id=323152
				IMarker[] markers = ResourcesPlugin.getWorkspace().getRoot().findMarkers(IJavaScriptBreakpoint.MARKER_ID, true, IResource.DEPTH_ZERO);
				for (int i = 0; i < markers.length; i++) {
					markers[i].delete();
				}
			}
			catch(CoreException ce) {
				JavaScriptDebugPlugin.log(ce);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener#preferenceChange(org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent)
	 */
	public void preferenceChange(PreferenceChangeEvent event) {
		if(event.getKey().equals(Constants.SUSPEND_ON_ALL_SCRIPT_LOADS)) {
			Object newval = event.getNewValue();
			if(newval != null && newval.equals(Boolean.TRUE.toString()) && allLoadsBreakpoint == null) {
				//create it
				allLoadsBreakpoint = createSuspendOnAllLoads();
			}
			else {
				deleteSuspendOnAllLoads();
			}
			return;
		}
		if(event.getKey().equals(Constants.SUSPEND_ON_THROWN_EXCEPTION)) {
			Object newval = event.getNewValue();
			if(newval != null && newval.equals(Boolean.TRUE.toString())) {
				//create it
				allExceptions = createSuspendOnException();
			}
			else {
				deleteSuspendOnException();
			}
		}
	}
	
	/**
	 * Creates the singleton exception breakpoint
	 * 
	 * @return the new {@link JavaScriptExceptionBreakpoint}
	 * @since 1.1
	 */
	private JavaScriptExceptionBreakpoint createSuspendOnException() {
		try {
			JavaScriptExceptionBreakpoint breakpoint = new JavaScriptExceptionBreakpoint(new HashMap());
			breakpoint.setPersisted(false); // do not persist - https://bugs.eclipse.org/bugs/show_bug.cgi?id=323152
			IDebugTarget[] targets = DebugPlugin.getDefault().getLaunchManager().getDebugTargets();
			for (int i = 0; i < targets.length; i++) {
				if(targets[i] instanceof JavaScriptDebugTarget) {
					((JavaScriptDebugTarget)targets[i]).breakpointAdded(breakpoint);
				}
			}
			return breakpoint;
		}
		catch(DebugException de) {
			JavaScriptDebugPlugin.log(de);
		} catch (CoreException ce) {
			JavaScriptDebugPlugin.log(ce);
		}
		return null;
	}
	
	/**
	 * Deletes any set exception breakpoints
	 * 
	 * @since 1.1
	 */
	private void deleteSuspendOnException() {
		if(allExceptions != null) {
			//notify all the targets
			IDebugTarget[] targets = DebugPlugin.getDefault().getLaunchManager().getDebugTargets();
			for (int i = 0; i < targets.length; i++) {
				if(targets[i] instanceof JavaScriptDebugTarget) {
					((JavaScriptDebugTarget)targets[i]).breakpointRemoved(allExceptions, null);
				}
			}
			try {
				allExceptions.delete();
			} catch (CoreException e) {
				JavaScriptDebugPlugin.log(e);
			}
			finally {
				allExceptions = null;
			}
		}
	}
	
	/**
	 * Deletes the "suspend on all script loads" breakpoint and notifies debug targets
	 */
	private void deleteSuspendOnAllLoads() {
		if(allLoadsBreakpoint != null) {
			//notify all the targets
			IDebugTarget[] targets = DebugPlugin.getDefault().getLaunchManager().getDebugTargets();
			for (int i = 0; i < targets.length; i++) {
				if(targets[i] instanceof JavaScriptDebugTarget) {
					((JavaScriptDebugTarget)targets[i]).breakpointRemoved(allLoadsBreakpoint, null);
				}
			}
			try {
				allLoadsBreakpoint.delete();
			} catch (CoreException e) {
				JavaScriptDebugPlugin.log(e);
			}
			finally {
				allLoadsBreakpoint = null;
			}
		}
	}
	
	/**
	 * Creates the "suspend on all script loads" breakpoint and notifies all active debug targets 
	 * @return the "suspend on all script loads" breakpoint or <code>null</code>
	 */
	private IJavaScriptLoadBreakpoint createSuspendOnAllLoads() {
		try {
			IJavaScriptLoadBreakpoint breakpoint = null;
			try {
				HashMap map = new HashMap();
				map.put(JavaScriptLoadBreakpoint.GLOBAL_SUSPEND, Boolean.TRUE);
				breakpoint = JavaScriptDebugModel.createScriptLoadBreakpoint(
													ResourcesPlugin.getWorkspace().getRoot(), 
													-1, 
													-1, 
													map, 
													false);
				breakpoint.setPersisted(false); //do not persist - https://bugs.eclipse.org/bugs/show_bug.cgi?id=323152
			} catch (DebugException e) {
				JavaScriptDebugPlugin.log(e);
			}
			if(breakpoint != null) {
				//notify all the targets
				IDebugTarget[] targets = DebugPlugin.getDefault().getLaunchManager().getDebugTargets();
				for (int i = 0; i < targets.length; i++) {
					if(targets[i] instanceof JavaScriptDebugTarget) {
						((JavaScriptDebugTarget)targets[i]).breakpointAdded(breakpoint);
					}
				}
			}
			return breakpoint;
		}
		catch(CoreException ce) {
			JavaScriptDebugPlugin.log(ce);
			return null;
		}
	}
	
	/**
	 * Allows the name of the script the global load breakpoint will suspend on
	 * 
	 * @param scriptpath the name of the script
	 */
	public static void setGlobalSuspendOn(String scriptpath) {
		if(allLoadsBreakpoint != null) {
			try {
				allLoadsBreakpoint.getMarker().setAttribute(IJavaScriptBreakpoint.SCRIPT_PATH, scriptpath);
			} catch (CoreException e) {
				JavaScriptDebugPlugin.log(e);
			}
		}
	}
	
	/**
	 * Returns the complete list of breakpoints that this manager
	 * creates or an empty list, never <code>null</code>
	 * @return the listing of managed breakpoints never <code>null</code>
	 */
	public static IJavaScriptBreakpoint[] getAllManagedBreakpoints() {
		ArrayList breakpoints = new ArrayList();
		if(allLoadsBreakpoint != null) {
			breakpoints.add(allLoadsBreakpoint);
		}
		if(allExceptions != null) {
			breakpoints.add(allExceptions);
		}
		return (IJavaScriptBreakpoint[]) breakpoints.toArray(new IJavaScriptBreakpoint[breakpoints.size()]);
	}
	
}
