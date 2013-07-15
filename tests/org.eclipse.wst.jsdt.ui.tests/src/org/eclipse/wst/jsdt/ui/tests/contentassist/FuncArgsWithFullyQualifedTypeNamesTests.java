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

public class FuncArgsWithFullyQualifedTypeNamesTests extends TestCase {

	private static final String TEST_NAME = "Test JavaScript Functions with Fully Qualified Argument types";

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
	public FuncArgsWithFullyQualifedTypeNamesTests() {
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
	public FuncArgsWithFullyQualifedTypeNamesTests(String name) {
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
		ts.addTestSuite(FuncArgsWithFullyQualifedTypeNamesTests_OtherFile_BeforeOpen.class);
		ts.addTestSuite(FuncArgsWithFullyQualifedTypeNamesTests_SameFile.class);
		ts.addTestSuite(FuncArgsWithFullyQualifedTypeNamesTests_OtherFile_AfterOpen.class);

		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false);
		
		return fTestProjectSetup;
	}

	public static class FuncArgsWithFullyQualifedTypeNamesTests_OtherFile_BeforeOpen extends TestCase {

		public void testFindFuncWithFullyQualifiedArgTypeNames_OtherFile_BeforeOpen_0() throws Exception {
			String[][] expectedProposals = new String[][] { {
					"funcArgsWithFullyQualifedTypeNames(fully.Qualified blarg) - Global" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFuncArgsWithFullyQualifedTypeNames_1.js", 0, 5,
					expectedProposals, false, true);
		}
	}

	public static class FuncArgsWithFullyQualifedTypeNamesTests_SameFile extends TestCase {

		public void testFindFuncWithFullyQualifiedArgTypeNames_SameFile_0() throws Exception {
			String[][] expectedProposals = new String[][] { {
					"funcArgsWithFullyQualifedTypeNames(fully.Qualified blarg) - Global" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFuncArgsWithFullyQualifedTypeNames_0.js", 12, 5,
					expectedProposals, false, true);
		}
	}

	public static class FuncArgsWithFullyQualifedTypeNamesTests_OtherFile_AfterOpen extends TestCase {

		public void testFindFuncWithFullyQualifiedArgTypeNames_OtherFile_AfterOpen_0() throws Exception {
			String[][] expectedProposals = new String[][] { {
					"funcArgsWithFullyQualifedTypeNames(fully.Qualified blarg) - Global" } };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFuncArgsWithFullyQualifedTypeNames_1.js", 0, 5,
					expectedProposals, false, true);
		}
	}
}