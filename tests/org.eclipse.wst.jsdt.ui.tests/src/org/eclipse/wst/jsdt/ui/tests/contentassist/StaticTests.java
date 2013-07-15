/*******************************************************************************
 * Copyright (c) 2011, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     
 *******************************************************************************/
package org.eclipse.wst.jsdt.ui.tests.contentassist;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.wst.jsdt.ui.tests.utils.TestProjectSetup;

public class StaticTests extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test Static vs Non Static JavaScript Content Assist";

	/**
	 * <p>
	 * Test project setup for this test.
	 * </p>
	 */
	private static TestProjectSetup fTestProjectSetup;
	
	/**
	 * <p>
	 * Default constructor
	 * <p>
	 * <p>
	 * Use {@link #suite()}
	 * </p>
	 * 
	 * @see #suite()
	 */
	public StaticTests() {
		super(TEST_NAME);
	}

	/**
	 * <p>
	 * Constructor that takes a test name.
	 * </p>
	 * <p>
	 * Use {@link #suite()}
	 * </p>
	 * 
	 * @param name
	 *            The name this test run should have.
	 * 
	 * @see #suite()
	 */
	public StaticTests(String name) {
		super(name);
	}

	/**
	 * <p>
	 * Use this method to add these tests to a larger test suite so set up and tear down can be
	 * performed
	 * </p>
	 * 
	 * @return a {@link TestSetup} that will run all of the tests in this class
	 *         with set up and tear down.
	 */
	public static Test suite() {
		TestSuite ts = new TestSuite(StaticTests.class, TEST_NAME);

		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false);
		
		return fTestProjectSetup;
	}

//	public void testCamelCase_OtherFile_BeforeOpen_ExpressionStarted_1() throws Exception {
//		String[][] expectedProposals = new String[][] { { "getServerIP() : String - Server" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 0, 11, expectedProposals);
//	}

	public void testStatic_CamelCase_OtherFile_BeforeOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "yahooDotCom : Server - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 2, 3, expectedProposals);
	}

	public void testStatic_CamelCase_OtherFile_BeforeOpen_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "clientIP : String - Server" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 4, 15, expectedProposals);
	}

//	public void testStatic_OtherFile_BeforeOpen_ExpressionStarted_4() throws Exception {
//		String[][] expectedProposals = new String[][] { { "getClientPort() - Server",
//				"getClientIP() : String - Server", "getServerIP() : String - Server" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 6, 13, expectedProposals);
//	}

	public void testStatic_OtherFile_BeforeOpen_ExpressionStarted_5() throws Exception {
		String[][] expectedProposals = new String[][] { { "serverIP : String - Server" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 8, 8, expectedProposals);
	}

	public void testStatic_NegativeTest_OtherFile_BeforeOpen_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "getServerIP() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 6, 13, expectedProposals, true, false);
	}

	public void testStatic_NegativeTest_OtherFile_BeforeOpen_ExpressionStarted_5() throws Exception {
		String[][] expectedProposals = new String[][] { { "clientIP : String - Server", "getClientIP() - Global",
				"getClientPort() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 8, 8, expectedProposals, true, false);
	}

//	public void testCamelCase_ThisFile_ExpressionStarted_1() throws Exception {
//		String[][] expectedProposals = new String[][] { { "getServerIP() : String - Server" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_0.js", 28, 11, expectedProposals);
//	}

	public void testStatic_CamelCase_ThisFile_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "yahooDotCom : Server - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_0.js", 30, 3, expectedProposals);
	}

	public void testStatic_CamelCase_ThisFile_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "clientIP : String - Server" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_0.js", 32, 15, expectedProposals);
	}

	public void testStatic_ThisFile_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "clientIP : String - Server", "port - Server",
				"prototype - Server", "getClientIP() : String - Server", "getClientPort() - Server" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_0.js", 34, 12, expectedProposals);
	}

//	public void testStatic_ThisFile_ExpressionStarted_5() throws Exception {
//		String[][] expectedProposals = new String[][] { { "serverIP : String - Server", "getServerIP() : String - Server" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_0.js", 36, 7, expectedProposals);
//	}

//	public void testCamelCase_OtherFile_AfterOpen_ExpressionStarted_1() throws Exception {
//		String[][] expectedProposals = new String[][] { { "getServerIP() : String - Server" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 0, 11, expectedProposals);
//	}

	public void testStatic_CamelCase_OtherFile_AfterOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "yahooDotCom : Server - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 2, 3, expectedProposals);
	}

	public void testStatic_CamelCase_OtherFile_AfterOpen_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "clientIP : String - Server" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 4, 15, expectedProposals);
	}

	public void testStatic_OtherFile_AfterOpen_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "clientIP : String - Server", "port - Server",
				"prototype - Server", "getClientIP() : String - Server", "getClientPort() - Server" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 6, 12, expectedProposals);
	}

//	public void testStatic_OtherFile_AfterOpen_ExpressionStarted_5() throws Exception {
//		String[][] expectedProposals = new String[][] { { "serverIP : String - Server", "getServerIP() : String - Server" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 8, 7, expectedProposals);
//	}

	public void testStatic_NegativeTest_OtherFile_AfterOpen_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "getServerIP() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 6, 12, expectedProposals, true, false);
	}

	public void testStatic_NegativeTest_OtherFile_AfterOpen_ExpressionStarted_5() throws Exception {
		String[][] expectedProposals = new String[][] { { "clientIP : String - Server", "getClientIP() - Global",
				"getClientPort() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 8, 7, expectedProposals, true, false);
	}
}