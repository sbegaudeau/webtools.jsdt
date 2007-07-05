package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.wst.jsdt.internal.infer.InferOptions;

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
	
	private InferOptions getDojoOptions()
	{
		InferOptions inferOptions=new InferOptions();
		inferOptions.useAssignments=true;
		inferOptions.useInitMethod=true;
		inferOptions.engineClass="org.eclipse.wst.jsdt.internal.infer.DojoInferEngine";
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
	public void test005() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			"dojo.provide(\"org.brcp.Bundle\");\n"+
			"org.brcp.Bundle = function(){\n"+
			"this.url = \"\";\n"+
			"this.activate = function(){}\n"+
			"}\n",
			"X.js",
			"class org.brcp.Bundle extends Object{\n  String url;\n  void activate()\n}\n",
			getDojoOptions()
			
		 );
	}
	*/
	
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
				"class ___foo0 extends Object{\n  Number onMouseDown()\n}\n",
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
				"class ___anonymous0 extends Object{\n  String bar;\n  void bar2()\n}\n",
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
				"class ___anonymous0 extends Object{\n  String bar;\n  void bar2()\n}\n",
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
				"class ___anonymous0 extends Object{\n  String bar;\n  ___anonymous1 bar2;\n}\n"+
				"class ___anonymous1 extends Object{\n  String bar3;\n}\n",
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
				"class ___anonymous0 extends Object{\n  String foo;\n  String bar()\n}\n",
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
				"class ___anonymous0 extends Object{\n  ___anonymous1 foo;\n}\n"+
				"class ___anonymous1 extends Object{\n  String bar;\n}\n",
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
				"class foo extends Object{\n  ___anonymous0 bar;\n  foo()\n}\n"+
				"class ___anonymous0 extends Object{\n  String bar2;\n}\n",
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
				"class ___anonymous0 extends Object{\n  ns.foo foo()\n}\n"+
				"class foo extends Object{\n  String bar;\n  String bar2();\n  foo()\n}\n",
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
				"class ___anonymous0 extends Object{\n  _anonymous1 n2;\n}\n"+
				"class ___anonymous1 extends Object{\n  ns1.ns2.foo foo()\n}\n"+
				"class foo extends Object{\n  String bar;\n  String bar2();\n  foo()\n}\n",
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
				"class ___anonymous0 extends Object{\n  String a;\n  String c;\n  String b()\n  ___anonymous1 d(x, y, z)\n}\n"+
				"class ___anonymous1 extends Object{\n  String x;\n  String y;\n  String z;\n}\n",
				getDefaultOptions()
				
			 );
		}
		
		public void test071() {
			CompilationUnitDeclaration declaration = this.runInferTest(
					"if( true ){" +
					"  var dojo = {};" +
					"}",
				"X.js",
				"class ___anonymous0 extends Object{\n}\n",
				getDefaultOptions()
				
			 );
		}
		
}
