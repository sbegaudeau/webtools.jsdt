/*******************************************************************************
 * Copyright (c) 2009, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.rhino.tests;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.rhino.debugger.RhinoDebuggerImpl;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.EventPacket;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.JSONConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.PipedTransportService;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.RhinoRequest;
import org.eclipse.wst.jsdt.debug.rhino.tests.TestEventHandler.Subhandler;
import org.eclipse.wst.jsdt.debug.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.transport.TransportService;
import org.eclipse.wst.jsdt.debug.transport.exception.DisconnectedException;
import org.eclipse.wst.jsdt.debug.transport.exception.TimeoutException;
import org.eclipse.wst.jsdt.debug.transport.packet.Response;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;

/**
 * Abstract test for making requests
 * 
 * @since 1.0
 */
public abstract class RequestTest extends TestCase {
	
	/**
	 * Default implementation of a {@link Subhandler}
	 */
	abstract class SubHandler implements Subhandler {

		private String testname = null;
		
		public SubHandler(String testname) {
			assertNotNull("The test name cannot be null", testname); //$NON-NLS-1$
			this.testname = testname;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.wst.jsdt.debug.rhino.tests.TestEventHandler.Subhandler#testName()
		 */
		public String testName() {
			return this.testname;
		}
	}
	
	/**
	 * Handler to check that we can get frames from a suspended thread
	 * @since 1.1
	 */
	final class FrameCheckHandler extends SubHandler {
		/**
		 * Constructor
		 * @param testname
		 */
		public FrameCheckHandler() {
			super(getName());
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.wst.jsdt.debug.rhino.tests.TestEventHandler.Subhandler#handleEvent(org.eclipse.wst.jsdt.debug.transport.DebugSession, org.eclipse.wst.jsdt.debug.internal.rhino.transport.EventPacket)
		 */
		public boolean handleEvent(DebugSession debugSession, EventPacket event) {
			if (event.getEvent().equals(JSONConstants.BREAK)) {
				Number threadId = (Number) event.getBody().get(JSONConstants.THREAD_ID);
				Number contextId = (Number) event.getBody().get(JSONConstants.CONTEXT_ID);
				RhinoRequest request = new RhinoRequest(JSONConstants.FRAMES);
				request.getArguments().put(JSONConstants.THREAD_ID, threadId);
				try {
					debugSession.send(request);
					Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
					assertTrue(testName()+": the request for frames from thread ["+threadId.intValue()+"] was not successful", response.isSuccess()); //$NON-NLS-1$ //$NON-NLS-2$
					Collection frames = (Collection) response.getBody().get(JSONConstants.FRAMES);
					for (Iterator iterator = frames.iterator(); iterator.hasNext();) {
						Number frameId = (Number) iterator.next();
						request = new RhinoRequest(JSONConstants.FRAME);
						request.getArguments().put(JSONConstants.THREAD_ID, threadId);
						request.getArguments().put(JSONConstants.CONTEXT_ID, contextId);
						request.getArguments().put(JSONConstants.FRAME_ID, frameId);
						debugSession.send(request);
						response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
						assertTrue(testName()+": the request for frame ["+frameId.intValue()+"] frmo thread ["+threadId.intValue()+"] was not successful", response.isSuccess()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					}
				} catch (DisconnectedException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					e.printStackTrace();
				}
				return true;
			}
			return false;
		}
		
	}
	
	/**
	 * Handler to check that the thread returned when suspended is the same thread
	 * from the break event
	 * @since 1.1
	 */
	final class ThreadCheckHandler extends SubHandler {
		/**
		 * Constructor
		 */
		public ThreadCheckHandler() {
			super(getName());
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.wst.jsdt.debug.rhino.tests.TestEventHandler.Subhandler#handleEvent(org.eclipse.wst.jsdt.debug.transport.DebugSession, org.eclipse.wst.jsdt.debug.internal.rhino.transport.EventPacket)
		 */
		public boolean handleEvent(DebugSession debugSession, EventPacket event) {
			if (event.getEvent().equals(JSONConstants.BREAK)) {
				Number threadId = (Number) event.getBody().get(JSONConstants.THREAD_ID);
				RhinoRequest request = new RhinoRequest(JSONConstants.THREADS);
				try {
					debugSession.send(request);
					Response response = debugSession.receiveResponse(request.getSequence(), 10000);
					assertTrue(response.isSuccess());
					List threads = (List) response.getBody().get(JSONConstants.THREADS);
					assertTrue(testName()+": the listing of threads must not be empty", threads.size() > 0); //$NON-NLS-1$
					assertEquals(testName()+": the thread ids do not match", threadId.intValue(), Util.numberAsInt(threads.get(0))); //$NON-NLS-1$
				} catch (DisconnectedException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					e.printStackTrace();
				}
				return true;
			}
			return false;
		}
	}
	
	/**
	 * Handler for setting breakpoints on all executable lines in a loaded script
	 * @since 1.1
	 */
	final class SetBreakpointsHandler extends SubHandler {
		/**
		 * Constructor
		 */
		public SetBreakpointsHandler() {
			super(getName());
		}

		/* (non-Javadoc)
		 * @see org.eclipse.wst.jsdt.debug.rhino.tests.TestEventHandler.Subhandler#handleEvent(org.eclipse.wst.jsdt.debug.transport.DebugSession, org.eclipse.wst.jsdt.debug.internal.rhino.transport.EventPacket)
		 */
		public boolean handleEvent(DebugSession debugSession, EventPacket event) {
			if (event.getEvent().equals(JSONConstants.SCRIPT)) {
				Number scriptId = (Number) event.getBody().get(JSONConstants.SCRIPT_ID);
				RhinoRequest request = new RhinoRequest(JSONConstants.SCRIPT);
				request.getArguments().put(JSONConstants.SCRIPT_ID, scriptId);
				try {
					debugSession.send(request);
					Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
					assertTrue(response.isSuccess());
					Map result = (Map) response.getBody().get(JSONConstants.SCRIPT);

					// line numbers
					List lineNumbers = (List) result.get(JSONConstants.LINES);
					for (Iterator iterator = lineNumbers.iterator(); iterator.hasNext();) {
						Number lineNumber = (Number) iterator.next();
						request = new RhinoRequest(JSONConstants.SETBREAKPOINT);
						request.getArguments().put(JSONConstants.SCRIPT_ID, scriptId);
						request.getArguments().put(JSONConstants.LINE, lineNumber);
						request.getArguments().put(JSONConstants.CONDITION, "1===1"); //$NON-NLS-1$
						debugSession.send(request);
						response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
						assertTrue(testName()+": the request to set a breakpoint on line ["+lineNumber+"] was not successful", response.isSuccess()); //$NON-NLS-1$ //$NON-NLS-2$
						
						Map breakpoint = (Map) response.getBody().get(JSONConstants.BREAKPOINT);
						Number breakpointId = (Number) breakpoint.get(JSONConstants.BREAKPOINT_ID);
						request = new RhinoRequest(JSONConstants.BREAKPOINT);
						request.getArguments().put(JSONConstants.BREAKPOINT_ID, breakpointId);
						debugSession.send(request);
						response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
						assertTrue(response.isSuccess());
						breakpoint = (Map) response.getBody().get(JSONConstants.BREAKPOINT);
						assertEquals(breakpointId.intValue(), Util.numberAsInt(breakpoint.get(JSONConstants.BREAKPOINT_ID)));
						assertEquals(scriptId.intValue(), Util.numberAsInt(breakpoint.get(JSONConstants.SCRIPT_ID)));
						assertEquals(lineNumber.intValue(), Util.numberAsInt(breakpoint.get(JSONConstants.LINE)));
						assertEquals("1===1", breakpoint.get(JSONConstants.CONDITION)); //$NON-NLS-1$
					}
				} catch (DisconnectedException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					e.printStackTrace();
				}
				return true;
			}
			return false;
		}
	}

	/**
	 * Handler to set breakpoints on a given set of lines. The lines are not checked
	 * to see if they are valid.
	 * 
	 * @since 1.1
	 */
	final class SetBreakpointHandler extends SubHandler {
		int[] adds = null;
		/**
		 * Constructor
		 * @param lines <code>null</code> is not accepted, not is an empty array
		 */
		public SetBreakpointHandler(int[] lines) {
			super(getName());
			assertNotNull(testName()+": no line numbers have been specified to set breakpoints on", lines); //$NON-NLS-1$
			assertTrue(testName()+": no line numbers have been specified to set breakpoints on", lines.length > 0); //$NON-NLS-1$
			this.adds = lines;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.wst.jsdt.debug.rhino.tests.TestEventHandler.Subhandler#handleEvent(org.eclipse.wst.jsdt.debug.transport.DebugSession, org.eclipse.wst.jsdt.debug.internal.rhino.transport.EventPacket)
		 */
		public boolean handleEvent(DebugSession debugSession, EventPacket event) {
			if (event.getEvent().equals(JSONConstants.SCRIPT)) {
				Number scriptId = (Number) event.getBody().get(JSONConstants.SCRIPT_ID);
				RhinoRequest request = new RhinoRequest(JSONConstants.SCRIPT);
				request.getArguments().put(JSONConstants.SCRIPT_ID, scriptId);
				try {
					debugSession.send(request);
					Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
					assertTrue(response.isSuccess());
					Map script = (Map) response.getBody().get(JSONConstants.SCRIPT);
					assertNotNull(testName()+": the response body cannot be null", script); //$NON-NLS-1$
					for (int i = 0; i < adds.length; i++) {
						request = new RhinoRequest(JSONConstants.SETBREAKPOINT);
						request.getArguments().put(JSONConstants.SCRIPT_ID, scriptId);
						request.getArguments().put(JSONConstants.LINE, new Integer(adds[i]));
						debugSession.send(request);
						response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
						assertTrue(testName()+": the request to set a breakpoint on line ["+adds[i]+"] was not successful", response.isSuccess()); //$NON-NLS-1$ //$NON-NLS-2$
					}					
				} catch (DisconnectedException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					e.printStackTrace();
				}
				return true;
			}
			return false;
		}
	}

	/**
	 * Clears all of the breakpoints that are hit
	 * 
	 * @since 1.1
	 */
	final class ClearBreakpointsHandler extends SubHandler {
		/**
		 * Constructor
		 */
		public ClearBreakpointsHandler() {
			super(getName());
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.wst.jsdt.debug.rhino.tests.TestEventHandler.Subhandler#handleEvent(org.eclipse.wst.jsdt.debug.transport.DebugSession, org.eclipse.wst.jsdt.debug.internal.rhino.transport.EventPacket)
		 */
		public boolean handleEvent(DebugSession debugSession, EventPacket event) {
			if (event.getEvent().equals(JSONConstants.BREAK)) {
				Number bid = (Number)event.getBody().get(JSONConstants.BREAKPOINT);
				if(bid == null && JSONConstants.STEP_OUT.equals(event.getBody().get(JSONConstants.STEP))) {
					return false;
				}
				RhinoRequest request = new RhinoRequest(JSONConstants.CLEARBREAKPOINT);
				request.getArguments().put(JSONConstants.BREAKPOINT_ID, bid);
				try {
					debugSession.send(request);
					Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
					assertTrue(testName()+": the request to clear breakpoint ["+bid+"] should have succeeded", response.isSuccess()); //$NON-NLS-1$ //$NON-NLS-2$
					return true;
				} catch (DisconnectedException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					e.printStackTrace();
				}
			}
			return false;
		}
	}
	
	protected RhinoDebuggerImpl debugger;
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
	
	/**
	 * Evaluates the given script source
	 * @param script
	 * @since 1.1
	 */
	protected void evalScript(String script) {
		Scriptable scope = null;
		Context context = contextFactory.enterContext();
		try {
			scope = context.initStandardObjects();
			context.evaluateString(scope, script, JSONConstants.SCRIPT, 0, null);
		} finally {
			Context.exit();
		}
	}
	
	/**
	 * Evaluates the given script source and waits for the given number of events
	 * @param script
	 * @param eventcount
	 * @since 1.1
	 */
	protected void evalScript(String script, int eventcount) {
		evalScript(script);
		waitForEvents(eventcount);
	}
	
	/**
	 * Evaluates the given script and waits for the specific events to be received
	 * @param script
	 * @param events
	 * @since 1.1
	 */
	protected void evalScript(String script, EventPacket[] events) {
		evalScript(script);
		waitForEvents(events);
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		TransportService pipedTransport = new PipedTransportService();
		ConnectionHelper helper = new ConnectionHelper(pipedTransport, null);

		debugger = new RhinoDebuggerImpl(pipedTransport, null, false, false);
		debugger.start();

		debugSession = new DebugSession(helper.getClientConnection());
		eventHandler = new TestEventHandler(debugSession, getName());
		eventHandler.start();

		assertTrue(suspendForRuntime(debugger, 100));
		contextFactory = new ContextFactory();
		contextFactory.addListener(debugger);
		super.setUp();
	}

	/**
	 * Suspend waiting for the debugger to have a connected session
	 * 
	 * @param debug the debugger to poll for a session connection
	 * @param timeout the amount of time to wait
	 * @return true when a {@link DebugSession} has connected
	 */
	public synchronized boolean suspendForRuntime(RhinoDebuggerImpl debug, long timeout) {
		while (!debug.isConnected())
			try {
				wait(timeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return debug.isConnected();
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
	
	/**
	 * @param props
	 * @param varname
	 * @return the variable map or <code>null</code>
	 */
	protected Map findVar(List props, String varname) {
		if(props != null && varname != null) {
			Map prop = null;
			for (Iterator i = props.iterator(); i.hasNext();) {
				prop = (Map) i.next();
				if(varname.equals(prop.get(JSONConstants.NAME))) {
					return prop;
				}
			}
		}
		return null;
	}
	
	/**
	 * @param session
	 * @param threadid
	 * @param contextid
	 * @param frameid
	 * @param refid
	 * @return the response, never <code>null</code>
	 */
	protected Response doLookup(DebugSession session, Number threadid, Number contextid, Number frameid, Number refid) throws Exception {
		RhinoRequest request = new RhinoRequest(JSONConstants.LOOKUP);
		request.getArguments().put(JSONConstants.THREAD_ID, threadid);
		request.getArguments().put(JSONConstants.CONTEXT_ID, contextid);
		request.getArguments().put(JSONConstants.FRAME_ID, frameid);
		request.getArguments().put(JSONConstants.REF, refid);
		debugSession.send(request);
		return debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
	}
}
