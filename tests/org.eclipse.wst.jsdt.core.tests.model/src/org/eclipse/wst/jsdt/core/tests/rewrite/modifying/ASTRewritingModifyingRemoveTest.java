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
package org.eclipse.wst.jsdt.core.tests.rewrite.modifying;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.Block;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;

public class ASTRewritingModifyingRemoveTest extends ASTRewritingModifyingTest {
	private static final Class THIS = ASTRewritingModifyingRemoveTest.class;
	
	public ASTRewritingModifyingRemoveTest(String name) {
		super(name);
	}
	
	public static Test allTests() {
		return new Suite(THIS);
	}
	
		public static Test suite() {
		if (true) {
			return allTests();
		}
		TestSuite suite= new Suite("one test");
		suite.addTest(new ASTRewritingModifyingRemoveTest("test0009"));
		return suite;
	}
	
	public void test0003() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0003", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("function X() {\n");
		buf.append("\n");
		buf.append("}\n");
		buf.append("function Y() {\n");
		buf.append("\n");
		buf.append("}\n");
		buf.append("function Z() {\n");
		buf.append("\n");
		buf.append("}\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createCU(cu, false);
		
		astRoot.recordModifications();
		
		List functions = astRoot.statements();
		functions.remove(1);
		
		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("function X() {\n");
		buf.append("\n");
		buf.append("}\n");
		buf.append("function Z() {\n");
		buf.append("\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
	
	public void test0004() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0004", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("function X() {\n");
		buf.append("\n");
		buf.append("}\n");
		buf.append("function Y() {\n");
		buf.append("\n");
		buf.append("}\n");
		buf.append("function Z() {\n");
		buf.append("\n");
		buf.append("}\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createCU(cu, false);
		
		astRoot.recordModifications();
		
		AST a = astRoot.getAST();
		
		List functions = astRoot.statements();
		FunctionDeclaration declaration1 = a.newFunctionDeclaration();
		declaration1.setName(a.newSimpleName("A"));
		functions.add(1, declaration1);
		functions.remove(1);
		
		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("function X() {\n");
		buf.append("\n");
		buf.append("}\n");
		buf.append("function Y() {\n");
		buf.append("\n");
		buf.append("}\n");
		buf.append("function Z() {\n");
		buf.append("\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
	
//	public void test0005() throws Exception {
//		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0005", false, null);
//		StringBuffer buf= new StringBuffer();
//		buf.append("package test0005;\n");
//		buf.append("\n");
//		buf.append("/**\n");
//		buf.append(" * NOTHING\n");
//		buf.append(" * @since now\n");
//		buf.append(" */\n");
//		buf.append("public class X {\n");
//		buf.append("\n");
//		buf.append("}\n");
//		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
//		
//		JavaScriptUnit astRoot= parseCompilationUnit(cu, false);
//		
//		astRoot.recordModifications();
//		
//		List types = astRoot.types();
//		TypeDeclaration typeDeclaration = (TypeDeclaration)types.get(0);
//		typeDeclaration.setJavadoc(null);
//		
//		String preview = evaluateRewrite(cu, astRoot);
//		
//		buf= new StringBuffer();
//		buf.append("package test0005;\n");
//		buf.append("\n");
//		buf.append("\n");
//		buf.append("public class X {\n");
//		buf.append("\n");
//		buf.append("}\n");
//		assertEqualString(preview, buf.toString());
//	}
	
	public void test0006() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0006", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test0006;\n");
		buf.append("\n");
		buf.append("public class X {\n");
		buf.append("    void foo() {\n");
		buf.append("        bar1();\n");
		buf.append("        \n");
		buf.append("        //comment1\n");
		buf.append("        bar2();//comment2\n");
		buf.append("        //comment3\n");
		buf.append("        bar3();\n");
		buf.append("    }\n");
		buf.append("}\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createCU(cu, false);
		
		astRoot.recordModifications();
		
		List types = astRoot.types();
		TypeDeclaration typeDeclaration = (TypeDeclaration)types.get(0);
		FunctionDeclaration methodDeclaration = typeDeclaration.getMethods()[0];
		Block body = methodDeclaration.getBody();
		List statements = body.statements();
		statements.remove(1);
		
		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("package test0006;\n");
		buf.append("\n");
		buf.append("public class X {\n");
		buf.append("    void foo() {\n");
		buf.append("        bar1();\n");
		buf.append("        \n");
		buf.append("        //comment3\n");
		buf.append("        bar3();\n");
		buf.append("    }\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
	
//	public void test0007() throws Exception {
//		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0007", false, null);
//		StringBuffer buf= new StringBuffer();
//		buf.append("package test0007;\n");
//		buf.append("\n");
//		buf.append("public class X {\n");
//		buf.append("    /*\\u002A\n");
//		buf.append("     * NOTHING\n");
//		buf.append("     * @see Object\n");
//		buf.append("     */\n");
//		buf.append("    public class Y {\n");
//		buf.append("    \n");
//		buf.append("    }\n");
//		buf.append("}\n");
//		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
//		
//		JavaScriptUnit astRoot= parseCompilationUnit(cu, false);
//		
//		astRoot.recordModifications();
//		
//		List types = astRoot.types();
//		TypeDeclaration typeDeclaration = (TypeDeclaration)types.get(0);
//		typeDeclaration = typeDeclaration.getTypes()[0];
//		Javadoc javadoc = typeDeclaration.getJavadoc();
//		List tags = javadoc.tags();
//		tags.remove(0);
//		
//		String preview = evaluateRewrite(cu, astRoot);
//		
//		buf= new StringBuffer();
//		buf.append("package test0007;\n");
//		buf.append("\n");
//		buf.append("public class X {\n");
//		buf.append("    /*\\u002A\n");
//		buf.append("     * @see Object\n");
//		buf.append("     */\n");
//		buf.append("    public class Y {\n");
//		buf.append("    \n");
//		buf.append("    }\n");
//		buf.append("}\n");
//		assertEqualString(preview, buf.toString());
//	}
//	
//	public void test0008() throws Exception {
//		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0008", false, null);
//		StringBuffer buf= new StringBuffer();
//		buf.append("package test0008;\n");
//		buf.append("\n");
//		buf.append("public class X {\n");
//		buf.append("    /*\\u002A\n");
//		buf.append("     * NOTHING\n");
//		buf.append("     * @see Object\n");
//		buf.append("     */\n");
//		buf.append("    public class Y {\n");
//		buf.append("    \n");
//		buf.append("    }\n");
//		buf.append("}\n");
//		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
//		
//		JavaScriptUnit astRoot= parseCompilationUnit(cu, false);
//		
//		astRoot.recordModifications();
//		
//		List types = astRoot.types();
//		TypeDeclaration typeDeclaration = (TypeDeclaration)types.get(0);
//		typeDeclaration = typeDeclaration.getTypes()[0];
//		Javadoc javadoc = typeDeclaration.getJavadoc();
//		List tags = javadoc.tags();
//		tags.remove(1);
//		
//		String preview = evaluateRewrite(cu, astRoot);
//		
//		buf= new StringBuffer();
//		buf.append("package test0008;\n");
//		buf.append("\n");
//		buf.append("public class X {\n");
//		buf.append("    /*\\u002A\n");
//		buf.append("     * NOTHING\n");
//		buf.append("     */\n");
//		buf.append("    public class Y {\n");
//		buf.append("    \n");
//		buf.append("    }\n");
//		buf.append("}\n");
//		assertEqualString(preview, buf.toString());
//	}
	public void test0009() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0009", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test0009;\n");
		buf.append("\n");
		buf.append("public class X {\n");
		buf.append("    // comment1\n");
		buf.append("\n");
		buf.append("    // comment2\n");
		buf.append("    // comment3\n");
		buf.append("    void foo() {\n");
		buf.append("    }\n");
		buf.append("    // comment4\n");
		buf.append("    void foo2() {\n");
		buf.append("    }\n");
		buf.append("}\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createCU(cu, false);
		
		astRoot.recordModifications();
		
		List types = astRoot.types();
		TypeDeclaration typeDeclaration = (TypeDeclaration)types.get(0);
		FunctionDeclaration methodDeclaration = typeDeclaration.getMethods()[0];
		typeDeclaration.bodyDeclarations().remove(methodDeclaration);
		
		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("package test0009;\n");
		buf.append("\n");
		buf.append("public class X {\n");
		buf.append("    // comment1\n");
		buf.append("\n");
		buf.append("    // comment4\n");
		buf.append("    void foo2() {\n");
		buf.append("    }\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
	
	public void test0010() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0010", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test0010;\n");
		buf.append("\n");
		buf.append("public class X {\n");
		buf.append("    // comment1\n");
		buf.append("\n");
		buf.append("    // comment2\n");
		buf.append("    // comment3\n");
		buf.append("    void foo() {\n");
		buf.append("    }\n");
		buf.append("    // comment4\n");
		buf.append("\n");
		buf.append("    // comment5\n");
		buf.append("}\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createCU(cu, false);
		
		astRoot.recordModifications();
		
		List types = astRoot.types();
		TypeDeclaration typeDeclaration = (TypeDeclaration)types.get(0);
		FunctionDeclaration methodDeclaration = typeDeclaration.getMethods()[0];
		typeDeclaration.bodyDeclarations().remove(methodDeclaration);
		
		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("package test0010;\n");
		buf.append("\n");
		buf.append("public class X {\n");
		buf.append("\n");
		buf.append("    // comment5\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
	
	public void test0011() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0011", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test0011;\n");
		buf.append("public class X {\n");
		buf.append("    // one line comment\n");
		buf.append("    private void foo(){\n");
		buf.append("    }\n");
		buf.append("\n");
		buf.append("    /**\n");
		buf.append("     *\n");
		buf.append("     */\n");
		buf.append("    private void foo1(){\n");
		buf.append("    }\n");
		buf.append("\n");
		buf.append("    private void foo2(){\n");
		buf.append("    }\n");	
		buf.append("}\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createCU(cu, false);
		
		astRoot.recordModifications();
		
		List types = astRoot.types();
		TypeDeclaration typeDeclaration = (TypeDeclaration)types.get(0);
		FunctionDeclaration methodDeclaration = typeDeclaration.getMethods()[0];
		typeDeclaration.bodyDeclarations().remove(methodDeclaration);
		
		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("package test0011;\n");
		buf.append("public class X {\n");
		buf.append("    /**\n");
		buf.append("     *\n");
		buf.append("     */\n");
		buf.append("    private void foo1(){\n");
		buf.append("    }\n");
		buf.append("\n");
		buf.append("    private void foo2(){\n");
		buf.append("    }\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
}
