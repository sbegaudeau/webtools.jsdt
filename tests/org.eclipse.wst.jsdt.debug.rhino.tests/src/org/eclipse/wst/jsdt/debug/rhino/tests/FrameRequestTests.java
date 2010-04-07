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
 * Variety of tests for requesting frame(s) / frame information
 * 
 * @since 1.1
 */
public class FrameRequestTests extends RequestTest {

	
	Subhandler setBreakpointHandler = new Subhandler() {
		public boolean handleEvent(DebugSession debugSession, EventPacket event) {
			if (event.getEvent().equals(JSONConstants.SCRIPT)) {
				Object scriptId = event.getBody().get(JSONConstants.SCRIPT_ID);
				Request request = new Request(JSONConstants.SCRIPT);
				request.getArguments().put(JSONConstants.SCRIPT_ID, scriptId);
				try {
					debugSession.sendRequest(request);
					Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
					assertTrue(response.isSuccess());
					Map result = (Map) response.getBody().get(JSONConstants.SCRIPT);
					assertNotNull("The script map cannot be null", result);
					// just set the breakpoint on line 6
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
	/**
	 * Tests getting a frame once suspended
	 * @throws Exception
	 */
	public void testFramesAndFrameLookup() throws Exception {
		
		eventHandler.addSubhandler(setBreakpointHandler);

		final Object[] success = new Object[1];

		Subhandler frameCheckHandler = new Subhandler() {
			public boolean handleEvent(DebugSession debugSession, EventPacket event) {
				if (event.getEvent().equals(JSONConstants.BREAK)) {
					Number threadId = (Number) event.getBody().get(JSONConstants.THREAD_ID);
					Number contextId = (Number) event.getBody().get(JSONConstants.CONTEXT_ID);
					Request request = new Request(JSONConstants.FRAMES);
					request.getArguments().put(JSONConstants.THREAD_ID, threadId);
					try {
						debugSession.sendRequest(request);
						Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
						assertTrue(response.isSuccess());
						Collection frames = (Collection) response.getBody().get(JSONConstants.FRAMES);
						for (Iterator iterator = frames.iterator(); iterator.hasNext();) {
							Number frameId = (Number) iterator.next();
							request = new Request(JSONConstants.FRAME);
							request.getArguments().put(JSONConstants.THREAD_ID, threadId);
							request.getArguments().put(JSONConstants.CONTEXT_ID, contextId);
							request.getArguments().put(JSONConstants.FRAME_ID, frameId);
							debugSession.sendRequest(request);
							response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
							assertTrue(response.isSuccess());
						}
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
		eventHandler.addSubhandler(frameCheckHandler);
		
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
		//script + breakpoint on line 6
		waitForEvents(2);
		assertEquals(Boolean.TRUE, success[0]);
	}
	
	/**
	 * Tests getting frames and running an evaluate
	 * @throws Exception
	 */
	public void testFramesAndFrameLookupAndEvaluate() throws Exception {
		eventHandler.addSubhandler(setBreakpointHandler);

		final Object[] success = new Object[1];

		Subhandler frameCheckHandler = new Subhandler() {
			public boolean handleEvent(DebugSession debugSession, EventPacket event) {
				if (event.getEvent().equals(JSONConstants.BREAK)) {
					Number threadId = (Number) event.getBody().get(JSONConstants.THREAD_ID);
					Number contextId = (Number) event.getBody().get(JSONConstants.CONTEXT_ID);
					Request request = new Request(JSONConstants.FRAMES);
					request.getArguments().put(JSONConstants.THREAD_ID, threadId);
					try {
						debugSession.sendRequest(request);
						Response response = debugSession.receiveResponse(request.getSequence(), 10000);
						assertTrue(response.isSuccess());
						Collection frames = (Collection) response.getBody().get(JSONConstants.FRAMES);
						for (Iterator iterator = frames.iterator(); iterator.hasNext();) {
							Number frameId = (Number) iterator.next();
							request = new Request(JSONConstants.EVALUATE);
							request.getArguments().put(JSONConstants.THREAD_ID, threadId);
							request.getArguments().put(JSONConstants.CONTEXT_ID, contextId);
							request.getArguments().put(JSONConstants.FRAME_ID, frameId);
							request.getArguments().put(JSONConstants.EXPRESSION, "this");
							debugSession.sendRequest(request);
							response = debugSession.receiveResponse(request.getSequence(), 10000);
							assertTrue(response.isSuccess());
							assertTrue(response.getBody().containsKey(JSONConstants.EVALUATE));
						}
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
		eventHandler.addSubhandler(frameCheckHandler);

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
		//script + breakpoint + StepEvent when we exit context
		waitForEvents(3);
		assertEquals(Boolean.TRUE, success[0]);
	}
}
