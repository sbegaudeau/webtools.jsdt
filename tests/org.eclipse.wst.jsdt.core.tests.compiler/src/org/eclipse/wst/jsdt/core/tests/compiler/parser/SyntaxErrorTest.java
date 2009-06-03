/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

import org.eclipse.wst.jsdt.core.compiler.CategorizedProblem;
import org.eclipse.wst.jsdt.core.tests.util.AbstractCompilerTest;
import org.eclipse.wst.jsdt.core.tests.util.Util;
import org.eclipse.wst.jsdt.internal.compiler.CompilationResult;
import org.eclipse.wst.jsdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.wst.jsdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.wst.jsdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.wst.jsdt.internal.compiler.parser.Parser;
import org.eclipse.wst.jsdt.internal.compiler.problem.DefaultProblem;
import org.eclipse.wst.jsdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.wst.jsdt.internal.compiler.problem.ProblemReporter;

public class SyntaxErrorTest extends AbstractCompilerTest {
	public static boolean optimizeStringLiterals = false;
	public static long sourceLevel = ClassFileConstants.JDK1_3; //$NON-NLS-1$
	
public SyntaxErrorTest(String testName){
	super(testName);
}
public void checkParse(
	char[] source, 
	String expectedSyntaxErrorDiagnosis,
	String testName) {

	/* using regular parser in DIET mode */
	Parser parser = 
		new Parser(
			new ProblemReporter(
				DefaultErrorHandlingPolicies.proceedWithAllProblems(), 
				new CompilerOptions(getCompilerOptions()), 
				new DefaultProblemFactory(Locale.getDefault())),
			optimizeStringLiterals);
	ICompilationUnit sourceUnit = new CompilationUnit(source, testName, null);
	CompilationResult compilationResult = new CompilationResult(sourceUnit, 0, 0, 0);	
	
	parser.parse(sourceUnit, compilationResult);

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
				}
				buffer.append("----------\n");
			}
		}
	}
	String computedSyntaxErrorDiagnosis = buffer.toString();
 	//System.out.println(Util.displayString(computedSyntaxErrorDiagnosis));
	assertEquals(
		"Invalid syntax error diagnosis" + testName,
		Util.convertToIndependantLineDelimiter(expectedSyntaxErrorDiagnosis),
		Util.convertToIndependantLineDelimiter(computedSyntaxErrorDiagnosis));
}
/*
 * Should diagnose parenthesis mismatch
 */
//TODO - fix bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=279009
public void Xtest01() {

	String s = 
		"function X() {									\n"+
		" function solve(){								\n"+
		"												\n"+
		"  var results = new X[10];						\n"+
		"  for(var i = 0; i < 10; i++){					\n"+
		"   var result = results[i];					\n"+
		"   var found = false;							\n"+
		"   for(var j = 0; j < 10; j++){				\n"+
		"    if (this == (result.documentName){			\n"+
		"     found = true;								\n"+
		"     break;									\n"+
		"    }											\n"+
		"   }											\n"+
		"  }											\n"+
		"  return andResult;							\n"+
		" }												\n"+
		"}												\n"; 

	String expectedSyntaxErrorDiagnosis =
		"----------\n" +
		"1. ERROR in <parenthesis mismatch> (at line 9)\n" + 
		"	if (this == (result.documentName){			\n" + 
		"	                                ^\n" + 
		"Syntax error, insert \") BlockStatement\" to complete BlockStatements\n" + 
		"----------\n";

	String testName = "<parenthesis mismatch>";
	checkParse(
		s.toCharArray(),
		expectedSyntaxErrorDiagnosis,
		testName);
}
/*
 * Should diagnose brace mismatch
 */
public void test02() {

	String s = 
		"function Bar() {		\n"+				
		" this.a = (fred().x{); \n"+					
		"}						\n"; 	

	String expectedSyntaxErrorDiagnosis =
		"----------\n" +
		"1. ERROR in <brace mismatch> (at line 2)\n" +
		"	this.a = (fred().x{); \n" +
		"	                  ^\n" +
		"Syntax error on token \"{\", delete this token\n" + 
		"----------\n";

	String testName = "<brace mismatch>";
	checkParse(
		s.toCharArray(),
		expectedSyntaxErrorDiagnosis,
		testName);
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=133292
public void test03() {

	String s = 
		"function X() { 												\n"+
		"    o = { s: \"Success\"; };								\n"+
		"}																\n"; 	

	String expectedSyntaxErrorDiagnosis =
		"----------\n"+
		"1. ERROR in <test> (at line 2)\n"+
		"	o = { s: \"Success\"; };								\n"+
		"	                  ^\n"+
		"Syntax error on token \";\", delete this token\n"+
		"----------\n";

	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedSyntaxErrorDiagnosis,
		testName);
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=133292
public void test04() {

	String s = 
		"function X() { 												\n"+
		"    var o = { s: \"Success\"; };								\n"+
		"}																\n"; 	

	String expectedSyntaxErrorDiagnosis =
		"----------\n"+
		"1. ERROR in <test> (at line 2)\n"+
		"	var o = { s: \"Success\"; };								\n"+
		"	                      ^\n"+
		"Syntax error on token \";\", delete this token\n"+
		"----------\n";

	String testName = "<test>";
	checkParse(
		s.toCharArray(),
		expectedSyntaxErrorDiagnosis,
		testName);
}
}
