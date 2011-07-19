/*******************************************************************************
 * Copyright (c) 2005, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import org.eclipse.wst.jsdt.core.infer.InferOptions;
import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;

public class InferTypesTests extends AbstractRegressionTest {

	public InferTypesTests(String name) {
		super(name);
 
	}
	
	private InferOptions getDefaultOptions()
	{
		InferOptions inferOptions=new InferOptions();
		inferOptions.setDefaultOptions();
		return inferOptions;
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
			"class MyClass extends Object{\n  String url;\n  void activate()\n  MyClass()\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	
	public void test002() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"Shape.prototype.GetArea = Shape_GetArea;"+ 
				"function Shape(){}"+
				"function Shape_GetArea()"+
				"{"+
				" var area = 5;"+ 
				"return area;"+ 
				"}",
			"X.js",
			"class Shape extends Object{\n  Number GetArea()\n  Shape()\n}\n",
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
			"class Circle extends Shape{\n  ?? constructor;\n  void GetArea()\n}\n",
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
					+ "Test.x=1;\n"
					+ "",
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
				"class ___foo0 extends Object{\n  static Number onMouseDown()\n}\n",
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
				"class ___anonymous10_44 extends Object{\n  String bar;\n  void bar2()\n}\n",
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
				"class ___anonymous14_48 extends Object{\n  String bar;\n  void bar2()\n}\n",
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
				"class ___anonymous10_52 extends Object{\n  String bar;\n  ___anonymous32_51 bar2;\n}\n"+
				"class ___anonymous32_51 extends Object{\n  String bar3;\n}\n",
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
				"class ___anonymous9_10 extends Object{\n  String foo;\n  String bar()\n}\n",
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
				"class ___anonymous9_10 extends Object{\n  ___anonymous21_31 foo;\n}\n"+
				"class ___anonymous21_31 extends Object{\n  String bar;\n}\n",
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
				"class ___anonymous9_10 extends Object{\n  ns.foo foo()\n}\n"+
				"class ns.foo extends Object{\n  String bar;\n  String bar2()\n  ns.foo()\n}\n",
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
				"class ___anonymous9_10 extends Object{\n  void foo()\n  void foo2()\n}\n",
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
				"class ___anonymous9_10 extends Object{\n  void foo()\n  void foo2()\n}\n",
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
				"class ___anonymous10_20 extends Object{\n  ___anonymous18_19 ns2;\n}\n"+
				"class ___anonymous18_19 extends Object{\n  ns1.ns2.foo foo()\n}\n"+
				"class ns1.ns2.foo extends Object{\n  String bar;\n  String bar2()\n  ns1.ns2.foo()\n}\n",
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
				"class ___anonymous10_52 extends Object{\n  String a;\n  String c;\n  String b()\n  ___anonymous101_126 d(x, y, z)\n}\n"+
				"class ___anonymous101_126 extends Object{\n  String x;\n  String y;\n  String z;\n}\n",
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
				"class ___anonymous23_24 extends Object{\n  String bar;\n}\n",
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
				"class Global extends Object{\n  Number someField;\n}\n",
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
				"s.area = function() {return this.length * this.width;};",
				"X.js",
				"class Shape extends Object{\n  ?? length;\n  ?? width;\n  Shape(l, w)\n}\n" +
				"class ___s0 extends Shape{\n  Number area()\n}\n",
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
				"class MyFunc extends Function{\n  static void myMeth()\n  MyFunc()\n}\n",
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
				"class foo.bar.Awesome extends Object{\n  String test;\n  void func()\n  foo.bar.Awesome()\n}\n",
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
				"class $ extends Object{\n  String jquery;\n  $()\n  static void hasData(element)\n}\n",
				getDefaultOptions()
			);
		}
}
