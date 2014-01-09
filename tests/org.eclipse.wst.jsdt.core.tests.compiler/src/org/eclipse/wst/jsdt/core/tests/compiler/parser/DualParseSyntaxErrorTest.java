/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler.parser;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import org.eclipse.wst.jsdt.core.compiler.CategorizedProblem;
import org.eclipse.wst.jsdt.core.tests.util.AbstractCompilerTest;
import org.eclipse.wst.jsdt.core.tests.util.Util;
import org.eclipse.wst.jsdt.internal.compiler.CompilationResult;
import org.eclipse.wst.jsdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.wst.jsdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.wst.jsdt.internal.compiler.parser.Parser;
import org.eclipse.wst.jsdt.internal.compiler.problem.DefaultProblem;
import org.eclipse.wst.jsdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.wst.jsdt.internal.compiler.problem.ProblemReporter;

public class DualParseSyntaxErrorTest extends AbstractCompilerTest {
	public static boolean optimizeStringLiterals = false;
	public static long sourceLevel = ClassFileConstants.JDK1_3; //$NON-NLS-1$
	
public DualParseSyntaxErrorTest(String testName){
	super(testName);
}
public void checkParse(
	char[] source, 
	String expectedSyntaxErrorDiagnosis,
	String testName) {

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
	if (computedUnit.types != null) {
		for (int i = computedUnit.types.length; --i >= 0;){
			computedUnit.types[i].parseMethod(parser, computedUnit);
		}
	}

	StringBuffer buffer = new StringBuffer(100);
	if (compilationResult.hasProblems() || compilationResult.hasTasks()) {
		CategorizedProblem[] problems = compilationResult.getAllProblems();
		int count = problems.length;
		int problemCount = 0;
		char[] unitSource = compilationResult.compilationUnit.getContents();
		for (int i = 0; i < count; i++) { 
			if (problems[i] != null) {
				if (problemCount == 0)
					buffer.append("----------\n");
				problemCount++;
				buffer.append(problemCount + (problems[i].isError() ? ". ERROR" : ". WARNING"));
				buffer.append(" in " + new String(problems[i].getOriginatingFileName()).replace('/', '\\'));
				try {
					buffer.append(((DefaultProblem)problems[i]).errorReportSource(unitSource));
					buffer.append("\n");
					buffer.append(problems[i].getMessage());
					buffer.append("\n");
				} catch (Exception e) {
					StringWriter stringWriter = new StringWriter();
					e.printStackTrace(new PrintWriter(stringWriter));
					buffer.append(stringWriter.getBuffer());
				}
				buffer.append("----------\n");
			}
		}
	}
	String computedSyntaxErrorDiagnosis = buffer.toString();
	if(!Util.convertToIndependantLineDelimiter(expectedSyntaxErrorDiagnosis)
			.equals(Util.convertToIndependantLineDelimiter(computedSyntaxErrorDiagnosis))) {
 		System.out.println(Util.displayString(computedSyntaxErrorDiagnosis));
	}
	assertEquals(
		"Invalid syntax error diagnosis" + testName,
		Util.convertToIndependantLineDelimiter(expectedSyntaxErrorDiagnosis),
		Util.convertToIndependantLineDelimiter(computedSyntaxErrorDiagnosis));
}

public void test01() {

	String s = 
		"var fX;         						\n"+
		"function foo() {   					\n"+
		"	fX = 0;  							\n"+
		"}			  							\n"+
		"function bar() {						\n"+
		"}										\n"; 	

	String expectedSyntaxErrorDiagnosis =
		"";

	String testName = "<test1>";
	checkParse(
		s.toCharArray(),
		expectedSyntaxErrorDiagnosis,
		testName);
}
public void test02() {

	String s = 
		"var fX;         						\n"+
		"function foo() {   							\n"+
		"	fX = 0;  							\n"+
		"function bar() {						\n"+
		"}										\n"; 	

	String expectedSyntaxErrorDiagnosis =
		"----------\n" +
		"1. ERROR in <test2> (at line 5)\n" +
		"	}										\n" +
		"	^\n" +
		"Syntax error, insert \"}\" to complete MethodBody\n" +
		"----------\n";

	String testName = "<test2>";
	checkParse(
		s.toCharArray(),
		expectedSyntaxErrorDiagnosis,
		testName);
}
public void Xtest03() {

	String s = 
		"var fX;         						\n"+
		"function foo()   							\n"+
		"	fX = 0;  							\n"+
		"function bar() {						\n"+
		"}										\n"; 	

	String expectedSyntaxErrorDiagnosis =
		"----------\n" + 
		"1. ERROR in <test3> (at line 3)\n" + 
		"	void foo()   							\n" + 
		"	         ^\n" + 
		"Syntax error on token \")\", { expected after this token\n" + 
		"----------\n" + 
		"2. ERROR in <test3> (at line 4)\n" + 
		"	fX = 0;  							\n" + 
		"	      ^\n" + 
		"Syntax error, insert \"}\" to complete MethodBody\n" + 
		"----------\n";

	String testName = "<test3>";
	checkParse(
		s.toCharArray(),
		expectedSyntaxErrorDiagnosis,
		testName);
}
public void test04() {

	String s = 
		"var fX;         						\n"+
		"function foo()   							\n"+
		"	fX = 0;  							\n"+
		"} 			  							\n"+
		"function bar() {						\n"+
		"}										\n"; 	

	String expectedSyntaxErrorDiagnosis =
		"----------\n" + 
		"1. ERROR in <test4> (at line 2)\n" + 
		"	function foo()   							\n" + 
		"	             ^\n" + 
		"Syntax error on token \")\", { expected after this token\n" + 
		"----------\n";

	String testName = "<test4>";
	checkParse(
		s.toCharArray(),
		expectedSyntaxErrorDiagnosis,
		testName);
}
public void test05() {

	String s = 
		"var fX;         						\n"+
		"function foo() {  							\n"+
		"	if(true){  							\n"+
		"} 			  							\n"+
		"function bar() {						\n"+
		"}										\n"; 	

	String expectedSyntaxErrorDiagnosis =
		"----------\n" +
		"1. ERROR in <test5> (at line 6)\n" +
		"	}										\n" +
		"	^\n" +
		"Syntax error, insert \"}\" to complete MethodBody\n" +
		"----------\n";

	String testName = "<test5>";
	checkParse(
		s.toCharArray(),
		expectedSyntaxErrorDiagnosis,
		testName);
}
public void test06() {

	String s = 
		"var fX;         						\n"+
		"function foo() {  							\n"+
		"	if(true){  							\n"+
		"} 			  							\n"+
		"//comment								\n"+
		"function bar() {						\n"+
		"}										\n"; 	

	String expectedSyntaxErrorDiagnosis =
		"----------\n" +
		"1. ERROR in <test6> (at line 7)\n" +
		"	}										\n" +
		"	^\n" +
		"Syntax error, insert \"}\" to complete MethodBody\n" +
		"----------\n";

	String testName = "<test6>";
	checkParse(
		s.toCharArray(),
		expectedSyntaxErrorDiagnosis,
		testName);
}
public void test07() {

	String s = 
		"var fX;         						\n"+
		"function foo() {  							\n"+
		"	if(true){  							\n"+
		"} 			  							\n"+
		"System.out.println();					\n"+
		"function bar() {						\n"+
		"}										\n"; 	

	String expectedSyntaxErrorDiagnosis =
		"----------\n" +
		"1. ERROR in <test7> (at line 7)\n" +
		"	}										\n" +
		"	^\n" +
		"Syntax error, insert \"}\" to complete MethodBody\n" +
		"----------\n";

	String testName = "<test7>";
	checkParse(
		s.toCharArray(),
		expectedSyntaxErrorDiagnosis,
		testName);
}
public void test08() {

	String s = 
		"var fX;         						\n"+
		"function foo() {  							\n"+
		"	if(true){  							\n"+
		"} 			  							\n"+
		"var bar;							\n";  	

	String expectedSyntaxErrorDiagnosis =
		"----------\n" + 
		"1. ERROR in <test8> (at line 5)\n" + 
		"	var bar;							\n" + 
		"	       ^\n" + 
		"Syntax error, insert \"}\" to complete MethodBody\n" + 
		"----------\n";

	String testName = "<test8>";
	checkParse(
		s.toCharArray(),
		expectedSyntaxErrorDiagnosis,
		testName);
}
public void test09() {

	String s = 
		"var fX;         						\n"+
		"function foo() {  							\n"+
		"	if(true){  							\n"+
		"} 			  							\n"+
		"//comment	  							\n"+
		"var bar;							\n";  	

	String expectedSyntaxErrorDiagnosis =
		"----------\n" + 
		"1. ERROR in <test9> (at line 6)\n" + 
		"	var bar;							\n" + 
		"	       ^\n" + 
		"Syntax error, insert \"}\" to complete MethodBody\n" + 
		"----------\n";

	String testName = "<test9>";
	checkParse(
		s.toCharArray(),
		expectedSyntaxErrorDiagnosis,
		testName);
}
public void test10() {

	String s = 
		"var fX;         						\n"+
		"function foo() {  							\n"+
		"	if(true){  							\n"+
		"} 			  							\n"+
		"System.out.println();					\n"+
		"var bar;							\n";  	

	String expectedSyntaxErrorDiagnosis =
		"----------\n" + 
		"1. ERROR in <test10> (at line 6)\n" + 
		"	var bar;							\n" + 
		"	       ^\n" + 
		"Syntax error, insert \"}\" to complete MethodBody\n" + 
		"----------\n";

	String testName = "<test10>";
	checkParse(
		s.toCharArray(),
		expectedSyntaxErrorDiagnosis,
		testName);
}
}
