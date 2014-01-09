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
package org.eclipse.wst.jsdt.core.tests.rewrite.modifying;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;

public class ASTRewritingModifyingOtherTest extends ASTRewritingModifyingTest {
	private static final Class THIS = ASTRewritingModifyingOtherTest.class;
	
	public ASTRewritingModifyingOtherTest(String name) {
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
		suite.addTest(new ASTRewritingModifyingOtherTest("test0009"));
		return suite;
	}
	
	public void test0000() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0000", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("var test0000;\n");
		buf.append("function X {\n");
		buf.append("}\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createCU(cu, false);
		
		try {
			evaluateRewrite(cu, astRoot);
			assertTrue("rewrite did not fail even though recording not on", false);
		} catch (IllegalStateException e) {
		}
	}
	
	public void test0001() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0001", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("var test0001;\n");
		buf.append("function X() {\n");
		buf.append("}\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createCU(cu, false);
		
		astRoot.recordModifications();
		
		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("var test0001;\n");
		buf.append("function X() {\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
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
		
		AST a = astRoot.getAST();
		
		List functions = astRoot.statements();
		FunctionDeclaration functionDeclaration1 = a.newFunctionDeclaration();
		functionDeclaration1.setName(a.newSimpleName("A"));
		functions.add(1, functionDeclaration1);
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
}
