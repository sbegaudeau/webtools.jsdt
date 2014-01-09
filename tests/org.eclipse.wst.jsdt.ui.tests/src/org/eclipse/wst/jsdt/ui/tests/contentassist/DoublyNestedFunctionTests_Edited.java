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

public class DoublyNestedFunctionTests_Edited extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test Doubly Nested Functions JavaScript Content Assist after Edit";

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
	public DoublyNestedFunctionTests_Edited() {
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
	public DoublyNestedFunctionTests_Edited(String name) {
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
		TestSuite ts = new TestSuite(DoublyNestedFunctionTests_Edited.class, TEST_NAME);
		
		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false) {
			public void additionalSetUp() throws Exception {
				editFile_test13_0();
			}
		};
		
		return fTestProjectSetup;
	}

	public static void editFile_test13_0() throws Exception {
		fTestProjectSetup.editFile("test13_0.js", 8, 11, 6, "edited");
		fTestProjectSetup.editFile("test13_0.js", 0, 9, 9, "editedFunc");
	}

	public void testFindInnerFunctions2_ThisFile_AfterEdit_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { { "editedFunc() - Global", "editedInnerFunc()", "innerFunc()" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_0.js", 7, 0, expectedProposals);
	}

	public void testMustFail_InnerFunctions2_ThisFile_AfterEdit_Expression1() throws Exception {
		String[][] unexpectedProposals = new String[][] { { "editedInnerFunc()", "localInnerFunc",
				"localInnerFunc(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_0.js", 2, 0, unexpectedProposals, true, false);
	}

	public void testFindInnerFunctions2_OtherFile_AfterEdit_ExpressionNotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "editedFunc() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_1.js", 0, 0, expectedProposals);
	}

	public void testMustFail_FindInnerFunctions2_OtherFile_AfterEdit_ExpressionStarted_1() throws Exception {
		String[][] unexpectedProposals = new String[][] { { "innerFunc()", "editedInnerFunc()",
				"localInnerFunc : Function", "localInnerFunc(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_1.js", 4, 1, unexpectedProposals, true, false);
	}
}