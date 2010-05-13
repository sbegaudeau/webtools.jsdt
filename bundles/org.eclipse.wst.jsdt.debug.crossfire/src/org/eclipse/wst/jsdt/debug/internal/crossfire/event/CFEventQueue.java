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
package org.eclipse.wst.jsdt.debug.internal.crossfire.event;

import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventQueue;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventSet;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ScriptLoadRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ThreadEnterRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ThreadExitRequest;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.VMDeathRequest;
import org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi.CFMirror;
import org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi.CFScriptReference;
import org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi.CFThreadReference;
import org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi.CFVirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.DisconnectedException;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Event;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.TimeoutException;

/**
 * Default {@link EventQueue} for Crossfire
 * 
 * @since 1.0
 */
public class CFEventQueue extends CFMirror implements EventQueue {

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
				Event event = crossfire().receiveEvent(timeout);
				String name = event.getEvent();
				CFEventSet set = new CFEventSet(crossfire());
				if(Event.CLOSED.equals(name)) {
					List deaths = eventmgr.vmDeathRequests();
					for (Iterator iter = deaths.iterator(); iter.hasNext();) {
						VMDeathRequest request = (VMDeathRequest) iter.next();
						set.add(new CFVMDeathEvent(crossfire(), request));
					}
					crossfire().terminate();
				}
				else if(Event.ON_SCRIPT.equals(name)) {
					ThreadReference thread = crossfire().findThread(event.getContextId());
					if(thread != null) {
						CFScriptReference script = crossfire().addScript(event.getContextId(), event.getBody());
						List scripts = eventmgr.scriptLoadRequests();
						for (Iterator iter = scripts.iterator(); iter.hasNext();) {
							ScriptLoadRequest request = (ScriptLoadRequest) iter.next();
							set.add(new CFScriptLoadEvent(crossfire(), request, thread, script));
						}
					}
				}
				else if(Event.ON_CONTEXT_CREATED.equals(name)) {
					List threads = eventmgr.threadEnterRequests();
					CFThreadReference thread = crossfire().addThread(event.getContextId(), (String) event.getBody().get(CFThreadReference.HREF));
					for (Iterator iter = threads.iterator(); iter.hasNext();) {
						ThreadEnterRequest request = (ThreadEnterRequest) iter.next();
						set.add(new CFThreadEnterEvent(crossfire(), request, thread));
					}
				}
				else if(Event.ON_CONTEXT_DESTROYED.equals(name)) {
					ThreadReference thread = crossfire().findThread(event.getContextId());
					crossfire().removeThread(event.getContextId());
					if(thread != null) {
						List threads = eventmgr.threadExitRequests();
						for (Iterator iter = threads.iterator(); iter.hasNext();) {
							ThreadExitRequest request = (ThreadExitRequest) iter.next();
							set.add(new CFThreadExitEvent(crossfire(), request, thread));
						}
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
			crossfire().disconnectVM();
		}
		catch(TimeoutException te) {
			handleException(te.getMessage(), te);
		}
		return null;
	}
	
	/**
	 * Flushes and cleans up the queue
	 */
	public void dispose() {
		disposed = true;
	}
}
