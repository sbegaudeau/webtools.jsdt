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
				},""
//				"----------\n" +
//				"1. ERROR in X.js (at line 2)\n" +
//				"	abc(); \n"+ 
//				"	^^^\n"+
//				"The function abc() is undefined\n"+ 
//				"----------\n"
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
				},""
//				"----------\n" +
//				"1. ERROR in X.js (at line 2)\n" +
//				"	i=j;\n"+ 
//				"	  ^\n"+
//				"j cannot be resolved\n"+ 
//				"----------\n"
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
				},""
//				"----------\n" +
//				"1. ERROR in X.js (at line 2)\n" +
//				"	i=j;\n"+ 
//				"	  ^\n"+
//				"j cannot be resolved\n"+ 
//				"----------\n"
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
				"----------\n" + 
				"1. WARNING in X.js (at line 2)\n" + 
				"	var t = new Test();\n" + 
				"	    ^\n" + 
				"The local variable t is never read\n" + 
				"----------\n"
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
				"----------\n" + 
				"1. WARNING in X.js (at line 3)\n" + 
				"	{var c=p;};\n" + 
				"	     ^\n" + 
				"The local variable c is never read\n" + 
				"----------\n"
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
				"----------\n" + 
				"1. WARNING in X.js (at line 2)\n" + 
				"	{var c=p;};\n" + 
				"	     ^\n" + 
				"The local variable c is never read\n" + 
				"----------\n"
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
						"var d=new Date(\"1\");\n" +
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
				"----------\n" + 
				"1. WARNING in X.js (at line 2)\n" + 
				"	var x= 10, z = null, i, j;\n" + 
				"	    ^\n" + 
				"The local variable x is never read\n" + 
				"----------\n" + 
				"2. WARNING in X.js (at line 2)\n" + 
				"	var x= 10, z = null, i, j;\n" + 
				"	           ^\n" + 
				"The local variable z is never read\n" + 
				"----------\n" + 
				"3. WARNING in X.js (at line 2)\n" + 
				"	var x= 10, z = null, i, j;\n" + 
				"	                     ^\n" + 
				"The local variable i is never read\n" + 
				"----------\n" + 
				"4. WARNING in X.js (at line 2)\n" + 
				"	var x= 10, z = null, i, j;\n" + 
				"	                        ^\n" + 
				"The local variable j is never read\n" + 
				"----------\n"
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
						"var foo = {};\n" +
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
						"var foo = {};\n" +
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
				"----------\n" + 
				"1. WARNING in X.js (at line 2)\n" + 
				"	var vv=arguments;}\n" + 
				"	    ^^\n" + 
				"The local variable vv is never read\n" + 
				"----------\n"
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
						"var b=x;\n;"+
						"b.a=\"\";\n;"+
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
				},""
//				"----------\n" +
//				"1. WARNING in X.js (at line 2)\n" +
//				"	x.a.b = \"\"\n"+ 
//				"	  ^\n"+
//				"a cannot be resolved or is not a field\n"+ 
//				"----------\n"
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
				"----------\n" + 
				"1. WARNING in X.js (at line 3)\n" + 
				"	function abc(){ var d= 1,e=4;\n" + 
				"	                    ^\n" + 
				"The local variable d is never read\n" + 
				"----------\n" + 
				"2. WARNING in X.js (at line 4)\n" + 
				"	var f=e;}\n" + 
				"	    ^\n" + 
				"The local variable f is never read\n" + 
				"----------\n"
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
				"----------\n" + 
				"1. WARNING in X.js (at line 1)\n" + 
				"	var arr=[];\n" + 
				"	        ^^\n" + 
				"Type mismatch: cannot convert from any[] to ___anonymous_arr\n" + 
				"----------\n"
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
				"----------\n" + 
				"1. WARNING in X.js (at line 4)\n" + 
				"	var c=this.i; },\n" + 
				"	    ^\n" + 
				"The local variable c is never read\n" + 
				"----------\n"
		);
	}
	
	/*public void testbug259187()	{	 

		this.runNegativeTest(
				new String[] {
						"X.js",
						"var params = \"some?string\".split('?');\n" +
				        "var base = params.shift();"
				},
				"----------\n" + 
		"1. WARNING in X.js (at line 1)\n" + 
		"	var params = \"some?string\".split(\'?\');\n" + 
		"	             ^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
		"Wrong number of arguments for the function split (), expecting 2 argument(s), but there was 1 \n" + 
		"----------\n"
		);
	}
	
	public void testbug259023()	{	 
		Map custom = new HashMap();
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.unusedLocal", "error");
		
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var myObject = {\n" +
							"val1: \"test1\",\n" +
							"val2: \"test2\"\n" +
						"};\n" +
						"myObject.val1 = \"test3\";"
				},
				"", null, true, custom
		);
	}
	
	public void testbug259023_2()	{	 
		Map custom = new HashMap();
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.unusedLocal", "error");
		
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var myObject = \"test3\";\n" +
						"if(myObject == \"test3\") {}"
				},
				"", null, true, custom
		);
	}
	
	public void testbug251374()	{	
		Map custom = new HashMap();
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.nullReference", "error");
		
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var a = null;\n" +
						"function foo() { a.toString();}\n" +
						"a = 1; foo();"
				},
				"", null, true, custom
		);
	}
	
	public void testChris()	{	 
		Map custom = new HashMap();
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.unusedLocal", "error");
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var square = function(x) {return x*x;};" +
						"square(2);",
				},
				"", null, true, custom
		);
	}

	public void testChris2()	{	 
		Map custom = new HashMap();
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.unusedLocal", "error");
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var square = \"chris\";" +
						"square.split(\".\", 1);",
				},
				"", null, true, custom
		);
	}*/

	public void test054()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function func1(pp){}\n"+ 
						"func1();\n"+ 
						"function obj(){}\n"+ 
						"var o=new obj(1);\n"+ 
						"" 
				},""
//				"----------\n" + 
//				"1. WARNING in X.js (at line 2)\n" + 
//				"	func1();\n" + 
//				"	^^^^^^^\n" + 
//				"Wrong number of arguments for the function func1 (), expecting 1 argument(s), but there was 0 \n" + 
//				"----------\n" + 
//				"2. WARNING in X.js (at line 4)\n" + 
//				"	var o=new obj(1);\n" + 
//				"	      ^^^^^^^^^^\n" + 
//				"Wrong number of arguments for the function obj (), expecting 0 argument(s), but there was 1 \n" + 
//				"----------\n"
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
				"----------\n" + 
				"1. WARNING in X.js (at line 2)\n" + 
				"	var bp = (base||0).prototype;\n" + 
				"	    ^^\n" + 
				"The local variable bp is never read\n" + 
				"----------\n"
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
	
	
	public void test058()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var cls= {};\n"+ 
						"cls.arr={};\n"+ 
						"cls.arr[\"ss\"]=1;\n"+ 
						"var dd=cls.arr[\"ssd\"];\n"+ 
						"" 
				},
				""
		);
	}
	
	
	public void test059()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						 "/**\n"
						+ " * Valid class javadoc\n"
						+ " * @param {String | Number} p1 param def\n"
						+ " */\n"
						+"function foo(p1){\n"  
						+"p1.length;\n" 
						+"p1.toPrecision(1);\n" 
						+"}\n" 
						+"" 
				},
				""
		);
	}
	
	
	public void test060()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function foo(p1){\n"  
						+"p1();\n" 
						+"}\n" 
						+"" 
				},
				""
		);
	}

	public void test061()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function foo(){\n"
						+"p1=1;\n" 
						+"p1();\n" 
						+"}\n" 
						+"" 
				},""
//				"----------\n" + 
//				"1. WARNING in X.js (at line 3)\n" + 
//				"	p1();\n" + 
//				"	^^\n" + 
//				"p1 is not a function \n" + 
//				"----------\n"
		);
	}
	
	
	public void test062()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var cc=function(){\n"  
						+"  function inner(){}\n" 
						+"  var dd=inner;\n" 
						+"}\n" 
						+"" 
				},
				"----------\n" + 
				"1. WARNING in X.js (at line 3)\n" + 
				"	var dd=inner;\n" + 
				"	    ^^\n" + 
				"The local variable dd is never read\n" + 
				"----------\n"
		);
	}

	
	public void test063()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var cc=function(){\n"  
						+"  var ii;\n" 
						+"  function inner(){\n" 
						+"    var dd=ii;\n" 
						+"  }\n" 
						+"}\n" 
						+"" 
				},
				"----------\n" + 
				"1. WARNING in X.js (at line 4)\n" + 
				"	var dd=ii;\n" + 
				"	    ^^\n" + 
				"The local variable dd is never read\n" + 
				"----------\n" + 
				"2. WARNING in X.js (at line 4)\n" + 
				"	var dd=ii;\n" + 
				"	       ^^\n" + 
				"The local variable ii may not have been initialized\n" + 
				"----------\n"
		);
	}
	
	public void test064()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						 "  function inner(){\n" 
						+"  var ii=0,i2=ii+1;\n" 
//						+"  var ii=0;\n" 
//						+"  var i2=ii+1;\n" 
						+"}\n" 
						+"" 
				},
				"----------\n" + 
				"1. WARNING in X.js (at line 2)\n" + 
				"	var ii=0,i2=ii+1;\n" + 
				"	         ^^\n" + 
				"The local variable i2 is never read\n" + 
				"----------\n"
		);
	}
 
	public void test065()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						 "  function inner(aArray){\n" 
						+"  var number = 0;\n" 
						+"  number -= aArray.length;\n" 
						+"  number += aArray.length;\n" 
						+"  return number;\n" 
						+"}\n" 
						+"" 
				},
				""
		);
	}

	public void test066()	{
		this.runNegativeTest(
				new String[] {
						"X.js",
						 "  TestClass = function() {\n" 
						+"  }\n" 
						+"  TestClass.prototype = new Object();\n" 
						+"  ns=new Object();\n" 
						+"  ns.TestClass = function() {\n" 
						+"  }\n" 
						+"  ns.TestClass.prototype = new Object();\n" 
						+"" 
				},
				""
		);
	}

	public void test067()	{	 

		this.runNegativeTest(
				new String[] {
						"X.js",
						"	function foo(a){\n" +
						"	  doo(a); \n" +
						"}\n",
						"Y.js",
						"	function doo(a){\n" +
						"	  foo(a); \n" +
						"}\n"
				},
				""
		);

	}


	public void test068()	{	 

		this.runNegativeTest(
				new String[] {
						"X.js",
						"	function foo(param1 , param2){\n" +
						"	if(\"\" || 0) {} \n" +
						"	 var value = param1 || param2; \n" +
						"	 if(param1 || param2) {} \n" +
						"}\n",
				},
				"----------\n" + 
				"1. WARNING in X.js (at line 3)\n" + 
				"	var value = param1 || param2; \n" + 
				"	    ^^^^^\n" + 
				"The local variable value is never read\n" + 
				"----------\n"
		);

	}
	
	public void test070()	{	 

		this.runNegativeTest(
				new String[] {
						"X.js",
						"	var s=new String();\n" +
						"	s.length=1; \n" +
						"\n",
				},
				""
		);
	}

	public void testbug255428()	{	 

		this.runNegativeTest(
				new String[] {
						"X.js",
						"	 function MyClass(){}\n" +
						" MyClass.prototype = {\n" +
						"    a : 0,\n" +
						"     myfunc : function(){} \n" +
						" };\n" +
						"function test() { \n" +
						"     var lObj = new MyClass();\n" +
						"     lObj.a = 2;\n" +
						"     lObj.myfunc();\n" +
						"}\n",
				},
				""
		);
	}
	
	public void testbug259187_String_slice()	{	 
		// String.split() argument count
		this.runNegativeTest(
					new String[] {
							"X.js",
							"var params = \"some?string\".slice('?');\n"
					},""
//					"----------\n" + 
//			"1. WARNING in X.js (at line 1)\n" + 
//			"	var params = \"some?string\".slice(\'?\');\n" + 
//			"	             ^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
//			"Wrong number of arguments for the function slice (), expecting 2 argument(s), but there was 1 \n" + 
//			"----------\n"
			);
		
		// check return type and argument count
		runBasicTest(new String[] {
			"Yprime.js",
			"var aString = \"some?string\".slice(2, 4);\n" +
			"aString.length;" 
		});
	}

	public void testbug259187_String_split()	{	 
		// String.split() argument count
		this.runNegativeTest(
					new String[] {
							"Y.js",
							"var params = \"some?string\".split('?');\n" +
							"var base = params.shift();"
					},""
					//"----------\n" + 
//			"1. WARNING in Y.js (at line 1)\n" + 
//			"	var params = \"some?string\".split(\'?\');\n" + 
//			"	             ^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
//			"Wrong number of arguments for the function split (), expecting 2 argument(s), but there was 1 \n" + 
//			"----------\n"
			);
		
		// check return type and argument count
		runBasicTest(new String[] {
			"Yprime.js",
			"var count = \"some?string\".split(\",\", 3);\n" +
			"count.length;" 
		});
	}

	public void testbug259187_String_substring() {
		// String.substring() argument count
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"var count = \"some?string\".substring('?');" 
					},""
//					"----------\n" + 
//			"1. WARNING in Z.js (at line 1)\n" + 
//			"	var count = \"some?string\".substring(\'?\');\n" + 
//			"	            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
//			"Wrong number of arguments for the function substring (), expecting 2 argument(s), but there was 1 \n" + 
//			"----------\n"
			);
		
		// check return type and argument count
		runBasicTest(new String[] {
			"Zprime.js",
			"var count = \"some?string\".substring(4, 3);\n" +
			"count.substring(4, 3);" 
		});
	}
	
	public void testbug196377_1() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"(function() {\n" +
							"hasClass();\n" +
							"function hasClass() {}\n" +
							"})();"
					},
					""
			);
	}
	
	public void testbug196377_2() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"top();" +
							"function top() {\n" +
							"inner();\n" +
							"function inner() {}\n" +
							"}"
					},
					""
			);
	}
	
	public void testbug196377_3() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"top();\n" +
							"function top() {}\n"
					},
					""
			);
	}
	
	public void testbug196377_4() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"top(1);\n" +
							"function top(a) {}\n"
					},
					""
			);
	}
	
	public void testbug283663() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"var myNum = 3;\n" +
							"if(myNum === undefined){}\n" +
							"if(myNum == undefined){}\n"
					},
					""
			);
	}
	
	public void testbug262728_A() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"function top() {\n"+
							"var x = function() {};\n"+
							"var x1 = 3;\n"+
							"inner();\n"+
							"function inner() {\n"+
							"var p = x1 + 3;\n"+
							"x();\n"+
							"inner2();\n"+
							"function inner2() {}\n"+
							"inner2();\n"+
							"}\n"+
							"x();\n"+
							"top();\n"+
							"}\n"+
							"top();"
					},
					"----------\n" + 
					"1. WARNING in Z.js (at line 6)\n" + 
					"	var p = x1 + 3;\n" + 
					"	    ^\n" + 
					"The local variable p is never read\n" + 
					"----------\n"
			);
	}
	
	public void testbug262728_B() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"function class2() {\n"+
							"this.publicFunction = function() {\n"+
							"privateFunction();\n"+
							"};\n"+
							"function privateFunction() {\n"+
							"return null;\n"+
							"};\n"+
							"}\n"+
							"function class1() {\n"+
							"function privateFunction() {\n"+
							"return null;\n"+
							"};\n"+
							"this.publicFunction = function() {\n"+
							"privateFunction();\n"+
							"};\n"+
							"}"
					},
					""
			);
	}
	public void testbug262728_C() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"inner();\n"+
							"function top() {\n"+
							"inner();\n"+
							"function inner(){};\n"+
							"inner();\n"+
							"}\n"+
							"inner();"
					},""
//					"----------\n" + 
//			"1. ERROR in Z.js (at line 1)\n" + 
//			"	inner();\n" + 
//			"	^^^^^\n" + 
//			"The function inner() is undefined\n" + 
//			"----------\n" +
//			"2. ERROR in Z.js (at line 7)\n" + 
//			"	inner();\n" + 
//			"	^^^^^\n" + 
//			"The function inner() is undefined\n" + 
//			"----------\n"
			);
	}
	
	public void testbug262728_D() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"(function() {\n"+
							"privateFunction();\n"+
							"var x;\n"+
							"function privateFunction() {\n"+
							"x + 3;\n"+
							"}\n"+
							"})();"
					},
					"----------\n" + 
		"1. WARNING in Z.js (at line 5)\n" + 
		"	x + 3;\n" + 
		"	^\n" + 
		"The local variable x may not have been initialized\n" + 
		"----------\n"
			);
	}
	
	public void testbug269203() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"function Square() {}\n" +
							"var sq = new Square();\n" +
							"sq.area = function() {};"
					},
					""
			);
	}
	
	public void testbug290414() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"function Test(w) {this.w1 = w;}\n" +
							"var t = new Test(3);\n" +
							"t.area = function() {};\n" +
							"var area = t.area();"
					},
					""
			);
	}
	
	public void testbug268989_1() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"/**@return {Date}*/function test(w) {return w}\n" +
							"var t = test(3);\n" +
							"t.getTime();"
					},
					""
			);
	}
	
	public void testbug268989_2() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"/**@returns {Date}*/function test(w) {return w}\n" +
							"var t = test(3);\n" +
							"t.getTime();"
					},
					""
			);
	}
	
	public void testObject() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"var o = new Object();\n" +
							"var x = o.toString();\n" +
							"x.charAt(0);\n" +
							"var x = o.toLocaleString();\n" +
							"x.charAt(0);\n" +
							"var x = o.valueOf();\n" +
							"x.toLocaleString();\n" +
							"var x = o.hasOwnProperty(1);\n" +
							"var x = o.isPrototypeOf(1);\n" +
							"var x = o.propertyIsEnumerable(1);\n" +
							"var x = o.constructor;\n" +
							"x.call();"
					},
					""
			);
	}
	
	public void Xtestbug247201() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"function Car() { this.color = 'red'; this.Move = function() {};};\n" +
							"Car.Stop = function() {};\n" +
							"Car.engine = 'diesel';\n" +
							"var o = '';\n" +
							"o += 'color => '+Car.color.prototype+'<br />';\n" +
							"var p = new Car();\n" +
							"o += 'Stop => '+p.Stop()+'<br />';\n" +
							"o += 'engine => '+p.engine+'<br />';\n" +
							"o += 'engine => '+p.engine.prototype+'<br />';\n" +
							"var MyCar = Car;\n" +
							"c = MyCar.Move;\n" +
							"o += 'Move => '+MyCar.Move()+'<br />';\n" +
							"o += 'Stop => '+MyCar.Stop+'<br />';\n" +
							"o += 'engine => '+MyCar.engine+'<br />';"
					},
					"----------\n" + 
					"1. ERROR in Z.js (at line 5)\n" + 
					"	o += \'color => \'+Car.color.prototype+\'<br />\';\n" + 
					"	                     ^^^^^\n" + 
					"Cannot make a static reference to the non-static field color\n" + 
					"----------\n" + 
					"2. WARNING in Z.js (at line 7)\n" + 
					"	o += \'Stop => \'+p.Stop()+\'<br />\';\n" + 
					"	                ^^^^^^^^\n" + 
					"The static function Stop() from the type Car should be accessed in a static way\n" + 
					"----------\n" + 
					"3. WARNING in Z.js (at line 8)\n" + 
					"	o += \'engine => \'+p.engine+\'<br />\';\n" + 
					"	                    ^^^^^^\n" + 
					"The static field Car.engine should be accessed in a static way\n" + 
					"----------\n" + 
					"4. WARNING in Z.js (at line 9)\n" + 
					"	o += \'engine => \'+p.engine.prototype+\'<br />\';\n" + 
					"	                    ^^^^^^\n" + 
					"The static field Car.engine should be accessed in a static way\n" + 
					"----------\n" + 
					"5. ERROR in Z.js (at line 11)\n" + 
					"	c = MyCar.Move;\n" + 
					"	    ^^^^^^^^^^\n" + 
					"Cannot make a static reference to the non-static function Move() from the type Car\n" + 
					"----------\n" + 
					"6. ERROR in Z.js (at line 12)\n" + 
					"	o += \'Move => \'+MyCar.Move()+\'<br />\';\n" + 
					"	                ^^^^^^^^^^^^\n" + 
					"Cannot make a static reference to the non-static function Move() from the type Car\n" + 
					"----------\n"
			);
	}
	
	public void testbug242871() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"TestClassBase = function() {};\n" +
							"TestFunction = function() {};\n" +
							"TestClass = function() {\n" +
							"	TestFunction.call(this, 1);\n" +
							"	TestClassBase.call(this, 2);\n" +
							"};\n" +
							"TestClass.prototype = new TestClassBase();"
					},
					""
			);
	}
	public void testbug242871_2() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"function TestClassBase() {};\n" +
							"TestFunction = function() {};\n" +
							"TestClass = function() {\n" +
							"	TestFunction.call(this, 1);\n" +
							"	TestClassBase.call(this, 2);\n" +
							"};\n" +
							"TestClass.prototype = new TestClassBase();"
					},
					""
			);
	}
	public void testbug242871_3() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"TestClassBase = function() {};\n" +
							"TestFunction = function() {};\n" +
							"TestClass = function() {\n" +
							"	TestFunction.call(this, 1);\n" +
							"	TestClassBase.call2(this, 2);\n" +
							"};\n" +
							"TestClass.prototype = new TestClassBase();"
					},""
//					"----------\n" + 
//					"1. ERROR in Z.js (at line 5)\n" + 
//					"	TestClassBase.call2(this, 2);\n" + 
//					"	              ^^^^^\n" + 
//					"The function call2(TestClass, Number) is undefined for the type TestClassBase\n" + 
//					"----------\n"
			);
	}
	
	public void testbug269094() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"function myFunc() {\n" +
							"return myFunc.a++;\n" +
							"}\n" +
							"myFunc.a = 0;"
					},
					""
			);
	}
	
	public void testbug269094_2() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"function myFunc() {\n" +
							"return 3;\n" +
							"}\n" +
							"myFunc.a = 0;"
					},
					""
			);
	}
	
	public void testbug269094_3() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"function myFunc() {\n" +
							"return 3;\n" +
							"}"
					},
					""
			);
	}
	
	public void testbug269094_4() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"/**@returns {String}*/\n" +
							"function myFunc() {\n" +
							"return 3;\n" +
							"}"
					},
					"----------\n" + 
					"1. WARNING in Z.js (at line 3)\n" + 
					"	return 3;\n" + 
					"	       ^\n" + 
					"Type mismatch: cannot convert from Number to String\n" + 
					"----------\n"
			);
	}
	
	public void testbug269094_5() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"/**@returns {Number}*/\n" +
							"function myFunc() {\n" +
							"return 3;\n" +
							"}"
					},
					""
			);
	}

	public void testbug278172() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"/**\n" +
							"* @param {boolean} options\n" +
							"*/\n" +
							"function foo(options) {\n" +
								"options = options || {};\n" +
								"if (!options.bar)\n" +
								"options.bar = 42;\n" +
							"}"
					},
					""
			);
	}
	
	public void testbug278172_2() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"/**\n" +
							"* @param {boolean} options\n" +
							"*/\n" +
							"function foo(options) {\n" +
								"if(options && 1 == 1) {}\n" + 
							"}"
					},
					""
			);
	}
	
	public void testbug318004() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"var obj = {};\n" +
							"obj.first = {};\n" +
							"obj.first.second = function() {};\n" +
							"obj.first.second.prototype = new Object();\n" +
							"if({} != obj.first.second) {}"
					},
					""
			);
	}
	
	public void testbug324241() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"var com = {};\n" +
							"com.MyType = function() {};\n" +
							"com.MyType.prototype.reload = function() {};\n" +
							"var c = new com.MyType();\n" +
							"var y = c.reload();\n" +
							"var x = c.reload;\n" +
							"c.reload2();\n" +
							"c.reload2;"
					},""
//					"----------\n" + 
//					"1. ERROR in Z.js (at line 7)\n" + 
//					"	c.reload2();\n" + 
//					"	  ^^^^^^^\n" + 
//					"The function reload2() is undefined for the type com.MyType\n" + 
//					"----------\n" + 
//					"2. WARNING in Z.js (at line 8)\n" + 
//					"	c.reload2;\n" + 
//					"	  ^^^^^^^\n" + 
//					"reload2 cannot be resolved or is not a field\n" + 
//					"----------\n"
			);
	}
	
	public void testbug333781() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"var com = {};\n" +
							"com.meth1 = function(){};\n" +
							"com[\"meth2\"] = function(){};\n" +
							"com.att1 = 1;\n" +
							"com[\"att2\"] = 2;\n" +
							"com.meth1();\n" +
							"com.meth2;" +
							"com.att1;\n" +
							"com.att2;"
					},
					""
			);
	}
	
	public void testBug397568() {
		this.runNegativeTest(
			new String[] {
				"Z.js",
				"switch (0) {\n"+
				"	case 0 :\n"+
				"		var x=123;\n"+
				"}\n"
			},
			""
		);
	}
	
	public void testWI90999() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"function abc() {\n" +
								"var createCookiePersister = function() {\n" +
								"switch (gadgetEnvironment) {\n" +
								"default:\n" +
								"break;\n" +
								"}" +
								"};" +
								"};"
					},
					"----------\n" + 
					"1. WARNING in Z.js (at line 2)\n" + 
					"	var createCookiePersister = function() {\n" + 
					"	    ^^^^^^^^^^^^^^^^^^^^^\n" + 
					"The local variable createCookiePersister is never read\n" + 
					"----------\n"
			);
	}
	
	public void testWI98930() {
		this.runNegativeTest(
					new String[] {
							"Z.js",
							"function outter() {\n" +
							"var path = { hmmm : function() {\n" +
							"switch (d) {\n" +
							"case \".\":\n" +
							";\n" +
							"break;\n" +
							"default:\n" +
							";\n" +
							"break;\n" +
							"}}};\n" +
							"var to = path.someFunc();\n" +
							"function testDupDefaultClass() {}\n" +
							"var testDupDefaultClassInit = new testDupDefaultClass();\n" +
							"testDupDefaultClassInit.myField = to;\n" +
							"}"
					},
					""
			);
	}
	
}