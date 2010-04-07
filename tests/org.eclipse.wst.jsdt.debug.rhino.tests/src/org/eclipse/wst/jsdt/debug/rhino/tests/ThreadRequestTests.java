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
import java.util.List;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.rhino.tests.TestEventHandler.Subhandler;
import org.eclipse.wst.jsdt.debug.rhino.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.rhino.transport.DisconnectedException;
import org.eclipse.wst.jsdt.debug.rhino.transport.EventPacket;
import org.eclipse.wst.jsdt.debug.rhino.transport.JSONConstants;
import org.eclipse.wst.jsdt.debug.rhino.transport.Request;
import org.eclipse.wst.jsdt.debug.rhino.transport.Response;
import org.eclipse.wst.jsdt.debug.rhino.transport.TimeoutException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

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
		Subhandler setbreakpointHandler = new Subhandler() {
			public boolean handleEvent(DebugSession debugSession, EventPacket event) {
				if (event.getEvent().equals(JSONConstants.SCRIPT)) {
					Number scriptId = (Number) event.getBody().get(JSONConstants.SCRIPT_ID);
					Request request = new Request(JSONConstants.SCRIPT);
					request.getArguments().put(JSONConstants.SCRIPT_ID, scriptId);
					try {
						debugSession.sendRequest(request);
						Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
						assertTrue(response.isSuccess());
						Map script = (Map) response.getBody().get(JSONConstants.SCRIPT);
						assertNotNull("The response body cannot be null", script);
						request = new Request(JSONConstants.SETBREAKPOINT);
						request.getArguments().put(JSONConstants.SCRIPT_ID, scriptId);
						request.getArguments().put(JSONConstants.LINE, new Integer(6));
						debugSession.sendRequest(request);
						response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
						assertTrue(response.isSuccess());
						
					} catch (DisconnectedException e) {
						e.printStackTrace();
					} catch (TimeoutException e) {
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
				if (event.getEvent().equals(JSONConstants.BREAK)) {
					Number threadId = (Number) event.getBody().get(JSONConstants.THREAD_ID);
					Request request = new Request(JSONConstants.THREADS);
					try {
						debugSession.sendRequest(request);
						Response response = debugSession.receiveResponse(request.getSequence(), 10000);
						assertTrue(response.isSuccess());
						List threads = (List) response.getBody().get(JSONConstants.THREADS);
						assertEquals(threadId.intValue(), Util.numberAsInt(threads.get(0)));
						success[0] = Boolean.TRUE;
					} catch (DisconnectedException e) {
						e.printStackTrace();
					} catch (TimeoutException e) {
						e.printStackTrace();
					}
					return true;
				}
				return false;
			}
		};
		eventHandler.addSubhandler(contextCheckHandler);

		String script = Util.getTestSource(Util.SRC_SCRIPTS_CONTAINER, "script1.js");
		assertNotNull("The test source for [script1.js] must exist", script);
		
		Scriptable scope = null;
		Context context = contextFactory.enterContext();
		try {
			scope = context.initStandardObjects();
			context.evaluateString(scope, script, JSONConstants.SCRIPT, 0, null);
		} finally {
			Context.exit();
		}
		//script event + breakpoint on line 6
		waitForEvents(2);
		assertEquals(Boolean.TRUE, success[0]);
	}
}
