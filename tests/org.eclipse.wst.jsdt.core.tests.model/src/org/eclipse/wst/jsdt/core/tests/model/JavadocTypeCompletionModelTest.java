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

import org.eclipse.wst.jsdt.core.CompletionProposal;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;

/**
 * Test class for completion in Javadoc comment of a type declaration.
 */
public class JavadocTypeCompletionModelTest extends AbstractJavadocCompletionModelTest {

public JavadocTypeCompletionModelTest(String name) {
	super(name);
}

static {
//	TESTS_RANGE = new int[] { 22, -1 };
//	TESTS_NUMBERS = new int[] { 20 };
}
public static Test suite() {
	return buildModelTestSuite(JavadocTypeCompletionModelTest.class);
}

/* (non-Javadoc)
 * @see org.eclipse.wst.jsdt.core.tests.model.AbstractJavadocCompletionModelTest#setUp()
 */
protected void setUp() throws Exception {
	super.setUp();
	setUpProjectOptions(CompilerOptions.VERSION_1_4);
}

/**
 * @category Tests for tag names completion
 */
public void test001() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" +
		" * Completion on empty tag name:\n" +
		" * 	@\n" +
		" */\n" +
		"public class Test {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/Test.js", source, true, "@");
	assertResults(
		"author[JSDOC_BLOCK_TAG]{@author, null, null, author, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"deprecated[JSDOC_BLOCK_TAG]{@deprecated, null, null, deprecated, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"see[JSDOC_BLOCK_TAG]{@see, null, null, see, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"version[JSDOC_BLOCK_TAG]{@version, null, null, version, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"category[JSDOC_BLOCK_TAG]{@category, null, null, category, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"since[JSDOC_BLOCK_TAG]{@since, null, null, since, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"serial[JSDOC_BLOCK_TAG]{@serial, null, null, serial, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"link[JSDOC_INLINE_TAG]{{@link}, null, null, link, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"docRoot[JSDOC_INLINE_TAG]{{@docRoot}, null, null, docRoot, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"linkplain[JSDOC_INLINE_TAG]{{@linkplain}, null, null, linkplain, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"value[JSDOC_INLINE_TAG]{{@value}, null, null, value, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test002() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" +
		" * Completion on impossible tag name:\n" +
		" * 	@par\n" +
		" */\n" +
		"public class Test {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/Test.js", source, true, "@par");
	assertResults("");
}

public void test003() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" +
		" * Completion on one letter:\n" +
		" * 	@v\n" +
		" */\n" +
		"public class Test {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/Test.js", source, true, "@v");
	assertResults(
		"version[JSDOC_BLOCK_TAG]{@version, null, null, version, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"value[JSDOC_INLINE_TAG]{{@value}, null, null, value, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test004() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" +
		" * Completion with several letters:\n" +
		" * 	@deprec\n" +
		" */\n" +
		"public class Test {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/Test.js", source, true, "@deprec");
	assertResults(
		"deprecated[JSDOC_BLOCK_TAG]{@deprecated, null, null, deprecated, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test005() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" +
		" * Completion on full tag name:\n" +
		" * 	@link\n" +
		" */\n" +
		"public class Test {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/Test.js", source, true, "@link");
	assertResults(
		"link[JSDOC_INLINE_TAG]{{@link}, null, null, link, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"linkplain[JSDOC_INLINE_TAG]{{@linkplain}, null, null, linkplain, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test006() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" +
		" * Completion on full tag name:\n" +
		" * 	@link\n" +
		" */\n" +
		"public class Test {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/Test.js", source, true, "@li");
	assertResults(
		"link[JSDOC_INLINE_TAG]{{@link}, null, null, link, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"linkplain[JSDOC_INLINE_TAG]{{@linkplain}, null, null, linkplain, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test007() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_3);
	String source =
		"package javadoc.types;\n" + 
		"/**\n" +
		" * Completion on empty tag name:\n" +
		" * 	@\n" +
		" */\n" +
		"// Note: this test should be done using compliance 1.3\n" +
		"public class Test {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/Test.js", source, true, "@");
	assertResults(
		"author[JSDOC_BLOCK_TAG]{@author, null, null, author, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"deprecated[JSDOC_BLOCK_TAG]{@deprecated, null, null, deprecated, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"see[JSDOC_BLOCK_TAG]{@see, null, null, see, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"version[JSDOC_BLOCK_TAG]{@version, null, null, version, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"category[JSDOC_BLOCK_TAG]{@category, null, null, category, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"since[JSDOC_BLOCK_TAG]{@since, null, null, since, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"serial[JSDOC_BLOCK_TAG]{@serial, null, null, serial, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"link[JSDOC_INLINE_TAG]{{@link}, null, null, link, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"docRoot[JSDOC_INLINE_TAG]{{@docRoot}, null, null, docRoot, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test008() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.types;\n" + 
		"/**\n" +
		" * Completion on empty tag name:\n" +
		" * 	@\n" +
		" */\n" +
		"// Note: this test should be done using compliance 1.5\n" +
		"public class Test<T> {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/Test.js", source, true, "@");
	assertResults(
		"author[JSDOC_BLOCK_TAG]{@author, null, null, author, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"deprecated[JSDOC_BLOCK_TAG]{@deprecated, null, null, deprecated, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"param[JSDOC_BLOCK_TAG]{@param, null, null, param, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"see[JSDOC_BLOCK_TAG]{@see, null, null, see, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"version[JSDOC_BLOCK_TAG]{@version, null, null, version, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"category[JSDOC_BLOCK_TAG]{@category, null, null, category, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"since[JSDOC_BLOCK_TAG]{@since, null, null, since, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
		"serial[JSDOC_BLOCK_TAG]{@serial, null, null, serial, null, "+this.positions+JAVADOC_RELEVANCE+"}\n" + 
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
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see Obj\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "Obj");
	assertResults(
		"Object[TYPE_REF]{Object, java.lang, Ljava.lang.Object;, null, null, "+this.positions+R_DICUNR+"}"
	);
}

public void test011() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see BasicTest\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "BasicTest");
	assertResults(
		"BasicTestTypes[TYPE_REF]{BasicTestTypes, javadoc.types.tags, Ljavadoc.types.tags.BasicTestTypes;, null, null, "+this.positions+R_DICUNR+"}\n" + 
		"BasicTestReferences[TYPE_REF]{org.eclipse.wst.jsdt.core.tests.BasicTestReferences, org.eclipse.wst.jsdt.core.tests, Lorg.eclipse.wst.jsdt.core.tests.BasicTestReferences;, null, null, "+this.positions+R_DICNR+"}"
	);
}

public void test012() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see BasicTestTypes\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "BasicTest");
	assertResults(
		"BasicTestTypes[TYPE_REF]{BasicTestTypes, javadoc.types.tags, Ljavadoc.types.tags.BasicTestTypes;, null, null, "+this.positions+R_DICUNR+"}\n" + 
		"BasicTestReferences[TYPE_REF]{org.eclipse.wst.jsdt.core.tests.BasicTestReferences, org.eclipse.wst.jsdt.core.tests, Lorg.eclipse.wst.jsdt.core.tests.BasicTestReferences;, null, null, "+this.positions+R_DICNR+"}"
	);
}

public void test013() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see BasicTest\n" + 
		" */\n" + 
		"public class BasicTestTypes<TPARAM> {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "BasicTest");
	assertResults(
		"BasicTestTypes<TPARAM>[TYPE_REF]{BasicTestTypes, javadoc.types.tags, Ljavadoc.types.tags.BasicTestTypes<TTPARAM;>;, null, null, "+this.positions+R_DICUNR+"}\n" + 
		"BasicTestReferences[TYPE_REF]{org.eclipse.wst.jsdt.core.tests.BasicTestReferences, org.eclipse.wst.jsdt.core.tests, Lorg.eclipse.wst.jsdt.core.tests.BasicTestReferences;, null, null, "+this.positions+R_DICNR+"}"
	);
}

public void test014() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see javadoc.types.tags.BasicTest\n" + 
		" * 		Note: JDT-UI failed on this one\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "javadoc.types.tags.BasicTest");
	assertResults(
		"BasicTestTypes[TYPE_REF]{BasicTestTypes, javadoc.types.tags, Ljavadoc.types.tags.BasicTestTypes;, null, null, "+this.positions+R_DICNR+"}"
	);
}

public void test015() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see javadoc.types.tags.BasicTest\n" + 
		" * 		Note: JDT-UI failed on this one\n" + 
		" */\n" + 
		"public class BasicTestTypes<TPARAM> {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "javadoc.types.tags.BasicTest");
	assertResults(
		"BasicTestTypes<TPARAM>[TYPE_REF]{BasicTestTypes, javadoc.types.tags, Ljavadoc.types.tags.BasicTestTypes<TTPARAM;>;, null, null, "+this.positions+R_DICNR+"}"
	);
}

public void test016() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see java.la\n" + 
		" * 		Note: JDT-UI fails on this one\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "java.la");
	assertResults(
		"java.lang[PACKAGE_REF]{java.lang, java.lang, null, null, null, "+this.positions+R_DICQNR+"}"
	);
}

public void test017() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see java.lang\n" + 
		" * 		Note: JDT-UI fails on this one\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "java.la");
	assertResults(
		"java.lang[PACKAGE_REF]{java.lang, java.lang, null, null, null, "+this.positions+R_DICQNR+"}"
	);
}

public void test018() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see pack.Bin\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "pack.Bin");
	assertSortedResults(
		"Bin1[TYPE_REF]{pack.Bin1, pack, Lpack.Bin1;, null, null, "+this.positions+R_DICQNR+"}\n" + 
		"Bin2[TYPE_REF]{pack.Bin2, pack, Lpack.Bin2;, null, null, "+this.positions+R_DICQNR+"}\n" + 
		"Bin3[TYPE_REF]{pack.Bin3, pack, Lpack.Bin3;, null, null, "+this.positions+R_DICQNR+"}"
	);
}

public void test019() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see pack.Bin2\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "pack.Bin");
	assertSortedResults(
		"Bin1[TYPE_REF]{pack.Bin1, pack, Lpack.Bin1;, null, null, "+this.positions+R_DICQNR+"}\n" + 
		"Bin2[TYPE_REF]{pack.Bin2, pack, Lpack.Bin2;, null, null, "+this.positions+R_DICQNR+"}\n" + 
		"Bin3[TYPE_REF]{pack.Bin3, pack, Lpack.Bin3;, null, null, "+this.positions+R_DICQNR+"}"
	);
}

public void test020() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see pack.Bin2\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "pack.Bin2");
	assertSortedResults(
		"Bin2[TYPE_REF]{pack.Bin2, pack, Lpack.Bin2;, null, null, "+this.positions+R_DICENQNR+"}"
	);
}

public void test021() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see I\n" + 
		" * 		Note: completion list shoud not include base types.\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "I");
	assertSortedResults(
		"IllegalMonitorStateException[TYPE_REF]{IllegalMonitorStateException, java.lang, Ljava.lang.IllegalMonitorStateException;, null, null, "+this.positions+R_DICUNR+"}\n" + 
		"InterruptedException[TYPE_REF]{InterruptedException, java.lang, Ljava.lang.InterruptedException;, null, null, "+this.positions+R_DICUNR+"}"
	);
}

public void test022() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see java.lang.\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "java.lang.");
	assertSortedResults(
		"java.lang.annotation[PACKAGE_REF]{java.lang.annotation, java.lang.annotation, null, null, null, "+this.positions+R_DICQNR+"}\n" + 
		"CharSequence[TYPE_REF]{CharSequence, java.lang, Ljava.lang.CharSequence;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"Class[TYPE_REF]{Class, java.lang, Ljava.lang.Class;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"CloneNotSupportedException[TYPE_REF]{CloneNotSupportedException, java.lang, Ljava.lang.CloneNotSupportedException;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"Comparable[TYPE_REF]{Comparable, java.lang, Ljava.lang.Comparable;, null, null, "+this.positions+R_DICNR+"}\n" + 
		"Enum[TYPE_REF]{Enum, java.lang, Ljava.lang.Enum;, null, null, "+this.positions+R_DICNR+"}\n" + 
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

public void test023() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see java.\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "java.");
	assertResults(
		"java.lang[PACKAGE_REF]{java.lang, java.lang, null, null, null, "+this.positions+R_DICQNR+"}\n" + 
		"java.io[PACKAGE_REF]{java.io, java.io, null, null, null, "+this.positions+R_DICQNR+"}"
	);
}

public void test024() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see java.lang\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "java.");
	assertResults(
		"java.lang.annotation[PACKAGE_REF]{java.lang.annotation, java.lang.annotation, null, null, null, "+this.positions+R_DICQNR+"}\n" + 
		"java.lang[PACKAGE_REF]{java.lang, java.lang, null, null, null, "+this.positions+R_DICQNR+"}\n" + 
		"java.io[PACKAGE_REF]{java.io, java.io, null, null, null, "+this.positions+R_DICQNR+"}"
	);
}

public void test025() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see java.lang.Obj\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "java.lang.");
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

public void test026() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see java.lang.Objec\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "java.lang.Ob");
	assertResults(
		"Object[TYPE_REF]{Object, java.lang, Ljava.lang.Object;, null, null, "+this.positions+R_DICNR+"}"
	);
}

/**
 * @category Tests for member types completion
 */
public void test030() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see BasicTestTypesM\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"	class BasicTestTypesMember {}\n" + 
		"}\n" + 
		"class BasicTestTypesTestSecondary {}";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "BasicTestTypesM");
	assertResults(
		"BasicTestTypes.BasicTestTypesMember[TYPE_REF]{BasicTestTypesMember, javadoc.types.tags, Ljavadoc.types.tags.BasicTestTypes$BasicTestTypesMember;, null, null, "+this.positions+R_DICUNR+"}"
	);
}

public void test031() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see BasicTestTypesMember\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"	class BasicTestTypesMember {}\n" + 
		"}\n" + 
		"class BasicTestTypesSecondary {}";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "BasicTestTypesM");
	assertResults(
		"BasicTestTypes.BasicTestTypesMember[TYPE_REF]{BasicTestTypesMember, javadoc.types.tags, Ljavadoc.types.tags.BasicTestTypes$BasicTestTypesMember;, null, null, "+this.positions+R_DICUNR+"}"
	);
}

public void test032() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see BasicTestTypes.BasicTestTypesM\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"	class BasicTestTypesMember {}\n" + 
		"}\n" + 
		"class BasicTestTypesSecondary {}";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "BasicTestTypesM");
	assertResults(
		"BasicTestTypes.BasicTestTypesMember[TYPE_REF]{BasicTestTypesMember, javadoc.types.tags, Ljavadoc.types.tags.BasicTestTypes$BasicTestTypesMember;, null, null, "+this.positions+R_DICNR+"}"
	);
}

public void test033() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see javadoc.types.tags.BasicTestTypes.BasicTestTypesM\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"	class BasicTestTypesMember {}\n" + 
		"}\n" + 
		"class BasicTestTypesSecondary {}";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "BasicTestTypesM");
	assertResults(
		"BasicTestTypes.BasicTestTypesMember[TYPE_REF]{BasicTestTypesMember, javadoc.types.tags, Ljavadoc.types.tags.BasicTestTypes$BasicTestTypesMember;, null, null, "+this.positions+R_DICNR+"}"
	);
}

public void test034() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see BasicTestTypesS\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n" + 
		"class BasicTestTypesSecondary {}";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "BasicTestTypesS");
	assertResults(
		"BasicTestTypesSecondary[TYPE_REF]{BasicTestTypesSecondary, javadoc.types.tags, Ljavadoc.types.tags.BasicTestTypesSecondary;, null, null, "+this.positions+R_DICUNR+"}"
	);
}

public void test035() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see javadoc.types.tags.BasicTestTypesS\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n" + 
		"class BasicTestTypesSecondary {}";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "javadoc.types.tags.BasicTestTypesS");
	assertResults(
		"BasicTestTypesSecondary[TYPE_REF]{BasicTestTypesSecondary, javadoc.types.tags, Ljavadoc.types.tags.BasicTestTypesSecondary;, null, null, "+this.positions+R_DICNR+"}"
	);
}

public void test036() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see javadoc.types.tags.BasicTestTypesSecondary\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n" + 
		"class BasicTestTypesSecondary {}";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "javadoc.types.tags.BasicTestTypesS");
	assertResults(
		"BasicTestTypesSecondary[TYPE_REF]{BasicTestTypesSecondary, javadoc.types.tags, Ljavadoc.types.tags.BasicTestTypesSecondary;, null, null, "+this.positions+R_DICNR+"}"
	);
}

public void test037() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see javadoc.types.tags.BasicTestTypes.BasicTestTypes\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}\n" + 
		"class BasicTestTypesSecondary {}";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "javadoc.types.tags.BasicTestTypes.BasicTestTypes");
	assertResults("");
}


/**
 * @category Tests for fields completion
 */
public void test040() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see BasicTestReferences#FIE\n" + 
		" * 		Note: JDT/UI create one proposal on this one\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "FIE");
	assertResults("");
}

public void test041() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see org.eclipse.wst.jsdt.core.tests.BasicTestReferences#FIE\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "FIE");
	assertResults(
		"FIELD[FIELD_REF]{FIELD, Lorg.eclipse.wst.jsdt.core.tests.BasicTestReferences;, I, FIELD, null, "+this.positions+R_DICNR+"}"
	);
}

public void test042() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see org.eclipse.wst.jsdt.core.tests.BasicTestReferences#FIELD\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"}";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "FIE");
	assertResults(
		"FIELD[FIELD_REF]{FIELD, Lorg.eclipse.wst.jsdt.core.tests.BasicTestReferences;, I, FIELD, null, "+this.positions+R_DICNR+"}"
	);
}

public void test043() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see #fo\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"	int foo;\n" + 
		"}";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "fo");
	assertResults(
		"foo[FIELD_REF]{foo, Ljavadoc.types.tags.BasicTestTypes;, I, foo, null, "+this.positions+R_DICNRNS+"}"
	);
}

public void test044() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see #foo\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"	int foo;\n" + 
		"}";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "fo");
	assertResults(
		"foo[FIELD_REF]{foo, Ljavadoc.types.tags.BasicTestTypes;, I, foo, null, "+this.positions+R_DICNRNS+"}"
	);
}

public void test045() throws JavaScriptModelException {
	String[] sources = {
		"/Completion/src/javadoc/types/tags/BasicTestTypes.js",
			"package javadoc.types.tags;\n" + 
			"/**\n" + 
			" * Completion after:\n" + 
			" * 	@see OtherTypes#fo\n" + 
			" */\n" + 
			"public class BasicTestTypes {\n" + 
			"}",
		"/Completion/src/javadoc/types/tags/OtherTypes.js",
			"package javadoc.types.tags;\n" + 
			"public class OtherTypes {\n" + 
			"	int foo;\n" + 
			"}"
	};
	completeInJavadoc(sources, true, "fo");
	assertResults(
		"foo[FIELD_REF]{foo, Ljavadoc.types.tags.OtherTypes;, I, foo, null, "+this.positions+R_DICNRNS+"}"
	);
}


/**
 * @category Tests for methods completion
 */
public void test050() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see BasicTestMethod.meth\n" + 
		" * 		Note that test result may change if bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=26814 was fixed\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"	void method() {}\n" + 
		"	void paramMethod(String str, int x, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "meth");
	assertResults("");
}

public void test051() throws JavaScriptModelException {
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@see BasicTestMethod#unknown\n" + 
		" * 		- completion list shoud be empty\n" + 
		" */\n" + 
		"public class BasicTestTypes {\n" + 
		"	void method() {}\n" + 
		"	void paramMethod(String str, int x, Object obj) {}\n" + 
		"}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "unknown");
	assertResults("");
}

public void test052() throws JavaScriptModelException {
	String[] sources = {
		"/Completion/src/javadoc/types/tags/BasicTestTypes.js",
			"package javadoc.types.tags;\n" + 
			"/**\n" + 
			" * Completion after:\n" + 
			" * 	@see OtherTypes#meth\n" + 
			" */\n" + 
			"public class BasicTestTypes {\n" + 
			"}",
		"/Completion/src/javadoc/types/tags/OtherTypes.js",
			"package javadoc.types.tags;\n" + 
			"public class OtherTypes {\n" + 
			"	void method() {};\n" +
			"}"
	};
	completeInJavadoc(sources, true, "meth");
	assertResults(
		"method[FUNCTION_REF]{method(), Ljavadoc.types.tags.OtherTypes;, ()V, method, null, "+this.positions+R_DICNRNS+"}"
	);
}

public void test053() throws JavaScriptModelException {
	String[] sources = {
		"/Completion/src/javadoc/types/tags/BasicTestTypes.js",
			"package javadoc.types.tags;\n" + 
			"/**\n" + 
			" * Completion after:\n" + 
			" * 	@see OtherTypes#method\n" + 
			" */\n" + 
			"public class BasicTestTypes {\n" + 
			"}",
		"/Completion/src/javadoc/types/tags/OtherTypes.js",
			"package javadoc.types.tags;\n" + 
			"public class OtherTypes {\n" + 
			"	void method() {};\n" +
			"}"
	};
	completeInJavadoc(sources, true, "meth");
	assertResults(
		"method[FUNCTION_REF]{method(), Ljavadoc.types.tags.OtherTypes;, ()V, method, null, "+this.positions+R_DICNRNS+"}"
	);
}

/**
 * @category Tests for type parameters completion
 */
public void test060() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@param \n" + 
		" */\n" + 
		"public class BasicTestTypes<TPARAM> {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "@param ", 0); // empty token
	assertResults(
		"TPARAM[JSDOC_PARAM_REF]{<TPARAM>, null, null, TPARAM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test061() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@param <\n" + 
		" * 	Note:\n" + 
		" * 		JDT/UI fails on this one (no proposal)\n" + 
		" */\n" + 
		"public class BasicTestTypes<TPARAM> {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "<");
	assertResults(
		"TPARAM[JSDOC_PARAM_REF]{<TPARAM>, null, null, TPARAM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test062() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@param <TPA\n" + 
		" * 	Note:\n" + 
		" * 		JDT/UI fails on this one (no proposal)\n" + 
		" */\n" + 
		"public class BasicTestTypes<TPARAM> {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "<TPA");
	assertResults(
		"TPARAM[JSDOC_PARAM_REF]{<TPARAM>, null, null, TPARAM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test063() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@param <TPARAM\n" + 
		" * 	Note:\n" + 
		" * 		JDT/UI fails on this one (no proposal)\n" + 
		" */\n" + 
		"public class BasicTestTypes<TPARAM> {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "<TPA");
	assertResults(
		"TPARAM[JSDOC_PARAM_REF]{<TPARAM>, null, null, TPARAM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test064() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@param <TPARAM\n" + 
		" * 	Note:\n" + 
		" * 		JDT/UI fails on this one (no proposal)\n" + 
		" */\n" + 
		"public class BasicTestTypes<TPARAM> {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "<TPARAM");
	assertResults(
		"TPARAM[JSDOC_PARAM_REF]{<TPARAM>, null, null, TPARAM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test065() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@param <TPARAM>\n" + 
		" * 	Note:\n" + 
		" * 		JDT/UI fails on this one (no proposal)\n" + 
		" */\n" + 
		"public class BasicTestTypes<TPARAM> {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "<TPARAM");
	assertResults(
		"TPARAM[JSDOC_PARAM_REF]{<TPARAM>, null, null, TPARAM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test066() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@param <TPARAM>\n" + 
		" * 	Note:\n" + 
		" * 		JDT/UI fails on this one (no proposal)\n" + 
		" */\n" + 
		"public class BasicTestTypes<TPARAM> {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "<TPARAM>");
	assertResults(
		"TPARAM[JSDOC_PARAM_REF]{<TPARAM>, null, null, TPARAM, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test067() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@param <T1>\n" + 
		" * 	@param <T1>\n" + 
		" */\n" + 
		"public class BasicTestTypes<T1, T2, T3> {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "<T1>");
	assertResults("");
}

public void test068() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@param <T1>\n" + 
		" * 	@param <T1>\n" + 
		" */\n" + 
		"public class BasicTestTypes<T1, T2, T3> {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "<T1>", 2); //2nd position
	assertResults("");
}

public void test069() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@param <T1>\n" + 
		" ** 	@param \n" + 
		" * 	@param <T3>\n" + 
		" */\n" + 
		"public class BasicTestTypes<T1, T2, T3> {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "** 	@param ", 0); // empty token
	assertResults(
		"T2[JSDOC_PARAM_REF]{<T2>, null, null, T2, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test070() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@param <T1>\n" + 
		" * 	@param <T2>\n" + 
		" * 	@param <T3>\n" + 
		" */\n" + 
		"public class BasicTestTypes<T1, T2, T3> {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "<T", 3); // 3rd position
	assertResults(
		"T3[JSDOC_PARAM_REF]{<T3>, null, null, T3, null, "+this.positions+JAVADOC_RELEVANCE+"}"
	);
}

public void test071() throws JavaScriptModelException {
	setUpProjectOptions(CompilerOptions.VERSION_1_5);
	String source =
		"package javadoc.types.tags;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	@param <T1>\n" + 
		" * 	@param <T2>\n" + 
		" * 	@param <T3>\n" + 
		" ** 	@param \n" + 
		" */\n" + 
		"public class BasicTestTypes<T1, T2, T3> {}\n";
	completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "** 	@param ", 0); // empty token
	assertResults("");
}

/**
 * @tests Tests for camel case completion
 */
public void test080() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	try {
		Hashtable options = new Hashtable(oldOptions);
		options.put(JavaScriptCore.CODEASSIST_CAMEL_CASE_MATCH, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		String source =
			"package javadoc.types.tags;\n" + 
			"/**\n" + 
			" * Completion after:\n" + 
			" * 	@see BTT\n" + 
			" */\n" + 
			"public class BasicTestTypes {}\n";
		completeInJavadoc("/Completion/src/javadoc/types/tags/BasicTestTypes.js", source, true, "BTT");
		assertResults("BasicTestTypes[TYPE_REF]{BasicTestTypes, javadoc.types.tags, Ljavadoc.types.tags.BasicTestTypes;, null, null, "+this.positions+"16}");
	} finally {
		JavaScriptCore.setOptions(oldOptions);
	}
}
/**
 * @category Tests for filtered completion
 */
public void test100() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	bla ZBasi bla\n" + 
		" */\n" + 
		"public class ZBasicTestTypes {}\n";
	completeInJavadoc(
			"/Completion/src/javadoc/types/ZBasicTestTypes.js",
			source,
			true,
			"ZBasi",
			1,
			new int[]{});
	assertResults(
			"ZBasicTestTypes[TYPE_REF]{ZBasicTestTypes, javadoc.types, Ljavadoc.types.ZBasicTestTypes;, null, null, "+this.positions+R_DICUNR+"}\n" + 
			"ZBasicTestTypes[JSDOC_TYPE_REF]{{@link ZBasicTestTypes}, javadoc.types, Ljavadoc.types.ZBasicTestTypes;, null, null, "+this.positions+R_DICUNRIT+"}"
	);
}
public void test101() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	bla ZBasi bla\n" + 
		" */\n" + 
		"public class ZBasicTestTypes {}\n";
	completeInJavadoc(
			"/Completion/src/javadoc/types/ZBasicTestTypes.js",
			source,
			true,
			"ZBasi",
			1,
			new int[]{CompletionProposal.JSDOC_TYPE_REF});
	assertResults(
			"ZBasicTestTypes[TYPE_REF]{ZBasicTestTypes, javadoc.types, Ljavadoc.types.ZBasicTestTypes;, null, null, "+this.positions+R_DICUNR+"}"
	);
}
public void test102() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	bla ZBasi bla\n" + 
		" */\n" + 
		"public class ZBasicTestTypes {}\n";
	completeInJavadoc(
			"/Completion/src/javadoc/types/ZBasicTestTypes.js",
			source,
			true,
			"ZBasi",
			1,
			new int[]{CompletionProposal.TYPE_REF});
	assertResults(
			"ZBasicTestTypes[JSDOC_TYPE_REF]{{@link ZBasicTestTypes}, javadoc.types, Ljavadoc.types.ZBasicTestTypes;, null, null, "+this.positions+R_DICUNRIT+"}"
	);
}
public void test103() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	bla ZBasicTestTypes#fo bla\n" + 
		" */\n" + 
		"public class ZBasicTestTypes {\n" + 
		"  public void foo() {}\n" + 
		"}\n";
	completeInJavadoc(
			"/Completion/src/javadoc/types/ZBasicTestTypes.js",
			source,
			true,
			"ZBasicTestTypes#fo",
			1,
			new int[]{});
	assertResults(
			"foo[JSDOC_METHOD_REF]{{@link ZBasicTestTypes#foo()}, Ljavadoc.types.ZBasicTestTypes;, ()V, foo, null, "+this.positions+R_DICNRNSIT+"}"
	);
}
public void test104() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	bla ZBasicTestTypes#fo bla\n" + 
		" */\n" + 
		"public class ZBasicTestTypes {\n" + 
		"  public void foo() {}\n" + 
		"}\n";
	completeInJavadoc(
			"/Completion/src/javadoc/types/ZBasicTestTypes.js",
			source,
			true,
			"ZBasicTestTypes#fo",
			1,
			new int[]{CompletionProposal.JSDOC_METHOD_REF});
	assertResults(
			""
	);
}
public void test105() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	bla ZBasicTestTypes#fo bla\n" + 
		" */\n" + 
		"public class ZBasicTestTypes {\n" + 
		"  public void foo() {}\n" + 
		"}\n";
	completeInJavadoc(
			"/Completion/src/javadoc/types/ZBasicTestTypes.js",
			source,
			true,
			"ZBasicTestTypes#fo",
			1,
			new int[]{CompletionProposal.METHOD_REF});
	assertResults(
			"foo[JSDOC_METHOD_REF]{{@link ZBasicTestTypes#foo()}, Ljavadoc.types.ZBasicTestTypes;, ()V, foo, null, "+this.positions+R_DICNRNSIT+"}"
	);
}
public void test106() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	bla ZBasicTestTypes#fo bla\n" + 
		" */\n" + 
		"public class ZBasicTestTypes {\n" + 
		"  public int foo;\n" + 
		"}\n";
	completeInJavadoc(
			"/Completion/src/javadoc/types/ZBasicTestTypes.js",
			source,
			true,
			"ZBasicTestTypes#fo",
			1,
			new int[]{});
	assertResults(
			"foo[JSDOC_FIELD_REF]{{@link ZBasicTestTypes#foo}, Ljavadoc.types.ZBasicTestTypes;, I, foo, null, "+this.positions+R_DICNRNSIT+"}"
	);
}
public void test107() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	bla ZBasicTestTypes#fo bla\n" + 
		" */\n" + 
		"public class ZBasicTestTypes {\n" + 
		"  public int foo;\n" + 
		"}\n";
	completeInJavadoc(
			"/Completion/src/javadoc/types/ZBasicTestTypes.js",
			source,
			true,
			"ZBasicTestTypes#fo",
			1,
			new int[]{CompletionProposal.JSDOC_FIELD_REF});
	assertResults(
			""
	);
}
public void test108() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	bla ZBasicTestTypes#fo bla\n" + 
		" */\n" + 
		"public class ZBasicTestTypes {\n" + 
		"  public int foo;\n" + 
		"}\n";
	completeInJavadoc(
			"/Completion/src/javadoc/types/ZBasicTestTypes.js",
			source,
			true,
			"ZBasicTestTypes#fo",
			1,
			new int[]{CompletionProposal.FIELD_REF});
	assertResults(
			"foo[JSDOC_FIELD_REF]{{@link ZBasicTestTypes#foo}, Ljavadoc.types.ZBasicTestTypes;, I, foo, null, "+this.positions+R_DICNRNSIT+"}"
	);
}
public void test109() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	bla javadoc.types.ZBasi bla\n" + 
		" */\n" + 
		"public class ZBasicTestTypes {}\n";
	completeInJavadoc(
			"/Completion/src/javadoc/types/ZBasicTestTypes.js",
			source,
			true,
			"javadoc.types.ZBasi",
			1,
			new int[]{});
	assertResults(
			"ZBasicTestTypes[TYPE_REF]{ZBasicTestTypes, javadoc.types, Ljavadoc.types.ZBasicTestTypes;, null, null, "+this.positions+R_DICNR+"}\n" + 
			"ZBasicTestTypes[JSDOC_TYPE_REF]{{@link ZBasicTestTypes}, javadoc.types, Ljavadoc.types.ZBasicTestTypes;, null, null, "+this.positions+R_DICNRIT+"}"
	);
}
public void test110() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	bla javadoc.types.ZBasi bla\n" + 
		" */\n" + 
		"public class ZBasicTestTypes {}\n";
	completeInJavadoc(
			"/Completion/src/javadoc/types/ZBasicTestTypes.js",
			source,
			true,
			"javadoc.types.ZBasi",
			1,
			new int[]{CompletionProposal.JSDOC_TYPE_REF});
	assertResults(
			"ZBasicTestTypes[TYPE_REF]{ZBasicTestTypes, javadoc.types, Ljavadoc.types.ZBasicTestTypes;, null, null, "+this.positions+R_DICNR+"}"
	);
}
public void test111() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	bla javadoc.types.ZBasi bla\n" + 
		" */\n" + 
		"public class ZBasicTestTypes {}\n";
	completeInJavadoc(
			"/Completion/src/javadoc/types/ZBasicTestTypes.js",
			source,
			true,
			"javadoc.types.ZBasi",
			1,
			new int[]{CompletionProposal.TYPE_REF});
	assertResults(
			"ZBasicTestTypes[JSDOC_TYPE_REF]{{@link ZBasicTestTypes}, javadoc.types, Ljavadoc.types.ZBasicTestTypes;, null, null, "+this.positions+R_DICNRIT+"}"
	);
}
public void test112() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	bla javadoc.types.ZBasicTestTypes.Inn bla\n" + 
		" */\n" + 
		"public class ZBasicTestTypes {\n" + 
		"  public class Inner {}\n" + 
		"}\n";
	completeInJavadoc(
			"/Completion/src/javadoc/types/ZBasicTestTypes.js",
			source,
			true,
			"javadoc.types.ZBasicTestTypes.Inn",
			1,
			new int[]{});
	assertResults(
			"ZBasicTestTypes.Inner[TYPE_REF]{Inner, javadoc.types, Ljavadoc.types.ZBasicTestTypes$Inner;, null, null, "+this.positions+R_DICNR+"}\n" + 
			"ZBasicTestTypes.Inner[JSDOC_TYPE_REF]{{@link Inner}, javadoc.types, Ljavadoc.types.ZBasicTestTypes$Inner;, null, null, "+this.positions+R_DICNRIT+"}"
	);
}
public void test113() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	bla javadoc.types.ZBasicTestTypes.Inn bla\n" + 
		" */\n" + 
		"public class ZBasicTestTypes {\n" + 
		"  public class Inner {}\n" + 
		"}\n";
	completeInJavadoc(
			"/Completion/src/javadoc/types/ZBasicTestTypes.js",
			source,
			true,
			"javadoc.types.ZBasicTestTypes.Inn",
			1,
			new int[]{CompletionProposal.JSDOC_TYPE_REF});
	assertResults(
			"ZBasicTestTypes.Inner[TYPE_REF]{Inner, javadoc.types, Ljavadoc.types.ZBasicTestTypes$Inner;, null, null, "+this.positions+R_DICNR+"}"
	);
}
public void test114() throws JavaScriptModelException {
	String source =
		"package javadoc.types;\n" + 
		"/**\n" + 
		" * Completion after:\n" + 
		" * 	bla javadoc.types.ZBasicTestTypes.Inn bla\n" + 
		" */\n" + 
		"public class ZBasicTestTypes {\n" + 
		"  public class Inner {}\n" + 
		"}\n";
	completeInJavadoc(
			"/Completion/src/javadoc/types/ZBasicTestTypes.js",
			source,
			true,
			"javadoc.types.ZBasicTestTypes.Inn",
			1,
			new int[]{CompletionProposal.TYPE_REF});
	assertResults(
			"ZBasicTestTypes.Inner[JSDOC_TYPE_REF]{{@link Inner}, javadoc.types, Ljavadoc.types.ZBasicTestTypes$Inner;, null, null, "+this.positions+R_DICNRIT+"}"
	);
}
}
