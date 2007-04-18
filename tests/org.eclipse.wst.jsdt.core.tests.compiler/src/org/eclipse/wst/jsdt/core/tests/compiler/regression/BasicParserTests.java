package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;

public class BasicParserTests extends AbstractRegressionTest {

	public BasicParserTests(String name) {
		super(name);
 
	}
	public void test001() {
		CompilationUnitDeclaration declaration = this.runParseTest(
			"{i=1;}" + 
			"\n",
			"X.js",
			"{\n  i = 1;\n}" + 
			"\n"
			
		 );
	}
	public void test002() {	// missing semicolon
		CompilationUnitDeclaration declaration = this.runParseTest(
				"i=1\n" + 
				"j=2;" + 
			"\n",
			"X.js",
			"i = 1;\n" + 
			"j = 2;" + 
			"\n"
			
		 );
	}

	public void test003() {	// var
		CompilationUnitDeclaration declaration = this.runParseTest(
				"var i=1,c=4;" + 
				"\n",
			"X.js",
			"var i = 1;\n" + 
			"var c = 4;" + 
			"\n"
		 );
		 
		this.runParseTest(
				"var i=1,c;" + 
				"\n",
			"X.js",
			"var i = 1;\n" + 
			"var c;" + 
			"\n"
		 );
	}

	public void test003a() {	// var
		CompilationUnitDeclaration declaration = this.runParseTest(
				"var foo=null;" + 
				"\n",
			"X.js",
			"var foo = null;" + 
			"\n"
		 );
		 
		this.runParseTest(
				"var i=1,c;" + 
				"\n",
			"X.js",
			"var i = 1;\n" + 
			"var c;" + 
			"\n"
		 );
	}
	
	public void test004() {	// functions
		this.runParseTest(
				"function abc(){}" + 
				"\n",
			"X.js",
			"function abc() {\n}" + 
			"\n"
		 );
		 
	}

	public void test004a() {	// functions
		this.runParseTest(
				"function abc(a){}" + 
				"\n",
			"X.js",
			"function abc(a) {\n}" + 
			"\n"
		 );
		 
	}

	public void test004b() {	// functions
		this.runParseTest(
				"function abc(a,b){i=1;}" + 
				"\n",
			"X.js",
			"function abc(a, b) {\n  i = 1;\n}" + 
			"\n"
		 );
		 
	}

	public void test005a() {	// expressions
		this.runParseTest(
				"i=this.v1+4;" + 
				"\n",
			"X.js",
			"i = (this.v1 + 4);" + 
			"\n"
		 );
		 
	}

	public void test005b() {	// expressions
		this.runParseTest(
				"i=funccall(c,b);" + 
				"i=cc.funccall(c,b);" + 
				"i=dd.cc.funccall(c,b);" + 
				"\n",
			"X.js",
			"i = funccall(c, b);\n" + 
			"i = cc.funccall(c, b);\n" + 
			"i = dd.cc.funccall(c, b);" + 
			"\n"
		 );
		 
	}


	public void test005b2() {	// expressions
		this.runParseTest(
				"i=funccall().methCall();" + 
				"\n",
			"X.js",
			"i = funccall().methCall();" + 
			"\n"
		 );
		 
	}

	public void test005d() {	// expressions
		this.runParseTest(
				"i= function (c,b) {i=2;}" + 
				"\n",
			"X.js",
			"i = function (c, b) {\n  i = 2;\n};" + 
			"\n"
		 );
		 
	}

	public void test005e() {	// expressions/
		this.runParseTest(
				"i= 1+(z-v);" + 
				"i= ++z * v--;" + 
				"\n",
			"X.js",
			"i = (1 + (z - v));\n" + 
			"i = ((++ z) * (v --));" + 
			"\n"
		 );
		 
	}

	public void test005f() {	// expressions
		this.runParseTest(
				"i= new abc(dd);" + 
				"\n",
			"X.js",
			"i = new abc(dd);" + 
			"\n"
		 );
		 
	}

	public void test005f1() {	// expressions
		this.runParseTest(
				"i= new dojo.uri.Uri(1,2);" + 
				"\n",
			"X.js",
			"i = new dojo.uri.Uri(1, 2);" + 
			"\n"
		 );
		 
	}

	public void test005f2() {	// expressions
		this.runParseTest(
				"dojo.string.normalizeNewlines = function (text,newlineChar) {i=1;}" + 
				"\n",
			"X.js",
			"dojo.string.normalizeNewlines = function (text, newlineChar) {\n  i = 1;\n};" + 
			"\n"
		 );
		 
	}

	public void test005f3() {	// expressions
		this.runParseTest(
				"	function foo() {\n" + 
				"		var maxUnits = 0;\n" + 
				"			var unitResult = \n" + 
				"				new CompilationUnitResult(\n" + 
				"					null, \n" + 
				"					i, \n" + 
				"					maxUnits); \n" + 
				"}\n",
			"X.js",
			"function foo() {\n"+
			"  var maxUnits = 0;\n"+
			"  var unitResult = new   CompilationUnitResult(null, i, maxUnits);\n"+
			"}"+
			"\n"
		 );
		 
	}

	public void test005f4() {	// expressions
		this.runParseTest(
				"i= new SomeClass;" + 
				"\n",
			"X.js",
			"i = new SomeClass;" + 
			"\n"
		 );
		 
	}

	public void test005f5() {	// expressions
		this.runParseTest(
				"function f(){\n" + 
				"i= new SomeClass \n}" + 
				"\n",
			"X.js",
			"function f() {\n" + 
			"  i = new SomeClass;\n" + 
			"}" + 
			"\n"
		 );
		 
	}

	public void test005f6() {	// expressions
		this.runParseTest(
				"	function X(i){}								\n" +
				"	function foo(){								\n" +
				"		var j = 0;							\n" +
				"		var x = new X(j);						\n" +
				"}	" +
				"\n",
			"X.js",
			"function X(i) {\n" + 
			"}\n" + 
			"function foo() {\n" + 
			"  var j = 0;\n" +
			"  var x = new   X(j);\n" +
			"}" + 
			"\n"
		 );
		 
	}

 
	
	public void test005g1() {	// expressions
		this.runParseTest(
				"i= typeof objpath != \"string\";" + 
				"\n",
			"X.js",
			"i = ((typeof objpath) != \"string\");" + 
			"\n"
		 );
		 
	}

	public void test005g2() {	// expressions
		this.runParseTest(
				"i= ar instanceof Error;" + 
				"\n",
			"X.js",
			"i = (ar instanceof Error);" + 
			"\n"
		 );
	}		 
	
	public void test005g3() {	// expressions
		this.runParseTest(
				"i= anArg.name != null ;" + 
				"\n",
			"X.js",
			"i = (anArg.name != null);" + 
			"\n"
		 );
	}		 
	
	public void test005g4() {	// expressions
		this.runParseTest(
				"i= anArg.name != undefined ;" + 
				"\n",
			"X.js",
			"i = (anArg.name != undefined);" + 
			"\n"
		 );
	}		 
	
	public void test005h1() {	// expressions
		this.runParseTest(
				"i= { a: 2 , b: 3+4};" + 
				"\n",
			"X.js",
			"i = {\n  a : 2,\n  b : (3 + 4)\n};" + 
			"\n"
		 );
		 
}

	public void test005h2() {	// expressions
		this.runParseTest(
				"i= { a: function(){ var ar={c:3,d:4,e:4}; } , b: function(cc){ var c=1;} , d:function(){}};" + 
				"\n",
			"X.js",
			"i = {\n  a : function () {\n  var ar = {\n    c : 3,\n    d : 4,\n    e : 4\n  };\n},\n  b : function (cc) {\n  var c = 1;\n},\n  d : function () {\n}\n};" + 
			"\n"
		 );
		 
}

	public void test005i() {	// expressions
		this.runParseTest(
				"i= arr[4];" + 
				"\n",
			"X.js",
			"i = arr[4];" + 
			"\n"
		 );
	}
	public void test005i1() {	// expressions
		this.runParseTest(
				"arr[4]=1;" + 
				"\n",
			"X.js",
			"arr[4] = 1;" + 
			"\n"
		 );
	}
	public void test005i2() {	// expressions
		this.runParseTest(
				"var arr=null;" + 
				"arr[4]=1;" + 
				"\n",
			"X.js",
			"var arr = null;\n" + 
			"arr[4] = 1;" + 
			"\n"
		 );
	}
	public void test005j() {	// expressions
		this.runParseTest(
				"i= [a,b];" + 
				"\n",
			"X.js",
			"i = [a, b];" + 
			"\n"
		 );
	}
	 
	public void test005j2() {	// expressions
		this.runParseTest(
				"i= [,a];" + 
				"\n",
			"X.js",
			"i = [, a];" + 
			"\n"
		 );
	}
	 
	public void test005j3() {	// expressions
		this.runParseTest(
				"i= [a,];" + 
				"\n",
			"X.js",
			"i = [a, ];" + 
			"\n"
		 );
	}

	public void test005j4() {	// expressions
		this.runParseTest(
				"i= [,];" + 
				"\n",
			"X.js",
			"i = [, ];" + 
			"\n"
		 );
	}

	public void test005j5() {	// expressions
		this.runParseTest(
				"i= [];" + 
				"\n",
			"X.js",
			"i = [];" + 
			"\n"
		 );
	}
	public void test005j6() {	// expressions
		this.runParseTest(
				"i= [a,,b];" + 
				"\n",
			"X.js",
			"i = [a, , b];" + 
			"\n"
		 );
	}

	public void test007() {	// if
		this.runParseTest(
				"if (a>1) this.c=f+5;" + 
				"\n",
			"X.js",
			"if ((a > 1))\n    this.c = (f + 5);" + 
			"\n"
		 );
	}

	public void test008() {	// try catch
		this.runParseTest(
				"try { a=2;} catch (ex) {a=3;}" + 
				"\n",
			"X.js",
			"try \n  {\n    a = 2;\n  }\ncatch (ex)   {\n    a = 3;\n  }" + 
			"\n"
		 );
	}

	public void test009() {	// for
		this.runParseTest(
				"for (i=1;i<3;i++)\n" +
				"  f++;" +
				"\n",
			"X.js",
			"for (i = 1; (i < 3); i ++) \n  f ++;"+	
			"\n"
		 );
	}
	public void test009a() {	// for
		this.runParseTest(
				"for (;i<3;i++)\n" +
				"  f++;" +
				"\n",
			"X.js",
			"for (; (i < 3); i ++) \n  f ++;"+	
			"\n"
		 );
	}

	public void test0010() {	// for in
		this.runParseTest(
				"for (var a in this.vars)\n" +
				"  f++;" +
				"\n",
			"X.js",
			"for (var a in this.vars) \n  f ++;"+	
			"\n"
		 );
	}

	public void test0010a() {	// for in
		this.runParseTest(
				"for (a in this.vars)\n" +
				"  f++;" +
				"\n",
			"X.js",
			"for (a in this.vars) \n  f ++;"+	
			"\n"
		 );
	}


	public void test0011() {	// missing semicolon
		this.runParseTest(
				"  function bar() \n{\n" +
				"    System.out.println()\n" +
				"  }\n" ,
			"X.js",
			"function bar() {\n"+	
			"  System.out.println();\n" +
			"}\n" 
		 );
	}
	
	public void test0011a() {	// missing semicolon
		this.runParseTest(
			"function bar() {\n"+	
			"  System.out\n" +
			"}\n", 
			"X.js",
			"function bar() {\n"+	
			"  System.out;\n" +
			"}\n" 
		 );
	}
	
	public void test0020() {	// missing semicolon
		this.runParseTest(
			      "function Bob(firstname, lastname) {\n" +
			      "   var Firstname = firstname;\n" +
			      "   var Lastname = lastname;\n" +
			      "}\n" +
			      "Bob.prototype.name = function () {return this.Fistname + this.Lastname;};\n",
			"X.js",
		      "function Bob(firstname, lastname) {\n" +
		      "  var Firstname = firstname;\n" +
		      "  var Lastname = lastname;\n" +
		      "}\n" +
		      "Bob.prototype.name = function () {\n  return (this.Fistname + this.Lastname);\n};\n"
		 );
	}

	public void test0022() {
		this.runParseTest(
				"var SingleQuote = {\n" +
				"   Version: '1.1-beta2' \n" +
				"}\n",
			"X.js",
				"var SingleQuote = {\n" +
				"  Version : '1.1-beta2'\n" +
				"};\n"
		);
	}

	public void test0023() {
		this.runParseTest(
				"var Try = { \n" +
				"	these: function() { \n" +
				"		var returnValue; \n" +
				"	} \n"+
				"	} \n",
			"X.js",
			"var Try = {\n" +
			"  these : function () {\n" +
			"  var returnValue;\n" +
			"}\n"+
			"};\n"
		);
	}

	public void test0024() {
		this.runParseTest(
				"var Try = { \n" +
				"	these: function() { \n" +
				"		var returnValue; \n" +
				"	} \n" +
				"};",
			"X.js",
				"var Try = {\n" +
				"  these : function () {\n" +
				"  var returnValue;\n" +
				"}\n" +
				"};\n"
		);
	}
	
	
	public void test0026() {
		this.runParseTest(
				"String.replace(/&/g, '&amp;');",
			"X.js",
				"String.replace(/&/g, '&amp;');\n"
		);
	}
	
	public void test0027() {
		this.runParseTest(
				"  (!options) ? options = {} : '';",
			"X.js",
				"((! options) ? (options = {}) : '');\n"			
		);
	}
	
	public void test0028() {
		this.runParseTest(
				" if(typeof dojo==\"undefined\"){ \n" +
				"	function dj_undef(){ \n" +
				"	} \n" +
				"}",
			"X.js",
				" if(typeof dojo==\"undefined\"){ \n" +
				"	function dj_undef(){ \n" +
				"	} \n" +
				"}"			
		);
	}
	
	public void test0029() {
		this.runParseTest(
				"  abc();",
			"X.js",
				"abc();\n"			
		);
	}
	
	public void test0030() {
		this.runParseTest(
				"  \"cc\".abc();",
			"X.js",
				"\"cc\".abc();\n"			
		);
	}
	
	public void test0031() {
		this.runParseTest(
			"var a = 1;\n" +
			"// test unicode \\u000a var a =1; \n" +
			"var b = 2; \n",
			"X.js",
			"var a = 1;\n" +
			"var b = 2;\n"			
		);
	}
	public void test0032() {
		this.runParseTest(
			"var a = 1;\n" +
			"/* \n" +
			"* test unicode \\u000a var a =1; \n " +
			"*/" +
			"var b = 2; \n",
			"X.js",
			"var a = 1;\n" +
			"var b = 2;\n"			
		);
	}
	
	public void test0033() {
		this.runParseTest(
				"var a = \"a\\>\";\n",
			"X.js",
				"var a = \"a>\";\n"			
		);
	}
	
	public void test0034() {
				this.runParseTest(
						"label: for (var i = 0; i < 10; i++) {\n" +
						"	     continue label;\n" +
						"}", 
						"X.js",
						"label: for (var i = 0;; (i < 10); i ++) \n" +
						"  {\n"+			
						"    continue label;\n"+			
						"  }\n"
					);
	}
	

	public void test0035() {
		// check a unicode " in string. The expected " is escaped 
		// because of the way the test framework works. It converts 
		// special characters to a character representation before
		// doing the compare. 
		this.runParseTest(
				"\"abc\\u0022def\";\n",
			"X.js",
				"\"abc\\\"def\";\n"			
		);
	}
	
	public void test0036() {
		this.runParseTest(
				"'abc\\u0027def';\n",
			"X.js",
				"'abc'def';\n"			
		);
	}
	
	public void test0037() {
		// check a unicode " in string. The expected " is escaped 
		// because of the way the test framework works. It converts 
		// special characters to a character representation before
		// doing the compare. 
		this.runParseTest(
				"\"\\u0022def\";\n",
			"X.js",
				"\"\\\"def\";\n"			
		);
	}
	
	public void test0038() {
		// check a unicode " in string. The expected " is escaped 
		// because of the way the test framework works. It converts 
		// special characters to a character representation before
		// doing the compare. 
		this.runParseTest(
				"\"abc\\x22def\";\n",
			"X.js",
				"\"abc\\\"def\";\n"			
		);
	}
	
	public void test0039() {
		// check a unicode " in string. The expected " is escaped 
		// because of the way the test framework works. It converts 
		// special characters to a character representation before
		// doing the compare. 
		this.runParseTest(
				"\"\\x22def\";\n",
			"X.js",
				"\"\\\"def\";\n"			
		);
	}
	
	public void test0040() {
		this.runParseTest(
				"var onStart = function() { \n" +
				"	this.onStart.fire();\n" + 
				"	this.runtimeAttributes = {};\n" +
				"	for (var attr in this.attributes) {\n" +
				"		this.xyz(attr);\n" +
				"	}\n" +
				"};",
			"X.js",
				"var onStart = function () {\n" +
				"  this.onStart.fire();\n" + 
				"  this.runtimeAttributes = {};\n" +
				"  for (var attr in this.attributes) \n" +
				"    {\n" +
				"      this.xyz(attr);\n" +
				"    }\n" +
				"};\n"		
		);
	}

	public void test0041() {
		// check a unicode " in string. The expected " is escaped 
		// because of the way the test framework works. It converts 
		// special characters to a character representation before
		// doing the compare. 
		this.runParseTest(
				"(function (){});\n",
			"X.js",
				"(function (){});\n"			
		);
	}

	
	public void test0042() {
		// check a unicode " in string. The expected " is escaped 
		// because of the way the test framework works. It converts 
		// special characters to a character representation before
		// doing the compare. 
		this.runParseTest(
				"function trim(oldString)\n" +
				"{\n" + 
				"var newString = oldString;\n" +
				"var safety = 0;\n" +
				"var safetyLimit = 10000;\n" +
				"while(newString.charAt(0) == \" \" && safety < safetyLimit){\n" +
				"	newString = newString.substring(1);\n" +
				"	safety++;\n" +
				"}\n" +
				"while(newString.charAt(newString.length-1) == \" \" && safety < safetyLimit){\n" +
				"newString = newString.substring(0, newString.length-1);\n" +
				"	safety++\n" +
				"	}\n" +
				"return newString;\n" +
 				"};",
			"X.js",
			
			"function trim(oldString) {\n" +
			"  var newString = oldString;\n" +
			"  var safety = 0;\n" +
			"  var safetyLimit = 10000;\n" +
			"  while (((newString.charAt(0) == \" \") && (safety < safetyLimit)))    {\n" +
			"      newString = newString.substring(1);\n" +
			"      safety ++;\n" +
			"    }\n" +
			"  while (((newString.charAt((newString.length - 1)) == \" \") && (safety < safetyLimit)))    {\n" +
			"      newString = newString.substring(0, (newString.length - 1));\n" +
			"      safety ++;\n" +
			"    }\n" +
			"  return newString;\n" +
			"}\n" +
				";\n"		
		);
	}
	
	
	

	public void test0043() {
		// check a unicode " in string. The expected " is escaped 
		// because of the way the test framework works. It converts 
		// special characters to a character representation before
		// doing the compare. 
		this.runParseTest(
				"Foo=function(){}\nbar=function(){}",
			"X.js",
				"Foo = function () {\n};\nbar = function () {\n};\n"			
		);
	}


	public void test0044() {
		// check a unicode " in string. The expected " is escaped 
		// because of the way the test framework works. It converts 
		// special characters to a character representation before
		// doing the compare. 
		this.runParseTest(
				"ptr[i]();",
			"X.js",
				"ptr[i]();\n"			
		);
	}

  


}
