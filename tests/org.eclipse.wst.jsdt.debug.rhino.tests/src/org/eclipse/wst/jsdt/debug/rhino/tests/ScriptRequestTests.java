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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.JSONConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.RhinoRequest;
import org.eclipse.wst.jsdt.debug.transport.packet.Response;
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
		RhinoRequest request = new RhinoRequest(JSONConstants.SCRIPTS);
		debugSession.send(request);
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
		String script = Util.getTestSource(Util.SRC_SCRIPTS_CONTAINER, "script1.js"); //$NON-NLS-1$
		assertNotNull("The test source for [script1.js] must exist", script); //$NON-NLS-1$
		Scriptable scope = null;
		Context context = contextFactory.enterContext();
		try {
			scope = context.initStandardObjects();
			context.evaluateString(scope, script, "script", 0, null); //$NON-NLS-1$
		} finally {
			Context.exit();
		}

		RhinoRequest request = new RhinoRequest(JSONConstants.SCRIPTS);
		debugSession.send(request);
		Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertTrue(response.isSuccess());
		List scripts = (List) response.getBody().get(JSONConstants.SCRIPTS);
		assertNotNull(scripts);
		assertFalse(scripts.isEmpty());

		request = new RhinoRequest("script"); //$NON-NLS-1$
		request.getArguments().put("scriptId", scripts.get(0)); //$NON-NLS-1$
		debugSession.send(request);
		response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertTrue(response.isSuccess());
		Map result = (Map) response.getBody().get(JSONConstants.SCRIPT);
		assertEquals(script, result.get(JSONConstants.SOURCE));
		List lineNumbers = (List) result.get(JSONConstants.LINES);
		assertEquals(8, lineNumbers.size());
		assertTrue("The line number [10] should be returned", lineNumbers.contains(new BigDecimal(10))); //$NON-NLS-1$
		assertTrue("The line number [11] should be returned", lineNumbers.contains(new BigDecimal(11))); //$NON-NLS-1$
		assertTrue("The line number [12] should be returned", lineNumbers.contains(new BigDecimal(12))); //$NON-NLS-1$
		assertTrue("The line number [15] should be returned", lineNumbers.contains(new BigDecimal(15))); //$NON-NLS-1$
		assertTrue("The line number [16] should be returned", lineNumbers.contains(new BigDecimal(16))); //$NON-NLS-1$
		assertTrue("The line number [19] should be returned", lineNumbers.contains(new BigDecimal(19))); //$NON-NLS-1$
		assertTrue("The line number [22] should be returned", lineNumbers.contains(new BigDecimal(22))); //$NON-NLS-1$
		assertTrue("The line number [23] should be returned", lineNumbers.contains(new BigDecimal(23))); //$NON-NLS-1$

		List functionNames = (List) result.get(JSONConstants.FUNCTIONS);
		assertEquals(2, functionNames.size());
		assertEquals("test", functionNames.get(0)); //$NON-NLS-1$
		assertEquals("test2", functionNames.get(1)); //$NON-NLS-1$
	}
}
