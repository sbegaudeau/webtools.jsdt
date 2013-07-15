/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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

public class AssignmentTest extends AbstractRegressionTest {

	public AssignmentTest(String name) {
		super(name);
	}

	protected Map getCompilerOptions() {
		Map options = super.getCompilerOptions();
		options.put(CompilerOptions.OPTION_ReportNullReference,
				CompilerOptions.ERROR);
		options.put(CompilerOptions.OPTION_ReportNoEffectAssignment,
				CompilerOptions.ERROR);
		return options;
	}

	public void test002() {
		this
				.runNegativeTest(
						new String[] {
								"X.js",
								"	var a;	\n"
										+ "	var next;	\n"
										+ "	 function foo( arg){	\n"
										+ "	\n"
										+ "		zork = zork;	\n"
										+ "		arg = zork;	\n"
										+ "	\n"
										+ "		arg = arg;  // noop	\n"
										+ "		a = a;  // noop	\n"
										+ "		this.next = this.next; // noop	\n"
										+ "		this.next = next; // noop	\n"
										+ "	\n"
										+ "		next.a = next.a; // could raise NPE	\n"
										+ "		this.next.next.a = next.next.a; // could raise NPE	\n"
										+ "		a = next.a; // could raise NPE	\n"
										+ "		this. a = next.a; 	\n" + "	}	\n"
										+ "\n", },
						"----------\n"
								+ "1. ERROR in X.js (at line 8)\n"
								+ "	arg = arg;  // noop	\n"
								+ "	^^^^^^^^^\n"
								+ "The assignment to variable arg has no effect\n"
								+ "----------\n"
								+ "2. ERROR in X.js (at line 9)\n"
								+ "	a = a;  // noop	\n"
								+ "	^^^^^\n"
								+ "The assignment to variable a has no effect\n"
								+ "----------\n"
								+ "3. ERROR in X.js (at line 10)\n"
								+ "	this.next = this.next; // noop	\n"
								+ "	^^^^^^^^^^^^^^^^^^^^^\n"
								+ "The assignment to variable next has no effect\n"
								+ "----------\n"
								+ "4. ERROR in X.js (at line 11)\n"
								+ "	this.next = next; // noop	\n"
								+ "	^^^^^^^^^^^^^^^^\n"
								+ "The assignment to variable next has no effect\n"
								+ "----------\n");
	}

	// null part has been repeated into NullReferenceTest#test1033
	public void test033() {
		this
				.runNegativeTest(
						new String[] {
								"X.js",
								"	\n" + "	function foo() {\n" + "		var a,b;\n"
										+ "		do{\n" + "		   a=\"Hello \";\n"
										+ "		}while(a!=null);\n" + "				\n"
										+ "		if(a!=null)\n" + "		{\n"
										+ "		   b=\"World!\";\n" + "		}\n"
										+ "		println(a+b);\n" + "	}\n" + "\n", },
						"----------\n"
								+ "1. WARNING in X.js (at line 12)\n"
								+ "	println(a+b);\n"
								+ "	          ^\n"
								+ "The local variable b may not have been initialized\n"
								+ "----------\n");
	}

	/*
	 * Check scenario: i = i++
	 * http://bugs.eclipse.org/bugs/show_bug.cgi?id=84480 disabled:
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=111898
	 */
	public void test035() {
		this.runNegativeTest(new String[] {
				"X.js",
				"	var f;\n" + "	function foo( i) {\n" + "		i = i++;\n"
						+ "		i = ++i;\n" + "		f = f++;\n" + "		f = ++f;\n"
						+ "		var z;" + "	}\n" + "\n", }, "----------\n"
				+ "1. ERROR in X.js (at line 4)\n" + "	i = ++i;\n"
				+ "	^^^^^^^\n" + "The assignment to variable i has no effect\n"
				+ "----------\n" + "2. ERROR in X.js (at line 6)\n"
				+ "	f = ++f;\n" + "	^^^^^^^\n"
				+ "The assignment to variable f has no effect\n"
				+ "----------\n");
	}

	// //https://bugs.eclipse.org/bugs/show_bug.cgi?id=93588
	// public void test037() {
	// this.runConformTest(
	// new String[] {
	// "X.js",
	// " class X extends Object implements Runnable {\n" +
	// "	var interval = 5;\n" +
	// "	 function run() {\n" +
	// "		try {\n" +
	// "			Thread.sleep(interval = interval + 100);\n" +
	// "			Thread.sleep(interval += 100);\n" +
	// "		} catch (InterruptedException e) {\n" +
	// "			e.printStackTrace();\n" +
	// "		}\n" +
	// "	}\n" +
	// "\n" +
	// "	  function main( args) {\n" +
	// "		new X().run();\n" +
	// "	}\n" +
	// "}\n",
	// },
	// "");
	// }
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=111703
	// public void test038() {
	// this.runNegativeTest(
	// new String[] {
	// "X.js",
	// "import java.awt.event.*;\n" +
	// "\n" +
	// "import javax.swing.*;\n" +
	// "import javax.swing.event.*;\n" +
	// "\n" +
	// " class X {\n" +
	// "    JButton myButton = new JButton();\n" +
	// "    JTree myTree = new JTree();\n" +
	// "    ActionListener action;\n" +
	// "    X() {\n" +
	// "        action = new ActionListener() {\n" +
	// "             function actionPerformed(ActionEvent e) {\n" +
	// "                if (true) {\n" +
	// "                    // unlock document\n" +
	// "                     Object document = new Object();\n" +
	// "                    myButton.addActionListener(new ActionListener() {\n"
	// +
	// "                          boolean selectionChanged;\n" +
	// "                         TreeSelectionListener list = new TreeSelectionListener() {\n"
	// +
	// "                             function valueChanged(TreeSelectionEvent e) {\n"
	// +
	// "                                selectionChanged = true;\n" +
	// "                            }\n" +
	// "                        };\n" +
	// "                       {\n" +
	// "                      myTree.addTreeSelectionListener(list);\n" +
	// "                      }\n" +
	// "                         function actionPerformed(ActionEvent e) {\n" +
	// "                            if(!selectionChanged)\n" +
	// "                            myButton.removeActionListener(this);\n" +
	// "                        }\n" +
	// "                    });\n" +
	// "                }\n" +
	// "            }\n" +
	// "        };\n" +
	// "    }\n" +
	// "      function main( args) {\n" +
	// "        new X();\n" +
	// "    }\n" +
	// "\n" +
	// "}",
	// },
	// "----------\n" +
	// "1. WARNING in X.js (at line 19)\n" +
	// "	 function valueChanged(TreeSelectionEvent e) {\n" +
	// "	                                            ^\n" +
	// "The parameter e is hiding another local variable defined in an enclosing type scope\n"
	// +
	// "----------\n" +
	// "2. ERROR in X.js (at line 23)\n" +
	// "	 {\n" +
	// "	       ^\n" +
	// "Cannot define  initializer in inner type new ActionListener(){}\n" +
	// "----------\n" +
	// "3. ERROR in X.js (at line 24)\n" +
	// "	myTree.addTreeSelectionListener(list);\n" +
	// "	^^^^^^\n" +
	// "Cannot make a  reference to the non- field myTree\n" +
	// "----------\n" +
	// "4. WARNING in X.js (at line 26)\n" +
	// "	 function actionPerformed(ActionEvent e) {\n" +
	// "	                                        ^\n" +
	// "The parameter e is hiding another local variable defined in an enclosing type scope\n"
	// +
	// "----------\n");
	// }
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=111898
	// public void test039() {
	// this.runConformTest(
	// new String[] {
	// "X.js",
	// " class X {\n" +
	// "	  function main( args) {\n" +
	// "		var a = 1;\n" +
	// "	    a = a++;\n" +
	// "		print(\"a=\"+a);\n" +
	// "		\n" +
	// "		var b = 1;\n" +
	// "		print(b = b++);\n" +
	// "		println(\"b=\"+b);\n" +
	// "	}\n" +
	// "}\n",
	// },
	// "a=11b=1");
	// }
	// warn upon parameter assignment
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=53773
	public void test040() {
		Map options = getCompilerOptions();
		options.put(CompilerOptions.OPTION_ReportParameterAssignment,
				CompilerOptions.ERROR);
		this.runNegativeTest(
				new String[] {
						"X.js",
						"  function foo(b) {\n" + "    b = false;\n" + "  }\n"
								+ "\n", }, "----------\n"
						+ "1. ERROR in X.js (at line 2)\n" + "	b = false;\n"
						+ "	^\n" + "The parameter b should not be assigned\n"
						+ "----------\n", null, true, options);
	}

	// warn upon parameter assignment
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=53773
	// diagnose within fake reachable code
	public void test041() {
		Map options = getCompilerOptions();
		options.put(CompilerOptions.OPTION_ReportParameterAssignment,
				CompilerOptions.ERROR);
		this.runNegativeTest(new String[] {
				"X.js",
				"  function foo(b) {\n" + "    if (false) {\n"
						+ "      b = false;\n" + "    }\n" + "  }\n" + "\n", },
				"----------\n" + "1. ERROR in X.js (at line 3)\n"
						+ "	b = false;\n" + "	^\n"
						+ "The parameter b should not be assigned\n"
						+ "----------\n", null, true, options);
	}

	// warn upon parameter assignment
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=53773
	// diagnose within fake reachable code
	public void test042() {
		Map options = getCompilerOptions();
		options.put(CompilerOptions.OPTION_ReportParameterAssignment,
				CompilerOptions.ERROR);
		this.runNegativeTest(new String[] {
				"X.js",
				"  function foo(b) {\n" + "    if (true) {\n"
						+ "      return;\n" + "    }\n" + "    b = false;\n"
						+ "  }\n" + "\n", }, "----------\n"
				+ "1. ERROR in X.js (at line 5)\n" + "	b = false;\n" + "	^\n"
				+ "The parameter b should not be assigned\n" + "----------\n",
				null, true, options);
	}

	// // warn upon parameter assignment
	// // https://bugs.eclipse.org/bugs/show_bug.cgi?id=53773
	// // we only show the 'assignment to final' error here
	// public void test043() {
	// Map options = getCompilerOptions();
	// options.put(CompilerOptions.OPTION_ReportParameterAssignment,
	// CompilerOptions.ERROR);
	// this.runNegativeTest(
	// new String[] {
	// "X.js",
	// " class X {\n" +
	// "  function foo( boolean b) {\n" +
	// "    if (false) {\n" +
	// "      b = false;\n" +
	// "    }\n" +
	// "  }\n" +
	// "}\n",
	// },
	// "----------\n" +
	// "1. ERROR in X.js (at line 4)\n" +
	// "	b = false;\n" +
	// "	^\n" +
	// "The final local variable b cannot be assigned. It must be blank and not using a compound assignment\n"
	// +
	// "----------\n",
	// null, true, options);
	// }
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=100369
	public void test044() {
		this.runNegativeTest(new String[] {
				"X.js",
				"	var length1 = 0;\n" + "	{\n"
						+ "		length1 = length1; // already detected\n" + "	}\n"
						+ "	var length2 = length2 = 0; // not detected\n"
						+ "	var length3 = 0;\n" + "	{\n"
						+ "		length3 = length3 = 0; // not detected\n" + "	}\n"
						+ "	 function foo() {\n" + "		var length1 = 0;\n"
						+ "		length1 = length1; // already detected\n"
						+ "		var length2 = length2 = 0; // not detected\n"
						+ "		var length3 = 0;\n"
						+ "		length3 = length3 = 0; // not detected\n" + "	}\n"
						+ "\n", }, "----------\n"
				+ "1. ERROR in X.js (at line 3)\n"
				+ "	length1 = length1; // already detected\n"
				+ "	^^^^^^^^^^^^^^^^^\n"
				+ "The assignment to variable length1 has no effect\n"
				+ "----------\n" + "2. ERROR in X.js (at line 5)\n"
				+ "	var length2 = length2 = 0; // not detected\n"
				+ "	    ^^^^^^^^^^^^^^^^^^^^^\n"
				+ "The assignment to variable length2 has no effect\n"
				+ "----------\n" + "3. ERROR in X.js (at line 8)\n"
				+ "	length3 = length3 = 0; // not detected\n"
				+ "	^^^^^^^^^^^^^^^^^^^^^\n"
				+ "The assignment to variable length3 has no effect\n"
				+ "----------\n" + "4. WARNING in X.js (at line 11)\n"
				+ "	var length1 = 0;\n"
				+ "	    ^^^^^^^\n"
				+ "The local variable length1 is hiding a global variable\n"
				+ "----------\n" + "5. ERROR in X.js (at line 12)\n"
				+ "	length1 = length1; // already detected\n"
				+ "	^^^^^^^^^^^^^^^^^\n"
				+ "The assignment to variable length1 has no effect\n"
				+ "----------\n" + "6. WARNING in X.js (at line 13)\n"
				+ "	var length2 = length2 = 0; // not detected\n"
				+ "	    ^^^^^^^\n"
				+ "The local variable length2 is hiding a global variable\n"
				+ "----------\n" + "7. ERROR in X.js (at line 13)\n"
				+ "	var length2 = length2 = 0; // not detected\n"
				+ "	    ^^^^^^^^^^^^^^^^^^^^^\n"
				+ "The assignment to variable length2 has no effect\n"
				+ "----------\n" + "8. WARNING in X.js (at line 14)\n"
				+ "	var length3 = 0;\n"
				+ "	    ^^^^^^^\n"
				+ "The local variable length3 is hiding a global variable\n"
				+ "----------\n" + "9. ERROR in X.js (at line 15)\n"
				+ "	length3 = length3 = 0; // not detected\n"
				+ "	^^^^^^^^^^^^^^^^^^^^^\n"
				+ "The assignment to variable length3 has no effect\n"
				+ "----------\n");
	}

	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=133351
	public void test045() {
		this.runNegativeTest(new String[] {
				"X.js",
				"	function foo() {\n"
						+ "		var length2 = length2 = 0; // first problem\n"
						+ "		var length3 = 0;\n"
						+ "		length3 = length3 = 0; // second problem\n"
						+ "	}\n" + "\n", }, "----------\n"
				+ "1. ERROR in X.js (at line 2)\n"
				+ "	var length2 = length2 = 0; // first problem\n"
				+ "	    ^^^^^^^^^^^^^^^^^^^^^\n"
				+ "The assignment to variable length2 has no effect\n"
				+ "----------\n" + "2. ERROR in X.js (at line 4)\n"
				+ "	length3 = length3 = 0; // second problem\n"
				+ "	^^^^^^^^^^^^^^^^^^^^^\n"
				+ "The assignment to variable length3 has no effect\n"
				+ "----------\n");
	}

	public static Class testClass() {
		return AssignmentTest.class;
	}
}
