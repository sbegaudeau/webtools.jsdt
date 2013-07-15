/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler.parser;

public class SelectionTest extends AbstractSelectionTest {
	
public SelectionTest(String testName) {
	super(testName);
}
/*
 * Select superclass
 */
public void test00() {

	String str = 
		"	var i=foo(); 									\n" +
		"";

	String selectionStartBehind = "=";
	String selectionEndBehind = "foo";
	
	String expectedCompletionNodeToString = "<SelectOnMessageSend:foo()>";
	String completionIdentifier = "foo";
	String expectedUnitDisplayString =
		"var i = <SelectOnMessageSend:foo()>;\n" + 
		"" + 
		"";
	String expectedReplacedSource = "foo()";
	String testName = "<select message send>";

	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
		
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName); 
}


public void test01() {

	String str = 
		"	var i=bb.foo(); 									\n" +
		"";

	String selectionStartBehind = "bb.";
	String selectionEndBehind = "foo";
	
	String expectedCompletionNodeToString = "<SelectOnMessageSend:bb.foo()>";
	String completionIdentifier = "foo";
	String expectedUnitDisplayString =
		"var i = <SelectOnMessageSend:bb.foo()>;\n" + 
		"" + 
		"";
	String expectedReplacedSource = "foo()";
	String testName = "<select message send>";

	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
		
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName); 
}


///*
// * Select superclass
// */
//public void test01() {
//
//	String str = 
//		"import java.io.*;							\n" + 
//		"											\n" + 
//		"public class X extends IOException {		\n" + 
//		"}											\n"; 
//
//	String selectionStartBehind = "extends ";
//	String selectionEndBehind = "IOException";
//	
//	String expectedCompletionNodeToString = "<SelectOnType:IOException>";
//	String completionIdentifier = "IOException";
//	String expectedUnitDisplayString =
//		"import java.io.*;\n" + 
//		"public class X extends <SelectOnType:IOException> {\n" + 
//		"  public X() {\n" + 
//		"  }\n" + 
//		"}\n";
//	String expectedReplacedSource = "IOException";
//	String testName = "<select superclass>";
//
//	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
//	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
//		
//	this.checkDietParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName); 
//}
/*
 * Select superinterface
 */
//public void test02() {
//
//	String str = 
//		"import java.io.*;													\n" + 
//		"																	\n" + 
//		"public class X extends IOException implements Serializable {		\n" +
//		" int foo(){} 														\n" +
//		"}																	\n"; 
//
//	String selectionStartBehind = "implements ";
//	String selectionEndBehind = "Serializable";
//	
//	String expectedCompletionNodeToString = "<SelectOnType:Serializable>";
//	String completionIdentifier = "Serializable";
//	String expectedUnitDisplayString =
//		"import java.io.*;\n" + 
//		"public class X extends IOException implements <SelectOnType:Serializable> {\n" + 
//		"  public X() {\n" + 
//		"  }\n" + 
//		"  int foo() {\n" + 
//		"  }\n" + 
//		"}\n";
//	String expectedReplacedSource = "Serializable";
//	String testName = "<select superinterface>";
//
//	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
//	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
//		
//	this.checkDietParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName); 
//}
///*
// * Select qualified superclass
// */
//public void test03() {
//
//	String str = 
//		"public class X extends java.io.IOException {	\n" + 
//		"}												\n"; 
//
//	String selectionStartBehind = "java.io.";
//	String selectionEndBehind = "IOException";
//	
//	String expectedCompletionNodeToString = "<SelectOnType:java.io.IOException>";
//	String completionIdentifier = "IOException";
//	String expectedUnitDisplayString =
//		"public class X extends <SelectOnType:java.io.IOException> {\n" + 
//		"  public X() {\n" + 
//		"  }\n" + 
//		"}\n";
//	String expectedReplacedSource = "java.io.IOException";
//	String testName = "<select qualified superclass>";
//
//	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
//	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
//		
//	this.checkDietParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName); 
//}
///*
// * Select package from qualified superclass
// */
//public void test04() {
//
//	String str = 
//		"public class X extends java.io.IOException {	\n" + 
//		"}												\n"; 
//
//	String selectionStartBehind = "java.";
//	String selectionEndBehind = "java.io";
//	
//	String expectedCompletionNodeToString = "<SelectOnType:java.io>";
//	String completionIdentifier = "io";
//	String expectedUnitDisplayString =
//		"public class X extends <SelectOnType:java.io> {\n" + 
//		"  public X() {\n" + 
//		"  }\n" + 
//		"}\n";
//	String expectedReplacedSource = "java.io.IOException";
//	String testName = "<select package from qualified superclass>";
//
//	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
//	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
//		
//	this.checkDietParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName); 
//}
/*
 * Select message send
 */
public void test05() {

	String str = 
		"function foo(){									\n" +
		"	System.out.println(\"hello\");			\n" +
		"}";

	String selectionStartBehind = "System.out.";
	String selectionEndBehind = "println";
	
	String expectedCompletionNodeToString = "<SelectOnMessageSend:System.out.println(\"hello\")>";
	String completionIdentifier = "println";
	String expectedUnitDisplayString =
		"function foo() {\n" + 
		"  <SelectOnMessageSend:System.out.println(\"hello\")>;\n" + 
		"}\n" + 
		"";
	String expectedReplacedSource = "println(\"hello\")";
	String testName = "<select message send>";

	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
		
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName); 
}

public void test05b() {

	String str = 
		"		System.out.println(\"hello\");			\n";

	String selectionStartBehind = "System.out.";
	String selectionEndBehind = "println";
	
	String expectedCompletionNodeToString = "<SelectOnMessageSend:System.out.println(\"hello\")>";
	String completionIdentifier = "println";
	String expectedUnitDisplayString =
		"<SelectOnMessageSend:System.out.println(\"hello\")>;\n" + 
		"";
	String expectedReplacedSource = "println(\"hello\")";
	String testName = "<select message send>";

	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
		
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName); 
}

/*
 * Select message send with recovery before
 */
public void test06() {

	String str = 
		"	function foo(){									\n" +
		"		System.out.println(\"hello\");			\n";

	String selectionStartBehind = "System.out.";
	String selectionEndBehind = "println";
	
	String expectedCompletionNodeToString = "<SelectOnMessageSend:System.out.println(\"hello\")>";
	String completionIdentifier = "println";
	String expectedUnitDisplayString =
		"function foo() {\n" + 
		"  <SelectOnMessageSend:System.out.println(\"hello\")>;\n" + 
		"  ;\n" +
		"}\n" + 
		"";
	String expectedReplacedSource = "println(\"hello\")";
	String testName = "<select message send with recovery before>";

	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
		
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName); 
}
/*
 * Select message send with sibling method
 */
public void test07() {

	String str = 
		"	function foo(){									\n" +
		"		this.bar(\"hello\");					\n" +
		"   }\n" +
		"	function bar( s){							\n" +
		"		return s.length();						\n"	+
		"	}											\n" +
		"";

	String selectionStartBehind = "this.";
	String selectionEndBehind = "this.bar";
	
	String expectedCompletionNodeToString = "<SelectOnMessageSend:this.bar(\"hello\")>";
	String completionIdentifier = "bar";
	String expectedUnitDisplayString =
		"function foo() {\n" + 
		"  <SelectOnMessageSend:this.bar(\"hello\")>;\n" + 
		"}\n" + 
		"function bar(s) {\n" + 
		"  return s.length();\n" +
		"}\n" + 
		"";
	String expectedReplacedSource = "this.bar(\"hello\")";
	String testName = "<select message send with sibling method>";

	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
		
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName); 
}
/*
 * Select field reference
 */
public void test08() {

	String str = 
		"	var num = 0;								\n" +
		"	function foo(){									\n" +
		"		var j = this.num;						\n" +
		"   }";

	String selectionStartBehind = "this.";
	String selectionEndBehind = "this.num";
	
	String expectedCompletionNodeToString = "<SelectionOnFieldReference:this.num>";
	String completionIdentifier = "num";
	String expectedUnitDisplayString =
		"var num = 0;\n" + 
		"function foo() {\n" + 
		"  var j = <SelectionOnFieldReference:this.num>;\n" + 
		"}\n" + 
		"";
	String expectedReplacedSource = "this.num";
	String testName = "<select field reference>";

	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
		
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName); 
}
/*
 * Select field reference with syntax errors
 */
public void test09() {

	String str = 
		"	var num 									\n" +
		"	function foo(){									\n" +
		"		var j = this.num;						\n" +
		"}												\n";

	String selectionStartBehind = "this.";
	String selectionEndBehind = "this.num";
	
	String expectedCompletionNodeToString = "<SelectionOnFieldReference:this.num>";
	String completionIdentifier = "num";
	String expectedUnitDisplayString =
		"var num;\n" + 
		"function foo() {\n" + 
		"  var j = <SelectionOnFieldReference:this.num>;\n" + 
		"}\n";
	String expectedReplacedSource = "this.num";
	String testName = "<select field reference with syntax errors>";

	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
		
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName); 
}
/*
 * Select field reference inside message receiver
 */
public void test10() {

	String str = 
		"	var x; 									\n" +
		"	function foo(){								\n" +
		"		var j = this.x.foo();				\n" +
		"}											\n";
		
	String selectionStartBehind = "this.";
	String selectionEndBehind = "this.x";
	
	String expectedCompletionNodeToString = "<SelectionOnFieldReference:this.x>";
	String completionIdentifier = "x";
	String expectedUnitDisplayString =
		"var x;\n" + 
		"function foo() {\n" + 
		"  var j = <SelectionOnFieldReference:this.x>.foo();\n" + 
		"}\n";
	String expectedReplacedSource = "this.x";
	String testName = "<select field reference inside message receiver>";

	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
		
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName); 
}
/*
 * Select allocation
 */
public void test11() {

	String str = 
		"	function X(i){}								\n" +
		"	function foo(){								\n" +
		"		var j = 0;							\n" +
		"		var x = new X(j);						\n" +
		"}											\n";
		
	String selectionStartBehind = "new ";
	String selectionEndBehind = "new X";
	
	String expectedCompletionNodeToString = "<SelectOnAllocationExpression:new <SelectOnName:X>(j)>";
	String completionIdentifier = "X";
	String expectedUnitDisplayString =
		"function X(i) {\n" + 
		"}\n" + 
		"function foo() {\n" + 
		"  var j = 0;\n" + 
		"  var x = <SelectOnAllocationExpression:new <SelectOnName:X>(j)>;\n" + 
		"}\n";
	String expectedReplacedSource = NONE;
	String testName = "<select allocation>";

	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
		
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName); 
}
/*
 * Select qualified allocation
 */
//public void test12() {
//
//	String str = 
//		"public class X {		 					\n" +
//		" 	class Y {								\n" +
//		"		Y(int i){}							\n" +
//		"	}										\n" +
//		"	X(int i){}								\n" +
//		"	int foo(){								\n" +
//		"		int j = 0;							\n" +
//		"		X x = new X(j);						\n" +
//		"		x.new Y(1);							\n" +
//		"	}										\n" +
//		"}											\n";
//		
//	String selectionStartBehind = "x.new ";
//	String selectionEndBehind = "x.new Y";
//	
//	String expectedCompletionNodeToString = "<SelectOnQualifiedAllocationExpression:x.new Y(1)>";
//	String completionIdentifier = "Y";
//	String expectedUnitDisplayString =
//		"public class X {\n" + 
//		"  class Y {\n" + 
//		"    Y(int i) {\n" + 
//		"    }\n" + 
//		"  }\n" + 
//		"  X(int i) {\n" + 
//		"  }\n" + 
//		"  int foo() {\n" + 
//		"    int j;\n" + 
//		"    X x;\n" + 
//		"    <SelectOnQualifiedAllocationExpression:x.new Y(1)>;\n" + 
//		"  }\n" + 
//		"}\n";
//	String expectedReplacedSource = "x.new Y(1)";
//	String testName = "<select qualified allocation>";
//
//	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
//	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName); 
//}
/*
 * Select qualified name reference receiver
 */
public void test13() {

	String str = 
		"	function foo(){								\n" +
		"		java.lang.System.out.println();		\n" +
		"}											\n";
		
	String selectionStartBehind = "java.lang.";
	String selectionEndBehind = "java.lang.System";
	
	String expectedCompletionNodeToString = "<SelectionOnFieldReference:java.lang.System>";
	String completionIdentifier = "System";
	String expectedUnitDisplayString =
		"function foo() {\n" + 
		"  <SelectionOnFieldReference:java.lang.System>.out.println();\n" + 
		"}\n";
	String expectedReplacedSource = "System";
	String testName = "<select qualified name receiver>";

	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
		
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName); 
}
/*
 * Select qualified name reference 
 */
public void test14() {

	String str = 
		"	function foo(){								\n" +
		"		var sys = java.lang.System;		\n" +
		"}											\n";
		
	String selectionStartBehind = "java.lang.";
	String selectionEndBehind = "java.lang.System";
	
	String expectedCompletionNodeToString = "<SelectionOnFieldReference:java.lang.System>";
	String completionIdentifier = "System";
	String expectedUnitDisplayString =
		"function foo() {\n" + 
		"  var sys = <SelectionOnFieldReference:java.lang.System>;\n" + 
		"}\n";
	String expectedReplacedSource = "System";
	String testName = "<select qualified name>";

	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
		
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName); 
}
///*
// * Select variable type with modifier
// */
//public void test15() {
//
//	String str = 
//		"public class X {		 					\n" +
//		"	int foo(){								\n" +
//		"		final System sys = null;			\n" +
//		"	}										\n" +
//		"}											\n";
//		
//	String selectionStartBehind = "final ";
//	String selectionEndBehind = "final System";
//	
//	String expectedCompletionNodeToString = "<SelectOnType:System>";
//	String completionIdentifier = "System";
//	String expectedUnitDisplayString =
//		"public class X {\n" + 
//		"  public X() {\n" + 
//		"  }\n" + 
//		"  int foo() {\n" + 
//		"    final <SelectOnType:System> sys;\n" + 
//		"  }\n" + 
//		"}\n";
//	String expectedReplacedSource = "System";
//	String testName = "<select variable type with modifier>";
//
//	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
//	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName); 
//}
/*
 * Select variable type
 */
//public void test16() {
//
//	String str = 
//		"public class X {		 					\n" +
//		"	int foo(){								\n" +
//		"		System sys = null;					\n" +
//		"	}										\n" +
//		"}											\n";
//		
//	String selectionStartBehind = "\n		";
//	String selectionEndBehind = "\n		System";
//	
//	String expectedCompletionNodeToString = "<SelectOnType:System>";
//	String completionIdentifier = "System";
//	String expectedUnitDisplayString =
//		"public class X {\n" + 
//		"  public X() {\n" + 
//		"  }\n" + 
//		"  int foo() {\n" + 
//		"    <SelectOnType:System> sys;\n" + 
//		"  }\n" + 
//		"}\n";
//	String expectedReplacedSource = "System";
//	String testName = "<select variable type>";
//
//	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
//	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName); 
//}
/*
 * Select name
 */
public void test17() {

	String str = 
		"function foo(){								\n" +
		"		System 								\n" +
		"}											\n";
		
	String selectionStartBehind = "\n		";
	String selectionEndBehind = "\n		System";
	
	String expectedCompletionNodeToString = "<SelectOnName:System>";
	String completionIdentifier = "System";
	String expectedUnitDisplayString =
		"function foo() {\n" + 
		"  <SelectOnName:System>;\n" + 
		"}\n";
		
	String expectedReplacedSource = "System";
	String testName = "<select name>";

	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
		
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName); 
}
/*
 * Select super constructor call
 */
//public void test27() {
//
//	String str =
//		"public class G {					\n" +
//		"	G() {							\n" +
//		"		super();					\n" +
//		"	}								\n" +
//		"}									\n";
//		
//	String selectionStartBehind = "\n\t\t";
//	String selectionEndBehind = "super";
//	
//	String expectedCompletionNodeToString = "<SelectOnExplicitConstructorCall:super()>;";
//	
//	String completionIdentifier = "super";
//	String expectedUnitDisplayString =
//		"public class G {\n" + 
//		"  G() {\n" + 
//		"    <SelectOnExplicitConstructorCall:super()>;\n" + 
//		"  }\n" + 
//		"}\n";
//		
//	String expectedReplacedSource = "super()";
//	String testName = "<select super constructor call>";
//
//	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
//	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName); 
//}
/*
 * Select qualified super constructor call
 */
//public void test28() {
//
//	String str =
//		"public class G {						\n" +
//		"	class M {}							\n" +
//		"	static Object foo() {				\n" +
//		"		class X extends M {				\n" +
//		"			X (){						\n" +
//		"				new G().super();		\n" +
//		"			}							\n" +
//		"		}								\n" +
//		"	}									\n" +
//		"}										\n";
//		
//	String selectionStartBehind = "new G().";
//	String selectionEndBehind = "new G().super";
//	
//	String expectedCompletionNodeToString = "<SelectOnExplicitConstructorCall:new G().super()>;";
//	
//	String completionIdentifier = "super";
//	String expectedUnitDisplayString =
//		"public class G {\n" + 
//		"  class M {\n" + 
//		"    M() {\n" + 
//		"    }\n" + 
//		"  }\n" + 
//		"  public G() {\n" + 
//		"  }\n" + 
//		"  static Object foo() {\n" + 
//		"    class X extends M {\n" + 
//		"      X() {\n" + 
//		"        <SelectOnExplicitConstructorCall:new G().super()>;\n" + 
//		"      }\n" + 
//		"    }\n" + 
//		"  }\n" + 
//		"}\n";	
//	String expectedReplacedSource = "new G().super()";
//	String testName = "<select qualified super constructor call>";
//
//	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
//	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName); 
//}
/*
 * Select qualified super constructor call with arguments
 */
//public void test29() {
//
//	String str =
//		"public class G {								\n" +
//		"	class M {}									\n" +
//		"	static Object foo() {						\n" +
//		"		class X extends M {						\n" +
//		"			X (){								\n" +
//		"				new G().super(23 + \"hello\");	\n" +
//		"			}									\n" +
//		"		}										\n" +
//		"	}											\n" +
//		"}												\n";
//		
//	String selectionStartBehind = "new G().";
//	String selectionEndBehind = "new G().super";
//	
//	String expectedCompletionNodeToString = "<SelectOnExplicitConstructorCall:new G().super((23 + \"hello\"))>;";
//	
//	String completionIdentifier = "super";
//	String expectedUnitDisplayString =
//		"public class G {\n" + 
//		"  class M {\n" + 
//		"    M() {\n" + 
//		"    }\n" + 
//		"  }\n" + 
//		"  public G() {\n" + 
//		"  }\n" + 
//		"  static Object foo() {\n" + 
//		"    class X extends M {\n" + 
//		"      X() {\n" + 
//		"        <SelectOnExplicitConstructorCall:new G().super((23 + \"hello\"))>;\n" + 
//		"      }\n" + 
//		"    }\n" + 
//		"  }\n" + 
//		"}\n";	
//	String expectedReplacedSource = "new G().super(23 + \"hello\")";
//	String testName = "<select qualified super constructor call with arguments>";
//
//	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
//	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName); 
//}
/*
 * Select super constructor call with arguments
 */
//public void test30() {
//
//	String str =
//		"public class G {					\n" +
//		"	G() {							\n" +
//		"		super(new G());				\n" +
//		"	}								\n" +
//		"}									\n";
//		
//	String selectionStartBehind = "\n\t\t";
//	String selectionEndBehind = "super";
//	
//	String expectedCompletionNodeToString = "<SelectOnExplicitConstructorCall:super(new G())>;";
//	
//	String completionIdentifier = "super";
//	String expectedUnitDisplayString =
//		"public class G {\n" + 
//		"  G() {\n" + 
//		"    <SelectOnExplicitConstructorCall:super(new G())>;\n" + 
//		"  }\n" + 
//		"}\n";
//		
//	String expectedReplacedSource = "super(new G())";
//	String testName = "<select super constructor call with arguments>";
//
//	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
//	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName); 
//}
/*
 * Regression test for 1FVQ0LK
 */
//public void test31() {
//
//	String str =
//		"class X {							\n" +
//		"	Y f;							\n" +
//		"	void foo() {					\n" +
//		"		new Bar(fred());			\n" +
//		"		Z z= new Z();				\n" +
//		"	}								\n" +
//		"}									\n";
//		
//	String selectionStartBehind = "\n\t";
//	String selectionEndBehind = "Y";
//	
//	String expectedCompletionNodeToString = "<SelectOnType:Y>";
//	
//	String completionIdentifier = "Y";
//	String expectedUnitDisplayString =
//		"class X {\n" +
//		"  <SelectOnType:Y> f;\n" +
//		"  X() {\n" +
//		"  }\n" +
//		"  void foo() {\n" +
//		"  }\n" +
//		"}\n";
//		
//	String expectedReplacedSource = "Y";
//	String testName = "<regression test for 1FVQ0LK>";
//
//	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
//	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
//		
//	this.checkDietParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName); 
//}
/*
 * Regression test for 1FWT4AJ: ITPCOM:WIN98 - SelectionParser produces duplicate type declaration
 */
public void test32() {

	String str =
		"function containsPhrase(){							\n"+
		"				var currentChar = \"hello\".toLowerCase()		\n"+
		"}																\n";
		
	String selectionStartBehind = "\"hello\".";
	String selectionEndBehind = "\"hello\".toLowerCase";
	
	String expectedCompletionNodeToString = "<SelectOnMessageSend:\"hello\".toLowerCase()>";
	
	String completionIdentifier = "toLowerCase";
	String expectedUnitDisplayString =
		"function containsPhrase() {\n" + 
		"  var currentChar = <SelectOnMessageSend:\"hello\".toLowerCase()>;\n" + 
		"}\n";
		
	String expectedReplacedSource = "toLowerCase()";
	String testName = "<1FWT4AJ: ITPCOM:WIN98 - SelectionParser produces duplicate type declaration>";

	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
		
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName); 
}
/*
 * Regression test for 1G4CLZM: ITPJUI:WINNT - 'Import Selection' - Set not found
 */
//public void test33() {
//
//	String str =
//		"	import java.util.AbstractMap;				\n"+
//		"	public class c4 extends AbstractMap {		\n"+
//		"		/**										\n"+
//		"		 * @see AbstractMap#entrySet			\n"+
//		"		 */										\n"+
//		"		public Set entrySet() {					\n"+
//		"			return null;						\n"+
//		"		}										\n"+
//		"	}											\n";
//		
//	String selectionStartBehind = "\n\t\tpublic ";
//	String selectionEndBehind = "public Set";
//	
//	String expectedCompletionNodeToString = "<SelectOnType:Set>";
//	
//	String completionIdentifier = "Set";
//	String expectedUnitDisplayString =
//		"import java.util.AbstractMap;\n" + 
//		"public class c4 extends AbstractMap {\n" + 
//		"  public c4() {\n" + 
//		"  }\n" + 
//		"  public <SelectOnType:Set> entrySet() {\n" + 
//		"  }\n" + 
//		"}\n";
//		
//	String expectedReplacedSource = "Set";
//	String testName = "<1G4CLZM: ITPJUI:WINNT - 'Import Selection' - Set not found>";
//
//	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
//	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
//	
//	this.checkDietParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName); 
//}
/*
 * Regression test for 1GB99S3: ITPJUI:WINNT - SH: NPE in editor while getting hover help
 */
public void test34() {

	String str =
		"function foo() {						\n"+
		"  var array = new Object();		\n"+
		"		return array.length;				\n"+
		"}											\n";
		
	String selectionStartBehind = "\n\t\treturn ";
	String selectionEndBehind = "array.length";
	
	String expectedCompletionNodeToString = NONE;
	
	String completionIdentifier = NONE;
	String expectedUnitDisplayString =
		"function foo() {\n" + 
		"  var array = new   Object();\n" + 
		"  return array.length;\n" + 
		"}\n";
		
	String expectedReplacedSource = NONE;
	String testName = "<1GB99S3: ITPJUI:WINNT - SH: NPE in editor while getting hover help>";

	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
	
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName); 
}

/*
 * Select this constructor call
 */
//public void test35() {
//
//	String str =
//		"public class G {					\n" +
//		"	G() {							\n" +
//		"	}								\n" +
//		"	G(int x) {						\n" +
//		"		this();						\n" +
//		"	}								\n" +
//		"}									\n";
//		
//	String selectionStartBehind = "\n\t\t";
//	String selectionEndBehind = "this";
//	
//	String expectedCompletionNodeToString = "<SelectOnExplicitConstructorCall:this()>;";
//	
//	String completionIdentifier = "this";
//	String expectedUnitDisplayString =
//		"public class G {\n" + 
//		"  G() {\n" +
//		"  }\n" +
//		"  G(int x) {\n" + 
//		"    <SelectOnExplicitConstructorCall:this()>;\n" + 
//		"  }\n" +
//		"}\n";
//		
//	String expectedReplacedSource = "this()";
//	String testName = "<select this constructor call>";
//
//	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
//	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName); 
//}
//
///*
// * Select qualified this constructor call
// */
//public void test36() {
//
//	String str =
//		"public class G {						\n" +
//		"	static Object foo() {				\n" +
//		"		class X {						\n" +
//		"			X (){						\n" +
//		"			}							\n" +
//		"			X (int x){					\n" +
//		"				new G().this();			\n" +
//		"			}							\n" +
//		"		}								\n" +
//		"	}									\n" +
//		"}										\n";
//		
//	String selectionStartBehind = "new G().";
//	String selectionEndBehind = "new G().this";
//	
//	String expectedCompletionNodeToString = "<SelectOnExplicitConstructorCall:new G().this()>;";
//	
//	String completionIdentifier = "this";
//	String expectedUnitDisplayString =
//		"public class G {\n" + 
//		"  public G() {\n" + 
//		"  }\n" + 
//		"  static Object foo() {\n" + 
//		"    class X {\n" + 
//		"      X() {\n" +
//		"        super();\n"+
//		"      }\n" + 
//		"      X(int x) {\n" + 
//		"        <SelectOnExplicitConstructorCall:new G().this()>;\n" + 
//		"      }\n" + 
//		"    }\n" + 
//		"  }\n" + 
//		"}\n";	
//	String expectedReplacedSource = "new G().this()";
//	String testName = "<select qualified this constructor call>";
//
//	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
//	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName); 
//}
///*
// * Select qualified this constructor call with arguments
// */
//public void test37() {
//
//	String str =
//		"public class G {								\n" +
//		"	static Object foo() {						\n" +
//		"		class X {								\n" +
//		"			X (){								\n" +
//		"			}									\n" +
//		"			X (int x){							\n" +
//		"				new G().this(23 + \"hello\");	\n" +
//		"			}									\n" +
//		"		}										\n" +
//		"	}											\n" +
//		"}												\n";
//		
//	String selectionStartBehind = "new G().";
//	String selectionEndBehind = "new G().this";
//	
//	String expectedCompletionNodeToString = "<SelectOnExplicitConstructorCall:new G().this((23 + \"hello\"))>;";
//	
//	String completionIdentifier = "this";
//	String expectedUnitDisplayString =
//		"public class G {\n" + 
//		"  public G() {\n" + 
//		"  }\n" + 
//		"  static Object foo() {\n" + 
//		"    class X {\n" + 
//		"      X() {\n" + 
//		"        super();\n"+
//		"      }\n" + 
//		"      X(int x) {\n" + 
//		"        <SelectOnExplicitConstructorCall:new G().this((23 + \"hello\"))>;\n" + 
//		"      }\n" + 
//		"    }\n" + 
//		"  }\n" + 
//		"}\n";	
//	String expectedReplacedSource = "new G().this(23 + \"hello\")";
//	String testName = "<select qualified this constructor call with arguments>";
//
//	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
//	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName); 
//}
///*
// * Select this constructor call with arguments
// */
//public void test38() {
//
//	String str =
//		"public class G {					\n" +
//		"	G() {							\n" +
//		"		this(new G());				\n" +
//		"	}								\n" +
//		"}									\n";
//		
//	String selectionStartBehind = "\n\t\t";
//	String selectionEndBehind = "this";
//	
//	String expectedCompletionNodeToString = "<SelectOnExplicitConstructorCall:this(new G())>;";
//	
//	String completionIdentifier = "this";
//	String expectedUnitDisplayString =
//		"public class G {\n" + 
//		"  G() {\n" + 
//		"    <SelectOnExplicitConstructorCall:this(new G())>;\n" + 
//		"  }\n" + 
//		"}\n";
//		
//	String expectedReplacedSource = "this(new G())";
//	String testName = "<select this constructor call with arguments>";
//
//	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
//	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName); 
//}
///*
// * bugs 3293 search does not work in inner class (1GEUQHJ)
// */
//public void test39() {
//
//	String str =
//		"public class X {                \n" +
//		"  Object hello = new Object(){  \n" +
//		"    public void foo(String s){  \n" +
//		"      s.length();               \n" +
//		"    }                           \n" +
//		"  };                            \n" +
//		"}								 \n";
//		
//	String selectionStartBehind = "s.";
//	String selectionEndBehind = "length";
//	
//	String expectedCompletionNodeToString = "<SelectOnMessageSend:s.length()>";
//	
//	String completionIdentifier = "length";
//	String expectedUnitDisplayString =
//		"public class X {\n" +
//		"  Object hello = new Object() {\n" +
//		"    public void foo(String s) {\n" +
//		"      <SelectOnMessageSend:s.length()>;\n" +
//		"    }\n" +
//		"  };\n" +
//		"  public X() {\n" +
//		"  }\n" +
//		"}\n";
//	String expectedReplacedSource = "s.length()";
//	String testName = "<select message send in anonymous class>";
//
//	int selectionStart = str.indexOf(selectionStartBehind) + selectionStartBehind.length();
//	int selectionEnd = str.indexOf(selectionEndBehind) + selectionEndBehind.length() - 1;
//		
//	this.checkDietParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName);
//}

///*
// * bugs 3229 OpenOnSelection - strange behaviour of code resolve (1GAVL08)
// */
//public void test40() {
//
//	String str =
//		"public class X {                \n" +
//		"  Object                        \n" +
//		"}								 \n";
//		
//	String selection = "Object";
//	
//	String expectedCompletionNodeToString = "<SelectOnType:Object>";
//	
//	String completionIdentifier = "Object";
//	String expectedUnitDisplayString =
//		"public class X {\n" + 
//		"  <SelectOnType:Object>;\n" + 
//		"  public X() {\n" + 
//		"  }\n" + 
//		"}\n";
//	String expectedReplacedSource = "Object";
//	String testName = "<select fake field>";
//
//	int selectionStart = str.indexOf(selection);
//	int selectionEnd = str.indexOf(selection) + selection.length() - 1;
//		
//	this.checkDietParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName);
//}
/*
 * bugs 11475 selection on local name.
 */
public void test41() {

	String str =
		"function foo(){                   \n" +
		"    var vari;              \n" +
		"}								 \n";
		
	String selection = "vari";
	
	String expectedCompletionNodeToString = "<SelectionOnLocalName:vari>;";
	
	String completionIdentifier = "vari";
	String expectedUnitDisplayString =
		"function foo() {\n" + 
		"  <SelectionOnLocalName:vari>;\n" + 
		"}\n";
	String expectedReplacedSource = "vari";
	String testName = "<select local name>";

	int selectionStart = str.indexOf(selection);
	int selectionEnd = str.indexOf(selection) + selection.length() - 1;
		
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName);
}
/*
 * bugs 11475 selection on argument name.
 */
public void test42() {

	String str =
		" function foo(vari){          \n" +
		"}								 \n";
		
	String selection = "vari";
	
	String expectedCompletionNodeToString = "<SelectionOnArgumentName:vari>";
	
	String completionIdentifier = "vari";
	String expectedUnitDisplayString =
		"function foo(<SelectionOnArgumentName:vari>) {\n" + 
		"}\n";
	String expectedReplacedSource = "vari";
	String testName = "<select argument name>";

	int selectionStart = str.indexOf(selection);
	int selectionEnd = str.indexOf(selection) + selection.length() - 1;
		
	this.checkDietParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName);
}
///*
// * bugs 11475 selection on argument name inside catch statement.
// */
//public void test43() {
//
//	String str =
//		"public class X {                \n" +
//		"  public void foo(){                   \n" +
//		"    try{              \n" +
//		"    }catch(Object var){}\n" +
//		"  }                             \n" +
//		"}								 \n";
//		
//	String selection = "var";
//	
//	String expectedCompletionNodeToString = "<SelectionOnArgumentName:Object var>";
//	
//	String completionIdentifier = "var";
//	String expectedUnitDisplayString =
//		"public class X {\n" + 
//		"  public X() {\n" + 
//		"  }\n" + 
//		"  public void foo() {\n" + 
//		"    <SelectionOnArgumentName:Object var>;\n" + 
//		"  }\n" + 
//		"}\n";
//	String expectedReplacedSource = "var";
//	String testName = "<select argument name inside catch statement>";
//
//	int selectionStart = str.indexOf(selection);
//	int selectionEnd = str.indexOf(selection) + selection.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName);
//}
/*
 * bugs 15430
 */
//public void test44() {
//
//	String str =
//		"public class X {                \n" +
//		"  String x = super.foo()  \n" +
//		"}								 \n";
//		
//	String selection = "super";
//	
//	String expectedCompletionNodeToString = "<SelectOnSuper:super>";
//	
//	String completionIdentifier = "super";
//	String expectedUnitDisplayString =
//		"public class X {\n" + 
//		"  String x = <SelectOnSuper:super>;\n" + 
//		"  public X() {\n" + 
//		"  }\n" + 
//		"}\n";
//	String expectedReplacedSource = "super";
//	String testName = "<select super in field initializer>";
//
//	int selectionStart = str.indexOf(selection);
//	int selectionEnd = str.indexOf(selection) + selection.length() - 1;
//		
//	this.checkDietParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName);
//}
/*
 * bugs 14468
 */
//public void test45() {
//
//	String str =
//		"public class X {                \n" +
//		"  void foo() {\n" +
//		"    if(x instanceof Object){\n" +
//		"    }\n" +
//		"  }  \n" +
//		"}								 \n";
//		
//	String selection = "Object";
//	
//	String expectedCompletionNodeToString = "<SelectOnType:Object>";
//	
//	String completionIdentifier = "Object";
//	String expectedUnitDisplayString =
//		"public class X {\n"+
//		"  public X() {\n"+
//		"  }\n"+
//		"  void foo() {\n"+
//		"    <SelectOnType:Object>;\n"+
//		"  }\n"+
//		"}\n";
//	String expectedReplacedSource = "Object";
//	String testName = "<select inside instanceof statement>";
//
//	int selectionStart = str.indexOf(selection);
//	int selectionEnd = str.indexOf(selection) + selection.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName);
//}

/*
 * bugs 14468
 */
//public void test46() {
//
//	String str =
//		"public class X {                \n" +
//		"  void foo() {\n" +
//		"    y = x instanceof Object;\n" +
//		"  }  \n" +
//		"}								 \n";
//		
//	String selection = "Object";
//	
//	String expectedCompletionNodeToString = "<SelectOnType:Object>";
//	
//	String completionIdentifier = "Object";
//	String expectedUnitDisplayString =
//		"public class X {\n"+
//		"  public X() {\n"+
//		"  }\n"+
//		"  void foo() {\n"+
//		"    <SelectOnType:Object>;\n"+
//		"  }\n"+
//		"}\n";
//	String expectedReplacedSource = "Object";
//	String testName = "<select inside instanceof statement>";
//
//	int selectionStart = str.indexOf(selection);
//	int selectionEnd = str.indexOf(selection) + selection.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName);
//}
/*
 * bugs 14468
 */
//public void test47() {
//
//	String str =
//		"public class X {                \n" +
//		"  void foo() {\n" +
//		"   boolean y = x instanceof Object;\n" +
//		"  }  \n" +
//		"}								 \n";
//		
//	String selection = "Object";
//	
//	String expectedCompletionNodeToString = "<SelectOnType:Object>";
//	
//	String completionIdentifier = "Object";
//	String expectedUnitDisplayString =
//		"public class X {\n"+
//		"  public X() {\n"+
//		"  }\n"+
//		"  void foo() {\n"+
//		"    boolean y = <SelectOnType:Object>;\n"+
//		"  }\n"+
//		"}\n";
//	String expectedReplacedSource = "Object";
//	String testName = "<select inside instanceof statement>";
//
//	int selectionStart = str.indexOf(selection);
//	int selectionEnd = str.indexOf(selection) + selection.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName);
//}
/*
 * bugs 14468
 */
//public void test48() {
//
//	String str =
//		"public class X {                \n" +
//		"  boolean y = x instanceof Object;\n" +
//		"}								 \n";
//		
//	String selection = "Object";
//	
//	String expectedCompletionNodeToString = "<SelectOnType:Object>";
//	
//	String completionIdentifier = "Object";
//	String expectedUnitDisplayString =
//		"public class X {\n"+
//		"  boolean y = <SelectOnType:Object>;\n"+
//		"  public X() {\n"+
//		"  }\n"+
//		"}\n";
//	String expectedReplacedSource = "Object";
//	String testName = "<select inside instanceof statement>";
//
//	int selectionStart = str.indexOf(selection);
//	int selectionEnd = str.indexOf(selection) + selection.length() - 1;
//		
//	this.checkDietParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName);
//}
/*
 * bugs 28064
 */
public void test49() {

	String str =
		"var x = new X();\n" +
		"								 \n";
		
	String selection = "X";
	
	String expectedCompletionNodeToString = "<SelectOnAllocationExpression:new <SelectOnName:X>()>" 
											 ;
	
	String completionIdentifier = "X";
	String expectedUnitDisplayString =
		"var x = <SelectOnAllocationExpression:new <SelectOnName:X>()>;\n";
	String expectedReplacedSource = NONE;
	String testName = "<select anonymous type>";

	int selectionStart = str.lastIndexOf(selection);
	int selectionEnd = str.lastIndexOf(selection) + selection.length() - 1;
		
	this.checkDietParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName);
}
/*
 * bugs https://bugs.eclipse.org/bugs/show_bug.cgi?id=52422
 */
//public void test50() {
//
//	String str =
//		"  void foo() {\n" +
//		"    new Object(){\n" +
//		"      void bar(){\n" +
//		"        bar2();\n" +
//		"      }\n" +
//		"      void bar2() {\n" +
//		"      }\n" +
//		"    }\n" +
//		"}								 \n";
//		
//	String selection = "bar2";
//	
//	String expectedCompletionNodeToString = "<SelectOnMessageSend:bar2()>";
//	
//	String completionIdentifier = "bar2";
//	String expectedUnitDisplayString =
//		"public class X {\n" + 
//		"  public X() {\n" + 
//		"  }\n" + 
//		"  void foo() {\n" + 
//		"    new Object() {\n" + 
//		"      () {\n" + 
//		"      }\n" + 
//		"      void bar() {\n" + 
//		"        <SelectOnMessageSend:bar2()>;\n" + 
//		"      }\n" + 
//		"      void bar2() {\n" + 
//		"      }\n" + 
//		"    };\n" + 
//		"  }\n" + 
//		"}\n";
//	String expectedReplacedSource = "bar2()";
//	String testName = "<select inside anonymous type>";
//
//	int selectionStart = str.indexOf(selection);
//	int selectionEnd = str.indexOf(selection) + selection.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName);
//}
/*
 * bugs https://bugs.eclipse.org/bugs/show_bug.cgi?id=52422
 */
//public void test51() {
//
//	String str =
//		"public class X {                \n" +
//		"  void foo() {\n" +
//		"    new Object(){\n" +
//		"      void foo0(){\n" +
//		"        new Object(){\n" +
//		"          void bar(){\n" +
//		"            bar2();\n" +
//		"          }\n" +
//		"          void bar2() {\n" +
//		"          }\n" +
//		"        }\n" +
//		"      }\n" +
//		"    }\n" +
//		"  }\n" +
//		"}								 \n";
//		
//	String selection = "bar2";
//	
//	String expectedCompletionNodeToString = "<SelectOnMessageSend:bar2()>";
//	
//	String completionIdentifier = "bar2";
//	String expectedUnitDisplayString =
//		"public class X {\n" + 
//		"  public X() {\n" + 
//		"  }\n" + 
//		"  void foo() {\n" + 
//		"    new Object() {\n" + 
//		"      () {\n" + 
//		"      }\n" + 
//		"      void foo0() {\n" + 
//		"        new Object() {\n" + 
//		"          () {\n" + 
//		"          }\n" + 
//		"          void bar() {\n" + 
//		"            <SelectOnMessageSend:bar2()>;\n" + 
//		"          }\n" + 
//		"          void bar2() {\n" + 
//		"          }\n" + 
//		"        };\n" + 
//		"      }\n" + 
//		"    };\n" + 
//		"  }\n" + 
//		"}\n";
//	String expectedReplacedSource = "bar2()";
//	String testName = "<select inside anonymous type>";
//
//	int selectionStart = str.indexOf(selection);
//	int selectionEnd = str.indexOf(selection) + selection.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName);
//}
/*
 * bugs https://bugs.eclipse.org/bugs/show_bug.cgi?id=52422
 */
//public void test52() {
//
//	String str =
//		"public class X {                \n" +
//		"  void foo() {\n" +
//		"    new Object(){\n" +
//		"      void foo0(){\n" +
//		"        new Object(){\n" +
//		"          void bar(){\n" +
//		"            bar2();\n" +
//		"          }\n" +
//		
//		"        }\n" +
//		"      }\n" +
//		"      void bar2() {\n" +
//		"      }\n" +
//		"    }\n" +
//		"  }\n" +
//		"}								 \n";
//		
//	String selection = "bar2";
//	
//	String expectedCompletionNodeToString = "<SelectOnMessageSend:bar2()>";
//	
//	String completionIdentifier = "bar2";
//	String expectedUnitDisplayString =
//		"public class X {\n" + 
//		"  public X() {\n" + 
//		"  }\n" + 
//		"  void foo() {\n" + 
//		"    new Object() {\n" + 
//		"      () {\n" + 
//		"      }\n" + 
//		"      void foo0() {\n" + 
//		"        new Object() {\n" + 
//		"          () {\n" + 
//		"          }\n" + 
//		"          void bar() {\n" + 
//		"            <SelectOnMessageSend:bar2()>;\n" + 
//		"          }\n" + 
//		"        };\n" + 
//		"      }\n" + 
//		"      void bar2() {\n" + 
//		"      }\n" + 
//		"    };\n" + 
//		"  }\n" + 
//		"}\n";
//	String expectedReplacedSource = "bar2()";
//	String testName = "<select inside anonymous type>";
//
//	int selectionStart = str.indexOf(selection);
//	int selectionEnd = str.indexOf(selection) + selection.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName);
//}
//public void test53() {
//
//	String str =
//		"public class X {                \n" +
//		"  void foo(String[] stringArray) {\n" +
//		"    for(String string2 : stringArray);\n" +
//		"  }\n" +
//		"}								 \n";
//		
//	String selection = "string2";
//	
//	String expectedCompletionNodeToString = "<SelectionOnLocalName:String string2>;";
//	
//	String completionIdentifier = "string2";
//	String expectedUnitDisplayString =
//		"public class X {\n" + 
//		"  public X() {\n" + 
//		"  }\n" + 
//		"  void foo(String[] stringArray) {\n" + 
//		"    for (<SelectionOnLocalName:String string2> : stringArray) \n" + 
//		"      ;\n" + 
//		"  }\n" + 
//		"}\n";
//	String expectedReplacedSource = "string2";
//	String testName = "<select>";
//
//	int selectionStart = str.indexOf(selection);
//	int selectionEnd = str.indexOf(selection) + selection.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName);
//}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=84001
//public void test54() {
//
//	String str =
//		"public class X {                \n" +
//		"  void foo() {\n" +
//		"    new Test.Sub();\n" +
//		"  }\n" +
//		"}								 \n";
//		
//	String selection = "Test";
//	
//	String expectedCompletionNodeToString = "<SelectOnType:Test>";
//	
//	String completionIdentifier = "Test";
//	String expectedUnitDisplayString =
//		"public class X {\n" + 
//		"  public X() {\n" + 
//		"  }\n" + 
//		"  void foo() {\n" + 
//		"    new <SelectOnType:Test>();\n" + 
//		"  }\n" + 
//		"}\n";
//	String expectedReplacedSource = "Test";
//	String testName = "<select>";
//
//	int selectionStart = str.indexOf(selection);
//	int selectionEnd = str.indexOf(selection) + selection.length() - 1;
//		
//	this.checkMethodParse(
//		str.toCharArray(), 
//		selectionStart,
//		selectionEnd,
//		expectedCompletionNodeToString,
//		expectedUnitDisplayString,
//		completionIdentifier,
//		expectedReplacedSource,
//		testName);
//}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=84001
public void test55() {

	String str =
		"function foo() {\n" +
		"    new Test.Sub();\n" +
		"}								 \n";
		
	String selection = "Sub";
	
	String expectedCompletionNodeToString = "<SelectionOnFieldReference:Test.Sub>";
	
	String completionIdentifier = "Sub";
	String expectedUnitDisplayString =
		"function foo() {\n" + 
		"  new   <SelectionOnFieldReference:Test.Sub>();\n" + 
		"}\n";
	String expectedReplacedSource = "Sub";
	String testName = "<select>";

	int selectionStart = str.indexOf(selection);
	int selectionEnd = str.indexOf(selection) + selection.length() - 1;
		
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName);
}


static final String RESOLVE_LOCAL_NAME=
	"function foo(){\n" +
	"var var1 = new Object();\n" +
	"var var2 = 1;\n" +
	"var1.toString();\n" +
	"var2++;\n" +
	"if (var2 == 3) {\n" +
	"	var var3 = var1;\n" +
	"	var3.hashCode();\n" +
	"} else {\n" +
	"	var var3 = new Object();\n" +
	"	var3.toString();\n" +
	"}\n" +
	"var var4 = 1;\n" +
	"\n" +
	"}\n" ;

public void test56() {

	String str =RESOLVE_LOCAL_NAME;
		
	String startString = "var1.toString();";
	String selection = "var1";
	
	String expectedCompletionNodeToString = "<SelectOnName:var1>";
	
	String completionIdentifier = "var1";
	String expectedUnitDisplayString =
		"function foo() {\n" + 
		"  var var1 = new   Object();\n" + 
		"  var var2 = 1;\n" + 
		"  <SelectOnName:var1>.toString();\n" + 
		"  var2 ++;\n" + 
		"  if ((var2 == 3))\n" + 
		"      {\n" +
		"        var var3 = var1;\n" + 
		"        var3.hashCode();\n" +
		"      }\n" + 
		"  else\n" +
		"      {\n" +
		"        var var3 = new         Object();\n" + 
		"        var3.toString();\n" +
		"      }\n" + 
		"  var var4 = 1;\n" +
		"}\n";
	
	String expectedReplacedSource = "var1";
	String testName = "<select>";

	int selectionStart = str.indexOf(startString);
	int selectionEnd = selectionStart + selection.length() - 1;
		
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName);
}



public void test57() {

	String str ="YAHOO.widget.Slider = function (sElementId, sGroup, oThumb, sType) {\n"+
				"if (sElementId) {\n"+
				"}\n"+
				"};\n";
		
	String startString = "sElementId)";
	String selection = "sElementId";
	
	String expectedCompletionNodeToString = "<SelectOnName:sElementId>";
	
	String completionIdentifier = "sElementId";
	String expectedUnitDisplayString  =
		  "YAHOO.widget.Slider = function (sElementId, sGroup, oThumb, sType) {\n"+
			"  if (<SelectOnName:sElementId>)\n      {\n"+
			"      }\n"+
			"};\n";
	
	
	String expectedReplacedSource = "sElementId";
	String testName = "<select>";

	int selectionStart = str.indexOf(startString);
	int selectionEnd = selectionStart + selection.length() - 1;
		
	this.checkMethodParse(
		str.toCharArray(), 
		selectionStart,
		selectionEnd,
		expectedCompletionNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		testName);
}




}
