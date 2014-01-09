/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;

public class BasicJsdocTests extends AbstractRegressionTest {

	public BasicJsdocTests(String name) {
		super(name);
 
	}
	public void test001() {
		CompilationUnitDeclaration declaration = this.runJSDocParseTest(
			 "/**\n"
				+ " * Valid class javadoc\n"
				+ " * @param p1 param def\n"
				+ " */\n"
			+"function foo(p1){}" + 
			"\n",
			"X.js",
			"/**\n" +
			"   * @param p1\n" +
			" */\n" +
			"function foo(p1) {\n}" + 
			"\n"
			
		 );
	}

	public void test002() {
		CompilationUnitDeclaration declaration = this.runJSDocParseTest(
			 "/**\n"
				+ " * Valid class javadoc\n"
				+ " * @param {String} p1 param def\n"
				+ " */\n"
			+"function foo(p1){}" + 
			"\n",
			"X.js",
			"/**\n" +
			"   * @param {String} p1\n" +
			" */\n" +
			"function foo(p1) {\n}" + 
			"\n"
			
		 );
	}
	
//	public void test002b() {
//		CompilationUnitDeclaration declaration = this.runJSDocParseTest(
//			 "/**\n"
//				+ " * Valid class javadoc\n"
//				+ " * @param {String} p1 param def\n"
//				+ " */\n"
//			+"function foo(p1){}" + 
//			"\n",
//			"X.js",
//			"/**\n" +
//			"   * @param {String*} p1\n" +
//			" */\n" +
//			"function foo(p1) {\n}" + 
//			"\n"
//			
//		 );
//	}
	public void test003() {
		CompilationUnitDeclaration declaration = this.runJSDocParseTest(
			 "/**\n"
				+ " * Valid class javadoc\n"
				+ " * @param {String|Number} p1 param def\n"
				+ " */\n"
			+"function foo(p1){}" + 
			"\n",
			"X.js",
			"/**\n" +
			"   * @param {String|Number} p1\n" +
			" */\n" +
			"function foo(p1) {\n}" + 
			"\n"
			
		 );
	}
	public void test004() {
		CompilationUnitDeclaration declaration = this.runJSDocParseTest(
			 "/**\n"
				+ " * Valid class javadoc\n"
				+ " * @constructor \n"
				+ " */\n"
			+"function foo(p1){}" + 
			"\n",
			"X.js",
			"/**\n" +
			"   * @constructor\n" +
			" */\n" +
			"function foo(p1) {\n}" + 
			"\n"
			
		 );
	}
	public void test005() {
		CompilationUnitDeclaration declaration = this.runJSDocParseTest(
			 "/**\n"
				+ " * Valid class javadoc\n"
				+ " * @type String \n"
				+ " */\n"
			+"function foo(p1){}" + 
			"\n",
			"X.js",
			"/**\n" +
			"   * @type String\n" +
			" */\n" +
			"function foo(p1) {\n}" + 
			"\n"
			
		 );
	}

	public void test006() {
		CompilationUnitDeclaration declaration = this.runJSDocParseTest(
			 "/**\n"
				+ " * Valid class javadoc\n"
				+ " * @private \n"
				+ " */\n"
			+"function foo(p1){}" + 
			"\n",
			"X.js",
			"/**\n" +
			"   * @private\n" +
			" */\n" +
			"function foo(p1) {\n}" + 
			"\n"
			
		 );
	}

	public void test007() {
		CompilationUnitDeclaration declaration = this.runJSDocParseTest(
			 "/**\n"
				+ " * Valid class javadoc\n"
				+ " * @member MyClass \n"
				+ " */\n"
			+"function foo(p1){}" + 
			"\n",
			"X.js",
			"/**\n" +
			"   * @member MyClass\n" +
			" */\n" +
			"function foo(p1) {\n}" + 
			"\n"
			
		 );
	}

	public void test008() {
		CompilationUnitDeclaration declaration = this.runJSDocParseTest(
			 "/**\n"
				+ " * Valid class javadoc\n"
				+ " * @base MyClass \n"
				+ " */\n"
			+"function foo(p1){}" + 
			"\n",
			"X.js",
			"/**\n" +
			"   * @extends MyClass\n" +
			" */\n" +
			"function foo(p1) {\n}" + 
			"\n"
			
		 );
	}

	public void test010() {
		CompilationUnitDeclaration declaration = this.runJSDocParseTest(
				"i= { \n"+
				"/**\n" +
				"   * @type Number\n" +
				" */\n" +
				" a: 2 ,\n"+
				"/**\n" +
				"   * @type Int\n" +
				" */\n" +
				" b: 3+4};" + 
				"\n",
			"X.js",
			"i = {\n"+
			"  /**\n" +
			"   * @type Number\n" +
			" */\n" +
			"a : 2,\n"+
			"  /**\n" +
			"   * @type Int\n" +
			" */\n" +
			"b : (3 + 4)\n};" + 
			"\n"
		 );
		 			
	 
	}



}
