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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.JSONConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Request;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Response;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Variety of tests requesting information from scripts
 * 
 * @since 1.1
 */
public class ScriptRequestTests extends RequestTest {

	/**
	 * Tests asking for all scripts when there are none
	 * 
	 * @throws Exception
	 */
	public void testScriptsWithNoScripts() throws Exception {
		Request request = new Request(JSONConstants.SCRIPTS);
		debugSession.sendRequest(request);
		Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertTrue(response.isSuccess());
		Collection scripts = (Collection) response.getBody().get(JSONConstants.SCRIPTS);
		assertNotNull(scripts);
		assertTrue(scripts.isEmpty());
	}
	
	/**
	 * Tests getting a script and asserting its information
	 * 
	 * @throws Exception
	 */
	public void testScript() throws Exception {
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

		Request request = new Request(JSONConstants.SCRIPTS);
		debugSession.sendRequest(request);
		Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertTrue(response.isSuccess());
		List scripts = (List) response.getBody().get(JSONConstants.SCRIPTS);
		assertNotNull(scripts);
		assertFalse(scripts.isEmpty());

		request = new Request("script");
		request.getArguments().put("scriptId", scripts.get(0));
		debugSession.sendRequest(request);
		response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertTrue(response.isSuccess());
		Map result = (Map) response.getBody().get(JSONConstants.SCRIPT);
		assertEquals(script, result.get(JSONConstants.SOURCE));
		List lineNumbers = (List) result.get(JSONConstants.LINES);
		assertEquals(7, lineNumbers.size());
		assertTrue("The line number [1] should be returned", lineNumbers.contains(new BigDecimal(1)));
		assertTrue("The line number [2] should be returned", lineNumbers.contains(new BigDecimal(2)));
		assertTrue("The line number [5] should be returned", lineNumbers.contains(new BigDecimal(5)));
		assertTrue("The line number [6] should be returned", lineNumbers.contains(new BigDecimal(6)));
		assertTrue("The line number [9] should be returned", lineNumbers.contains(new BigDecimal(9)));
		assertTrue("The line number [12] should be returned", lineNumbers.contains(new BigDecimal(12)));
		assertTrue("The line number [13] should be returned", lineNumbers.contains(new BigDecimal(13)));
		//assertTrue("The line number [14] should be returned", lineNumbers.contains(new BigDecimal(14)));

		List functionNames = (List) result.get(JSONConstants.FUNCTIONS);
		assertEquals(2, functionNames.size());
		assertEquals("test", functionNames.get(0));
		assertEquals("test2", functionNames.get(1));
	}
}
