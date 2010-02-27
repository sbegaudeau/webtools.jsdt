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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.connect.DisconnectedException;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.TimeoutException;
import org.eclipse.wst.jsdt.debug.internal.rhino.bundles.RhinoClassLoader;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Request;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Response;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSBundleException;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSConstants;
import org.osgi.framework.Constants;

public class RequestScriptTest extends RequestTest {

	public void testScript() throws DisconnectedException, TimeoutException, JSBundleException {

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

		Request request = new Request("scripts");
		debugSession.sendRequest(request);
		Response response = debugSession.receiveResponse(request.getSequence(), 30000);
		assertTrue(response.isSuccess());
		List scripts = (List) response.getBody().get("scripts");
		assertNotNull(scripts);
		assertFalse(scripts.isEmpty());

		request = new Request("script");
		request.getArguments().put("scriptId", scripts.get(0));
		debugSession.sendRequest(request);
		response = debugSession.receiveResponse(request.getSequence(), 30000);
		assertTrue(response.isSuccess());
		Map result = (Map) response.getBody().get("script");
		assertEquals(script, result.get("source"));
		List lineNumbers = (List) result.get("lines");
		assertEquals(7, lineNumbers.size());
		assertEquals(2, Util.numberAsInt(lineNumbers.get(0)));
		assertEquals(3, Util.numberAsInt(lineNumbers.get(1)));
		assertEquals(6, Util.numberAsInt(lineNumbers.get(2)));
		assertEquals(7, Util.numberAsInt(lineNumbers.get(3)));
		assertEquals(10, Util.numberAsInt(lineNumbers.get(4)));
		assertEquals(13, Util.numberAsInt(lineNumbers.get(5)));
		assertEquals(14, Util.numberAsInt(lineNumbers.get(6)));

		List functionNames = (List) result.get("functions");
		assertEquals(2, functionNames.size());
		assertEquals("test", functionNames.get(0));
		assertEquals("test2", functionNames.get(1));
	}
}
