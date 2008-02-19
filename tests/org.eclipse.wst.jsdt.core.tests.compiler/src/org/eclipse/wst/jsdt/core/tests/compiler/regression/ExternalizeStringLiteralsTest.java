/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import java.util.Map;

import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;

import junit.framework.Test;

public class ExternalizeStringLiteralsTest extends AbstractRegressionTest {

static {
//	TESTS_NAMES = new String[] { "test000" };
//	TESTS_NUMBERS = new int[] { 16 };
//	TESTS_RANGE = new int[] { 11, -1 };
}
public ExternalizeStringLiteralsTest(String name) {
	super(name);
}
public static Test suite() {
	return buildAllCompliancesTestSuite(testClass());
}

public void test001() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.ERROR);
	this.runNegativeTest(
		new String[] {
			"A.js",
			"	function foo() {\n" + 
			"		println(\"a\");\n" + 
			"	} //$NON-NLS-1$	\n" + 
			""
		},
		"----------\n" + 
		"1. ERROR in A.js (at line 2)\n" + 
		"	println(\"a\");\n" + 
		"	                   ^^^\n" + 
		"Non-externalized string literal; it should be followed by //$NON-NLS-<n>$\n" + 
		"----------\n" + 
		"2. ERROR in A.js (at line 3)\n" + 
		"	} //$NON-NLS-1$	\n" + 
		"	  ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n",
		null,
		true,
		customOptions);
}

public void test002() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.ERROR);
	this.runNegativeTest(
		new String[] {
			"X.js",
			"	var s = null; //$NON-NLS-1$\n" +
			"	var s2 = \"\"; //$NON-NLS-1$\n" +
			"	var s3 = \"\"; //$NON-NLS-1$//$NON-NLS-2$\n" +
			"	\n" +
			"	function foo() {\n" +
			"		var s4 = null; //$NON-NLS-1$\n" +
			"		var s5 = \"\"; //$NON-NLS-1$\n" +
			"		var s6 = \"\"; //$NON-NLS-2$//$NON-NLS-1$\n" +
			"		println(\"foo\");//$NON-NLS-1$//$NON-NLS-2$\n" +
			"	} //$NON-NLS-1$\n" +
			"	//$NON-NLS-1$\n" +
			"",
		}, 
		"----------\n" + 
		"1. ERROR in X.js (at line 1)\n" + 
		"	var s = null; //$NON-NLS-1$\n" + 
		"	                 ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n" + 
		"2. ERROR in X.js (at line 3)\n" + 
		"	var s3 = \"\"; //$NON-NLS-1$//$NON-NLS-2$\n" + 
		"	                             ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n" + 
		"3. ERROR in X.js (at line 6)\n" + 
		"	var s4 = null; //$NON-NLS-1$\n" + 
		"	                  ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n" + 
		"4. ERROR in X.js (at line 8)\n" + 
		"	var s6 = \"\"; //$NON-NLS-2$//$NON-NLS-1$\n" + 
		"	                ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n" + 
		"5. ERROR in X.js (at line 9)\n" + 
		"	println(\"foo\");//$NON-NLS-1$//$NON-NLS-2$\n" + 
		"	                                       ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n" + 
		"6. ERROR in X.js (at line 10)\n" + 
		"	} //$NON-NLS-1$\n" + 
		"	  ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n",
		null,
		true,
		customOptions);
}
public void test003() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.ERROR);
	this.runNegativeTest(
		new String[] {
			"Foo.js",
			"    function foo() {\n" + 
			"		println(\"string1\" + \"string2\" //$NON-NLS-1$\n" + 
			"		);\n" + 
			"",
		}, 
		"----------\n" + 
		"1. ERROR in Foo.js (at line 2)\n" + 
		"	println(\"string1\" + \"string2\" //$NON-NLS-1$\n" + 
		"	                               ^^^^^^^^^\n" + 
		"Non-externalized string literal; it should be followed by //$NON-NLS-<n>$\n" + 
		"----------\n" + 
		"2. ERROR in p\\Foo.js (at line 4)\n" + 
		"	}\n" + 
		"	^\n" + 
		"Syntax error, insert \"}\" to complete ClassBody\n" + 
		"----------\n",
		null,
		true,
		customOptions);	
}
public void test004() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.ERROR);
	this.runConformTest(
		new String[] {
			"Foo.js",
			"    function foo() {\n" + 
			"		//$NON-NLS-1$\n" + 
			"	 };\n" + 
			"",
		}, 
		"",
		null,
		true,
		null,
		customOptions,
		null);	
}
public void test005() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.ERROR);
	this.runNegativeTest(
		new String[] {
			"X.js",
			"	function main( args) {\r\n" + 
			"		var s = \"\"; //$NON-NLS-1$//$NON-NLS-1$\r\n" + 
			"    }\r\n" + 
			"",
		}, 
		"----------\n" + 
		"1. ERROR in X.js (at line 2)\n" + 
		"	var s = \"\"; //$NON-NLS-1$//$NON-NLS-1$\n" + 
		"	                            ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n",
		null,
		true,
		customOptions);	
}
public void test006() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.ERROR);
	this.runNegativeTest(
		new String[] {
			"X.js",
			"	public static function main(String[] args) {\r\n" + 
			"		var s = \"\"; //$NON-NLS-1$//$NON-NLS-1$\r\n" + 
			"    \r\n" +
			"",
		}, 
		"----------\n" + 
		"1. ERROR in X.js (at line 2)\n" + 
		"	var s = \"\"; //$NON-NLS-1$//$NON-NLS-1$\n" + 
		"	                            ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n" + 
		"2. ERROR in X.js (at line 3)\n" + 
		"	}\n" + 
		"	^\n" + 
		"Syntax error, insert \"}\" to complete ClassBody\n" + 
		"----------\n",
		null,
		true,
		customOptions);	
}
public void test007() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.ERROR);
	this.runNegativeTest(
		new String[] {
			"X.js",
			"	function main(args) {\r\n" + 
			"		var s = null; //$NON-NLS-1$//$NON-NLS-1$\r\n" + 
			"    }\r\n" +
			"",
		}, 
		"----------\n" + 
		"1. ERROR in X.js (at line 2)\n" + 
		"	var s = null; //$NON-NLS-1$//$NON-NLS-1$\n" + 
		"	                 ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n" + 
		"2. ERROR in X.js (at line 2)\n" + 
		"	var s = null; //$NON-NLS-1$//$NON-NLS-1$\n" + 
		"	                              ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n",
		null,
		true,
		customOptions);	
}
public void test008() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.ERROR);
	this.runNegativeTest(
		new String[] {
			"X.js",
			"	function main(args) {\r\n" + 
			"		var s = \"test\"; //$NON-NLS-2$//$NON-NLS-3$\r\n" + 
			"    }\r\n" +
			"",
		}, 
		"----------\n" + 
		"1. ERROR in X.js (at line 2)\n" + 
		"	var s = \"test\"; //$NON-NLS-2$//$NON-NLS-3$\n" + 
		"	           ^^^^^^\n" + 
		"Non-externalized string literal; it should be followed by //$NON-NLS-<n>$\n" + 
		"----------\n" + 
		"2. ERROR in X.js (at line 2)\n" + 
		"	var s = \"test\"; //$NON-NLS-2$//$NON-NLS-3$\n" + 
		"	                   ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n" + 
		"3. ERROR in X.js (at line 2)\n" + 
		"	var s = \"test\"; //$NON-NLS-2$//$NON-NLS-3$\n" + 
		"	                                ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n",
		null,
		true,
		customOptions);	
}
public void test009() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.ERROR);
	this.runConformTest(
		new String[] {
			"Foo.js",
			"    function foo(i) {\n" + 
			"		println(\"test1\" + i + \"test2\"); //$NON-NLS-2$//$NON-NLS-1$\r\n" + 
			"	 };\n" + 
			"",
		}, 
		"",
		null,
		true,
		null,
		customOptions,
		null);	
}
public void test010() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.ERROR);
	this.runNegativeTest(
		new String[] {
			"X.js",
			"	function main(args) {\n" +
			"		var s = \"test\"; //$NON-NLS-2$//$NON-NLS-3$\n" +
			"		var i = s;\n" +
			"		println(s);\n" +
			"    }\n" +
			"",
		}, 
		"----------\n" + 
		"1. ERROR in X.js (at line 2)\n" + 
		"	var s = \"test\"; //$NON-NLS-2$//$NON-NLS-3$\n" + 
		"	           ^^^^^^\n" + 
		"Non-externalized string literal; it should be followed by //$NON-NLS-<n>$\n" + 
		"----------\n" + 
		"2. ERROR in X.js (at line 2)\n" + 
		"	var s = \"test\"; //$NON-NLS-2$//$NON-NLS-3$\n" + 
		"	                   ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n" + 
		"3. ERROR in X.js (at line 2)\n" + 
		"	var s = \"test\"; //$NON-NLS-2$//$NON-NLS-3$\n" + 
		"	                                ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n",
		null,
		true,
		customOptions);	
}
public void test011() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.ERROR);
	this.runNegativeTest(
		new String[] {
			"X.js",
			"	function main(args) {\n" +
			"		var i = null;\n" +
			"		var s = \"test\"; //$NON-NLS-2$//$NON-NLS-3$\n" +
			"		println(s + i);\n" +
			"    }\n" +
			"",
		}, 
		"----------\n" + 
		"2. ERROR in X.js (at line 3)\n" + 
		"	var s = \"test\"; //$NON-NLS-2$//$NON-NLS-3$\n" + 
		"	           ^^^^^^\n" + 
		"Non-externalized string literal; it should be followed by //$NON-NLS-<n>$\n" + 
		"----------\n" + 
		"3. ERROR in X.js (at line 3)\n" + 
		"	var s = \"test\"; //$NON-NLS-2$//$NON-NLS-3$\n" + 
		"	                   ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n" + 
		"4. ERROR in X.js (at line 3)\n" + 
		"	var s = \"test\"; //$NON-NLS-2$//$NON-NLS-3$\n" + 
		"	                                ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n",
		null,
		true,
		customOptions);	
}
public void test012() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.ERROR);
	this.runNegativeTest(
		new String[] {
			"X.js",
			"	function main(args) {\n" +
			"		var i = null;\n" +
			"		var s = null; //$NON-NLS-2$//$NON-NLS-3$\n" +
			"		println(s + i);\n" +
			"    }\n" +
			"",
		}, 
		"----------\n" + 
		"2. ERROR in X.js (at line 3)\n" + 
		"	var s = null; //$NON-NLS-2$//$NON-NLS-3$\n" + 
		"	                 ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n" + 
		"3. ERROR in X.js (at line 3)\n" + 
		"	var s = null; //$NON-NLS-2$//$NON-NLS-3$\n" + 
		"	                              ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n",
		null,
		true,
		customOptions);	
}
public void test013() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.ERROR);
	this.runNegativeTest(
		new String[] {
			"X.js",
			"	function main(args) {\n" +
			"		var s = \"test1\";\n" +
			"		println(s);\n" +
			"    }\n" +
			"",
		}, 
		"----------\n" + 
		"1. ERROR in X.js (at line 2)\n" + 
		"	var s = \"test1\";\n" + 
		"	           ^^^^^^^\n" + 
		"Non-externalized string literal; it should be followed by //$NON-NLS-<n>$\n" + 
		"----------\n",
		null,
		true,
		customOptions);	
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=112973
public void test014() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.ERROR);
	this.runNegativeTest(
		new String[] {
			"X.js",
			"	function main(args) {\n" +
			"		var s = \"test1\"; //$NON-NLS-?$\n" +
			"		println(s);\n" +
			"    }\n" +
			"",
		}, 
		"----------\n" + 
		"1. ERROR in X.js (at line 2)\n" + 
		"	var s = \"test1\"; //$NON-NLS-?$\n" + 
		"	           ^^^^^^^\n" + 
		"Non-externalized string literal; it should be followed by //$NON-NLS-<n>$\n" + 
		"----------\n" + 
		"2. ERROR in X.js (at line 2)\n" + 
		"	var s = \"test1\"; //$NON-NLS-?$\n" + 
		"	                    ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n",
		null,
		true,
		customOptions);	
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=114077
public void test015() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.ERROR);
	this.runNegativeTest(
		new String[] {
			"X.js",
			"	public function foo() {\n" +
			"		var s1= null; //$NON-NLS-1$\n" +
			"		var s2= \"\";\n" +
			"	}\n" +
			"",
		}, 
		"----------\n" + 
		"1. ERROR in X.js (at line 2)\n" + 
		"	var s1= null; //$NON-NLS-1$\n" + 
		"	                 ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n" + 
		"2. ERROR in X.js (at line 3)\n" + 
		"	var s2= \"\";\n" + 
		"	           ^^\n" + 
		"Non-externalized string literal; it should be followed by //$NON-NLS-<n>$\n" + 
		"----------\n",
		null,
		true,
		customOptions);	
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=114077
public void test016() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.ERROR);
	this.runNegativeTest(
		new String[] {
			"X.js",
			"	var s1= null; //$NON-NLS-1$\n" +
			"	\n" +
			"	function foo() {\n" +
			"		var s2= \"\";\n" +
			"	}\n" +
			"}",
		}, 
		"----------\n" + 
		"1. ERROR in X.js (at line 1)\n" + 
		"	private var s1= null; //$NON-NLS-1$\n" + 
		"	                         ^^^^^^^^^^^^^\n" + 
		"Unnecessary $NON-NLS$ tag\n" + 
		"----------\n" + 
		"2. ERROR in X.js (at line 4)\n" + 
		"	var s2= \"\";\n" + 
		"	           ^^\n" + 
		"Non-externalized string literal; it should be followed by //$NON-NLS-<n>$\n" + 
		"----------\n",
		null,
		true,
		customOptions);	
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=148352
public void test017() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.ERROR);
	this.runNegativeTest(
		new String[] {
			"X.js",
			"	function foo(locationInAST) {\n" +
			"		var enclosingType= \"\"; //$NON-NLS-1$\n" +
			"		if (locationInAST != null) {\n" +
			"			enclosingType.toString()\n" +
			"		}\n" +
			"	}\n" +
			"",
		}, 
		"----------\n" + 
		"1. ERROR in X.js (at line 5)\n" + 
		"	enclosingType.toString()\n" + 
		"	                       ^\n" + 
		"Syntax error, insert \";\" to complete BlockStatements\n" + 
		"----------\n",
		null,
		true,
		customOptions,
		false,
		false,
		false,
		false,
		true);	
}
public static Class testClass() {
	return ExternalizeStringLiteralsTest.class;
}
}
