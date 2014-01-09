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

public class ProposalInfoTest extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test JavaScript Content Assist Proposal Info";

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
	public ProposalInfoTest() {
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
	public ProposalInfoTest(String name) {
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
		TestSuite ts = new TestSuite(ProposalInfoTest.class, TEST_NAME);

		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false);
		
		return fTestProjectSetup;
	}

	public void testProposalInfo_OtherFile_BeforeOpen_Expression_NotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "HelloWorld() - Global", "HelloAmerica(State, City) - Global" } };
		String[][] expectedInfo = new String[][] { { "Hello World", "State" } };
		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_1.js", 0, 0, expectedProposals, expectedInfo);
	}

	public void testProposalInfo_OtherFile_BeforeOpen_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "HelloWorld() - Global",
				"HelloAmerica(State, City)  - Global" } };
		String[][] expectedInfo = new String[][] { { "Hello World", "State" } };
		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_1.js", 2, 3, expectedProposals, expectedInfo);
	}

	public void _testProposalInfo_OtherFile_BeforeOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "nodeOne : String - Global" } };
		String[][] expectedInfo = new String[][] { { "nodeOne = test" } };
		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_1.js", 4, 3, expectedProposals, expectedInfo);
	}

	public void testProposalInfo_ThisFile_Expression_NotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "HelloWorld() - Global", "HelloAmerica(State, City) - Global" } };
		String[][] expectedInfo = new String[][] { { "Hello World", "State" } };
		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_0.js", 0, 0, expectedProposals, expectedInfo);
	}

	public void testProposalInfo_ThisFile_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "HelloWorld() - Global", "HelloAmerica(State, City) - Global" } };
		String[][] expectedInfo = new String[][] { { "Hello World", "State" } };
		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_0.js", 15, 3, expectedProposals, expectedInfo);
	}

	public void _testProposalInfo_ThisFile_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "nodeOne : String - Global" } };
		String[][] expectedInfo = new String[][] { { "nodeOne = test" } };
		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_0.js", 22, 3, expectedProposals, expectedInfo);
	}

	public void testProposalInfo_OtherFile_AfterOpen_Expression_NotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "HelloWorld() - Global", "HelloAmerica(State, City) - Global" } };
		String[][] expectedInfo = new String[][] { { "Hello World", "City" } };
		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_1.js", 0, 0, expectedProposals, expectedInfo);
	}

	public void testProposalInfo_OtherFile_AfterOpen_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "HelloWorld() - Global", "HelloAmerica(State, City) - Global" } };
		String[][] expectedInfo = new String[][] { { "Hello World", "State" } };
		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_1.js", 2, 3, expectedProposals, expectedInfo);
	}

	public void _testProposalInfo_OtherFile_AfterOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "nodeOne : String - Global" } };
		String[][] expectedInfo = new String[][] { { "nodeOne = test" } };
		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_1.js", 4, 3, expectedProposals, expectedInfo);
	}
	
//	public void testWI82131() throws Exception {
//		String[][] expectedProposals = new String[][] { { "nifty : Number - {}" } };
//		String[][] expectedInfo = new String[][] { { "hi" } };
//		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_0.js", 29, 6, expectedProposals, expectedInfo);
//	}
}