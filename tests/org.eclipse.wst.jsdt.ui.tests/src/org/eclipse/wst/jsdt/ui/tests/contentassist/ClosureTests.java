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

public class ClosureTests extends TestCase {
	/**
	 * <p>
	 * This test's name
	 * </p>
	 */
	private static final String TEST_NAME = "Test Elements Defined in Closures JavaScript Content Assist";

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
	public ClosureTests() {
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
	public ClosureTests(String name) {
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
		TestSuite ts = new TestSuite(ClosureTests.class, TEST_NAME);
		
		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false);
		
		return fTestProjectSetup;
	}

	public void testClosures_OtherFile_BeforeOpen_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { { "closure : {} - Global", "closure2 : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_1.js", 0, 0, expectedProposals);
	}

	public void testClosures_OtherFile_BeforeOpen_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "closure : {} - Global", "closure2 : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_1.js", 2, 3, expectedProposals);
	}

	public void _testClosures_OtherFile_BeforeOpen_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "nifty : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_1.js", 4, 9, expectedProposals);
	}

	public void _testClosures_OtherFile_BeforeOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "burg : String - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_1.js", 6, 10, expectedProposals);
	}

	public void testClosures_SameFile_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { { "closure : {} - Global", "closure2 : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_0.js", 12, 0, expectedProposals);
	}

	public void testClosures_SameFile_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "closure : {} - Global", "closure2 : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_0.js", 14, 3, expectedProposals);
	}

	public void testClosures_SameFile_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "nifty : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_0.js", 16, 9, expectedProposals);
	}

	public void testClosures_SameFile_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "burg : String - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_0.js", 18, 10, expectedProposals);
	}

	public void testClosures_OtherFile_AfterOpen_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { { "closure : {} - Global", "closure2 : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_1.js", 0, 0, expectedProposals);
	}

	public void testClosures_OtherFile_AfterOpen_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "closure : {} - Global", "closure2 : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_1.js", 2, 3, expectedProposals);
	}

	public void _testClosures_OtherFile_AfterOpen_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "nifty : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_1.js", 4, 9, expectedProposals);
	}


	public void _testClosures_OtherFile_AfterOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "burg : String - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_1.js", 6, 10, expectedProposals);
	}
	
	//WI97137 - JSDT: Closure functions need to pass back their return value
	public void testClosures_ReturnValue_Var() throws Exception {
		String[][] expectedProposals = new String[][] { { "NaN : Number - Number" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosureReturnValue_0.js", 20, 18, expectedProposals);
	}
	
	//WI97137 - JSDT: Closure functions need to pass back their return value
	public void testClosures_ReturnValue_Assign() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosureReturnValue_0.js", 22, 18, expectedProposals);
	}
	
	//WI97137 - JSDT: Closure functions need to pass back their return value
	public void testClosures_ReturnValue_Var_function() throws Exception {
		String[][] expectedProposals = new String[][] { { "call(Object thisObject, Object args) : Object - Function" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosureReturnValue_0.js", 24, 18, expectedProposals);
	}
	
	//WI97137 - JSDT: Closure functions need to pass back their return value
	public void testClosures_ReturnValue_InsideOtherClosure() throws Exception {
		String[][] expectedProposals = new String[][] { { "call(Object thisObject, Object args) : Object - Function" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosureReturnValue_0.js", 26, 20, expectedProposals);
	}
	
	//WI97137 - JSDT: Closure functions need to pass back their return value
	public void testClosures_ReturnValue_Var_OtherFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "NaN : Number - Number" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosureReturnValue_1.js", 0, 18, expectedProposals);
	}
	
	//WI97137 - JSDT: Closure functions need to pass back their return value
	public void testClosures_ReturnValue_Assign_OtherFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosureReturnValue_1.js", 2, 18, expectedProposals);
	}
	
	//WI97137 - JSDT: Closure functions need to pass back their return value
	public void testClosures_ReturnValue_Var_function_OtherFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "call(Object thisObject, Object args) : Object - Function" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosureReturnValue_1.js", 4, 18, expectedProposals);
	}
	
	//WI97137 - JSDT: Closure functions need to pass back their return value
	public void testClosures_ReturnValue_InsideOtherClosure_OtherFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "call(Object thisObject, Object args) : Object - Function" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosureReturnValue_1.js", 6, 20, expectedProposals);
	}
	
	// WI97001 - tests for WI97000 - we need to be able to determine the types of parameters passed into a closure function
	public void testClosure_ArgumentTypeAddedTo_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "TestClosure_ArgumentTypeAddedTo_1 : Number - Window" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosure_ArgumentTypeAddedTo_1.js", 3, 7, expectedProposals);
	}

	// WI97001 - tests for WI97000 - we need to be able to determine the types of parameters passed into a closure function
	public void testClosure_ArgumentTypeAddedTo_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "TestClosure_ArgumentTypeAddedTo_2 : Number - Window" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosure_ArgumentTypeAddedTo_2.js", 3, 7, expectedProposals);
	}
}