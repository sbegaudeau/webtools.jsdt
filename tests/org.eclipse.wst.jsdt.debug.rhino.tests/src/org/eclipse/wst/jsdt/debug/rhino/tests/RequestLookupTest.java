/*******************************************************************************
 * Copyright (c) 2009, 2012 IBM Corporation and others.
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
import java.util.List;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.EventPacket;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.JSONConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.RhinoRequest;
import org.eclipse.wst.jsdt.debug.rhino.tests.TestEventHandler.Subhandler;
import org.eclipse.wst.jsdt.debug.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.transport.exception.DisconnectedException;
import org.eclipse.wst.jsdt.debug.transport.exception.TimeoutException;
import org.eclipse.wst.jsdt.debug.transport.packet.Response;

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
		eventHandler.addSubhandler(new SetBreakpointHandler(new int[] {16}));

		final Object[] success = new Object[1];

		Subhandler frameCheckHandler = new Subhandler() {
			/* (non-Javadoc)
			 * @see org.eclipse.wst.jsdt.debug.rhino.tests.TestEventHandler.Subhandler#testName()
			 */
			public String testName() {
				return getName();
			}
			public boolean handleEvent(DebugSession debugSession, EventPacket event) {
				if (event.getEvent().equals(JSONConstants.BREAK)) {
					Number threadId = (Number) event.getBody().get(JSONConstants.THREAD_ID);
					Number contextId = (Number) event.getBody().get(JSONConstants.CONTEXT_ID);
					RhinoRequest request = new RhinoRequest(JSONConstants.FRAMES);
					request.getArguments().put(JSONConstants.THREAD_ID, threadId);
					try {
						debugSession.send(request);
						Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
						assertTrue(response.isSuccess());
						Collection frames = (Collection) response.getBody().get(JSONConstants.FRAMES);
						for (Iterator iterator = frames.iterator(); iterator.hasNext();) {
							Number frameId = (Number) iterator.next();
							request = new RhinoRequest(JSONConstants.LOOKUP);
							request.getArguments().put(JSONConstants.THREAD_ID, threadId);
							request.getArguments().put(JSONConstants.CONTEXT_ID, contextId);
							request.getArguments().put(JSONConstants.FRAME_ID, frameId);
							request.getArguments().put(JSONConstants.REF, new Integer(0));
							debugSession.send(request);
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

		String script = Util.getTestSource(Util.SRC_SCRIPTS_CONTAINER, "script1.js"); //$NON-NLS-1$
		assertNotNull("The test source for [script1.js] must exist", script); //$NON-NLS-1$
		//script + breakpoint on line 6
		evalScript(script, 2);
	}
	
	/**
	 * Tests that the lookup produces the correct result in Rhino 1.7R4
	 * 
	 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=387465
	 * @throws Exception
	 */
	public void testLookupBug387465() throws Exception {
		eventHandler.addSubhandler(new SetBreakpointHandler(new int[] {17}));

		final Object[] success = new Object[1];

		Subhandler frameCheckHandler = new Subhandler() {
			/* (non-Javadoc)
			 * @see org.eclipse.wst.jsdt.debug.rhino.tests.TestEventHandler.Subhandler#testName()
			 */
			public String testName() {
				return getName();
			}
			public boolean handleEvent(DebugSession debugSession, EventPacket event) {
				if (event.getEvent().equals(JSONConstants.BREAK)) {
					Number threadId = (Number) event.getBody().get(JSONConstants.THREAD_ID);
					Number contextId = (Number) event.getBody().get(JSONConstants.CONTEXT_ID);
					RhinoRequest request = new RhinoRequest(JSONConstants.FRAMES);
					request.getArguments().put(JSONConstants.THREAD_ID, threadId);
					try {
						debugSession.send(request);
						Response response = debugSession.receiveResponse(request.getSequence(), VirtualMachine.DEFAULT_TIMEOUT);
						assertTrue(response.isSuccess());
						Collection frames = (Collection) response.getBody().get(JSONConstants.FRAMES);
						assertTrue("There should be at least one frame", frames.size() > 0); //$NON-NLS-1$
						Number frameId = (Number) frames.iterator().next();
						
						response = doLookup(debugSession, threadId, contextId, frameId, new Integer(0));
						assertTrue(response.isSuccess());
						Map map = (Map) response.getBody().get(JSONConstants.LOOKUP);
						assertNotNull("There must be a lookup response", map); //$NON-NLS-1$
						List props = (List) map.get(JSONConstants.PROPERTIES);
						assertNotNull("The successful lookup should have properties", props); //$NON-NLS-1$
						map = findVar(props, "s"); //$NON-NLS-1$
						assertNotNull("There must be an 's' variable", map); //$NON-NLS-1$
						Number ref = (Number) map.get(JSONConstants.REF);
						assertNotNull("There must be a 'ref' entry in the variable map", ref); //$NON-NLS-1$
						response = doLookup(debugSession, threadId, contextId, frameId, ref);
						assertTrue(response.isSuccess());
						map = (Map) response.getBody().get(JSONConstants.LOOKUP);
						assertNotNull("There should be a lookup entry trying to lookup 's' with ref: "+ref, map); //$NON-NLS-1$
						assertEquals("The type should be string", JSONConstants.STRING,  map.get(JSONConstants.TYPE)); //$NON-NLS-1$
						assertEquals("The value should be 'aa'", "aa", map.get(JSONConstants.VALUE)); //$NON-NLS-1$ //$NON-NLS-2$
						success[0] = Boolean.TRUE;
					} catch (DisconnectedException e) {
						e.printStackTrace();
					} catch (TimeoutException e) {
						e.printStackTrace();
					}
					catch (Exception e) {
						fail(e.getMessage());
					}
					return true;
				}
				return false;
			}
		};
		eventHandler.addSubhandler(frameCheckHandler);

		String script = Util.getTestSource(Util.SRC_SCRIPTS_CONTAINER, "bug387465.js"); //$NON-NLS-1$
		assertNotNull("The test source for [bug387465.js] must exist", script); //$NON-NLS-1$
		//script + breakpoint on line 17
		evalScript(script, 2);
	}
}
