/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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
import org.eclipse.wst.jsdt.core.dom.JSdoc;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteral;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteralField;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.TagElement;
import org.eclipse.wst.jsdt.core.dom.TextElement;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationFragment;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationStatement;

public class ASTRewritingModifyingInsertTest extends ASTRewritingModifyingTest {
	private static final Class THIS = ASTRewritingModifyingInsertTest.class;
	
	public ASTRewritingModifyingInsertTest(String name) {
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
		suite.addTest(new ASTRewritingModifyingInsertTest("test0009"));
		return suite;
	}
	
	/**
	 * insert a new function at first position
	 */
	public void test0001() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0001", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("var test0001;\n");
		buf.append("\n");
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
		statements.add(0, functionDeclaration);
		
		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("function AAA() {\n");
		buf.append("}\n");
		buf.append("var test0001;\n");
		buf.append("\n");
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

	/**
	 * insert a new function
	 */
	public void test0002() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0002", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("var test0002;\n");
		buf.append("\n");
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
		statements.add(1, functionDeclaration);
		
		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("var test0002;\n");
		buf.append("\n");
		buf.append("function AAA() {\n");
		buf.append("}\n");
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
	/**
	 * insert a new function at last position
	 */
	public void test0003() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0003", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("var test0003;\n");
		buf.append("\n");
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
		statements.add(4, functionDeclaration);
		
		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("var test0003;\n");
		buf.append("\n");
		buf.append("function X() {\n");
		buf.append("\n");
		buf.append("}\n");
		buf.append("function Y() {\n");
		buf.append("\n");
		buf.append("}\n");
		buf.append("function Z() {\n");
		buf.append("\n");
		buf.append("}\n");
		buf.append("function AAA() {\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
	/**
	 * insert a new javadoc for a type
	 */ 
	public void test0004() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0004", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("var test0004;\n");
		buf.append("\n");
		buf.append("function X() {\n");
		buf.append("\n");
		buf.append("}\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createCU(cu, false);
		
		astRoot.recordModifications();
		
		AST a = astRoot.getAST();
		
		List statements = astRoot.statements();
		FunctionDeclaration functionDeclaration = (FunctionDeclaration)statements.get(1);
		JSdoc jsdoc = a.newJSdoc();
		List tags = jsdoc.tags();
		TagElement tag = a.newTagElement();
		List fragment = tag.fragments();
		TextElement text = a.newTextElement();
		text.setText("NOTHING");
		fragment.add(text);
		tags.add(tag);
		functionDeclaration.setJavadoc(jsdoc);
		
		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("var test0004;\n");
		buf.append("\n");
		buf.append("/**\n");
		buf.append(" * NOTHING\n");
		buf.append(" */\n");
		buf.append("function X() {\n");
		buf.append("\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
	
	/**
	 * insert a new nested function
	 */
	public void test0005() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0005", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("function X() {\n");
		buf.append("}\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createCU(cu, false);
		
		astRoot.recordModifications();
		
		AST a = astRoot.getAST();
		
		List statements = astRoot.statements();
		FunctionDeclaration functionDeclaration = (FunctionDeclaration)statements.get(0);
		Block functionBody = functionDeclaration.getBody();
		FunctionDeclaration functionDeclaration2 = a.newFunctionDeclaration();
		functionDeclaration2.setName(a.newSimpleName("Z"));
		functionBody.statements().add(functionDeclaration2);
		
		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("function X() {\n");
		buf.append("    function Z() {\n");
		buf.append("    }\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
	
	/**
	 * insert a new member type after another member type
	 */
	public void test0007() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0007", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("function X() {\n");
		buf.append("    function Y() {\n");
		buf.append("    }\n");
		buf.append("}\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createCU(cu, false);
		
		astRoot.recordModifications();
		
		AST a = astRoot.getAST();
		
		List statements = astRoot.statements();
		FunctionDeclaration functionDeclaration = (FunctionDeclaration)statements.get(0);
		Block functionBody = functionDeclaration.getBody();
		FunctionDeclaration functionDeclaration2 = a.newFunctionDeclaration();
		functionDeclaration2.setName(a.newSimpleName("Z"));
		functionBody.statements().add(functionDeclaration2);
		
		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("function X() {\n");
		buf.append("    function Y() {\n");
		buf.append("    }\n");
		buf.append("    function Z() {\n");
		buf.append("    }\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
	
	public void test0008() throws Exception {
		String source = "\n";
		JavaScriptUnit astRoot= createCU(source.toCharArray());
		astRoot.recordModifications();
		
		AST a = astRoot.getAST();
		
		FunctionDeclaration functionDeclaration = a.newFunctionDeclaration();
		functionDeclaration.setName(a.newSimpleName("X"));
		
		astRoot.statements().add(functionDeclaration);
		
		String preview = evaluateRewrite(source, astRoot);
		
		StringBuffer buf= new StringBuffer();
		buf.append("\n");
		buf.append("function X() {\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
	
	/**
	 * insert a new function in an object literal
	 */
	public void test0009() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test0009", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("var o = {\n");
		buf.append("con : function(args){},\n");
		buf.append("fun1 : function(args){}\n");
		buf.append("};\n");
		
		IJavaScriptUnit cu= pack1.createCompilationUnit("X.js", buf.toString(), false, null);
		
		JavaScriptUnit astRoot= createCU(cu, false);
		
		astRoot.recordModifications();
		
		AST a = astRoot.getAST();
		
		List statements = astRoot.statements();
		VariableDeclarationStatement varDeclaration = (VariableDeclarationStatement)statements.get(0);
		VariableDeclarationFragment frag = (VariableDeclarationFragment) varDeclaration.fragments().get(0);
		ObjectLiteral obLit = (ObjectLiteral) frag.getInitializer();
		List fields = obLit.fields();
		ObjectLiteralField newObjectLiteralField = a.newObjectLiteralField();
		newObjectLiteralField.setFieldName(a.newSimpleName("newMethod"));
		newObjectLiteralField.setInitializer(a.newFunctionExpression());
		fields.add(newObjectLiteralField);

		String preview = evaluateRewrite(cu, astRoot);
		
		buf= new StringBuffer();
		buf.append("var o = {\n");
		buf.append("con : function(args){},\n");
		buf.append("fun1 : function(args){}, ");
		buf.append("newMethod : function (){}\n");
		buf.append("};\n");
		assertEqualString(preview, buf.toString());
	}
}
