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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.rhino.debugger.RhinoDebugger;
import org.eclipse.wst.jsdt.debug.rhino.tests.TestEventHandler.Subhandler;
import org.eclipse.wst.jsdt.debug.rhino.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.rhino.transport.DisconnectedException;
import org.eclipse.wst.jsdt.debug.rhino.transport.EventPacket;
import org.eclipse.wst.jsdt.debug.rhino.transport.PipedTransportService;
import org.eclipse.wst.jsdt.debug.rhino.transport.Request;
import org.eclipse.wst.jsdt.debug.rhino.transport.Response;
import org.eclipse.wst.jsdt.debug.rhino.transport.TimeoutException;
import org.eclipse.wst.jsdt.debug.rhino.transport.TransportService;
import org.mozilla.javascript.ContextFactory;

/**
 * Abstract test for making requests
 * 
 * @since 1.0
 */
public abstract class RequestTest extends TestCase {
	/**
	 * This default handler handles a script load event and sets a 
	 * breakpoint on every single debuggable line 
	 * 
	 * @since 1.1
	 */
	protected static Subhandler setBreakpointsHandler = new Subhandler() {
		/* (non-Javadoc)
		 * @see org.eclipse.wst.jsdt.debug.rhino.tests.TestEventHandler.Subhandler#handleEvent(org.eclipse.wst.jsdt.debug.rhino.transport.DebugSession, org.eclipse.wst.jsdt.debug.rhino.transport.EventPacket)
		 */
		public boolean handleEvent(DebugSession debugSession, EventPacket event) {
			if (event.getEvent().equals("script")) {
				Object scriptId = event.getBody().get("scriptId");
				Request request = new Request("script");
				request.getArguments().put("scriptId", scriptId);
				try {
					debugSession.sendRequest(request);
					Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
					assertTrue(response.isSuccess());
					Map result = (Map) response.getBody().get("script");

					// line numbers
					List lineNumbers = (List) result.get("lines");
					for (Iterator iterator = lineNumbers.iterator(); iterator.hasNext();) {
						Number lineNumber = (Number) iterator.next();
						request = new Request("setbreakpoint");
						request.getArguments().put("scriptId", scriptId);
						request.getArguments().put("line", lineNumber);
						debugSession.sendRequest(request);
						response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
						assertTrue(response.isSuccess());
					}

					// functions
					List functionNames = (List) result.get("functions");
					for (Iterator iterator = functionNames.iterator(); iterator.hasNext();) {
						String functionName = (String) iterator.next();
						request = new Request("setbreakpoint");
						request.getArguments().put("scriptId", scriptId);
						request.getArguments().put("function", functionName);
						debugSession.sendRequest(request);
						response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
						assertTrue(response.isSuccess());
					}

					// script onEnter
					request = new Request("setbreakpoint");
					request.getArguments().put("scriptId", scriptId);
					debugSession.sendRequest(request);
					response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
					assertTrue(response.isSuccess());
				} catch (DisconnectedException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					e.printStackTrace();
				}
				return true;
			}
			return false;
		}
	};
	
	/**
	 * Default handler that handles a <code>break</code> event and clears the 
	 * associated breakpoint
	 * 
	 * @since 1.1
	 */
	protected static Subhandler clearBreakpointsHandler = new Subhandler() {
		/* (non-Javadoc)
		 * @see org.eclipse.wst.jsdt.debug.rhino.tests.TestEventHandler.Subhandler#handleEvent(org.eclipse.wst.jsdt.debug.rhino.transport.DebugSession, org.eclipse.wst.jsdt.debug.rhino.transport.EventPacket)
		 */
		public boolean handleEvent(DebugSession debugSession, EventPacket event) {
			if (event.getEvent().equals("break")) {
				List breakpoints = (List) event.getBody().get("breakpoints");
				for (Iterator iterator = breakpoints.iterator(); iterator.hasNext();) {
					Number breakpointId = (Number) iterator.next();
					Request request = new Request("clearbreakpoint");
					request.getArguments().put("breakpointId", breakpointId);
					try {
						debugSession.sendRequest(request);
						Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
						assertTrue(response.isSuccess());
					} catch (DisconnectedException e) {
						e.printStackTrace();
					} catch (TimeoutException e) {
						e.printStackTrace();
					}
				}
				return true;
			}
			return false;
		}
	};
	
	protected RhinoDebugger debugger;
	protected DebugSession debugSession;
	protected TestEventHandler eventHandler;
	protected ContextFactory contextFactory;
	private static boolean tracing = false;

	/**
	 * Turns on tracing for the test. All testing event handlers use this.
	 */
	protected void useTracing() {
		tracing = true;
	}
	
	/**
	 * Turns off tracing. This method is called on every {@link #tearDown()} invocation.
	 */
	protected void disableTracing() {
		tracing = false;
	}
	
	/**
	 * Returns if tracing is enabled
	 * 
	 * @return if tracing is enabled
	 */
	public static boolean isTracing() {
		return tracing;
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		TransportService pipedTransport = new PipedTransportService();
		ConnectionHelper helper = new ConnectionHelper(pipedTransport, null);

		debugger = new RhinoDebugger(pipedTransport, null, false);
		debugger.start();

		debugSession = new DebugSession(helper.getClientConnection());
		eventHandler = new TestEventHandler(debugSession, getName());
		eventHandler.start();

		assertTrue(debugger.suspendForRuntime(5000));
		contextFactory = new ContextFactory();
		contextFactory.addListener(debugger);
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		disableTracing();
		super.tearDown();
		contextFactory.removeListener(debugger);
		eventHandler.stop();
		debugger.stop();
		debugSession.dispose();
	}

	/**
	 * Forces the backing event handler to block until the given number of events have been 
	 * received or a timeout occurs.
	 * 
	 * @param count the event count to wait for
	 */
	synchronized void waitForEvents(int count) {
		eventHandler.waitForEvents(count);
	}

	/**
	 * Sets the collection of specific events we are expecting to handle
	 * 
	 * @param events the collection of events
	 */
	synchronized void setExpectedEvents(EventPacket[] events) {
		eventHandler.setExpectedEvents(events);
	}
	
	/**
	 * Forces the backing event handler to block until the given number of events have been 
	 * received or a timeout occurs.
	 * 
	 * @param events the specific set of events expected
	 * @see #setExpectedEvents(EventPacket[])
	 */
	synchronized void waitForEvents(EventPacket[] events) {
		eventHandler.waitForEvents(events);
	}
}
