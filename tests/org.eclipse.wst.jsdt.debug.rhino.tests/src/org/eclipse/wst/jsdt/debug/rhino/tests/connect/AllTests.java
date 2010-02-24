/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.rhino.tests.connect;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests extends TestSuite {

	public static Test suite() {
		return new AllTests();
	}

	public AllTests() {
		addTestSuite(TransportTest.class);
		addTestSuite(DebugSessionTest.class);
		addTestSuite(RhinoDebuggerTest.class);
		addTestSuite(RequestBadCommandTest.class);
		addTestSuite(RequestBreakpointsTest.class);
		addTestSuite(RequestBreakpointTest.class);
		addTestSuite(RequestClearBreakpointTest.class);
		addTestSuite(RequestThreadsTest.class);
		addTestSuite(RequestThreadTest.class);
		addTestSuite(RequestContinueTest.class);
		addTestSuite(RequestDisposeTest.class);
		addTestSuite(RequestEvaluateTest.class);
		addTestSuite(RequestFramesTest.class);
		addTestSuite(RequestFrameTest.class);
		addTestSuite(RequestLookupTest.class);
		addTestSuite(RequestScriptsTest.class);
		addTestSuite(RequestScriptTest.class);
		addTestSuite(RequestSetBreakpointTest.class);
		addTestSuite(RequestSuspendTest.class);
		addTestSuite(RequestVersionTest.class);
	}
}
