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
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.SimpleName;

public class ASTRewritingModifyingReplaceTest extends ASTRewritingModifyingTest {
	private static final Class THIS = ASTRewritingModifyingReplaceTest.class;
	
	public ASTRewritingModifyingReplaceTest(String name) {
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
		suite.addTest(new ASTRewritingModifyingReplaceTest("test0009"));
		return suite;
	}
	
	public void test0004() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0004", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("function X() {\n");
		buf.append("}\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createCU(cu, false);
		
		astRoot.recordModifications();
		
		List statements = astRoot.statements();
		SimpleName name = astRoot.getAST().newSimpleName("AAA");
		FunctionDeclaration f = (FunctionDeclaration)statements.get(0);
		f.setName(name);
		
		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("function AAA() {\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
	
	public void test0005() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0005", false, null);
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
		
		List statements = astRoot.statements();
		FunctionDeclaration functionDeclaration = a.newFunctionDeclaration();
		SimpleName name = a.newSimpleName("AAA");
		functionDeclaration.setName(name);
		statements.set(1, functionDeclaration);
		
		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("function X() {\n");
		buf.append("\n");
		buf.append("}\n");
		buf.append("function AAA() {\n");
		buf.append("}\n");
		buf.append("function Z() {\n");
		buf.append("\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
}
