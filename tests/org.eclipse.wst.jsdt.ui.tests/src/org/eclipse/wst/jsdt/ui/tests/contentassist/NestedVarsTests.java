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

public class NestedVarsTests extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test Nested Local Variables in JavaScript Content Assist";

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
	public NestedVarsTests() {
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
	public NestedVarsTests(String name) {
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
		TestSuite ts = new TestSuite(NestedVarsTests.class, TEST_NAME);
		
		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false);
		
		return fTestProjectSetup;
	}

	public void testLocalVars_SameFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "l1", "l2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNestedVars_0.js", 17, 1, expectedProposals, true, false);
	}

	public void testLocalVars_SameFile2() throws Exception {
		String[][] expectedProposals = new String[][] { { "g1 : Number - Global", "g2 : Number - Global",
				"g3 : Number - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNestedVars_0.js", 18, 1, expectedProposals, false, false);
	}

	public void testLocalVars_OtherFile_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "l1", "l2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNestedVars_1.js", 0, 1, expectedProposals, true, false);
	}

	public void testLocalVars_OtherFile2() throws Exception {
		String[][] expectedProposals = new String[][] { { "g1 : Number - Global", "g2 : Number - Global",
				"g3 : Number - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNestedVars_1.js", 1, 1, expectedProposals, false, false);
	}
}