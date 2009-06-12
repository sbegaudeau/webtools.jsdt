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

package org.eclipse.wst.jsdt.core.tests.dom;

import junit.framework.Test;

import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;

public class ASTPositionsTest extends ConverterTestSetup {
	
	IJavaScriptUnit workingCopy;
	
	public void setUpSuite() throws Exception {
		super.setUpSuite();
		this.ast = AST.newAST(AST.JLS3);
	}
	
	public ASTPositionsTest(String name) {
		super(name);
	}

	public static Test suite() {
		return buildModelTestSuite(ASTPositionsTest.class);
	}
	
	private void sanityCheck(final String contents, JavaScriptUnit compilationUnit) {
		for (int i = 0, max = contents.length(); i < max; i++) {
    		final int lineNumber = compilationUnit.getLineNumber(i);
    		assertTrue("Wrong value for char at " + i, lineNumber >= 1);
    		final int columnNumber = compilationUnit.getColumnNumber(i);
    		assertTrue("Wrong value for char at " + i, columnNumber >= 0);
    		final int position = compilationUnit.getPosition(lineNumber, columnNumber);
    		assertTrue("Wrong value for char at i", position >= 0);
    		if (position == 0) {
    			assertEquals("Only true for first character", 0, i);
    		}
			assertEquals("Wrong char", contents.charAt(i), contents.charAt(position));
    	}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		if (this.workingCopy != null) {
			this.workingCopy.discardWorkingCopy();
			this.workingCopy = null;
		}
	}
		
	public void test001() throws JavaScriptModelException {
    	this.workingCopy = getWorkingCopy("/Converter/src/X.js", true/*resolve*/);
    	final String contents =
			"var d = new Date();\r\n" +
			"function X() {\r\n" +
			"	var date= d;\r\n" +
			"}";
    	ASTNode node = buildAST(
    			contents,
    			this.workingCopy,
    			false);
    	assertEquals("Not a compilation unit", ASTNode.JAVASCRIPT_UNIT, node.getNodeType());
    	JavaScriptUnit compilationUnit = (JavaScriptUnit) node;
    	assertEquals("Wrong char", 'X', contents.charAt(compilationUnit.getPosition(2, 9)));
    	assertEquals("Wrong char", 'v', contents.charAt(compilationUnit.getPosition(1, 0)));
    	assertEquals("Wrong position", -1, compilationUnit.getPosition(1, -1));
    	assertEquals("Wrong position", -1, compilationUnit.getPosition(-1, 0));
    	assertEquals("Wrong position", -1, compilationUnit.getPosition(5, 0));
    	assertEquals("Wrong position", -1, compilationUnit.getPosition(4, 1));
    	assertEquals("Wrong char", '}', contents.charAt(compilationUnit.getPosition(4, 0)));
    	assertEquals("Wrong char", '\r', contents.charAt(compilationUnit.getPosition(1, 19)));
    	
    	sanityCheck(contents, compilationUnit);
	}

	public void test002() throws JavaScriptModelException {
    	this.workingCopy = getWorkingCopy("/Converter/src/X.js", true/*resolve*/);
    	final String contents =
			"function X() {\n" +
			"	var map= null;\n" +
			"}\n";
    	ASTNode node = buildAST(
    			contents,
    			this.workingCopy,
    			false);
    	assertEquals("Not a compilation unit", ASTNode.JAVASCRIPT_UNIT, node.getNodeType());
    	JavaScriptUnit compilationUnit = (JavaScriptUnit) node;
    	sanityCheck(contents, compilationUnit);
	}
	
	public void test003() throws JavaScriptModelException {
    	this.workingCopy = getWorkingCopy("/Converter/src/X.js", true/*resolve*/);
    	final String contents =
			"function X() {\r" +
			"	var map= null;\r" +
			"}\r";
    	ASTNode node = buildAST(
    			contents,
    			this.workingCopy,
    			false);
    	assertEquals("Not a compilation unit", ASTNode.JAVASCRIPT_UNIT, node.getNodeType());
    	JavaScriptUnit compilationUnit = (JavaScriptUnit) node;
    	sanityCheck(contents, compilationUnit);
	}
	
	public void test004() throws JavaScriptModelException {
    	this.workingCopy = getWorkingCopy("/Converter/src/X.js", true/*resolve*/);
    	String contents =
			"function X() {}";
    	ASTNode node = buildAST(
    			contents,
    			this.workingCopy,
    			false);
       	assertEquals("Not a compilation unit", ASTNode.JAVASCRIPT_UNIT, node.getNodeType());
       	JavaScriptUnit compilationUnit = (JavaScriptUnit) node;
       	sanityCheck(contents, compilationUnit);
		assertEquals(1, compilationUnit.getLineNumber(0));
	}
	
	public void test005() throws JavaScriptModelException {
    	this.workingCopy = getWorkingCopy("/Converter/src/X.js", true/*resolve*/);
    	String contents =
			"function X() {}";
    	ASTNode node = buildAST(
    			contents,
    			this.workingCopy,
    			false);
       	assertEquals("Not a compilation unit", ASTNode.JAVASCRIPT_UNIT, node.getNodeType());
       	JavaScriptUnit compilationUnit = (JavaScriptUnit) node;
		assertEquals(1, compilationUnit.getLineNumber(0));
       	sanityCheck(contents, compilationUnit);
	}
}
