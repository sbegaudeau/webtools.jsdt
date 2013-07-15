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

public class FunctionPrototypeTests extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test that prototype (i.e. a function property) is available in content assist";

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
	public FunctionPrototypeTests() {
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
	public FunctionPrototypeTests(String name) {
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
		TestSuite ts = new TestSuite(FunctionPrototypeTests.class, TEST_NAME);

		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false);
		
		return fTestProjectSetup;
	}

	public void testPrototypeFunction_SameFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "prototype - Function", "getServerIP() - FunctionPrototype0" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFunctionPrototype_0.js", 25, 19, expectedProposals);
	}

	public void testSimpleFunction_SameFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "prototype - Function" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFunctionPrototype_0.js", 27, 10, expectedProposals);
	}

	/**
	 * @throws Exception
	 */
	public void testPrototypeFunction_OtherFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "prototype - Function", "getServerIP() - FunctionPrototype0" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFunctionPrototype_1.js", 0, 19, expectedProposals);
	}

	public void testSimpleFunction_OtherFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "prototype - Function" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFunctionPrototype_1.js", 2, 10, expectedProposals);
	}

	public void testNotGlobal_ThisFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "getServerIP()", "getClientIP" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_0.js", 37, 0, expectedProposals, true, false);
	}

	public void testNotGlobal_OtherFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "getServerIP()", "getClientIP" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 7, 0, expectedProposals, true, false);
	}
}