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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.Event;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventQueue;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventSet;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequest;

/**
 * Event dispatcher that notifies registered model elements
 * 
 * @see IJavaScriptEventListener
 * @since 1.0
 */
public final class EventDispatcher implements Runnable {

	private HashMap listeners = null;
	private boolean shutdown = false;
	private JavaScriptDebugTarget target = null;

	/**
	 * Constructor
	 * 
	 * @param target
	 */
	public EventDispatcher(JavaScriptDebugTarget target) {
		this.target = target;
	}

	/**
	 * Registers the given listener for the specified request.
	 * 
	 * @param listener
	 * @param request
	 */
	public synchronized void addEventListener(IJavaScriptEventListener listener, EventRequest request) {
		if(this.listeners == null) {
			this.listeners = new HashMap(4);
		}
		HashSet lsts = (HashSet) this.listeners.get(request);
		if(lsts == null) {
			lsts = new HashSet(4);
			this.listeners.put(request, lsts);
		}
		lsts.add(listener);
	}

	/**
	 * Removes the given listener for the given request and returns the status of the removal
	 * as per the contract of {@link Collection#remove(Object)}
	 * 
	 * @param listener
	 * @param request
	 */
	public synchronized boolean removeEventListener(IJavaScriptEventListener listener, EventRequest request) {
		if(this.listeners != null) {
			HashSet lsts = (HashSet) this.listeners.get(request);
			if(lsts != null) {
				return lsts.remove(listener);
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if (shutdown) {
			return; // no-op
		}
		VirtualMachine vm = target.getVM();
		if (vm != null) {
			EventQueue queue = vm.eventQueue();
			EventSet eventset = null;
			while (!shutdown) {
				eventset = queue.remove();
				if (eventset != null) {
					dispatch(eventset);
				}
			}
		}
	}

	/**
	 * performs the re-firing of JSDI events up to any model elements listening
	 * 
	 * @param eventset
	 */
	void dispatch(EventSet eventSet) {
		Event event = null;
		boolean resume = true;
		HashSet lsts = null;
		for (Iterator iter = eventSet.iterator(); iter.hasNext();) {
			event = (Event) iter.next();
			if (event == null) {
				continue;
			}
			lsts = (HashSet) this.listeners.get(event.request());
			if(lsts != null) {
				for (Iterator iter2 = lsts.iterator(); iter2.hasNext();) {
					resume &= ((IJavaScriptEventListener)iter2.next()).handleEvent(event, target, false, eventSet);
				}
			}
		}

		for (Iterator iter = eventSet.iterator(); iter.hasNext();) {
			event = (Event) iter.next();
			if (event == null) {
				continue;
			}
			lsts = (HashSet) this.listeners.get(event.request());
			if(lsts != null) {
				for (Iterator iter2 = lsts.iterator(); iter2.hasNext();) {
					((IJavaScriptEventListener)iter2.next()).eventSetComplete(event, target, !resume, eventSet);
				}
			}
		}
		if (resume) {
			eventSet.resume();
		}
	}

	/**
	 * Shut down and clean up the dispatcher
	 */
	public void shutdown() {
		shutdown = true;
		listeners.clear();
	}
}
