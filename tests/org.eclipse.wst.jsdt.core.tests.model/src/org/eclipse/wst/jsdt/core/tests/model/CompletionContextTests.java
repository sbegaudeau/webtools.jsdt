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


import org.eclipse.wst.jsdt.internal.codeassist.RelevanceConstants;
import org.eclipse.wst.jsdt.core.*;

import junit.framework.*;

public class CompletionContextTests extends AbstractJavaModelCompletionTests implements RelevanceConstants {

public CompletionContextTests(String name) {
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

public static Test suite() {
	return buildModelTestSuite(CompletionContextTests.class);
}
public void test0001() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0001/X.js",
		"function X(){\n" + 
		"  ZZZZ\n" + 
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
		"function X() {\n" + 
		"  ZZZZ\n" + 
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
		"function X() {\n" + 
		"  ZZZZ\n" + 
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
		"function X() {\n" + 
		"  /**/\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("/**/") + "/**/".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("/**/") + "/**/".length();

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
//public void test0005() throws JavaScriptModelException {
//	this.workingCopies = new IJavaScriptUnit[1];
//	this.workingCopies[0] = getWorkingCopy(
//		"/Completion/src3/test0005/X.js",
//		"package test0005;\n" + 
//		"public class X {\n" + 
//		"  ZZZZ foo()\n" + 
//		"}");
//	
//	String str = this.workingCopies[0].getSource();
//	int tokenStart = str.lastIndexOf("ZZZZ");
//	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
//	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZZZ".length();
//
//	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
//
//	assertResults(
//		"completion offset="+(cursorLocation)+"\n" +
//		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
//		"completion token=\"ZZZZ\"\n" +
//		"completion token kind=TOKEN_KIND_NAME\n" +
//		"expectedTypesSignatures=null\n" +
//		"expectedTypesKeys=null",
//		result.context);
//}
//public void test0006() throws JavaScriptModelException {
//	this.workingCopies = new IJavaScriptUnit[1];
//	this.workingCopies[0] = getWorkingCopy(
//		"/Completion/src3/test0006/X.js",
//		"package test0006;\n" + 
//		"public class X {\n" + 
//		"  ZZZZ foo()\n" + 
//		"}");
//	
//	String str = this.workingCopies[0].getSource();
//	int tokenStart = str.lastIndexOf("ZZZZ");
//	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
//	int cursorLocation = str.lastIndexOf("ZZZZ");
//
//	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
//
//	assertResults(
//		"completion offset="+(cursorLocation)+"\n" +
//		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
//		"completion token=\"\"\n" +
//		"completion token kind=TOKEN_KIND_NAME\n" +
//		"expectedTypesSignatures=null\n" +
//		"expectedTypesKeys=null",
//		result.context);
//}
//public void test0007() throws JavaScriptModelException {
//	this.workingCopies = new IJavaScriptUnit[1];
//	this.workingCopies[0] = getWorkingCopy(
//		"/Completion/src3/test0007/X.js",
//		"package test0007;\n" + 
//		"public class X {\n" + 
//		"  ZZZZ foo()\n" + 
//		"}");
//	
//	String str = this.workingCopies[0].getSource();
//	int tokenStart = str.lastIndexOf("ZZZZ");
//	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
//	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZ".length();
//
//	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
//	
//	assertResults(
//		"completion offset="+(cursorLocation)+"\n" +
//		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
//		"completion token=\"ZZ\"\n" +
//		"completion token kind=TOKEN_KIND_NAME\n" +
//		"expectedTypesSignatures=null\n" +
//		"expectedTypesKeys=null",
//		result.context);
//}
public void test0008() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0008/X.js",
		"  /**/ function foo()\n" + 
		"");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("/**/") + "/**/".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("/**/") + "/**/".length();

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
//public void test0009() throws JavaScriptModelException {
//	this.workingCopies = new IJavaScriptUnit[1];
//	this.workingCopies[0] = getWorkingCopy(
//		"/Completion/src3/test0009/X.js",
//		"package test0009;\n" + 
//		"public class X {\n" + 
//		"  void foo() {\n" + 
//		"    ZZZZ\n" + 
//		"  }\n" + 
//		"}");
//	
//	String str = this.workingCopies[0].getSource();
//	int tokenStart = str.lastIndexOf("ZZZZ");
//	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
//	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZZZ".length();
//
//	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
//
//	assertResults(
//		"completion offset="+(cursorLocation)+"\n" +
//		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
//		"completion token=\"ZZZZ\"\n" +
//		"completion token kind=TOKEN_KIND_NAME\n" +
//		"expectedTypesSignatures=null\n" +
//		"expectedTypesKeys=null",
//		result.context);
//}
public void test0010() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0010/X.js",
		"package test0010;\n" + 
		"public class X {\n" + 
		"  void foo() {\n" + 
		"    ZZZZ\n" + 
		"  }\n" + 
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
		"public class X {\n" + 
		"  void foo() {\n" + 
		"    ZZZZ\n" + 
		"  }\n" + 
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
		"public class X {\n" + 
		"  void foo() {\n" + 
		"    /**/\n" + 
		"  }\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("/**/") + "/**/".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("/**/") + "/**/".length();

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
		"public class X extends ZZZZ {\n" + 
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
		"public class X extends ZZZZ {\n" + 
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
		"public class X extends ZZZZ {\n" + 
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
		"/Completion/src3/test0016/X.js",
		"package test0016;\n" + 
		"public class X extends /**/ {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("/**/") + "/**/".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("/**/") + "/**/".length();

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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src3/test0017/YYYY.js",
		"package test0017;\n" + 
		"public class YYYY {\n" + 
		"  public class ZZZZ {\n" + 
		"  }\n" + 
		"}");
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0017/X.js",
		"package test0017;\n" + 
		"public class X {\n" + 
		"  void foo() {\n" + 
		"    YYYY.ZZZZ\n" + 
		"  }\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src3/test0018/YYYY.js",
		"package test0018;\n" + 
		"public class YYYY {\n" + 
		"  public class ZZZZ {\n" + 
		"  }\n" + 
		"}");
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0018/X.js",
		"package test0018;\n" + 
		"public class X {\n" + 
		"  void foo() {\n" + 
		"    YYYY.ZZZZ\n" + 
		"  }\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src3/test0019/YYYY.js",
		"package test0019;\n" + 
		"public class YYYY {\n" + 
		"  public class ZZZZ {\n" + 
		"  }\n" + 
		"}");
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0019/X.js",
		"package test0019;\n" + 
		"public class X {\n" + 
		"  void foo() {\n" + 
		"    YYYY.ZZZZ\n" + 
		"  }\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src3/test0020/YYYY.js",
		"package test0020;\n" + 
		"public class YYYY {\n" + 
		"  public class ZZZZ {\n" + 
		"  }\n" + 
		"}");
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0020/X.js",
		"package test0020;\n" + 
		"public class X {\n" + 
		"  void foo() {\n" + 
		"    YYYY.\n" + 
		"  }\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("YYYY.") + "YYYY.".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("YYYY.") + "YYYY.".length();

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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0021/X.js",
		"package test0021;\n" + 
		"public class X {\n" + 
		"  void foo() {\n" + 
		"    test0021.ZZZZ\n" + 
		"  }\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0022/X.js",
		"package test0022;\n" + 
		"public class X {\n" + 
		"  void foo() {\n" + 
		"    test0022.ZZZZ\n" + 
		"  }\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0023/X.js",
		"package test0023;\n" + 
		"public class X {\n" + 
		"  void foo() {\n" + 
		"    test0023.ZZZZ\n" + 
		"  }\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0024/X.js",
		"package test0024;\n" + 
		"public class X {\n" + 
		"  void foo() {\n" + 
		"    test0024.\n" + 
		"  }\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("test0024.") + "test0024.".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("test0024.") + "test0024.".length();

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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0025/X.js",
		"package test0025;\n" + 
		"public class X {\n" + 
		"  Object var;\n" + 
		"  void foo() {\n" + 
		"    var.ZZZZ\n" + 
		"  }\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0026/X.js",
		"package test0026;\n" + 
		"public class X {\n" + 
		"  Object var;\n" + 
		"  void foo() {\n" + 
		"    var.ZZZZ\n" + 
		"  }\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0027/X.js",
		"package test0027;\n" + 
		"public class X {\n" + 
		"  Object var;\n" + 
		"  void foo() {\n" + 
		"    var.ZZZZ\n" + 
		"  }\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0028/X.js",
		"package test0028;\n" + 
		"public class X {\n" + 
		"  Object var;\n" + 
		"  void foo() {\n" + 
		"    var.\n" + 
		"  }\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("var.") + "var.".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("var.") + "var.".length();

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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src3/test0029/YYYY.js",
		"package test0029;\n" + 
		"public class YYYY {\n" + 
		"  public class ZZZZ {\n" + 
		"  }\n" + 
		"}");
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0029/X.js",
		"package test0029;\n" + 
		"public class X extends YYYY.ZZZZ {\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src3/test0030/YYYY.js",
		"package test0030;\n" + 
		"public class YYYY {\n" + 
		"  public class ZZZZ {\n" + 
		"  }\n" + 
		"}");
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0030/X.js",
		"package test0030;\n" + 
		"public class X extends YYYY.ZZZZ {\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src3/test0031/YYYY.js",
		"package test0031;\n" + 
		"public class YYYY {\n" + 
		"  public class ZZZZ {\n" + 
		"  }\n" + 
		"}");
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0031/X.js",
		"package test0031;\n" + 
		"public class X extends YYYY.ZZZZ {\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src3/test0032/YYYY.js",
		"package test0032;\n" + 
		"public class YYYY {\n" + 
		"  public class ZZZZ {\n" + 
		"  }\n" + 
		"}");
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0032/X.js",
		"package test0032;\n" + 
		"public class X extends YYYY. {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("YYYY.") + "YYYY.".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("YYYY.") + "YYYY.".length();

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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0033/X.js",
		"package test0033;\n" + 
		"public class X extends test0033.ZZZZ {\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0034/X.js",
		"package test0034;\n" + 
		"public class X extends test0034.ZZZZ {\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0035/X.js",
		"package test0035;\n" + 
		"public class X extends test0035.ZZZZ {\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0036/X.js",
		"package test0036;\n" + 
		"public class X extends test0036. {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("test0036.") + "test0036.".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("test0036.") + "test0036.".length();

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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0037/X.js",
		"package test0037;\n" + 
		"public class X {\n" + 
		"  X ZZZZ;\n" + 
		"  X foo(){\n" + 
		"    foo().ZZZZ\n" + 
		"  }\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0038/X.js",
		"package test0038;\n" + 
		"public class X {\n" + 
		"  X ZZZZ;\n" + 
		"  X foo(){\n" + 
		"    foo().ZZZZ\n" + 
		"  }\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0039/X.js",
		"package test0039;\n" + 
		"public class X {\n" + 
		"  X ZZZZ;\n" + 
		"  X foo(){\n" + 
		"    foo().ZZZZ\n" + 
		"  }\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0040/X.js",
		"package test0040;\n" + 
		"public class X {\n" + 
		"  X ZZZZ;\n" + 
		"  X foo(){\n" + 
		"    foo().\n" + 
		"  }\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("foo().") + "foo().".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("foo().") + "foo().".length();

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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0041/X.js",
		"package test0041;\n" + 
		"public class X {\n" + 
		"  X ZZZZ;\n" + 
		"  X foo(){\n" + 
		"    int.ZZZZ\n" + 
		"  }\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0042/X.js",
		"package test0042;\n" + 
		"public class X {\n" + 
		"  X ZZZZ;\n" + 
		"  X foo(){\n" + 
		"    int.ZZZZ\n" + 
		"  }\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0043/X.js",
		"package test0043;\n" + 
		"public class X {\n" + 
		"  X ZZZZ;\n" + 
		"  X foo(){\n" + 
		"    int.ZZZZ\n" + 
		"  }\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0044/X.js",
		"package test0044;\n" + 
		"public class X {\n" + 
		"  X ZZZZ;\n" + 
		"  X foo(){\n" + 
		"    int.\n" + 
		"  }\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("int.") + "int.".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("int.") + "int.".length();

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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0045/X.js",
		"package test0045;\n" + 
		"public class X {\n" + 
		"  void ZZZZ(){\n" + 
		"  }\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0046/X.js",
		"package test0046;\n" + 
		"public class X {\n" + 
		"  void ZZZZ(){\n" + 
		"  }\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0047/X.js",
		"package test0047;\n" + 
		"public class X {\n" + 
		"  void ZZZZ(){\n" + 
		"  }\n" + 
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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0048/X.js",
		"package test0048;\n" + 
		"public class X {\n" + 
		"  void (){\n" + 
		"  }\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("void ") + "void ".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("void ") + "void ".length();

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
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0049/X.js",
		"package test0049;\n" + 
		"public class X {\n" + 
		"  int ZZZZ;\n" + 
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
public void test0050() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0050/X.js",
		"package test0050;\n" + 
		"public class X {\n" + 
		"  int ZZZZ;\n" + 
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
public void test0051() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0051/X.js",
		"package test0051;\n" + 
		"public class X {\n" + 
		"  int ZZZZ;\n" + 
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
public void test0052() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0052/X.js",
		"package test0052;\n" + 
		"public class X {\n" + 
		"  int ;\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("int ") + "int ".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("int ") + "int ".length();

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
public void test0053() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0053/X.js",
		"package test0053;\n" + 
		"public class X {\n" + 
		"  {int ZZZZ;}\n" + 
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
public void test0054() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0054/X.js",
		"package test0054;\n" + 
		"public class X {\n" + 
		"  {int ZZZZ;}\n" + 
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
public void test0055() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0055/X.js",
		"package test0055;\n" + 
		"public class X {\n" + 
		"  {int ZZZZ;}\n" + 
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
public void test0056() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0056/X.js",
		"package test0056;\n" + 
		"public class X {\n" + 
		"  {int ;}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("int ") + "int ".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("int ") + "int ".length();

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
public void test0057() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0057/X.js",
		"package test0057;\n" + 
		"public class X {\n" + 
		"  void foo(int ZZZZ){}\n" + 
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
public void test0058() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0058/X.js",
		"package test0058;\n" + 
		"public class X {\n" + 
		"  void foo(int ZZZZ){}\n" + 
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
public void test0059() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0059/X.js",
		"package test0059;\n" + 
		"public class X {\n" + 
		"  void foo(int ZZZZ){}\n" + 
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
public void test0060() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0060/X.js",
		"package test0060;\n" + 
		"public class X {\n" + 
		"  void foo(int ){}\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("int ") + "int ".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("int ") + "int ".length();

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
public void test0061() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0061/X.js",
		"package test0061;\n" + 
		"public class X ZZZZ {\n" + 
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
public void test0062() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0062/X.js",
		"package test0062;\n" + 
		"public class X ZZZZ {\n" + 
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
public void test0063() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0063/X.js",
		"package test0063;\n" + 
		"public class X ZZZZ {\n" + 
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
public void test0064() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0064/X.js",
		"package test0064;\n" + 
		"public class X  {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("X ") + "X ".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("X ") + "X ".length();

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
public void test0065() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0065/X.js",
		"package test0065;\n" + 
		"ZZZZ\n" + 
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
public void test0066() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0066/X.js",
		"package test0066;\n" + 
		"ZZZZ\n" + 
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
public void test0067() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0067/X.js",
		"package test0067;\n" + 
		"ZZZZ\n" + 
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
public void test0068() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0068/X.js",
		"package test0068;\n" + 
		"/**/\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("/**/") + "/**/".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("/**/") + "/**/".length();

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
public void test0069() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0069/X.js",
		"package test0069;\n" + 
		"public class X {\n" + 
		"  {\n" + 
		"    do{\n" + 
		"    } ZZZZ\n" + 
		"  }\n" + 
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
public void test0070() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0070/X.js",
		"package test0070;\n" + 
		"public class X {\n" + 
		"  {\n" + 
		"    do{\n" + 
		"    } ZZZZ\n" + 
		"  }\n" + 
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
public void test0071() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0071/X.js",
		"package test0071;\n" + 
		"public class X {\n" + 
		"  {\n" + 
		"    do{\n" + 
		"    } ZZZZ\n" + 
		"  }\n" + 
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
public void test0072() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0072/X.js",
		"package test0072;\n" + 
		"public class X {\n" + 
		"  {\n" + 
		"    do{\n" + 
		"    }/**/ \n" + 
		"  }\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("/**/ ") + "/**/ ".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("/**/ ") + "/**/ ".length();

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
public void test0073() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0073/X.js",
		"package ZZZZ;\n" + 
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
public void test0074() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0074/X.js",
		"package ZZZZ;\n" + 
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
public void test0075() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0075/X.js",
		"package ZZZZ;\n" + 
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
public void test0076() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0076/X.js",
		"package \n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("package ") + "package ".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("package ") + "package ".length();

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
public void test0077() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0077/test/X.js",
		"package test0077.ZZZZ;\n" + 
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
public void test0078() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0078/test/X.js",
		"package test0078.ZZZZ;\n" + 
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
public void test0079() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0079/test/X.js",
		"package test0079.ZZZZ;\n" + 
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
public void test0080() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0080/test/X.js",
		"package test0080.\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("test0080.") + "test0080.".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("test0080.") + "test0080.".length();

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
public void test0081() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0081/X.js",
		"package test0081;\n" + 
		"import ZZZZ;\n" + 
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
public void test0082() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0082/X.js",
		"package test0082;\n" + 
		"import ZZZZ;\n" + 
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
public void test0083() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0083/X.js",
		"package test0083;\n" + 
		"import ZZZZ;\n" + 
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
public void test0084() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0084/X.js",
		"package test0084;\n" + 
		"import \n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("import ") + "import ".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("import ") + "import ".length();

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
public void test0085() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0085/test/X.js",
		"package test0085;\n" + 
		"import test0085.ZZZZ;\n" + 
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
public void test0086() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0086/test/X.js",
		"package test0086;\n" + 
		"import test0086.ZZZZ;\n" + 
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
public void test0087() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0087/test/X.js",
		"package test0087;\n" + 
		"import test0087.ZZZZ;\n" + 
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
public void test0088() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0084/test/X.js",
		"package test0088;\n" + 
		"import test0085.\n" + 
		"public class X {\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("test0085.") + "test0085.".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("test0085.") + "test0085.".length();

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
public void test0089() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0089/X.js",
		"package test0089;\n" + 
		"public class X {\n" + 
		"  void foo(int a, int b) {\n" + 
		"    this.foo(ZZZZ\n" + 
		"  }\n" + 
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
		"expectedTypesSignatures={I}\n" +
		"expectedTypesKeys={I}",
		result.context);
}
public void test0090() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0090/X.js",
		"package test0090;\n" + 
		"public class X {\n" + 
		"  void foo(int a, int b) {\n" + 
		"    this.foo(ZZZZ\n" + 
		"  }\n" + 
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
public void test0091() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0091/X.js",
		"package test0091;\n" + 
		"public class X {\n" + 
		"  void foo(int a, int b) {\n" + 
		"    this.foo(ZZZZ\n" + 
		"  }\n" + 
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
		"expectedTypesSignatures={I}\n" +
		"expectedTypesKeys={I}",
		result.context);
}
public void test0092() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0092/X.js",
		"package test0092;\n" + 
		"public class X {\n" + 
		"  void foo(int a, int b) {\n" + 
		"    this.foo(\n" + 
		"  }\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("this.foo(") + "this.foo(".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("this.foo(") + "this.foo(".length();

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
public void test0093() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0093/X.js",
		"package test0093;\n" + 
		"public class X {\n" + 
		"  void foo(int a, int b) {\n" + 
		"    this.foo(0,ZZZZ\n" + 
		"  }\n" + 
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
		"expectedTypesSignatures={I}\n" +
		"expectedTypesKeys={I}",
		result.context);
}
public void test0094() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0094/X.js",
		"package test0094;\n" + 
		"public class X {\n" + 
		"  void foo(int a, int b) {\n" + 
		"    this.foo(0,ZZZZ\n" + 
		"  }\n" + 
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
public void test0095() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0095/X.js",
		"package test0095;\n" + 
		"public class X {\n" + 
		"  void foo(int a, int b) {\n" + 
		"    this.foo(0,ZZZZ\n" + 
		"  }\n" + 
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
		"expectedTypesSignatures={I}\n" +
		"expectedTypesKeys={I}",
		result.context);
}
public void test0096() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0096/X.js",
		"package test0096;\n" + 
		"public class X {\n" + 
		"  void foo(int a, int b) {\n" + 
		"    this.foo(0,\n" + 
		"  }\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("this.foo(0,") + "this.foo(0,".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("this.foo(0,") + "this.foo(0,".length();

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
public void test0097() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0097/X.js",
		"package test0097;\n" + 
		"public class X {\n" + 
		"  X(int a, int b) {}\n" +
		"  void foo(int a, int b) {\n" + 
		"    new X(ZZZZ\n" + 
		"  }\n" + 
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
		"expectedTypesSignatures={I}\n" +
		"expectedTypesKeys={I}",
		result.context);
}
public void test0098() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0098/X.js",
		"package test0098;\n" + 
		"public class X {\n" + 
		"  X(int a, int b) {}\n" +
		"  void foo(int a, int b) {\n" + 
		"    new X(ZZZZ\n" + 
		"  }\n" + 
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
public void test0099() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0099/X.js",
		"package test0099;\n" + 
		"public class X {\n" + 
		"  X(int a, int b) {}\n" +
		"  void foo(int a, int b) {\n" + 
		"    new X(ZZZZ\n" + 
		"  }\n" + 
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
		"expectedTypesSignatures={I}\n" +
		"expectedTypesKeys={I}",
		result.context);
}
public void test0100() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0100/X.js",
		"package test0100;\n" + 
		"public class X {\n" + 
		"  X(int a, int b) {}\n" +
		"  void foo(int a, int b) {\n" + 
		"    new X(\n" + 
		"  }\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("new X(") + "new X(".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("new X(") + "new X(".length();

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
public void test0101() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0101/X.js",
		"package test0101;\n" + 
		"public class X {\n" + 
		"  X(int a, int b) {}\n" +
		"  void foo(int a, int b) {\n" + 
		"    new X(0,ZZZZ\n" + 
		"  }\n" + 
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
		"expectedTypesSignatures={I}\n" +
		"expectedTypesKeys={I}",
		result.context);
}
public void test0102() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0102/X.js",
		"package test0102;\n" + 
		"public class X {\n" + 
		"  X(int a, int b) {}\n" +
		"  void foo(int a, int b) {\n" + 
		"    new X(0,ZZZZ\n" + 
		"  }\n" + 
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
public void test0103() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0103/X.js",
		"package test0103;\n" + 
		"public class X {\n" + 
		"  X(int a, int b) {}\n" +
		"  void foo(int a, int b) {\n" + 
		"    new X(0,ZZZZ\n" + 
		"  }\n" + 
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
		"expectedTypesSignatures={I}\n" +
		"expectedTypesKeys={I}",
		result.context);
}
public void test0104() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0104/X.js",
		"package test0104;\n" + 
		"public class X {\n" + 
		"  X(int a, int b) {}\n" +
		"  void foo(int a, int b) {\n" + 
		"    new X(0,\n" + 
		"  }\n" + 
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("new X(0,") + "new X(0,".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("new X(0,") + "new X(0,".length();

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
public void test0105() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0105/X.js",
		"package test0105;\n" + 
		"public class X {\n" + 
		"  Object o = ZZZZ\n" +
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
		"expectedTypesSignatures={Ljava.lang.Object;}\n" +
		"expectedTypesKeys={Ljava/lang/Object;}",
		result.context);
}
public void test0106() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0106/X.js",
		"package test0106;\n" + 
		"public class X {\n" + 
		"  Object o = ZZZZ\n" +
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
		"expectedTypesSignatures={Ljava.lang.Object;}\n" +
		"expectedTypesKeys={Ljava/lang/Object;}",
		result.context);
}
public void test0107() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0107/X.js",
		"package test0107;\n" + 
		"public class X {\n" + 
		"  Object o = ZZZZ\n" +
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
		"expectedTypesSignatures={Ljava.lang.Object;}\n" +
		"expectedTypesKeys={Ljava/lang/Object;}",
		result.context);
}
public void test0108() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0108/X.js",
		"package test0108;\n" + 
		"public class X {\n" + 
		"  Object o = \n" +
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("Object o = ") + "Object o = ".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("Object o = ") + "Object o = ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures={Ljava.lang.Object;}\n" +
		"expectedTypesKeys={Ljava/lang/Object;}",
		result.context);
}
public void test0109() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0109/X.js",
		"package test0109;\n" + 
		"public class X {\n" + 
		"  Object o = new ZZZZ\n" +
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
		"expectedTypesSignatures={Ljava.lang.Object;}\n" +
		"expectedTypesKeys={Ljava/lang/Object;}",
		result.context);
}
public void test0110() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0110/X.js",
		"package test0110;\n" + 
		"public class X {\n" + 
		"  Object o = new ZZZZ\n" +
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
		"expectedTypesSignatures={Ljava.lang.Object;}\n" +
		"expectedTypesKeys={Ljava/lang/Object;}",
		result.context);
}
public void test0111() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0111/X.js",
		"package test0111;\n" + 
		"public class X {\n" + 
		"  Object o = new ZZZZ\n" +
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
		"expectedTypesSignatures={Ljava.lang.Object;}\n" +
		"expectedTypesKeys={Ljava/lang/Object;}",
		result.context);
}
public void test0112() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0112/X.js",
		"package test0112;\n" + 
		"public class X {\n" + 
		"  Object o = new \n" +
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("Object o = new ") + "Object o = new ".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("Object o = new ") + "Object o = new ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures={Ljava.lang.Object;}\n" +
		"expectedTypesKeys={Ljava/lang/Object;}",
		result.context);
}
public void test0113() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0113/X.js",
		"package test0113;\n" + 
		"public class X {\n" + 
		"  Object o = new Object() {\n" +
		"    ZZZZ\n" +
		"  };\n" +
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
public void test0114() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0114/X.js",
		"package test0114;\n" + 
		"public class X {\n" + 
		"  Object o = new Object() {\n" +
		"    ZZZZ\n" +
		"  };\n" +
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
public void test0115() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0115/X.js",
		"package test0115;\n" + 
		"public class X {\n" + 
		"  Object o = new Object() {\n" +
		"    ZZZZ\n" +
		"  };\n" +
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
public void test0116() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0116/X.js",
		"package test0116;\n" + 
		"public class X {\n" + 
		"  Object o = new Object() {\n" +
		"    /**/\n" +
		"  };\n" +
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("/**/") + "/**/".length();
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("/**/") + "/**/".length();

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
public void test0117() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0117/X.js",
		"package test0117;\n" + 
		"public class X {\n" + 
		"  String s = \"ZZZZ\";\n" +
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("\"ZZZZ\"");
	int tokenEnd = tokenStart + "\"ZZZZ\"".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZZZ\"\n" +
		"completion token kind=TOKEN_KIND_STRING_LITERAL\n" +
		"expectedTypesSignatures={Ljava.lang.String;}\n" +
		"expectedTypesKeys={Ljava/lang/String;}",
		result.context);
}
public void test0118() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0118/X.js",
		"package test0118;\n" + 
		"public class X {\n" + 
		"  String s = \"ZZZZ\";\n" +
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("\"ZZZZ\"");
	int tokenEnd = tokenStart + "\"ZZZZ\"".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZ\"\n" +
		"completion token kind=TOKEN_KIND_STRING_LITERAL\n" +
		"expectedTypesSignatures={Ljava.lang.String;}\n" +
		"expectedTypesKeys={Ljava/lang/String;}",
		result.context);
}
public void test0119() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0119/X.js",
		"package test0119;\n" + 
		"public class X {\n" + 
		"  String s = \"ZZZZ\";\n" +
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("\"ZZZZ\"");
	int tokenEnd = tokenStart + "\"ZZZZ\"".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_STRING_LITERAL\n" +
		"expectedTypesSignatures={Ljava.lang.String;}\n" +
		"expectedTypesKeys={Ljava/lang/String;}",
		result.context);
}
public void test0120() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0120/X.js",
		"package test0120;\n" + 
		"public class X {\n" + 
		"  String s = \"ZZZZ\";\n" +
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("\"ZZZZ\"");
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("\"ZZZZ") + "".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures={Ljava.lang.String;}\n" +
		"expectedTypesKeys={Ljava/lang/String;}",
		result.context);
}
public void test0121() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0121/X.js",
		"package test0121;\n" + 
		"public class X {\n" + 
		"  String s = \"ZZZZ\";\n" +
		"}");
	
	String str = this.workingCopies[0].getSource();
	int cursorLocation = str.lastIndexOf("\"ZZZZ\"") + "\"ZZZZ\"".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=[-1, -1]\n" +
		"completion token=null\n" +
		"completion token kind=TOKEN_KIND_UNKNOWN\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
public void test0122() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0122/X.js",
		"package test0122;\n" + 
		"public class X {\n" + 
		"  String s = \"\";\n" +
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("\"\"");
	int tokenEnd = tokenStart + "\"\"".length() - 1;
	int cursorLocation = str.lastIndexOf("\"\"") + "\"".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_STRING_LITERAL\n" +
		"expectedTypesSignatures={Ljava.lang.String;}\n" +
		"expectedTypesKeys={Ljava/lang/String;}",
		result.context);
}
public void test0123() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0123/X.js",
		"package test0123;\n" + 
		"public class X {\n" + 
		"  String s = \"ZZZZ\n" +
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("\"ZZZZ");
	int tokenEnd = tokenStart + "\"ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZZZ\"\n" +
		"completion token kind=TOKEN_KIND_STRING_LITERAL\n" +
		"expectedTypesSignatures={Ljava.lang.String;}\n" +
		"expectedTypesKeys={Ljava/lang/String;}",
		result.context);
}
public void test0124() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0124/X.js",
		"package test0124;\n" + 
		"public class X {\n" + 
		"  String s = \"ZZZZ\n" +
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("\"ZZZZ");
	int tokenEnd = tokenStart + "\"ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "ZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZ\"\n" +
		"completion token kind=TOKEN_KIND_STRING_LITERAL\n" +
		"expectedTypesSignatures={Ljava.lang.String;}\n" +
		"expectedTypesKeys={Ljava/lang/String;}",
		result.context);
}
public void test0125() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0125/X.js",
		"package test0125;\n" + 
		"public class X {\n" + 
		"  String s = \"ZZZZ\n" +
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("\"ZZZZ");
	int tokenEnd = tokenStart + "\"ZZZZ".length() - 1;
	int cursorLocation = str.lastIndexOf("ZZZZ") + "".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_STRING_LITERAL\n" +
		"expectedTypesSignatures={Ljava.lang.String;}\n" +
		"expectedTypesKeys={Ljava/lang/String;}",
		result.context);
}
public void test0126() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0126/X.js",
		"package test0126;\n" + 
		"public class X {\n" + 
		"  String s = \"ZZZZ\n" +
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("\"ZZZZ");
	int tokenEnd = tokenStart + "".length() - 1;
	int cursorLocation = str.lastIndexOf("\"ZZZZ") + "".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures={Ljava.lang.String;}\n" +
		"expectedTypesKeys={Ljava/lang/String;}",
		result.context);
}
public void test0127() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0127/X.js",
		"package test0127;\n" + 
		"public class X {\n" + 
		"  String s = \"\n" +
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("\"");
	int tokenEnd = tokenStart + "\"".length() - 1;
	int cursorLocation = str.lastIndexOf("\"") + "\"".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_STRING_LITERAL\n" +
		"expectedTypesSignatures={Ljava.lang.String;}\n" +
		"expectedTypesKeys={Ljava/lang/String;}",
		result.context);
}
public void test0128() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0127/X.js",
		"package test0127;\n" + 
		"public class X {\n" + 
		"  String s0 = \"\n" +
		"  String s = \"ZZZZ\"\n" +
		"}");
	
	String str = this.workingCopies[0].getSource();
	int tokenStart = str.lastIndexOf("\"ZZZZ\"");
	int tokenEnd = tokenStart + "\"ZZZZ\"".length() - 1;
	int cursorLocation = str.lastIndexOf("\"ZZZZ\"") + "\"ZZZZ".length();

	CompletionResult result = contextComplete(this.workingCopies[0], cursorLocation);
	
	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"ZZZZ\"\n" +
		"completion token kind=TOKEN_KIND_STRING_LITERAL\n" +
		"expectedTypesSignatures={Ljava.lang.String;}\n" +
		"expectedTypesKeys={Ljava/lang/String;}",
		result.context);
}
}
