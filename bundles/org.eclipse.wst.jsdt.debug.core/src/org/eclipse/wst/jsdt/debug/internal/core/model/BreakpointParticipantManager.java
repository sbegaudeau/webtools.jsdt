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
package org.eclipse.wst.jsdt.debug.internal.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpoint;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpointParticipant;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptFunctionBreakpoint;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptLineBreakpoint;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptLoadBreakpoint;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptThread;
import org.eclipse.wst.jsdt.debug.internal.core.Constants;
import org.eclipse.wst.jsdt.debug.internal.core.JavaScriptDebugPlugin;

/**
 * Manager for all {@link IJavaScriptBreakpointParticipant}s
 * 
 * @since 1.0
 */
public class BreakpointParticipantManager {

	/**
	 * proxy to a "real" {@link IJavaScriptBreakpointParticipant} to allow lazy loading
	 * of classes from extension points
	 */
	class ListenerExtension implements IJavaScriptBreakpointParticipant {
		
		private IConfigurationElement element = null;
		private IJavaScriptBreakpointParticipant delegate = null;
		
		/**
		 * Constructor
		 * @param element
		 */
		public ListenerExtension(IConfigurationElement element) {
			this.element = element;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpointParticipant#breakpointHit(org.eclipse.wst.jsdt.debug.core.model.IJavaScriptThread, org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpoint)
		 */
		public int breakpointHit(IJavaScriptThread thread, IJavaScriptBreakpoint breakpoint) {
			IJavaScriptBreakpointParticipant part = getDelegate();
			if(part != null) {
				return part.breakpointHit(thread, breakpoint);
			}
			return DONT_CARE;
		}
		
		/**
		 * @return the delegate
		 */
		synchronized IJavaScriptBreakpointParticipant getDelegate() {
			if(delegate == null) {
				try {
					delegate = (IJavaScriptBreakpointParticipant) element.createExecutableExtension(Constants.CLASS);
				} catch (CoreException e) {
					JavaScriptDebugPlugin.log(e);
				}
			}
			return delegate;
		}
	}
	
	static final IJavaScriptBreakpointParticipant[] NO_PARTICIPANTS = new IJavaScriptBreakpointParticipant[0];
	
	public static final String ALL = "*"; //$NON-NLS-1$
	public static final String FUNCTION = "function"; //$NON-NLS-1$
	public static final String LINE = "line"; //$NON-NLS-1$
	public static final String SCRIPT = "script"; //$NON-NLS-1$
	
	private Map participants = null;
	
	
	IJavaScriptBreakpointParticipant[] getParticipants(String kind) {
		initialize();
		ArrayList parts = (ArrayList) this.participants.get(kind);
		if(parts != null){
			return (IJavaScriptBreakpointParticipant[]) parts.toArray(new IJavaScriptBreakpointParticipant[parts.size()]);
		}
		return NO_PARTICIPANTS;
	}
	
	/**
	 * Returns the complete collection of {@link IJavaScriptBreakpointParticipant}s for the given
	 * breakpoint.<br>
	 * <br>
	 * This method does not return <code>null</code>, an empty array is returned if no {@link IJavaScriptBreakpointParticipant}s
	 * have been defined for the given breakpoint.
	 * 
	 * @param kind the kind
	 * @return the complete list of {@link IJavaScriptBreakpointParticipant}s or an empty array, never <code>null</code>
	 */
	public IJavaScriptBreakpointParticipant[] getParticipants(IJavaScriptBreakpoint breakpoint) {
		initialize();
		ArrayList parts = new ArrayList();
		parts.addAll((ArrayList)this.participants.get(ALL));
		if(breakpoint instanceof IJavaScriptFunctionBreakpoint) {
			parts.addAll((ArrayList)this.participants.get(FUNCTION));
		}
		else if(breakpoint instanceof IJavaScriptLoadBreakpoint) {
			parts.addAll((ArrayList)this.participants.get(SCRIPT));
		}
		else if(breakpoint instanceof IJavaScriptLineBreakpoint) {
			parts.addAll((ArrayList)this.participants.get(LINE));
		}
		if(parts.size() < 1) {
			return NO_PARTICIPANTS;
		}
		return (IJavaScriptBreakpointParticipant[]) parts.toArray(new IJavaScriptBreakpointParticipant[parts.size()]);
	}
	
	/**
	 * Adds the given participant to the manager.
	 * @param kind the kind, <code>null</code> is not accepted
	 * @param participant
	 */
	public void addParticipant(String kind, IJavaScriptBreakpointParticipant participant) {
		initialize();
		Assert.isNotNull(kind);
		this.participants.put(kind, participant);
	}
	
	/**
	 * Load all the extensions
	 */
	private synchronized void initialize() {
		if(this.participants == null) {
			this.participants = Collections.synchronizedMap(new HashMap());
			IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(JavaScriptDebugPlugin.PLUGIN_ID, Constants.BREAKPOINT_PARTICIPANTS);
			IConfigurationElement[] elements = extensionPoint.getConfigurationElements();
			for (int i = 0; i < elements.length; i++) {
				this.participants.put(elements[i].getAttribute(Constants.KIND), elements[i]);
			}
		}
	}
	
	/**
	 * Disposes and cleans up resources
	 */
	public void dispose() {
		if(this.participants != null) {
			this.participants.clear();
			this.participants = null;
		}
	}
}
