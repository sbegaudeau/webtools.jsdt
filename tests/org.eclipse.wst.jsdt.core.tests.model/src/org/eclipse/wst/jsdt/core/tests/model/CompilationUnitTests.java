/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.model;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.jsdt.internal.core.*;
import org.eclipse.wst.jsdt.internal.core.util.Util;
import org.eclipse.wst.jsdt.core.*;

import junit.framework.Test;

public class CompilationUnitTests extends ModifyingResourceTests {
	IJavaScriptUnit cu;
	IJavaScriptUnit workingCopy;
	IJavaScriptProject testProject;
	
public CompilationUnitTests(String name) {
	super(name);
}
public void setUpSuite() throws Exception {
	super.setUpSuite();
	
	this.testProject = createJavaProject("P", new String[] {"src"}, new String[] {getSystemJsPathString()}, "bin", "1.5");
	createFolder("/P/src/p");
	createFile(
		"/P/src/p/X.js",
		"\n\n" + 	// package now includes comment (see bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=93880)
						// => need some empty line at beginning to be able to have cu without any other element (see testGetElementAt())
		"/* some comment */" +
		"  var f1=1;\n" +
		"  /** @deprecated\n */" +
		"  var f2;\n" +
		"  var f3;\n" +
		"  var f4;\n" +
		"  var f5, f6, f7;\n" +
		"  // @Deprecated\n" +
		"  var f8;\n" +
		"  function foo( y) {\n" +
		"  }\n" +
		"  function bar() {\n" +
		"  }\n" +
		"  /** @deprecated\n */" +
		"  function fred() {\n" +
		"  }\n" +
		"  // @Deprecated\n" +
		"  function fred2() {\n" +
		"  }\n" +
		""
	);
	this.cu = getCompilationUnit("/P/src/p/X.js");
}

// Use this static initializer to specify subset for tests
// All specified tests which do not belong to the class are skipped...
static {
//	TESTS_PREFIX = "testGetChildren";
//	TESTS_NAMES = new String[] { "testDefaultFlag1" };
//	TESTS_NUMBERS = new int[] { 13 };
//	TESTS_RANGE = new int[] { 16, -1 };
}
public static Test suite() {
	return buildModelTestSuite(CompilationUnitTests.class);
}
protected void tearDown() throws Exception {
	if (this.workingCopy != null)
		this.workingCopy.discardWorkingCopy();
	super.tearDown();
}
public void tearDownSuite() throws Exception {
	this.deleteProject("P");
	super.tearDownSuite();
}
/**
 * Create working copy and compute problems.
 * 
 * Note that in this case, a complete parse of javadoc comment is performed
 * (ie. done with checkDocComment = true) instead of a "light" parse when
 * problems are not computed.
 * 
 * See JavaScriptUnit#buildStructure() line with comment: // disable javadoc parsing if not computing problems, not resolving and not creating ast
 * and org.eclipse.wst.jsdt.internal.compiler.parser.JavadocParser#checkDeprecation(int)
 */
private IJavaScriptUnit createWorkingCopyComputingProblems(String source) throws JavaScriptModelException {
	this.workingCopy = getWorkingCopy("/P/src/p/Y.js", source, true);
	return this.workingCopy;
}


public void test00Class() throws CoreException {
	try {
		
		String source =
			"MyClass.prototype.someMethod = MyClass_someMethod;\n" + 
			"function MyClass()\n" + 
			"{\n" + 
			"this.field1=0;\n" + 
			"}\n" + 
			"function MyClass_someMethod()\n" + 
			"{\n" + 
			"}";
		createFile("/P/src/X.js", source);
		final IJavaScriptUnit compilationUnit = getCompilationUnit("/P/src/X.js");
		IType type = compilationUnit.getType("MyClass");
		assertTrue("Type not defined", type.exists());
		
		IField[] fields= type.getFields();
		assertEquals("Wrong number of fields returned",  1, fields.length);
		assertEquals("Incorrect name for the  field", "field1", fields[0].getElementName());
		assertTrue("Field should exist " , fields[0].exists());

		IFunction[] methods= type.getFunctions();
		assertEquals("Wrong number of methods returned",  2, methods.length);
		assertEquals("Incorrect name for the  method", "someMethod", methods[0].getElementName());
		assertTrue("Field should exist " ,methods[0].exists());

		
	} finally {
		deleteFile("/P/src/X.js");
	}
}





/**
 * Calls methods that do nothing to ensure code coverage
 */
public void testCodeCoverage() throws JavaScriptModelException {
	this.cu.discardWorkingCopy();
	this.cu.restore();
}
/**
 * Ensures <code>commitWorkingCopy(boolean, IProgressMonitor)</code> throws the correct 
 * <code>JavaScriptModelException</code> for a <code>JavaScriptUnit</code>.
 */
public void testCommitWorkingCopy() {
	try {
		this.cu.commitWorkingCopy(false, null);
	} catch (JavaScriptModelException jme) {
		assertTrue("Incorrect status for committing a JavaScriptUnit", jme.getStatus().getCode() == IJavaScriptModelStatusConstants.INVALID_ELEMENT_TYPES);
		return;
	}
	assertTrue("A compilation unit should throw an exception is a commit is attempted", false);
}

/*
 * Ensure that the deprecated flag is correctly reported
 * (regression test fo bug 23207 Flags.isDeprecated(IFunction.getFlags()) doesn't work)
 */
//public void testDeprecatedFlag01() throws JavaScriptModelException {
//	IType type = this.cu.getType("X");
//	assertTrue("Type X should not be deprecated", !Flags.isDeprecated(type.getFlags()));
//}
//
/*
 * Ensure that the deprecated flag is correctly reported
 * (regression test fo bug 23207 Flags.isDeprecated(IFunction.getFlags()) doesn't work)
 */
//public void testDeprecatedFlag02() throws JavaScriptModelException {
//	IType type = this.cu.getType("I");
//	assertTrue("Type I should be deprecated", Flags.isDeprecated(type.getFlags()));
//}

/*
 * Ensure that the deprecated flag is correctly reported
 * (regression test fo bug 23207 Flags.isDeprecated(IFunction.getFlags()) doesn't work)
 */
public void testDeprecatedFlag03() throws JavaScriptModelException {
	IField field = this.cu.getField("f1");
	assertTrue("Field f1 should not be deprecated", !Flags.isDeprecated(field.getFlags()));
}

/*
 * Ensure that the deprecated flag is correctly reported
 * (regression test fo bug 23207 Flags.isDeprecated(IFunction.getFlags()) doesn't work)
 */
public void testDeprecatedFlag04() throws JavaScriptModelException {
	IField field = this.cu.getField("f2");
	assertTrue("Field f2 should be deprecated", Flags.isDeprecated(field.getFlags()));
}

/*
 * Ensure that the deprecated flag is correctly reported
 * (regression test fo bug 23207 Flags.isDeprecated(IFunction.getFlags()) doesn't work)
 */
public void testDeprecatedFlag05() throws JavaScriptModelException {
	IFunction method = this.cu.getFunction("bar", new String[]{});
	assertTrue("Method bar should not be deprecated", !Flags.isDeprecated(method.getFlags()));
}

/*
 * Ensure that the deprecated flag is correctly reported
 * (regression test fo bug 23207 Flags.isDeprecated(IFunction.getFlags()) doesn't work)
 */
public void testDeprecatedFlag06() throws JavaScriptModelException {
	IFunction method = this.cu.getFunction("fred", new String[]{});
	assertTrue("Method fred should be deprecated", Flags.isDeprecated(method.getFlags()));
}

/*
 * Ensure that the deprecated flag is correctly reported
 * (regression test fo bug 89807 Outliner should recognize @Deprecated annotation)
 */
public void testDeprecatedFlag07() throws JavaScriptModelException {
	IType type = this.cu.getType("I3");
	assertTrue("Type I3 should be deprecated", Flags.isDeprecated(type.getFlags()));
}

/*
 * Ensure that the deprecated flag is correctly reported
 * (regression test fo bug 89807 Outliner should recognize @Deprecated annotation)
 */
public void testDeprecatedFlag08() throws JavaScriptModelException {
	IField field = this.cu.getType("X").getField("f8");
	assertTrue("Field f8 should be deprecated", Flags.isDeprecated(field.getFlags()));
}

/*
 * Ensure that the deprecated flag is correctly reported
 * (regression test fo bug 89807 Outliner should recognize @Deprecated annotation)
 */
public void testDeprecatedFlag09() throws JavaScriptModelException {
	IFunction method = this.cu.getType("X").getFunction("fred2", new String[0]);
	assertTrue("Method fred2 should be deprecated", Flags.isDeprecated(method.getFlags()));
}

/*
 * Ensures that the categories for a class are correct.
 */
public void testGetCategories01() throws CoreException {
	createWorkingCopyComputingProblems(
		"package p;\n" +
		"/**\n" +
		" * @category test\n" +
		" */\n" +
		"public class Y {\n" +
		"}"
	);
	String[] categories = this.workingCopy.getType("Y").getCategories();
	assertStringsEqual(
		"Unexpected categories",
		"test\n",
		categories);
}

/*
 * Ensures that the categories for an interface are correct.
 */
public void testGetCategories02() throws CoreException {
	createWorkingCopyComputingProblems(
		"package p;\n" +
		"/**\n" +
		" * @category test\n" +
		" */\n" +
		"public interface Y {\n" +
		"}"
	);
	String[] categories = workingCopy.getType("Y").getCategories();
	assertStringsEqual(
		"Unexpected categories",
		"test\n",
		categories);
}

/*
 * Ensures that the categories for an enumeration type are correct.
 */
public void testGetCategories03() throws CoreException {
	createWorkingCopyComputingProblems(
		"package p;\n" +
		"/**\n" +
		" * @category test\n" +
		" */\n" +
		"public enum Y {\n" +
		"}"
	);
	String[] categories = workingCopy.getType("Y").getCategories();
	assertStringsEqual(
		"Unexpected categories",
		"test\n",
		categories);
}

/*
 * Ensures that the categories for an annotation type type are correct.
 */
public void testGetCategories04() throws CoreException {
	createWorkingCopyComputingProblems(
		"package p;\n" +
		"/**\n" +
		" * @category test\n" +
		" */\n" +
		"public @interface Y {\n" +
		"}"
	);
	String[] categories = workingCopy.getType("Y").getCategories();
	assertStringsEqual(
		"Unexpected categories",
		"test\n",
		categories);
}

/*
 * Ensures that the categories for a method are correct.
 */
public void testGetCategories05() throws CoreException {
	createWorkingCopyComputingProblems(
		"package p;\n" +
		"public class Y {\n" +
		"  /**\n" +
		"   * @category test\n" +
		"   */\n" +
		"  void foo() {}\n" +
		"}"
	);
	String[] categories = workingCopy.getType("Y").getFunction("foo", new String[0]).getCategories();
	assertStringsEqual(
		"Unexpected categories",
		"test\n",
		categories);
}

/*
 * Ensures that the categories for a constructor are correct.
 */
public void testGetCategories06() throws CoreException {
	createWorkingCopyComputingProblems(
		"package p;\n" +
		"public class Y {\n" +
		"  /**\n" +
		"   * @category test\n" +
		"   */\n" +
		"  public Y() {}\n" +
		"}"
	);
	String[] categories = workingCopy.getType("Y").getFunction("Y", new String[0]).getCategories();
	assertStringsEqual(
		"Unexpected categories",
		"test\n",
		categories);
}

/*
 * Ensures that the categories for a field are correct.
 */
public void testGetCategories07() throws CoreException {
	createWorkingCopyComputingProblems(
		"package p;\n" +
		"public class Y {\n" +
		"  /**\n" +
		"   * @category test\n" +
		"   */\n" +
		"  int field;\n" +
		"}"
	);
	String[] categories = workingCopy.getType("Y").getField("field").getCategories();
	assertStringsEqual(
		"Unexpected categories",
		"test\n",
		categories);
}

/*
 * Ensures that the categories for a member type are correct.
 */
public void testGetCategories08() throws CoreException {
	createWorkingCopyComputingProblems(
		"package p;\n" +
		"public class Y {\n" +
		"  /**\n" +
		"   * @category test\n" +
		"   */\n" +
		"  class Member {}\n" +
		"}"
	);
	String[] categories = workingCopy.getType("Y").getType("Member").getCategories();
	assertStringsEqual(
		"Unexpected categories",
		"test\n",
		categories);
}

/*
 * Ensures that the categories for an element that has no categories is empty.
 */
public void testGetCategories09() throws CoreException {
	createWorkingCopyComputingProblems(
		"package p;\n" +
		"public class Y {\n" +
		"  /**\n" +
		"  */\n" +
		"  void foo() {}\n" +
		"}"
	);
	String[] categories = workingCopy.getType("Y").getFunction("foo", new String[0]).getCategories();
	assertStringsEqual(
		"Unexpected categories",
		"",
		categories);
}

/*
 * Ensures that the categories for an element that has multiple category tags is correct.
 */
public void testGetCategories10() throws CoreException {
	createWorkingCopyComputingProblems(
		"package p;\n" +
		"public class Y {\n" +
		"  /**\n" +
		"   * @category test1\n" +
		"   * @category test2\n" +
		"   */\n" +
		"  void foo() {}\n" +
		"}"
	);
	String[] categories = workingCopy.getType("Y").getFunction("foo", new String[0]).getCategories();
	assertStringsEqual(
		"Unexpected categories",
		"test1\n" +
		"test2\n",
		categories);
}

/*
 * Ensures that the categories for an element that has multiple categories for one category tag is correct.
 */
public void testGetCategories11() throws CoreException {
	createWorkingCopyComputingProblems(
		"package p;\n" +
		"public class Y {\n" +
		"  /**\n" +
		"   * @category test1 test2\n" +
		"   */\n" +
		"  void foo() {}\n" +
		"}"
	);
	String[] categories = workingCopy.getType("Y").getFunction("foo", new String[0]).getCategories();
	assertStringsEqual(
		"Unexpected categories",
		"test1\n" +
		"test2\n",
		categories);
}
public void testGetCategories12() throws CoreException {
	createWorkingCopyComputingProblems(
		"package p;\n" +
		"public class Y {\n" +
		"  /**\n" +
		"   * @category test1 test2\n" +
		"   */\n" +
		"  void foo() {}\n" +
		"}"
	);
	String[] categories = this.workingCopy.getType("Y").getFunction("foo", new String[0]).getCategories();
	assertStringsEqual(
		"Unexpected categories",
		"test1\n" +
		"test2\n",
		categories);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=125676
public void testGetCategories13() throws CoreException {
	createWorkingCopyComputingProblems(
		"package p;\n" +
		"public class Y {\n" +
		"  /**\n" +
		"   * @category " +
		"	 *		test\n" +
		"   */\n" +
		"  void foo() {}\n" +
		"}"
	);
	String[] categories = this.workingCopy.getType("Y").getFunction("foo", new String[0]).getCategories();
	assertStringsEqual(
		"Unexpected categories",
		"",
		categories);
}
public void testGetCategories14() throws CoreException {
	createWorkingCopyComputingProblems(
		"package p;\n" +
		"public class Y {\n" +
		"  /**\n" +
		"   * @category" +
		"	 *		test\n" +
		"   */\n" +
		"  void foo() {}\n" +
		"}"
	);
	String[] categories = this.workingCopy.getType("Y").getFunction("foo", new String[0]).getCategories();
	assertStringsEqual(
		"Unexpected categories",
		"",
		categories);
}
public void testGetCategories15() throws CoreException {
	createWorkingCopyComputingProblems(
		"package p;\n" +
		"public class Y {\n" +
		"  /**\n" +
		"   * @category test1" +
		"	 *		test2\n" +
		"   */\n" +
		"  void foo() {}\n" +
		"}"
	);
	String[] categories = this.workingCopy.getType("Y").getFunction("foo", new String[0]).getCategories();
	assertStringsEqual(
		"Unexpected categories",
		"test1\n",
		categories);
}

/*
 * Ensures that the children of a type for a given category are correct.
 */
public void testGetChildrenForCategory01() throws CoreException {
	createWorkingCopyComputingProblems(
		"package p;\n" +
		"public class Y {\n" +
		"  /**\n" +
		"   * @category test\n" +
		"   */\n" +
		"  int field;\n" +
		"  /**\n" +
		"   * @category test\n" +
		"   */\n" +
		"  void foo1() {}\n" +
		"  /**\n" +
		"   * @category test\n" +
		"   */\n" +
		"  void foo2() {}\n" +
		"  /**\n" +
		"   * @category other\n" +
		"   */\n" +
		"  void foo3() {}\n" +
		"}"
	);
	IJavaScriptElement[] children = workingCopy.getType("Y").getChildrenForCategory("test");
	assertElementsEqual(
		"Unexpected children",
		"field [in Y [in [Working copy] Y.js [in p [in src [in P]]]]]\n" + 
		"foo1() [in Y [in [Working copy] Y.js [in p [in src [in P]]]]]\n" + 
		"foo2() [in Y [in [Working copy] Y.js [in p [in src [in P]]]]]",
		children);
}

/*
 * Ensures that the children of a type for a given category are correct.
 */
public void testGetChildrenForCategory02() throws CoreException {
	createWorkingCopyComputingProblems(
		"package p;\n" +
		"public class Y {\n" +
		"  /**\n" +
		"   * @category test1 test2\n" +
		"   */\n" +
		"  class Member {}\n" +
		"  /**\n" +
		"   * @category test1\n" +
		"   */\n" +
		"  void foo1() {}\n" +
		"  /**\n" +
		"   * @category test2\n" +
		"   */\n" +
		"  void foo2() {}\n" +
		"}"
	);
	IJavaScriptElement[] children = workingCopy.getType("Y").getChildrenForCategory("test1");
	assertElementsEqual(
		"Unexpected children",
		"Member [in Y [in [Working copy] Y.js [in p [in src [in P]]]]]\n" + 
		"foo1() [in Y [in [Working copy] Y.js [in p [in src [in P]]]]]",
		children);
}
public void testGetChildrenForCategory03() throws CoreException, IOException {
	createWorkingCopyComputingProblems(
		"package p;\n" +
		"public class Y {\n" +
		"  /**\n" +
		"   * @category fields test all\n" +
		"   */\n" +
		"  int field;\n" +
		"  /**\n" +
		"   * @category methods test all\n" +
		"   */\n" +
		"  void foo1() {}\n" +
		"  /**\n" +
		"   * @category methods test all\n" +
		"   */\n" +
		"  void foo2() {}\n" +
		"  /**\n" +
		"   * @category methods other all\n" +
		"   */\n" +
		"  void foo3() {}\n" +
		"}"
	);
	IJavaScriptElement[] tests  = this.workingCopy.getType("Y").getChildrenForCategory("test");
	assertElementsEqual(
		"Unexpected children",
		"field [in Y [in [Working copy] Y.js [in p [in src [in P]]]]]\n" + 
		"foo1() [in Y [in [Working copy] Y.js [in p [in src [in P]]]]]\n" + 
		"foo2() [in Y [in [Working copy] Y.js [in p [in src [in P]]]]]",
		tests);
	IJavaScriptElement[] methods = this.workingCopy.getType("Y").getChildrenForCategory("methods");
	assertElementsEqual(
		"Unexpected children",
		"foo1() [in Y [in [Working copy] Y.js [in p [in src [in P]]]]]\n" + 
		"foo2() [in Y [in [Working copy] Y.js [in p [in src [in P]]]]]\n" + 
		"foo3() [in Y [in [Working copy] Y.js [in p [in src [in P]]]]]",
		methods);
	IJavaScriptElement[] others = this.workingCopy.getType("Y").getChildrenForCategory("other");
	assertElementsEqual(
		"Unexpected children",
		"foo3() [in Y [in [Working copy] Y.js [in p [in src [in P]]]]]",
		others);
	IJavaScriptElement[] all = this.workingCopy.getType("Y").getChildrenForCategory("all");
	assertElementsEqual(
		"Unexpected children",
		"field [in Y [in [Working copy] Y.js [in p [in src [in P]]]]]\n" + 
		"foo1() [in Y [in [Working copy] Y.js [in p [in src [in P]]]]]\n" + 
		"foo2() [in Y [in [Working copy] Y.js [in p [in src [in P]]]]]\n" + 
		"foo3() [in Y [in [Working copy] Y.js [in p [in src [in P]]]]]",
		all);
}

/**
 * Ensures <code>getContents()</code> returns the correct value
 * for a <code>JavaScriptUnit</code> that is not present
 */
public void testGetContentsForNotPresent() {
	CompilationUnit compilationUnit = (CompilationUnit)getCompilationUnit("/P/src/p/Absent.js");
	
	assertSourceEquals("Unexpected contents for non present cu", "", new String(compilationUnit.getContents()));
}
/**
 * Tests Java element retrieval via source position 
 */
public void testGetElementAt() throws JavaScriptModelException {
	IField field = this.cu.getField( "f2");
	ISourceRange sourceRange= field.getSourceRange();
	//ensure that we are into the body of the type
	IJavaScriptElement element= 
		this.cu.getElementAt(sourceRange.getOffset() + field.getElementName().length() + 1);
	assertTrue("Should have found a type", element instanceof IField);
	assertEquals(
		"Should have found f2",
		"f2",
		element.getElementName());
	//ensure that null is returned if there is no element other than the compilation
 	//unit itself at the given position
 	element= this.cu.getElementAt(this.cu.getSourceRange().getOffset() + 1);
 	assertEquals("Should have not found any element", null, element);
}
///**
// * Tests import declararion retrieval via source position.
// * (regression test for bug 14331 IJavaScriptUnit.getElementAt dos not find import decl)
// */
//public void testGetElementAt2() throws JavaScriptModelException {
//	IImportContainer container = this.cu.getImportContainer();
//	ISourceRange sourceRange= container.getSourceRange();
//	//ensure that we are inside the import container
//	IJavaScriptElement element= this.cu.getElementAt(sourceRange.getOffset() + 1);
//	assertTrue("Should have found an import", element instanceof IImportDeclaration);
//	assertEquals(
//		"Import not found",
//		"p2.*",
//		 element.getElementName());
//}
/*
 * Ensures that the right field is returnd in a muti-declaration field.
 */
public void testGetElementAt3() throws JavaScriptModelException {
	int fieldPos = this.cu.getSource().indexOf("f5");
	IJavaScriptElement element= this.cu.getElementAt(fieldPos);
	assertEquals(
		"Unexpected field found",
		this.cu.getField("f5"),
		 element);
}
/*
 * Ensures that the right field is returnd in a muti-declaration field.
 */
public void testGetElementAt4() throws JavaScriptModelException {
	int fieldPos = this.cu.getSource().indexOf("f6");
	IJavaScriptElement element= this.cu.getElementAt(fieldPos);
	assertEquals(
		"Unexpected field found",
		this.cu.getField("f6"),
		 element);
}
/*
 * Ensures that the right field is returnd in a muti-declaration field.
 */
public void testGetElementAt5() throws JavaScriptModelException {
	int fieldPos = this.cu.getSource().indexOf("f7");
	IJavaScriptElement element= this.cu.getElementAt(fieldPos);
	assertEquals(
		"Unexpected field found",
		this.cu.getField("f7"),
		 element);
}
/*
 * Ensures that the right field is returned in a muti-declaration field.
 */
public void testGetElementAt6() throws JavaScriptModelException {
	int fieldPos = this.cu.getSource().indexOf("var f5");
	IJavaScriptElement element= this.cu.getElementAt(fieldPos);
	assertEquals(
		"Unexpected field found",
		this.cu.getField("f5"),
		element);
}
///*
// * Ensures that the right type is returnd if an annotation type as a comment in its header.
// */
//public void testGetElementAt7() throws JavaScriptModelException {
//	int fieldPos = this.cu.getSource().indexOf("Annot");
//	IJavaScriptElement element= this.cu.getElementAt(fieldPos);
//	assertEquals(
//		"Unexpected type found",
//		this.cu.getType("Annot"),
//		element);
//}
/**
 * Ensures that correct number of fields with the correct names, modifiers, signatures
 * and declaring types exist in a type.
 */
public void testGetFields() throws JavaScriptModelException {
//	IType type = this.cu.getType("X");
	IField[] fields= this.cu.getFields();
	String[] fieldNames = new String[] {"f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8"};
	String[] flags = new String[] {"public", "protected", "private", "", "", "", "", ""};
	String[] signatures = new String[] {"I", "QObject;", "QX;", "Qjava.lang.String;", "I", "I", "I", "I"};
	assertEquals("Wrong number of fields returned",  fieldNames.length, fields.length);
	for (int i = 0; i < fields.length; i++) {
		assertEquals("Incorrect name for the " + i + " field", fieldNames[i], fields[i].getElementName());
//		String mod= Flags.toString(fields[i].getFlags());
//		assertEquals("Unexpected modifier for " + fields[i].getElementName(), flags[i], mod);
//		assertEquals("Unexpected type signature for " + fields[i].getElementName(), signatures[i], fields[i].getTypeSignature());
//		assertEquals("Unexpected declaring type for " + fields[i].getElementName(), type, fields[i].getDeclaringType());
		assertTrue("Field should exist " + fields[i], fields[i].exists());
	}
}
///**
// * Ensure that import declaration handles are returned from the
// * compilation unit.
// * Checks non-existant handle, on demand and not.
// */
//public void testGetImport() {
//	IImportDeclaration imprt = this.cu.getImport("java.lang");
//	assertTrue("Import should not exist " + imprt, !imprt.exists());
//	
//	imprt = this.cu.getImport("p2.*");
//	assertTrue("Import should exist " + imprt, imprt.exists());
//	
//	imprt = this.cu.getImport("p3.Z");
//	assertTrue("Import should exist " + imprt, imprt.exists());
//}
///**
// * Ensures that correct number of imports with the correct names
// * exist in "GraphicsTest" compilation unit.
// */
//public void testGetImports() throws JavaScriptModelException {
//	IImportDeclaration[] imprts = this.cu.getImports();
//	IImportContainer container= this.cu.getImportContainer();
//	String[] importNames = new String[] {"p2.*", "p3.Z"};
//
//	assertEquals("Wrong number of imports returned", importNames.length, imprts.length);
//	for (int i = 0; i < imprts.length; i++) {
//		assertTrue("Incorrect name for the type in this position: " + imprts[i].getElementName(), imprts[i].getElementName().equals(importNames[i]));
//		assertTrue("Import does not exist " + imprts[i], imprts[i].exists());
//		if (i == 0) {
//			assertTrue("Import is not on demand " + imprts[i], imprts[i].isOnDemand());
//			assertTrue("Import should be non-static " + imprts[i], imprts[i].getFlags() == Flags.AccDefault);
//		} else {
//			assertTrue("Import is on demand " + imprts[i], !imprts[i].isOnDemand());
//			assertTrue("Import should be non-static " + imprts[i], imprts[i].getFlags() == Flags.AccDefault);
//		}
//		assertTrue("Container import does not equal import", container.getImport(imprts[i].getElementName()).equals(imprts[i]));
//	}
//
//	assertTrue("Import container must exist and have children", container.exists() && container.hasChildren());
//	ISourceRange containerRange= container.getSourceRange();
//	assertEquals(
//		"Offset container range not correct", 
//		imprts[0].getSourceRange().getOffset(),
//		containerRange.getOffset());
//	assertEquals(
//		"Length container range not correct",
//		imprts[imprts.length-1].getSourceRange().getOffset() + imprts[imprts.length-1].getSourceRange().getLength(),
//		containerRange.getOffset() + containerRange.getLength());
//	assertSourceEquals("Source not correct", 
//		"import p2.*;\n" +
//		"import p3.Z;",
//		container.getSource());
//	
//}
/**
 * Ensure that type handles are returned from the
 * compilation unit for an inner type.
 */
//public void testGetInnerTypes() throws JavaScriptModelException {
//	IType type1 = cu.getType("X");
//	assertTrue("X type should have children", type1.hasChildren());
//	assertTrue("X type superclass name should be null", type1.getSuperclassName() == null);
//	String[] superinterfaceNames= type1.getSuperInterfaceNames();
//	assertEquals("X type should have one superinterface", 1, superinterfaceNames.length);
//	assertEquals("Unexpected super interface name", "Runnable", superinterfaceNames[0]);
//	assertEquals("Fully qualified name of the type is incorrect", "p.X", type1.getFullyQualifiedName());
//	IType type2 = type1.getType("Inner");
//	superinterfaceNames = type2.getSuperInterfaceNames();
//	assertEquals("X$Inner type should not have a superinterface", 0, superinterfaceNames.length);
//	assertEquals("Fully qualified name of the inner type is incorrect", "p.X$Inner", type2.getFullyQualifiedName());
//	assertEquals("Declaring type of the inner type is incorrect", type1, type2.getDeclaringType());
//	IType type3 = type2.getType("InnerInner");
//	assertTrue("InnerInner type should not have children", !type3.hasChildren());
//}
/*
 * Ensures that the key for a top level type is correct
 */
public void testGetKey1() {
//	IType type = this.cu.getType("X");
//	assertEquals("Lp/X;", type.getKey());
	IField type = this.cu.getField("f2");
	assertEquals("Up/X.js;.f2", type.getKey());
}
/*
 * Ensures that the key for a member type is correct
 */
public void testGetKey2() {
	IType type = this.cu.getType("X").getType("Inner");
	assertEquals("Lp/X$Inner;", type.getKey());
}
/*
 * Ensures that the key for a secondary type is correct
 */
public void testGetKey3() {
	IType type = this.cu.getType("I");
	assertEquals("Lp/X~I;", type.getKey());
}
/*
 * Ensures that the key for an anonymous type is correct
 */
public void testGetKey4() {
	IType type = this.cu.getType("X").getFunction("foo", new String[0]).getType("", 1);
	assertEquals("Lp/X$1;", type.getKey());
}
/**
 * Ensures that a method has the correct return type, parameters and exceptions.
 */
public void testGetMethod1() throws JavaScriptModelException {
//	IType type = this.cu.getType("X");
	IFunction foo = this.cu.getFunction("foo", new String[]{null});
//	String[] exceptionTypes= foo.getExceptionTypes();
//	assertEquals("Wrong number of exception types", 1, exceptionTypes.length);
//	assertEquals("Unxepected exception type", "QIOException;", exceptionTypes[0]);
//	assertEquals("Wrong return type", "V", foo.getReturnType());
	String[] parameterNames = foo.getParameterNames();
	assertEquals("Wrong number of parameter names", 1, parameterNames.length);
	assertEquals("Unexpected parameter name", "y", parameterNames[0]);
}
///**
// * Ensures that a method has the correct AccVarargs flag set.
// */
//public void testGetMethod2() throws JavaScriptModelException {
//	IType type = this.cu.getType("X");
//	IFunction method = type.getMethod("testIsVarArgs", new String[]{"QString;", "[QObject;"});
//	assertTrue("Should have the AccVarargs flag set", Flags.isVarargs(method.getFlags()));
//}
///**
// * Ensures that a constructor has the correct AccVarargs flag set.
// * (regression test for bug 77422 [1.5] ArrayIndexOutOfBoundsException with vararg constructor of generic superclass)
// */
//public void testGetMethod3() throws JavaScriptModelException {
//	IType type = this.cu.getType("X");
//	IFunction method = type.getMethod("X", new String[]{"[QString;"});
//	assertTrue("Should have the AccVarargs flag set", Flags.isVarargs(method.getFlags()));
//}
/**
 * Ensures that correct number of methods with the correct names and modifiers
 * exist in a type.
 */
public void testGetMethods() throws JavaScriptModelException {
//	IType type = this.cu.getType("X");
	IFunction[] methods= this.cu.getFunctions();
	String[] methodNames = new String[] {"foo", "bar", "fred", "fred2"};
	String[] flags = new String[] {"public", "protected static", "private", "private"};
	assertEquals("Wrong number of methods returned", methodNames.length, methods.length);
	for (int i = 0; i < methods.length; i++) {
		assertEquals("Incorrect name for the " + i + " method", methodNames[i], methods[i].getElementName());
		int modifiers = methods[i].getFlags() & ~Flags.AccVarargs;
//		String mod= Flags.toString(modifiers);
//		assertEquals("Unexpected modifier for " + methods[i].getElementName(), flags[i], mod);
		assertTrue("Method does not exist " + methods[i], methods[i].exists());
	}
}
///**
// * Ensures that correct modifiers are reported for a method in an interface.
// */
//public void testCheckInterfaceMethodModifiers() throws JavaScriptModelException {
//	IType type = this.cu.getType("I");
//	IFunction method = type.getMethod("run", new String[0]);
//	String expectedModifiers = "";
//	String modifiers = Flags.toString(method.getFlags() & ~Flags.AccVarargs);
//	assertEquals("Expected modifier for " + method.getElementName(), expectedModifiers, modifiers);
//}
///*
// * Ensures that IType#getSuperInterfaceTypeSignatures() is correct for a source type.
// */
//public void testGetSuperInterfaceTypeSignatures() throws JavaScriptModelException {
//	IType type = this.cu.getType("Y");
//	assertStringsEqual(
//		"Unexpected signatures", 
//		"QI2<QE;>;\n",
//		type.getSuperInterfaceTypeSignatures());
//}
/**
 * Ensure that the same element is returned for the primary element of a
 * compilation unit.
 */
public void testGetPrimary() {
	IJavaScriptElement primary = this.cu.getPrimaryElement();
	assertEquals("Primary element for a compilation unit should be the same", this.cu, primary);
	primary = this.cu.getPrimary();
	assertEquals("Primary for a compilation unit should be the same", this.cu, primary);
	
}
///////*
////// * Ensures that the occurrence count for an initializer is correct
////// */
//////public void testGetOccurrenceCount01() {
//////	IInitializer initializer = this.cu.getType("X").getInitializer(2);
//////	assertEquals("Unexpected occurrence count", 2, initializer.getOccurrenceCount());
//////}
/////*
//// * Ensures that the occurrence count for an anonymous type is correct
//// */
////public void testGetOccurrenceCount02() {
////	IType type = this.cu.getType("X").getMethod("foo", new String[]{"QY;"}).getType("", 3);
////	assertEquals("Unexpected occurrence count", 3, type.getOccurrenceCount());
////}
///**
// * Ensures that correct number of package declarations with the correct names
// * exist a compilation unit.
// */
//public void testGetPackages() throws JavaScriptModelException {
//	IPackageDeclaration[] packages = this.cu.getPackageDeclarations();
//	String packageName = "p";
//	assertEquals("Wrong number of packages returned", 1, packages.length);
//	assertEquals("Wrong package declaration returned: ", packageName, packages[0].getElementName());
//}
/**
 * Ensure that type handles are returned from the
 * compilation unit.
 * Checks non-existant handle and existing handles.
 */
public void testGetType() {
	IField type = this.cu.getField("someType");
	assertTrue("Type should not exist " + type, !type.exists());
	
	type = this.cu.getField("f2");
	assertTrue("Type should exist " + type, type.exists());
	
//	type = this.cu.getType("I"); // secondary type
//	assertTrue("Type should exist " + type, type.exists());
}
///**
// * Ensures that correct number of types with the correct names and modifiers
// * exist in a compilation unit.
// */
//public void testGetTypes() throws JavaScriptModelException {
//	IType[] types = this.cu.getTypes();
//	String[] typeNames = new String[] {"X", "I", "I2", "I3", "Y", "Colors", "Annot"};
//	String[] flags = new String[] {"public", "", "", "", "", "", ""};
//	boolean[] isClass = new boolean[] {true, false, false, false, true, false, false};
//	boolean[] isInterface = new boolean[] {false, true, true, true, false, false, true};
//	boolean[] isAnnotation = new boolean[] {false, false, false, false, false, false, true};
//	boolean[] isEnum = new boolean[] {false, false, false, false, false, true, false};
//	String[] superclassName = new String[] {null, null, null, null, null, null, null};
//	String[] superclassType = new String[] {null, null, null, null, null, null, null};
//	String[][] superInterfaceNames = new String[][] {
//			new String[] {"Runnable"}, new String[0], new String[0], new String[0], new String[] {"I2<E>"}, new String[0], new String[0]
//	};
//	String[][] superInterfaceTypes = new String[][] {
//			new String[] {"QRunnable;"}, new String[0], new String[0], new String[0], new String[] {"QI2<QE;>;"}, new String[0], new String[0]
//	};
//	String[][] formalTypeParameters = new String[][] {
//		new String[0], new String[0], new String[] {"E"}, new String[0], new String[] {"E"}, new String[0], new String[0]
//	};
//	
//	assertEquals("Wrong number of types returned", typeNames.length, types.length);
//	for (int i = 0; i < types.length; i++) {
//		assertEquals("Incorrect name for the " + i + " type", typeNames[i], types[i].getElementName());
//		String mod= Flags.toString(types[i].getFlags());
//		assertEquals("Unexpected modifier for " + types[i].getElementName(), flags[i], mod);
//		assertTrue("Type does not exist " + types[i], types[i].exists());
//		assertEquals("Incorrect isClass for the " + i + " type", isClass[i], types[i].isClass());
//		assertEquals("Incorrect isInterface for the " + i + " type", isInterface[i], types[i].isInterface());
//		assertEquals("Incorrect isAnnotation for the " + i + " type", isAnnotation[i], types[i].isAnnotation());
//		assertEquals("Incorrect isEnum for the " + i + " type", isEnum[i], types[i].isEnum());
//		assertEquals("Incorrect superclassName for the " + i + " type", superclassName[i], types[i].getSuperclassName());
//		assertEquals("Incorrect superclassType for the " + i + " type", superclassType[i], types[i].getSuperclassTypeSignature());
//		assertEquals("Incorrect superInterfaceNames for the " + i + " type", superInterfaceNames[i].length, types[i].getSuperInterfaceNames().length);
//		assertEquals("Incorrect superInterfaceTypes for the " + i + " type", superInterfaceTypes[i].length, types[i].getSuperInterfaceTypeSignatures().length);
//		assertEquals("Incorrect formalTypeParameters for the " + i + " type", formalTypeParameters[i].length, types[i].getTypeParameters().length);
//	}
//}
/**
 * Ensures that a compilation unit has children.
 */
public void testHasChildren() throws JavaScriptModelException {
	this.cu.close();
	assertTrue("A closed compilation unit should have children", this.cu.hasChildren());
	this.cu.getChildren();
	assertTrue("The compilation unit should have children", this.cu.hasChildren());
}
/**
 * Ensures that a compilation unit's resource has not changed.
 */
public void testHasResourceChanged() {
	assertTrue(
		"A compilation unit's resource should not have changed", 
		!this.cu.hasResourceChanged());
}
///*
// * Ensures that hasChildren doesn't return true for an import container that doesn't exist
// * (regression test for bug 76761 [model] ImportContainer.hasChildren() should not return true
// */
//public void testImportContainerHasChildren() throws JavaScriptModelException {
//	IImportContainer importContainer = getCompilationUnit("/Test/DoesNotExist.js").getImportContainer();
//	boolean gotException = false;
//	try {
//		importContainer.hasChildren();
//	} catch (JavaScriptModelException e) {
//		gotException = e.isDoesNotExist();
//	}
//	assertTrue("Should get a not present exception", gotException);
//}
///*
// * Ensures that isEnumConstant returns true for a field representing an enum constant.
// */
//public void testIsEnumConstant1() throws JavaScriptModelException {
//	IField field = this.cu.getType("Colors").getField("BLUE");
//	assertTrue("Colors#BLUE should be an enum constant", field.isEnumConstant());
//}
///*
// * Ensures that isEnumConstant returns false for a field that is not representing an enum constant.
// */
//public void testIsEnumConstant2() throws JavaScriptModelException {
//	IField field = this.cu.getType("X").getField("f1");
//	assertTrue("X#f1 should not be an enum constant", !field.isEnumConstant());
//}
/*
 * Ensure that the utility method Util.#getNameWithoutJavaLikeExtension(String) works as expected
 * (regression test for bug 107735 StringIndexOutOfBoundsException in Util.getNameWithoutJavaLikeExtension())
 */
public void testNameWithoutJavaLikeExtension() {
	String name = Util.getNameWithoutJavaLikeExtension("Test.aj");
	assertEquals("Unepected name without extension", "Test.aj", name);
}
/**
 * Ensures that a compilation unit that does not exist responds
 * false to #exists() and #isOpen()
 */
public void testNotPresent1() {
	IJavaScriptUnit compilationUnit = ((IPackageFragment)this.cu.getParent()).getJavaScriptUnit("DoesNotExist.js");
	assertTrue("CU should not be open", !compilationUnit.isOpen());
	assertTrue("CU should not exist", !compilationUnit.exists());
	assertTrue("CU should still not be open", !compilationUnit.isOpen());
}
///**
// * Ensures that a compilation unit that does not exist
// * (because it is a child of a jar package fragment)
// * responds false to #exists() and #isOpen()
// * (regression test for PR #1G2RKD2)
// */
//public void testNotPresent2() throws CoreException {
//	IJavaScriptUnit compilationUnit = getPackageFragment("P", getExternalJCLPathString(), "java.lang").getCompilationUnit("DoesNotExist.js");
//	assertTrue("CU should not be open", !compilationUnit.isOpen());
//	assertTrue("CU should not exist", !compilationUnit.exists());
//	assertTrue("CU should still not be open", !compilationUnit.isOpen());
//}

/*
 * Ensure that the absence of visibility flags is correctly reported as package default
 * (regression test fo bug 127213 Flags class missing methods)
 */
public void testPackageDefaultFlag1() throws JavaScriptModelException {
	IField field = this.cu.getField("f4");
	assertTrue("X#f4 should be package default", Flags.isPackageDefault(field.getFlags()));
}

///*
// * Ensure that the presence of a visibility flags is correctly reported as non package default
// * (regression test fo bug 127213 Flags class missing methods)
// */
//public void testPackageDefaultFlag2() throws JavaScriptModelException {
//	IType type = this.cu.getType("X");
//	assertTrue("X should not be package default", !Flags.isPackageDefault(type.getFlags()));
//}

/*
 * Ensure that the presence of a visibility flags as well as the deprecated flag is correctly reported as non package default
 * (regression test fo bug 127213 Flags class missing methods)
 */
public void testPackageDefaultFlag3() throws JavaScriptModelException {
	IField field = this.cu.getType("X").getField("f2");
	assertTrue("X#f2 should not be package default", !Flags.isPackageDefault(field.getFlags()));
}

/*
 * Ensure that the absence of a visibility flags and the presence of the deprecated flag is correctly reported as package default
 * (regression test fo bug 127213 Flags class missing methods)
 */
public void testPackageDefaultFlag4() throws JavaScriptModelException {
	IType type = this.cu.getType("I");
	assertTrue("X should be package default", Flags.isPackageDefault(type.getFlags()));
}

/**
 * Ensures that the "structure is known" flag is set for a valid compilation unit. 
 */
public void testStructureKnownForCU() throws JavaScriptModelException {
	assertTrue("Structure is unknown for valid CU", this.cu.isStructureKnown());
}
/**
 *  Ensures that the "structure is unknown" flag is set for a non valid compilation unit. 
 */
public void testStructureUnknownForCU() throws CoreException {
	try {
		this.createFile(
			"/P/src/p/Invalid.js",
			"@#D(03");
		IJavaScriptUnit badCU = getCompilationUnit("/P/src/p/Invalid.js");
		assertTrue("Structure is known for an invalid CU", !badCU.isStructureKnown());
	} finally {
		this.deleteFile("/P/src/p/Invalid.js");
	}
}

///*
// * Ensure that the super flags is correctly reported
// * (regression test fo bug 127213 Flags class missing methods)
// */
//public void testSuperFlag1() throws JavaScriptModelException {
//	assertTrue("Should contain super flag", Flags.isSuper(Flags.AccSuper));
//}
//
///*
// * Ensure that the super flags is correctly reported
// * (regression test fo bug 127213 Flags class missing methods)
// */
//public void testSuperFlag2() throws JavaScriptModelException {
//	assertTrue("Should not contain super flag", !Flags.isSuper(Flags.AccDefault));
//}

///*
// * Verify fix for bug 73884: [1.5] Unexpected error for class implementing generic interface
// * (see bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=73884)
// */
//public void testBug73884() throws CoreException {
//	try {
//		String cuSource = 
//			"package p;\n" +
//			"public interface I<T> {\n" +
//			"}";
//		createFile("/P/src/p/I.js", cuSource);
//		ITypeParameter[] typeParameters = getCompilationUnit("/P/src/p/I.js").getType("I").getTypeParameters();
//		assertTypeParametersEqual(
//			"T\n",
//			typeParameters);
//	} finally {
//		deleteFile("/P/src/p/I.js");
//	}
//}

///*
// * Ensure that the type parameters for a type are correct.
// */
//public void testTypeParameter1() throws CoreException {
//	createWorkingCopy(
//		"package p;\n" +
//		"public class Y<T> {\n" +
//		"}"
//	);
//	ITypeParameter[] typeParameters = workingCopy.getType("Y").getTypeParameters();
//	assertTypeParametersEqual(
//		"T\n",
//		typeParameters);
//}
//
///*
// * Ensure that the type parameters for a type are correct.
// */
//public void testTypeParameter2() throws CoreException {
//	createWorkingCopy(
//		"package p;\n" +
//		"public class Y<T, U> {\n" +
//		"}"
//	);
//	ITypeParameter[] typeParameters = workingCopy.getType("Y").getTypeParameters();
//	assertTypeParametersEqual(
//		"T\n" +
//		"U\n",
//		typeParameters);
//}
//
///*
// * Ensure that the type parameters for a type are correct.
// */
//public void testTypeParameter3() throws CoreException {
//	createWorkingCopy(
//		"package p;\n" +
//		"public class Y<T extends List> {\n" +
//		"}"
//	);
//	ITypeParameter[] typeParameters = workingCopy.getType("Y").getTypeParameters();
//	assertTypeParametersEqual(
//		"T extends List\n",
//		typeParameters);
//}
//
///*
// * Ensure that the type parameters for a type are correct.
// */
//public void testTypeParameter4() throws CoreException {
//	createWorkingCopy(
//		"package p;\n" +
//		"public class Y<T extends List & Runnable & Comparable> {\n" +
//		"}"
//	);
//	ITypeParameter[] typeParameters = workingCopy.getType("Y").getTypeParameters();
//	assertTypeParametersEqual(
//		"T extends List & Runnable & Comparable\n",
//		typeParameters);
//}
//
///*
// * Ensure that the type parameters for a method are correct.
// * (regression test for bug 75658 [1.5] SourceElementParser do not compute correctly bounds of type parameter)
// */
//public void testTypeParameter5() throws CoreException {
//	createWorkingCopy(
//		"package p;\n" +
//		"public class Y {\n" +
//		"  <T extends List, U extends X & Runnable> void foo() {\n" +
//		"  }\n" +
//		"}"
//	);
//	ITypeParameter[] typeParameters = workingCopy.getType("Y").getMethod("foo", new String[]{}).getTypeParameters();
//	assertTypeParametersEqual(
//		"T extends List\n" + 
//		"U extends X & Runnable\n",
//		typeParameters);
//}
//
///*
// * Verify fix for bug 78275: [recovery] NPE in GoToNextPreviousMemberAction with syntax error
// * (see bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=78275)
// */
//public void testBug78275() throws CoreException {
//	try {
//		String cuSource = 
//			"	function a() {\n" + 
//			"	     }\n" + 
//			"	}\n" + 
//			"	function m() {}\n" + 
//			"";
//		createFile("/P/src/X.js", cuSource);
//		IType type = getCompilationUnit("/P/src/X.js").getType("X");
//		IInitializer[] initializers = type.getInitializers();
//		assertEquals("Invalid number of initializers", 1, initializers.length);
//		assertTrue("Invalid length for initializer", initializers[0].getSourceRange().getLength() > 0);
//	} finally {
//		deleteFile("/P/src/X.js");
//	}
//}
public void test110172() throws CoreException {
	try {
		String source =
			"	 /**\n" +
			"	  * Javadoc for field f \n" +
			"	  */\n" +
			"	var f;\n" +
			"	\n" +
			"	/**\n" +
			"	 * Javadoc for method foo\n" +
			"	 */\n" +
			"	function foo( i,  l,  s) {\n" +
			"	}\n" +
			"	\n" +
			"	/**\n" +
			"	 * Javadoc for member type A\n" +
			"	 */\n" +
			"	\n" +
			"	/**\n" +
			"	 * Javadoc for f3\n" +
			"	 */\n" +
			"	/*\n" +
			"	 * Not a javadoc comment\n" +
			"	 */\n" +
			"	/**\n" +
			"	 * Real javadoc for f3\n" +
			"	 */\n" +
			"	var f3;\n" +
			"	\n" +
			"	var f2;\n" +
			"	\n" +
			"	function foo2() {\n" +
			"	}\n" +
			"	\n" +
			"\n" +
			"";
		createFile("/P/src/X.js", source);
//		IType type = getCompilationUnit("/P/src/X.js").getType("X");
		IJavaScriptElement[] members = getCompilationUnit("/P/src/X.js").getChildren();
		final int length = members.length;
		assertEquals("Wrong number", 5, length);
		for (int i = 0; i < length; i++) {
			final IJavaScriptElement element = members[i];
			assertTrue(element instanceof IMember);
			final ISourceRange javadocRange = ((IMember) element).getJSdocRange();
			final String elementName = element.getElementName();
			if ("f".equals(elementName)) {
				assertNotNull("No javadoc source range", javadocRange);
				final int start = javadocRange.getOffset();
				final int end = javadocRange.getLength() + start - 1;
				String javadocSource = source.substring(start, end);
				assertTrue("Wrong javadoc", javadocSource.indexOf("field f") != -1);
			} else if ("foo".equals(elementName)) {
				assertNotNull("No javadoc source range", javadocRange);
				final int start = javadocRange.getOffset();
				final int end = javadocRange.getLength() + start - 1;
				String javadocSource = source.substring(start, end);
				assertTrue("Wrong javadoc", javadocSource.indexOf("method foo") != -1);
			} else if ("A".equals(elementName)) {
				assertNotNull("No javadoc source range", javadocRange);
				final int start = javadocRange.getOffset();
				final int end = javadocRange.getLength() + start - 1;
				String javadocSource = source.substring(start, end);
				assertTrue("Wrong javadoc", javadocSource.indexOf("member type A") != -1);
			} else if ("X".equals(elementName)) {
				// need treatment for the two constructors
				assertTrue("Not an IFunction", element instanceof IFunction);
				IFunction method = (IFunction) element;
				switch(method.getNumberOfParameters()) {
					case 0 :
						assertNull("Has a javadoc source range", javadocRange);
						break;
					case 1:
						assertNotNull("No javadoc source range", javadocRange);
						final int start = javadocRange.getOffset();
						final int end = javadocRange.getLength() + start - 1;
						String javadocSource = source.substring(start, end);
						assertTrue("Wrong javadoc", javadocSource.indexOf("constructor") != -1);
				}
			} else if ("f3".equals(elementName)) {
				assertNotNull("No javadoc source range", javadocRange);
				final int start = javadocRange.getOffset();
				final int end = javadocRange.getLength() + start - 1;
				String javadocSource = source.substring(start, end);
				assertTrue("Wrong javadoc", javadocSource.indexOf("Real") != -1);
			} else if ("f2".equals(elementName)) {
				assertNull("Has a javadoc source range", javadocRange);
			} else if ("foo2".equals(elementName)) {
				assertNull("Has a javadoc source range", javadocRange);
			} else if ("B".equals(elementName)) {
				assertNull("Has a javadoc source range", javadocRange);
			} else if (element instanceof IInitializer) {
				IInitializer initializer = (IInitializer) element;
				if (Flags.isStatic(initializer.getFlags())) {
					assertNotNull("No javadoc source range", javadocRange);
					final int start = javadocRange.getOffset();
					final int end = javadocRange.getLength() + start - 1;
					String javadocSource = source.substring(start, end);
					assertTrue("Wrong javadoc", javadocSource.indexOf("initializer") != -1);
				} else {
					assertNull("Has a javadoc source range", javadocRange);
				}
			}
		}
	} finally {
		deleteFile("/P/src/X.js");
	}
}
public void test120902() throws CoreException {
	try {
		String source =
			"/**\r\n" + 
			" * Toy\r\n" + 
			" */\r\n" + 
			"function foo() {\r\n" +
			"}";
		createFile("/P/src/X.js", source);
		final IJavaScriptUnit compilationUnit = getCompilationUnit("/P/src/X.js");
		IFunction type = compilationUnit.getFunction("foo",null);
		ISourceRange javadocRange = type.getJSdocRange();
		assertNotNull("No source range", javadocRange);
		compilationUnit.getBuffer().setContents("");
		try {
			javadocRange = type.getJSdocRange();
			assertNull("Got a source range", javadocRange);
		} catch (ArrayIndexOutOfBoundsException e) {
			assertFalse("Should not happen", true);
		}		
	} finally {
		deleteFile("/P/src/X.js");
	}
}
}
