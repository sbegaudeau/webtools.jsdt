/*******************************************************************************
 * Copyright (c) 2010, 2012 IBM Corporation and others.
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
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.JSONConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.RhinoRequest;
import org.eclipse.wst.jsdt.debug.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.transport.exception.DisconnectedException;
import org.eclipse.wst.jsdt.debug.transport.exception.TimeoutException;
import org.eclipse.wst.jsdt.debug.transport.packet.Response;

/**
 * Variety of tests for requesting breakpoint(s)
 * 
 * @since 1.1
 */
public class BreakpointRequestTests extends RequestTest {

	/**
	 * Tests that asking for an invalid breakpoint returns an unsuccessful response
	 * 
	 * @throws Exception
	 */
	public void testInvalidBreakpoint() throws Exception {
		RhinoRequest request = new RhinoRequest(JSONConstants.BREAKPOINT);
		request.getArguments().put(JSONConstants.BREAKPOINT_ID, new Integer("9999")); //$NON-NLS-1$
		debugSession.send(request);
		Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertFalse(response.isSuccess());
	}

	
	/**
	 * Tests asking for all breakpoints when none have been set
	 * 
	 * @throws Exception
	 */
	public void testBreakpointsWithNoBreakpoints() throws Exception {
		RhinoRequest request = new RhinoRequest(JSONConstants.BREAKPOINTS);
		debugSession.send(request);
		Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertTrue(response.isSuccess());
		Collection breakpoints = (Collection) response.getBody().get(JSONConstants.BREAKPOINTS);
		assertNotNull(breakpoints);
		assertTrue(breakpoints.isEmpty());
	}

	/**
	 * Tests trying to clear an invalid breakpoint id
	 * @throws Exception
	 */
	public void testClearInvalidBreakpoint() throws Exception {
		RhinoRequest request = new RhinoRequest(JSONConstants.CLEARBREAKPOINT);
		request.getArguments().put(JSONConstants.BREAKPOINT_ID, new Integer("9999")); //$NON-NLS-1$
		debugSession.send(request);
		Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertFalse(response.isSuccess());
	}

	/**
	 * Tests setting a breakpoint on every valid line of the test script and verifying they exist
	 * 
	 * @throws Exception
	 */
	public void testBreakpoints() throws Exception {
		eventHandler.addSubhandler(new SetBreakpointsHandler());
		
		RhinoRequest request = new RhinoRequest(JSONConstants.BREAKPOINTS);
		debugSession.send(request);
		Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertTrue(response.isSuccess());
		Collection breakpoints = (Collection) response.getBody().get(JSONConstants.BREAKPOINTS);
		assertNotNull(breakpoints);
		assertTrue(breakpoints.isEmpty());

		String script = Util.getTestSource(Util.SRC_SCRIPTS_CONTAINER, "script1.js"); //$NON-NLS-1$
		assertNotNull("The test source for [script1.js] must exist", script); //$NON-NLS-1$

		evalScript(script, 1);

		request = new RhinoRequest(JSONConstants.BREAKPOINTS);
		debugSession.send(request);
		response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertTrue(response.isSuccess());
		breakpoints = (Collection) response.getBody().get(JSONConstants.BREAKPOINTS);
		assertNotNull(breakpoints);
		assertEquals(8, breakpoints.size());

		for (Iterator iterator = breakpoints.iterator(); iterator.hasNext();) {
			Number breakpointId = (Number) iterator.next();
			request = new RhinoRequest(JSONConstants.BREAKPOINT);
			request.getArguments().put(JSONConstants.BREAKPOINT_ID, breakpointId);
			debugSession.send(request);
			response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
			assertTrue(response.isSuccess());
			Map breakpoint = (Map) response.getBody().get(JSONConstants.BREAKPOINT);
			assertEquals(breakpointId.intValue(), Util.numberAsInt(breakpoint.get(JSONConstants.BREAKPOINT_ID)));
			assertTrue(breakpoint.containsKey(JSONConstants.SCRIPT_ID));
			deleteBreakpoint(debugSession, breakpointId);
		}
	}
	
	/**
	 * Tests setting breakpoints and removing them
	 * @throws Exception
	 */
	public void testGetSetClearBreakpoint() throws Exception {
		eventHandler.addSubhandler(new SetBreakpointsHandler());
		eventHandler.addSubhandler(new ClearBreakpointsHandler());

		RhinoRequest request = new RhinoRequest(JSONConstants.BREAKPOINTS);
		debugSession.send(request);
		Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertTrue(response.isSuccess());
		Collection breakpoints = (Collection) response.getBody().get(JSONConstants.BREAKPOINTS);
		assertNotNull(breakpoints);
		assertTrue(breakpoints.isEmpty());

		String script = Util.getTestSource(Util.SRC_SCRIPTS_CONTAINER, "script1.js"); //$NON-NLS-1$
		assertNotNull("The test source for [script1.js] must exist", script); //$NON-NLS-1$
		evalScript(script, 7);
		
		request = new RhinoRequest(JSONConstants.BREAKPOINTS);
		debugSession.send(request);
		response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertTrue(response.isSuccess());
		breakpoints = (Collection) response.getBody().get(JSONConstants.BREAKPOINTS);
		assertNotNull(breakpoints);
		assertEquals(2, breakpoints.size());

		for (Iterator iterator = breakpoints.iterator(); iterator.hasNext();) {
			Number breakpointId = (Number) iterator.next();
			request = new RhinoRequest(JSONConstants.BREAKPOINT);
			request.getArguments().put(JSONConstants.BREAKPOINT_ID, breakpointId);
			debugSession.send(request);
			response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
			assertTrue(response.isSuccess());
			Map breakpoint = (Map) response.getBody().get(JSONConstants.BREAKPOINT);
			assertEquals(breakpointId.intValue(), Util.numberAsInt(breakpoint.get(JSONConstants.BREAKPOINT_ID)));
			assertTrue(breakpoint.containsKey(JSONConstants.SCRIPT_ID));
			deleteBreakpoint(debugSession, breakpointId);
		}
	}
	
	/**
	 * Deletes the breakpoint with the given id from the given {@link DebugSession}
	 * @param session
	 * @param breakpointid
	 */
	void deleteBreakpoint(DebugSession session, Number breakpointid) {
		RhinoRequest request = new RhinoRequest(JSONConstants.CLEARBREAKPOINT);
		request.getArguments().put(JSONConstants.BREAKPOINT_ID, breakpointid);
		try {
			debugSession.send(request);
			Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
			assertTrue(response.isSuccess());
		} catch (DisconnectedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
}
