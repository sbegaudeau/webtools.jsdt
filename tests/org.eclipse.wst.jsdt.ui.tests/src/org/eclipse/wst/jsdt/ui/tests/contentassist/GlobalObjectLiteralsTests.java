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

public class GlobalObjectLiteralsTests extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test JavaScript Content Assist for Global Object Literals";

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
	public GlobalObjectLiteralsTests() {
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
	public GlobalObjectLiteralsTests(String name) {
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
		TestSuite ts = new TestSuite(GlobalObjectLiteralsTests.class, TEST_NAME);
		
		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false);
		
		return fTestProjectSetup;
	}

	public void testFindGlobalObjectLiteral_OtherFile_BeforeOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "org : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 0, 0, expectedProposals);
	}

	public void testFindGlobalObjectLiteral_OtherFile_BeforeOpen_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "org : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 2, 1, expectedProposals);
	}

	public void testFindFieldOnGlobalObjectLiteral_OtherFile_BeforeOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "eclipse : {} - {}", "eclipse2 : {} - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 4, 4, expectedProposals);
	}

	public void testFindFieldOnGlobalObjectLiteral_OtherFile_BeforeOpen_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "eclipse : {} - {}", "eclipse2 : {} - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 4, 5, expectedProposals);
	}

	public void testFindFunctionOnFieldOnGlobalObjectLiteral_OtherFile_BeforeOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "fun() - {}", "crazy() - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 6, 12, expectedProposals);
	}

	public void testFindGlobalObjectLiteral_SameFile_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "org : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 6, 0, expectedProposals);
	}

	public void testFindGlobalObjectLiteral_SameFile_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "org : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 8, 1, expectedProposals);
	}

	public void testFindFieldOnGlobalObjectLiteral_SameFile_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "eclipse : {} - {}", "eclipse2 : {} - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 10, 4, expectedProposals);
	}

	public void testFindFieldOnGlobalObjectLiteral_SameFile_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "eclipse : {} - {}", "eclipse2 : {} - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 10, 5, expectedProposals);
	}

	public void testFindFunctionOnFieldOnGlobalObjectLiteral_SameFile_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "fun() - {}", "crazy() - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 12, 12, expectedProposals);
	}

	public void testFindGlobalObjectLiteral_OtherFile_AfterOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "org : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 0, 0, expectedProposals);
	}

	public void testFindGlobalObjectLiteral_OtherFile_AfterOpen_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "org : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 2, 1, expectedProposals);
	}

	public void testFindFieldOnGlobalObjectLiteral_OtherFile_AfterOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "eclipse : {} - {}", "eclipse2 : {} - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 4, 4, expectedProposals);
	}

	public void testFindFieldOnGlobalObjectLiteral_OtherFile_AfterOpen_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "eclipse : {} - {}", "eclipse2 : {} - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 4, 5, expectedProposals);
	}

	public void testFindFunctionOnFieldOnGlobalObjectLiteral_OtherFile_AfterOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "fun() - {}", "crazy() - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 6, 12, expectedProposals);
	}

	public void testFindDuplicateGlobalObjectLiteral_OtherFile_1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 2, 1);
	}

	public void testFindDuplicateFieldOnGlobalObjectLiteral_OtherFile_0() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 4, 4);
	}

	public void testFindDuplicateFieldOnGlobalObjectLiteral_OtherFile_1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 4, 5);
	}

	public void testFindDuplicateFunctionOnFieldOnGlobalObjectLiteral_OtherFile_0() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 6, 12);
	}

	public void testFindDuplicateGlobalObjectLiteral_SameFile_1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 8, 1);
	}

	public void testFindDuplicateFieldOnGlobalObjectLiteral_SameFile_0() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 10, 4);
	}

	public void testFindDuplicateFieldOnGlobalObjectLiteral_SameFile_1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 10, 5);
	}

	public void testFindDuplicateFunctionOnFieldOnGlobalObjectLiteral_SameFile_0() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 12, 12);
	}
	
//	// WI102885
//	public void testNoLeakedGlobal_SameFile() throws Exception {
//		String[][] expectedProposals = new String[][] { { "globalObjLitTestVar" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 21, 19, expectedProposals, true, false);
//	}
	
	// WI102885
	public void testNoLeakedGlobal_OtherFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalObjLitTestVar" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 8, 19, expectedProposals, true, false);
	}
}