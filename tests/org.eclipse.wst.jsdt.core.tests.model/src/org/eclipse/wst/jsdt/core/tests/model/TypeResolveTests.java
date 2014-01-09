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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.jsdt.core.*;

import junit.framework.Test;

public class TypeResolveTests extends ModifyingResourceTests {
	IJavaScriptUnit cu;
public TypeResolveTests(String name) {
	super(name);
}
private IType getType(IType[] types, String sourceTypeName) throws JavaScriptModelException {
	for (int i = 0; i < types.length; i++) {
		IType sourceType = types[i];
		if (sourceType.getTypeQualifiedName().equals(sourceTypeName)) {
			return sourceType;
		} else if ((sourceType = getType(sourceType.getTypes(), sourceTypeName)) != null) {
			return sourceType;
		}
	}
	return null;
}
private IType getType(String sourceTypeName) throws JavaScriptModelException {
	return getType(this.cu.getTypes(), sourceTypeName);
}
private String[][] resolveType(String typeName, String sourceTypeName) throws JavaScriptModelException {
	IType sourceType = this.getType(sourceTypeName);
	assertTrue("Type " + sourceTypeName + " was not found", sourceType != null);
	return sourceType.resolveType(typeName);
}
private String resultToString(String[][] result) {
	StringBuffer toString = new StringBuffer();
	if(result != null) {
		for (int i = 0; i < result.length; i++) {
			String[] qualifiedName = result[i];
			String packageName = qualifiedName[0];
			if (packageName.length() > 0) {
				toString.append(packageName);
				toString.append(".");
			}
			toString.append(qualifiedName[1]);
			if (i < result.length-1) {
				toString.append("\n");
			}
		}
	}
	return toString.toString();
}
/* (non-Javadoc)
 * @see org.eclipse.wst.jsdt.core.tests.model.AbstractJavaModelTests#setUpSuite()
 */
public void setUpSuite() throws Exception {
	super.setUpSuite();
	this.setUpJavaProject("TypeResolve");
	this.cu = this.getCompilationUnit("TypeResolve", "src", "p", "TypeResolve.js");
}
	static {
//		TESTS_NUMBERS = new int[] { 182, 183 };
//		TESTS_NAMES = new String[] {"test0177"};
	}
	public static Test suite() {
		return buildModelTestSuite(TypeResolveTests.class);
	}
/* (non-Javadoc)
 * @see org.eclipse.wst.jsdt.core.tests.model.SuiteOfTestCases#tearDownSuite()
 */
public void tearDownSuite() throws Exception {
	this.deleteProject("TypeResolve");
	super.tearDownSuite();
}
/**
 * Resolve the type "B" within one of the secondary types.
 * (regression test for bug 23829 IType::resolveType incorrectly returns null)
 */
public void testResolveInSecondaryType() throws JavaScriptModelException {
	IType type = this.getCompilationUnit("/TypeResolve/src/p3/B.js").getType("Test");
	String[][] types = type.resolveType("B");
	assertEquals(
		"Unexpected result", 
		"p3.B",
		this.resultToString(types));	
}
/**
 * Resolve the type "B" within one of its inner classes.
 */
public void testResolveMemberTypeInInner() throws JavaScriptModelException {
	String[][] types = this.resolveType("B", "TypeResolve$A$B$D");
	assertEquals(
		"Unexpected result", 
		"p.TypeResolve.A.B",
		this.resultToString(types));	
}
/*
 * Resolve a parameterized type
 * (regression test for bug 94903 Error setting method breakpoint in 1.5 project)
 */
public void testResolveParameterizedType() throws CoreException {
	try {
		createJavaProject("P", new String[] {"src"}, new String[] {"JCL15_LIB"}, "bin", "1.5");
		createFile(
			"/P/src/X.js",
			"public class X<T> {\n" +
			"  X<String> field;\n" +
			"}"
		);
		IType type = getCompilationUnit("/P/src/X.js").getType("X");
		String[][] types = type.resolveType("X<String>");
		assertEquals(
			"Unexpected result", 
			"X",
			this.resultToString(types));	
	} finally {
		deleteProject("P");
	}
}
/**
 * Resolve the type "C" within one of its sibling classes.
 */
public void testResolveSiblingTypeInInner() throws JavaScriptModelException {
	String[][] types = this.resolveType("C", "TypeResolve$A$B");
	assertEquals(
		"Unexpected result", 
		"p.TypeResolve.A.C",
		this.resultToString(types));	
}
/**
 * Resolve the type "X" with a type import for it
 * within an inner class
 */
public void testResolveTypeInInner() throws JavaScriptModelException {
	String[][] types = this.resolveType("X", "TypeResolve$A");
	assertEquals(
		"Unexpected result", 
		"p1.X",
		this.resultToString(types));	
}
/**
 * Resolve the type "Object" within a local class.
 * (regression test for bug 48350 IType#resolveType(String) fails on local types)
 */
public void testResolveTypeInInner2() throws JavaScriptModelException {
	IType type = this.getCompilationUnit("/TypeResolve/src/p5/A.js").getType("A").getFunction("foo", new String[] {}).getType("Local", 1);
	
	String[][] types = type.resolveType("Object");
	assertEquals(
		"Unexpected result", 
		"java.lang.Object",
		this.resultToString(types));		
}
/**
 * Resolve the type "String".
 */
public void testResolveTypeInJavaLang() throws JavaScriptModelException {
	String[][] types = this.resolveType("String", "TypeResolve");
	assertEquals(
		"Unexpected result", 
		"java.lang.String",
		this.resultToString(types));	
}
/**
 * Resolve the type "Vector" with no imports.
 */
public void testResolveTypeWithNoImports() throws JavaScriptModelException {
	String[][] types = this.resolveType("Vector", "TypeResolve");
	assertEquals(
		"Unexpected result", 
		"",
		this.resultToString(types));	
}
/**
 * Resolve the type "Y" with an on-demand import.
 */
public void testResolveTypeWithOnDemandImport() throws JavaScriptModelException {
	String[][] types = this.resolveType("Y", "TypeResolve");
	assertEquals(
		"Unexpected result", 
		"p2.Y",
		this.resultToString(types));	
}
/**
 * Resolve the type "X" with a type import for it.
 */
public void testResolveTypeWithTypeImport() throws JavaScriptModelException {
	String[][] types = this.resolveType("X", "TypeResolve");
	assertEquals(
		"Unexpected result", 
		"p1.X",
		this.resultToString(types));	
}
/**
 * Resolve the type "String".
 */
public void testResolveString() throws JavaScriptModelException {
	String[][] types = this.resolveType("String", "TypeResolve");
	assertEquals(
		"Unexpected result", 
		"java.lang.String",
		this.resultToString(types));	
}
/**
 * Resolve the type "A.Inner".
 */
public void testResolveInnerType1() throws JavaScriptModelException {
	IType type = this.getCompilationUnit("/TypeResolve/src/p4/B.js").getType("B");
	String[][] types = type.resolveType("A.Inner");
	assertEquals(
		"Unexpected result", 
		"p4.A.Inner",
		this.resultToString(types));		
}
/**
 * Resolve the type "p4.A.Inner".
 */
public void testResolveInnerType2() throws JavaScriptModelException {
	IType type = this.getCompilationUnit("/TypeResolve/src/p4/B.js").getType("B");
	String[][] types = type.resolveType("p4.A.Inner");
	assertEquals(
		"Unexpected result", 
		"p4.A.Inner",
		this.resultToString(types));		
}
}
