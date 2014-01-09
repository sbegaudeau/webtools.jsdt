/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
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

public class GlobalsFieldAssignmentsTests extends TestCase {

	private static final String TEST_NAME = "Test JavaScript Fields Assigned to Globals";

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
	public GlobalsFieldAssignmentsTests() {
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
	public GlobalsFieldAssignmentsTests(String name) {
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
		ts.addTestSuite(GlobalsFieldAssignmentsTests_OtherFile_BeforeOpen_Tests.class);
		ts.addTestSuite(GlobalsFieldAssignmentsTests_SameFile_Tests.class);
		ts.addTestSuite(GlobalsFieldAssignmentsTests_OtherFile_AfterOpen_Tests.class);

		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false);
		
		return fTestProjectSetup;
	}

	public static class GlobalsFieldAssignmentsTests_OtherFile_BeforeOpen_Tests extends TestCase {
		public GlobalsFieldAssignmentsTests_OtherFile_BeforeOpen_Tests(String name) {
			super(name);
		}

		public void testFindGlobalsDefinedFromFieldAssignments_0() throws Exception {
			String[][] expectedProposals =
					new String[][] { {
							"global_FieldAssignments0 : {} - Global",
							"global_FieldAssignments2 : {} - Global",
							"global_FieldAssignments3 : {} - Global",
							"global_FieldAssignments5 : {} - Global",
							"global_FieldAssignments6 : {} - Global",
							"global_FieldAssignments7 : {} - Global",
							"global_FieldAssignments8 : {} - Global" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_1.js", 0, 18, expectedProposals,
					false, true);
		}
	}

	public static class GlobalsFieldAssignmentsTests_SameFile_Tests extends TestCase {
		public GlobalsFieldAssignmentsTests_SameFile_Tests(String name) {
			super(name);
		}

		public void testFindGlobalsDefinedFromFieldAssignments_0() throws Exception {
			String[][] expectedProposals =
					new String[][] { {
							"global_FieldAssignments0 : {} - Global",
							"global_FieldAssignments2 : {} - Global",
							"global_FieldAssignments3 : {} - Global",
							"global_FieldAssignments5 : {} - Global",
							"global_FieldAssignments6 : {} - Global",
							"global_FieldAssignments7 : {} - Global",
							"global_FieldAssignments8 : {} - Global" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 26, 18, expectedProposals,
					false, true);
		}

		public void testFindFieldsAssignedToGlobals_0() throws Exception {
			String[][] expectedProposals = new String[][] { { "global_FieldAssignments0_0 : Number - {}" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 28, 25, expectedProposals,
					false, true);
		}

		public void testFindFieldsAssignedToGlobals_3() throws Exception {
			String[][] expectedProposals = new String[][] { { "global_FieldAssignments2_0 : {} - {}" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 34, 25, expectedProposals,
					false, true);
		}

		public void testFindFieldsAssignedToGlobals_4() throws Exception {
			String[][] expectedProposals = new String[][] { { "global_FieldAssignments2_0_0 : Number - {}" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 36, 52, expectedProposals,
					false, true);
		}

		public void testFindFieldsAssignedToGlobals_5() throws Exception {
			String[][] expectedProposals = new String[][] { { "global_FieldAssignments3_0 : Number - {}" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 38, 25, expectedProposals,
					false, true);
		}

		public void testFindFieldsAssignedToGlobals_8() throws Exception {
			String[][] expectedProposals = new String[][] { { "global_FieldAssignments5_0 : {} - {}" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 46, 25, expectedProposals,
					false, true);
		}

		public void testFindFieldsAssignedToGlobals_9() throws Exception {
			String[][] expectedProposals = new String[][] { { "global_FieldAssignments5_0_0 : Number - {}" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 48, 52, expectedProposals,
					false, true);
		}

		public void testFindFieldsAssignedToGlobals_10() throws Exception {
			String[][] expectedProposals = new String[][] { { "global_FieldAssignments6_0 : Number - {}" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 50, 25, expectedProposals,
					false, true);
		}

		public void testFindFieldsAssignedToGlobals_11() throws Exception {
			String[][] expectedProposals = new String[][] { { "global_FieldAssignments7_0 : {} - {}" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 54, 25, expectedProposals,
					false, true);
		}

		public void testFindFieldsAssignedToGlobals_12() throws Exception {
			String[][] expectedProposals = new String[][] { { "global_FieldAssignments7_0_0 : Number - {}" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 56, 52, expectedProposals,
					false, true);
		}

		public void testFindFieldsAssignedToGlobals_13() throws Exception {
			String[][] expectedProposals = new String[][] { { "global_FieldAssignments8_0 : {} - {}" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 58, 25, expectedProposals,
					false, true);
		}

		public void testFindFieldsAssignedToGlobals_14() throws Exception {
			String[][] expectedProposals = new String[][] { { "global_FieldAssignments8_0_0 : Number - {}" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 60, 52, expectedProposals,
					false, true);
		}
	}

	public static class GlobalsFieldAssignmentsTests_OtherFile_AfterOpen_Tests extends TestCase {
		public GlobalsFieldAssignmentsTests_OtherFile_AfterOpen_Tests(String name) {
			super(name);
		}

		public void testFindGlobalsDefinedFromFieldAssignments_0() throws Exception {
			String[][] expectedProposals =
					new String[][] { {
							"global_FieldAssignments0 : {} - Global",
							"global_FieldAssignments2 : {} - Global",
							"global_FieldAssignments3 : {} - Global",
							"global_FieldAssignments5 : {} - Global",
							"global_FieldAssignments6 : {} - Global",
							"global_FieldAssignments7 : {} - Global",
							"global_FieldAssignments8 : {} - Global" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_1.js", 0, 18, expectedProposals,
					false, true);
		}
	}
}