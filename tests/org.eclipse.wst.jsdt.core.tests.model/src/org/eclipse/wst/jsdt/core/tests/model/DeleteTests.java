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

import junit.framework.Test;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.wst.jsdt.core.IClassFile;
import org.eclipse.wst.jsdt.core.IField;
import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.IImportDeclaration;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptModelStatusConstants;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.tests.util.Util;

/*
 * Tests for ISourceManipulation.delete(...)
 */
public class DeleteTests extends ModifyingResourceTests {

public DeleteTests(String name) {
	super(name);
}
protected void setUp() throws Exception {
	super.setUp();
	// ensure that indexing is not going to interfer with deletion
	waitUntilIndexesReady();
}
public void setUpSuite() throws Exception {
	super.setUpSuite();
	createJavaProject("P");
}
public static Test suite() {
	return buildModelTestSuite(DeleteTests.class);
}
// Use this static initializer to specify subset for tests
// All specified tests which do not belong to the class are skipped...
static {
//		TESTS_NAMES = new String[] { "testDeleteField5" };
//		TESTS_NUMBERS = new int[] { 2, 12 };
//		TESTS_RANGE = new int[] { 16, -1 };
}
public void tearDownSuite() throws Exception {
	deleteProject("P");
	super.tearDownSuite();
}
/**
 * Ensures that all import declarations can be deleted, and the container
 * is reported as removed.
 */
public void testDeleteAllImports() throws CoreException {
	try {
		createFile(
			"P/X.js",
			"import java.util.*;\n" +
			"import q.Y;\n" +
			"public class X {\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P/X.js");
		IImportDeclaration[] children= cu.getImports();

		startDeltas();
		assertDeletion(children);
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		<default>[*]: {CHILDREN}\n" + 
			"			X.java[*]: {CHILDREN | FINE GRAINED | PRIMARY RESOURCE}\n" + 
			"				<import container>[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteFile("P/X.js");
	}
}
/**
 * Ensures that a binary method cannot be deleted.
 */
public void testDeleteBinaryMethod() throws CoreException {
	try {
		createJavaProject("P1", new String[] {}, new String[] {"lib"});
		
		/* Evaluate the following in a scrapbook:
			org.eclipse.wst.jsdt.core.tests.model.ModifyingResourceTests.generateClassFile(
				"X",
				"public class X {\n" +
				"  public void foo() {\n" +
				"  }\n" +
				"}")
		*/
		byte[] bytes = new byte[] {
			-54, -2, -70, -66, 0, 3, 0, 45, 0, 14, 1, 0, 1, 88, 7, 0, 1, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 7, 0, 3, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 12, 0, 5, 0, 6, 10, 0, 4, 0, 8, 1, 0, 15, 76, 105, 110, 101, 78, 117, 
			109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 3, 102, 111, 111, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 6, 88, 46, 106, 97, 118, 97, 0, 33, 0, 2, 0, 4, 0, 0, 0, 0, 0, 2, 0, 1, 0, 5, 0, 6, 0, 1, 0, 7, 0, 0, 0, 29, 0, 1, 0, 1, 0, 0, 0, 5, 42, -73, 0, 9, -79, 0, 0, 0, 1, 
			0, 10, 0, 0, 0, 6, 0, 1, 0, 0, 0, 1, 0, 1, 0, 11, 0, 6, 0, 1, 0, 7, 0, 0, 0, 25, 0, 0, 0, 1, 0, 0, 0, 1, -79, 0, 0, 0, 1, 0, 10, 0, 0, 0, 6, 0, 1, 0, 0, 0, 3, 0, 1, 0, 12, 0, 0, 0, 2, 0, 13, 
		};
		this.createFile("P1/lib/X.class", bytes);
		
		IClassFile cf = getClassFile("P1/lib/X.class");
		IFunction method = cf.getType().getFunction("foo", new String[] {});
		
		try {
			method.delete(false, null);
		} catch (JavaScriptModelException e) {
			assertTrue("Should be read-only", e.getStatus().getCode() == IJavaScriptModelStatusConstants.READ_ONLY);
			return;
		}
		assertTrue("Should not be able to delete a binary method", false);
	} finally {
		deleteProject("P1");
	}
}
/**
 * Ensures that a binary type cannot be deleted.
 */
public void testDeleteBinaryType() throws CoreException {
	try {
		createJavaProject("P1", new String[] {}, new String[] {"lib"});
		
		/* Evaluate the following in a scrapbook:
			org.eclipse.wst.jsdt.core.tests.model.ModifyingResourceTests.generateClassFile(
				"X",
				"public class X {\n" +
				"}")
		*/
		byte[] bytes = new byte[] {
			-54, -2, -70, -66, 0, 3, 0, 45, 0, 13, 1, 0, 1, 88, 7, 0, 1, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 7, 0, 3, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 12, 0, 5, 0, 6, 10, 0, 4, 0, 8, 1, 0, 15, 76, 105, 110, 101, 78, 117, 
			109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 6, 88, 46, 106, 97, 118, 97, 0, 33, 0, 2, 0, 4, 0, 0, 0, 0, 0, 1, 0, 1, 0, 5, 0, 6, 0, 1, 0, 7, 0, 0, 0, 29, 0, 1, 0, 1, 0, 0, 0, 5, 42, -73, 0, 9, -79, 0, 0, 0, 1, 0, 10, 0, 0, 0, 6, 
			0, 1, 0, 0, 0, 1, 0, 1, 0, 11, 0, 0, 0, 2, 0, 12, 
		};
		this.createFile("P1/lib/X.class", bytes);
		
		IClassFile cf = getClassFile("P1/lib/X.class");
		IType binaryType = cf.getType();
		
		try {
			cf.getJavaScriptModel().delete(new IJavaScriptElement[] {binaryType}, false, null);
		} catch (JavaScriptModelException e) {
			assertTrue("Should be read-only", e.getStatus().getCode() == IJavaScriptModelStatusConstants.READ_ONLY);
			return;
		}
		assertTrue("Should not be able to delete a class file", false);
	} finally {
		deleteProject("P1");
	}
}
/**
 * Should be able to delete a CU.
 */
public void testDeleteCompilationUnit1() throws CoreException {
	try {
		createFile(
			"P/X.js",
			"public class X {\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P/X.js");

		startDeltas();
		cu.delete(false, null);
		assertTrue("Should be able to delete a CU", !cu.exists());
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		<default>[*]: {CHILDREN}\n" + 
			"			X.java[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteFile("P/X.js");
	}
}

/**
 * Ensure that if a CU is deleted from underneath us in the default
 * package of a nested root, it disappears from existence.
 */
public void testDeleteCompilationUnit2() throws CoreException {
	try {
		IFile file = createFile(
			"P/X.js",
			"public class X {\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P/X.js");

		startDeltas();
		Util.delete(file);
		assertTrue("Should be able to delete a CU", !cu.exists());
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		<default>[*]: {CHILDREN}\n" + 
			"			X.java[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteFile("P/X.js");
	}
}
/**
 * After deleting a CU in an IWorkspaceRunnable, it should not exist.
 * (regression test for bug 9232 IJavaScriptUnit.delete() fails)
 */
public void testDeleteCompilationUnit3() throws CoreException {
	try {
		createFile(
			"P/X.js",
			"public class X {\n" +
			"}"
		);
		final IJavaScriptUnit cu = getCompilationUnit("P/X.js");
		
		// force the cu to be opened
		cu.open(null);
	
		startDeltas();
		ResourcesPlugin.getWorkspace().run(
			new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					cu.delete(true, null);
					assertTrue("Should be able to delete a CU", !cu.exists());
				}
			},
			null
		);
		assertTrue("Should be able to delete a CU", !cu.exists());
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		<default>[*]: {CHILDREN}\n" + 
			"			X.java[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteFile("P/X.js");
	}
}
/**
 * Should be able to delete a CU in a non-default package.
 */
public void testDeleteCompilationUnit4() throws CoreException {
	try {
		createFolder("P/p");
		IFile file = createFile(
			"P/p/X.js",
			"package p;\n" +
			"public class X {\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P/p/X.js");

		startDeltas();
		cu.delete(false, null);
		assertTrue("CU should not exist", !cu.exists());
		assertTrue("Corresponding file should not exist", !file.exists());
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		p[*]: {CHILDREN}\n" + 
			"			X.java[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteFolder("P/p");
	}
}
/**
 * Ensures that a constructor can be deleted.
 * Verify that the correct change deltas are generated.
 */
public void testDeleteConstructor() throws CoreException {
	try {
		createFile(
			"P/X.js",
			"public class X {\n" +
			"  public X(String s) {\n" +
			"  }\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P/X.js");
		IFunction constructor = cu.getType("X").getFunction("X", new String[] {"QString;"});

		startDeltas();
		assertDeletion(constructor);
		assertTrue("Should be able to delete a constructor", !constructor.exists());
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		<default>[*]: {CHILDREN}\n" + 
			"			X.java[*]: {CHILDREN | FINE GRAINED | PRIMARY RESOURCE}\n" + 
			"				X[*]: {CHILDREN | FINE GRAINED}\n" + 
			"					X(String)[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteFile("P/X.js");
	}
}
/**
 * Ensure that deleting an empty package fragment that has a sub-package is not possible.
 */
public void testDeleteEmptyPackageFragment() throws CoreException {
	try {
		createFolder("P/p1/p2");
		IPackageFragment pkg = getPackage("P/p1");
		IFolder folder = getFolder("P/p1");

		startDeltas();
		pkg.delete(false, null);
		assertTrue("Folder should exist", folder.exists());
		assertTrue("Fragment should exist", pkg.exists());
		assertDeltas(
			"Unexpected delta",
			""
		);
	} finally {
		stopDeltas();
		deleteFolder("P/p1");
	}
}
/**
 * Ensures that a field can be deleted.
 */
public void testDeleteField1() throws CoreException { // was testDeleteField
	try {
		createFile(
			"P/X.js",
			"public class X {\n" +
			"  int field;\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P/X.js");
		IField field = cu.getType("X").getField("field");

		startDeltas();
		assertDeletion(field);
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		<default>[*]: {CHILDREN}\n" + 
			"			X.java[*]: {CHILDREN | FINE GRAINED | PRIMARY RESOURCE}\n" + 
			"				X[*]: {CHILDREN | FINE GRAINED}\n" + 
			"					field[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteFile("P/X.js");
	}
}
/**
 * Ensures that deletion can be canceled.
 */
public void testDeleteField2() throws CoreException { // was testDeleteFieldWithCancel
	try {
		createFile(
			"P/X.js",
			"public class X {\n" +
			"  int field;\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P/X.js");
		IField field = cu.getType("X").getField("field");

		boolean isCanceled = false;
		try {
			TestProgressMonitor monitor = TestProgressMonitor.getInstance();
			monitor.setCancelledCounter(1);
			getJavaModel().delete(new IJavaScriptElement[] {field}, false, monitor);
		} catch (OperationCanceledException e) {
			isCanceled = true;
		}
		assertTrue("Operation should have thrown an operation canceled exception", isCanceled);
	} finally {
		deleteFile("P/X.js");
	}
}
/*
 * Ensures that a field can be deleted inside a scheduling rule that include the resource only.
 * (regression test for bug 73078 ISourceManipulation.delete() tries to run in WorkspaceRoot scheduling rule)
 */
public void testDeleteField3() throws CoreException {
	try {
		IFile file = createFile(
			"P/X.js",
			"public class X {\n" +
			"  int field;\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P/X.js");
		final IField field = cu.getType("X").getField("field");

		startDeltas();
		getWorkspace().run(
			new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					assertDeletion(field);
				}
			}, 
			file,
			IWorkspace.AVOID_UPDATE,
			null);
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		<default>[*]: {CHILDREN}\n" + 
			"			X.java[*]: {CHILDREN | FINE GRAINED | PRIMARY RESOURCE}\n" + 
			"				X[*]: {CHILDREN | FINE GRAINED}\n" + 
			"					field[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteFile("P/X.js");
	}
}
/*
 * Ensures that a field with initializer can be deleted.
 * (regression test for bug 112935 IField.delete is not deleting the value of the variable.)
 */
public void testDeleteField4() throws CoreException {
	try {
		createFile(
			"P/X.js",
			"public class X {\n" +
			"  private String t = \"sample test\";\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P/X.js");
		IField field = cu.getType("X").getField("t");
		field.delete(false, null);
		assertSourceEquals(
			"Unexpected source", 
			"public class X {\n" + 
			"}",
			cu.getSource());
	} finally {
		deleteFile("P/X.js");
	}
}
/**
 * Delete enum
 */
public void testDeleteField5() throws CoreException {
	try {
		this.createJavaProject("P1", new String[] {""}, new String[] {"JCL15_LIB"}, null, "", "1.5");
		createFile(
			"P1/X.js",
			"public enum X {\n" +
			"  A, B, C\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P1/X.js");
		IField field = cu.getType("X").getField("A");
		field.delete(false, null);
		assertSourceEquals(
			"Unexpected source", 
			"public enum X {\n" + 
			"  B, C\n" + 
			"}",
			cu.getSource());
	} finally {
		deleteProject("P1");
	}
}
/**
 * Ensures that an import declaration can be deleted.
 */
public void testDeleteImportDeclaration() throws CoreException {
	try {
		createFile(
			"P/X.js",
			"import java.util.*;\n" +
			"import q.Y;\n" +
			"public class X {\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P/X.js");
		IImportDeclaration imp= cu.getImport("q.Y");

		startDeltas();
		assertDeletion(imp);
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		<default>[*]: {CHILDREN}\n" + 
			"			X.java[*]: {CHILDREN | FINE GRAINED | PRIMARY RESOURCE}\n" + 
			"				<import container>[*]: {CHILDREN | FINE GRAINED}\n" + 
			"					import q.Y[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteFile("P/X.js");
	}
}
/**
 * Ensures that a method can be deleted.
 */
public void testDeleteMethod() throws CoreException {
	try {
		createFile(
			"P/X.js",
			"public class X {\n" +
			"  public void foo() {\n" +
			"  }\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P/X.js");
		IFunction method = cu.getType("X").getFunction("foo", new String[] {});

		startDeltas();
		assertDeletion(method);
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		<default>[*]: {CHILDREN}\n" + 
			"			X.java[*]: {CHILDREN | FINE GRAINED | PRIMARY RESOURCE}\n" + 
			"				X[*]: {CHILDREN | FINE GRAINED}\n" + 
			"					foo()[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteFile("P/X.js");
	}
}
/**
 * Ensures that multiple member Java elements contained within different
 * compilation units can be deleted.
 * Verifies that the correct changed deltas are generated.
 */
public void testDeleteMultipleMembersFromVariousCUs() throws CoreException {
	try {
		createFolder("P/a/b/c");
		createFile(
			"P/a/b/c/X.js",
			"package a.b.c;\n" +
			"import java.util.Vector;\n" +
			"import java.util.Enumeration;\n" +
			"public class X {\n" +
			"  public static void main(String[] args) {\n" +
			"    System.out.println(\"Hello World\");\n" +
			"  }\n" +
			"  static class Bar {\n" +
			"    private final java.lang.String test = \"testminor\";\n" +
			"    public Bar() {\n" +
			"      super();\n" +
			"    }\n" +
			"    private void test() {\n" +
			"    }\n" +
			"  }\n" +
			"}"
		);
		createFile(
			"P/a/b/Y.js",
			"package a.b;\n" +
			"public class Y {\n" +
			"  int foo;\n" +
			"  public static void main(String[] args) {\n" +
			"    System.out.println(\"Hello World\");\n" +
			"  }\n" +
			"}"
		);
		
		// elements to be deleted:
		// from a/b/c/X.java:
		//   java.util.Vector
		//	  main
		//   Bar (inner type)
		//	    Bar (constructor)
		//	    test
		//   Bar (inner type, same as above)
	
		// from a/b/Y.java
		//   foo
		//   main
		
		IJavaScriptUnit cuX = getCompilationUnit("P/a/b/c/X.js");
		IType typeX = cuX.getType("X");
		IType typeBar = typeX.getType("Bar");
	
		IJavaScriptElement[] toBeDeleted = new IJavaScriptElement[8];
		toBeDeleted[0] = cuX.getImport("java.util.Vector");
		toBeDeleted[1] = typeX.getFunction("main", new String[] {"[QString;"});
		toBeDeleted[2] = typeBar;
		toBeDeleted[3] = typeBar.getFunction("Bar", new String[] {});
		toBeDeleted[4] = typeBar.getFunction("test", new String[] {});
		toBeDeleted[5] = typeBar;
		
		IJavaScriptUnit cuY = getCompilationUnit("P/a/b/Y.js");
		IType typeY = cuY.getType("Y");
		
		toBeDeleted[6] = typeY.getField("foo");
		toBeDeleted[7] = typeY.getFunction("main", new String[] {"[QString;"});
	
		startDeltas();
		assertDeletion(toBeDeleted);
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		a.b[*]: {CHILDREN}\n" + 
			"			Y.java[*]: {CHILDREN | FINE GRAINED | PRIMARY RESOURCE}\n" + 
			"				Y[*]: {CHILDREN | FINE GRAINED}\n" + 
			"					foo[-]: {}\n" + 
			"					main(String[])[-]: {}\n" + 
			"		a.b.c[*]: {CHILDREN}\n" + 
			"			X.java[*]: {CHILDREN | FINE GRAINED | PRIMARY RESOURCE}\n" + 
			"				<import container>[*]: {CHILDREN | FINE GRAINED}\n" + 
			"					import java.util.Vector[-]: {}\n" + 
			"				X[*]: {CHILDREN | FINE GRAINED}\n" + 
			"					Bar[-]: {}\n" + 
			"					main(String[])[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteFolder("P/a");
	}
}
public void testDeletePackageFragment1() throws CoreException {
	try {
		createFolder("P/a/b/c");
		createFile(
			"P/a/b/c/X.js",
			"package a.b.c;\n" +
			"public class X {\n" +
			"}"
		);
		IPackageFragment pkg = getPackage("P/a/b/c");
		IFolder folder = getFolder("P/a/b/c");

		startDeltas();
		pkg.delete(false, null);
		assertTrue("Folder should not exist", !folder.exists());
		assertTrue("Fragment should not exist", !pkg.exists());
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		a.b.c[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteFolder("P/p1");
	}
}
/*
 * Ensures that deleting a default package doesn't remove the source folder.
 * (regression test for bug 38450 Delete: Removing default package removes source folder)
 */
public void testDeletePackageFragment2() throws CoreException {
	try {
		createJavaProject("P1", new String[] {"src"});
		IFile file = createFile(
			"P1/src/X.js",
			"public class X {\n" +
			"}"
		);
		IPackageFragment pkg = getPackage("P1/src");
		IFolder folder = getFolder("P1/src");
		IJavaScriptUnit cu = getCompilationUnit("P1/src/X.js");

		startDeltas();
		pkg.delete(false, null);
		assertTrue("Folder should still exist", folder.exists());
		assertTrue("Fragment should still exist", pkg.exists());
		assertTrue("File should no longer exist", !file.exists());
		assertTrue("Compilation unit should no longer exist", !cu.exists());
		assertDeltas(
			"Unexpected delta",
			"P1[*]: {CHILDREN}\n" + 
			"	src[*]: {CHILDREN}\n" + 
			"		<default>[*]: {CHILDREN}\n" + 
			"			X.java[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteProject("P1");
	}
}
/*
 * Ensures that deleting a default package where prj=src removes its compilation units is successful.
 * (regression test for bug 39926 deleting default package (not in source folder) does nothing)
 */
public void testDeletePackageFragment3() throws CoreException {
	try {
		createJavaProject("P1");
		IFile file = createFile(
			"P1/X.js",
			"public class X {\n" +
			"}"
		);
		IPackageFragment pkg = getPackage("P1");
		IProject project = getProject("P1");
		IJavaScriptUnit cu = getCompilationUnit("P1/X.js");

		startDeltas();
		pkg.delete(false, null);
		assertTrue("Project should still exist", project.exists());
		assertTrue("Fragment should still exist", pkg.exists());
		assertTrue("File should no longer exist", !file.exists());
		assertTrue("Compilation unit should no longer exist", !cu.exists());
		assertDeltas(
			"Unexpected delta",
			"P1[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		<default>[*]: {CHILDREN}\n" + 
			"			X.java[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteProject("P1");
	}
}
/*
 * Ensures that deleting a package that only contains a .class file is successful.
 * (regression test for bug 40606 Unable to discard empty package if containing .class files)
 */
public void testDeletePackageFragment4() throws CoreException {
	try {
		createJavaProject("P1");
		IFolder folder = createFolder("P1/p");
		IFile file = createFile("P1/p/X.class", "");
		IPackageFragment pkg = getPackage("P1/p");

		startDeltas();
		pkg.delete(false, null);
		assertTrue("Folder should no longer exist", !folder.exists());
		assertTrue("Fragment should no longer exist", !pkg.exists());
		assertTrue("File should no longer exist", !file.exists());
		assertDeltas(
			"Unexpected delta",
			"P1[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		p[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteProject("P1");
	}
}

/**
 * Ensures that a field can be deleted if it contains syntax errors
 */
public void testDeleteSyntaxErrorField() throws CoreException {
	try {
		createFile(
			"P/X.js",
			"public class X {\n" +
			"  int field\n" + // missing semi-colon
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P/X.js");
		IField field = cu.getType("X").getField("field");

		startDeltas();
		assertDeletion(field);
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		<default>[*]: {CHILDREN}\n" + 
			"			X.java[*]: {CHILDREN | FINE GRAINED | PRIMARY RESOURCE}\n" + 
			"				X[*]: {CHILDREN | FINE GRAINED}\n" + 
			"					field[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteFile("P/X.js");
	}
}
/**
 * Ensures that a method can be deleted if it contains syntax errors
 */
public void testDeleteSyntaxErrorInMethod1() throws CoreException {
	try {
		createFile(
			"P/X.js",
			"public class X {\n" +
			"  public void foo() {\n" +
			"    String s = ;\n" +
			"    System.out.println(s);\n" +
			"  }\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P/X.js");
		IFunction method = cu.getType("X").getFunction("foo", new String[] {});

		startDeltas();
		assertDeletion(method);
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		<default>[*]: {CHILDREN}\n" + 
			"			X.java[*]: {CHILDREN | FINE GRAINED | PRIMARY RESOURCE}\n" + 
			"				X[*]: {CHILDREN | FINE GRAINED}\n" + 
			"					foo()[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteFile("P/X.js");
	}
}
/**
 * Ensures that a method can be deleted if it contains syntax errors
 */
public void testDeleteSyntaxErrorInMethod2() throws CoreException {
	try {
		createFile(
			"P/X.js",
			"public class X {\n" +
			"  public void foo() \n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P/X.js");
		IFunction method = cu.getType("X").getFunction("foo", new String[] {});

		startDeltas();
		assertDeletion(method);
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		<default>[*]: {CHILDREN}\n" + 
			"			X.java[*]: {CHILDREN | FINE GRAINED | PRIMARY RESOURCE}\n" + 
			"				X[*]: {CHILDREN | FINE GRAINED}\n" + 
			"					foo()[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteFile("P/X.js");
	}
}
/**
 * Ensures that a method can be deleted if it contains syntax errors
 */
public void testDeleteSyntaxErrorInMethod3() throws CoreException {
	try {
		createFile(
			"P/X.js",
			"public class X {\n" +
			"  public void foo( \n" +
			"  }\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P/X.js");
		IFunction method = cu.getType("X").getFunction("foo", new String[] {});

		startDeltas();
		assertDeletion(method);
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		<default>[*]: {CHILDREN}\n" + 
			"			X.java[*]: {CHILDREN | FINE GRAINED | PRIMARY RESOURCE}\n" + 
			"				X[*]: {CHILDREN | FINE GRAINED}\n" + 
			"					foo()[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteFile("P/X.js");
	}
}
/**
 * Ensures that a type can be deleted if it contains syntax errors
 */
public void testDeleteSyntaxErrorType() throws CoreException {
	try {
		createFile(
			"P/X.js",
			"public class X {\n" +
			"  method() {\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P/X.js");
		IType type = cu.getType("X");

		startDeltas();
		assertDeletion(type);
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		<default>[*]: {CHILDREN}\n" + 
			"			X.java[*]: {CHILDREN | FINE GRAINED | PRIMARY RESOURCE}\n" + 
			"				X[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteFile("P/X.js");
	}
}
/**
 * Ensures that a type can be deleted from a compilation unit.
 */
public void testDeleteType1() throws CoreException{
	try {
		createFile(
			"P/X.js",
			"public class X {\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P/X.js");
		IType type = cu.getType("X");

		startDeltas();
		assertDeletion(type);
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		<default>[*]: {CHILDREN}\n" + 
			"			X.java[*]: {CHILDREN | FINE GRAINED | PRIMARY RESOURCE}\n" + 
			"				X[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteFile("P/X.js");
	}
}
/**
 * Delete a type in a default package that is nested
 * in a root folder that is not the project folder.
 */
public void testDeleteType2() throws CoreException {
	try {
		createJavaProject("P1", new String[] {"src"});
		createFile(
			"P1/src/X.js",
			"public class X {\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P1/src/X.js");
		IType type = cu.getType("X");

		startDeltas();
		assertDeletion(type);
		assertDeltas(
			"Unexpected delta",
			"P1[*]: {CHILDREN}\n" + 
			"	src[*]: {CHILDREN}\n" + 
			"		<default>[*]: {CHILDREN}\n" + 
			"			X.java[*]: {CHILDREN | FINE GRAINED | PRIMARY RESOURCE}\n" + 
			"				X[-]: {}"
		);
	} finally {
		stopDeltas();
		deleteProject("P1");
	}
}
/**
 * Ensure that the correct exception is thrown for invalid input to the <code>DeleteOperation</code>
 */
public void testDeleteWithInvalidInput() throws CoreException {
	IType type = null;
	try {
		createFile(
			"P/X.js",
			"public class X {\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("P/X.js");
		type = cu.getType("X");

		getJavaModel().delete(null, false, null);
	} catch (JavaScriptModelException e) {
		assertTrue("Should be an no elements to process: null supplied", e.getStatus().getCode() == IJavaScriptModelStatusConstants.NO_ELEMENTS_TO_PROCESS);
		try {
			getJavaModel().delete(new IJavaScriptElement[] {type}, false, null);
		} catch (JavaScriptModelException e2) {
			assertTrue("Should be an no elements to process: null in the array supplied", e2.getStatus().getCode() == IJavaScriptModelStatusConstants.NO_ELEMENTS_TO_PROCESS);
		}
		return;
	} finally {
		deleteFile("P/X.js");
	}
}
}
