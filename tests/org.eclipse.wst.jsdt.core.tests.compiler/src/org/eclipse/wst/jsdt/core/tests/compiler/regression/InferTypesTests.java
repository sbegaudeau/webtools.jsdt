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
			"class MyClass extends Object{\n  String url;\n  MyClass()\n  void activate()\n}\n",
			getDefaultOptions()
			
		 );
	}
	
	
	public void test002() {
		CompilationUnitDeclaration declaration = this.runInferTest(
				"Shape.prototype.GetArea = Shape_GetArea;"+ 
				"function Shape(){}"+
				"function Shape_GetArea()"+
				"{"+
				" this.area=5;"+ 
				"return this.area;"+ 
				"}",
			"X.js",
			"class Shape extends Object{\n  Number area;\n  Number GetArea()\n  Shape()\n}\n",
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
			"class Circle extends Shape{\n  void GetArea()\n}\n",
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
				"class ___anonymous9_10 extends Object{\n  void foo()\n}\n"+
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
				"class ___anonymous18_19 extends Object{\n  void foo()\n}\n"+
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
				"class x extends Object{\n  static String foo;\n  static String bar()\n}\n",
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
		

		
		//		/*
//		 * Dynamic extend built-in through prototype
//		 */
//		public void test077() {
//			CompilationUnitDeclaration declaration = this.runInferTest(
//				"Object.prototype.foo = \"\";" +
//				"Object.prototype.bar = function(){" +
//				"  return \"\";" +
//				"}",
//				"X.js",
//				"",
//				getDefaultOptions()
//				
//			 );
//		}
}
