/*******************************************************************************
 * Copyright (c) 2010, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;

public class BasicAnalyseTests extends AbstractRegressionTest {
	public BasicAnalyseTests(String name) {
		super(name);
	}
	
	public void testBug251374_1() {
		Map custom = new HashMap();
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.nullReference", "error");
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.potentialNullReference", "error");
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var b = null;\n" +
						"function boo() {\n" +
						"b.toString();\n" +
						"}\n" +
						"b = 2;\n" +
						"boo();" 
				},
				"", null, false, custom
		);
	}
	public void testBug251374_2() {
		Map custom = new HashMap();
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.nullReference", "error");
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.potentialNullReference", "error");
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var b = null;\n" +
						"function boo() {\n" +
						"b.toString();\n" +
						"}\n" +
						"boo();" 
				},""
//				"----------\n" + 
//				"1. ERROR in X.js (at line 3)\n" + 
//				"	b.toString();\n" + 
//				"	^\n" + 
//				"Null pointer access: The variable b can only be null at this location\n" + 
//				"----------\n" 
				, null, false, custom
		);
	}
	
	public void testBug251374_3() {
		Map custom = new HashMap();
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.nullReference", "error");
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.potentialNullReference", "error");
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var b = null;\n" +
						"function boo() {\n" +
						"b = null\n" +
						"b.toString();\n" +
						"}\n" +
						"b = 2;\n" +
						"boo();" 
				},""
//				"----------\n" + 
//				"1. ERROR in X.js (at line 4)\n" + 
//				"	b.toString();\n" + 
//				"	^\n" + 
//				"Null pointer access: The variable b can only be null at this location\n" + 
//				"----------\n"
				, null, false, custom
		);
	}
	
	public void testBug251374_4() {
		Map custom = new HashMap();
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.nullReference", "error");
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.potentialNullReference", "error");
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var b = null;\n" +
						"function boo() {\n" +
						"b = null\n" +
						"b.toString();\n" +
						"}\n" +
						"b = 2;\n" +
						"boo();\n" +
						"b.toString();" 
				},""
//				"----------\n" + 
//				"1. ERROR in X.js (at line 4)\n" + 
//				"	b.toString();\n" + 
//				"	^\n" + 
//				"Null pointer access: The variable b can only be null at this location\n" + 
//				"----------\n"
				, null, false, custom
		);
	}
	
	public void testBug251374_5() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var b = null;\n" +
						"function boo() {\n" +
						"b = null\n" +
						"b.toString();\n" +
						"}\n" +
						"b = 2;\n" +
						"boo();\n" +
						"b.toString();" 
				},
				""
		);
	}
	
	public void testBug286029_1() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var sub;\n" +
						"if(!sub) sub = {};" 
				},
				""
		);
	}
	
	public void testBug286029_2() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function abc() {\n" +
						"var sub;\n" +
						"if(!sub) sub = {};\n" +
						"}"
				},
				"----------\n" + 
		"1. WARNING in X.js (at line 3)\n" + 
		"	if(!sub) sub = {};\n" + 
		"	    ^^^\n" + 
		"The local variable sub may not have been initialized\n" + 
		"----------\n"
		);
	}
	
	public void testBug286029_3() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function abc() {\n" +
						"var sub; sub = {};\n" +
						"if(!sub) sub = {};\n" +
						"}"
				},
				""
		);
	}
	
	public void testBug251225_1() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var temp = function () {};\n" +
						"temp();"
				},
				""
		);
	}
	
	public void testBug251225_2() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var temp = function () {};\n" +
						"new temp();"
				},
				""
		);
	}
	
	public void testBug251225_3() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function testFunction() {\n" +
						"var temp = function () {};\n" +
						"function temp2(){}" + 
						"new temp2();}"
				},
				"----------\n" + 
				"1. WARNING in X.js (at line 2)\n" + 
				"	var temp = function () {};\n" + 
				"	    ^^^^\n" + 
				"The local variable temp is never read\n" + 
				"----------\n"
		);
	}
	
	public void testWI82701() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function foo() {\n" +
							"var content = {};\n" +
							"var content1 = {};\n" +
							"if(useLocation) {\n" +
								"content.geocode = \"123,123\";\n" +
								"doSearch(content);\n" +
							"} else {\n" +
								"doSearch(content);\n" +
							"}\n" +
						"}"
				},
				"----------\n" + 
				"1. WARNING in X.js (at line 3)\n" + 
				"	var content1 = {};\n" + 
				"	    ^^^^^^^^\n" + 
				"The local variable content1 is never read\n" + 
				"----------\n"
		);
	}
	
	public void testWI93475() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function value() {this.val = 3;}\n" +
						"this.value = this.options[si != -1 ? si : 0].value;"
				},
				""
		);
	}
	// uninitialized local in a switch
	public void test001() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function foo(arg) {\n" +
						"	switch(arg) {\n" +
						"		case 1:\n" +
						"			var j = 1;\n" +
						"		case 2:\n" +
						"			switch(5) {\n" +
						"				case j:\n" +
						"			}\n" +
						"	}\n" +
						"}"
				},
				"----------\n" + 
				"1. WARNING in X.js (at line 7)\n" + 
				"	case j:\n" + 
				"	     ^\n" + 
				"The local variable j may not have been initialized\n" + 
				"----------\n"
		);
	}
	
	// nested uninitialized local
	public void test002() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function foo(arg) {\n" +
						"	if(false) {\n" +
						"		var s;\n" +
						"		if(System.out != null) {\n" +
						"			System.out.doSomething(s);\n" +
						"		}\n" +
						"	}\n" +
						"}"
				},
				"----------\n" + 
				"1. WARNING in X.js (at line 5)\n" + 
				"	System.out.doSomething(s);\n" + 
				"	                       ^\n" + 
				"The local variable s may not have been initialized\n" + 
				"----------\n"
		);
	}
	
	// nested uninitialized local
	public void test003() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function foo(arg) {\n" +
						"	if(false) {\n" +
						"		var s;\n" +
						"		System.out.doSomething(s);\n" +
						"	}\n" +
						"}"
				},
				"----------\n" + 
				"1. WARNING in X.js (at line 4)\n" + 
				"	System.out.doSomething(s);\n" + 
				"	                       ^\n" + 
				"The local variable s may not have been initialized\n" + 
				"----------\n"
		);
	}
	
	// uninitialized local in a switch
	public void test004() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function foo(arg) {\n" +
						"	switch(1) {\n" +
						"		case 0:\n" +
						"			var j = 0;\n" +
						"		case 1:\n" +
						"			System.out.doSomething(j);\n" +
						"	}\n" +
						"}"
				},
				"----------\n" + 
				"1. WARNING in X.js (at line 6)\n" + 
				"	System.out.doSomething(j);\n" + 
				"	                       ^\n" + 
				"The local variable j may not have been initialized\n" + 
				"----------\n"
		);
	}
	
	// uninitialized local
	public void test005() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function foo(arg) {\n" +
						"    var s;\n" + 
						"    var o2 = arg;\n" + 
						"    if (o2 == null) {\n" + 
						"      s = \"\";\n" + 
						"    }\n" + 
						"    System.out.doSomething(s);\n" + 
						"}"
				},
				"----------\n" + 
				"1. WARNING in X.js (at line 7)\n" + 
				"	System.out.doSomething(s);\n" + 
				"	                       ^\n" + 
				"The local variable s may not have been initialized\n" + 
				"----------\n"
		);
	}
	
	// uninitialized local in a do while
	public void test006() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function foo(b) {\n" +
						"    var l;\n" + 
						"    do {\n" + 
						"   	if (b) {\n" + 
						"      		l = new Object();\n" + 
						"			break;\n" +
						"    	}\n" + 
						"    } while (false);\n" + 
						"	l.toString();\n" +
						"}"
				},
				"----------\n" + 
				"1. WARNING in X.js (at line 9)\n" + 
				"	l.toString();\n" + 
				"	^\n" + 
				"The local variable l may not have been initialized\n" + 
				"----------\n"
		);
	}
	
	// empty control statements
	public void test007() {
		Map options = getCompilerOptions();
		options.put(CompilerOptions.OPTION_ReportEmptyStatement, CompilerOptions.ERROR);
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function foo(args) {\n" +
						"	if(true)\n" + 
						"		;\n" + 
						"	else\n" + 
						"		;\n" + 
						"}"
				},
				"----------\n" + 
				"1. ERROR in X.js (at line 3)\n" + 
				"	;\n" + 
				"	^\n" + 
				"Empty control-flow statement\n" + 
				"----------\n" +
				"2. ERROR in X.js (at line 5)\n" + 
				"	;\n" + 
				"	^\n" + 
				"Empty control-flow statement\n" + 
				"----------\n"
				, null, true, options
		);
	}
	
	// switch case fall through
	public void test008() {
		Map options = getCompilerOptions();
		options.put(CompilerOptions.OPTION_ReportFallthroughCase, CompilerOptions.ERROR);
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function foo(p, b) {\n" +
						"	switch(p) {\n" +
						"		case 0:\n" +
						"			if(b) {\n" +
						"				break;\n" +
						"			}\n" +
						"		case 1:\n" +
						"			System.out.doSomething(j);\n" +
						"	}\n" +
						"}"
				},
				"----------\n" + 
				"1. ERROR in X.js (at line 7)\n" + 
				"	case 1:\n" + 
				"	^^^^^^\n" + 
				"Switch case may be entered by falling through previous case\n" + 
				"----------\n", null, true, options
		);
	}
	
	// switch case fall through
	public void test009() {
		Map options = getCompilerOptions();
		options.put(CompilerOptions.OPTION_ReportFallthroughCase, CompilerOptions.ERROR);
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function foo(p, b) {\n" +
						"	switch(p) {\n" +
						"		case 0:\n" +
						"			System.exit();\n" +
						"		case 1:\n" +
						"			System.out.doSomething(1);\n" +
						"	}\n" +
						"}"
				},
				"----------\n" + 
				"1. ERROR in X.js (at line 5)\n" + 
				"	case 1:\n" + 
				"	^^^^^^\n" + 
				"Switch case may be entered by falling through previous case\n" + 
				"----------\n", null, true, options
		);
	}
	
	// switch case fall through
	public void test010() {
		Map options = getCompilerOptions();
		options.put(CompilerOptions.OPTION_ReportFallthroughCase, CompilerOptions.ERROR);
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function foo(p) {\n" +
						"	switch(p) {\n" +
						"		case 0:\n" +
						"			System.exit(0);\n" +
						"		case 1:\n" +
						"			System.out.doSomething(1);\n" +
						"			break;\n" +
						"		case 2:\n" +
						"			System.out.doSomething(2);\n" +
						"			return;\n" +
						"		case 3:\n" +
						"		case 4:\n" +
						"		default:\n" +
						"			System.out.println(3);\n" +
						"	}\n" +
						"}"
				},
				"----------\n" + 
				"1. ERROR in X.js (at line 5)\n" + 
				"	case 1:\n" + 
				"	^^^^^^\n" + 
				"Switch case may be entered by falling through previous case\n" + 
				"----------\n", null, true, options
		);
	}
	
	// uninitialized local in a do while
	public void test011() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function foo() {\n" +
						"   var c1, c2;\n" + 
						"   do ; while((c1 = 0) == 1);\n" + 
						"	if (c1 == 0) {} // silent\n" + 
						"	if (c2 == 0) {} // complain\n" + 
						"}"
				},
				"----------\n" + 
				"1. WARNING in X.js (at line 5)\n" + 
				"	if (c2 == 0) {} // complain\n" + 
				"	    ^^\n" + 
				"The local variable c2 may not have been initialized\n" + 
				"----------\n"
		);
	}
	
	// uninitialized local in a for
	public void test012() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function foo() {\n" +
						"   var c1, c2;\n" + 
						"   for(; (c1 = 0) == 1;);\n" + 
						"	if (c1 == 0) {} // silent\n" + 
						"	if (c2 == 0) {} // complain\n" + 
						"}"
				},
				"----------\n" + 
				"1. WARNING in X.js (at line 5)\n" + 
				"	if (c2 == 0) {} // complain\n" + 
				"	    ^^\n" + 
				"The local variable c2 may not have been initialized\n" + 
				"----------\n"
		);
	}
	
	// uninitialized local in a while
	public void test013() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function foo() {\n" +
						"   var c1, c2;\n" + 
						"   while((c1 = 0) == 1);\n" + 
						"	if (c1 == 0) {} // silent\n" + 
						"	if (c2 == 0) {} // complain\n" + 
						"}"
				},
				"----------\n" + 
				"1. WARNING in X.js (at line 5)\n" + 
				"	if (c2 == 0) {} // complain\n" + 
				"	    ^^\n" + 
				"The local variable c2 may not have been initialized\n" + 
				"----------\n"
		);
	}
}
