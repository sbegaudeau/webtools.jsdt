/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.model;

import java.util.Hashtable;

import junit.framework.Test;

import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.internal.codeassist.CompletionEngine;
import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;

/**
 * Test class for completion in Javadoc comment of a method declaration.
 */
public class JavadocMethodCompletionModelTest extends AbstractJavadocCompletionModelTest {

public JavadocMethodCompletionModelTest(String name) {
	super(name);
}

static {
//	TESTS_NUMBERS = new int[] { 58 };
//	TESTS_RANGE = new int[] { 58, 69 };
}
public static Test suite() {
	return buildModelTestSuite(JavadocMethodCompletionModelTest.class);
}

/* (non-Javadoc)
 * @see org.eclipse.wst.jsdt.core.tests.model.AbstractJavadocCompletionModelTest#setUp()
 */
protected void setUp() throws Exception {
	super.setUp();
	setUpProjectOptions(CompilerOptions.VERSION_1_4);
}

/**
 * @tests Tests for tag names completion
 */
public void test001() throws JavaScriptModelException {
	String source =
		"package javadoc.methods;\n" + 
		"public class Test {\n" +
		"	/**\n" +
		"	 * Completion on empty tag name:\n" +
		"	 * 	@\n" +
		"	 */\n" +
		"	public void foo() {}\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/Test.js", source, true, "@");
	assertResults(
		"deprecated[JSDOC_BLOCK_TAG]{@deprecated, null, null, deprecated, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"exception[JSDOC_BLOCK_TAG]{@exception, null, null, exception, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"param[JSDOC_BLOCK_TAG]{@param, null, null, param, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"return[JSDOC_BLOCK_TAG]{@return, null, null, return, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"see[JSDOC_BLOCK_TAG]{@see, null, null, see, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"category[JSDOC_BLOCK_TAG]{@category, null, null, category, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"since[JSDOC_BLOCK_TAG]{@since, null, null, since, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"serialData[JSDOC_BLOCK_TAG]{@serialData, null, null, serialData, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"throws[JSDOC_BLOCK_TAG]{@throws, null, null, throws, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"link[JSDOC_INLINE_TAG]{{@link}, null, null, link, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"docRoot[JSDOC_INLINE_TAG]{{@docRoot}, null, null, docRoot, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"inheritDoc[JSDOC_INLINE_TAG]{{@inheritDoc}, null, null, inheritDoc, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"linkplain[JSDOC_INLINE_TAG]{{@linkplain}, null, null, linkplain, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"value[JSDOC_INLINE_TAG]{{@value}, null, null, value, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test002() throws JavaScriptModelException {
	String source =
		"package javadoc.methods;\n" + 
		"public class Test {\n" +
		"	/**\n" +
		"	 * Completion on impossible tag name:\n" +
		"	 * 	@aut\n" +
		"	 */\n" +
		"	public void foo() {}\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/Test.js", source, true, "@aut");
	assertResults("");
}

public void test003() throws JavaScriptModelException {
	String source =
		"package javadoc.methods;\n" + 
		"public class Test {\n" +
		"	/**\n" +
		"	 * Completion on one letter:\n" +
		"	 * 	@r\n" +
		"	 */\n" +
		"	public void foo() {}\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/Test.js", source, true, "@r");
	assertResults(
		"return[JSDOC_BLOCK_TAG]{@return, null, null, return, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test004() throws JavaScriptModelException {
	String source =
		"package javadoc.methods;\n" + 
		"public class Test {\n" +
		"	/**\n" +
		"	 * Completion with several letters:\n" +
		"	 * 	@ser\n" +
		"	 */\n" +
		"	public void foo() {}\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/Test.js", source, true, "@ser");
	assertResults(
		"serialData[JSDOC_BLOCK_TAG]{@serialData, null, null, serialData, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test005() throws JavaScriptModelException {
	String source =
		"package javadoc.methods;\n" + 
		"public class Test {\n" +
		"	/**\n" +
		"	 * Completion on full tag name:\n" +
		"	 * 	@inheritDoc\n" +
		"	 */\n" +
		"	public void foo() {}\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/Test.js", source, true, "@inheritDoc");
	assertResults(
		"inheritDoc[JSDOC_INLINE_TAG]{{@inheritDoc}, null, null, inheritDoc, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test006() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_3);
	String source =
		"package javadoc.methods;\n" + 
		"public class Test {\n" +
		"	/**\n" +
		"	 * Completion on empty tag name:\n" +
		"	 * 	@\n" +
		"	 */\n" +
		"	public void foo() {}\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/Test.js", source, true, "@");
	assertResults(
		"deprecated[JSDOC_BLOCK_TAG]{@deprecated, null, null, deprecated, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"exception[JSDOC_BLOCK_TAG]{@exception, null, null, exception, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"param[JSDOC_BLOCK_TAG]{@param, null, null, param, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"return[JSDOC_BLOCK_TAG]{@return, null, null, return, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"see[JSDOC_BLOCK_TAG]{@see, null, null, see, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"category[JSDOC_BLOCK_TAG]{@category, null, null, category, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"since[JSDOC_BLOCK_TAG]{@since, null, null, since, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"serialData[JSDOC_BLOCK_TAG]{@serialData, null, null, serialData, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"throws[JSDOC_BLOCK_TAG]{@throws, null, null, throws, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"link[JSDOC_INLINE_TAG]{{@link}, null, null, link, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"docRoot[JSDOC_INLINE_TAG]{{@docRoot}, null, null, docRoot, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test007() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods;\n" + 
		"public class Test {\n" +
		"	/**\n" +
		"	 * Completion on empty tag name:\n" +
		"	 * 	@\n" +
		"	 */\n" +
		"	public void foo() {}\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/Test.js", source, true, "@");
	assertResults(
		"deprecated[JSDOC_BLOCK_TAG]{@deprecated, null, null, deprecated, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"exception[JSDOC_BLOCK_TAG]{@exception, null, null, exception, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"param[JSDOC_BLOCK_TAG]{@param, null, null, param, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"return[JSDOC_BLOCK_TAG]{@return, null, null, return, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"see[JSDOC_BLOCK_TAG]{@see, null, null, see, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"category[JSDOC_BLOCK_TAG]{@category, null, null, category, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"since[JSDOC_BLOCK_TAG]{@since, null, null, since, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"serialData[JSDOC_BLOCK_TAG]{@serialData, null, null, serialData, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"throws[JSDOC_BLOCK_TAG]{@throws, null, null, throws, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"link[JSDOC_INLINE_TAG]{{@link}, null, null, link, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"docRoot[JSDOC_INLINE_TAG]{{@docRoot}, null, null, docRoot, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"inheritDoc[JSDOC_INLINE_TAG]{{@inheritDoc}, null, null, inheritDoc, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"linkplain[JSDOC_INLINE_TAG]{{@linkplain}, null, null, linkplain, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"value[JSDOC_INLINE_TAG]{{@value}, null, null, value, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"code[JSDOC_INLINE_TAG]{{@code}, null, null, code, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"literal[JSDOC_INLINE_TAG]{{@literal}, null, null, literal, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

/**
 * @tests Tests for types completion
 */
public void test010() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see BasicTestMethodsE\n" + 
		"	 */\n" + 
		"	public void foo() {}\n" + 
		"}\n" + 
		"class BasicTestMethodsException1 extends Exception{}\n" + 
		"class BasicTestMethodsException2 extends Exception{}\n" + 
		"class BasicTestMethodsExample {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BasicTestMethodsE");
	assertSortedResults(
		"BasicTestMethodsExample[TYPE_REF]{BasicTestMethodsExample, javadoc.methods.tags, Ljavadoc.methods.tags.BasicTestMethodsExample;, null, null, "+this.positions+R_DICUNR+"}\n" + 
		"BasicTestMethodsException1[TYPE_REF]{BasicTestMethodsException1, javadoc.methods.tags, Ljavadoc.methods.tags.BasicTestMethodsException1;, null, null, "+this.positions+R_DICUNR+"}\n" + 
		"BasicTestMethodsException2[TYPE_REF]{BasicTestMethodsException2, javadoc.methods.tags, Ljavadoc.methods.tags.BasicTestMethodsException2;, null, null, "+this.positions+R_DICUNR+"}"
	);
}

public void test011() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see \n" + 
		"	 */\n" + 
		"	public void foo() {}\n" + 
		"}\n" + 
		"class BasicTestMethodsException1 extends Exception{}\n" + 
		"class BasicTestMethodsException2 extends Exception{}\n" + 
		"class BasicTestMethodsExample {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "@see ", 0); // completion on empty token
	if(CompletionEngine.NO_TYPE_COMPLETION_ON_EMPTY_TOKEN) {
		assertResults("");
	} else {
		assertResults(
			"BasicTestMethods[TYPE_REF]{BasicTestMethods, javadoc.methods.tags, Ljavadoc.methods.tags.BasicTestMethods;, null, null, "+this.positions+R_DICUNR+"}\n" + 
			"BasicTestMethodsException1[TYPE_REF]{BasicTestMethodsException1, javadoc.methods.tags, Ljavadoc.methods.tags.BasicTestMethodsException1;, null, null, "+this.positions+R_DICUNR+"}\n" + 
			"BasicTestMethodsException2[TYPE_REF]{BasicTestMethodsException2, javadoc.methods.tags, Ljavadoc.methods.tags.BasicTestMethodsException2;, null, null, "+this.positions+R_DICUNR+"}\n" + 
			"BasicTestMethodsExample[TYPE_REF]{BasicTestMethodsExample, javadoc.methods.tags, Ljavadoc.methods.tags.BasicTestMethodsExample;, null, null, "+this.positions+R_DICUNR+"}");
	}
}

public void test012() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@throws BasicTestMethodsE\n" + 
		"	 */\n" + 
		"	public void foo() {}\n" + 
		"}\n" + 
		"class BasicTestMethodsException1 extends Exception{}\n" + 
		"class BasicTestMethodsException2 extends Exception{}\n" + 
		"class BasicTestMethodsExample {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BasicTestMethodsE");
	assertSortedResults(
		"BasicTestMethodsException1[TYPE_REF]{BasicTestMethodsException1, javadoc.methods.tags, Ljavadoc.methods.tags.BasicTestMethodsException1;, null, null, "+this.positions+R_DICUNRE+"}\n" + 
		"BasicTestMethodsException2[TYPE_REF]{BasicTestMethodsException2, javadoc.methods.tags, Ljavadoc.methods.tags.BasicTestMethodsException2;, null, null, "+this.positions+R_DICUNRE+"}\n" + 
		"BasicTestMethodsExample[TYPE_REF]{BasicTestMethodsExample, javadoc.methods.tags, Ljavadoc.methods.tags.BasicTestMethodsExample;, null, null, "+this.positions+R_DICUNR+"}"
	);
}

public void test013() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@throws BasicTestMethodsE\n" + 
		"	 */\n" + 
		"	public void foo() throws BasicTestMethodsException2 {}\n" + 
		"}\n" + 
		"class BasicTestMethodsException1 extends Exception{}\n" + 
		"class BasicTestMethodsException2 extends Exception{}\n" + 
		"class BasicTestMethodsExample {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BasicTestMethodsE");
	assertSortedResults(
		"BasicTestMethodsException2[TYPE_REF]{BasicTestMethodsException2, javadoc.methods.tags, Ljavadoc.methods.tags.BasicTestMethodsException2;, null, null, "+this.positions+R_DICUNREEET+"}\n" + 
		"BasicTestMethodsException1[TYPE_REF]{BasicTestMethodsException1, javadoc.methods.tags, Ljavadoc.methods.tags.BasicTestMethodsException1;, null, null, "+this.positions+R_DICUNRE+"}\n" + 
		"BasicTestMethodsExample[TYPE_REF]{BasicTestMethodsExample, javadoc.methods.tags, Ljavadoc.methods.tags.BasicTestMethodsExample;, null, null, "+this.positions+R_DICUNR+"}"
	);
}

public void test014() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@throws \n" + 
		"	 */\n" + 
		"	public void foo() throws BasicTestMethodsException {}\n" + 
		"}\n" + 
		"class BasicTestMethodsException extends Exception{}\n" + 
		"class BasicTestMethodsExample {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "@throws ", 0); // completion on empty token
	if(CompletionEngine.NO_TYPE_COMPLETION_ON_EMPTY_TOKEN) {
		assertResults(
			"BasicTestMethodsException[TYPE_REF]{BasicTestMethodsException, javadoc.methods.tags, Ljavadoc.methods.tags.BasicTestMethodsException;, null, null, "+this.positions+R_DICUNREET+"}"
		);
	} else {
		assertResults(
			"BasicTestMethods[TYPE_REF]{BasicTestMethods, javadoc.methods.tags, Ljavadoc.methods.tags.BasicTestMethods;, null, null, "+this.positions+R_DICUNR+"}\n" + 
			"BasicTestMethodsException[TYPE_REF]{BasicTestMethodsException, javadoc.methods.tags, Ljavadoc.methods.tags.BasicTestMethodsException;, null, null, "+this.positions+R_DICUNREEET+"}\n" + 
			"BasicTestMethodsExample[TYPE_REF]{BasicTestMethodsExample, javadoc.methods.tags, Ljavadoc.methods.tags.BasicTestMethodsExample;, null, null, "+this.positions+R_DICUNR+"}");
	}
}

public void test015() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@throws I\n" + 
		"	 * 		Note: there should be NO base types in proposals." + 
		"	 */\n" + 
		"	public void foo() {\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "I");
	assertResults(
		"IllegalMonitorStateException[TYPE_REF]{IllegalMonitorStateException, java.lang, Ljava.lang.IllegalMonitorStateException;, null, null, "+this.positions+R_DICUNRE+"}\n" + 
		"InterruptedException[TYPE_REF]{InterruptedException, java.lang, Ljava.lang.InterruptedException;, null, null, "+this.positions+R_DICUNRE+"}"
	);
}

public void test016() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@throws java.lang.I\n" + 
		"	 */\n" + 
		"	public void foo() throws InterruptedException {\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "java.lang.I");
	assertResults(
		"IllegalMonitorStateException[TYPE_REF]{IllegalMonitorStateException, java.lang, Ljava.lang.IllegalMonitorStateException;, null, null, "+this.positions+R_DICNRE+"}\n" + 
		"InterruptedException[TYPE_REF]{InterruptedException, java.lang, Ljava.lang.InterruptedException;, null, null, "+this.positions+R_DICNREEET+"}"
	);
}

/**
 * @tests Tests for fields completion
 */
public void test020() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #fo\n" + 
		"	 */\n" + 
		"	int foo;\n" + 
		"	void foo() {}\n" + 
		"}";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "fo");
	assertResults(
		"foo[FIELD_REF]{foo, Ljavadoc.methods.tags.BasicTestMethods;, I, foo, null, "+this.positions+R_DICNRNS+"}\n" + 
		"foo[FUNCTION_REF]{foo(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, foo, null, "+this.positions+R_DICNRNS+"}"
	);
}

public void test021() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see BasicTestMethods#fo\n" + 
		"	 */\n" + 
		"	int foo;\n" + 
		"	void foo() {}\n" + 
		"}";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "fo");
	assertResults(
		"foo[FIELD_REF]{foo, Ljavadoc.methods.tags.BasicTestMethods;, I, foo, null, "+this.positions+R_DICNRNS+"}\n" + 
		"foo[FUNCTION_REF]{foo(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, foo, null, "+this.positions+R_DICNRNS+"}"
	);
}

public void test022() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see javadoc.methods.tags.BasicTestMethods#fo\n" + 
		"	 */\n" + 
		"	int foo;\n" + 
		"	void foo() {}\n" + 
		"}";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "fo");
	assertResults(
		"foo[FIELD_REF]{foo, Ljavadoc.methods.tags.BasicTestMethods;, I, foo, null, "+this.positions+R_DICNRNS+"}\n" + 
		"foo[FUNCTION_REF]{foo(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, foo, null, "+this.positions+R_DICNRNS+"}"
	);
}

public void test023() throws JavaScriptModelException {
	String[] sources = {
		"/Completion/src/javadoc/methods/tags/BasicTestMethods.js",
			"package javadoc.methods.tags;\n" + 
			"public class BasicTestMethods {\n" + 
			"	/**\n" + 
			"	 * Completion after:\n" + 
			"	 * 	@see OtherFields#fo\n" + 
			"	 */\n" + 
			"	int foo;\n" +
			"}",
		"/Completion/src/javadoc/methods/tags/OtherFields.js",
			"package javadoc.methods.tags;\n" + 
			"public class OtherFields {\n" + 
			"	int foo;\n" + 
			"	void foo() {}\n" + 
			"}"
	};
	completeInJavadoc(sources, true, "fo");
	assertResults(
		"foo[FIELD_REF]{foo, Ljavadoc.methods.tags.OtherFields;, I, foo, null, "+this.positions+R_DICNRNS+"}\n" + 
		"foo[FUNCTION_REF]{foo(), Ljavadoc.methods.tags.OtherFields;, ()V, foo, null, "+this.positions+R_DICNRNS+"}"
	);
}

/**
 * @tests Tests for methods completion
 */
public void test030() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see fo\n" + 
		"	 */\n" + 
		"	void foo() {}\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "fo");
	assertResults("");
}

public void test031() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #fo\n" + 
		"	 */\n" + 
		"	<T> void foo() {}\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "fo");
	assertResults(
		"foo[FUNCTION_REF]{foo(), Ljavadoc.methods.tags.BasicTestMethods;, <T:Ljava.lang.Object;>()V, foo, null, "+this.positions+R_DICNRNS+"}"
	);
}

public void test032() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #ba\n" + 
		"	 * \n" + 
		"	 * Note that argument names are put in proposals although there are not while completing\n" + 
		"	 * in javadoc text {@link javadoc.text.BasicTestMethods }. This is due to the fact that while\n" + 
		"	 * completing in javadoc tags, it\'s JDT-UI which compute arguments, not JDT-CORE.\n" + 
		"	 */\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "ba");
	assertResults(
		"bar[FUNCTION_REF]{bar(String, boolean, Object), Ljavadoc.methods.tags.BasicTestMethods;, (Ljava.lang.String;ZLjava.lang.Object;)V, bar, (str, flag, obj), "+this.positions+R_DICNRNS+"}"
	);
}

public void test033() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #ba\n" + 
		"	 * \n" + 
		"	 * Note that argument names are put in proposals although there are not while completing\n" + 
		"	 * in javadoc text {@link javadoc.text.BasicTestMethods }. This is due to the fact that while\n" + 
		"	 * completing in javadoc tags, it\'s JDT-UI which compute arguments, not JDT-CORE.\n" + 
		"	 */\n" + 
		"	<T, U> void bar(String str, Class<T> clt, Class<U> clu) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "ba");
	assertResults(
		"bar[FUNCTION_REF]{bar(String, Class, Class), Ljavadoc.methods.tags.BasicTestMethods;, <T:Ljava.lang.Object;U:Ljava.lang.Object;>(Ljava.lang.String;Ljava.lang.Class<TT;>;Ljava.lang.Class<TU;>;)V, bar, (str, clt, clu), "+this.positions+R_DICNRNS+"}"
	);
}

public void test034() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see BasicTestMethods#ba\n" + 
		"	 * \n" + 
		"	 * Note that argument names are put in proposals although there are not while completing\n" + 
		"	 * in javadoc text {@link javadoc.text.BasicTestMethods }. This is due to the fact that while\n" + 
		"	 * completing in javadoc tags, it\'s JDT-UI which compute arguments, not JDT-CORE.\n" + 
		"	 */\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "ba");
	assertResults(
		"bar[FUNCTION_REF]{bar(String, boolean, Object), Ljavadoc.methods.tags.BasicTestMethods;, (Ljava.lang.String;ZLjava.lang.Object;)V, bar, (str, flag, obj), "+this.positions+R_DICNRNS+"}"
	);
}

public void test035() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see javadoc.methods.tags.BasicTestMethods#ba\n" + 
		"	 * \n" + 
		"	 * Note that argument names are put in proposals although there are not while completing\n" + 
		"	 * in javadoc text {@link javadoc.text.BasicTestMethods }. This is due to the fact that while\n" + 
		"	 * completing in javadoc tags, it\'s JDT-UI which compute arguments, not JDT-CORE.\n" + 
		"	 */\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "ba");
	assertResults(
		"bar[FUNCTION_REF]{bar(String, boolean, Object), Ljavadoc.methods.tags.BasicTestMethods;, (Ljava.lang.String;ZLjava.lang.Object;)V, bar, (str, flag, obj), "+this.positions+R_DICNRNS+"}"
	);
}

public void test036() throws JavaScriptModelException {
	String[] sources = {
		"/Completion/src/javadoc/methods/tags/BasicTestMethods.js",
			"package javadoc.methods.tags;\n" + 
			"public class BasicTestMethods {\n" + 
			"	/**\n" + 
			"	 * Completion after:\n" + 
			"	 * 	@see OtherTypes#fo\n" + 
			"	 */\n" + 
			"	void foo() {};\n" +
			"}",
		"/Completion/src/javadoc/methods/tags/OtherTypes.js",
			"package javadoc.methods.tags;\n" + 
			"public class OtherTypes {\n" + 
			"	void foo() {};\n" +
			"}"
	};
	completeInJavadoc(sources, true, "fo");
	assertResults(
		"foo[FUNCTION_REF]{foo(), Ljavadoc.methods.tags.OtherTypes;, ()V, foo, null, "+this.positions+R_DICNRNS+"}"
	);
}

public void test037() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #\n" + 
		"	 */\n" + 
		"	void foo() {}\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "#", 0); // completion on empty token
	assertResults(
		"foo[FUNCTION_REF]{foo(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, foo, null, "+this.positions+R_DICNRNS+"}\n" + 
		"bar[FUNCTION_REF]{bar(String, boolean, Object), Ljavadoc.methods.tags.BasicTestMethods;, (Ljava.lang.String;ZLjava.lang.Object;)V, bar, (str, flag, obj), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(long, int), Ljava.lang.Object;, (JI)V, wait, (millis, nanos), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(long), Ljava.lang.Object;, (J)V, wait, (millis), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(), Ljava.lang.Object;, ()V, wait, null, "+this.positions+R_DICNRNS+"}\n" + 
		"toString[FUNCTION_REF]{toString(), Ljava.lang.Object;, ()Ljava.lang.String;, toString, null, "+this.positions+R_DICNRNS+"}\n" + 
		"notifyAll[FUNCTION_REF]{notifyAll(), Ljava.lang.Object;, ()V, notifyAll, null, "+this.positions+R_DICNRNS+"}\n" + 
		"notify[FUNCTION_REF]{notify(), Ljava.lang.Object;, ()V, notify, null, "+this.positions+R_DICNRNS+"}\n" + 
		"hashCode[FUNCTION_REF]{hashCode(), Ljava.lang.Object;, ()I, hashCode, null, "+this.positions+R_DICNRNS+"}\n" + 
		"getClass[FUNCTION_REF]{getClass(), Ljava.lang.Object;, ()Ljava.lang.Class;, getClass, null, "+this.positions+R_DICNRNS+"}\n" + 
		"finalize[FUNCTION_REF]{finalize(), Ljava.lang.Object;, ()V, finalize, null, "+this.positions+R_DICNRNS+"}\n" + 
		"equals[FUNCTION_REF]{equals(Object), Ljava.lang.Object;, (Ljava.lang.Object;)Z, equals, (obj), "+this.positions+R_DICNRNS+"}\n" + 
		"clone[FUNCTION_REF]{clone(), Ljava.lang.Object;, ()Ljava.lang.Object;, clone, null, "+this.positions+R_DICNRNS+"}\n" + 
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, BasicTestMethods, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test038() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #\n" + 
		"	 */\n" + 
		"	<T> void foo() {}\n" + 
		"	<TParam1, TParam2> void bar(TParam1 tp1, TParam2 tp2) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "#", 0); // completion on empty token
	assertResults(
		"foo[FUNCTION_REF]{foo(), Ljavadoc.methods.tags.BasicTestMethods;, <T:Ljava.lang.Object;>()V, foo, null, "+this.positions+R_DICNRNS+"}\n" + 
		"bar[FUNCTION_REF]{bar(Object, Object), Ljavadoc.methods.tags.BasicTestMethods;, <TParam1:Ljava.lang.Object;TParam2:Ljava.lang.Object;>(TTParam1;TTParam2;)V, bar, (tp1, tp2), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(long, int), Ljava.lang.Object;, (JI)V, wait, (millis, nanos), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(long), Ljava.lang.Object;, (J)V, wait, (millis), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(), Ljava.lang.Object;, ()V, wait, null, "+this.positions+R_DICNRNS+"}\n" + 
		"toString[FUNCTION_REF]{toString(), Ljava.lang.Object;, ()Ljava.lang.String;, toString, null, "+this.positions+R_DICNRNS+"}\n" + 
		"notifyAll[FUNCTION_REF]{notifyAll(), Ljava.lang.Object;, ()V, notifyAll, null, "+this.positions+R_DICNRNS+"}\n" + 
		"notify[FUNCTION_REF]{notify(), Ljava.lang.Object;, ()V, notify, null, "+this.positions+R_DICNRNS+"}\n" + 
		"hashCode[FUNCTION_REF]{hashCode(), Ljava.lang.Object;, ()I, hashCode, null, "+this.positions+R_DICNRNS+"}\n" + 
		"getClass[FUNCTION_REF]{getClass(), Ljava.lang.Object;, ()Ljava.lang.Class;, getClass, null, "+this.positions+R_DICNRNS+"}\n" + 
		"finalize[FUNCTION_REF]{finalize(), Ljava.lang.Object;, ()V, finalize, null, "+this.positions+R_DICNRNS+"}\n" + 
		"equals[FUNCTION_REF]{equals(Object), Ljava.lang.Object;, (Ljava.lang.Object;)Z, equals, (obj), "+this.positions+R_DICNRNS+"}\n" + 
		"clone[FUNCTION_REF]{clone(), Ljava.lang.Object;, ()Ljava.lang.Object;, clone, null, "+this.positions+R_DICNRNS+"}\n" + 
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, BasicTestMethods, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test039() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see BasicTestMethods#\n" + 
		"	 */\n" + 
		"	void foo() {}\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "#", 0); // completion on empty token
	assertResults(
		"foo[FUNCTION_REF]{foo(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, foo, null, "+this.positions+R_DICNRNS+"}\n" + 
		"bar[FUNCTION_REF]{bar(String, boolean, Object), Ljavadoc.methods.tags.BasicTestMethods;, (Ljava.lang.String;ZLjava.lang.Object;)V, bar, (str, flag, obj), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(long, int), Ljava.lang.Object;, (JI)V, wait, (millis, nanos), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(long), Ljava.lang.Object;, (J)V, wait, (millis), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(), Ljava.lang.Object;, ()V, wait, null, "+this.positions+R_DICNRNS+"}\n" + 
		"toString[FUNCTION_REF]{toString(), Ljava.lang.Object;, ()Ljava.lang.String;, toString, null, "+this.positions+R_DICNRNS+"}\n" + 
		"notifyAll[FUNCTION_REF]{notifyAll(), Ljava.lang.Object;, ()V, notifyAll, null, "+this.positions+R_DICNRNS+"}\n" + 
		"notify[FUNCTION_REF]{notify(), Ljava.lang.Object;, ()V, notify, null, "+this.positions+R_DICNRNS+"}\n" + 
		"hashCode[FUNCTION_REF]{hashCode(), Ljava.lang.Object;, ()I, hashCode, null, "+this.positions+R_DICNRNS+"}\n" + 
		"getClass[FUNCTION_REF]{getClass(), Ljava.lang.Object;, ()Ljava.lang.Class;, getClass, null, "+this.positions+R_DICNRNS+"}\n" + 
		"finalize[FUNCTION_REF]{finalize(), Ljava.lang.Object;, ()V, finalize, null, "+this.positions+R_DICNRNS+"}\n" + 
		"equals[FUNCTION_REF]{equals(Object), Ljava.lang.Object;, (Ljava.lang.Object;)Z, equals, (obj), "+this.positions+R_DICNRNS+"}\n" + 
		"clone[FUNCTION_REF]{clone(), Ljava.lang.Object;, ()Ljava.lang.Object;, clone, null, "+this.positions+R_DICNRNS+"}\n" + 
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, BasicTestMethods, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test040() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see javadoc.methods.tags.BasicTestMethods#\n" + 
		"	 */\n" + 
		"	void foo() {}\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "#", 0); // completion on empty token
	assertResults(
		"foo[FUNCTION_REF]{foo(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, foo, null, "+this.positions+R_DICNRNS+"}\n" + 
		"bar[FUNCTION_REF]{bar(String, boolean, Object), Ljavadoc.methods.tags.BasicTestMethods;, (Ljava.lang.String;ZLjava.lang.Object;)V, bar, (str, flag, obj), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(long, int), Ljava.lang.Object;, (JI)V, wait, (millis, nanos), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(long), Ljava.lang.Object;, (J)V, wait, (millis), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(), Ljava.lang.Object;, ()V, wait, null, "+this.positions+R_DICNRNS+"}\n" + 
		"toString[FUNCTION_REF]{toString(), Ljava.lang.Object;, ()Ljava.lang.String;, toString, null, "+this.positions+R_DICNRNS+"}\n" + 
		"notifyAll[FUNCTION_REF]{notifyAll(), Ljava.lang.Object;, ()V, notifyAll, null, "+this.positions+R_DICNRNS+"}\n" + 
		"notify[FUNCTION_REF]{notify(), Ljava.lang.Object;, ()V, notify, null, "+this.positions+R_DICNRNS+"}\n" + 
		"hashCode[FUNCTION_REF]{hashCode(), Ljava.lang.Object;, ()I, hashCode, null, "+this.positions+R_DICNRNS+"}\n" + 
		"getClass[FUNCTION_REF]{getClass(), Ljava.lang.Object;, ()Ljava.lang.Class;, getClass, null, "+this.positions+R_DICNRNS+"}\n" + 
		"finalize[FUNCTION_REF]{finalize(), Ljava.lang.Object;, ()V, finalize, null, "+this.positions+R_DICNRNS+"}\n" + 
		"equals[FUNCTION_REF]{equals(Object), Ljava.lang.Object;, (Ljava.lang.Object;)Z, equals, (obj), "+this.positions+R_DICNRNS+"}\n" + 
		"clone[FUNCTION_REF]{clone(), Ljava.lang.Object;, ()Ljava.lang.Object;, clone, null, "+this.positions+R_DICNRNS+"}\n" + 
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, BasicTestMethods, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test041() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #bar(\n" + 
		"	 */\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "bar(");
	assertResults(
		"bar[FUNCTION_REF]{bar(String, boolean, Object), Ljavadoc.methods.tags.BasicTestMethods;, (Ljava.lang.String;ZLjava.lang.Object;)V, bar, (str, flag, obj), "+this.positions+R_DICENUNR+"}"
	);
}

public void test042() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #bar(Str\n" + 
		"	 */\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "Str");
	assertResults(
		"String[TYPE_REF]{String, java.lang, Ljava.lang.String;, null, null, "+this.positions+R_DICUNR+"}"
	);
}

public void test043() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #bar(java.lang.\n" + 
		"	 */\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "java.lang.");
	assertSortedResults(
		"Class[TYPE_REF]{Class, java.lang, Ljava.lang.Class;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"CloneNotSupportedException[TYPE_REF]{CloneNotSupportedException, java.lang, Ljava.lang.CloneNotSupportedException;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"Error[TYPE_REF]{Error, java.lang, Ljava.lang.Error;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"Exception[TYPE_REF]{Exception, java.lang, Ljava.lang.Exception;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"IllegalMonitorStateException[TYPE_REF]{IllegalMonitorStateException, java.lang, Ljava.lang.IllegalMonitorStateException;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"InterruptedException[TYPE_REF]{InterruptedException, java.lang, Ljava.lang.InterruptedException;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"Object[TYPE_REF]{Object, java.lang, Ljava.lang.Object;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"RuntimeException[TYPE_REF]{RuntimeException, java.lang, Ljava.lang.RuntimeException;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"String[TYPE_REF]{String, java.lang, Ljava.lang.String;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"Throwable[TYPE_REF]{Throwable, java.lang, Ljava.lang.Throwable;, null, null, "+this.positions+R_DICNR+"}"
	);
}

public void test044() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #bar(java.lang.St\n" + 
		"	 */\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "java.lang.St");
	assertResults(
		"String[TYPE_REF]{String, java.lang, Ljava.lang.String;, null, null, "+this.positions+R_DICNR+"}"
	);
}

public void test045() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #bar(String s\n" + 
		"	 */\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "bar(String s");
	assertResults(
		"bar[FUNCTION_REF]{bar(String, boolean, Object), Ljavadoc.methods.tags.BasicTestMethods;, (Ljava.lang.String;ZLjava.lang.Object;)V, bar, (str, flag, obj), "+this.positions+R_DICENUNR+"}"
	);
}

public void test046() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #bar(String str, \n" + 
		"	 */\n" + 
		"	// Note: Completion takes place just after trailoing comma (there's a space after)\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "bar(String str,");
	assertResults(
		"bar[FUNCTION_REF]{bar(String, boolean, Object), Ljavadoc.methods.tags.BasicTestMethods;, (Ljava.lang.String;ZLjava.lang.Object;)V, bar, (str, flag, obj), "+this.positions+R_DICENUNR+"}"
	);
}

public void test047() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #bar(String str, \n" + 
		"	 */\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "bar(String str, ");
	assertResults(
		"bar[FUNCTION_REF]{bar(String, boolean, Object), Ljavadoc.methods.tags.BasicTestMethods;, (Ljava.lang.String;ZLjava.lang.Object;)V, bar, (str, flag, obj), "+this.positions+R_DICENUNR+"}"
	);
}

public void test048() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #bar(String,\n" + 
		"	 */\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "bar(String,");
	assertResults(
		"bar[FUNCTION_REF]{bar(String, boolean, Object), Ljavadoc.methods.tags.BasicTestMethods;, (Ljava.lang.String;ZLjava.lang.Object;)V, bar, (str, flag, obj), "+this.positions+R_DICENUNR+"}"
	);
}

public void test049() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #bar(String str, bool\n" + 
		"	 */\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "bool");
	assertResults(
		"boolean[KEYWORD]{boolean, null, null, boolean, null, "+this.positions+R_DICNR+"}"
	);
}

/*
 * Specific case where we can complete but we don't want to as the prefix is not syntaxically correct
 */
public void test050() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #bar(String str, boolean,\n" + 
		"	 */\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "bar(String str, boolean,");
	assertResults("");
}

public void test051() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #bar(String str, boolean flag,\n" + 
		"	 */\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "bar(String str, boolean flag,");
	assertResults(
		"bar[FUNCTION_REF]{bar(String, boolean, Object), Ljavadoc.methods.tags.BasicTestMethods;, (Ljava.lang.String;ZLjava.lang.Object;)V, bar, (str, flag, obj), "+this.positions+R_DICENUNR+"}"
	);
}

public void test052() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #bar(String,boolean,\n" + 
		"	 */\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "bar(String,boolean,");
	assertResults(
		"bar[FUNCTION_REF]{bar(String, boolean, Object), Ljavadoc.methods.tags.BasicTestMethods;, (Ljava.lang.String;ZLjava.lang.Object;)V, bar, (str, flag, obj), "+this.positions+R_DICENUNR+"}"
	);
}

public void test053() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #bar(String,boolean,Object\n" + 
		"	 */\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "Object");
	assertResults(
		"Object[TYPE_REF]{Object, java.lang, Ljava.lang.Object;, null, null, "+this.positions+R_DICENUNR+"}"
	);
}

/*
 * Specific case where we can complete but we don't want to as the prefix is not syntaxically correct
 */
public void test054() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #bar(String, boolean, Object o\n" + 
		"	 */\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "bar(String, boolean, Object o");
	assertResults("");
}

public void test055() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #bar(String str, boolean flag, Object o\n" + 
		"	 */\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "bar(String str, boolean flag, Object o");
	assertResults(
		"bar[FUNCTION_REF]{bar(String, boolean, Object), Ljavadoc.methods.tags.BasicTestMethods;, (Ljava.lang.String;ZLjava.lang.Object;)V, bar, (str, flag, obj), "+this.positions+R_DICENUNR+"}"
	);
}

public void test056() throws JavaScriptModelException {
	String[] sources = {
		"/Completion/src/javadoc/methods/tags/BasicTestMethods.js",
			"package javadoc.methods.tags;\n" + 
			"public class BasicTestMethods {\n" + 
			"	/**\n" + 
			"	 * Completion after:\n" + 
			"	 * 	@see OtherTypes#foo(\n" + 
			"	 */\n" + 
			"	void foo() {};\n" +
			"}",
		"/Completion/src/javadoc/methods/tags/OtherTypes.js",
			"package javadoc.methods.tags;\n" + 
			"public class OtherTypes {\n" + 
			"	void foo(String str) {};\n" +
			"}"
	};
	completeInJavadoc(sources, true, "foo(");
	assertResults(
		"foo[FUNCTION_REF]{foo(String), Ljavadoc.methods.tags.OtherTypes;, (Ljava.lang.String;)V, foo, (str), "+this.positions+R_DICENUNR+"}"
	);
}

public void test057() throws JavaScriptModelException {
	String[] sources = {
		"/Completion/src/javadoc/methods/tags/BasicTestMethods.js",
			"package javadoc.methods.tags;\n" + 
			"public class BasicTestMethods {\n" + 
			"	/**\n" + 
			"	 * Completion after:\n" + 
			"	 * 	@see javadoc.methods.tags.OtherTypes#foo(\n" + 
			"	 */\n" + 
			"	void foo() {};\n" +
			"}",
		"/Completion/src/javadoc/methods/tags/OtherTypes.js",
			"package javadoc.methods.tags;\n" + 
			"public class OtherTypes {\n" + 
			"	void foo(String str) {};\n" +
			"}"
	};
	completeInJavadoc(sources, true, "foo(");
	assertResults(
		"foo[FUNCTION_REF]{foo(String), Ljavadoc.methods.tags.OtherTypes;, (Ljava.lang.String;)V, foo, (str), "+this.positions+R_DICENUNR+"}"
	);
}

public void test058() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see BasicTestMethods#method()\n" + 
		"	 */\n" + 
		"	void method() {}\n" + 
		"	void bar(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "meth", 2); // 2nd occurrence
	assertResults(
		"method[FUNCTION_REF]{method(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, method, null, "+this.positions+R_DICNRNS+"}"
	);
}

public void test059() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see BasicTestMethods#method()\n" + 
		"	 */\n" + 
		"	void method() {}\n" + 
		"	void method(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "meth", 2); // 2nd occurrence
	assertResults(
		"method[FUNCTION_REF]{method(String, boolean, Object), Ljavadoc.methods.tags.BasicTestMethods;, (Ljava.lang.String;ZLjava.lang.Object;)V, method, (str, flag, obj), "+this.positions+R_DICNRNS+"}\n" + 
		"method[FUNCTION_REF]{method(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, method, null, "+this.positions+R_DICNRNS+"}"
	);
}

public void test060() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see BasicTestMethods#method(String)\n" + 
		"	 */\n" + 
		"	void method() {}\n" + 
		"	void method(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "meth", 2); // 2nd occurrence
	assertResults(
		"method[FUNCTION_REF]{method(String, boolean, Object), Ljavadoc.methods.tags.BasicTestMethods;, (Ljava.lang.String;ZLjava.lang.Object;)V, method, (str, flag, obj), "+this.positions+R_DICNRNS+"}\n" + 
		"method[FUNCTION_REF]{method(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, method, null, "+this.positions+R_DICNRNS+"}"
	);
}

public void test061() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see BasicTestMethods#method(String,boolean,Object)\n" + 
		"	 */\n" + 
		"	void method() {}\n" + 
		"	void method(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "meth", 2); // 2nd occurrence
	assertResults(
		"method[FUNCTION_REF]{method(String, boolean, Object), Ljavadoc.methods.tags.BasicTestMethods;, (Ljava.lang.String;ZLjava.lang.Object;)V, method, (str, flag, obj), [116, 145], "+R_DICNRNS+"}\n" + 
		"method[FUNCTION_REF]{method(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, method, null, [116, 145], "+R_DICNRNS+"}"
	);
}

// TODO (frederic) See with David what to do on this case...
public void _test062() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see BasicTestMethods#method(String str,boolean,Object)\n" + 
		"	 */\n" + 
		"	void method() {}\n" + 
		"	void method(String str, boolean flag, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "meth", 2); // 2nd occurrence
	assertResults(
		"method[FUNCTION_REF]{method(String, boolean, Object), Ljavadoc.methods.tags.BasicTestMethods;, (Ljava.lang.String;ZLjava.lang.Object;)V, method, (str, flag, obj), "+this.positions+R_DICUNR+"}\n" + 
		"method[FUNCTION_REF]{method(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, method, null, "+this.positions+R_DICUNR+"}"
	);
}

/**
 * @tests Tests for method parameters completion
 */
public void test070() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param \n" + 
		"	 */\n" + 
		"	public String foo(String str) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "@param ", 0); // empty token
	assertResults(
		"str[JSDOC_PARAM_REF]{str, null, null, str, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}"
	);
}

public void test071() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param x\n" + 
		"	 */\n" + 
		"	public String foo(String xstr) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "x");
	assertResults(
		"xstr[JSDOC_PARAM_REF]{xstr, null, null, xstr, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}"
	);
}

public void test072() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param xstr\n" + 
		"	 */\n" + 
		"	public String foo(String xstr) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "xstr");
	assertResults(
		"xstr[JSDOC_PARAM_REF]{xstr, null, null, xstr, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}"
	);
}

public void test073() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param xstr\n" + 
		"	 */\n" + 
		"	public String foo(String xstr) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "x");
	assertResults(
		"xstr[JSDOC_PARAM_REF]{xstr, null, null, xstr, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}"
	);
}

public void test074() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param xx\n" + 
		"	 */\n" + 
		"	public String foo(String xstr) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "xx");
	assertResults("");
}

public void test075() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param xstr\n" + 
		"	 ** 	@param \n" + 
		"	 */\n" + 
		"	public String foo(String xstr) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "** 	@param ", 0); // empty token
	assertResults(	"");
}

public void test076() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param xstr\n" + 
		"	 * 	@param xstr\n" + 
		"	 */\n" + 
		"	public String foo(String xstr) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "xstr");
	assertResults("");
}

public void test077() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param xstr\n" + 
		"	 * 	@param xstr\n" + 
		"	 */\n" + 
		"	public String foo(String xstr) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "xstr", 2);
	assertResults("");
}

public void test078() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param xstr\n" + 
		"	 * 	@param xstr\n" + 
		"	 */\n" + 
		"	public String foo(String xstr, String xstr2) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "xstr");
	assertResults(
		"xstr2[JSDOC_PARAM_REF]{xstr2, null, null, xstr2, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}"
	);
}

public void test079() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param xstr\n" + 
		"	 * 	@param xstr\n" + 
		"	 */\n" + 
		"	public String foo(String xstr, String xstr2) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "xstr", 2); // 2nd occurence
	assertResults(
		"xstr2[JSDOC_PARAM_REF]{xstr2, null, null, xstr2, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}"
	);
}

public void test080() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param xstr\n" + 
		"	 * 	@param xstr2\n" + 
		"	 */\n" + 
		"	public String foo(String xstr, String xstr2) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "xstr");
	assertResults(
		"xstr[JSDOC_PARAM_REF]{xstr, null, null, xstr, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}"
	);
}

public void test081() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param xstr\n" + 
		"	 * 	@param xstr2\n" + 
		"	 */\n" + 
		"	public String foo(String xstr, String xstr2) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "xstr", 2); // 2nd occurence
	assertResults(
		"xstr2[JSDOC_PARAM_REF]{xstr2, null, null, xstr2, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}"
	);
}

public void test082() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param xstr\n" + 
		"	 * 	@param xstr\n" + 
		"	 * 	@param xstr2\n" + 
		"	 */\n" + 
		"	public String foo(String xstr, String xstr2) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "xstr");
	assertResults("");
}

public void test083() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param xstr\n" + 
		"	 * 	@param xstr\n" + 
		"	 * 	@param xstr2\n" + 
		"	 */\n" + 
		"	public String foo(String xstr, String xstr2) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "xstr", 2); // 2nd occurence
	assertResults("");
}

public void test084() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param xstr\n" + 
		"	 * 	@param xstr\n" + 
		"	 * 	@param xstr2\n" + 
		"	 */\n" + 
		"	public String foo(String xstr, String xstr2) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "xstr", 3); // 3rd position
	assertResults(
		"xstr2[JSDOC_PARAM_REF]{xstr2, null, null, xstr2, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}"
	);
}

public void test085() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param \n" + 
		"	 */\n" + 
		"	public String foo(String xstr, boolean flag, Object obj) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "@param ", 0); // empty token
	assertResults(
		"xstr[JSDOC_PARAM_REF]{xstr, null, null, xstr, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING+2)+"}\n" + 
		"flag[JSDOC_PARAM_REF]{flag, null, null, flag, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING+1)+"}\n" + 
		"obj[JSDOC_PARAM_REF]{obj, null, null, obj, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}"
	);
}

public void test086() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param xstr\n" + 
		"	 ** 	@param \n" + 
		"	 */\n" + 
		"	public String methodMultipleParam2(String xstr, boolean flag, Object obj) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "** 	@param ", 0); // empty token
	assertResults(
		"flag[JSDOC_PARAM_REF]{flag, null, null, flag, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING+1)+"}\n" + 
		"obj[JSDOC_PARAM_REF]{obj, null, null, obj, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}"
	);
}

public void test087() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param \n" + 
		"	 * 	@param flag\n" + 
		"	 */\n" + 
		"	public String methodMultipleParam3(String xstr, boolean flag, Object obj) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "@param ", 0); // empty token
	assertResults(
		"xstr[JSDOC_PARAM_REF]{xstr, null, null, xstr, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING+1)+"}\n" + 
		"obj[JSDOC_PARAM_REF]{obj, null, null, obj, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}"
	);
}

public void test088() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param obj\n" + 
		"	 * 	@param xstr\n" + 
		"	 ** 	@param \n" + 
		"	 */\n" + 
		"	public String methodMultipleParam4(String xstr, boolean flag, Object obj) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "** 	@param ", 0); // empty token
	assertResults(
		"flag[JSDOC_PARAM_REF]{flag, null, null, flag, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}"
	);
}

public void test089() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param \n" + 
		"	 * 	@param obj\n" + 
		"	 * 	@param xstr\n" + 
		"	 * 	@param flag\n" + 
		"	 */\n" + 
		"	public String methodMultipleParam5(String xstr, boolean flag, Object obj) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "@param ", 0); // empty token
	assertResults("");
}

public void test090() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param obj\n" + 
		"	 * 	@param xstr\n" + 
		"	 * 	@param flag\n" + 
		"	 */\n" + 
		"	public String methodMultipleParam5(String xstr, boolean flag, Object obj) {\n" + 
		"		return null;\n" + 
		"	}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "ob");
	assertResults(
		"obj[JSDOC_PARAM_REF]{obj, null, null, obj, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}"
	);
}

/**
 * @tests Tests for type parameters completion
 */
public void test100() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param \n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "@param ", 0); // empty token
	assertSortedResults(
		"xtm[JSDOC_PARAM_REF]{xtm, null, null, xtm, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING+1)+"}\n" +
		"xtc[JSDOC_PARAM_REF]{xtc, null, null, xtc, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}\n" +
		"TM[JSDOC_PARAM_REF]{<TM>, null, null, TM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test101() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param <TM>\n" + 
		"	 ** 	@param \n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "** 	@param ", 0); // empty token
	assertSortedResults(
		"xtm[JSDOC_PARAM_REF]{xtm, null, null, xtm, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING+1)+"}\n" +
		"xtc[JSDOC_PARAM_REF]{xtc, null, null, xtc, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}"
	);
}

public void test102() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param xtc\n" + 
		"	 * 	@param <TM>\n" + 
		"	 ** 	@param \n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true,"** 	@param ", 0); // empty token
	assertSortedResults(
		"xtm[JSDOC_PARAM_REF]{xtm, null, null, xtm, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}"
	);
}

public void test103() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param xtc\n" + 
		"	 ** 	@param \n" + 
		"	 * 	@param xtc\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true,"** 	@param ", 0); // empty token
	assertSortedResults(
		"xtm[JSDOC_PARAM_REF]{xtm, null, null, xtm, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}\n" +
		"TM[JSDOC_PARAM_REF]{<TM>, null, null, TM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test104() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 ** 	@param \n" + 
		"	 * 	@param xtc\n" + 
		"	 * 	@param xtm\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true,"** 	@param ", 0); // empty token
	assertSortedResults(
		"TM[JSDOC_PARAM_REF]{<TM>, null, null, TM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test105() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param xtc\n" + 
		"	 * 	@param xtm\n" + 
		"	 * 	@param <TM>\n" + 
		"	 ** 	@param \n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true,"** 	@param ", 0); // empty token
	assertSortedResults("");
}

public void test106() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param <\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "<", 2); // 2nd occurence
	assertSortedResults(
		"TM[JSDOC_PARAM_REF]{<TM>, null, null, TM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test107() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param <T\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "<T", 2); // 2nd occurence
	assertSortedResults(
		"TM[JSDOC_PARAM_REF]{<TM>, null, null, TM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test108() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param <TC\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "<TC", 2); // 2nd occurence
	assertSortedResults("");
}

public void test109() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param <TM>\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "<TM");
	assertSortedResults(
		"TM[JSDOC_PARAM_REF]{<TM>, null, null, TM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test110() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param <TM>\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "<TM>");
	assertSortedResults(
		"TM[JSDOC_PARAM_REF]{<TM>, null, null, TM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test111() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param <TM>\n" + 
		"	 * 	@param <TM>\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "<TM");
	assertSortedResults("");
}

public void test112() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param <TM>\n" + 
		"	 * 	@param <TM>\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "<TM>", 2); // 2nd occurence
	assertSortedResults("");
}

public void test113() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param ab\n" + 
		"	 */\n" + 
		"	void foo(Object ab1, Object ab2) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "@param ", 0);
	assertSortedResults(
		"ab1[JSDOC_PARAM_REF]{ab1, null, null, ab1, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING+1)+"}\n" + 
		"ab2[JSDOC_PARAM_REF]{ab2, null, null, ab2, null, "+this.positions+(JAVADOC_RELEVANCE+R_INTERESTING)+"}"
	);
}

public void test114() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param <ZZZ>\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "@param ", 0); // empty token
	assertSortedResults(
		"xtm[JSDOC_PARAM_REF]{xtm, null, null, xtm, null, "+this.positions+"14}\n" + 
		"xtc[JSDOC_PARAM_REF]{xtc, null, null, xtc, null, "+this.positions+"13}\n" + 
		"TM[JSDOC_PARAM_REF]{<TM>, null, null, TM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test115() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param <ZZZ>\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "<", 2); // 2nd occurrence
	assertSortedResults(
		"TM[JSDOC_PARAM_REF]{<TM>, null, null, TM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test116() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param <ZZZ>\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "Z");
	assertSortedResults("");
}

public void test117() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param <ZZZ>\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "ZZZ");
	assertSortedResults("");
}

public void test118() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param ZZZ>\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "@param ", 0); // empty token
	assertSortedResults(
		"xtm[JSDOC_PARAM_REF]{xtm, null, null, xtm, null, [105, 108], 14}\n" + 
		"xtc[JSDOC_PARAM_REF]{xtc, null, null, xtc, null, [105, 108], 13}\n" + 
		"TM[JSDOC_PARAM_REF]{<TM>, null, null, TM, null, [105, 108], "+JAVADOC_RELEVANCE+"}"
	);
}

public void test119() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param ZZZ>\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "Z");
	assertSortedResults("");
}

public void test120() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param ZZZ>\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "ZZZ");
	assertSortedResults("");
}

public void test121() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param ZZZ.\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "@param ", 0); // empty token
	assertSortedResults(
		"xtm[JSDOC_PARAM_REF]{xtm, null, null, xtm, null, "+this.positions+"14}\n" + 
		"xtc[JSDOC_PARAM_REF]{xtc, null, null, xtc, null, "+this.positions+"13}\n" + 
		"TM[JSDOC_PARAM_REF]{<TM>, null, null, TM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test122() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param ZZZ#\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "@param ", 0); // empty token
	assertSortedResults(
		"xtm[JSDOC_PARAM_REF]{xtm, null, null, xtm, null, "+this.positions+"14}\n" + 
		"xtc[JSDOC_PARAM_REF]{xtc, null, null, xtc, null, "+this.positions+"13}\n" + 
		"TM[JSDOC_PARAM_REF]{<TM>, null, null, TM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test123() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods<TC> {\n" +
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@param ZZZ?\n" + 
		"	 */\n" + 
		"	<TM> void foo(Class<TM> xtm, Class<TC> xtc) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "@param ", 0); // empty token
	assertSortedResults(
		"xtm[JSDOC_PARAM_REF]{xtm, null, null, xtm, null, "+this.positions+"14}\n" + 
		"xtc[JSDOC_PARAM_REF]{xtc, null, null, xtc, null, "+this.positions+"13}\n" + 
		"TM[JSDOC_PARAM_REF]{<TM>, null, null, TM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

/**
 * @tests Tests for constructors completion
 */
public void test130() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTest\n" + 
		"	 */\n" + 
		"	BasicTestMethods() {}\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BasicTest", 2); // 2nd occurence
	assertResults(
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(int, float, Class), Ljavadoc.methods.tags.BasicTestMethods;, (IFLjava.lang.Class;)V, BasicTestMethods, (xxx, real, clazz), "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, BasicTestMethods, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test131() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTest\n" + 
		"	 * @since 3.2\n" + 
		"	 */\n" + 
		"	BasicTestMethods() {}\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BasicTest", 2); // 2nd occurence
	assertResults(
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(int, float, Class), Ljavadoc.methods.tags.BasicTestMethods;, (IFLjava.lang.Class;)V, BasicTestMethods, (xxx, real, clazz), "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, BasicTestMethods, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test132() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTest\n" + 
		"	 */\n" + 
		"	BasicTestMethods() {}\n" + 
		"	<T> BasicTestMethods(int xxx, float real, Class<T> clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BasicTest", 2); // 2nd occurence
	assertResults(
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(int, float, Class), Ljavadoc.methods.tags.BasicTestMethods;, <T:Ljava.lang.Object;>(IFLjava.lang.Class<TT;>;)V, BasicTestMethods, (xxx, real, clazz), "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, BasicTestMethods, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test133() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see BasicTestMethods#BasicTest\n" + 
		"	 */\n" + 
		"	BasicTestMethods() {}\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BasicTest", 3); // 3rd occurence
	assertResults(
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(int, float, Class), Ljavadoc.methods.tags.BasicTestMethods;, (IFLjava.lang.Class;)V, BasicTestMethods, (xxx, real, clazz), "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, BasicTestMethods, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test134() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see javadoc.methods.tags.BasicTestMethods#BasicTest\n" + 
		"	 */\n" + 
		"	BasicTestMethods() {}\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BasicTest", 3); // 3rd occurence
	assertResults(
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(int, float, Class), Ljavadoc.methods.tags.BasicTestMethods;, (IFLjava.lang.Class;)V, BasicTestMethods, (xxx, real, clazz), "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, BasicTestMethods, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test135() throws JavaScriptModelException {
	String[] sources = {
		"/Completion/src/javadoc/methods/tags/BasicTestMethods.js",
			"package javadoc.methods.tags;\n" + 
			"public class BasicTestMethods {\n" + 
			"	/**\n" + 
			"	 * Completion after:\n" + 
			"	 * 	@see OtherTypes#O\n" + 
			"	 */\n" + 
			"	void foo() {};\n" +
			"}",
		"/Completion/src/javadoc/methods/tags/OtherTypes.js",
			"package javadoc.methods.tags;\n" + 
			"public class OtherTypes {\n" + 
			"	OtherTypes() {};\n" +
			"}"
	};
	completeInJavadoc(sources, true, "O", 2); // 2nd occurence
	assertResults(
		"OtherTypes[FUNCTION_REF<CONSTRUCTOR>]{OtherTypes(), Ljavadoc.methods.tags.OtherTypes;, ()V, OtherTypes, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test136() throws JavaScriptModelException {
	String[] sources = {
		"/Completion/src/javadoc/methods/tags/BasicTestMethods.js",
			"package javadoc.methods.tags;\n" + 
			"public class BasicTestMethods {\n" + 
			"	/**\n" + 
			"	 * Completion after:\n" + 
			"	 * 	@see OtherTypes#O implicit default constructor\n" + 
			"	 */\n" + 
			"	void foo() {};\n" +
			"}",
		"/Completion/src/javadoc/methods/tags/OtherTypes.js",
			"package javadoc.methods.tags;\n" + 
			"public class OtherTypes {\n" + 
			"}"
	};
	completeInJavadoc(sources, true, "O", 2); // 2nd occurence
	assertResults(
		"OtherTypes[FUNCTION_REF<CONSTRUCTOR>]{OtherTypes(), Ljavadoc.methods.tags.OtherTypes;, ()V, OtherTypes, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test137() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #\n" + 
		"	 */\n" + 
		"	BasicTestMethods() {}\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "#", 0); // empty token
	assertResults(
		"wait[FUNCTION_REF]{wait(long, int), Ljava.lang.Object;, (JI)V, wait, (millis, nanos), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(long), Ljava.lang.Object;, (J)V, wait, (millis), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(), Ljava.lang.Object;, ()V, wait, null, "+this.positions+R_DICNRNS+"}\n" + 
		"toString[FUNCTION_REF]{toString(), Ljava.lang.Object;, ()Ljava.lang.String;, toString, null, "+this.positions+R_DICNRNS+"}\n" + 
		"notifyAll[FUNCTION_REF]{notifyAll(), Ljava.lang.Object;, ()V, notifyAll, null, "+this.positions+R_DICNRNS+"}\n" + 
		"notify[FUNCTION_REF]{notify(), Ljava.lang.Object;, ()V, notify, null, "+this.positions+R_DICNRNS+"}\n" + 
		"hashCode[FUNCTION_REF]{hashCode(), Ljava.lang.Object;, ()I, hashCode, null, "+this.positions+R_DICNRNS+"}\n" + 
		"getClass[FUNCTION_REF]{getClass(), Ljava.lang.Object;, ()Ljava.lang.Class;, getClass, null, "+this.positions+R_DICNRNS+"}\n" + 
		"finalize[FUNCTION_REF]{finalize(), Ljava.lang.Object;, ()V, finalize, null, "+this.positions+R_DICNRNS+"}\n" + 
		"equals[FUNCTION_REF]{equals(Object), Ljava.lang.Object;, (Ljava.lang.Object;)Z, equals, (obj), "+this.positions+R_DICNRNS+"}\n" + 
		"clone[FUNCTION_REF]{clone(), Ljava.lang.Object;, ()Ljava.lang.Object;, clone, null, "+this.positions+R_DICNRNS+"}\n" + 
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(int, float, Class), Ljavadoc.methods.tags.BasicTestMethods;, (IFLjava.lang.Class;)V, BasicTestMethods, (xxx, real, clazz), "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, BasicTestMethods, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test138() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #\n" + 
		"	 * @since 3.2\n" + 
		"	 */\n" + 
		"	BasicTestMethods() {}\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "#", 0); // empty token
	assertResults(
		"wait[FUNCTION_REF]{wait(long, int), Ljava.lang.Object;, (JI)V, wait, (millis, nanos), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(long), Ljava.lang.Object;, (J)V, wait, (millis), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(), Ljava.lang.Object;, ()V, wait, null, "+this.positions+R_DICNRNS+"}\n" + 
		"toString[FUNCTION_REF]{toString(), Ljava.lang.Object;, ()Ljava.lang.String;, toString, null, "+this.positions+R_DICNRNS+"}\n" + 
		"notifyAll[FUNCTION_REF]{notifyAll(), Ljava.lang.Object;, ()V, notifyAll, null, "+this.positions+R_DICNRNS+"}\n" + 
		"notify[FUNCTION_REF]{notify(), Ljava.lang.Object;, ()V, notify, null, "+this.positions+R_DICNRNS+"}\n" + 
		"hashCode[FUNCTION_REF]{hashCode(), Ljava.lang.Object;, ()I, hashCode, null, "+this.positions+R_DICNRNS+"}\n" + 
		"getClass[FUNCTION_REF]{getClass(), Ljava.lang.Object;, ()Ljava.lang.Class;, getClass, null, "+this.positions+R_DICNRNS+"}\n" + 
		"finalize[FUNCTION_REF]{finalize(), Ljava.lang.Object;, ()V, finalize, null, "+this.positions+R_DICNRNS+"}\n" + 
		"equals[FUNCTION_REF]{equals(Object), Ljava.lang.Object;, (Ljava.lang.Object;)Z, equals, (obj), "+this.positions+R_DICNRNS+"}\n" + 
		"clone[FUNCTION_REF]{clone(), Ljava.lang.Object;, ()Ljava.lang.Object;, clone, null, "+this.positions+R_DICNRNS+"}\n" + 
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(int, float, Class), Ljavadoc.methods.tags.BasicTestMethods;, (IFLjava.lang.Class;)V, BasicTestMethods, (xxx, real, clazz), "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, BasicTestMethods, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test139() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #\n" + 
		"	 */\n" + 
		"	<T> BasicTestMethods() {}\n" + 
		"	<T, U> BasicTestMethods(int xxx, Class<T> cl1, Class<U> cl2) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "#", 0); // empty token
	assertResults(
		"wait[FUNCTION_REF]{wait(long, int), Ljava.lang.Object;, (JI)V, wait, (millis, nanos), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(long), Ljava.lang.Object;, (J)V, wait, (millis), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(), Ljava.lang.Object;, ()V, wait, null, "+this.positions+R_DICNRNS+"}\n" + 
		"toString[FUNCTION_REF]{toString(), Ljava.lang.Object;, ()Ljava.lang.String;, toString, null, "+this.positions+R_DICNRNS+"}\n" + 
		"notifyAll[FUNCTION_REF]{notifyAll(), Ljava.lang.Object;, ()V, notifyAll, null, "+this.positions+R_DICNRNS+"}\n" + 
		"notify[FUNCTION_REF]{notify(), Ljava.lang.Object;, ()V, notify, null, "+this.positions+R_DICNRNS+"}\n" + 
		"hashCode[FUNCTION_REF]{hashCode(), Ljava.lang.Object;, ()I, hashCode, null, "+this.positions+R_DICNRNS+"}\n" + 
		"getClass[FUNCTION_REF]{getClass(), Ljava.lang.Object;, ()Ljava.lang.Class;, getClass, null, "+this.positions+R_DICNRNS+"}\n" + 
		"finalize[FUNCTION_REF]{finalize(), Ljava.lang.Object;, ()V, finalize, null, "+this.positions+R_DICNRNS+"}\n" + 
		"equals[FUNCTION_REF]{equals(Object), Ljava.lang.Object;, (Ljava.lang.Object;)Z, equals, (obj), "+this.positions+R_DICNRNS+"}\n" + 
		"clone[FUNCTION_REF]{clone(), Ljava.lang.Object;, ()Ljava.lang.Object;, clone, null, "+this.positions+R_DICNRNS+"}\n" + 
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(int, Class, Class), Ljavadoc.methods.tags.BasicTestMethods;, <T:Ljava.lang.Object;U:Ljava.lang.Object;>(ILjava.lang.Class<TT;>;Ljava.lang.Class<TU;>;)V, BasicTestMethods, (xxx, cl1, cl2), "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(), Ljavadoc.methods.tags.BasicTestMethods;, <T:Ljava.lang.Object;>()V, BasicTestMethods, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test140() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see #\n" + 
		" */\n" + 
		"public class BasicTestMethods {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "#", 0); // empty token
	assertResults(
		"wait[FUNCTION_REF]{wait(long, int), Ljava.lang.Object;, (JI)V, wait, (millis, nanos), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(long), Ljava.lang.Object;, (J)V, wait, (millis), "+this.positions+R_DICNRNS+"}\n" + 
		"wait[FUNCTION_REF]{wait(), Ljava.lang.Object;, ()V, wait, null, "+this.positions+R_DICNRNS+"}\n" + 
		"toString[FUNCTION_REF]{toString(), Ljava.lang.Object;, ()Ljava.lang.String;, toString, null, "+this.positions+R_DICNRNS+"}\n" + 
		"notifyAll[FUNCTION_REF]{notifyAll(), Ljava.lang.Object;, ()V, notifyAll, null, "+this.positions+R_DICNRNS+"}\n" + 
		"notify[FUNCTION_REF]{notify(), Ljava.lang.Object;, ()V, notify, null, "+this.positions+R_DICNRNS+"}\n" + 
		"hashCode[FUNCTION_REF]{hashCode(), Ljava.lang.Object;, ()I, hashCode, null, "+this.positions+R_DICNRNS+"}\n" + 
		"getClass[FUNCTION_REF]{getClass(), Ljava.lang.Object;, ()Ljava.lang.Class;, getClass, null, "+this.positions+R_DICNRNS+"}\n" + 
		"finalize[FUNCTION_REF]{finalize(), Ljava.lang.Object;, ()V, finalize, null, "+this.positions+R_DICNRNS+"}\n" + 
		"equals[FUNCTION_REF]{equals(Object), Ljava.lang.Object;, (Ljava.lang.Object;)Z, equals, (obj), "+this.positions+R_DICNRNS+"}\n" + 
		"clone[FUNCTION_REF]{clone(), Ljava.lang.Object;, ()Ljava.lang.Object;, clone, null, "+this.positions+R_DICNRNS+"}\n" + 
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, BasicTestMethods, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test141() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTestMethods(\n" + 
		"	 */\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BasicTestMethods(");
	assertResults(
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(int, float, Class), Ljavadoc.methods.tags.BasicTestMethods;, (IFLjava.lang.Class;)V, BasicTestMethods, (xxx, real, clazz), "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test142() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTestMethods(\n" + 
		"	 * @since 3.2\n" + 
		"	 */\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BasicTestMethods(");
	assertResults(
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(int, float, Class), Ljavadoc.methods.tags.BasicTestMethods;, (IFLjava.lang.Class;)V, BasicTestMethods, (xxx, real, clazz), "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test143() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTestMethods( trailing text\n" + 
		"	 */\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BasicTestMethods(");
	assertResults(
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(int, float, Class), Ljavadoc.methods.tags.BasicTestMethods;, (IFLjava.lang.Class;)V, BasicTestMethods, (xxx, real, clazz), "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test144() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTestMethods(   ...\n" + 
		"	 */\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BasicTestMethods(");
	assertResults(
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(int, float, Class), Ljavadoc.methods.tags.BasicTestMethods;, (IFLjava.lang.Class;)V, BasicTestMethods, (xxx, real, clazz), "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test145() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTestMethods(        \n" + 
		"	 */\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BasicTestMethods(");
	assertResults(
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(int, float, Class), Ljavadoc.methods.tags.BasicTestMethods;, (IFLjava.lang.Class;)V, BasicTestMethods, (xxx, real, clazz), "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test146() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTestMethods(     ????\n" + 
		"	 */\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BasicTestMethods(");
	assertResults(
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(int, float, Class), Ljavadoc.methods.tags.BasicTestMethods;, (IFLjava.lang.Class;)V, BasicTestMethods, (xxx, real, clazz), "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test147() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTestMethods(  ,,\n" + 
		"	 */\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BasicTestMethods(");
	assertResults(
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(int, float, Class), Ljavadoc.methods.tags.BasicTestMethods;, (IFLjava.lang.Class;)V, BasicTestMethods, (xxx, real, clazz), "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test148() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see BasicTestMethods#BasicTestMethods(\n" + 
		"	 */\n" + 
		"	BasicTestMethods() {}\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BasicTestMethods(");
	assertResults(
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(int, float, Class), Ljavadoc.methods.tags.BasicTestMethods;, (IFLjava.lang.Class;)V, BasicTestMethods, (xxx, real, clazz), "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, BasicTestMethods, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test149() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see javadoc.methods.tags.BasicTestMethods#BasicTestMethods(\n" + 
		"	 */\n" + 
		"	void foo() {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BasicTestMethods(");
	assertResults(
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(), Ljavadoc.methods.tags.BasicTestMethods;, ()V, BasicTestMethods, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

// TODO (frederic) Reduce proposal as there's only a single valid proposal: int
public void test150() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTestMethods(in\n" + 
		"	 */\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "in");
	assertResults(
		"int[KEYWORD]{int, null, null, int, null, "+this.positions+R_DICNR+"}\n" + 
		"InterruptedException[TYPE_REF]{InterruptedException, java.lang, Ljava.lang.InterruptedException;, null, null, "+this.positions+R_DIUNR+"}"
	);
}

// TODO (frederic) Reduce proposal as there's only a single valid proposal: int
public void test151() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTestMethods(int\n" + 
		"	 */\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "int");
	assertResults(
		"int[KEYWORD]{int, null, null, int, null, "+this.positions+R_DICENNR+"}\n" + 
		"InterruptedException[TYPE_REF]{InterruptedException, java.lang, Ljava.lang.InterruptedException;, null, null, "+this.positions+R_DIUNR+"}"
	);
}

public void test152() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTestMethods(int aaa, fl\n" + 
		"	 */\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "fl");
	assertResults(
		"float[KEYWORD]{float, null, null, float, null, "+this.positions+R_DICNR+"}"
	);
}

public void test153() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTestMethods(int aaa, float\n" + 
		"	 */\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "float");
	assertResults(
		"float[KEYWORD]{float, null, null, float, null, "+this.positions+R_DICENNR+"}"
	);
}

public void test154() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTestMethods(int, float, Cla\n" + 
		"	 */\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "Cla");
	assertResults(
		"Class[TYPE_REF]{Class, java.lang, Ljava.lang.Class;, null, null, "+this.positions+R_DICUNR+"}"
	);
}

// TODO (frederic) Reduce proposal as there's only a single valid proposal: Class
public void test155() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTestMethods(int, float, java.lang.\n" + 
		"	 */\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "java.lang.");
	assertSortedResults(
		"Class[TYPE_REF]{Class, java.lang, Ljava.lang.Class;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"CloneNotSupportedException[TYPE_REF]{CloneNotSupportedException, java.lang, Ljava.lang.CloneNotSupportedException;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"Error[TYPE_REF]{Error, java.lang, Ljava.lang.Error;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"Exception[TYPE_REF]{Exception, java.lang, Ljava.lang.Exception;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"IllegalMonitorStateException[TYPE_REF]{IllegalMonitorStateException, java.lang, Ljava.lang.IllegalMonitorStateException;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"InterruptedException[TYPE_REF]{InterruptedException, java.lang, Ljava.lang.InterruptedException;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"Object[TYPE_REF]{Object, java.lang, Ljava.lang.Object;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"RuntimeException[TYPE_REF]{RuntimeException, java.lang, Ljava.lang.RuntimeException;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"String[TYPE_REF]{String, java.lang, Ljava.lang.String;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"Throwable[TYPE_REF]{Throwable, java.lang, Ljava.lang.Throwable;, null, null, "+this.positions+R_DICNR+"}"
	);
}

public void test156() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTestMethods(int, float, java.lang.Cla\n" + 
		"	 */\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "java.lang.Cla");
	assertResults(
		"Class[TYPE_REF]{Class, java.lang, Ljava.lang.Class;, null, null, "+this.positions+R_DICNR+"}"
	);
}

public void test157() throws JavaScriptModelException {
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	void foo() {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTestMethods(int, float, Class\n" + 
		"	 * \n" + 
		"	 */\n" + 
		"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "Class");
	assertResults(
		"Class[TYPE_REF]{Class, java.lang, Ljava.lang.Class;, null, null, "+this.positions+R_DICENUNR+"}"
	);
}
/**
 * @tests Tests for camel case completion
 */
public void test160() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	try {
		Hashtable options = new Hashtable(oldOptions);
		options.put(JavaScriptCore.CODEASSIST_CAMEL_CASE_MATCH, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		String source =
			"package javadoc.methods.tags;\n" + 
			"public class BasicTestMethods {\n" + 
			"	void foo() {}\n" + 
			"	/**\n" + 
			"	 * Completion after:\n" + 
			"	 * 	@see #BTM\n" + 
			"	 * \n" + 
			"	 */\n" + 
			"	BasicTestMethods(int xxx, float real, Class clazz) {}\n" + 
			"}\n";
		completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BTM");
		assertResults(
			"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(int, float, Class), Ljavadoc.methods.tags.BasicTestMethods;, (IFLjava.lang.Class;)V, BasicTestMethods, (xxx, real, clazz), "+this.positions+JAVADOC_RELEVANCE+"}"
		);
	} finally {
		JavaScriptCore.setOptions(oldOptions);
	}
}
public void test161() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	try {
		Hashtable options = new Hashtable(oldOptions);
		options.put(JavaScriptCore.CODEASSIST_CAMEL_CASE_MATCH, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		String source =
			"package javadoc.methods.tags;\n" + 
			"public class BasicTestMethods {\n" + 
			"	void oneTwoThree(int i) {}\n" + 
			"	/**\n" + 
			"	 * Completion after:\n" + 
			"	 * 	@see #oTT\n" + 
			"	 * \n" + 
			"	 */\n" + 
			"	BasicTestMethods() {}\n" + 
			"}\n";
		completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "oTT");
		assertResults(
			"oneTwoThree[FUNCTION_REF]{oneTwoThree(int), Ljavadoc.methods.tags.BasicTestMethods;, (I)V, oneTwoThree, (i), "+this.positions+"24}"
		);
	} finally {
		JavaScriptCore.setOptions(oldOptions);
	}
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=155824
public void test162() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	public void oneTwoThree(Object... o) {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #oneTwoT\n" + 
		"	 * \n" + 
		"	 */\n" + 
		"	BasicTestMethods() {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "oneTwoT", 2);
	assertResults(
		"oneTwoThree[FUNCTION_REF]{oneTwoThree(Object...), Ljavadoc.methods.tags.BasicTestMethods;, ([Ljava.lang.Object;)V, oneTwoThree, (o), "+this.positions+R_DICNRNS+"}"
	);
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=155824
public void test163() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	
	String source =
		"package javadoc.methods.tags;\n" + 
		"public class BasicTestMethods {\n" + 
		"	public BasicTestMethods(Object... o) {}\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #BasicTestMeth\n" + 
		"	 * \n" + 
		"	 */\n" + 
		"	void foo() {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/methods/tags/BasicTestMethods.js", source, true, "BasicTestMeth", 3);
	assertResults(
		"BasicTestMethods[FUNCTION_REF<CONSTRUCTOR>]{BasicTestMethods(Object...), Ljavadoc.methods.tags.BasicTestMethods;, ([Ljava.lang.Object;)V, BasicTestMethods, (o), "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}
}
