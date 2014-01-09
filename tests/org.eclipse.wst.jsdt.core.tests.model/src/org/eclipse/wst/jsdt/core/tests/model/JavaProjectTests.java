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

import java.io.File;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.wst.jsdt.core.IAccessRule;
import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.IIncludePathAttribute;
import org.eclipse.wst.jsdt.core.IIncludePathEntry;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptModelStatusConstants;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.IPackageFragmentRoot;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.internal.core.JavaModelManager;
import org.eclipse.wst.jsdt.internal.core.UserLibrary;
import org.eclipse.wst.jsdt.internal.core.UserLibraryManager;

public class JavaProjectTests extends ModifyingResourceTests {
public JavaProjectTests(String name) {
	super(name);
}
public static Test suite() {
	TestSuite suite = (TestSuite) buildModelTestSuite(JavaProjectTests.class);

	// The following test must be at the end as it deletes a package and this would have side effects
	// on other tests
	if (suite.testCount() > 1) // if not running only 1 test
		suite.addTest(new JavaProjectTests("lastlyTestDeletePackageWithAutobuild"));
	
	return suite;
}
public void setUpSuite() throws Exception {
	super.setUpSuite();
	setUpJavaProject("JavaProjectTests");
	setUpJavaProject("JavaProjectSrcTests");
	setUpJavaProject("JavaProjectLibTests");
}
public void tearDownSuite() throws Exception {
	deleteProject("JavaProjectTests");
	deleteProject("JavaProjectSrcTests");
	deleteProject("JavaProjectLibTests");
	super.tearDownSuite();
}


/**
 * Test adding a non-java resource in a package fragment root that correspond to
 * the project.
 * (Regression test for PR #1G58NB8)
 */
public void testAddNonJavaResourcePackageFragmentRoot() throws JavaScriptModelException, CoreException {
	// get resources of source package fragment root at project level
	IPackageFragmentRoot root = getPackageFragmentRoot("JavaProjectTests", "");
	Object[] resources = root.getNonJavaScriptResources();
	assertResourceNamesEqual(
		"unexpected non Java resources",
		".project\n" + 
		".settings\n" + 
		"lib142530.jar\n" + 
		"lib148949.jar",
		resources);
	IFile resource = (IFile)resources[0];
	IPath newPath = root.getUnderlyingResource().getFullPath().append("TestNonJavaResource.abc");
	try {
		// copy and rename resource
		resource.copy(
			newPath, 
			true, 
			null);
		
		// ensure the new resource is present
		resources = root.getNonJavaScriptResources();
		assertResourcesEqual(
			"incorrect non java resources", 
			"/JavaProjectTests/.project\n" +
			"/JavaProjectTests/.settings\n" +
			"/JavaProjectTests/TestNonJavaResource.abc\n" +
			"/JavaProjectTests/lib142530.jar\n" +
			"/JavaProjectTests/lib148949.jar",
			resources);
	} finally {
		// clean up
		deleteResource(resource.getWorkspace().getRoot().getFile(newPath));
	}
}
/*
 * Ensures that adding a project prerequisite in the classpath updates the referenced projects
 */
public void testAddProjectPrerequisite() throws CoreException {
	try {
		createJavaProject("P1");
		createJavaProject("P2");
		waitForAutoBuild();
		editFile(
			"/P2/.settings/.jsdtscope", 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<classpath>\n" +
			"    <classpathentry kind=\"src\" path=\"/P1\"/>\n" +
			"</classpath>"
		);
		getProject("P2").build(IncrementalProjectBuilder.FULL_BUILD, null);
		IProject[] referencedProjects = getProject("P2").getReferencedProjects();
		assertResourcesEqual(
			"Unexpected project references", 
			"/P1", 
			referencedProjects);
	} finally {
		deleteProjects(new String[] {"P1", "P2"});
	}
}
/**
 * Test that a compilation unit
 * has a corresponding resource.
 */
public void testCompilationUnitCorrespondingResource() throws JavaScriptModelException {
	IJavaScriptUnit element= getCompilationUnit("JavaProjectTests", "", "q", "A.js");
	IResource corr= element.getCorrespondingResource();
	IResource res= getWorkspace().getRoot().getProject("JavaProjectTests").getFolder("q").getFile("A.js");
	assertTrue("incorrect corresponding resource", corr.equals(res));
	assertEquals("Project is incorrect for the compilation unit", "JavaProjectTests", corr.getProject().getName());
}
/**
 * Tests the fix for "1FWNMKD: ITPJCORE:ALL - Package Fragment Removal not reported correctly"
 */
public void lastlyTestDeletePackageWithAutobuild() throws CoreException {
	// close all project except JavaProjectTests so as to avoid side effects while autobuilding
	IProject[] projects = getWorkspaceRoot().getProjects();
	for (int i = 0; i < projects.length; i++) {
		IProject project = projects[i];
		if (project.getName().equals("JavaProjectTests")) continue;
		project.close(null);
	}

	// turn autobuilding on
	IWorkspace workspace = getWorkspace();
	boolean autoBuild = workspace.isAutoBuilding();
	IWorkspaceDescription description = workspace.getDescription();
	description.setAutoBuilding(true);
	workspace.setDescription(description);

	startDeltas();
	IPackageFragment frag = getPackageFragment("JavaProjectTests", "", "x.y");
	IFolder folder = (IFolder) frag.getUnderlyingResource();
	try {
		deleteResource(folder);
		assertDeltas(
			"Unexpected delta",
			"JavaProjectTests[*]: {CHILDREN}\n" + 
			"	<project root>[*]: {CHILDREN}\n" + 
			"		x.y[-]: {}"
		);
	} finally {
		stopDeltas();
		
		// turn autobuild off
		description.setAutoBuilding(autoBuild);
		workspace.setDescription(description);

		// reopen projects
		projects = getWorkspaceRoot().getProjects();
		for (int i = 0; i < projects.length; i++) {
			IProject project = projects[i];
			if (project.getName().equals("JavaProjectTests")) continue;
			project.open(null);
		}
	}
}
/**
 * Test that an (external) jar
 * has no corresponding resource.
 */
public void testExternalArchiveCorrespondingResource() throws JavaScriptModelException {
	IJavaScriptProject project= getJavaProject("JavaProjectTests");
	IPackageFragmentRoot element= project.getPackageFragmentRoot(getSystemJsPathString());
	IResource corr= element.getCorrespondingResource();
	assertTrue("incorrect corresponding resource", corr == null);
}
/*
 * Ensures that a file with an extra Java-like extension is listed in the children of a package.
 */
public void testExtraJavaLikeExtension1() throws CoreException {
	try {
		createJavaProject("P");
		createFolder("/P/pack");
		createFile("/P/pack/X.js", "function X() {}");
		createFile("/P/pack/Y.bar", "function Y() {}");
		IPackageFragment pkg = getPackage("/P/pack");
		assertSortedElementsEqual(
			"Unexpected children of package pack", 
			"X.js [in pack [in <project root> [in P]]]",
			pkg.getChildren());
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a file with an extra Java-like extension is not listed in the non-Java resources of a package.
 */
public void testExtraJavaLikeExtension2() throws CoreException {
	try {
		createJavaProject("P");
		createFolder("/P/pack");
		createFile("/P/pack/X.txt", "");
		createFile("/P/pack/Y.bar", "function Y() {}");
		IPackageFragment pkg = getPackage("/P/pack");
		assertResourceNamesEqual(
			"Unexpected non-Java resources of package pack", 
			"X.txt\n" +
			"Y.bar",
			pkg.getNonJavaScriptResources());
	} finally {
		deleteProject("P");
	}
}
/**
 * Test that a compilation unit can be found
 */
public void testFindElementCompilationUnit() throws JavaScriptModelException {
	IJavaScriptProject project= getJavaProject("JavaProjectTests");
	IJavaScriptElement element= project.findElement(new Path("x/y/Main.js"));
	assertTrue("CU not found" , element != null && element.getElementType() == IJavaScriptElement.JAVASCRIPT_UNIT
		&& element.getElementName().equals("Main.js"));
}
/**
 * Test that a compilation unit can be found in a default package
 */
public void testFindElementCompilationUnitDefaultPackage() throws JavaScriptModelException {
	IJavaScriptProject project= getJavaProject("JavaProjectTests");
	IJavaScriptElement element= project.findElement(new Path("B.js"));
	assertTrue("CU not found" , element != null && element.getElementType() == IJavaScriptElement.JAVASCRIPT_UNIT
		&& element.getElementName().equals("B.js"));
}
/**
 * Test that an invlaid path throws an exception
 */
public void testFindElementInvalidPath() throws JavaScriptModelException {
	IJavaScriptProject project= getJavaProject("JavaProjectTests");
	boolean failed= false;
	try {
		project.findElement(null);
	} catch (JavaScriptModelException e) {
		failed= true;
		assertTrue("wrong status code" , e.getStatus().getCode() == IJavaScriptModelStatusConstants.INVALID_PATH);
	}
	assertTrue("Shold have failed", failed);
	
	failed = false;
	try {
		project.findElement(new Path("/something/absolute"));
	} catch (JavaScriptModelException e) {
		failed= true;
		assertTrue("wrong status code" , e.getStatus().getCode() == IJavaScriptModelStatusConstants.INVALID_PATH);
	}
	assertTrue("Shold have failed", failed);

	IJavaScriptElement element= project.findElement(new Path("does/not/exist/HelloWorld.js"));
	assertTrue("should get no element", element == null);
}
/**
 * Test that a package can be found
 */
public void testFindElementPackage() throws JavaScriptModelException {
	IJavaScriptProject project= getJavaProject("JavaProjectTests");
	IJavaScriptElement element= project.findElement(new Path("x/y"));
	assertTrue("package not found" , element != null && element.getElementType() == IJavaScriptElement.PACKAGE_FRAGMENT
		&& element.getElementName().equals("x.y"));
}
/**
 * Test that a class can be found even if the project prereq a simple project
 * (regression test for bug 28434 Open Type broken when workspace has build path problems)
 */
public void testFindElementPrereqSimpleProject() throws CoreException {
	try {
		this.createProject("R");
		IJavaScriptProject project = this.createJavaProject("J", new String[] {"src"}, new String[] {}, new String[] {"/R"});
		this.createFile(
			"J/src/X.js",
			"public class X {\n" +
			"}"
		);
		assertTrue("X.java not found", project.findElement(new Path("X.js")) != null);
	} finally {
		this.deleteProject("R");
		this.deleteProject("J");
	}
}
/**
 * Test that a package fragment root can be found from a classpath entry.
 */
public void testFindPackageFragmentRootFromClasspathEntry() {
	IJavaScriptProject project = getJavaProject("JavaProjectTests");
	
	// existing classpath entry
	IIncludePathEntry entry = JavaScriptCore.newLibraryEntry(new Path("/JavaProjectTests/lib.jar"), null, null);
	IPackageFragmentRoot[] roots = project.findPackageFragmentRoots(entry);
	assertEquals("Unexpected number of roots for existing entry", 1, roots.length);
	assertEquals("Unexpected root", "/JavaProjectTests/lib.jar", roots[0].getPath().toString());
	
	// non-existing classpath entry
	entry = JavaScriptCore.newSourceEntry(new Path("/JavaProjectTests/nonExisting"));
	roots = project.findPackageFragmentRoots(entry);
	assertEquals("Unexpected number of roots for non existing entry", 0, roots.length);

}
/**
 * Test that a folder with a dot name does not relate to a package fragment
 */
public void testFolderWithDotName() throws JavaScriptModelException, CoreException {
	IPackageFragmentRoot root= getPackageFragmentRoot("JavaProjectTests", "");
	IContainer folder= (IContainer)root.getCorrespondingResource();
	try {
		startDeltas();
		folder.getFolder(new Path("org.eclipse")).create(false, true, null);
		assertTrue("should be one Java Delta", this.deltaListener.deltas.length == 1);
		
		stopDeltas();
		IJavaScriptElement[] children = root.getChildren();
		IPackageFragment bogus = root.getPackageFragment("org.eclipse");
		for (int i = 0; i < children.length; i++) {
			assertTrue("org.eclipse should not be present as child", !children[i].equals(bogus));
		}
		assertTrue("org.eclipse should not exist", !bogus.exists());
	} finally {
		deleteResource(folder.getFolder(new Path("org.eclipse")));
	}	
}
/*
 * Ensures that getting the classpath on a closed project throws a JavaScriptModelException
 * (regression test for bug 25358 Creating a new Java class - Browse for parent)
 */ 
public void testGetClasspathOnClosedProject() throws CoreException {
	IProject project = getProject("JavaProjectTests");
	try {
		project.close(null);
		boolean gotException = false;
		IJavaScriptProject javaProject = JavaScriptCore.create(project);
		try {
			javaProject.getRawIncludepath();
		} catch (JavaScriptModelException e) {
			if (e.isDoesNotExist()) {
				gotException = true;
			}
		}
		assertTrue("Should get a not present exception for getRawClasspath()", gotException);
		gotException = false;
		try {
			javaProject.getResolvedIncludepath(true);
		} catch (JavaScriptModelException e) {
			if (e.isDoesNotExist()) {
				gotException = true;
			}
		}
		assertTrue("Should get a not present exception for getResolvedClasspath(true)", gotException);
	} finally {
		project.open(null);
	}
}
/*
 * Ensures that the non-java resources for a project do not contain the project output location. 
 */
public void testGetNonJavaResources1() throws CoreException {
	try {
		IJavaScriptProject project = this.createJavaProject("P", new String[] {"src"});
		assertResourcesEqual(
			"Unexpected non-java resources for project",
			"/P/.project\n" +
			"/P/.settings",
			project.getNonJavaScriptResources());
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Ensures that the non-java resources for a project do not contain a custom output location. 
 * (regression test for 27494  Source folder output folder shown in Package explorer)
 */
public void testGetNonJavaResources2() throws CoreException {
	try {
		IJavaScriptProject project = this.createJavaProject("P", new String[] {"src"}, "bin1", new String[] {"bin2"});
		assertResourcesEqual(
			"Unexpected non-java resources for project",
			"/P/.project\n" +
			"/P/.settings",
			project.getNonJavaScriptResources());
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Ensures that the non-java resources for a project do not contain a folder that should be a package fragment.
 */
public void testGetNonJavaResources3() throws CoreException {
	try {
		IJavaScriptProject project = this.createJavaProject("P", new String[] {""});
		this.createFolder("/P/p1");
		assertResourcesEqual(
			"Unexpected non-java resources for project",
			"/P/.project\n" +
			"/P/.settings",
			project.getNonJavaScriptResources());
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Ensures that the non-java resources for a project contain a folder that have an invalid name for a package fragment.
 * (regression test for bug 31757 Folder with invalid pkg name should be non-Java resource)
 */
public void testGetNonJavaResources4() throws CoreException {
	try {
		IJavaScriptProject project = this.createJavaProject("P");
		this.createFolder("/P/x.y");
		assertResourcesEqual(
			"Unexpected non-java resources for project",
			"/P/.project\n" + 
			"/P/.settings\n" + 
			"/P/x.y",
			project.getNonJavaScriptResources());
	} finally {
		this.deleteProject("P");
	}
} 
/*
 * Ensures that getRequiredProjectNames() returns the project names in the classpath order
 * (regression test for bug 25605 [API] someJavaProject.getRequiredProjectNames(); API should specify that the array is returned in ClassPath order)
 */
public void testGetRequiredProjectNames() throws CoreException {
	try {
		IJavaScriptProject project = this.createJavaProject(
			"P", 
			new String[] {}, 
			new String[] {}, 
			new String[] {"/JavaProjectTests", "/P1", "/P0", "/P2", "/JavaProjectSrcTests"});
		String[] requiredProjectNames = project.getRequiredProjectNames();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0, length = requiredProjectNames.length; i < length; i++) {
			buffer.append(requiredProjectNames[i]);
			if (i != length-1) {
				buffer.append(", ");
			}
		}
		assertEquals(
			"Unexpected required project names",
			"JavaProjectTests, P1, P0, P2, JavaProjectSrcTests",
			buffer.toString());
	} finally {
		this.deleteProject("P");
	}
}
/**
 * Test that an (internal) jar
 * has a corresponding resource.
 */
public void testInternalArchiveCorrespondingResource() throws JavaScriptModelException {
	IPackageFragmentRoot element= getPackageFragmentRoot("JavaProjectTests", "lib.jar");
	IResource corr= element.getCorrespondingResource();
	IResource res= getWorkspace().getRoot().getProject("JavaProjectTests").getFile("lib.jar");
	assertTrue("incorrect corresponding resource", corr.equals(res));
}
/**
 * Test IJavaPackageFragment.isDefaultPackage().
 */
public void testIsDefaultPackage() throws JavaScriptModelException {
	IPackageFragment def = getPackageFragment("JavaProjectTests", "", "");
	assertTrue("should be default package", def.isDefaultPackage());
	IPackageFragment y =
		getPackageFragment("JavaProjectTests", "", "x.y");
	assertTrue("x.y should not be default pakackage", !y.isDefaultPackage());

	IPackageFragment def2 = getPackageFragment("JavaProjectTests", "lib.jar", "");
	assertTrue("lib.jar should have default package", def2.isDefaultPackage());
	IPackageFragment p =
		getPackageFragment("JavaProjectTests", "lib.jar", "p");
	assertTrue("p should not be default package", !p.isDefaultPackage());
}
/**
 * Test that a package fragment (non-external, non-jar, non-default)
 * has a corresponding resource.
 */
public void testPackageFragmentCorrespondingResource() throws JavaScriptModelException {
	IPackageFragment element= getPackageFragment("JavaProjectTests", "", "x.y");
	IResource corr= element.getCorrespondingResource();
	IResource res= getWorkspace().getRoot().getProject("JavaProjectTests").getFolder("x").getFolder("y");
	assertTrue("incorrect corresponding resource", corr.equals(res));
}
/**
 * Test that a package fragment (non-external, non-jar, non-default)
 * has a corresponding resource.
 */
public void testPackageFragmentHasSubpackages() throws JavaScriptModelException {
	IPackageFragment def=		getPackageFragment("JavaProjectTests", "", "");
	IPackageFragment x=		getPackageFragment("JavaProjectTests", "", "x");
	IPackageFragment y=		getPackageFragment("JavaProjectTests", "", "x.y");
	assertTrue("default should have subpackages",							def.hasSubpackages());
	assertTrue("x should have subpackages",								x.hasSubpackages());
	assertTrue("x.y should NOT have subpackages",		!y.hasSubpackages());

	IPackageFragment java = getPackageFragment("JavaProjectTests", getSystemJsPathString(), "java");
	IPackageFragment lang= getPackageFragment("JavaProjectTests", getSystemJsPathString(), "java.lang");

	assertTrue("java should have subpackages",					java.hasSubpackages());
	assertTrue("java.lang  should NOT have subpackages",			!lang.hasSubpackages());
}
/*
 * Ensures that the structure is known for a package fragment on the classpath.
 */
public void testPackageFragmentIsStructureKnown1() throws CoreException {
	IPackageFragment pkg = getPackageFragment("JavaProjectTests", "", "x");
	assertTrue("Structure of package 'x' should be known", pkg.isStructureKnown());
}
/*
 * Ensures that asking if the structure is known for a package fragment outside the classpath throws a JavaScriptModelException.
 * (regression test for bug 138577 Package content disapear in package explorer)
 */
public void testPackageFragmentIsStructureKnown2() throws CoreException {
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
		JavaScriptModelException exception = null;
		try {
			pkg.isStructureKnown();
		} catch (JavaScriptModelException e) {
			exception = e;
		}
		assertExceptionEquals(
			"Unexpected exception", 
			"pack [in <project root> [in P]] does not exist",
			exception);
	} finally {
		deleteProject("P");
	}
}

/*
 * Ensure that the non-Java resources of a source package are correct.
 */
public void testPackageFragmentNonJavaResources1() throws JavaScriptModelException {
	// regular source package with resources
	IPackageFragment pkg = getPackageFragment("JavaProjectTests", "", "x");
	Object[] resources = pkg.getNonJavaScriptResources();
	assertResourcesEqual(
		"Unexpected resources", 
		"/JavaProjectTests/x/readme.txt\n" + 
		"/JavaProjectTests/x/readme2.txt",
		resources);
}

/*
 * Ensure that the non-Java resources of a source package without resources are correct.
 */
public void testPackageFragmentNonJavaResources2() throws JavaScriptModelException {	
	IPackageFragment pkg = getPackageFragment("JavaProjectTests", "", "x.y");
	Object[] resources = pkg.getNonJavaScriptResources();
	assertResourcesEqual(
		"Unexpected resources", 
		"",
		resources);
}

/*
 * Ensure that the non-Java resources of the default package are correct.
 */
public void testPackageFragmentNonJavaResources3() throws JavaScriptModelException {	
	IPackageFragment pkg = getPackageFragment("JavaProjectTests", "", "");
	Object[] resources = pkg.getNonJavaScriptResources();
	assertResourcesEqual(
		"Unexpected resources", 
		"",
		resources);
}

/*
 * Ensure that the non-Java resources of a zip package without resources are correct.
 */
public void testPackageFragmentNonJavaResources4() throws JavaScriptModelException {	
	IPackageFragment pkg = getPackageFragment("JavaProjectTests", "lib.jar", "p");
	Object[] resources = pkg.getNonJavaScriptResources();
	assertResourcesEqual(
		"Unexpected resources", 
		"",
		resources);
}

// TODO: zip default package with potentialy resources

/*
 * Ensure that the non-Java resources of a zip default package without resources are correct.
 */
public void testPackageFragmentNonJavaResources5() throws JavaScriptModelException {	
	IPackageFragment pkg = getPackageFragment("JavaProjectTests", "lib.jar", "");
	Object[] resources = pkg.getNonJavaScriptResources();
	assertResourcesEqual(
		"Unexpected resources", 
		"",
		resources);	
}

/*
 * Ensure that the non-Java resources of a zip package with resources are correct.
 * (regression test for bug 142530 [hierarchical packages] '.' in folder names confuses package explorer)
 */
public void testPackageFragmentNonJavaResources6() throws JavaScriptModelException {	
	// regular zip package without resources
	IPackageFragment pkg = getPackageFragment("JavaProjectTests", "lib142530.jar", "p");
	Object[] resources = pkg.getNonJavaScriptResources();
	assertResourcesEqual(
		"Unexpected resources", 
		"x.y/Test.txt",
		resources);
}

/*
 * Ensure that the non-Java resources of a zip package with resources are correct.
 * (regression test for bug 148949 JarEntryFile now returning 'null')
 */
public void testPackageFragmentNonJavaResources7() throws JavaScriptModelException {	
	// regular zip package without resources
	IPackageFragment pkg = getPackageFragment("JavaProjectTests", "lib148949.jar", "p");
	Object[] resources = pkg.getNonJavaScriptResources();
	assertResourceNamesEqual(
		"Unexpected resources", 
		"test.txt",
		resources);
}

/*
 * Ensures that the package-info.class file doesn't appear as a child of a package if proj=src
 * (regression test for bug 99654 [5.0] JavaModel returns both IClassFile and IJavaScriptUnit for package-info.java)
 */
public void testPackageFragmentPackageInfoClass() throws CoreException {
	try {
		createJavaProject("P");
		createFolder("/P/p1");
		IPackageFragment pkg = getPackage("/P/p1");
		pkg.open(null);
		createFile("/P/p1/package-info.class", "");
		assertResourceNamesEqual(
			"Unexpected resources of /P/p1",
			"",
			pkg.getNonJavaScriptResources());
	} finally {
		deleteProject("P");
	}
}
/**
 * Tests that after a package "foo" has been renamed into "bar", it is possible to recreate
 * a "foo" package.
 * @see "1FWX0HY: ITPCORE:WIN98 - Problem after renaming a Java package"
 */
public void testPackageFragmentRenameAndCreate() throws JavaScriptModelException, CoreException {
	IPackageFragment y = getPackageFragment("JavaProjectTests", "", "x.y");
	IFolder yFolder = (IFolder) y.getUnderlyingResource();
	IPath yPath = yFolder.getFullPath();
	IPath fooPath = yPath.removeLastSegments(1).append("foo");
	
	yFolder.move(fooPath, true, null);
	try {
		yFolder.create(true, true, null);
	} catch (Throwable e) {
		e.printStackTrace();
		assertTrue("should be able to recreate the y folder", false);
	}
	// restore the original state
	deleteResource(yFolder);
	IPackageFragment foo = getPackageFragment("JavaProjectTests", "", "x.foo");
	IFolder fooFolder = (IFolder) foo.getUnderlyingResource();
	fooFolder.move(yPath, true, null);
}
/**
 * Test that a package fragment root (non-external, non-jar, non-default root)
 * has a corresponding resource.
 */
public void testPackageFragmentRootCorrespondingResource() throws JavaScriptModelException {
	IPackageFragmentRoot element= getPackageFragmentRoot("JavaProjectTests", "");
	IResource corr= element.getCorrespondingResource();
	IResource res= getWorkspace().getRoot().getProject("JavaProjectTests");
	assertTrue("incorrect corresponding resource", corr.equals(res));
	assertEquals("Project incorrect for folder resource", "JavaProjectTests", corr.getProject().getName());
}
/**
 * Test getting the non-java resources from a package fragment root.
 */
public void testPackageFragmentRootNonJavaResources() throws JavaScriptModelException {
	// source package fragment root with resources
	IPackageFragmentRoot root = getPackageFragmentRoot("JavaProjectTests", "");
	Object[] resources = root.getNonJavaScriptResources();
	assertResourceNamesEqual(
		"unexpected non java resoures (test case 1)", 
		".project\n" + 
		".settings\n" + 
		"lib142530.jar\n" +
		"lib148949.jar",
		resources);

	// source package fragment root without resources
 	root = getPackageFragmentRoot("JavaProjectSrcTests", "src");
	resources = root.getNonJavaScriptResources();
	assertResourceNamesEqual(
		"unexpected non java resoures (test case 2)", 
		"",
		resources);

	// zip package fragment root with resources
	// TO DO
	
	// zip package fragment root without resources
	root = getPackageFragmentRoot("JavaProjectTests", "lib.jar");
	resources = root.getNonJavaScriptResources();
	assertResourceNamesEqual(
		"unexpected non java resoures (test case 4)", 
		"MANIFEST.MF",
		resources);
}
/**
 * Test raw entry inference performance for package fragment root
 */
public void testPackageFragmentRootRawEntry() throws CoreException, IOException {
	File libDir = null;
	try {
		String libPath = getExternalPath() + "lib";
		JavaScriptCore.setIncludepathVariable("MyVar", new Path(libPath), null);
		IJavaScriptProject proj =  this.createJavaProject("P", new String[] {});
		libDir = new File(libPath);
		libDir.mkdirs();
		final int length = 200;
		IIncludePathEntry[] classpath = new IIncludePathEntry[length];
		for (int i = 0; i < length; i++){
			File libJar = new File(libDir, "lib"+i+".jar");
			libJar.createNewFile();
			classpath[i] = JavaScriptCore.newVariableEntry(new Path("/MyVar/lib"+i+".jar"), null, null);
		}
		proj.setRawIncludepath(classpath, null);
		
		IPackageFragmentRoot[] roots = proj.getPackageFragmentRoots();
		assertEquals("wrong number of entries:", length, roots.length);
		//long start = System.currentTimeMillis();
		for (int i = 0; i < roots.length; i++){
			IIncludePathEntry rawEntry = roots[i].getRawIncludepathEntry();
			assertEquals("unexpected root raw entry:", classpath[i], rawEntry);
		}
		//System.out.println((System.currentTimeMillis() - start)+ "ms for "+roots.length+" roots");
	} finally {
		if (libDir != null) {
			org.eclipse.wst.jsdt.core.tests.util.Util.delete(libDir);
		}
		this.deleteProject("P");
		JavaScriptCore.removeIncludepathVariable("MyVar", null);
	}
}
/**
 * Test raw entry inference performance for package fragment root in case
 * original classpath had duplicate entries pointing to it: first raw entry should be found
 */
public void testPackageFragmentRootRawEntryWhenDuplicate() throws CoreException, IOException {
	File libDir = null;
	try {
		String externalPath = getExternalPath();
		String libPath = externalPath + "lib";
		JavaScriptCore.setIncludepathVariable("MyVar", new Path(externalPath), null);
		IJavaScriptProject proj =  this.createJavaProject("P", new String[] {});
		libDir = new File(libPath);
		libDir.mkdirs();
		IIncludePathEntry[] classpath = new IIncludePathEntry[2];
		File libJar = new File(libDir, "lib.jar");
		libJar.createNewFile();
		classpath[0] = JavaScriptCore.newLibraryEntry(new Path(libPath).append("lib.jar"), null, null);
		classpath[1] = JavaScriptCore.newVariableEntry(new Path("/MyVar").append("lib.jar"), null, null);
		proj.setRawIncludepath(classpath, null);
		JavaScriptCore.setIncludepathVariable("MyVar", new Path(libPath), null); // change CP var value to cause collision
		
		IPackageFragmentRoot[] roots = proj.getPackageFragmentRoots();
		assertEquals("wrong number of entries:", 1, roots.length);
		IIncludePathEntry rawEntry = roots[0].getRawIncludepathEntry();
		assertEquals("unexpected root raw entry:", classpath[0], rawEntry); // ensure first entry is associated to the root
	} finally {
		if (libDir != null) {
			org.eclipse.wst.jsdt.core.tests.util.Util.delete(libDir);
		}
		this.deleteProject("P");
		JavaScriptCore.removeIncludepathVariable("MyVar", null);
	}
}
/*
 * Ensures that opening a project update the project references
 * (regression test for bug 73253 [model] Project references not set on project open)
 */
public void testProjectOpen() throws CoreException {
	try {
		createJavaProject("P1");
		createJavaProject("P2", new String[0], new String[0], new String[] {"/P1"});
		IProject p2 = getProject("P2");
		p2.close(null);
		p2.open(null);
		IProject[] references = p2.getDescription().getDynamicReferences();
		assertResourcesEqual(
			"Unexpected referenced projects",
			"/P1",
			references);
	} finally {
		deleteProjects(new String[] {"P1", "P2"});
	}
}
/**
 * Tests that closing and opening a project triggers the correct deltas.
 */
public void testProjectClose() throws JavaScriptModelException, CoreException {
	IJavaScriptProject jproject= getJavaProject("JavaProjectTests");
	IPackageFragmentRoot[] originalRoots = jproject.getPackageFragmentRoots();
	IProject project= jproject.getProject();

	try {
		startDeltas();
		project.close(null);
		assertDeltas(
			"Unexpected delta 1",
			"JavaProjectTests[*]: {CLOSED}\n" + 
			"ResourceDelta(/JavaProjectTests)"
		);
	} finally {
		try {
			clearDeltas();
			
			project.open(null);
			assertDeltas(
				"Unexpected delta 2",
				"JavaProjectTests[*]: {OPENED}\n" + 
				"ResourceDelta(/JavaProjectTests)"
			);

			IPackageFragmentRoot[] openRoots = jproject.getPackageFragmentRoots();
			assertTrue("should have same number of roots", openRoots.length == originalRoots.length);
			for (int i = 0; i < openRoots.length; i++) {
				assertTrue("root not the same", openRoots[i].equals(originalRoots[i]));
			}
		} finally {
			stopDeltas();
		}
	}
}
/**
 * Test that a project has a corresponding resource.
 */
public void testProjectCorrespondingResource() throws JavaScriptModelException {
	IJavaScriptProject project= getJavaProject("JavaProjectTests");
	IResource corr= project.getCorrespondingResource();
	IResource res= getWorkspace().getRoot().getProject("JavaProjectTests");
	assertTrue("incorrect corresponding resource", corr.equals(res));
}
/**
 * Test that the correct children exist in a project
 */
public void testProjectGetChildren() throws JavaScriptModelException {
	IJavaScriptProject project = getJavaProject("JavaProjectTests");
	IJavaScriptElement[] roots= project.getChildren();
	assertElementsEqual(
		"Unexpected package fragment roots",
		"<project root> [in JavaProjectTests]\n" + 
		getSystemJsPathString() + "\n" + 
		"lib.jar [in JavaProjectTests]\n" + 
		"lib142530.jar [in JavaProjectTests]\n" + 
		"lib148949.jar [in JavaProjectTests]",
		roots);
}
/**
 * Test that the correct package fragments exist in the project.
 */
public void testProjectGetPackageFragments() throws JavaScriptModelException {
	IJavaScriptProject project= getJavaProject("JavaProjectTests");
	IPackageFragment[] fragments= project.getPackageFragments();
	assertSortedElementsEqual(
		"unexpected package fragments",
		"<default> [in "+ getSystemJsPathString() + "]\n" + 
		"q [in <project root> [in JavaProjectTests]]\n" + 
		"x [in <project root> [in JavaProjectTests]]\n" + 
		"x/y [in <project root> [in JavaProjectTests]]",
		fragments);
}
/*
 * Ensures that importing a project correctly update the project references
 * (regression test for bug 121569 [Import/Export] Importing projects in workspace, the default build order is alphabetical instead of by dependency)
 */
public void testProjectImport() throws CoreException {
	try {
		createJavaProject("P1");
		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				createJavaProject("P2");
				editFile(
					"/P2/.settings/.jsdtscope", 
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
					"<classpath>\n" +
					"    <classpathentry kind=\"src\" path=\"/P1\"/>\n" +
					"    <classpathentry kind=\"output\" path=\"\"/>\n" +
					"</classpath>"
				);
			}
		};
		getWorkspace().run(runnable, null);
		waitForAutoBuild();
		IProject[] referencedProjects = getProject("P2").getReferencedProjects();
		assertResourcesEqual(
			"Unexpected project references", 
			"/P1", 
			referencedProjects);
	} finally {
		deleteProjects(new String[] {"P1", "P2"});
	}
}
/**
 * Test that the correct package fragments exist in the project.
 */
public void testRootGetPackageFragments() throws JavaScriptModelException {
	IPackageFragmentRoot root= getPackageFragmentRoot("JavaProjectTests", "");
	IJavaScriptElement[] fragments= root.getChildren();
	assertElementsEqual(
		"unexpected package fragments in source folder",
		"<default> [in <project root> [in JavaProjectTests]]\n" + 
		"q [in <project root> [in JavaProjectTests]]\n" + 
		"x [in <project root> [in JavaProjectTests]]\n" + 
		"x/y [in <project root> [in JavaProjectTests]]",
		fragments);

	root= getPackageFragmentRoot("JavaProjectTests", "lib.jar");
	fragments= root.getChildren();	
	assertSortedElementsEqual(
		"unexpected package fragments in library",
		"<default> [in lib.jar [in JavaProjectTests]]\n" + 
		"p [in lib.jar [in JavaProjectTests]]",
		fragments);
}
/**
 * Test that the correct package fragments exist in the project.
 * (regression test for bug 65693 Package Explorer shows .class files instead of .java)
 */
public void testRootGetPackageFragments3() throws CoreException {
	try {
		IJavaScriptProject p1 = createJavaProject("P1");
		createFile(
			"/P1/X.js",
			"function X() {\n" +
			"}"
		);
		getProject("P1").build(IncrementalProjectBuilder.FULL_BUILD, null);
		IJavaScriptProject p2 = createJavaProject("P2");
		editFile(
			"/P2/.settings/.jsdtscope", 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<classpath>\n" +
			"    <classpathentry kind=\"src\" path=\"\"/>\n" +
			"    <classpathentry kind=\"lib\" path=\"/P1\"/>\n" +
			"</classpath>"
		);
		IPackageFragment pkg = p1.getPackageFragmentRoot(p1.getProject()).getPackageFragment("");
		assertElementsEqual(
			"Unexpected packages for P1",
			"X.js [in <default> [in <project root> [in P1]]]",
			pkg.getChildren());
		pkg = p2.getPackageFragmentRoot(p1.getProject()).getPackageFragment("");
		assertElementsEqual(
			"Unexpected packages for P2",
			"X.js [in <default> [in /P1 [in P2]]]",
			pkg.getChildren());	
	} finally {
		deleteProject("P1");
		deleteProject("P2");
	}
}
/**
 * Ensure a source folder can have a name ending with ".jar"
 */
public void testSourceFolderWithJarName() throws CoreException {
	try {
		this.createJavaProject("P", new String[] {"src.jar"});
		IFile file = createFile("/P/src.jar/X.js", "class X {}");
		IJavaScriptUnit unit = (IJavaScriptUnit)JavaScriptCore.create(file);
		unit.getAllTypes(); // force to open
	} catch (CoreException e) {
		assertTrue("unable to open unit in 'src.jar' source folder", false);
	} finally {
		this.deleteProject("P");
	}
}/**
 * Test that a method
 * has no corresponding resource.
 */
public void testSourceMethodCorrespondingResource() throws JavaScriptModelException {
	IJavaScriptUnit element= getCompilationUnit("JavaProjectTests", "", "q", "A.js");
	IFunction[] methods = element.getFunctions();
	assertTrue("missing methods", methods.length > 0);
	IResource corr= methods[0].getCorrespondingResource();
	assertTrue("incorrect corresponding resource", corr == null);
}

/**
 * Test User Library preference. External jar file referenced in library entry does not exist.
 * It does not need to as we only test the preference value...
 * 
 * @test bug 88719: UserLibrary.serialize /createFromString need support for access restriction / attributes
 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=88719"
 */
public void testUserLibrary() throws JavaScriptModelException {

	IIncludePathEntry[] userEntries = new IIncludePathEntry[2];

	// Set first classpath entry
	IPath path = new Path("/tmp/test.jar");
	IAccessRule[] pathRules = new IAccessRule[3];
	pathRules[0] = JavaScriptCore.newAccessRule(new Path("**/forbidden/**"), IAccessRule.K_NON_ACCESSIBLE);
	pathRules[1] = JavaScriptCore.newAccessRule(new Path("**/discouraged/**"), IAccessRule.K_DISCOURAGED);
	pathRules[2] = JavaScriptCore.newAccessRule(new Path("**/accessible/**"), IAccessRule.K_ACCESSIBLE);
	IIncludePathAttribute[] extraAttributes = new IIncludePathAttribute[2];
	extraAttributes[0] = JavaScriptCore.newIncludepathAttribute("javadoc_location", "http://www.sample-url.org/doc/");
	extraAttributes[1] = JavaScriptCore.newIncludepathAttribute("org.eclipse.wst.jsdt.launching.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY", "/tmp");
	userEntries[0] = JavaScriptCore.newLibraryEntry(path, null, null, pathRules, extraAttributes, false);

	// Set second classpath entry
	path = new Path("/tmp/test.jar");
	pathRules = new IAccessRule[3];
	pathRules[0] = JavaScriptCore.newAccessRule(new Path("/org/eclipse/forbidden/**"), IAccessRule.K_NON_ACCESSIBLE);
	pathRules[1] = JavaScriptCore.newAccessRule(new Path("/org/eclipse/discouraged/**"), IAccessRule.K_DISCOURAGED);
	pathRules[2] = JavaScriptCore.newAccessRule(new Path("/org/eclipse/accessible/**"), IAccessRule.K_ACCESSIBLE);
	extraAttributes = new IIncludePathAttribute[2];
	extraAttributes[0] = JavaScriptCore.newIncludepathAttribute("javadoc_location", "http://www.sample-url.org/doc/");
	extraAttributes[1] = JavaScriptCore.newIncludepathAttribute("org.eclipse.wst.jsdt.launching.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY", "/tmp");
	userEntries[1] = JavaScriptCore.newLibraryEntry(path, null, null, pathRules, extraAttributes, false);
	
	// Create user library
	UserLibrary library = new UserLibrary(userEntries, false);
	UserLibraryManager.setUserLibrary("TEST", library, null);
	
	// Verify it has been written in preferences
	IEclipsePreferences instancePreferences = JavaModelManager.getJavaModelManager().getInstancePreferences();
	String containerKey = UserLibraryManager.CP_USERLIBRARY_PREFERENCES_PREFIX+"TEST";
	String libraryPreference = instancePreferences.get(containerKey, null);
	assertNotNull("Should get a preference for TEST user library", libraryPreference);

	assertSourceEquals(
		"Invalid library contents", 
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
		"<userlibrary systemlibrary=\"false\" version=\"1\">\n" + 
		"	<archive path=\"/tmp/test.jar\">\n" + 
		"		<attributes>\n" + 
		"			<attribute name=\"javadoc_location\" value=\"http://www.sample-url.org/doc/\"/>\n" + 
		"			<attribute name=\"org.eclipse.wst.jsdt.launching.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY\" value=\"/tmp\"/>\n" + 
		"		</attributes>\n" + 
		"		<accessrules>\n" + 
		"			<accessrule kind=\"nonaccessible\" pattern=\"**/forbidden/**\"/>\n" + 
		"			<accessrule kind=\"discouraged\" pattern=\"**/discouraged/**\"/>\n" + 
		"			<accessrule kind=\"accessible\" pattern=\"**/accessible/**\"/>\n" + 
		"		</accessrules>\n" + 
		"	</archive>\n" + 
		"	<archive path=\"/tmp/test.jar\">\n" + 
		"		<attributes>\n" + 
		"			<attribute name=\"javadoc_location\" value=\"http://www.sample-url.org/doc/\"/>\n" + 
		"			<attribute name=\"org.eclipse.wst.jsdt.launching.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY\" value=\"/tmp\"/>\n" + 
		"		</attributes>\n" + 
		"		<accessrules>\n" + 
		"			<accessrule kind=\"nonaccessible\" pattern=\"/org/eclipse/forbidden/**\"/>\n" + 
		"			<accessrule kind=\"discouraged\" pattern=\"/org/eclipse/discouraged/**\"/>\n" + 
		"			<accessrule kind=\"accessible\" pattern=\"/org/eclipse/accessible/**\"/>\n" + 
		"		</accessrules>\n" + 
		"	</archive>\n" + 
		"</userlibrary>\n", 
		libraryPreference);
}

/**
 * @bug 148859: [model][delta] Package Explorer only shows default package after import
 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=148859"
 */
public void testBug148859() throws CoreException {
	try {
		ResourcesPlugin.getWorkspace().run(
			new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					IJavaScriptProject project = createJavaProject("P");
					project.findType("X");
					createFolder("/P/pack");
				}
			},
			null);
		IPackageFragmentRoot root = getPackageFragmentRoot("P", "");
		assertElementsEqual(
			"Unexpected children size in 'P' default source folder",
			"<default> [in <project root> [in P]]\n" + 
			"pack [in <project root> [in P]]",
			root.getChildren());
	} finally {
		deleteProject("P");
	}
}
}
