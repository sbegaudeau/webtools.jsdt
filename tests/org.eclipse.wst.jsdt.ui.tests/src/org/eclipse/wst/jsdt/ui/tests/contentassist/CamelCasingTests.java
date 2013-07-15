/*******************************************************************************
 * Copyright (c) 2011, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.ui.tests.contentassist;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.wst.jsdt.ui.tests.utils.TestProjectSetup;

public class CamelCasingTests extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test Camel Casing JavaScript Content Assist";

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
	public CamelCasingTests() {
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
	public CamelCasingTests(String name) {
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
		TestSuite ts = new TestSuite(CamelCasingTests.class, TEST_NAME);
		
		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false) {
			/**
			 * @see org.eclipse.wst.jsdt.ui.tests.contentassist.ContentAssistTestUtilities.ContentAssistTestsSetup#additionalSetUp()
			 */
			public void additionalSetUp() throws Exception {
				// for some reason this test suite wants an extra second before running otherwise
				// the first test fails...
				Thread.sleep(1000);
			}
		};
		
		return fTestProjectSetup;
	}

	public void testCamelCasing_OtherFile_BeforeOpen_Expression1() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun", "mail.iGotSpam(a, b) - mail.iGotSpam",
				"iGotMessage(param1) - iGotMessage" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 0, 6, expectedProposals);
	}

	public void testCamelCasing_OtherFile_BeforeOpen_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun", "mail.iGotSpam(a, b) - mail.iGotSpam" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 2, 7, expectedProposals);
	}

	public void testMustFail_OtherFile_Expression2_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "iGotMessage(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 2, 7, expectedProposals, true,
				false);
	}

	public void testCamelCasing_OtherFile_BeforeOpen_Expression3() throws Exception {
		String[][] expectedProposals = new String[][] { { "mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 4, 8, expectedProposals);
	}

	public void testCamelCasing_ThisFile_Expression1() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun", "mail.iGotSpam(a, b) - mail.iGotSpam",
				"iGotMessage(param1) - iGotMessage" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_0.js", 17, 6, expectedProposals);
	}

	public void testCamelCasing_ThisFile_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun", "mail.iGotSpam(a, b) - mail.iGotSpam" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_0.js", 19, 7, expectedProposals);
	}

	public void testCamelCasing_ThisFile_Expression3() throws Exception {
		String[][] expectedProposals = new String[][] { { "mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_0.js", 21, 8, expectedProposals);
	}

	public void testCamelCasing_OtherFile_AfterEdit_Expression3_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "iGotMessage(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 4, 8, expectedProposals, true,
				false);
	}

	public void testCamelCasing_OtherFile_AfterOpen_Expression1() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun", "mail.iGotSpam(a, b) - mail.iGotSpam",
				"iGotMessage(param1) - iGotMessage" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 0, 6, expectedProposals);
	}

	public void testCamelCasing_OtherFile_AfterOpen_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun", "mail.iGotSpam(a, b) - mail.iGotSpam" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 2, 7, expectedProposals);
	}

	public void testCamelCasing_OtherFile_AfterOpen_Expression3() throws Exception {
		String[][] expectedProposals = new String[][] { { "mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 4, 8, expectedProposals);
	}

	//	Camel case testing for global variables

	public void testGlobalVar_CamelCasing_OtherFile_BeforeOpen_Expresssion1() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalVarNum : Number - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 5, 3, expectedProposals);
	}

	public void testGlobalVar_CamelCasing_OtherFile_BeforeOpen_Expresssion2() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalVarNum : Number - Global", "globalVar - Global",
				"globalVarObject : {} - Global", "globalVarString : String - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 7, 2, expectedProposals);
	}

	public void testGlobalVar_CamelCasing_Thisfile_Expresssion1() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalVarNum : Number - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_0.js", 15, 3, expectedProposals);
	}

	public void testGlobalVar_CamelCasing_ThisFile_Expresssion2() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalVarNum : Number - Global", "globalVar - Global",
				"globalVarObject : {} - Global", "globalVarString : String - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_0.js", 17, 2, expectedProposals);
	}

	public void testGlobalVar_CamelCasing_OtherFile_AfterOpen_Expresssion1() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalVarNum : Number - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 5, 3, expectedProposals);
	}

	public void testGlobalVar_CamelCasing_OtherFile_AfterOpen_Expresssion2() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalVarNum : Number - Global", "globalVar - Global",
				"globalVarObject : {} - Global", "globalVarString : String - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 7, 2, expectedProposals);
	}

	//	Camel Casing tests for doubly nested functions

	public void testDoublyNestedFunc_CamelCasing_OtherFile_BeforeOpen_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { { "outerFunc() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_1.js", 2, 2, expectedProposals);
	}

	public void testDoublyNestedFunc_CamelCasing_ThisFile_AfterOpen_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { { "outerFunc() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_1.js", 2, 2, expectedProposals);
	}

	public void testDoublyNestedFunc_CamelCasing_OtherFile_AfterOpen_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { { "outerFunc() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_1.js", 2, 2, expectedProposals);
	}
}