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

public class CamelCasingTests_Edited extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test Camel Casing JavaScript Content Assist after Edit";

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
	public CamelCasingTests_Edited() {
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
	public CamelCasingTests_Edited(String name) {
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
		TestSuite ts = new TestSuite(CamelCasingTests_Edited.class, TEST_NAME);
		
		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false) {
			/**
			 * @see org.eclipse.wst.jsdt.ui.tests.contentassist.ContentAssistTestUtilities.ContentAssistTestsSetup#additionalSetUp()
			 */
			public void additionalSetUp() throws Exception {
				editFile_TestConstructorCamelCase_0();
				editFile_test13_0();
				editFile_test11_0();
				editFile_test11_1();
			}
		};
		
		return fTestProjectSetup;
	}

	public void testCamelCasing_thisFile_AfterEdit_Expression1_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "iGotMessage(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_0.js", 17, 6, expectedProposals, true,
				false);
	}

	public void testCamelCasing_thisFile_AfterEdit_Expression2_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "iGotMessage(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_0.js", 19, 7, expectedProposals, true,
				false);
	}

	public void testCamelCasing_thisFile_AfterEdit_Expression3_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "iGotMessage(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_0.js", 21, 8, expectedProposals, true,
				false);
	}

	public void testCamelCasing_ThisFile_AfterEdit_Expression1() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun", "mail.iGotSpam(a, b) - mail.iGotSpam",
				"iGotSentMessage(param1) - iGotSentMessage" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_0.js", 17, 6, expectedProposals);
	}

	public void testCamelCasing_ThisFile_AfterEdit_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun", "mail.iGotSpam(a, b) - mail.iGotSpam",
				"iGotSentMessage(param1) - iGotSentMessage" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_0.js", 19, 7, expectedProposals);
	}

	public void testCamelCasing_ThisFile_AfterEdit_Expression3() throws Exception {
		String[][] expectedProposals = new String[][] { { "mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_0.js", 21, 8, expectedProposals);
	}

	public void testCamelCasing_OtherFile_AfterEdit_Expression1_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "iGotMessage(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 0, 6, expectedProposals, true,
				false);
	}

	public void testCamelCasing_OtherFile_AfterEdit_Expression2_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "iGotMessage(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 2, 7, expectedProposals, true,
				false);
	}

	public void testCamelCasing_OtherFile_AfterEdit_Expression3_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "iGotMessage(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 4, 8, expectedProposals, true,
				false);
	}

	public void testCamelCasing_OtherFile_AfterEdit_Expression1() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun", "mail.iGotSpam(a, b) - mail.iGotSpam",
				"iGotSentMessage(param1) - iGotSentMessage" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 0, 6, expectedProposals);
	}

	public void testCamelCasing_OhisFile_AfterEdit_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun", "mail.iGotSpam(a, b) - mail.iGotSpam",
				"iGotSentMessage(param1) - iGotSentMessage" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 2, 7, expectedProposals);
	}

	public void testCamelCasing_OhisFile_AfterEdit_Expression3() throws Exception {
		String[][] expectedProposals = new String[][] { { "mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 4, 8, expectedProposals);
	}

	public void testCamelCasing_Thisfile_AfterEdit_Expresssion1() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalEditedNumber : Number - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_0.js", 15, 3, expectedProposals);
	}

	public void testCamelCasing_ThisFile_AfterEdit_Expresssion2() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalEditedNumber : Number - Global",
				"globalEditedString : String - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_0.js", 17, 2, expectedProposals);
	}

	public void testCamelCasing_OtherFile_AfterEdit_Expresssion1() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalEditedNumber : Number - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 5, 3, expectedProposals);
	}

	public void testCamelCasing_OtherFile_AfterEdit_Expresssion2() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalEditedString : String - Global",
				"globalEditedNumber : Number - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 7, 2, expectedProposals);
	}

	public void testCamelCasing_OtherFile_AfterEdit_Expression3() throws Exception {
		String[][] expectedProposals = new String[][] { { "editedFunc() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_1.js", 6, 2, expectedProposals);
	}

	/**
	 * file -> TestConstructorCamelCase_0.js
	 * iGotMessage -> iSentMessage
	 * 
	 * @throws Exception
	 */
	private static void editFile_TestConstructorCamelCase_0() throws Exception {
		fTestProjectSetup.editFile("TestConstructorCamelCase_0.js", 0, 4, 11, "iGotSentMessage");
		fTestProjectSetup.editFile("TestConstructorCamelCase_0.js", 0, 31, 11, "iGotSentMessage");
	}

	private static void editFile_test11_0() throws Exception {
		fTestProjectSetup.editFile("test11_0.js", 1, 4, 12, "globalEditedNumber");
		fTestProjectSetup.editFile("test11_0.js", 7, 0, 12, "globalEditedString");
		fTestProjectSetup.editFile("test11_0.js", 11, 6, 1, "E");
		fTestProjectSetup.editFile("test11_0.js", 15, 1, 1, "E");
		fTestProjectSetup.editFile("test11_0.js", 17, 1, 1, "E");
	}

	private static void editFile_test11_1() throws Exception {
		fTestProjectSetup.editFile("test11_1.js", 3, 6, 1, "E");
		fTestProjectSetup.editFile("test11_1.js", 5, 1, 1, "E");
		fTestProjectSetup.editFile("test11_1.js", 7, 1, 1, "E");
	}

	private static void editFile_test13_0() throws Exception {
		fTestProjectSetup.editFile("test13_0.js", 7, 11, 6, "edited");
		fTestProjectSetup.editFile("test13_0.js", 0, 9, 9, "editedFunc");
	}
}