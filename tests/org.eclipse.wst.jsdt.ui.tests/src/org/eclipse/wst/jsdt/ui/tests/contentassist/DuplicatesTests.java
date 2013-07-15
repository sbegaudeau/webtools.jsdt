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

public class DuplicatesTests extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test for Duplicate Content Assist Proposals";

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
	public DuplicatesTests() {
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
	public DuplicatesTests(String name) {
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
		TestSuite ts = new TestSuite(DuplicatesTests.class, TEST_NAME);
		
		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false);
		
		return fTestProjectSetup;
	}

	public void testForDuplicates_Expression1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestConstructorCamelCase_0.js", 17, 6);
	}

	public void testForDuplicates_Expression2() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestConstructorCamelCase_0.js", 19, 7);
	}

	public void testForDuplicates_Expression3() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestConstructorCamelCase_0.js", 21, 8);
	}

	public void testForDuplicates_Expression4() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestConstructorCamelCase_1.js", 2, 7);
	}

	public void testForDuplicates_Expression5() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestConstructorCamelCase_1.js", 4, 8);
	}

	public void testForDuplicates_Expression6() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test2_1.js", 2, 6);
	}

	public void testForDuplicates_Expression7() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test2_1.js", 6, 10);
	}

	public void testForDuplicates_Expression8() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test2_1.js", 10, 5);
	}

	public void testForDuplicates_Expression9() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test2_0.js", 19, 6);
	}

	public void testForDuplicates_Expression10() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test2_0.js", 23, 9);
	}

	public void testForDuplicates_Expression11() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test2_0.js", 29, 5);
	}

	public void testForDuplicates_Expression12() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test6.js", 0, 8);
	}

	public void testForDuplicates_Expression13() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test7.js", 5, 8);
	}

	public void testForDuplicates_Expression14() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test9_1.js", 0, 7);
	}

	public void testForDuplicates_Expression15() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test9_0.js", 7, 7);
	}

	public void testForDuplicates_Expression16() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "ThisReferenceInMemberAndStaticFunctions_1.js", 0, 6);
	}

	public void testForDuplicates_Expression17() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "ThisReferenceInMemberAndStaticFunctions_0.js", 9, 6);
	}

	public void testForDuplicates_InsideDoubleNestedFunctionDeclaration_0() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test13_0.js", 4, 3);
	}

	public void testForDuplicates_InsideDoubleNestedFunctionDeclaration_1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test13_0.js", 5, 3);
	}

	public void testForDuplicates_InsideFunctionDeclaration_0() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test13_0.js", 1, 2);
	}

	public void testForDuplicates_InsideFunctionDeclaration_1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test13_0.js", 2, 2);
	}

	public void testForDuplicates_Expression20() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test13_1.js", 4, 1);
	}

	public void testForDuplicates_Expression21() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test0_0.js", 51, 1);
	}

	public void testForDuplicates_Expression22() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test0_0.js", 59, 4);
	}

	public void testForDuplicates_Expression23() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test0_1.js", 2, 3);
	}

	public void testForDuplicates_Expression24() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test0_1.js", 4, 5);
	}

	public void testForDuplicates_Expression26() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_0.js", 10, 1);
	}

	public void testForDuplicates_Expression27() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_1.js", 2, 1);
	}

	public void testForDuplicates_Expression30() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test11_1.js", 3, 7);
	}

	public void testForDuplicates_Expression31() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test11_0.js", 11, 7);
	}

	public void testForDuplicates_Expression39() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test10_1.js", 1, 4);
	}
}