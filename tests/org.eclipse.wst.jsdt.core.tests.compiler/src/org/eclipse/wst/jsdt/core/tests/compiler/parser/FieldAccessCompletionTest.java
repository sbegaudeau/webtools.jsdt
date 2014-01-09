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

import junit.framework.Test;

/**
 * Completion is expected to be a FieldAccess.
 */
public class FieldAccessCompletionTest extends AbstractCompletionTest {
public FieldAccessCompletionTest(String testName) {
	super(testName);
}
/*
 * AdditiveExpression ::= AdditiveExpression '-' <MultiplicativeExpression>  
 */
public void testAdditiveExpressionMinus() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {										\n" +
		"	return 1 - fred().xyz;						\n" +
		"}												\n",
			// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (1 - <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on additive expression minus>"
	);
}
/*
 * AdditiveExpression ::= AdditiveExpression '+' <MultiplicativeExpression> 
 */
public void testAdditiveExpressionPlus() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {										\n" +
		"	return 1 + fred().xyz;						\n" +
		"}												\n",
			// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (1 + <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on additive expression plus>"
	);
}
/*
 * AndExpression ::= AndExpression '&' <EqualityExpression>
 */
public void testAndExpression() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {									\n" +
		"	return isTrue & fred().xyz;					\n" +
		"}												\n",
			// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (isTrue & <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// expectedReplacedSource:
		"<complete on and expression>"
	);
}
/*
 * ArgumentList ::= ArgumentList ',' <Expression> 
 */
public void testArgumentList() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {								\n" +
		"	bizz(1, \"2\", fred().xyz);				\n" +
		"}											\n",
			// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  bizz(1, \"2\", <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on argument list>"
	);
}
/*
 * ArrayAccess ::= Name '[' <Expression> ']' 
 */
public void testArrayAccess() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {									\n" +
		"	return v[fred().xyz];						\n" +
		"}											\n",
			// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return v[<CompleteOnMemberAccess:fred().x>];\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on array access>"
	);
}
/*
 * ArrayInitializer ::= '{' <VariableInitializers> '}' 
 */
public void testArrayInitializer() {
	this.runTestCheckMethodParse(
		// compilationUnit:
		"function foo() {										\n" +
		"	var i = [fred().xyz];				\n" +
		"}													\n",
			// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  var i = [<CompleteOnMemberAccess:fred().x>];\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on array initializer>"
	);
}
/*
 * ArrayInitializer ::= '{' <VariableInitializers> , '}' 
 */
public void testArrayInitializerComma() {
	this.runTestCheckMethodParse(
			// compilationUnit:
			"function foo() {										\n" +
			"	var i = [fred().xyz];				\n" +
			"}													\n",
				// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  var i = [<CompleteOnMemberAccess:fred().x>];\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on array initializer comma>"
	);
}
/*
 * Assignment ::= LeftHandSide AssignmentOperator <AssignmentExpression> 
 */
public void testAssignment() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {										\n" +
		"	i = fred().xyz;									\n" +
		"}													\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  i = <CompleteOnMemberAccess:fred().x>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on assignment>"
	);
}
/*
 * Block ::= OpenBlock '{' <BlockStatementsopt> '}' 
 */
public void testBlock() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	try {									\n" +
		"		fred().xyz = new Foo();				\n" +
		"	} catch (e) {}				\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  try \n" +
		"    {\n" + 
		"      <CompleteOnMemberAccess:fred().x> = new Foo();\n" + 
		"    }\n" + 
		"  catch (e)     {\n" +
		"    }\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on block>"
	);
}
/*
 * BlockStatements ::= BlockStatements <BlockStatement>
 */
public void testBlockStatements() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	var i = 0;								\n" +
		"	fred().xyz = new Foo();					\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  var i = 0;\n" + 
		"  <CompleteOnMemberAccess:fred().x> = new Foo();\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on block statements>"
	);
}
/*
 * BlockStatements ::= BlockStatements <BlockStatement>
 *
 * in a non static initializer.
 */
public void testBlockStatementsInInitializer() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function Bar() {									\n" +
		"	{											\n" +
		"		var i = 0;								\n" +
		"		fred().xyz = new Foo();					\n" +
		"	}											\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function Bar() {\n" + 
		"  {\n" + 
		"    var i = 0;\n" + 
		"    <CompleteOnMemberAccess:fred().x> = new Foo();\n" + 
		"  }\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on block statements in initializer>"
	);
}
/*
 * ClassInstanceCreationExpression ::= 'new' ClassType '(' <ArgumentListopt> ')' ClassBodyopt 
 */
public void testClassInstanceCreationExpression() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	new Bar(fred().xyz);						\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  new   Bar(<CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on class instance creation expression>"
	);
}
/*
 * ConditionalAndExpression ::= ConditionalAndExpression '&&' <InclusiveOrExpression> 
 */
public void testConditionalAndExpression() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {									\n" +
		"	return isTrue && fred().xyz;					\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (isTrue && <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on conditional and expression>"
	);
}
/*
 * ConditionalExpression ::= ConditionalOrExpression '?' <Expression> ':' ConditionalExpression
 */
public void testConditionalExpression() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {										\n" +
		"	return fred().xyz == null ? null : new Bar();	\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return ((<CompleteOnMemberAccess:fred().x> == null) ? null : new Bar());\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on conditional expression>"
	);
}
/*
 * ConditionalExpression ::= ConditionalOrExpression '?' Expression ':' <ConditionalExpression> 
 */
public void testConditionalExpressionConditionalExpression() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {									\n" +
		"	return isTrue ? true : fred().xyz;			\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (isTrue ? true : <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on conditional expression conditional expression>"
	);
}
/*
 * ConditionalOrExpression ::= ConditionalOrExpression '||' <ConditionalAndExpression> 
 */
public void testConditionalOrExpression() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {									\n" +
		"	return isTrue || fred().xyz;					\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (isTrue || <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on conditional or expression>"
	);
}
/*
 * DoStatement ::= 'do' Statement 'while' '(' <Expression> ')' ';'  
 */
public void testDoExpression() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	do										\n" +
		"		System.out.println();				\n" +
		"	while (fred().xyz);						\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  do\n" +
		"    System.out.println();\n" +
		"while (<CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on do expression>"
	);
}
/*
 * DoStatement ::= 'do' <Statement> 'while' '(' Expression ')' ';'  
 */
public void testDoStatement() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	do										\n" +
		"		fred().xyz = new Foo();				\n" +
		"	while (true);							\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  do\n" +
		"    <CompleteOnMemberAccess:fred().x> = new Foo();\n" + 
		"while (true);\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on do statement>"
	);
}
/*
 * EqualityExpression ::= EqualityExpression '==' <RelationalExpression> 
 */
public void testEqualityExpression() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {									\n" +
		"	return 1 == fred().xyz;						\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (1 == <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on equality expression>"
	);
}
/*
 * EqualityExpression ::= EqualityExpression '!=' <RelationalExpression> 
 */
public void testEqualityExpressionNot() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {									\n" +
		"	return 1 != fred().xyz;						\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (1 != <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on equality expression not>"
	);
}
/*
 * ExclusiveOrExpression ::= ExclusiveOrExpression '^' <AndExpression> 
 */
public void testExclusiveOrExpression() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {									\n" +
		"	return isTrue ^ fred().xyz;					\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (isTrue ^ <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on exclusive or expression>"
	);
}
/*
 * ForStatement ::= 'for' '(' <ForInitopt> ';' Expressionopt ';' ForUpdateopt ')' Statement
 * or
 * ForStatementNoShortIf ::= 'for' '(' <ForInitopt> ';' Expressionopt ';' ForUpdateopt ')' StatementNoShortIf 
 */
public void testForInit() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	for (var i = fred().xyz; i < 2; i++)		\n" +
		"		System.out.println();				\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  for (var i = <CompleteOnMemberAccess:fred().x>;; (i < 2); i ++) \n" + 
		"    System.out.println();\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on for init>"
	);
}
/*
 * ForStatement ::= 'for' '(' ForInitopt ';' Expressionopt ';' ForUpdateopt ')' <Statement>
 * or
 * ForStatementNoShortIf ::= 'for' '(' ForInitopt ';' Expressionopt ';' ForUpdateopt ')' <StatementNoShortIf> 
 */
public void testForStatement() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	for (var i = 0; i < 2; i++)				\n" +
		"		fred().xyz = new Foo();				\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  for (var i = 0;; (i < 2); i ++) \n" +
		"    <CompleteOnMemberAccess:fred().x> = new Foo();\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on for statement>"
	);
}
/*
 * ForStatement ::= 'for' '(' ForInitopt ';' <Expressionopt> ';' ForUpdateopt ')' Statement 
 * or
 * ForStatementNoShortIf ::= 'for' '(' ForInitopt ';' <Expressionopt> ';' ForUpdateopt ')' StatementNoShortIf  
 */
public void testForStatementExpression() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	for(var i = 0; fred().xyz > i; i++)		\n" +
		"		System.out.println();				\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  for (var i = 0;; (<CompleteOnMemberAccess:fred().x> > i); i ++) \n" + 
		"    System.out.println();\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on for statement expression>"
	);
}
/*
 * ForStatement ::= 'for' '(' ForInitopt ';' Expressionopt ';' <ForUpdateopt> ')' Statement 
 * or
 * ForStatementNoShortIf ::= 'for' '(' ForInitopt ';' Expressionopt ';' <ForUpdateopt> ')' StatementNoShortIf 
 */
public void testForUpdate() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	for (var i = 0; i < 2; i+= fred().xyz)	\n" +
		"		System.out.println();				\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  for (var i = 0;; (i < 2); i += <CompleteOnMemberAccess:fred().x>) \n" + 
		"    System.out.println();\n" +  
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on for update>"
	);
}
/*
 * IfThenStatement ::= 'if' '(' <Expression> ')' Statement 
 */
public void testIfExpresionThen() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	if (fred().xyz)							\n" +
		"		System.out.println();				\n"	+
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  if (<CompleteOnMemberAccess:fred().x>)\n" + 
		"      System.out.println();\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on \"if expression then\">"
	);
}
/*
 * IfThenElseStatement ::= 'if' '(' <Expression> ')' StatementNoShortIf 'else' Statement
 * or
 * IfThenElseStatementNoShortIf ::= 'if' '(' <Expression> ')' StatementNoShortIf 'else' StatementNoShortIf 
 */
public void testIfExpresionThenElse() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	if (fred().xyz)							\n" +
		"		System.out.println();				\n"	+
		"	else									\n" +
		"		System.out.println();				\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  if (<CompleteOnMemberAccess:fred().x>)\n" + 
		"      System.out.println();\n" +
		"  else\n" +
		"      System.out.println();\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on \"if expression then else\">"
	);
}
/*
 * IfThenElseStatement ::= 'if' '(' Expression ')' StatementNoShortIf 'else' <Statement>
 * or
 * IfThenElseStatementNoShortIf ::= 'if' '(' Expression ')' StatementNoShortIf 'else' <StatementNoShortIf>
 */
public void testIfThenElseStatement() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	if (false)								\n" +
		"		 System.out.println();				\n"	+
		"	else									\n" +
		"		fred().xyz = new Foo();				\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  if (false)\n" +
		"      System.out.println();\n" +
		"  else\n" +
		"      <CompleteOnMemberAccess:fred().x> = new Foo();\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on \"if then else\" statement>"
	);
}
/*
 * IfThenStatement ::= 'if' '(' Expression ')' <Statement> 
 */
public void testIfThenStatement() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	if (true)								\n" +
		"		fred().xyz = new Foo();				\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  if (true)\n" +
		"      <CompleteOnMemberAccess:fred().x> = new Foo();\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on \"if then\" statement>"
	);
}
/*
 * IfThenStatementElse ::= 'if' '(' Expression ')' <StatementNoShortIf> 'else' Statement
 * or
 * IfThenElseStatementNoShortIf ::= 'if' '(' Expression ')' <StatementNoShortIf> 'else' StatementNoShortIf
 */
public void testIfThenStatementElse() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	if (true)								\n" +
		"		fred().xyz = new Foo();				\n"	+
		"	else									\n" +
		"		System.out.println();				\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  if (true)\n" +
		"      <CompleteOnMemberAccess:fred().x> = new Foo();\n" + 
		"  else\n" +
		"      System.out.println();\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on \"if then statement else\">"
	);
}
/*
 * InclusiveOrExpression ::= InclusiveOrExpression '|' <ExclusiveOrExpression> 
 */
public void testInclusiveOrExpression() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {									\n" +
		"	return isTrue | fred().xyz;					\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (isTrue | <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on inclusive or expression>"
	);
}
/*
 * LabeledStatement ::= 'Identifier' ':' <Statement>
 * or
 * LabeledStatementNoShortIf ::= 'Identifier' ':' <StatementNoShortIf> 
 */
public void testLabeledStatement() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	fredCall: fred().xyz = new Foo();			\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  fredCall: <CompleteOnMemberAccess:fred().x> = new Foo();\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// expectedLabels:
		new String[] {"fredCall"},
		// test name
		"<complete on labeled statement>"
	);
}
/*
 * MethodBody ::= NestedMethod '{' <BlockStatementsopt> '}'
 */
public void testMethodBody() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	fred().xyz = new Foo();					\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  <CompleteOnMemberAccess:fred().x> = new Foo();\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on method body>"
	);
}
/*
 * FunctionInvocation ::= Name '(' <ArgumentListopt> ')'  
 */
public void testMethodInvocation() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	bizz(fred().xyz);							\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  bizz(<CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on method invocation>"
	);
}
/*
 * FunctionInvocation ::= Primary '.' 'Identifier' '(' <ArgumentListopt> ')'  
 */
public void testMethodInvocationPrimary() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	buzz().bizz(fred().xyz);					\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  buzz().bizz(<CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on method invocation primary>"
	);
}
/*
 * MultiplicativeExpression ::= MultiplicativeExpression '/' <UnaryExpression>
 */
public void testMultiplicativeExpressiondDivision() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {									\n" +
		"	return 2 / fred().xyz;						\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (2 / <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on multiplicative expression division>"
	);
}
/*
 * MultiplicativeExpression ::= MultiplicativeExpression '*' <UnaryExpression> 
 */
public void testMultiplicativeExpressionMultiplication() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {										\n" +
		"	return 2 * fred().xyz;						\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (2 * <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on multiplicative expression multiplication>"
	);
}
/*
 * MultiplicativeExpression ::= MultiplicativeExpression '%' <UnaryExpression> 
 */
public void testMultiplicativeExpressionRemainder() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {										\n" +
		"	return 2 % fred().xyz;						\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (2 % <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on multiplicative expression remainder>"
	);
}
/*
 * PreDecrementExpression ::= '--' PushPosition <UnaryExpression> 
 */
public void testPreIncrementExpressionMinusMinus() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	-- fred().xyz;							\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  -- <CompleteOnMemberAccess:fred().x>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on pre increment expression minus minus>"
	);
}
/*
 * PreIncrementExpression ::= '++' PushPosition <UnaryExpression> 
 */
public void testPreIncrementExpressionPlusPlus() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	++ fred().xyz;							\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  ++ <CompleteOnMemberAccess:fred().x>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on pre increment expression plus plus>"
	);
}
/*
 * PrimaryNoNewArray ::= PushLPAREN <Expression> PushRPAREN
 */
public void testPrimaryNoNewArray() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	(fred().xyz).zzz();						\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  <CompleteOnMemberAccess:fred().x>.zzz();\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"(fred().xyz)",
		// test name
		"<complete on primary no new array>"
	);
}
/*
 * RelationalExpression ::= RelationalExpression '>' <ShiftExpression> 
 */
public void testRelationalExpressionGreaterThan() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {									\n" +
		"	return 1 > fred().xyz;						\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (1 > <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on relational expression greater than>"
	);
}
/*
 * RelationalExpression ::= RelationalExpression '>=' <ShiftExpression> 
 */
public void testRelationalExpressionGreaterThanOrEquals() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {									\n" +
		"	return 1 >= fred().xyz;						\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (1 >= <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on relational expression greater than or equal>"
	);
}
/*
 * RelationalExpression ::= RelationalExpression '<' <ShiftExpression>  
 */
public void testRelationalExpressionLessThan() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {									\n" +
		"	return 1 < fred().xyz;						\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (1 < <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on relational expression less than>"
	);
}
/*
 * RelationalExpression ::= RelationalExpression '<=' <ShiftExpression> 
 */
public void testRelationalExpressionLessThanOrEqual() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {									\n" +
		"	return 1 <= fred().xyz;						\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (1 <= <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on relational expression less than or equal>"
	);
}
/*
 * ReturnStatement ::= 'return' <Expressionopt> ';
 */
public void testReturnStatement() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {									\n" +
		"	return fred().xyz;						\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return <CompleteOnMemberAccess:fred().x>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on return statement>"
	);
}
/*
 * ShiftExpression ::= ShiftExpression '<<' <AdditiveExpression> 
 */
public void testShiftExpressionLeft() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {										\n" +
		"	return i << fred().xyz;						\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (i << <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on shift expression left>"
	);
}
/*
 * ShiftExpression ::= ShiftExpression '>>' <AdditiveExpression>
 */
public void testShiftExpressionRight() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {										\n" +
		"	return i >> fred().xyz;						\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (i >> <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on shift expression right>"
	);
}
/*
 * ShiftExpression ::= ShiftExpression '>>>' <AdditiveExpression>
 */
public void testShiftExpressionRightUnSigned() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {										\n" +
		"	return i >>> fred().xyz;						\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  return (i >>> <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on shift expression right unsigned>"
	);
}
/*
 * StatementExpressionList ::= StatementExpressionList ',' <StatementExpression> 
 */
public void testStatementExpressionList() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {										\n" +
		"	for (var i = 0, length = fred().xyz; i < 2; i++)	\n" +
		"		System.out.println();						\n" +
		"}													\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  for (var i = 0;, var length = <CompleteOnMemberAccess:fred().x>;; (i < 2); i ++) \n" + 
		"    System.out.println();\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on statement expression list>"
	);
}
/*
 * SwitchBlockStatement ::= SwitchLabels <BlockStatements>   
 */
public void testSwitchBlockStatement() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	var i =  0;								\n" +
		"	switch (i) {							\n" +
		"		case 0: fred().xyz = new Foo();		\n" +
		"	}										\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  var i = 0;\n" +
		"  switch (i) {\n" +
		"  case 0 : ;\n" +
		"      <CompleteOnMemberAccess:fred().x> = new Foo();\n" +
		"  }\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on switch block statement>"
	);
}
/*
 * SwitchStatement ::= 'switch' OpenBlock '(' <Expression> ')' SwitchBlock    
 */
public void testSwitchExpression() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	switch (fred().xyz) {						\n" +
		"		case 0: System.out.println();		\n" +
		"	}										\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  switch (<CompleteOnMemberAccess:fred().x>) {\n" +
		"  case 0 : ;\n" +
		"      System.out.println();\n" + 
		"  }\n" +
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on switch expression>"
	);
}
/*
 * SwitchLabel ::= 'case' <ConstantExpression> ':' 
 */
public void testSwitchLabel() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {									\n" +
		"	var i =  0;									\n" +
		"	switch (i) {								\n" +
		"		case fred().xyz: System.out.println();	\n" +
		"	}											\n" +
		"}												\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  var i = 0;\n" + 
		"  switch (i) {\n" + 
		"  case <CompleteOnMemberAccess:fred().x> : ;\n" +
		"      System.out.println();\n" + 
		"  }\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on switch label>"
	);
}
/*
 * ThrowStatement ::= 'throw' <Expression> ';' 
 */
public void testThrowExpression() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	throw fred().xyz;							\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  throw <CompleteOnMemberAccess:fred().x>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on throw expression>"
	);
}
/*
 * UnaryExpressionNotPlusMinus ::= '~' PushPosition <UnaryExpression> 
 */
public void testUnaryExpressionBitwiseComplement() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	i = ~ fred().xyz;							\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  i = (~ <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on unary expression bitwise complement>"
	);
}
/*
 * UnaryExpressionNotPlusMinus ::= '!' PushPosition <UnaryExpression>  
 */
public void testUnaryExpressionLogicalComplement() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	i = ! fred().xyz;							\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  i = (! <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on unary expression logical complement>"
	);
}
/*
 * UnaryExpression ::= '-' PushPosition <UnaryExpression> 
 */
public void testUnaryExpressionMinus() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	i = - fred().xyz;							\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  i = (- <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on unary expression minus>"
	);
}
/*
 * UnaryExpression ::= '+' PushPosition <UnaryExpression>
 */
public void testUnaryExpressionPlus() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	i = + fred().xyz;							\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  i = (+ <CompleteOnMemberAccess:fred().x>);\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on unary expression plus>"
	);
}
/*
 * VariableDeclarator ::= VariableDeclaratorId EnterField '=' ForceNoDiet <VariableInitializer> RestoreDiet ExitField 
 */
public void testVariableDeclarator() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {										\n" +
		"	var i = fred().xyz;								\n" +
		"}													\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  var i = <CompleteOnMemberAccess:fred().x>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on variable declarator>"
	);
}
/*
 * VariableInitializers ::= VariableInitializers ',' <VariableInitializer>
 */
public void testVariableInitializers() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {										\n" +
		"	var i = 0, j = fred().xyz;						\n" +
		"}													\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  var i = 0, j = <CompleteOnMemberAccess:fred().x>;\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on variable initializers>"
	);
}
/*
 * WhileStatement ::= 'while' '(' <Expression> ')' Statement 
 * or
 * WhileStatementNoShortIf ::= 'while' '(' <Expression> ')' StatementNoShortIf 
 */
public void testWhileExpression() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	while (fred().xyz)						\n" +
		"		System.out.println();				\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  while (<CompleteOnMemberAccess:fred().x>)" +
		"    System.out.println();\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on while expresion>"
	);
}
/*
 * WhileStatement ::= 'while' '(' Expression ')' <Statement>
 * or
 * WhileStatementNoShortIf ::= 'while' '(' Expression ')' <StatementNoShortIf> 
 */
public void testWhileStatement() {
	this.runTestCheckMethodParse(
		// compilationUnit: 
		"function foo() {								\n" +
		"	while (true)							\n" +
		"		fred().xyz = new Foo();				\n" +
		"}											\n",
		// completeBehind:
		"x",
		// expectedCompletionNodeToString:
		"<CompleteOnMemberAccess:fred().x>",
		// expectedUnitDisplayString:
		"function foo() {\n" + 
		"  while (true)" +
		"    <CompleteOnMemberAccess:fred().x> = new Foo();\n" + 
		"}\n",
		// expectedCompletionIdentifier:
		"x",
		// expectedReplacedSource:
		"xyz",
		// test name
		"<complete on while statement>"
	);
}
}
