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

import junit.framework.Test;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IField;
import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;

public class CreateMembersTests extends AbstractJavaModelTests {

	public CreateMembersTests(String name) {
		super(name);
	}

	public static Test suite() {
		return buildModelTestSuite(CreateMembersTests.class, 1/*sort ascending order*/);
	}
	public void setUpSuite() throws Exception {
		super.setUpSuite();
		setUpJavaProject("CreateMembers", "1.5");
	}
	public void tearDownSuite() throws Exception {
		deleteProject("CreateMembers");

		super.tearDownSuite();
	}

	public void test001() throws JavaScriptModelException {
		IJavaScriptUnit compilationUnit = getCompilationUnit("CreateMembers", "src", "", "A.js");
		assertNotNull("No compilation unit", compilationUnit);
//		IType[] types = compilationUnit.getTypes();
//		assertNotNull("No types", types);
//		assertEquals("Wrong size", 1, types.length);
//		IType type = types[0];
		compilationUnit.createMethod("\tfunction foo() {\n\t\tSystem.out.println(\"Hello World\");\n\t}\n", null, true, new NullProgressMonitor());
		String expectedSource = 
			"var aVar;\n" + 
			"\n" + 
			"function foo() {\n" + 
			"\tSystem.out.println(\"Hello World\");\n" + 
			"}" +
			"";
		assertSourceEquals("Unexpected source", expectedSource, compilationUnit.getSource());
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=86906
	public void test002() throws JavaScriptModelException {
		IJavaScriptUnit compilationUnit = getCompilationUnit("CreateMembers", "src", "", "A2.js");
		assertNotNull("No compilation unit", compilationUnit);
//		IType[] types = compilationUnit.getTypes();
//		assertNotNull("No types", types);
//		assertEquals("Wrong size", 1, types.length);
//		IType type = types[0];
		IField sibling = compilationUnit.getField("aVar");
		compilationUnit.createField("var i;", sibling, true, null);
		String expectedSource = 
			"var i;\n" + 
			"\n" + 
			"var aVar;" + 
			"";
		assertSourceEquals("Unexpected source", expectedSource, compilationUnit.getSource());
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=86906
	public void test003() throws JavaScriptModelException {
		IJavaScriptUnit compilationUnit = getCompilationUnit("CreateMembers", "src", "", "Annot.js");
		assertNotNull("No compilation unit", compilationUnit);
		IType[] types = compilationUnit.getTypes();
		assertNotNull("No types", types);
		assertEquals("Wrong size", 1, types.length);
		IType type = types[0];
		IFunction sibling = type.getFunction("foo", new String[]{});
		type.createMethod("String bar();", sibling, true, null);
		String expectedSource = 
			"public @interface Annot {\n" + 
			"	String bar();\n" + 
			"\n" + 
			"	String foo();\n" + 
			"}";
		assertSourceEquals("Unexpected source", expectedSource, type.getSource());
	}
	
	/*
	 * Ensures that the handle for a created method that has varargs type arguments is correct.
	 * (regression test for bug 93487 IType#findMethods fails on vararg methods)
	 */
	public void test004() throws JavaScriptModelException {
		IType type = getCompilationUnit("/CreateMembers/src/A.js").getType("A");
		IFunction method = type.createMethod(
			"void bar(String... args) {}",
			null, // no siblings
			false, // don't force
			null // no progress monitor
		);
		assertTrue("Method should exist", method.exists());
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=95580
	public void test005() throws JavaScriptModelException {
		IJavaScriptUnit compilationUnit = getCompilationUnit("CreateMembers", "src", "", "E2.js");
		assertNotNull("No compilation unit", compilationUnit);
		IType[] types = compilationUnit.getTypes();
		assertNotNull("No types", types);
		assertEquals("Wrong size", 1, types.length);
		IType type = types[0];
		type.createField("int i;", null, true, null);
		String expectedSource = 
			"public enum E2 {\n" + 
			"	A, B, C;\n\n" +
			"	int i;\n" + 
			"}";
		assertSourceEquals("Unexpected source", expectedSource, type.getSource());
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=95580
	public void test006() throws JavaScriptModelException {
		IJavaScriptUnit compilationUnit = getCompilationUnit("CreateMembers", "src", "", "E3.js");
		assertNotNull("No compilation unit", compilationUnit);
		IType[] types = compilationUnit.getTypes();
		assertNotNull("No types", types);
		assertEquals("Wrong size", 1, types.length);
		IType type = types[0];
		type.createType("class DD {}", null, true, null);
		String expectedSource = 
			"public enum E3 {\n" + 
			"	A, B, C;\n\n" +
			"	class DD {}\n" + 
			"}";
		assertSourceEquals("Unexpected source", expectedSource, type.getSource());
	}
}
