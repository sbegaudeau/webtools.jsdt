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
package org.eclipse.wst.jsdt.core.tests.model;

import org.eclipse.wst.jsdt.core.*;

import org.eclipse.wst.jsdt.internal.codeassist.RelevanceConstants;

import junit.framework.*;

public class JavadocCompletionContextTests extends AbstractJavaModelCompletionTests implements RelevanceConstants {

public JavadocCompletionContextTests(String name) {
	super(name);
}
public void setUpSuite() throws Exception {
	if (COMPLETION_PROJECT == null)  {
		COMPLETION_PROJECT = setUpJavaProject("Completion", "1.4");
	} else {
		setUpProjectCompliance(COMPLETION_PROJECT, "1.4");
	}
	super.setUpSuite();
}

public void tearDownSuite() throws Exception {
	super.tearDownSuite();
}
public static Test suite() {
	return buildModelTestSuite(JavadocCompletionContextTests.class);
}
public void test0001() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0001/X.js",
		"package test0001;\n" + 
		"/**\n" + 
		" * @see ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0002() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0002/X.js",
		"package test0002;\n" + 
		"/**\n" + 
		" * @see ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ");

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0003() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0003/X.js",
		"package test0003;\n" + 
		"/**\n" + 
		" * @see ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0004() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0004/X.js",
		"package test0004;\n" + 
		"/**\n" + 
		" * @see \n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("@see ") + "@see ".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("@see ") + "@see ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0005() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0005/X.js",
		"package test0005;\n" + 
		"/**\n" + 
		" * @see X.ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0006() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0006/X.js",
		"package test0006;\n" + 
		"/**\n" + 
		" * @see X.ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ");

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0007() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0007/X.js",
		"package test0007;\n" + 
		"/**\n" + 
		" * @see X.ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0008() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0008/X.js",
		"package test0008;\n" + 
		"/**\n" + 
		" * @see X.\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("X.") + "X.".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("X.") + "X.".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0009() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0009/X.js",
		"package test0009;\n" + 
		"/**\n" + 
		" * @see test0009.ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0010() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0010/X.js",
		"package test0010;\n" + 
		"/**\n" + 
		" * @see test0010.ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ");

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0011() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0011/X.js",
		"package test0011;\n" + 
		"/**\n" + 
		" * @see test0011.ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0012() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0012/X.js",
		"package test0012;\n" + 
		"/**\n" + 
		" * @see test0012.\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("test0012.") + "test0012.".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("test0012.") + "test0012.".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0013() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0013/X.js",
		"package test0013;\n" + 
		"/**\n" + 
		" * @see #ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0014() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0014/X.js",
		"package test0014;\n" + 
		"/**\n" + 
		" * @see #ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ");

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0015() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0015/X.js",
		"package test0015;\n" + 
		"/**\n" + 
		" * @see #ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0016() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0012/X.js",
		"package test0016;\n" + 
		"/**\n" + 
		" * @see #\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("#") + "#".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("#") + "#".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0017() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0017/X.js",
		"package test0017;\n" + 
		"/**\n" + 
		" * @see X#ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0018() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0018/X.js",
		"package test0018;\n" + 
		"/**\n" + 
		" * @see X#ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ");

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0019() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0019/X.js",
		"package test0019;\n" + 
		"/**\n" + 
		" * @see X#ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0020() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0020/X.js",
		"package test0020;\n" + 
		"/**\n" + 
		" * @see X#\n" + 
		" */\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("#") + "#".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("#") + "#".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0021() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0021/X.js",
		"package test0021;\n" + 
		"/**\n" + 
		" * @see X#foo(ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public void foo(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0022() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0022/X.js",
		"package test0022;\n" + 
		"/**\n" + 
		" * @see X#foo(ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public void foo(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ");

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0023() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0023/X.js",
		"package test0023;\n" + 
		"/**\n" + 
		" * @see X#foo(ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public void foo(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0024() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0024/X.js",
		"package test0024;\n" + 
		"/**\n" + 
		" * @see X#foo(\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public void foo(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("#foo(") + "#foo(".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("#foo(") + "#foo(".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0025() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0025/X.js",
		"package test0025;\n" + 
		"/**\n" + 
		" * @see X#foo(Object ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public void foo(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0026() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0026/X.js",
		"package test0026;\n" + 
		"/**\n" + 
		" * @see X#foo(Object ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public void foo(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ");

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0027() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0027/X.js",
		"package test0027;\n" + 
		"/**\n" + 
		" * @see X#foo(Object ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public void foo(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0028() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0028/X.js",
		"package test0028;\n" + 
		"/**\n" + 
		" * @see X#foo(Object \n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public void foo(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("#foo(Object ") + "#foo(Object ".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("#foo(Object ") + "#foo(Object ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0029() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0029/X.js",
		"package test0029;\n" + 
		"/**\n" + 
		" * @see X#foo(Object a,ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public void foo(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0030() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0030/X.js",
		"package test0030;\n" + 
		"/**\n" + 
		" * @see X#foo(Object a,ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public void foo(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ");

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0031() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0031/X.js",
		"package test0031;\n" + 
		"/**\n" + 
		" * @see X#foo(Object a,ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public void foo(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0032() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0032/X.js",
		"package test0032;\n" + 
		"/**\n" + 
		" * @see X#foo(Object a,\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public void foo(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("#foo(Object a,") + "#foo(Object a,".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("#foo(Object a,") + "#foo(Object a,".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0033() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0033/X.js",
		"package test0033;\n" + 
		"/**\n" + 
		" * @see X#X(ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public X(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0034() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0034/X.js",
		"package test0034;\n" + 
		"/**\n" + 
		" * @see X#X(ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public X(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ");

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0035() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0035/X.js",
		"package test0035;\n" + 
		"/**\n" + 
		" * @see X#X(ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public X(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0036() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0036/X.js",
		"package test0036;\n" + 
		"/**\n" + 
		" * @see X#X(\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public X(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("#X(") + "#X(".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("#X(") + "#X(".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0037() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0037/X.js",
		"package test0037;\n" + 
		"/**\n" + 
		" * @see X#X(Object ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public X(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0038() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0038/X.js",
		"package test0038;\n" + 
		"/**\n" + 
		" * @see X#X(Object ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public X(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ");

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0039() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0039/X.js",
		"package test0039;\n" + 
		"/**\n" + 
		" * @see X#X(Object ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public X(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0040() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0040/X.js",
		"package test0040;\n" + 
		"/**\n" + 
		" * @see X#X(Object \n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public X(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("#X(Object ") + "#X(Object ".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("#X(Object ") + "#X(Object ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0041() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0041/X.js",
		"package test0041;\n" + 
		"/**\n" + 
		" * @see X#X(Object a,ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public X(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0042() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0042/X.js",
		"package test0042;\n" + 
		"/**\n" + 
		" * @see X#X(Object a,ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public X(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ");

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0043() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0043/X.js",
		"package test0043;\n" + 
		"/**\n" + 
		" * @see X#X(Object a,ZZZZ\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public X(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0044() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0044/X.js",
		"package test0044;\n" + 
		"/**\n" + 
		" * @see X#X(Object a,\n" + 
		" */\n" + 
		"public class X {\n" + 
		"  public X(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("#X(Object a,") + "#X(Object a,".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("#X(Object a,") + "#X(Object a,".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0045() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0045/X.js",
		"package test0045;\n" + 
		"public class X {\n" + 
		"  /**\n" + 
		"   * @param ZZZZ\n" + 
		"   */\n" + 
		"  public void foo(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0046() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0046/X.js",
		"package test0046;\n" + 
		"public class X {\n" + 
		"  /**\n" + 
		"   * @param ZZZZ\n" + 
		"   */\n" + 
		"  public void foo(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ");

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0047() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0047/X.js",
		"package test0047;\n" + 
		"public class X {\n" + 
		"  /**\n" + 
		"   * @param ZZZZ\n" + 
		"   */\n" + 
		"  public void foo(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0048() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0048/X.js",
		"package test0048;\n" + 
		"public class X {\n" + 
		"  /**\n" + 
		"   * @param \n" + 
		"   */\n" + 
		"  public void foo(Object a, Object b){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("@param ") + "@param ".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("@param ") + "@param ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0049() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0049/X.js",
		"package test0049;\n" + 
		"public class X {\n" + 
		"  /**\n" + 
		"   * @ZZZZ\n" + 
		"   */\n" + 
		"  public void foo(){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("@ZZZZ");
	int tokenEnd = tokenStart + "@ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"@ZZZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0050() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0050/X.js",
		"package test0050;\n" + 
		"public class X {\n" + 
		"  /**\n" + 
		"   * @ZZZZ\n" + 
		"   */\n" + 
		"  public void foo(){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("@ZZZZ");
	int tokenEnd = tokenStart + "@ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ");

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"@\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0051() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0051/X.js",
		"package test0051;\n" + 
		"public class X {\n" + 
		"  /**\n" + 
		"   * @ZZZZ\n" + 
		"   */\n" + 
		"  public void foo(){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("@ZZZZ");
	int tokenEnd = tokenStart + "@ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"@ZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0052() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0052/X.js",
		"package test0052;\n" + 
		"public class X {\n" + 
		"  /**\n" + 
		"   * @\n" + 
		"   */\n" + 
		"  public void foo(){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("@");
	int tokenEnd = tokenStart + "@".length() - 1;
	int cursorLocation = str.lastIndexOf("@") + "@".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"@\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0053() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0053/X.js",
		"package test0053;\n" + 
		"public class X {\n" + 
		"  /**\n" + 
		"   * blabla @ZZZZ\n" + 
		"   */\n" + 
		"  public void foo(){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("@ZZZZ");
	int tokenEnd = tokenStart + "@ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"@ZZZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0054() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0054/X.js",
		"package test0054;\n" + 
		"public class X {\n" + 
		"  /**\n" + 
		"   * blabla @ZZZZ\n" + 
		"   */\n" + 
		"  public void foo(){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("@ZZZZ");
	int tokenEnd = tokenStart + "@ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ");

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"@\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0055() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0055/X.js",
		"package test0055;\n" + 
		"public class X {\n" + 
		"  /**\n" + 
		"   * blabla @ZZZZ\n" + 
		"   */\n" + 
		"  public void foo(){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("@ZZZZ");
	int tokenEnd = tokenStart + "@ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"@ZZ\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0056() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0056/X.js",
		"package test0056;\n" + 
		"public class X {\n" + 
		"  /**\n" + 
		"   * blabla @\n" + 
		"   */\n" + 
		"  public void foo(){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("@");
	int tokenEnd = tokenStart + "@".length() - 1;
	int cursorLocation = str.lastIndexOf("@") + "@".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"@\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
}
