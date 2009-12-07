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
package org.eclipse.wst.jsdt.core.tests.rewrite.describing;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.ArrayAccess;
import org.eclipse.wst.jsdt.core.dom.Assignment;
import org.eclipse.wst.jsdt.core.dom.Block;
import org.eclipse.wst.jsdt.core.dom.BooleanLiteral;
import org.eclipse.wst.jsdt.core.dom.CatchClause;
import org.eclipse.wst.jsdt.core.dom.ConditionalExpression;
import org.eclipse.wst.jsdt.core.dom.ExpressionStatement;
import org.eclipse.wst.jsdt.core.dom.FieldAccess;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.InfixExpression;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.NumberLiteral;
import org.eclipse.wst.jsdt.core.dom.ParenthesizedExpression;
import org.eclipse.wst.jsdt.core.dom.PostfixExpression;
import org.eclipse.wst.jsdt.core.dom.PrefixExpression;
import org.eclipse.wst.jsdt.core.dom.ReturnStatement;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.SingleVariableDeclaration;
import org.eclipse.wst.jsdt.core.dom.StringLiteral;
import org.eclipse.wst.jsdt.core.dom.ThisExpression;
import org.eclipse.wst.jsdt.core.dom.TryStatement;
import org.eclipse.wst.jsdt.core.dom.rewrite.ASTRewrite;

public class ASTRewritingExpressionsTest extends ASTRewritingTest {
	private static final Class THIS= ASTRewritingExpressionsTest.class;

	public ASTRewritingExpressionsTest(String name) {
		super(name);
	}

	public static Test allTests() {
		return new Suite(THIS);
	}
	
	public static Test setUpTest(Test someTest) {
		TestSuite suite= new Suite("one test");
		suite.addTest(someTest);
		return suite;
	}
	
	public static Test suite() {
		return allTests();
	}
	
	public void testArrayAccess() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("function foo(o) {\n");
		buf.append("	o[3 /* comment*/ - 1]= this.o[3 - 1];\n");
		buf.append("}\n");
		buf.append("");	
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		AST ast= astRoot.getAST();
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
		FunctionDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		Block block= methodDecl.getBody();
		List statements= block.statements();
		assertTrue("Number of statements not 1", statements.size() == 1);
		{	// replace left hand side index, replace right hand side index by left side index
			ExpressionStatement stmt= (ExpressionStatement) statements.get(0);
			Assignment assignment= (Assignment) stmt.getExpression();
			
			ArrayAccess left= (ArrayAccess) assignment.getLeftHandSide();
			ArrayAccess right= (ArrayAccess) assignment.getRightHandSide();
			
			NumberLiteral name= ast.newNumberLiteral("1");
			rewrite.replace(left.getIndex(), name, null);
			
			ASTNode placeHolder= rewrite.createCopyTarget(left.getIndex());
			rewrite.replace(right.getIndex(), placeHolder, null);
			
			SimpleName newName= ast.newSimpleName("o");
			rewrite.replace(right.getArray(), newName, null);
		}

		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("function foo(o) {\n");
		buf.append("	o[1]= o[3 /* comment*/ - 1];\n");
		buf.append("}\n");	
			
		assertEqualString(preview, buf.toString());
	}
	
	public void testAssignment() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("function foo() {\n");
		buf.append("    var i, j;\n");
		buf.append("    i= 0;\n");
		buf.append("    i-= j= 3;\n");
		buf.append("}\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		AST ast= astRoot.getAST();
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
		FunctionDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		Block block= methodDecl.getBody();
		List statements= block.statements();
		assertTrue("Number of statements not 3", statements.size() == 3);
		{ // change left side & right side
			ExpressionStatement stmt= (ExpressionStatement) statements.get(1);
			Assignment assignment= (Assignment) stmt.getExpression();
			
			SimpleName name= ast.newSimpleName("j");
			rewrite.replace(assignment.getLeftHandSide(), name, null);
			
			FunctionInvocation invocation= ast.newFunctionInvocation();
			invocation.setName(ast.newSimpleName("goo"));
			invocation.setExpression(ast.newSimpleName("other"));
			
			rewrite.replace(assignment.getRightHandSide(), invocation, null);
		}
		{ // change operator and operator of inner
			ExpressionStatement stmt= (ExpressionStatement) statements.get(2);
			Assignment assignment= (Assignment) stmt.getExpression();
			
			rewrite.set(assignment, Assignment.OPERATOR_PROPERTY, Assignment.Operator.DIVIDE_ASSIGN, null);
			
			Assignment inner= (Assignment) assignment.getRightHandSide();
						
			rewrite.set(inner, Assignment.OPERATOR_PROPERTY, Assignment.Operator.RIGHT_SHIFT_UNSIGNED_ASSIGN, null);
		}
				
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("function foo() {\n");
		buf.append("    var i, j;\n");
		buf.append("    j= other.goo();\n");
		buf.append("    i/= j>>>= 3;\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
		
	}
	
	public void testCatchClause() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("function foo() {\n");
		buf.append("    try {\n");
		buf.append("    } catch (e) {\n");
		buf.append("    }\n");			
		buf.append("}\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		AST ast= astRoot.getAST();
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
		FunctionDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		Block block= methodDecl.getBody();
		List statements= block.statements();
		assertTrue("Number of statements not 3", statements.size() == 1);
		List catchClauses= ((TryStatement) statements.get(0)).catchClauses();
		assertTrue("Number of catchClauses not 1", catchClauses.size() == 1);
		{ // change exception type
			CatchClause clause= (CatchClause) catchClauses.get(0);
			
			SingleVariableDeclaration exception= clause.getException();
			
			SingleVariableDeclaration newException= ast.newSingleVariableDeclaration();
						
			newException.setName(ast.newSimpleName("ex"));
			
			rewrite.replace(exception, newException, null);
		}
		{ // change body
			CatchClause clause= (CatchClause) catchClauses.get(0);
			Block body= clause.getBody();
			
			Block newBody= ast.newBlock();
			ReturnStatement returnStatement= ast.newReturnStatement();
			newBody.statements().add(returnStatement);
			
			rewrite.replace(body, newBody, null);
		}
				
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("function foo() {\n");
		buf.append("    try {\n");
		buf.append("    } catch (ex) {\n");
		buf.append("        return;\n");	
		buf.append("    }\n");			
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
		
	}

//	/** @deprecated using deprecated code */
//	public void testClassInstanceCreation() throws Exception {
//		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
//		StringBuffer buf= new StringBuffer();
//		buf.append("package test1;\n");
//		buf.append("public class E {\n");
//		buf.append("    function foo() {\n");
//		buf.append("        goo().new Inner();\n");
//		buf.append("        new Runnable(\"Hello\") {\n");
//		buf.append("            public void run() {\n");
//		buf.append("            }\n");		
//		buf.append("        };\n");
//		buf.append("    }\n");
//		buf.append("}\n");	
//		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
//		
//		JavaScriptUnit astRoot= createAST(cu);
//		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		
//		AST ast= astRoot.getAST();
//		
//		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");
//		FunctionDeclaration methodDecl= findMethodDeclaration(type, "foo");
//		Block block= methodDecl.getBody();
//		List statements= block.statements();
//		assertTrue("Number of statements not 2", statements.size() == 2);
//		{ // remove expression, change type name, add argument, add anonym decl
//			ExpressionStatement stmt= (ExpressionStatement) statements.get(0);
//			ClassInstanceCreation creation= (ClassInstanceCreation) stmt.getExpression();
//
//			rewrite.remove(creation.getExpression(), null);
//			
//			SimpleName newName= ast.newSimpleName("NewInner");
//			rewrite.replace(creation.getName(), newName, null);
//			
//			StringLiteral stringLiteral1= ast.newStringLiteral();
//			stringLiteral1.setLiteralValue("Hello");
//			rewrite.getListRewrite(creation, ClassInstanceCreation.ARGUMENTS_PROPERTY).insertLast(stringLiteral1, null);
//
//			
//			StringLiteral stringLiteral2= ast.newStringLiteral();
//			stringLiteral2.setLiteralValue("World");
//			rewrite.getListRewrite(creation, ClassInstanceCreation.ARGUMENTS_PROPERTY).insertLast(stringLiteral2, null);
//			
//			assertTrue("Has anonym class decl", creation.getAnonymousClassDeclaration() == null);
//			
//			AnonymousClassDeclaration anonymDecl= ast.newAnonymousClassDeclaration();
//			FunctionDeclaration anonymMethDecl= createNewMethod(ast, "newMethod", false);
//			anonymDecl.bodyDeclarations().add(anonymMethDecl);
//			
//			rewrite.set(creation, ClassInstanceCreation.ANONYMOUS_CLASS_DECLARATION_PROPERTY, anonymDecl, null);			
//
//		}
//		{ // add expression, remove argument, remove anonym decl 
//			ExpressionStatement stmt= (ExpressionStatement) statements.get(1);
//			ClassInstanceCreation creation= (ClassInstanceCreation) stmt.getExpression();
//
//			assertTrue("Has expression", creation.getExpression() == null);
//			
//			SimpleName newExpression= ast.newSimpleName("x");
//			rewrite.set(creation, ClassInstanceCreation.EXPRESSION_PROPERTY, newExpression, null);			
//
//			
//			List arguments= creation.arguments();
//			assertTrue("Must have 1 argument", arguments.size() == 1);
//			
//			rewrite.remove((ASTNode) arguments.get(0), null);
//			
//			rewrite.remove(creation.getAnonymousClassDeclaration(), null);
//		}
//		
//		String preview= evaluateRewrite(cu, rewrite);
//		
//		buf= new StringBuffer();
//		buf.append("package test1;\n");
//		buf.append("public class E {\n");
//		buf.append("    function foo() {\n");
//		buf.append("        new NewInner(\"Hello\", \"World\") {\n");
//		buf.append("            private void newMethod(String str) {\n");
//		buf.append("            }\n");
//		buf.append("        };\n");
//		buf.append("        x.new Runnable();\n");
//		buf.append("    }\n");
//		buf.append("}\n");	
//		assertEqualString(preview, buf.toString());
//		
//	}
	
//	public void testClassInstanceCreation2() throws Exception {
//		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
//		StringBuffer buf= new StringBuffer();
//		buf.append("package test1;\n");
//		buf.append("public class E<A> {\n");
//		buf.append("    function foo() {\n");
//		buf.append("        new Inner();\n");
//		buf.append("        new <A>Inner();\n");
//		buf.append("        new<A>Inner();\n");
//		buf.append("        new <A, A>Inner();\n");
//		buf.append("    }\n");
//		buf.append("}\n");	
//		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
//		
//		JavaScriptUnit astRoot= createAST3(cu);
//		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		
//		AST ast= astRoot.getAST();
//		
//		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");
//		FunctionDeclaration methodDecl= findMethodDeclaration(type, "foo");
//		Block block= methodDecl.getBody();
//		List statements= block.statements();
//		assertTrue("Number of statements not 3", statements.size() == 4);
//		{ // add type argument
//			ExpressionStatement stmt= (ExpressionStatement) statements.get(0);
//			ClassInstanceCreation creation= (ClassInstanceCreation) stmt.getExpression();
//			
//			Type newTypeArg= ast.newSimpleType(ast.newSimpleName("A"));
//			ListRewrite listRewrite= rewrite.getListRewrite(creation, ClassInstanceCreation.TYPE_ARGUMENTS_PROPERTY);
//			listRewrite.insertFirst(newTypeArg, null);
//			
//		}
//		{ // remove type argument
//			ExpressionStatement stmt= (ExpressionStatement) statements.get(1);
//			ClassInstanceCreation creation= (ClassInstanceCreation) stmt.getExpression();
//
//			List typeArgs= creation.typeArguments();
//			rewrite.remove((ASTNode) typeArgs.get(0), null);
//		}
//		{ // remove type argument
//			ExpressionStatement stmt= (ExpressionStatement) statements.get(2);
//			ClassInstanceCreation creation= (ClassInstanceCreation) stmt.getExpression();
//
//			List typeArgs= creation.typeArguments();
//			rewrite.remove((ASTNode) typeArgs.get(0), null);
//		}
//		{ // add type argument to existing
//			ExpressionStatement stmt= (ExpressionStatement) statements.get(3);
//			ClassInstanceCreation creation= (ClassInstanceCreation) stmt.getExpression();
//
//			Type newTypeArg= ast.newSimpleType(ast.newSimpleName("String"));
//
//			ListRewrite listRewrite= rewrite.getListRewrite(creation, ClassInstanceCreation.TYPE_ARGUMENTS_PROPERTY);
//			listRewrite.insertLast(newTypeArg, null);
//		}
//		
//		String preview= evaluateRewrite(cu, rewrite);
//		
//		buf= new StringBuffer();
//		buf.append("package test1;\n");
//		buf.append("public class E<A> {\n");
//		buf.append("    function foo() {\n");
//		buf.append("        new <A> Inner();\n");
//		buf.append("        new Inner();\n");
//		buf.append("        new Inner();\n");
//		buf.append("        new <A, A, String>Inner();\n");
//		buf.append("    }\n");
//		buf.append("}\n");	
//		assertEqualString(preview, buf.toString());
//	}

	
	
	/** @deprecated using deprecated code */
	public void testConditionalExpression() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        i= (k == 0) ? 1 : 2;\n");
		buf.append("    }\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		AST ast= astRoot.getAST();
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");
		FunctionDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		Block block= methodDecl.getBody();
		List statements= block.statements();
		assertTrue("Number of statements not 1", statements.size() == 1);
		{ // change compare expression, then expression & else expression
			ExpressionStatement stmt= (ExpressionStatement) statements.get(0);
			Assignment assignment= (Assignment) stmt.getExpression();
			ConditionalExpression condExpression= (ConditionalExpression) assignment.getRightHandSide();
			
			BooleanLiteral literal= ast.newBooleanLiteral(true);
			rewrite.replace(condExpression.getExpression(), literal, null);
			
			SimpleName newThenExpre= ast.newSimpleName("x");
			rewrite.replace(condExpression.getThenExpression(), newThenExpre, null);
			
			InfixExpression infixExpression= ast.newInfixExpression();
			infixExpression.setLeftOperand(ast.newNumberLiteral("1"));
			infixExpression.setRightOperand(ast.newNumberLiteral("2"));
			infixExpression.setOperator(InfixExpression.Operator.PLUS);
			
			rewrite.replace(condExpression.getElseExpression(), infixExpression, null);
		}
		
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        i= true ? x : 1 + 2;\n");
		buf.append("    }\n");
		assertEqualString(preview, buf.toString());
		
	}
	
	/** @deprecated using deprecated code */
	public void testFieldAccess() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        foo().i= goo().i;\n");
		buf.append("    }\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		AST ast= astRoot.getAST();
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");
		FunctionDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		Block block= methodDecl.getBody();
		List statements= block.statements();
		assertTrue("Number of statements not 1", statements.size() == 1);
		{ // replace field expression, replace field name
			ExpressionStatement stmt= (ExpressionStatement) statements.get(0);
			Assignment assignment= (Assignment) stmt.getExpression();
			FieldAccess leftFieldAccess= (FieldAccess) assignment.getLeftHandSide();
			FieldAccess rightFieldAccess= (FieldAccess) assignment.getRightHandSide();
			
			FunctionInvocation invocation= ast.newFunctionInvocation();
			invocation.setName(ast.newSimpleName("xoo"));
			rewrite.replace(leftFieldAccess.getExpression(), invocation, null);
			
			SimpleName newName= ast.newSimpleName("x");
			rewrite.replace(leftFieldAccess.getName(), newName, null);

			SimpleName rightHand= ast.newSimpleName("b");
			rewrite.replace(rightFieldAccess.getExpression(), rightHand, null);
		}
		
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        xoo().x= b.i;\n");
		buf.append("    }\n");
		assertEqualString(preview, buf.toString());
		
	}
	
	/** @deprecated using deprecated code */
	public void testInfixExpression() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        i= 1 + 2;\n");
		buf.append("        j= 1 + 2 + 3 + 4 + 5;\n");
		buf.append("        k= 1 + 2 + 3 + 4 + 5;\n");		
		buf.append("    }\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		AST ast= astRoot.getAST();
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");
		FunctionDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		Block block= methodDecl.getBody();
		List statements= block.statements();
		assertTrue("Number of statements not 3", statements.size() == 3);
		{ // change left side & right side & operand
			ExpressionStatement stmt= (ExpressionStatement) statements.get(0);
			Assignment assignment= (Assignment) stmt.getExpression();
			InfixExpression expr= (InfixExpression) assignment.getRightHandSide();
			
			SimpleName leftOp= ast.newSimpleName("k");
			rewrite.replace(expr.getLeftOperand(), leftOp, null);	

			SimpleName rightOp= ast.newSimpleName("j");
			rewrite.replace(expr.getRightOperand(), rightOp, null);	
			
			// change operand
			rewrite.set(expr, InfixExpression.OPERATOR_PROPERTY, InfixExpression.Operator.MINUS, null);
		}
		
		{ // remove an ext. operand, add one and replace one
			ExpressionStatement stmt= (ExpressionStatement) statements.get(1);
			Assignment assignment= (Assignment) stmt.getExpression();
			InfixExpression expr= (InfixExpression) assignment.getRightHandSide();
			
			List extendedOperands= expr.extendedOperands();
			assertTrue("Number of extendedOperands not 3", extendedOperands.size() == 3);
			
			rewrite.remove((ASTNode) extendedOperands.get(0), null);
			
			SimpleName newOp1= ast.newSimpleName("k");
			rewrite.replace((ASTNode) extendedOperands.get(1), newOp1, null);
			
			SimpleName newOp2= ast.newSimpleName("n");
			rewrite.getListRewrite(expr, InfixExpression.EXTENDED_OPERANDS_PROPERTY).insertLast(newOp2, null);

		}
		
		{ // change operand
			ExpressionStatement stmt= (ExpressionStatement) statements.get(2);
			Assignment assignment= (Assignment) stmt.getExpression();
			InfixExpression expr= (InfixExpression) assignment.getRightHandSide();			
			
			rewrite.set(expr, InfixExpression.OPERATOR_PROPERTY, InfixExpression.Operator.TIMES, null);
		}			
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        i= k - j;\n");
		buf.append("        j= 1 + 2 + k + 5 + n;\n");
		buf.append("        k= 1 * 2 * 3 * 4 * 5;\n");		
		buf.append("    }\n");
		assertEqualString(preview, buf.toString());
		
	}

//	/** @deprecated using deprecated code */
//	public void testInstanceofExpression() throws Exception {
//		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
//		StringBuffer buf= new StringBuffer();
//		buf.append("package test1;\n");
//		buf.append("public class E {\n");
//		buf.append("    function foo() {\n");
//		buf.append("        goo(k instanceof Vector);\n");
//		buf.append("        goo(k()instanceof Vector);\n");
//		buf.append("    }\n");
//		buf.append("}\n");	
//		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
//		
//		JavaScriptUnit astRoot= createAST(cu);
//		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		
//		AST ast= astRoot.getAST();
//		
//		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");
//		FunctionDeclaration methodDecl= findMethodDeclaration(type, "foo");
//		Block block= methodDecl.getBody();
//		List statements= block.statements();
//		assertTrue("Number of statements not 2", statements.size() == 2);
//		{ // change left side & right side
//			ExpressionStatement stmt= (ExpressionStatement) statements.get(0);
//			FunctionInvocation invocation= (FunctionInvocation) stmt.getExpression();
//			
//			List arguments= invocation.arguments();
//			InstanceofExpression expr= (InstanceofExpression) arguments.get(0);
//			
//			SimpleName name= ast.newSimpleName("x");
//			rewrite.replace(expr.getLeftOperand(), name, null);
//			
//			Type newCastType= ast.newSimpleType(ast.newSimpleName("List"));
//
//			rewrite.replace(expr.getRightOperand(), newCastType, null);
//		}
//		{ // change left side
//			ExpressionStatement stmt= (ExpressionStatement) statements.get(1);
//			FunctionInvocation invocation= (FunctionInvocation) stmt.getExpression();
//			
//			List arguments= invocation.arguments();
//			InstanceofExpression expr= (InstanceofExpression) arguments.get(0);
//			
//			SimpleName name= ast.newSimpleName("x");
//			rewrite.replace(expr.getLeftOperand(), name, null);
//		}			
//		String preview= evaluateRewrite(cu, rewrite);
//		
//		buf= new StringBuffer();
//		buf.append("package test1;\n");
//		buf.append("public class E {\n");
//		buf.append("    function foo() {\n");
//		buf.append("        goo(x instanceof List);\n");
//		buf.append("        goo(x instanceof Vector);\n");
//		buf.append("    }\n");
//		buf.append("}\n");	
//		assertEqualString(preview, buf.toString());
//		
//	}
	
	/** @deprecated using deprecated code */
	public void testMethodInvocation() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        foo(1, 2).goo();\n");
		buf.append("        foo(1, 2).goo();\n");
		buf.append("        foo(1, 2).goo();\n");
		buf.append("    }\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		AST ast= astRoot.getAST();
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");
		FunctionDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		Block block= methodDecl.getBody();
		List statements= block.statements();
		assertTrue("Number of statements not 3", statements.size() == 3);
		{ // remove expression, add param, change name
			ExpressionStatement stmt= (ExpressionStatement) statements.get(0);
			FunctionInvocation invocation= (FunctionInvocation) stmt.getExpression();
			
			rewrite.remove(invocation.getExpression(), null);
			
			SimpleName name= ast.newSimpleName("x");
			rewrite.replace(invocation.getName(), name, null);
			
			ASTNode arg= ast.newNumberLiteral("1");
			rewrite.getListRewrite(invocation, FunctionInvocation.ARGUMENTS_PROPERTY).insertLast(arg, null);

		}
		{ // insert expression, delete params
			ExpressionStatement stmt= (ExpressionStatement) statements.get(1);
			FunctionInvocation invocation= (FunctionInvocation) stmt.getExpression();
			
			FunctionInvocation leftInvocation= (FunctionInvocation) invocation.getExpression();
			
			SimpleName newExpression= ast.newSimpleName("x");
			rewrite.set(leftInvocation, FunctionInvocation.EXPRESSION_PROPERTY, newExpression, null);
			
			List args= leftInvocation.arguments();
			rewrite.remove((ASTNode) args.get(0), null);
			rewrite.remove((ASTNode) args.get(1), null);
		}
		{ // remove expression, add it as parameter
			ExpressionStatement stmt= (ExpressionStatement) statements.get(2);
			FunctionInvocation invocation= (FunctionInvocation) stmt.getExpression();
			
			ASTNode placeHolder= rewrite.createCopyTarget(invocation.getExpression());
			
			rewrite.set(invocation, FunctionInvocation.EXPRESSION_PROPERTY, null, null);

			rewrite.getListRewrite(invocation, FunctionInvocation.ARGUMENTS_PROPERTY).insertLast(placeHolder, null);
		}
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        x(1);\n");
		buf.append("        x.foo().goo();\n");		
		buf.append("        goo(foo(1, 2));\n");
		buf.append("    }\n");
		assertEqualString(preview, buf.toString());
		
	}

	/** @deprecated using deprecated code */
	public void testMethodParamsRenameReorder() throws Exception {
		if (true)
			return;
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("    function m( y,  a) {\n");
		buf.append("        m(y, a);\n");
		buf.append("    }\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		AST ast= astRoot.getAST();
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");
		FunctionDeclaration methodDecl= findMethodDeclaration(astRoot, "m");
		Block block= methodDecl.getBody();
		List statements= block.statements();
		assertTrue("Number of statements not 1", statements.size() == 1);
		{ 
			//params 
			List params= methodDecl.parameters();
			SingleVariableDeclaration firstParam= (SingleVariableDeclaration) params.get(0);
			SingleVariableDeclaration secondParam= (SingleVariableDeclaration) params.get(1);

			//args
			ExpressionStatement stmt= (ExpressionStatement) statements.get(0);
			FunctionInvocation invocation= (FunctionInvocation) stmt.getExpression();
			List arguments= invocation.arguments();
			SimpleName first= (SimpleName) arguments.get(0);
			SimpleName second= (SimpleName) arguments.get(1);
			

			//rename args
			SimpleName newFirstArg= methodDecl.getAST().newSimpleName("yyy");
			SimpleName newSecondArg= methodDecl.getAST().newSimpleName("bb");
			rewrite.replace(first, newFirstArg, null);
			rewrite.replace(second, newSecondArg, null);
			

			//rename params
			SimpleName newFirstName= methodDecl.getAST().newSimpleName("yyy");
			SimpleName newSecondName= methodDecl.getAST().newSimpleName("bb");
			rewrite.replace(firstParam.getName(), newFirstName, null);
			rewrite.replace(secondParam.getName(), newSecondName, null);
			
			//reoder params
			ASTNode paramplaceholder1= rewrite.createCopyTarget(firstParam);
			ASTNode paramplaceholder2= rewrite.createCopyTarget(secondParam);
			
			rewrite.replace(firstParam, paramplaceholder2, null);
			rewrite.replace(secondParam, paramplaceholder1, null);
			
			//reorder args
			ASTNode placeholder1= rewrite.createCopyTarget(first);
			ASTNode placeholder2= rewrite.createCopyTarget(second);
			
			rewrite.replace(first, placeholder2, null);
			rewrite.replace(second, placeholder1, null);

			
		}
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("    function m(bb, yyy) {\n");
		buf.append("        m(bb, yyy);\n");
		buf.append("    }\n");
		assertEqualString(preview, buf.toString());
		
	}
	
	/** @deprecated using deprecated code */
	public void testMethodInvocation1() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        foo(foo(1, 2), 3);\n");
		buf.append("    }\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");
		FunctionDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		Block block= methodDecl.getBody();
		List statements= block.statements();
		assertTrue("Number of statements not 1", statements.size() == 1);
		{ // remove expression, add param, change name
			ExpressionStatement stmt= (ExpressionStatement) statements.get(0);
			FunctionInvocation invocation= (FunctionInvocation) stmt.getExpression();
			
			List arguments= invocation.arguments();
			FunctionInvocation first= (FunctionInvocation) arguments.get(0);
			ASTNode second= (ASTNode) arguments.get(1);
			
			ASTNode placeholder1= rewrite.createCopyTarget(first);
			ASTNode placeholder2= rewrite.createCopyTarget(second);
			
			rewrite.replace(first, placeholder2, null);
			rewrite.replace(second, placeholder1, null);
			
			List innerArguments= first.arguments();
			ASTNode innerFirst= (ASTNode) innerArguments.get(0);
			ASTNode innerSecond= (ASTNode) innerArguments.get(1);
			
			ASTNode innerPlaceholder1= rewrite.createCopyTarget(innerFirst);
			ASTNode innerPlaceholder2= rewrite.createCopyTarget(innerSecond);
			
			rewrite.replace(innerFirst, innerPlaceholder2, null);
			rewrite.replace(innerSecond, innerPlaceholder1, null);			
			
			
			
		}
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        foo(3, foo(2, 1));\n");
		buf.append("    }\n");
		assertEqualString(preview, buf.toString());
		
	}
	
//	public void testMethodInvocation2() throws Exception {
//		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
//		StringBuffer buf= new StringBuffer();
//		buf.append("    function foo() {\n");
//		buf.append("        this.foo(3);\n");
//		buf.append("        this.<String>foo(3);\n");
//		buf.append("    }\n");
//		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
//		
//		JavaScriptUnit astRoot= createAST3(cu);
//		AST ast= astRoot.getAST();
//		ASTRewrite rewrite= ASTRewrite.create(ast);
//		
//		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");
//		FunctionDeclaration methodDecl= findMethodDeclaration(type, "foo");
//		Block block= methodDecl.getBody();
//		List statements= block.statements();
//		assertTrue("Number of statements not 2", statements.size() == 2);
//		{ // add type arguments
//			ExpressionStatement stmt= (ExpressionStatement) statements.get(0);
//			FunctionInvocation invocation= (FunctionInvocation) stmt.getExpression();
//			SimpleType newType= ast.newSimpleType(ast.newSimpleName("String"));
//			ListRewrite listRewriter= rewrite.getListRewrite(invocation, FunctionInvocation.TYPE_ARGUMENTS_PROPERTY);
//			listRewriter.insertFirst(newType, null);
//		}
//		{ // remove type arguments
//			ExpressionStatement stmt= (ExpressionStatement) statements.get(1);
//			FunctionInvocation invocation= (FunctionInvocation) stmt.getExpression();
//			rewrite.remove((ASTNode) invocation.typeArguments().get(0), null);
//		}
//		String preview= evaluateRewrite(cu, rewrite);
//		
//		buf= new StringBuffer();
//		buf.append("package test1;\n");
//		buf.append("public class E {\n");
//		buf.append("    function foo() {\n");
//		buf.append("        this.<String>foo(3);\n");
//		buf.append("        this.foo(3);\n");
//		buf.append("    }\n");
//		buf.append("}\n");		
//		assertEqualString(preview, buf.toString());
//		
//	}	
//	
	/** @deprecated using deprecated code */
	public void testParenthesizedExpression() throws Exception {
		//System.out.println(getClass().getName()+"::" + getName() +" disabled (bug 23362)");
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        i= (1 + 2) * 3;\n");
		buf.append("    }\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		AST ast= astRoot.getAST();
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");
		FunctionDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		Block block= methodDecl.getBody();
		List statements= block.statements();
		assertTrue("Number of statements not 1", statements.size() == 1);
		{ // replace expression
			ExpressionStatement stmt= (ExpressionStatement) statements.get(0);
			Assignment assignment= (Assignment) stmt.getExpression();
			
			InfixExpression multiplication= (InfixExpression) assignment.getRightHandSide();
			
			ParenthesizedExpression parenthesizedExpression= (ParenthesizedExpression) multiplication.getLeftOperand();
						
			SimpleName name= ast.newSimpleName("x");
			rewrite.replace(parenthesizedExpression.getExpression(), name, null);
		}
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        i= (x) * 3;\n");
		buf.append("    }\n");
		assertEqualString(preview, buf.toString());
		
		
	}
	
	/** @deprecated using deprecated code */
	public void testPrefixExpression() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        i= --x;\n");
		buf.append("    }\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		AST ast= astRoot.getAST();
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");
		FunctionDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		Block block= methodDecl.getBody();
		List statements= block.statements();
		assertTrue("Number of statements not 1", statements.size() == 1);
		{ // modify operand and operation
			ExpressionStatement stmt= (ExpressionStatement) statements.get(0);
			Assignment assignment= (Assignment) stmt.getExpression();
			
			PrefixExpression preExpression= (PrefixExpression) assignment.getRightHandSide();
					
			NumberLiteral newOperation= ast.newNumberLiteral("10");
			rewrite.replace(preExpression.getOperand(), newOperation, null);
			
			rewrite.set(preExpression, PrefixExpression.OPERATOR_PROPERTY, PrefixExpression.Operator.COMPLEMENT, null);
		}
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        i= ~10;\n");
		buf.append("    }\n");
		assertEqualString(preview, buf.toString());
		
	}	
	
	/** @deprecated using deprecated code */
	public void testPostfixExpression() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        i= x--;\n");
		buf.append("    }\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		AST ast= astRoot.getAST();
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");
		FunctionDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		Block block= methodDecl.getBody();
		List statements= block.statements();
		assertTrue("Number of statements not 1", statements.size() == 1);
		{ // modify operand and operation
			ExpressionStatement stmt= (ExpressionStatement) statements.get(0);
			Assignment assignment= (Assignment) stmt.getExpression();
			
			PostfixExpression postExpression= (PostfixExpression) assignment.getRightHandSide();
					
			NumberLiteral newOperation= ast.newNumberLiteral("10");
			rewrite.replace(postExpression.getOperand(), newOperation, null);
			
			rewrite.set(postExpression, PostfixExpression.OPERATOR_PROPERTY, PostfixExpression.Operator.INCREMENT, null);
		}
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        i= 10++;\n");
		buf.append("    }\n");
		assertEqualString(preview, buf.toString());
		
	}		
	
	/** @deprecated using deprecated code */
	//TODO fix
	public void XtestThisExpression() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("function foo() {\n");
		buf.append("    return this;\n");		
		buf.append("    return Outer.this;\n");
		buf.append("}\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		AST ast= astRoot.getAST();
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);

		FunctionDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		Block block= methodDecl.getBody();
		List statements= block.statements();
		assertTrue("Number of statements not 2", statements.size() == 2);
		{ // add qualifier
			ReturnStatement returnStatement= (ReturnStatement) statements.get(0);
			
			ThisExpression thisExpression= (ThisExpression) returnStatement.getExpression();

			SimpleName newExpression= ast.newSimpleName("X");
			rewrite.set(thisExpression, ThisExpression.QUALIFIER_PROPERTY, newExpression, null);
		}
		{ // remove qualifier
			ReturnStatement returnStatement= (ReturnStatement) statements.get(1);
			
			ThisExpression thisExpression= (ThisExpression) returnStatement.getExpression();

			rewrite.remove(thisExpression.getQualifier(), null);
		}

			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        return X.this;\n");		
		buf.append("        return this;\n");
		buf.append("    }\n");
		assertEqualString(preview, buf.toString());
		
	}
	
	/** @deprecated using deprecated code */
	public void testSimpleName() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("    function foo(hello) {\n");
		buf.append("        return hello;\n");		
		buf.append("    }\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");

		FunctionDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		Block block= methodDecl.getBody();
		List statements= block.statements();
		assertTrue("Number of statements not 1", statements.size() == 1);
		{ // replace identifier
			ReturnStatement returnStatement= (ReturnStatement) statements.get(0);
			
			SimpleName simpleName= (SimpleName) returnStatement.getExpression();
			rewrite.set(simpleName, SimpleName.IDENTIFIER_PROPERTY, "changed", null);
		}
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("    function foo(hello) {\n");
		buf.append("        return changed;\n");		
		buf.append("    }\n");
		assertEqualString(preview, buf.toString());
		
	}
	
	/** @deprecated using deprecated code */
	public void testNumberLiteral() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        return 1;\n");		
		buf.append("    }\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");

		FunctionDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		Block block= methodDecl.getBody();
		List statements= block.statements();
		assertTrue("Number of statements not 1", statements.size() == 1);
		{ // replace number
			ReturnStatement returnStatement= (ReturnStatement) statements.get(0);
			
			NumberLiteral literal= (NumberLiteral) returnStatement.getExpression();
			rewrite.set(literal, NumberLiteral.TOKEN_PROPERTY, "11", null);
		}
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        return 11;\n");		
		buf.append("    }\n");
		assertEqualString(preview, buf.toString());
		
	}
	
	/** @deprecated using deprecated code */
	public void testBooleanLiteral() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        return true;\n");		
		buf.append("    }\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");

		FunctionDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		Block block= methodDecl.getBody();
		List statements= block.statements();
		assertTrue("Number of statements not 1", statements.size() == 1);
		{ // replace number
			ReturnStatement returnStatement= (ReturnStatement) statements.get(0);
			
			BooleanLiteral literal= (BooleanLiteral) returnStatement.getExpression();
			rewrite.set(literal, BooleanLiteral.BOOLEAN_VALUE_PROPERTY, Boolean.FALSE, null);
		}
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        return false;\n");		
		buf.append("    }\n");
		assertEqualString(preview, buf.toString());
		
	}
	
	/** @deprecated using deprecated code */
	public void testStringLiteral() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        return \"Hello\";\n");		
		buf.append("    }\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");

		FunctionDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		Block block= methodDecl.getBody();
		List statements= block.statements();
		assertTrue("Number of statements not 1", statements.size() == 1);
		{ // replace number
			ReturnStatement returnStatement= (ReturnStatement) statements.get(0);
			
			StringLiteral literal= (StringLiteral) returnStatement.getExpression();
			rewrite.set(literal, StringLiteral.ESCAPED_VALUE_PROPERTY, "\"Eclipse\"", null);
		}
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        return \"Eclipse\";\n");		
		buf.append("    }\n");
		assertEqualString(preview, buf.toString());
		
	}
	
	/** @deprecated using deprecated code */
	public void testCharacterLiteral() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        return 'x';\n");		
		buf.append("    }\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");

		FunctionDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		Block block= methodDecl.getBody();
		List statements= block.statements();
		assertTrue("Number of statements not 1", statements.size() == 1);
		{ // replace number
			ReturnStatement returnStatement= (ReturnStatement) statements.get(0);
			
			StringLiteral literal= (StringLiteral) returnStatement.getExpression();
			rewrite.set(literal, StringLiteral.ESCAPED_VALUE_PROPERTY, "'y'", null);
		}
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("    function foo() {\n");
		buf.append("        return 'y';\n");		
		buf.append("    }\n");
		assertEqualString(preview, buf.toString());
		
	}
			
}
