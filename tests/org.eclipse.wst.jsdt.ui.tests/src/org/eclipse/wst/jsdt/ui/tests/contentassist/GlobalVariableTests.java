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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchConstants;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchScope;
import org.eclipse.wst.jsdt.core.search.SearchEngine;
import org.eclipse.wst.jsdt.core.search.SearchMatch;
import org.eclipse.wst.jsdt.core.search.SearchParticipant;
import org.eclipse.wst.jsdt.core.search.SearchPattern;
import org.eclipse.wst.jsdt.core.search.SearchRequestor;
import org.eclipse.wst.jsdt.ui.JSdocContentAccess;
import org.eclipse.wst.jsdt.ui.tests.utils.TestProjectSetup;

public class GlobalVariableTests extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test Global Field Variables JavaScript Content Assist";

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
	public GlobalVariableTests() {
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
	public GlobalVariableTests(String name) {
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
		TestSuite ts = new TestSuite(GlobalVariableTests.class, TEST_NAME);

		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false);
		
		return fTestProjectSetup;
	}

	public void testFindGlobalVariables_OtherFile_BeforeOpen_Expression_NotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalNum : Number - Global",
				"globalString : String - Global", "globalVar - Global", "globalVarNum : Number - Global",
				"globalVarObject : {} - Global", "globalVarString : String - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 0, 0, expectedProposals);
	}

	public void testFindGlobalVariables_OtherFile_BeforeOpen_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalNum : Number - Global",
				"globalString : String - Global", "globalVar - Global", "globalVarNum : Number - Global",
				"globalVarObject : {} - Global", "globalVarString : String - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 1, 1, expectedProposals);
	}

	public void testFindGlobalVariables_OtherFile_BeforeOpen_NegativeTest_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalNum : Number - Global",
				"globalString : String - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 3, 7, expectedProposals, true, false);
	}

	public void testFindGlobalVariables_OtherFile_BeforeOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalVarNum : Number - Global",
				"globalVarObject : {} - Global", "globalVarString : String - Global", "globalVar - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 3, 7, expectedProposals);
	}

	public void testFindGlobalVariables_ThisFile_Expression_NotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalNum : Number - Global",
				"globalString : String - Global", "globalVar - Global", "globalVarNum : Number - Global",
				"globalVarObject : {} - Global", "globalVarString : String - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_0.js", 10, 0, expectedProposals);
	}

	public void testFindGlobalVariables_ThisFile_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalNum : Number - Global",
				"globalString : String - Global", "globalVarNum : Number - Global", "globalVarObject : {} - Global",
				"globalVarString : String - Global", "globalVar - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_0.js", 9, 1, expectedProposals);
	}

	public void testFindGlobalVariables_ThisFile_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalVarNum : Number - Global",
				"globalVarObject : {} - Global", "globalVarString : String - Global", "globalVar - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_0.js", 11, 7, expectedProposals);
	}

	public void testFindGlobalVariables_OtherFile_AfterOpen_Expression_NotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalNum : Number - Global",
				"globalString : String - Global", "globalVar - Global", "globalVarNum : Number - Global",
				"globalVarObject : {} - Global", "globalVarString : String - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 0, 0, expectedProposals);
	}

	public void testFindGlobalVariables_OtherFile_AfterOpen_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalNum : Number - Global",
				"globalString : String - Global", "globalVar - Global", "globalVarNum : Number - Global",
				"globalVarObject : {} - Global", "globalVarString : String - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 1, 1, expectedProposals);
	}

	public void testFindGlobalVariables_OtherFile_AfterOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalVarNum : Number - Global",
				"globalVarObject : {} - Global", "globalVarString : String - Global", "globalVar - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 3, 7, expectedProposals);
	}

	public void testFindGlobalVariables_OtherFile_AfterOpen_Negativetest_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalNum : Number - Global",
				"globalString : String - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 3, 7, expectedProposals, true, false);
	}
	
	public void testFindVariable_OtherFile_FromJAR_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"s : String - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test14_0.js", 2, 1, expectedProposals);
	}
	public void testFindAdditionalInfoFromJAR() throws Exception {
		final byte[] found = new byte[1];
		new SearchEngine().search(
			SearchPattern.createPattern("num", IJavaScriptSearchConstants.FIELD, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_PREFIX_MATCH),
			new SearchParticipant[]{SearchEngine.getDefaultSearchParticipant()},
			SearchEngine.createJavaSearchScope(new IJavaScriptElement[]{JavaScriptCore.create(fTestProjectSetup.getProject())}, IJavaScriptSearchScope.APPLICATION_LIBRARIES),
			new SearchRequestor() {
				public void acceptSearchMatch(SearchMatch match) throws CoreException {
					found[0]++;
					assertNotNull(JSdocContentAccess.getContentReader((IJavaScriptElement) match.getElement(), true));
				}
			}, 
			new NullProgressMonitor());
		assertTrue(found[0] > 0);
	}
	public void testFindAdditionalInfoFromLIB() throws Exception {
		final byte[] found = new byte[1];
		new SearchEngine().search(
			SearchPattern.createPattern("num2", IJavaScriptSearchConstants.FIELD, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_PREFIX_MATCH),
			new SearchParticipant[]{SearchEngine.getDefaultSearchParticipant()},
			SearchEngine.createJavaSearchScope(new IJavaScriptElement[]{JavaScriptCore.create(fTestProjectSetup.getProject())}, IJavaScriptSearchScope.APPLICATION_LIBRARIES),
			new SearchRequestor() {
				public void acceptSearchMatch(SearchMatch match) throws CoreException {
					found[0]++;
					assertNotNull(JSdocContentAccess.getContentReader((IJavaScriptElement) match.getElement(), true));
				}
			}, 
			new NullProgressMonitor());
		assertTrue(found[0] > 0);
	}
}