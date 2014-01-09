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
import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;

/**
 * Test class for completion in Javadoc comment of a field declaration.
 */
public class JavadocFieldCompletionModelTest extends AbstractJavadocCompletionModelTest {

public JavadocFieldCompletionModelTest(String name) {
	super(name);
}

static {
//	TESTS_RANGE = new int[] { 22, -1 };
//	TESTS_NUMBERS = new int[] { 16 };
}
public static Test suite() {
	return buildModelTestSuite(JavadocFieldCompletionModelTest.class);
}

/* (non-Javadoc)
 * @see org.eclipse.wst.jsdt.core.tests.model.AbstractJavaModelCompletionTests#setUp()
 */
protected void setUp() throws Exception {
	super.setUp();
	setUpProjectOptions(CompilerOptions.VERSION_1_4); // default compliance
}

/**
 * @category Tests for tag names completion
 */
public void test001() throws JavaScriptModelException {
	String source =
		"package javadoc.fields;\n" +
		"public class Test {\n" +
		"	/**\n" +
		"	 * Completion on empty tag name:\n" +
		"	 * 	@\n" +
		"	 */\n" +
		"	int field;\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/fields/Test.js", source, true, "@");
	assertResults(
		"deprecated[JSDOC_BLOCK_TAG]{@deprecated, null, null, deprecated, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"see[JSDOC_BLOCK_TAG]{@see, null, null, see, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"category[JSDOC_BLOCK_TAG]{@category, null, null, category, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"since[JSDOC_BLOCK_TAG]{@since, null, null, since, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"serial[JSDOC_BLOCK_TAG]{@serial, null, null, serial, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"serialField[JSDOC_BLOCK_TAG]{@serialField, null, null, serialField, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"link[JSDOC_INLINE_TAG]{{@link}, null, null, link, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"docRoot[JSDOC_INLINE_TAG]{{@docRoot}, null, null, docRoot, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"linkplain[JSDOC_INLINE_TAG]{{@linkplain}, null, null, linkplain, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"value[JSDOC_INLINE_TAG]{{@value}, null, null, value, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test002() throws JavaScriptModelException {
	String source =
		"package javadoc.fields;\n" +
		"public class Test {\n" +
		"	/**\n" +
		"	 * Completion on impossible tag name:\n" +
		"	 * 	@thr\n" +
		"	 */\n" +
		"	int field;\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/fields/Test.js", source, true, "@thr");
	assertResults("");
}

public void test003() throws JavaScriptModelException {
	String source =
		"package javadoc.fields;\n" +
		"public class Test {\n" +
		"	/**\n" +
		"	 * Completion on one letter:\n" +
		"	 * 	@v\n" +
		"	 */\n" +
		"	int field;\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/fields/Test.js", source, true, "@v");
	assertResults(
		"value[JSDOC_INLINE_TAG]{{@value}, null, null, value, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test004() throws JavaScriptModelException {
	String source =
		"package javadoc.fields;\n" +
		"public class Test {\n" +
		"	/**\n" +
		"	 * Completion with several letters:\n" +
		"	 * 	@ser\n" +
		"	 */\n" +
		"	int field;\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/fields/Test.js", source, true, "@ser");
	assertResults(
		"serial[JSDOC_BLOCK_TAG]{@serial, null, null, serial, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"serialField[JSDOC_BLOCK_TAG]{@serialField, null, null, serialField, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test005() throws JavaScriptModelException {
	String source =
		"package javadoc.fields;\n" +
		"public class Test {\n" +
		"	/**\n" +
		"	 * Completion on full tag name:\n" +
		"	 * 	@docRoot\n" +
		"	 */\n" +
		"	int field;\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/fields/Test.js", source, true, "@docRoot");
	assertResults(
		"docRoot[JSDOC_INLINE_TAG]{{@docRoot}, null, null, docRoot, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test006() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_3);
	String source =
		"package javadoc.fields;\n" +
		"public class Test {\n" +
		"	/**\n" +
		"	 * Completion on empty tag name:\n" +
		"	 * 	@\n" +
		"	 */\n" +
		"	int field;\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/fields/Test.js", source, true, "@");
	assertResults(
		"deprecated[JSDOC_BLOCK_TAG]{@deprecated, null, null, deprecated, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"see[JSDOC_BLOCK_TAG]{@see, null, null, see, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"category[JSDOC_BLOCK_TAG]{@category, null, null, category, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"since[JSDOC_BLOCK_TAG]{@since, null, null, since, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"serial[JSDOC_BLOCK_TAG]{@serial, null, null, serial, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"serialField[JSDOC_BLOCK_TAG]{@serialField, null, null, serialField, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"link[JSDOC_INLINE_TAG]{{@link}, null, null, link, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"docRoot[JSDOC_INLINE_TAG]{{@docRoot}, null, null, docRoot, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test007() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.fields;\n" +
		"public class Test {\n" +
		"	/**\n" +
		"	 * Completion on empty tag name:\n" +
		"	 * 	@\n" +
		"	 */\n" +
		"	int field;\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/fields/Test.js", source, true, "@");
	assertResults(
		"deprecated[JSDOC_BLOCK_TAG]{@deprecated, null, null, deprecated, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"see[JSDOC_BLOCK_TAG]{@see, null, null, see, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"category[JSDOC_BLOCK_TAG]{@category, null, null, category, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"since[JSDOC_BLOCK_TAG]{@since, null, null, since, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"serial[JSDOC_BLOCK_TAG]{@serial, null, null, serial, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"serialField[JSDOC_BLOCK_TAG]{@serialField, null, null, serialField, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"link[JSDOC_INLINE_TAG]{{@link}, null, null, link, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"docRoot[JSDOC_INLINE_TAG]{{@docRoot}, null, null, docRoot, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"linkplain[JSDOC_INLINE_TAG]{{@linkplain}, null, null, linkplain, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"value[JSDOC_INLINE_TAG]{{@value}, null, null, value, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"code[JSDOC_INLINE_TAG]{{@code}, null, null, code, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"literal[JSDOC_INLINE_TAG]{{@literal}, null, null, literal, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

/**
 * @category Tests for types completion
 */
public void test010() throws JavaScriptModelException {
	String source =
		"package javadoc.fields.tags;\n" + 
		"public class BasicTestFields {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see Obj\n" + 
		"	 */\n" + 
		"	int field;\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/fields/tags/BasicTestFields.js", source, true, "Obj");
	assertResults(
		"Object[TYPE_REF]{Object, java.lang, Ljava.lang.Object;, null, null, "+this.positions+R_DICUNR+"}"
	);
}

public void test011() throws JavaScriptModelException {
	String source =
		"package javadoc.fields.tags;\n" + 
		"public class BasicTestFields {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see BasicTest\n" + 
		"	 */\n" + 
		"	int field;\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/fields/tags/BasicTestFields.js", source, true, "BasicTest", 2);
	assertResults(
		"BasicTestFields[TYPE_REF]{BasicTestFields, javadoc.fields.tags, Ljavadoc.fields.tags.BasicTestFields;, null, null, "+this.positions+R_DICUNR+"}\n" + 
		"BasicTestReferences[TYPE_REF]{org.eclipse.wst.jsdt.core.tests.BasicTestReferences, org.eclipse.wst.jsdt.core.tests, Lorg.eclipse.wst.jsdt.core.tests.BasicTestReferences;, null, null, "+this.positions+R_DICNR+"}"
	);
}

public void test012() throws JavaScriptModelException {
	String source =
		"package javadoc.fields.tags;\n" + 
		"public class BasicTestFields {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see javadoc.fields.tags.BasicTest\n" + 
		"	 * 		Note: JDT-UI failed on this one\n" + 
		"	 */\n" + 
		"	int field;\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/fields/tags/BasicTestFields.js", source, true, "javadoc.fields.tags.BasicTest");
	assertResults(
		"BasicTestFields[TYPE_REF]{BasicTestFields, javadoc.fields.tags, Ljavadoc.fields.tags.BasicTestFields;, null, null, "+this.positions+R_DICNR+"}"
	);
}

public void test013() throws JavaScriptModelException {
	String source =
		"package javadoc.fields.tags;\n" + 
		"public class BasicTestFields {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see java.la\n" + 
		"	 * 		Note: JDT-UI fails on this one\n" + 
		"	 */\n" + 
		"	int field;\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/fields/tags/BasicTestFields.js", source, true, "java.la");
	assertResults(
		"java.lang[PACKAGE_REF]{java.lang, java.lang, null, null, null, "+this.positions+R_DICQNR+"}"
	);
}

public void test014() throws JavaScriptModelException {
	String source =
		"package javadoc.fields.tags;\n" + 
		"public class BasicTestFields {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see pack.Bin\n" + 
		"	 */\n" + 
		"	int field;\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/fields/tags/BasicTestFields.js", source, true, "pack.Bin");
	assertSortedResults(
		"Bin1[TYPE_REF]{pack.Bin1, pack, Lpack.Bin1;, null, null, "+this.positions+R_DICQNR+"}\n" + 
		"Bin2[TYPE_REF]{pack.Bin2, pack, Lpack.Bin2;, null, null, "+this.positions+R_DICQNR+"}\n" + 
		"Bin3[TYPE_REF]{pack.Bin3, pack, Lpack.Bin3;, null, null, "+this.positions+R_DICQNR+"}"
	);
}

public void test015() throws JavaScriptModelException {
	String source =
		"package javadoc.fields.tags;\n" + 
		"public class BasicTestFields {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see I\n" + 
		"	 * 		Note: completion list shoud not include base types.\n" + 
		"	 */\n" + 
		"	int field;\n" +
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/fields/tags/BasicTestFields.js", source, true, "I");
	assertSortedResults(
		"IllegalMonitorStateException[TYPE_REF]{IllegalMonitorStateException, java.lang, Ljava.lang.IllegalMonitorStateException;, null, null, "+this.positions+R_DICUNR+"}\n" + 
		"InterruptedException[TYPE_REF]{InterruptedException, java.lang, Ljava.lang.InterruptedException;, null, null, "+this.positions+R_DICUNR+"}"
	);
}

/**
 * @category Tests for fields completion
 */
public void test020() throws JavaScriptModelException {
	String source =
		"package javadoc.fields.tags;\n" + 
		"public class BasicTestFields {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #fo\n" + 
		"	 */\n" + 
		"	int foo;\n" + 
		"}";
	completeInJavadoc("/Completion/src/javadoc/fields/tags/BasicTestFields.js", source, true, "fo");
	assertResults(
		"foo[FIELD_REF]{foo, Ljavadoc.fields.tags.BasicTestFields;, I, foo, null, "+this.positions+R_DICNRNS+"}"
	);
}

public void test021() throws JavaScriptModelException {
	String source =
		"package javadoc.fields.tags;\n" + 
		"public class BasicTestFields {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see BasicTestFields#fo\n" + 
		"	 */\n" + 
		"	int foo;\n" + 
		"}";
	completeInJavadoc("/Completion/src/javadoc/fields/tags/BasicTestFields.js", source, true, "fo");
	assertResults(
		"foo[FIELD_REF]{foo, Ljavadoc.fields.tags.BasicTestFields;, I, foo, null, "+this.positions+R_DICNRNS+"}"
	);
}

public void test022() throws JavaScriptModelException {
	String source =
		"package javadoc.fields.tags;\n" + 
		"public class BasicTestFields {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see javadoc.fields.tags.BasicTestFields#fo\n" + 
		"	 */\n" + 
		"	int foo;\n" + 
		"}";
	completeInJavadoc("/Completion/src/javadoc/fields/tags/BasicTestFields.js", source, true, "fo");
	assertResults(
		"foo[FIELD_REF]{foo, Ljavadoc.fields.tags.BasicTestFields;, I, foo, null, "+this.positions+R_DICNRNS+"}"
	);
}

public void test023() throws JavaScriptModelException {
	String[] sources = {
		"/Completion/src/javadoc/fields/tags/BasicTestFields.js",
			"package javadoc.fields.tags;\n" + 
			"public class BasicTestFields {\n" + 
			"	/**\n" + 
			"	 * Completion after:\n" + 
			"	 * 	@see OtherFields#oth\n" + 
			"	 */\n" + 
			"	int foo;\n" +
			"}",
		"/Completion/src/javadoc/fields/tags/OtherFields.js",
			"package javadoc.fields.tags;\n" + 
			"public class OtherFields {\n" + 
			"	int other;\n" +
			"}"
	};
	completeInJavadoc(sources, true, "oth");
	assertResults(
		"other[FIELD_REF]{other, Ljavadoc.fields.tags.OtherFields;, I, other, null, "+this.positions+R_DICNRNS+"}"
	);
}

public void test024() throws JavaScriptModelException {
	String source =
		"package javadoc.fields.tags;\n" + 
		"public class BasicTestFields {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see #\n" + 
		"	 */\n" + 
		"	int foo;\n" + 
		"	Object obj;\n" + 
		"}";
	completeInJavadoc("/Completion/src/javadoc/fields/tags/BasicTestFields.js", source, true, "#", 0); // completion on empty token
	assertResults(
		"obj[FIELD_REF]{obj, Ljavadoc.fields.tags.BasicTestFields;, Ljava.lang.Object;, obj, null, "+this.positions+R_DICNRNS+"}\n" + 
		"foo[FIELD_REF]{foo, Ljavadoc.fields.tags.BasicTestFields;, I, foo, null, "+this.positions+R_DICNRNS+"}\n" + 
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
		"BasicTestFields[FUNCTION_REF<CONSTRUCTOR>]{BasicTestFields(), Ljavadoc.fields.tags.BasicTestFields;, ()V, BasicTestFields, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test025() throws JavaScriptModelException {
	String source =
		"package javadoc.fields.tags;\n" + 
		"public class BasicTestFields {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see BasicTestFields#\n" + 
		"	 */\n" + 
		"	int foo;\n" + 
		"	Object obj;\n" + 
		"}";
	completeInJavadoc("/Completion/src/javadoc/fields/tags/BasicTestFields.js", source, true, "#", 0); // completion on empty token
	assertResults(
		"obj[FIELD_REF]{obj, Ljavadoc.fields.tags.BasicTestFields;, Ljava.lang.Object;, obj, null, "+this.positions+R_DICNRNS+"}\n" + 
		"foo[FIELD_REF]{foo, Ljavadoc.fields.tags.BasicTestFields;, I, foo, null, "+this.positions+R_DICNRNS+"}\n" + 
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
		"BasicTestFields[FUNCTION_REF<CONSTRUCTOR>]{BasicTestFields(), Ljavadoc.fields.tags.BasicTestFields;, ()V, BasicTestFields, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test026() throws JavaScriptModelException {
	String source =
		"package javadoc.fields.tags;\n" + 
		"public class BasicTestFields {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see javadoc.fields.tags.BasicTestFields#\n" + 
		"	 */\n" + 
		"	int foo;\n" + 
		"	Object obj;\n" + 
		"}";
	completeInJavadoc("/Completion/src/javadoc/fields/tags/BasicTestFields.js", source, true, "#", 0); // completion on empty token
	assertResults(
		"obj[FIELD_REF]{obj, Ljavadoc.fields.tags.BasicTestFields;, Ljava.lang.Object;, obj, null, "+this.positions+R_DICNRNS+"}\n" + 
		"foo[FIELD_REF]{foo, Ljavadoc.fields.tags.BasicTestFields;, I, foo, null, "+this.positions+R_DICNRNS+"}\n" + 
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
		"BasicTestFields[FUNCTION_REF<CONSTRUCTOR>]{BasicTestFields(), Ljavadoc.fields.tags.BasicTestFields;, ()V, BasicTestFields, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test027() throws JavaScriptModelException {
	String source =
		"package javadoc.fields.tags;\n" + 
		"public class BasicTestFields {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see BasicTestReferences#FIE\n" + 
		"	 */\n" + 
		"	int foo;\n" + 
		"	Object obj;\n" + 
		"}";
	completeInJavadoc("/Completion/src/javadoc/fields/tags/BasicTestFields.js", source, true, "FIE");
	assertResults("");
}

public void test028() throws JavaScriptModelException {
	String source =
		"package javadoc.fields.tags;\n" + 
		"public class BasicTestFields {\n" + 
		"	/**\n" + 
		"	 * Completion after:\n" + 
		"	 * 	@see org.eclipse.wst.jsdt.core.tests.BasicTestReferences#FIE\n" + 
		"	 */\n" + 
		"	int foo;\n" + 
		"	Object obj;\n" + 
		"}";
	completeInJavadoc("/Completion/src/javadoc/fields/tags/BasicTestFields.js", source, true, "FIE");
	assertResults(
		"FIELD[FIELD_REF]{FIELD, Lorg.eclipse.wst.jsdt.core.tests.BasicTestReferences;, I, FIELD, null, "+this.positions+R_DICNR+"}"
	);
}
/**
 * @tests Tests for camel case completion
 */
public void test030() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	try {
		Hashtable options = new Hashtable(oldOptions);
		options.put(JavaScriptCore.CODEASSIST_CAMEL_CASE_MATCH, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		String source =
			"package javadoc.fields.tags;\n" + 
			"public class BasicTestFields {\n" + 
			"	Object oneTwoThree;\n" + 
			"	/**\n" + 
			"	 * Completion after:\n" + 
			"	 * 	@see #oTT\n" + 
			"	 */\n" + 
			"	int foo;\n" + 
			
			"}";
		completeInJavadoc("/Completion/src/javadoc/fields/tags/BasicTestFields.js", source, true, "oTT");
		assertResults(
			"oneTwoThree[FIELD_REF]{oneTwoThree, Ljavadoc.fields.tags.BasicTestFields;, Ljava.lang.Object;, oneTwoThree, null, "+this.positions+"24}"
		);
	} finally {
		JavaScriptCore.setOptions(oldOptions);
	}
}
}
