/*******************************************************************************
 * Copyright (c) 2011, 2012 IBM Corporation and others.
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

public class GlobalFunctionTests_Edited extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test Global Functions JavaScript Content Assist after Edit";

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
	public GlobalFunctionTests_Edited() {
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
	public GlobalFunctionTests_Edited(String name) {
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
		TestSuite ts = new TestSuite(GlobalFunctionTests_Edited.class, TEST_NAME);
		
		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false) {
			/**
			 * @see org.eclipse.wst.jsdt.ui.tests.contentassist.ContentAssistTestUtilities.ContentAssistTestsSetup#additionalSetUp()
			 */
			public void additionalSetUp() throws Exception {
				editFile_test0_0();
				editFile_TestNamedFunctionsAssignedToVariables_0();
			}
		};
		
		return fTestProjectSetup;
	}

	public void testFindFunctions_ThisFile_AfterEdit_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { { "functionOne() - Global", "functionTwo() : String - Global",
				"funcThree(paramOne) - Global", "funcFour(paramOne, paramTwo) : Number - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test0_0.js", 55, 0, expectedProposals);
	}

	public void testFindFunctions_ThisFile_After_EditExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "functionOne() - Global", "functionTwo() : String - Global",
				"funcThree(paramOne) - Global", "funcFour(paramOne, paramTwo) : Number - Global",
				"funcFive(Number paramOne, String paramTwo) : String - Global",
				"funcSix(paramOne, String paramTwo) : Number - Global",
				"funcSeven(String paramOne, paramTwo) : Number - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test0_0.js", 57, 1, expectedProposals);
	}

	public void testFindFunctions_ThisFile_AfterEdit_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "functionOne() - Global", "functionTwo() : String - Global",
				"funcThree(paramOne) - Global", "funcFour(paramOne, paramTwo) : Number - Global",
				"funcFive(Number paramOne, String paramTwo) : String - Global",
				"funcSix(paramOne, String paramTwo) : Number - Global",
				"funcSeven(String paramOne, paramTwo) : Number - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test0_0.js", 59, 4, expectedProposals);
	}

	public void testFindFunctions_ThisFile_AfterEdit_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "functionTwo() : String - Global",
				"funcThree(paramOne) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test0_0.js", 61, 5, expectedProposals);
	}

	public void testFindFunctions_OtherFile_AferEdit_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "functionOne() - Global", "functionTwo() : String - Global",
				"funcThree(paramOne) - Global", "funcFour(paramOne, paramTwo) : Number - Global",
				"funcFive(Number paramOne, String paramTwo) : String - Global",
				"funcSix(paramOne, String paramTwo) : Number - Global",
				"funcSeven(String paramOne, paramTwo) : Number - Global", "funcEight(paramOne) : String - Global",
				"funcNine(paramOne) : Number - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test0_1.js", 0, 1, expectedProposals);
	}

	public void testFindFunctions_OtherFile_AfterEdit_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "functionOne() - Global", "functionTwo() : String - Global",
				"funcThree(paramOne) - Global", "funcFour(paramOne, paramTwo) : Number - Global",
				"funcFive(Number paramOne, String paramTwo) : String - Global",
				"funcSix(paramOne, String paramTwo) : Number - Global",
				"funcSeven(String paramOne, paramTwo) : Number - Global", "funcEight(paramOne) : String - Global",
				"funcNine(paramOne) : Number - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test0_1.js", 2, 4, expectedProposals);
	}

	public void testFindFunctions_OtherFile_AfterEdit_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "functionTwo() : String - Global",
				"funcThree(paramOne) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test0_1.js", 4, 5, expectedProposals);
	}

	public void testNamedFunctionsAssignedToVariables_ThisFile_AfterEdit_ExpressionNotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "foo1Edit : Function - Global", "foo1Edit(param2) - Global",
				"foo2Edit : Function - Global", "foo2Edit(param3, param4) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_0.js", 8, 0,
				expectedProposals);
	}

	public void testNamedFunctionsAssignedToVariables_ThisFile_AfterEdit_ExpressionStarted1() throws Exception {
		String[][] expectedProposals = new String[][] { { "foo1Edit : Function - Global", "foo1Edit(param2) - Global",
				"foo2Edit : Function - Global", "foo2Edit(param3, param4) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_0.js", 10, 1,
				expectedProposals);
	}

	public void testNamedFunctionsAssignedToVariables_ThisFile_AfterEdit_ExpressionNotStarted_NegativeTest()
			throws Exception {
		String[][] expectedProposals = new String[][] { { "foo1Ignored", "foo2Ignored", "foo1EditIgnored",
				"foo2EditIgnored", "foo1 : Function - Global", "foo1(param2) - Global", "foo2 : Function - Global",
				"foo2(param3, param4) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_0.js", 8, 0,
				expectedProposals, true, false);
	}

	public void testNamedFunctionsAssignedToVariables_ThisFile_AfterEdit_ExpressionStarted1_NegativeTest()
			throws Exception {
		String[][] expectedProposals = new String[][] { { "foo1Ignored", "foo2Ignored", "foo1EditIgnored",
				"foo2EditIgnored", "foo1 : Function - Global", "foo1(param2) - Global", "foo2 : Function - Global",
				"foo2(param3, param4) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_0.js", 10, 1,
				expectedProposals, true, false);
	}

	public void testNamedFunctionsAssignedToVariables_OtherFile_AfterEdit_ExpressionNotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "foo1Edit : Function - Global", "foo1Edit(param2) - Global",
				"foo2Edit : Function - Global", "foo2Edit(param3, param4) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_1.js", 0, 0,
				expectedProposals);
	}

	public void testNamedFunctionsAssignedToVariables_OtherFile_AfterEdit_ExpressionStarted1() throws Exception {
		String[][] expectedProposals = new String[][] { { "foo1Edit : Function - Global", "foo1Edit(param2) - Global",
				"foo2Edit : Function - Global", "foo2Edit(param3, param4) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_1.js", 2, 1,
				expectedProposals);
	}

	public void testNamedFunctionsAssignedToVariables_OtherFile_AfterEdit_ExpressionNotStarted_NegativeTest()
			throws Exception {
		String[][] expectedProposals = new String[][] { { "foo1Ignored", "foo2Ignored", "foo1EditIgnored",
				"foo2EditIgnored", "foo1 : Function - Global", "foo1(param2) - Global", "foo2 : Function - Global",
				"foo2(param3, param4) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_1.js", 0, 0,
				expectedProposals, true, false);
	}

	public void testNamedFunctionsAssignedToVariables_OtherFile_AfterEdit_ExpressionStarted1_NegativeTest()
			throws Exception {
		String[][] expectedProposals = new String[][] { { "foo1Ignored", "foo2Ignored", "foo1EditIgnored",
				"foo2EditIgnored", "foo1 : Function - Global", "foo1(param2) - Global", "foo2 : Function - Global",
				"foo2(param3, param4) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_1.js", 2, 1,
				expectedProposals, true, false);
	}

	/**
	 * file -> test0_0.js
	 * funcOne -> functionOne
	 * funcTwo -> functionTwo
	 * 
	 * @throws Exception
	 */
	private static void editFile_test0_0() throws Exception {
		fTestProjectSetup.editFile("test0_0.js", 0, 9, 4, "function");
		fTestProjectSetup.editFile("test0_0.js", 4, 9, 4, "function");
	}

	private static void editFile_TestNamedFunctionsAssignedToVariables_0() throws Exception {
		fTestProjectSetup.editFile("TestNamedFunctionsAssignedToVariables_0.js", 0, 4, 4, "foo1Edit");
		fTestProjectSetup.editFile("TestNamedFunctionsAssignedToVariables_0.js", 0, 24, 4, "foo1Edit");
		fTestProjectSetup.editFile("TestNamedFunctionsAssignedToVariables_0.js", 4, 0, 4, "foo2Edit");
		fTestProjectSetup.editFile("TestNamedFunctionsAssignedToVariables_0.js", 4, 20, 4, "foo2Edit");
	}
}