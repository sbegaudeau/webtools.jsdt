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
import org.eclipse.wst.jsdt.debug.internal.rhino.bundles.RhinoClassLoader;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.EventPacket;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Request;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Response;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSBundleException;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSConstants;
import org.eclipse.wst.jsdt.debug.rhino.tests.connect.TestEventHandler.Subhandler;
import org.osgi.framework.Constants;

public class RequestLookupTest extends RequestTest {

	public void testLookup() throws DisconnectedException, TimeoutException, JSBundleException {

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
						Map result = (Map) response.getBody().get("script");
						// functions
						List functionNames = (List) result.get("functions");
						for (Iterator iterator = functionNames.iterator(); iterator.hasNext();) {
							String functionName = (String) iterator.next();
							request = new Request("setbreakpoint");
							request.getArguments().put("scriptId", scriptId);
							request.getArguments().put("function", functionName);
							debugSession.sendRequest(request);
							response = debugSession.receiveResponse(request.getSequence(), 10000);
							assertTrue(response.isSuccess());
						}
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

		Subhandler frameCheckHandler = new Subhandler() {
			public boolean handleEvent(DebugSession debugSession, EventPacket event) {
				if (event.getEvent().equals("break")) {
					Number threadId = (Number) event.getBody().get("threadId");
					Number contextId = (Number) event.getBody().get("contextId");
					Request request = new Request("frames");
					request.getArguments().put("threadId", threadId);
					try {
						debugSession.sendRequest(request);
						Response response = debugSession.receiveResponse(request.getSequence(), 10000);
						assertTrue(response.isSuccess());
						Collection frames = (Collection) response.getBody().get("frames");
						for (Iterator iterator = frames.iterator(); iterator.hasNext();) {
							Number frameId = (Number) iterator.next();
							request = new Request("lookup");
							request.getArguments().put("threadId", threadId);
							request.getArguments().put("contextId", contextId);
							request.getArguments().put("frameId", frameId);
							request.getArguments().put("handle", new Integer(0));
							debugSession.sendRequest(request);
							response = debugSession.receiveResponse(request.getSequence(), 10000);
							assertTrue(response.isSuccess());
							assertTrue(response.getBody().containsKey("lookup"));
						}
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
		eventHandler.addSubhandler(frameCheckHandler);

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
		waitForEvents(4);
		assertEquals(Boolean.TRUE, success[0]);
	}
}
