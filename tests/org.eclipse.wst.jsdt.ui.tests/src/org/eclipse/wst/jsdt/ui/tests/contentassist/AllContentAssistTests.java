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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.jsdt.ui.tests.utils.TestProjectSetup;

/**
 * <p>
 * Test suite containing all JSDT content assist tests.
 * </p>
 */
public class AllContentAssistTests extends TestSuite {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "All Content Assist Tests";
	
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
	public AllContentAssistTests() {
		this(TEST_NAME);
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
	public AllContentAssistTests(String name) {
		super(name);
	}

	public static Test suite() {
		TestSuite all = new TestSuite(TEST_NAME);
		
		all.addTest(GlobalFunctionTests.suite());
		all.addTest(GlobalVariableTests.suite());
		all.addTest(ConstructorTests.suite());
		all.addTest(InnerFunctionTests.suite());
		all.addTest(DoublyNestedFunctionTests.suite());
		all.addTest(CamelCasingTests.suite());
		all.addTest(OtherContentAssistTests.suite());		
		all.addTest(TypeTests.suite());
		all.addTest(DuplicatesTests.suite());
		all.addTest(StaticTests.suite());
		all.addTest(GlobalObjectLiteralsTests.suite());
		all.addTest(ClosureTests.suite());
		all.addTest(LocalVarDefinedInFunctionInObjectLiteralTests.suite());
		all.addTest(ProposalInfoTest.suite());
		all.addTest(BrowserLibraryTests.suite());
		all.addTest(NestedVarsTests.suite());
		all.addTest(GlobalVariable_LocalDeclaration_DefinedInOneFile_Tests.suite());
		all.addTest(GlobalVariable_LocalDeclaration_DefinedInMultiFiles_Tests.suite());
		all.addTest(GlobalVariable_AssignmentOnly_DefinedInOneFile_Tests.suite());
		all.addTest(FunctionPrototypeTests.suite());
		all.addTest(FunctionParamsTest.suite());
		all.addTest(FuncArgsWithFullyQualifedTypeNamesTests.suite());
		all.addTest(AssignToFuncArgWithJSDocedType.suite());
		all.addTest(OrderOfRecomendationsTests.suite());
		all.addTest(AlreadyDefinedFunctionAssingedToFieldTests.suite());
		all.addTest(GlobalsFieldAssignmentsTests.suite());
		all.addTest(GlobalShadowedByFuncArgTests.suite());
		all.addTest(AddToNavigatorTests.suite());
		all.addTest(Dom5LibraryTests.suite());
		all.addTest(NestedWithinParenthesesTests.suite());
		all.addTest(ArrayStylePropertyAccessTests.suite());
		
		// tests that do editing to the files
		all.addTest(GlobalFunctionTests_Edited.suite());
		all.addTest(GlobalVariableTests_Edited.suite());
		all.addTest(ConstructorTests_Edited.suite());
		all.addTest(InnerFunctionTests_Edited.suite());
		all.addTest(DoublyNestedFunctionTests_Edited.suite());
		all.addTest(CamelCasingTests_Edited.suite());
		all.addTest(TypeTests_Edited.suite());
		all.addTest(StaticTests_Edited.suite());
		all.addTest(ProposalInfoTest_Edited.suite());
		
		//delete the project after running all JSDT content assist tests
		return new TestProjectSetup(all, "ContentAssist", "root", true);
	}
}