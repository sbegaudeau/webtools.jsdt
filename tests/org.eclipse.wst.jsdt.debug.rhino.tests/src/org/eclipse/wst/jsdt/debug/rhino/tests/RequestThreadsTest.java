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
package org.eclipse.wst.jsdt.debug.rhino.tests;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.rhino.tests.TestEventHandler.Subhandler;
import org.eclipse.wst.jsdt.debug.rhino.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.rhino.transport.DisconnectedException;
import org.eclipse.wst.jsdt.debug.rhino.transport.EventPacket;
import org.eclipse.wst.jsdt.debug.rhino.transport.Request;
import org.eclipse.wst.jsdt.debug.rhino.transport.Response;
import org.eclipse.wst.jsdt.debug.rhino.transport.TimeoutException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.osgi.framework.Constants;

public class RequestThreadsTest extends RequestTest {

	public void testThreadsWithNoThreads() throws DisconnectedException, TimeoutException {
		Request request = new Request("threads");
		debugSession.sendRequest(request);
		Response response = debugSession.receiveResponse(request.getSequence(), 30000);
		assertTrue(response.isSuccess());
		Collection threads = (Collection) response.getBody().get("threads");
		assertNotNull(threads);
		assertTrue(threads.isEmpty());
	}

	public void testThreads() throws DisconnectedException, TimeoutException {

		Subhandler setbreakpointHandler = new Subhandler() {
			public boolean handleEvent(DebugSession debugSession, EventPacket event) {
				if (event.getEvent().equals("script")) {
					Object scriptId = event.getBody().get("scriptId");
					Request request = new Request("script");
					request.getArguments().put("scriptId", scriptId);
					try {
						debugSession.sendRequest(request);
						Response response = debugSession.receiveResponse(request.getSequence(), 10000);
						assertTrue(response.isSuccess());
						// script onEnter
						request = new Request("setbreakpoint");
						request.getArguments().put("scriptId", scriptId);
						debugSession.sendRequest(request);
						response = debugSession.receiveResponse(request.getSequence(), 10000);
						assertTrue(response.isSuccess());
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

		final Object[] success = new Object[1];

		Subhandler contextCheckHandler = new Subhandler() {
			public boolean handleEvent(DebugSession debugSession, EventPacket event) {
				if (event.getEvent().equals("break")) {
					Number threadId = (Number) event.getBody().get("threadId");
					Request request = new Request("threads");
					try {
						debugSession.sendRequest(request);
						Response response = debugSession.receiveResponse(request.getSequence(), 10000);
						assertTrue(response.isSuccess());
						List threads = (List) response.getBody().get("threads");
						assertEquals(threadId.intValue(), Util.numberAsInt(threads.get(0)));
						success[0] = Boolean.TRUE;
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
		eventHandler.addSubhandler(contextCheckHandler);

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

		Scriptable scope = null;
		Context context = contextFactory.enterContext();
		try {
			scope = context.initStandardObjects();
			context.evaluateString(scope, script, "script", 0, null);
		} finally {
			Context.exit();
		}
		waitForEvents(2);
		assertEquals(Boolean.TRUE, success[0]);

	}
}
