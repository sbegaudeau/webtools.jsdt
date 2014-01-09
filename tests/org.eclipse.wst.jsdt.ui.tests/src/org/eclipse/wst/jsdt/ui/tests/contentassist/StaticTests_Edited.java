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

public class StaticTests_Edited extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test Edited for Static vs Non Static JavaScript Content Assist";

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
	public StaticTests_Edited() {
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
	public StaticTests_Edited(String name) {
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
		TestSuite ts = new TestSuite(StaticTests_Edited.class, TEST_NAME);
		
		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false) {

			public void additionalSetUp() throws Exception {

				editFile_StaticTests_0();
				editFile_StaticTests_1();

			}
		};
		
		return fTestProjectSetup;
	}

//	public void testStatic_CamelCase_OtherFile_AfterEdit_ExpressionStarted_1() throws Exception {
//		String[][] expectedProposals = new String[][] { { "getRouterIP() : String - Server" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 0, 11, expectedProposals);
//	}

	public void testStatic_CamelCase_OtherFile_AfterEdit_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "yahooDotCom : Server - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 2, 3, expectedProposals);
	}

	public void testStatic_CamelCase_OtherFile_AfterEdit_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "switchIP : String - Server" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 4, 15, expectedProposals);
	}

	public void testStatic_OtherFile_AfterEdit_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "switchIP : String - Server", "port - Server",
				"prototype - Server", "getSwitchIP() : String - Server", "getSwitchPort() - Server" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 6, 12, expectedProposals);
	}

//	public void testStatic_OtherFile_AfterEdit_ExpressionStarted_5() throws Exception {
//		String[][] expectedProposals = new String[][] { { "routerIP : String - Server", "getRouterIP() : String - Server" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 8, 7, expectedProposals);
//	}

	public void testStatic_NegativeTest_OtherFile_AfterEdit_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "getRouterIP() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 6, 12, expectedProposals, true, false);
	}

	public void testStatic_NegativeTest_OtherFile_AfterEdit_ExpressionStarted_5() throws Exception {
		String[][] expectedProposals = new String[][] { { "switchIP : String - Server", "getSwitchIP() - Global",
				"getSwitchPort() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 8, 7, expectedProposals, true, false);
	}

//	public void testStatic_CamelCase_ThisFile_AfterEdit_ExpressionStarted_1() throws Exception {
//		String[][] expectedProposals = new String[][] { { "getRouterIP() : String - Server" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_0.js", 28, 11, expectedProposals);
//	}

	public void testStatic_CamelCase_ThisFile_AfterEdit_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "yahooDotCom : Server - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_0.js", 30, 3, expectedProposals);
	}

	public void testStatic_CamelCase_ThisFile_AfterEdit_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "switchIP : String - Server" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_0.js", 32, 15, expectedProposals);
	}

	public void testStatic_ThisFile_AfterEdit_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "switchIP : String - Server", "port - Server",
				"prototype - Server", "getSwitchIP() : String - Server", "getSwitchPort() - Server" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_0.js", 34, 12, expectedProposals);
	}

//	public void testStatic_ThisFile_AfterEdit_ExpressionStarted_5() throws Exception {
//		String[][] expectedProposals = new String[][] { { "routerIP : String - Server", "getRouterIP() : String - Server" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_0.js", 36, 7, expectedProposals);
//	}

	/**
	 * file -> StaticTests_0.js
	 * getServerIP -> getRouterIP
	 * serverIP -> routerIP
	 * gSIP -> gRIP
	 * client -> switch
	 * Client -> Switch
	 * cIP -> sIP
	 * 
	 * @throws Exception
	 */
	public static void editFile_StaticTests_0() throws Exception {
		fTestProjectSetup.editFile("StaticTests_0.js", 7, 10, 6, "Router");
		fTestProjectSetup.editFile("StaticTests_0.js", 10, 7, 8, "routerIP");
		fTestProjectSetup.editFile("StaticTests_0.js", 8, 15, 8, "routerIP");
		fTestProjectSetup.editFile("StaticTests_0.js", 28, 8, 1, "R");
		fTestProjectSetup.editFile("StaticTests_0.js", 14, 20, 6, "Switch");
		fTestProjectSetup.editFile("StaticTests_0.js", 17, 17, 8, "switchIP");
		fTestProjectSetup.editFile("StaticTests_0.js", 18, 20, 8, "SwitchIP");
		fTestProjectSetup.editFile("StaticTests_0.js", 19, 13, 8, "switchIP");
		fTestProjectSetup.editFile("StaticTests_0.js", 32, 12, 1, "s");
	}

	/**
	 * file -> StaticTests_1.js
	 * 
	 * 
	 * @throws Exception
	 */
	public static void editFile_StaticTests_1() throws Exception {
		fTestProjectSetup.editFile("StaticTests_1.js", 0, 8, 1, "R");
		fTestProjectSetup.editFile("StaticTests_1.js", 4, 12, 1, "s");
	}
}
