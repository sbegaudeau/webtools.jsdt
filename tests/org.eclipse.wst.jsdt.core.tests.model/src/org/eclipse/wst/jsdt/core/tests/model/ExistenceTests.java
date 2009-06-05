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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.jsdt.core.*;
import org.eclipse.wst.jsdt.core.tests.util.Util;
public class ExistenceTests extends ModifyingResourceTests {
public ExistenceTests(String name) {
	super(name);
}

public static Test suite() {
	return buildModelTestSuite(ExistenceTests.class);
}
protected void assertCorrespondingResourceFails(IJavaScriptElement element) {
	boolean gotException = false;
	try {
		element.getCorrespondingResource();
	} catch (JavaScriptModelException e) {
		if (e.isDoesNotExist()) {
			gotException = true;
		}
	}
	assertTrue("Should not be able to get corresponding resource", gotException);
}
protected void assertOpenFails(String expectedMessage, IOpenable openable) {
	String message = "";
	try {
		openable.open(null);
	} catch (JavaScriptModelException e) {
		message = e.getMessage();
	}
	if (!expectedMessage.equals(message)) {
		System.out.print(Util.displayString(message, 3));
		System.out.println(",");
	}
	assertEquals(expectedMessage, message);
}
protected void assertUnderlyingResourceFails(IJavaScriptElement element) {
	boolean gotException = false;
	try {
		element.getUnderlyingResource();
	} catch (JavaScriptModelException e) {
		if (e.isDoesNotExist()) {
			gotException = true;
		}
	}
	assertTrue("Should not be able to get underlying resource", gotException);
}
public void testBinaryMethodAfterNonExistingMember() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {}, new String[] {});
		IClassFile classFile = project.getPackageFragmentRoot(getSystemJsPathString()).getPackageFragment("").getClassFile("system.js");
		classFile.open(null);
		IType type = classFile.getType();
		type.getFunction("foo", new String[0]).exists();
		assertTrue("Object.toString() should exist", type.getFunction("toString", new String[0]).exists());
	} finally {
		deleteProject("P");
	}
}
public void testClassFileInBinary() throws CoreException {
	try {
		this.createJavaProject("P", new String[] {"src"});
		this.createFile("P/bin/X.class", "");
		IJavaScriptElement classFile = this.getClassFile("P/bin/X.class");
		assertTrue(!classFile.exists());
	} finally {
		this.deleteProject("P");
	}
}
public void testClassFileInLibrary() throws CoreException {
	try {
		this.createJavaProject("P", new String[] {}, new String[] {"lib"});
		this.createFile("P/lib/X.class", "");
		IJavaScriptElement classFile = this.getClassFile("P/lib/X.class");
		assertTrue(classFile.exists());
	} finally {
		this.deleteProject("P");
	}
}
public void testClassFileInLibraryInOtherProject() throws CoreException {
	try {
		this.createJavaProject("P2", new String[] {});
		this.createFolder("P2/lib");
		String path = "P2/lib/X.class";
		IFile file = this.createFile(path, "");
		IJavaScriptProject p1 = createJavaProject("P1", new String[] {}, new String[] {"/P2/lib"});
		IJavaScriptElement nonExistingFile = getClassFile(path);
		assertFalse("File '"+path+"' should not exist in P2!", nonExistingFile.exists());
		IJavaScriptElement element = JavaScriptCore.create(getFolder("/P2/lib"));
		assertTrue("folder '/P2/lib' should be found in P1!", element.exists());
		IClassFile existingFile = (IClassFile)JavaScriptCore.create(file, p1);
		assertTrue("File '"+path+"' should exist in P1!", existingFile.exists());
	} finally {
		this.deleteProject("P1");
		this.deleteProject("P2");
	}
}
public void testJarFile() throws Exception {
	try {
		IJavaScriptProject p2 = createJavaProject("P2");
		String[] pathsAndContents = new String[] {
			"test/X.js", 
			"package test;\n" +
			"public class X {\n" + 
			"}",
		};
		addLibrary(p2, "lib.jar", "libsrc.zip", pathsAndContents, JavaScriptCore.VERSION_1_5);
		IJavaScriptProject p1 = createJavaProject("P1", new String[] {}, new String[] {"/P2/lib.jar"});
		IPackageFragmentRoot root2 = getPackageFragmentRoot("/P2/lib.jar");
		assertTrue(root2.exists());
		assertEquals(p1.getPackageFragmentRoots()[0], root2);
	} finally {
		this.deleteProject("P1");
		this.deleteProject("P2");
	}
}
/*
 * Ensure that an IClassFile handle created on a .class file in a source folder
 * doesn't not exist.
 * (regression test for bug 36499 exists() returns true for a source file inside a classfolder)
 */
public void testClassFileInSource1() throws CoreException {
	try {
		this.createJavaProject("P", new String[] {"src"});
		this.createFile("P/src/X.class", "");
		IJavaScriptElement classFile = this.getClassFile("P/src/X.class");
		assertTrue("Class file should not exist", !classFile.exists()); 
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Ensure that an IClassFile handle created on a .class file in a source folder
 * cannot be opened.
 * (regression test for bug 36499 exists() returns true for a source file inside a classfolder)
 */
 public void testClassFileInSource2() throws CoreException {
	try {
		this.createJavaProject("P", new String[] {"src"});
		this.createFile("P/src/X.class", "");
		IClassFile classFile = this.getClassFile("P/src/X.class");
		assertOpenFails(
			"Operation not supported for specified element type(s):src [in P]", 
			classFile);
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Ensure that an IJavaScriptUnit handle created on a .java file in a library folder
 * doesn't not exist.
 * (regression test for bug 36499 exists() returns true for a source file inside a classfolder)
 */
public void testCompilationUnitInLibrary1() throws CoreException {
	try {
		this.createJavaProject("P", new String[] {}, new String[] {"lib"});
		this.createFile(
			"P/lib/X.js", 
			"public class X {}"
		);
		IJavaScriptUnit cu = this.getCompilationUnit("P/lib/X.js");
		assertTrue("Ccompilation unit should not exist", !cu.exists()); 
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Ensure that an IJavaScriptUnit handle created on a .java file in a library folder
 *cannot be opened.
 * (regression test for bug 36499 exists() returns true for a source file inside a classfolder)
 */
public void testCompilationUnitInLibrary2() throws CoreException {
	try {
		this.createJavaProject("P", new String[] {}, new String[] {"lib"});
		this.createFile(
			"P/lib/X.js", 
			"public class X {}"
		);
		IJavaScriptUnit cu = this.getCompilationUnit("P/lib/X.js");
		assertOpenFails(
			"Operation not supported for specified element type(s):lib [in P]",
			cu);
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Ensure that a method with invalid parameters returns false to exists().
 * (regression test for bug 80338 getReturnType() throws a NullArgumentException)
 */
public void testMethodWithInvalidParameter() throws CoreException {
	try {
		createJavaProject("P");
		createFile(
			"P/X.js", 
			"public class X {}"
		);
		IFunction method = getCompilationUnit("P/X.js").getType("X").getFunction("foo", new String[] {"~12345@"});
		assertTrue("Methodr should not exist", !method.exists()); 
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensure that a non-existing class file cannot be opened.
 */
public void testNonExistingClassFile1() throws CoreException {
	try {
		this.createJavaProject("P", new String[] {"src"}, new String[] {"lib"});
		IClassFile classFile = getClassFile("/P/lib/X.class");
		assertOpenFails(
			"X.class [in <default> [in lib [in P]]] does not exist", 
			classFile);
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Ensure that a non-existing class file cannot be opened.
 * (regression test for 52379 JavaElement.getElementInfo no longer works)
 */
public void testNonExistingClassFile2() throws CoreException {
	try {
		this.createJavaProject("P", new String[] {"src"}, new String[] {});
		IClassFile classFile = getClassFile("/P/lib/X.class");
		assertOpenFails(
			"lib [in P] is not on its project\'s build path",
			classFile);
	} finally {
		this.deleteProject("P");
	}
}

/*
 * Ensure that a non-existing compilation unit cannot be opened.
 */
public void testNonExistingCompilationUnit() throws CoreException {
	try {
		this.createJavaProject("P", new String[] {"src"});
		IJavaScriptUnit cu = getCompilationUnit("/P/src/X.js");
		assertOpenFails(
			"X.java [in <default> [in src [in P]]] does not exist", 
			cu);
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Ensure that a non-existing package fragment cannot be opened.
 */
public void testNonExistingPackageFragment1() throws CoreException {
	try {
		this.createJavaProject("P", new String[] {"src"});
		IPackageFragment pkg = this.getPackage("/P/src/x");
		assertOpenFails(
			"x [in src [in P]] does not exist", 
			pkg);
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Ensure that a non-existing package fragment cannot be opened.
 */
public void testNonExistingPackageFragment2() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {});
		IFolder folder = createFolder("/P/src/x");
		IPackageFragment pkg = project.getPackageFragmentRoot(folder).getPackageFragment("x");
		assertOpenFails(
			"src/x [in P] is not on its project\'s build path",
			pkg);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensure that an excluded package fragment doesn't exist.
 * (regression test for bug 138577 Package content disapear in package explorer)
 */
public void testNonExistingPackageFragment3() throws CoreException {
	try {
		createJavaProject("P");
		createFolder("/P/pack");
		IPackageFragment pkg = getPackage("/P/pack");
		editFile(
			"/P/.classpath",
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"	<classpathentry excluding=\"pack/\" kind=\"src\" path=\"\"/>\n" + 
			"	<classpathentry kind=\"output\" path=\"\"/>\n" + 
			"</classpath>"
		);
		assertFalse(	"pack should not exist", pkg.exists());
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensure that a non-Java project doesn't exist.
 * (regression test for bug 28545 JavaProject.exists() returns true if project doesn't have Java nature)
 */
public void testNonJavaProject() throws CoreException {
	try {
		createProject("P");
		IProject project = getProject("P");
		IJavaScriptProject javaProject = JavaScriptCore.create(project);
		
		assertTrue("Simple project should not exist", !javaProject.exists());
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a package fragment root that is not on the classpath cannot be opened.
 */
public void testPkgFragmentRootNotInClasspath() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IFolder folder = createFolder("/P/otherRoot");
		IPackageFragmentRoot root = project.getPackageFragmentRoot(folder);
		assertTrue("Root should not exist", !root.exists());
		assertOpenFails(
			"otherRoot [in P] is not on its project\'s build path",
			root);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that one cannot get the corresponding resource of a non-existing class file.
 */
public void testCorrespondingResourceNonExistingClassFile() throws CoreException {
	try {
		createJavaProject("P", new String[] {"src"}, new String[] {"lib"});
		IJavaScriptElement classFile = getClassFile("/P/lib/X.class");
		assertCorrespondingResourceFails(classFile);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that one cannot get the corresponding resource of a non-existing compilation unit.
 */
public void testCorrespondingResourceNonExistingCompilationUnit() throws CoreException {
	try {
		createJavaProject("P", new String[] {"src"});
		IJavaScriptUnit compilationUnit = getCompilationUnit("/P/src/X.js");
		assertCorrespondingResourceFails(compilationUnit);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that one cannot get the corresponding resource of a non-existing jar package fragment root.
 */
public void testCorrespondingResourceNonExistingJarPkgFragmentRoot() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IPackageFragmentRoot root = project.getPackageFragmentRoot("/nonExisting.jar");
		assertCorrespondingResourceFails(root);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that one cannot get the corresponding resource of a non-existing package fragment.
 */
public void testCorrespondingResourceNonExistingPkgFragment() throws CoreException {
	try {
		createJavaProject("P", new String[] {"src"});
		IPackageFragment pkg = getPackage("/P/src/nonExisting");
		assertCorrespondingResourceFails(pkg);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that one cannot get the corresponding resource of a non-existing package fragment root.
 */
public void testCorrespondingResourceNonExistingPkgFragmentRoot() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IFolder folder = createFolder("/P/nonExistingRoot");
		IPackageFragmentRoot root = project.getPackageFragmentRoot(folder);
		assertCorrespondingResourceFails(root);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that one cannot get the corresponding resource of a non-existing java project.
 */
public void testCorrespondingResourceNonExistingProject() {
	IProject nonExistingProject = ResourcesPlugin.getWorkspace().getRoot().getProject("NonExisting");
	IJavaScriptProject javaProject = JavaScriptCore.create(nonExistingProject);
	assertCorrespondingResourceFails(javaProject);
}
/*
 * Ensures that one cannot get the corresponding resource of a non-existing type.
 */
public void testCorrespondingResourceNonExistingType() throws CoreException {
	try {
		createJavaProject("P", new String[] {"src"});
		createFile(
			"/P/src/X.js",
			"public class X{\n" +
			"}"
		);
		IType type = getCompilationUnit("/P/src/X.js").getType("NonExisting");
		assertCorrespondingResourceFails(type);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that one cannot get the underlying resource of a non-existing class file.
 */
public void testUnderlyingResourceNonExistingClassFile() throws CoreException {
	try {
		createJavaProject("P", new String[] {"src"}, new String[] {"lib"});
		IJavaScriptElement classFile = getClassFile("/P/lib/X.js");
		assertUnderlyingResourceFails(classFile);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that one cannot get the underlying resource of a non-existing compilation unit.
 */
public void testUnderlyingResourceNonExistingCompilationUnit() throws CoreException {
	try {
		createJavaProject("P", new String[] {"src"});
		IJavaScriptUnit compilationUnit = getCompilationUnit("/P/src/X.js");
		assertUnderlyingResourceFails(compilationUnit);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that one cannot get the underlying resource of a non-existing package fragment.
 */
public void testUnderlyingResourceNonExistingPkgFragment() throws CoreException {
	try {
		createJavaProject("P", new String[] {"src"});
		IPackageFragment pkg = getPackage("/P/src/nonExisting");
		assertUnderlyingResourceFails(pkg);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that one cannot get the underlying resource of a non-existing package fragment root.
 */
public void testUnderlyingResourceNonExistingPkgFragmentRoot() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IFolder folder = createFolder("/P/nonExistingRoot");
		IPackageFragmentRoot root = project.getPackageFragmentRoot(folder);
		assertUnderlyingResourceFails(root);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that one cannot get the underlying resource of a non-existing java project.
 */
public void testUnderlyingResourceNonExistingProject() {
	IProject nonExistingProject = ResourcesPlugin.getWorkspace().getRoot().getProject("NonExisting");
	IJavaScriptProject javaProject = JavaScriptCore.create(nonExistingProject);
	assertUnderlyingResourceFails(javaProject);
}
/*
 * Ensures that one cannot get the underlying resource of a non-existing type.
 */
public void testUnderlyingResourceNonExistingType() throws CoreException {
	try {
		createJavaProject("P", new String[] {"src"});
		createFile(
			"/P/src/X.js",
			"function X(){\n" +
			"}"
		);
		IType type = getCompilationUnit("/P/src/X.js").getType("NonExisting");
		assertUnderlyingResourceFails(type);
	} finally {
		deleteProject("P");
	}
}
}
