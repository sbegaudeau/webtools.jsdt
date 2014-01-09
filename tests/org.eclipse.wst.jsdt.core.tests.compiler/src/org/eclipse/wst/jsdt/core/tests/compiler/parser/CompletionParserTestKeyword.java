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

public class CompletionParserTestKeyword extends AbstractCompletionTest {
public CompletionParserTestKeyword(String testName) {
	super(testName);
}
/*
 * Test for 'break' keyword.
 */
public void test0023(){
	String str =
		"function foo(){\n" +
		"  bre\n" +
		"}\n";

	String completeBehind = "bre";
	int cursorLocation = str.lastIndexOf("bre") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"function foo() {\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:bre>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "bre";
	expectedReplacedSource = "bre";
	expectedUnitDisplayString =
		"function foo() {\n" +
		"  <CompleteOnName:bre>;\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'break' keyword.
 */
public void test0024(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    for(int i; i < 10; i++) {\n" +
		"      bre\n" +
		"    }\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "bre";
	int cursorLocation = str.lastIndexOf("bre") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:bre>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "bre";
	expectedReplacedSource = "bre";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    int i;\n" +
		"    {\n" +
		"      <CompleteOnName:bre>;\n" +
		"    }\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'case' keyword.
 */
public void test0025(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    cas\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "cas";
	int cursorLocation = str.lastIndexOf("cas") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:cas>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "cas";
	expectedReplacedSource = "cas";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:cas>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'case' keyword.
 */
public void test0026(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    switch(0) {\n" +
		"      cas\n" +
		"    }\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "cas";
	int cursorLocation = str.lastIndexOf("cas") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnKeyword:cas>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "cas";
	expectedReplacedSource = "cas";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnKeyword:cas>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'catch' keyword.
 */
public void test0027(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"     cat\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "cat";
	int cursorLocation = str.lastIndexOf("cat") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:cat>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "cat";
	expectedReplacedSource = "cat";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:cat>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'catch' keyword.
 */
public void test0028(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    try {\n" +
		"    } cat\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "cat";
	int cursorLocation = str.lastIndexOf("cat") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnKeyword:cat>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "cat";
	expectedReplacedSource = "cat";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnKeyword:cat>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'continue' keyword.
 */
public void test0040(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"     con\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "con";
	int cursorLocation = str.lastIndexOf("con") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:con>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "con";
	expectedReplacedSource = "con";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:con>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'continue' keyword.
 */
public void test0041(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"     for(int i; i < 5; i++) {\n" +
		"       con\n" +
		"     }\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "con";
	int cursorLocation = str.lastIndexOf("con") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:con>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "con";
	expectedReplacedSource = "con";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    int i;\n" +
		"    {\n" +
		"      <CompleteOnName:con>;\n" +
		"    }\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'default' keyword.
 */
public void test0042(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"     def\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "def";
	int cursorLocation = str.lastIndexOf("def") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:def>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "def";
	expectedReplacedSource = "def";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:def>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'default' keyword.
 */
public void test0043(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"     switch(0) {\n" +
		"       case 1 : break;\n" +
		"       def\n" +
		"     }\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "def";
	int cursorLocation = str.lastIndexOf("def") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:def>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "def";
	expectedReplacedSource = "def";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    {\n" +
		"      <CompleteOnName:def>;\n" +
		"    }\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'do' keyword.
 */
public void test0044(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"     do\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "do";
	int cursorLocation = str.lastIndexOf("do") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:do>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "do";
	expectedReplacedSource = "do";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:do>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'else' keyword.
 */
public void test0045(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"     els\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "els";
	int cursorLocation = str.lastIndexOf("els") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:els>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "els";
	expectedReplacedSource = "els";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:els>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'else' keyword.
 */
public void test0046(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"     if(true) {\n" +
		"     } els\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "els";
	int cursorLocation = str.lastIndexOf("els") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:els>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "els";
	expectedReplacedSource = "els";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:els>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'finally' keyword.
 */
public void test0055(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"     fin" +
		"  }\n" +
		"}\n";

	String completeBehind = "fin";
	int cursorLocation = str.lastIndexOf("fin") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:fin>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "fin";
	expectedReplacedSource = "fin";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:fin>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'finally' keyword.
 */
public void test0056(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"     try {" +
		"     } fin" +
		"  }\n" +
		"}\n";

	String completeBehind = "fin";
	int cursorLocation = str.lastIndexOf("fin") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnKeyword:fin>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "fin";
	expectedReplacedSource = "fin";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnKeyword:fin>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'for' keyword.
 */
public void test0057(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"     for" +
		"  }\n" +
		"}\n";

	String completeBehind = "for";
	int cursorLocation = str.lastIndexOf("for") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:for>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "for";
	expectedReplacedSource = "for";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:for>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'if' keyword.
 */
public void test0058(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"     if" +
		"  }\n" +
		"}\n";

	String completeBehind = "if";
	int cursorLocation = str.lastIndexOf("if") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:if>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "if";
	expectedReplacedSource = "if";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:if>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'switch' keyword.
 */
public void test0059(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"     swi" +
		"  }\n" +
		"}\n";

	String completeBehind = "swi";
	int cursorLocation = str.lastIndexOf("swi") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:swi>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "swi";
	expectedReplacedSource = "swi";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:swi>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'return' keyword.
 */
public void test0090(){
	String str =
		"public class X {\n" +
		"  int foo() {\n" +
		"    ret\n" +
		"  }\n" +
		"}";

	String completeBehind = "ret";
	int cursorLocation = str.lastIndexOf("ret") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  int foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:ret>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "ret";
	expectedReplacedSource = "ret";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  int foo() {\n" +
		"    <CompleteOnName:ret>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'throw' keyword.
 */
public void test0091(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    thr\n" +
		"  }\n" +
		"}";

	String completeBehind = "thr";
	int cursorLocation = str.lastIndexOf("thr") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:thr>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "thr";
	expectedReplacedSource = "thr";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:thr>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'try' keyword.
 */
public void test0092(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    try\n" +
		"  }\n" +
		"}";

	String completeBehind = "try";
	int cursorLocation = str.lastIndexOf("try") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:try>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "try";
	expectedReplacedSource = "try";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:try>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'try' keyword.
 */
public void test0093(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    if(try\n" +
		"  }\n" +
		"}";

	String completeBehind = "try";
	int cursorLocation = str.lastIndexOf("try") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:try>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "try";
	expectedReplacedSource = "try";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:try>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'do' keyword.
 */
public void test0094(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    if(do\n" +
		"  }\n" +
		"}";

	String completeBehind = "do";
	int cursorLocation = str.lastIndexOf("do") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:do>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "do";
	expectedReplacedSource = "do";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:do>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'for' keyword.
 */
public void test0095(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    if(for\n" +
		"  }\n" +
		"}";

	String completeBehind = "for";
	int cursorLocation = str.lastIndexOf("for") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:for>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "for";
	expectedReplacedSource = "for";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:for>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'if' keyword.
 */
public void test0096(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    if(if\n" +
		"  }\n" +
		"}";

	String completeBehind = "if";
	int cursorLocation = str.lastIndexOf("if") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:if>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "if";
	expectedReplacedSource = "if";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:if>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'switch' keyword.
 */
public void test0097(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    if(swi\n" +
		"  }\n" +
		"}";

	String completeBehind = "swi";
	int cursorLocation = str.lastIndexOf("swi") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:swi>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "swi";
	expectedReplacedSource = "swi";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:swi>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'new' keyword.
 */
public void test0098(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    new\n" +
		"  }\n" +
		"}";

	String completeBehind = "new";
	int cursorLocation = str.lastIndexOf("new") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:new>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "new";
	expectedReplacedSource = "new";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:new>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'new' keyword.
 */
public void test0099(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    new X\n" +
		"  }\n" +
		"}";

	String completeBehind = "new";
	int cursorLocation = str.lastIndexOf("new") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:new>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "new";
	expectedReplacedSource = "new";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:new>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'new' keyword.
 */
public void test0100(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    new X()\n" +
		"  }\n" +
		"}";

	String completeBehind = "new";
	int cursorLocation = str.lastIndexOf("new") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:new>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "new";
	expectedReplacedSource = "new";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:new>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'while' keyword.
 */
public void test0107(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    whi\n" +
		"  }\n" +
		"}";

	String completeBehind = "whi";
	int cursorLocation = str.lastIndexOf("whi") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:whi>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "whi";
	expectedReplacedSource = "whi";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:whi>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'while' keyword.
 */
public void test0108(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    if(whi\n" +
		"  }\n" +
		"}";

	String completeBehind = "whi";
	int cursorLocation = str.lastIndexOf("whi") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:whi>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "whi";
	expectedReplacedSource = "whi";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:whi>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'this' keyword.
 */
public void test0182(){
	String str =
		"public class X {\n" +
		"  void foo(){\n" +
		"     thi\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "thi";
	int cursorLocation = str.lastIndexOf("thi") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:thi>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "thi";
	expectedReplacedSource = "thi";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:thi>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'true' keyword.
 */
public void test0183(){
	String str =
		"public class X {\n" +
		"  void foo(){\n" +
		"     tru\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "tru";
	int cursorLocation = str.lastIndexOf("tru") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:tru>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "tru";
	expectedReplacedSource = "tru";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:tru>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'false' keyword.
 */
public void test0184(){
	String str =
		"public class X {\n" +
		"  void foo(){\n" +
		"     fal\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "fal";
	int cursorLocation = str.lastIndexOf("fal") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:fal>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "fal";
	expectedReplacedSource = "fal";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:fal>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'null' keyword.
 */
public void test0185(){
	String str =
		"public class X {\n" +
		"  void foo(){\n" +
		"     nul\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "nul";
	int cursorLocation = str.lastIndexOf("nul") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:nul>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "nul";
	expectedReplacedSource = "nul";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:nul>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'instanceof' keyword.
 */
public void test0186(){
	String str =
		"public class X {\n" +
		"  void foo(){\n" +
		"     if(zzz ins\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "ins";
	int cursorLocation = str.lastIndexOf("ins") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnKeyword:ins>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "ins";
	expectedReplacedSource = "ins";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnKeyword:ins>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'instanceof' keyword.
 */
public void test0187(){
	String str =
		"public class X {\n" +
		"  void foo(){\n" +
		"     ins\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "ins";
	int cursorLocation = str.lastIndexOf("ins") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:ins>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "ins";
	expectedReplacedSource = "ins";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:ins>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'instanceof' keyword.
 */
public void test0188(){
	String str =
		"public class X {\n" +
		"  void foo(){\n" +
		"     if(zzz zzz ins\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "ins";
	int cursorLocation = str.lastIndexOf("ins") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:ins>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "ins";
	expectedReplacedSource = "ins";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    zzz zzz;\n" +
		"    <CompleteOnName:ins>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'while' keyword.
 */
public void test0189(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    do{\n" +
		"    } whi\n" +
		"  }\n" +
		"}";

	String completeBehind = "whi";
	int cursorLocation = str.lastIndexOf("whi") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnKeyword:whi>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "whi";
	expectedReplacedSource = "whi";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnKeyword:whi>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'catch' keyword.
 */
public void test0190(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    try {\n" +
		"    } catch(E e) {\n" +
		"    } cat\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "cat";
	int cursorLocation = str.lastIndexOf("cat") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:cat>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "cat";
	expectedReplacedSource = "cat";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:cat>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'finally' keyword.
 */
public void test0191(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"     try {" +
		"     } catch(E e) {" +
		"     } fin" +
		"  }\n" +
		"}\n";

	String completeBehind = "fin";
	int cursorLocation = str.lastIndexOf("fin") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:fin>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "fin";
	expectedReplacedSource = "fin";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:fin>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'finally' keyword.
 */
public void test0192(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"     try {" +
		"     } finally {" +
		"     } fin" +
		"  }\n" +
		"}\n";

	String completeBehind = "fin";
	int cursorLocation = str.lastIndexOf("fin") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:fin>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "fin";
	expectedReplacedSource = "fin";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:fin>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'this' keyword.
 */
public void test0193(){
	String str =
		"public class X {\n" +
		"  void foo(){\n" +
		"     X.thi\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "thi";
	int cursorLocation = str.lastIndexOf("thi") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:X.thi>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "thi";
	expectedReplacedSource = "X.thi";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:X.thi>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/*
 * Test for 'break' keyword.
 */
public void test0217(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    bre\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "bre";
	int cursorLocation = str.lastIndexOf("bre") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:bre>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "bre";
	expectedReplacedSource = "bre";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:bre>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'break' keyword.
 */
public void test0218(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    for(int i; i < 10; i++) {\n" +
		"      bre\n" +
		"    }\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "bre";
	int cursorLocation = str.lastIndexOf("bre") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:bre>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "bre";
	expectedReplacedSource = "bre";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    int i;\n" +
		"    {\n" +
		"      <CompleteOnName:bre>;\n" +
		"    }\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'case' keyword.
 */
public void test0219(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    cas\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "cas";
	int cursorLocation = str.lastIndexOf("cas") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:cas>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "cas";
	expectedReplacedSource = "cas";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:cas>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'case' keyword.
 */
public void test0220(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    switch(0) {\n" +
		"      cas\n" +
		"    }\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "cas";
	int cursorLocation = str.lastIndexOf("cas") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnKeyword:cas>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "cas";
	expectedReplacedSource = "cas";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    {\n" +
		"      <CompleteOnKeyword:cas>;\n" +
		"    }\n"+
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'catch' keyword.
 */
public void test0221(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    cat\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "cat";
	int cursorLocation = str.lastIndexOf("cat") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:cat>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "cat";
	expectedReplacedSource = "cat";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:cat>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'catch' keyword.
 */
public void test0222(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    try {\n" +
		"    } cat\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "cat";
	int cursorLocation = str.lastIndexOf("cat") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnKeyword:cat>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "cat";
	expectedReplacedSource = "cat";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnKeyword:cat>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}

/*
 * Test for 'continue' keyword.
 */
public void test0234(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    con\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "con";
	int cursorLocation = str.lastIndexOf("con") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:con>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "con";
	expectedReplacedSource = "con";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:con>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'continue' keyword.
 */
public void test0235(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    for(int i; i < 5; i++) {\n" +
		"      con\n" +
		"    }\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "con";
	int cursorLocation = str.lastIndexOf("con") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:con>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "con";
	expectedReplacedSource = "con";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    int i;\n" +
		"    {\n" +
		"      <CompleteOnName:con>;\n" +
		"    }\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'default' keyword.
 */
public void test0236(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    def\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "def";
	int cursorLocation = str.lastIndexOf("def") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:def>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "def";
	expectedReplacedSource = "def";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:def>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'default' keyword.
 */
public void test0237(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    switch(0) {\n" +
		"      case 1 : break;\n" +
		"      def\n" +
		"    }\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "def";
	int cursorLocation = str.lastIndexOf("def") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:def>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "def";
	expectedReplacedSource = "def";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    {\n" +
		"      <CompleteOnName:def>;\n" +
		"    }\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'do' keyword.
 */
public void test0238(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    do\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "do";
	int cursorLocation = str.lastIndexOf("do") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:do>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "do";
	expectedReplacedSource = "do";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:do>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'else' keyword.
 */
public void test0239(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    els\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "els";
	int cursorLocation = str.lastIndexOf("els") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:els>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "els";
	expectedReplacedSource = "els";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:els>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'else' keyword.
 */
public void test0240(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    if(true) {\n" +
		"    } els\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "els";
	int cursorLocation = str.lastIndexOf("els") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:els>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "els";
	expectedReplacedSource = "els";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:els>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'finally' keyword.
 */
public void test0249(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    fin" +
		"  }\n" +
		"}\n";

	String completeBehind = "fin";
	int cursorLocation = str.lastIndexOf("fin") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:fin>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "fin";
	expectedReplacedSource = "fin";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:fin>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'finally' keyword.
 */
public void test0250(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    try {" +
		"    } fin" +
		"  }\n" +
		"}\n";

	String completeBehind = "fin";
	int cursorLocation = str.lastIndexOf("fin") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnKeyword:fin>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "fin";
	expectedReplacedSource = "fin";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnKeyword:fin>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'for' keyword.
 */
public void test0251(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    for" +
		"  }\n" +
		"}\n";

	String completeBehind = "for";
	int cursorLocation = str.lastIndexOf("for") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:for>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "for";
	expectedReplacedSource = "for";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:for>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'if' keyword.
 */
public void test0252(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    if" +
		"  }\n" +
		"}\n";

	String completeBehind = "if";
	int cursorLocation = str.lastIndexOf("if") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:if>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "if";
	expectedReplacedSource = "if";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:if>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'switch' keyword.
 */
public void test0253(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    swi" +
		"  }\n" +
		"}\n";

	String completeBehind = "swi";
	int cursorLocation = str.lastIndexOf("swi") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:swi>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "swi";
	expectedReplacedSource = "swi";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:swi>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'return' keyword.
 */
public void test0284(){
	String str =
		"public class X {\n" +
		"  int foo() {\n" +
		"    #\n" +
		"    ret\n" +
		"  }\n" +
		"}";

	String completeBehind = "ret";
	int cursorLocation = str.lastIndexOf("ret") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  int foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:ret>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "ret";
	expectedReplacedSource = "ret";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  int foo() {\n" +
		"    <CompleteOnName:ret>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'throw' keyword.
 */
public void test0285(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    #\n" +
		"    thr\n" +
		"  }\n" +
		"}";

	String completeBehind = "thr";
	int cursorLocation = str.lastIndexOf("thr") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:thr>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "thr";
	expectedReplacedSource = "thr";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:thr>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'try' keyword.
 */
public void test0286(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    #\n" +
		"    try\n" +
		"  }\n" +
		"}";

	String completeBehind = "try";
	int cursorLocation = str.lastIndexOf("try") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:try>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "try";
	expectedReplacedSource = "try";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:try>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'try' keyword.
 */
public void test0287(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    #\n" +
		"    if(try\n" +
		"  }\n" +
		"}";

	String completeBehind = "try";
	int cursorLocation = str.lastIndexOf("try") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:try>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "try";
	expectedReplacedSource = "try";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:try>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'do' keyword.
 */
public void test0288(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    #\n" +
		"    if(do\n" +
		"  }\n" +
		"}";

	String completeBehind = "do";
	int cursorLocation = str.lastIndexOf("do") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:do>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "do";
	expectedReplacedSource = "do";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:do>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'for' keyword.
 */
public void test0289(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    #\n" +
		"    if(for\n" +
		"  }\n" +
		"}";

	String completeBehind = "for";
	int cursorLocation = str.lastIndexOf("for") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:for>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "for";
	expectedReplacedSource = "for";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:for>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'if' keyword.
 */
public void test0290(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    #\n" +
		"    if(if\n" +
		"  }\n" +
		"}";

	String completeBehind = "if";
	int cursorLocation = str.lastIndexOf("if") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:if>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "if";
	expectedReplacedSource = "if";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:if>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'switch' keyword.
 */
public void test0291(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    #\n" +
		"    if(swi\n" +
		"  }\n" +
		"}";

	String completeBehind = "swi";
	int cursorLocation = str.lastIndexOf("swi") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:swi>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "swi";
	expectedReplacedSource = "swi";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:swi>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'new' keyword.
 */
public void test0292(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    #\n" +
		"    new\n" +
		"  }\n" +
		"}";

	String completeBehind = "new";
	int cursorLocation = str.lastIndexOf("new") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:new>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "new";
	expectedReplacedSource = "new";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:new>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'new' keyword.
 */
public void test0293(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    #\n" +
		"    new X\n" +
		"  }\n" +
		"}";

	String completeBehind = "new";
	int cursorLocation = str.lastIndexOf("new") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:new>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "new";
	expectedReplacedSource = "new";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:new>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'new' keyword.
 */
public void test0294(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    #\n" +
		"    new X()\n" +
		"  }\n" +
		"}";

	String completeBehind = "new";
	int cursorLocation = str.lastIndexOf("new") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:new>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "new";
	expectedReplacedSource = "new";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:new>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'while' keyword.
 */
public void test0301(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    #\n" +
		"    whi\n" +
		"  }\n" +
		"}";

	String completeBehind = "whi";
	int cursorLocation = str.lastIndexOf("whi") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:whi>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "whi";
	expectedReplacedSource = "whi";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:whi>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'while' keyword.
 */
public void test0302(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    #\n" +
		"    if(whi\n" +
		"  }\n" +
		"}";

	String completeBehind = "whi";
	int cursorLocation = str.lastIndexOf("whi") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:whi>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "whi";
	expectedReplacedSource = "whi";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:whi>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'this' keyword.
 */
public void test0376(){
	String str =
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    thi\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "thi";
	int cursorLocation = str.lastIndexOf("thi") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:thi>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "thi";
	expectedReplacedSource = "thi";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:thi>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'true' keyword.
 */
public void test0377(){
	String str =
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    tru\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "tru";
	int cursorLocation = str.lastIndexOf("tru") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:tru>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "tru";
	expectedReplacedSource = "tru";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:tru>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'false' keyword.
 */
public void test0378(){
	String str =
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    fal\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "fal";
	int cursorLocation = str.lastIndexOf("fal") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:fal>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "fal";
	expectedReplacedSource = "fal";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:fal>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'null' keyword.
 */
public void test0379(){
	String str =
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    nul\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "nul";
	int cursorLocation = str.lastIndexOf("nul") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:nul>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "nul";
	expectedReplacedSource = "nul";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:nul>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'instanceof' keyword.
 */
public void test0380(){
	String str =
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    if(zzz ins\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "ins";
	int cursorLocation = str.lastIndexOf("ins") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnKeyword:ins>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "ins";
	expectedReplacedSource = "ins";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnKeyword:ins>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'instanceof' keyword.
 */
public void test0381(){
	String str =
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    ins\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "ins";
	int cursorLocation = str.lastIndexOf("ins") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:ins>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "ins";
	expectedReplacedSource = "ins";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:ins>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'instanceof' keyword.
 */
public void test0382(){
	String str =
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    if(zzz zzz ins\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "ins";
	int cursorLocation = str.lastIndexOf("ins") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:ins>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "ins";
	expectedReplacedSource = "ins";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    zzz zzz;\n" +
		"    <CompleteOnName:ins>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'while' keyword.
 */
public void test0384(){
	String str =
		"public class X {\n" +
		"  void foo() {\n" +
		"    #\n" +
		"    do{\n" +
		"    } whi\n" +
		"  }\n" +
		"}";

	String completeBehind = "whi";
	int cursorLocation = str.lastIndexOf("whi") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnKeyword:whi>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "whi";
	expectedReplacedSource = "whi";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnKeyword:whi>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'catch' keyword.
 */
public void test0385(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    try {\n" +
		"    } catch(E e) {\n" +
		"    } cat\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "cat";
	int cursorLocation = str.lastIndexOf("cat") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:cat>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "cat";
	expectedReplacedSource = "cat";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:cat>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'finally' keyword.
 */
public void test0386(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    try {" +
		"    } catch(E e) {" +
		"    } fin" +
		"  }\n" +
		"}\n";

	String completeBehind = "fin";
	int cursorLocation = str.lastIndexOf("fin") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:fin>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "fin";
	expectedReplacedSource = "fin";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:fin>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'finally' keyword.
 */
public void test0387(){
	String str =
		"package p;\n" +
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    try {" +
		"    } finally {" +
		"    } fin" +
		"  }\n" +
		"}\n";

	String completeBehind = "fin";
	int cursorLocation = str.lastIndexOf("fin") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:fin>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "fin";
	expectedReplacedSource = "fin";
	expectedUnitDisplayString =
		"package p;\n" +
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:fin>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
/*
 * Test for 'this' keyword.
 */
public void test0388(){
	String str =
		"public class X {\n" +
		"  void foo(){\n" +
		"    #\n" +
		"    X.thi\n" +
		"  }\n" +
		"}\n";

	String completeBehind = "thi";
	int cursorLocation = str.lastIndexOf("thi") + completeBehind.length() - 1;

	String expectedCompletionNodeToString = "<NONE>";
	String expectedParentNodeToString = "<NONE>";
	String completionIdentifier = "<NONE>";
	String expectedReplacedSource = "<NONE>";
	String expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"  }\n" +
		"}\n";

	checkDietParse(
		str.toCharArray(),
		cursorLocation,
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"diet ast");
	
	expectedCompletionNodeToString = "<CompleteOnName:X.thi>";
	expectedParentNodeToString = "<NONE>";
	completionIdentifier = "thi";
	expectedReplacedSource = "X.thi";
	expectedUnitDisplayString =
		"public class X {\n" +
		"  public X() {\n" +
		"  }\n" +
		"  void foo() {\n" +
		"    <CompleteOnName:X.thi>;\n" +
		"  }\n" +
		"}\n";
	
	checkMethodParse(
		str.toCharArray(), 
		cursorLocation, 
		expectedCompletionNodeToString,
		expectedParentNodeToString,
		expectedUnitDisplayString,
		completionIdentifier,
		expectedReplacedSource,
		"full ast");
}
}
