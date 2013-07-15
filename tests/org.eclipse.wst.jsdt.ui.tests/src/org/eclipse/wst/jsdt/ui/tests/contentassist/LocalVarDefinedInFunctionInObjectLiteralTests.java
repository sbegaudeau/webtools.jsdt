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

public class LocalVarDefinedInFunctionInObjectLiteralTests extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test Local Variable Defined in Function in an Object Literal";

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
	public LocalVarDefinedInFunctionInObjectLiteralTests() {
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
	public LocalVarDefinedInFunctionInObjectLiteralTests(String name) {
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
		TestSuite ts = new TestSuite(LocalVarDefinedInFunctionInObjectLiteralTests.class, TEST_NAME);
		
		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false);
		
		return fTestProjectSetup;
	}

	public void testLocalVarDefinedInFuncationInOBjectLiteral_OtherFile_BeforeOpen_NegativeTest() throws Exception {
		String[][] unexpectedProposals = new String[][] { { "ninjaLocal : Number - Global", "ninjaLocal : Number" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestLocalVarDefinedInFunctionInObjectLiteral_1.js", 0, 3,
				unexpectedProposals, true, false);
	}

	public void testLocalVarDefinedInFuncationInOBjectLiteral_ThisFile_InFunction() throws Exception {
		String[][] expectedProposals = new String[][] { { "ninjaLocal : Number" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestLocalVarDefinedInFunctionInObjectLiteral_0.js", 3, 5,
				expectedProposals);
	}

	public void testLocalVarDefinedInFuncationInOBjectLiteral_ThisFile_OutsideFunction_NegativeTest() throws Exception {
		String[][] unexpectedProposals = new String[][] { { "ninjaLocal : Number - Global", "ninjaLocal : Number" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestLocalVarDefinedInFunctionInObjectLiteral_0.js", 7, 3,
				unexpectedProposals, true, false);
	}

	public void testLocalVarDefinedInFuncationInOBjectLiteral_OtherFile_AfterOpen_NegativeTest() throws Exception {
		String[][] unexpectedProposals = new String[][] { { "ninjaLocal : Number - Global", "ninjaLocal : Number" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestLocalVarDefinedInFunctionInObjectLiteral_1.js", 0, 3,
				unexpectedProposals, true, false);
	}
}