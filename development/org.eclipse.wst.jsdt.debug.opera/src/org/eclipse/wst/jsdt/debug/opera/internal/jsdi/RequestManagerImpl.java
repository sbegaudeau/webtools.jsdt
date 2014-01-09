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
package org.eclipse.wst.jsdt.debug.opera.internal.jsdi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
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
import org.eclipse.wst.jsdt.debug.opera.internal.jsdi.request.BreakpointRequestImpl;
import org.eclipse.wst.jsdt.debug.opera.internal.jsdi.request.DebuggerStatementRequestImpl;
import org.eclipse.wst.jsdt.debug.opera.internal.jsdi.request.ExceptionRequestImpl;
import org.eclipse.wst.jsdt.debug.opera.internal.jsdi.request.ResumeRequestImpl;
import org.eclipse.wst.jsdt.debug.opera.internal.jsdi.request.ScriptLoadRequestImpl;
import org.eclipse.wst.jsdt.debug.opera.internal.jsdi.request.StepRequestImpl;
import org.eclipse.wst.jsdt.debug.opera.internal.jsdi.request.SuspendRequestImpl;
import org.eclipse.wst.jsdt.debug.opera.internal.jsdi.request.ThreadEnterRequestImpl;
import org.eclipse.wst.jsdt.debug.opera.internal.jsdi.request.ThreadExitRequestImpl;
import org.eclipse.wst.jsdt.debug.opera.internal.jsdi.request.VMDeathRequestImpl;
import org.eclipse.wst.jsdt.debug.opera.internal.jsdi.request.VMDisconnectRequestImpl;

/**
 * Default {@link EventRequestManager} implementation for Opera
 * 
 * @since 0.1
 */
public class RequestManagerImpl extends MirrorImpl implements EventRequestManager {

	private final Map eventsMap = new HashMap();
	private final List bpreqs = new ArrayList();
	private final List exreqs = new ArrayList();
	private final List dsreqs = new ArrayList();
	private final List slreqs = new ArrayList();
	private final List stepreqs = new ArrayList();
	private final List susreqs = new ArrayList();
	private final List resreqs = new ArrayList();
	private final List tereqs = new ArrayList();
	private final List texreqs = new ArrayList();
	private final List vdetreqs = new ArrayList();
	private final List vdisreqs = new ArrayList();
	
	/**
	 * Constructor
	 * @param vm
	 */
	public RequestManagerImpl(VirtualMachineImpl vm) {
		super(vm);
		eventsMap.put(BreakpointRequestImpl.class, bpreqs);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createBreakpointRequest(org.eclipse.wst.jsdt.debug.core.jsdi.Location)
	 */
	public BreakpointRequest createBreakpointRequest(Location location) {
		BreakpointRequestImpl request = new BreakpointRequestImpl((VirtualMachineImpl)virtualMachine(), location);
		bpreqs.add(request);
		return request;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#breakpointRequests()
	 */
	public List breakpointRequests() {
		return Collections.unmodifiableList(bpreqs);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createDebuggerStatementRequest()
	 */
	public DebuggerStatementRequest createDebuggerStatementRequest() {
		DebuggerStatementRequestImpl request = new DebuggerStatementRequestImpl((VirtualMachineImpl) virtualMachine());
		dsreqs.add(request);
		return request;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#debuggerStatementRequests()
	 */
	public List debuggerStatementRequests() {
		return Collections.unmodifiableList(dsreqs);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createExceptionRequest()
	 */
	public ExceptionRequest createExceptionRequest() {
		ExceptionRequestImpl request = new ExceptionRequestImpl((VirtualMachineImpl) virtualMachine());
		exreqs.add(request);
		return request;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#exceptionRequests()
	 */
	public List exceptionRequests() {
		return Collections.unmodifiableList(exreqs);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createScriptLoadRequest()
	 */
	public ScriptLoadRequest createScriptLoadRequest() {
		ScriptLoadRequestImpl request = new ScriptLoadRequestImpl((VirtualMachineImpl) virtualMachine());
		slreqs.add(request);
		return request;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#scriptLoadRequests()
	 */
	public List scriptLoadRequests() {
		return Collections.unmodifiableList(slreqs);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createStepRequest(org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference, int)
	 */
	public StepRequest createStepRequest(ThreadReference thread, int step) {
		StepRequestImpl request= new StepRequestImpl((VirtualMachineImpl) virtualMachine(), thread, step);
		stepreqs.add(request);
		return request;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#stepRequests()
	 */
	public List stepRequests() {
		return Collections.unmodifiableList(stepreqs);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createSuspendRequest(org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference)
	 */
	public SuspendRequest createSuspendRequest(ThreadReference thread) {
		SuspendRequestImpl request = new SuspendRequestImpl((VirtualMachineImpl) virtualMachine(), thread);
		susreqs.add(request);
		return request;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#suspendRequests()
	 */
	public List suspendRequests() {
		return Collections.unmodifiableList(susreqs);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createResumeRequest(org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference)
	 */
	public ResumeRequest createResumeRequest(ThreadReference thread) {
		ResumeRequestImpl request = new ResumeRequestImpl((VirtualMachineImpl) virtualMachine(), thread);
		resreqs.add(request);
		return request;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#resumeRequests()
	 */
	public List resumeRequests() {
		return Collections.unmodifiableList(resreqs);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createThreadEnterRequest()
	 */
	public ThreadEnterRequest createThreadEnterRequest() {
		ThreadEnterRequestImpl request = new ThreadEnterRequestImpl((VirtualMachineImpl) virtualMachine());
		tereqs.add(request);
		return request;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#threadEnterRequests()
	 */
	public List threadEnterRequests() {
		return Collections.unmodifiableList(tereqs);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createThreadExitRequest()
	 */
	public ThreadExitRequest createThreadExitRequest() {
		ThreadExitRequestImpl request = new ThreadExitRequestImpl((VirtualMachineImpl) virtualMachine());
		texreqs.add(request);
		return request;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#threadExitRequests()
	 */
	public List threadExitRequests() {
		return Collections.unmodifiableList(texreqs);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#deleteEventRequest(org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequest)
	 */
	public void deleteEventRequest(EventRequest eventRequest) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#deleteEventRequest(java.util.List)
	 */
	public void deleteEventRequest(List eventRequests) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createVMDeathRequest()
	 */
	public VMDeathRequest createVMDeathRequest() {
		VMDeathRequestImpl request = new VMDeathRequestImpl((VirtualMachineImpl) virtualMachine());
		vdetreqs.add(request);
		return request;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#vmDeathRequests()
	 */
	public List vmDeathRequests() {
		return Collections.unmodifiableList(vdetreqs);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#createVMDisconnectRequest()
	 */
	public VMDisconnectRequest createVMDisconnectRequest() {
		VMDisconnectRequestImpl request = new VMDisconnectRequestImpl((VirtualMachineImpl) virtualMachine());
		vdetreqs.add(request);
		return request;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager#vmDisconnectRequests()
	 */
	public List vmDisconnectRequests() {
		return Collections.unmodifiableList(vdisreqs);
	}
}
