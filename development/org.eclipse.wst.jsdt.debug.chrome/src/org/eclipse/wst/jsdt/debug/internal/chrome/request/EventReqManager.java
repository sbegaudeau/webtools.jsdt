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
package org.eclipse.wst.jsdt.debug.internal.chrome.request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.BreakpointRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.DebuggerStatementRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ExceptionRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ScriptLoadRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.StepRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.SuspendRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ThreadEnterRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ThreadExitRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.VMDeathRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.VMDisconnectRequest;
import org.eclipse.wst.jsdt.debug.internal.chrome.jsdi.MirrorImpl;

/**
 * {@link EventRequestManager} for Chrome
 * 
 * @since 1.0
 */
public class EventReqManager extends MirrorImpl implements EventRequestManager {

	private List threadexits = Collections.synchronizedList(new ArrayList(4));
	private List threadenters = Collections.synchronizedList(new ArrayList(4));
	private List breakpoints = Collections.synchronizedList(new ArrayList(4));
	private List debuggers = Collections.synchronizedList(new ArrayList(4));
	private List exceptions = Collections.synchronizedList(new ArrayList(4));
	private List loads = Collections.synchronizedList(new ArrayList(4));
	private List steps = Collections.synchronizedList(new ArrayList(4));
	private List suspends = Collections.synchronizedList(new ArrayList(4));
	private List disconnects = Collections.synchronizedList(new ArrayList(4));
	private List deaths = Collections.synchronizedList(new ArrayList(4));
	
	private HashMap kind = new HashMap(10);
	
	/**
	 * Constructor
	 * 
	 * @param vm the underlying virtual machine
	 */
	public EventReqManager(VirtualMachine vm) {
		super(vm);
		/*kind.put(CFBreakpointRequest.class, breakpoints);
		kind.put(CFDebuggerRequest.class, debuggers);
		kind.put(CFExceptionRequest.class, exceptions);
		kind.put(CFScriptLoadRequest.class, loads);
		kind.put(CFStepRequest.class, steps);
		kind.put(CFSuspendRequest.class, suspends);
		kind.put(CFThreadEnterRequest.class, threadenters);
		kind.put(CFThreadExitRequest.class, threadexits);
		kind.put(CFDisconnectRequest.class, disconnects);
		kind.put(CFDeathRequest.class, deaths);*/
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createBreakpointRequest(org.eclipse.wst.jsdt.debug.core.jsdi.Location)
	 */
	public BreakpointRequest createBreakpointRequest(Location location) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#breakpointRequests()
	 */
	public List breakpointRequests() {
		return Collections.unmodifiableList(breakpoints);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createDebuggerStatementRequest()
	 */
	public DebuggerStatementRequest createDebuggerStatementRequest() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#debuggerStatementRequests()
	 */
	public List debuggerStatementRequests() {
		return Collections.unmodifiableList(debuggers);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createExceptionRequest()
	 */
	public ExceptionRequest createExceptionRequest() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#exceptionRequests()
	 */
	public List exceptionRequests() {
		return Collections.unmodifiableList(exceptions);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createScriptLoadRequest()
	 */
	public ScriptLoadRequest createScriptLoadRequest() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#scriptLoadRequests()
	 */
	public List scriptLoadRequests() {
		return Collections.unmodifiableList(loads);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createStepRequest(org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference, int)
	 */
	public StepRequest createStepRequest(ThreadReference thread, int step) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#stepRequests()
	 */
	public List stepRequests() {
		return Collections.unmodifiableList(steps);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createSuspendRequest(org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference)
	 */
	public SuspendRequest createSuspendRequest(ThreadReference thread) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#suspendRequests()
	 */
	public List suspendRequests() {
		return Collections.unmodifiableList(suspends);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createThreadEnterRequest()
	 */
	public ThreadEnterRequest createThreadEnterRequest() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#threadEnterRequests()
	 */
	public List threadEnterRequests() {
		return Collections.unmodifiableList(threadenters);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createThreadExitRequest()
	 */
	public ThreadExitRequest createThreadExitRequest() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#threadExitRequests()
	 */
	public List threadExitRequests() {
		return Collections.unmodifiableList(threadexits);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#deleteEventRequest(org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequest)
	 */
	public void deleteEventRequest(EventRequest eventRequest) {
		List requests = (List) kind.get(eventRequest.getClass());
		if(requests != null) {
			requests.remove(eventRequest);
			eventRequest.setEnabled(false);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#deleteEventRequest(java.util.List)
	 */
	public void deleteEventRequest(List eventRequests) {
		for (Iterator iter = eventRequests.iterator(); iter.hasNext();) {
			deleteEventRequest((EventRequest) iter.next());
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createVMDeathRequest()
	 */
	public VMDeathRequest createVMDeathRequest() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#vmDeathRequests()
	 */
	public List vmDeathRequests() {
		return Collections.unmodifiableList(deaths);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createVMDisconnectRequest()
	 */
	public VMDisconnectRequest createVMDisconnectRequest() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#vmDisconnectRequests()
	 */
	public List vmDisconnectRequests() {
		return Collections.unmodifiableList(disconnects);
	}
}
