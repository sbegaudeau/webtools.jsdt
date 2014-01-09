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

public class InnerFunctionTests_Edited extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test Inner Functions JavaScript Content Assist after Edit";

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
	public InnerFunctionTests_Edited() {
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
	public InnerFunctionTests_Edited(String name) {
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
		TestSuite ts = new TestSuite(InnerFunctionTests_Edited.class, TEST_NAME);
		
		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false) {

			public void additionalSetUp() throws Exception {
				this.editFile("TestInnerFunctions_0.js", 2, 22, 0, "Edit");
				this.editFile("TestInnerFunctions_0.js", 6, 17, 0, "Edit");
				this.editFile("TestInnerFunctions_0.js", 10, 13, 0, "Edit");
			}
		};
		
		return fTestProjectSetup;
	}

	public void testFindInnerFunctions_OtherFile_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInnerEdit2 : Function - Global", "funcTenInnerEdit2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 0, 0, expectedProposals);
	}

	public void testFindInnerFunctions_OtherFile_ExpressionStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInnerEdit2 : Function - Global", "funcTenInnerEdit2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 2, 5, expectedProposals);
	}

	public void testFindInnerFunctions_OtherFile_CamelCase() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInnerEdit2 : Function - Global", "funcTenInnerEdit2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 4, 2, expectedProposals);
	}

	public void testFindInnerFunctions_OtherFile_EmptyLine_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInnerEdit : Function - Global",
				"funcTenInnerEdit1 : Function - Global",
				"funcTenInnerEdit(newParam111, newParam222) : String - Global", "funcTenInnerEdit1(param1) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 0, 0, expectedProposals, true, false);
	}

	public void testFindInnerFunctions_OtherFile_ExpresionStarted_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInnerEdit : Function - Global",
				"funcTenInnerEdit1 : Function - Global",
				"funcTenInnerEdit(newParam111, newParam222) : String - Global", "funcTenInnerEdit1(param1) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 2, 5, expectedProposals, true, false);
	}

	public void testFindDuplicateInnerFunctions_OtherFile_ExpressionStarted() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_1.js", 2, 5);
	}

	public void testFindDuplicateInnerFunctions_OtherFile_CamelCase() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_1.js", 4, 2);
	}

	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInnerEdit2 : Function - Global", "funcTenInnerEdit2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 32, 0, expectedProposals);
	}

	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_ExpressionStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInnerEdit2 : Function - Global", "funcTenInnerEdit2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 33, 5, expectedProposals);
	}

	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_CamelCase() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInnerEdit2 : Function - Global", "funcTenInnerEdit2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 34, 2, expectedProposals);
	}

	public void testFindInnerFunctions_SameFile_InsideInnerFunction_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInnerEdit1 : Function", "funcTenInnerEdit2 : Function - Global",
				"funcTenInnerEdit(newParam111, newParam222) : String", "funcTenInnerEdit1(param1)",
				"funcTenInnerEdit2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 27, 5, expectedProposals);
	}

	public void testFindInnerFunctions_SameFile_InsideInnerFunction_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInnerEdit1 : Function", "funcTenInnerEdit2 : Function - Global",
				"funcTenInnerEdit(newParam111, newParam222) : String", "funcTenInnerEdit1(param1)",
				"funcTenInnerEdit2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 29, 1, expectedProposals);
	}

	public void testFindInnerFunctions_SameFile_InsideInnerFunction_CamelCase() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInnerEdit1 : Function", "funcTenInnerEdit2 : Function - Global",
				"funcTenInnerEdit(newParam111, newParam222) : String", "funcTenInnerEdit1(param1)",
				"funcTenInnerEdit2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 28, 2, expectedProposals);
	}

	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_EmptyLine_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInnerEdit : Function - Global",
				"funcTenInnerEdit1 : Function - Global",
				"funcTenInnerEdit(newParam111, newParam222) : String - Global", "funcTenInnerEdit1(param1) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 32, 0, expectedProposals, true, false);
	}

	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_ExpressionStarted_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInnerEdit : Function - Global",
				"funcTenInnerEdit1 : Function - Global",
				"funcTenInnerEdit(newParam111, newParam222) : String - Global", "funcTenInnerEdit1(param1) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 33, 5, expectedProposals, true, false);
	}

	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_CamelCase_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInnerEdit : Function - Global",
				"funcTenInnerEdit1 : Function - Global",
				"funcTenInnerEdit(newParam111, newParam222) : String - Global", "funcTenInnerEdit1(param1) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 34, 2, expectedProposals, true, false);
	}
}