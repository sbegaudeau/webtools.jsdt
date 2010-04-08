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

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.rhino.transport.JSONConstants;
import org.eclipse.wst.jsdt.debug.rhino.transport.Request;
import org.eclipse.wst.jsdt.debug.rhino.transport.Response;

/**
 * Variety of tests for thread requests
 * 
 * @since 1.1
 */
public class ThreadRequestTests extends RequestTest {

	/**
	 * Tests requesting the threads from the debugger when there are none
	 * @throws Exception
	 */
	public void testThreadsWithNoThreads() throws Exception {
		Request request = new Request(JSONConstants.THREADS);
		debugSession.sendRequest(request);
		Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertTrue(response.isSuccess());
		Collection threads = (Collection) response.getBody().get(JSONConstants.THREADS);
		assertNotNull(threads);
		assertTrue(threads.isEmpty());
	}

	/**
	 * Tests asking the VM for a thread with an invalid ID
	 * @throws Exception
	 */
	public void testInvalidThread() throws Exception {
		Request request = new Request("context");
		request.getArguments().put(JSONConstants.THREAD_ID, new Integer("9999"));
		debugSession.sendRequest(request);
		Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
		assertFalse(response.isSuccess());
	}
	
	/**
	 * Tests getting threads from the VM
	 * @throws Exception
	 */
	public void testThreads() throws Exception {
		eventHandler.addSubhandler(new SetBreakpointHandler(new int[] {6}));
		eventHandler.addSubhandler(new ThreadCheckHandler());
		String script = Util.getTestSource(Util.SRC_SCRIPTS_CONTAINER, "script1.js");
		assertNotNull("The test source for [script1.js] must exist", script);
		evalScript(script);
		//script event + breakpoint on line 6
		waitForEvents(2);
	}
}
