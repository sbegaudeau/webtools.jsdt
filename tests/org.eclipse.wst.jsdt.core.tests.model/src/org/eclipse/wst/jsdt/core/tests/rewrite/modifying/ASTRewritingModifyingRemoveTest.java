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
	
	public void test0005() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0005", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("/**\n");
		buf.append(" * NOTHING\n");
		buf.append(" * @since now\n");
		buf.append(" */\n");
		buf.append("function X() {\n");
		buf.append("\n");
		buf.append("}\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createCU(cu, false);
		
		astRoot.recordModifications();
		
		List functions = astRoot.statements();
		FunctionDeclaration functionDeclaration = (FunctionDeclaration)functions.get(0);
		functionDeclaration.setJavadoc(null);
		
		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("function X() {\n");
		buf.append("\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
	
	public void Xtest0006() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0006", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("function foo() {\n");
		buf.append("    bar1();\n");
		buf.append("    \n");
		buf.append("    //comment1\n");
		buf.append("    bar2();//comment2\n");
		buf.append("    //comment3\n");
		buf.append("    bar3();\n");
		buf.append("}\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createCU(cu, false);
		
		astRoot.recordModifications();
		
		List functions = astRoot.statements();
		FunctionDeclaration functionDeclaration = (FunctionDeclaration)functions.get(0);
		Block body = functionDeclaration.getBody();
		List statements = body.statements();
		statements.remove(1);
		
		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("function foo() {\n");
		buf.append("    bar1();\n");
		buf.append("    \n");
		buf.append("    //comment3\n");
		buf.append("    bar3();\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
	
	public void test0011() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0011", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("function X() {\n");
		buf.append("    // one line comment\n");
		buf.append("    function foo(){\n");
		buf.append("    }\n");
		buf.append("\n");
		buf.append("    /**\n");
		buf.append("     *\n");
		buf.append("     */\n");
		buf.append("    function foo1(){\n");
		buf.append("    }\n");
		buf.append("\n");
		buf.append("    function foo2(){\n");
		buf.append("    }\n");	
		buf.append("}\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createCU(cu, false);
		
		astRoot.recordModifications();
		
		List functions = astRoot.statements();
		FunctionDeclaration functionDeclaration = (FunctionDeclaration)functions.get(0);
		functionDeclaration.getBody().statements().remove(0);
		
		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("function X() {\n");
		buf.append("    // one line comment\n");
		buf.append("    /**\n");
		buf.append("     *\n");
		buf.append("     */\n");
		buf.append("    function foo1(){\n");
		buf.append("    }\n");
		buf.append("\n");
		buf.append("    function foo2(){\n");
		buf.append("    }\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
}
