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

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.wst.jsdt.core.*;

import junit.framework.Test;
/**
 * Tests IJavaScriptModel API.
 */
public class JavaModelTests extends ModifyingResourceTests {

public static Test suite() {
	return buildModelTestSuite(JavaModelTests.class);
}

// Use this static initializer to specify subset for tests
// All specified tests which do not belong to the class are skipped...
static {
//	TESTS_PREFIX =  "testBug100772_ProjectScope";
//	TESTS_NAMES = new String[] { "testFindLineSeparator04" };
//	TESTS_NUMBERS = new int[] { 100772 };
//	TESTS_RANGE = new int[] { 83304, -1 };
}

public JavaModelTests(String name) {
	super(name);
}
protected int indexOf(String projectName, IJavaScriptProject[] projects) {
	for (int i = 0, length = projects.length; i < length; i++) {
		if (projects[i].getElementName().equals(projectName)) {
			return i;
		}
	}
	return -1;
}
/*
 * Ensure that a java project is not added to the list of known java project
 * when a file is added to a non-java project.
 * (regression test for bug 18698 Seeing non-java projects in package view)
 */
public void testAddFileToNonJavaProject() throws CoreException {
	IJavaScriptModel model = this.getJavaModel();
	IJavaScriptProject[] projects = model.getJavaScriptProjects();
	assertTrue(
		"Project P should not be present already",
		this.indexOf("P", projects) == -1
	);
	try {
		this.createProject("P");
		this.createFile("/P/toto.txt", "");
		projects = model.getJavaScriptProjects();
		assertTrue(
			"Project P should not be present",
			this.indexOf("P", projects) == -1
		);
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Ensiure that no markers are created for a non-Java project
 * (regression test for bug 131937 JDT core adding problem markers to non-java projects)
 */
public void testCreateNonJavaProject() throws CoreException {
	try {
		IProject project = createProject("NonJava");
		waitForAutoBuild();
		IMarker[] markers = project.findMarkers(null/*all type of markers*/, true, IResource.DEPTH_INFINITE);
		assertMarkers("Unexpected markers", "", markers);
	} finally {
		deleteProject("NonJava");
	}
}
/*
 * Ensure that a resource belonging to the Java model is known to be contained in the Java model.
 * Case of non-accessible resources
 */
public void testContains1() throws CoreException {
	try {
		IJavaScriptProject project = this.createJavaProject("P", new String[] {""});

		// .java file
		IFile file = this.getFile("/P/X.js");
		assertTrue("/P/X.java should be in model", getJavaModel().contains(file));

		// .class file
//		file = this.getFile("/P/X.class");
//		assertTrue("/P/X.class should not be in model", !getJavaModel().contains(file));

		// non-Java resource
		file = this.getFile("/P/read.txt");
		assertTrue("/P/read.txt should be in model", getJavaModel().contains(file));

		// package
		IFolder folder = this.getFolder("/P/p");
		assertTrue("/P/p should be in model", getJavaModel().contains(folder));
		
		// resource in closed project
		file = this.createFile("/P/X.js", "");
		project.getProject().close(null);
		assertTrue("/P/X.java should be in model (even if project is closed)", getJavaModel().contains(file));
		
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Ensure that a resource belonging to the Java model is known to be contained in the Java model.
 * Case of projects
 */
public void testContains2() throws CoreException {
	try {
		// Java project
		IProject project = this.createJavaProject("P1", new String[] {""}).getProject();
		assertTrue("/P1 should be in model", getJavaModel().contains(project));

		// non-Java project
		project = this.createProject("P2");
		assertTrue("/P2 should be in model", getJavaModel().contains(project));
	} finally {
		this.deleteProject("P1");
		this.deleteProject("P2");
	}
}
/*
 * Ensure that a resource belonging to the Java model is known to be contained in the Java model.
 * Case of prj=src=bin
 */
public void testContains3() throws CoreException {
	try {
		this.createJavaProject("P", new String[] {""});

		// .java file
		IFile file = this.createFile("/P/X.js", "");
		assertTrue("/P/X.java should be in model", getJavaModel().contains(file));

		// .class file
		file = this.createFile("/P/X.class", "");
		assertTrue("/P/X.class should not be in model", !getJavaModel().contains(file));

		// non-Java resource
		file = this.createFile("/P/read.txt", "");
		assertTrue("/P/read.txt should be in model", getJavaModel().contains(file));

		// package
		IFolder folder = this.createFolder("/P/p");
		assertTrue("/P/p should be in model", getJavaModel().contains(folder));
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Ensure that a resource belonging to the Java model is known to be contained in the Java model.
 * Case of empty classpath.
 */
public void testContains4() throws CoreException {
	try {
		this.createJavaProject("P", new String[] {});

		// .java file
		IFile file = this.createFile("/P/X.js", "");
		assertTrue("/P/X.java should be in model", getJavaModel().contains(file));
		
		// .class file
		file = this.createFile("/P/X.class", "");
		assertTrue("/P/X.class should be in model", getJavaModel().contains(file));

		// non-Java resource file
		file = this.createFile("/P/read.txt", "");
		assertTrue("/P/read.txt should be in model", getJavaModel().contains(file));

		// non-Java resource folder
		IFolder folder = this.createFolder("/P/p");
		assertTrue("/P/p should be in model", getJavaModel().contains(folder));
		
		// bin folder
		folder = this.getFolder("/P/bin");
		assertTrue("/P/bin should not be in model", !getJavaModel().contains(folder));
		
		// classfile in bin folder
		file = this.createFile("/P/bin/X.class", "");
		assertTrue("/P/bin/X.class should not be in model", !getJavaModel().contains(file));
		
		// resource file in bin folder
		this.createFolder("/P/bin/image");
		file = this.createFile("/P/bin/image/ok.gif", "");
		assertTrue("/P/bin/image/ok.gif should not be in model", !getJavaModel().contains(file));
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Ensure that a resource belonging to the Java model is known to be contained in the Java model.
 * Case of src != bin
 */
public void testContains5() throws CoreException {
	try {
		this.createJavaProject("P", new String[] {"src"});

		// .java file
		IFile file = this.createFile("/P/src/X.js", "");
		assertTrue("/P/src/X.java should be in model", getJavaModel().contains(file));

		// resource file in src
		this.createFolder("/P/src/image");
		file = this.createFile("/P/src/image/ok.gif", "");
		assertTrue("/P/src/image/ok.gif should not be in model", getJavaModel().contains(file));
		
		// .class file in bin
		file = this.createFile("/P/bin/X.class", "");
		assertTrue("/P/bin/X.class should not be in model", !getJavaModel().contains(file));

		// resource file in bin
		this.createFolder("/P/bin/image");
		file = this.createFile("/P/bin/image/ok.gif", "");
		assertTrue("/P/bin/image/ok.gif should not be in model", !getJavaModel().contains(file));

		// .class file in src
		file = this.createFile("/P/src/X.class", "");
		assertTrue("/P/src/X.class should not be in model", !getJavaModel().contains(file));

		// non-Java resource
		file = this.createFile("/P/src/read.txt", "");
		assertTrue("/P/src/read.txt should be in model", getJavaModel().contains(file));

		// package
		IFolder folder = this.createFolder("/P/src/p");
		assertTrue("/P/src/p should be in model", getJavaModel().contains(folder));
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Ensure that a resource belonging to the Java model is known to be contained in the Java model.
 * Case of prj==src and separate bin
 */
public void testContains6() throws CoreException {
	try {
		this.createJavaProject("P", new String[] {""});

		// .java file
		IFile file = this.createFile("/P/X.js", "");
		assertTrue("/P/X.java should be in model", getJavaModel().contains(file));

		// resource file in src
		this.createFolder("/P/image");
		file = this.createFile("/P/image/ok.gif", "");
		assertTrue("/P/image/ok.gif should not be in model", getJavaModel().contains(file));
		
		// .class file in bin
		file = this.createFile("/P/bin/X.class", "");
		assertTrue("/P/bin/X.class should not be in model", !getJavaModel().contains(file));

		// resource file in bin
		this.createFolder("/P/bin/image");
		file = this.createFile("/P/bin/image/ok.gif", "");
		assertTrue("/P/bin/image/ok.gif should not be in model", !getJavaModel().contains(file));

		// .class file in src
		file = this.createFile("/P/X.class", "");
		assertTrue("/P/X.class should not be in model", !getJavaModel().contains(file));

		// non-Java resource
		file = this.createFile("/P/read.txt", "");
		assertTrue("/P/read.txt should be in model", getJavaModel().contains(file));

		// package
		IFolder folder = this.createFolder("/P/p");
		assertTrue("/P/p should be in model", getJavaModel().contains(folder));
	} finally {
		this.deleteProject("P");
	}
}

/*
 * Ensure that using JavaScriptCore.create(IResource) for a package that is defined in a different project
 * returns a non-null value
 * (regression test for bug 97487 [call hierarchy] Call Hierarchy Fails in mounted classes with attached src files)
 */
public void testCreatePkgHandleInDifferentProject() throws CoreException {
	try {
		createJavaProject("P1", new String[] {});
		IFolder folder = createFolder("/P1/lib/x/y");
		createJavaProject("P2", new String[] {}, new String[] {"/P1/lib"});
		IJavaScriptElement element = JavaScriptCore.create(folder);
		assertElementEquals(
			"Unexpected element",
			"x.y [in /P1/lib [in P2]]",
			element
		);
	} finally {
		deleteProjects(new String[] {"P1", "P2"});
	}
}

/*
 * Ensures that the right line separator is found for a compilation unit.
 */
public void testFindLineSeparator01() throws CoreException {
	try {
		createJavaProject("P");
		createFile(
			"/P/X.js", 
			"public class X {\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("/P/X.js");
		assertEquals("\n", cu.findRecommendedLineSeparator());
	} finally {
		deleteProject("P");
	}
}

/*
 * Ensures that the right line separator is found for a compilation unit.
 */
public void testFindLineSeparator02() throws CoreException {
	try {
		createJavaProject("P");
		createFile(
			"/P/X.js", 
			"public class X {\r\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("/P/X.js");
		assertEquals("\r\n", cu.findRecommendedLineSeparator());
	} finally {
		deleteProject("P");
	}
}

/*
 * Ensures that the right line separator is found for an empty compilation unit.
 */
public void testFindLineSeparator03() throws CoreException {
	try {
		createJavaProject("P");
		createFile(
			"/P/X.js", 
			""
		);
		IJavaScriptUnit cu = getCompilationUnit("/P/X.js");
		assertEquals(System.getProperty("line.separator"), cu.findRecommendedLineSeparator());
	} finally {
		deleteProject("P");
	}
}

/*
 * Ensures that the right line separator is found for a package fragment
 */
public void testFindLineSeparator04() throws CoreException {
	try {
		createJavaProject("P");
		createFolder("/P/p");
		IPackageFragment pkg = getPackage("/P/p");
		assertEquals(System.getProperty("line.separator"), pkg.findRecommendedLineSeparator());
	} finally {
		deleteProject("P");
	}
}

/**
 * Test that a model has no project.
 */
public void testGetJavaProject() {
	IJavaScriptModel model= getJavaModel();
	assertTrue("project should be null", model.getJavaScriptProject() == null);
}
/*
 * Ensure that a java project that is added appears in the list of known java project,
 * and that it is removed from this list when deleted.
 */
public void testGetJavaProjects1() throws CoreException {
	IJavaScriptModel model = this.getJavaModel();
	IJavaScriptProject[] projects = model.getJavaScriptProjects();
	assertTrue(
		"Project P should not be present already",
		this.indexOf("P", projects) == -1
	);
	try {
		this.createJavaProject("P", new String[] {});
		projects = model.getJavaScriptProjects();
		assertTrue(
			"Project P should be present",
			this.indexOf("P", projects) != -1
		);
		this.deleteProject("P");
		projects = model.getJavaScriptProjects();
		assertTrue(
			"Project P should not be present any longer",
			this.indexOf("P", projects) == -1
		);
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Ensure that a non-java project that is added does not appears in the list of known java project.
 */
public void testGetJavaProjects2() throws CoreException {
	IJavaScriptModel model = this.getJavaModel();
	IJavaScriptProject[] projects = model.getJavaScriptProjects();
	assertTrue(
		"Project P should not be present already",
		this.indexOf("P", projects) == -1
	);
	try {
		this.createProject("P");
		projects = model.getJavaScriptProjects();
		assertTrue(
			"Project P should not be present",
			this.indexOf("P", projects) == -1
		);
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Test retrieving non-Java projects.
 */
public void testGetNonJavaResources() throws CoreException {
	try {
		IJavaScriptModel model = this.getJavaModel();

		this.createJavaProject("JP", new String[]{});
		assertResourceNamesEqual(
			"Unexpected non-Java resources",
			"",
			model.getNonJavaScriptResources());

		this.createProject("SP1");
		assertResourceNamesEqual(
			"Unexpected non-Java resources after creation of SP1",
			"SP1",
			model.getNonJavaScriptResources());
		
		this.createProject("SP2");
		assertResourceNamesEqual(
			"Unexpected non-Java resources after creation of SP2",
			"SP1\n" +
			"SP2",
			model.getNonJavaScriptResources());

		this.deleteProject("SP1");
		assertResourceNamesEqual(
			"Unexpected non-Java resources after deletion of SP1",
			"SP2",
			model.getNonJavaScriptResources());
	} finally {
		this.deleteProject("SP1");
		this.deleteProject("SP2");
		this.deleteProject("JP");
	}
}
/*
 * Ensures that the right scheduling rule is returned for a Java project
 */
public void testGetSchedulingRule1() {
	IJavaScriptProject project = getJavaProject("P");
	assertEquals(
		"Unexpected scheduling rule",
		project.getResource(),
		project.getSchedulingRule());
}
/*
 * Ensures that the right scheduling rule is returned for a source package fragment root
 */
public void testGetSchedulingRule2() {
	IResource folder = getFolder("/P/src");
	IPackageFragmentRoot root = getJavaProject("P").getPackageFragmentRoot(folder);
	assertEquals(
		"Unexpected scheduling rule",
		root.getResource(),
		root.getSchedulingRule());
}
/*
 * Ensures that the right scheduling rule is returned for an external jar package fragment root
 */
public void testGetSchedulingRule3() {
	IPackageFragmentRoot root1 = getJavaProject("P1").getPackageFragmentRoot("c:\\some.jar");
	ISchedulingRule rule1 = root1.getSchedulingRule();
	IPackageFragmentRoot root2 = getJavaProject("P2").getPackageFragmentRoot("c:\\some.jar");
	ISchedulingRule rule2 = root2.getSchedulingRule();
	assertTrue("Rule 1 should contain rule 2", rule1.contains(rule2));
	assertTrue("Rule 1 should conflict with rule 2", rule1.isConflicting(rule2));
	assertTrue("Rule 2 should contain rule 1", rule2.contains(rule1));
	assertTrue("Rule 2 should conflict with rule 1", rule2.isConflicting(rule1));
}
/*
 * Ensures that the right scheduling rule is returned for a source package fragment
 */
public void testGetSchedulingRule4() {
	IResource folder = getFolder("/P/src");
	IPackageFragment pkg = getJavaProject("P").getPackageFragmentRoot(folder).getPackageFragment("p");
	assertEquals(
		"Unexpected scheduling rule",
		pkg.getResource(),
		pkg.getSchedulingRule());
}
/*
 * Ensures that JavaScriptCore#initializeAfterLoad() can be called on startup
 */
public void testInitializeAfterLoad() throws CoreException {
	simulateExitRestart();
	JavaScriptCore.initializeAfterLoad(null);
}


/*
 * Ensures that a registered POST_BUILD pre-processing resource changed listener is correctly called.
 */
public void testPreProcessingResourceChangedListener03() throws CoreException {
	final int[] eventType = new int[] {0};
	IResourceChangeListener listener = new IResourceChangeListener(){
		public void resourceChanged(IResourceChangeEvent event) {
			eventType[0] |= event.getType();
		}
	};
	try {
		JavaScriptCore.addPreProcessingResourceChangedListener(listener, IResourceChangeEvent.POST_BUILD);
		createProject("Test");
		waitForAutoBuild();
		assertEquals("Unexpected event type", IResourceChangeEvent.POST_BUILD, eventType[0]);
	} finally {
		JavaScriptCore.removePreProcessingResourceChangedListener(listener);
		deleteProject("Test");
	}
}

/*
 * Ensures that a registered POST_CHANGE | PRE_BUILD pre-processing resource changed listener is correctly called.
 */
public void testPreProcessingResourceChangedListener04() throws CoreException {
	final int[] eventType = new int[] {0};
	IResourceChangeListener listener = new IResourceChangeListener(){
		public void resourceChanged(IResourceChangeEvent event) {
			eventType[0] |= event.getType();
		}
	};
	try {
		JavaScriptCore.addPreProcessingResourceChangedListener(listener, IResourceChangeEvent.POST_CHANGE | IResourceChangeEvent.POST_BUILD);
		createProject("Test");
		waitForAutoBuild();
		assertEquals("Unexpected event type", IResourceChangeEvent.POST_CHANGE | IResourceChangeEvent.POST_BUILD, eventType[0]);
	} finally {
		JavaScriptCore.removePreProcessingResourceChangedListener(listener);
		deleteProject("Test");
	}
}
}

