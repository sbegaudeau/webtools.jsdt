/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler.parser;

import java.util.Locale;

import org.eclipse.wst.jsdt.internal.compiler.CompilationResult;
import org.eclipse.wst.jsdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.wst.jsdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.wst.jsdt.internal.compiler.parser.Parser;
import org.eclipse.wst.jsdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.wst.jsdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.wst.jsdt.core.tests.util.AbstractCompilerTest;
import org.eclipse.wst.jsdt.core.tests.util.Util;
 
public class StatementRecoveryTest extends AbstractCompilerTest {
	public static final boolean ONLY_DIET_PLUS_BODY_WITH_STATEMENT_RECOVERY = true;
	public static boolean optimizeStringLiterals = false;
	
public StatementRecoveryTest(String testName){
	super(testName);
}
public void checkParse(
	char[] source, 
	String expectedDietUnitToString,
	String expectedDietWithStatementRecoveryUnitToString,
	String expectedDietPlusBodyUnitToString,	
	String expectedDietPlusBodyWithStatementRecoveryUnitToString,	
	String expectedFullUnitToString,
	String expectedFullWithStatementRecoveryUnitToString,
	String testName) {

	/* using regular parser in DIET mode */
	if(!ONLY_DIET_PLUS_BODY_WITH_STATEMENT_RECOVERY){
		Parser parser = 
			new Parser(
				new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(), 
					new CompilerOptions(getCompilerOptions()), 
					new DefaultProblemFactory(Locale.getDefault())),
				optimizeStringLiterals);
		parser.setMethodsFullRecovery(false);
		parser.setStatementsRecovery(false);
		
		ICompilationUnit sourceUnit = new CompilationUnit(source, testName, null);
		CompilationResult compilationResult = new CompilationResult(sourceUnit, 0, 0, 0);	
		
		CompilationUnitDeclaration computedUnit = parser.dietParse(sourceUnit, compilationResult);
		String computedUnitToString = computedUnit.toString();
		if (!expectedDietUnitToString.equals(computedUnitToString)){
			System.out.println(Util.displayString(computedUnitToString));
		}
		assertEquals(
			"Invalid unit diet structure" + testName,
			expectedDietUnitToString,
			computedUnitToString);
	}
	/* using regular parser in DIET mode and statementRecoveryEnabled */
	if(!ONLY_DIET_PLUS_BODY_WITH_STATEMENT_RECOVERY){
		Parser parser = 
			new Parser(
				new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(), 
					new CompilerOptions(getCompilerOptions()), 
					new DefaultProblemFactory(Locale.getDefault())),
				optimizeStringLiterals);

		ICompilationUnit sourceUnit = new CompilationUnit(source, testName, null);
		CompilationResult compilationResult = new CompilationResult(sourceUnit, 0, 0, 0);	
		
		CompilationUnitDeclaration computedUnit = parser.dietParse(sourceUnit, compilationResult);
		String computedUnitToString = computedUnit.toString();
		if (!expectedDietWithStatementRecoveryUnitToString.equals(computedUnitToString)){
			System.out.println(Util.displayString(computedUnitToString));
		}
		assertEquals(
			"Invalid unit diet structure with statement recovery enabled" + testName,
			expectedDietWithStatementRecoveryUnitToString,
			computedUnitToString);
	}
	/* using regular parser in DIET mode + getMethodBodies */
	if(!ONLY_DIET_PLUS_BODY_WITH_STATEMENT_RECOVERY){
		Parser parser = 
			new Parser(
				new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(), 
					new CompilerOptions(getCompilerOptions()), 
					new DefaultProblemFactory(Locale.getDefault())),
				optimizeStringLiterals);
		parser.setMethodsFullRecovery(false);
		parser.setStatementsRecovery(false);

		ICompilationUnit sourceUnit = new CompilationUnit(source, testName, null);
		CompilationResult compilationResult = new CompilationResult(sourceUnit, 0, 0, 0);	
		
		CompilationUnitDeclaration computedUnit = parser.dietParse(sourceUnit, compilationResult);
		String computedUnitToString = computedUnit.toString();
		if (!expectedDietUnitToString.equals(computedUnitToString)){
			System.out.println(Util.displayString(computedUnitToString));
		}
		assertEquals(
			"Invalid unit diet structure" + testName,
			expectedDietUnitToString,
			computedUnitToString);
		if (computedUnit.types != null) {
			for (int i = computedUnit.types.length; --i >= 0;){
				computedUnit.types[i].parseMethod(parser, computedUnit);
			}
		}
		computedUnitToString = computedUnit.toString();
		if (!expectedDietPlusBodyUnitToString.equals(computedUnitToString)){
			System.out.println(Util.displayString(computedUnitToString));
		}
		
		assertEquals(
			"Invalid unit diet+body structure" + testName,
			expectedDietPlusBodyUnitToString,
			computedUnitToString);
	}
	/* using regular parser in DIET mode + getMethodBodies and statementRecoveryEnabled */
	{
		Parser parser = 
			new Parser(
				new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(), 
					new CompilerOptions(getCompilerOptions()), 
					new DefaultProblemFactory(Locale.getDefault())),
				optimizeStringLiterals);

		ICompilationUnit sourceUnit = new CompilationUnit(source, testName, null);
		CompilationResult compilationResult = new CompilationResult(sourceUnit, 0, 0, 0);	
		
		CompilationUnitDeclaration computedUnit = parser.dietParse(sourceUnit, compilationResult);
		String computedUnitToString = computedUnit.toString();
//		if (!expectedDietWithStatementRecoveryUnitToString.equals(computedUnitToString)){
//			System.out.println(Util.displayString(computedUnitToString));
//		}
//		assertEquals(
//			"Invalid unit diet structure" + testName,
//			expectedDietWithStatementRecoveryUnitToString,
//			computedUnitToString);
//		computedUnitToString = computedUnit.toString();
		if (!expectedDietPlusBodyWithStatementRecoveryUnitToString.equals(computedUnitToString)){
			System.out.println(Util.displayString(computedUnitToString));
		}
		
		assertEquals(
			"Invalid unit diet+body structure with statement recovery enabled" + testName,
			expectedDietPlusBodyWithStatementRecoveryUnitToString,
			computedUnitToString);
	}
	/* using regular parser in FULL mode */
	if(!ONLY_DIET_PLUS_BODY_WITH_STATEMENT_RECOVERY){
		Parser parser = 
			new Parser(
				new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(), 
					new CompilerOptions(getCompilerOptions()), 
					new DefaultProblemFactory(Locale.getDefault())),
				optimizeStringLiterals);
		parser.setMethodsFullRecovery(false);
		parser.setStatementsRecovery(false);

		ICompilationUnit sourceUnit = new CompilationUnit(source, testName, null);
		CompilationResult compilationResult = new CompilationResult(sourceUnit, 0, 0, 0);	
		
		CompilationUnitDeclaration computedUnit = parser.parse(sourceUnit, compilationResult);
		String computedUnitToString = computedUnit.toString();
		if (!expectedFullUnitToString.equals(computedUnitToString)){
			System.out.println(Util.displayString(computedUnitToString));
		}
		assertEquals(
			"Invalid unit full structure" + testName,
			expectedFullUnitToString,
			computedUnitToString);

	}
	/* using regular parser in FULL mode and statementRecoveryEnabled */
	if(!ONLY_DIET_PLUS_BODY_WITH_STATEMENT_RECOVERY){
		Parser parser = 
			new Parser(
				new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(), 
					new CompilerOptions(getCompilerOptions()), 
					new DefaultProblemFactory(Locale.getDefault())),
				optimizeStringLiterals);

		ICompilationUnit sourceUnit = new CompilationUnit(source, testName, null);
		CompilationResult compilationResult = new CompilationResult(sourceUnit, 0, 0, 0);	
		
		CompilationUnitDeclaration computedUnit = parser.parse(sourceUnit, compilationResult);
		String computedUnitToString = computedUnit.toString();
		if (!expectedFullWithStatementRecoveryUnitToString.equals(computedUnitToString)){
			System.out.println(Util.displayString(computedUnitToString));
		}
		assertEquals(
			"Invalid unit full structure with statement recovery enabled" + testName,
			expectedFullWithStatementRecoveryUnitToString,
			computedUnitToString);

	}
}

public void test0001() {

	String s = 
			"function foo() {							\n" +
			"    System.out.println();					\n" +
			"}											\n"; 	

	String expectedDietUnitToString = 
		"function foo() {\n" + 
    	 "  System.out.println();\n"+
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		expectedDietPlusBodyUnitToString;
	
	String expectedFullUnitToString =
		expectedDietPlusBodyUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedFullUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0002() {

	String s = 
		" function foo() {								\n"
		+ "    #                    					\n"
		+ "    System.out.println();					\n"
		+ "}											\n"; 	

	String expectedDietUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}

public void test0002a() {

	String s = 
		  "    System.out.print1();					\n"
		 + "    #                    					\n"
		+ "    System.out.print2();					\n"
		+ "											\n"; 	

	String expectedDietUnitToString = 
   	    "System.out.print1();\n"+
   	    "System.out.print2();\n"+
 		"";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
   	    "  System.out.println();\n"+
		"\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
   	    "System.out.print1();\n"+
   	    "System.out.print2();\n"+
 		"";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}

public void test0002b() {

	String s = 
		"  foo = function () {								\n"
		+ "    #                    					\n"
		+ "    System.out.println();					\n"
		+ "}											\n"; 	

	String expectedDietUnitToString = 
		"foo = function () {\n" + 
   	    "  System.out.println();\n"+
		"};\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"foo = function () {\n" + 
   	    "  System.out.println();\n"+
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"foo = function () {\n" + 
   	    "  System.out.println();\n"+
		"};\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}




public void test0002c() {

	String s = 
		"  obj ={								\n"
		+"   p1 : 2,							\n"
		+" meth: function () {								\n"
		+ "    #                    					\n"
		+ "    FOO();					\n"
		+ "}											\n" 	
		+ "}											\n"; 	

	String expectedDietUnitToString = 
		"obj = {\n" + 
		"  p1 : 2,\n" +
		"  meth : function () {\n" +
   	    "  FOO();\n" +
   	    "}\n" +
		"};\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"obj = {\n" + 
		"  p1 : 2,\n" +
		"  meth : function () {\n" +
   	    "  FOO();\n" +
   	    "}\n" +
		"};\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"obj = {\n" + 
		"  p1 : 2,\n" +
		"  meth : function () {\n" +
   	    "  FOO();\n" +
   	    "}\n" +
		"};\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0003() {

	String s = 
		"  function foo() {								\n"
		+ "    System.out.println();					\n"
		+ "    #                    					\n"
		+ "}											\n"; 	

	String expectedDietUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}


public void test0003b() {

	String s = 
		"  function foo1() {								\n"
		+ "    var i;					\n"
		+ "}											\n" 	
		+ "  function foo2() {								\n"
		+ "    #                    					\n"
		+ "    a=1;					\n"
		+ "}											\n" 	
		+ "  function foo3() {								\n"
		+ "    var k;					\n"
		+ "}											\n"; 	

	String expectedDietUnitToString = 
		"function foo1() {\n" + 
		"}\n" +
		"function foo2() {\n" + 
		"}\n" +
		"function foo3() {\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function foo1() {\n" + 
		"  var i;\n" +
		"}\n" +
		"function foo2() {\n" + 
		"  a = 1;\n" +
		"}\n" +
		"function foo3() {\n" + 
		"  var k;\n" +
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function foo1() {\n" + 
		"  var i;\n" +
		"}\n" +
		"function foo2() {\n" + 
		"  a = 1;\n" +
		"}\n" +
		"function foo3() {\n" + 
		"  var k;\n" +
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}



public void test0004() {

	String s = 
		"  function foo() {								\n"
		+ "    #                    					\n"
		+ "    System.out.println();					\n"
		+ "    System.out.println();					\n"
		+ "}											\n"; 	

	String expectedDietUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0005() {

	String s = 
		"  function foo() {								\n"
		+ "    System.out.println();					\n"
		+ "    System.out.println();					\n"
		+ "    #                    					\n"
		+ "}											\n"; 	

	String expectedDietUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0006() {

	String s = 
		"function foo() {\n"  
		+ "    System.out.println();					\n"
		+ "    System.out.println();					\n"
		+ "    #                    					\n"
		+ "    System.out.println();					\n"
		+ "    System.out.println();					\n"
		+"}\n";

	String expectedDietUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function foo() {\n" + 
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
   	    "  System.out.println();\n"+
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0007() {

	String s = 
		"function foo() {\n"  
		+ "    #                    					\n"
		+ "    System.out.println();					\n"
		+ "    if(true) {								\n"
		+ "      System.out.println();					\n"
		+ "    }										\n"
		+ "    System.out.println();					\n"
		+"}\n";

	String expectedDietUnitToString = 
		"function foo() {\n" 
		+ "  System.out.println();\n"
		+ "  if (true)\n" 
		+ "      {\n"
		+ "        System.out.println();\n"
		+ "      }\n"
		+ "  System.out.println();\n"
		+"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function foo() {\n" 
		+ "  System.out.println();\n"
		+ "  if (true)\n" 
		+ "      {\n"
		+ "        System.out.println();\n"
		+ "      }\n"
		+ "  System.out.println();\n"
		+"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function foo() {\n" 
		+ "  System.out.println();\n"
		+ "  if (true)\n" 
		+ "      {\n"
		+ "        System.out.println();\n"
		+ "      }\n"
		+ "  System.out.println();\n"
		+"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0008() {

	String s = 
			"function X() {								\n"
			+ "  function foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    if(true) {								\n"
			+ "      System.out.println();					\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "    #                    					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"function X() {\n" + 
		"  function foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function X() {\n" +  
		"  function foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function X() {\n" + 
		"  function foo() {\n" + 
		"    System.out.println();\n" + 
		"    if (true)\n" + 
		"        {\n" + 
		"          System.out.println();\n" + 
		"        }\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0009() {

	String s = 
			"function X() {								\n"
			+ "  function foo() {								\n"
			+ "    System.out.println();					\n"
			+ "    if(true) {								\n"
			+ "      System.out.println();					\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "    #                    					\n"
			+ "    System.out.println();					\n"
			+ "    if(true) {								\n"
			+ "      System.out.println();					\n"
			+ "    }										\n"
			+ "    System.out.println();					\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"function X() {\n" + 
		"  function foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function X() {\n" + 
		"  function foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function X() {\n" + 
		"  function foo() {\n" + 
		"    System.out.println();\n" + 
		"    if (true)\n" + 
		"        {\n" + 
		"          System.out.println();\n" + 
		"        }\n" + 
		"    System.out.println();\n" + 
		"    System.out.println();\n" + 
		"    if (true)\n" + 
		"        {\n" + 
		"          System.out.println();\n" + 
		"        }\n" + 
		"    System.out.println();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0010() {

	String s = 
			"function X() {								\n"
			+ "  function foo() {								\n"
			+ "    bar(\\u0029								\n"
			+ "  }											\n"
			+ "}											\n"; 	

	String expectedDietUnitToString = 
		"function X() {\n" + 
		"  function foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function X() {\n" + 
		"  function foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function X() {\n" + 
		"  function foo() {\n" + 
		"    bar();\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0011() {

	String s = 
			"function X() {								\n"
			+ "  function foo() {								\n"
			+ "    if(true) {								\n"
			+ "      foo();									\n"
			+ "    }										\n"
			+ "    for(;									\n"
			+ "    if(true) {								\n"
			+ "      foo();									\n"
			+ "    }										\n"
			+ "  }											\n"
			+ "}											\n";

	String expectedDietUnitToString = 
		"function X() {\n" + 
		"  function foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function X() {\n" + 
		"  function foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function X() {\n" + 
		"}\n" + 
		"function foo() {\n" + 
		"  if (true)\n" + 
		"      {\n" + 
		"        foo();\n" + 
		"      }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0012() {

	String s = 
			"function X() {								\n"
			+ "  function foo() {								\n"
			+ "    if() {									\n"
			+ "      foo();									\n"
			+ "    }										\n"
			+ "  }											\n"
			+ "}											\n";

	String expectedDietUnitToString = 
		"function X() {\n" + 
		"  function foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function X() {\n" + 
		"  function foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function X() {\n" + 
		"  function foo() {\n" + 
		"    if ($missing$)\n" + 
		"        {\n" + 
		"          foo();\n" + 
		"        }\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}
public void test0013() {

	String s = 
			"function X() {								\n"
			+ "  function foo() {								\n"
			+ "    for(var i								\n"
			+ "  }											\n"
			+ "}											\n";

	String expectedDietUnitToString = 
		"function X() {\n" + 
		"  function foo() {\n" + 
		"  }\n" + 
		"}\n";
	
	String expectedDietWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String expectedDietPlusBodyUnitToString = 
		"function X() {\n" + 
		"  function foo() {\n" + 
		"  }\n" + 
		"}\n";

	String expectedDietPlusBodyWithStatementRecoveryUnitToString = 
		"function X() {\n" + 
		"}\n" +
		"function foo() {\n" + 
		"  var i;\n" + 
		"}\n";
	
	String expectedFullUnitToString =
		expectedDietUnitToString;
	
	String expectedFullWithStatementRecoveryUnitToString =
		expectedDietUnitToString;
	
	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedDietUnitToString,
		expectedDietWithStatementRecoveryUnitToString,
		expectedDietPlusBodyUnitToString,
		expectedDietPlusBodyWithStatementRecoveryUnitToString,
		expectedFullUnitToString,
		expectedFullWithStatementRecoveryUnitToString,
		testName);
}

}
