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
package org.eclipse.wst.jsdt.debug.rhino.tests;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.rhino.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.rhino.transport.DisconnectedException;
import org.eclipse.wst.jsdt.debug.rhino.transport.JSONConstants;
import org.eclipse.wst.jsdt.debug.rhino.transport.Request;
import org.eclipse.wst.jsdt.debug.rhino.transport.Response;
import org.eclipse.wst.jsdt.debug.rhino.transport.TimeoutException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

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
		Request request = new Request(JSONConstants.BREAKPOINT);
		request.getArguments().put(JSONConstants.BREAKPOINT_ID, new Integer("9999"));
		debugSession.sendRequest(request);
		Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertFalse(response.isSuccess());
	}

	
	/**
	 * Tests asking for all breakpoints when none have been set
	 * 
	 * @throws Exception
	 */
	public void testBreakpointsWithNoBreakpoints() throws Exception {
		Request request = new Request(JSONConstants.BREAKPOINTS);
		debugSession.sendRequest(request);
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
		Request request = new Request(JSONConstants.CLEARBREAKPOINT);
		request.getArguments().put(JSONConstants.BREAKPOINT_ID, new Integer("9999"));
		debugSession.sendRequest(request);
		Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertFalse(response.isSuccess());
	}

	/**
	 * Tests setting a breakpoint on every valid line of the test script and verifying they exist
	 * 
	 * @throws Exception
	 */
	public void testBreakpoints() throws Exception {
		eventHandler.addSubhandler(setBreakpointsHandler);
		Request request = new Request(JSONConstants.BREAKPOINTS);
		debugSession.sendRequest(request);
		Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertTrue(response.isSuccess());
		Collection breakpoints = (Collection) response.getBody().get(JSONConstants.BREAKPOINTS);
		assertNotNull(breakpoints);
		assertTrue(breakpoints.isEmpty());

		String script = Util.getTestSource(Util.SRC_SCRIPTS_CONTAINER, "script1.js");
		assertNotNull("The test source for [script1.js] must exist", script);
		
		Scriptable scope = null;
		Context context = contextFactory.enterContext();
		try {
			scope = context.initStandardObjects();
			context.evaluateString(scope, script, "script", 0, null);
		} finally {
			Context.exit();
		}

		waitForEvents(1);

		request = new Request(JSONConstants.BREAKPOINTS);
		debugSession.sendRequest(request);
		response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertTrue(response.isSuccess());
		breakpoints = (Collection) response.getBody().get(JSONConstants.BREAKPOINTS);
		assertNotNull(breakpoints);
		assertEquals(7, breakpoints.size());

		for (Iterator iterator = breakpoints.iterator(); iterator.hasNext();) {
			Number breakpointId = (Number) iterator.next();
			request = new Request(JSONConstants.BREAKPOINT);
			request.getArguments().put(JSONConstants.BREAKPOINT_ID, breakpointId);
			debugSession.sendRequest(request);
			response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
			assertTrue(response.isSuccess());
			Map breakpoint = (Map) response.getBody().get(JSONConstants.BREAKPOINT);
			assertEquals(breakpointId.intValue(), Util.numberAsInt(breakpoint.get(JSONConstants.BREAKPOINT_ID)));
			assertTrue(breakpoint.containsKey(JSONConstants.SCRIPT_ID));
		}
	}
	
	/**
	 * Tests setting breakpoints and removing them
	 * @throws Exception
	 */
	public void testGetSetClearBreakpoint() throws Exception {
		eventHandler.addSubhandler(setBreakpointsHandler);
		eventHandler.addSubhandler(clearBreakpointsHandler);

		Request request = new Request(JSONConstants.BREAKPOINTS);
		debugSession.sendRequest(request);
		Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertTrue(response.isSuccess());
		Collection breakpoints = (Collection) response.getBody().get(JSONConstants.BREAKPOINTS);
		assertNotNull(breakpoints);
		assertTrue(breakpoints.isEmpty());

		String script = Util.getTestSource(Util.SRC_SCRIPTS_CONTAINER, "script1.js");
		assertNotNull("The test source for [script1.js] must exist", script);
		
		Scriptable scope = null;
		Context context = contextFactory.enterContext();
		try {
			scope = context.initStandardObjects();
			context.evaluateString(scope, script, "script", 0, null);
		} finally {
			Context.exit();
		}
		waitForEvents(6);
		request = new Request(JSONConstants.BREAKPOINTS);
		debugSession.sendRequest(request);
		response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertTrue(response.isSuccess());
		breakpoints = (Collection) response.getBody().get(JSONConstants.BREAKPOINTS);
		assertNotNull(breakpoints);
		assertEquals(2, breakpoints.size());

		for (Iterator iterator = breakpoints.iterator(); iterator.hasNext();) {
			Number breakpointId = (Number) iterator.next();
			request = new Request(JSONConstants.BREAKPOINT);
			request.getArguments().put(JSONConstants.BREAKPOINT_ID, breakpointId);
			debugSession.sendRequest(request);
			response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
			assertTrue(response.isSuccess());
			Map breakpoint = (Map) response.getBody().get(JSONConstants.BREAKPOINT);
			assertEquals(breakpointId.intValue(), Util.numberAsInt(breakpoint.get(JSONConstants.BREAKPOINT_ID)));
			assertTrue(breakpoint.containsKey(JSONConstants.SCRIPT_ID));
			deleteBreakpoint(debugSession, breakpointId);
		}
	}
	
	void deleteBreakpoint(DebugSession session, Number breakpointid) {
		Request request = new Request(JSONConstants.CLEARBREAKPOINT);
		request.getArguments().put(JSONConstants.BREAKPOINT_ID, breakpointid);
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
}
