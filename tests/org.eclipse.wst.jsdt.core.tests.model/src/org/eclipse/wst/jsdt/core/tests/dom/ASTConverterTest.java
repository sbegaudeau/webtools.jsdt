/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.core.tests.dom;

import java.util.List;

import junit.framework.Test;

import org.eclipse.wst.jsdt.core.IField;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTMatcher;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.ArrayAccess;
import org.eclipse.wst.jsdt.core.dom.ArrayCreation;
import org.eclipse.wst.jsdt.core.dom.ArrayInitializer;
import org.eclipse.wst.jsdt.core.dom.ArrayType;
import org.eclipse.wst.jsdt.core.dom.Assignment;
import org.eclipse.wst.jsdt.core.dom.Block;
import org.eclipse.wst.jsdt.core.dom.BooleanLiteral;
import org.eclipse.wst.jsdt.core.dom.BreakStatement;
import org.eclipse.wst.jsdt.core.dom.CatchClause;
import org.eclipse.wst.jsdt.core.dom.CharacterLiteral;
import org.eclipse.wst.jsdt.core.dom.ClassInstanceCreation;
import org.eclipse.wst.jsdt.core.dom.ConditionalExpression;
import org.eclipse.wst.jsdt.core.dom.ContinueStatement;
import org.eclipse.wst.jsdt.core.dom.DoStatement;
import org.eclipse.wst.jsdt.core.dom.EmptyStatement;
import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.ExpressionStatement;
import org.eclipse.wst.jsdt.core.dom.FieldAccess;
import org.eclipse.wst.jsdt.core.dom.ForStatement;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.IBinding;
import org.eclipse.wst.jsdt.core.dom.IFunctionBinding;
import org.eclipse.wst.jsdt.core.dom.ITypeBinding;
import org.eclipse.wst.jsdt.core.dom.IVariableBinding;
import org.eclipse.wst.jsdt.core.dom.IfStatement;
import org.eclipse.wst.jsdt.core.dom.InfixExpression;
import org.eclipse.wst.jsdt.core.dom.JSdoc;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.LabeledStatement;
import org.eclipse.wst.jsdt.core.dom.Modifier;
import org.eclipse.wst.jsdt.core.dom.Name;
import org.eclipse.wst.jsdt.core.dom.NullLiteral;
import org.eclipse.wst.jsdt.core.dom.NumberLiteral;
import org.eclipse.wst.jsdt.core.dom.ParenthesizedExpression;
import org.eclipse.wst.jsdt.core.dom.PostfixExpression;
import org.eclipse.wst.jsdt.core.dom.PrefixExpression;
import org.eclipse.wst.jsdt.core.dom.QualifiedName;
import org.eclipse.wst.jsdt.core.dom.ReturnStatement;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.SimpleType;
import org.eclipse.wst.jsdt.core.dom.SingleVariableDeclaration;
import org.eclipse.wst.jsdt.core.dom.Statement;
import org.eclipse.wst.jsdt.core.dom.StringLiteral;
import org.eclipse.wst.jsdt.core.dom.SwitchCase;
import org.eclipse.wst.jsdt.core.dom.SwitchStatement;
import org.eclipse.wst.jsdt.core.dom.ThrowStatement;
import org.eclipse.wst.jsdt.core.dom.TryStatement;
import org.eclipse.wst.jsdt.core.dom.Type;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationExpression;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationFragment;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.WhileStatement;

public class ASTConverterTest extends ConverterTestSetup {
	
	/** @deprecated using deprecated code */
	public void setUpSuite() throws Exception {
		super.setUpSuite();
		this.ast = AST.newAST(AST.JLS2);
	}

	public ASTConverterTest(String name) {
		super(name);
	}

	static {
//		TESTS_NUMBERS = new int[] { 305 };
	}
	public static Test suite() {
		return buildModelTestSuite(ASTConverterTest.class);
	}

	public void test00() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0005", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ExpressionStatement statement = (ExpressionStatement) getASTNode((JavaScriptUnit) result, 0, 0, 0);
		ASTNode expression=statement.getExpression();
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$

		FunctionInvocation methodInvocation = this.ast.newFunctionInvocation();
		ArrayAccess arrayAccess = this.ast.newArrayAccess();
		arrayAccess.setArray(this.ast.newSimpleName("args"));
		arrayAccess.setIndex(this.ast.newNumberLiteral("5"));
		methodInvocation.setExpression(arrayAccess);
		
		NumberLiteral literal = this.ast.newNumberLiteral("1");
		methodInvocation.arguments().add(literal);
		
		
		assertTrue("Both AST trees should be identical", methodInvocation.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "args[5](1)", source); //$NON-NLS-1$
	}
	
	/** @deprecated using deprecated code */
	public void test0001() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0001", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		
		// check that we have the right tree
		JavaScriptUnit unit = this.ast.newJavaScriptUnit();
//		PackageDeclaration packageDeclaration = this.ast.newPackageDeclaration();
//		packageDeclaration.setName(this.ast.newSimpleName("test0001"));//$NON-NLS-1$
//		unit.setPackage(packageDeclaration);
//		ImportDeclaration importDeclaration = this.ast.newImportDeclaration();
//		QualifiedName name = 
//			this.ast.newQualifiedName(
//				this.ast.newSimpleName("java"),//$NON-NLS-1$
//				this.ast.newSimpleName("util"));//$NON-NLS-1$
//		importDeclaration.setName(name);
//		importDeclaration.setOnDemand(true);
//		unit.imports().add(importDeclaration);
//		TypeDeclaration type = this.ast.newTypeDeclaration();
//		type.setInterface(false);
//		type.setModifiers(Modifier.PUBLIC);
//		type.setName(this.ast.newSimpleName("Test"));//$NON-NLS-1$
		FunctionDeclaration methodDeclaration = this.ast.newFunctionDeclaration();
		methodDeclaration.setConstructor(false);
//		methodDeclaration.setModifiers(Modifier.PUBLIC | Modifier.STATIC);
		methodDeclaration.setName(this.ast.newSimpleName("main"));//$NON-NLS-1$
//		methodDeclaration.setReturnType(this.ast.newPrimitiveType(PrimitiveType.VOID));
		SingleVariableDeclaration variableDeclaration = this.ast.newSingleVariableDeclaration();
//		variableDeclaration.setModifiers(Modifier.NONE);
//		variableDeclaration.setType(this.ast.newArrayType(this.ast.newSimpleType(this.ast.newSimpleName("String"))));//$NON-NLS-1$
		variableDeclaration.setName(this.ast.newSimpleName("args"));//$NON-NLS-1$
		methodDeclaration.parameters().add(variableDeclaration);
		org.eclipse.wst.jsdt.core.dom.Block block = this.ast.newBlock();
		FunctionInvocation methodInvocation = this.ast.newFunctionInvocation();
		QualifiedName name = 
			this.ast.newQualifiedName(
				this.ast.newSimpleName("System"),//$NON-NLS-1$
				this.ast.newSimpleName("out"));//$NON-NLS-1$
		methodInvocation.setExpression(name);
		methodInvocation.setName(this.ast.newSimpleName("println")); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setOperator(InfixExpression.Operator.PLUS);
		StringLiteral literal = this.ast.newStringLiteral();
		literal.setLiteralValue("Hello");//$NON-NLS-1$
		infixExpression.setLeftOperand(literal);
		literal = this.ast.newStringLiteral();
		literal.setLiteralValue(" world");//$NON-NLS-1$
		infixExpression.setRightOperand(literal);//$NON-NLS-1$
		methodInvocation.arguments().add(infixExpression);
		ExpressionStatement expressionStatement = this.ast.newExpressionStatement(methodInvocation);
		block.statements().add(expressionStatement);
		methodDeclaration.setBody(block);
		unit.statements().add(methodDeclaration);
		assertTrue("Both AST trees should be identical", result.subtreeMatch(new ASTMatcher(), unit));//$NON-NLS-1$
		String expected =
			"function main(args) {\n" + 
			"	print(\"Hello\" + \" world\");\n" + 
			"}" + 
			"";
		checkSourceRange(result, expected, source);
	}
	
	/**
	 * Test allocation expression: new Object() ==> ClassInstanceCreation
	 * @deprecated using deprecated code
	 */
	public void test0002() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0002", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		ClassInstanceCreation classInstanceCreation = this.ast.newClassInstanceCreation();
		classInstanceCreation.setMember(this.ast.newSimpleName("Object")); //$NON-NLS-1$
		assertTrue("Both AST trees should be identical", classInstanceCreation.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "new Object()", source); //$NON-NLS-1$
	}

	/**
	 * Test allocation expression: new java.lang.Object() ==> ClassInstanceCreation
	 * @deprecated using deprecated code
	 */
	public void test0003() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0003", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		ClassInstanceCreation classInstanceCreation = this.ast.newClassInstanceCreation();
		
		FieldAccess expr = this.ast.newFieldAccess();
		expr.setName(this.ast.newSimpleName("Object"));
		
		FieldAccess expr2 = this.ast.newFieldAccess();
		expr2.setName(this.ast.newSimpleName("lang"));
		expr2.setExpression(this.ast.newSimpleName("java"));
		
		expr.setExpression(expr2);

		classInstanceCreation.setMember(expr);
		assertTrue("Both AST trees should be identical", classInstanceCreation.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "new java.lang.Object()", source); //$NON-NLS-1$
	}

	/**
	 * Test allocation expression: new java.lang.Exception("ERROR") ==> ClassInstanceCreation
	 * @deprecated using deprecated code
	 */
	public void test0004() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0004", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		ClassInstanceCreation classInstanceCreation = this.ast.newClassInstanceCreation();

		FieldAccess expr = this.ast.newFieldAccess();
		expr.setName(this.ast.newSimpleName("Exception"));
		
		FieldAccess expr2 = this.ast.newFieldAccess();
		expr2.setName(this.ast.newSimpleName("lang"));
		expr2.setExpression(this.ast.newSimpleName("java"));
		
		expr.setExpression(expr2);

		classInstanceCreation.setMember(expr);
		StringLiteral literal = this.ast.newStringLiteral();
		literal.setLiteralValue("ERROR"); //$NON-NLS-1$
		classInstanceCreation.arguments().add(literal);
		assertTrue("Both AST trees should be identical", classInstanceCreation.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "new java.lang.Exception(\"ERROR\")", source); //$NON-NLS-1$
	}

	public void test0005() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0005", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ExpressionStatement statement = (ExpressionStatement) getASTNode((JavaScriptUnit) result, 0, 0, 0);
		ASTNode expression=statement.getExpression();
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$

		FunctionInvocation methodInvocation = this.ast.newFunctionInvocation();
		ArrayAccess arrayAccess = this.ast.newArrayAccess();
		arrayAccess.setArray(this.ast.newSimpleName("args"));
		arrayAccess.setIndex(this.ast.newNumberLiteral("5"));
		methodInvocation.setExpression(arrayAccess);
		
		NumberLiteral literal = this.ast.newNumberLiteral("1");
		methodInvocation.arguments().add(literal);
		
		
		assertTrue("Both AST trees should be identical", methodInvocation.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "args[5](1)", source); //$NON-NLS-1$
	}
	
				
//	/**
//	 * Test allocation expression: new java.lang.Runnable() { public void run() {}} ==> ClassInstanceCreation
//	 * @deprecated using deprecated code
//	 */
//	public void test0006() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0006", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
//		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
//		ClassInstanceCreation classInstanceCreation = this.ast.newClassInstanceCreation();
//		QualifiedName name = 
//			this.ast.newQualifiedName(
//				this.ast.newQualifiedName(
//					this.ast.newSimpleName("java"), //$NON-NLS-1$
//					this.ast.newSimpleName("lang")), //$NON-NLS-1$
//				this.ast.newSimpleName("Runnable"));//$NON-NLS-1$
//		classInstanceCreation.setName(name);
//		FunctionDeclaration methodDeclaration = this.ast.newMethodDeclaration();
//		methodDeclaration.setBody(this.ast.newBlock());
//		methodDeclaration.setConstructor(false);
//		methodDeclaration.setModifiers(Modifier.PUBLIC);
//		methodDeclaration.setName(this.ast.newSimpleName("run"));//$NON-NLS-1$
//		methodDeclaration.setReturnType(this.ast.newPrimitiveType(PrimitiveType.VOID));
//		AnonymousClassDeclaration anonymousClassDeclaration = this.ast.newAnonymousClassDeclaration();
//		anonymousClassDeclaration.bodyDeclarations().add(methodDeclaration);
//		classInstanceCreation.setAnonymousClassDeclaration(anonymousClassDeclaration);
//		assertTrue("Both AST trees should be identical", classInstanceCreation.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
//		checkSourceRange(expression, "new java.lang.Runnable() { public void run() {}}", source); //$NON-NLS-1$
//	}
//
//	/**
//	 * Test allocation expression: new Test().new D() ==> ClassInstanceCreation
//	 * @deprecated using deprecated code
//	 */
//	public void test0007() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0007", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertTrue("Not an ExpressionStatement", node instanceof ExpressionStatement); //$NON-NLS-1$
//		ExpressionStatement expressionStatement = (ExpressionStatement) node;
//		ASTNode expression = (ASTNode) ((FunctionInvocation) expressionStatement.getExpression()).arguments().get(0);
//		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
//		ClassInstanceCreation classInstanceCreation = this.ast.newClassInstanceCreation();
//		classInstanceCreation.setName(this.ast.newSimpleName("D")); //$NON-NLS-1$
//		ClassInstanceCreation classInstanceCreationExpression = this.ast.newClassInstanceCreation();
//		classInstanceCreationExpression.setName(this.ast.newSimpleName("Test")); //$NON-NLS-1$
//		classInstanceCreation.setExpression(classInstanceCreationExpression);
//		assertTrue("Both AST trees should be identical", classInstanceCreation.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
//		checkSourceRange(expression, "new Test().new D()", source); //$NON-NLS-1$
//	}

	/**
	 * Test allocation expression: new int[] {1, 2, 3, 4} ==> ArrayCreation
	 */
	public void test0008() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0008", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		ArrayInitializer arrayInitializer = this.ast.newArrayInitializer();
		arrayInitializer.expressions().add(this.ast.newNumberLiteral("1"));//$NON-NLS-1$
		arrayInitializer.expressions().add(this.ast.newNumberLiteral("2"));//$NON-NLS-1$
		arrayInitializer.expressions().add(this.ast.newNumberLiteral("3"));//$NON-NLS-1$
		arrayInitializer.expressions().add(this.ast.newNumberLiteral("4"));//$NON-NLS-1$
		assertTrue("Both AST trees should be identical", arrayInitializer.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "[1, 2, 3, 4]", source); //$NON-NLS-1$
	}

	/**
	 * Test allocation expression: new int[][] {{1}, {2}} ==> ArrayCreation
	 */
	public void test0009() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0009", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		ArrayInitializer arrayInitializer = this.ast.newArrayInitializer();
		ArrayInitializer innerArrayInitializer = this.ast.newArrayInitializer();
		innerArrayInitializer.expressions().add(this.ast.newNumberLiteral("1"));//$NON-NLS-1$
		arrayInitializer.expressions().add(innerArrayInitializer);
		innerArrayInitializer = this.ast.newArrayInitializer();
		innerArrayInitializer.expressions().add(this.ast.newNumberLiteral("2"));//$NON-NLS-1$
		arrayInitializer.expressions().add(innerArrayInitializer);
		assertTrue("Both AST trees should be identical", arrayInitializer.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "[[1], [2]]", source); //$NON-NLS-1$
	}

//	/**
//	 * Test allocation expression: new int[3] ==> ArrayCreation
//	 */
//	public void test0010() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0010", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
//		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
//		ArrayCreation arrayCreation = this.ast.newArrayCreation();
//		arrayCreation.setType(this.ast.newArrayType(this.ast.newPrimitiveType(PrimitiveType.INT), 1));
//		arrayCreation.dimensions().add(this.ast.newNumberLiteral("3")); //$NON-NLS-1$
//		assertTrue("Both AST trees should be identical", arrayCreation.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
//		checkSourceRange(expression, "new int[3]", source); //$NON-NLS-1$
//	}
//
//	/**
//	 * Test allocation expression: new int[3][] ==> ArrayCreation
//	 */
//	public void test0011() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0011", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
//		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
//		ArrayCreation arrayCreation = this.ast.newArrayCreation();
//		arrayCreation.setType(this.ast.newArrayType(this.ast.newPrimitiveType(PrimitiveType.INT), 2));
//		arrayCreation.dimensions().add(this.ast.newNumberLiteral("3")); //$NON-NLS-1$
//		assertTrue("Both AST trees should be identical", arrayCreation.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
//		checkSourceRange(expression, "new int[3][]", source); //$NON-NLS-1$
//	}
//		
//	/**
//	 * Test allocation expression: new int[][] {{},{}} ==> ArrayCreation
//	 */
//	public void test0012() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0012", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
//		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
//		ArrayCreation arrayCreation = this.ast.newArrayCreation();
//		arrayCreation.setType(this.ast.newArrayType(this.ast.newPrimitiveType(PrimitiveType.INT), 2));
//		ArrayInitializer arrayInitializer = this.ast.newArrayInitializer();
//		ArrayInitializer innerArrayInitializer = this.ast.newArrayInitializer();
//		arrayInitializer.expressions().add(innerArrayInitializer);
//		innerArrayInitializer = this.ast.newArrayInitializer();
//		arrayInitializer.expressions().add(innerArrayInitializer);
//		arrayCreation.setInitializer(arrayInitializer);
//		assertTrue("Both AST trees should be identical", arrayCreation.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
//		checkSourceRange(expression, "new int[][] {{}, {}}", source); //$NON-NLS-1$
//	}

	/**
	 * int i; ==> VariableDeclarationFragment
	 * @deprecated using deprecated code
	 */
	public void test0013() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0013", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("i")); //$NON-NLS-1$
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.INT));
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var i;", source); //$NON-NLS-1$
	}

	/**
	 * int i = 0; ==> VariableDeclarationFragment
	 * @deprecated using deprecated code
	 */
	public void test0014() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0014", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("i")); //$NON-NLS-1$
		variableDeclarationFragment.setInitializer(this.ast.newNumberLiteral("0"));//$NON-NLS-1$
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.INT));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var i = 0;", source); //$NON-NLS-1$
	}

	/**
	 * i = 1; ==> ExpressionStatement(Assignment)
	 */
	public void test0015() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0015", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		Assignment assignment = this.ast.newAssignment();
		assignment.setLeftHandSide(this.ast.newSimpleName("i")); //$NON-NLS-1$
		assignment.setRightHandSide(this.ast.newNumberLiteral("1")); //$NON-NLS-1$
		assignment.setOperator(Assignment.Operator.ASSIGN);
		ExpressionStatement statement = this.ast.newExpressionStatement(assignment);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "i = 1;", source); //$NON-NLS-1$
	}

	/**
	 * i += 2; ==> ExpressionStatement(Assignment)
	 */
	public void test0016() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0016", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		Assignment assignment = this.ast.newAssignment();
		assignment.setLeftHandSide(this.ast.newSimpleName("i")); //$NON-NLS-1$
		assignment.setRightHandSide(this.ast.newNumberLiteral("2")); //$NON-NLS-1$
		assignment.setOperator(Assignment.Operator.PLUS_ASSIGN);
		ExpressionStatement statement = this.ast.newExpressionStatement(assignment);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "i += 2;", source); //$NON-NLS-1$
	}

	/**
	 * i -= 2; ==> ExpressionStatement(Assignment)
	 */
	public void test0017() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0017", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		Assignment assignment = this.ast.newAssignment();
		assignment.setLeftHandSide(this.ast.newSimpleName("i")); //$NON-NLS-1$
		assignment.setRightHandSide(this.ast.newNumberLiteral("2")); //$NON-NLS-1$
		assignment.setOperator(Assignment.Operator.MINUS_ASSIGN);
		ExpressionStatement statement = this.ast.newExpressionStatement(assignment);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "i -= 2;", source); //$NON-NLS-1$
	}
	
	/**
	 * i *= 2; ==> ExpressionStatement(Assignment)
	 */
	public void test0018() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0018", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		Assignment assignment = this.ast.newAssignment();
		assignment.setLeftHandSide(this.ast.newSimpleName("i")); //$NON-NLS-1$
		assignment.setRightHandSide(this.ast.newNumberLiteral("2")); //$NON-NLS-1$
		assignment.setOperator(Assignment.Operator.TIMES_ASSIGN);
		ExpressionStatement statement = this.ast.newExpressionStatement(assignment);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "i *= 2;", source); //$NON-NLS-1$
	}

	/**
	 * i /= 2; ==> ExpressionStatement(Assignment)
	 */
	public void test0019() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0019", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		Assignment assignment = this.ast.newAssignment();
		assignment.setLeftHandSide(this.ast.newSimpleName("i")); //$NON-NLS-1$
		assignment.setRightHandSide(this.ast.newNumberLiteral("2")); //$NON-NLS-1$
		assignment.setOperator(Assignment.Operator.DIVIDE_ASSIGN);
		ExpressionStatement statement = this.ast.newExpressionStatement(assignment);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "i /= 2;", source); //$NON-NLS-1$
	}

	/**
	 * i &= 2 ==> ExpressionStatement(Assignment)
	 */
	public void test0020() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0020", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		Assignment assignment = this.ast.newAssignment();
		assignment.setLeftHandSide(this.ast.newSimpleName("i")); //$NON-NLS-1$
		assignment.setRightHandSide(this.ast.newNumberLiteral("2")); //$NON-NLS-1$
		assignment.setOperator(Assignment.Operator.BIT_AND_ASSIGN);
		ExpressionStatement statement = this.ast.newExpressionStatement(assignment);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "i &= 2;", source); //$NON-NLS-1$
	}

	/**
	 * i |= 2; ==> ExpressionStatement(Assignment)
	 */
	public void test0021() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0021", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		Assignment assignment = this.ast.newAssignment();
		assignment.setLeftHandSide(this.ast.newSimpleName("i")); //$NON-NLS-1$
		assignment.setRightHandSide(this.ast.newNumberLiteral("2")); //$NON-NLS-1$
		assignment.setOperator(Assignment.Operator.BIT_OR_ASSIGN);
		ExpressionStatement statement = this.ast.newExpressionStatement(assignment);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "i |= 2;", source); //$NON-NLS-1$
	}

	/**
	 * i ^= 2; ==> ExpressionStatement(Assignment)
	 */
	public void test0022() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0022", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		Assignment assignment = this.ast.newAssignment();
		assignment.setLeftHandSide(this.ast.newSimpleName("i")); //$NON-NLS-1$
		assignment.setRightHandSide(this.ast.newNumberLiteral("2")); //$NON-NLS-1$
		assignment.setOperator(Assignment.Operator.BIT_XOR_ASSIGN);
		ExpressionStatement statement = this.ast.newExpressionStatement(assignment);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "i ^= 2;", source); //$NON-NLS-1$
	}

	/**
	 * i %= 2; ==> ExpressionStatement(Assignment)
	 */
	public void test0023() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0023", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		Assignment assignment = this.ast.newAssignment();
		assignment.setLeftHandSide(this.ast.newSimpleName("i")); //$NON-NLS-1$
		assignment.setRightHandSide(this.ast.newNumberLiteral("2")); //$NON-NLS-1$
		assignment.setOperator(Assignment.Operator.REMAINDER_ASSIGN);
		ExpressionStatement statement = this.ast.newExpressionStatement(assignment);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "i %= 2;", source); //$NON-NLS-1$
	}

	/**
	 * i <<= 2; ==> ExpressionStatement(Assignment)
	 */
	public void test0024() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0024", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		Assignment assignment = this.ast.newAssignment();
		assignment.setLeftHandSide(this.ast.newSimpleName("i")); //$NON-NLS-1$
		assignment.setRightHandSide(this.ast.newNumberLiteral("2")); //$NON-NLS-1$
		assignment.setOperator(Assignment.Operator.LEFT_SHIFT_ASSIGN);
		ExpressionStatement statement = this.ast.newExpressionStatement(assignment);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "i <<= 2;", source); //$NON-NLS-1$
	}

	/**
	 * i >>= 2; ==> ExpressionStatement(Assignment)
	 */
	public void test0025() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0025", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		Assignment assignment = this.ast.newAssignment();
		assignment.setLeftHandSide(this.ast.newSimpleName("i")); //$NON-NLS-1$
		assignment.setRightHandSide(this.ast.newNumberLiteral("2")); //$NON-NLS-1$
		assignment.setOperator(Assignment.Operator.RIGHT_SHIFT_SIGNED_ASSIGN);
		ExpressionStatement statement = this.ast.newExpressionStatement(assignment);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "i >>= 2;", source); //$NON-NLS-1$
	}

	/**
	 * i >>>= 2; ==> ExpressionStatement(Assignment)
	 */
	public void test0026() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0026", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		Assignment assignment = this.ast.newAssignment();
		assignment.setLeftHandSide(this.ast.newSimpleName("i")); //$NON-NLS-1$
		assignment.setRightHandSide(this.ast.newNumberLiteral("2")); //$NON-NLS-1$
		assignment.setOperator(Assignment.Operator.RIGHT_SHIFT_UNSIGNED_ASSIGN);
		ExpressionStatement statement = this.ast.newExpressionStatement(assignment);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "i >>>= 2;", source); //$NON-NLS-1$
	}

	/**
	 * --i; ==> ExpressionStatement(PrefixExpression)
	 */
	public void test0027() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0027", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		PrefixExpression prefixExpression = this.ast.newPrefixExpression();
		prefixExpression.setOperand(this.ast.newSimpleName("i"));//$NON-NLS-1$
		prefixExpression.setOperator(PrefixExpression.Operator.DECREMENT);//$NON-NLS-1$
		ExpressionStatement statement = this.ast.newExpressionStatement(prefixExpression);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "--i;", source); //$NON-NLS-1$
	}

	/**
	 * --i; ==> ExpressionStatement(PrefixExpression)
	 */
	public void test0028() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0028", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		PrefixExpression prefixExpression = this.ast.newPrefixExpression();
		prefixExpression.setOperand(this.ast.newSimpleName("i"));//$NON-NLS-1$
		prefixExpression.setOperator(PrefixExpression.Operator.INCREMENT);//$NON-NLS-1$
		ExpressionStatement statement = this.ast.newExpressionStatement(prefixExpression);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "++i;", source); //$NON-NLS-1$
	}
	
	/**
	 * i--; ==> ExpressionStatement(PostfixExpression)
	 */
	public void test0029() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0029", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		PostfixExpression postfixExpression = this.ast.newPostfixExpression();
		postfixExpression.setOperand(this.ast.newSimpleName("i"));//$NON-NLS-1$
		postfixExpression.setOperator(PostfixExpression.Operator.DECREMENT);//$NON-NLS-1$
		ExpressionStatement statement = this.ast.newExpressionStatement(postfixExpression);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "i--;", source); //$NON-NLS-1$
	}

	/**
	 * i++; ==> ExpressionStatement(PostfixExpression)
	 */
	public void test0030() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0030", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		PostfixExpression postfixExpression = this.ast.newPostfixExpression();
		postfixExpression.setOperand(this.ast.newSimpleName("i"));//$NON-NLS-1$
		postfixExpression.setOperator(PostfixExpression.Operator.INCREMENT);//$NON-NLS-1$
		ExpressionStatement statement = this.ast.newExpressionStatement(postfixExpression);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "i++;", source); //$NON-NLS-1$
	}

//	/**
//	 * (String) o; ==> ExpressionStatement(CastExpression)
//	 * @deprecated using deprecated code
//	 */
//	public void test0031() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0031", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
//		variableDeclarationFragment.setName(this.ast.newSimpleName("s")); //$NON-NLS-1$
//		CastExpression castExpression = this.ast.newCastExpression();
//		castExpression.setExpression(this.ast.newSimpleName("o"));//$NON-NLS-1$
//		castExpression.setType(this.ast.newSimpleType(this.ast.newSimpleName("String")));//$NON-NLS-1$
//		variableDeclarationFragment.setInitializer(castExpression);
//		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
//		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newSimpleType(this.ast.newSimpleName("String")));//$NON-NLS-1$
//		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(node, "String s = (String) o;", source); //$NON-NLS-1$
//	}						
//
//	/**
//	 * (int) d; ==> ExpressionStatement(CastExpression)
//	 * @deprecated using deprecated code
//	 */
//	public void test0032() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0032", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
//		variableDeclarationFragment.setName(this.ast.newSimpleName("i")); //$NON-NLS-1$
//		CastExpression castExpression = this.ast.newCastExpression();
//		castExpression.setExpression(this.ast.newSimpleName("d"));//$NON-NLS-1$
//		castExpression.setType(this.ast.newPrimitiveType(PrimitiveType.INT));//$NON-NLS-1$
//		variableDeclarationFragment.setInitializer(castExpression);
//		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
//		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.INT));//$NON-NLS-1$
//		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(node, "int i = (int) d;", source); //$NON-NLS-1$
//	}	
//	
//	/**
//	 * (float) d; ==> ExpressionStatement(CastExpression)
//	 * @deprecated using deprecated code
//	 */
//	public void test0033() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0033", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//
//		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
//		variableDeclarationFragment.setName(this.ast.newSimpleName("f")); //$NON-NLS-1$
//		CastExpression castExpression = this.ast.newCastExpression();
//		castExpression.setExpression(this.ast.newSimpleName("d"));//$NON-NLS-1$
//		castExpression.setType(this.ast.newPrimitiveType(PrimitiveType.FLOAT));//$NON-NLS-1$
//		variableDeclarationFragment.setInitializer(castExpression);
//		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
//		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.FLOAT));//$NON-NLS-1$
//
//		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(node, "float f = (float) d;", source); //$NON-NLS-1$
//	}	
//
//	/**
//	 * (byte) d; ==> ExpressionStatement(CastExpression)
//	 * @deprecated using deprecated code
//	 */
//	public void test0034() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0034", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//
//		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
//		variableDeclarationFragment.setName(this.ast.newSimpleName("b")); //$NON-NLS-1$
//		CastExpression castExpression = this.ast.newCastExpression();
//		castExpression.setExpression(this.ast.newSimpleName("d"));//$NON-NLS-1$
//		castExpression.setType(this.ast.newPrimitiveType(PrimitiveType.BYTE));//$NON-NLS-1$
//		variableDeclarationFragment.setInitializer(castExpression);
//		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
//		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.BYTE));//$NON-NLS-1$
//
//		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(node, "byte b = (byte) d;", source); //$NON-NLS-1$
//	}	
//
//	/**
//	 * (short) d; ==> ExpressionStatement(CastExpression)
//	 * @deprecated using deprecated code
//	 */
//	public void test0035() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0035", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//
//		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
//		variableDeclarationFragment.setName(this.ast.newSimpleName("s")); //$NON-NLS-1$
//		CastExpression castExpression = this.ast.newCastExpression();
//		castExpression.setExpression(this.ast.newSimpleName("d"));//$NON-NLS-1$
//		castExpression.setType(this.ast.newPrimitiveType(PrimitiveType.SHORT));//$NON-NLS-1$
//		variableDeclarationFragment.setInitializer(castExpression);
//		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
//		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.SHORT));//$NON-NLS-1$
//
//		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(node, "short s = (short) d;", source); //$NON-NLS-1$
//	}
//
//	/**
//	 * (long) d; ==> ExpressionStatement(CastExpression)
//	 * @deprecated using deprecated code
//	 */
//	public void test0036() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0036", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//
//		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
//		variableDeclarationFragment.setName(this.ast.newSimpleName("l")); //$NON-NLS-1$
//		CastExpression castExpression = this.ast.newCastExpression();
//		castExpression.setExpression(this.ast.newSimpleName("d"));//$NON-NLS-1$
//		castExpression.setType(this.ast.newPrimitiveType(PrimitiveType.LONG));//$NON-NLS-1$
//		variableDeclarationFragment.setInitializer(castExpression);
//		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
//		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.LONG));//$NON-NLS-1$
//
//		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(node, "long l = (long) d;", source); //$NON-NLS-1$
//	}
//
//	/**
//	 * (char) i; ==> ExpressionStatement(CastExpression)
//	 * @deprecated using deprecated code
//	 */
//	public void test0037() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0037", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//
//		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
//		variableDeclarationFragment.setName(this.ast.newSimpleName("c")); //$NON-NLS-1$
//		CastExpression castExpression = this.ast.newCastExpression();
//		castExpression.setExpression(this.ast.newSimpleName("i"));//$NON-NLS-1$
//		castExpression.setType(this.ast.newPrimitiveType(PrimitiveType.CHAR));//$NON-NLS-1$
//		variableDeclarationFragment.setInitializer(castExpression);
//		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
//		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.CHAR));//$NON-NLS-1$
//
//		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(node, "char c = (char) i;", source); //$NON-NLS-1$
//	}	
//
//	/**
//	 * int.class; ==> ExpressionStatement(TypeLiteral)
//	 * @deprecated using deprecated code
//	 */
//	public void test0038() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0038", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//
//		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
//		variableDeclarationFragment.setName(this.ast.newSimpleName("c")); //$NON-NLS-1$
//		TypeLiteral typeLiteral = this.ast.newTypeLiteral();
//		typeLiteral.setType(this.ast.newPrimitiveType(PrimitiveType.INT));
//		variableDeclarationFragment.setInitializer(typeLiteral);
//		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
//		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newSimpleType(this.ast.newSimpleName("Class")));//$NON-NLS-1$
//
//		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(((VariableDeclarationFragment)((VariableDeclarationStatement)node).fragments().get(0)).getInitializer(), "int.class", source); //$NON-NLS-1$
//	}	
//
//	/**
//	 * void.class; ==> ExpressionStatement(TypeLiteral)
//	 * @deprecated using deprecated code
//	 */
//	public void test0039() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0039", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//
//		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
//		variableDeclarationFragment.setName(this.ast.newSimpleName("c")); //$NON-NLS-1$
//		TypeLiteral typeLiteral = this.ast.newTypeLiteral();
//		typeLiteral.setType(this.ast.newPrimitiveType(PrimitiveType.VOID));
//		variableDeclarationFragment.setInitializer(typeLiteral);
//		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
//		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newSimpleType(this.ast.newSimpleName("Class")));//$NON-NLS-1$
//
//		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(((VariableDeclarationFragment)((VariableDeclarationStatement)node).fragments().get(0)).getInitializer(), "void.class", source); //$NON-NLS-1$
//	}	
//
//	/**
//	 * double.class; ==> ExpressionStatement(TypeLiteral)
//	 * @deprecated using deprecated code
//	 */
//	public void test0040() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0040", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//
//		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
//		variableDeclarationFragment.setName(this.ast.newSimpleName("c")); //$NON-NLS-1$
//		TypeLiteral typeLiteral = this.ast.newTypeLiteral();
//		typeLiteral.setType(this.ast.newPrimitiveType(PrimitiveType.DOUBLE));
//		variableDeclarationFragment.setInitializer(typeLiteral);
//		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
//		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newSimpleType(this.ast.newSimpleName("Class")));//$NON-NLS-1$
//
//		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(((VariableDeclarationFragment)((VariableDeclarationStatement)node).fragments().get(0)).getInitializer(), "double.class", source); //$NON-NLS-1$
//	}	
//
//	/**
//	 * long.class; ==> ExpressionStatement(TypeLiteral)
//	 * @deprecated using deprecated code
//	 */
//	public void test0041() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0041", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//
//		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
//		variableDeclarationFragment.setName(this.ast.newSimpleName("c")); //$NON-NLS-1$
//		TypeLiteral typeLiteral = this.ast.newTypeLiteral();
//		typeLiteral.setType(this.ast.newPrimitiveType(PrimitiveType.LONG));
//		variableDeclarationFragment.setInitializer(typeLiteral);
//		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
//		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newSimpleType(this.ast.newSimpleName("Class")));//$NON-NLS-1$
//
//		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(((VariableDeclarationFragment)((VariableDeclarationStatement)node).fragments().get(0)).getInitializer(), "long.class", source); //$NON-NLS-1$
//	}	
		
	/**
	 * false ==> BooleanLiteral
	 */
	public void test0042() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0042", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		BooleanLiteral literal = this.ast.newBooleanLiteral(false);
		assertTrue("Both AST trees should be identical", literal.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "false", source); //$NON-NLS-1$
	}

	/**
	 * true ==> BooleanLiteral
	 */
	public void test0043() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0043", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		BooleanLiteral literal = this.ast.newBooleanLiteral(true);
		assertTrue("Both AST trees should be identical", literal.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "true", source); //$NON-NLS-1$
	}

	/**
	 * null ==> NullLiteral
	 */
	public void test0044() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0044", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		NullLiteral literal = this.ast.newNullLiteral();
		assertTrue("Both AST trees should be identical", literal.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "null", source); //$NON-NLS-1$
	}
		
	/**
	 * CharLiteral ==> CharacterLiteral
	 */
	public void Xtest0045() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0045", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		CharacterLiteral literal = this.ast.newCharacterLiteral();
		literal.setEscapedValue("'c'"); //$NON-NLS-1$
		assertTrue("Both AST trees should be identical", literal.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "'c'", source); //$NON-NLS-1$
	}

	/**
	 * DoubleLiteral ==> NumberLiteral
	 */
	public void test0046() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0046", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		NumberLiteral literal = this.ast.newNumberLiteral("1.00001");//$NON-NLS-1$
		assertTrue("Both AST trees should be identical", literal.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "1.00001", source); //$NON-NLS-1$
	}

	/**
	 * FloatLiteral ==> NumberLiteral
	 */
	public void test0047() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0047", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		NumberLiteral literal = this.ast.newNumberLiteral("1.00001f");//$NON-NLS-1$
		assertTrue("Both AST trees should be identical", literal.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "1.00001f", source); //$NON-NLS-1$
	}

	/**
	 * IntLiteral ==> NumberLiteral
	 */
	public void test0048() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0048", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		NumberLiteral literal = this.ast.newNumberLiteral("30000");//$NON-NLS-1$
		assertTrue("Both AST trees should be identical", literal.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "30000", source); //$NON-NLS-1$
	}

	/**
	 * IntLiteralMinValue ==> NumberLiteral
	 */
	public void test0049() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0049", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		NumberLiteral literal = this.ast.newNumberLiteral("-2147483648");//$NON-NLS-1$
		assertTrue("Both AST trees should be identical", literal.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "-2147483648", source); //$NON-NLS-1$
	}

	/**
	 * LongLiteral ==> NumberLiteral
	 */
	public void test0050() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0050", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		NumberLiteral literal = this.ast.newNumberLiteral("2147483648L");//$NON-NLS-1$
		assertTrue("Both AST trees should be identical", literal.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "2147483648L", source); //$NON-NLS-1$
	}

	/**
	 * LongLiteral ==> NumberLiteral (negative value)
	 */
	public void test0051() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0051", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		NumberLiteral literal = this.ast.newNumberLiteral("2147483648L");//$NON-NLS-1$
		PrefixExpression prefixExpression = this.ast.newPrefixExpression();
		prefixExpression.setOperand(literal);
		prefixExpression.setOperator(PrefixExpression.Operator.MINUS);
		assertTrue("Both AST trees should be identical", prefixExpression.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "-2147483648L", source); //$NON-NLS-1$
	}

	/**
	 * LongLiteralMinValue ==> NumberLiteral
	 */
	public void Xtest0052() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0052", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		NumberLiteral literal = this.ast.newNumberLiteral("-9223372036854775808L");//$NON-NLS-1$
		assertTrue("Both AST trees should be identical", literal.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "-9223372036854775808L", source); //$NON-NLS-1$
	}

	/**
	 * ExtendedStringLiteral ==> StringLiteral
	 */
	public void test0053() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0053", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		/*
		StringLiteral literal = this.ast.newStringLiteral();//$NON-NLS-1$
		literal.setLiteralValue("Hello World");*/
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setOperator(InfixExpression.Operator.PLUS);
		StringLiteral literal = this.ast.newStringLiteral();
		literal.setLiteralValue("Hello");//$NON-NLS-1$
		infixExpression.setLeftOperand(literal);
		literal = this.ast.newStringLiteral();
		literal.setLiteralValue(" World");//$NON-NLS-1$
		infixExpression.setRightOperand(literal);//$NON-NLS-1$
		
		assertTrue("Both AST trees should be identical", infixExpression.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "\"Hello\" + \" World\"", source); //$NON-NLS-1$
	}

	/**
	 * AND_AND_Expression ==> InfixExpression
	 * @deprecated using deprecated code
	 */
	public void test0054() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0054", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 2);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		
		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("b3")); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("b")); //$NON-NLS-1$
		infixExpression.setRightOperand(this.ast.newSimpleName("b2")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.CONDITIONAL_AND);
		variableDeclarationFragment.setInitializer(infixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.BOOLEAN));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var b3 = b && b2;", source); //$NON-NLS-1$
	}	

	/**
	 * OR_OR_Expression ==> InfixExpression
	 * @deprecated using deprecated code
	 */
	public void test0055() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0055", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 2);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("b3")); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("b")); //$NON-NLS-1$
		infixExpression.setRightOperand(this.ast.newSimpleName("b2")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.CONDITIONAL_OR);
		variableDeclarationFragment.setInitializer(infixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.BOOLEAN));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var b3 = b || b2;", source); //$NON-NLS-1$
	}	

	/**
	 * EqualExpression ==> InfixExpression
	 * @deprecated using deprecated code
	 */
	public void test0056() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0056", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 2);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("b3")); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("b")); //$NON-NLS-1$
		infixExpression.setRightOperand(this.ast.newSimpleName("b2")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.EQUALS);
		variableDeclarationFragment.setInitializer(infixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.BOOLEAN));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var b3 = b == b2;", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression (+) ==> InfixExpression
	 * @deprecated using deprecated code
	 */
	public void test0057() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0057", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 2);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("n")); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("i")); //$NON-NLS-1$
		infixExpression.setRightOperand(this.ast.newSimpleName("j")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.PLUS);
		variableDeclarationFragment.setInitializer(infixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.INT));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var n = i + j;", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression (-) ==> InfixExpression
	 * @deprecated using deprecated code
	 */
	public void test0058() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0058", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 2);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("n")); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("i")); //$NON-NLS-1$
		infixExpression.setRightOperand(this.ast.newSimpleName("j")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.MINUS);
		variableDeclarationFragment.setInitializer(infixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.INT));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var n = i - j;", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression (*) ==> InfixExpression
	 * @deprecated using deprecated code
	 */
	public void test0059() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0059", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 2);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("n")); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("i")); //$NON-NLS-1$
		infixExpression.setRightOperand(this.ast.newSimpleName("j")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.TIMES);
		variableDeclarationFragment.setInitializer(infixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.INT));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var n = i * j;", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression (/) ==> InfixExpression
	 * @deprecated using deprecated code
	 */
	public void test0060() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0060", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 2);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("n")); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("i")); //$NON-NLS-1$
		infixExpression.setRightOperand(this.ast.newSimpleName("j")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.DIVIDE);
		variableDeclarationFragment.setInitializer(infixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.INT));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var n = i / j;", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression (%) ==> InfixExpression
	 * @deprecated using deprecated code
	 */
	public void test0061() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0061", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 2);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("n")); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("i")); //$NON-NLS-1$
		infixExpression.setRightOperand(this.ast.newSimpleName("j")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.REMAINDER);
		variableDeclarationFragment.setInitializer(infixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.INT));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var n = i % j;", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression (^) ==> InfixExpression
	 * @deprecated using deprecated code
	 */
	public void test0062() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0062", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 2);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("n")); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("i")); //$NON-NLS-1$
		infixExpression.setRightOperand(this.ast.newSimpleName("j")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.XOR);
		variableDeclarationFragment.setInitializer(infixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.INT));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var n = i ^ j;", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression (&) ==> InfixExpression
	 * @deprecated using deprecated code
	 */
	public void test0063() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0063", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 2);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("n")); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("i")); //$NON-NLS-1$
		infixExpression.setRightOperand(this.ast.newSimpleName("j")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.AND);
		variableDeclarationFragment.setInitializer(infixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.INT));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var n = i & j;", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression (|) ==> InfixExpression
	 * @deprecated using deprecated code
	 */
	public void test0064() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0064", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 2);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("n")); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("i")); //$NON-NLS-1$
		infixExpression.setRightOperand(this.ast.newSimpleName("j")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.OR);
		variableDeclarationFragment.setInitializer(infixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.INT));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var n = i | j;", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression (<) ==> InfixExpression
	 * @deprecated using deprecated code
	 */
	public void test0065() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0065", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 2);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("b2")); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("b")); //$NON-NLS-1$
		infixExpression.setRightOperand(this.ast.newSimpleName("b1")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.LESS);
		variableDeclarationFragment.setInitializer(infixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.BOOLEAN));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var b2 = b < b1;", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression (<=) ==> InfixExpression
	 * @deprecated using deprecated code
	 */
	public void test0066() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0066", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 2);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("b2")); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("b")); //$NON-NLS-1$
		infixExpression.setRightOperand(this.ast.newSimpleName("b1")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.LESS_EQUALS);
		variableDeclarationFragment.setInitializer(infixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.BOOLEAN));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var b2 = b <= b1;", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression (>) ==> InfixExpression
	 * @deprecated using deprecated code
	 */
	public void test0067() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0067", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 2);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("b2")); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("b")); //$NON-NLS-1$
		infixExpression.setRightOperand(this.ast.newSimpleName("b1")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.GREATER);
		variableDeclarationFragment.setInitializer(infixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.BOOLEAN));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var b2 = b > b1;", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression (>=) ==> InfixExpression
	 * @deprecated using deprecated code
	 */
	public void test0068() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0068", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 2);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("b2")); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("b")); //$NON-NLS-1$
		infixExpression.setRightOperand(this.ast.newSimpleName("b1")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.GREATER_EQUALS);
		variableDeclarationFragment.setInitializer(infixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.BOOLEAN));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var b2 = b >= b1;", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression (!=) ==> InfixExpression
	 * @deprecated using deprecated code
	 */
	public void test0069() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0069", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 2);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("b2")); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("b")); //$NON-NLS-1$
		infixExpression.setRightOperand(this.ast.newSimpleName("b1")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.NOT_EQUALS);
		variableDeclarationFragment.setInitializer(infixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.BOOLEAN));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var b2 = b != b1;", source); //$NON-NLS-1$
	}	

	/**
	 * InstanceofExpression ==> InfixExpression
	 * @deprecated using deprecated code
	 */
	public void test0070() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0070", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("b")); //$NON-NLS-1$
		InfixExpression instanceOfExpression = this.ast.newInfixExpression();
		instanceOfExpression.setLeftOperand(this.ast.newSimpleName("o"));//$NON-NLS-1$ 
		instanceOfExpression.setOperator(InfixExpression.Operator.INSTANCEOF); 
//		SimpleType simpleType = this.ast.newSimpleType(this.ast.newSimpleName("Integer"));//$NON-NLS-1$
		instanceOfExpression.setRightOperand(this.ast.newSimpleName("Number")); 
		variableDeclarationFragment.setInitializer(instanceOfExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.BOOLEAN));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var b = o instanceof Number;", source); //$NON-NLS-1$
	}	

//	/**
//	 * InstanceofExpression ==> InfixExpression
//	 * @deprecated using deprecated code
//	 */
//	public void test0071() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0071", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//
//		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
//		variableDeclarationFragment.setName(this.ast.newSimpleName("b")); //$NON-NLS-1$
//		InstanceofExpression instanceOfExpression = this.ast.newInstanceofExpression();
//		instanceOfExpression.setLeftOperand(this.ast.newSimpleName("o")); //$NON-NLS-1$
//		QualifiedName name =
//			this.ast.newQualifiedName(
//				this.ast.newQualifiedName(
//					this.ast.newSimpleName("java"), //$NON-NLS-1$
//					this.ast.newSimpleName("lang")), //$NON-NLS-1$
//				this.ast.newSimpleName("Integer")); //$NON-NLS-1$
//		Type type = ast.newSimpleType(name);
//		instanceOfExpression.setRightOperand(type);
//		variableDeclarationFragment.setInitializer(instanceOfExpression);
//		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
//		statement.setModifiers(Modifier.NONE);
////		statement.setType(this.ast.newPrimitiveType(PrimitiveType.BOOLEAN));
//
//		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(node, "var b = o instanceof java.lang.Integer;", source); //$NON-NLS-1$
//	}	

	/**
	 * UnaryExpression (!) ==> PrefixExpression
	 * @deprecated using deprecated code
	 */
	public void test0072() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0072", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("b1")); //$NON-NLS-1$
		PrefixExpression prefixExpression = this.ast.newPrefixExpression();
		prefixExpression.setOperator(PrefixExpression.Operator.NOT);
		prefixExpression.setOperand(this.ast.newSimpleName("b"));//$NON-NLS-1$
		variableDeclarationFragment.setInitializer(prefixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.BOOLEAN));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var b1 = !b;", source); //$NON-NLS-1$
	}	

	/**
	 * UnaryExpression (~) ==> PrefixExpression
	 * @deprecated using deprecated code
	 */
	public void test0073() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0073", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("n")); //$NON-NLS-1$
		PrefixExpression prefixExpression = this.ast.newPrefixExpression();
		prefixExpression.setOperator(PrefixExpression.Operator.COMPLEMENT);
		prefixExpression.setOperand(this.ast.newSimpleName("i"));//$NON-NLS-1$
		variableDeclarationFragment.setInitializer(prefixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.INT));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var n = ~i;", source); //$NON-NLS-1$
	}	

	/**
	 * UnaryExpression (+) ==> PrefixExpression
	 * @deprecated using deprecated code
	 */
	public void test0074() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0074", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("i")); //$NON-NLS-1$
		PrefixExpression prefixExpression = this.ast.newPrefixExpression();
		prefixExpression.setOperator(PrefixExpression.Operator.PLUS);
		prefixExpression.setOperand(this.ast.newNumberLiteral("2"));//$NON-NLS-1$
		variableDeclarationFragment.setInitializer(prefixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.INT));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var i = +2;", source); //$NON-NLS-1$
	}	

	/**
	 * UnaryExpression (-) ==> PrefixExpression
	 * @deprecated using deprecated code
	 */
	public void test0075() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0075", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("i")); //$NON-NLS-1$
		PrefixExpression prefixExpression = this.ast.newPrefixExpression();
		prefixExpression.setOperator(PrefixExpression.Operator.MINUS);
		prefixExpression.setOperand(this.ast.newNumberLiteral("2"));//$NON-NLS-1$
		variableDeclarationFragment.setInitializer(prefixExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.INT));


		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var i = -2;", source); //$NON-NLS-1$
	}	

	/**
	 * ConditionalExpression ==> ConditionalExpression
	 * @deprecated using deprecated code
	 */
	public void test0076() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0076", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("b")); //$NON-NLS-1$
		ConditionalExpression conditionalExpression = this.ast.newConditionalExpression();
		InfixExpression condition = this.ast.newInfixExpression();
		condition.setLeftOperand(this.ast.newSimpleName("args")); //$NON-NLS-1$
		condition.setRightOperand(this.ast.newNullLiteral()); //$NON-NLS-1$
		condition.setOperator(InfixExpression.Operator.NOT_EQUALS);
		conditionalExpression.setExpression(condition);
		conditionalExpression.setThenExpression(this.ast.newBooleanLiteral(true));
		conditionalExpression.setElseExpression(this.ast.newBooleanLiteral(false));
		variableDeclarationFragment.setInitializer(conditionalExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.BOOLEAN));
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var b = args != null ? true : false;", source); //$NON-NLS-1$
	}	

	/**
	 * ConditionalExpression ==> ConditionalExpression
	 * @deprecated using deprecated code
	 */
	public void test0077() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0077", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("i")); //$NON-NLS-1$
		ConditionalExpression conditionalExpression = this.ast.newConditionalExpression();
		conditionalExpression.setExpression(this.ast.newBooleanLiteral(true));
//		QualifiedName name = 
//			this.ast.newQualifiedName(
//				this.ast.newSimpleName("args"), //$NON-NLS-1$
//				this.ast.newSimpleName("length")); //$NON-NLS-1$
//		conditionalExpression.setThenExpression(name);
		FieldAccess fieldAccess=this.ast.newFieldAccess();
		fieldAccess.setExpression(this.ast.newSimpleName("args"));
		fieldAccess.setName(this.ast.newSimpleName("length"));
		conditionalExpression.setThenExpression(fieldAccess);
		conditionalExpression.setElseExpression(this.ast.newNumberLiteral("0"));//$NON-NLS-1$
		variableDeclarationFragment.setInitializer(conditionalExpression);
		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
		statement.setModifiers(Modifier.NONE);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.INT));

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var i = true ? args.length: 0;", source); //$NON-NLS-1$
	}	
//
//	/**
//	 * MessageSend ==> SuperMethodInvocation
//	 */
//	public void test0078() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0078", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		SuperMethodInvocation superMethodInvocation = this.ast.newSuperMethodInvocation();
//		superMethodInvocation.setName(this.ast.newSimpleName("bar")); //$NON-NLS-1$
//		ExpressionStatement statement = this.ast.newExpressionStatement(superMethodInvocation);
//		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(node, "super.bar();", source); //$NON-NLS-1$
//	}	
//
//	/**
//	 * MessageSend ==> SuperMethodInvocation
//	 */
//	public void test0079() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0079", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		SuperMethodInvocation superMethodInvocation = this.ast.newSuperMethodInvocation();
//		superMethodInvocation.setName(this.ast.newSimpleName("bar")); //$NON-NLS-1$
//		superMethodInvocation.arguments().add(this.ast.newNumberLiteral("4"));//$NON-NLS-1$
//		ExpressionStatement statement = this.ast.newExpressionStatement(superMethodInvocation);
//		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(node, "super.bar(4);", source); //$NON-NLS-1$
//	}	
	
	/**
	 * MessageSend ==> FunctionInvocation
	 */
	public void test0080() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0080", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		FunctionInvocation methodInvocation = this.ast.newFunctionInvocation();
		methodInvocation.setName(this.ast.newSimpleName("bar")); //$NON-NLS-1$
		methodInvocation.arguments().add(this.ast.newNumberLiteral("4"));//$NON-NLS-1$
		ExpressionStatement statement = this.ast.newExpressionStatement(methodInvocation);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "bar(4);", source); //$NON-NLS-1$
	}
	
	/**
	 * MessageSend ==> FunctionInvocation
	 */
	public void test0081() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0081", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		FunctionInvocation methodInvocation = this.ast.newFunctionInvocation();
		methodInvocation.setName(this.ast.newSimpleName("bar")); //$NON-NLS-1$
		methodInvocation.setExpression(this.ast.newThisExpression());
		methodInvocation.arguments().add(this.ast.newNumberLiteral("4"));//$NON-NLS-1$
		ExpressionStatement statement = this.ast.newExpressionStatement(methodInvocation);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "this.bar(4);", source); //$NON-NLS-1$
	}
	
	/**
	 * ForStatement ==> ForStatement
	 */
	public void test0082() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0082", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		ForStatement forStatement = this.ast.newForStatement();
		forStatement.setBody(this.ast.newEmptyStatement());
		assertTrue("Both AST trees should be identical", forStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "for (;;);", source); //$NON-NLS-1$
	}
	
	/**
	 * ForStatement ==> ForStatement
	 * @deprecated using deprecated code
	 */
	public void test0083() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0083", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		ForStatement forStatement = this.ast.newForStatement();
		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("i")); //$NON-NLS-1$
		variableDeclarationFragment.setInitializer(this.ast.newNumberLiteral("0"));//$NON-NLS-1$
		VariableDeclarationExpression variableDeclarationExpression = this.ast.newVariableDeclarationExpression(variableDeclarationFragment);
		variableDeclarationExpression.setModifiers(Modifier.NONE);
//		variableDeclarationExpression.setType(this.ast.newPrimitiveType(PrimitiveType.INT));
		forStatement.initializers().add(variableDeclarationExpression);
		PostfixExpression postfixExpression = this.ast.newPostfixExpression();
		postfixExpression.setOperand(this.ast.newSimpleName("i"));//$NON-NLS-1$
		postfixExpression.setOperator(PostfixExpression.Operator.INCREMENT);
		forStatement.updaters().add(postfixExpression);
		forStatement.setBody(this.ast.newBlock());
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("i")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.LESS);
		infixExpression.setRightOperand(this.ast.newNumberLiteral("10")); //$NON-NLS-1$
		forStatement.setExpression(infixExpression);
		assertTrue("Both AST trees should be identical", forStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "for (var i = 0; i < 10; i++) {}", source); //$NON-NLS-1$
	}
	
	/**
	 * ForStatement ==> ForStatement
	 * @deprecated using deprecated code
	 */
	public void test0084() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0084", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		ForStatement forStatement = this.ast.newForStatement();
		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("i")); //$NON-NLS-1$
		variableDeclarationFragment.setInitializer(this.ast.newNumberLiteral("0"));//$NON-NLS-1$

		VariableDeclarationExpression variableDeclarationExpression = this.ast.newVariableDeclarationExpression(variableDeclarationFragment);
		variableDeclarationExpression.setModifiers(Modifier.NONE);
//		variableDeclarationExpression.setType(this.ast.newPrimitiveType(PrimitiveType.INT));
		
		forStatement.initializers().add(variableDeclarationExpression);
		PostfixExpression postfixExpression = this.ast.newPostfixExpression();
		postfixExpression.setOperand(this.ast.newSimpleName("i"));//$NON-NLS-1$
		postfixExpression.setOperator(PostfixExpression.Operator.INCREMENT);
		forStatement.updaters().add(postfixExpression);
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("i")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.LESS);
		infixExpression.setRightOperand(this.ast.newNumberLiteral("10")); //$NON-NLS-1$
		forStatement.setExpression(infixExpression);
		forStatement.setBody(this.ast.newEmptyStatement());
		assertTrue("Both AST trees should be identical", forStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "for (var i = 0; i < 10; i++);", source); //$NON-NLS-1$
	}

	/**
	 * ForStatement ==> ForStatement
	 * @deprecated using deprecated code
	 */
	public void test0085() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0085", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		ForStatement forStatement = this.ast.newForStatement();
		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("i")); //$NON-NLS-1$
		variableDeclarationFragment.setInitializer(this.ast.newNumberLiteral("0"));//$NON-NLS-1$

		VariableDeclarationExpression variableDeclarationExpression = this.ast.newVariableDeclarationExpression(variableDeclarationFragment);
		variableDeclarationExpression.setModifiers(Modifier.NONE);
//		variableDeclarationExpression.setType(this.ast.newPrimitiveType(PrimitiveType.INT));
		
		forStatement.initializers().add(variableDeclarationExpression);
		PostfixExpression postfixExpression = this.ast.newPostfixExpression();
		postfixExpression.setOperand(this.ast.newSimpleName("i"));//$NON-NLS-1$
		postfixExpression.setOperator(PostfixExpression.Operator.INCREMENT);
		forStatement.updaters().add(postfixExpression);
		forStatement.setBody(this.ast.newEmptyStatement());
		assertTrue("Both AST trees should be identical", forStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "for (var i = 0;; i++);", source); //$NON-NLS-1$
	}

	/**
	 * ForStatement ==> ForStatement
	 */
	public void test0086() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0086", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		ForStatement forStatement = this.ast.newForStatement();
		PostfixExpression postfixExpression = this.ast.newPostfixExpression();
		postfixExpression.setOperand(this.ast.newSimpleName("i"));//$NON-NLS-1$
		postfixExpression.setOperator(PostfixExpression.Operator.INCREMENT);
		forStatement.updaters().add(postfixExpression);
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("i")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.LESS);
		infixExpression.setRightOperand(this.ast.newNumberLiteral("10")); //$NON-NLS-1$
		forStatement.setExpression(infixExpression);
		forStatement.setBody(this.ast.newEmptyStatement());
		assertTrue("Both AST trees should be identical", forStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "for (; i < 10; i++);", source); //$NON-NLS-1$
	}

	/**
	 * ForStatement ==> ForStatement
	 */
	public void test0087() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0087", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		ForStatement forStatement = this.ast.newForStatement();
		PostfixExpression postfixExpression = this.ast.newPostfixExpression();
		postfixExpression.setOperand(this.ast.newSimpleName("i"));//$NON-NLS-1$
		postfixExpression.setOperator(PostfixExpression.Operator.INCREMENT);
		forStatement.updaters().add(postfixExpression);
		forStatement.setBody(this.ast.newEmptyStatement());
		assertTrue("Both AST trees should be identical", forStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "for (;;i++);", source); //$NON-NLS-1$
	}

	/**
	 * LocalDeclaration ==> VariableDeclarationStatement
	 * @deprecated using deprecated code
	 */
	public void test0088() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0088", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$

		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("i")); //$NON-NLS-1$

		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.INT));
		statement.setModifiers(Modifier.NONE);

		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var i;", source); //$NON-NLS-1$
	}

//	/**
//	 * LocalDeclaration ==> VariableDeclarationStatement
//	 * @deprecated using deprecated code
//	 */
//	public void test0089() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0089", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//
//		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
//		variableDeclarationFragment.setName(this.ast.newSimpleName("s")); //$NON-NLS-1$
//
//		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
//		QualifiedName name = 
//			this.ast.newQualifiedName(
//				this.ast.newQualifiedName(
//					this.ast.newSimpleName("java"),//$NON-NLS-1$
//					this.ast.newSimpleName("lang")//$NON-NLS-1$
//				),
//				this.ast.newSimpleName("String") //$NON-NLS-1$
//			);
//		statement.setType(this.ast.newSimpleType(name));
//		statement.setModifiers(Modifier.NONE);
//
//		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(node, "java.lang.String s;", source); //$NON-NLS-1$
//	}

	/**
	 * LocalDeclaration ==> VariableDeclarationStatement
	 * @deprecated using deprecated code
	 */
	public void test0090() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0090", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
	
		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		ArrayInitializer initializer = this.ast.newArrayInitializer();
		initializer.expressions().add(this.ast.newNumberLiteral("1"));//$NON-NLS-1$
		initializer.expressions().add(this.ast.newNumberLiteral("2"));//$NON-NLS-1$
		variableDeclarationFragment.setInitializer(initializer);
		variableDeclarationFragment.setName(this.ast.newSimpleName("tab")); //$NON-NLS-1$

		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(variableDeclarationFragment);
//		statement.setType(this.ast.newArrayType(this.ast.newPrimitiveType(PrimitiveType.INT), 1));
		statement.setModifiers(Modifier.NONE);
		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var tab = [1, 2];", source); //$NON-NLS-1$
	}
	
	/**
	 * Argument ==> SingleVariableDeclaration
	 * @deprecated using deprecated code
	 */
	public void test0091() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0091", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		FunctionDeclaration method = (FunctionDeclaration) ((JavaScriptUnit) result).statements().get(0);
		SingleVariableDeclaration node = (SingleVariableDeclaration) method.parameters().get(0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		SingleVariableDeclaration variableDeclaration = this.ast.newSingleVariableDeclaration();
		variableDeclaration.setModifiers(Modifier.NONE);
//		variableDeclaration.setType(this.ast.newSimpleType(this.ast.newSimpleName("String")));//$NON-NLS-1$
		variableDeclaration.setName(this.ast.newSimpleName("s")); //$NON-NLS-1$
		assertTrue("Both AST trees should be identical", variableDeclaration.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "s", source); //$NON-NLS-1$
	}

	/**
	 * Argument ==> SingleVariableDeclaration
	 * @deprecated using deprecated code
	 */
//	public void test0092() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0092", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		FunctionDeclaration method = (FunctionDeclaration)((TypeDeclaration) ((JavaScriptUnit) result).types().get(0)).bodyDeclarations().get(0);
//		SingleVariableDeclaration node = (SingleVariableDeclaration) method.parameters().get(0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		SingleVariableDeclaration variableDeclaration = this.ast.newSingleVariableDeclaration();
//		variableDeclaration.setModifiers(Modifier.FINAL);
//		variableDeclaration.setType(this.ast.newSimpleType(this.ast.newSimpleName("String")));//$NON-NLS-1$
//		variableDeclaration.setName(this.ast.newSimpleName("s")); //$NON-NLS-1$
//		assertTrue("Both AST trees should be identical", variableDeclaration.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(node, "final String s", source); //$NON-NLS-1$
//		assertEquals("Wrong dimension", 0, node.getExtraDimensions()); //$NON-NLS-1$
//	}

	/**
	 * Break ==> BreakStatement
	 */
	public void test0093() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0093", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		ForStatement forStatement = (ForStatement) node;
		BreakStatement statement = (BreakStatement) ((Block) forStatement.getBody()).statements().get(0);
		assertNotNull("Expression should not be null", statement); //$NON-NLS-1$
		BreakStatement breakStatement = this.ast.newBreakStatement();
		assertTrue("Both AST trees should be identical", breakStatement.subtreeMatch(new ASTMatcher(), statement));		//$NON-NLS-1$
		checkSourceRange(statement, "break;", source); //$NON-NLS-1$
	}

	/**
	 * Continue ==> ContinueStatement
	 */
	public void test0094() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0094", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		ForStatement forStatement = (ForStatement) node;
		ContinueStatement statement = (ContinueStatement) ((Block) forStatement.getBody()).statements().get(0);
		assertNotNull("Expression should not be null", statement); //$NON-NLS-1$
		ContinueStatement continueStatement = this.ast.newContinueStatement();
		assertTrue("Both AST trees should be identical", continueStatement.subtreeMatch(new ASTMatcher(), statement));		//$NON-NLS-1$
		checkSourceRange(statement, "continue;", source); //$NON-NLS-1$
	}
	
	/**
	 * Continue with Label ==> ContinueStatement
	 */
	public void test0095() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0095", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		LabeledStatement labeledStatement = (LabeledStatement) getASTNode((JavaScriptUnit) result, 0, 0, 0);
		ForStatement forStatement = (ForStatement) labeledStatement.getBody();
		ContinueStatement statement = (ContinueStatement) ((Block) forStatement.getBody()).statements().get(0);
		assertNotNull("Expression should not be null", statement); //$NON-NLS-1$
		ContinueStatement continueStatement = this.ast.newContinueStatement();
		continueStatement.setLabel(this.ast.newSimpleName("label")); //$NON-NLS-1$
		assertTrue("Both AST trees should be identical", continueStatement.subtreeMatch(new ASTMatcher(), statement));		//$NON-NLS-1$
		checkSourceRange(statement, "continue label;", source); //$NON-NLS-1$
	}
	
	/**
	 * Break + label  ==> BreakStatement
	 */
	public void test0096() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0096", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		LabeledStatement labeledStatement = (LabeledStatement) getASTNode((JavaScriptUnit) result, 0, 0, 0);
		ForStatement forStatement = (ForStatement) labeledStatement.getBody();
		BreakStatement statement = (BreakStatement) ((Block) forStatement.getBody()).statements().get(0);
		assertNotNull("Expression should not be null", statement); //$NON-NLS-1$
		BreakStatement breakStatement = this.ast.newBreakStatement();
		breakStatement.setLabel(this.ast.newSimpleName("label")); //$NON-NLS-1$
		assertTrue("Both AST trees should be identical", breakStatement.subtreeMatch(new ASTMatcher(), statement));		//$NON-NLS-1$
		checkSourceRange(statement, "break label;", source); //$NON-NLS-1$
	}

	/**
	 * SwitchStatement ==> SwitchStatement
	 */
	public void test0097() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0097", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		SwitchStatement switchStatement = this.ast.newSwitchStatement();
		switchStatement.setExpression(this.ast.newSimpleName("i"));//$NON-NLS-1$
		SwitchCase _case = this.ast.newSwitchCase();
		_case.setExpression(this.ast.newNumberLiteral("1"));//$NON-NLS-1$
		switchStatement.statements().add(_case);
		switchStatement.statements().add(this.ast.newBreakStatement());
		_case = this.ast.newSwitchCase();
		_case.setExpression(this.ast.newNumberLiteral("2"));//$NON-NLS-1$
		switchStatement.statements().add(_case);
		FunctionInvocation methodInvocation = this.ast.newFunctionInvocation();
//		QualifiedName name = 
//			this.ast.newQualifiedName(
//				this.ast.newSimpleName("System"),//$NON-NLS-1$
//				this.ast.newSimpleName("out"));//$NON-NLS-1$
//		methodInvocation.setExpression(name);
		FieldAccess fieldAccess = this.ast.newFieldAccess();
		fieldAccess.setExpression(this.ast.newSimpleName("System"));
		fieldAccess.setName(this.ast.newSimpleName("out"));
		methodInvocation.setExpression(fieldAccess);
		methodInvocation.setName(this.ast.newSimpleName("println")); //$NON-NLS-1$
		methodInvocation.arguments().add(this.ast.newNumberLiteral("2"));//$NON-NLS-1$
		ExpressionStatement expressionStatement = this.ast.newExpressionStatement(methodInvocation);
		switchStatement.statements().add(expressionStatement);
		switchStatement.statements().add(this.ast.newBreakStatement());
		_case = this.ast.newSwitchCase();
		_case.setExpression(null);
		switchStatement.statements().add(_case);
		methodInvocation = this.ast.newFunctionInvocation();
//		name = 
//			this.ast.newQualifiedName(
//				this.ast.newSimpleName("System"),//$NON-NLS-1$
//				this.ast.newSimpleName("out"));//$NON-NLS-1$
//		methodInvocation.setExpression(name);
		fieldAccess = this.ast.newFieldAccess();
		fieldAccess.setExpression(this.ast.newSimpleName("System"));
		fieldAccess.setName(this.ast.newSimpleName("out"));
		methodInvocation.setExpression(fieldAccess);
		methodInvocation.setName(this.ast.newSimpleName("println")); //$NON-NLS-1$
		StringLiteral literal = this.ast.newStringLiteral();
		literal.setLiteralValue("default");	//$NON-NLS-1$
		methodInvocation.arguments().add(literal);
		expressionStatement = this.ast.newExpressionStatement(methodInvocation);
		switchStatement.statements().add(expressionStatement);
		assertTrue("Both AST trees should be identical", switchStatement.subtreeMatch(new ASTMatcher(), node));	//$NON-NLS-1$
		String expectedSource = "switch(i) {\n" +//$NON-NLS-1$
			 "			case 1: \n" +//$NON-NLS-1$
			 "              break;\n" +//$NON-NLS-1$
			 "			case 2:\n" +//$NON-NLS-1$
			 "				System.out.println(2);\n" +//$NON-NLS-1$
			 "              break;\n" +//$NON-NLS-1$
			 "          default:\n" +//$NON-NLS-1$
			 "				System.out.println(\"default\");\n" +//$NON-NLS-1$
			 "		}"; //$NON-NLS-1$
		checkSourceRange(node, expectedSource, source);
		SwitchStatement switchStatement2 = (SwitchStatement) node;
		List statements = switchStatement2.statements();
		assertEquals("wrong size", 7, statements.size()); //$NON-NLS-1$
		Statement stmt = (Statement) statements.get(5);
		assertTrue("Not a case statement", stmt instanceof SwitchCase); //$NON-NLS-1$
		SwitchCase switchCase = (SwitchCase) stmt;
		assertTrue("Not the default case", switchCase.isDefault()); //$NON-NLS-1$
	}

	/**
	 * EmptyStatement ==> EmptyStatement
	 */
	public void test0098() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0098", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		EmptyStatement emptyStatement = this.ast.newEmptyStatement();
		assertTrue("Both AST trees should be identical", emptyStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, ";", source); //$NON-NLS-1$
	}

	/**
	 * DoStatement ==> DoStatement
	 */
	public void test0099() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0099", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		DoStatement doStatement = this.ast.newDoStatement();
		Block block = this.ast.newBlock();
		block.statements().add(this.ast.newEmptyStatement());
		doStatement.setBody(block);
		doStatement.setExpression(this.ast.newBooleanLiteral(true));
		assertTrue("Both AST trees should be identical", doStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		String expectedSource = "do {;\n" +//$NON-NLS-1$
			 "		} while(true);";//$NON-NLS-1$
		checkSourceRange(node, expectedSource, source);
	}

	/**
	 * WhileStatement ==> WhileStatement
	 */
	public void test0100() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0100", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		WhileStatement whileStatement = this.ast.newWhileStatement();
		whileStatement.setExpression(this.ast.newBooleanLiteral(true));
		whileStatement.setBody(this.ast.newEmptyStatement());
		assertTrue("Both AST trees should be identical", whileStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "while(true);", source);//$NON-NLS-1$
	}

	/**
	 * WhileStatement ==> WhileStatement
	 */
	public void test0101() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0101", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		WhileStatement whileStatement = this.ast.newWhileStatement();
		whileStatement.setExpression(this.ast.newBooleanLiteral(true));
		whileStatement.setBody(this.ast.newBlock());
		assertTrue("Both AST trees should be identical", whileStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "while(true) {}", source);//$NON-NLS-1$
	}
	
	/**
	 * ExtendedStringLiteral ==> StringLiteral
	 */
	public void test0102() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0102", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setOperator(InfixExpression.Operator.PLUS);
		StringLiteral literal = this.ast.newStringLiteral();//$NON-NLS-1$
		literal.setLiteralValue("Hello"); //$NON-NLS-1$
		infixExpression.setLeftOperand(literal);
		literal = this.ast.newStringLiteral();//$NON-NLS-1$
		literal.setLiteralValue(" World"); //$NON-NLS-1$
		infixExpression.setRightOperand(literal);		
		literal = this.ast.newStringLiteral();//$NON-NLS-1$
		literal.setLiteralValue("!"); //$NON-NLS-1$
		infixExpression.extendedOperands().add(literal);
		assertTrue("Both AST trees should be identical", infixExpression.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "\"Hello\" + \" World\" + \"!\"", source);//$NON-NLS-1$
	}
	
	/**
	 * ExtendedStringLiteral ==> StringLiteral
	 */
	public void test0103() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0103", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setOperator(InfixExpression.Operator.PLUS);
		StringLiteral literal = this.ast.newStringLiteral();//$NON-NLS-1$
		literal.setLiteralValue("Hello"); //$NON-NLS-1$
		infixExpression.setLeftOperand(literal);
		literal = this.ast.newStringLiteral();//$NON-NLS-1$
		literal.setLiteralValue(" World"); //$NON-NLS-1$
		infixExpression.setRightOperand(literal);		
		literal = this.ast.newStringLiteral();//$NON-NLS-1$
		literal.setLiteralValue("!"); //$NON-NLS-1$
		infixExpression.extendedOperands().add(literal);
		literal = this.ast.newStringLiteral();//$NON-NLS-1$
		literal.setLiteralValue("!"); //$NON-NLS-1$
		infixExpression.extendedOperands().add(literal);
		assertTrue("Both AST trees should be identical", infixExpression.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "\"Hello\" + \" World\" + \"!\" + \"!\"", source);//$NON-NLS-1$
	}

	/**
	 * ExtendedStringLiteral ==> StringLiteral
	 */
	public void test0104() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0104", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setOperator(InfixExpression.Operator.PLUS);
		StringLiteral literal = this.ast.newStringLiteral();//$NON-NLS-1$
		literal.setLiteralValue("Hello"); //$NON-NLS-1$
		infixExpression.setLeftOperand(literal);
		literal = this.ast.newStringLiteral();//$NON-NLS-1$
		literal.setLiteralValue(" World"); //$NON-NLS-1$
		infixExpression.setRightOperand(literal);		
		literal = this.ast.newStringLiteral();//$NON-NLS-1$
		literal.setLiteralValue("!"); //$NON-NLS-1$
		infixExpression.extendedOperands().add(literal);
		NumberLiteral numberLiteral = this.ast.newNumberLiteral();//$NON-NLS-1$
		numberLiteral.setToken("4"); //$NON-NLS-1$
		infixExpression.extendedOperands().add(numberLiteral);
		assertTrue("Both AST trees should be identical", infixExpression.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "\"Hello\" + \" World\" + \"!\" + 4", source);//$NON-NLS-1$
	}

	/**
	 * NumberLiteral ==> InfixExpression
	 */
	public void test0105() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0105", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setOperator(InfixExpression.Operator.PLUS);
		NumberLiteral literal = this.ast.newNumberLiteral();//$NON-NLS-1$
		literal.setToken("4"); //$NON-NLS-1$
		infixExpression.setLeftOperand(literal);
		literal = this.ast.newNumberLiteral();//$NON-NLS-1$
		literal.setToken("5"); //$NON-NLS-1$
		infixExpression.setRightOperand(literal);		
		literal = this.ast.newNumberLiteral();//$NON-NLS-1$
		literal.setToken("6"); //$NON-NLS-1$
		infixExpression.extendedOperands().add(literal);
		literal = this.ast.newNumberLiteral();//$NON-NLS-1$
		literal.setToken("4"); //$NON-NLS-1$
		infixExpression.extendedOperands().add(literal);
		assertTrue("Both AST trees should be identical", infixExpression.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "4 + 5 + 6 + 4", source);//$NON-NLS-1$
	}
	
	/**
	 * NumberLiteral ==> InfixExpression
	 */
	public void test0106() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0106", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setOperator(InfixExpression.Operator.MINUS);
		NumberLiteral literal = this.ast.newNumberLiteral();//$NON-NLS-1$
		literal.setToken("4"); //$NON-NLS-1$
		infixExpression.setLeftOperand(literal);
		literal = this.ast.newNumberLiteral();//$NON-NLS-1$
		literal.setToken("5"); //$NON-NLS-1$
		infixExpression.setRightOperand(literal);		
		
		InfixExpression infixExpression2 = this.ast.newInfixExpression();
		infixExpression2.setOperator(InfixExpression.Operator.PLUS);
		infixExpression2.setLeftOperand(infixExpression);
		literal = this.ast.newNumberLiteral();//$NON-NLS-1$
		literal.setToken("6"); //$NON-NLS-1$
		infixExpression2.setRightOperand(literal);		
		
		InfixExpression infixExpression3 = this.ast.newInfixExpression();
		infixExpression3.setOperator(InfixExpression.Operator.PLUS);
		infixExpression3.setLeftOperand(infixExpression2);
		literal = this.ast.newNumberLiteral();//$NON-NLS-1$
		literal.setToken("4"); //$NON-NLS-1$
		infixExpression3.setRightOperand(literal);		
		
		assertTrue("Both AST trees should be identical", infixExpression3.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "4 - 5 + 6 + 4", source);//$NON-NLS-1$
	}

	/**
	 * NumberLiteral ==> InfixExpression
	 */
	public void test0107() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0107", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setOperator(InfixExpression.Operator.MINUS);
		NumberLiteral literal = this.ast.newNumberLiteral();//$NON-NLS-1$
		literal.setToken("4"); //$NON-NLS-1$
		infixExpression.setLeftOperand(literal);
		literal = this.ast.newNumberLiteral();//$NON-NLS-1$
		literal.setToken("5"); //$NON-NLS-1$
		infixExpression.setRightOperand(literal);		
		literal = this.ast.newNumberLiteral();//$NON-NLS-1$
		literal.setToken("6"); //$NON-NLS-1$
		infixExpression.extendedOperands().add(literal);
		literal = this.ast.newNumberLiteral();//$NON-NLS-1$
		literal.setToken("4"); //$NON-NLS-1$
		infixExpression.extendedOperands().add(literal);
		assertTrue("Both AST trees should be identical", infixExpression.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "4 - 5 - 6 - 4", source);//$NON-NLS-1$
	}

	/**
	 * NumberLiteral ==> InfixExpression
	 */
	public void test0108() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0108", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setOperator(InfixExpression.Operator.PLUS);
		StringLiteral stringLiteral = this.ast.newStringLiteral();//$NON-NLS-1$
		stringLiteral.setLiteralValue("4"); //$NON-NLS-1$
		infixExpression.setLeftOperand(stringLiteral);
		NumberLiteral literal = this.ast.newNumberLiteral();//$NON-NLS-1$
		literal.setToken("5"); //$NON-NLS-1$
		infixExpression.setRightOperand(literal);		
		literal = this.ast.newNumberLiteral();//$NON-NLS-1$
		literal.setToken("6"); //$NON-NLS-1$
		infixExpression.extendedOperands().add(literal);
		literal = this.ast.newNumberLiteral();//$NON-NLS-1$
		literal.setToken("4"); //$NON-NLS-1$
		infixExpression.extendedOperands().add(literal);
		assertTrue("Both AST trees should be identical", infixExpression.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "\"4\" + 5 + 6 + 4", source);//$NON-NLS-1$
	}
	
	/**
	 * NumberLiteral ==> InfixExpression
	 */
	public void test0109() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0109", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setOperator(InfixExpression.Operator.MINUS);
		StringLiteral stringLiteral = this.ast.newStringLiteral();//$NON-NLS-1$
		stringLiteral.setLiteralValue("4"); //$NON-NLS-1$
		infixExpression.setLeftOperand(stringLiteral);
		NumberLiteral literal = this.ast.newNumberLiteral();//$NON-NLS-1$
		literal.setToken("5"); //$NON-NLS-1$
		infixExpression.setRightOperand(literal);		
		
		InfixExpression infixExpression2 = this.ast.newInfixExpression();
		infixExpression2.setOperator(InfixExpression.Operator.PLUS);
		infixExpression2.setLeftOperand(infixExpression);
		literal = this.ast.newNumberLiteral();//$NON-NLS-1$
		literal.setToken("6"); //$NON-NLS-1$
		infixExpression2.setRightOperand(literal);		
		
		InfixExpression infixExpression3 = this.ast.newInfixExpression();
		infixExpression3.setOperator(InfixExpression.Operator.PLUS);
		infixExpression3.setLeftOperand(infixExpression2);
		literal = this.ast.newNumberLiteral();//$NON-NLS-1$
		literal.setToken("4"); //$NON-NLS-1$
		infixExpression3.setRightOperand(literal);		
		
		assertTrue("Both AST trees should be identical", infixExpression3.subtreeMatch(new ASTMatcher(), expression));		//$NON-NLS-1$
		checkSourceRange(expression, "\"4\" - 5 + 6 + 4", source);//$NON-NLS-1$
	}

	/**
	 * ReturnStatement ==> ReturnStatement
	 */
	public void test0110() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0110", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		ReturnStatement returnStatement = this.ast.newReturnStatement();
		NumberLiteral literal = this.ast.newNumberLiteral();
		literal.setToken("2");//$NON-NLS-1$
		returnStatement.setExpression(literal);
		assertTrue("Both AST trees should be identical", returnStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "return 2;", source);//$NON-NLS-1$
	}

	/**
	 * ReturnStatement ==> ReturnStatement
	 */
	public void test0111() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0111", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		ReturnStatement returnStatement = this.ast.newReturnStatement();
		NumberLiteral literal = this.ast.newNumberLiteral();
		literal.setToken("2");//$NON-NLS-1$
		returnStatement.setExpression(literal);
		assertTrue("Both AST trees should be identical", returnStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "return 2\\u003B", source);//$NON-NLS-1$
	}
	
//	/**
//	 * SynchronizedStatement ==> SynchronizedStatement
//	 */
//	public void test0112() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0112", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		SynchronizedStatement synchronizedStatement = this.ast.newSynchronizedStatement();
//		synchronizedStatement.setExpression(this.ast.newThisExpression());
//		synchronizedStatement.setBody(this.ast.newBlock());
//		assertTrue("Both AST trees should be identical", synchronizedStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		String expectedSource = "synchronized(this) {\n" +//$NON-NLS-1$
//			 "		}"; //$NON-NLS-1$
//		checkSourceRange(node, expectedSource, source);
//	}

	/**
	 * TryStatement ==> TryStatement
	 * @deprecated using deprecated code
	 */
	public void test0113() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0113", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		TryStatement tryStatement = this.ast.newTryStatement();
		tryStatement.setBody(this.ast.newBlock());
		tryStatement.setFinally(this.ast.newBlock());
		CatchClause catchBlock = this.ast.newCatchClause();
		catchBlock.setBody(this.ast.newBlock());
		SingleVariableDeclaration exceptionVariable = this.ast.newSingleVariableDeclaration();
		exceptionVariable.setModifiers(Modifier.NONE);
		exceptionVariable.setName(this.ast.newSimpleName("e"));//$NON-NLS-1$
//		exceptionVariable.setType(this.ast.newSimpleType(this.ast.newSimpleName("Exception")));//$NON-NLS-1$
		catchBlock.setException(exceptionVariable);
		tryStatement.catchClauses().add(catchBlock);
		assertTrue("Both AST trees should be identical", tryStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		String expectedSource = "try {\n" +//$NON-NLS-1$
			 "		} catch( e) {\n" +//$NON-NLS-1$
			 "		} finally {\n" +//$NON-NLS-1$
			 "		}"; //$NON-NLS-1$
		checkSourceRange(node, expectedSource, source);
	}

	/**
	 * TryStatement ==> TryStatement
	 * @deprecated using deprecated code
	 */
	public void test0114() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0114", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		TryStatement tryStatement = this.ast.newTryStatement();
		tryStatement.setBody(this.ast.newBlock());
		CatchClause catchBlock = this.ast.newCatchClause();
		catchBlock.setBody(this.ast.newBlock());
		SingleVariableDeclaration exceptionVariable = this.ast.newSingleVariableDeclaration();
		exceptionVariable.setModifiers(Modifier.NONE);
		exceptionVariable.setName(this.ast.newSimpleName("e"));//$NON-NLS-1$
//		exceptionVariable.setType(this.ast.newSimpleType(this.ast.newSimpleName("Exception")));//$NON-NLS-1$
		catchBlock.setException(exceptionVariable);
		tryStatement.catchClauses().add(catchBlock);
		assertTrue("Both AST trees should be identical", tryStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		String expectedSource = "try {\n" +//$NON-NLS-1$
			 "		} catch(e) {\n" +//$NON-NLS-1$
			 "		}"; //$NON-NLS-1$
		checkSourceRange(node, expectedSource, source);
	}

	/**
	 * TryStatement ==> TryStatement
	 * @deprecated using deprecated code
	 */
	public void test0115() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0115", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		TryStatement tryStatement = this.ast.newTryStatement();
		Block block = this.ast.newBlock();
		ReturnStatement returnStatement = this.ast.newReturnStatement();
		NumberLiteral literal = this.ast.newNumberLiteral();
		literal.setToken("2");//$NON-NLS-1$
		returnStatement.setExpression(literal);
		block.statements().add(returnStatement);
		tryStatement.setBody(block);
		CatchClause catchBlock = this.ast.newCatchClause();
		catchBlock.setBody(this.ast.newBlock());
		SingleVariableDeclaration exceptionVariable = this.ast.newSingleVariableDeclaration();
		exceptionVariable.setModifiers(Modifier.NONE);
		exceptionVariable.setName(this.ast.newSimpleName("e"));//$NON-NLS-1$
//		exceptionVariable.setType(this.ast.newSimpleType(this.ast.newSimpleName("Exception")));//$NON-NLS-1$
		catchBlock.setException(exceptionVariable);
		tryStatement.catchClauses().add(catchBlock);
		assertTrue("Both AST trees should be identical", tryStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		String expectedSource = "try {\n" +//$NON-NLS-1$
			 "			return 2;\n" +//$NON-NLS-1$
			 "		} catch(e) {\n" +//$NON-NLS-1$
			 "		}"; //$NON-NLS-1$
		checkSourceRange(node, expectedSource, source);
	}
		
	/**
	 * ThrowStatement ==> ThrowStatement
	 */
	public void test0116() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0116", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		ThrowStatement throwStatement = this.ast.newThrowStatement();
		throwStatement.setExpression(this.ast.newSimpleName("e")); //$NON-NLS-1$
		assertTrue("Both AST trees should be identical", throwStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "throw e   \\u003B", source);//$NON-NLS-1$
	}

	/**
	 * ThrowStatement ==> ThrowStatement
	 */
	public void test0117() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0117", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		ThrowStatement throwStatement = this.ast.newThrowStatement();
		throwStatement.setExpression(this.ast.newSimpleName("e")); //$NON-NLS-1$
		assertTrue("Both AST trees should be identical", throwStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "throw e /* comment in the middle of a throw */  \\u003B", source);//$NON-NLS-1$
	}

	/**
	 * ThrowStatement ==> ThrowStatement
	 */
	public void test0118() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0118", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		ThrowStatement throwStatement = this.ast.newThrowStatement();
		throwStatement.setExpression(this.ast.newSimpleName("e")); //$NON-NLS-1$
		assertTrue("Both AST trees should be identical", throwStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "throw e /* comment in the middle of a throw */  \\u003B", source);//$NON-NLS-1$
	}

	/**
	 * IfStatement ==> IfStatement
	 */
	public void test0119() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0119", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		IfStatement ifStatement = this.ast.newIfStatement();
		ifStatement.setExpression(this.ast.newBooleanLiteral(true));
		ifStatement.setThenStatement(this.ast.newEmptyStatement());
		assertTrue("Both AST trees should be identical", ifStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "if (true)\\u003B", source);//$NON-NLS-1$
	}

	/**
	 * IfStatement ==> IfStatement
	 */
	public void test0120() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0120", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		IfStatement ifStatement = this.ast.newIfStatement();
		ifStatement.setExpression(this.ast.newBooleanLiteral(true));
		ifStatement.setThenStatement(this.ast.newEmptyStatement());
		ifStatement.setElseStatement(this.ast.newEmptyStatement());
		assertTrue("Both AST trees should be identical", ifStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		String expectedSource = "if (true)\\u003B\n" +//$NON-NLS-1$
			 "\t\telse ;"; //$NON-NLS-1$
		checkSourceRange(node, expectedSource, source);
	}

	/**
	 * IfStatement ==> IfStatement
	 */
	public void test0121() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0121", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		IfStatement ifStatement = this.ast.newIfStatement();
		ifStatement.setExpression(this.ast.newBooleanLiteral(true));
		ifStatement.setThenStatement(this.ast.newBlock());
		ifStatement.setElseStatement(this.ast.newEmptyStatement());
		assertTrue("Both AST trees should be identical", ifStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		String expectedSource = "if (true) {}\n" +//$NON-NLS-1$
			 "		else ;"; //$NON-NLS-1$
		checkSourceRange(node, expectedSource, source);
	}

	/**
	 * IfStatement ==> IfStatement
	 */
	public void test0122() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0122", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		IfStatement ifStatement = this.ast.newIfStatement();
		ifStatement.setExpression(this.ast.newBooleanLiteral(true));
		ReturnStatement returnStatement = this.ast.newReturnStatement();
		NumberLiteral literal = this.ast.newNumberLiteral();
		literal.setToken("2");//$NON-NLS-1$
		returnStatement.setExpression(literal);
		ifStatement.setThenStatement(returnStatement);
		assertTrue("Both AST trees should be identical", ifStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "if (true) return 2\\u003B", source);//$NON-NLS-1$
	}

	/**
	 * IfStatement ==> IfStatement
	 */
	public void test0123() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0123", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		IfStatement ifStatement = this.ast.newIfStatement();
		ifStatement.setExpression(this.ast.newBooleanLiteral(true));
		ReturnStatement returnStatement = this.ast.newReturnStatement();
		NumberLiteral literal = this.ast.newNumberLiteral();
		literal.setToken("2");//$NON-NLS-1$
		returnStatement.setExpression(literal);
		ifStatement.setThenStatement(returnStatement);
		returnStatement = this.ast.newReturnStatement();
		literal = this.ast.newNumberLiteral();
		literal.setToken("3");//$NON-NLS-1$
		returnStatement.setExpression(literal);		
		ifStatement.setElseStatement(returnStatement);
		assertTrue("Both AST trees should be identical", ifStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		String expectedSource = "if (true) return 2;\n" +//$NON-NLS-1$
			 "		else return 3;"; //$NON-NLS-1$
		checkSourceRange(node, expectedSource, source);
	}

	/**
	 * Multiple local declaration => VariabledeclarationStatement
	 * @deprecated using deprecated code
	 */
//	public void test0124() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0124", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		VariableDeclarationFragment fragment = this.ast.newVariableDeclarationFragment();
//		fragment.setName(this.ast.newSimpleName("x"));//$NON-NLS-1$
//		NumberLiteral literal = this.ast.newNumberLiteral();
//		literal.setToken("10");//$NON-NLS-1$
//		fragment.setInitializer(literal);
//		fragment.setExtraDimensions(0);
//		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(fragment);
//		fragment = this.ast.newVariableDeclarationFragment();
//		fragment.setName(this.ast.newSimpleName("z"));//$NON-NLS-1$
//		fragment.setInitializer(this.ast.newNullLiteral());
//		fragment.setExtraDimensions(1);
//		statement.fragments().add(fragment);
//		fragment = this.ast.newVariableDeclarationFragment();
//		fragment.setName(this.ast.newSimpleName("i"));//$NON-NLS-1$
//		fragment.setExtraDimensions(0);
//		statement.fragments().add(fragment);
//		fragment = this.ast.newVariableDeclarationFragment();
//		fragment.setName(this.ast.newSimpleName("j"));//$NON-NLS-1$
//		fragment.setExtraDimensions(2);
//		statement.fragments().add(fragment);
//		statement.setType(this.ast.newPrimitiveType(PrimitiveType.INT));
//		statement.setModifiers(Modifier.NONE);
//		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		VariableDeclarationFragment[] fragments = (VariableDeclarationFragment[])((VariableDeclarationStatement) node).fragments().toArray(new VariableDeclarationFragment[4]);
//		assertTrue("fragments.length != 4", fragments.length == 4); //$NON-NLS-1$
//		checkSourceRange(fragments[0], "x= 10", source);//$NON-NLS-1$
//		checkSourceRange(fragments[1], "z[] = null", source);//$NON-NLS-1$
//		checkSourceRange(fragments[2], "i", source);//$NON-NLS-1$
//		checkSourceRange(fragments[3], "j[][]", source);//$NON-NLS-1$
//		checkSourceRange(node, "int x= 10, z[] = null, i, j[][];", source);//$NON-NLS-1$
//	}
//
//	/**
//	 * Multiple local declaration => VariabledeclarationStatement
//	 * @deprecated using deprecated code
//	 */
//	public void test0125() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0125", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		VariableDeclarationFragment fragment = this.ast.newVariableDeclarationFragment();
//		fragment.setName(this.ast.newSimpleName("x"));//$NON-NLS-1$
//		NumberLiteral literal = this.ast.newNumberLiteral();
//		literal.setToken("10");//$NON-NLS-1$
//		fragment.setInitializer(literal);
//		fragment.setExtraDimensions(0);
//		VariableDeclarationStatement statement = this.ast.newVariableDeclarationStatement(fragment);
//		fragment = this.ast.newVariableDeclarationFragment();
//		fragment.setName(this.ast.newSimpleName("z"));//$NON-NLS-1$
//		fragment.setInitializer(this.ast.newNullLiteral());
//		fragment.setExtraDimensions(1);
//		statement.fragments().add(fragment);
//		fragment = this.ast.newVariableDeclarationFragment();
//		fragment.setName(this.ast.newSimpleName("i"));//$NON-NLS-1$
//		fragment.setExtraDimensions(0);
//		statement.fragments().add(fragment);
//		fragment = this.ast.newVariableDeclarationFragment();
//		fragment.setName(this.ast.newSimpleName("j"));//$NON-NLS-1$
//		fragment.setExtraDimensions(2);
//		statement.fragments().add(fragment);
//		statement.setType(this.ast.newArrayType(this.ast.newPrimitiveType(PrimitiveType.INT), 1));
//		statement.setModifiers(Modifier.NONE);
//		assertTrue("Both AST trees should be identical", statement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(node, "int[] x= 10, z[] = null, i, j[][];", source); //$NON-NLS-1$
//		VariableDeclarationFragment[] fragments = (VariableDeclarationFragment[])((VariableDeclarationStatement) node).fragments().toArray(new VariableDeclarationFragment[4]);
//		assertTrue("fragments.length != 4", fragments.length == 4); //$NON-NLS-1$
//		checkSourceRange(fragments[0], "x= 10", source);//$NON-NLS-1$
//		checkSourceRange(fragments[1], "z[] = null", source);//$NON-NLS-1$
//		checkSourceRange(fragments[2], "i", source);//$NON-NLS-1$
//		checkSourceRange(fragments[3], "j[][]", source);//$NON-NLS-1$
//	}
//
//	/**
//	 * ForStatement
//	 * @deprecated using deprecated code
//	 */
//	public void test0126() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0126", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		ForStatement forStatement = this.ast.newForStatement();
//		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
//		variableDeclarationFragment.setName(this.ast.newSimpleName("tab")); //$NON-NLS-1$
//		variableDeclarationFragment.setInitializer(this.ast.newNullLiteral());//$NON-NLS-1$
//		variableDeclarationFragment.setExtraDimensions(1);
//		VariableDeclarationExpression variableDeclarationExpression = this.ast.newVariableDeclarationExpression(variableDeclarationFragment);
//		variableDeclarationExpression.setModifiers(Modifier.NONE);
//		variableDeclarationExpression.setType(this.ast.newArrayType(this.ast.newSimpleType(this.ast.newSimpleName("String")), 1));//$NON-NLS-1$
//		forStatement.initializers().add(variableDeclarationExpression);
//		PrefixExpression prefixExpression = this.ast.newPrefixExpression();
//		prefixExpression.setOperand(this.ast.newSimpleName("i"));//$NON-NLS-1$
//		prefixExpression.setOperator(PrefixExpression.Operator.INCREMENT);
//		forStatement.updaters().add(prefixExpression);
//		forStatement.setBody(this.ast.newBlock());
//		assertTrue("Both AST trees should be identical", forStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(node, "for (String[] tab[] = null;; ++i) {}", source); //$NON-NLS-1$
//		checkSourceRange((ASTNode) ((ForStatement) node).updaters().get(0), "++i", source); //$NON-NLS-1$
//		checkSourceRange((ASTNode) ((ForStatement) node).initializers().get(0), "String[] tab[] = null", source); //$NON-NLS-1$
//	}
//
//	/**
//	 * ForStatement
//	 * @deprecated using deprecated code
//	 */
//	public void test0127() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0127", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		ForStatement forStatement = this.ast.newForStatement();
//		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
//		variableDeclarationFragment.setName(this.ast.newSimpleName("tab")); //$NON-NLS-1$
//		variableDeclarationFragment.setInitializer(this.ast.newNullLiteral());//$NON-NLS-1$
//		variableDeclarationFragment.setExtraDimensions(1);
//		VariableDeclarationExpression variableDeclarationExpression = this.ast.newVariableDeclarationExpression(variableDeclarationFragment);
//		variableDeclarationExpression.setModifiers(Modifier.NONE);
//		variableDeclarationExpression.setType(this.ast.newSimpleType(this.ast.newSimpleName("String")));//$NON-NLS-1$
//		forStatement.initializers().add(variableDeclarationExpression);
//		PrefixExpression prefixExpression = this.ast.newPrefixExpression();
//		prefixExpression.setOperand(this.ast.newSimpleName("i"));//$NON-NLS-1$
//		prefixExpression.setOperator(PrefixExpression.Operator.INCREMENT);
//		forStatement.updaters().add(prefixExpression);
//		forStatement.setBody(this.ast.newBlock());
//		assertTrue("Both AST trees should be identical", forStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
//		checkSourceRange(node, "for (String tab[] = null;; ++i) {}", source); //$NON-NLS-1$
//		checkSourceRange((ASTNode) ((ForStatement) node).updaters().get(0), "++i", source); //$NON-NLS-1$
//		checkSourceRange((ASTNode) ((ForStatement) node).initializers().get(0), "String tab[] = null", source); //$NON-NLS-1$
//	}
//
	/**
	 * ForStatement
	 * @deprecated using deprecated code
	 */
	public void test0128() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0128", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		ForStatement forStatement = this.ast.newForStatement();
		VariableDeclarationFragment variableDeclarationFragment = this.ast.newVariableDeclarationFragment();
		variableDeclarationFragment.setName(this.ast.newSimpleName("tab")); //$NON-NLS-1$
		variableDeclarationFragment.setInitializer(this.ast.newNullLiteral());//$NON-NLS-1$
//		variableDeclarationFragment.setExtraDimensions(1);
		VariableDeclarationExpression variableDeclarationExpression = this.ast.newVariableDeclarationExpression(variableDeclarationFragment);
		variableDeclarationExpression.setModifiers(Modifier.NONE);
//		variableDeclarationExpression.setType(this.ast.newSimpleType(this.ast.newSimpleName("String")));//$NON-NLS-1$
		forStatement.initializers().add(variableDeclarationExpression);
		PostfixExpression postfixExpression = this.ast.newPostfixExpression();
		postfixExpression.setOperand(this.ast.newSimpleName("i"));//$NON-NLS-1$
		postfixExpression.setOperator(PostfixExpression.Operator.INCREMENT);
		forStatement.updaters().add(postfixExpression);
		forStatement.setBody(this.ast.newBlock());
		assertTrue("Both AST trees should be identical", forStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "for (var tab = null;; i++/**/) {}", source); //$NON-NLS-1$
		checkSourceRange((ASTNode) ((ForStatement) node).updaters().get(0), "i++", source); //$NON-NLS-1$
		checkSourceRange((ASTNode) ((ForStatement) node).initializers().get(0), "var tab = null", source); //$NON-NLS-1$
	}

	/**
	 * VariableDeclarationStatement
	 * @deprecated using deprecated code
	 */
	public void test0129() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0129", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("The node is not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationFragment frag = (VariableDeclarationFragment) ((VariableDeclarationStatement) node).fragments().get(0);
		assertTrue("Not a declaration", frag.getName().isDeclaration()); //$NON-NLS-1$

		VariableDeclarationFragment fragment = this.ast.newVariableDeclarationFragment();
		fragment.setName(this.ast.newSimpleName("i")); //$NON-NLS-1$

		
		VariableDeclarationStatement var = this.ast.newVariableDeclarationStatement(fragment);
//		fragment.setExtraDimensions(0);
//		VariableDeclarationStatement fieldDeclaration = this.ast.newVariableDeclarationStatement(fragment);
//		fieldDeclaration.setModifiers(Modifier.NONE);
//		fieldDeclaration.setType(this.ast.newPrimitiveType(PrimitiveType.INT));
		assertTrue("Both AST trees should be identical", var.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var i;", source); //$NON-NLS-1$
	}

	/**
	 * VariableDeclarationStatement
	 * @deprecated using deprecated code
	 */
	public void test0130() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0130", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("The node is not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationFragment fragment = this.ast.newVariableDeclarationFragment();
		fragment.setName(this.ast.newSimpleName("x")); //$NON-NLS-1$
		NumberLiteral literal = this.ast.newNumberLiteral();
		literal.setToken("10"); //$NON-NLS-1$
		fragment.setInitializer(literal);
//		fragment.setExtraDimensions(0);
		VariableDeclarationStatement variableDeclaration = this.ast.newVariableDeclarationStatement(fragment);
//		variableDeclaration.setModifiers(Modifier.PUBLIC);
//		fieldDeclaration.setType(this.ast.newPrimitiveType(PrimitiveType.INT));
		fragment = this.ast.newVariableDeclarationFragment();
		fragment.setName(this.ast.newSimpleName("y"));//$NON-NLS-1$
//		fragment.setExtraDimensions(1);
		fragment.setInitializer(this.ast.newNullLiteral());
		variableDeclaration.fragments().add(fragment);
		fragment = this.ast.newVariableDeclarationFragment();
		fragment.setName(this.ast.newSimpleName("i"));//$NON-NLS-1$
//		fragment.setExtraDimensions(0);
		variableDeclaration.fragments().add(fragment);
		fragment = this.ast.newVariableDeclarationFragment();
		fragment.setName(this.ast.newSimpleName("j"));//$NON-NLS-1$
//		fragment.setExtraDimensions(2);
		variableDeclaration.fragments().add(fragment);
		assertTrue("Both AST trees should be identical", variableDeclaration.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "var x= 10, y = null, i, j;", source); //$NON-NLS-1$
		VariableDeclarationFragment[] fragments = (VariableDeclarationFragment[])((VariableDeclarationStatement) node).fragments().toArray(new VariableDeclarationFragment[4]);
		assertTrue("fragments.length != 4", fragments.length == 4); //$NON-NLS-1$
		checkSourceRange(fragments[0], "x= 10", source);//$NON-NLS-1$
		checkSourceRange(fragments[1], "y = null", source);//$NON-NLS-1$
		checkSourceRange(fragments[2], "i", source);//$NON-NLS-1$
		checkSourceRange(fragments[3], "j", source);//$NON-NLS-1$
	}

//	/**
//	 * Argument with final modifier
//	 * @deprecated using deprecated code
//	 */
//	public void test0131() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0131", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("The node is not a FunctionDeclaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
//		assertTrue("Not a declaration", ((FunctionDeclaration) node).getName().isDeclaration()); //$NON-NLS-1$
//		List parameters = ((FunctionDeclaration) node).parameters();
//		assertTrue("Parameters.length != 1", parameters.size() == 1);		//$NON-NLS-1$
//		SingleVariableDeclaration arg = (SingleVariableDeclaration) ((FunctionDeclaration) node).parameters().get(0);
//		SingleVariableDeclaration singleVariableDeclaration = this.ast.newSingleVariableDeclaration();
//		singleVariableDeclaration.setModifiers(Modifier.FINAL);
//		singleVariableDeclaration.setName(this.ast.newSimpleName("i")); //$NON-NLS-1$
//		singleVariableDeclaration.setType(this.ast.newPrimitiveType(PrimitiveType.INT));
//		assertTrue("Both AST trees should be identical", singleVariableDeclaration.subtreeMatch(new ASTMatcher(), arg));		//$NON-NLS-1$
//		checkSourceRange(node, "void foo(final int i) {}", source); //$NON-NLS-1$
//		checkSourceRange(arg, "final int i", source); //$NON-NLS-1$
//	}

	/**
	 * Check javadoc for FunctionDeclaration
	 * @deprecated marking deprecated since using deprecated code
	 */
	public void test0132() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0132", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("The node is not a FunctionDeclaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
		JSdoc actualJavadoc = ((FunctionDeclaration) node).getJavadoc();
		JSdoc javadoc = this.ast.newJSdoc();
		javadoc.setComment("/** JavaDoc Comment*/");//$NON-NLS-1$*/
		assertTrue("Both AST trees should be identical", javadoc.subtreeMatch(new ASTMatcher(), actualJavadoc));//$NON-NLS-1$
		checkSourceRange(node, "/** JavaDoc Comment*/\n  function foo(i) {}", source); //$NON-NLS-1$
		checkSourceRange(actualJavadoc, "/** JavaDoc Comment*/", source); //$NON-NLS-1$
	}
	
	/**
	 * Check javadoc for FunctionDeclaration
	 */
	public void test0133() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0133", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("The node is not a FunctionDeclaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
		JSdoc actualJavadoc = ((FunctionDeclaration) node).getJavadoc();
		assertTrue("Javadoc must be null", actualJavadoc == null);//$NON-NLS-1$
		checkSourceRange(node, "function foo(i) {}", source); //$NON-NLS-1$
	}

	/**
	 * Check javadoc for FunctionDeclaration
	 */
	public void test0134() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0134", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("The node is not a FunctionDeclaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
		JSdoc actualJavadoc = ((FunctionDeclaration) node).getJavadoc();
		assertTrue("Javadoc must be null", actualJavadoc == null);//$NON-NLS-1$
		checkSourceRange(node, "function foo( i) {}", source); //$NON-NLS-1$
	}

	/**
	 * Check javadoc for VariableDeclarationStatement
	 * @deprecated marking deprecated since using deprecated code
	 */
	public void test0135() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0135", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("The node is not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		JSdoc actualJavadoc = ((VariableDeclarationStatement) node).getJavadoc();
		JSdoc javadoc = this.ast.newJSdoc();
		javadoc.setComment("/** JavaDoc Comment*/");//$NON-NLS-1$*/
		assertTrue("Both AST trees should be identical", javadoc.subtreeMatch(new ASTMatcher(), actualJavadoc));//$NON-NLS-1$
		checkSourceRange(node, "/** JavaDoc Comment*/\n var i;", source); //$NON-NLS-1$
	}

	/**
	 * Check javadoc for VariableDeclarationStatement
	 */
	public void test0136() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0136", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("The node is not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		JSdoc actualJavadoc = ((VariableDeclarationStatement) node).getJavadoc();
		assertTrue("Javadoc must be null", actualJavadoc == null);//$NON-NLS-1$
		checkSourceRange(node, "var i;", source); //$NON-NLS-1$
	}

	/**
	 * Check javadoc for VariableDeclarationStatement
	 */
	public void test0137() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0137", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("The node is not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		JSdoc actualJavadoc = ((VariableDeclarationStatement) node).getJavadoc();
		assertTrue("Javadoc must be null", actualJavadoc == null);//$NON-NLS-1$
		checkSourceRange(node, "var i;", source); //$NON-NLS-1$
	}

//	/**
//	 * Check javadoc for TypeDeclaration
//	 */
//	public void test0138() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0138", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("The node is not a TypeDeclaration", node instanceof TypeDeclaration); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((TypeDeclaration) node).getJavadoc();
//		assertTrue("Javadoc must be null", actualJavadoc == null);//$NON-NLS-1$
//		String expectedContents = "public class Test {\n" +//$NON-NLS-1$
//			"  int i;\n"  +//$NON-NLS-1$
//			"}";//$NON-NLS-1$
//		checkSourceRange(node, expectedContents, source); //$NON-NLS-1$
//	}
//
//	/**
//	 * Check javadoc for TypeDeclaration
//	 */
//	public void test0139() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0139", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("The node is not a TypeDeclaration", node instanceof TypeDeclaration); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((TypeDeclaration) node).getJavadoc();
//		assertTrue("Javadoc must be null", actualJavadoc == null);//$NON-NLS-1$
//		String expectedContents = "public class Test {\n" +//$NON-NLS-1$
//			"  int i;\n"  +//$NON-NLS-1$
//			"}";//$NON-NLS-1$
//		checkSourceRange(node, expectedContents, source); //$NON-NLS-1$
//	}
//
//	/**
//	 * Check javadoc for TypeDeclaration
//	 * @deprecated marking deprecated since using deprecated code
//	 */
//	public void test0140() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0140", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("The node is not a TypeDeclaration", node instanceof TypeDeclaration); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((TypeDeclaration) node).getJavadoc();
//		Javadoc javadoc = this.ast.newJavadoc();
//		javadoc.setComment("/** JavaDoc Comment*/");//$NON-NLS-1$*/
//		assertTrue("Both AST trees should be identical", javadoc.subtreeMatch(new ASTMatcher(), actualJavadoc));//$NON-NLS-1$
//		String expectedContents = 
//			 "/** JavaDoc Comment*/\n" + //$NON-NLS-1$
//			"public class Test {\n" +//$NON-NLS-1$
//			"  int i;\n"  +//$NON-NLS-1$
//			"}";//$NON-NLS-1$
//		checkSourceRange(node, expectedContents, source); //$NON-NLS-1$
//		checkSourceRange(actualJavadoc, "/** JavaDoc Comment*/", source); //$NON-NLS-1$
//	}

//	/**
//	 * Check javadoc for MemberTypeDeclaration
//	 * @deprecated marking deprecated since using deprecated code
//	 */
//	public void test0141() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0141", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("The node is not a TypeDeclaration", node instanceof TypeDeclaration); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((TypeDeclaration) node).getJavadoc();
//		Javadoc javadoc = this.ast.newJavadoc();
//		javadoc.setComment("/** JavaDoc Comment*/");//$NON-NLS-1$*/
//		assertTrue("Both AST trees should be identical", javadoc.subtreeMatch(new ASTMatcher(), actualJavadoc));//$NON-NLS-1$
//		String expectedContents = 
//			 "/** JavaDoc Comment*/\n" + //$NON-NLS-1$
//			 "  class B {}";//$NON-NLS-1$
//		checkSourceRange(node, expectedContents, source); //$NON-NLS-1$
//		checkSourceRange(actualJavadoc, "/** JavaDoc Comment*/", source); //$NON-NLS-1$
//	}
//
//	/**
//	 * Check javadoc for MemberTypeDeclaration
//	 */
//	public void test0142() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0142", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("The node is not a TypeDeclaration", node instanceof TypeDeclaration); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((TypeDeclaration) node).getJavadoc();
//		assertTrue("Javadoc must be null", actualJavadoc == null);//$NON-NLS-1$
//		checkSourceRange(node, "class B {}", source); //$NON-NLS-1$
//	}
//
//	/**
//	 * Check javadoc for MemberTypeDeclaration
//	 */
//	public void test0143() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0143", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("The node is not a TypeDeclaration", node instanceof TypeDeclaration); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((TypeDeclaration) node).getJavadoc();
//		assertTrue("Javadoc must be null", actualJavadoc == null);//$NON-NLS-1$
//		checkSourceRange(node, "public static class B {}", source); //$NON-NLS-1$
//	}
//
//	/**
//	 * Check javadoc for MemberTypeDeclaration
//	 */
//	public void test0144() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0144", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("The node is not a TypeDeclaration", node instanceof TypeDeclaration); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((TypeDeclaration) node).getJavadoc();
//		assertTrue("Javadoc must be null", actualJavadoc == null);//$NON-NLS-1$
//		checkSourceRange(node, "public static class B {}", source); //$NON-NLS-1$
//	}

//	/**
//	 * Checking initializers
//	 */
//	public void test0145() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0145", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		checkSourceRange(node, "{}", source); //$NON-NLS-1$
//	}
//
//	/**
//	 * Checking initializers
//	 */
//	public void test0146() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0146", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		checkSourceRange(node, "static {}", source); //$NON-NLS-1$
//	}
//
//	/**
//	 * Checking initializers
//	 * @deprecated marking deprecated since using deprecated code
//	 */
//	public void test0147() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0147", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((Initializer) node).getJavadoc();
//		assertNotNull("Javadoc comment should no be null", actualJavadoc); //$NON-NLS-1$
//		Javadoc javadoc = this.ast.newJavadoc();
//		javadoc.setComment("/** JavaDoc Comment*/");//$NON-NLS-1$*/
//		assertTrue("Both AST trees should be identical", javadoc.subtreeMatch(new ASTMatcher(), actualJavadoc));//$NON-NLS-1$
//		String expectedContents = 
//			 "/** JavaDoc Comment*/\n" + //$NON-NLS-1$
//			 "  static {}";//$NON-NLS-1$
//		checkSourceRange(node, expectedContents, source); //$NON-NLS-1$
//		checkSourceRange(actualJavadoc, "/** JavaDoc Comment*/", source); //$NON-NLS-1$
//		
//	}
//
//	/**
//	 * Checking initializers
//	 * @deprecated marking deprecated since using deprecated code
//	 */
//	public void test0148() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0148", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((Initializer) node).getJavadoc();
//		assertNotNull("Javadoc comment should not be null", actualJavadoc); //$NON-NLS-1$
//		Javadoc javadoc = this.ast.newJavadoc();
//		javadoc.setComment("/** JavaDoc Comment*/");//$NON-NLS-1$*/
//		assertTrue("Both AST trees should be identical", javadoc.subtreeMatch(new ASTMatcher(), actualJavadoc));//$NON-NLS-1$
//		String expectedContents = 
//			 "/** JavaDoc Comment*/\n" + //$NON-NLS-1$
//			 "  {}";//$NON-NLS-1$
//		checkSourceRange(node, expectedContents, source); //$NON-NLS-1$
//		checkSourceRange(actualJavadoc, "/** JavaDoc Comment*/", source); //$NON-NLS-1$
//		
//	}
//
//	/**
//	 * Checking initializers
//	 */
//	public void test0149() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0149", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((Initializer) node).getJavadoc();
//		assertNull("Javadoc comment should be null", actualJavadoc); //$NON-NLS-1$
//		checkSourceRange(node, "{}", source); //$NON-NLS-1$
//	}

	/**
	 * Checking syntax error
	 */
	public void test0150() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0150", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, false);
		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
		assertTrue("The result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit unit = (JavaScriptUnit) result;
		assertTrue("The compilation unit is malformed", !isMalformed(unit)); //$NON-NLS-1$
//		assertTrue("The package declaration is not malformed", isMalformed(unit.getPackage())); //$NON-NLS-1$
//		List imports = unit.imports();
//		assertTrue("The imports list size is not one", imports.size() == 1); //$NON-NLS-1$
//		assertTrue("The first import is malformed", !isMalformed((ASTNode) imports.get(0))); //$NON-NLS-1$
	}

//	/**
//	 * Checking syntax error
//	 */
//	public void test0151() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0151", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, false);
//		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
//		assertTrue("The compilation unit is malformed", !isMalformed(result)); //$NON-NLS-1$
//	}

	/**
	 * Checking syntax error
	 */
	public void test0152() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0152", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, false);
		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
		assertTrue("The compilation unit is malformed", !isMalformed(result)); //$NON-NLS-1$
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("The type is malformed", !isMalformed(node)); //$NON-NLS-1$
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("The field is not malformed", isMalformed(node)); //$NON-NLS-1$
//		node = getASTNode((JavaScriptUnit) result, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("The method is not malformed", isMalformed(node)); //$NON-NLS-1$
	}

	/**
	 * Checking syntax error
	 */
	public void Xtest0153() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0153", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, false);
		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
		assertTrue("The compilation unit is malformed", !isMalformed(result)); //$NON-NLS-1$
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("The method is not original", isOriginal(node)); //$NON-NLS-1$
		assertTrue("The method is not malformed", isMalformed(node)); //$NON-NLS-1$
	}
//
//	/**
//	 * Checking binding of package declaration
//	 */
//	public void test0154() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0154", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
//		assertTrue("The result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		IBinding binding = compilationUnit.getPackage().getName().resolveBinding();
//		assertNotNull("The package binding is null", binding); //$NON-NLS-1$
//		assertTrue("The binding is not a package binding", binding instanceof IPackageBinding); //$NON-NLS-1$
//		IPackageBinding packageBinding = (IPackageBinding) binding;
//		assertEquals("The package name is incorrect", "test0154", packageBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		IBinding binding2 = compilationUnit.getPackage().getName().resolveBinding();
//		assertTrue("The package binding is not canonical", binding == binding2); //$NON-NLS-1$
//	}

	/**
	 * Checking arguments positions
	 */
	public void test0155() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0155", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
		assertTrue("The result is not a compilation unit", result instanceof JavaScriptUnit);  //$NON-NLS-1$
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertTrue("The result is not a method declaration", node instanceof FunctionDeclaration);  //$NON-NLS-1$
		FunctionDeclaration methodDecl = (FunctionDeclaration) node;
		List parameters = methodDecl.parameters();
		assertTrue("The parameters size is different from 2", parameters.size() == 2);  //$NON-NLS-1$
		Object parameter = parameters.get(0);
		assertTrue("The parameter is not a SingleVariableDeclaration", parameter instanceof SingleVariableDeclaration);  //$NON-NLS-1$
		checkSourceRange((ASTNode) parameter, "i", source); //$NON-NLS-1$
		parameter = parameters.get(1);
		assertTrue("The parameter is not a SingleVariableDeclaration", parameter instanceof SingleVariableDeclaration);  //$NON-NLS-1$
		checkSourceRange((ASTNode) parameter, "b", source); //$NON-NLS-1$
	}
	
	/**
	 * Checking arguments positions
	 */
	public void test0156() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0156", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
		assertTrue("The result is not a compilation unit", result instanceof JavaScriptUnit);  //$NON-NLS-1$
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertTrue("The result is not a method declaration", node instanceof FunctionDeclaration);  //$NON-NLS-1$
		FunctionDeclaration methodDecl = (FunctionDeclaration) node;
		List parameters = methodDecl.parameters();
		assertTrue("The parameters size is different from 1", parameters.size() == 1);  //$NON-NLS-1$
		Object parameter = parameters.get(0);
		assertTrue("The parameter is not a SingleVariableDeclaration", parameter instanceof SingleVariableDeclaration);  //$NON-NLS-1$
		checkSourceRange((ASTNode) parameter, "i", source); //$NON-NLS-1$
		Block block = methodDecl.getBody();
		List statements = block.statements();
		assertTrue("The statements size is different from 2", statements.size() == 2);  //$NON-NLS-1$
		ASTNode statement = (ASTNode) statements.get(0);
		assertTrue("The statements[0] is a postfixExpression statement", statement instanceof ExpressionStatement);  //$NON-NLS-1$
	}

	/**
	 * Check canonic binding for fields
	 */
	public void test0157() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "", "Test0157.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
		assertTrue("The result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		List statements = compilationUnit.statements();
		assertTrue("The types list is empty", statements.size() != 0); //$NON-NLS-1$
//		TypeDeclaration typeDeclaration = (TypeDeclaration) types.get(0);
//		ITypeBinding typeBinding = typeDeclaration.resolveBinding();
//		assertNotNull("Type binding is null", typeBinding); //$NON-NLS-1$
//		assertTrue("The type binding is canonical", typeBinding == typeDeclaration.resolveBinding()); //$NON-NLS-1$
//		List bodyDeclarations = typeDeclaration.bodyDeclarations();
//		assertTrue("The body declaration list is empty", bodyDeclarations.size() != 0); //$NON-NLS-1$
		Statement bodyDeclaration = (Statement) statements.get(0);
		assertTrue("This is not a field", bodyDeclaration instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) bodyDeclaration;
		List variableFragments = fieldDeclaration.fragments();
		assertTrue("The fragment list is empty", variableFragments.size() != 0); //$NON-NLS-1$
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) variableFragments.get(0);
		IVariableBinding variableBinding = fragment.resolveBinding();
		assertNotNull("the field binding is null", variableBinding); //$NON-NLS-1$
		assertTrue("The field binding is not canonical", variableBinding == fragment.resolveBinding()); //$NON-NLS-1$
		ITypeBinding typeBinding = variableBinding.getType();
//		assertTrue("The type is not an array type", typeBinding.isArray()); //$NON-NLS-1$
		assertTrue("The type binding for the field is not canonical", typeBinding == variableBinding.getType()); //$NON-NLS-1$
		SimpleName name = fragment.getName();
		assertTrue("is a declaration", name.isDeclaration()); //$NON-NLS-1$
		IBinding binding = name.resolveBinding();
		assertNotNull("No binding", binding); //$NON-NLS-1$
		assertEquals("wrong type", IBinding.VARIABLE, binding.getKind()); //$NON-NLS-1$
		assertTrue("is a field", !((IVariableBinding) binding).isField()); //$NON-NLS-1$
	}

	/**
	 * Check canonic bindings for fields
	 */
	public void test0158() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "", "Test0158.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
		assertTrue("The result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		List statements = compilationUnit.statements();
		assertTrue("The types list is empty", statements.size() != 0); //$NON-NLS-1$
//		TypeDeclaration typeDeclaration = (TypeDeclaration) types.get(0);
//		ITypeBinding typeBinding = typeDeclaration.resolveBinding();
//		assertNotNull("Type binding is null", typeBinding); //$NON-NLS-1$
//		assertTrue("The type binding is canonical", typeBinding == typeDeclaration.resolveBinding()); //$NON-NLS-1$
//		SimpleName simpleName = typeDeclaration.getName();
//		assertTrue("is a declaration", simpleName.isDeclaration()); //$NON-NLS-1$
//		IBinding binding = simpleName.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertEquals("wrong type", IBinding.TYPE, binding.getKind()); //$NON-NLS-1$
//		assertEquals("wrong name", simpleName.getIdentifier(), binding.getName()); //$NON-NLS-1$
//		List bodyDeclarations = typeDeclaration.bodyDeclarations();
//		assertTrue("The body declaration list is empty", bodyDeclarations.size() != 0); //$NON-NLS-1$
		Statement bodyDeclaration = (Statement) statements.get(0);
		assertTrue("This is not a field", bodyDeclaration instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) bodyDeclaration;
		List variableFragments = fieldDeclaration.fragments();
		assertTrue("The fragment list is empty", variableFragments.size() != 0); //$NON-NLS-1$
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) variableFragments.get(0);
		IVariableBinding variableBinding = fragment.resolveBinding();
		assertNotNull("the field binding is null", variableBinding); //$NON-NLS-1$
		assertTrue("The field binding is not canonical", variableBinding == fragment.resolveBinding()); //$NON-NLS-1$
		ITypeBinding typeBinding2 = variableBinding.getType();
//		assertTrue("The type is not an array type", typeBinding2.isArray()); //$NON-NLS-1$
		assertTrue("The type binding for the field is not canonical", typeBinding2 == variableBinding.getType()); //$NON-NLS-1$
//		assertTrue("The type binding for the field is not canonical with the declaration type binding", typeBinding == typeBinding2.getElementType()); //$NON-NLS-1$
	}
	
//	/**
//	 * Define an anonymous type
//	 */
//	public void test0159() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0159", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
//		assertTrue("The result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//	}
	
	/**
	 * Check bindings for multiple field declarations
	 */
	public void test0160() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0160", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
		assertTrue("The result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		List statements = compilationUnit.statements();
		assertTrue("The statements list is empty", statements.size() != 0); //$NON-NLS-1$
//		TypeDeclaration typeDeclaration = (TypeDeclaration) types.get(0);
//		ITypeBinding typeBinding = typeDeclaration.resolveBinding();
//		assertNotNull("Type binding is null", typeBinding); //$NON-NLS-1$
//		assertTrue("The type binding is canonical", typeBinding == typeDeclaration.resolveBinding()); //$NON-NLS-1$
//		List bodyDeclarations = typeDeclaration.bodyDeclarations();
//		assertTrue("The body declaration list is empty", bodyDeclarations.size() != 0); //$NON-NLS-1$
		Statement bodyDeclaration = (Statement) statements.get(0);
		assertTrue("This is not a field", bodyDeclaration instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) bodyDeclaration;
		List variableFragments = fieldDeclaration.fragments();
		assertTrue("The fragment list size is not 2", variableFragments.size() == 2); //$NON-NLS-1$
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) variableFragments.get(0);
		IVariableBinding variableBinding1 = fragment.resolveBinding();
		assertNotNull("the field binding is null", variableBinding1); //$NON-NLS-1$
		assertTrue("The field binding is not canonical", variableBinding1 == fragment.resolveBinding()); //$NON-NLS-1$
		ITypeBinding type1 = variableBinding1.getType();
		assertNotNull("The type is null", type1); //$NON-NLS-1$
		assertTrue("The field type is canonical", type1 == variableBinding1.getType()); //$NON-NLS-1$
//		assertTrue("The type is not an array type",type1.isArray()); //$NON-NLS-1$
//		assertTrue("The type dimension is 1", type1.getDimensions() == 1); //$NON-NLS-1$
		fragment = (VariableDeclarationFragment) variableFragments.get(1);
		IVariableBinding variableBinding2 = fragment.resolveBinding();
		assertNotNull("the field binding is null", variableBinding2); //$NON-NLS-1$
		assertTrue("The field binding is not canonical", variableBinding2 == fragment.resolveBinding()); //$NON-NLS-1$
		ITypeBinding type2 = variableBinding2.getType();
		type2 = variableBinding2.getType();
		assertNotNull("The type is null", type2); //$NON-NLS-1$
		assertTrue("The field type is canonical", type2 == variableBinding2.getType()); //$NON-NLS-1$
//		assertTrue("The type is not an array type",type2.isArray()); //$NON-NLS-1$
//		assertTrue("The type dimension is 2", type2.getDimensions() == 2); //$NON-NLS-1$
//		assertTrue("Element type is canonical", type1.getElementType() == type2.getElementType()); //$NON-NLS-1$
		assertTrue("type1.id < type2.id", variableBinding1.getVariableId() < variableBinding2.getVariableId()); //$NON-NLS-1$
				
	}
//	
//	/**
//	 * Check ITypeBinding APIs:
//	 *  - getModifiers()
//	 *  - getElementType() when it is not an array type
//	 *  - getDimensions() when it is not an array type
//	 *  - getDeclaringClass()
//	 *  - getDeclaringName()
//	 *  - getName()
//	 *  - isNested()
//	 *  - isAnonymous()
//	 *  - isLocal()
//	 *  - isMember()
//	 *  - isArray()
//	 *  - getDeclaredMethods() => returns binding for default constructor
//	 *  - isPrimitive()
//	 *  - isTopLevel()
//	 *  - getSuperclass()
//	 */
//	public void test0161() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0161", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
//		assertTrue("The result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		List types = compilationUnit.types();
//		assertTrue("The types list is empty", types.size() != 0); //$NON-NLS-1$
//		TypeDeclaration typeDeclaration = (TypeDeclaration) types.get(0);
//		ITypeBinding typeBinding = typeDeclaration.resolveBinding();
//		assertNotNull("The type binding should not be null", typeBinding); //$NON-NLS-1$
//		assertEquals("The modifier is PUBLIC", Modifier.PUBLIC, typeBinding.getModifiers()); //$NON-NLS-1$
//		assertNull("There is no element type", typeBinding.getElementType()); //$NON-NLS-1$
//		assertEquals("There is no dimension", 0, typeBinding.getDimensions()); //$NON-NLS-1$
//		assertNull("This is not a member type", typeBinding.getDeclaringClass()); //$NON-NLS-1$
//		IFunctionBinding[] methods = typeBinding.getDeclaredMethods();
//		assertEquals("Contains the default constructor", 1, methods.length); //$NON-NLS-1$
//		assertEquals("The name is not Test", "Test", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertTrue("An anonymous class", !typeBinding.isAnonymous()); //$NON-NLS-1$
//		assertTrue("A local class", !typeBinding.isLocal()); //$NON-NLS-1$
//		assertTrue("A nested class", !typeBinding.isNested()); //$NON-NLS-1$
//		assertTrue("A member class", !typeBinding.isMember()); //$NON-NLS-1$
//		assertTrue("An array", !typeBinding.isArray()); //$NON-NLS-1$
//		assertTrue("Not a class", typeBinding.isClass()); //$NON-NLS-1$
//		assertTrue("An interface", !typeBinding.isInterface()); //$NON-NLS-1$
//		assertTrue("Not from source", typeBinding.isFromSource()); //$NON-NLS-1$
//		assertTrue("Is nested", typeBinding.isTopLevel()); //$NON-NLS-1$
//		assertTrue("A primitive type", !typeBinding.isPrimitive()); //$NON-NLS-1$
//		ITypeBinding superclass = typeBinding.getSuperclass();
//		assertNotNull("No superclass", superclass); //$NON-NLS-1$
//		assertTrue("From source", !superclass.isFromSource()); //$NON-NLS-1$
//		ITypeBinding supersuperclass = superclass.getSuperclass();
//		assertNull("No superclass for java.lang.Object", supersuperclass); //$NON-NLS-1$
//		ITypeBinding[] interfaces = typeBinding.getInterfaces();
//		assertNotNull("No interfaces", interfaces); //$NON-NLS-1$
//		assertEquals("More then one super interface", 1, interfaces.length); //$NON-NLS-1$
//		assertTrue("is not an interface", interfaces[0].isInterface()); //$NON-NLS-1$
//		assertTrue("From source", !interfaces[0].isFromSource()); //$NON-NLS-1$
//		assertEquals("Has fields", 0, typeBinding.getDeclaredFields().length); //$NON-NLS-1$
//	}
//
//	/**
//	 * Check ITypeBinding APIs:
//	 *  - getModifiers()
//	 *  - getElementType() when it is not an array type
//	 *  - getDimensions() when it is not an array type
//	 *  - getDeclaringClass()
//	 *  - getDeclaringName()
//	 *  - getName()
//	 *  - isNested()
//	 *  - isAnonymous()
//	 *  - isLocal()
//	 *  - isMember()
//	 *  - isArray()
//	 *  - getDeclaredMethods() => returns binding for default constructor
//	 *  - isPrimitive()
//	 *  - isTopLevel()
//	 *  - getSuperclass()
//	 */
//	public void test0162() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0162", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
//		assertTrue("The result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		List types = compilationUnit.types();
//		assertTrue("The types list is empty", types.size() != 0); //$NON-NLS-1$
//		TypeDeclaration typeDeclaration = (TypeDeclaration) types.get(0);
//		ITypeBinding typeBinding = typeDeclaration.resolveBinding();
//		assertNotNull("The type binding should not be null", typeBinding); //$NON-NLS-1$
//		assertEquals("The modifier is PUBLIC", Modifier.PUBLIC, typeBinding.getModifiers()); //$NON-NLS-1$
//		assertNull("There is no element type", typeBinding.getElementType()); //$NON-NLS-1$
//		assertEquals("There is no dimension", 0, typeBinding.getDimensions()); //$NON-NLS-1$
//		assertNull("This is not a member type", typeBinding.getDeclaringClass()); //$NON-NLS-1$
//		IFunctionBinding[] methods = typeBinding.getDeclaredMethods();
//		assertEquals("Contains no methos", 0, methods.length); //$NON-NLS-1$
//		assertEquals("The name is not Test", "Test", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertTrue("An anonymous class", !typeBinding.isAnonymous()); //$NON-NLS-1$
//		assertTrue("A local class", !typeBinding.isLocal()); //$NON-NLS-1$
//		assertTrue("A nested class", !typeBinding.isNested()); //$NON-NLS-1$
//		assertTrue("A member class", !typeBinding.isMember()); //$NON-NLS-1$
//		assertTrue("An array", !typeBinding.isArray()); //$NON-NLS-1$
//		assertTrue("A class", !typeBinding.isClass()); //$NON-NLS-1$
//		assertTrue("Not an interface", typeBinding.isInterface()); //$NON-NLS-1$
//		assertTrue("Not from source", typeBinding.isFromSource()); //$NON-NLS-1$
//		assertTrue("Is nested", typeBinding.isTopLevel()); //$NON-NLS-1$
//		assertTrue("A primitive type", !typeBinding.isPrimitive()); //$NON-NLS-1$
//		ITypeBinding superclass = typeBinding.getSuperclass();
//		assertNull("No superclass", superclass); //$NON-NLS-1$
//		assertEquals("Has fields", 0, typeBinding.getDeclaredFields().length); //$NON-NLS-1$
//	}
//
//	/**
//	 * Test binding for anonymous declaration: new java.lang.Object() {}
//	 */
//	public void test0163() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0163", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode expression = getASTNodeToCompare((JavaScriptUnit) result);
//		assertNotNull("Expression should not be null", expression); //$NON-NLS-1$
//		assertTrue("Not an anonymous type declaration", expression instanceof ClassInstanceCreation); //$NON-NLS-1$
//		ClassInstanceCreation anonymousClass = (ClassInstanceCreation) expression;
//		ITypeBinding typeBinding = anonymousClass.resolveTypeBinding();
//		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
//		assertTrue("Not an anonymous class", typeBinding.isAnonymous()); //$NON-NLS-1$
//		assertEquals("The modifier is not default", Modifier.NONE, typeBinding.getModifiers()); //$NON-NLS-1$
//		assertNull("There is no element type", typeBinding.getElementType()); //$NON-NLS-1$
//		assertEquals("There is no dimension", 0, typeBinding.getDimensions()); //$NON-NLS-1$
//		assertNotNull("This is a member type", typeBinding.getDeclaringClass()); //$NON-NLS-1$
//		assertEquals("The name is not empty", "", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		IFunctionBinding[] methods = typeBinding.getDeclaredMethods();
//		assertEquals("Contains the default constructor", 1, methods.length); //$NON-NLS-1$
//		assertTrue("Not a local class", typeBinding.isLocal()); //$NON-NLS-1$
//		assertTrue("Not a nested class", typeBinding.isNested()); //$NON-NLS-1$
//		assertTrue("A member class", !typeBinding.isMember()); //$NON-NLS-1$
//		assertTrue("An array", !typeBinding.isArray()); //$NON-NLS-1$
//		assertTrue("Not a class", typeBinding.isClass()); //$NON-NLS-1$
//		assertTrue("An interface", !typeBinding.isInterface()); //$NON-NLS-1$
//		assertTrue("Not from source", typeBinding.isFromSource()); //$NON-NLS-1$
//		assertTrue("Is a top level", !typeBinding.isTopLevel()); //$NON-NLS-1$
//		assertTrue("A primitive type", !typeBinding.isPrimitive()); //$NON-NLS-1$
//		assertEquals("wrong qualified name", "", typeBinding.getQualifiedName()); //$NON-NLS-1$ //$NON-NLS-2$
//		ITypeBinding superclass = typeBinding.getSuperclass();
//		assertNotNull("No superclass", superclass); //$NON-NLS-1$
//		assertEquals("Has fields", 0, typeBinding.getDeclaredFields().length); //$NON-NLS-1$
//	}
//	
//	/**
//	 * Test binding for member type declaration
//	 */
//	public void test0164() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0164", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("Not an type declaration", node instanceof TypeDeclaration); //$NON-NLS-1$
//		TypeDeclaration typeDeclaration = (TypeDeclaration) node;
//		ITypeBinding typeBinding = typeDeclaration.resolveBinding();
//		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
//		assertTrue("An anonymous class", !typeBinding.isAnonymous()); //$NON-NLS-1$
//		assertEquals("The modifier is not default", Modifier.PRIVATE, typeBinding.getModifiers()); //$NON-NLS-1$
//		assertNull("There is no element type", typeBinding.getElementType()); //$NON-NLS-1$
//		assertEquals("There is no dimension", 0, typeBinding.getDimensions()); //$NON-NLS-1$
//		assertNotNull("This is not a member type", typeBinding.getDeclaringClass()); //$NON-NLS-1$
//		assertEquals("The name is not 'B'", "B", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		IFunctionBinding[] methods = typeBinding.getDeclaredMethods();
//		assertEquals("Contains the default constructor", 1, methods.length); //$NON-NLS-1$
//		assertTrue("A local class", !typeBinding.isLocal()); //$NON-NLS-1$
//		assertTrue("Not a nested class", typeBinding.isNested()); //$NON-NLS-1$
//		assertTrue("Not a member class", typeBinding.isMember()); //$NON-NLS-1$
//		assertTrue("An array", !typeBinding.isArray()); //$NON-NLS-1$
//		assertTrue("Not a class", typeBinding.isClass()); //$NON-NLS-1$
//		assertTrue("An interface", !typeBinding.isInterface()); //$NON-NLS-1$
//		assertTrue("Not from source", typeBinding.isFromSource()); //$NON-NLS-1$
//		assertTrue("Is a top level", !typeBinding.isTopLevel()); //$NON-NLS-1$
//		assertTrue("A primitive type", !typeBinding.isPrimitive()); //$NON-NLS-1$
//		ITypeBinding superclass = typeBinding.getSuperclass();
//		assertNotNull("No superclass", superclass); //$NON-NLS-1$
//		assertEquals("Has fields", 0, typeBinding.getDeclaredFields().length); //$NON-NLS-1$
//	}
//	
//	/**
//	 * Test binding for local type declaration
//	 * @deprecated using deprecated code
//	 */
//	public void test0165() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0165", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("Not an type declaration", node instanceof TypeDeclarationStatement); //$NON-NLS-1$
//		TypeDeclarationStatement statement = (TypeDeclarationStatement) node;
//		TypeDeclaration typeDeclaration = statement.getTypeDeclaration();
//		ITypeBinding typeBinding = typeDeclaration.resolveBinding();
//		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
//		assertTrue("An anonymous class", !typeBinding.isAnonymous()); //$NON-NLS-1$
//		assertEquals("The modifier is not default", Modifier.NONE, typeBinding.getModifiers()); //$NON-NLS-1$
//		assertNull("There is no element type", typeBinding.getElementType()); //$NON-NLS-1$
//		assertEquals("There is no dimension", 0, typeBinding.getDimensions()); //$NON-NLS-1$
//		assertNotNull("This is not a member type", typeBinding.getDeclaringClass()); //$NON-NLS-1$
//		assertEquals("The name is not 'C'", "C", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		IFunctionBinding[] methods = typeBinding.getDeclaredMethods();
//		assertEquals("Contains the default constructor", 1, methods.length); //$NON-NLS-1$
//		assertTrue("Not a local class", typeBinding.isLocal()); //$NON-NLS-1$
//		assertTrue("Not a nested class", typeBinding.isNested()); //$NON-NLS-1$
//		assertTrue("A member class", !typeBinding.isMember()); //$NON-NLS-1$
//		assertTrue("An array", !typeBinding.isArray()); //$NON-NLS-1$
//		assertTrue("Not a class", typeBinding.isClass()); //$NON-NLS-1$
//		assertTrue("An interface", !typeBinding.isInterface()); //$NON-NLS-1$
//		assertTrue("Not from source", typeBinding.isFromSource()); //$NON-NLS-1$
//		assertTrue("Is a top level", !typeBinding.isTopLevel()); //$NON-NLS-1$
//		assertTrue("A primitive type", !typeBinding.isPrimitive()); //$NON-NLS-1$
//		assertEquals("wrong qualified name", "", typeBinding.getQualifiedName()); //$NON-NLS-1$ //$NON-NLS-2$
//		ITypeBinding superclass = typeBinding.getSuperclass();
//		assertNotNull("No superclass", superclass); //$NON-NLS-1$
//		assertEquals("Has fields", 0, typeBinding.getDeclaredFields().length); //$NON-NLS-1$
//	}

	/**
	 * Multiple local declaration => VariabledeclarationStatement
	 */
	public void test0166() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0166", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertTrue("Not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
		List fragments = variableDeclarationStatement.fragments();
		assertTrue("Fragment list is not 4 ", fragments.size() == 4); //$NON-NLS-1$
		VariableDeclarationFragment fragment1 = (VariableDeclarationFragment) fragments.get(0);
		IVariableBinding binding1 = fragment1.resolveBinding();
		assertNotNull("Binding is null", binding1); //$NON-NLS-1$
		assertEquals("wrong name for binding1", "x", binding1.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("wrong modifier for binding1", 0, binding1.getModifiers()); //$NON-NLS-1$
		assertTrue("a field", !binding1.isField()); //$NON-NLS-1$
//		assertNull("declaring class is not null", binding1.getDeclaringClass()); //$NON-NLS-1$
		ITypeBinding typeBinding1 = binding1.getType();
		assertNotNull("typeBinding1 is null", typeBinding1); //$NON-NLS-1$
//		assertTrue("typeBinding1 is not a primitive type", typeBinding1.isPrimitive()); //$NON-NLS-1$
//		assertTrue("typeBinding1 is not canonical", typeBinding1 == binding1.getType()); //$NON-NLS-1$
		VariableDeclarationFragment fragment2 = (VariableDeclarationFragment) fragments.get(1);
		IVariableBinding binding2 = fragment2.resolveBinding();
		assertNotNull("Binding is null", binding2); //$NON-NLS-1$
		assertEquals("wrong name for binding2", "z", binding2.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("wrong modifier for binding2", 0, binding2.getModifiers()); //$NON-NLS-1$
		assertTrue("a field", !binding2.isField()); //$NON-NLS-1$
//		assertNull("declaring class is not null", binding2.getDeclaringClass()); //$NON-NLS-1$
		ITypeBinding typeBinding2 = binding2.getType();
		assertNotNull("typeBinding2 is null", typeBinding2); //$NON-NLS-1$
//		assertTrue("typeBinding2 is not an array type", typeBinding2.isArray()); //$NON-NLS-1$
//		assertTrue("typeBinding2 is not canonical", typeBinding2 == binding2.getType()); //$NON-NLS-1$
//		assertTrue("primitive type is not canonical", typeBinding1 == typeBinding2.getElementType()); //$NON-NLS-1$
//		assertEquals("dimension is 1", 1, typeBinding2.getDimensions()); //$NON-NLS-1$
//		assertEquals("it is not int[]", "int[]", typeBinding2.getName());		 //$NON-NLS-1$ //$NON-NLS-2$
		VariableDeclarationFragment fragment3 = (VariableDeclarationFragment) fragments.get(2);
		IVariableBinding binding3 = fragment3.resolveBinding();
		assertNotNull("Binding is null", binding3); //$NON-NLS-1$
		assertEquals("wrong name for binding3", "i", binding3.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("wrong modifier for binding3", 0, binding3.getModifiers()); //$NON-NLS-1$
		assertTrue("a field", !binding3.isField()); //$NON-NLS-1$
//		assertNull("declaring class is not null", binding3.getDeclaringClass()); //$NON-NLS-1$
		ITypeBinding typeBinding3 = binding3.getType();
		assertNotNull("typeBinding3 is null", typeBinding3); //$NON-NLS-1$
//		assertTrue("typeBinding3 is not an primitive type", typeBinding3.isPrimitive()); //$NON-NLS-1$
//		assertTrue("typeBinding3 is not canonical", typeBinding3 == binding3.getType()); //$NON-NLS-1$
//		assertTrue("primitive type is not canonical", typeBinding1 == typeBinding3); //$NON-NLS-1$
		assertEquals("dimension is 0", 0, typeBinding3.getDimensions()); //$NON-NLS-1$
		assertEquals("it is not the primitive type any", "any", typeBinding3.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		VariableDeclarationFragment fragment4 = (VariableDeclarationFragment) fragments.get(3);
		IVariableBinding binding4 = fragment4.resolveBinding();
		assertNotNull("Binding is null", binding4); //$NON-NLS-1$
		assertEquals("wrong name for binding4", "j", binding4.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("wrong modifier for binding4", 0, binding4.getModifiers()); //$NON-NLS-1$
		assertTrue("a field", !binding4.isField()); //$NON-NLS-1$
		assertNull("declaring class is not null", binding4.getDeclaringClass()); //$NON-NLS-1$
		ITypeBinding typeBinding4 = binding4.getType();
		assertNotNull("typeBinding4 is null", typeBinding4); //$NON-NLS-1$
//		assertTrue("typeBinding4 is not an array type", typeBinding4.isArray()); //$NON-NLS-1$
//		assertTrue("typeBinding4 is not canonical", typeBinding4 == binding4.getType()); //$NON-NLS-1$
//		assertTrue("primitive type is not canonical", typeBinding1 == typeBinding4.getElementType()); //$NON-NLS-1$
//		assertEquals("dimension is 2", 2, typeBinding4.getDimensions()); //$NON-NLS-1$
//		assertEquals("it is not int[][]", "int[][]", typeBinding4.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue("ids in the wrong order", binding1.getVariableId() < binding2.getVariableId()); //$NON-NLS-1$
		assertTrue("ids in the wrong order", binding2.getVariableId() < binding3.getVariableId()); //$NON-NLS-1$
		assertTrue("ids in the wrong order", binding3.getVariableId() < binding4.getVariableId()); //$NON-NLS-1$
	}
//	
//	/**
//	 * Check source position for new Test[1+2].length.
//	 */
//	public void test0167() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0167", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("Instance of VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
//		List fragments = variableDeclarationStatement.fragments();
//		assertTrue("fragment list size is not 1", fragments.size() == 1); //$NON-NLS-1$
//		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(0);
//		Expression initialization = fragment.getInitializer();
//		assertNotNull("No initialization", initialization); //$NON-NLS-1$
//		assertTrue("Not a FieldAccess", initialization instanceof FieldAccess); //$NON-NLS-1$
//		checkSourceRange(initialization, "new Test[1+2].length", source); //$NON-NLS-1$
//	}
//	
//	/**
//	 * Check package binding: test0168.test
//	 */
//	public void test0168() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0168.test1", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
//		assertTrue("The result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		List types = compilationUnit.types();
//		assertTrue("The types list is empty", types.size() != 0); //$NON-NLS-1$
//		TypeDeclaration typeDeclaration = (TypeDeclaration) types.get(0);
//		ITypeBinding typeBinding = typeDeclaration.resolveBinding();
//		assertNotNull("Binding not null", typeBinding); //$NON-NLS-1$
//		IPackageBinding packageBinding = typeBinding.getPackage();
//		assertNotNull("No package binding", packageBinding); //$NON-NLS-1$
//		assertEquals("wrong name", "test0168.test1", packageBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		String[] components = packageBinding.getNameComponents();
//		assertNotNull("no components", components); //$NON-NLS-1$
//		assertTrue("components size != 2", components.length == 2); //$NON-NLS-1$
//		assertEquals("wrong component name", "test0168", components[0]); //$NON-NLS-1$ //$NON-NLS-2$
//		assertEquals("wrong component name", "test1", components[1]); //$NON-NLS-1$ //$NON-NLS-2$
//		assertEquals("wrong type", IBinding.PACKAGE, packageBinding.getKind()); //$NON-NLS-1$
//		assertTrue("Unnamed package", !packageBinding.isUnnamed()); //$NON-NLS-1$
//		assertTrue("Package binding is not canonical", packageBinding == typeBinding.getPackage()); //$NON-NLS-1$
//	}
	
//	/**
//	 * Check package binding: test0169
//	 */
//	public void test0169() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0169", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
//		assertTrue("The result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		List types = compilationUnit.types();
//		assertTrue("The types list is empty", types.size() != 0); //$NON-NLS-1$
//		TypeDeclaration typeDeclaration = (TypeDeclaration) types.get(0);
//		ITypeBinding typeBinding = typeDeclaration.resolveBinding();
//		assertNotNull("Binding not null", typeBinding); //$NON-NLS-1$
//		IPackageBinding packageBinding = typeBinding.getPackage();
//		assertNotNull("No package binding", packageBinding); //$NON-NLS-1$
//		assertEquals("wrong name", "test0169", packageBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		String[] components = packageBinding.getNameComponents();
//		assertNotNull("no components", components); //$NON-NLS-1$
//		assertTrue("components size != 1", components.length == 1); //$NON-NLS-1$
//		assertEquals("wrong component name", "test0169", components[0]); //$NON-NLS-1$ //$NON-NLS-2$
//		assertEquals("wrong type", IBinding.PACKAGE, packageBinding.getKind()); //$NON-NLS-1$
//		assertTrue("Unnamed package", !packageBinding.isUnnamed()); //$NON-NLS-1$
//		assertTrue("Package binding is not canonical", packageBinding == typeBinding.getPackage()); //$NON-NLS-1$
//	}
	
//	/**
//	 * Check package binding: test0170
//	 */
//	public void test0170() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "", "Test0170.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
//		assertTrue("The result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		List types = compilationUnit.types();
//		assertTrue("The types list is empty", types.size() != 0); //$NON-NLS-1$
//		TypeDeclaration typeDeclaration = (TypeDeclaration) types.get(0);
//		ITypeBinding typeBinding = typeDeclaration.resolveBinding();
//		assertNotNull("Binding not null", typeBinding); //$NON-NLS-1$
//		IPackageBinding packageBinding = typeBinding.getPackage();
//		assertNotNull("No package binding", packageBinding); //$NON-NLS-1$
//		assertEquals("wrong name", "", packageBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		String[] components = packageBinding.getNameComponents();
//		assertNotNull("no components", components); //$NON-NLS-1$
//		assertTrue("components size != 0", components.length == 0); //$NON-NLS-1$
//		assertEquals("wrong type", IBinding.PACKAGE, packageBinding.getKind()); //$NON-NLS-1$
//		assertTrue("Not an unnamed package", packageBinding.isUnnamed()); //$NON-NLS-1$
//		assertTrue("Package binding is not canonical", packageBinding == typeBinding.getPackage()); //$NON-NLS-1$
//	}
//
//	/**
//	 * Check package binding: test0171
//	 */
//	public void test0171() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0171", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
//		assertTrue("The result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		List types = compilationUnit.types();
//		assertTrue("The types list is empty", types.size() == 2); //$NON-NLS-1$
//		TypeDeclaration typeDeclaration = (TypeDeclaration) types.get(0);
//		ITypeBinding typeBinding = typeDeclaration.resolveBinding();
//		assertNotNull("Binding not null", typeBinding); //$NON-NLS-1$
//		IPackageBinding packageBinding = typeBinding.getPackage();
//		assertNotNull("No package binding", packageBinding); //$NON-NLS-1$
//		assertEquals("wrong name", "test0171", packageBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		String[] components = packageBinding.getNameComponents();
//		assertNotNull("no components", components); //$NON-NLS-1$
//		assertTrue("components size != 1", components.length == 1); //$NON-NLS-1$
//		assertEquals("wrong component name", "test0171", components[0]); //$NON-NLS-1$ //$NON-NLS-2$
//		assertEquals("wrong type", IBinding.PACKAGE, packageBinding.getKind()); //$NON-NLS-1$
//		assertTrue("Unnamed package", !packageBinding.isUnnamed()); //$NON-NLS-1$
//		assertTrue("Package binding is not canonical", packageBinding == typeBinding.getPackage()); //$NON-NLS-1$
//		
//		typeDeclaration = (TypeDeclaration) types.get(1);
//		typeBinding = typeDeclaration.resolveBinding();
//		assertNotNull("Binding not null", typeBinding); //$NON-NLS-1$
//		IPackageBinding packageBinding2 = typeBinding.getPackage();
//		assertNotNull("No package binding", packageBinding); //$NON-NLS-1$
//		assertTrue("Package binding is not canonical", packageBinding == packageBinding2); //$NON-NLS-1$
//	}

	/**
	 * Check method binding
	 */
	public void test0172() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0172", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
		assertTrue("The result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		List statements = compilationUnit.statements();
		assertTrue("The statements list is empty", statements.size() != 0); //$NON-NLS-1$
//		TypeDeclaration typeDeclaration = (TypeDeclaration) statements.get(0);
//		ITypeBinding typeBinding =  typeDeclaration.resolveBinding();
//		assertNotNull("Binding not null", typeBinding); //$NON-NLS-1$
//		IFunctionBinding[] methods = null;// typeBinding.getDeclaredMethods();
//		assertNotNull("No methods", methods);
//		assertEquals("methods.length != 4", 4, methods.length); //$NON-NLS-1$
//		List bodyDeclarations = typeDeclaration.bodyDeclarations();
		assertEquals("statements size != 3", 3, statements.size()); //$NON-NLS-1$
		FunctionDeclaration method1 = (FunctionDeclaration) statements.get(0);
		IFunctionBinding methodBinding1 = method1.resolveBinding();
		assertNotNull("No method binding for foo", methodBinding1); //$NON-NLS-1$
		SimpleName simpleName = method1.getName();
		assertTrue("not a declaration", simpleName.isDeclaration()); //$NON-NLS-1$
		IBinding binding = simpleName.resolveBinding();
		assertNotNull("No binding", binding); //$NON-NLS-1$
		assertEquals("wrong name", binding.getName(), simpleName.getIdentifier()); //$NON-NLS-1$
		// search method foo
//		IFunctionBinding methodBinding = null;
//		loop: for (int i = 0, max = methods.length; i < max; i++) {
//			IFunctionBinding currentMethod = methods[i];
//			if ("foo".equals(currentMethod.getName())) {
//				methodBinding = currentMethod;
//				break loop;
//			}
//		}
//		assertNotNull("Cannot be null", methodBinding);
//		assertTrue("Canonical method binding", methodBinding1 == methodBinding); //$NON-NLS-1$
//		assertTrue("declaring class is canonical", typeBinding == methodBinding1.getDeclaringClass()); //$NON-NLS-1$
//		ITypeBinding[] exceptionTypes = methodBinding1.getExceptionTypes();
//		assertNotNull("No exception types", exceptionTypes); //$NON-NLS-1$
//		assertEquals("One exception", 1, exceptionTypes.length); //$NON-NLS-1$
//		assertEquals("wrong name for exception", "IOException", exceptionTypes[0].getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("wrong modifier", Modifier.PUBLIC, methodBinding1.getModifiers()); //$NON-NLS-1$
		assertEquals("wrong name for method", "foo", methodBinding1.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		ITypeBinding[] parameters = methodBinding1.getParameterTypes();
		assertNotNull("No parameters", parameters); //$NON-NLS-1$
		assertEquals("wrong size", 1, parameters.length); //$NON-NLS-1$
//		assertEquals("wrong type", "int[]", parameters[0].getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("wrong return type", "void", methodBinding1.getReturnType().getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertTrue("A constructor", !methodBinding1.isConstructor()); //$NON-NLS-1$
		
		FunctionDeclaration method2 = (FunctionDeclaration) statements.get(1);
		IFunctionBinding methodBinding2 = method2.resolveBinding();
		assertNotNull("No method binding for main", methodBinding2); //$NON-NLS-1$
//		methodBinding = null;
//		loop: for (int i = 0, max = methods.length; i < max; i++) {
//			IFunctionBinding currentMethod = methods[i];
//			if ("main".equals(currentMethod.getName())) {
//				methodBinding = currentMethod;
//				break loop;
//			}
//		}
//		assertNotNull("Cannot be null", methodBinding);
//		assertTrue("Canonical method binding", methodBinding2 == methodBinding); //$NON-NLS-1$
//		assertTrue("declaring class is canonical", typeBinding == methodBinding2.getDeclaringClass()); //$NON-NLS-1$
//		ITypeBinding[] exceptionTypes2 = methodBinding2.getExceptionTypes();
//		assertNotNull("No exception types", exceptionTypes2); //$NON-NLS-1$
//		assertEquals("No exception", 0, exceptionTypes2.length); //$NON-NLS-1$
//		assertEquals("wrong modifier", Modifier.PUBLIC | Modifier.STATIC, methodBinding2.getModifiers()); //$NON-NLS-1$
		assertEquals("wrong name for method", "main", methodBinding2.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		ITypeBinding[] parameters2 = methodBinding2.getParameterTypes();
		assertNotNull("No parameters", parameters2); //$NON-NLS-1$
		assertEquals("wrong size", 1, parameters2.length); //$NON-NLS-1$
//		assertEquals("wrong type for parameter2[0]", "String[]", parameters2[0].getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("wrong return type", "void", methodBinding2.getReturnType().getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertTrue("A constructor", !methodBinding2.isConstructor()); //$NON-NLS-1$
		
		FunctionDeclaration method3 = (FunctionDeclaration) statements.get(2);
		IFunctionBinding methodBinding3 = method3.resolveBinding();
		assertNotNull("No method binding for main", methodBinding3); //$NON-NLS-1$
//		methodBinding = null;
//		loop: for (int i = 0, max = methods.length; i < max; i++) {
//			IFunctionBinding currentMethod = methods[i];
//			if ("bar".equals(currentMethod.getName())) {
//				methodBinding = currentMethod;
//				break loop;
//			}
//		}		
//		assertNotNull("Cannot be null", methodBinding);
//		assertTrue("Canonical method binding", methodBinding3 == methodBinding); //$NON-NLS-1$
//		assertTrue("declaring class is canonical", typeBinding == methodBinding3.getDeclaringClass()); //$NON-NLS-1$
//		ITypeBinding[] exceptionTypes3 = methodBinding3.getExceptionTypes();
//		assertNotNull("No exception types", exceptionTypes3); //$NON-NLS-1$
//		assertEquals("No exception", 1, exceptionTypes3.length); //$NON-NLS-1$
//		assertEquals("wrong modifier", Modifier.PRIVATE, methodBinding3.getModifiers()); //$NON-NLS-1$
		assertEquals("wrong name for method", "bar", methodBinding3.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		ITypeBinding[] parameters3 = methodBinding3.getParameterTypes();
		assertNotNull("No parameters", parameters3); //$NON-NLS-1$
		assertEquals("wrong size", 1, parameters3.length); //$NON-NLS-1$
		assertEquals("wrong type", "any", parameters3[0].getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("wrong return type", "any", methodBinding3.getReturnType().getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertTrue("A constructor", !methodBinding3.isConstructor()); //$NON-NLS-1$
		assertTrue("The binding is not canonical", parameters3[0] == methodBinding3.getReturnType()); //$NON-NLS-1$
	}
	
	/**
	 * i++; IVariableBinding
	 */
	public void test0173() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0173", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("Not an expressionStatement", node instanceof ExpressionStatement); //$NON-NLS-1$
		ExpressionStatement expressionStatement = (ExpressionStatement) node;
		Expression ex = expressionStatement.getExpression();
		assertTrue("Not a postfixexpression", ex instanceof PostfixExpression); //$NON-NLS-1$
		PostfixExpression postfixExpression = (PostfixExpression) ex;
		Expression expr = postfixExpression.getOperand();
		assertTrue("Not a simpleName", expr instanceof SimpleName); //$NON-NLS-1$
		SimpleName name = (SimpleName) expr;
		assertTrue("a declaration", !name.isDeclaration()); //$NON-NLS-1$
		IBinding binding = name.resolveBinding();
		assertNotNull("No binding", binding); //$NON-NLS-1$

		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertTrue("VariableDeclarationStatement", node2 instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node2;
		List fragments = variableDeclarationStatement.fragments();
		assertTrue("No fragment", fragments.size() == 1); //$NON-NLS-1$
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(0);
		IVariableBinding variableBinding = fragment.resolveBinding();
		assertTrue(variableBinding == binding);
	}

	/**
	 * i++; IVariableBinding (field)
	 */
	public void test0174() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0174", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 1, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("Not an expressionStatement", node instanceof ExpressionStatement); //$NON-NLS-1$
		ExpressionStatement expressionStatement = (ExpressionStatement) node;
		Expression ex = expressionStatement.getExpression();
		assertTrue("Not a postfixexpression", ex instanceof PostfixExpression); //$NON-NLS-1$
		PostfixExpression postfixExpression = (PostfixExpression) ex;
		Expression expr = postfixExpression.getOperand();
		assertTrue("Not a simpleName", expr instanceof SimpleName); //$NON-NLS-1$
		SimpleName name = (SimpleName) expr;
		IBinding binding = name.resolveBinding();
		assertNotNull("No binding", binding); //$NON-NLS-1$

		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0);
		assertTrue("VariableDeclarationStatement", node2 instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node2;
		List fragments = fieldDeclaration.fragments();
		assertTrue("No fragment", fragments.size() == 1); //$NON-NLS-1$
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(0);
		IVariableBinding variableBinding = fragment.resolveBinding();
		assertTrue(variableBinding == binding);
	}
	
	/**
	 * int i = 0; Test IntBinding for the field declaration and the 0 literal
	 */
	public void test0175() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0175", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0);
		assertTrue("VariableDeclarationStatement", node2 instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node2;
		List fragments = fieldDeclaration.fragments();
		assertTrue("No fragment", fragments.size() == 1); //$NON-NLS-1$
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(0);
		ITypeBinding typeBinding = fragment.getInitializer().resolveTypeBinding();
		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
		assertEquals("Not Number", "Number", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}	

	/**
	 * i++; IVariableBinding
	 */
	public void test0177() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0177", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 1, 1);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("Not an expressionStatement", node instanceof ExpressionStatement); //$NON-NLS-1$
		ExpressionStatement expressionStatement = (ExpressionStatement) node;
		Expression ex = expressionStatement.getExpression();
		assertTrue("Not a postfixexpression", ex instanceof PostfixExpression); //$NON-NLS-1$
		PostfixExpression postfixExpression = (PostfixExpression) ex;
		Expression expr = postfixExpression.getOperand();
		assertTrue("Not a simpleName", expr instanceof SimpleName); //$NON-NLS-1$
		SimpleName name = (SimpleName) expr;
		IBinding binding = name.resolveBinding();
		assertNotNull("No binding", binding); //$NON-NLS-1$

		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 1, 0);
		assertTrue("VariableDeclarationStatement", node2 instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node2;
		List fragments = variableDeclarationStatement.fragments();
		assertTrue("No fragment", fragments.size() == 1); //$NON-NLS-1$
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(0);
		IVariableBinding variableBinding = fragment.resolveBinding();
		assertEquals("return type is not Number", "Number", variableBinding.getType().getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(variableBinding == binding);
	}
//
//	/**
//	 * SuperReference
//	 */
//	public void test0178() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0178", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode node2 = getASTNode((JavaScriptUnit) result, 1, 0, 0);
//		assertTrue("Return statement", node2 instanceof ReturnStatement); //$NON-NLS-1$
//		ReturnStatement returnStatement = (ReturnStatement) node2;
//		Expression expr = returnStatement.getExpression();
//		assertTrue("Not a field access", expr instanceof SuperFieldAccess); //$NON-NLS-1$
//		SuperFieldAccess fieldAccess = (SuperFieldAccess) expr;
//		ITypeBinding typeBinding = fieldAccess.resolveTypeBinding();
//		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
//		assertTrue("Not a primitive type", typeBinding.isPrimitive()); //$NON-NLS-1$
//		assertEquals("Not int", "int", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}	
	
	/**
	 * Allocation expression
	 */
	public void test0179() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0179", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertTrue("VariableDeclarationStatement", node2 instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node2;
		List fragments = variableDeclarationStatement.fragments();
		assertTrue("No fragment", fragments.size() == 1); //$NON-NLS-1$
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(0);
		IVariableBinding variableBinding = fragment.resolveBinding();
		Expression initialization = fragment.getInitializer();
		ITypeBinding typeBinding = initialization.resolveTypeBinding();
		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
		assertTrue(variableBinding.getType() == typeBinding);
	}	

//	/**
//	 * Allocation expression
//	 */
//	public void test0180() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0180", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertTrue("VariableDeclarationStatement", node2 instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node2;
//		List fragments = variableDeclarationStatement.fragments();
//		assertTrue("No fragment", fragments.size() == 1); //$NON-NLS-1$
//		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(0);
//		IVariableBinding variableBinding = fragment.resolveBinding();
//		Expression initialization = fragment.getInitializer();
//		assertTrue("No an array creation", initialization instanceof ArrayCreation); //$NON-NLS-1$
//		ITypeBinding typeBinding = initialization.resolveTypeBinding();
//		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
//		assertTrue("Not an array", typeBinding.isArray()); //$NON-NLS-1$
//		assertTrue(variableBinding.getType() == typeBinding);
//	}	
//
//	/**
//	 * Allocation expression
//	 */
//	public void test0181() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0181", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertTrue("VariableDeclarationStatement", node2 instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node2;
//		List fragments = variableDeclarationStatement.fragments();
//		assertTrue("No fragment", fragments.size() == 1); //$NON-NLS-1$
//		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(0);
//		IVariableBinding variableBinding = fragment.resolveBinding();
//		Expression initialization = fragment.getInitializer();
//		ITypeBinding typeBinding = initialization.resolveTypeBinding();
//		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
//		assertTrue("Not an array", typeBinding.isArray()); //$NON-NLS-1$
//		assertTrue(variableBinding.getType() == typeBinding);
//	}	

	/**
	 * BinaryExpression
	 */
	public void Xtest0182() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0182", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertTrue("IfStatement", node2 instanceof IfStatement); //$NON-NLS-1$
		IfStatement ifStatement = (IfStatement) node2;
		Expression expr = ifStatement.getExpression();
		assertNotNull("No condition", expr); //$NON-NLS-1$
		ITypeBinding typeBinding = expr.resolveTypeBinding();
		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
		assertEquals("Not a boolean", "boolean", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		checkSourceRange(expr, "i < 10", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression
	 */
	public void Xtest0183() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0183", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertTrue("IfStatement", node2 instanceof IfStatement); //$NON-NLS-1$
		IfStatement ifStatement = (IfStatement) node2;
		Expression expr = ifStatement.getExpression();
		assertNotNull("No condition", expr); //$NON-NLS-1$
		ITypeBinding typeBinding = expr.resolveTypeBinding();
		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
		assertEquals("Not a boolean", "boolean", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		checkSourceRange(expr, "i < 10 && i < 20", source); //$NON-NLS-1$
	}	
	
	/**
	 * BinaryExpression
	 */
	public void Xtest0184() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0184", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertTrue("IfStatement", node2 instanceof IfStatement); //$NON-NLS-1$
		IfStatement ifStatement = (IfStatement) node2;
		Expression expr = ifStatement.getExpression();
		assertNotNull("No condition", expr); //$NON-NLS-1$
		ITypeBinding typeBinding = expr.resolveTypeBinding();
		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
		assertEquals("Not a boolean", "boolean", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		checkSourceRange(expr, "i < 10 || i < 20", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression
	 */
	public void Xtest0185() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0185", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertTrue("IfStatement", node2 instanceof IfStatement); //$NON-NLS-1$
		IfStatement ifStatement = (IfStatement) node2;
		Expression expr = ifStatement.getExpression();
		assertNotNull("No condition", expr); //$NON-NLS-1$
		ITypeBinding typeBinding = expr.resolveTypeBinding();
		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
		assertEquals("Not a boolean", "boolean", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		checkSourceRange(expr, "i == 10", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression
	 */
	public void Xtest0186() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0186", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertTrue("IfStatement", node2 instanceof IfStatement); //$NON-NLS-1$
		IfStatement ifStatement = (IfStatement) node2;
		Expression expr = ifStatement.getExpression();
		assertNotNull("No condition", expr); //$NON-NLS-1$
		ITypeBinding typeBinding = expr.resolveTypeBinding();
		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
		assertEquals("Not a boolean", "boolean", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		checkSourceRange(expr, "o == o", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression
	 */
	public void Xtest0187() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0187", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertTrue("IfStatement", node2 instanceof WhileStatement); //$NON-NLS-1$
		WhileStatement whileStatement = (WhileStatement) node2;
		Expression expr = whileStatement.getExpression();
		assertNotNull("No condition", expr); //$NON-NLS-1$
		ITypeBinding typeBinding = expr.resolveTypeBinding();
		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
		assertEquals("Not a boolean", "boolean", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		checkSourceRange(expr, "i <= 10", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression
	 */
	public void Xtest0188() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0188", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 2);
		assertTrue("DoStatement", node2 instanceof DoStatement); //$NON-NLS-1$
		DoStatement statement = (DoStatement) node2;
		Expression expr = statement.getExpression();
		assertNotNull("No condition", expr); //$NON-NLS-1$
		ITypeBinding typeBinding = expr.resolveTypeBinding();
		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
		assertEquals("Not a boolean", "boolean", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		checkSourceRange(expr, "i <= 10", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression
	 */
	public void Xtest0189() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0189", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertTrue("ForStatement", node2 instanceof ForStatement); //$NON-NLS-1$
		ForStatement statement = (ForStatement) node2;
		Expression expr = statement.getExpression();
		assertNotNull("No condition", expr); //$NON-NLS-1$
		ITypeBinding typeBinding = expr.resolveTypeBinding();
		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
		assertEquals("Not a boolean", "boolean", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		checkSourceRange(expr, "i < 10", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression
	 */
	public void Xtest0190() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0190", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 2, 1);
		assertTrue("IfStatement", node2 instanceof IfStatement); //$NON-NLS-1$
		IfStatement statement = (IfStatement) node2;
		Expression expr = statement.getExpression();
		assertNotNull("No condition", expr); //$NON-NLS-1$
		ITypeBinding typeBinding = expr.resolveTypeBinding();
		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
		assertEquals("Not a boolean", "boolean", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		checkSourceRange(expr, "scanner.x < selection.start && selection.start < scanner.y", source); //$NON-NLS-1$
	}	

	/**
	 * BinaryExpression
	 */
	public void Xtest0191() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0191", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertTrue("ExpressionStatement", node2 instanceof ExpressionStatement); //$NON-NLS-1$
		ExpressionStatement expressionStatement = (ExpressionStatement) node2;
		Expression ex = expressionStatement.getExpression();
		assertTrue("Assignment", ex instanceof Assignment); //$NON-NLS-1$
		Assignment statement = (Assignment) ex;
		Expression rightExpr = statement.getRightHandSide();
		assertTrue("Not an infix expression", rightExpr instanceof InfixExpression); //$NON-NLS-1$
		InfixExpression infixExpression = (InfixExpression) rightExpr;
		Expression expr = infixExpression.getRightOperand();
		assertNotNull("No right hand side expression", expr); //$NON-NLS-1$
		ITypeBinding typeBinding = expr.resolveTypeBinding();
		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
		assertEquals("Not a boolean", "boolean", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		checkSourceRange(expr, "2 < 20", source); //$NON-NLS-1$
	}	

	/**
	 * Initializer
	 */
	public void test0192() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0192", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertTrue("VariableDeclarationStatement", node2 instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node2;
		List fragments = variableDeclarationStatement.fragments();
		assertTrue("No fragment", fragments.size() == 1); //$NON-NLS-1$
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(0);
		IVariableBinding variableBinding = fragment.resolveBinding();
		Expression initialization = fragment.getInitializer();
		ITypeBinding typeBinding = initialization.resolveTypeBinding();
		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
		assertTrue(variableBinding.getType() == typeBinding);
		checkSourceRange(initialization, "0", source); //$NON-NLS-1$
	}	

//	/**
//	 * Initializer
//	 */
//	public void test0193() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0193", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 1, 0);
//		assertTrue("VariableDeclarationStatement", node2 instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node2;
//		List fragments = variableDeclarationStatement.fragments();
//		assertTrue("No fragment", fragments.size() == 1); //$NON-NLS-1$
//		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(0);
//		IVariableBinding variableBinding = fragment.resolveBinding();
//		Expression initialization = fragment.getInitializer();
//		ITypeBinding typeBinding = initialization.resolveTypeBinding();
//		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
//		assertTrue(variableBinding.getType() == typeBinding);
//		checkSourceRange(initialization, "new Inner()", source); //$NON-NLS-1$
//		assertEquals("Wrong type", "Inner", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}	
//
//	/**
//	 * Initializer
//	 */
//	public void test0194() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0194", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 1, 0);
//		assertTrue("VariableDeclarationStatement", node2 instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node2;
//		List fragments = variableDeclarationStatement.fragments();
//		assertTrue("No fragment", fragments.size() == 1); //$NON-NLS-1$
//		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(0);
//		IVariableBinding variableBinding = fragment.resolveBinding();
//		Expression initialization = fragment.getInitializer();
//		ITypeBinding typeBinding = initialization.resolveTypeBinding();
//		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
//		assertTrue(variableBinding.getType() == typeBinding);
//		checkSourceRange(initialization, "new Inner[10]", source); //$NON-NLS-1$
//		assertTrue("Not an array", typeBinding.isArray()); //$NON-NLS-1$
//		assertEquals("Wrong type", "Inner[]", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}	

//	/**
//	 * Initializer
//	 */
//	public void test0195() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0195", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode node2 = getASTNode((JavaScriptUnit) result, 1, 0, 1);
//		assertTrue("ExpressionStatement", node2 instanceof ExpressionStatement); //$NON-NLS-1$
//		ExpressionStatement expressionStatement = (ExpressionStatement) node2;
//		Expression ex = expressionStatement.getExpression();
//		assertTrue("FunctionInvocation", ex instanceof FunctionInvocation); //$NON-NLS-1$
//		FunctionInvocation methodInvocation = (FunctionInvocation) ex;
//		checkSourceRange(methodInvocation, "a.useFile(/*]*/a.getFile()/*[*/)", source); //$NON-NLS-1$
//		List list = methodInvocation.arguments();
//		assertTrue("Parameter list not empty", list.size() == 1); //$NON-NLS-1$
//		Expression parameter = (Expression) list.get(0);
//		assertTrue("Not a method invocation", parameter instanceof FunctionInvocation); //$NON-NLS-1$
//		ITypeBinding typeBinding = parameter.resolveTypeBinding();
//		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
//		assertEquals("Not a boolean", "File", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		checkSourceRange(parameter, "a.getFile()", source); //$NON-NLS-1$
//	}	

//	/**
//	 * Initializer
//	 */
//	public void test0196() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0196", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 1, 2);
//		assertTrue("ExpressionStatement", node2 instanceof ExpressionStatement); //$NON-NLS-1$
//		ExpressionStatement expressionStatement = (ExpressionStatement) node2;
//		Expression ex = expressionStatement.getExpression();
//		assertTrue("Assignment", ex instanceof Assignment); //$NON-NLS-1$
//		Assignment statement = (Assignment) ex;
//		Expression rightExpr = statement.getRightHandSide();
//		assertTrue("Not an instanceof expression", rightExpr instanceof InstanceofExpression); //$NON-NLS-1$
//		ITypeBinding typeBinding = rightExpr.resolveTypeBinding();
//		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
//		assertEquals("wrong type", "boolean", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		checkSourceRange(rightExpr, "inner instanceof Inner", source); //$NON-NLS-1$
//	}	
//
//	/**
//	 * Initializer
//	 */
//	public void test0197() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0197", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertEquals("Not a compilation unit", ASTNode.JAVASCRIPT_UNIT, result.getNodeType());
//		JavaScriptUnit unit = (JavaScriptUnit) result;
//		assertProblemsSize(unit, 0); 
//		ASTNode node2 = getASTNode(unit, 1, 0, 1);
//		assertTrue("ExpressionStatement", node2 instanceof ExpressionStatement); //$NON-NLS-1$
//		ExpressionStatement expressionStatement = (ExpressionStatement) node2;
//		Expression ex = expressionStatement.getExpression();
//		assertTrue("FunctionInvocation", ex instanceof FunctionInvocation); //$NON-NLS-1$
//		FunctionInvocation methodInvocation = (FunctionInvocation) ex;
//		checkSourceRange(methodInvocation, "a.getFile()/*[*/.getName()", source); //$NON-NLS-1$
//		Expression receiver = methodInvocation.getExpression();
//		assertTrue("Not a method invocation", receiver instanceof FunctionInvocation); //$NON-NLS-1$
//		FunctionInvocation methodInvocation2 = (FunctionInvocation) receiver;
//		ITypeBinding typeBinding = methodInvocation2.resolveTypeBinding();
//		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "File", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		checkSourceRange(methodInvocation2, "a.getFile()", source); //$NON-NLS-1$
//	}	

	/**
	 * Initializer
	 */
	public void Xtest0198() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0198", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertTrue("ReturnStatement", node2 instanceof ReturnStatement); //$NON-NLS-1$
		ReturnStatement returnStatement = (ReturnStatement) node2;
		Expression expr = returnStatement.getExpression();
		assertTrue("Not an infixExpression", expr instanceof InfixExpression); //$NON-NLS-1$
		InfixExpression infixExpression = (InfixExpression) expr;
		Expression left = infixExpression.getLeftOperand();
		assertTrue("Not an InfixExpression", left instanceof InfixExpression); //$NON-NLS-1$
		InfixExpression infixExpression2 = (InfixExpression) left;
		Expression right = infixExpression2.getRightOperand();
		assertTrue("Not an InfixExpression", right instanceof InfixExpression); //$NON-NLS-1$
		InfixExpression infixExpression3 = (InfixExpression) right;
		assertEquals("A multiplication", InfixExpression.Operator.TIMES, infixExpression3.getOperator()); //$NON-NLS-1$
		ITypeBinding typeBinding = infixExpression3.resolveTypeBinding();
		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
		assertEquals("Not Number", "Number", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		checkSourceRange(infixExpression3, "20 * 30", source); //$NON-NLS-1$
	}	

	/**
	 * Initializer
	 */
	public void Xtest0199() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0199", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertTrue("VariableDeclarationStatement", node2 instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node2;
		List fragments = variableDeclarationStatement.fragments();
		assertTrue("No fragment", fragments.size() == 1); //$NON-NLS-1$
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(0);
		Expression initialization = fragment.getInitializer();
		assertTrue("Not an infixExpression", initialization instanceof InfixExpression); //$NON-NLS-1$
		InfixExpression infixExpression = (InfixExpression) initialization;
		Expression left = infixExpression.getLeftOperand();
		assertTrue("Not an InfixExpression", left instanceof InfixExpression); //$NON-NLS-1$
		InfixExpression infixExpression2 = (InfixExpression) left;
		Expression right = infixExpression2.getRightOperand();
		assertTrue("Not an InfixExpression", right instanceof InfixExpression); //$NON-NLS-1$
		InfixExpression infixExpression3 = (InfixExpression) right;
		assertEquals("A multiplication", InfixExpression.Operator.TIMES, infixExpression3.getOperator()); //$NON-NLS-1$
		ITypeBinding typeBinding = infixExpression3.resolveTypeBinding();
		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
		assertEquals("Not Number", "Number", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		checkSourceRange(infixExpression3, "10 * 30", source); //$NON-NLS-1$
	}	
//
//	/**
//	 * Initializer
//	 */
//	public void test0200() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0200", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode node2 = getASTNode((JavaScriptUnit) result, 1, 0, 0);
//		assertTrue("VariableDeclarationStatement", node2 instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node2;
//		List fragments = variableDeclarationStatement.fragments();
//		assertTrue("No fragment", fragments.size() == 1); //$NON-NLS-1$
//		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(0);
//		Expression initialization = fragment.getInitializer();
//		assertTrue("Not an infixExpression", initialization instanceof FieldAccess); //$NON-NLS-1$
//		FieldAccess fieldAccess = (FieldAccess) initialization;
//		Expression receiver = fieldAccess.getExpression();
//		assertTrue("ArrayCreation", receiver instanceof ArrayCreation); //$NON-NLS-1$
//		ArrayCreation arrayCreation = (ArrayCreation) receiver;
//		List dimensions = arrayCreation.dimensions();
//		assertEquals("Wrong dimension", 1, dimensions.size()); //$NON-NLS-1$
//		Expression dim = (Expression) dimensions.get(0);
//		assertTrue("InfixExpression", dim instanceof InfixExpression); //$NON-NLS-1$
//		InfixExpression infixExpression = (InfixExpression) dim;
//		ITypeBinding typeBinding = infixExpression.resolveTypeBinding();
//		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
//		assertEquals("Not int", "int", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		checkSourceRange(infixExpression, "1 + 2", source); //$NON-NLS-1$
//	}	

	/**
	 * Position inside for statement: PR 3300
	 */
	public void test0201() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0201", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertTrue("ForStatement", node2 instanceof ForStatement); //$NON-NLS-1$
		ForStatement forStatement = (ForStatement) node2;
		List initializers = forStatement.initializers();
		assertTrue("wrong size", initializers.size() == 1); //$NON-NLS-1$
		Expression init = (Expression) initializers.get(0);
		checkSourceRange(init, "var i= 0", source); //$NON-NLS-1$
	}
	
	/**
	 * PR 7386
	 */
	public void test0202() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0202", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0);
		assertTrue("VariableDeclarationStatement", node2 instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node2;
		checkSourceRange(fieldDeclaration, "var f= (2);", source); //$NON-NLS-1$
		List fragments = fieldDeclaration.fragments();
		assertEquals("wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(0);
		Expression initialization = fragment.getInitializer();
		assertTrue("Not a parenthesized expression", initialization instanceof ParenthesizedExpression); //$NON-NLS-1$
		checkSourceRange(initialization, "(2)", source); //$NON-NLS-1$
		ITypeBinding typeBinding = initialization.resolveTypeBinding();
		assertNotNull("no binding", typeBinding); //$NON-NLS-1$
		assertEquals("not Number", "Number", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}		

	/**
	 * PR 7386
	 */
	public void test0203() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0203", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0);
		assertTrue("VariableDeclarationStatement", node2 instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node2;
		checkSourceRange(fieldDeclaration, "var f= (2);", source); //$NON-NLS-1$
		List fragments = fieldDeclaration.fragments();
		assertEquals("wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(0);
		Expression initialization = fragment.getInitializer();
		assertTrue("Not a parenthesized expression", initialization instanceof ParenthesizedExpression); //$NON-NLS-1$
		ParenthesizedExpression parenthesizedExpression = (ParenthesizedExpression) initialization;
		checkSourceRange(parenthesizedExpression, "(2)", source); //$NON-NLS-1$
		Expression expr = parenthesizedExpression.getExpression();
		checkSourceRange(expr, "2", source); //$NON-NLS-1$
		ITypeBinding typeBinding = expr.resolveTypeBinding();
		assertNotNull("no binding", typeBinding); //$NON-NLS-1$
		assertEquals("not Number", "Number", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertTrue("type binding is canonical", typeBinding == parenthesizedExpression.resolveTypeBinding()); //$NON-NLS-1$
	}		

	/**
	 * PR 7386
	 */
	public void test0204() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0204", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0);
		assertTrue("VariableDeclarationStatement", node2 instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node2;
		checkSourceRange(fieldDeclaration, "var f= ((2));", source); //$NON-NLS-1$
		List fragments = fieldDeclaration.fragments();
		assertEquals("wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(0);
		Expression initialization = fragment.getInitializer();
		assertTrue("Not a parenthesized expression", initialization instanceof ParenthesizedExpression); //$NON-NLS-1$
		ParenthesizedExpression parenthesizedExpression = (ParenthesizedExpression) initialization;
		checkSourceRange(parenthesizedExpression, "((2))", source); //$NON-NLS-1$
		Expression expr = parenthesizedExpression.getExpression();
		assertTrue("Not a parenthesized expression", expr instanceof ParenthesizedExpression); //$NON-NLS-1$
		ParenthesizedExpression parenthesizedExpression2 = (ParenthesizedExpression) expr;
		checkSourceRange(parenthesizedExpression2, "(2)", source); //$NON-NLS-1$
		expr = parenthesizedExpression2.getExpression();
		checkSourceRange(expr, "2", source); //$NON-NLS-1$
		ITypeBinding typeBinding = expr.resolveTypeBinding();
		assertNotNull("no binding", typeBinding); //$NON-NLS-1$
		assertEquals("not Number", "Number", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		typeBinding = parenthesizedExpression.resolveTypeBinding();
		assertNotNull("no binding", typeBinding); //$NON-NLS-1$
		assertEquals("not Number", "Number", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertTrue("type binding is canonical", typeBinding == parenthesizedExpression2.resolveTypeBinding()); //$NON-NLS-1$
	}		


//	/**
//	 * Local class end position when trailing comment
//	 * @deprecated using deprecated code
//	 */
//	public void test0205() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0205", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertTrue("TypeDeclarationStatement", node2 instanceof TypeDeclarationStatement); //$NON-NLS-1$
//		TypeDeclarationStatement typeDeclarationStatement = (TypeDeclarationStatement) node2;
//		TypeDeclaration typeDeclaration = typeDeclarationStatement.getTypeDeclaration();
//		assertEquals("wrong name", "AA", typeDeclaration.getName().getIdentifier()); //$NON-NLS-1$ //$NON-NLS-2$
//		checkSourceRange(typeDeclaration, "class AA extends Test {}", source); //$NON-NLS-1$
//	}		

//	/**
//	 * QualifiedName
//	 */
//	public void test0206() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0206", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 5, 0);
//		assertTrue("ReturnStatement", node2 instanceof ReturnStatement); //$NON-NLS-1$
//		ReturnStatement returnStatement = (ReturnStatement) node2;
//		Expression expr = returnStatement.getExpression();
//		assertTrue("Not a qualifiedName", expr instanceof QualifiedName); //$NON-NLS-1$
//		QualifiedName qualifiedName = (QualifiedName) expr;
//		ITypeBinding typeBinding = expr.resolveTypeBinding();
//		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
//		assertEquals("Not an int (typeBinding)", "int", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		checkSourceRange(qualifiedName, "field1.field2.field3.field4.i", source); //$NON-NLS-1$
//		assertTrue("Not a simple name", qualifiedName.getName().isSimpleName()); //$NON-NLS-1$
//		SimpleName simpleName = qualifiedName.getName();
//		assertTrue("a declaration", !simpleName.isDeclaration()); //$NON-NLS-1$
//		checkSourceRange(simpleName, "i", source); //$NON-NLS-1$
//		ITypeBinding typeBinding2 = simpleName.resolveTypeBinding();
//		assertNotNull("No typebinding2", typeBinding2); //$NON-NLS-1$
//		assertEquals("Not an int (typeBinding2)", "int", typeBinding2.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		IBinding binding = simpleName.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertTrue("VariableBinding", binding instanceof IVariableBinding); //$NON-NLS-1$
//		IVariableBinding variableBinding = (IVariableBinding) binding;
//		assertEquals("Not Test", "Test", variableBinding.getDeclaringClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertEquals("Not default", Modifier.PUBLIC, variableBinding.getModifiers()); //$NON-NLS-1$
//		Name qualifierName = qualifiedName.getQualifier();
//		assertTrue("Not a qualified name", qualifierName.isQualifiedName()); //$NON-NLS-1$
//		checkSourceRange(qualifierName, "field1.field2.field3.field4", source); //$NON-NLS-1$
//		ITypeBinding typeBinding5 = qualifierName.resolveTypeBinding();
//		assertNotNull("No binding5", typeBinding5); //$NON-NLS-1$
//		assertEquals("Not Test", "Test", typeBinding5.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//
//		qualifiedName = (QualifiedName) qualifierName;
//		simpleName = qualifiedName.getName();
//		checkSourceRange(simpleName, "field4", source); //$NON-NLS-1$
//		ITypeBinding typeBinding6 = simpleName.resolveTypeBinding();
//		assertNotNull("No binding6", typeBinding6); //$NON-NLS-1$
//		assertEquals("Not Test", "Test", typeBinding6.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		
//		qualifierName = qualifiedName.getQualifier();
//		assertTrue("Not a qualified name", qualifierName.isQualifiedName()); //$NON-NLS-1$
//		checkSourceRange(qualifierName, "field1.field2.field3", source); //$NON-NLS-1$
//		ITypeBinding typeBinding7 = qualifierName.resolveTypeBinding();
//		assertNotNull("No binding7", typeBinding7); //$NON-NLS-1$
//		assertEquals("Not Test", "Test", typeBinding7.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		
//		qualifiedName = (QualifiedName) qualifierName;
//		simpleName = qualifiedName.getName();
//		checkSourceRange(simpleName, "field3", source); //$NON-NLS-1$
//		qualifierName = qualifiedName.getQualifier();
//		assertTrue("Not a qualified name", qualifierName.isQualifiedName()); //$NON-NLS-1$
//		checkSourceRange(qualifierName, "field1.field2", source); //$NON-NLS-1$
//		ITypeBinding typeBinding3 = qualifierName.resolveTypeBinding();
//		assertNotNull("No binding3", typeBinding3); //$NON-NLS-1$
//		assertEquals("Not Test", "Test", typeBinding3.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		qualifiedName = (QualifiedName) qualifierName;
//		simpleName = qualifiedName.getName();
//		checkSourceRange(simpleName, "field2", source); //$NON-NLS-1$
//		qualifierName = qualifiedName.getQualifier();
//		assertTrue("Not a simple name", qualifierName.isSimpleName()); //$NON-NLS-1$
//		assertTrue("a declaration", !((SimpleName)qualifierName).isDeclaration()); //$NON-NLS-1$
//		checkSourceRange(qualifierName, "field1", source); //$NON-NLS-1$
//		ITypeBinding typeBinding4 = qualifierName.resolveTypeBinding();
//		assertNotNull("No binding4", typeBinding4); //$NON-NLS-1$
//		assertEquals("Not Test", "Test", typeBinding4.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}
//
	/**
	 * Check javadoc for FunctionDeclaration
	 * @deprecated marking deprecated since using deprecated code
	 */
	public void test0207() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0207", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("The node is not a FunctionDeclaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
		JSdoc actualJavadoc = ((FunctionDeclaration) node).getJavadoc();
		JSdoc javadoc = this.ast.newJSdoc();
		javadoc.setComment("/** JavaDoc Comment*/");//$NON-NLS-1$*/
		assertTrue("Both AST trees should be identical", javadoc.subtreeMatch(new ASTMatcher(), actualJavadoc));//$NON-NLS-1$
		checkSourceRange(node, "/** JavaDoc Comment*/\n function foo( i) {}", source); //$NON-NLS-1$
		checkSourceRange(actualJavadoc, "/** JavaDoc Comment*/", source); //$NON-NLS-1$
	}
	
	/**
	 * Check javadoc for FunctionDeclaration
	 */
	public void test0208() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0208", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("The node is not a FunctionDeclaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
		JSdoc actualJavadoc = ((FunctionDeclaration) node).getJavadoc();
		assertTrue("Javadoc must be null", actualJavadoc == null);//$NON-NLS-1$
		checkSourceRange(node, "function foo( i) {}", source); //$NON-NLS-1$
	}

	/**
	 * Check javadoc for FunctionDeclaration
	 */
	public void test0209() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0209", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("The node is not a FunctionDeclaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
		JSdoc actualJavadoc = ((FunctionDeclaration) node).getJavadoc();
		assertTrue("Javadoc must be null", actualJavadoc == null);//$NON-NLS-1$
		checkSourceRange(node, "function foo( i) {}", source); //$NON-NLS-1$
	}

	/**
	 * Check javadoc for VariableDeclarationStatement
	 * @deprecated marking deprecated since using deprecated code
	 */
	public void test0210() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0210", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("The node is not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		JSdoc actualJavadoc = ((VariableDeclarationStatement) node).getJavadoc();
		JSdoc javadoc = this.ast.newJSdoc();
		javadoc.setComment("/** JavaDoc Comment*/");//$NON-NLS-1$*/
		assertTrue("Both AST trees should be identical", javadoc.subtreeMatch(new ASTMatcher(), actualJavadoc));//$NON-NLS-1$
		checkSourceRange(node, "/** JavaDoc Comment*/\n  var i;", source); //$NON-NLS-1$
	}

	/**
	 * Check javadoc for VariableDeclarationStatement
	 */
	public void test0211() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0211", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("The node is not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		JSdoc actualJavadoc = ((VariableDeclarationStatement) node).getJavadoc();
		assertTrue("Javadoc must be null", actualJavadoc == null);//$NON-NLS-1$
		checkSourceRange(node, "var i;", source); //$NON-NLS-1$
	}

	/**
	 * Check javadoc for VariableDeclarationStatement
	 */
	public void test0212() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0212", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("The node is not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		JSdoc actualJavadoc = ((VariableDeclarationStatement) node).getJavadoc();
		assertTrue("Javadoc must be null", actualJavadoc == null);//$NON-NLS-1$
		checkSourceRange(node, "var i;", source); //$NON-NLS-1$
	}

	/**
	 * Check javadoc for TypeDeclaration
	 */
	public void test0213() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0213", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0,0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		assertTrue("The node is not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		JSdoc actualJavadoc = ((VariableDeclarationStatement) node).getJavadoc();
		assertTrue("Javadoc must be null", actualJavadoc == null);//$NON-NLS-1$
		String expectedContents = "" +//$NON-NLS-1$
			"var i;"  +//$NON-NLS-1$
			"";//$NON-NLS-1$
		checkSourceRange(node, expectedContents, source); //$NON-NLS-1$	}	
	}

//	/**
//	 * Check javadoc for TypeDeclaration
//	 */
//	public void test0214() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0214", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("The node is not a TypeDeclaration", node instanceof TypeDeclaration); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((TypeDeclaration) node).getJavadoc();
//		assertTrue("Javadoc must be null", actualJavadoc == null);//$NON-NLS-1$
//		String expectedContents = "public class Test {\n" +//$NON-NLS-1$
//			"  int i;\n"  +//$NON-NLS-1$
//			"}";//$NON-NLS-1$
//		checkSourceRange(node, expectedContents, source); //$NON-NLS-1$
//	}
//
//	/**
//	 * Check javadoc for TypeDeclaration
//	 * @deprecated marking deprecated since using deprecated code
//	 */
//	public void test0215() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0215", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("The node is not a TypeDeclaration", node instanceof TypeDeclaration); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((TypeDeclaration) node).getJavadoc();
//		Javadoc javadoc = this.ast.newJavadoc();
//		javadoc.setComment("/** JavaDoc Comment*/");//$NON-NLS-1$*/
//		assertTrue("Both AST trees should be identical", javadoc.subtreeMatch(new ASTMatcher(), actualJavadoc));//$NON-NLS-1$
//		String expectedContents = 
//			 "/** JavaDoc Comment*/\n" + //$NON-NLS-1$
//			"public class Test {\n" +//$NON-NLS-1$
//			"  int i;\n"  +//$NON-NLS-1$
//			"}";//$NON-NLS-1$
//		checkSourceRange(node, expectedContents, source); //$NON-NLS-1$
//		checkSourceRange(actualJavadoc, "/** JavaDoc Comment*/", source); //$NON-NLS-1$
//	}

//	/**
//	 * Check javadoc for MemberTypeDeclaration
//	 * @deprecated marking deprecated since using deprecated code
//	 */
//	public void test0216() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0216", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("The node is not a TypeDeclaration", node instanceof TypeDeclaration); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((TypeDeclaration) node).getJavadoc();
//		Javadoc javadoc = this.ast.newJavadoc();
//		javadoc.setComment("/** JavaDoc Comment*/");//$NON-NLS-1$*/
//		assertTrue("Both AST trees should be identical", javadoc.subtreeMatch(new ASTMatcher(), actualJavadoc));//$NON-NLS-1$
//		String expectedContents = 
//			 "/** JavaDoc Comment*/\n" + //$NON-NLS-1$
//			 "  class B {}";//$NON-NLS-1$
//		checkSourceRange(node, expectedContents, source); //$NON-NLS-1$
//		checkSourceRange(actualJavadoc, "/** JavaDoc Comment*/", source); //$NON-NLS-1$
//	}
//
//	/**
//	 * Check javadoc for MemberTypeDeclaration
//	 */
//	public void test0217() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0217", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("The node is not a TypeDeclaration", node instanceof TypeDeclaration); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((TypeDeclaration) node).getJavadoc();
//		assertTrue("Javadoc must be null", actualJavadoc == null);//$NON-NLS-1$
//		checkSourceRange(node, "class B {}", source); //$NON-NLS-1$
//	}
//
//	/**
//	 * Check javadoc for MemberTypeDeclaration
//	 */
//	public void test0218() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0218", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("The node is not a TypeDeclaration", node instanceof TypeDeclaration); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((TypeDeclaration) node).getJavadoc();
//		assertTrue("Javadoc must be null", actualJavadoc == null);//$NON-NLS-1$
//		checkSourceRange(node, "public static class B {}", source); //$NON-NLS-1$
//	}

//	/**
//	 * Check javadoc for MemberTypeDeclaration
//	 */
//	public void test0219() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0219", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		assertTrue("The node is not a TypeDeclaration", node instanceof TypeDeclaration); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((TypeDeclaration) node).getJavadoc();
//		assertTrue("Javadoc must be null", actualJavadoc == null);//$NON-NLS-1$
//		checkSourceRange(node, "public static class B {}", source); //$NON-NLS-1$
//	}
//
//	/**
//	 * Checking initializers
//	 */
//	public void test0220() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0220", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		checkSourceRange(node, "{}", source); //$NON-NLS-1$
//	}

//	/**
//	 * Checking initializers
//	 */
//	public void test0221() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0221", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		checkSourceRange(node, "static {}", source); //$NON-NLS-1$
//	}

//	/**
//	 * Checking initializers
//	 * @deprecated marking deprecated since using deprecated code
//	 */
//	public void test0222() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0222", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((Initializer) node).getJavadoc();
//		assertNotNull("Javadoc comment should no be null", actualJavadoc); //$NON-NLS-1$
//		Javadoc javadoc = this.ast.newJavadoc();
//		javadoc.setComment("/** JavaDoc Comment*/");//$NON-NLS-1$*/
//		assertTrue("Both AST trees should be identical", javadoc.subtreeMatch(new ASTMatcher(), actualJavadoc));//$NON-NLS-1$
//		String expectedContents = 
//			 "/** JavaDoc Comment*/\n" + //$NON-NLS-1$
//			 "  static {}";//$NON-NLS-1$
//		checkSourceRange(node, expectedContents, source); //$NON-NLS-1$
//		checkSourceRange(actualJavadoc, "/** JavaDoc Comment*/", source); //$NON-NLS-1$
//		
//	}

//	/**
//	 * Checking initializers
//	 * @deprecated marking deprecated since using deprecated code
//	 */
//	public void test0223() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0223", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((Initializer) node).getJavadoc();
//		assertNotNull("Javadoc comment should not be null", actualJavadoc); //$NON-NLS-1$
//		Javadoc javadoc = this.ast.newJavadoc();
//		javadoc.setComment("/** JavaDoc Comment*/");//$NON-NLS-1$*/
//		assertTrue("Both AST trees should be identical", javadoc.subtreeMatch(new ASTMatcher(), actualJavadoc));//$NON-NLS-1$
//		String expectedContents = 
//			 "/** JavaDoc Comment*/\n" + //$NON-NLS-1$
//			 "  {}";//$NON-NLS-1$
//		checkSourceRange(node, expectedContents, source); //$NON-NLS-1$
//		checkSourceRange(actualJavadoc, "/** JavaDoc Comment*/", source); //$NON-NLS-1$
//		
//	}

//	/**
//	 * Checking initializers
//	 */
//	public void test0224() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0224", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
//		Javadoc actualJavadoc = ((Initializer) node).getJavadoc();
//		assertNull("Javadoc comment should be null", actualJavadoc); //$NON-NLS-1$
//		checkSourceRange(node, "{}", source); //$NON-NLS-1$
//	}

	/**
	 * Continue ==> ContinueStatement
	 */
	public void test0225() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0225", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		LabeledStatement labeledStatement = (LabeledStatement) getASTNode((JavaScriptUnit) result, 0, 0, 0);
		checkSourceRange(labeledStatement.getLabel(), "label", source); //$NON-NLS-1$
		ForStatement forStatement = (ForStatement) labeledStatement.getBody();
		ContinueStatement statement = (ContinueStatement) ((Block) forStatement.getBody()).statements().get(0);
		assertNotNull("Expression should not be null", statement); //$NON-NLS-1$
		ContinueStatement continueStatement = this.ast.newContinueStatement();
		continueStatement.setLabel(this.ast.newSimpleName("label")); //$NON-NLS-1$
		assertTrue("Both AST trees should be identical", continueStatement.subtreeMatch(new ASTMatcher(), statement));		//$NON-NLS-1$
		checkSourceRange(statement, "continue label;", source); //$NON-NLS-1$
		checkSourceRange(statement.getLabel(), "label", source); //$NON-NLS-1$
	}
		
	/**
	 * Break + label  ==> BreakStatement
	 */
	public void test0226() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0226", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		LabeledStatement labeledStatement = (LabeledStatement) getASTNode((JavaScriptUnit) result, 0, 0, 0);
		checkSourceRange(labeledStatement.getLabel(), "label", source); //$NON-NLS-1$
		ForStatement forStatement = (ForStatement) labeledStatement.getBody();
		BreakStatement statement = (BreakStatement) ((Block) forStatement.getBody()).statements().get(0);
		assertNotNull("Expression should not be null", statement); //$NON-NLS-1$
		BreakStatement breakStatement = this.ast.newBreakStatement();
		breakStatement.setLabel(this.ast.newSimpleName("label")); //$NON-NLS-1$
		assertTrue("Both AST trees should be identical", breakStatement.subtreeMatch(new ASTMatcher(), statement));		//$NON-NLS-1$
		checkSourceRange(statement, "break label;", source); //$NON-NLS-1$
		checkSourceRange(statement.getLabel(), "label", source); //$NON-NLS-1$
	}

//	/**
//	 * QualifiedName
//	 */
//	public void test0227() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0227", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode node2 = getASTNode((JavaScriptUnit) result, 3, 2, 0);
//		assertTrue("ReturnStatement", node2 instanceof ReturnStatement); //$NON-NLS-1$
//		ReturnStatement returnStatement = (ReturnStatement) node2;
//		Expression expr = returnStatement.getExpression();
//		assertTrue("Not a qualifiedName", expr instanceof QualifiedName); //$NON-NLS-1$
//		QualifiedName qualifiedName = (QualifiedName) expr;
//		ITypeBinding typeBinding = expr.resolveTypeBinding();
//		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
//		assertEquals("Not an long (typeBinding)", "long", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		checkSourceRange(qualifiedName, "field.fB.fA.j", source); //$NON-NLS-1$
//
//		SimpleName simpleName = qualifiedName.getName();
//		checkSourceRange(simpleName, "j", source); //$NON-NLS-1$
//		ITypeBinding typeBinding2 = simpleName.resolveTypeBinding();
//		assertEquals("Not an long (typeBinding2)", "long", typeBinding2.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		IBinding binding = simpleName.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertTrue("VariableBinding", binding instanceof IVariableBinding); //$NON-NLS-1$
//		IVariableBinding variableBinding = (IVariableBinding) binding;
//		assertEquals("Not A", "A", variableBinding.getDeclaringClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertEquals("Not default", Modifier.NONE, variableBinding.getModifiers()); //$NON-NLS-1$
//		assertEquals("wrong name", "j", variableBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//
//		Name qualifierName = qualifiedName.getQualifier();
//		assertTrue("Not a qualified name", qualifierName.isQualifiedName()); //$NON-NLS-1$
//		checkSourceRange(qualifierName, "field.fB.fA", source); //$NON-NLS-1$
//		qualifiedName = (QualifiedName) qualifierName;
//		ITypeBinding typeBinding3 = qualifiedName.resolveTypeBinding();
//		assertNotNull("No type binding3", typeBinding3); //$NON-NLS-1$
//		assertEquals("Not an A", "A", typeBinding3.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		simpleName = qualifiedName.getName();
//		checkSourceRange(simpleName, "fA", source); //$NON-NLS-1$
//		ITypeBinding typeBinding4 = simpleName.resolveTypeBinding();
//		assertNotNull("No typeBinding4", typeBinding4); //$NON-NLS-1$
//		assertEquals("Not an A", "A", typeBinding4.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		IBinding binding2 = qualifiedName.resolveBinding();
//		assertNotNull("No binding2", binding2); //$NON-NLS-1$
//		assertTrue("VariableBinding", binding2 instanceof IVariableBinding); //$NON-NLS-1$
//		IVariableBinding variableBinding2 = (IVariableBinding) binding2;
//		assertEquals("Not B", "B", variableBinding2.getDeclaringClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertEquals("Not default", Modifier.NONE, variableBinding2.getModifiers()); //$NON-NLS-1$
//		assertEquals("wrong name", "fA", variableBinding2.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		
//		qualifierName = qualifiedName.getQualifier();
//		assertTrue("Not a qualified name", qualifierName.isQualifiedName()); //$NON-NLS-1$
//		checkSourceRange(qualifierName, "field.fB", source); //$NON-NLS-1$
//		qualifiedName = (QualifiedName) qualifierName;
//		ITypeBinding typeBinding5 = qualifiedName.resolveTypeBinding();
//		assertNotNull("No typeBinding5", typeBinding5); //$NON-NLS-1$
//		assertEquals("Not a B", "B", typeBinding5.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		simpleName = qualifiedName.getName();
//		checkSourceRange(simpleName, "fB", source); //$NON-NLS-1$
//		ITypeBinding typeBinding6 = simpleName.resolveTypeBinding();
//		assertNotNull("No typebinding6", typeBinding6); //$NON-NLS-1$
//		assertEquals("not a B", "B", typeBinding6.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		IBinding binding3 = qualifiedName.resolveBinding();
//		assertNotNull("No binding2", binding3); //$NON-NLS-1$
//		assertTrue("VariableBinding", binding3 instanceof IVariableBinding); //$NON-NLS-1$
//		IVariableBinding variableBinding3 = (IVariableBinding) binding3;
//		assertEquals("Not C", "C", variableBinding3.getDeclaringClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertEquals("Not default", Modifier.NONE, variableBinding3.getModifiers()); //$NON-NLS-1$
//		assertEquals("wrong name", "fB", variableBinding3.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		
//		qualifierName = qualifiedName.getQualifier();
//		assertTrue("Not a simple name", qualifierName.isSimpleName()); //$NON-NLS-1$
//		checkSourceRange(qualifierName, "field", source); //$NON-NLS-1$
//		simpleName = (SimpleName) qualifierName;
//		ITypeBinding typeBinding7 = simpleName.resolveTypeBinding();
//		assertNotNull("No typeBinding7", typeBinding7); //$NON-NLS-1$
//		assertEquals("Not a C", "C", typeBinding7.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		IBinding binding4 = simpleName.resolveBinding();
//		assertNotNull("No binding4", binding4); //$NON-NLS-1$
//		assertTrue("VariableBinding", binding4 instanceof IVariableBinding); //$NON-NLS-1$
//		IVariableBinding variableBinding4 = (IVariableBinding) binding4;
//		assertEquals("Not Test", "Test", variableBinding4.getDeclaringClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertEquals("Not public", Modifier.PUBLIC, variableBinding4.getModifiers()); //$NON-NLS-1$
//		assertEquals("wrong name", "field", variableBinding4.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertEquals("wrong return type", "C", variableBinding4.getType().getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}
//
//	/**
//	 * QualifiedName as TypeReference
//	 */
//	public void test0228() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0228", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 1, 0);
//		assertTrue("ReturnStatement", node2 instanceof ReturnStatement); //$NON-NLS-1$
//		ReturnStatement returnStatement = (ReturnStatement) node2;
//		Expression expr = returnStatement.getExpression();
//		checkSourceRange(expr, "test0228.Test.foo()", source); //$NON-NLS-1$
//		assertTrue("FunctionInvocation", expr instanceof FunctionInvocation); //$NON-NLS-1$
//		FunctionInvocation methodInvocation = (FunctionInvocation) expr;
//		Expression qualifier = methodInvocation.getExpression();
//		assertNotNull("no qualifier", qualifier); //$NON-NLS-1$
//		assertTrue("QualifiedName", qualifier instanceof QualifiedName); //$NON-NLS-1$
//		QualifiedName qualifiedName = (QualifiedName) qualifier;
//		checkSourceRange(qualifiedName, "test0228.Test", source); //$NON-NLS-1$
//		ITypeBinding typeBinding = qualifiedName.resolveTypeBinding();
//		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong type", "Test", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		IBinding binding = qualifiedName.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertEquals("Not a type", IBinding.TYPE, binding.getKind()); //$NON-NLS-1$
//		
//	}

//	/**
//	 * FunctionInvocation
//	 */
//	public void test0229() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0229", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertTrue("ExpressionStatement", node2 instanceof ExpressionStatement); //$NON-NLS-1$
//		ExpressionStatement expressionStatement = (ExpressionStatement) node2;
//		Expression expr = expressionStatement.getExpression();
//		assertTrue("FunctionInvocation", expr instanceof FunctionInvocation); //$NON-NLS-1$
//		checkSourceRange(expr, "System.err.println()", source); //$NON-NLS-1$
//		FunctionInvocation methodInvocation = (FunctionInvocation) expr;
//		Expression qualifier = methodInvocation.getExpression();
//		assertTrue("QualifiedName", qualifier instanceof QualifiedName); //$NON-NLS-1$
//		QualifiedName qualifiedName = (QualifiedName) qualifier;
//		ITypeBinding typeBinding = qualifier.resolveTypeBinding();
//		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "PrintStream", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		IBinding binding = qualifiedName.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertTrue("VariableBinding", binding instanceof IVariableBinding); //$NON-NLS-1$
//		IVariableBinding variableBinding = (IVariableBinding) binding;
//		assertEquals("wrong name", "err", variableBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		SimpleName methodName = methodInvocation.getName();
//		IBinding binding2 = methodName.resolveBinding();
//		assertNotNull("No binding2", binding2); //$NON-NLS-1$
//	}
	
//	/**
//	 * FunctionInvocation
//	 */
//	public void test0230() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0230", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 1, 0);
//		assertTrue("ExpressionStatement", node2 instanceof ExpressionStatement); //$NON-NLS-1$
//		ExpressionStatement expressionStatement = (ExpressionStatement) node2;
//		Expression expr = expressionStatement.getExpression();
//		assertTrue("FunctionInvocation", expr instanceof FunctionInvocation); //$NON-NLS-1$
//		checkSourceRange(expr, "err.println()", source); //$NON-NLS-1$
//		FunctionInvocation methodInvocation = (FunctionInvocation) expr;
//		Expression qualifier = methodInvocation.getExpression();
//		assertTrue("SimpleName", qualifier instanceof SimpleName); //$NON-NLS-1$
//		SimpleName name = (SimpleName) qualifier;
//		IBinding binding = name.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertEquals("Wrong name", "err", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		ITypeBinding typeBinding = name.resolveTypeBinding();
//		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wron type name", "PrintStream", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}
	
//	/**
//	 * FunctionInvocation
//	 */
//	public void test0231() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0231", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertTrue("ExpressionStatement", node2 instanceof ExpressionStatement); //$NON-NLS-1$
//		ExpressionStatement expressionStatement = (ExpressionStatement) node2;
//		Expression expr = expressionStatement.getExpression();
//		assertTrue("FunctionInvocation", expr instanceof FunctionInvocation); //$NON-NLS-1$
//		checkSourceRange(expr, "System.err.println()", source); //$NON-NLS-1$
//		FunctionInvocation methodInvocation = (FunctionInvocation) expr;
//		Expression qualifier = methodInvocation.getExpression();
//		assertTrue("QualifiedName", qualifier instanceof QualifiedName); //$NON-NLS-1$
//		QualifiedName qualifiedName = (QualifiedName) qualifier;
//		ITypeBinding typeBinding = qualifier.resolveTypeBinding();
//		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "PrintStream", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		IBinding binding = qualifiedName.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertTrue("VariableBinding", binding instanceof IVariableBinding); //$NON-NLS-1$
//		IVariableBinding variableBinding = (IVariableBinding) binding;
//		assertEquals("wrong name", "err", variableBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		SimpleName methodName = methodInvocation.getName();
//		IBinding binding2 = methodName.resolveBinding();
//		assertNotNull("No binding2", binding2); //$NON-NLS-1$
//		Name name = qualifiedName.getQualifier();
//		assertTrue("SimpleName", name.isSimpleName()); //$NON-NLS-1$
//		SimpleName simpleName = (SimpleName) name;
//		ITypeBinding typeBinding2 = simpleName.resolveTypeBinding();
//		assertNotNull("No typeBinding2", typeBinding2); //$NON-NLS-1$
//		assertEquals("wrong type name", "System", typeBinding2.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}

//	/**
//	 * FunctionInvocation
//	 */
//	public void test0232() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0232", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		ASTNode node2 = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertTrue("VariableDeclarationStatement", node2 instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node2;
//		List fragments = variableDeclarationStatement.fragments();
//		assertEquals("wrong size", 1, fragments.size()); //$NON-NLS-1$
//		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
//		Expression initialization = variableDeclarationFragment.getInitializer();
//		ITypeBinding typeBinding = initialization.resolveTypeBinding();
//		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
//		assertTrue("Not a primitive type", typeBinding.isPrimitive()); //$NON-NLS-1$
//		assertEquals("wrong name", "int", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertTrue("QualifiedName", initialization instanceof QualifiedName); //$NON-NLS-1$
//		QualifiedName qualifiedName = (QualifiedName) initialization;
//		SimpleName simpleName = qualifiedName.getName();
//		ITypeBinding typeBinding2 = simpleName.resolveTypeBinding();
//		assertNotNull("No type binding", typeBinding2); //$NON-NLS-1$
//		assertTrue("Not a primitive type", typeBinding2.isPrimitive()); //$NON-NLS-1$
//		assertEquals("wrong name", "int", typeBinding2.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		IBinding binding = simpleName.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertTrue("IVariableBinding", binding instanceof IVariableBinding); //$NON-NLS-1$
//		IVariableBinding variableBinding = (IVariableBinding) binding;
//		assertNull("No declaring class", variableBinding.getDeclaringClass()); //$NON-NLS-1$
//	}
	
//	/**
//	 * Checking that only syntax errors are reported for the MALFORMED tag
//	 */
//	public void test0233() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0233", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("Expression should not be null", result); //$NON-NLS-1$
//		assertTrue("The compilation unit is malformed", !isMalformed(result)); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit unit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertTrue("The fiels is not malformed", !isMalformed(node)); //$NON-NLS-1$
//		assertEquals("No problem found", 1, unit.getMessages().length); //$NON-NLS-1$
//		assertEquals("No problem found", 1, unit.getProblems().length); //$NON-NLS-1$
//	}
//
//	/**
//	 * Checking that null is returned for a resolveBinding if the type is unknown
//	 */
//	public void test0234() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0234", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertTrue("The fiels is not malformed", !isMalformed(node)); //$NON-NLS-1$
//		JavaScriptUnit unit = (JavaScriptUnit) result;
//		assertEquals("No problem found", 1, unit.getMessages().length); //$NON-NLS-1$
//		assertEquals("No problem found", 1, unit.getProblems().length); //$NON-NLS-1$
//		assertTrue("VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
//		List fragments = fieldDeclaration.fragments();
//		assertEquals("wrong size", 1, fragments.size()); //$NON-NLS-1$
//		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(0);
//		IVariableBinding variableBinding = fragment.resolveBinding();
//		assertNull("binding not null", variableBinding); //$NON-NLS-1$
//	}

	/**
	 * Checking that null is returned for a resolveBinding if the type is unknown
	 */
	public void test0235() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0235", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertTrue("The fiels is not malformed", !isMalformed(node)); //$NON-NLS-1$
		JavaScriptUnit unit = (JavaScriptUnit) result;
		assertEquals("problems found", 0, unit.getMessages().length); //$NON-NLS-1$
		assertEquals("problems found", 0, unit.getProblems().length); //$NON-NLS-1$
		assertTrue("VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
		List fragments = fieldDeclaration.fragments();
		assertEquals("wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(0);
		IVariableBinding variableBinding = fragment.resolveBinding();
		assertNotNull("No binding", variableBinding); //$NON-NLS-1$
	}

	/**
	 * Test the removal of a IField inside a CU that has an initializer
	 */
	public void test0236() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0236", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		IType type = sourceUnit.getType("Test"); //$NON-NLS-1$
//		assertNotNull("No type", type); //$NON-NLS-1$
		IField field = sourceUnit.getField("i"); //$NON-NLS-1$
		assertNotNull("No field", field); //$NON-NLS-1$
		field.delete(true, null);
	}

	/**
	 * http://bugs.eclipse.org/bugs/show_bug.cgi?id=9452
	 */
	public void test0237() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "junit.framework", "TestCase.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
	}
		
//	/**
//	 * Check ThisExpression
//	 * @deprecated using deprecated code
//	 */
//	public void test0238() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0238", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		char[] source = sourceUnit.getSource().toCharArray();
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertTrue("Not a type declaration statement", node instanceof TypeDeclarationStatement); //$NON-NLS-1$
//		TypeDeclarationStatement typeDeclarationStatement = (TypeDeclarationStatement) node;
//		TypeDeclaration typeDecl = typeDeclarationStatement.getTypeDeclaration();
//		Object o = typeDecl.bodyDeclarations().get(0);
//		assertTrue("Not a method", o instanceof FunctionDeclaration); //$NON-NLS-1$
//		FunctionDeclaration methodDecl = (FunctionDeclaration) o;
//		Block block = methodDecl.getBody();
//		List statements = block.statements();
//		assertEquals("Not 1", 1, statements.size()); //$NON-NLS-1$
//		Statement stmt = (Statement) statements.get(0);
//		assertTrue("Not a return statement", stmt instanceof ReturnStatement); //$NON-NLS-1$
//		ReturnStatement returnStatement = (ReturnStatement) stmt;
//		Expression expr = returnStatement.getExpression();
//		assertTrue("Not a method invocation", expr instanceof FunctionInvocation); //$NON-NLS-1$
//		FunctionInvocation methodInvocation = (FunctionInvocation) expr;
//		checkSourceRange(methodInvocation, "Test.this.bar()", source); //$NON-NLS-1$
//		Expression qualifier = methodInvocation.getExpression();
//		assertTrue("Not a ThisExpression", qualifier instanceof ThisExpression); //$NON-NLS-1$
//		ThisExpression thisExpression = (ThisExpression) qualifier;
//		Name name = thisExpression.getQualifier();
//		IBinding binding = name.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertEquals("wrong name", "Test", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}
//
//	/**
//	 * Check ThisExpression
//	 * @deprecated using deprecated code
//	 */
//	public void test0239() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0239", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		ASTNode node = getASTNode((JavaScriptUnit) result, 1, 0, 0);
//		assertTrue("Not a type declaration statement", node instanceof TypeDeclarationStatement); //$NON-NLS-1$
//		TypeDeclarationStatement typeDeclarationStatement = (TypeDeclarationStatement) node;
//		TypeDeclaration typeDecl = typeDeclarationStatement.getTypeDeclaration();
//		Object o = typeDecl.bodyDeclarations().get(0);
//		assertTrue("Not a method", o instanceof FunctionDeclaration); //$NON-NLS-1$
//		FunctionDeclaration methodDecl = (FunctionDeclaration) o;
//		Block block = methodDecl.getBody();
//		List statements = block.statements();
//		assertEquals("Not 1", 1, statements.size()); //$NON-NLS-1$
//		Statement stmt = (Statement) statements.get(0);
//		assertTrue("Not a return statement", stmt instanceof ReturnStatement); //$NON-NLS-1$
//		ReturnStatement returnStatement = (ReturnStatement) stmt;
//		Expression expr = returnStatement.getExpression();
//		assertTrue("Not a SuperMethodInvocation", expr instanceof SuperMethodInvocation); //$NON-NLS-1$
//		SuperMethodInvocation superMethodInvocation = (SuperMethodInvocation) expr;
//		Name name = superMethodInvocation.getQualifier();
//		IBinding binding = name.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertTrue("A type binding", binding instanceof ITypeBinding); //$NON-NLS-1$
//		assertEquals("Not Test", "Test", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		Name methodName = superMethodInvocation.getName();
//		IBinding binding2 = methodName.resolveBinding();
//		assertNotNull("No binding2", binding2); //$NON-NLS-1$
//		assertTrue("No an IFunctionBinding", binding2 instanceof IFunctionBinding); //$NON-NLS-1$
//		IFunctionBinding methodBinding = (IFunctionBinding) binding2;
//		assertEquals("Not bar", "bar", methodBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertEquals("Not T", "T", methodBinding.getDeclaringClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}
	
//	/**
//	 * Check FieldAccess
//	 * @deprecated using deprecated code
//	 */
//	public void test0240() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0240", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertTrue("Not a type declaration statement", node instanceof TypeDeclarationStatement); //$NON-NLS-1$
//		TypeDeclarationStatement typeDeclarationStatement = (TypeDeclarationStatement) node;
//		TypeDeclaration typeDecl = typeDeclarationStatement.getTypeDeclaration();
//		Object o = typeDecl.bodyDeclarations().get(0);
//		assertTrue("Not a method", o instanceof FunctionDeclaration); //$NON-NLS-1$
//		FunctionDeclaration methodDecl = (FunctionDeclaration) o;
//		Block block = methodDecl.getBody();
//		List statements = block.statements();
//		assertEquals("Not 1", 1, statements.size()); //$NON-NLS-1$
//		Statement stmt = (Statement) statements.get(0);
//		assertTrue("Not a return statement", stmt instanceof ReturnStatement); //$NON-NLS-1$
//		ReturnStatement returnStatement = (ReturnStatement) stmt;
//		Expression expr = returnStatement.getExpression();
//		assertTrue("Not a field access", expr instanceof FieldAccess); //$NON-NLS-1$
//		FieldAccess fieldAccess = (FieldAccess) expr;
//		Expression qualifier = fieldAccess.getExpression();
//		assertTrue("Not a ThisExpression", qualifier instanceof ThisExpression); //$NON-NLS-1$
//		ThisExpression thisExpression = (ThisExpression) qualifier;
//		Name name = thisExpression.getQualifier();
//		IBinding binding = name.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertEquals("Not Test", "Test", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		Name fieldName = fieldAccess.getName();
//		IBinding binding2 = fieldName.resolveBinding();
//		assertNotNull("No binding2", binding2); //$NON-NLS-1$
//		assertEquals("Wrong name", "f", binding2.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertEquals("Wrong modifier", Modifier.PUBLIC, binding2.getModifiers()); //$NON-NLS-1$
//		ITypeBinding typeBinding = fieldName.resolveTypeBinding();
//		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
//		assertEquals("Not int", "int", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}
//
	/**
	 * Check order of body declarations
	 */
	public void test0241() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0241", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0);
//		assertTrue("Not a type declaration", node instanceof TypeDeclaration); //$NON-NLS-1$
//		assertTrue("Not a declaration", ((TypeDeclaration) node).getName().isDeclaration()); //$NON-NLS-1$
//		assertEquals("Wrong size", 11, ((TypeDeclaration)node).bodyDeclarations().size()); //$NON-NLS-1$
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertTrue("Not a field declaration", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		node = getASTNode((JavaScriptUnit) result, 0, 1);
		assertTrue("Not a FunctionDeclaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
//		node = getASTNode((JavaScriptUnit) result, 0, 2);
//		assertTrue("Not a Type declaration", node instanceof TypeDeclaration); //$NON-NLS-1$
//		node = getASTNode((JavaScriptUnit) result, 0, 3);
//		assertTrue("Not a Type declaration", node instanceof TypeDeclaration); //$NON-NLS-1$
		node = getASTNode((JavaScriptUnit) result, 0, 2);
		assertTrue("Not a FunctionDeclaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
		node = getASTNode((JavaScriptUnit) result, 0, 3);
		assertTrue("Not a field declaration", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		node = getASTNode((JavaScriptUnit) result, 0, 4);
		assertTrue("Not a FunctionDeclaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
		node = getASTNode((JavaScriptUnit) result, 0, 5);
		assertTrue("Not a field declaration", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		node = getASTNode((JavaScriptUnit) result, 0, 6);
		assertTrue("Not a field declaration", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		node = getASTNode((JavaScriptUnit) result, 0, 7);
		assertTrue("Not a FunctionDeclaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
//		node = getASTNode((JavaScriptUnit) result, 0, 10);
//		assertTrue("Not a Type declaration", node instanceof TypeDeclaration); //$NON-NLS-1$
	}

//	/**
//	 * Check ThisExpression
//	 * @deprecated using deprecated code
//	 */
//	public void test0242() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0242", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		ASTNode node = getASTNode((JavaScriptUnit) result, 1, 0, 0);
//		assertTrue("Not a type declaration statement", node instanceof TypeDeclarationStatement); //$NON-NLS-1$
//		TypeDeclarationStatement typeDeclarationStatement = (TypeDeclarationStatement) node;
//		TypeDeclaration typeDecl = typeDeclarationStatement.getTypeDeclaration();
//		Object o = typeDecl.bodyDeclarations().get(0);
//		assertTrue("Not a method", o instanceof FunctionDeclaration); //$NON-NLS-1$
//		FunctionDeclaration methodDecl = (FunctionDeclaration) o;
//		Block block = methodDecl.getBody();
//		List statements = block.statements();
//		assertEquals("Not 1", 1, statements.size()); //$NON-NLS-1$
//		Statement stmt = (Statement) statements.get(0);
//		assertTrue("Not a return statement", stmt instanceof ReturnStatement); //$NON-NLS-1$
//		ReturnStatement returnStatement = (ReturnStatement) stmt;
//		Expression expr = returnStatement.getExpression();
//		assertTrue("Not a SuperFieldAccess", expr instanceof SuperFieldAccess); //$NON-NLS-1$
//		SuperFieldAccess superFieldAccess = (SuperFieldAccess) expr;
//		Name name = superFieldAccess.getQualifier();
//		IBinding binding = name.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertTrue("A type binding", binding instanceof ITypeBinding); //$NON-NLS-1$
//		assertEquals("Not Test", "Test", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		Name fieldName = superFieldAccess.getName();
//		IBinding binding2 = fieldName.resolveBinding();
//		assertNotNull("No binding2", binding2); //$NON-NLS-1$
//		assertTrue("No an IVariableBinding", binding2 instanceof IVariableBinding); //$NON-NLS-1$
//		IVariableBinding variableBinding = (IVariableBinding) binding2;
//		assertEquals("Not f", "f", variableBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertEquals("Not T", "T", variableBinding.getDeclaringClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		ITypeBinding typeBinding2 = fieldName.resolveTypeBinding();
//		assertNotNull("No type binding", typeBinding2); //$NON-NLS-1$
//		assertEquals("Not int", "int", typeBinding2.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}

	/**
	 * Check catch clause positions:
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10570
	 */
	public void test0243() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0243", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertTrue("Not a try statement", node instanceof TryStatement); //$NON-NLS-1$
		TryStatement tryStatement = (TryStatement) node;
		List catchClauses = tryStatement.catchClauses();
		assertEquals("wrong size", 1, catchClauses.size()); //$NON-NLS-1$
		CatchClause catchClause = (CatchClause) catchClauses.get(0);
		checkSourceRange(catchClause, "catch (e){m();}", source); //$NON-NLS-1$
	}

	/**
	 * Check catch clause positions:
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10570
	 */
	public void test0244() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0244", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertTrue("Not a try statement", node instanceof TryStatement); //$NON-NLS-1$
		TryStatement tryStatement = (TryStatement) node;
		List catchClauses = tryStatement.catchClauses();
		assertEquals("wrong size", 1, catchClauses.size()); //$NON-NLS-1$
		CatchClause catchClause = (CatchClause) catchClauses.get(0);
		checkSourceRange(catchClause, "catch (e){m();}", source); //$NON-NLS-1$
//		catchClause = (CatchClause) catchClauses.get(1);
//		checkSourceRange(catchClause, "catch(e) {}", source); //$NON-NLS-1$
	}

	/**
	 * http://bugs.eclipse.org/bugs/show_bug.cgi?id=10587
	 */
	public void test0245() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0245", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit unit = (JavaScriptUnit) result;
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
		assertTrue("Not a return statement", node instanceof ReturnStatement); //$NON-NLS-1$
		ReturnStatement returnStatement = (ReturnStatement) node;
		Expression expr = returnStatement.getExpression();
		assertTrue("not a name", expr instanceof Name); //$NON-NLS-1$
		Name name = (Name) expr;
		IBinding binding = name.resolveBinding();
		assertTrue("Not a variable binding", binding instanceof IVariableBinding); //$NON-NLS-1$
		IVariableBinding variableBinding = (IVariableBinding) binding;
		assertEquals("Not i", "i", variableBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Not Number", "Number", variableBinding.getType().getName()); //$NON-NLS-1$ //$NON-NLS-2$
		ASTNode declaringNode = unit.findDeclaringNode(variableBinding);
		assertNotNull("No declaring node", declaringNode); //$NON-NLS-1$
		assertTrue("Not a VariableDeclarationFragment", declaringNode instanceof VariableDeclarationFragment); //$NON-NLS-1$
	}
	
//	/**
//	 * Test binding resolution for import declaration
//	 */
//	public void test0246() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0246", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit unit = (JavaScriptUnit) result;
//		List imports = unit.imports();
//		assertEquals("wrong imports size", 2, imports.size()); //$NON-NLS-1$
//		ImportDeclaration importDeclaration = (ImportDeclaration) imports.get(0);
//		assertTrue("Not on demand", importDeclaration.isOnDemand()); //$NON-NLS-1$
//		checkSourceRange(importDeclaration, "import java.util.*;", source); //$NON-NLS-1$
//		IBinding binding = importDeclaration.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertEquals("Wrong type", IBinding.PACKAGE, binding.getKind()); //$NON-NLS-1$
//		assertEquals("Wrong name", "java.util", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		importDeclaration = (ImportDeclaration) imports.get(1);
//		assertTrue("On demand", !importDeclaration.isOnDemand()); //$NON-NLS-1$
//		checkSourceRange(importDeclaration, "import java.io.IOException;", source); //$NON-NLS-1$
//		binding = importDeclaration.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertEquals("Wrong type", IBinding.TYPE, binding.getKind()); //$NON-NLS-1$
//		assertEquals("Wrong name", "IOException", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}
//
//	/**
//	 * Test binding resolution for import declaration
//	 */
//	public void test0247() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0247", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit unit = (JavaScriptUnit) result;
//		PackageDeclaration packageDeclaration = unit.getPackage();
//		checkSourceRange(packageDeclaration, "package test0247;", source); //$NON-NLS-1$
//		IPackageBinding binding = packageDeclaration.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertEquals("Wrong type", IBinding.PACKAGE, binding.getKind()); //$NON-NLS-1$
//		assertEquals("Wrong name", "test0247", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10592
	 */
	public void test0248() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0248", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
		assertTrue("Not a method declaration", node instanceof FunctionDeclaration);		 //$NON-NLS-1$
		FunctionDeclaration methodDeclaration = (FunctionDeclaration) node;
		List parameters = methodDeclaration.parameters();
		assertEquals("wrong size", 1, parameters.size()); //$NON-NLS-1$
		SingleVariableDeclaration singleVariableDeclaration = (SingleVariableDeclaration) parameters.get(0);
		Name name = singleVariableDeclaration.getName();
		IBinding binding = name.resolveBinding();
		assertNotNull("No binding", binding); //$NON-NLS-1$
		assertTrue("Not a variable binding", binding instanceof IVariableBinding); //$NON-NLS-1$
		IVariableBinding variableBinding = (IVariableBinding) binding;
		assertEquals("Wrong name", "i", variableBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Wrong type", "any", variableBinding.getType().getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}

//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10592
//	 */
//	public void test0249() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0249", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 2, 1);
//		assertTrue("Not an ExpressionStatement", node instanceof ExpressionStatement); //$NON-NLS-1$
//		ExpressionStatement expressionStatement = (ExpressionStatement) node;
//		Expression expression = expressionStatement.getExpression();
//		assertTrue("Not an assignment", expression instanceof Assignment); //$NON-NLS-1$
//		Assignment assignment = (Assignment) expression;
//		Expression leftHandSide = assignment.getLeftHandSide();
//		assertTrue("Not a qualified name", leftHandSide instanceof QualifiedName); //$NON-NLS-1$
//		QualifiedName qualifiedName = (QualifiedName) leftHandSide;
//		Name simpleName = qualifiedName.getName();
//		IBinding binding = simpleName.resolveBinding();
//		assertNotNull("no binding", binding); //$NON-NLS-1$
//		assertTrue("Not a IVariableBinding", binding instanceof IVariableBinding); //$NON-NLS-1$
//		IVariableBinding variableBinding = (IVariableBinding) binding;
//		assertEquals("Wrong name", "k", variableBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertEquals("Wrong modifier", Modifier.STATIC, variableBinding.getModifiers()); //$NON-NLS-1$
//		assertEquals("Wrong type", "int", variableBinding.getType().getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertEquals("Wrong declaring class name", "j", variableBinding.getDeclaringClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}
//
//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10592
//	 */
//	public void test0250() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0250", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertTrue("Not a method declaration", node instanceof FunctionDeclaration);		 //$NON-NLS-1$
//		FunctionDeclaration methodDeclaration = (FunctionDeclaration) node;
//		List parameters = methodDeclaration.parameters();
//		assertEquals("wrong size", 2, parameters.size()); //$NON-NLS-1$
//		SingleVariableDeclaration singleVariableDeclaration = (SingleVariableDeclaration) parameters.get(0);
//		Name name = singleVariableDeclaration.getName();
//		IBinding binding = name.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertTrue("Not a variable binding", binding instanceof IVariableBinding); //$NON-NLS-1$
//		IVariableBinding variableBinding = (IVariableBinding) binding;
//		assertEquals("Wrong name", "i", variableBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertEquals("Wrong type", "int", variableBinding.getType().getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}
//		
//	/**
//	 * Check qualified name resolution for static fields
//	 */
//	public void test0251() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0251", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertTrue("Not a ExpressionStatement", node instanceof ExpressionStatement); //$NON-NLS-1$
//		ExpressionStatement expressionStatement = (ExpressionStatement) node;
//		Expression expression = expressionStatement.getExpression();
//		assertTrue("Not a method invocation", expression instanceof FunctionInvocation); //$NON-NLS-1$
//		FunctionInvocation methodInvocation = (FunctionInvocation) expression;
//		checkSourceRange(methodInvocation, "java.lang.System.out.println()", source); //$NON-NLS-1$
//		Expression qualifier = methodInvocation.getExpression();
//		assertTrue("Not a qualified name", qualifier instanceof QualifiedName); //$NON-NLS-1$
//		checkSourceRange(qualifier, "java.lang.System.out", source); //$NON-NLS-1$
//		QualifiedName qualifiedName = (QualifiedName) qualifier;
//		Name typeName = qualifiedName.getQualifier();
//		assertTrue("Not a QualifiedName", typeName instanceof QualifiedName); //$NON-NLS-1$
//		QualifiedName qualifiedTypeName = (QualifiedName) typeName;
//		IBinding binding = qualifiedTypeName.getName().resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertEquals("Wrong name", "System", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		binding = qualifiedTypeName.getQualifier().resolveBinding();
//		assertNotNull("No binding2", binding); //$NON-NLS-1$
//		assertEquals("Wrong type binding", IBinding.PACKAGE, binding.getKind()); //$NON-NLS-1$
//	}
//		
//	/**
//	 * Check binding for anonymous class
//	 */
//	public void test0252() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0252", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 1);
//		assertTrue("Not a return statement", node instanceof ReturnStatement); //$NON-NLS-1$
//		ReturnStatement returnStatement = (ReturnStatement) node;
//		Expression expression = returnStatement.getExpression();
//		assertTrue("Not a classinstancecreation", expression instanceof ClassInstanceCreation); //$NON-NLS-1$
//		ClassInstanceCreation classInstanceCreation = (ClassInstanceCreation) expression;
//		IFunctionBinding methodBinding = classInstanceCreation.resolveConstructorBinding();
//		assertNotNull("No methodBinding", methodBinding); //$NON-NLS-1$
//		assertTrue("Not a constructor", methodBinding.isConstructor()); //$NON-NLS-1$
//		assertTrue("Not an anonymous class", methodBinding.getDeclaringClass().isAnonymous()); //$NON-NLS-1$
//		assertEquals("Not an anonymous class of java.lang.Object", "Object", methodBinding.getDeclaringClass().getSuperclass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertEquals("Not an anonymous class of java.lang.Object", "java.lang", methodBinding.getDeclaringClass().getSuperclass().getPackage().getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}

	/**
	 * Check binding for allocation expression
	 */
	public void test0253() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0253", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertTrue("Not a return statement", node instanceof ReturnStatement); //$NON-NLS-1$
		ReturnStatement returnStatement = (ReturnStatement) node;
		Expression expression = returnStatement.getExpression();
		assertTrue("Not a classinstancecreation", expression instanceof ClassInstanceCreation); //$NON-NLS-1$
		ClassInstanceCreation classInstanceCreation = (ClassInstanceCreation) expression;
		IFunctionBinding methodBinding = classInstanceCreation.resolveConstructorBinding();
		assertNotNull("No methodBinding", methodBinding); //$NON-NLS-1$
		assertTrue("Not a constructor", methodBinding.isConstructor()); //$NON-NLS-1$
//		assertEquals("Wrong size", 1, methodBinding.getParameterTypes().length); //$NON-NLS-1$
//		assertEquals("Wrong type", "String", methodBinding.getParameterTypes()[0].getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}

//	/**
//	 * Check binding for allocation expression
//	 */
//	public void test0254() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0254", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 1, 0);
//		assertTrue("Not a return statement", node instanceof ReturnStatement); //$NON-NLS-1$
//		ReturnStatement returnStatement = (ReturnStatement) node;
//		Expression expression = returnStatement.getExpression();
//		assertTrue("Not a class instance creation", expression instanceof ClassInstanceCreation); //$NON-NLS-1$
//		ClassInstanceCreation classInstanceCreation = (ClassInstanceCreation) expression;
//		IFunctionBinding binding = classInstanceCreation.resolveConstructorBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertEquals("wrong type", "C", binding.getDeclaringClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}


//	/**
//	 * Check binding for allocation expression
//	 */
//	public void test0255() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0255", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertTrue("Not an ExpressionStatement", node instanceof ExpressionStatement); //$NON-NLS-1$
//		ExpressionStatement expressionStatement = (ExpressionStatement) node;
//		Expression expression = expressionStatement.getExpression();
//		assertTrue("Not a FunctionInvocation", expression instanceof FunctionInvocation); //$NON-NLS-1$
//		FunctionInvocation methodInvocation = (FunctionInvocation) expression;
//		List arguments = methodInvocation.arguments();
//		assertEquals("wrong size", 1, arguments.size()); //$NON-NLS-1$
//		Expression expression2 = (Expression) arguments.get(0);
//		assertTrue("Not a CastExpression", expression2 instanceof CastExpression); //$NON-NLS-1$
//		CastExpression castExpression = (CastExpression) expression2;
//		Type type = castExpression.getType();
//		ITypeBinding binding = type.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertTrue("Not an array type", binding.isArray()); //$NON-NLS-1$
//	}

//	/**
//	 * Check binding for allocation expression
//	 */
//	public void test0256() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0256", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertTrue("Not an ExpressionStatement", node instanceof ExpressionStatement); //$NON-NLS-1$
//		ExpressionStatement expressionStatement = (ExpressionStatement) node;
//		Expression expression = expressionStatement.getExpression();
//		assertTrue("Not a FunctionInvocation", expression instanceof FunctionInvocation); //$NON-NLS-1$
//		FunctionInvocation methodInvocation = (FunctionInvocation) expression;
//		List arguments = methodInvocation.arguments();
//		assertEquals("wrong size", 1, arguments.size()); //$NON-NLS-1$
//		Expression expression2 = (Expression) arguments.get(0);
//		assertTrue("Not a CastExpression", expression2 instanceof CastExpression); //$NON-NLS-1$
//		CastExpression castExpression = (CastExpression) expression2;
//		Type type = castExpression.getType();
//		assertTrue("Not a simple type", type.isSimpleType()); //$NON-NLS-1$
//		SimpleType simpleType = (SimpleType) type;
//		ITypeBinding binding = type.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertTrue("Not a class", binding.isClass()); //$NON-NLS-1$
//		Name name = simpleType.getName();
//		IBinding binding2 = name.resolveBinding();
//		assertNotNull("No binding2", binding2); //$NON-NLS-1$
//		assertEquals("Wrong type", "Object", binding2.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}
//
//	/**
//	 * Check binding for allocation expression
//	 */
//	public void test0257() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0257", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertTrue("Not an ExpressionStatement", node instanceof ExpressionStatement); //$NON-NLS-1$
//		ExpressionStatement expressionStatement = (ExpressionStatement) node;
//		Expression expression = expressionStatement.getExpression();
//		assertTrue("Not a FunctionInvocation", expression instanceof FunctionInvocation); //$NON-NLS-1$
//		FunctionInvocation methodInvocation = (FunctionInvocation) expression;
//		List arguments = methodInvocation.arguments();
//		assertEquals("wrong size", 1, arguments.size()); //$NON-NLS-1$
//		Expression expression2 = (Expression) arguments.get(0);
//		assertTrue("Not a CastExpression", expression2 instanceof CastExpression); //$NON-NLS-1$
//		CastExpression castExpression = (CastExpression) expression2;
//		Type type = castExpression.getType();
//		assertTrue("Not a primitive type", type.isPrimitiveType()); //$NON-NLS-1$
//		PrimitiveType primitiveType = (PrimitiveType) type;
//		assertEquals("Not int", PrimitiveType.INT, primitiveType.getPrimitiveTypeCode()); //$NON-NLS-1$
//	}

//	/**
//	 * Check binding for allocation expression
//	 */
//	public void test0258() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0258", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
//		assertTrue("Not an ExpressionStatement", node instanceof ExpressionStatement); //$NON-NLS-1$
//		ExpressionStatement expressionStatement = (ExpressionStatement) node;
//		Expression expression = expressionStatement.getExpression();
//		assertTrue("Not a FunctionInvocation", expression instanceof FunctionInvocation); //$NON-NLS-1$
//		FunctionInvocation methodInvocation = (FunctionInvocation) expression;
//		List arguments = methodInvocation.arguments();
//		assertEquals("wrong size", 1, arguments.size()); //$NON-NLS-1$
//		Expression expression2 = (Expression) arguments.get(0);
//		assertTrue("Not a CastExpression", expression2 instanceof CastExpression); //$NON-NLS-1$
//		CastExpression castExpression = (CastExpression) expression2;
//		Type type = castExpression.getType();
//		assertTrue("Not a simple type", type.isSimpleType()); //$NON-NLS-1$
//		SimpleType simpleType = (SimpleType) type;
//		ITypeBinding binding = type.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		assertTrue("Not a class", binding.isClass()); //$NON-NLS-1$
//		Name name = simpleType.getName();
//		IBinding binding2 = name.resolveBinding();
//		assertNotNull("No binding2", binding2); //$NON-NLS-1$
//		assertEquals("Wrong type", "Object", binding2.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10663
	 */
	public void test0259() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0259", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
	}
//
//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10592
//	 */
//	public void test0260() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0260", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0);
//		assertTrue("Not a method declaration", node instanceof FunctionDeclaration);		 //$NON-NLS-1$
//		FunctionDeclaration methodDeclaration = (FunctionDeclaration) node;
//		List parameters = methodDeclaration.parameters();
//		assertEquals("wrong size", 2, parameters.size()); //$NON-NLS-1$
//		SingleVariableDeclaration singleVariableDeclaration = (SingleVariableDeclaration) parameters.get(0);
//		IBinding binding = singleVariableDeclaration.resolveBinding();
//		assertNotNull("No binding", binding); //$NON-NLS-1$
//		Name name = singleVariableDeclaration.getName();
//		assertTrue("Not a simple name", name instanceof SimpleName); //$NON-NLS-1$
//		SimpleName simpleName = (SimpleName) name;
//		assertEquals("Wrong name", "i", simpleName.getIdentifier()); //$NON-NLS-1$ //$NON-NLS-2$
//		IBinding binding2 = name.resolveBinding();
//		assertNotNull("No binding", binding2); //$NON-NLS-1$
//		assertTrue("binding == binding2", binding == binding2); //$NON-NLS-1$
//		assertTrue("Not a variable binding", binding2 instanceof IVariableBinding); //$NON-NLS-1$
//		IVariableBinding variableBinding = (IVariableBinding) binding2;
//		assertEquals("Wrong name", "i", variableBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		assertEquals("Wrong type", "int", variableBinding.getType().getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10676
	 */
	public void test0262() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0262", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertTrue("Not an ExpressionStatement", node instanceof ExpressionStatement); //$NON-NLS-1$
		ExpressionStatement expressionStatement = (ExpressionStatement) node;
		Expression expr = expressionStatement.getExpression();
		assertTrue("Not a FunctionInvocation", expr instanceof FunctionInvocation); //$NON-NLS-1$
		FunctionInvocation methodInvocation = (FunctionInvocation) expr;
		List arguments = methodInvocation.arguments();
		assertEquals("Wrong argument list size", 1, arguments.size()); //$NON-NLS-1$
		Expression expr2 = (Expression) arguments.get(0);
		assertTrue("Not a class instance creation", expr2 instanceof ClassInstanceCreation); //$NON-NLS-1$
		ClassInstanceCreation classInstanceCreation = (ClassInstanceCreation) expr2;
		arguments = classInstanceCreation.arguments();
		assertEquals("Wrong size", 1, arguments.size()); //$NON-NLS-1$
		Expression expression2 = (Expression) arguments.get(0);
		assertTrue("Not a string literal", expression2 instanceof StringLiteral); //$NON-NLS-1$
		StringLiteral stringLiteral = (StringLiteral) expression2;
		ITypeBinding typeBinding = stringLiteral.resolveTypeBinding();
		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
		assertEquals("Wrong name", "String", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10700
	 */
	public void test0263() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0263", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertTrue("Not an ExpressionStatement", node instanceof ExpressionStatement); //$NON-NLS-1$
		ExpressionStatement expressionStatement = (ExpressionStatement) node;
		Expression expr = expressionStatement.getExpression();
		assertTrue("Not a FunctionInvocation", expr instanceof FunctionInvocation); //$NON-NLS-1$
		FunctionInvocation methodInvocation = (FunctionInvocation) expr;
		List arguments = methodInvocation.arguments();
		assertEquals("Wrong argument list size", 1, arguments.size()); //$NON-NLS-1$
		Expression expr2 = (Expression) arguments.get(0);
		assertTrue("Not a simple name", expr2 instanceof SimpleName); //$NON-NLS-1$
		SimpleName simpleName = (SimpleName) expr2;
		IBinding binding = simpleName.resolveBinding();
		assertNotNull("No binding", binding); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10699
	 */
//	public void test0264() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0264", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
//		assertTrue("Not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
//		List fragments = variableDeclarationStatement.fragments();
//		assertEquals("Wrong fragment size", 1, fragments.size()); //$NON-NLS-1$
//		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
//		Expression expression = variableDeclarationFragment.getInitializer();
//		assertTrue("Not a classinstancecreation", expression instanceof ClassInstanceCreation); //$NON-NLS-1$
//		ClassInstanceCreation classInstanceCreation = (ClassInstanceCreation) expression;
//		AnonymousClassDeclaration anonymousClassDeclaration = classInstanceCreation.getAnonymousClassDeclaration();
//		assertNotNull("No anonymousclassdeclaration", anonymousClassDeclaration); //$NON-NLS-1$
//		String expectedSourceRange = 
//			"{\n"+  //$NON-NLS-1$
//			"			void m(int k){\n"+ //$NON-NLS-1$
//			"				k= i;\n"+ //$NON-NLS-1$
//			"			}\n"+ //$NON-NLS-1$
//			"		}"; //$NON-NLS-1$
//		checkSourceRange(anonymousClassDeclaration, expectedSourceRange, source);
//		List bodyDeclarations = anonymousClassDeclaration.bodyDeclarations();
//		assertEquals("Wrong size", 1, bodyDeclarations.size()); //$NON-NLS-1$
//		BodyDeclaration bodyDeclaration = (BodyDeclaration) bodyDeclarations.get(0);
//		assertTrue("Not a method declaration", bodyDeclaration instanceof FunctionDeclaration); //$NON-NLS-1$
//		FunctionDeclaration methodDeclaration = (FunctionDeclaration) bodyDeclaration;
//		assertEquals("Wrong name", "m", methodDeclaration.getName().getIdentifier()); //$NON-NLS-1$ //$NON-NLS-2$
//	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10698
	 */
	public void test0265() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0265", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
	}

//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10759
//	 */
//	public void test0266() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0266", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0, 1, 0);
//		assertTrue("Not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
//		Type type = variableDeclarationStatement.getType();
//		checkSourceRange(type, "Inner\\u005b]", source); //$NON-NLS-1$
//		assertTrue("Not an array type", type.isArrayType()); //$NON-NLS-1$
//		ArrayType arrayType = (ArrayType) type;
//		Type type2 = arrayType.getElementType();
//		assertTrue("Not a simple type", type2.isSimpleType()); //$NON-NLS-1$
//		SimpleType simpleType = (SimpleType) type2;
//		checkSourceRange(simpleType, "Inner", source); //$NON-NLS-1$
//		Name name = simpleType.getName();
//		assertTrue("not a simple name", name.isSimpleName()); //$NON-NLS-1$
//		SimpleName simpleName = (SimpleName) name;
//		checkSourceRange(simpleName, "Inner", source); //$NON-NLS-1$
//	}
//
//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10759
//	 */
//	public void test0267() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0267", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0, 1, 0);
//		assertTrue("Not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
//		Type type = variableDeclarationStatement.getType();
//		checkSourceRange(type, "Inner[]", source); //$NON-NLS-1$
//		assertTrue("Not an array type", type.isArrayType()); //$NON-NLS-1$
//		ArrayType arrayType = (ArrayType) type;
//		Type type2 = arrayType.getElementType();
//		assertTrue("Not a simple type", type2.isSimpleType()); //$NON-NLS-1$
//		SimpleType simpleType = (SimpleType) type2;
//		checkSourceRange(simpleType, "Inner", source); //$NON-NLS-1$
//		Name name = simpleType.getName();
//		assertTrue("not a simple name", name.isSimpleName()); //$NON-NLS-1$
//		SimpleName simpleName = (SimpleName) name;
//		checkSourceRange(simpleName, "Inner", source); //$NON-NLS-1$
//	}
//
//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10759
//	 */
//	public void test0268() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0268", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0, 1, 0);
//		assertTrue("Not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
//		Type type = variableDeclarationStatement.getType();
//		checkSourceRange(type, "test0268.Test.Inner[]", source); //$NON-NLS-1$
//		assertTrue("Not an array type", type.isArrayType()); //$NON-NLS-1$
//		ArrayType arrayType = (ArrayType) type;
//		Type type2 = arrayType.getElementType();
//		assertTrue("Not a simple type", type2.isSimpleType()); //$NON-NLS-1$
//		SimpleType simpleType = (SimpleType) type2;
//		checkSourceRange(simpleType, "test0268.Test.Inner", source); //$NON-NLS-1$
//		Name name = simpleType.getName();
//		assertTrue("not a qualified name", name.isQualifiedName()); //$NON-NLS-1$
//		checkSourceRange(name, "test0268.Test.Inner", source); //$NON-NLS-1$
//	}
//	
//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10759
//	 */
//	public void test0269() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0269", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0, 1, 0);
//		assertTrue("Not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
//		Type type = variableDeclarationStatement.getType();
//		checkSourceRange(type, "test0269.Test.Inner[/**/]", source); //$NON-NLS-1$
//		assertTrue("Not an array type", type.isArrayType()); //$NON-NLS-1$
//		ArrayType arrayType = (ArrayType) type;
//		Type type2 = arrayType.getElementType();
//		assertTrue("Not a simple type", type2.isSimpleType()); //$NON-NLS-1$
//		SimpleType simpleType = (SimpleType) type2;
//		checkSourceRange(simpleType, "test0269.Test.Inner", source); //$NON-NLS-1$
//		Name name = simpleType.getName();
//		assertTrue("not a qualified name", name.isQualifiedName()); //$NON-NLS-1$
//		checkSourceRange(name, "test0269.Test.Inner", source); //$NON-NLS-1$
//	}
//
//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10759
//	 */
//	public void test0270() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0270", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0, 1, 0);
//		assertTrue("Not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
//		Type type = variableDeclarationStatement.getType();
//		checkSourceRange(type, "test0270.Test.Inner", source); //$NON-NLS-1$
//		assertTrue("Not a simple type", type.isSimpleType()); //$NON-NLS-1$
//		SimpleType simpleType = (SimpleType) type;
//		Name name = simpleType.getName();
//		assertTrue("not a qualified name", name.isQualifiedName()); //$NON-NLS-1$
//		checkSourceRange(name, "test0270.Test.Inner", source); //$NON-NLS-1$
//	}
//
//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10759
//	 */
//	public void test0271() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0271", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0, 1, 0);
//		assertTrue("Not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
//		Type type = variableDeclarationStatement.getType();
//		checkSourceRange(type, "test0271.Test.Inner[]", source); //$NON-NLS-1$
//		assertTrue("Not an array type", type.isArrayType()); //$NON-NLS-1$
//		ArrayType arrayType = (ArrayType) type;
//		Type type2 = arrayType.getElementType();
//		assertTrue("Not a simple type", type2.isSimpleType()); //$NON-NLS-1$
//		SimpleType simpleType = (SimpleType) type2;
//		checkSourceRange(simpleType, "test0271.Test.Inner", source); //$NON-NLS-1$
//		Name name = simpleType.getName();
//		assertTrue("not a qualified name", name.isQualifiedName()); //$NON-NLS-1$
//		checkSourceRange(name, "test0271.Test.Inner", source); //$NON-NLS-1$
//	}
//
	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10843
	 */
	public void test0272() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0272", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertTrue("Not a For statement", node instanceof ForStatement); //$NON-NLS-1$
		ForStatement forStatement = (ForStatement) node;
		checkSourceRange(forStatement, "for (var i= 0; i < 10; i++) foo();", source); //$NON-NLS-1$
		Statement action = forStatement.getBody();
		checkSourceRange(action, "foo();", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10843
	 */
	public void test0273() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0273", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertTrue("Not a For statement", node instanceof ForStatement); //$NON-NLS-1$
		ForStatement forStatement = (ForStatement) node;
		checkSourceRange(forStatement, "for (var i= 0; i < 10; i++) { foo(); }", source); //$NON-NLS-1$
		Statement action = forStatement.getBody();
		checkSourceRange(action, "{ foo(); }", source); //$NON-NLS-1$
		assertTrue("Not a block", action instanceof Block); //$NON-NLS-1$
		Block block = (Block) action;
		List statements = block.statements();
		assertEquals("Wrong size", 1, statements.size()); //$NON-NLS-1$
		Statement stmt = (Statement) statements.get(0);
		checkSourceRange(stmt, "foo();", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10843
	 */
	public void test0274() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0274", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0, 1);
		assertTrue("Not a While statement", node instanceof WhileStatement); //$NON-NLS-1$
		WhileStatement whileStatement = (WhileStatement) node;
		checkSourceRange(whileStatement, "while (i < 10) { foo(i++); }", source); //$NON-NLS-1$
		Statement action = whileStatement.getBody();
		checkSourceRange(action, "{ foo(i++); }", source); //$NON-NLS-1$
		assertTrue("Not a block", action instanceof Block); //$NON-NLS-1$
		Block block = (Block) action;
		List statements = block.statements();
		assertEquals("Wrong size", 1, statements.size()); //$NON-NLS-1$
		Statement stmt = (Statement) statements.get(0);
		checkSourceRange(stmt, "foo(i++);", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10843
	 */
	public void test0275() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0275", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0, 1);
		assertTrue("Not a While statement", node instanceof WhileStatement); //$NON-NLS-1$
		WhileStatement whileStatement = (WhileStatement) node;
		checkSourceRange(whileStatement, "while (i < 10) foo(i++);", source); //$NON-NLS-1$
		Statement action = whileStatement.getBody();
		checkSourceRange(action, "foo(i++);", source); //$NON-NLS-1$
	}
	
	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10798
	 */
	public void test0276() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0276", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertTrue("Not a method declaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
		FunctionDeclaration methodDeclaration = (FunctionDeclaration) node;
		String expectedSource = 
			"function foo() {\n" + //$NON-NLS-1$
			"		foo();\n" + //$NON-NLS-1$
			"	}"; //$NON-NLS-1$
		checkSourceRange(methodDeclaration, expectedSource, source);
		expectedSource = 
			"{\n" + //$NON-NLS-1$
			"		foo();\n" + //$NON-NLS-1$
			"	}";		 //$NON-NLS-1$
		checkSourceRange(methodDeclaration.getBody(), expectedSource, source);
	}
		
	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10798
	 */
	public void test0277() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0277", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertTrue("Not a method declaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
		FunctionDeclaration methodDeclaration = (FunctionDeclaration) node;
		String expectedSource = 
			"function foo() {\n" + //$NON-NLS-1$
			"	}"; //$NON-NLS-1$
		checkSourceRange(methodDeclaration, expectedSource, source);
		expectedSource = 
			"{\n" + //$NON-NLS-1$
			"	}";		 //$NON-NLS-1$
		checkSourceRange(methodDeclaration.getBody(), expectedSource, source);
	}

//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10861
//	 */
//	public void test0278() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0278", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0, 0);
//		assertTrue("Not a Field declaration", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
//		checkSourceRange(fieldDeclaration, "Class c = java.lang.String.class;", source); //$NON-NLS-1$
//		List fragments = fieldDeclaration.fragments();
//		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
//		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
//		Expression expression = variableDeclarationFragment.getInitializer();
//		assertTrue("Not a type literal", expression instanceof TypeLiteral); //$NON-NLS-1$
//		ITypeBinding typeBinding = expression.resolveTypeBinding();
//		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "Class", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}
//	
//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10861
//	 */
//	public void test0279() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0279", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0, 0,0);
//		assertTrue("Not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
//		checkSourceRange(variableDeclarationStatement, "Class c = java.lang.String.class;", source); //$NON-NLS-1$
//		List fragments = variableDeclarationStatement.fragments();
//		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
//		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
//		Expression expression = variableDeclarationFragment.getInitializer();
//		assertTrue("Not a type literal", expression instanceof TypeLiteral); //$NON-NLS-1$
//		ITypeBinding typeBinding = expression.resolveTypeBinding();
//		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "Class", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10865
	 * Check well known types
	 */
	public void test0280() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0280", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		AST newAst = result.getAST();
		ITypeBinding typeBinding = newAst.resolveWellKnownType("Boolean"); //$NON-NLS-1$
		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
		assertEquals("Wrong name", "Boolean", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		typeBinding = newAst.resolveWellKnownType("String"); //$NON-NLS-1$
		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
		assertEquals("Wrong name", "String", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		typeBinding = newAst.resolveWellKnownType("Number"); //$NON-NLS-1$
		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
		assertEquals("Wrong name", "Number", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		typeBinding = newAst.resolveWellKnownType("short"); //$NON-NLS-1$
//		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "short", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		typeBinding = newAst.resolveWellKnownType("int"); //$NON-NLS-1$
//		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "int", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		typeBinding = newAst.resolveWellKnownType("long"); //$NON-NLS-1$
//		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "long", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		typeBinding = newAst.resolveWellKnownType("float"); //$NON-NLS-1$
//		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "float", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		typeBinding = newAst.resolveWellKnownType("double"); //$NON-NLS-1$
//		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "double", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		typeBinding = newAst.resolveWellKnownType("void"); //$NON-NLS-1$
//		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "void", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		typeBinding = newAst.resolveWellKnownType("java.lang.Object"); //$NON-NLS-1$
//		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "Object", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		typeBinding = newAst.resolveWellKnownType("java.lang.String"); //$NON-NLS-1$
//		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "String", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		typeBinding = newAst.resolveWellKnownType("java.lang.StringBuffer"); //$NON-NLS-1$
//		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "StringBuffer", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		typeBinding = newAst.resolveWellKnownType("java.lang.Throwable"); //$NON-NLS-1$
//		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "Throwable", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		typeBinding = newAst.resolveWellKnownType("java.lang.Exception"); //$NON-NLS-1$
//		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "Exception", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		typeBinding = newAst.resolveWellKnownType("java.lang.RuntimeException"); //$NON-NLS-1$
//		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "RuntimeException", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		typeBinding = newAst.resolveWellKnownType("java.lang.Error"); //$NON-NLS-1$
//		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "Error", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		typeBinding = newAst.resolveWellKnownType("java.lang.Class"); //$NON-NLS-1$
//		assertNotNull("No typeBinding", typeBinding); //$NON-NLS-1$
//		assertEquals("Wrong name", "Class", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		typeBinding = newAst.resolveWellKnownType("java.lang.Runnable"); //$NON-NLS-1$
//		assertNull("typeBinding not null", typeBinding); //$NON-NLS-1$
//		typeBinding = newAst.resolveWellKnownType("java.lang.Cloneable"); //$NON-NLS-1$
//		assertNotNull("typeBinding not null", typeBinding); //$NON-NLS-1$
//		typeBinding = newAst.resolveWellKnownType("java.io.Serializable"); //$NON-NLS-1$
//		assertNotNull("typeBinding not null", typeBinding); //$NON-NLS-1$				
//		typeBinding = newAst.resolveWellKnownType("java.lang.Boolean"); //$NON-NLS-1$
//		assertNotNull("typeBinding not null", typeBinding); //$NON-NLS-1$				
//		typeBinding = newAst.resolveWellKnownType("java.lang.Byte"); //$NON-NLS-1$
//		assertNotNull("typeBinding not null", typeBinding); //$NON-NLS-1$				
//		typeBinding = newAst.resolveWellKnownType("java.lang.Character"); //$NON-NLS-1$
//		assertNotNull("typeBinding not null", typeBinding); //$NON-NLS-1$				
//		typeBinding = newAst.resolveWellKnownType("java.lang.Double"); //$NON-NLS-1$
//		assertNotNull("typeBinding not null", typeBinding); //$NON-NLS-1$				
//		typeBinding = newAst.resolveWellKnownType("java.lang.Float"); //$NON-NLS-1$
//		assertNotNull("typeBinding not null", typeBinding); //$NON-NLS-1$				
//		typeBinding = newAst.resolveWellKnownType("java.lang.Integer"); //$NON-NLS-1$
//		assertNotNull("typeBinding not null", typeBinding); //$NON-NLS-1$				
//		typeBinding = newAst.resolveWellKnownType("java.lang.Long"); //$NON-NLS-1$
//		assertNotNull("typeBinding not null", typeBinding); //$NON-NLS-1$				
//		typeBinding = newAst.resolveWellKnownType("java.lang.Short"); //$NON-NLS-1$
//		assertNotNull("typeBinding not null", typeBinding); //$NON-NLS-1$				
//		typeBinding = newAst.resolveWellKnownType("java.lang.Void"); //$NON-NLS-1$
//		assertNotNull("typeBinding not null", typeBinding); //$NON-NLS-1$				
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10874
	 */
	public void test0281() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0281", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertTrue("Not a VariableDeclarationStatement declaration", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
		checkSourceRange(fieldDeclaration, "var o= /*]*/new Object()/*[*/;", source); //$NON-NLS-1$
		List fragments = fieldDeclaration.fragments();
		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		checkSourceRange(expression, "new Object()", source); //$NON-NLS-1$
	}
	
	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10874
	 */
	public void test0282() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0282", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertTrue("Not a VariableDeclarationStatement declaration", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
		checkSourceRange(fieldDeclaration, "var b = /*]*/true/*[*/;", source); //$NON-NLS-1$
		List fragments = fieldDeclaration.fragments();
		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		checkSourceRange(expression, "true", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10874
	 */
	public void test0283() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0283", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertTrue("Not a VariableDeclarationStatement declaration", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
		checkSourceRange(fieldDeclaration, "var c = /*]*/'c'/*[*/;", source); //$NON-NLS-1$
		List fragments = fieldDeclaration.fragments();
		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		checkSourceRange(expression, "'c'", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10874
	 */
	public void test0284() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0284", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertTrue("Not a Field declaration", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
		checkSourceRange(fieldDeclaration, "var o = /*]*/null/*[*/;", source); //$NON-NLS-1$
		List fragments = fieldDeclaration.fragments();
		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		checkSourceRange(expression, "null", source); //$NON-NLS-1$
	}

//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10874
//	 */
//	public void test0285() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0285", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0, 0);
//		assertTrue("Not a Field declaration", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
//		checkSourceRange(fieldDeclaration, "var o = /*]*/Object.class/*[*/;", source); //$NON-NLS-1$
//		List fragments = fieldDeclaration.fragments();
//		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
//		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
//		Expression expression = variableDeclarationFragment.getInitializer();
//		checkSourceRange(expression, "Object.class", source); //$NON-NLS-1$
//	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10874
	 */
	public void test0286() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0286", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertTrue("Not a Field declaration", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
		checkSourceRange(fieldDeclaration, "var i = /**/(2)/**/;", source); //$NON-NLS-1$
		List fragments = fieldDeclaration.fragments();
		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		checkSourceRange(expression, "(2)", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10874
	 */
	public void test0287() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0287", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertTrue("Not a VariableDeclarationStatement declaration", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
		checkSourceRange(fieldDeclaration, "var tab = /**/new String/**/;", source); //$NON-NLS-1$
		List fragments = fieldDeclaration.fragments();
		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		checkSourceRange(expression, "new String", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10874
	 */
	public void test0288() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0288", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertTrue("Not a VariableDeclarationStatement declaration", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
		checkSourceRange(fieldDeclaration, "var tab = /**/[]/**/;", source); //$NON-NLS-1$
		List fragments = fieldDeclaration.fragments();
		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		checkSourceRange(expression, "[]", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10874
	 */
	public void test0289() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0289", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 1);
		assertTrue("Not a VariableDeclarationStatement declaration", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
		checkSourceRange(fieldDeclaration, "var s = /**/tab1[0]/**/;", source); //$NON-NLS-1$
		List fragments = fieldDeclaration.fragments();
		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		checkSourceRange(expression, "tab1[0]", source); //$NON-NLS-1$
	}
//
//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10874
//	 */
//	public void test0290() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0290", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0, 0);
//		assertTrue("Not a Field declaration", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
//		checkSourceRange(fieldDeclaration, "var o = /*]*/new java.lang.Object()/*[*/;", source); //$NON-NLS-1$
//		List fragments = fieldDeclaration.fragments();
//		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
//		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
//		Expression expression = variableDeclarationFragment.getInitializer();
//		checkSourceRange(expression, "new java.lang.Object()", source); //$NON-NLS-1$
//	}
//
//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10898
//	 */
//	public void test0291() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0291", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit unit = (JavaScriptUnit) result;
//		assertEquals("no errors", 1, unit.getMessages().length); //$NON-NLS-1$
//		assertEquals("no errors", 1, unit.getProblems().length); //$NON-NLS-1$
//	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10913
	 */
	public void Xtest0292() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0292", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertTrue("Not a return statement", node instanceof ReturnStatement); //$NON-NLS-1$
		ReturnStatement returnStatement = (ReturnStatement) node;
		Expression expression = returnStatement.getExpression();
		assertTrue("Not a FieldAccess", expression instanceof FieldAccess); //$NON-NLS-1$
		FieldAccess fieldAccess = (FieldAccess) expression;
		SimpleName simpleName = fieldAccess.getName();
		assertEquals("Wrong name", "x", simpleName.getIdentifier()); //$NON-NLS-1$ //$NON-NLS-2$
		IBinding binding = simpleName.resolveBinding();
		assertNotNull("NO binding", binding); //$NON-NLS-1$
		assertTrue("Not a variable binding", binding instanceof IVariableBinding); //$NON-NLS-1$
		assertEquals("wrong name", "x", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		Expression expr = fieldAccess.getExpression();
		assertTrue("Not a simpleName", expr instanceof SimpleName); //$NON-NLS-1$
		SimpleName simpleName2 = (SimpleName) expr;
		IBinding binding2 = simpleName2.resolveBinding();
		assertNotNull("No binding2", binding2); //$NON-NLS-1$
		assertTrue("Not a type binding", binding2 instanceof ITypeBinding); //$NON-NLS-1$
		assertEquals("Wrong name", "Test", binding2.getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}

//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10933
// 	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10935
//	 */
//	public void test0293() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0293", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
//		List fragments = variableDeclarationStatement.fragments();
//		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
//		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
//		Expression expression = variableDeclarationFragment.getInitializer();
//		assertTrue("Not a class instance creation", expression instanceof ClassInstanceCreation); //$NON-NLS-1$
//		ClassInstanceCreation classInstanceCreation = (ClassInstanceCreation) expression;
//		AnonymousClassDeclaration anonymousClassDeclaration = classInstanceCreation.getAnonymousClassDeclaration();
//		assertNotNull("No body", anonymousClassDeclaration); //$NON-NLS-1$
//		String expectedSource = 
//				"{\n" + //$NON-NLS-1$
//				"			public void run() {\n" + //$NON-NLS-1$
//				"				/*]*/foo();/*[*/\n" + //$NON-NLS-1$
//				"			}\n" + //$NON-NLS-1$
//				"		}"; //$NON-NLS-1$
//		checkSourceRange(anonymousClassDeclaration, expectedSource, source);
//		expectedSource =
//				"run= new Runnable() {\n" + //$NON-NLS-1$
//				"			public void run() {\n" + //$NON-NLS-1$
//				"				/*]*/foo();/*[*/\n" + //$NON-NLS-1$
//				"			}\n" + //$NON-NLS-1$
//				"		}"; //$NON-NLS-1$
//		checkSourceRange(variableDeclarationFragment, expectedSource, source);
//	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10984
	 */
	public void test0294() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0294", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertTrue("Not a method declaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
		FunctionDeclaration methodDeclaration = (FunctionDeclaration) node;
		String expectedSource = 
				"function fails() {\n" + //$NON-NLS-1$
				"		foo()\n" + //$NON-NLS-1$
				"	}"; //$NON-NLS-1$
		checkSourceRange(methodDeclaration, expectedSource, source);
		Block block = methodDeclaration.getBody();
		expectedSource = 
				"{\n" + //$NON-NLS-1$
				"		foo()\n" + //$NON-NLS-1$
				"	}"; //$NON-NLS-1$
		checkSourceRange(block, expectedSource, source);	
		node = getASTNode(compilationUnit, 0, 1);	
		assertTrue("Not a method declaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
		methodDeclaration = (FunctionDeclaration) node;
		block = methodDeclaration.getBody();
		List statements = block.statements();
		assertEquals("wrong size", 1, statements.size()); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10986
	 */
	public void test0295() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0295", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		assertEquals("Wrong size", 2, compilationUnit.getMessages().length); //$NON-NLS-1$
//		assertEquals("Wrong size", 2, compilationUnit.getProblems().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 1, 0);
		assertTrue("Not an ExpressionStatement", node instanceof ExpressionStatement); //$NON-NLS-1$
		ExpressionStatement expressionStatement = (ExpressionStatement) node;
		Expression expression = expressionStatement.getExpression();
		assertTrue("not a method invocation", expression instanceof FunctionInvocation); //$NON-NLS-1$
		FunctionInvocation methodInvocation = (FunctionInvocation) expression;
		ITypeBinding typeBinding = methodInvocation.resolveTypeBinding();
		assertNotNull("type binding is null", typeBinding); //$NON-NLS-1$
	}


	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10984
	 */
	public void test0296() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0296", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertTrue("Not a method declaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
		FunctionDeclaration methodDeclaration = (FunctionDeclaration) node;
		String expectedSource = 
				"function fails() {\n" + //$NON-NLS-1$
				"		foo()\n" + //$NON-NLS-1$
				"	}"; //$NON-NLS-1$
		checkSourceRange(methodDeclaration, expectedSource, source);
		Block block = methodDeclaration.getBody();
		expectedSource = 
				"{\n" + //$NON-NLS-1$
				"		foo()\n" + //$NON-NLS-1$
				"	}"; //$NON-NLS-1$
		checkSourceRange(block, expectedSource, source);	
		node = getASTNode(compilationUnit, 0, 1);	
		assertTrue("Not a method declaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
		methodDeclaration = (FunctionDeclaration) node;
		block = methodDeclaration.getBody();
		List statements = block.statements();
		assertEquals("wrong size", 1, statements.size()); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11037
	 */
	public void test0297() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0297", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		runConversion(sourceUnit, false);
	}
//
//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10984
//	 */
//	public void test0298() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0298", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, false);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
//		assertTrue("Not a ReturnStatement", node instanceof ReturnStatement); //$NON-NLS-1$
//		ReturnStatement returnStatement = (ReturnStatement) node;
//		Expression expression = returnStatement.getExpression();
//		checkSourceRange(expression, "a().length != 3", source); //$NON-NLS-1$
//	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10874
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11104
	 */
	public void test0299() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0299", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertTrue("Not a Field declaration", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
		checkSourceRange(fieldDeclaration, "var i = (/**/2/**/);", source); //$NON-NLS-1$
		List fragments = fieldDeclaration.fragments();
		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		assertTrue("Not a parenthesized expression", expression instanceof ParenthesizedExpression); //$NON-NLS-1$
		ParenthesizedExpression parenthesizedExpression = (ParenthesizedExpression) expression;
		Expression expression2 = parenthesizedExpression.getExpression();
		checkSourceRange(expression2, "2", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10874
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11104
	 */
	public void test0300() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0300", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertTrue("Not a Field declaration", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
		checkSourceRange(fieldDeclaration, "var b = /**/true/**/;", source); //$NON-NLS-1$
		List fragments = fieldDeclaration.fragments();
		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		checkSourceRange(expression, "true", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=10874
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11104
	 */
	public void test0301() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0301", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertTrue("Not a Field declaration", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
		checkSourceRange(fieldDeclaration, "var o = /**/null/**/;", source); //$NON-NLS-1$
		List fragments = fieldDeclaration.fragments();
		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		checkSourceRange(expression, "null", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11106
	 */
	public void test0302() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0302", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertTrue("Not a DoStatement", node instanceof DoStatement); //$NON-NLS-1$
		DoStatement doStatement = (DoStatement) node;
		String expectedSource = 
				"do\n" +   //$NON-NLS-1$
				"			foo();\n" +  //$NON-NLS-1$
				"		while(1 < 10);"; //$NON-NLS-1$
		checkSourceRange(doStatement, expectedSource, source);
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11129
	 */
	public void test0303() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0303", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0, 1);
		assertTrue("Not an ExpressionStatement", node instanceof ExpressionStatement); //$NON-NLS-1$
		ExpressionStatement expressionStatement = (ExpressionStatement) node;
		Expression expression2 = expressionStatement.getExpression();
		assertTrue("Not an Assignement", expression2 instanceof Assignment); //$NON-NLS-1$
		Assignment assignment = (Assignment) expression2;
		Expression expression = assignment.getRightHandSide();
//		assertTrue("Not a CastExpression", expression instanceof CastExpression); //$NON-NLS-1$
//		CastExpression castExpression = (CastExpression) expression;
		ITypeBinding typeBinding = expression.resolveTypeBinding();
		assertNotNull("No binding", typeBinding); //$NON-NLS-1$
		assertEquals("Wrong name", "Number", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		Type type = castExpression.getType();
//		ITypeBinding typeBinding2 = type.resolveBinding();
//		assertNotNull("No binding2", typeBinding2); //$NON-NLS-1$
//		assertEquals("Wrong name", "char", typeBinding2.getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11151
	 */
	public void test0304() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0304", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertTrue("not a method declaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
		checkSourceRange(node, "function foo(arg);", source); //$NON-NLS-1$
		FunctionDeclaration methodDeclaration = (FunctionDeclaration) node;
		Block block = methodDeclaration.getBody();
		assertNull("Has a body", block); //$NON-NLS-1$
	}
//
//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11125
//	 */
//	public void test0305() {
//		char[] source = 
//				("" +  //$NON-NLS-1$
//				"	function  foo(arg) {}\n" +  //$NON-NLS-1$
//				"").toCharArray(); //$NON-NLS-1$
//		IJavaScriptProject project = getJavaProject("Converter"); //$NON-NLS-1$
//		ASTNode result = runConversion(source, "Test.js", project, true); //$NON-NLS-1$
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0);
//		assertTrue("not a TypeDeclaration", node instanceof TypeDeclaration); //$NON-NLS-1$
//		TypeDeclaration typeDeclaration = (TypeDeclaration) node;
//		ITypeBinding typeBinding = typeDeclaration.resolveBinding();
//		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
////		assertEquals("Wrong name", "Test", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
////		assertEquals("Wrong package", "test0305", typeBinding.getPackage().getName()); //$NON-NLS-1$ //$NON-NLS-2$
////		assertTrue("Not an interface", typeBinding.isClass()); //$NON-NLS-1$
//	}

//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11125
//	 */
//	public void test0306() {
//		char[] source = 
//				("" +  //$NON-NLS-1$
//				"	function  foo(arg) {}\n" +  //$NON-NLS-1$
//				"").toCharArray(); //$NON-NLS-1$
//		IJavaScriptProject project = getJavaProject("Converter"); //$NON-NLS-1$
//		ASTNode result = runConversion(source, "Object.js", project, true); //$NON-NLS-1$
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0);
//		assertTrue("not a TypeDeclaration", node instanceof TypeDeclaration); //$NON-NLS-1$
//		TypeDeclaration typeDeclaration = (TypeDeclaration) node;
//		ITypeBinding typeBinding = typeDeclaration.resolveBinding();
//		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
////		assertEquals("Wrong name", "Object", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
////		assertEquals("Wrong package", "java.lang", typeBinding.getPackage().getName()); //$NON-NLS-1$ //$NON-NLS-2$
////		assertTrue("Not an interface", typeBinding.isClass()); //$NON-NLS-1$
//		assertEquals("Wrong size", 2, typeBinding.getDeclaredMethods().length); //$NON-NLS-1$
//	}
//
//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11371
//	 */
//	public void test0307() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0307", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0, 1, 0);
//		assertTrue("not a method declaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
//		FunctionDeclaration methodDeclaration = (FunctionDeclaration) node;
//		Block block = methodDeclaration.getBody();
//		assertNotNull("No body", block); //$NON-NLS-1$
//		List statements = block.statements();
//		assertEquals("wrong size", 1, statements.size()); //$NON-NLS-1$
//		Statement statement = (Statement) statements.get(0);
//		assertTrue("Not a super constructor invocation", statement instanceof SuperConstructorInvocation); //$NON-NLS-1$
//		checkSourceRange(statement, "super(10);", source);
//	}
//
//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11371
//	 */
//	public void test0308() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0308", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0, 1, 0);
//		assertTrue("not a method declaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
//		FunctionDeclaration methodDeclaration = (FunctionDeclaration) node;
//		Block block = methodDeclaration.getBody();
//		assertNotNull("No body", block); //$NON-NLS-1$
//		List statements = block.statements();
//		assertEquals("wrong size", 1, statements.size()); //$NON-NLS-1$
//		Statement statement = (Statement) statements.get(0);
//		assertTrue("Not a super constructor invocation", statement instanceof SuperConstructorInvocation); //$NON-NLS-1$
//		SuperConstructorInvocation superConstructorInvocation = (SuperConstructorInvocation) statement;
//		IFunctionBinding methodBinding = superConstructorInvocation.resolveConstructorBinding();
//		assertNotNull("No methodBinding", methodBinding); //$NON-NLS-1$
//		IFunctionBinding methodBinding2 = methodDeclaration.resolveBinding();
//		assertNotNull("No methodBinding2", methodBinding2); //$NON-NLS-1$
//	}
	
	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11380
	 */
	public void Xtest0309() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0309", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertTrue("not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
		List fragments = variableDeclarationStatement.fragments();
		assertEquals("wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		assertTrue("Not a conditional expression", expression instanceof ConditionalExpression); //$NON-NLS-1$
		ConditionalExpression conditionalExpression = (ConditionalExpression) expression;
		ITypeBinding typeBinding = conditionalExpression.resolveTypeBinding();
		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
		assertEquals("wrong name", "Number", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11380
	 */
	public void test0310() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0310", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertTrue("not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
		List fragments = fieldDeclaration.fragments();
		assertEquals("wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		assertTrue("Not a fieldAccess", expression instanceof FieldAccess); //$NON-NLS-1$
		FieldAccess fieldAccess = (FieldAccess) expression;
		SimpleName qualifier = (SimpleName)fieldAccess.getExpression();
		IBinding binding = qualifier.resolveBinding();
		assertNotNull("No binding", binding); //$NON-NLS-1$
		assertEquals("wrong name", "I", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}

//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11638
//	 */
//	public void test0311() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0311", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0, 0, 1);
//		assertTrue("Not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
//		List fragments = variableDeclarationStatement.fragments();
//		assertEquals("wrong size", 1, fragments.size()); //$NON-NLS-1$
//		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
//		Expression expression = variableDeclarationFragment.getInitializer();
//		assertTrue("not a class instance creation", expression instanceof ClassInstanceCreation); //$NON-NLS-1$
//		ClassInstanceCreation classInstanceCreation = (ClassInstanceCreation) expression;
//		AnonymousClassDeclaration anonymousClassDeclaration = classInstanceCreation.getAnonymousClassDeclaration();
//		assertNotNull("No body", anonymousClassDeclaration); //$NON-NLS-1$
//		List bodyDeclarations = anonymousClassDeclaration.bodyDeclarations();
//		assertEquals("wrong size for body declarations", 1, bodyDeclarations.size()); //$NON-NLS-1$
//		BodyDeclaration bodyDeclaration = (BodyDeclaration) bodyDeclarations.get(0);
//		assertTrue("Not a method declaration", bodyDeclaration instanceof FunctionDeclaration); //$NON-NLS-1$
//		FunctionDeclaration methodDeclaration = (FunctionDeclaration) bodyDeclaration;
//		Block block = methodDeclaration.getBody();
//		assertNotNull("no body", block); //$NON-NLS-1$
//		List statements = block.statements();
//		assertEquals("Wrong size for statements", 1, statements.size()); //$NON-NLS-1$
//		Statement statement = (Statement) statements.get(0);
//		assertTrue("not a variable declaration statement", statement instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement2 = (VariableDeclarationStatement) statement;
//		List fragments2 = variableDeclarationStatement2.fragments();
//		assertEquals("wrong size for fragments2", 1, fragments2.size()); //$NON-NLS-1$
//		VariableDeclarationFragment variableDeclarationFragment2 = (VariableDeclarationFragment) fragments2.get(0);
//		Expression expression2 = variableDeclarationFragment2.getInitializer();
//		assertTrue("Not a name", expression2 instanceof Name); //$NON-NLS-1$
//		Name name = (Name) expression2;
//		checkSourceRange(name, "j", source); //$NON-NLS-1$
//		IBinding binding = name.resolveBinding();
//		ASTNode declaringNode = compilationUnit.findDeclaringNode(binding);
//		assertNotNull("No declaring node", declaringNode); //$NON-NLS-1$
//		checkSourceRange(declaringNode, "var j", source); //$NON-NLS-1$
//		assertTrue("Not a single variable declaration", declaringNode instanceof SingleVariableDeclaration); //$NON-NLS-1$
//	}

//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11638
//	 * There is a error in this source. A is unresolved. Then there is no
//	 * declaring node.
//	 */
//	public void test0312() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0312", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0, 0, 1);
//		assertTrue("Not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
//		List fragments = variableDeclarationStatement.fragments();
//		assertEquals("wrong size", 1, fragments.size()); //$NON-NLS-1$
//		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
//		Expression expression = variableDeclarationFragment.getInitializer();
//		assertTrue("not a class instance creation", expression instanceof ClassInstanceCreation); //$NON-NLS-1$
//		ClassInstanceCreation classInstanceCreation = (ClassInstanceCreation) expression;
//		AnonymousClassDeclaration anonymousClassDeclaration = classInstanceCreation.getAnonymousClassDeclaration();
//		assertNotNull("No body", anonymousClassDeclaration); //$NON-NLS-1$
//		List bodyDeclarations = anonymousClassDeclaration.bodyDeclarations();
//		assertEquals("wrong size for body declarations", 1, bodyDeclarations.size()); //$NON-NLS-1$
//		BodyDeclaration bodyDeclaration = (BodyDeclaration) bodyDeclarations.get(0);
//		assertTrue("Not a method declaration", bodyDeclaration instanceof FunctionDeclaration); //$NON-NLS-1$
//		FunctionDeclaration methodDeclaration = (FunctionDeclaration) bodyDeclaration;
//		Block block = methodDeclaration.getBody();
//		assertNotNull("no body", block); //$NON-NLS-1$
//		List statements = block.statements();
//		assertEquals("Wrong size for statements", 1, statements.size()); //$NON-NLS-1$
//		Statement statement = (Statement) statements.get(0);
//		assertTrue("not a variable declaration statement", statement instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement2 = (VariableDeclarationStatement) statement;
//		List fragments2 = variableDeclarationStatement2.fragments();
//		assertEquals("wrong size for fragments2", 1, fragments2.size()); //$NON-NLS-1$
//		VariableDeclarationFragment variableDeclarationFragment2 = (VariableDeclarationFragment) fragments2.get(0);
//		Expression expression2 = variableDeclarationFragment2.getInitializer();
//		assertTrue("Not a name", expression2 instanceof Name); //$NON-NLS-1$
//		Name name = (Name) expression2;
//		checkSourceRange(name, "j", source); //$NON-NLS-1$
//		IBinding binding = name.resolveBinding();
//		ASTNode declaringNode = compilationUnit.findDeclaringNode(binding);
//		assertNull("No declaring node is available", declaringNode); //$NON-NLS-1$
//	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11659
	 */
	public void test0313() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0313", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertTrue("Not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
		List fragments = variableDeclarationStatement.fragments();
		assertEquals("wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		assertTrue("Not an InfixExpression", expression instanceof InfixExpression); //$NON-NLS-1$
		InfixExpression infixExpression = (InfixExpression) expression;
		checkSourceRange(infixExpression, "i+j", source); //$NON-NLS-1$
		Expression expression2 = infixExpression.getLeftOperand();
		checkSourceRange(expression2, "i", source); //$NON-NLS-1$
		assertTrue("Not a name", expression2 instanceof Name); //$NON-NLS-1$
		Name name = (Name) expression2;
		IBinding binding = name.resolveBinding();
		assertNotNull("No binding", binding); //$NON-NLS-1$
		ASTNode astNode = compilationUnit.findDeclaringNode(binding);
		assertNotNull("No declaring node", astNode); //$NON-NLS-1$
		checkSourceRange(astNode, "i", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=12326
	 */
	public void test0314() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0314", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		assertNotNull("No result", result); //$NON-NLS-1$
		assertTrue("Not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("Wrong line number", 1, compilationUnit.getLineNumber(0)); //$NON-NLS-1$
		// ensure that last character is on the last line
		assertEquals("Wrong line number", 1, compilationUnit.getLineNumber(source.length - 1)); //$NON-NLS-1$
		// source.length is beyond the size of the compilation unit source
		assertEquals("Wrong line number", -1, compilationUnit.getLineNumber(source.length)); //$NON-NLS-1$
	}
		
	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=12326
	 */
	public void test0315() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0315", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertTrue("Not a Return statement", node instanceof ReturnStatement); //$NON-NLS-1$
		ReturnStatement returnStatement = (ReturnStatement) node;
		Expression expression = returnStatement.getExpression();
		assertTrue("Not an InfixExpression expression", expression instanceof InfixExpression); //$NON-NLS-1$
		InfixExpression instanceOfExpression = (InfixExpression) expression;
		Expression rightOperand = instanceOfExpression.getRightOperand();
		assertTrue("Not a SimpleName", rightOperand instanceof SimpleName); //$NON-NLS-1$
		SimpleName simpleType = (SimpleName) rightOperand;
		assertTrue("wrong operator",instanceOfExpression.getOperator().equals(InfixExpression.Operator.INSTANCEOF));
//		Name n = simpleType.getName();
//		assertTrue("Not a qualified name", n instanceof QualifiedName); //$NON-NLS-1$
//		QualifiedName name = (QualifiedName) n;
		checkSourceRange(simpleType, "String", source); //$NON-NLS-1$
		ITypeBinding typeBinding = simpleType.resolveTypeBinding();
		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
		assertEquals("Wrong name", "String", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		Name qualifier = name.getQualifier();
//		assertTrue("Not a qualified name", qualifier instanceof QualifiedName); //$NON-NLS-1$
//		ITypeBinding typeBinding2 = qualifier.resolveTypeBinding();
//		assertNull("typebinding2 is not null", typeBinding2); //$NON-NLS-1$
//		IBinding binding = qualifier.resolveBinding();
//		assertNotNull("no binding", binding); //$NON-NLS-1$
//		assertEquals("Wrong type", IBinding.PACKAGE, binding.getKind()); //$NON-NLS-1$
//		IPackageBinding pBinding = (IPackageBinding) binding;
//		assertEquals("Wrong name", "java.io", pBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}
		
//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=12454
//	 */
//	public void test0316() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "", "Hello.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No result", result); //$NON-NLS-1$
//		assertTrue("Not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		assertEquals("Wrong size", 1, compilationUnit.getMessages().length); //$NON-NLS-1$
//		assertEquals("Wrong size", 1, compilationUnit.getProblems().length); //$NON-NLS-1$
//	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=12781
	 */
	public void test0317() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0317", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertTrue("Not a return statement", node instanceof ReturnStatement); //$NON-NLS-1$
		ReturnStatement returnStatement = (ReturnStatement) node;
		Expression expression = returnStatement.getExpression();
		assertTrue("not an InfixExpression expression", expression instanceof InfixExpression); //$NON-NLS-1$
		InfixExpression instanceOfExpression = (InfixExpression) expression;
		assertTrue("wrong operator",instanceOfExpression.getOperator().equals(InfixExpression.Operator.INSTANCEOF));
		Expression left = instanceOfExpression.getLeftOperand();
		assertTrue("Not a Name", left instanceof Name); //$NON-NLS-1$
		Name name = (Name) left;
		IBinding binding = name.resolveBinding();
		assertNotNull("No binding", binding); //$NON-NLS-1$
		assertEquals("wrong name", "x", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		ITypeBinding typeBinding = name.resolveTypeBinding();
		assertNotNull("No typebinding", typeBinding); //$NON-NLS-1$
		assertEquals("wrong type", "any", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		Expression right = instanceOfExpression.getRightOperand();
		assertTrue("Not a simpleName", right instanceof SimpleName); //$NON-NLS-1$
		SimpleName simpleType = (SimpleName) right;
		name = simpleType;//.getName();
		assertTrue("Not a simpleName", name instanceof SimpleName); //$NON-NLS-1$
		SimpleName simpleName = (SimpleName) name;
		IBinding binding2 = simpleName.resolveBinding();
		assertNotNull("No binding2", binding2); //$NON-NLS-1$
		assertEquals("Wrong name", "Boolean", binding2.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		ITypeBinding typeBinding2 = simpleName.resolveTypeBinding();
		assertNotNull("No typeBinding2", typeBinding2); //$NON-NLS-1$
		assertEquals("Wrong name", "Boolean", typeBinding2.getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}
					
	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=13233
	 */
	public void Xtest0318() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0318", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit unit = (JavaScriptUnit) result;
		assertEquals("No error", 1, unit.getMessages().length); //$NON-NLS-1$
		assertEquals("No error", 1, unit.getProblems().length); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=13807
	 */
	public void Xtest0319() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0319", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertTrue("Not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
		List fragments = variableDeclarationStatement.fragments();
		assertEquals("wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		assertTrue("Not an array creation", expression instanceof ArrayInitializer); //$NON-NLS-1$
		ArrayInitializer arrayCreation = (ArrayInitializer) expression;
		ITypeBinding typeBinding = arrayCreation.resolveTypeBinding();
		assertNotNull("no type binding", typeBinding); //$NON-NLS-1$
		assertEquals("wrong name", "any[]", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		ArrayType arrayType = arrayCreation.getType();
//		ITypeBinding typeBinding2 = arrayType.resolveBinding();
//		assertNotNull("no type binding2", typeBinding2); //$NON-NLS-1$
//		assertEquals("wrong name", "Object[]", typeBinding2.getName()); //$NON-NLS-1$ //$NON-NLS-2$
//		Type type = arrayType.getElementType();
//		assertTrue("Not a simple type", type instanceof SimpleType); //$NON-NLS-1$
//		SimpleType simpleType = (SimpleType) type;
//		ITypeBinding typeBinding3 = simpleType.resolveBinding();
//		assertNotNull("no type binding3", typeBinding3); //$NON-NLS-1$
//		assertEquals("wrong name", "Object", typeBinding3.getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}
			
	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=13807
	 */
	public void test0320() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0320", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertTrue("Not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;

		List fragments = variableDeclarationStatement.fragments();
		assertEquals("wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);

		
		IVariableBinding binding = variableDeclarationFragment.resolveBinding();
		ITypeBinding typeBinding = binding.getType();
		assertNotNull("no type binding", typeBinding); //$NON-NLS-1$
		assertEquals("wrong name", "Number[]", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		
//		Type type = variableDeclarationStatement.getType();
//		checkSourceRange(type, "int[]", source); //$NON-NLS-1$
//		assertTrue("Not an array type", type.isArrayType()); //$NON-NLS-1$
//		ArrayType arrayType = (ArrayType) type;
//		ITypeBinding typeBinding = arrayType.resolveBinding();
//		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
//		Type elementType = arrayType.getElementType();
//		assertTrue("Not a simple type", elementType.isPrimitiveType()); //$NON-NLS-1$
//		ITypeBinding typeBinding2 = elementType.resolveBinding();
//		assertNotNull("No type binding2", typeBinding2); //$NON-NLS-1$
	}

//	/**
//	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=13807
//	 */
//	public void test0321() throws JavaScriptModelException {
//		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0321", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//		char[] source = sourceUnit.getSource().toCharArray();
//		ASTNode result = runConversion(sourceUnit, true);
//		assertNotNull("No compilation unit", result); //$NON-NLS-1$
//		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
//		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
//		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
//		assertTrue("Not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
//		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
//		Type type = variableDeclarationStatement.getType();
//		assertTrue("Not an array type", type.isArrayType()); //$NON-NLS-1$
//		ArrayType arrayType = (ArrayType) type;
//		ITypeBinding typeBinding = arrayType.resolveBinding();
//		checkSourceRange(type, "java.lang.Object[][]", source); //$NON-NLS-1$
//		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
//		Type elementType = arrayType.getComponentType();
//		ITypeBinding typeBinding2 = elementType.resolveBinding();
//		assertNotNull("No type binding2", typeBinding2); //$NON-NLS-1$
//		assertEquals("wrong dimension", 1, typeBinding2.getDimensions()); //$NON-NLS-1$
//		assertEquals("wrong name", "Object[]", typeBinding2.getName());		 //$NON-NLS-1$ //$NON-NLS-2$
//		assertTrue("Not an array type", elementType.isArrayType()); //$NON-NLS-1$
//		Type elementType2 = ((ArrayType) elementType).getComponentType();
//		assertTrue("Not a simple type", elementType2.isSimpleType()); //$NON-NLS-1$
//		ITypeBinding typeBinding3 = elementType2.resolveBinding();
//		assertNotNull("No type binding3", typeBinding3); //$NON-NLS-1$
//		assertEquals("wrong dimension", 0, typeBinding3.getDimensions()); //$NON-NLS-1$
//		assertEquals("wrong name", "Object", typeBinding3.getName());		 //$NON-NLS-1$ //$NON-NLS-2$
//	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=13231
	 */
	public void test0322() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0322", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertTrue("Not a VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
		List fragments = fieldDeclaration.fragments();
		assertEquals("wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		assertTrue("Not a null literal", expression instanceof NullLiteral); //$NON-NLS-1$
		NullLiteral nullLiteral = (NullLiteral) expression;
		ITypeBinding typeBinding = nullLiteral.resolveTypeBinding();
		assertNotNull("no type binding", typeBinding); //$NON-NLS-1$
		assertTrue("Not the null type", typeBinding.isNullType()); //$NON-NLS-1$
		assertEquals("Wrong qualified name", typeBinding.getQualifiedName(), "null"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=14198
	 */
	public void test0323() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0323", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0, 1);
		assertTrue("Not an ExpressionStatement", node instanceof ExpressionStatement); //$NON-NLS-1$
		ExpressionStatement expressionStatement = (ExpressionStatement) node;
		Expression expression2 = expressionStatement.getExpression();
		assertTrue("Not an Assignement", expression2 instanceof Assignment); //$NON-NLS-1$
	}					
	
	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=14217
	 */
	public void Xtest0326() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0326", "A.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		char[] source = sourceUnit.getSource().toCharArray();
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 1, 0);
		assertTrue("Not an ExpressionStatement", node instanceof ExpressionStatement); //$NON-NLS-1$
		ExpressionStatement expressionStatement = (ExpressionStatement) node;
		checkSourceRange(expressionStatement.getExpression(), "a().f= a()", source); //$NON-NLS-1$
		checkSourceRange(expressionStatement, "a().f= a();", source); //$NON-NLS-1$
	}					

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=14322
	 */
	public void Xtest0331() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0331", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertTrue("Not an VariableDeclarationStatement", node instanceof VariableDeclarationStatement); //$NON-NLS-1$
		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
		List fragments = variableDeclarationStatement.fragments();
		assertEquals("wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		assertTrue("Not a QualifiedName", expression instanceof QualifiedName); //$NON-NLS-1$
		QualifiedName qualifiedName = (QualifiedName) expression;
		IBinding binding = qualifiedName.getName().resolveBinding();
		assertNotNull("no binding", binding); //$NON-NLS-1$
		assertEquals("Wrong type", IBinding.VARIABLE, binding.getKind()); //$NON-NLS-1$
		IVariableBinding variableBinding = (IVariableBinding) binding;
		assertTrue("Not a field", variableBinding.isField()); //$NON-NLS-1$
		assertNull("Got a declaring class", variableBinding.getDeclaringClass()); //$NON-NLS-1$
		assertEquals("wrong name", "length", variableBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}					

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=15061
	 */
	public void Xtest0339() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0339", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("No errors found", 3, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertNotNull("not null", node); //$NON-NLS-1$
		assertTrue("not a Type declaration", node instanceof TypeDeclaration); //$NON-NLS-1$
		TypeDeclaration typeDeclaration = (TypeDeclaration) node;
		List bodyDeclarations = typeDeclaration.bodyDeclarations();
		assertEquals("wrong size", 1, bodyDeclarations.size()); //$NON-NLS-1$
		FunctionDeclaration methodDeclaration = (FunctionDeclaration) bodyDeclarations.get(0);
		checkSourceRange(methodDeclaration, "function doQuery(boolean x);", source); //$NON-NLS-1$
		node = getASTNode(compilationUnit, 0, 1);
		assertNotNull("not null", node); //$NON-NLS-1$
		assertTrue("not a FunctionDeclaration", node instanceof FunctionDeclaration); //$NON-NLS-1$
		String expectedSource = 
			"function setX( x) {\n" +  //$NON-NLS-1$
			" 		{\n" +  //$NON-NLS-1$
			"		z\n" +  //$NON-NLS-1$
			"	}\n" +  //$NON-NLS-1$
			"}"; //$NON-NLS-1$
		checkSourceRange(node, expectedSource, source);
		int methodEndPosition = node.getStartPosition() + node.getLength();
		node = getASTNode(compilationUnit, 0);
		assertNotNull("not null", node); //$NON-NLS-1$
		assertTrue("not a TypeDeclaration", node instanceof TypeDeclaration); //$NON-NLS-1$
		int typeEndPosition = node.getStartPosition() + node.getLength();
		assertEquals("different positions", methodEndPosition, typeEndPosition); //$NON-NLS-1$
	}	

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=14852
	 */
	public void Xtest0340() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "p3", "B.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertNotNull("not null", node); //$NON-NLS-1$
		assertTrue("Not an expression statement", node.getNodeType() == ASTNode.EXPRESSION_STATEMENT); //$NON-NLS-1$
		ExpressionStatement expressionStatement = (ExpressionStatement) node;
		Expression expression = expressionStatement.getExpression();
		assertTrue("Not an method invocation", expression.getNodeType() == ASTNode.FUNCTION_INVOCATION); //$NON-NLS-1$
		FunctionInvocation methodInvocation = (FunctionInvocation) expression;
		Expression expression2 = methodInvocation.getExpression();
		assertNotNull("No receiver", expression2); //$NON-NLS-1$
		ITypeBinding binding = expression2.resolveTypeBinding();
		assertNotNull("No type binding", binding); //$NON-NLS-1$
		assertEquals("wrong name", "A", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("wrong name", "p2", binding.getPackage().getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue("Not a qualified name", expression2.getNodeType() == ASTNode.QUALIFIED_NAME); //$NON-NLS-1$
		QualifiedName qualifiedName = (QualifiedName) expression2;
		SimpleName simpleName = qualifiedName.getName();
		assertEquals("wrong name", "A", simpleName.getIdentifier()); //$NON-NLS-1$ //$NON-NLS-2$
		ITypeBinding typeBinding = simpleName.resolveTypeBinding();
		assertNotNull("No type binding", typeBinding); //$NON-NLS-1$
		assertEquals("wrong name", "A", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("wrong name", "p2", typeBinding.getPackage().getName()); //$NON-NLS-1$ //$NON-NLS-2$
		Name name = qualifiedName.getQualifier();
		assertTrue("Not a simple name", name.getNodeType() == ASTNode.SIMPLE_NAME); //$NON-NLS-1$
		SimpleName simpleName2 = (SimpleName) name;
		assertEquals("wrong name", "p2", simpleName2.getIdentifier()); //$NON-NLS-1$ //$NON-NLS-2$
		IBinding binding2 = simpleName2.resolveBinding();
		assertNotNull("No binding", binding2); //$NON-NLS-1$
		assertEquals("wrong type", IBinding.PACKAGE, binding2.getKind()); //$NON-NLS-1$
		assertEquals("wrong name", "p2", binding2.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		node = getASTNode(compilationUnit, 0, 1, 0);
		assertNotNull("not null", node); //$NON-NLS-1$
		assertTrue("Not an expression statement", node.getNodeType() == ASTNode.EXPRESSION_STATEMENT); //$NON-NLS-1$
		ExpressionStatement expressionStatement2 = (ExpressionStatement) node;
		Expression expression3 = expressionStatement2.getExpression();
		assertTrue("Not an method invocation", expression3.getNodeType() == ASTNode.FUNCTION_INVOCATION); //$NON-NLS-1$
		FunctionInvocation methodInvocation2 = (FunctionInvocation) expression3;
		Expression expression4 = methodInvocation2.getExpression();
		assertNotNull("No receiver", expression4); //$NON-NLS-1$
		ITypeBinding binding3 = expression4.resolveTypeBinding();
		assertNotNull("No type binding", binding3); //$NON-NLS-1$
		assertEquals("wrong name", "A", binding3.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("wrong name", "p1", binding3.getPackage().getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}	

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=15804
	 */
	public void Xtest0341() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0341", "A.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 1, 0);
		assertNotNull("not null", node); //$NON-NLS-1$
		assertTrue("Not an if statement", node.getNodeType() == ASTNode.IF_STATEMENT); //$NON-NLS-1$
		String expectedSource = 
				"if (field != null) {\n" + //$NON-NLS-1$
				"			throw new IOException();\n" + //$NON-NLS-1$
				"		} else if (field == null) {\n" + //$NON-NLS-1$
				"			throw new MalformedURLException();\n" + //$NON-NLS-1$
				"		} else if (field == null) {\n" + //$NON-NLS-1$
				"			throw new InterruptedIOException();\n" + //$NON-NLS-1$
				"		} else {\n" + //$NON-NLS-1$
				"			throw new UnsupportedEncodingException();\n" + //$NON-NLS-1$
				"		}"; //$NON-NLS-1$
		checkSourceRange(node, expectedSource, source);
		IfStatement ifStatement = (IfStatement) node;
		Statement thenStatement = ifStatement.getThenStatement();
		expectedSource = 
				"{\n" + //$NON-NLS-1$
				"			throw new IOException();\n" + //$NON-NLS-1$
				"		}"; //$NON-NLS-1$
		checkSourceRange(thenStatement, expectedSource, source);
		Statement elseStatement = ifStatement.getElseStatement();
		expectedSource = 
				"if (field == null) {\n" + //$NON-NLS-1$
				"			throw new MalformedURLException();\n" + //$NON-NLS-1$
				"		} else if (field == null) {\n" + //$NON-NLS-1$
				"			throw new InterruptedIOException();\n" + //$NON-NLS-1$
				"		} else {\n" + //$NON-NLS-1$
				"			throw new UnsupportedEncodingException();\n" + //$NON-NLS-1$
				"		}"; //$NON-NLS-1$
		checkSourceRange(elseStatement, expectedSource, source);
		assertTrue("Not a if statement", elseStatement.getNodeType() == ASTNode.IF_STATEMENT); //$NON-NLS-1$
		ifStatement = (IfStatement) elseStatement;
		thenStatement = ifStatement.getThenStatement();
		expectedSource = 
				"{\n" + //$NON-NLS-1$
				"			throw new MalformedURLException();\n" + //$NON-NLS-1$
				"		}"; //$NON-NLS-1$
		checkSourceRange(thenStatement, expectedSource, source);
		elseStatement = ifStatement.getElseStatement();
		expectedSource = 
				"if (field == null) {\n" + //$NON-NLS-1$
				"			throw new InterruptedIOException();\n" + //$NON-NLS-1$
				"		} else {\n" + //$NON-NLS-1$
				"			throw new UnsupportedEncodingException();\n" + //$NON-NLS-1$
				"		}"; //$NON-NLS-1$
		checkSourceRange(elseStatement, expectedSource, source);
		assertTrue("Not a if statement", elseStatement.getNodeType() == ASTNode.IF_STATEMENT); //$NON-NLS-1$
		ifStatement = (IfStatement) elseStatement;
		thenStatement = ifStatement.getThenStatement();
		expectedSource = 
				"{\n" + //$NON-NLS-1$
				"			throw new InterruptedIOException();\n" + //$NON-NLS-1$
				"		}"; //$NON-NLS-1$
		checkSourceRange(thenStatement, expectedSource, source);
		elseStatement = ifStatement.getElseStatement();
		expectedSource = 
				"{\n" + //$NON-NLS-1$
				"			throw new UnsupportedEncodingException();\n" + //$NON-NLS-1$
				"		}"; //$NON-NLS-1$
		checkSourceRange(elseStatement, expectedSource, source);
	}	

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=16051
	 */
	public void Xtest0343() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0343", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 1, 1);
		assertNotNull("not null", node); //$NON-NLS-1$
		assertTrue("Not an if statement", node.getNodeType() == ASTNode.IF_STATEMENT); //$NON-NLS-1$
		String expectedSource = 
				"if (flag)\n" + //$NON-NLS-1$
				"			i= 10;"; //$NON-NLS-1$
		checkSourceRange(node, expectedSource, source);
	}
	
	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=17922
	 */
	public void Xtest0345() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0345", "A.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertNotNull("not null", node); //$NON-NLS-1$
		assertTrue("Not an field declaration", node.getNodeType() == ASTNode.FIELD_DECLARATION); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
		List fragments = fieldDeclaration.fragments();
		assertEquals("wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		assertTrue("Not an ArrayCreation", expression.getNodeType() == ASTNode.ARRAY_CREATION); //$NON-NLS-1$
		ArrayCreation arrayCreation = (ArrayCreation) expression;
		ArrayType arrayType = arrayCreation.getType();
		IBinding binding2 = arrayType.resolveBinding();
		assertNotNull("no binding2", binding2); //$NON-NLS-1$
		assertEquals("not a type", binding2.getKind(), IBinding.TYPE); //$NON-NLS-1$
		ITypeBinding typeBinding2 = (ITypeBinding) binding2;
		assertTrue("Not an array type binding2", typeBinding2.isArray()); //$NON-NLS-1$
		Type type = arrayType.getElementType();
		assertTrue("Not a simple type", type.isSimpleType()); //$NON-NLS-1$
		SimpleType simpleType = (SimpleType) type;
		Name name = simpleType.getName();
		assertTrue("QualifiedName", name.getNodeType() == ASTNode.QUALIFIED_NAME); //$NON-NLS-1$
		SimpleName simpleName = ((QualifiedName) name).getName();
		IBinding binding = simpleName.resolveBinding();
		assertNotNull("no binding", binding); //$NON-NLS-1$
		assertEquals("not a type", binding.getKind(), IBinding.TYPE); //$NON-NLS-1$
		ITypeBinding typeBinding = (ITypeBinding) binding;
		assertTrue("An array type binding", !typeBinding.isArray()); //$NON-NLS-1$
		Type type2 = fieldDeclaration.getType();
		assertTrue("Not a array type", type2.isArrayType()); //$NON-NLS-1$
		ArrayType arrayType2 = (ArrayType) type2;
		Type type3 = arrayType2.getElementType();
		assertTrue("Not a simple type", type3.isSimpleType()); //$NON-NLS-1$
		SimpleType simpleType2 = (SimpleType) type3;
		Name name2 = simpleType2.getName();
		assertTrue("Not a qualified name", name2.getNodeType() == ASTNode.QUALIFIED_NAME); //$NON-NLS-1$
		SimpleName simpleName2 = ((QualifiedName) name2).getName();
		IBinding binding3 = simpleName2.resolveBinding();
		assertNotNull("no binding", binding3); //$NON-NLS-1$
		assertEquals("not a type", binding3.getKind(), IBinding.TYPE); //$NON-NLS-1$
		ITypeBinding typeBinding3 = (ITypeBinding) binding3;
		assertTrue("An array type binding", !typeBinding3.isArray()); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=18138
	 */
	public void Xtest0346() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0346", "Test2.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertNotNull("not null", node); //$NON-NLS-1$
		assertTrue("Not an variable declaration", node.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT); //$NON-NLS-1$
		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
		Type type = variableDeclarationStatement.getType();
		checkSourceRange(type, "Vector", source); //$NON-NLS-1$
		assertTrue("not an array type", !type.isArrayType()); //$NON-NLS-1$
		assertTrue("Not a simple type", type.isSimpleType()); //$NON-NLS-1$
		SimpleType simpleType = (SimpleType) type;
		Name name = simpleType.getName();
		assertTrue("Not a simpleName", name.isSimpleName()); //$NON-NLS-1$
		SimpleName simpleName = (SimpleName) name;
		IBinding binding = simpleName.resolveBinding();
		assertNotNull("No binding", binding); //$NON-NLS-1$
		assertEquals("Wrong type", IBinding.TYPE, binding.getKind()); //$NON-NLS-1$
		ITypeBinding typeBinding = (ITypeBinding) binding;
		assertTrue("An array", !typeBinding.isArray()); //$NON-NLS-1$
		assertEquals("Wrong name", "Vector", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		ITypeBinding typeBinding2 = simpleType.resolveBinding();
		assertNotNull("No binding", typeBinding2); //$NON-NLS-1$
		assertEquals("Wrong type", IBinding.TYPE, typeBinding2.getKind()); //$NON-NLS-1$
		assertTrue("An array", !typeBinding2.isArray()); //$NON-NLS-1$
		assertEquals("Wrong name", "Vector", typeBinding2.getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=18138
	 */
	public void Xtest0347() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0347", "Test2.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertNotNull("not null", node); //$NON-NLS-1$
		assertTrue("Not an variable declaration", node.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT); //$NON-NLS-1$
		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
		Type type = variableDeclarationStatement.getType();
		checkSourceRange(type, "Vector[]", source); //$NON-NLS-1$
		assertTrue("not an array type", type.isArrayType()); //$NON-NLS-1$
		ArrayType arrayType = (ArrayType) type;
		ITypeBinding binding = arrayType.resolveBinding();
		assertNotNull("No binding", binding); //$NON-NLS-1$
		assertEquals("Wrong type", IBinding.TYPE, binding.getKind()); //$NON-NLS-1$
		assertTrue("Not an array type", binding.isArray()); //$NON-NLS-1$
		assertEquals("Wrong name", "Vector[]", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=18138
	 */
	public void Xtest0348() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0348", "Test2.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertNotNull("not null", node); //$NON-NLS-1$
		assertTrue("Not an variable declaration", node.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT); //$NON-NLS-1$
		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
		Type type = variableDeclarationStatement.getType();
		checkSourceRange(type, "Vector[][]", source); //$NON-NLS-1$
		assertTrue("not an array type", type.isArrayType()); //$NON-NLS-1$
		ArrayType arrayType = (ArrayType) type;
		ITypeBinding binding = arrayType.resolveBinding();
		assertNotNull("No binding", binding); //$NON-NLS-1$
		assertEquals("Wrong type", IBinding.TYPE, binding.getKind()); //$NON-NLS-1$
		assertTrue("Not an array type", binding.isArray()); //$NON-NLS-1$
		assertEquals("Wrong name", "Vector[][]", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=18138
	 */
	public void Xtest0349() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0349", "Test2.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertNotNull("not null", node); //$NON-NLS-1$
		assertTrue("Not an field declaration", node.getNodeType() == ASTNode.FIELD_DECLARATION); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
		Type type = fieldDeclaration.getType();
		checkSourceRange(type, "Vector[][]", source); //$NON-NLS-1$
		assertTrue("not an array type", type.isArrayType()); //$NON-NLS-1$
		ArrayType arrayType = (ArrayType) type;
		ITypeBinding binding = arrayType.resolveBinding();
		assertNotNull("No binding", binding); //$NON-NLS-1$
		assertEquals("Wrong type", IBinding.TYPE, binding.getKind()); //$NON-NLS-1$
		assertTrue("Not an array type", binding.isArray()); //$NON-NLS-1$
		assertEquals("Wrong name", "Vector[][]", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=18138
	 */
	public void Xtest0350() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0350", "Test2.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertNotNull("not null", node); //$NON-NLS-1$
		assertTrue("Not an field declaration", node.getNodeType() == ASTNode.FIELD_DECLARATION); //$NON-NLS-1$
		VariableDeclarationStatement fieldDeclaration = (VariableDeclarationStatement) node;
		Type type = fieldDeclaration.getType();
		checkSourceRange(type, "Vector", source); //$NON-NLS-1$
		assertTrue("not a simple type", type.isSimpleType()); //$NON-NLS-1$
		SimpleType simpleType = (SimpleType) type;
		ITypeBinding binding = simpleType.resolveBinding();
		assertNotNull("No binding", binding); //$NON-NLS-1$
		assertEquals("Wrong type", IBinding.TYPE, binding.getKind()); //$NON-NLS-1$
		assertTrue("An array type", binding.isClass()); //$NON-NLS-1$
		assertEquals("Wrong name", "Vector", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=18169
	 */
	public void Xtest0351() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0351", "Test2.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertNotNull("not null", node); //$NON-NLS-1$
		assertTrue("Not an method declaration", node.getNodeType() == ASTNode.FUNCTION_DECLARATION); //$NON-NLS-1$
		FunctionDeclaration methodDeclaration = (FunctionDeclaration) node;
		List parameters = methodDeclaration.parameters();
		assertEquals("wrong size", 2, parameters.size()); //$NON-NLS-1$
		SingleVariableDeclaration singleVariableDeclaration = (SingleVariableDeclaration) parameters.get(0);
		checkSourceRange(singleVariableDeclaration, "var a", source); //$NON-NLS-1$
		singleVariableDeclaration = (SingleVariableDeclaration) parameters.get(1);
		checkSourceRange(singleVariableDeclaration, "var b", source); //$NON-NLS-1$
		node = getASTNode(compilationUnit, 0, 1);
		assertNotNull("not null", node); //$NON-NLS-1$
		assertTrue("Not an method declaration", node.getNodeType() == ASTNode.FUNCTION_DECLARATION); //$NON-NLS-1$
		methodDeclaration = (FunctionDeclaration) node;
		parameters = methodDeclaration.parameters();
		assertEquals("wrong size", 2, parameters.size()); //$NON-NLS-1$
		singleVariableDeclaration = (SingleVariableDeclaration) parameters.get(0);
		checkSourceRange(singleVariableDeclaration, "var a", source); //$NON-NLS-1$
		singleVariableDeclaration = (SingleVariableDeclaration) parameters.get(1);
		checkSourceRange(singleVariableDeclaration, "var b", source);			 //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=18169
	 */
	public void Xtest0352() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0352", "Test2.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertNotNull("not null", node); //$NON-NLS-1$
		assertTrue("Not an method declaration", node.getNodeType() == ASTNode.FUNCTION_DECLARATION); //$NON-NLS-1$
		FunctionDeclaration methodDeclaration = (FunctionDeclaration) node;
		List parameters = methodDeclaration.parameters();
		assertEquals("wrong size", 2, parameters.size()); //$NON-NLS-1$
		SingleVariableDeclaration singleVariableDeclaration = (SingleVariableDeclaration) parameters.get(0);
		checkSourceRange(singleVariableDeclaration, "var a", source); //$NON-NLS-1$
		singleVariableDeclaration = (SingleVariableDeclaration) parameters.get(1);
		checkSourceRange(singleVariableDeclaration, "var b", source); //$NON-NLS-1$
		node = getASTNode(compilationUnit, 0, 1);
		assertNotNull("not null", node); //$NON-NLS-1$
		assertTrue("Not an method declaration", node.getNodeType() == ASTNode.FUNCTION_DECLARATION); //$NON-NLS-1$
		methodDeclaration = (FunctionDeclaration) node;
		parameters = methodDeclaration.parameters();
		assertEquals("wrong size", 2, parameters.size()); //$NON-NLS-1$
		singleVariableDeclaration = (SingleVariableDeclaration) parameters.get(0);
		checkSourceRange(singleVariableDeclaration, "vara", source); //$NON-NLS-1$
		singleVariableDeclaration = (SingleVariableDeclaration) parameters.get(1);
		checkSourceRange(singleVariableDeclaration, "var b", source);			 //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=18042
	 */
	public void Xtest0353() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0353", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertNotNull("not null", node); //$NON-NLS-1$
		assertTrue("Not an variable declaration", node.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT); //$NON-NLS-1$
		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
		Type type = variableDeclarationStatement.getType();
		checkSourceRange(type, "InputStream", source); //$NON-NLS-1$
		assertTrue("not a simple type", type.isSimpleType()); //$NON-NLS-1$
		ITypeBinding binding = type.resolveBinding();
		assertNotNull("No binding", binding); //$NON-NLS-1$
		assertEquals("Wrong type", IBinding.TYPE, binding.getKind()); //$NON-NLS-1$
		assertTrue("Not a class", binding.isClass()); //$NON-NLS-1$
		assertEquals("Wrong name", "InputStream", binding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Wrong package", "java.io", binding.getPackage().getName()); //$NON-NLS-1$ //$NON-NLS-2$
		SimpleType simpleType = (SimpleType) type;
		Name name = simpleType.getName();
		IBinding binding2 = name.resolveBinding();
		assertNotNull("No binding", binding2); //$NON-NLS-1$
		assertEquals("Wrong type", IBinding.TYPE, binding2.getKind()); //$NON-NLS-1$
		ITypeBinding typeBinding = (ITypeBinding) binding2;
		assertTrue("Not a class", typeBinding.isClass()); //$NON-NLS-1$
		assertEquals("Wrong name", "InputStream", typeBinding.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Wrong package", "java.io", typeBinding.getPackage().getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=19851
	 */
	public void Xtest0354() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0354", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 2, compilationUnit.getMessages().length); //$NON-NLS-1$
	}
	
	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=20520
	 */
	public void Xtest0355() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0355", "Foo.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertNotNull(node);
		assertTrue("Not an if statement", node.getNodeType() == ASTNode.IF_STATEMENT); //$NON-NLS-1$
		IfStatement ifStatement = (IfStatement) node;
		Expression condition = ifStatement.getExpression();
		assertTrue("Not an infixExpression", condition.getNodeType() == ASTNode.INFIX_EXPRESSION); //$NON-NLS-1$
		InfixExpression infixExpression = (InfixExpression) condition;
		Expression expression = infixExpression.getLeftOperand();
		assertTrue("Not a method invocation expression", expression.getNodeType() == ASTNode.FUNCTION_INVOCATION); //$NON-NLS-1$
		FunctionInvocation methodInvocation = (FunctionInvocation) expression;
		Expression expression2 = methodInvocation.getExpression();
		assertTrue("Not a parenthesis expression", expression2.getNodeType() == ASTNode.PARENTHESIZED_EXPRESSION); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=20865
	 */
	public void Xtest0356() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0356", "X.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 1, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertNotNull(node);
		assertTrue("Not a variable declaration statement", node.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT); //$NON-NLS-1$
		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
		Type type = variableDeclarationStatement.getType();
		ITypeBinding binding = type.resolveBinding();
		assertNotNull("Binding should NOT be null for type: "+type, binding);
	}
	
	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=21757
	 */
	public void Xtest0357() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0357", "A.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0);
		assertNotNull(node);
		assertTrue("Not a type declaration statement", node.getNodeType() == ASTNode.TYPE_DECLARATION); //$NON-NLS-1$
		TypeDeclaration typeDeclaration = (TypeDeclaration) node;
		SimpleName name = typeDeclaration.getName();
		checkSourceRange(name, "A", source); //$NON-NLS-1$
	}
	
	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=21768
	 */
	public void Xtest0358() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0358", "A.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0,0);
		assertNotNull(node);
		assertTrue("Not a method declaration statement", node.getNodeType() == ASTNode.FUNCTION_DECLARATION); //$NON-NLS-1$
		FunctionDeclaration methodDeclaration = (FunctionDeclaration) node;
		SimpleName name = methodDeclaration.getName();
		checkSourceRange(name, "mdd", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=21768
	 */
	public void Xtest0359() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0359", "A.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0,0);
		assertNotNull(node);
		assertTrue("Not a method declaration statement", node.getNodeType() == ASTNode.FUNCTION_DECLARATION); //$NON-NLS-1$
		FunctionDeclaration methodDeclaration = (FunctionDeclaration) node;
		SimpleName name = methodDeclaration.getName();
		checkSourceRange(name, "mdd", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=21916
	 */
	public void Xtest0360() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0360", "X.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0,0, 0);
		assertNotNull(node);
		assertTrue("Not a for statement", node.getNodeType() == ASTNode.FOR_STATEMENT); //$NON-NLS-1$
		ForStatement forStatement = (ForStatement) node;
		List initializers = forStatement.initializers();
		assertEquals("Wrong size", 1, initializers.size()); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=21916
	 */
	public void Xtest0361() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0361", "X.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0,0, 0);
		assertNotNull(node);
		assertTrue("Not a for statement", node.getNodeType() == ASTNode.FOR_STATEMENT); //$NON-NLS-1$
		ForStatement forStatement = (ForStatement) node;
		List initializers = forStatement.initializers();
		assertEquals("Wrong size", 1, initializers.size()); //$NON-NLS-1$
	}
	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=21916
	 * @deprecated using deprecated code
	 */
	public void test0362() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0362", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, false);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		ForStatement forStatement = this.ast.newForStatement();

		VariableDeclarationFragment iFragment = this.ast.newVariableDeclarationFragment();
		iFragment.setName(this.ast.newSimpleName("i")); //$NON-NLS-1$
		iFragment.setInitializer(this.ast.newNumberLiteral("0"));//$NON-NLS-1$
		VariableDeclarationFragment jFragment = this.ast.newVariableDeclarationFragment();
		jFragment.setName(this.ast.newSimpleName("j")); //$NON-NLS-1$
		jFragment.setInitializer(this.ast.newNumberLiteral("0"));//$NON-NLS-1$
		VariableDeclarationFragment kFragment = this.ast.newVariableDeclarationFragment();
		kFragment.setName(this.ast.newSimpleName("k")); //$NON-NLS-1$
		kFragment.setInitializer(this.ast.newNumberLiteral("0"));//$NON-NLS-1$

		VariableDeclarationExpression variableDeclarationExpression = this.ast.newVariableDeclarationExpression(iFragment);
		variableDeclarationExpression.setModifiers(Modifier.NONE);
//		variableDeclarationExpression.setType(this.ast.newPrimitiveType(PrimitiveType.INT));
		variableDeclarationExpression.fragments().add(jFragment);
		variableDeclarationExpression.fragments().add(kFragment);
		forStatement.initializers().add(variableDeclarationExpression);

		PostfixExpression iPostfixExpression = this.ast.newPostfixExpression();
		iPostfixExpression.setOperand(this.ast.newSimpleName("i"));//$NON-NLS-1$
		iPostfixExpression.setOperator(PostfixExpression.Operator.INCREMENT);
		forStatement.updaters().add(iPostfixExpression);
		
		PostfixExpression jPostfixExpression = this.ast.newPostfixExpression();
		jPostfixExpression.setOperand(this.ast.newSimpleName("j"));//$NON-NLS-1$
		jPostfixExpression.setOperator(PostfixExpression.Operator.INCREMENT);
		forStatement.updaters().add(jPostfixExpression);

		PostfixExpression kPostfixExpression = this.ast.newPostfixExpression();
		kPostfixExpression.setOperand(this.ast.newSimpleName("k"));//$NON-NLS-1$
		kPostfixExpression.setOperator(PostfixExpression.Operator.INCREMENT);
		forStatement.updaters().add(kPostfixExpression);

		forStatement.setBody(this.ast.newBlock());
		
		InfixExpression infixExpression = this.ast.newInfixExpression();
		infixExpression.setLeftOperand(this.ast.newSimpleName("i")); //$NON-NLS-1$
		infixExpression.setOperator(InfixExpression.Operator.LESS);
		infixExpression.setRightOperand(this.ast.newNumberLiteral("10")); //$NON-NLS-1$
		forStatement.setExpression(infixExpression);
		
		assertTrue("Both AST trees should be identical", forStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		checkSourceRange(node, "for (var i=0, j=0, k=0; i<10 ; i++, j++, k++) {}", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=22939
	 */
	public void Xtest0363() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0363", "A.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0, 1);
		assertNotNull(node);
		assertTrue("Not a variable declaration statement", node.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT); //$NON-NLS-1$
		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
		List fragments = variableDeclarationStatement.fragments();
		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		Expression expression = variableDeclarationFragment.getInitializer();
		assertTrue("Not a parenthesized expression", expression.getNodeType() == ASTNode.PARENTHESIZED_EXPRESSION); //$NON-NLS-1$
		Expression expression2 = ((ParenthesizedExpression) expression).getExpression();
		checkSourceRange(expression2, "xxxx", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11529
	 */
	public void Xtest0364() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0364", "A.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertNotNull(node);
		assertTrue("Not a variable declaration statement", node.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT); //$NON-NLS-1$
		VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) node;
		List fragments = variableDeclarationStatement.fragments();
		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		checkSourceRange(variableDeclarationStatement, "var local;", source); //$NON-NLS-1$
		SimpleName simpleName = variableDeclarationFragment.getName();
		IBinding binding = simpleName.resolveBinding();
		assertNotNull("No binding", binding); //$NON-NLS-1$
	}
	
	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11529
	 */
	public void Xtest0365() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0365", "A.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertNotNull(node);
		assertTrue("Not a for statement", node.getNodeType() == ASTNode.FOR_STATEMENT); //$NON-NLS-1$
		ForStatement forStatement = (ForStatement) node;
		List initializers = forStatement.initializers();
		assertEquals("Wrong size", 1, initializers.size()); //$NON-NLS-1$
		VariableDeclarationExpression variableDeclarationExpression = (VariableDeclarationExpression) initializers.get(0);
		List fragments = variableDeclarationExpression.fragments();
		assertEquals("Wrong size", 1, fragments.size()); //$NON-NLS-1$
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) fragments.get(0);
		SimpleName simpleName = variableDeclarationFragment.getName();
		IBinding binding = simpleName.resolveBinding();
		assertNotNull("No binding", binding); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=23048
	 */
	public void Xtest0366() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0366", "A.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertNotNull(node);
		assertTrue("Not a for statement", node.getNodeType() == ASTNode.FOR_STATEMENT); //$NON-NLS-1$
		ForStatement forStatement = (ForStatement) node;
		checkSourceRange(forStatement, "for (var i = 0; i < 5; ++i);", source); //$NON-NLS-1$
		Statement statement = forStatement.getBody();
		assertTrue("Not an empty statement", statement.getNodeType() == ASTNode.EMPTY_STATEMENT); //$NON-NLS-1$
		checkSourceRange(statement, ";", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=23048
	 */
	public void Xtest0367() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0367", "A.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertEquals("errors found", 0, compilationUnit.getMessages().length); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertNotNull(node);
		assertTrue("Not a while statement", node.getNodeType() == ASTNode.WHILE_STATEMENT); //$NON-NLS-1$
		WhileStatement whileStatement = (WhileStatement) node;
		checkSourceRange(whileStatement, "while(i == 2);", source); //$NON-NLS-1$
		Statement statement = whileStatement.getBody();
		assertTrue("Not an empty statement", statement.getNodeType() == ASTNode.EMPTY_STATEMENT); //$NON-NLS-1$
		checkSourceRange(statement, ";", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=23048
	 */
	public void Xtest0368() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0368", "A.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertProblemsSize(compilationUnit, 1, "The label test is never explicitly referenced"); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertNotNull(node);
		assertTrue("Not a labeled statement", node.getNodeType() == ASTNode.LABELED_STATEMENT); //$NON-NLS-1$
		LabeledStatement labeledStatement = (LabeledStatement) node;
		checkSourceRange(labeledStatement, "test:;", source); //$NON-NLS-1$
		Statement statement = labeledStatement.getBody();
		assertTrue("Not an empty statement", statement.getNodeType() == ASTNode.EMPTY_STATEMENT); //$NON-NLS-1$
		checkSourceRange(statement, ";", source); //$NON-NLS-1$
	}

	/**
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=23048
	 */
	public void Xtest0369() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0369", "A.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		assertNotNull("No compilation unit", result); //$NON-NLS-1$
		assertTrue("result is not a compilation unit", result instanceof JavaScriptUnit); //$NON-NLS-1$
		JavaScriptUnit compilationUnit = (JavaScriptUnit) result;
		assertProblemsSize(compilationUnit, 1, "The label test is never explicitly referenced"); //$NON-NLS-1$
		ASTNode node = getASTNode(compilationUnit, 0, 0, 0);
		assertNotNull(node);
		assertTrue("Not a labeled statement", node.getNodeType() == ASTNode.LABELED_STATEMENT); //$NON-NLS-1$
		LabeledStatement labeledStatement = (LabeledStatement) node;
		checkSourceRange(labeledStatement, "test:\\u003B", source); //$NON-NLS-1$
		Statement statement = labeledStatement.getBody();
		assertTrue("Not an empty statement", statement.getNodeType() == ASTNode.EMPTY_STATEMENT); //$NON-NLS-1$
		checkSourceRange(statement, "\\u003B", source); //$NON-NLS-1$
	}
			
	/**
	 * DoStatement ==> DoStatement
	 */
	public void test0370() throws JavaScriptModelException {
		IJavaScriptUnit sourceUnit = getCompilationUnit("Converter" , "src", "test0370", "Test.js"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		char[] source = sourceUnit.getSource().toCharArray();
		ASTNode result = runConversion(sourceUnit, true);
		ASTNode node = getASTNode((JavaScriptUnit) result, 0, 0, 0);
		assertNotNull("Expression should not be null", node); //$NON-NLS-1$
		DoStatement doStatement = this.ast.newDoStatement();
		doStatement.setBody(this.ast.newEmptyStatement());
		doStatement.setExpression(this.ast.newBooleanLiteral(true));
		assertTrue("Both AST trees should be identical", doStatement.subtreeMatch(new ASTMatcher(), node));		//$NON-NLS-1$
		String expectedSource = "do ; while(true);";//$NON-NLS-1$
		checkSourceRange(node, expectedSource, source);
		DoStatement doStatement2 = (DoStatement) node;
		Statement statement = doStatement2.getBody();
		assertTrue("Not an empty statement", statement.getNodeType() == ASTNode.EMPTY_STATEMENT); //$NON-NLS-1$
		checkSourceRange(statement, ";", source); //$NON-NLS-1$
	}
}

