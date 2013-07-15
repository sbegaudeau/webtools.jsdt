/*******************************************************************************
 * Copyright (c) 2005, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.core.infer.InferOptions;
import org.eclipse.wst.jsdt.core.infer.InferredType;
import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;

public class InferTypesTests extends AbstractRegressionTest {

	public InferTypesTests(String name) {
		super(name);
 
	}
	
	protected InferOptions getDefaultOptions()
	{
		InferOptions inferOptions=new InferOptions();
		inferOptions.setDefaultOptions();
		return inferOptions;
	}
	 
	/**
	 * @param typeName
	 * @param precursorToBodyStart
	 * @param precursorToBodyEnd
	 */
	private void verifySourceRangeWithContents(CompilationUnitDeclaration declaration, String typeName, String precursorToBodyStart, String precursorToBodyEnd) {
		InferredType testType = (InferredType) declaration.inferredTypesHash.get(typeName.toCharArray());
		assertNotNull(typeName + " not found in CompilationUnitDeclaration", testType);
		char[] contents = declaration.compilationResult.compilationUnit.getContents();
		assertNotNull(contents);
		assertTrue(contents.length > 0);
		char[] precursor = precursorToBodyStart.toCharArray();
		assertTrue("precursorToBodyStart not found", CharOperation.indexOf(precursorToBodyStart.toCharArray(), contents, true) >= 0);
		int expectedStart = CharOperation.indexOf(precursor, contents, true) + precursor.length - 1;
		assertTrue("precursorToBodyStart not unique", CharOperation.indexOf(precursor, contents, true, expectedStart, contents.length - 1) < 0);

		assertTrue("precursorToBodyEnd not found", CharOperation.indexOf(precursorToBodyEnd.toCharArray(), contents, true) >= 0);
		int expectedEnd = CharOperation.indexOf(precursorToBodyEnd.toCharArray(), contents, true) + precursorToBodyEnd.length() - 1;
		assertTrue("precursorToBodyEnd not unique", CharOperation.indexOf(precursorToBodyEnd.toCharArray(), contents, true, expectedEnd, contents.length - 1) < 0);

		assertEquals("Wrong sourceStart for " + new String(testType.getName()), expectedStart, testType.sourceStart());
		assertEquals("Wrong sourceEnd for " + new String(testType.getName()), expectedEnd, testType.sourceEnd());
	}
		
	public void test001() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function MyClass() {\n"+
			"  this.url = \"\";\n"+
			"  this.activate = function(){}\n"+
			"}\n"+
			"var myClassObj = new MyClass();\n"+
			"\n",
			"X.js",
			"class MyClass extends Object{\n" +
			"  String url;\n" +
			"  void activate()\n" +
			"  MyClass()\n" +
			"}\n",
			getDefaultOptions()
			
		 );
	}
	
	
	public void test002() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"Shape.prototype.GetArea = Shape_GetArea;"+ 
				"function Shape(){}"+
				"function Shape_GetArea(){"+
				" var area = 5;"+ 
				"  return area;"+ 
				"}",
			"X.js",
			"class Shape extends Object{\n" +
			"  Number GetArea()\n" +
			"  Shape()\n" +
			"}\n",
			getDefaultOptions()
			
		 );
	}
	public void test003() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"Shape.prototype.GetArea = function(a){};"+ 
				"function Shape(){}\n"+
				"",
			"X.js",
			"class Shape extends Object{\n  void GetArea(a)\n  Shape()\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test004() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"Shape.prototype.GetArea = Shape_GetArea;"+ 
				"function Shape(){}"+
				"function Shape_GetArea()"+
				"{"+
				"return this.area;"+ 
				"}"+
				"Circle.prototype = new Shape();"+ 
				"Circle.prototype.constructor = Circle;"+
				"Circle.prototype.GetArea = Circle_GetArea;"+
				"function Circle_GetArea()"+
				"{"+
				"}",
			"X.js",
			"class Shape extends Object{\n  ?? GetArea()\n  Shape()\n}\n"+
			"class Circle extends Shape{\n  Circle constructor;\n  void GetArea()\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	
	/*
	 * This test setting members using the this.
	 * 
	 * The InferEngine will no be able to tell the types of the members... there is no information
	 * provided.
	 */
	public void test010() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			      "function Bob(firstname, lastname) {\n" +
			      "   this.Firstname = firstname;\n" +
			      "   this.Lastname = lastname;\n" +
			      "}\n" +
			      "Bob.prototype.name = function () {return this.Firstname + this.Lastname;};\n",
			"X.js",
			"class Bob extends Object{\n  ?? Firstname;\n  ?? Lastname;\n  ?? name()\n  Bob(firstname, lastname)\n}\n",
			getDefaultOptions()
			
		 );
	}	
	public void test011() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				 "function X() {\n"
				+ "  this.h=1;\n"
				+ "  this.i=[];\n"
				+ "}\n"
				+ "function X_foo() {\n"
				+ "}\n"
				+ "X.prototype.foo=X_foo;\n"
				+ "",
				"X.js",
			"class X extends Object{\n  Number h;\n  Array i;\n  void foo()\n  X()\n}\n",
			getDefaultOptions()
			
		 );
	}

	public void test011b() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				 "P.prototype=new Object();\n"
				+ "P.prototype.f=1;\n"
				+ "function P(){}\n"
				+ "function a(){}\n"
				+ "function m() {\n"
				+ "                this.f++;\n"
				+ "                var p= new P();\n"
				+ "                    a();"
				+ "}\n"
				+ "P.prototype.mm=m;\n",
				"X.js",
			"class P extends Object{\n  Number f;\n  void mm()\n  P()\n}\n",
			getDefaultOptions()
			
		 );
	}

	public void test012() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				 "Test.prototype=new Object();\n"
				+ "Test.x=1;\n",
				"X.js",
			"class Test extends Object{\n  static Number x;\n}\n",
			getDefaultOptions()
			
		 );
	}


	public void test013() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function MyClass() {\n"+
			"  this.arr = [1];\n"+
			"}\n"+
			"var myClassObj = new MyClass();\n"+
			"\n",
			"X.js",
			"class MyClass extends Object{\n  Array(Number) arr;\n  MyClass()\n}\n",
			getDefaultOptions()
			
		 );
	}

	

	public void test020() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"var foo;\n"+
			"  foo.onMouseDown = function () { return 1; };\n"+
			"\n",
			"X.js",
			"class ___anonymous_foo extends Object{\n  Number onMouseDown()\n}\n",
			getDefaultOptions()
			
		 );
	}

			
	
	public void test040() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				 "/**\n"
				+ " * @constructor \n"
				+ " */\n"
			+"function MyClass(){}"   
			+ "/**\n"
			+ " * @memberOf MyClass \n"
			+ " * @type String \n"
			+ " */\n"
		+"var s;"   
		+ "/**\n"
		+ " * @memberOf MyClass \n"
		+ " * @type Number \n"
		+ " */\n"
		+"function numValue(){};"   
		+"\n",
			"X.js",
			"class MyClass extends Object{\n  String s;\n  MyClass()\n  Number numValue()\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	



	public void test041() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				" i= { \n"+
				"/**\n" +
				"   * @memberOf MyClass\n" +
				"   * @type Number\n" +
				" */\n" +
				" a: 2 ,\n"+
				"/**\n" +
				"   * @memberOf MyClass\n" +
				"   * @type String\n" +
				" */\n" +
				" b: function(){}};" + 
				"\n",
				"X.js",
			"class MyClass extends Object{\n  Number a;\n  String b()\n}\n",
			getDefaultOptions()
			
		 );
	}
	

	public void test041a() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				" i= { \n"+
				"/**\n" +
				"   * @memberOf jsns.MyClass\n" +
				"   * @type jsns.Number\n" +
				" */\n" +
				" a: 2 ,\n"+
				"/**\n" +
				"   * @memberOf jsns.MyClass\n" +
				"   * @type jsns.String\n" +
				" */\n" +
				" b: function(){}};" + 
				"\n",
				"X.js",
			"class jsns.MyClass extends Object{\n  jsns.Number a;\n  jsns.String b()\n}\n",
			getDefaultOptions()
			
		 );
	}
	

	public void test042() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				 "/**\n"
				+ " * @constructor \n"
				+ " * @extends String \n"
				+ " */\n"
			+"function MyClass(){}"   

			+"MyClass.prototype = { \n"+
				"/**\n" +
				"   * @memberOf MyClass\n" +
				"   * @type Number\n" +
				" */\n" +
				" a: 2 ,\n"+
				"/**\n" +
				"   * @memberOf MyClass\n" +
				"   * @type String\n" +
				" */\n" +
				" b: function(){}};" + 
				"\n",
				"X.js",
			"class MyClass extends String{\n  Number a;\n  MyClass()\n  String b()\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test042a() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				 "/**\n"
				+ " * @constructor \n"
				+ " * @extends String \n"
				+ " */\n"
			+"function MyClass(){}"   

			+"MyClass.prototype = { \n"+
				"/**\n" +
				"   * @memberOf MyClass\n" +
				"   * @type jsns.Number\n" +
				" */\n" +
				" a: 2 ,\n"+
				"/**\n" +
				"   * @memberOf MyClass\n" +
				"   * @type jsns.String\n" +
				" */\n" +
				" b: function(){}};" + 
				"\n",
				"X.js",
			"class MyClass extends String{\n  jsns.Number a;\n  MyClass()\n  jsns.String b()\n}\n",
			getDefaultOptions()
			
		 );
	}
	

	public void test043() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				 "/**\n"
				+ " * @constructor \n"
				+ " */\n"
			+"function MyClass(){}"   
		+ "/**\n"
		+ " * @memberOf MyClass \n"
		+ " * @param {Number} p1\n" 
		+ " * @type String \n"
		+ " */\n"
		+"function foo(p1){};"   
		+"\n",
			"X.js",
			"class MyClass extends Object{\n  MyClass()\n  String foo(Number p1)\n}\n",
			getDefaultOptions()
		 );
	}
	
	public void test043a() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				 "/**\n"
				+ " * @constructor \n"
				+ " */\n"
			+"function MyClass(){}"   
		+ "/**\n"
		+ " * @memberOf MyClass \n"
		+ " * @param {jsns2.Number} p1\n" 
		+ " * @type jsns.String \n"
		+ " */\n"
		+"function foo(p1){};"   
		+"\n",
			"X.js",
			"class MyClass extends Object{\n  MyClass()\n  jsns.String foo(jsns2.Number p1)\n}\n",
			getDefaultOptions()
		 );
	}
	
	public void test060() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"Shape.prototype.GetArea = Shape_GetArea;"+ 
				"function Shape_GetArea()"+
				"{"+
				" var str=\"\";"+ 
				"return str;"+ 
				"}",
			"X.js",
			"class Shape extends Object{\n  String GetArea()\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	/**
	 * Test Object literal local variable declaration
	 */
	public void test061() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var foo = {"+ 
				"  bar: \"bar\","+
				"  bar2: function(){}"+
				"}",
			"X.js",
			"class ___anonymous_foo extends Object{\n" +
			"  String bar;\n" +
			"  void bar2()\n" +
			"}\n",
			getDefaultOptions()
			
		 );
	}
	
	/**
	 * Test Object literal assignment
	 */
	public void test062() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var foo;"+
				"foo = {"+ 
				"  bar: \"bar\","+
				"  bar2: function(){}"+
				"}",
			"X.js",
			"class ___anonymous_foo extends Object{\n" +
			"  String bar;\n" +
			"  void bar2()\n" +
			"}\n",
			getDefaultOptions()
			
		 );
	}
	
	/**
	 * Test nested Object literals
	 */
	public void test063() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var foo = {"+ 
				"  bar: \"bar\","+
				"  bar2: {" +
				"    bar3: \"bar3\"" +
				"  }"+
				"}",
			"X.js",
			"class ___anonymous_foo extends Object{\n" +
			"  String bar;\n" +
			"  ___anonymous32_51 bar2;\n" +
			"}\n"+
			"class ___anonymous32_51 extends Object{\n" +
			"  String bar3;\n" +
			"}\n",
			getDefaultOptions()
			
		 );
	}
	
	/**
	 * Runtime simple member assignment to Object literal
	 */
	public void test064() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var ns = {};" + 
				"ns.foo = \"\";" +
				"ns.bar = function(){" +
				"  return \"\";" +
				"}",
			"X.js",
			"class ___anonymous_ns extends Object{\n" +
			"  String foo;\n" +
			"  String bar()\n" +
			"}\n",
			getDefaultOptions()
			
		 );
	}
	
	/**
	 * Runtime complex member (setting to an Object literal) asignment to Object literal 
	 */
	public void test065() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var ns = {};"+ 
				"ns.foo = {" +
				"  bar: \"\""+
				"};",
			"X.js",
			"class ___anonymous_ns extends Object{\n" +
			"  ___anonymous_foo foo;\n" +
			"}\n"+
			"class ___anonymous_foo extends Object{\n" +
			"  String bar;\n" +
			"}\n",
			getDefaultOptions()
			
		 );
	}
	
	/**
	 * Assign Object Literal to prototype
	 */
	public void test066() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"function foo(){"+
				"};"+
				"foo.prototype = {"+
				"  bar: \"\""+ 
				"}",
			"X.js",
			"class foo extends Object{\n  String bar;\n  foo()\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	/**
	 * Assign Object Literal to a prototype member
	 */
	public void test067() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"function foo(){"+
				"};"+
				"foo.prototype.bar = {"+
				"  bar2: \"\""+ 
				"}",
			"X.js",
			"class foo extends Object{\n  ___anonymous37_48 bar;\n  foo()\n}\n"+
			"class ___anonymous37_48 extends Object{\n  String bar2;\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	/**
	 * namespaced type (new "class" nested inside an Object Literal)
	 */
	public void test068() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var ns = {};"+ 
				"ns.foo = function(){" +
				"};" +
				"ns.foo.prototype.bar = \"\";" +
				"ns.foo.prototype.bar2 = function(){" +
				"  return \"\";" +
				"}",
			"X.js",
			"class ___anonymous_ns extends Object{\n" +
			"  void foo()\n" +
			"}\n"+
			"class ns.foo extends Object{\n" +
			"  String bar;\n" +
			"  String bar2()\n" +
			"  ns.foo()\n" +
			"}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test068b() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var ns = {};"+ 
				"ns.foo = function(){};" +
				"function abc(){};" +
				"ns.foo2 = abc;" +
				"",
			"X.js",
			"class ___anonymous_ns extends Object{\n" +
			"  void foo()\n" +
			"  void foo2()\n" +
			"}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test068c() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var ns = {};"+ 
				"ns.foo = function(){};" +
				"ns.foo2 = ns.foo;" +
				"",
			"X.js",
			"class ___anonymous_ns extends Object{\n" +
			"  void foo()\n" +
			"  void foo2()\n" +
			"}\n",
			getDefaultOptions()
			
		 );
	}
	
	/**
	 * namespaced type (new "class" nested inside an Object Literal)
	 */
	public void test069() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var ns1 = {" +
				"  ns2: {}" +
				"};"+ 
				"ns1.ns2.foo = function(){" +
				"};" +
				"ns1.ns2.foo.prototype.bar = \"\";" +
				"ns1.ns2.foo.prototype.bar2 = function(){" +
				"  return \"\";" +
				"}",
			"X.js",
			"class ___anonymous_ns1 extends Object{\n" +
			"  ___anonymous18_19 ns2;\n" +
			"}\n"+
			"class ___anonymous18_19 extends Object{\n" +
			"  void foo()\n" +
			"}\n"+
			"class ns1.ns2.foo extends Object{\n" +
			"  String bar;\n" +
			"  String bar2()\n" +
			"  ns1.ns2.foo()\n" +
			"}\n",
			getDefaultOptions()
			
		 );
	}
	
	/*
	 * Test a potential problem with anonymous and members when returning an {} from a member
	 */
	public void test070() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var foo = {" +
				"  a: \"\"," +
				"  b: function(){" +
				"    return \"\";" +
				"  }"+
				"};" +
				"foo.c = \"\";" +
				"foo.d = function(x, y, z) {" +
				"  return { x : \"\", y : \"\", z : \"\" };" +
				"};",
			"X.js",
			"class ___anonymous_foo extends Object{\n" +
			"  String a;\n" +
			"  String c;\n" +
			"  String b()\n" +
			"  ___anonymous101_126 d(x, y, z)\n" +
			"}\n"+
			"class ___anonymous101_126 extends Object{\n" +
			"  String x;\n" +
			"  String y;\n" +
			"  String z;\n" +
			"}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test071() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"if( true ){" +
				"  var foo = {};" +
				"}" +
				"foo.bar = \"\"",
			"X.js",
			"class ___anonymous_foo extends Object{\n" +
			"  String bar;\n" +
			"}\n",
			getDefaultOptions()
			
		 );
	}
	
	/*
	 * Object literal within a function as return (need to prevent duplicates)
	 */
	public void test072() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var foo = function(){" +
				"	return {" +
				"		x: \"\"," +
				"		y: \"\"" +
				"	}" +
				"};",
			"X.js",
			"class ___anonymous29_46 extends Object{\n  String x;\n  String y;\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	/*
	 * Object literal within a function (not a return
	 */
	public void test073() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var foo = function(){" +
				"	var bar = {" +
				"		x: \"\"," +
				"		y: \"\"" +
				"	}" +
				"};",
			"X.js",
			"class ___anonymous32_49 extends Object{\n  String x;\n  String y;\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test074() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"/**\n" +
			"  * Object Node()\n" +
			"  * @super Object\n" +
			"  * @constructor\n" +
			"  * @class Node\n" +
			"  * @since Standard ECMA-262 3rd. Edition\n" +
			"  * @since Level 2 Document Object Model Core Definition.\n" +
			"  * @link   http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html\n" +
			" */\n" +
			"function Node(){};\n" +
			"/**\n" +
			"  * Property firstChild\n" + 
			"  * @type Node\n" +
			"  * @class Node\n" +
			"  * @see Node\n" +
			"\n" + 
			"  * @since Standard ECMA-262 3rd. Edition\n" + 
			"  * @since Level 2 Document Object Model Core Definition.\n" +
			"  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html\n" +     
			" */\n" +
			"Node.prototype.firstChild=new Node();\n" + 
			"/**\n" +
			"  * function insertBefore(newChild, refChild)\n" +   
			"  * @type Node\n" +
			"  * @class Node\n" +
			"  * @param newChilds Node\n" +
			"  * @param refChild Node\n" +
			"  * @return Node\n" +
			"  * @throws DOMException\n" +
			"  * @see Node\n" +
			"  * @since Standard ECMA-262 3rd. Edition\n" + 
			"  * @since Level 2 Document Object Model Core Definition.\n" +
			"  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html\n" +     
			" */\n" +
			"Node.prototype.insertBefore = function(newChild, refChild){};\n",
			"X.js",
			"class Node extends Object{\n  Node firstChild;\n  Node()\n  Node insertBefore(newChild, refChild)\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	/*
	 * Static member check
	 */
	public void test075() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"var x = function(){};" +
			"x.prototype = {};" +
			"x.foo = \"\";" +
			"x.bar = function(){" +
			"  return \"\";" +
			"}",
			"X.js",
			"class x extends Object{\n  static String foo;\n  static String bar()\n  x()\n}\n",
			getDefaultOptions()
			
		 );
	}


	/*
	 * only statics
	 */
	public void test075b() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function x(){};" +
			"x.foo = \"\";" +
			"x.bar = function(){" +
			"  return \"\";" +
			"}",
			"X.js",
			"class x extends Object{\n  static String foo;\n  static String bar()\n  x()\n}\n",
			getDefaultOptions()
			
		 );
	}

	
	
	/*
	 * Global Object mixin
	 */
	public void test080() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"(function(){" +
			"this.someField = 1;" +
			"})();" ,
			"X.js",
			"class @G extends Object{\n  Number someField;\n}\n",
			getDefaultOptions()
			
		 );
	}
	

	/*
	 * Static member on built-in
	 */
	public void test081() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"String.foo = \"\";" +
			"String.bar = function(){" +
			"  return \"\";" +
			"}",
			"X.js",
			"class String extends Object{\n  static String foo;\n  static String bar()\n}\n",
			getDefaultOptions()
			
		 );
	}
	

	public void test082() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"String.foo = \"\";" +
			"String.bar1 = String.bar2 = function(){" +
			"  return \"\";" +
			"}",
			"X.js",
			"class String extends Object{\n  static String foo;\n  static String bar1()\n  static String bar2()\n}\n",
			getDefaultOptions()
			
		 );
	}

	public void test083() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function Car() {" +
			"	this.color = 'red';" +
			"	this.Move = function() { return \"I'm moving\"; };" +
			"};" +
			"Car.Stop = function() { return \"I'm not moving\"; };" +
			"Car.engine = 'diesel';" +
			"",
			"X.js",
			"class Car extends Object{\n  String color;\n  static String engine;\n  String Move()\n  static String Stop()\n  Car()\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	// test type infered from function with 'this' assignments
	public void test084() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function Car() {" +
			"	this.color = 'red';" +
			"	this.Move = function() { return \"I'm moving\"; };" +
			"};" +
			"",
			"X.js",
			"class Car extends Object{\n  String color;\n  String Move()\n  Car()\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test085() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function Shape(l, w){" +
			"	this.length = l;" +
			"	this.width = w;" +
			"}",
			"X.js",
			"class Shape extends Object{\n  ?? length;\n  ?? width;\n  Shape(l, w)\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test086() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function Shape(l, w){" +
			"	this.length = l;" +
			"	this.width = w;" +
			"	return this.length * this.width;" +
			"}",
			"X.js",
			"class Shape extends Object{\n  ?? length;\n  ?? width;\n  Shape(l, w)\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test087() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function Shape(l, w){" +
			"	this.length = l;" +
			"	this.width = w;" +
			"}" + 
			"var s = new Shape(2, 3);" + 
			"s.area = function() {" +
			"	return this.length * this.width;" +
			"};",
			"X.js",
			"class Shape extends Object{\n" +
			"  ?? length;\n" +
			"  ?? width;\n" +
			"  Shape(l, w)\n" +
			"}\n" +
			"class ___anonymous_s extends Shape{\n" +
			"  Number area()\n" +
			"}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test088() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function Shape(l, w){" +
			"	this.length = l;" +
			"	this.width = w;" +
			"	this.area = function() {return this.length * this.width;};" +
			"}",
			"X.js",
			"class Shape extends Object{\n  ?? length;\n  ?? width;\n  Number area()\n  Shape(l, w)\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test089() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function Shape(l, w){" +
			"	this.length = l;" +
			"	this.width = w;" +
			"}" +
			"Shape.prototype.area = function() {return this.length * this.width;};",
			"X.js",
			"class Shape extends Object{\n  ?? length;\n  ?? width;\n  Number area()\n  Shape(l, w)\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test090() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function Shape(l, w){" +
			"	this.length = l;" +
			"	this.width = w;" +
			"}" +
			"Shape.CONSTANT = 3;",
			"X.js",
			"class Shape extends Object{\n  ?? length;\n  ?? width;\n  static Number CONSTANT;\n  Shape(l, w)\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test091() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function Round(r){" +
			"	this.rad = r;" +
			"}" +
			"Round.PI = 3.14;" +
			"Round.prototype.area = function() {return Round.PI * this.rad * this.rad;};" +
			"Round.equal = function(a, b) {" +
			"if(a == b) return true;" +
			"return false;" +
			"};",
			"X.js",
			"class Round extends Object{\n  ?? rad;\n  static Number PI;\n  Number area()\n  static Boolean equal(a, b)\n  Round(r)\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test092() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function Com(r, i){" +
			"	this.r1 = r;" +
			"	this.i1 = i;" +
			"}" +
			"Com.prototype.meth1 = function() {return 1;};" +
			"Com.prototype.meth2 = function() {return new Com(1, 2);};" +
			"Com.prototype.meth3 = function(that) {return new Com(that+1, that-1);};" +
			"Com.prototype.toString = function() {return \"hi\"};" +
			"Com.classMeth1 = function(a, b) {return new Com(a, b);};" +
			"Com.classMeth2 = function(a, b) {return new Com(a, b);};" +
			"Com.ZERO = new Com(0,0);" +
			"Com.ONE = new Com(1,0);",
			"X.js",
			"class Com extends Object{\n  ?? r1;\n  ?? i1;\n  static Com ZERO;\n  static Com ONE;\n" +
			"  Number meth1()\n  Com meth2()\n  Com meth3(that)\n  String toString()\n  static Com classMeth1(a, b)\n  static Com classMeth2(a, b)\n  Com(r, i)\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test093() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function Shape(l, w){" +
			"	this.length = function() {return l;};" +
			"	this.width = function() {return w;};" +
			"}" +
			"Shape.prototype.perimeter = function() {return (this.length * 2) + (this.width * 2);};",
			"X.js",
			"class Shape extends Object{\n  ?? length()\n  ?? width()\n  Number perimeter()\n  Shape(l, w)\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test094() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function Shape(l, w){" +
			"	this.length = l;" +
			"	this.width = w;" +
			"}" +
			"Shape.prototype.area = function() {return this.length * this.width;};" +
			"function SubShape(l, w, x) {" +
			"Shape.call(this, l, w);" +
			"this.x = y;" +
			"}" +
			"SubShape.prototype = new Shape();" +
			"SubShape.prototype.meth = function() {return 1};",
			"X.js",
			"class Shape extends Object{\n  ?? length;\n  ?? width;\n  Number area()\n  Shape(l, w)\n}\n" +
			"class SubShape extends Shape{\n  ?? x;\n  Number meth()\n  SubShape(l, w, x)\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test095() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function Abc(){" +
			"	this.mult = function(a, b){return a * b;};" +
			"	this.div = function(a, b){return a / b;};" +
			"	this.rem = function(a, b){return a % b;};" +
			"	this.sub = function(a, b){return a - b;};" +
			"}",
			"X.js",
			"class Abc extends Object{\n  Number mult(a, b)\n  Number div(a, b)\n  Number rem(a, b)\n  Number sub(a, b)\n  Abc()\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test096() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function Plus(){" +
			"	this.strings = function(){return \"a\" + \"b\";};" +
			"	this.oneStringOneNumber = function(){return \"a\" + 1;};" +
			"	this.oneStringOneNumber2 = function(){return \"3\" + 1;};" +
			"	this.numbers = function(){return 1 + 2;};" +
			"	this.unknownString = function(a){return a + \"b\";};" +
			"	this.unknownNumber = function(a){return a + 3;};" +
			"	this.unknownUnknown = function(a, b){return a + b;};" +
			"}",
			"X.js",
			"class Plus extends Object{\n  String strings()\n  String oneStringOneNumber()\n  String oneStringOneNumber2()\n  " +
			"Number numbers()\n  String unknownString(a)\n  ?? unknownNumber(a)\n  ?? unknownUnknown(a, b)\n  Plus()\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test097() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function Equality(){" +
			"	this.equalsEquals = function(){return \"a\" == \"b\";};" +
			"	this.equalsEqualsEquals = function(){return \"a\" === \"b\";};" +
			"	this.notEquals = function(){return \"a\" != \"b\";};" +
			"	this.notEqualsEquals = function(){return \"a\" !== \"b\";};" +
			"}",
			"X.js",
			"class Equality extends Object{\n  Boolean equalsEquals()\n  Boolean equalsEqualsEquals()\n  Boolean notEquals()\n  Boolean notEqualsEquals()\n  Equality()\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test098() {
		// BUG286010
		CompilationUnitDeclaration declaration = this.runInferTest(
			"var MyFunc = function () {};\n" +
			"MyFunc.myMeth = function () {};",
			"X.js",
			"class MyFunc extends Function{\n" +
			"  static void myMeth()\n" +
			"  MyFunc()\n" +
			"}\n",
			getDefaultOptions()
			
		 );
	}		

	public void test099() {
		// BUG278904
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function MyType(){}"+
			"MyType.prototype = new Object();\n"+
			"/**\n"+
			"  * Property length\n"+
			"  * @type    Number\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"MyType.prototype.length = \"\";\n",
			"X.js",
			"class MyType extends Object{\n  Number length;\n  MyType()\n}\n",
			getDefaultOptions()
			
		 );
	}		
	public void test099a() {
		// BUG278904
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function MyType(){}"+
			"MyType.prototype = new Object();\n"+
			"/**\n"+
			"  * Property length\n"+
			"  * @type    Number\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"MyType.prototype.length = \"\";\n"+
			"MyType.prototype.name = \"\";\n",
			"X.js",
			"class MyType extends Object{\n  Number length;\n  String name;\n  MyType()\n}\n",
			getDefaultOptions()
			
		 );
	}		
	public void test099b() {
		// BUG278904
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function MyType(){}"+
			"MyType.prototype = new Object();\n"+
			"/**\n"+
			"  * Property length\n"+
			"  * @type    Number\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"MyType.prototype.length = \"\";\n"+
			"MyType.prototype.name = \"\";\n"+
			"MyType.prototype.date = new Date();\n",
			"X.js",
			"class MyType extends Object{\n  Number length;\n  String name;\n  Date date;\n  MyType()\n}\n",
			getDefaultOptions()
			
		 );
	}		
	public void test099c() {
		// BUG278904
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function MyType(){}"+
			"MyType.prototype = new Object();\n"+
			"/**\n"+
			"  * Property length\n"+
			"  * @type    String\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"MyType.prototype.time = 1;\n"+
			"/**\n"+
			"  * Property length\n"+
			"  * @type    Number\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"MyType.prototype.length = \"\";\n"+
			"MyType.prototype.name = \"\";\n"+
			"MyType.prototype.date = new Date();\n",
			"X.js",
			"class MyType extends Object{\n  String time;\n  Number length;\n  String name;\n  Date date;\n  MyType()\n}\n",
			getDefaultOptions()
			
		 );
	}		
	public void test100() {
		// BUG278904
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function MyType(){}"+
			"MyType.prototype = new Object();\n"+
			"/**\n"+
			"  * Property length\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"MyType.prototype.length = \"\";\n",
			"X.js",
			"class MyType extends Object{\n  String length;\n  MyType()\n}\n",
			getDefaultOptions()
			
		 );
	}		
	public void test101() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"MyType = {\n"+
			"/**\n"+
			"  * Property length\n"+
			"  * @type    Number\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"length : \"value\"\n"+
			"};",
			"X.js",
			"class MyType extends Object{\n  Number length;\n}\n",
			getDefaultOptions()
			
		 );
	}		
	public void test102() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"MyType = {\n"+
			"/**\n"+
			"  * Property length\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"length : \"value\"\n"+
			"};",
			"X.js",
			"class MyType extends Object{\n  String length;\n}\n",
			getDefaultOptions()
			
		 );
	}		
	public void test103() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"MyTypeInner = {\n"+
			"/**\n"+
			"  * @memberOf   MyTypeInner\n"+
			" */\n"+  
			"length: 5\n"+
			"};\n"+
			"MyType = {\n"+
			"/**\n"+
			"  * Property events\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @type    MyTypeInner\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"events : \"\"\n"+
			"};",
			"X.js",
			"class MyTypeInner extends Object{\n  Number length;\n}\nclass MyType extends Object{\n  MyTypeInner events;\n}\n",
			getDefaultOptions()
			
		 );
	}
	public void test104() {
		// same as 103, except events is an object literal 
		CompilationUnitDeclaration declaration = this.runInferTest(
			"MyTypeInner = {\n"+
			"/**\n"+
			"  * @memberOf   MyTypeInner\n"+
			" */\n"+  
			"length: 5\n"+
			"};\n"+
			"MyType = {\n"+
			"/**\n"+
			"  * Property events\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @type    MyTypeInner\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"events : {}\n"+
			"};",
			"X.js",
			"class MyTypeInner extends Object{\n  Number length;\n}\nclass MyType extends Object{\n  MyTypeInner events;\n}\n",
			getDefaultOptions()
			
		 );
	}
	public void test105() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"MyTypeInner = {\n"+
			"/**\n"+
			"  * @memberOf   MyTypeInner\n"+
			" */\n"+  
			"length: 5\n"+
			"};\n"+
			"MyType = {\n"+
			"a : \"\",\n"+
			"/**\n"+
			"  * Property events\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @type    MyTypeInner\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"events : {},\n"+
			"b : 7\n"+
			"};",
			"X.js",
			"class MyTypeInner extends Object{\n  Number length;\n}\nclass MyType extends Object{\n  String a;\n  MyTypeInner events;\n  Number b;\n}\n",
			getDefaultOptions()
			
		 );
	}
	public void test106() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"MyTypeInner = {\n"+
			"/**\n"+
			"  * @memberOf   MyTypeInner\n"+
			" */\n"+  
			"length: 5\n"+
			"};\n"+
			"MyType = {\n"+
			"a : {},\n"+
			"/**\n"+
			"  * Property events\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @type    MyTypeInner\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"events : {},\n"+
			"b : {}\n"+
			"};",
			"X.js",
			"class MyTypeInner extends Object{\n  Number length;\n}\nclass MyType extends Object{\n  ___anonymous80_81 a;\n  MyTypeInner events;\n  ___anonymous220_221 b;\n}\nclass ___anonymous80_81 extends Object{\n}\nclass ___anonymous220_221 extends Object{\n}\n",
			getDefaultOptions()
			
		 );
	}
	public void test107() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"MyTypeInner = {\n"+
			"/**\n"+
			"  * @memberOf   MyTypeInner\n"+
			" */\n"+  
			"length: 5\n"+
			"};\n"+
			"MyType = {\n"+
			"a : {},\n"+
			"/**\n"+
			"  * Property events\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @type    MyTypeInner\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"events : {},\n"+
			"/**\n"+
			"  * Property b\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @type    String\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"b : {}\n"+
			"};",
			"X.js",
			"class MyTypeInner extends Object{\n  Number length;\n}\nclass MyType extends Object{\n  ___anonymous80_81 a;\n  MyTypeInner events;\n  String b;\n}\nclass ___anonymous80_81 extends Object{\n}\n",
			getDefaultOptions()
			
		 );
	}
	public void test108() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"MyTypeInner = {\n"+
			"/**\n"+
			"  * @memberOf   MyTypeInner\n"+
			" */\n"+  
			"length: 5\n"+
			"};\n"+
			"MyType = {\n"+
			"a : {},\n"+
			"/**\n"+
			"  * Property events\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @type    MyTypeInner\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"events : {},\n"+
			"b : {}\n"+
			"};",
			"X.js",
			"class MyTypeInner extends Object{\n  Number length;\n}\nclass MyType extends Object{\n  ___anonymous80_81 a;\n  MyTypeInner events;\n  ___anonymous220_221 b;\n}\nclass ___anonymous80_81 extends Object{\n}\nclass ___anonymous220_221 extends Object{\n}\n",
			getDefaultOptions()
			
		 );
	}
	public void test108a() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"MyType = {\n"+
			"/**\n"+
			"  * Property events\n"+
			"  * @memberOf   MyType\n"+
			"  * @type    MyOwnNamespace.String\n"+
			" */\n"+  
			"events : {},\n"+
			"b : \"\"\n"+
			"};",
			"X.js",
			"class MyType extends Object{\n  MyOwnNamespace.String events;\n  String b;\n}\n",
			getDefaultOptions()
			
		 );
	}
	public void test108d() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"MyTypeInner = {\n"+
			"/**\n"+
			"  * @memberOf   MyTypeInner\n"+
			" */\n"+  
			"length: 5\n"+
			"};\n"+
			"MyType = {\n"+
			"a : {},\n"+
			"/**\n"+
			"  * Property events\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @type    MyTypeInner\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"events : {},\n"+
			"b : 5\n"+
			"};",
			"X.js",
			"class MyTypeInner extends Object{\n  Number length;\n}\nclass MyType extends Object{\n  ___anonymous80_81 a;\n  MyTypeInner events;\n  Number b;\n}\nclass ___anonymous80_81 extends Object{\n}\n",
			getDefaultOptions()
			
		 );
	}
	public void test108b() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"MyTypeInner = {\n"+
			"/**\n"+
			"  * @memberOf   MyTypeInner\n"+
			" */\n"+  
			"length: 5\n"+
			"};\n"+
			"MyType = {\n"+
			"/**\n"+
			"  * Property events\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @type    MyTypeInner\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"events : {},\n"+
			"b : \"\"\n"+
			"};",
			"X.js",
			"class MyTypeInner extends Object{\n  Number length;\n}\nclass MyType extends Object{\n  MyTypeInner events;\n  String b;\n}\n",
			getDefaultOptions()
			
		 );
	}
	public void test108c() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"MyTypeInner = {\n"+
			"/**\n"+
			"  * @memberOf   MyTypeInner\n"+
			" */\n"+  
			"length: 5\n"+
			"};\n"+
			"MyType = {\n"+
			"/**\n"+
			"  * Property a\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"a : 5,\n"+
			"/**\n"+
			"  * Property events\n"+
			"  * @memberOf   MyType\n"+
			"  * @see     MyType\n"+
			"  * @type    MyTypeInner\n"+
			"  * @since   WTP 3.2.2\n"+
			" */\n"+  
			"events : {},\n"+
			"b : {}\n"+
			"};",
			"X.js",
			"class MyTypeInner extends Object{\n  Number length;\n}\nclass MyType extends Object{\n  Number a;\n  MyTypeInner events;\n  ___anonymous308_309 b;\n}\nclass ___anonymous308_309 extends Object{\n}\n",
			getDefaultOptions()
			
		 );
	}
//		public void test109() {
//			CompilationUnitDeclaration declaration = this.runInferTest(
//				"var foo = function () {\n"+
//				"this.length= 5;\n"+
//				"};\n",
//				"X.js",
//				"class foo extends Object{\n  Number length;\n}\n",
//				getDefaultOptions()
//				
//			 );
//		}
	
	public void testBug329803_1() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"var fun1 = function() {" +
			"	this.abc = 1" +
			"}",
			"X.js",
			"class fun1 extends Object{\n  Number abc;\n  fun1()\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void test201() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"foo.bar.Awesome = function(){\n"+
			"this.test = \"\";\n"+
			"this.func = function(){}\n"+
			"}\n",
			"X.js",
			"class foo.bar.Awesome extends Object{\n" +
			"  String test;\n" +
			"  void func()\n" +
			"  foo.bar.Awesome()\n" +
			"}\n",
			getDefaultOptions()
		);
	}
	
	public void test201_1() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"var foo = {\n" +
			"  bar : {}\n" +
			"}\n" +
			"foo.bar.Awesome = function(){\n"+
			"this.test = \"\";\n"+
			"this.func = function(){}\n"+
			"}\n",
			"X.js",
			"class ___anonymous_foo extends Object{\n" +
			"  ___anonymous20_21 bar;\n" +
			"}\n" +
			"class ___anonymous20_21 extends Object{\n" +
			"  void Awesome()\n" +
			"}\n" +
			"class foo.bar.Awesome extends Object{\n" +
			"  String test;\n" +
			"  void func()\n" +
			"  foo.bar.Awesome()\n" +
			"}\n",
			getDefaultOptions()
		);
	}
	
	public void test202() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"Test = function(/** String */ arg1){\n"+
			"/**@type String */\n"+
			"this.test = arg1;\n"+
			"}\n",
			"X.js",
			"class Test extends Object{\n  String test;\n  Test(arg1)\n}\n",
			getDefaultOptions()
		);
	}
	
	public void testBUG317281() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function A$b(){\n"+
			"}\n" +
			"A$b.prototype.fun1 = function() {};\n",
			"X.js",
			"class A$b extends Object{\n  void fun1()\n  A$b()\n}\n",
			getDefaultOptions()
		);
	}
	
	public void testBUG343691() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function $(){\n"+
			"}\n" +
			"$.hasData = function(element) {};\n" +
			"$.prototype.jquery = \"\";\n",
			"X.js",
			"class $ extends Object{\n" +
			"  String jquery;\n" +
			"  $()\n" +
			"  static void hasData(element)\n" +
			"}\n",
			getDefaultOptions()
		);
	}
	public void testHierarchyLoop1() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"Test = function(){\n"+
			"this.test = \"\";\n"+
			"}\n"+
			"Test2 = function() {\n"+
			"this.test2 = \"\""+
			"}\n"+
			"Test.prototype = new Test2();\n"+
			"Test2.prototype = new Test();\n",
			"X.js",
			"class Test extends Test2{\n" + 
			"  String test;\n" + 
			"  Test()\n" + 
			"}\n" + 
			"class Test2 extends Object{\n" + 
			"  String test2;\n" + 
			"  Test2()\n" + 
			"}\n",
			getDefaultOptions()
		);
	}
	public void testHierarchyLoop2() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"Test = function(){\n"+
			"this.test = 5.0;\n"+
			"}\n"+
			"Test.prototype = new Test();\n",
			"X.js",
			"class Test extends Object{\n  Number test;\n  Test()\n}\n",
			getDefaultOptions()
		);
	}
	
	
	public void testPrototypeAssignmentNotTreatedAsPartOfFunctionName() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"function testPrototypeNotTreatedAsPartOfName(){};\n"+
			"testPrototypeNotTreatedAsPartOfName.prototype.toString = function(){return \"\";};\n",
			"X.js",
			"class testPrototypeNotTreatedAsPartOfName extends Object{\n  String toString()\n  testPrototypeNotTreatedAsPartOfName()\n}\n",
			getDefaultOptions()
		);
	}
	
	public void testFunctionNamed_Function_ShouldNotBecomeConstructorForFunctionType() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var test0 = function() {};" +
				"var test1 = {" +
				"	Function : function(test) {}" +
				"}",
				"X.js",
				"class ___anonymous_test1 extends Object{\n" +
				"  void Function(test)\n" +
				"}\n",
				getDefaultOptions()
		);
		verifySourceRangeWithContents(declaration, "___anonymous_test1", "test1 = {", "}}");
	}
	
	public void testAssigningToFieldsOnGlobals_0() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"foo.bar = 42;",
				"X.js",
				"class ___anonymous_foo extends Object{\n" +
				"  Number bar;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_1() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"foo = {}" +
				"foo.bar = 42;",
				"X.js",
				"class ___anonymous_foo extends Object{\n" +
				"  Number bar;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_2() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var foo = {}" +
				"foo.bar = 42;",
				"X.js",
				"class ___anonymous_foo extends Object{\n" +
				"  Number bar;\n" +
				"}\n",
				getDefaultOptions()
			);
		verifySourceRangeWithContents(declaration, "___anonymous_foo", "foo = {", "{}");
	}
	
	public void testAssigningToFieldsOnGlobals_3() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"foo.bar.awesome = 42;",
				"X.js",
				"class ___anonymous0_6 extends Object{\n" +
				"  Number awesome;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_4() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"foo = {}" +
				"foo.bar.awesome = 42;",
				"X.js",
				"class ___anonymous_foo extends Object{\n" +
				"  ___anonymous8_14 bar;\n" +
				"}\n" +
				"class ___anonymous8_14 extends Object{\n" +
				"  Number awesome;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_5() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"foo = {" +
				"  bar : {}" +
				"}" +
				"foo.bar.awesome = 42;",
				"X.js",
				"class ___anonymous_foo extends Object{\n" +
				"  ___anonymous15_16 bar;\n" +
				"}\n" +
				"class ___anonymous15_16 extends Object{\n" +
				"  Number awesome;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_6() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var foo = {}" +
				"foo.bar.awesome = 42;",
				"X.js",
				"class ___anonymous_foo extends Object{\n" +
				"  ___anonymous12_18 bar;\n" +
				"}\n" +
				"class ___anonymous12_18 extends Object{\n" +
				"  Number awesome;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_7() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var foo = {" +
				"  bar : {}" +
				"}" +
				"foo.bar.awesome = 42;",
				"X.js",
				"class ___anonymous_foo extends Object{\n" +
				"  ___anonymous19_20 bar;\n" +
				"}\n" +
				"class ___anonymous19_20 extends Object{\n" +
				 "  Number awesome;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_8() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"(function() {" +
				"  foo.bar = 42;" +
				"})();",
				"X.js",
				"class @G extends Object{\n" +
				"}\n" +
				"class ___anonymous_foo extends Object{\n" +
				"  Number bar;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_9() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"(function() {" +
				"  this.foo.bar = 42;" +
				"})();",
				"X.js",
				"class @G extends Object{\n" +
				"  ___anonymous_foo foo;\n" +
				"}\n" +
				"class ___anonymous_foo extends Object{\n" +
				"  Number bar;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_10() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"(function() {" +
				"  foo = {};" +
				"  foo.bar = 42;" +
				"})();",
				"X.js",
				"class @G extends Object{\n" +
				"}\n" +
				"class ___anonymous_foo extends Object{\n" +
				"  Number bar;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_11() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"(function() {" +
				"  this.foo = {}" +
				"  foo.bar = 42;" +
				"})();",
				"X.js",
				"class @G extends Object{\n" +
				"  ___anonymous_foo foo;\n" +
				"}\n" +
				"class ___anonymous_foo extends Object{\n" +
				"  Number bar;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_12() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"(function() {" +
				"  this.foo = {}" +
				"  this.foo.bar = 42;" +
				"})();",
				"X.js",
				"class @G extends Object{\n" +
				"  ___anonymous_foo foo;\n" +
				"}\n" +
				"class ___anonymous_foo extends Object{\n" +
				"  Number bar;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_13() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"(function() {" +
				"  foo.bar.awesome = 42;" +
				"})();",
				"X.js",
				"class @G extends Object{\n" +
				"}\n" +
				"class ___anonymous15_21 extends Object{\n" +
				"  Number awesome;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_14() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"(function() {" +
				"  this.foo.bar.awesome = 42;" +
				"})();",
				"X.js",
				"class @G extends Object{\n" +
				"  ___anonymous_foo foo;\n" +
				"}\n" +
				"class ___anonymous_foo extends Object{\n" +
				"  ___anonymous15_26 bar;\n" +
				"}\n" +
				"class ___anonymous15_26 extends Object{\n" +
				"  Number awesome;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_15() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"(function() {" +
				"  foo = {}" +
				"  foo.bar.awesome = 42;" +
				"})();",
				"X.js",
				"class @G extends Object{\n" +
				"}\n" +
				"class ___anonymous_foo extends Object{\n" +
				"  ___anonymous25_31 bar;\n" +
				"}\n" +
				"class ___anonymous25_31 extends Object{\n" +
				"  Number awesome;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_16() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"(function() {" +
				"  this.foo = {}" +
				"  foo.bar.awesome = 42;" +
				"})();",
				"X.js",
				"class @G extends Object{\n" +
				"  ___anonymous_foo foo;\n" +
				"}\n" +
				"class ___anonymous_foo extends Object{\n" +
				"  ___anonymous30_36 bar;\n" +
				"}\n" +
				"class ___anonymous30_36 extends Object{\n" +
				"  Number awesome;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_17() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"(function() {" +
				"  this.foo = {}" +
				"  this.foo.bar.awesome = 42;" +
				"})();",
				"X.js",
				"class @G extends Object{\n" +
				"  ___anonymous_foo foo;\n" +
				"}\n" +
				"class ___anonymous_foo extends Object{\n" +
				"  ___anonymous30_41 bar;\n" +
				"}\n" +
				"class ___anonymous30_41 extends Object{\n" +
				"  Number awesome;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_18() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"(function() {" +
				"  foo = {" +
				"    bar : {}" +
				"  }" +
				"  foo.bar.awesome = 42;" +
				"})();",
				"X.js",
				"class @G extends Object{\n" +
				"}\n" +
				"class ___anonymous_foo extends Object{\n" +
				"  ___anonymous32_33 bar;\n" +
				"}\n" +
				"class ___anonymous32_33 extends Object{\n" +
				"  Number awesome;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_19() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"(function() {" +
				"  this.foo = {" +
				"    bar : {}" +
				"  }" +
				"  foo.bar.awesome = 42;" +
				"})();",
				"X.js",
				"class @G extends Object{\n" +
				"  ___anonymous_foo foo;\n" +
				"}\n" +
				"class ___anonymous_foo extends Object{\n" +
				"  ___anonymous37_38 bar;\n" +
				"}\n" +
				"class ___anonymous37_38 extends Object{\n" +
				"  Number awesome;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_20() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"(function() {" +
				"  this.foo = {" +
				"    bar : {}" +
				"  }" +
				"  this.foo.bar.awesome = 42;" +
				"})();",
				"X.js",
				"class @G extends Object{\n" +
				"  ___anonymous_foo foo;\n" +
				"}\n" +
				"class ___anonymous_foo extends Object{\n" +
				"  ___anonymous37_38 bar;\n" +
				"}\n" +
				"class ___anonymous37_38 extends Object{\n" +
				"  Number awesome;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
	
	public void testAssigningToFieldsOnGlobals_21() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"foo.bar['awesome'] = 42;",
				"X.js",
				"class ___anonymous0_6 extends Object{\n" +
				"  Number awesome;\n" +
				"}\n",
				getDefaultOptions()
			);
	}
		
	public void testAssigningToReferenceToType_0() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"function Foo() {" +
				"	this.test0 = \"\";" +
				"}" +
				"Foo.prototype.test1 = 53;" +
				"Foo.test2 = \"test\";" +
				"var ReferenceToFoo = Foo;" +
				"ReferenceToFoo.addedToReference0 = \"test\";" +
				"ReferenceToFoo.prototype.addedToReference1 = \"test\";",
				"X.js",
				
				"class Foo extends Object{\n" +
				"  String test0;\n" +
				"  Number test1;\n" +
				"  static String test2;\n" +
				"  static String addedToReference0;\n" +
				"  String addedToReference1;\n" +
				"  Foo()\n" +
				"}\n",
				this.getDefaultOptions()
		);
	}
	
	public void testAssigningToReferenceToType_1() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var Foo = function() {" +
				"	this.test0 = \"\";" +
				"}" +
				"Foo.prototype.test1 = 53;" +
				"Foo.test2 = \"test\";" +
				"var ReferenceToFoo = Foo;" +
				"ReferenceToFoo.addedToReference0 = \"test\";" +
				"ReferenceToFoo.prototype.addedToReference1 = \"test\";",
				"X.js",
				
				"class Foo extends Object{\n" +
				"  String test0;\n" +
				"  Number test1;\n" +
				"  static String test2;\n" +
				"  static String addedToReference0;\n" +
				"  String addedToReference1;\n" +
				"  Foo()\n" +
				"}\n",
				this.getDefaultOptions()
		);
	}
	
	public void testAssigningToReferenceToType_2() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"Foo = function() {" +
				"	this.test0 = \"\";" +
				"}" +
				"Foo.prototype.test1 = 53;" +
				"Foo.test2 = \"test\";" +
				"var ReferenceToFoo = Foo;" +
				"ReferenceToFoo.addedToReference0 = \"test\";" +
				"ReferenceToFoo.prototype.addedToReference1 = \"test\";",
				"X.js",
				
				"class Foo extends Object{\n" +
				"  String test0;\n" +
				"  Number test1;\n" +
				"  static String test2;\n" +
				"  static String addedToReference0;\n" +
				"  String addedToReference1;\n" +
				"  Foo()\n" +
				"}\n",
				this.getDefaultOptions()
		);
	}
	
	public void testAssigningToReferenceToType_3() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"function Foo() {" +
				"	this.test0 = \"\";" +
				"}" +
				"Foo.prototype.test1 = 53;" +
				"Foo.test2 = \"test\";" +
				"ReferenceToFoo = Foo;" +
				"ReferenceToFoo.addedToReference0 = \"test\";" +
				"ReferenceToFoo.prototype.addedToReference1 = \"test\";",
				"X.js",
				
				"class Foo extends Object{\n" +
				"  String test0;\n" +
				"  Number test1;\n" +
				"  static String test2;\n" +
				"  static String addedToReference0;\n" +
				"  String addedToReference1;\n" +
				"  Foo()\n" +
				"}\n",
				this.getDefaultOptions()
		);
	}
	
	public void testAssigningToReferenceToType_4() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var Foo = function() {" +
				"	this.test0 = \"\";" +
				"}" +
				"Foo.prototype.test1 = 53;" +
				"Foo.test2 = \"test\";" +
				"ReferenceToFoo = Foo;" +
				"ReferenceToFoo.addedToReference0 = \"test\";" +
				"ReferenceToFoo.prototype.addedToReference1 = \"test\";",
				"X.js",
				
				"class Foo extends Object{\n" +
				"  String test0;\n" +
				"  Number test1;\n" +
				"  static String test2;\n" +
				"  static String addedToReference0;\n" +
				"  String addedToReference1;\n" +
				"  Foo()\n" +
				"}\n",
				this.getDefaultOptions()
		);
	}
	
	public void testAssigningToReferenceToType_5() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"Foo = function() {" +
				"	this.test0 = \"\";" +
				"}" +
				"Foo.prototype.test1 = 53;" +
				"Foo.test2 = \"test\";" +
				"ReferenceToFoo = Foo;" +
				"ReferenceToFoo.addedToReference0 = \"test\";" +
				"ReferenceToFoo.prototype.addedToReference1 = \"test\";",
				"X.js",
				
				"class Foo extends Object{\n" +
				"  String test0;\n" +
				"  Number test1;\n" +
				"  static String test2;\n" +
				"  static String addedToReference0;\n" +
				"  String addedToReference1;\n" +
				"  Foo()\n" +
				"}\n",
				this.getDefaultOptions()
		);
	}
	
	/**
	 * <p>
	 * Nothing should be created in this case because the type of 'foo' is unknown so the type of
	 * its field 'bar' is also unknown so there is no reason to create or assign a type to 'b'.
	 * </p>
	 * 
	 * <p>
	 * <b>NOTE:</b> At build time 'b' may end up with a type if it can resolve 'foo' and its field
	 * 'bar' at that time.
	 * </p>
	 */
	public void test_FieldOnGlobalAssignedToGlobal() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var b = foo.bar;",
				"X.js",
				
				"",
				this.getDefaultOptions()
		);
	}
	
	/**
	 * <p>
	 * A global field for 'foo' and a type for it should NOT be created in this case.
	 * </p>
	 */
	public void test_FieldOnGlobalAssignedToFieldOnAnotherGlobal() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"b.bar = foo.bar;",
				"X.js",
				
				"class ___anonymous_b extends Object{\n" +
				"  ?? bar;\n" +
				"}\n",
				this.getDefaultOptions()
		);
	}
	
	public void test_TwoDifferentAssignmentsToSameGlobal() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var global0 = \"Test\";" +
				"global0 = {" +
				"	feild0 : 0" +
				"};",
				"X.js",
				
				"class ___anonymous31_43 extends Object{\n" +
				"  Number feild0;\n" +
				"}\n" +
				"class ___anonymous_global0 extends Object{\n" +
				"  Number feild0;\n" +
				"}\n",
				this.getDefaultOptions()
		);
	}
	
	public void test_AssignFieldToGlobalVariable() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var foo = new Object()" + 
				"foo.bar = {};",
				"X.js",
				
				"class ___anonymous_foo extends Object{\n" +
				"  ___anonymous_bar bar;\n" +
				"}\n" +
				"class ___anonymous_bar extends Object{\n" +
				"}\n",
				this.getDefaultOptions()
		);
	}
	
	public void test_AssignFieldToFieldOnGlobalVariable() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"var foo = new Object()" +
				"foo.bar.awesome = 42;",
				"X.js",
				
				"class ___anonymous_foo extends Object{\n" +
				"  ___anonymous22_28 bar;\n" +
				"}\n" +
				"class ___anonymous22_28 extends Object{\n" +
				"  Number awesome;\n" +
				"}\n",
				this.getDefaultOptions()
		);
	}
	
	public void test_TwoConstructorFunctions() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"function Mixer1() {" +
				"	this.mixAtt1 = 3;" +
				"	this.mixMeth1 = function() {" +
				"	};" +
				"}" +
				"function Mixer2() {" +
				"	this.mixAtt2 = true;" +
				"	this.mixMeth2 = function(a) {" +
				"		return true;" +
				"	};" +
				"}",
				"X.js",
				
				"class Mixer1 extends Object{\n" +
				"  Number mixAtt1;\n" +
				"  void mixMeth1()\n" +
				"  Mixer1()\n" +
				"}\n" +
				"class Mixer2 extends Object{\n" +
				"  Boolean mixAtt2;\n" +
				"  Boolean mixMeth2(a)\n" +
				"  Mixer2()\n" +
				"}\n",
				this.getDefaultOptions()
		);
	}
	
	public void test_LocalTypeShouldNotGetGlobalTypeName() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"(function(){\n" +
				"	var ta = {};\n" +
				"	ta.UnderlineAnnotation = function(foo){\n" +
				"		this.transform={dx:0,dy:0};\n" +
				"	};\n" +
				"	ta.UnderlineAnnotation.prototype = new ta.Annotation;\n" +
				"	var p = ta.UnderlineAnnotation.prototype;\n" +
				"	p.constructor=ta.UnderlineAnnotation;\n" +
				"})();",
				"X.js",
				
				"class @G extends Object{\n" +
				"}\n" +
				"class ___anonymous23_24 extends Object{\n" +
				"  ___anonymous85_95 transform;\n" +
				"  void UnderlineAnnotation(foo)\n" +
				"}\n" +
				"class ta.UnderlineAnnotation extends ta.Annotation{\n" +
				"  ___anonymous85_95 transform;\n" +
				"  ta.UnderlineAnnotation(foo)\n" +
				"}\n" +
				"class ___anonymous85_95 extends Object{\n" +
				"  Number dx;\n" +
				"  Number dy;\n" +
				"}\n" +
				"class ___anonymous201_201 extends Object{\n" +
				"  void constructor(foo)\n" +
				"}\n",
				this.getDefaultOptions()
		);
	} 
	
	public void testClosureArgumentIsGlobalWithAddedProperty() {
		CompilationUnitDeclaration declaration = this.runInferTest(
					"(function(window, undefined){\n" +
					"	var t = {};\n" +
					"	window.jChris = t;\n" +
					"})(window);\n" +
					"window.ta = 4;\n",
					"X.js",
					"class ___anonymous_window extends Object{\n"+
					"  ___anonymous_jChris jChris;\n"+
					"  Number ta;\n"+
					"}\n"+
					"class @G extends Object{\n" +
					"}\n"+
					"class ___anonymous_jChris extends Object{\n"+
					"}\n",
					this.getDefaultOptions()
		);
	}
	public void testClosureArgumentNamedWindowButLocalDeclarationPassedIn() {
		CompilationUnitDeclaration declaration = this.runInferTest(
					"var s = 5;\n"+
					"(function(window, undefined){\n" +
					"	var t = {};\n" +
					"	window.jChris = t;\n" +
					"})(s);\n",
					"X.js",
					"class @G extends Object{\n"+
					"}\n"+
					"class ___anonymous50_51 extends Object{\n"+
					"}\n" +
					"class ___anonymous55_60 extends Number{\n"+
					"  ___anonymous50_51 jChris;\n"+
					"}\n",
					this.getDefaultOptions()
		);
	}
	public void testClosureArgumentIsLocalDeclaration() {
		CompilationUnitDeclaration declaration = this.runInferTest(
					"var s = 5;\n"+
					"(function(x, undefined){\n" +
					"	var t = {};\n" +
					"	x.jChris = t;\n" +
					"})(s);\n",
					"X.js",
					"class @G extends Object{\n"+
					"}\n"+
					"class ___anonymous45_46 extends Object{\n"+
					"}\n" +
					"class ___anonymous50_50 extends Number{\n"+
					"  ___anonymous45_46 jChris;\n"+
					"}\n",
					this.getDefaultOptions()
		);
	}
	public void testClosureArgumentIsAssignment() {
		CompilationUnitDeclaration declaration = this.runInferTest(
					"s = 5;\n"+
					"(function(x, undefined){\n" +
					"	var t = {};\n" +
					"	x.jChris = t;\n" +
					"})(s);\n",
					"X.js",
					"class @G extends Object{\n"+
					"}\n"+
					"class ___anonymous41_42 extends Object{\n"+
					"}\n" +
					"class ___anonymous46_46 extends Number{\n"+
					"  ___anonymous41_42 jChris;\n"+
					"}\n",
					this.getDefaultOptions()
		);
	}
	public void testClosureArgumentIsGlobal() {
		CompilationUnitDeclaration declaration = this.runInferTest(
					"(function(window, undefined){\n" +
					"	var t = {};\n" +
					"	window.jChris = t;\n" +
					"})(window);\n",
					"X.js",
					"class ___anonymous_window extends Object{\n"+
					"  ___anonymous_jChris jChris;\n"+
					"}\n"+
					"class @G extends Object{\n" +
					"}\n"+
					"class ___anonymous_jChris extends Object{\n"+
					"}\n",
					this.getDefaultOptions()
		);
	}
	//WI97682
	public void testClosureArgumentIsGlobalType() {
		CompilationUnitDeclaration declaration = this.runInferTest(
					"(function(window, undefined){\n" +
					"	var jChris = function(){};\n" +
					"	jChris.fn = jChris.prototype = {hop: 1};\n" +
					"   window.jChris = jChris;\n" +
					"})(window);\n",
					"X.js",
					"class ___anonymous_window extends Object{\n"+
					"  ___anonymous_jChris jChris;\n"+
					"  void jChris()\n" +
					"}\n"+
					"class @G extends Object{\n" +
					"}\n"+
					"class ___anonymous_jChris extends Function{\n"+
					"  ___anonymous90_97 fn;\n" +
					"}\n" +
					"class ___anonymous90_97 extends Object{\n"+
					"  Number hop;\n" +
					"}\n" +
					"class jChris extends Object{\n"+
					"  static ___anonymous90_97 fn;\n" +
					"  jChris()\n" +
					"}\n",
					this.getDefaultOptions()
		);
	}
	
	public void testWI97616() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"/** @memberOf Number */\n" +
			"var hiliteSearchTerm = \"hi\";",
			"X.js",
			"class Number extends Object{\n" +
			"  String hiliteSearchTerm;\n" +
			"}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void testWI97616_2() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"/** @memberOf Number */\n" +
			"var hiliteSearchTerm = (function() {\n" +
			"return \"hi\";})();",
			"X.js",
			"class Number extends Object{\n" +
			"  String hiliteSearchTerm;\n" +
			"}\n",
			getDefaultOptions()
			
		 );
	}
	
	//WI97682
	public void testClosureReturnsFunction() {
		CompilationUnitDeclaration declaration = this.runInferTest(
					"(function( window) {\n" +
					"	var jChris = (function() {\n" +
					"	var jChris = function( selector, context ) {return new jChris.fn.init();};\n" +
					"	jChris.fn = jChris.prototype = {};\n" +
					"	jChris.fn.init.prototype = jChris.fn;\n" +
					"	return jChris;})();" +
					"	jChris.extend({\n" +
					"	data: function() {var internalKey = jChris.expando;}\n" +
					"	});\n" +
					"	window.jChris = jChris;})( window );",
					"X.js",
					"class ___anonymous_window extends Object{\n"+
					"  ___anonymous_jChris jChris;\n"+
					"  jChris.fn.init jChris(selector, context)\n" +
					"}\n"+
					"class @G extends Object{\n" +
					"}\n"+
					"class ___anonymous105_113 extends Object{\n" +
					"}\n"+
					"class jChris.fn.init extends ___anonymous157_158{\n" +
					"}\n" +
					"class ___anonymous_jChris extends Function{\n" +
					"  ___anonymous157_158 fn;\n" +
					"  ___anonymous157_158 prototype;\n" +
					"}\n" +
					"class ___anonymous157_158 extends Object{\n" +
					"}\n" +
					"class ___anonymous235_292 extends Object{\n" +
					"  void data()\n" +
					"}\n",
					this.getDefaultOptions()
		);
	}
	
	public void testWI98919() {
		CompilationUnitDeclaration declaration = this.runInferTest(
					"(function( window) {\n" +
					"	var jChris = (function() {\n" +
					"	var jChris = function( selector, context ) {return new jChris.fn.init();};\n" +
					"	jChris.fn = jChris.prototype = {init: function() {}, ready: function(fn) {}};\n" +
					"	jChris.fn.init.prototype = jChris.fn;\n" +
					"	return jChris;})();" +
					"	window.jChris = jChris;})( window );",
					"X.js",
					"class ___anonymous_window extends Object{\n"+
					"  ___anonymous_jChris jChris;\n"+
					"  jChris.fn.init jChris(selector, context)\n" +
					"}\n"+
					"class @G extends Object{\n" +
					"}\n"+
					"class ___anonymous105_113 extends Object{\n" +
					"}\n"+
					"class jChris.fn.init extends ___anonymous157_201{\n" +
					"}\n" +
					"class ___anonymous_jChris extends Function{\n" +
					"  ___anonymous157_201 fn;\n" +
					"  ___anonymous157_201 prototype;\n" +
					"}\n" +
					"class ___anonymous157_201 extends Object{\n" +
					"  void init()\n" +
					"  void ready(fn)\n" +
					"}\n",
			getDefaultOptions()
			
		 );
	}
	
	public void testWI99473() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"foo.prototype.bar = /**\n" +
			" */\n" +
			"function (e) {\n" +
			"  this.baz = true;\n" +
			"  return true;\n" +
			"}",
			"X.js",
			"class foo extends Object{\n"+
			"  Boolean baz;\n"+
			"  Boolean bar(e)\n"+
			"}\n",
			getDefaultOptions()
		 );
		assertFalse("There shouldn't be a type named 'foo.prototype.bar'", declaration.findInferredType("foo.prototype.bar".toCharArray()) != null);
	}
	public void testAssignAsThisInClosure() {
		// add properties to "this" indirectly in a closure
		CompilationUnitDeclaration declaration = this.runInferTest(
			"(function(\n"+
			" userConfig,\n"+
			" defaultConfig\n"+
			")\n" +
			"{\n" +
			"var noop = function(){\n" +
			"},\n"+
			"global=this;\n" +
			"var def = function(\n"+
			"mid,		  //(commonjs.moduleId, optional) list of modules to be loaded before running factory\n"+
			"dependencies, //(array of commonjs.moduleId, optional)\n"+
			"factory		  //(any)\n"+
			"){};\n"+
			"  global.define = def;\n" +
			"}){}",
			"X.js",
			"class @G extends Object{\n  Function define;\n}\n",
			getDefaultOptions()
		 );
		assertNull("There should not be a global function def()", declaration.findInferredType("@G".toCharArray()).findMethod("def".toCharArray(), null));
	}
	public void testDeclareAsThisInClosure() {
		// add properties to "this" indirectly in a closure
		CompilationUnitDeclaration declaration = this.runInferTest(
			"(function(\n"+
			" userConfig,\n"+
			" defaultConfig\n"+
			")\n" +
			"{\n" +
			"var noop = function(){\n" +
			"},\n"+
			"global=this;\n" +
			"var def = function(\n"+
			"mid,		  //(commonjs.moduleId, optional) list of modules to be loaded before running factory\n"+
			"dependencies, //(array of commonjs.moduleId, optional)\n"+
			"factory		  //(any)\n"+
			"){};\n"+
			"  global.define = def;\n" +
			"}){}",
			"X.js",
			"class @G extends Object{\n  Function define;\n}\n",
			getDefaultOptions()
		 );
		assertNull("There should not be a global function def()", declaration.findInferredType("@G".toCharArray()).findMethod("def".toCharArray(), null));
	}
}
