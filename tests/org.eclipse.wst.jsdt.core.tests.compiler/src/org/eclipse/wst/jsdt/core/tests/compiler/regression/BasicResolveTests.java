package org.eclipse.wst.jsdt.core.tests.compiler.regression;


public class BasicResolveTests extends AbstractRegressionTest {

	public BasicResolveTests(String name) {
		super(name);
 
	}
	public void test002()	{	// local method 
		this.runNegativeTest(
			new String[] {
				"X.js",
				"	function foo(){\n" +
				"	  abc(); \n" +
				"}\n"
			},
			"----------\n" +
			"1. ERROR in X.js (at line 2)\n" +
			"	abc(); \n"+ 
			"	^^^\n"+
			"The function abc() is undefined for the type \n"+ 
			"----------\n"
		);

		this.runNegativeTest(
				new String[] {
					"X.js",
					"	function foo(){\n" +
					"	  foo(); \n" +
					"}\n"
				},
				""
			);

	}
	
	public void test003()	{	// local var 
		this.runNegativeTest(
				new String[] {
					"X.js",
					"var i;" +
					"i=1;\n" +
					"\n"
				},
				""
			);
		
		this.runNegativeTest(
				new String[] {
					"X.js",
					"var i;\n" +
					"i=j;\n" 
				},
					"----------\n" +
					"1. ERROR in X.js (at line 2)\n" +
					"	i=j;\n"+ 
					"	  ^\n"+
					"j cannot be resolved\n"+ 
					"----------\n"
			);
		

	}
	
	public void test004()	{	// system reference 
		this.runNegativeTest(
				new String[] {
					"X.js",
					"var win=window;\n" +
					"alert();\n" +
					"\n"
				},
				""
			);
		
		this.runNegativeTest(
				new String[] {
					"X.js",
					"var i;\n" +
					"i=j;\n" 
				},
					"----------\n" +
					"1. ERROR in X.js (at line 2)\n" +
					"	i=j;\n"+ 
					"	  ^\n"+
					"j cannot be resolved\n"+ 
					"----------\n"
			);
		

	}

	public void test005()	{	// system reference 
		this.runNegativeTest(
				new String[] {
					"X.js",
					"alert();\n" +
					"\n"
				},
				""
			);
		

	}
	
//	
//	With inferred types
	
	public void test0010()	{	// field reference 
		this.runNegativeTest(
				new String[] {
					"X.js",
					"MyClass.prototype.someMethod = MyClass_someMethod;"+ 
					"function MyClass(){}"+
					"function MyClass_someMethod(){}"+
					"var myClassObj = new MyClass();\n"+
					"myClassObj.someMethod();\n",
					"\n"
				},
				""
			);
		

	}
	
	public void test0011()	{	// field reference 
		this.runNegativeTest(
				new String[] {
					"X.js",
					"function MyClass() {\n"+
					"  this.url = \"\";\n"+
					"  this.activate = function(){}\n"+
					"}\n"+
					"var myClassObj = new MyClass();\n"+
					"var u=myClassObj.url;\n"+
					"\n"
				},
				""
			);
		

	}
	
	
	public void test0012()	{	// field reference 
		this.runNegativeTest(
				new String[] {
					"X.js",
				      "function Bob(firstname, lastname) {\n" +
				      "   var Firstname = firstname;\n" +
				      "   var Lastname = lastname;\n" +
				      "}\n" +
				      "Bob.prototype.name = function () {return this.Fistname + this.Lastname;};\n",
				},
				""
			);
		

	}
	
	public void test0013()	{
		this.runNegativeTest(
				new String[] {
					"X.js",
					"var SingleQuote = {\n" +
					"   Version: '1.1-beta2' \n" +
					"}\n"
				},
				""
			);

	}
	
	public void test0014()	{
		this.runNegativeTest(
				new String[] {
					"X.js",
					"var o = {x:1, y: 2, name: \"print\" };\n" +
					"o.Z = 0; \n"
				},
				""
			);

	}
	
	
	 
	public void test0020()	{
		this.runNegativeTest(
				new String[] {
					"X.js",
					"function foo() {\n" +
					"		var t = new Test();\n" +
					"}\n" +
					"   function Test()\n" +
					"{\n" +
					"}\n"
				},
				""
			);

	}
	public void test0021()	{
		this.runNegativeTest(
				new String[] {
					"X.js",
					"function foo() {\n" +
					"	var i=1;\n" +
					"	if (2>i )\n" +
					"		foo();\n" +
					"}\n" 
				},
				""
			);

	}
	
	public void test0022()	{
		this.runNegativeTest(
				new String[] {
					"X.js",
					"function foo() {\n" +
					"	var ff=function(p) \n" +
					"	{var c=p;};\n" +
					"	ff(1);\n" +
					"}\n" 
				},
				""
			);

	}

	public void test0022b()	{
		this.runNegativeTest(
				new String[] {
					"X.js",
					"	var ff=function(p) \n" +
					"	{var c=p;};\n" +
					"	ff(1);\n" +
					"" 
				},
				""
			);

	}
 
		
		public void test0023()	{
			this.runNegativeTest(
					new String[] {
						"X.js",
						"	var ff=new String();\n" +
						"" 
					},
					""
				);

		}		
		
		public void test0024()	{
			this.runNegativeTest(
					new String[] {
						"X.js",
						" function bar() {;\n" +
						"return Test.x;\n" +
						"}\n" +
						"Test.prototype=new Object();\n" +
						"Test.x=1;\n" +
						"" 
					},
					""
				);

		}

		public void test0025()	{
			this.runNegativeTest(
					new String[] {
						"X.js",
						" function g() {\n" +
						"return null;\n" +
						"}\n" +
						"function foo() {\n" +
						"	g();\n" +
						"}\n" +
						"" 
					},
					""
				);

		}

		public void test0026()	{
			this.runNegativeTest(
					new String[] {
						"X.js",
						"var i=[10];\n" +
						"" 
					},
					""
				);

		}


		public void test0027()	{
			this.runNegativeTest(
					new String[] {
							"X.js",
							" function bar(vv) {;\n" +
							"return vv%4;\n" +
							"}\n" +
							"" 
					},
					""
				);

		}

		public void test0028()	{
			this.runNegativeTest(
					new String[] {
							"X.js",
							"var c;\n" +
							"var d=!c;\n" +
							"" 
					},
					""
				);

		}

		public void test0029()	{
			this.runNegativeTest(
					new String[] {
							"X.js",
							"var d=new Date();\n" +
							"" 
					},
					""
				);

		}

		
		public void test0030()	{
			this.runNegativeTest(
					new String[] {
							"X.js",
							"function foo(e) {\n" +
							"var x= 10, z = null, i, j;\n" +
							"}\n" +
							"" 
					},
					""
				);

		}
		public void test0031()	{
			this.runNegativeTest(
					new String[] {
							"X.js",
							"function OBJ(){}\n" +
							"var o = new OBJ();\n" +
							"" 
					},
					""
				);

		}
		
}