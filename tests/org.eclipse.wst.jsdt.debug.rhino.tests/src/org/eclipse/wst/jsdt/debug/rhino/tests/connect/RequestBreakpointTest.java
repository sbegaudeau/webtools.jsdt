/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.rhino.tests.connect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.connect.DisconnectedException;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.TimeoutException;
import org.eclipse.wst.jsdt.debug.core.jsdi.json.JSONConstants;
import org.eclipse.wst.jsdt.debug.internal.core.jsdi.connect.DebugSession;
import org.eclipse.wst.jsdt.debug.internal.core.jsdi.connect.EventPacket;
import org.eclipse.wst.jsdt.debug.internal.core.jsdi.connect.Request;
import org.eclipse.wst.jsdt.debug.internal.core.jsdi.connect.Response;
import org.eclipse.wst.jsdt.debug.internal.rhino.bundles.RhinoClassLoader;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSBundleException;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSConstants;
import org.eclipse.wst.jsdt.debug.rhino.tests.connect.TestEventHandler.Subhandler;
import org.osgi.framework.Constants;

public class RequestBreakpointTest extends RequestTest {

	/**
	 * Tests that asking for an invalid breakpoint returns an unsuccessful response
	 * 
	 * @throws DisconnectedException
	 * @throws TimeoutException
	 */
	public void testInvalidBreakpoint() throws DisconnectedException, TimeoutException {
		Request request = new Request(JSONConstants.BREAKPOINT);
		request.getArguments().put(JSONConstants.BREAKPOINT_ID, new Integer("9999"));
		debugSession.sendRequest(request);
		Response response = debugSession.receiveResponse(request.getSequence(), 30000);
		assertFalse(response.isSuccess());
	}

	public void testGetSetClearBreakpoint() throws DisconnectedException, TimeoutException, JSBundleException {

		Subhandler setbreakpointHandler = new Subhandler() {
			public boolean handleEvent(DebugSession debugSession, EventPacket event) {
				if (event.getEvent().equals(JSONConstants.SCRIPT)) {
					Number scriptId = (Number) event.getBody().get(JSONConstants.SCRIPT_ID);
					Request request = new Request(JSONConstants.SCRIPT);
					request.getArguments().put(JSONConstants.SCRIPT_ID, scriptId);
					try {
						debugSession.sendRequest(request);
						Response response = debugSession.receiveResponse(request.getSequence(), 10000);
						assertTrue(response.isSuccess());
						Map result = (Map) response.getBody().get(JSONConstants.SCRIPT);

						// line numbers
						List lineNumbers = (List) result.get(JSONConstants.LINES);
						for (Iterator iterator = lineNumbers.iterator(); iterator.hasNext();) {
							Number lineNumber = (Number) iterator.next();
							request = new Request(JSONConstants.SETBREAKPOINT);
							request.getArguments().put(JSONConstants.SCRIPT_ID, scriptId);
							request.getArguments().put(JSONConstants.LINE, lineNumber);
							debugSession.sendRequest(request);
							response = debugSession.receiveResponse(request.getSequence(), 10000);
							assertTrue(response.isSuccess());

							Map breakpoint = (Map) response.getBody().get(JSONConstants.BREAKPOINT);
							Number breakpointId = (Number) breakpoint.get(JSONConstants.BREAKPOINT_ID);
							request = new Request(JSONConstants.BREAKPOINT);
							request.getArguments().put(JSONConstants.BREAKPOINT_ID, breakpointId);
							debugSession.sendRequest(request);
							response = debugSession.receiveResponse(request.getSequence(), 10000);
							assertTrue(response.isSuccess());
							breakpoint = (Map) response.getBody().get(JSONConstants.BREAKPOINT);
							assertEquals(breakpointId.intValue(), Util.numberAsInt(breakpoint.get(JSONConstants.BREAKPOINT_ID)));
							assertEquals(scriptId.intValue(), Util.numberAsInt(breakpoint.get(JSONConstants.SCRIPT_ID)));
							assertEquals(lineNumber.intValue(), Util.numberAsInt(breakpoint.get(JSONConstants.LINE)));
							assertNull(breakpoint.get(JSONConstants.FUNCTION));
						}

						// functions
						List functionNames = (List) result.get(JSONConstants.FUNCTIONS);
						for (Iterator iterator = functionNames.iterator(); iterator.hasNext();) {
							String functionName = (String) iterator.next();
							request = new Request(JSONConstants.SETBREAKPOINT);
							request.getArguments().put(JSONConstants.SCRIPT_ID, scriptId);
							request.getArguments().put(JSONConstants.FUNCTION, functionName);
							request.getArguments().put(JSONConstants.CONDITION, "1===1");
							debugSession.sendRequest(request);
							response = debugSession.receiveResponse(request.getSequence(), 10000);
							assertTrue(response.isSuccess());

							Map breakpoint = (Map) response.getBody().get(JSONConstants.BREAKPOINT);
							Number breakpointId = (Number) breakpoint.get(JSONConstants.BREAKPOINT_ID);
							request = new Request(JSONConstants.BREAKPOINT);
							request.getArguments().put(JSONConstants.BREAKPOINT_ID, breakpointId);
							debugSession.sendRequest(request);
							response = debugSession.receiveResponse(request.getSequence(), 10000);
							assertTrue(response.isSuccess());
							breakpoint = (Map) response.getBody().get(JSONConstants.BREAKPOINT);
							assertEquals(breakpointId.intValue(), Util.numberAsInt(breakpoint.get(JSONConstants.BREAKPOINT_ID)));
							assertEquals(scriptId.intValue(), Util.numberAsInt(breakpoint.get(JSONConstants.SCRIPT_ID)));
							assertEquals(functionName, breakpoint.get(JSONConstants.FUNCTION));
							assertEquals("1===1", breakpoint.get(JSONConstants.CONDITION));
							assertNull(breakpoint.get(JSONConstants.LINE));
						}

						// script onEnter
						request = new Request(JSONConstants.SETBREAKPOINT);
						request.getArguments().put(JSONConstants.SCRIPT_ID, scriptId);
						debugSession.sendRequest(request);
						response = debugSession.receiveResponse(request.getSequence(), 10000);
						assertTrue(response.isSuccess());

						Map breakpoint = (Map) response.getBody().get(JSONConstants.BREAKPOINT);
						Number breakpointId = (Number) breakpoint.get(JSONConstants.BREAKPOINT_ID);
						request = new Request(JSONConstants.BREAKPOINT);
						request.getArguments().put(JSONConstants.BREAKPOINT_ID, breakpointId);
						debugSession.sendRequest(request);
						response = debugSession.receiveResponse(request.getSequence(), 10000);
						assertTrue(response.isSuccess());
						breakpoint = (Map) response.getBody().get(JSONConstants.BREAKPOINT);
						assertEquals(breakpointId.intValue(), Util.numberAsInt(breakpoint.get(JSONConstants.BREAKPOINT_ID)));
						assertEquals(scriptId.intValue(), Util.numberAsInt(breakpoint.get(JSONConstants.SCRIPT_ID)));
						assertNull(breakpoint.get(JSONConstants.LINE));
						assertNull(breakpoint.get(JSONConstants.FUNCTION));
					} catch (DisconnectedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TimeoutException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return true;
				}
				return false;
			}
		};
		eventHandler.addSubhandler(setbreakpointHandler);

		Subhandler clearbreakpointHandler = new Subhandler() {
			public boolean handleEvent(DebugSession debugSession, EventPacket event) {
				if (event.getEvent().equals(JSONConstants.BREAK)) {
					List breakpoints = (List) event.getBody().get(JSONConstants.BREAKPOINTS);
					for (Iterator iterator = breakpoints.iterator(); iterator.hasNext();) {
						Number breakpointId = (Number) iterator.next();
						Request request = new Request(JSONConstants.CLEARBREAKPOINT);
						request.getArguments().put(JSONConstants.BREAKPOINT_ID, breakpointId);
						try {
							debugSession.sendRequest(request);
							Response response = debugSession.receiveResponse(request.getSequence(), 10000);
							assertTrue(response.isSuccess());

							Map breakpoint = (Map) response.getBody().get(JSONConstants.BREAKPOINT);
							breakpointId = (Number) breakpoint.get(JSONConstants.BREAKPOINT_ID);
							request = new Request(JSONConstants.BREAKPOINT);
							request.getArguments().put(JSONConstants.BREAKPOINT_ID, breakpointId);
							debugSession.sendRequest(request);
							response = debugSession.receiveResponse(request.getSequence(), 10000);
							assertFalse(response.isSuccess());
						} catch (DisconnectedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (TimeoutException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return true;
				}
				return false;
			}
		};
		eventHandler.addSubhandler(clearbreakpointHandler);

		Request request = new Request(JSONConstants.BREAKPOINTS);
		debugSession.sendRequest(request);
		Response response = debugSession.receiveResponse(request.getSequence(), 30000);
		assertTrue(response.isSuccess());
		Collection breakpoints = (Collection) response.getBody().get(JSONConstants.BREAKPOINTS);
		assertNotNull(breakpoints);
		assertTrue(breakpoints.isEmpty());

		Map headers = new HashMap();
		headers.put(Constants.BUNDLE_SYMBOLICNAME, "test");
		String script = "var line1 = true;\r\n";
		script += "function test() { // line 2 \r\n";
		script += " return \"line 3\";\r\n";
		script += "} // line 4 \r\n";
		script += "// line 5\r\n";
		script += "var line6 = test();\r\n";
		script += "var line7 = test();\r\n";
		script += "// line 8\r\n";
		script += "// line 9\r\n";
		script += "var line10 = test();\r\n";
		script += "// line 11\r\n";
		script += "// line 12\r\n";
		script += "function test2() { // line 13 \r\n";
		script += " return \"line 14\";\r\n";
		script += "} // line 15 \r\n";
		script += "// line 16\r\n";
		script += "// line 17\r\n";

		headers.put(JSConstants.BUNDLE_SCRIPT, script);

		framework.installBundle("testloc", headers, new RhinoClassLoader(Activator.getBundleContext().getBundle()));
		framework.resolve();
		waitForEvents(7);
		request = new Request(JSONConstants.BREAKPOINTS);
		debugSession.sendRequest(request);
		response = debugSession.receiveResponse(request.getSequence(), 30000);
		assertTrue(response.isSuccess());
		breakpoints = (Collection) response.getBody().get(JSONConstants.BREAKPOINTS);
		assertNotNull(breakpoints);
		assertEquals(3, breakpoints.size());

		for (Iterator iterator = breakpoints.iterator(); iterator.hasNext();) {
			Number breakpointId = (Number) iterator.next();
			request = new Request(JSONConstants.BREAKPOINT);
			request.getArguments().put(JSONConstants.BREAKPOINT_ID, breakpointId);
			debugSession.sendRequest(request);
			response = debugSession.receiveResponse(request.getSequence(), 10000);
			assertTrue(response.isSuccess());
			Map breakpoint = (Map) response.getBody().get(JSONConstants.BREAKPOINT);
			assertEquals(breakpointId.intValue(), Util.numberAsInt(breakpoint.get(JSONConstants.BREAKPOINT_ID)));
			assertTrue(breakpoint.containsKey(JSONConstants.SCRIPT_ID));
		}
	}
}
