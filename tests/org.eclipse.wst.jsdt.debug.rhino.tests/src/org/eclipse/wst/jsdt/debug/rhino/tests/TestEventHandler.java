/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.rhino.tests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.EventPacket;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.JSONConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.JSONUtil;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.RhinoRequest;
import org.eclipse.wst.jsdt.debug.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.transport.exception.DisconnectedException;
import org.eclipse.wst.jsdt.debug.transport.exception.TimeoutException;

/**
 * Event handler for testing purposes
 * 
 * @since 1.0
 */
public class TestEventHandler implements Runnable {

	/**
	 * Sub-handler for test to implement and add to the root
	 * {@link TestEventHandler}
	 */
	public interface Subhandler {
		boolean handleEvent(DebugSession debugSession, EventPacket event);

		String testName();
	}

	/**
	 * Sub-handler for breakpoint specific operations
	 * 
	 * @since 1.1
	 */
	public interface BreakpointSubHandler extends Subhandler {
		/**
		 * Array of line numbers to add breakpoints to
		 * 
		 * @param lines
		 */
		void linesToAddBreakpoints(int[] lines);

		/**
		 * Array of lines to remove breakpoints from
		 * 
		 * @param lines
		 */
		void linesToRemoveBreakpoints(int[] lines);
	}

	private DebugSession debugSession;
	private Thread thread;
	private final List subhandlers = new ArrayList();
	private final ArrayList expectedEvents = new ArrayList();
	private final ArrayList actualEvents = new ArrayList();
	private volatile boolean shutdown = false;

	private int eventCount = 0;
	private String testname = null;

	/**
	 * Constructor
	 * 
	 * @param debugSession
	 *            the current session to handle
	 * @param testname
	 *            the name of the test using this handler
	 */
	public TestEventHandler(DebugSession debugSession, String testname) {
		this.debugSession = debugSession;
		this.thread = new Thread(this, "TestEventHandler"); //$NON-NLS-1$
		this.testname = testname;
	}

	/**
	 * start the test handler
	 */
	public void start() {
		thread.start();
	}

	/**
	 * stop the test handler
	 */
	public void stop() {
		shutdown = true;
		thread.interrupt();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized (this) {
			this.expectedEvents.clear();
			this.actualEvents.clear();
			this.subhandlers.clear();
			this.eventCount = 0;
		}
	}

	/**
	 * Sets the events that the handler is expected to receive
	 * 
	 * @param events
	 */
	public synchronized void setExpectedEvents(EventPacket[] events) {
		this.expectedEvents.clear();
		for (int i = 0; i < events.length; i++) {
			this.expectedEvents.add(events[i]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while (!shutdown) {
			try {
				EventPacket event = (EventPacket) debugSession.receive(JSONConstants.EVENT, VirtualMachine.DEFAULT_TIMEOUT);
				handleEvent(event);
			} catch (TimeoutException e) {
				// ignore
			} catch (DisconnectedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Add a sub handler
	 * 
	 * @param subhandler
	 */
	public synchronized void addSubhandler(Subhandler subhandler) {
		subhandlers.add(subhandler);
	}

	/**
	 * Remove a sub handler
	 * 
	 * @param subhandler
	 */
	public synchronized void removeSubhandler(Subhandler subhandler) {
		subhandlers.remove(subhandler);
	}

	/**
	 * Handles the given event packet
	 * 
	 * @param event
	 */
	private synchronized void handleEvent(EventPacket event) {
		String estring = null;
		if (RequestTest.isTracing()) {
			estring = JSONUtil.write(event.toJSON());
			System.out.println(this.testname + " got event: " + estring); //$NON-NLS-1$
		}
		for (ListIterator iterator = subhandlers.listIterator(); iterator.hasNext();) {
			Subhandler subhandler = (Subhandler) iterator.next();
			try {
				if (subhandler.handleEvent(debugSession, event)) {
					if (RequestTest.isTracing()) {
						System.out.println("\t" + this.testname + " handled event: " + estring); //$NON-NLS-1$ //$NON-NLS-2$
					}
					this.expectedEvents.remove(event);
					actualEvents.add(event);
					eventCount++;
					notifyAll();
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		if (!event.getEvent().equals("thread")) { //$NON-NLS-1$
			sendContinue(event, null);
		}

	}

	/**
	 * Continues the handler
	 * 
	 * @param event
	 * @param step
	 */
	protected void sendContinue(EventPacket event, String step) {
		Number threadId = (Number) event.getBody().get("threadId"); //$NON-NLS-1$

		RhinoRequest request = new RhinoRequest("continue"); //$NON-NLS-1$
		request.getArguments().put("threadId", threadId); //$NON-NLS-1$
		request.getArguments().put("step", step); //$NON-NLS-1$
		try {
			debugSession.send(request);
			debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		} catch (DisconnectedException e) {
			if (!shutdown)
				e.printStackTrace();
		} catch (TimeoutException e) {
			if (!shutdown)
				e.printStackTrace();
		}
	}

	/**
	 * Waits for all of the events specified from
	 * {@link #setExpectedEvents(EventPacket[])}
	 * 
	 * @param count
	 */
	public synchronized void waitForEvents(int count) {
		long timeout = System.currentTimeMillis() + 5000;
		while (eventCount < count && System.currentTimeMillis() < timeout) {
			try {
				wait(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			if (eventCount != count) {
				for (Iterator it = actualEvents.iterator(); it.hasNext();) {
					System.err.println(it.next().toString());
				}
				throw new IllegalStateException("eventcount was: " + eventCount + " expected: " + count); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} finally {
			this.eventCount = 0;
		}
	}

	/**
	 * Waits for the given collection of events to be handled, or a default
	 * timeout to occur
	 * 
	 * @param events
	 */
	public synchronized void waitForEvents(EventPacket[] events) {
		long timeout = System.currentTimeMillis() + 5000;
		while (!this.expectedEvents.isEmpty() && System.currentTimeMillis() < timeout) {
			try {
				wait(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (!this.expectedEvents.isEmpty()) {
			try {
				throw new IllegalStateException("Some expected events were not received"); //$NON-NLS-1$
			} finally {
				this.eventCount = 0;
				for (int i = 0; i < this.expectedEvents.size(); i++) {
					System.out.println(this.testname + " missed expected event: " + JSONUtil.write(this.expectedEvents.get(i))); //$NON-NLS-1$
				}
			}
		}
		if (this.eventCount > events.length) {
			try {
				throw new IllegalStateException(this.testname + " got too many events - expected [" + events.length + "] got [" + eventCount + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} finally {
				this.eventCount = 0;
			}
		}
	}
}
