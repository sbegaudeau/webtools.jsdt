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
 * Variety of tests for sending <code>lookup</code> requests
 * 
 * @since 1.0
 */
public class RequestLookupTest extends FrameRequestTests {

	/**
	 * Tests performing a <code>lookup</code>
	 * @throws Exception
	 */
	public void testLookup() throws Exception {
		
		eventHandler.addSubhandler(setBreakpointHandler);

		final Object[] success = new Object[1];

		Subhandler frameCheckHandler = new Subhandler() {
			public boolean handleEvent(DebugSession debugSession, EventPacket event) {
				if (event.getEvent().equals(JSONConstants.BREAK)) {
					Number threadId = (Number) event.getBody().get(JSONConstants.THREAD_ID);
					Number contextId = (Number) event.getBody().get(JSONConstants.CONTEXT_ID);
					Request request = new Request("frames");
					request.getArguments().put(JSONConstants.THREAD_ID, threadId);
					try {
						debugSession.sendRequest(request);
						Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
						assertTrue(response.isSuccess());
						Collection frames = (Collection) response.getBody().get("frames");
						for (Iterator iterator = frames.iterator(); iterator.hasNext();) {
							Number frameId = (Number) iterator.next();
							request = new Request(JSONConstants.LOOKUP);
							request.getArguments().put(JSONConstants.THREAD_ID, threadId);
							request.getArguments().put(JSONConstants.CONTEXT_ID, contextId);
							request.getArguments().put(JSONConstants.FRAME_ID, frameId);
							request.getArguments().put(JSONConstants.HANDLE, new Integer(0));
							debugSession.sendRequest(request);
							response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
							assertTrue(response.isSuccess());
							assertTrue(response.getBody().containsKey(JSONConstants.LOOKUP));
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
		//script + breakpoint on line 6 + exit step event
		waitForEvents(3);
		assertEquals(Boolean.TRUE, success[0]);
	}
}
