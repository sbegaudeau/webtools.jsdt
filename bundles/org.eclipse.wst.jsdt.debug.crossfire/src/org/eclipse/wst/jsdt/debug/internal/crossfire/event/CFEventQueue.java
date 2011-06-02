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
package org.eclipse.wst.jsdt.debug.internal.crossfire.event;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventQueue;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventSet;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ResumeRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ScriptLoadRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.SuspendRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ThreadEnterRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ThreadExitRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.VMDeathRequest;
import org.eclipse.wst.jsdt.debug.internal.crossfire.CrossFirePlugin;
import org.eclipse.wst.jsdt.debug.internal.crossfire.Tracing;
import org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi.CFLocation;
import org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi.CFMirror;
import org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi.CFScriptReference;
import org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi.CFThreadReference;
import org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi.CFVirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Attributes;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFEventPacket;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.JSON;
import org.eclipse.wst.jsdt.debug.transport.exception.DisconnectedException;
import org.eclipse.wst.jsdt.debug.transport.exception.TimeoutException;

/**
 * Default {@link EventQueue} for Crossfire
 * 
 * @since 1.0
 */
public class CFEventQueue extends CFMirror implements EventQueue {

	private static boolean TRACE = false;
	
	private EventRequestManager eventmgr = null;
	private boolean disposed = false;
	
	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param manager
	 */
	public CFEventQueue(CFVirtualMachine vm, EventRequestManager manager) {
		super(vm);
		this.eventmgr = manager;
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
			while(true && !disposed) {
				CFEventPacket event = crossfire().receiveEvent(timeout);
				String name = event.getEvent();
				CFEventSet set = new CFEventSet(crossfire());
				if(CFEventPacket.CLOSED.equals(name)) {
					List deaths = eventmgr.vmDeathRequests();
					for (Iterator iter = deaths.iterator(); iter.hasNext();) {
						VMDeathRequest request = (VMDeathRequest) iter.next();
						set.add(new CFVMDeathEvent(crossfire(), request));
					}
					if(TRACE) {
						Tracing.writeString("QUEUE [event - "+CFEventPacket.CLOSED+"] "+JSON.serialize(event)); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				else if(CFEventPacket.ON_BREAK.equals(name)) {
					if(TRACE) {
						Tracing.writeString("QUEUE [event - "+CFEventPacket.ON_BREAK+"] "+JSON.serialize(event)); //$NON-NLS-1$ //$NON-NLS-2$
					}
					String threadid = event.getContextId();
					if(threadid != null) {
						CFThreadReference thread = crossfire().findThread(threadid);
						set.setThread(thread);
						if(thread != null && !thread.isSuspended()) {
							List suspends = eventmgr.suspendRequests();
							for (Iterator iter = suspends.iterator(); iter.hasNext();) {
								SuspendRequest request = (SuspendRequest) iter.next();
								String url = (String) event.getBody().get(Attributes.URL);
								Number line = (Number) event.getBody().get(Attributes.LINE);
								CFScriptReference script = crossfire().findScript(url);
								if(script != null) {
									CFLocation loc = new CFLocation(crossfire(), script, null, line.intValue());
									set.add(new CFSuspendEvent(crossfire(), request, thread, loc));
								}
							}
							thread.markSuspended(true);
						}
						else {
							return null;
						}
					}
					else {
						return null;
					}
				}
				else if(CFEventPacket.ON_RESUME.equals(name)) {
					if(TRACE) {
						Tracing.writeString("QUEUE [event - "+CFEventPacket.ON_RESUME+"] "+JSON.serialize(event)); //$NON-NLS-1$ //$NON-NLS-2$
					}
					String threadid = event.getContextId();
					if(threadid != null) {
						CFThreadReference thread = crossfire().findThread(threadid);
						if(thread != null) {
							set.setThread(thread);
							List resumes = eventmgr.resumeRequests();
							for (Iterator iter = resumes.iterator(); iter.hasNext();) {
								ResumeRequest request = (ResumeRequest) iter.next();
								if(request.thread().equals(thread)) {
									CFLocation loc = new CFLocation(crossfire(), null, null, 0);
									set.add(new CFResumeEvent(crossfire(), request, thread, loc));
								}
							}
							thread.eventResume();
						}
						else {
							return null;
						}
					}
					else {
						return null;
					}
				}
				else if(CFEventPacket.ON_SCRIPT.equals(name)) {
					ThreadReference thread = crossfire().findThread(event.getContextId());
					if(thread != null) {
						set.setThread(thread);
						Map json = (Map) event.getBody().get(Attributes.SCRIPT);
						if(json != null) {
							CFScriptReference script = crossfire().addScript(event.getContextId(), json);
							List scripts = eventmgr.scriptLoadRequests();
							for (Iterator iter = scripts.iterator(); iter.hasNext();) {
								ScriptLoadRequest request = (ScriptLoadRequest) iter.next();
								set.add(new CFScriptLoadEvent(crossfire(), request, thread, script));
							}
						}
						else {
							return null;
						}
					}
					else {
						return null;
					}
					if(TRACE) {
						Tracing.writeString("QUEUE [event - "+CFEventPacket.ON_SCRIPT+"] "+JSON.serialize(event)); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				else if(CFEventPacket.ON_CONTEXT_SELECTED.equals(name)) {
					handleContext(set, event, true);
					if(TRACE) {
						Tracing.writeString("QUEUE [event - "+CFEventPacket.ON_CONTEXT_SELECTED+"] "+JSON.serialize(event)); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				else if(CFEventPacket.ON_CONTEXT_CHANGED.equals(name)) {
					handleContext(set, event, true);
					if(TRACE) {
						Tracing.writeString("QUEUE [event - "+CFEventPacket.ON_CONTEXT_CHANGED+"] "+JSON.serialize(event)); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				else if(CFEventPacket.ON_CONTEXT_LOADED.equals(name)) {
					handleContext(set, event, true);
					if(TRACE) {
						Tracing.writeString("QUEUE [event - "+CFEventPacket.ON_CONTEXT_LOADED+"] "+JSON.serialize(event)); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				else if(CFEventPacket.ON_CONTEXT_DESTROYED.equals(name)) {
					ThreadReference thread = crossfire().findThread(event.getContextId());
					crossfire().removeThread(event.getContextId());
					crossfire().removeScriptsForContext(event.getContextId());
					if(thread != null) {
						List threads = eventmgr.threadExitRequests();
						for (Iterator iter = threads.iterator(); iter.hasNext();) {
							ThreadExitRequest request = (ThreadExitRequest) iter.next();
							set.add(new CFThreadExitEvent(crossfire(), request, thread));
						}
					}
					else {
						return null;
					}
					if(TRACE) {
						Tracing.writeString("QUEUE [event - "+CFEventPacket.ON_CONTEXT_DESTROYED+"] "+JSON.serialize(event)); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				else if(CFEventPacket.ON_CONSOLE_DEBUG.equals(name)) {
					List info = (List) event.getBody().get(Attributes.DATA);
					if(info != null) {
						log(IStatus.INFO, info);
					}
					if(TRACE) {
						Tracing.writeString("QUEUE [event - "+CFEventPacket.ON_CONSOLE_DEBUG+"] "+JSON.serialize(event)); //$NON-NLS-1$ //$NON-NLS-2$
					}
					return null;
				}
				else if(CFEventPacket.ON_CONSOLE_ERROR.equals(name)) {
					List info = (List) event.getBody().get(Attributes.VALUE);
					if(info != null) {
						log(IStatus.ERROR, info);
					}
					if(TRACE) {
						Tracing.writeString("QUEUE [event - "+CFEventPacket.ON_CONSOLE_ERROR+"] "+JSON.serialize(event)); //$NON-NLS-1$ //$NON-NLS-2$
					}
					return null;
				}
				else if(CFEventPacket.ON_CONSOLE_INFO.equals(name)) {
					List info = (List) event.getBody().get(Attributes.DATA);
					if(info != null) {
						log(IStatus.INFO, info);
					}
					if(TRACE) {
						Tracing.writeString("QUEUE [event - "+CFEventPacket.ON_CONSOLE_INFO+"] "+JSON.serialize(event)); //$NON-NLS-1$ //$NON-NLS-2$
					}
					return null;
				}
				else if(CFEventPacket.ON_CONSOLE_LOG.equals(name)) {
					List info = (List) event.getBody().get(Attributes.DATA);
					if(info != null) {
						log(IStatus.INFO, info);
					}
					if(TRACE) {
						Tracing.writeString("QUEUE [event - "+CFEventPacket.ON_CONSOLE_LOG+"] "+JSON.serialize(event)); //$NON-NLS-1$ //$NON-NLS-2$
					}
					return null;
				}
				else if(CFEventPacket.ON_CONSOLE_WARN.equals(name)) {
					List info = (List) event.getBody().get(Attributes.DATA);
					if(info != null) {
						log(IStatus.WARNING, info);
					}
					if(TRACE) {
						Tracing.writeString("QUEUE [event - "+CFEventPacket.ON_CONSOLE_WARN+"] "+JSON.serialize(event)); //$NON-NLS-1$ //$NON-NLS-2$
					}
					return null;
				}
				else if(CFEventPacket.ON_INSPECT_NODE.equals(name)) {
					if(TRACE) {
						Tracing.writeString("QUEUE [event - "+CFEventPacket.ON_INSPECT_NODE+"] "+JSON.serialize(event)); //$NON-NLS-1$ //$NON-NLS-2$
					}
					return null;
				}
				else if(CFEventPacket.ON_TOGGLE_BREAKPOINT.equals(name)) {
					crossfire().toggleBreakpoint(event.getBody());
					if(TRACE) {
						Tracing.writeString("QUEUE [event - "+CFEventPacket.ON_TOGGLE_BREAKPOINT+"] "+JSON.serialize(event)); //$NON-NLS-1$ //$NON-NLS-2$
					}
					return null;
				}
				else {
					if(TRACE) {
						Tracing.writeString("QUEUE [unknown event - "+name+"] "+JSON.serialize(event)); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				if (set.isEmpty()) {
					set.resume();
					continue;
				}
				return set;
			}
		}
		catch(DisconnectedException de) {
			if(TRACE) {
				Tracing.writeString("QUEUE [disconnect exception]: "+de.getMessage()); //$NON-NLS-1$
			}
			crossfire().disconnectVM();
			handleException(de.getMessage(), (de.getCause() == null ? de : de.getCause()));
		}
		catch(TimeoutException te) {
			CrossFirePlugin.log(te);
		}
		return null;
	}
	
	/**
	 * Logs the entry from the queue
	 * 
	 * @param kind
	 * @param objects
	 */
	void log(int kind, List objects) {
		IStatus status = null;
		if(objects.size() > 1) {
			MultiStatus mstatus = new MultiStatus(CrossFirePlugin.PLUGIN_ID, kind, "Messages logged from Crossfire", null); //$NON-NLS-1$
			for (Iterator i = objects.iterator(); i.hasNext();) {
				mstatus.add(new Status(kind, CrossFirePlugin.PLUGIN_ID, i.next().toString()));
			}
			status = mstatus;
		}
		else if(objects.size() == 1) {
			status = new Status(kind, CrossFirePlugin.PLUGIN_ID, objects.iterator().next().toString());
		}
		if(status != null) {
			CrossFirePlugin.log(status);
		}
	}
	
	/**
	 * Handles a context created, loaded, and changed event
	 * @param set the {@link EventSet} to add to
	 * @param event the {@link CFEventPacket} received
	 * @param lookup if we should try to lookup the {@link ThreadReference} before creating a new one
	 */
	void handleContext(CFEventSet set, CFEventPacket event, boolean lookup) {
		List threads = eventmgr.threadEnterRequests();
		CFThreadReference thread = null;
		String context = event.getContextId();
		if(lookup) {
			thread = crossfire().findThread(context);
		}
		if(thread == null) {
			thread = crossfire().addThread(context, (String) event.getBody().get(Attributes.URL));
		}
		set.setThread(thread);
		for (Iterator iter = threads.iterator(); iter.hasNext();) {
			ThreadEnterRequest request = (ThreadEnterRequest) iter.next();
			set.add(new CFThreadEnterEvent(crossfire(), request, thread));
		}
	}
	
	/**
	 * Flushes and cleans up the queue
	 */
	public void dispose() {
		disposed = true;
	}
	
	/**
	 * Turns on / off tracing in the event queue
	 * @param tracing
	 */
	public static void setTracing(boolean tracing) {
		TRACE = tracing;
	}
}
