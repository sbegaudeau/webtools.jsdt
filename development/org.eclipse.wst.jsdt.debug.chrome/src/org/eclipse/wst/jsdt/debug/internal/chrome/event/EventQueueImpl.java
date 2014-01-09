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
package org.eclipse.wst.jsdt.debug.internal.chrome.event;

import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventQueue;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventSet;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager;
import org.eclipse.wst.jsdt.debug.internal.chrome.Tracing;
import org.eclipse.wst.jsdt.debug.internal.chrome.jsdi.MirrorImpl;
import org.eclipse.wst.jsdt.debug.internal.chrome.transport.Commands;
import org.eclipse.wst.jsdt.debug.transport.exception.DisconnectedException;
import org.eclipse.wst.jsdt.debug.transport.exception.TimeoutException;
import org.eclipse.wst.jsdt.debug.transport.packet.Event;

/**
 * EVent queue for Chrome
 * 
 * @since 1.0
 */
public class EventQueueImpl extends MirrorImpl implements EventQueue {

	private EventRequestManager ermanager = null;
	
	static boolean TRACE = false;
	
	/**
	 * Constructor
	 * 
	 * @param vm the underlying {@link VirtualMachine}
	 * @param manager the {@link EventRequestManager} to ask about pending requests
	 */
	public EventQueueImpl(VirtualMachine vm, EventRequestManager manager) {
		super(vm);
		this.ermanager = manager;
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
			//loop until disconnected - exception breaks loop
			while(true) {
				Event event = chromeVM().receiveEvent();
				if(event != null) {
					//TODO
					if(event.getEvent().equals(Commands.NAVIGATED)) {
						if(TRACE) {
							Tracing.writeString("got navigated event"); //$NON-NLS-1$
						}
					}
					else if(event.getEvent().equals(Commands.CLOSED)) {
						List requests = ermanager.threadExitRequests();
						for(Iterator i = requests.iterator(); i.hasNext();) {
							//TODO
						}
						if(TRACE) {
							Tracing.writeString("got closed event"); //$NON-NLS-1$
						}
					}
				}
			}
		}
		catch(DisconnectedException de) {
			
		}
		catch(TimeoutException te) {
			
		}
		return null;
	}
	
	/**
	 * Enables / Disables tracing in the all of the JSDI implementations
	 * 
	 * @param trace
	 */
	public static void setTracing(boolean trace) {
		TRACE = trace;
	}
}
