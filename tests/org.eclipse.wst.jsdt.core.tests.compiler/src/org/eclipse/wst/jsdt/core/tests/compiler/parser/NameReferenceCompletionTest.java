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

import junit.framework.Test;

/**
 * Completion is expected to be a name reference.
 */
public class NameReferenceCompletionTest extends AbstractCompletionTest {
public NameReferenceCompletionTest(String testName) {
	super(testName);
}
/*
 * Regression test for 1FTZ849.
 * The instance creation before the completion is not properly closed, and thus
 * the completion parser used to think the completion was on a type.
 */
public void test1FTZ849() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	var o = new X;						\n" +
		"	fred.xyz;							\n" +
		"}										\n",
		// completeBehind:
		"fred.x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred.x>",
		// expectedUnitDisplayString:
		"function foo() {\n" +
		"  var o = new   X;\n" + 
		"  <CompleteOnMemberAccess:fred.x>;\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<1FTZ849>"
	);
}
/*
 * Completion in a field initializer with no syntax error. 
 */
public void test1FUUP73() {
	this.runTestCheckDietParse(
		// compilationUnit:
		"var s = \"hello\";			\n" +
		"var o = s.concat(\"boo\");	\n",
		// completeBehind:
		"var o = s",
		// expectedCompletionNodeToString:
		"<CompleteOnName:s>",
		// expectedUnitDisplayString:
		"var s = \"hello\";\n" +
		"var o = <CompleteOnName:s>.concat(\"boo\");\n",
		// expectedCompletionIdentifier:
		"s",
		// expectedReplacedSource:
		"s",
		// test name
		"<1FUUP73>"
	);
	this.runTestCheckDietParse(
		// compilationUnit:
		"var s = \"hello\";			\n" +
		"var o = s.concat(\"boo\");	\n",
		// completeBehind:
		"var o = s.c",
		// expectedCompletionNodeToString:
		"<CompleteOnMessageSend:s.c(\"boo\")>",
		// expectedUnitDisplayString:
		"var s = \"hello\";\n" +
		"var o = <CompleteOnMessageSend:s.c(\"boo\")>;\n",
		// expectedCompletionIdentifier:
		"c",
		// expectedReplacedSource:
		"concat(\"boo\")",
		// test name
		"<1FUUP73>"
	);
}
/*
 * Completion on an empty name reference. 
 */
public void testEmptyNameReference() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	var i = 0;							\n" +
		"											\n" +
		"}										\n",
		// completeBehind:
		"var i = 0;							\n		",
		// expectedCompletionNodeToString:
		"<CompleteOnName:>",
		// expectedUnitDisplayString:
		"function foo() {\n" +
		"  var i = 0;\n" + 
		"  <CompleteOnName:>;\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"",
		// test name
		"<complete on empty name reference>"
	);
}
/*
 * Completion on an empty name reference after + operator. 
 */
public void testEmptyNameReferenceAfterPlus() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	1 + 								\n" +
		"											\n" +
		"}										\n",
		// completeBehind:
		"1 +",
		// expectedCompletionNodeToString:
		"<CompleteOnName:>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  (1 + <CompleteOnName:>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"",
		// test name
		"<complete on empty name reference after + operator>"
	);
}
/*
 * Completion in the statement following an if expression. 
 */
public void testInIfThenStatement() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	if (bar()) 							\n" +
		"											\n" +
		"											\n" +
		"}										\n",
		// completeBehind:
		"\n			",
		// expectedCompletionNodeToString:
		"<CompleteOnName:>",
		// expectedUnitDisplayString:
		"function foo() {\n" +
		"  if (bar())\n" +
		"      <CompleteOnName:>;\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"",
		// test name
		"<complete in if then statement>"
	);
}
/*
 * Completion on an empty name reference inside an invocation in a field initializer. 
 */
public void testInvocationFieldInitializer() {
	this.runTestCheckDietParse(
		// compilationUnit:
		"var s = fred(1 + );					\n" +
		"function foo() {							\n" +
		"}										\n",
		// completeBehind:
		"(1 + ",
		// expectedCompletionNodeToString:
		"<CompleteOnName:>",
		// expectedUnitDisplayString:
		"var s = fred((1 + <CompleteOnName:>));\n" +
		"function foo() {\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"",
		// test name
		"<complete on empty name reference in invocation in field initializer>"
	);
}
/*
 * Completion on a qualified name reference that contains a unicode. 
 */
public void testUnicode() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {			\n" + 
		"	bar.\\u005ax 		\n" +
		"}						\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:bar.Zx>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  <CompleteOnMemberAccess:bar.Zx>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"Zx",
		// expectedReplacedSource:
		"\\u005ax",
		// test name
		"<complete on unicode>"
	);
}
}
