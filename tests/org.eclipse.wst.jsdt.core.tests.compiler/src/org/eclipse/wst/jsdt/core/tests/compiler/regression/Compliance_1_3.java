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
package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import java.util.Map;

import junit.framework.Test;

import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;

public class Compliance_1_3 extends AbstractRegressionTest {
boolean docSupport = false;

public Compliance_1_3(String name) {
	super(name);
}

/*
 * Toggle compiler in mode -1.3
 */
protected Map getCompilerOptions() {
	Map options = super.getCompilerOptions();
	if (docSupport) {
		options.put(CompilerOptions.OPTION_DocCommentSupport, CompilerOptions.ENABLED);
		options.put(CompilerOptions.OPTION_ReportInvalidJavadoc, CompilerOptions.ERROR);
		options.put(CompilerOptions.OPTION_ReportInvalidJavadocTags, CompilerOptions.ENABLED);
	}
	return options;
}
public static Test suite() {
		return buildUniqueComplianceTestSuite(testClass(), COMPLIANCE_1_3);
}
public static Class testClass() {
	return Compliance_1_3.class;
}
// Use this static initializer to specify subset for tests
// All specified tests which does not belong to the class are skipped...
static {
//		TESTS_NAMES = new String[] { "Bug58069" };
//		TESTS_NUMBERS = new int[] { 104 };
//		TESTS_RANGE = new int[] { 76, -1 };
}
/* (non-Javadoc)
 * @see junit.framework.TestCase#setUp()
 */
protected void setUp() throws Exception {
	super.setUp();
	// Javadoc disabled by default 
	docSupport = false;
}

// test001 - moved to SuperTypeTest#test002
// test002 - moved to SuperTypeTest#test003
// test003 - moved to SuperTypeTest#test004
// test004 - moved to SuperTypeTest#test005
// test005 - moved to SuperTypeTest#test006
// test006 - moved to SuperTypeTest#test007
// test007 - moved to TryStatementTest#test057
// test008 - moved to LookupTest#test074
// test009 - moved to RuntimeTests#test1004

// check actualReceiverType when array type
// test unreachable code complaints
public void test011() {
	this.runNegativeTest(
		new String[] {
			"X.js",
			"	void foo() { \n"+
			"		while (false);	\n" +
			"		while (false) println(\"unreachable\");	\n" +
			"		do ; while (false);	\n" +
			"		do println(\"unreachable\"); while (false);	\n" +
			"		for (;false;);	\n" +
			"		for (;false;) println(\"unreachable\");	\n" +
			"		if (false);	\n" +
			"		if (false) println(\"unreachable\");		\n" +		
			"	}	\n" +
			"	function println(s){}	\n" +
			" \n"
		},
		"----------\n" + 
		"1. ERROR in p1\\X.js (at line 3)\n" + 
		"	while (false) System.out.println(\"unreachable\");	\n" + 
		"	              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
		"Unreachable code\n" + 
		"----------\n" + 
		"2. ERROR in p1\\X.js (at line 7)\n" + 
		"	for (;false;) System.out.println(\"unreachable\");	\n" + 
		"	              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
		"Unreachable code\n" + 
		"----------\n"
	);
}			
// binary compatibility


/*
 * http://dev.eclipse.org/bugs/show_bug.cgi?id=24744
 * http://dev.eclipse.org/bugs/show_bug.cgi?id=23096
 */
public void test037() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_TaskTags, "TODO:");
	this.runNegativeTest(
		new String[] {
			"X.js",
			"function X() {\n"+
			"}\n"+
			"// TODO: something"
		},
		"----------\n" + 
		"1. WARNING in p\\X.js (at line 4)\n" + 
		"	// TODO: something\n" + 
		"	   ^^^^^^^^^^^^^^^\n" + 
		"TODO: something\n" + 
		"----------\n",
		null,
		true,
		customOptions);
}

/*
 * http://dev.eclipse.org/bugs/show_bug.cgi?id=24833
 * http://dev.eclipse.org/bugs/show_bug.cgi?id=23096
 */
public void test038() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_TaskTags, "TODO:");
	this.runNegativeTest(
		new String[] {
			"X.js",
			"// TODO: something"
		},
		"----------\n" + 
		"1. WARNING in X.js (at line 1)\n" + 
		"	// TODO: something\n" + 
		"	   ^^^^^^^^^^^^^^^\n" + 
		"TODO: something\n" + 
		"----------\n",
		null,
		true,
		customOptions);
}


public void test072() {
	
	this.runNegativeTest(
		new String[] {
			"X.js",
			"    function main( args) {\n" + 
			"        try {\n" + 
			"            f();\n" + 
			"        } catch(e) {\n" + 
			"            println(\"SUCCESS\");\n" + 
			"        }\n" + 
			"    }\n" + 
			"    function f() {\n" + 
			"       function ff ()\n" + 
			"      //      {\n" + 
			"                    if (true) throw null;\n" + 
			"            }\n" + 
			"      //  };\n" + 
			"    }\n" + 
			"  function println(s){}",
		},
		"----------\n" + 
		"1. ERROR in X.js (at line 12)\n" + 
		"	if (true) throw null;\n" + 
		"	                ^^^^\n" + 
		"Cannot throw null as an exception\n" + 
		"----------\n");
}


// checking for captured outer local initialization status
// NOTE: only complain against non-inlinable outer locals
// http://bugs.eclipse.org/bugs/show_bug.cgi?id=26134
public void test074() {
	this.runNegativeTest(
		new String[] {
			"X.js",
			"    function main(args) {	\n" +
			"    	var nonInlinedString = \"[Local]\";	\n" +
			"    	var i = 2;	\n" +
			"		switch(i){	\n" +
			"			case 1:	\n" +
			"				var displayString = nonInlinedString;\n" +
			"				var inlinedString = \"a\";	\n" +
			"//				class Local {	\n" +
			"					function toString() {	\n" +
			"						return inlinedString + displayString;	\n" +
			"					}	\n" +
			"//				}	\n" +
			"			case 2:	\n" +
			"//				print(new Local());	\n" +
			"//				print(\"-\");	\n" +
//			"				println(new Local(){	\n" +
//			"					public String toString() {	\n" +
//			"						return super.toString()+\": anonymous\";	\n" +
//			"					}	\n" +
//			"				});	\n" +
			"		}	\n" +
			"    }	\n" +
			"	function println(s){}\n",
		},
		"----------\n" + 
		"1. ERROR in X.js (at line 14)\n" + 
		"	System.out.print(new Local());	\n" + 
		"	                 ^^^^^^^^^^^\n" + 
		"The local variable displayString may not have been initialized\n" + 
		"----------\n" + 
		"2. ERROR in X.js (at line 16)\n" + 
		"	System.out.println(new Local(){	\n" + 
		"					public String toString() {	\n" + 
		"						return super.toString()+\": anonymous\";	\n" + 
		"					}	\n" + 
		"				});	\n" + 
		"	                   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
		"The local variable displayString may not have been initialized\n" + 
		"----------\n");
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=66533
 */
public void test084() {
	this.runNegativeTest(
		new String[] {
			"X.js",
			"	void foo() {\n" + 
			"		Object enum = null;\n" + 
			"	}\n" + 
			"\n"
		},
		"----------\n" + 
		"1. WARNING in X.js (at line 2)\n" + 
		"	Object enum = null;\n" + 
		"	       ^^^^\n" + 
		"\'enum\' should not be used as an identifier, since it is a reserved keyword from source level 5.0 on\n" + 
		"----------\n");
}
public void test100() {
	this.runNegativeTest(
		new String[] {
			"X.js",
			"    var \\ud800\\udc05\\ud800\\udc04\\ud800\\udc03\\ud800\\udc02\\ud800\\udc01\\ud800\\udc00;\n" + 
			"    function foo() {\n" + 
			"        var \\ud800\\udc05\\ud800\\udc04\\ud800\\udc03\\ud800\\udc02\\ud800\\udc01\\ud800\\udc00;\n" + 
			"    }\n" + 
			"\n"
		},
		"----------\n" + 
		"1. ERROR in X.js (at line 1)\n" + 
		"	int \\ud800\\udc05\\ud800\\udc04\\ud800\\udc03\\ud800\\udc02\\ud800\\udc01\\ud800\\udc00;\n" + 
		"	    ^^^^^^\n" + 
		"Invalid unicode\n" + 
		"----------\n" + 
		"2. ERROR in X.js (at line 3)\n" + 
		"	int \\ud800\\udc05\\ud800\\udc04\\ud800\\udc03\\ud800\\udc02\\ud800\\udc01\\ud800\\udc00;\n" + 
		"	    ^^^^^^\n" + 
		"Invalid unicode\n" + 
		"----------\n"
	);
}
}
