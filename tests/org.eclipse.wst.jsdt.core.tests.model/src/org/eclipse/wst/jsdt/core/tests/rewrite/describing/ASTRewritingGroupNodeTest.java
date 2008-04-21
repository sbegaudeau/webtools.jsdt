/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.rewrite.describing;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.jsdt.core.ICompilationUnit;
import org.eclipse.wst.jsdt.core.IPackageFragment;

import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.Block;
import org.eclipse.wst.jsdt.core.dom.CompilationUnit;
import org.eclipse.wst.jsdt.core.dom.ExpressionStatement;
import org.eclipse.wst.jsdt.core.dom.MethodDeclaration;
import org.eclipse.wst.jsdt.core.dom.MethodInvocation;
import org.eclipse.wst.jsdt.core.dom.ReturnStatement;
import org.eclipse.wst.jsdt.core.dom.Statement;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.rewrite.ASTRewrite;

public class ASTRewritingGroupNodeTest extends ASTRewritingTest {
	private static final Class THIS= ASTRewritingGroupNodeTest.class;

	public ASTRewritingGroupNodeTest(String name) {
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
	
	public void testCollapsedTargetNodes() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		
		StringBuffer buf= new StringBuffer();
		buf.append("    function foo(o) {\n");
		buf.append("        return;\n");
		buf.append("    }\n");
		ICompilationUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);

		CompilationUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		AST ast= astRoot.getAST();
		
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");
		MethodDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		
		ReturnStatement returnStatement= (ReturnStatement) methodDecl.getBody().statements().get(0);
		
		MethodInvocation newMethodInv1= ast.newMethodInvocation();
		newMethodInv1.setName(ast.newSimpleName("foo1"));
		ExpressionStatement st1= ast.newExpressionStatement(newMethodInv1);
		
		MethodInvocation newMethodInv2= ast.newMethodInvocation();
		newMethodInv2.setName(ast.newSimpleName("foo2"));
		ExpressionStatement st2= ast.newExpressionStatement(newMethodInv2);
		
		ASTNode placeholder= rewrite.createGroupNode(new Statement[] { st1, st2 });
		rewrite.replace(returnStatement, placeholder, null);
			
		String preview= evaluateRewrite(cu, rewrite); 
		
		buf= new StringBuffer();
		buf.append("    function foo(o) {\n");
		buf.append("        foo1();\n");
		buf.append("        foo2();\n");
		buf.append("    }\n");
		String expected= buf.toString();		

		assertEqualString(preview, expected);
	}
	
	public void testCollapsedTargetNodes2() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		
		StringBuffer buf= new StringBuffer();
		buf.append("    function foo( o) {\n");
		buf.append("        {\n");
		buf.append("            return;\n");
		buf.append("        }\n");
		buf.append("    }\n");
		ICompilationUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);

		CompilationUnit astRoot= createAST(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		AST ast= astRoot.getAST();
		
//		TypeDeclaration type= findTypeDeclaration(astRoot, "E");
		MethodDeclaration methodDecl= findMethodDeclaration(astRoot, "foo");
		
		Statement statement= (Statement) methodDecl.getBody().statements().get(0);
		
		MethodInvocation newMethodInv1= ast.newMethodInvocation();
		newMethodInv1.setName(ast.newSimpleName("foo1"));
		ExpressionStatement st1= ast.newExpressionStatement(newMethodInv1);
		
		MethodInvocation newMethodInv2= ast.newMethodInvocation();
		newMethodInv2.setName(ast.newSimpleName("foo2"));
		ExpressionStatement st2= ast.newExpressionStatement(newMethodInv2);
		
		ReturnStatement st3= (ReturnStatement) rewrite.createCopyTarget((ASTNode) ((Block) statement).statements().get(0));
		
		ASTNode placeholder= rewrite.createGroupNode(new Statement[] { st1, st2, st3 });
		rewrite.replace(statement, placeholder, null);
		
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("    function foo( o) {\n");
		buf.append("        foo1();\n");
		buf.append("        foo2();\n");
		buf.append("        return;\n");
		buf.append("    }\n");
		String expected= buf.toString();		

		assertEqualString(preview, expected);
	}
	
}
