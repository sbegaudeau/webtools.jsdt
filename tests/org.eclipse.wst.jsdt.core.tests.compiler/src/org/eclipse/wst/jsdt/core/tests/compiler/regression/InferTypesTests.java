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
	
	
	public void test010() {
		CompilationUnitDeclaration declaration = this.runInferTest(
			      "function Bob(firstname, lastname) {\n" +
			      "   var Firstname = firstname;\n" +
			      "   var Lastname = lastname;\n" +
			      "}\n" +
			      "Bob.prototype.name = function () {return this.Fistname + this.Lastname;};\n",
			"X.js",
			"class Bob extends Object{\n  String Firstname;\n  String Firstname;\n  Bob()\n}\n",
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

				+" var box= { \n"+
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
	
}
