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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.rhino.transport.JSONConstants;
import org.eclipse.wst.jsdt.debug.rhino.transport.Request;
import org.eclipse.wst.jsdt.debug.rhino.transport.Response;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Tests setting breakpoints and making sure we get the break events for them when evaluating the script
 * 
 * @since 1.0
 */
public class RequestSetBreakpointTest extends RequestTest {

	/**
	 * Tests asking for the current set of breakpoints when the session has none
	 * 
	 * @throws Exception if the test fails
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
	 * Tests setting a variety of breakpoints and that they are hit
	 * 
	 * @throws Exception if the test fails
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
		assertNotNull("The test source for [script1.js] should exist", script);
		Context context = contextFactory.enterContext();
		try {
			Scriptable scope = context.initStandardObjects();
			context.evaluateString(scope, script, JSONConstants.SCRIPT, 0, null);
		} finally {
			Context.exit();
		}
		waitForEvents(8);
		
		request = new Request(JSONConstants.BREAKPOINTS);
		debugSession.sendRequest(request);
		response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertTrue(response.isSuccess());
		breakpoints = (Collection) response.getBody().get(JSONConstants.BREAKPOINTS);
		assertNotNull(breakpoints);
		assertEquals(3, breakpoints.size());

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
}
