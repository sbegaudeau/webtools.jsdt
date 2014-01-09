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
package org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi;

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
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ResumeRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ScriptLoadRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.StepRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.SuspendRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ThreadEnterRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ThreadExitRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.VMDeathRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.VMDisconnectRequest;
import org.eclipse.wst.jsdt.debug.internal.crossfire.request.CFBreakpointRequest;
import org.eclipse.wst.jsdt.debug.internal.crossfire.request.CFDeathRequest;
import org.eclipse.wst.jsdt.debug.internal.crossfire.request.CFDebuggerRequest;
import org.eclipse.wst.jsdt.debug.internal.crossfire.request.CFDisconnectRequest;
import org.eclipse.wst.jsdt.debug.internal.crossfire.request.CFExceptionRequest;
import org.eclipse.wst.jsdt.debug.internal.crossfire.request.CFResumeRequest;
import org.eclipse.wst.jsdt.debug.internal.crossfire.request.CFScriptLoadRequest;
import org.eclipse.wst.jsdt.debug.internal.crossfire.request.CFStepRequest;
import org.eclipse.wst.jsdt.debug.internal.crossfire.request.CFSuspendRequest;
import org.eclipse.wst.jsdt.debug.internal.crossfire.request.CFThreadEnterRequest;
import org.eclipse.wst.jsdt.debug.internal.crossfire.request.CFThreadExitRequest;

/**
 * Default {@link EventRequestManager} for Crossfire
 * 
 * @since 1.0
 */
public class CFEventRequestManager implements EventRequestManager {

	private List threadexits = Collections.synchronizedList(new ArrayList(4));
	private List threadenters = Collections.synchronizedList(new ArrayList(4));
	private List breakpoints = Collections.synchronizedList(new ArrayList(4));
	private List debuggers = Collections.synchronizedList(new ArrayList(4));
	private List exceptions = Collections.synchronizedList(new ArrayList(4));
	private List loads = Collections.synchronizedList(new ArrayList(4));
	private List steps = Collections.synchronizedList(new ArrayList(4));
	private List suspends = Collections.synchronizedList(new ArrayList(4));
	private List resumes = Collections.synchronizedList(new ArrayList(4));
	private List disconnects = Collections.synchronizedList(new ArrayList(4));
	private List deaths = Collections.synchronizedList(new ArrayList(4));
	
	private HashMap kind = new HashMap(10);
	
	private VirtualMachine vm = null;
	
	/**
	 * Constructor
	 * 
	 * @param vm
	 */
	public CFEventRequestManager(VirtualMachine vm) {
		this.vm = vm;
		kind.put(CFBreakpointRequest.class, breakpoints);
		kind.put(CFDebuggerRequest.class, debuggers);
		kind.put(CFExceptionRequest.class, exceptions);
		kind.put(CFScriptLoadRequest.class, loads);
		kind.put(CFStepRequest.class, steps);
		kind.put(CFSuspendRequest.class, suspends);
		kind.put(CFResumeRequest.class, resumes);
		kind.put(CFThreadEnterRequest.class, threadenters);
		kind.put(CFThreadExitRequest.class, threadexits);
		kind.put(CFDisconnectRequest.class, disconnects);
		kind.put(CFDeathRequest.class, deaths);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createBreakpointRequest(org.eclipse.wst.jsdt.debug.core.jsdi.Location)
	 */
	public BreakpointRequest createBreakpointRequest(Location location) {
		CFBreakpointRequest request = new CFBreakpointRequest(vm, location);
		breakpoints.add(request);
		return request;
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
		CFDebuggerRequest request = new CFDebuggerRequest(vm);
		debuggers.add(request);
		return request;
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
		CFExceptionRequest request = new CFExceptionRequest(vm);
		exceptions.add(request);
		return request;
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
		CFScriptLoadRequest request = new CFScriptLoadRequest(vm);
		loads.add(request);
		return request;
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
		CFStepRequest request = new CFStepRequest(vm, thread, step);
		steps.add(request);
		return request; 
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
		CFSuspendRequest request = new CFSuspendRequest(vm, thread);
		suspends.add(request);
		return request;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#suspendRequests()
	 */
	public List suspendRequests() {
		return Collections.unmodifiableList(suspends);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createResumeRequest(org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference)
	 */
	public ResumeRequest createResumeRequest(ThreadReference thread) {
		CFResumeRequest request = new CFResumeRequest(vm, thread);
		resumes.add(request);
		return request;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#resumeRequests()
	 */
	public List resumeRequests() {
		return Collections.unmodifiableList(resumes);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createThreadEnterRequest()
	 */
	public ThreadEnterRequest createThreadEnterRequest() {
		CFThreadEnterRequest request = new CFThreadEnterRequest(vm);
		threadenters.add(request);
		return request;
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
		CFThreadExitRequest request = new CFThreadExitRequest(vm);
		threadexits.add(request);
		return request;
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
		CFDeathRequest request = new CFDeathRequest(vm);
		deaths.add(request);
		return request;
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
		CFDisconnectRequest request = new CFDisconnectRequest(vm);
		disconnects.add(request);
		return request;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#vmDisconnectRequests()
	 */
	public List vmDisconnectRequests() {
		return Collections.unmodifiableList(disconnects);
	}

	/**
	 * Cleans up all requests
	 */
	public void dispose() {
		for (Iterator iter = kind.keySet().iterator(); iter.hasNext();) {
			List list = (List) kind.get(iter.next());
			list.clear();
		}
	}
}
