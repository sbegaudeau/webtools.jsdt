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

/**
 * Completion is expected to be a FunctionInvocation.
 */
public class MethodInvocationCompletionTest extends AbstractCompletionTest {
public MethodInvocationCompletionTest(String testName) {
	super(testName);
}
/*
 * Completion with no receiver inside a for statement.
 */
public void test1FVVWS8_1() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	for (var i = 10; i > 0; --i)		\n" +
		"		fred(							\n" +
		"}										\n",
		// completeBehind:
		"fred(",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:fred()>",
		// expectedUnitDisplayString:
		"function foo() {\n" +
		"  var i;\n" +
		"  <CompleteOnMessageSend:fred()>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"fred(",
		// test name
		"<1FVVWS8_1>"
	);
}
/*
 * Completion with no receiver inside an if statement.
 */
public void test1FVVWS8_2() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	if (true)							\n" +
		"		fred(							\n" +
		"}										\n",
		// completeBehind:
		"fred(",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:fred()>",
		// expectedUnitDisplayString:
		"function foo() {\n" +
		"  <CompleteOnMessageSend:fred()>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"fred(",
		// test name
		"<1FVVWS8_2>"
	);
}
/*
 * Completion with no receiver inside a for statement
 * and after a field access.
 */
public void test1FW2ZTB_1() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"var array = [];									\n" +
		"function foo() {									\n" +
		"	for (var i = this.array.length; i > 0; --i)	\n" +
		"		fred(									\n" +
		"}												\n",
		// completeBehind:
		"fred(",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:fred()>",
		// expectedUnitDisplayString:
		"var array = [];\n" +
		"void foo() {\n" +
		"  var i;\n" +
		"  <CompleteOnMessageSend:fred()>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"fred(",
		// test name
		"<1FW2ZTB_1"
	);
}
/*
 * Complete on method invocation with expression receiver
 * inside another invocation with no receiver.
 */
public void test1FW35YZ_1() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	bar(primary().fred(					\n" +
		"}										\n",
		// completeBehind:
		"fred(",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:primary().fred()>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  <CompleteOnMessageSend:primary().fred()>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"fred(",
		// test name
		"<1FW35YZ_1>"
	);
}
/*
 * Completion with primary receiver.
 */
public void test1FWYBKF() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"		this.x.bar(						\n" +
		"}										\n",
		// completeBehind:
		"bar(",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:this.x.bar()>",
		// expectedUnitDisplayString:
		"function foo() {\n" +
		"  <CompleteOnMessageSend:this.x.bar()>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"bar(",
		// test name
		"<1FWYBKF>"
	);
}
/*
 * Completion just after a parameter which is a message send.
 */
public void test1GAJBUQ() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	x.y.Z.fred(buzz());					\n" +
		"}										\n",
		// completeBehind:
		"fred(buzz()",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:x.y.Z.fred(buzz())>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  <CompleteOnMessageSend:x.y.Z.fred(buzz())>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"fred(buzz()",
		// test name
		"<1GAJBUQ>"
	);
}
/*
 * Completion just after the first parameter.
 */
public void testAfterFirstParameter() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	this.fred(\"abc\" , 2, i);		\n" +
		"}										\n",
		// completeBehind:
		"fred(\"abc\" ",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:this.fred(\"abc\")>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  <CompleteOnMessageSend:this.fred(\"abc\")>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"fred(\"abc\" ",
		// test name
		"<completion just after first parameter>"
	);
}
/*
 * Completion just before the first parameter.
 */
public void testBeforeFirstParameter() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	this.fred(1, 2, i);					\n" +
		"}										\n",
		// completeBehind:
		"fred(",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:this.fred()>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  <CompleteOnMessageSend:this.fred()>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"fred(",
		// test name
		"<completion just before first parameter>"
	);
}
/*
 * Completion just before the last parameter.
 */
public void testBeforeLastParameter() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	this.fred(1, 2, i);					\n" +
		"}										\n",
		// completeBehind:
		"fred(1, 2,",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:this.fred(1, 2)>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  <CompleteOnMessageSend:this.fred(1, 2)>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"fred(1, 2,",
		// test name
		"<completion just before last parameter>"
	);
}
/*
 * Completion just before the second parameter.
 */
public void testBeforeSecondParameter() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	this.fred(1, 2, i);					\n" +
		"}										\n",
		// completeBehind:
		"fred(1, ",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:this.fred(1)>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  <CompleteOnMessageSend:this.fred(1)>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"fred(1, ",
		// test name
		"<completion just before second parameter>"
	);
}
/*
 * Completion on empty name inside the expression of the first parameter.
 */
public void testEmptyInFirstParameter() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	this.fred(\"abc\" + , 2, i);		\n" +
		"}										\n",
		// completeBehind:
		"fred(\"abc\" +",
		// expectedCompletionNodeToString:
		"<CompleteOnName:>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  (\"abc\" + <CompleteOnName:>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"",
		// test name
		"<completion empty in first parameter>"
	);
}
/*
 * Completion inside the expression of the first parameter.
 */
public void testInFirstParameter() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	this.fred(\"abc\" + bizz, 2, i);	\n" +
		"}										\n",
		// completeBehind:
		"fred(\"abc\" + bi",
		// expectedCompletionNodeToString:
		"<CompleteOnName:bi>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  (\"abc\" + <CompleteOnName:bi>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"bi",
		// expectedReplacedSource:
		"bizz",
		// test name
		"<completion inside first parameter>"
	);
}
/*
 * Completion inside an if statement.
 */
public void testInIfStatement() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	if (true) {							\n" +
		"		bar.fred();						\n" +
		"	}									\n" +
		"}										\n",
		// completeBehind:
		"fred(",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:bar.fred()>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  {\n" + 
		"    <CompleteOnMessageSend:bar.fred()>;\n" + 
		"  }\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"fred(",
		// test name
		"<completion inside a if statement>"
	);
}
/*
 * Completion in labeled method invocation with expression receiver.
 */
public void testLabeledWithExpressionReceiver() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	label1: bar().fred(1, 2, o);		\n" +
		"}										\n",
		// completeBehind:
		"fred(1, 2,",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:bar().fred(1, 2)>",
		// expectedUnitDisplayString:
		"function foo() {\n" +
		"  <CompleteOnMessageSend:bar().fred(1, 2)>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"fred(1, 2,",
		// expectedLabels:
		new String[] {"label1"},
		// test name
		"<completion in labeled method invocation with expression receiver>"
	);
}
/*
 * Completion in labeled method invocation without receiver.
 */
public void testLabeledWithoutReceiver() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	label1: fred(1, 2, o);				\n" +
		"}										\n",
		// completeBehind:
		"fred(1, 2,",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:fred(1, 2)>",
		// expectedUnitDisplayString:
		"function foo() {\n" +
		"  <CompleteOnMessageSend:fred(1, 2)>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"fred(1, 2,",
		// expectedLabels:
		new String[] {"label1"},
		// test name
		"<completion in labeled method invocation without receiver>"
	);
}
/*
 * FunctionInvocation ::= Name '(' ArgumentListopt ')'
 */
public void testNoReceiver() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	fred();								\n" +
		"}										\n",
		// completeBehind:
		"fred(",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:fred()>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  <CompleteOnMessageSend:fred()>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"fred(",
		// test name
		"<completion on method invocation with no receiver>"
	);
}
/*
 * Completion just before the first parameter with a space after the open parenthesis.
 */
public void testSpaceThenFirstParameter() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	this.fred( 1, 2, i);				\n" +
		"}										\n",
		// completeBehind:
		"fred( ",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:this.fred()>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  <CompleteOnMessageSend:this.fred()>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"fred( ",
		// test name
		"<completion just before first parameter with a space after open parenthesis>"
	);
}
/*
 * Complete on method invocation with expression receiver.
 */
public void testWithExpressionReceiver() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	bar().fred();						\n" +
		"}										\n",
		// completeBehind:
		"fred(",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:bar().fred()>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  <CompleteOnMessageSend:bar().fred()>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"fred(",
		// test name
		"<completion on method invocation with expression receiver>"
	);
}
/*
 * Completion with a name receiver.
 */
public void testWithNameReceiver() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	var v = new Vector();			\n" +
		"	v.addElement(\"1\");				\n" +
		"}										\n",
		// completeBehind:
		"addElement(",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:v.addElement()>",
		// expectedUnitDisplayString:
		"function foo() {\n" +
		"  var v = new Vector();\n" +
		"  <CompleteOnMessageSend:v.addElement()>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"addElement(",
		// test name
		"<completion with name receiver>"
	);
}
/*
 * Completion with a name receiver after conditional expression.
 */
public void testWithNameReceiverAfterConditionalExpression() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	buzz.test(cond ? max : min);		\n" +
		"	bar.fred();							\n" +
		"}										\n",
		// completeBehind:
		"fred(",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:bar.fred()>",
		// expectedUnitDisplayString:
		"function foo() {\n" +
		"  <CompleteOnMessageSend:bar.fred()>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"fred(",
		// expectedLabels:
		new String[] {},
		// test name
		"<completion with name receiver after conditional expression>"
	);
}
/*
 * Completion with a name receiver and 2 arguments.
 */
public void testWithNameReceiverAndTwoArgs() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	var x = new X();						\n" +
		"	x.fred(1, 2, o);					\n" +
		"}										\n",
		// completeBehind:
		"x.fred(1, 2,",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:x.fred(1, 2)>",
		// expectedUnitDisplayString:
		"function foo() {\n" +
		"  var x = new X();\n" +
		"  <CompleteOnMessageSend:x.fred(1, 2)>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"fred(1, 2,",
		// test name
		"<completion with name receiver and 2 arguments>"
	);
}
/*
 * Completion with a qualified name receiver.
 */
public void testWithQualifiedNameReceiver() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	var x = new X();						\n" +
		"	y.x.fred(1, 2, o);					\n" +
		"}										\n",
		// completeBehind:
		"x.fred(1, 2,",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:y.x.fred(1, 2)>",
		// expectedUnitDisplayString:
		"function foo() {\n" +
		"  var x = new X();\n" +
		"  <CompleteOnMessageSend:y.x.fred(1, 2)>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"fred(1, 2,",
		// test name
		"<completion with qualified name receiver>"
	);
}
}
