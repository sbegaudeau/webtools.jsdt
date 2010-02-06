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

import java.io.File;
import java.io.IOException;

import junit.framework.Test;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.jsdt.core.IImportDeclaration;
import org.eclipse.wst.jsdt.core.IJavaScriptModelStatusConstants;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;

public class CreateCompilationUnitTests extends ModifyingResourceTests {
public CreateCompilationUnitTests(String name) {
	super(name);
}
public static Test suite() {
	return buildModelTestSuite(CreateCompilationUnitTests.class);
}
public void setUp() throws Exception {
	super.setUp();
	createJavaProject("P");
	createFolder("/P/p");
	startDeltas();
}
public void tearDown() throws Exception {
	stopDeltas();
	deleteProject("P");
	super.tearDown();
}
/**
 * Ensures that a compilation unit can be created with specified source
 * in a package.
 * Verifies that the proper change deltas are generated as a side effect
 * of running the operation.
 * Ensure that the import container has been created correctly.
 */
//no imports in JS
public void _testCUAndImportContainer() throws JavaScriptModelException {
	IPackageFragment pkg = getPackage("/P/p");
	IJavaScriptUnit cu= pkg.createCompilationUnit("HelloImports.js", 
		("package p;\n" +
		"\n" +
		"import java.util.Enumeration;\n" +
		"import java.util.Vector;\n" +
		"\n" +
		"public class HelloImports {\n" +
		"\n" +
		"	public static main(String[] args) {\n" +
		"		System.out.println(\"HelloWorld\");\n" +
		"	}\n" +
		"}\n"),  false,null);
	assertCreation(cu);
	assertDeltas(
		"Unexpected delta",
		"P[*]: {CHILDREN}\n" + 
		"	<project root>[*]: {CHILDREN}\n" + 
		"		p[*]: {CHILDREN}\n" + 
		"			HelloImports.js[+]: {}"
	);
	IImportDeclaration[] imprts= cu.getImports();
	assertTrue("Import does not exist", imprts.length == 2 && imprts[0].exists());
	cu.close();
	imprts= cu.getImports();
	assertTrue("Import does not exist", imprts.length == 2 && imprts[0].exists());
}
/**
 * Ensures that a default compilation unit is created for a type if
 * it does not yet exist.
 */
public void testDefaultCU() throws CoreException {
	IPackageFragment pkg = getPackage("/P/p");
	IJavaScriptUnit cu= pkg.getJavaScriptUnit("Default.js");
	IType type= cu.createType("function Default (){this.foo = \"\";}", null, false, null);
	assertCreation(cu);
	assertCreation(type);
	assertDeltas(
		"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		p[*]: {CHILDREN}\n" + 
			"			Default.js[+]: {}\n" + 
			"\n" + 
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		p[*]: {CHILDREN}\n" + 
			"			Default.js[*]: {CHILDREN | FINE GRAINED | PRIMARY RESOURCE}\n" + 
			"				Default[+]: {}"
	);
	// CU should have a package statement and type
	assertElementDescendants(
		"Unexpected children",
		"Default.js\n" + 
		"  function Default()\n" + 
		"  class Default\n" +
		"    var foo\n" + 
		"    Default()",
		cu);

	// should fail if we try again
	try {
		pkg.createCompilationUnit("Default.js", "", false, null);
	} catch (JavaScriptModelException jme) {
		assertTrue("Exception status not correct for creating a cu that already exists", jme.getStatus().getCode() == IJavaScriptModelStatusConstants.NAME_COLLISION);
	}
	// should fail if we try again
	try {
		pkg.createCompilationUnit("Default.js", "public class Default {}", true, null);
		return;
	} catch (JavaScriptModelException jme) {
	}
	assertTrue("Creation should not fail if the compilation unit already exists", false);
}
/**
 * Ensures that a default compilation unit is created for a type if
 * it does not yet exist.
 */
public void testEmptyCU() {
	IPackageFragment pkg = getPackage("/P/p");
	// should fail if we try again
	try {
		pkg.createCompilationUnit("Empty.js", "", true, null);
	} catch (JavaScriptModelException jme) {
	}
	IJavaScriptUnit cu= pkg.getJavaScriptUnit("Empty.js");
	assertCreation(cu);
}
/*
 * Ensures that a compilation unit can be created even if a file already exists on the file system.
 * (regression test for bug 41611 CreateCompilationUnitOperation.executeOperation() should probably force creation more agressively)
 */
public void testForce() throws JavaScriptModelException, IOException {
	IPackageFragment pkg = getPackage("/P/p");
	File folder = pkg.getResource().getLocation().toFile();
	new File(folder, "X.js").createNewFile();
	IJavaScriptUnit cu = pkg.createCompilationUnit(
		"X.js", 
		"function X() {\n" +
		"  \"\".toLowerCase();\n" +
		"}",
		true, // force,
		null);
	assertCreation(cu);
	assertDeltas(
		"Unexpected delta",
		"P[*]: {CHILDREN}\n" + 
		"	<project root>[*]: {CHILDREN}\n" + 
		"		p[*]: {CHILDREN}\n" + 
		"			X.js[+]: {}"
	);
}
/**
 * Ensures that a compilation unit cannot be created with an invalid name
 * in a package.
 */
public void testInvalidName() {
	IPackageFragment pkg = getPackage("/P/p");
	try {
		pkg.createCompilationUnit("HelloWorld.j", null,  false, null);
	} catch (JavaScriptModelException jme) {
		assertTrue("Incorrect JavaScriptModelException thrown for creating a cu with invalid name", jme.getStatus().getCode() == IJavaScriptModelStatusConstants.INVALID_NAME);
		try {
			pkg.createCompilationUnit(null, null,  false,null);
		} catch (JavaScriptModelException jme2) {
			assertTrue("Incorrect JavaScriptModelException thrown for creating a cu with invalid name", jme2.getStatus().getCode() == IJavaScriptModelStatusConstants.INVALID_NAME);
			return;
		}
	}
	assertTrue("No JavaScriptModelException thrown for creating a cu with an invalid name", false);
}
/**
 * Ensures that a compilation unit cannot be created with <code>null</code> source
 * in a package.
 */
public void testNullContents() {
	IPackageFragment pkg = getPackage("/P/p");
	try {
		pkg.createCompilationUnit("HelloWorld.js", null, false, null);
	} catch (JavaScriptModelException jme) {
		assertTrue("Incorrect JavaScriptModelException thrown for creating a cu with null contents: " + jme, jme.getStatus().getCode() == IJavaScriptModelStatusConstants.INVALID_CONTENTS);
		return;
	}
	assertTrue("No JavaScriptModelException thrown for creating a cu with null contents", false);
}
/**
 * Ensures that a compilation unit can be created with specified source
 * in a package.
 * Verifies that the proper change deltas are generated as a side effect
 * of running the operation.
 */
public void testSimpleCreation() throws JavaScriptModelException {
	IPackageFragment pkg = getPackage("/P/p");
	IJavaScriptUnit cu= pkg.createCompilationUnit("HelloWorld.js", 
		("function HelloWorld() {\n" +
		"\n" +
		"	this.main = function(String[] args) {\n" +
		"		\"HelloWorld\".toLowerCase();\n" +
		"	}\n" +
		"}\n"), false, null);
	assertCreation(cu);
	assertDeltas(
		"Unexpected delta",
		"P[*]: {CHILDREN}\n" + 
		"	<project root>[*]: {CHILDREN}\n" + 
		"		p[*]: {CHILDREN}\n" + 
		"			HelloWorld.js[+]: {}"
	);
}
/**
 * Ensures that a default compilation unit is created for a type if
 * it does not yet exist.
 */
public void testDefaultCUNewFolder() throws CoreException {
	createFolder("/P/z");
	IPackageFragment pkg = getPackage("/P/z");
	IJavaScriptUnit cu= pkg.getJavaScriptUnit("Default.js");
	IType type= cu.createType("function Default2 (){this.foo = \"\";}", null, false, null);
	assertCreation(cu);
	assertCreation(type);
	assertDeltas(
		"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" +
			"		z[+]: {}\n"+
			"\n"+
			"P[*]: {CHILDREN}\n" +
			"	<project root>[*]: {CHILDREN}\n" + 
			"		z[*]: {CHILDREN}\n" + 
			"			Default.js[+]: {}\n" + 
			"\n" + 
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		z[*]: {CHILDREN}\n" + 
			"			Default.js[*]: {CHILDREN | FINE GRAINED | PRIMARY RESOURCE}\n" + 
			"				Default2[+]: {}"
	);
	// CU should have a package statement and type
	assertElementDescendants(
		"Unexpected children",
		"Default.js\n" + 
		"  function Default2()\n" + 
		"  class Default2\n" +
		"    var foo\n" + 
		"    Default2()",
		cu);

	// should fail if we try again
	try {
		pkg.createCompilationUnit("Default.js", "", false, null);
	} catch (JavaScriptModelException jme) {
		assertTrue("Exception status not correct for creating a cu that already exists", jme.getStatus().getCode() == IJavaScriptModelStatusConstants.NAME_COLLISION);
	}
	// should fail if we try again
	try {
		pkg.createCompilationUnit("Default.js", "public class Default {}", true, null);
		return;
	} catch (JavaScriptModelException jme) {
	}
	assertTrue("Creation should not fail if the compilation unit already exists", false);
}

/**
 * Ensures that creating a folder, compilation unit, and types results in
 * minimized deltas if in a workspace runnable, but that element info is
 * still made available.
 */
public void testDefaultCUNewFolderOperation() throws CoreException {
	IWorkspaceRunnable testRun = new IWorkspaceRunnable(){
		public void run(IProgressMonitor monitor) throws CoreException {
			createFolder("/P/z");
			IPackageFragment pkg = getPackage("/P/z");
			IJavaScriptUnit cu= pkg.getJavaScriptUnit("Default.js");
			IType type= cu.createType("function Default2 (){this.foo = \"\";}", null, false, null);
			assertCreation(cu);
			assertCreation(type);
		}
	};
	ResourcesPlugin.getWorkspace().run(testRun, null);
	
	assertDeltas(
				"Unexpected delta",
					"P[*]: {CHILDREN}\n" + 
					"	<project root>[*]: {CHILDREN}\n" +
					"		z[+]: {}"//\n"+
//					"\n"+
//					"P[*]: {CHILDREN}\n" +
//					"	<project root>[*]: {CHILDREN}\n" + 
//					"		z[*]: {CHILDREN}\n" + 
//					"			Default.js[+]: {}\n" + 
//					"\n" + 
//					"P[*]: {CHILDREN}\n" + 
//					"	<project root>[*]: {CHILDREN}\n" + 
//					"		z[*]: {CHILDREN}\n" + 
//					"			Default.js[*]: {CHILDREN | FINE GRAINED | PRIMARY RESOURCE}\n" + 
//					"				Default2[+]: {}"
			);

	IPackageFragment pkg = getPackage("/P/z");
	IJavaScriptUnit cu= pkg.getJavaScriptUnit("Default.js");
	
	// CU should have a package statement and type
	assertElementDescendants(
		"Unexpected children",
		"Default.js\n" + 
		"  function Default2()\n" + 
		"  class Default2\n" +
		"    var foo\n" + 
		"    Default2()",
		cu);

	// should fail if we try again
	try {
		pkg.createCompilationUnit("Default.js", "", false, null);
	} catch (JavaScriptModelException jme) {
		assertTrue("Exception status not correct for creating a cu that already exists", jme.getStatus().getCode() == IJavaScriptModelStatusConstants.NAME_COLLISION);
	}
	// should fail if we try again
	try {
		pkg.createCompilationUnit("Default.js", "public class Default {}", true, null);
		return;
	} catch (JavaScriptModelException jme) {
	}
	assertTrue("Creation should not fail if the compilation unit already exists", false);
}
}
