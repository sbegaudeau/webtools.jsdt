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

import junit.framework.TestCase;

import org.eclipse.wst.jsdt.debug.internal.rhino.RhinoDebugger;
import org.eclipse.wst.jsdt.debug.internal.rhino.bundles.JSFrameworkImpl;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.connect.TransportService;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.PipedTransportService;
import org.mozilla.javascript.ContextFactory;

public class RequestTest extends TestCase {
	protected RhinoDebugger debugger;
	protected DebugSession debugSession;
	protected TestEventHandler eventHandler;
	protected JSFrameworkImpl framework;
	protected ContextFactory contextFactory;

	protected void setUp() throws Exception {
		TransportService pipedTransport = new PipedTransportService();
		ConnectionHelper helper = new ConnectionHelper(pipedTransport, null);

		debugger = new RhinoDebugger(pipedTransport, null, false);
		debugger.start();

		debugSession = new DebugSession(helper.getClientConnection());
		eventHandler = new TestEventHandler(debugSession);
		eventHandler.start();

		assertTrue(debugger.suspendForRuntime(5000));
		contextFactory = new ContextFactory();
		contextFactory.addListener(debugger);

		framework = new JSFrameworkImpl(contextFactory);
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		framework.shutdown();
		contextFactory.removeListener(debugger);
		eventHandler.stop();
		debugger.stop();
		debugSession.dispose();
	}

	synchronized void waitForEvents(int count) {
		eventHandler.waitForEvents(count);
	}

}
