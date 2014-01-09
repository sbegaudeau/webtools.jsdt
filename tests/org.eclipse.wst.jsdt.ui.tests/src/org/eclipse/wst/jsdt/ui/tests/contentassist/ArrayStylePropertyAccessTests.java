/*******************************************************************************
 * Copyright (c) 2013 IBM Corporation and others.
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

public class ArrayStylePropertyAccessTests extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test Array-style Property Access Content Assist";

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
	public ArrayStylePropertyAccessTests() {
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
	public ArrayStylePropertyAccessTests(String name) {
		super(name);
	}

	/**
	 * <p>
	 * Use this method to add these tests to a larger test suite so set up and
	 * tear down can be performed
	 * </p>
	 * 
	 * @return a {@link TestSetup} that will run all of the tests in this
	 *         class with set up and tear down.
	 */
	public static Test suite() {
		TestSuite ts = new TestSuite(ArrayStylePropertyAccessTests.class, TEST_NAME);

		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false);

		return fTestProjectSetup;
	}

	public void testPropertyAccessUsingArrayStyle_377241() throws Exception {
		// When an object property is referenced by array-style, test if
		// ContentAssist lists are properly show.
		String[][] expectedProposals = new String[][]{{"id : Number - bar"}};
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test377241.js", 5, 10, expectedProposals);		
	}

	public void testPropertyAccessUsingArrayStyle_377241_01() throws Exception {
		// When an object property is referenced by array-style, test if
		// ContentAssist lists are properly show.
		String[][] expectedProposals = new String[][]{{"id : Number - bar"}};
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup,"test377241_01.js", 5, 10, expectedProposals);
	}

	public void testPropertyAccessUsingArrayStyle_377241_02() throws Exception {
		// When an object property is referenced by array-style, test if
		// ContentAssist lists are properly show.
		String[][] expectedProposals = new String[][]{{"id : Number - bar"}};
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup,"test377241_02.js", 5, 10, expectedProposals);
	}

	public void testPropertyAccessUsingArrayStyle_377241_03() throws Exception {
		// When an object property is referenced by array-style, test if
		// ContentAssist lists are properly show.
		String[][] expectedProposals = new String[][]{{"id : Number - {}","data : String - {}"}}; 
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup,"test377241_03.js", 2, 10, expectedProposals);
	}
}