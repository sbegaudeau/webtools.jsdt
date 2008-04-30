/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
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

public class SnippetCompletionContextTests extends AbstractJavaModelCompletionTests implements RelevanceConstants {

public SnippetCompletionContextTests(String name) {
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
	return buildModelTestSuite(SnippetCompletionContextTests.class);
}
static {
	TESTS_NAMES = new String[]{"test0002"};
}
public void test0001() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0001/X.js",
		"package test0001;\n" + 
		"public class X {\n" + 
		"  /**/\n" + 
		"}");
	
	String snippet =
		"ZZZZ";
	
	IType type = this.workingCopies[0].getType("X");
	String str = this.workingCopies[0].getSource();
	int insertion = str.lastIndexOf("/**/");
	
	int tokenStart = snippet.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = snippet.lastIndexOf("ZZZZ") + "ZZZZ".length();
	
	
	CompletionResult result = snippetContextComplete(
			type,
			snippet,
			insertion,
			cursorLocation,
			false);

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
		"public class X {\n" + 
		"  /**/\n" + 
		"}");
	
	String snippet =
		"ZZZZ";
	
	IType type = this.workingCopies[0].getType("X");
	String str = this.workingCopies[0].getSource();
	int insertion = str.lastIndexOf("/**/");
	
	int tokenStart = snippet.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = snippet.lastIndexOf("ZZZZ");
	
	
	CompletionResult result = snippetContextComplete(
			type,
			snippet,
			insertion,
			cursorLocation,
			false);

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
		"public class X {\n" + 
		"  /**/\n" + 
		"}");
	
	String snippet =
		"ZZZZ";
	
	IType type = this.workingCopies[0].getType("X");
	String str = this.workingCopies[0].getSource();
	int insertion = str.lastIndexOf("/**/");
	
	int tokenStart = snippet.lastIndexOf("ZZZZ");
	int tokenEnd = tokenStart + "ZZZZ".length() - 1;
	int cursorLocation = snippet.lastIndexOf("ZZZZ") + "ZZ".length();
	
	
	CompletionResult result = snippetContextComplete(
			type,
			snippet,
			insertion,
			cursorLocation,
			false);

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
		"public class X {\n" + 
		"  /**/\n" + 
		"}");
	
	String snippet =
		"/**/";
	
	IType type = this.workingCopies[0].getType("X");
	String str = this.workingCopies[0].getSource();
	int insertion = str.lastIndexOf("/**/");
	
	int tokenStart = snippet.lastIndexOf("/**/");
	int tokenEnd = tokenStart + "".length();
	int cursorLocation = snippet.lastIndexOf("/**/") + "".length();
	
	
	CompletionResult result = snippetContextComplete(
			type,
			snippet,
			insertion,
			cursorLocation,
			false);

	assertResults(
		"completion offset="+(cursorLocation)+"\n" +
		"completion range=["+(tokenStart)+", "+(tokenEnd)+"]\n" +
		"completion token=\"\"\n" +
		"completion token kind=TOKEN_KIND_NAME\n" +
		"expectedTypesSignatures=null\n" +
		"expectedTypesKeys=null",
		result.context);
}
}
