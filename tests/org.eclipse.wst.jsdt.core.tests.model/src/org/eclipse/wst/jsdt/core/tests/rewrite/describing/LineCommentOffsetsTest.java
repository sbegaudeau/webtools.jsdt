/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.rewrite.describing;

import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.ExpressionStatement;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.IfStatement;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.wst.jsdt.core.dom.rewrite.ListRewrite;
import org.eclipse.wst.jsdt.internal.core.dom.rewrite.LineCommentEndOffsets;

/**
 *
 */
public class LineCommentOffsetsTest extends ASTRewritingTest {
	
	private static final Class THIS= LineCommentOffsetsTest.class;

	public LineCommentOffsetsTest(String name) {
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
	
	public void testEmptyLineComments() throws Exception {
		
		StringBuffer buf= new StringBuffer();
		buf.append("\n");
		
		LineCommentEndOffsets offsets= new LineCommentEndOffsets(null);
		boolean res= offsets.isEndOfLineComment(0);
		assertFalse(res);
		res= offsets.remove(0);
		assertFalse(res);
	}
	
	public void testRemove() throws Exception {
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);

		StringBuffer buf= new StringBuffer();
		buf.append("package test1;//comment Y\n");
		buf.append("public class E//comment Y\n");
		buf.append("{//comment Y\n");
		buf.append("}//comment Y");	
		String contents= buf.toString();
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", contents, false, null);
		
		JavaScriptUnit astRoot= createAST3(cu);
		
		LineCommentEndOffsets offsets= new LineCommentEndOffsets(astRoot.getCommentList());

		int p1= contents.indexOf('Y') + 1;
		int p2= contents.indexOf('Y', p1) + 1;
		int p3= contents.indexOf('Y', p2) + 1;
		int p4= contents.indexOf('Y', p3) + 1;
		
		assertFalse(offsets.isEndOfLineComment(0));
		assertTrue(offsets.isEndOfLineComment(p1));
		assertTrue(offsets.isEndOfLineComment(p2));
		assertTrue(offsets.isEndOfLineComment(p3));
		assertTrue(offsets.isEndOfLineComment(p4));
		
		boolean res= offsets.remove(p2);
		assertTrue(res);
		
		res= offsets.remove(p2);
		assertFalse(res);
		
		assertFalse(offsets.isEndOfLineComment(0));
		assertTrue(offsets.isEndOfLineComment(p1));
		assertFalse(offsets.isEndOfLineComment(p2));
		assertTrue(offsets.isEndOfLineComment(p3));
		assertTrue(offsets.isEndOfLineComment(p4));
		
		res= offsets.remove(p4);
		assertTrue(res);
		
		assertFalse(offsets.isEndOfLineComment(0));
		assertTrue(offsets.isEndOfLineComment(p1));
		assertFalse(offsets.isEndOfLineComment(p2));
		assertTrue(offsets.isEndOfLineComment(p3));
		assertFalse(offsets.isEndOfLineComment(p4));
		
		res= offsets.remove(p1);
		assertTrue(res);
		
		assertFalse(offsets.isEndOfLineComment(0));
		assertFalse(offsets.isEndOfLineComment(p1));
		assertFalse(offsets.isEndOfLineComment(p2));
		assertTrue(offsets.isEndOfLineComment(p3));
		assertFalse(offsets.isEndOfLineComment(p4));
	}
	
	
	
	public void testLineCommentEndOffsets() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		
		
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("/* comment */\n");
		buf.append("// comment Y\n");
		buf.append("public class E {\n");
		buf.append("    public void foo() {\n");
		buf.append("        while (i == 0) {\n");
		buf.append("            foo();\n");
		buf.append("            i++; // comment Y\n");
		buf.append("            i++;\n");
		buf.append("        }// comment// comment Y\n");
		buf.append("        return;\n");
		buf.append("    }\n");
		buf.append("} // comment Y");
		String content= buf.toString();
		
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", content, false, null);
		JavaScriptUnit astRoot= createAST(cu);
		
		LineCommentEndOffsets offsets= new LineCommentEndOffsets(astRoot.getCommentList());
		HashSet expectedOffsets= new HashSet();

		for (int i= 0; i < content.length(); i++) {
			char ch= content.charAt(i);
			if (ch == 'Y') {
				expectedOffsets.add(new Integer(i + 1));
			}
		}
		
		int count= 0;
		
		char[] charContent= content.toCharArray();
		for (int i= 0; i <= content.length() + 5; i++) {
			boolean expected= i > 0 && i <= content.length() && charContent[i - 1] == 'Y';
			boolean actual= offsets.isEndOfLineComment(i, charContent);
			assertEquals(expected, actual);
			
			actual= offsets.isEndOfLineComment(i);
			assertEquals(expected, actual);
			
			if (expected) {
				count++;
			}
			
		}
		assertEquals(4, count);
	}
	
	public void testLineCommentEndOffsetsMixedLineDelimiter() throws Exception {
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("/* comment */\r\n");
		buf.append("// comment Y\n");
		buf.append("public class E {\r\n");
		buf.append("    public void foo() {\n");
		buf.append("        while (i == 0) {\n");
		buf.append("            foo();\n");
		buf.append("            i++; // comment Y\r\n");
		buf.append("            i++;\n");
		buf.append("        }// comment// comment Y\r");
		buf.append("        return;\n");
		buf.append("    }\r\n");
		buf.append("} // comment Y");
		String content= buf.toString();
		
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", content, false, null);
		JavaScriptUnit astRoot= createAST(cu);
		
		LineCommentEndOffsets offsets= new LineCommentEndOffsets(astRoot.getCommentList());
		HashSet expectedOffsets= new HashSet();

		for (int i= 0; i < content.length(); i++) {
			char ch= content.charAt(i);
			if (ch == 'Y') {
				expectedOffsets.add(new Integer(i + 1));
			}
		}
		
		int count= 0;
		
		char[] charContent= content.toCharArray();
		for (int i= 0; i <= content.length() + 5; i++) {
			boolean expected= i > 0 && i <= content.length() && charContent[i - 1] == 'Y';
			boolean actual= offsets.isEndOfLineComment(i, charContent);
			assertEquals(expected, actual);
			if (expected) {
				count++;
			}
			
		}
		assertEquals(4, count);
	}
	
	public void testBug95839() throws Exception {
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("function foo() {\n");
		buf.append("  object.method(\n");
		buf.append("    param1, // text about param1\n");
		buf.append("    param2  // text about param2\n");
		buf.append("  );\n");	
		buf.append("}\n");	
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST3(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		AST ast= astRoot.getAST();
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
		FunctionDeclaration function= findMethodDeclaration(astRoot, "foo");
		ExpressionStatement statement= (ExpressionStatement) function.getBody().statements().get(0);
		FunctionInvocation inv= (FunctionInvocation) statement.getExpression();
		
		ListRewrite listRewrite= rewrite.getListRewrite(inv, FunctionInvocation.ARGUMENTS_PROPERTY);
		listRewrite.insertLast(ast.newSimpleName("param3"), null);
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("function foo() {\n");
		buf.append("  object.method(\n");
		buf.append("    param1, // text about param1\n");
		buf.append("    param2  // text about param2\n");
		buf.append(", param3\n");
		buf.append("  );\n");	
		buf.append("}\n");	
		assertEqualString(preview, buf.toString());
	}
	
	public void testBug128818() throws Exception {
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("function foo() {\n");
		buf.append("  if (true) {\n");
		buf.append("  } // comment\n");
		buf.append("  else\n");
		buf.append("    return;\n");
		buf.append("}\n");	
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST3(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		AST ast= astRoot.getAST();
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
		FunctionDeclaration function= findMethodDeclaration(astRoot, "foo");
		IfStatement statement= (IfStatement) function.getBody().statements().get(0);
		
		rewrite.set(statement, IfStatement.ELSE_STATEMENT_PROPERTY, ast.newBlock(), null);
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("function foo() {\n");
		buf.append("  if (true) {\n");
		buf.append("  } // comment\n");
		buf.append(" else {\n");
		buf.append("}\n");	
		buf.append("}\n");	
		assertEqualString(preview, buf.toString());
	}
	
	public void testCommentAtEnd() throws Exception {
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("function E() \n");
		buf.append("{\n");
		buf.append("}//comment");	
		IJavaScriptUnit cu= pack1.createCompilationUnit("E.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createAST3(cu);
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
		AST ast= astRoot.getAST();
		
		assertTrue("Parse errors", (astRoot.getFlags() & ASTNode.MALFORMED) == 0);
		
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		FunctionDeclaration newFunction= ast.newFunctionDeclaration();
		newFunction.setName(ast.newSimpleName("B"));
		listRewrite.insertLast(newFunction, null);
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("function E() \n");
		buf.append("{\n");
		buf.append("}//comment\n");
		buf.append("\n");
		buf.append("function B() {\n");
		buf.append("}");
		assertEqualString(preview, buf.toString());
	}
	

	
}
