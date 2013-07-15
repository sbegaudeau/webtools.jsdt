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

public class FunctionParamsTest extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test Function Parameters JavaScript Content Assist";

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
	public FunctionParamsTest() {
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
	public FunctionParamsTest(String name) {
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
		TestSuite ts = new TestSuite(FunctionParamsTest.class, TEST_NAME);

		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false);
		
		return fTestProjectSetup;
	}

	public void testFindParamInFunctionAsLocal() throws Exception {
		String[][] expectedProposals = new String[][] { { "testfunctionParam_Fun1_param1 : Number" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFunctionParams_0.js", 2, 0, expectedProposals);
	}

	public void testDoNotFindParamOutsideFunctionAsGlobalSameFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "testfunctionParam_Fun1_param1" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFunctionParams_0.js", 5, 28, expectedProposals, true, false);
	}

	public void testDoNotFindParamOutsideFunctionAsGlobalOtherFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "testfunctionParam_Fun1_param1" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFunctionParams_1.js", 0, 28, expectedProposals, true, false);
	}
}