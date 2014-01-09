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

/**
 * Completion is expected to be a ReferenceType.
 */
public class ReferenceTypeCompletionTest extends AbstractCompletionTest {
public ReferenceTypeCompletionTest(String testName) {
	super(testName);
}
/*
 * ClassInstanceCreationExpression ::= 'new' <ClassType> '(' ArgumentListopt ')' ClassBodyopt 
 */
public void testClassInstanceCreationExpression1() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	new Xxx().zzz();					\n" +
		"}										\n",
		// completeBehind:
		"X",
		// expectedCompletionNodeToString:
		"<CompleteOnName:X>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  new <CompleteOnType:X>().zzz();\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"X",
		// expectedReplacedSource:
		"Xxx",
		// test name
		"<complete on class instance creation expression 1>"
	);
}
/*
 * ClassInstanceCreationExpression ::= 'new' <ClassType> '(' ArgumentListopt ')' ClassBodyopt 
 */
public void testClassInstanceCreationExpression2() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	new Y(new Xxx()).zzz();				\n" +
		"}										\n",
		// completeBehind:
		"X",
		// expectedCompletionNodeToString:
		"<CompleteOnName:X>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  new Y(new <CompleteOnType:X>()).zzz();\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"X",
		// expectedReplacedSource:
		"Xxx",
		// test name
		"<complete on class instance creation expression 2>"
	);
}
/*
 * ClassInstanceCreationExpression ::= 'new' <ClassType> '(' ArgumentListopt ')' ClassBodyopt 
 */
public void testClassInstanceCreationExpression3() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	new Y(1, true, new Xxx()).zzz();	\n" +
		"}										\n",
		// completeBehind:
		"X",
		// expectedCompletionNodeToString:
		"<CompleteOnName:X>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  new Y(1, true, new <CompleteOnType:X>()).zzz();\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"X",
		// expectedReplacedSource:
		"Xxx",
		// test name
		"<complete on class instance creation expression 3>"
	);
}
/*
 * RelationalExpression ::= RelationalExpression 'instanceof' <ReferenceType>
 */
public void testInstanceOf() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {								\n" +
		"	return this instanceof Xxx;				\n" +
		"}											\n",
		// completeBehind:
		"X",
		// expectedCompletionNodeToString:
		"<CompleteOnName:X>",
		// expectedUnitDisplayString:
		"function foo() {\n" +
		"  return (this instanceof <CompleteOnName:X>);\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"X",
		// expectedReplacedSource:
		"Xxx",
		// test name
		"<complete on instanceof>"
	);
}

/*
 * RelationalExpression ::= RelationalExpression 'typeof' <ReferenceType>
 */
public void testTypeOf() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {								\n" +
		"	return this typeof Xxx;				\n" +
		"}											\n",
		// completeBehind:
		"X",
		// expectedCompletionNodeToString:
		"<CompleteOnName:X>",
		// expectedUnitDisplayString:
		"function foo() {\n" +
		"  (typeof <CompleteOnName:X>);\n" +
		"}\n" +
		";\n",
		// expectedCompletionIdentifier:
		"X",
		// expectedReplacedSource:
		"Xxx",
		// test name
		"<complete on instanceof>"
	);
}
/*
 * Completion on a qualified type reference, where the cursor is in the 
 * first type reference. 
 */
public void testQualifiedTypeReferenceShrinkAll() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {							\n" +
		"	var i = 0;							\n" +
		"	new a.b.c.Xxx();			\n" +
		"}										\n",
		// completeBehind:
		"	new a",
		// expectedCompletionNodeToString:
		"<CompleteOnName:a>",
		// expectedUnitDisplayString:
		"function foo() {\n" +
		"  var i = 0;\n" + 
		"  new   <CompleteOnName:a>.b.c.Xxx();\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"a",
		// expectedReplacedSource:
		"a",
		// test name
		"<complete on qualified type reference (shrink all)>"
	);
}
/*
 * Completion on a qualified type reference, where the cursor is right after the first dot. 
 */
public void testQualifiedTypeReferenceShrinkAllButOne() {
	this.runTestCheckMethodParse(
		// compilationUnit:
			"function foo() {							\n" +
			"	var i = 0;							\n" +
			"	new a.b.c.Xxx();			\n" +
			"}										\n",
		// completeBehind:
		"a.",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:a.>",
		// expectedUnitDisplayString:
		"function foo() {\n" +
		"  var i = 0;\n" + 
		"  new   <CompleteOnMemberAccess:a.>.c.Xxx();\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"b",
		// test name
		"<complete on qualified type reference (shrink all but one)>"
	);
}
/*
 * Completion on a qualified type reference, where the cursor is right after the end 
 * of the last type reference. 
 */
public void testQualifiedTypeReferenceShrinkNone() {
	this.runTestCheckMethodParse(
		// compilationUnit:
			"function foo() {							\n" +
			"	var i = 0;							\n" +
			"	new a.b.c.Xxx();			\n" +
			"}										\n",
		// completeBehind:
		"X",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:a.b.c.X>",
		// expectedUnitDisplayString:
		"function foo() {\n" +
		"  var i = 0;\n" + 
		"  new   <CompleteOnType:a.b.c.X>();\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"X",
		// expectedReplacedSource:
		"Xxx",
		// test name
		"<complete on qualified type reference (shrink none)>"
	);
}
/*
 * Completion on a qualified type reference, where the cursor is right after the 
 * last dot. 
 */
public void testQualifiedTypeReferenceShrinkOne() {
	this.runTestCheckMethodParse(
		// compilationUnit:
			"function foo() {							\n" +
			"	var i = 0;							\n" +
			"	new a.b.c.Xxx();			\n" +
			"}										\n",
		// completeBehind:
		"a.b.c.",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:a.b.c.>",
		// expectedUnitDisplayString:
		"function foo() {\n" +
		"  var i = 0;\n" + 
		"  new   <CompleteOnType:a.b.c.>();\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"",
		// expectedReplacedSource:
		"Xxx",
		// test name
		"<complete on qualified type reference (shrink one)>"
	);
}
}
