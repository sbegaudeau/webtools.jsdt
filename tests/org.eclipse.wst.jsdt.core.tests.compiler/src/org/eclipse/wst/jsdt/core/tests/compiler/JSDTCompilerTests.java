/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler;

import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.DualParseSyntaxErrorTest;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.FieldAccessCompletionTest;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.JavadocCompletionParserTest;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.NameReferenceCompletionTest;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.ParserTest;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.ReferenceTypeCompletionTest;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.SelectionTest;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.SelectionTest3;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.StatementRecoveryTest;
import org.eclipse.wst.jsdt.core.tests.compiler.parser.SyntaxErrorTest;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.ASTImplTests;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.AssignmentTest;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.BasicAnalyseTests;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.BasicJsdocTests;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.BasicParserTests;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.BasicResolveTests;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.CharOperationTest;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.CompilerInvocationTests;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.InferTypesTests;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.InternalScannerTest;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.ScannerTest;
import org.eclipse.wst.jsdt.core.tests.compiler.regression.UtilTest;
import org.eclipse.wst.jsdt.core.tests.compiler.util.ExclusionTests;
import org.eclipse.wst.jsdt.core.tests.interpret.BasicInterpretTest;
import org.eclipse.wst.jsdt.core.tests.search.SearchTests;

/**
 * Run all compiler regression tests
 */
public class JSDTCompilerTests extends TestSuite {

static {
	JavaScriptCore.getPlugin().getPluginPreferences().setValue("semanticValidation", true);
}

public JSDTCompilerTests() {
	this("JavaScript Model Tests");
}

public JSDTCompilerTests(String testName) {
	super(testName);
}
public static Test suite() {

	ArrayList standardTests = new ArrayList();
	
	// regression tests
	standardTests.add(AssignmentTest.class);
	standardTests.add(ASTImplTests.class);
	standardTests.add(BasicAnalyseTests.class);
	standardTests.add(BasicJsdocTests.class);
	standardTests.add(BasicParserTests.class);
	standardTests.add(BasicResolveTests.class);
	standardTests.add(CharOperationTest.class);
	standardTests.add(CompilerInvocationTests.class);
	standardTests.add(InferTypesTests.class);
	standardTests.add(InternalScannerTest.class);
	standardTests.add(ScannerTest.class);
	standardTests.add(UtilTest.class);
	
	// parser tests
	
	standardTests.add(DualParseSyntaxErrorTest.class);
	standardTests.add(FieldAccessCompletionTest.class);
	standardTests.add(JavadocCompletionParserTest.class);
	standardTests.add(NameReferenceCompletionTest.class);
	standardTests.add(ParserTest.class);
	standardTests.add(ReferenceTypeCompletionTest.class);
	standardTests.add(SelectionTest.class);
	standardTests.add(SelectionTest3.class);
	standardTests.add(StatementRecoveryTest.class);
	standardTests.add(SyntaxErrorTest.class);
	
	
	// interpret tests
	standardTests.add(BasicInterpretTest.class);
	
	
	TestSuite all = new TestSuite("JSDT 'Compiler' Tests");
	all.addTest(ExclusionTests.suite());
	all.addTest(SearchTests.suite());
	

	for (Iterator iter = standardTests.iterator(); iter.hasNext();) {
		Class test = (Class) iter.next();
		all.addTestSuite(test); 
	}
	
	return all;
} 
}
