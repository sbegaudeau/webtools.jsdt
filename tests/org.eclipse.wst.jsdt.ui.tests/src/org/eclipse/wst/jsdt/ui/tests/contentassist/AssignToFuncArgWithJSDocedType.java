/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
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

/**
 * <p>
 * Test Assign to Function Argument with JSDoced Type.
 * </p>
 */
public class AssignToFuncArgWithJSDocedType extends TestCase {

	private static final String TEST_NAME = "Test Assign to Function Argument with JSDoced Type";

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
	public AssignToFuncArgWithJSDocedType() {
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
	public AssignToFuncArgWithJSDocedType(String name) {
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
		TestSuite ts = new TestSuite(TEST_NAME);
		ts.addTestSuite(AssignToFuncArgWithJSDocedType_OtherFile_BeforeOpen.class);
		ts.addTestSuite(AssignToFuncArgWithJSDocedType_SameFile.class);
		ts.addTestSuite(AssignToFuncArgWithJSDocedType_OtherFile_AfterOpen.class);

		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false);
		
		return fTestProjectSetup;
	}

	public static class AssignToFuncArgWithJSDocedType_OtherFile_BeforeOpen extends TestCase {
		public void test_AssignToFuncArgWithJSDocedType_OutsideFunc_ConpleteOnFuncName() throws Exception {
			String[][] expectedProposals = new String[][] { {
				"assignToFuncArgWithJSDocedType_0(String arg0) - Global"} };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "AssignToFuncArgWithJSDocedType_1.js", 0, 8, expectedProposals);
		}
	}

	public static class AssignToFuncArgWithJSDocedType_SameFile extends TestCase {
		public void test_AssignToFuncArgWithDocedType_InsideFunc0_CompleteOnArgumentField() throws Exception {
			String[][] expectedProposals = new String[][] { { "arg0 : {}" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "AssignToFuncArgWithJSDocedType_0.js", 6, 3, expectedProposals);
		}
		
		public void test_AssignToFuncArgWithDocedType_InsideFunc0_CompleteOnArgumentName() throws Exception {
			String[][] expectedProposals = new String[][] { { "foo : Number - {}" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "AssignToFuncArgWithJSDocedType_0.js", 7, 6, expectedProposals);
		}

		public void test_AssignToFuncArgWithJSDocedType_OutsideFunc_ConpleteOnFuncName() throws Exception {
			String[][] expectedProposals = new String[][] { {
				"assignToFuncArgWithJSDocedType_0(String arg0) - Global"} };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "AssignToFuncArgWithJSDocedType_0.js", 10, 8, expectedProposals);
		}
	}

	public static class AssignToFuncArgWithJSDocedType_OtherFile_AfterOpen extends TestCase {
		public void test_AssignToFuncArgWithJSDocedType_OutsideFunc_ConpleteOnFuncName() throws Exception {
			String[][] expectedProposals = new String[][] { {
				"assignToFuncArgWithJSDocedType_0(String arg0) - Global"} };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "AssignToFuncArgWithJSDocedType_1.js", 0, 8, expectedProposals);
		}
	}
}