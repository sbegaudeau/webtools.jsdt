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

public class AlreadyDefinedFunctionAssingedToFieldTests extends TestCase {

	private static final String TEST_NAME = "Test Already Defined Functions Assigned to a Field";

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
	public AlreadyDefinedFunctionAssingedToFieldTests() {
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
	public AlreadyDefinedFunctionAssingedToFieldTests(String name) {
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
		ts.addTestSuite(AlreadyDefinedFunctionAssingedToField_OtherFile_BeforeOpen.class);
		ts.addTestSuite(AlreadyDefinedFunctionAssingedToField_SameFile.class);
		ts.addTestSuite(AlreadyDefinedFunctionAssingedToField_OtherFile_AfterOpen.class);

		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false);
		
		return fTestProjectSetup;
	}

	public static class AlreadyDefinedFunctionAssingedToField_OtherFile_BeforeOpen extends TestCase {
		public AlreadyDefinedFunctionAssingedToField_OtherFile_BeforeOpen(String name) {
			super(name);
		}

		public void testAlreadyDefinedFunctionAssingedToField_OtherFile_BeforeOpen_0() throws Exception {
			String[][] expectedProposals = new String[][] { { "func7 : Function - {}", "func8 : Function - {}",
					"func9 : Function - {}", "func7(test) : String - {}", "func8(blarg) : Number - {}",
					"func9(foo) : Boolean - {}" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestAlreadyDefinedFunctionAssingedToField_2.js", 0, 40,
					expectedProposals, false, true);
		}
	}

	public static class AlreadyDefinedFunctionAssingedToField_SameFile extends TestCase {
		public AlreadyDefinedFunctionAssingedToField_SameFile(String name) {
			super(name);
		}

		public void testAlreadyDefinedFunctionAssingedToField_SameFile_0() throws Exception {
			String[][] expectedProposals = new String[][] { { "func7 : Function - {}", "func8 : Function - {}",
					"func9 : Function - {}", "func7(test) : String - {}", "func8(blarg) : Number - {}",
					"func9(foo) : Boolean - {}" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestAlreadyDefinedFunctionAssingedToField_1.js", 6, 40,
					expectedProposals, false, true);
		}
	}

	public static class AlreadyDefinedFunctionAssingedToField_OtherFile_AfterOpen extends TestCase {
		public AlreadyDefinedFunctionAssingedToField_OtherFile_AfterOpen(String name) {
			super(name);
		}

		public void testAlreadyDefinedFunctionAssingedToField_OtherFile_AfterOpen_0() throws Exception {
			String[][] expectedProposals = new String[][] { { "func7 : Function - {}", "func8 : Function - {}",
					"func9 : Function - {}", "func7(test) : String - {}", "func8(blarg) : Number - {}",
					"func9(foo) : Boolean - {}" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestAlreadyDefinedFunctionAssingedToField_2.js", 0, 40,
					expectedProposals, false, true);
		}
	}
}