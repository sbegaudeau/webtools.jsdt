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

public class InnerFunctionTests extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test Inner Functions JavaScript Content Assist";

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
	public InnerFunctionTests() {
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
	public InnerFunctionTests(String name) {
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
		TestSuite ts = new TestSuite(InnerFunctionTests.class, TEST_NAME);
		
		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false);
		
		return fTestProjectSetup;
	}

	public void testFindInnerFunctions_OtherFile_BeforeOpen_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInner2 : Function - Global", "funcTenInner2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 0, 0, expectedProposals);
	}

	public void testFindInnerFunctions_OtherFile_BeforeOpen_ExpressionStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInner2 : Function - Global", "funcTenInner2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 2, 5, expectedProposals);
	}

	public void testFindInnerFunctions_OtherFile_BeforeOpen_CamelCase() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInner2 : Function - Global", "funcTenInner2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 4, 2, expectedProposals);
	}

	public void testFindInnerFunctions_OtherFile_BeforeOpen_EmptyLine_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInner : Function - Global",
				"funcTenInner1 : Function - Global", "funcTenInner(newParam111, newParam222) : String - Global",
				"funcTenInner1(param1) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 0, 0, expectedProposals, true, false);
	}

	public void testFindInnerFunctions_OtherFile_BeforeOpen_ExpresionStarted_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInner : Function - Global",
				"funcTenInner1 : Function - Global", "funcTenInner(newParam111, newParam222) : String - Global",
				"funcTenInner1(param1) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 2, 5, expectedProposals, true, false);
	}

	public void testFindDuplicateInnerFunctions_OtherFile_BeforeOpen_ExpressionStarted() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_1.js", 2, 5);
	}

	public void testFindDuplicateInnerFunctions_OtherFile_BeforeOpen_CamelCase() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_1.js", 4, 2);
	}
	
	public void testFindInnerFunctions_SameFile_InsideInnerFunction_InsideFunctionCall_ExpressionStarted()
	throws Exception {
		String[][] expectedProposals = new String[][] { { "subtract(x, y) : Number" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 24, 3, expectedProposals);
	}
	
	public void testFindInnerFunctions_SameFile_InsideInnerFunction_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInner1 : Function", "funcTenInner2 : Function - Global", "funcTenInner3 : Function",
				"funcTenInner(newParam111, newParam222) : String", "funcTenInner1(param1)",
				"funcTenInner2(param1, param2) - Global", "funcTenInner3(param1, param2)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 26, 0, expectedProposals);
	}
	
	public void testFindInnerFunctions_SameFile_InsideInnerFunction_ExpressionStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
			"funcTenInner1 : Function", "funcTenInner2 : Function - Global", "funcTenInner3 : Function",
			"funcTenInner(newParam111, newParam222) : String", "funcTenInner1(param1)",
			"funcTenInner2(param1, param2) - Global", "funcTenInner3(param1, param2)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 27, 5, expectedProposals);
	}
	
	public void testFindInnerFunctions_SameFile_InsideInnerFunction_CamelCase() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
			"funcTenInner1 : Function", "funcTenInner2 : Function - Global", "funcTenInner3 : Function",
			"funcTenInner(newParam111, newParam222) : String", "funcTenInner1(param1)",
			"funcTenInner2(param1, param2) - Global", "funcTenInner3(param1, param2)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 28, 2, expectedProposals);
	}
	
	public void testFindInnerFunctions_SameFile_InsideInnerFunction_ExpressionStarted2() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
			"funcTenInner1 : Function", "funcTenInner2 : Function - Global", "funcTenInner3 : Function",
			"funcTenInner(newParam111, newParam222) : String", "funcTenInner1(param1)",
			"funcTenInner2(param1, param2) - Global", "funcTenInner3(param1, param2)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 29, 1, expectedProposals);
	}

	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInner2 : Function - Global", "funcTenInner2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 32, 0, expectedProposals);
	}

	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_ExpressionStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInner2 : Function - Global", "funcTenInner2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 33, 5, expectedProposals);
	}

	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_CamelCase() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInner2 : Function - Global", "funcTenInner2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 34, 2, expectedProposals);
	}

	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_EmptyLine_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInner : Function - Global",
				"funcTenInner1 : Function - Global", "funcTenInner3 : Function", "funcTenInner(newParam111, newParam222) : String - Global",
				"funcTenInner1(param1) - Global", "funcTenInner3(param1, param2)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 32, 0, expectedProposals, true, false);
	}

	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_ExpressionStarted_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInner : Function - Global",
			"funcTenInner1 : Function - Global", "funcTenInner3 : Function", "funcTenInner(newParam111, newParam222) : String - Global",
			"funcTenInner1(param1) - Global", "funcTenInner3(param1, param2)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 33, 5, expectedProposals, true, false);
	}

	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_CamelCase_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInner : Function - Global",
			"funcTenInner1 : Function - Global", "funcTenInner3 : Function", "funcTenInner(newParam111, newParam222) : String - Global",
			"funcTenInner1(param1) - Global", "funcTenInner3(param1, param2)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 34, 2, expectedProposals, true, false);
	}

	public void testFindDuplicateInnerFunctions_SameFile_OutsideInnerFunction_ExpressionStarted_0() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_0.js", 33, 5);
	}

	public void testFindDuplicateInnerFunctions_SameFile_OutsideInnerFunction_ExpressionStarted_1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_0.js", 37, 1);
	}

	public void testFindDuplicateInnerFunctions_SameFile_OutsideInnerFunction_CamelCase() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_0.js", 34, 2);
	}

	public void testFindDuplicateInnerFunctions_SameFile_InsideInnerFunction_ExpressionStarted_0() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_0.js", 27, 5);
	}

	public void testFindDuplicateInnerFunctions_SameFile_InsideInnerFunction_ExpressionStarted_1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_0.js", 29, 1);
	}

	public void testFindDuplicateInnerFunctions_SameFile_InsideInnerFunction_CamelCase() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_0.js", 28, 2);
	}

	public void testFindInnerFunctions_OtherFile_AfterOpen_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInner2 : Function - Global", "funcTenInner2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 0, 0, expectedProposals);
	}

	public void testFindInnerFunctions_OtherFile_AfterOpen_ExpressionStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInner2 : Function - Global", "funcTenInner2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 2, 5, expectedProposals);
	}

	public void testFindInnerFunctions_OtherFile_AfterOpen_CamelCase() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInner2 : Function - Global", "funcTenInner2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 4, 2, expectedProposals);
	}

	public void testFindInnerFunctions_OtherFile_AfterOpen_EmptyLine_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInner : Function - Global",
				"funcTenInner1 : Function - Global", "funcTenInner(newParam111, newParam222) : String - Global",
				"funcTenInner1(param1) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 0, 0, expectedProposals, true, false);
	}

	public void testFindInnerFunctions_OtherFile_AfterOpen_ExpresionStarted_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInner : Function - Global",
				"funcTenInner1 : Function - Global", "funcTenInner(newParam111, newParam222) : String - Global",
				"funcTenInner1(param1) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 2, 5, expectedProposals, true, false);
	}

	public void testFindDuplicateInnerFunctions_OtherFile_AfterOpen_ExpressionStarted_0() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_1.js", 0, 1);
	}

	public void testFindDuplicateInnerFunctions_OtherFile_AfterOpen_ExpressionStarted_1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_1.js", 2, 5);
	}

	public void testFindDuplicateInnerFunctions_OtherFile_AfterOpen_CamelCase() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_1.js", 4, 2);
	}
}