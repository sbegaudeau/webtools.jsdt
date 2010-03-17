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
package org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventQueue;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventSet;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ExceptionRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ScriptLoadRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ThreadEnterRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ThreadExitRequest;
import org.eclipse.wst.jsdt.debug.internal.rhino.RhinoDebugPlugin;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.ScriptReferenceImpl;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.ThreadReferenceImpl;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.VirtualMachineImpl;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.request.BreakpointRequestImpl;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.request.DebuggerStatementRequestImpl;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.request.EventRequestManagerImpl;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.request.StepRequestImpl;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.request.SuspendRequestImpl;
import org.eclipse.wst.jsdt.debug.rhino.transport.DisconnectedException;
import org.eclipse.wst.jsdt.debug.rhino.transport.EventPacket;
import org.eclipse.wst.jsdt.debug.rhino.transport.JSONConstants;
import org.eclipse.wst.jsdt.debug.rhino.transport.TimeoutException;

/**
 * Default implementation of {@link EventQueue}
 * 
 * @since 1.0
 */
public final class EventQueueImpl implements EventQueue {

	private VirtualMachineImpl vm;
	private EventRequestManagerImpl eventRequestManager;

	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param eventRequestManager
	 */
	public EventQueueImpl(VirtualMachineImpl vm, EventRequestManagerImpl eventRequestManager) {
		this.vm = vm;
		this.eventRequestManager = eventRequestManager;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.event.EventQueue#remove()
	 */
	public EventSet remove() {
		return remove(-1);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.event.EventQueue#remove(int)
	 */
	public EventSet remove(int timeout) {
		try {
			while (true) {
				EventPacket event = vm.receiveEvent(timeout);
				String eventName = event.getEvent();
				EventSetImpl eventSet = new EventSetImpl(vm);
				if (eventName.equals(JSONConstants.SCRIPT)) {
					Long threadId = new Long(((Number) event.getBody().get(JSONConstants.THREAD_ID)).longValue());
					ThreadReferenceImpl thread = vm.getThread(threadId);
					if(thread == null) {
						continue;
					}
					eventSet.setThread(thread);
					Long scriptId = new Long(((Number) event.getBody().get(JSONConstants.SCRIPT_ID)).longValue());
					ScriptReferenceImpl script = vm.addScript(scriptId);
					if(script != null) {
						List scriptLoadRequests = eventRequestManager.scriptLoadRequests();
						for (Iterator iterator = scriptLoadRequests.iterator(); iterator.hasNext();) {
							ScriptLoadRequest request = (ScriptLoadRequest) iterator.next();
							if (request.isEnabled()) {
								eventSet.add(new ScriptLoadEventImpl(vm, thread, script, request));
							}
						}
					}
				} else if (eventName.equals(JSONConstants.BREAK)) {
					Long threadId = new Long(((Number) event.getBody().get(JSONConstants.THREAD_ID)).longValue());
					ThreadReferenceImpl thread = vm.getThread(threadId);
					if(thread == null) {
						continue;
					}
					eventSet.setThread(thread);
					Long scriptId = new Long(((Number) event.getBody().get(JSONConstants.SCRIPT_ID)).longValue());
					ScriptReferenceImpl script = vm.getScript(scriptId);
					if(script != null) {
						int lineNumber = ((Number) event.getBody().get(JSONConstants.LINE_NUMBER)).intValue();
						Location location = script.lineLocation(lineNumber);
						boolean atBreakpoint = false;
						List jsonBreakpoints = (List) event.getBody().get(JSONConstants.BREAKPOINTS);
						if (jsonBreakpoints != null) {
							List breakpoints = new ArrayList(jsonBreakpoints.size());
							for (Iterator iterator = jsonBreakpoints.iterator(); iterator.hasNext();) {
								Number breakpointId = (Number) iterator.next();
								breakpoints.add(new Long(breakpointId.longValue()));
							}
							List breakpointRequests = eventRequestManager.breakpointRequests();
							for (Iterator iterator = breakpointRequests.iterator(); iterator.hasNext();) {
								BreakpointRequestImpl request = (BreakpointRequestImpl) iterator.next();
								ThreadReference requestThread = request.thread();
								if (request.isEnabled() && (requestThread == null || thread == requestThread) && breakpoints.contains(request.breakpointId())) {
									eventSet.add(new BreakpointEventImpl(vm, thread, location, request));
									atBreakpoint = true;
								}
							}
						}
						String stepType = (String) event.getBody().get(JSONConstants.STEP);
						if (JSONConstants.SUSPENDED.equals(stepType)) {
							List suspendRequests = eventRequestManager.suspendRequests();
							for (Iterator iterator = suspendRequests.iterator(); iterator.hasNext();) {
								SuspendRequestImpl request = (SuspendRequestImpl) iterator.next();
								ThreadReference requestThread = request.thread();
								if (request.isEnabled() && (requestThread == null || thread == requestThread)) {
									eventSet.add(new SuspendEventImpl(vm, thread, location, request));
								}
							}
						} else if (stepType != null) {
							List stepRequests = eventRequestManager.stepRequests();
							for (Iterator iterator = stepRequests.iterator(); iterator.hasNext();) {
								StepRequestImpl request = (StepRequestImpl) iterator.next();
								ThreadReference requestThread = request.thread();
								if (request.isEnabled() && (requestThread == null || thread == requestThread)) {
									eventSet.add(new StepEventImpl(vm, thread, location, request));
								}
							}
						}
						Boolean debuggerStatement = (Boolean) event.getBody().get(JSONConstants.DEBUGGER_STATEMENT);
						if (debuggerStatement.booleanValue()) {
							List debuggerStatementRequests = eventRequestManager.debuggerStatementRequests();
							for (Iterator iterator = debuggerStatementRequests.iterator(); iterator.hasNext();) {
								DebuggerStatementRequestImpl request = (DebuggerStatementRequestImpl) iterator.next();
								ThreadReference requestThread = request.thread();
								if (request.isEnabled() && (requestThread == null || thread == requestThread)) {
									eventSet.add(new DebuggerStatementEventImpl(vm, thread, location, request));
								}
							}
						}
						if (!eventSet.isEmpty()) {
							thread.markSuspended(atBreakpoint);
						}
					}
				} else if (eventName.equals(JSONConstants.EXCEPTION)) {
					Long threadId = new Long(((Number) event.getBody().get(JSONConstants.THREAD_ID)).longValue());
					ThreadReferenceImpl thread = vm.getThread(threadId);
					if(thread != null) {
						eventSet.setThread(thread);
						Long scriptId = new Long(((Number) event.getBody().get(JSONConstants.SCRIPT_ID)).longValue());
						ScriptReferenceImpl script = vm.getScript(scriptId);
						int lineNumber = ((Number) event.getBody().get(JSONConstants.LINE_NUMBER)).intValue();
						Location location = script.lineLocation(lineNumber);
						String message = (String) event.getBody().get(JSONConstants.MESSAGE);
						List exceptionRequests = eventRequestManager.exceptionRequests();
						for (Iterator iterator = exceptionRequests.iterator(); iterator.hasNext();) {
							ExceptionRequest request = (ExceptionRequest) iterator.next();
							if (request.isEnabled()) {
								eventSet.add(new ExceptionEventImpl(vm, thread, location, message, request));
							}
						}
						if (!eventSet.isEmpty()) {
							thread.markSuspended(false);
						}
					}
				} else if (eventName.equals(JSONConstants.THREAD)) {
					Long threadId = new Long(((Number) event.getBody().get(JSONConstants.THREAD_ID)).longValue());
					String type = (String) event.getBody().get(JSONConstants.TYPE);
					if (JSONConstants.ENTER.equals(type)) {
						ThreadReferenceImpl thread = vm.getThread(threadId);
						if(thread != null) {
							eventSet.setThread(thread);
							List threadEnterRequests = eventRequestManager.threadEnterRequests();
							for (Iterator iterator = threadEnterRequests.iterator(); iterator.hasNext();) {
								ThreadEnterRequest request = (ThreadEnterRequest) iterator.next();
								if (request.isEnabled()) {
									eventSet.add(new ThreadEnterEventImpl(vm, thread, request));
								}
							}
						}
					} else if (JSONConstants.EXIT.equals(type)) {
						ThreadReferenceImpl thread = vm.removeThread(threadId);
						List threadExitRequests = eventRequestManager.threadExitRequests();
						for (Iterator iterator = threadExitRequests.iterator(); iterator.hasNext();) {
							ThreadExitRequest request = (ThreadExitRequest) iterator.next();
							if (request.isEnabled()) {
								eventSet.add(new ThreadExitEventImpl(vm, thread, request));
							}
						}
					}
				}
				if (eventSet.isEmpty()) {
					eventSet.resume();
					continue;
				}
				return eventSet;
			}
		} catch (TimeoutException e) {
			RhinoDebugPlugin.log(e);
		} catch (DisconnectedException e) {
			vm.disconnectVM();
		}
		return null;
	}
}