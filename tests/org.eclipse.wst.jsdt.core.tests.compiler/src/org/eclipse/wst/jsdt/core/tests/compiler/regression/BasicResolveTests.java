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
				"The function abc() is undefined\n"+ 
				"----------\n"
		);

		this.runNegativeTest(
				new String[] {
						"X.js",
						"	function foo(a){\n" +
						"	  foo(a); \n" +
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
						"var win=debugger;\n" +
						"Object();\n" +
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
						"Object();\n" +
						"\n"
				},
				""
		);


	}


//	With inferred types

	public void test010()	{	// field reference 
		this.runNegativeTest(
				new String[] {
						"X.js",
						"MyClass.prototype.someMethod = MyClass_someMethod;"+ 
						"function MyClass(){}"+
						"function MyClass_someMethod(){}"+
						"var myClassObj = new MyClass();\n"+
						"myClassObj.someMethod();\n"
				},
				""
		);


	}

	public void test011()	{	// field reference 
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


	public void test012()	{	// field reference 
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function Bob(firstname, lastname) {\n" +
						"   this.Firstname = firstname;\n" +
						"   this.Lastname = lastname;\n" +
						"}\n" +
						"Bob.prototype.name = function () {return this.Firstname + this.Lastname;};\n",
				},
				""
		);


	}

	public void test013()	{
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

	public void test014()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var o = {x:1, y: 2, name: \"print\" };\n" +
						"o.Z = 0; \n"
				},
				""
		);

	}



	public void test020()	{
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
	public void test021()	{
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

	public void test022()	{
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

	public void test022b()	{
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


	public void test023()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"	var ff=new String();\n" +
						"" 
				},
				""
		);

	}		

	public void test024()	{
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

	public void test025()	{
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

	public void test026()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var i=[10];\n" +
						"" 
				},
				""
		);

	}


	public void test027()	{
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

	public void test028()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var c=false;\n" +
						"var d=!c;\n" +
						"" 
				},
				""
		);

	}

	public void test029()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var d=new Date(1);\n" +
						"" 
				},
				""
		);

	}


	public void test030()	{
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
	public void test031()	{
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

	public void test032()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var foo;\n" +
						" foo.onMouseDown = function () { return 1; };\n" +
						" foo.onMouseDown();\n" +
						"" 
				},
				""
		);

	}

	public void test032b()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var foo;\n" +
						" foo.level1=new Object();\n" +
						" foo.level1.onMouseDown = function () { return 1; };\n" +
						" foo.level1.onMouseDown();\n" +
						"" 
				},
				""
		);

	}


	public void test033()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						" if (typeof abc == \"undefined\") {}\n" +
						"" 
				},
				""
		);

	}

	public void test034()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						" if (true) {\n" +
						"   var abc=1;}\n" +
						" var d=abc;\n" +
						"" 
				},
				""
		);

	}

	public void test035()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						" function foo() {\n" +
						"   var vv=arguments;}\n" +
						"" 
				},
				""
		);

	}


	public void test036()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						" function foo() {\n" +
						"   function inner(){}\n" +
						"   inner();\n" +
						"   }\n" +
						"" 
				},
				""
		);

	}


	public void test037()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						" var s = new String();\n" +
						" var sub=s.substring(0,0);\n" +
						" var i=sub.length;\n" +
						"" 
				},
				""
		);

	}

	public void test038()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						" s = new String();\n" +
						" sub=s.substring(0,0);\n" +
						" i=sub.length;\n" +
						"" 
				},
				""
		);

	}


	public void test039()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						" var s = \"\";\n" +
						" with (s) {\n" +
						"   var i=length;\n" +
						" }\n" +
						"" 
				},
				""
		);

	}

	public void test040()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						" var s = \"\";\n" +
						" with (s) {\n" +
						"   var i=charAt(0);\n" +
						" }\n" +
						"" 
				},
				""
		);

	}

	/*
	 * Field reference error testing
	 */
	public void test041()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var x = {};\n" +
						"x.a = \"\""
				},
				""
		);

	}

	public void test042()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var x = {};\n" +
						"x.a.b = \"\""
				},
				"----------\n" +
				"1. WARNING in X.js (at line 2)\n" +
				"	x.a.b = \"\"\n"+ 
				"	  ^\n"+
				"a cannot be resolved or is not a field\n"+ 
				"----------\n"
		);
	}

	public void test043()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var x = null;\n" +
						"x>3;"+
						"var y;\n" +
						"y=null;\n" +
						"y>3"+
						""
				},
				""
		);

	}

	public void test044()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						" if (typeof abc == \"undefined\") { abc=1;}\n" +
						" var c= abc;\n" +
						"" 
				},
				""
		);

	}

	public void test045()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						" var a,b=1;\n" +
						" var c= b;\n" +
						" function abc(){" +
						" var d= 1,e=4;\n" +
						" var f=e;}\n" +
						"" 
				},
				""
		);

	}

	
	public void test046()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function debug2() {\n" +
						" var keyFunct = null;\n" +
						" keyFunct = function () {};\n" +
						"  keyFunct();\n" +
						"  }\n" +
						"" 
				},
				""
		);

	}

	
	public void test046b()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function debug2() {\n" +
						" var keyFunct = null;\n" +
						" keyFunct = new function () {};\n" +
						"  keyFunct();\n" +
						"  }\n" +
						"" 
				},
				""
		);

	}
	

	public void test047()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function Config() {}\n" +
						"Config.printDocTypes = function() { throw new (\"doctype 1.\"); };\n" +
						"Config.prototype.toString = function () { return \"\"; };\n" +
						"function main() {\n" +
						"    Config.printDocTypes();\n" +
						"}   \n" +
						"" 
				},
				""
		);
	}


	public void test048()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function Config2() {};\n" +
						"Config2.INPUT_DIR = \"\";\n" +
						"Config2.OUTPUT_DIR = Config2.INPUT_DIR  ;\n" +
						"Config2.getNum = function() { return 1; }\n" +
						"function numberGen() { \n" +
						"    return Config2.getNum(); \n" +
						"}   \n" +
						"" 
				},
				""
		);
	}

	public void test049()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var arr=[];\n" +
						"var ref=arr.length;\n" +
						"var o=arr.pop();\n" +
						"" 
				},
				""
		);
	}


	public void test050()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function getDateTime() {\n" +
						"	    this.ctime = new Object();\n" +
						"	    this.ctime.getDay = new function() { return \"Mon\"; };\n" +
						"	    return this;\n" +
						"	}\n" +
						"	function debug3() {\n" +
						"	    var newObj = getDateTime();\n" +
						"	    return newObj.ctime.getDay();\n" +
						"	}\n" +
						"" 
				},
				""
		);
	}

	public void test050b()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function getDateTime() {\n" +
						"	    this.ctime = new Object();\n" +
						"	    this.ctime.getDay = new function() { return \"Mon\"; };\n" +
						"	    return this;\n" +
						"	}\n" +
						"	function debug3() {\n" +
						"	    var newObj = new getDateTime();\n" +
						"	    return newObj.ctime.getDay();\n" +
						"	}\n" +
						"" 
				},
				""
		);
	}

	   
	public void test051()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var ns = {};\n"+ 
						"ns.foo = function(){\n" +
						"};\n" +
						"ns.foo.prototype.bar = \"\";\n" +
						"ns.foo.prototype.bar2 = function(){\n" +
						"  return \"\";\n" +
						"}\n"+
						"c=new ns.foo();\n"+
						"c.bar2();\n"+
						"i=c.bar;\n"+
						"" 
				},
				""
		);
	}


	public void test052()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var myObject=new Object();\n"+ 
						"myObject.ctor=   function(){\n" +
						"};\n" +
						"myObject.ctor.prototype.bar = \"\";\n" +
						"myObject.ctor.prototype.bar2 = function(){\n" +
						"  return \"\";\n" +
						"}\n"+
						"c=new myObject.ctor();\n"+
						"c.bar2();\n"+
						"i=c.bar;\n"+
						"" 
				},
				""
		);
	}

	

	public void test053()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function funccall(pp){}\n"+ 
						"funccall({\n"+ 
						"meth : function(){  \n" +
						"  var c=this.i; },\n" +
						" i : 1 \n" +
						"});\n"+
						"" 
				},
				""
		);
	}


	public void test054()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function func1(pp){}\n"+ 
						"func1();\n"+ 
						"function obj(){}\n"+ 
						"var o=new obj(1);\n"+ 
						"" 
				},
				"----------\n" + 
				"1. WARNING in X.js (at line 2)\n" + 
				"	func1();\n" + 
				"	^^^^^^^\n" + 
				"Wrong number of arguments for the function func1 (), expecting 1 argument(s), but there was 0 \n" + 
				"----------\n" + 
				"2. WARNING in X.js (at line 4)\n" + 
				"	var o=new obj(1);\n" + 
				"	      ^^^^^^^^^^\n" + 
				"Wrong number of arguments for the function obj (), expecting 0 argument(s), but there was 1 \n" + 
				"----------\n"
		);
	}

	
	public void test055()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function func1(base,mixin){\n"+ 
						"var bp = (base||0).prototype;\n"+ 
						"}\n"+ 
						"" 
				},
				""
		);
	}
		
	
	public void test056()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var arr=[];\n"+ 
						"arr.push(1);\n"+ 
						"" 
				},
				""
		);
	}
	
	public void test057()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var arr= {\n"+ 
						"  func: function(){ \n"+ 
						"       this.func();\n"+ 
						"   } \n"+ 
						"};\n"+ 
						"" 
				},
				""
		);
	}
}