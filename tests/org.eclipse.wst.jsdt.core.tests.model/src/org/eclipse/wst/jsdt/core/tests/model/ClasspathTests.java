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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import junit.framework.Test;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.jsdt.core.IAccessRule;
import org.eclipse.wst.jsdt.core.IIncludePathAttribute;
import org.eclipse.wst.jsdt.core.IIncludePathEntry;
import org.eclipse.wst.jsdt.core.IJavaScriptElementDelta;
import org.eclipse.wst.jsdt.core.IJavaScriptModelMarker;
import org.eclipse.wst.jsdt.core.IJavaScriptModelStatus;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IJsGlobalScopeContainer;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.IPackageFragmentRoot;
import org.eclipse.wst.jsdt.core.JavaScriptConventions;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.tests.util.Util;
import org.eclipse.wst.jsdt.internal.core.ClasspathEntry;
import org.eclipse.wst.jsdt.internal.core.JavaModelManager;
import org.eclipse.wst.jsdt.internal.core.JavaProject;

public class ClasspathTests extends ModifyingResourceTests {

	public class TestContainer implements IJsGlobalScopeContainer {
		IPath path;
		IIncludePathEntry[] entries;
		TestContainer(IPath path, IIncludePathEntry[] entries){
			this.path = path;
			this.entries = entries;
		}
		public IPath getPath() { return this.path; }
		/**
		 * @deprecated Use {@link #getIncludepathEntries()} instead
		 */
		public IIncludePathEntry[] getClasspathEntries() {
			return getIncludepathEntries();
		}
		public IIncludePathEntry[] getIncludepathEntries() { return this.entries;	}
		public String getDescription() { return this.path.toString(); 	}
		public int getKind() { return 0; }
		/* (non-Javadoc)
		 * @see org.eclipse.wst.jsdt.core.IJsGlobalScopeContainer#resolvedLibraryImport(java.lang.String)
		 */
		public String[] resolvedLibraryImport(String a) {
			return new String[] {a};
		}
	}

public ClasspathTests(String name) {
	super(name);
}
// Use this static initializer to specify subset for tests
// All specified tests which do not belong to the class are skipped...
static {
	// Names of tests to run: can be "testBugXXXX" or "BugXXXX")
//	TESTS_PREFIX = "testClasspathDuplicateExtraAttribute";
//	TESTS_NAMES = new String[] {"testClasspathValidation42"};
//	TESTS_NUMBERS = new int[] { 23, 28, 38 };
//	TESTS_RANGE = new int[] { 21, 38 };
}
public static Test suite() {
	return buildModelTestSuite(ClasspathTests.class);
}
public void setUpSuite() throws Exception {
	super.setUpSuite();
	
	setupExternalJCL("jclMin");
}
protected void assertCycleMarkers(IJavaScriptProject project, IJavaScriptProject[] p, int[] expectedCycleParticipants) throws CoreException {
	waitForAutoBuild();
	StringBuffer expected = new StringBuffer("{");
	int expectedCount = 0;
	StringBuffer computed = new StringBuffer("{");			
	int computedCount = 0;
	for (int j = 0; j < p.length; j++){
		int markerCount = this.numberOfCycleMarkers(p[j]);
		if (markerCount > 0){
			if (computedCount++ > 0) computed.append(", ");
			computed.append(p[j].getElementName());
			//computed.append(" (" + markerCount + ")");
		}
		markerCount = expectedCycleParticipants[j];
		if (markerCount > 0){
			if (expectedCount++ > 0) expected.append(", ");
			expected.append(p[j].getElementName());
			//expected.append(" (" + markerCount + ")");
		}
	}
	expected.append("}");
	computed.append("}");
	assertEquals("Invalid cycle detection after setting classpath for: "+project.getElementName(), expected.toString(), computed.toString());
}
private void assertEncodeDecodeEntry(String projectName, String expectedEncoded, IIncludePathEntry entry) {
	IJavaScriptProject project = getJavaProject(projectName);
	String encoded = project.encodeIncludepathEntry(entry);
	assertSourceEquals(
		"Unexpected encoded entry",
		expectedEncoded,
		encoded);
	IIncludePathEntry decoded = project.decodeIncludepathEntry(encoded);
	assertEquals(
		"Unexpected decoded entry",
		entry,
		decoded);
}
protected File createFile(File parent, String name, String content) throws IOException {
	File file = new File(parent, name);
	FileOutputStream out = new FileOutputStream(file);
	out.write(content.getBytes());
	out.close();
	/*
	 * Need to change the time stamp to realize that the file has been modified
	 */
	file.setLastModified(System.currentTimeMillis() + 2000);
	return file;
}
protected File createFolder(File parent, String name) {
	File file = new File(parent, name);
	file.mkdirs();
	return file;
}
protected int numberOfCycleMarkers(IJavaScriptProject javaProject) throws CoreException {
	IMarker[] markers = javaProject.getProject().findMarkers(IJavaScriptModelMarker.BUILDPATH_PROBLEM_MARKER, false, IResource.DEPTH_ZERO);
	int result = 0;
	for (int i = 0, length = markers.length; i < length; i++) {
		IMarker marker = markers[i];
		String cycleAttr = (String)marker.getAttribute(IJavaScriptModelMarker.CYCLE_DETECTED);
		if (cycleAttr != null && cycleAttr.equals("true")){ //$NON-NLS-1$
			result++;
		}
	}
	return result;
}

/**
 * Add an entry to the classpath for a non-existent root. Then create
 * the root and ensure that it comes alive.
 */
public void testAddRoot1() throws CoreException {
	IJavaScriptProject project = this.createJavaProject("P", new String[] {"src"});
	IIncludePathEntry[] originalCP= project.getRawIncludepath();

	try {
		IIncludePathEntry newEntry= JavaScriptCore.newSourceEntry(project.getProject().getFullPath().append("extra"));

		IIncludePathEntry[] newCP= new IIncludePathEntry[originalCP.length + 1];
		System.arraycopy(originalCP, 0 , newCP, 0, originalCP.length);
		newCP[originalCP.length]= newEntry;

		project.setRawIncludepath(newCP, null);

		// now create the actual resource for the root and populate it
		project.getProject().getFolder("extra").create(false, true, null);

		IPackageFragmentRoot newRoot= getPackageFragmentRoot("P", "extra");
		assertTrue("New root should now be visible", newRoot != null);
	} finally {
		// cleanup  
		this.deleteProject("P");
	}
}

/*
 * Adds an entry to the classpath for a non-existent root. Then creates
 * the root and ensures that the marker is removed.
 * (regression test for bug 161581 Adding a missing folder doesn't remove classpath marker)
 */
public void testAddRoot2() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {});
		project.setRawIncludepath(createClasspath("P", new String[] {"/P/src", ""}), null);
		waitForAutoBuild();

		// now create the actual resource for the root
		project.getProject().getFolder("src").create(false, true, null);
		assertMarkers("Unexpected markers", "", project);
	} finally {
		// cleanup  
		this.deleteProject("P");
	}
}

/**
 * Ensures that the reordering external resources in the classpath
 * generates the correct deltas.
 */
public void testClasspathChangeExternalResources() throws CoreException {
	try {
		IJavaScriptProject proj = this.createJavaProject("P", new String[] {"src"});

		IIncludePathEntry[] newEntries = new IIncludePathEntry[2];
		newEntries[0] = JavaScriptCore.newLibraryEntry(getSystemJsPath(), null, null, false);
		newEntries[1] = JavaScriptCore.newLibraryEntry(getExternalJCLSourcePath(), null, null, false);
		setClasspath(proj, newEntries);
		startDeltas();
		IIncludePathEntry[] swappedEntries = new IIncludePathEntry[2];
		swappedEntries[0] = newEntries[1];
		swappedEntries[1] = newEntries[0];
		setClasspath(proj, swappedEntries);
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN | CONTENT | CLASSPATH CHANGED}\n" + 
			"	"+  getSystemJsPathString() +"[*]: {REORDERED}\n" + 
			"	"+  getExternalJCLSourcePathString() +"[*]: {REORDERED}\n" + 
			"	ResourceDelta(/P/.settings)[*]"
		);
	} finally {
		stopDeltas();
		this.deleteProject("P");
	}
}

/*
 * Test classpath corruption (23977)
 */
public void testClasspathCorruption() throws CoreException {
	try {
		JavaProject p1 = (JavaProject)this.createJavaProject("P1", new String[]{""}, new String[]{}, new String[]{});
		this.createJavaProject("P2", new String[]{""}, new String[]{}, new String[]{});
		this.createFile("P2/foo.txt", "not a project");
		String newCPContent = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"
			+"<classpath>	\n"
			+"	<classpathentry kind=\"src\" path=\"\"/>	\n"
			+"	<classpathentry kind=\"src\" path=\"/P2/foo.txt\"/>	\n" // corruption here: target isn't a project
			+"</classpath>	\n";

		IFile fileRsc = p1.getProject().getFile(JavaProject.SHARED_PROPERTIES_DIRECTORY + "/" + JavaProject.CLASSPATH_FILENAME);
		fileRsc.setContents(new ByteArrayInputStream(newCPContent.getBytes()), true, false, null);

		p1.close();
		JavaModelManager.PerProjectInfo perProjectInfo = JavaModelManager.getJavaModelManager().getPerProjectInfo(p1.getProject(), true/*create if missing*/);
		perProjectInfo.setClasspath(null, null, null, null, null, null, null);

		// shouldn't fail 
		p1.getExpandedClasspath();

		// if could reach that far, then all is fine
		
	} catch(ClassCastException e){
		assertTrue("internal ClassCastException on corrupted classpath file", false);
	} finally {
		// cleanup  
		this.deleteProject("P1");
		this.deleteProject("P2");
	}
}

/*
 * Test classpath read for non-javascript project or javascript project not opened yet (40658)
 */
public void testClasspathFileRead() throws CoreException {
	try {
		final IProject proj = this.createProject("P1");
		String newCPContent = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"
			+"<classpath>	\n"
			+"	<classpathentry kind=\"src\" path=\"src\"/>	\n"
			+"</classpath>	\n";

		this.createFolder("/P1/.settings/");
		this.createFile("/P1/.settings/"+JavaProject.CLASSPATH_FILENAME, newCPContent);
		final IJavaScriptProject jproj = JavaScriptCore.create(proj);
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
			workspace.run(new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor)	{

					IIncludePathEntry[] entries = jproj.readRawIncludepath(); // force to read classpath
					IIncludePathEntry entry = entries[0];
					assertEquals("first classpath entry should have been read", "/P1/src", entry.getPath().toString());
				}
			}, null);	
	} finally {
		// cleanup  
		this.deleteProject("P1");
	}
}

/*
 * Test classpath forced reload (20931) and new way to read classpath file (40658)
 */
public void testClasspathForceReload() throws CoreException {
	try {
		final JavaProject p1 = (JavaProject)this.createJavaProject("P1", new String[]{""}, new String[]{}, new String[]{});
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
			workspace.run(new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor)	throws CoreException {

					p1.getRawIncludepath(); // force to read classpath
					createFolder("P1/src");
					String newCPContent = 
						"<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"
						+"<classpath>	\n"
						+"	<classpathentry kind=\"src\" path=\"src\"/>	\n"
						+"</classpath>	\n";

					IFile fileRsc = p1.getProject().getFile(JavaProject.SHARED_PROPERTIES_DIRECTORY + "/" + JavaProject.CLASSPATH_FILENAME);
					fileRsc.setContents(new ByteArrayInputStream(newCPContent.getBytes()), true, false, null);
					
					p1.close();
					IIncludePathEntry[] entries = p1.readRawIncludepath(); // force to read classpath
					assertEquals("source location should not have been refreshed", "/P1", entries[0].getPath().toString());
				}
			}, null);	
	} finally {
		// cleanup  
		this.deleteProject("P1");
	}
}

/**
 * Ensures that the setting the classpath with a library entry
 * changes the kind of the root from K_SOURCE to K_BINARY.
 */
public void testClasspathCreateLibraryEntry() throws CoreException {
	try {
		IJavaScriptProject proj = this.createJavaProject("P", new String[] {"src"});
		this.createFile("P/src/X.js", "function X() {}");
	
		IFolder rootFolder = proj.getProject().getFolder(new Path("src"));
		IPackageFragmentRoot root = proj.getPackageFragmentRoot(rootFolder);
		
		assertEquals(
			"Unexpected root kind 1", 
			IPackageFragmentRoot.K_SOURCE,
			root.getKind());
		IPackageFragment pkg = root.getPackageFragment("");
		assertEquals(
			"Unexpected numbers of compilation units",
			1,
			pkg.getJavaScriptUnits().length);
			
		this.setClasspath(
			proj, 
			new IIncludePathEntry[] {
				JavaScriptCore.newLibraryEntry(rootFolder.getFullPath(), null, null, false)
			});
		assertEquals(
			"Unexpected root kind 2", 
			IPackageFragmentRoot.K_BINARY,
			root.getKind());
		assertEquals(
			"Unexpected numbers of compilation units",
			0,
			pkg.getJavaScriptUnits().length);

		//ensure that the new kind has been persisted in the classpath file
		proj.close();
		assertEquals(
			"Unexpected root kind 3", 
			IPackageFragmentRoot.K_BINARY,
			root.getKind());

	} finally {
		this.deleteProject("P");
	}
}

/**
 * Tests the cross project classpath setting
 */
public void testClasspathCrossProject() throws CoreException {
	IJavaScriptProject project = this.createJavaProject("P1", new String[] {""});
	this.createJavaProject("P2", new String[] {});
	try {
		startDeltas();
		IPackageFragmentRoot oldRoot= getPackageFragmentRoot("P1", "");
 		IIncludePathEntry projectEntry= JavaScriptCore.newProjectEntry(new Path("/P2"), false);
		IIncludePathEntry[] newClasspath= new IIncludePathEntry[]{projectEntry};
		project.setRawIncludepath(newClasspath, null);
		project.getAllPackageFragmentRoots();
		IJavaScriptElementDelta removedDelta= getDeltaFor(oldRoot, true);
		assertDeltas(
			"Unexpected delta", 
			"<project root>[*]: {REMOVED FROM CLASSPATH}", 
			removedDelta);
	} finally {
		stopDeltas();
		this.deleteProjects(new String[] {"P1", "P2"});
	}
}
/**
 * Delete a root and ensure the classpath is not updated (i.e. entry isn't removed).
 */
public void testClasspathDeleteNestedRoot() throws CoreException {
	IJavaScriptProject project = this.createJavaProject("P", new String[] {"nested/src"}, new String[] {});
	IPackageFragmentRoot root= getPackageFragmentRoot("P", "nested/src");
	IIncludePathEntry[] originalCP= project.getRawIncludepath();

	// delete the root
	deleteResource(root.getUnderlyingResource());

	IIncludePathEntry[] newCP= project.getRawIncludepath();

	try {
		// should still be an entry for the "src" folder
		assertTrue("classpath should not have been updated", 
			newCP.length == 2 &&
			newCP[0].equals(originalCP[0]) &&
			newCP[1].equals(originalCP[1]));
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Test classpath diamond (23979)
 */
public void testClasspathDiamond() throws CoreException {
	try {
		this.createJavaProject("P1", new String[]{""});
		this.createJavaProject("P2", new String[]{""}, new String[]{}, new String[]{"/P1"});
		this.createJavaProject("P3", new String[]{""}, new String[]{}, new String[]{"/P1", "/P2"});
		IJavaScriptProject p4 = this.createJavaProject("P4", new String[]{""}, new String[]{}, new String[]{"/P2", "/P3"});
	
		assertTrue("Should not detect cycle", !p4.hasIncludepathCycle(null));
		
	} finally {
		// cleanup  
		this.deleteProjects(new String[] {"P1", "P2", "P3", "P4"});
	}
}
 
/**
 * Delete a nested root's parent folder and ensure the classpath is
 * not updated (i.e. entry isn't removed).
 */
public void testClasspathDeleteNestedRootParent() throws CoreException {
	IJavaScriptProject project = this.createJavaProject("P", new String[] {"nested/src"}, new String[] {});
	IPackageFragmentRoot root= getPackageFragmentRoot("P", "nested/src");
	IIncludePathEntry[] originalCP= project.getRawIncludepath();

	// delete the root's parent folder
	IFolder folder= (IFolder)root.getUnderlyingResource().getParent();
	deleteResource(folder);

	IIncludePathEntry[] newCP= project.getRawIncludepath();

	try {
		
		// should still be an entry for the "src" folder
		assertTrue("classpath should not have been updated", 
			newCP.length == 2 &&
			newCP[0].equals(originalCP[0]) &&
			newCP[1].equals(originalCP[1]));
	} finally {
		this.deleteProject("P");
	}
}
/**
 * Test that a classpath entry for an external jar is externalized
 * properly.
 */
public void testClasspathExternalize() throws CoreException {
	try {
		IJavaScriptProject project= this.createJavaProject("P", new String[] {}, new String[] {getSystemJsPathString()});
		IIncludePathEntry[] classpath= project.getRawIncludepath();
		IIncludePathEntry jar= null;
		for (int i= 0; i < classpath.length; i++) {
			if (classpath[i].getEntryKind() == IIncludePathEntry.CPE_LIBRARY) {
				jar= classpath[i];
				break;
			}
		}
		project.close();
		project.open(null);
	
		classpath= project.getRawIncludepath();
		for (int i= 0; i < classpath.length; i++) {
			if (classpath[i].getEntryKind() == IIncludePathEntry.CPE_LIBRARY) {
				assertTrue("Paths must be the same", classpath[i].getPath().equals(jar.getPath()));
				break;
			}
		}   
	} finally {
		this.deleteProject("P");
	}
}
/**
 * Move a root and ensure the classpath is not updated (i.e. entry not renamed).
 */
public void testClasspathMoveNestedRoot() throws CoreException {
	IJavaScriptProject project = this.createJavaProject("P", new String[] {"nested/src"}, new String[] {});
	IPackageFragmentRoot root= getPackageFragmentRoot("P", "nested/src");
	IIncludePathEntry[] originalCP= project.getRawIncludepath();

	// delete the root
	IFolder folder= (IFolder)root.getUnderlyingResource();
	IPath originalPath= folder.getFullPath();
	IPath newPath= originalPath.removeLastSegments(1);
	newPath= newPath.append(new Path("newsrc"));

	startDeltas(); 
	
	folder.move(newPath, true, null);

	IIncludePathEntry[] newCP= project.getRawIncludepath();

	IPackageFragmentRoot newRoot= project.getPackageFragmentRoot(project.getProject().getFolder("nested").getFolder("newsrc")); 

	try {
		// entry for the "src" folder wasn't replaced
		assertTrue("classpath not automatically updated", newCP.length == 2 &&
			newCP[1].equals(originalCP[1]) &&
			newCP[0].equals(originalCP[0]));

		IJavaScriptElementDelta rootDelta = getDeltaFor(root, true);
		IJavaScriptElementDelta projectDelta = getDeltaFor(newRoot.getParent(), true);
		assertTrue("should get delta for moved root", rootDelta != null &&
				rootDelta.getKind() == IJavaScriptElementDelta.REMOVED &&
				rootDelta.getFlags() == 0);
		assertTrue("should get delta indicating content changed for project", this.deltaContentChanged(projectDelta));
	
	} finally {
		stopDeltas();
		this.deleteProject("P");
	}
}

/**
 * Move a parent of a nested root and ensure the classpath is not updated (i.e. entry not renamed).
 */
public void testClasspathMoveNestedRootParent() throws CoreException {
	try {
		IJavaScriptProject project =this.createJavaProject("P", new String[] {"nested/src"}, new String[] {});
		IPackageFragmentRoot root= getPackageFragmentRoot("P", "nested/src");
		IIncludePathEntry[] originalCP= project.getRawIncludepath();
	
		// delete the root
		IFolder folder= (IFolder)root.getUnderlyingResource().getParent();
		IPath originalPath= folder.getFullPath();
		IPath newPath= originalPath.removeLastSegments(1);
		newPath= newPath.append(new Path("newsrc"));
		folder.move(newPath, true, null);
	
		IIncludePathEntry[] newCP= project.getRawIncludepath();

		// entry for the "src" folder wasn't replaced
		// entry for the "src" folder should not be replaced
		assertTrue("classpath should not automatically be updated", newCP.length == 2 &&
			newCP[1].equals(originalCP[1]) &&
			newCP[0].equals(originalCP[0]));

	} finally {
		this.deleteProject("P");
	}
}
/**
 * Tests that nothing occurs when setting to the same classpath
 */
public void testClasspathNoChanges() throws CoreException {
	try {
		IJavaScriptProject p = this.createJavaProject("P", new String[] {""});
		IIncludePathEntry[] oldClasspath= p.getRawIncludepath();
		startDeltas();
		p.setRawIncludepath(oldClasspath, null);
		assertDeltas("Unexpected delta", "");
	} finally {
		stopDeltas();
		this.deleteProject("P");
	}
}
/**
 * Ensures that the setting the classpath with a reordered classpath generates
 * the correct deltas.
 */
public void testClasspathReordering() throws CoreException {
	IJavaScriptProject proj = this.createJavaProject("P", new String[] {"src"}, new String[] {getSystemJsPathString()});
	IIncludePathEntry[] originalCP = proj.getRawIncludepath();
	IPackageFragmentRoot root = getPackageFragmentRoot("P", "src");
	try {
		IIncludePathEntry[] newEntries = new IIncludePathEntry[originalCP.length];
		int index = originalCP.length - 1;
		for (int i = 0; i < originalCP.length; i++) {
			newEntries[index] = originalCP[i];
			index--;
		}
		startDeltas();
		setClasspath(proj, newEntries);
		assertTrue("should be one delta - two roots reordered", this.deltaListener.deltas.length == 1);
		IJavaScriptElementDelta d = null;
		assertTrue("root should be reordered in the classpath", (d = getDeltaFor(root, true)) != null
			&& (d.getFlags() & IJavaScriptElementDelta.F_REORDER) > 0);
	} finally {
		stopDeltas();
		this.deleteProject("P");
	}
}

/**
 * Should detect duplicate entries on the classpath
 */ 
public void testClasspathValidation01() throws CoreException {
	try {
		IJavaScriptProject proj = this.createJavaProject("P", new String[] {"src"});
		IIncludePathEntry[] originalCP = proj.getRawIncludepath();
	
		IIncludePathEntry[] newCP = new IIncludePathEntry[originalCP.length+1];
		System.arraycopy(originalCP, 0, newCP, 0, originalCP.length);
		newCP[originalCP.length] = newCP[0];
		
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspath(proj, newCP);
		
		assertStatus(
			"should have detected duplicate entries on the classpath", 
			"Include path contains duplicate entry: \'src\' for project P",
			status);
	} finally {
		this.deleteProject("P");
	}
}

/**
 * Should detect nested source folders on the classpath
 */ 
public void testClasspathValidation02() throws CoreException {
	try {
		IJavaScriptProject proj =  this.createJavaProject("P", new String[] {"src"});
		IIncludePathEntry[] originalCP = proj.getRawIncludepath();
	
		IIncludePathEntry[] newCP = new IIncludePathEntry[originalCP.length+1];
		System.arraycopy(originalCP, 0, newCP, 0, originalCP.length);
		newCP[originalCP.length] = JavaScriptCore.newSourceEntry(new Path("/P"));
		
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspath(proj, newCP);
		
		assertStatus(
			"should have detected nested source folders on the classpath", 
			"Cannot nest \'P/src\' inside \'P\'. To enable the nesting exclude \'src/\' from \'P\'",
			status);
	} finally {
		this.deleteProject("P");
	}
}

/**
 * Should detect library folder nested inside source folder on the classpath
 */ 
public void testClasspathValidation03() throws CoreException {
	try {
		IJavaScriptProject proj =  this.createJavaProject("P", new String[] {"src"});
		IIncludePathEntry[] originalCP = proj.getRawIncludepath();
	
		IIncludePathEntry[] newCP = new IIncludePathEntry[originalCP.length+1];
		System.arraycopy(originalCP, 0, newCP, 0, originalCP.length);
		newCP[originalCP.length] = JavaScriptCore.newLibraryEntry(new Path("/P/src/lib"), null, null);
		
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspath(proj, newCP);
		
		assertStatus(
			"should have detected library folder nested inside source folder on the classpath", 
			"Cannot nest \'P/src/lib\' inside \'P/src\'. To enable the nesting exclude \'lib/\' from \'P/src\'",
			status);
	} finally {
		this.deleteProject("P");
	}
}

public void testClasspathValidation04() throws CoreException {
	
	IJavaScriptProject[] p = null;
	try {

		p = new IJavaScriptProject[]{
			this.createJavaProject("P0", new String[] {"src0"}),
			this.createJavaProject("P1", new String[] {"src1"}),
		};

		JavaScriptCore.setIncludepathVariable("var", new Path("/P1"), null);
		
		IIncludePathEntry[] newClasspath = new IIncludePathEntry[]{
			JavaScriptCore.newSourceEntry(new Path("/P0/src0")),
			JavaScriptCore.newVariableEntry(new Path("var/src1"), null, null),
		};
				
		// validate classpath
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspath(p[0], newClasspath);
		assertStatus(
			"should not detect external source folder through a variable on the classpath", 
			"OK",
			status);

	} finally {
		this.deleteProjects(new String[] {"P0", "P1"});
	}
}

public void testClasspathValidation05() throws CoreException {
	
	IJavaScriptProject[] p = null;
	try {

		p = new IJavaScriptProject[]{
			this.createJavaProject("P0", new String[] {"src0", "src1"}),
			this.createJavaProject("P1", new String[] {"src1"}),
		};

		JavaScriptCore.setJsGlobalScopeContainer(
		new Path("container/default"), 
			new IJavaScriptProject[]{ p[0] },
			new IJsGlobalScopeContainer[] {
				new TestContainer(new Path("container/default"),
					new IIncludePathEntry[]{
						JavaScriptCore.newSourceEntry(new Path("/P0/src0")),
						JavaScriptCore.newVariableEntry(new Path("var/src1"), null, null) }) 
			}, 
			null);
		
		IIncludePathEntry[] newClasspath = new IIncludePathEntry[]{
			JavaScriptCore.newSourceEntry(new Path("/P0/src1")),
			JavaScriptCore.newContainerEntry(new Path("container/default")),
		};
				
		// validate classpath
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspath(p[0], newClasspath);
		assertStatus(
			"should not have detected external source folder through a container on the classpath", 
			"OK",
			status);

		// validate classpath entry
		status = JavaScriptConventions.validateClasspathEntry(p[0], newClasspath[1], true);
		assertStatus(
			"should have detected external source folder through a container on the classpath", 
			"Invalid classpath container: \'container/default\' in project P0",
			status);

	} finally {
		this.deleteProjects(new String[] {"P0", "P1"});
	}
}

public void testClasspathValidation06() throws CoreException {
	
	IJavaScriptProject[] p = null;
	try {

		p = new IJavaScriptProject[]{
			this.createJavaProject("P0", new String[] {"src"}),
		};

		// validate classpath entry
		IIncludePathEntry[] newClasspath = new IIncludePathEntry[]{
			JavaScriptCore.newSourceEntry(new Path("/P0")),
			JavaScriptCore.newSourceEntry(new Path("/P0/src")),
		};
				
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspath(p[0], newClasspath);
		assertStatus(
			"should have detected nested source folder", 
			"Cannot nest \'P0/src\' inside \'P0\'. To enable the nesting exclude \'src/\' from \'P0\'",
			status);
	} finally {
		this.deleteProject("P0");
	}
}
/**
 * Should allow nested source folders on the classpath as long as the outer
 * folder excludes the inner one.
 */ 
public void testClasspathValidation07() throws CoreException {
	try {
		IJavaScriptProject proj =  this.createJavaProject("P", new String[] {"src"});
		IIncludePathEntry[] originalCP = proj.getRawIncludepath();
	
		IIncludePathEntry[] newCP = new IIncludePathEntry[originalCP.length+1];
		System.arraycopy(originalCP, 0, newCP, 0, originalCP.length);
		newCP[originalCP.length] = JavaScriptCore.newSourceEntry(new Path("/P"), new IPath[] {new Path("src/")});
		
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspath(proj, newCP);
		
		assertStatus(
			"should have allowed nested source folders with exclusion on the classpath", 
			"OK",
			status);
	} finally {
		this.deleteProject("P");
	}
}
/**
 * Should allow a nested binary folder in a source folder on the classpath as
 * long as the outer folder excludes the inner one.
 */ 
public void testClasspathValidation08() throws CoreException {
	try {
		IJavaScriptProject proj =  this.createJavaProject("P", new String[] {}, new String[] {"lib"});
		IIncludePathEntry[] originalCP = proj.getRawIncludepath();
	
		IIncludePathEntry[] newCP = new IIncludePathEntry[originalCP.length+1];
		System.arraycopy(originalCP, 0, newCP, 0, originalCP.length);
		newCP[originalCP.length] = JavaScriptCore.newSourceEntry(new Path("/P"), new IPath[] {new Path("lib/")});
		
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspath(proj, newCP);
		
		assertStatus(
			"should have allowed nested lib folders with exclusion on the classpath", 
			"OK",
			status);
	} finally {
		this.deleteProject("P");
	}
}
/**
 * Should not allow nested source folders on the classpath if exclusion filter has no trailing slash.
 */ 
public void testClasspathValidation15() throws CoreException {
	try {
		IJavaScriptProject proj =  this.createJavaProject("P", new String[] {"src"});
		IIncludePathEntry[] originalCP = proj.getRawIncludepath();
	
		IIncludePathEntry[] newCP = new IIncludePathEntry[originalCP.length+1];
		System.arraycopy(originalCP, 0, newCP, 0, originalCP.length);
		newCP[originalCP.length] = JavaScriptCore.newSourceEntry(new Path("/P"), new IPath[] {new Path("**/src")});
		
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspath(proj, newCP);
		
		assertStatus(
			"End exclusion filter \'src\' with / to fully exclude \'P/src\'",
			status);
	} finally {
		this.deleteProject("P");
	}
}
/**
 * Should detect source folder nested inside library folder on the classpath
 */ 
public void testClasspathValidation19() throws CoreException {
	try {
		IJavaScriptProject proj =  this.createJavaProject("P", new String[] {}, new String[] {"lib"});
		IIncludePathEntry[] originalCP = proj.getRawIncludepath();
	
		IIncludePathEntry[] newCP = new IIncludePathEntry[originalCP.length+1];
		System.arraycopy(originalCP, 0, newCP, 0, originalCP.length);
		newCP[originalCP.length] = JavaScriptCore.newSourceEntry(new Path("/P/lib/src"));
		
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspath(proj, newCP);
		
		assertStatus(
			"should have detected library folder nested inside source folder on the classpath", 
			"Cannot nest \'P/lib/src\' inside library \'P/lib\'",
			status);
	} finally {
		this.deleteProject("P");
	}
}
/**
 * Should not allow exclusion patterns if project preference disallow them
 */
public void testClasspathValidation21() throws CoreException {
	try {
		IJavaScriptProject proj =  this.createJavaProject("P", new String[] {});
		IIncludePathEntry[] originalCP = proj.getRawIncludepath();
	
		IIncludePathEntry[] newCP = new IIncludePathEntry[originalCP.length+1];
		System.arraycopy(originalCP, 0, newCP, 0, originalCP.length);
		newCP[originalCP.length] = JavaScriptCore.newSourceEntry(new Path("/P/src"), new IPath[]{new Path("**/src")}, null);
		
		Map options = new Hashtable(5);
		options.put(JavaScriptCore.CORE_ENABLE_CLASSPATH_EXCLUSION_PATTERNS, JavaScriptCore.DISABLED);
		proj.setOptions(options);
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspath(proj, newCP);
		
		assertStatus(
			"Inclusion or exclusion patterns are disabled in project P, cannot selectively include or exclude from entry: \'src\'",
			status);
	} finally {
		this.deleteProject("P");
	}
}
/**
 * @bug 159325: Any idea why ClasspathEntry checks for string object reference instead of equals
 * @test Ensure that validation is correctly done even for other strings than JavaScriptCore constants...
 * 	Note that it's needed to change JavaScriptCore options as "ignore" is the default value and set option
 * 	to this value on java project will just remove it instead of putting another string object...
 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=159325"
 */
public void testClasspathValidation27_Bug159325_project() throws CoreException {
	Hashtable javaCoreOptions = JavaScriptCore.getOptions();
	try {
		IJavaScriptProject proj1 =  this.createJavaProject("P1", new String[] {});
		proj1.setOption(JavaScriptCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaScriptCore.VERSION_1_4);

		Hashtable options = JavaScriptCore.getOptions();
		options.put(JavaScriptCore.CORE_INCOMPATIBLE_JDK_LEVEL, JavaScriptCore.WARNING);
		JavaScriptCore.setOptions(options);
		IJavaScriptProject proj2 =  this.createJavaProject("P2", new String[] {});
		proj2.setOption(JavaScriptCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaScriptCore.VERSION_1_1);
		proj2.setOption(JavaScriptCore.CORE_INCOMPATIBLE_JDK_LEVEL, new String("ignore".toCharArray()));

		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspathEntry(proj2, JavaScriptCore.newProjectEntry(new Path("/P1")), false);
		assertStatus("OK", status);
	} finally {
		JavaScriptCore.setOptions(javaCoreOptions);
		this.deleteProjects(new String[]{"P1", "P2"});
	}
}
public void testClasspathValidation27_Bug159325_lib() throws CoreException {
	Hashtable javaCoreOptions = JavaScriptCore.getOptions();
	try {
		IJavaScriptProject proj =  this.createJavaProject("P1", new String[] {});
		proj.setOption(JavaScriptCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaScriptCore.VERSION_1_1);

		Hashtable options = JavaScriptCore.getOptions();
		options.put(JavaScriptCore.CORE_INCOMPATIBLE_JDK_LEVEL, JavaScriptCore.WARNING);
		JavaScriptCore.setOptions(options);
		proj.setOption(JavaScriptCore.CORE_INCOMPATIBLE_JDK_LEVEL, new String("ignore".toCharArray()));

		IIncludePathEntry library = JavaScriptCore.newLibraryEntry(new Path(getExternalJCLPathString(JavaScriptCore.VERSION_1_5)), null, null, ClasspathEntry.NO_ACCESS_RULES, null, false);
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspathEntry(proj, library, false);
		assertStatus("OK", status);
	} finally {
		JavaScriptCore.setOptions(javaCoreOptions);
		this.deleteProjects(new String[]{"P1", "P2"});
	}
}
/**
 * Should not allow nested source folders on the classpath if the outer
 * folder includes the inner one.
 */ 
public void testClasspathValidation34() throws CoreException {
	try {
		IJavaScriptProject proj =  this.createJavaProject("P", new String[] {"src"});
		IIncludePathEntry[] originalCP = proj.getRawIncludepath();
	
		IIncludePathEntry[] newCP = new IIncludePathEntry[originalCP.length+1];
		System.arraycopy(originalCP, 0, newCP, 0, originalCP.length);
		newCP[originalCP.length] = JavaScriptCore.newSourceEntry(new Path("/P"), new IPath[] {new Path("src/")}, new IPath[0], null);
		
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspath(proj, newCP);
		
		assertStatus(
			"should not have allowed nested source folders with inclusion on the classpath", 
			"Cannot nest \'P/src\' inside \'P\'. To enable the nesting exclude \'src/\' from \'P\'",
			status);
	} finally {
		this.deleteProject("P");
	}
}

/**
 * Should not allow a nested binary folder in a source folder on the classpath
 * if the outer folder includes the inner one.
 */ 
public void testClasspathValidation35() throws CoreException {
	try {
		IJavaScriptProject proj =  this.createJavaProject("P", new String[] {}, new String[] {"lib"});
		IIncludePathEntry[] originalCP = proj.getRawIncludepath();
	
		IIncludePathEntry[] newCP = new IIncludePathEntry[originalCP.length+1];
		System.arraycopy(originalCP, 0, newCP, 0, originalCP.length);
		newCP[originalCP.length] = JavaScriptCore.newSourceEntry(new Path("/P"), new IPath[] {new Path("lib/")}, new Path[0], null);
		
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspath(proj, newCP);
		
		assertStatus(
			"should not have allowed nested lib folders with inclusion on the classpath", 
			"Cannot nest \'P/lib\' inside \'P\'. To enable the nesting exclude \'lib/\' from \'P\'",
			status);
	} finally {
		this.deleteProject("P");
	}
}

/**
 * Should allow nested source folders on the classpath if inclusion filter has no trailing slash.
 */ 
public void testClasspathValidation36() throws CoreException {
	try {
		IJavaScriptProject proj =  this.createJavaProject("P", new String[] {"src"});
		IIncludePathEntry[] originalCP = proj.getRawIncludepath();
	
		IIncludePathEntry[] newCP = new IIncludePathEntry[originalCP.length+1];
		System.arraycopy(originalCP, 0, newCP, 0, originalCP.length);
		newCP[originalCP.length] = JavaScriptCore.newSourceEntry(new Path("/P"), new IPath[] {new Path("**/src")}, new Path[0], null);
		
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspath(proj, newCP);
		
		assertStatus(
			"OK",
			status);
	} finally {
		this.deleteProject("P");
	}
}
/**
 * Should not allow inclusion patterns if project preference disallow them
 */
public void testClasspathValidation37() throws CoreException {
	try {
		IJavaScriptProject proj =  this.createJavaProject("P", new String[] {});
		IIncludePathEntry[] originalCP = proj.getRawIncludepath();
	
		IIncludePathEntry[] newCP = new IIncludePathEntry[originalCP.length+1];
		System.arraycopy(originalCP, 0, newCP, 0, originalCP.length);
		newCP[originalCP.length] = JavaScriptCore.newSourceEntry(new Path("/P/src"), new IPath[]{new Path("**/src")}, new Path[0], null);
		
		Map options = new Hashtable(5);
		options.put(JavaScriptCore.CORE_ENABLE_CLASSPATH_EXCLUSION_PATTERNS, JavaScriptCore.DISABLED);
		proj.setOptions(options);
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspath(proj, newCP);
		
		assertStatus(
			"Inclusion or exclusion patterns are disabled in project P, cannot selectively include or exclude from entry: \'src\'",
			status);
	} finally {
		this.deleteProject("P");
	}
}
/*
 * Should detect nested source folders on the classpath and indicate the preference if disabled
 * (regression test for bug 122615 validate classpath propose to exlude a source folder even though exlusion patterns are disabled)
 */ 
public void testClasspathValidation42() throws CoreException {
	try {
		IJavaScriptProject proj =  this.createJavaProject("P", new String[] {"src"});
		proj.setOption(JavaScriptCore.CORE_ENABLE_CLASSPATH_EXCLUSION_PATTERNS, JavaScriptCore.DISABLED);
		IIncludePathEntry[] originalCP = proj.getRawIncludepath();
	
		IIncludePathEntry[] newCP = new IIncludePathEntry[originalCP.length+1];
		System.arraycopy(originalCP, 0, newCP, 0, originalCP.length);
		newCP[originalCP.length] = JavaScriptCore.newSourceEntry(new Path("/P"));
		
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspath(proj, newCP);
		
		assertStatus(
			"should have detected nested source folders on the classpath", 
			"Cannot nest \'P/src\' inside \'P\'. To allow the nesting enable use of exclusion patterns in the preferences of project \'P\' and exclude \'src/\' from \'P\'",
			status);
	} finally {
		this.deleteProject("P");
	}
}
/**
 * Setting the classpath with two entries specifying the same path
 * should fail.
 */
public void testClasspathWithDuplicateEntries() throws CoreException {
	try {
		IJavaScriptProject project =  this.createJavaProject("P", new String[] {"src"});
		IIncludePathEntry[] cp= project.getRawIncludepath();
		IIncludePathEntry[] newCp= new IIncludePathEntry[cp.length *2];
		System.arraycopy(cp, 0, newCp, 0, cp.length);
		System.arraycopy(cp, 0, newCp, cp.length, cp.length);
		try {
			project.setRawIncludepath(newCp, null);
		} catch (JavaScriptModelException jme) {
			return;
		}
		assertTrue("Setting the classpath with two entries specifying the same path should fail", false);
	} finally {
		this.deleteProject("P");
	}
}
/**
 * Bug 94404: [model] Disallow classpath attributes with same key
 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=94404"
 */
public void testClasspathDuplicateExtraAttribute() throws CoreException {
	try {
		IJavaScriptProject proj =  this.createJavaProject("P1", new String[] {});
		IIncludePathAttribute[] extraAttributes = new IIncludePathAttribute[2];
		extraAttributes[0] = JavaScriptCore.newIncludepathAttribute("javadoc_location", "http://www.sample-url.org/doc/");
		extraAttributes[1] = JavaScriptCore.newIncludepathAttribute("javadoc_location", "d:/tmp");

		// Verify container entry validation
		IIncludePathEntry container = JavaScriptCore.newContainerEntry(new Path("JRE_CONTAINER"), ClasspathEntry.NO_ACCESS_RULES, extraAttributes, false);
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspathEntry(proj, container, false);
		assertStatus(
			"Duplicate extra attribute: \'javadoc_location\' in classpath entry \'JRE_CONTAINER\' for project 'P1'",
			status);

		// Verify library entry validation
		IIncludePathEntry library = JavaScriptCore.newLibraryEntry(new Path(getSystemJsPathString()), null, null, ClasspathEntry.NO_ACCESS_RULES, extraAttributes, false);
		status = JavaScriptConventions.validateClasspathEntry(proj, library, false);
		assertStatus(
			"Duplicate extra attribute: \'javadoc_location\' in classpath entry \'"+getExternalJCLPath("")+"\' for project 'P1'",
			status);

		// Verify project entry validation
		createJavaProject("P2");
		IIncludePathEntry projectEntry = JavaScriptCore.newProjectEntry(new Path("/P2"), ClasspathEntry.NO_ACCESS_RULES, false, extraAttributes, false);
		status = JavaScriptConventions.validateClasspathEntry(proj, projectEntry, false);
		assertStatus(
			"Duplicate extra attribute: \'javadoc_location\' in classpath entry \'/P2\' for project 'P1'",
			status);

		// Verify source entry validation
		createFolder("/P1/src");
		IIncludePathEntry sourceEntry = JavaScriptCore.newSourceEntry(new Path("/P1/src"), new IPath[0], new IPath[0], null, extraAttributes);
		status = JavaScriptConventions.validateClasspathEntry(proj, sourceEntry, false);
		assertStatus(
			"Duplicate extra attribute: \'javadoc_location\' in classpath entry \'src\' for project 'P1'",
			status);

		// Verify variable entry validation
		IIncludePathEntry variable = JavaScriptCore.newVariableEntry(new Path("JCL_LIB"), new Path("JCL_SRC"), null, ClasspathEntry.NO_ACCESS_RULES, extraAttributes, false);
		status = JavaScriptConventions.validateClasspathEntry(proj, variable, false);
		assertStatus(
			"Duplicate extra attribute: \'javadoc_location\' in classpath entry \'"+getExternalJCLPath("")+"\' for project 'P1'",
			status);
	} finally {
		this.deleteProject("P1");
		this.deleteProject("P2");
	}
}

/**
 * Adding an entry to the classpath for a library that does not exist
 * should not break the model. The classpath should contain the
 * entry, but the root should not appear in the children.
 */
public void testClasspathWithNonExistentLibraryEntry() throws CoreException {
	try {
		IJavaScriptProject project=  this.createJavaProject("P", new String[] {"src"});
		IIncludePathEntry[] originalPath= project.getRawIncludepath();
		IPackageFragmentRoot[] originalRoots= project.getPackageFragmentRoots();
	
		IIncludePathEntry[] newPath= new IIncludePathEntry[originalPath.length + 1];
		System.arraycopy(originalPath, 0, newPath, 0, originalPath.length);
	
		IIncludePathEntry newEntry= JavaScriptCore.newLibraryEntry(new Path("c:/nothing/nozip.jar").makeAbsolute(), null, null, false);
		newPath[originalPath.length]= newEntry;
	
		project.setRawIncludepath(newPath, null);

		IIncludePathEntry[] getPath= project.getRawIncludepath();
		assertTrue("should be the same length", getPath.length == newPath.length);
		for (int i= 0; i < getPath.length; i++) {
			assertTrue("entries should be the same", getPath[i].equals(newPath[i]));
		}

		IPackageFragmentRoot[] newRoots= project.getPackageFragmentRoots();
		assertTrue("Should be the same number of roots", originalRoots.length == newRoots.length);
		for (int i= 0; i < newRoots.length; i++) {
			assertTrue("roots should be the same", originalRoots[i].equals(newRoots[i]));
		}
	} finally {
		this.deleteProject("P");
	}
}
/**
 * Adding an entry to the classpath for a project that does not exist
 * should not break the model. The classpath should contain the
 * entry, but the root should not appear in the children.
 */
public void testClasspathWithNonExistentProjectEntry() throws CoreException {
	try {
		IJavaScriptProject project= this.createJavaProject("P", new String[] {"src"});
		IIncludePathEntry[] originalPath= project.getRawIncludepath();
		IPackageFragmentRoot[] originalRoots= project.getPackageFragmentRoots();
	
		IIncludePathEntry[] newPath= new IIncludePathEntry[originalPath.length + 1];
		System.arraycopy(originalPath, 0, newPath, 0, originalPath.length);
	
		IIncludePathEntry newEntry= JavaScriptCore.newProjectEntry(new Path("/NoProject"), false);
		newPath[originalPath.length]= newEntry;
	
		project.setRawIncludepath(newPath, null);
	
		IIncludePathEntry[] getPath= project.getRawIncludepath();
		assertTrue("should be the same length", getPath.length == newPath.length);
		for (int i= 0; i < getPath.length; i++) {
			assertTrue("entries should be the same", getPath[i].equals(newPath[i]));
		}
	
		IPackageFragmentRoot[] newRoots= project.getPackageFragmentRoots();
		assertTrue("Should be the same number of roots", originalRoots.length == newRoots.length);
		for (int i= 0; i < newRoots.length; i++) {
			assertTrue("roots should be the same", originalRoots[i].equals(newRoots[i]));
		}
	} finally {
		this.deleteProject("P");
	}
}
/**
 * Adding an entry to the classpath for a folder that does not exist
 * should not break the model. The classpath should contain the
 * entry, but the root should not appear in the children.
 */
public void testClasspathWithNonExistentSourceEntry() throws CoreException {
	try {
		IJavaScriptProject project= this.createJavaProject("P", new String[] {"src"});
		IIncludePathEntry[] originalPath= project.getRawIncludepath();
		IPackageFragmentRoot[] originalRoots= project.getPackageFragmentRoots();

		IIncludePathEntry[] newPath= new IIncludePathEntry[originalPath.length + 1];
		System.arraycopy(originalPath, 0, newPath, 0, originalPath.length);

		IIncludePathEntry newEntry= JavaScriptCore.newSourceEntry(new Path("/P/moreSource"));
		newPath[originalPath.length]= newEntry;

		project.setRawIncludepath(newPath, null);

		IIncludePathEntry[] getPath= project.getRawIncludepath();
		assertTrue("should be the same length", getPath.length == newPath.length);
		for (int i= 0; i < getPath.length; i++) {
			assertTrue("entries should be the same", getPath[i].equals(newPath[i]));
		}

		IPackageFragmentRoot[] newRoots= project.getPackageFragmentRoots();
		assertTrue("Should be the same number of roots", originalRoots.length == newRoots.length);
		for (int i= 0; i < newRoots.length; i++) {
			assertTrue("roots should be the same", originalRoots[i].equals(newRoots[i]));
		}
	} finally {
		this.deleteProject("P");
	}
}

/**
 * Ensure that cycle are properly reported.
 */
public void testCycleReport() throws CoreException {

	try {
		IJavaScriptProject p1 = this.createJavaProject("P1", new String[] {""});
		IJavaScriptProject p2 = this.createJavaProject("P2", new String[] {""});
		IJavaScriptProject p3 = this.createJavaProject("P3", new String[] {""}, new String[] {}, new String[] {"/P2"});
	
		// Ensure no cycle reported
		IJavaScriptProject[] projects = { p1, p2, p3 };
		int cycleMarkerCount = 0;
		for (int i = 0; i < projects.length; i++){
			cycleMarkerCount += this.numberOfCycleMarkers(projects[i]);
		}
		assertTrue("Should have no cycle markers", cycleMarkerCount == 0);
	
		// Add cycle
		IIncludePathEntry[] originalP1CP= p1.getRawIncludepath();
		IIncludePathEntry[] originalP2CP= p2.getRawIncludepath();

		// Add P1 as a prerequesite of P2
		int length = originalP2CP.length;
		IIncludePathEntry[] newCP= new IIncludePathEntry[length + 1];
		System.arraycopy(originalP2CP, 0 , newCP, 0, length);
		newCP[length]= JavaScriptCore.newProjectEntry(p1.getProject().getFullPath(), false);
		p2.setRawIncludepath(newCP, null);

		// Add P3 as a prerequesite of P1
		length = originalP1CP.length;
		newCP= new IIncludePathEntry[length + 1];
		System.arraycopy(originalP1CP, 0 , newCP, 0, length);
		newCP[length]= JavaScriptCore.newProjectEntry(p3.getProject().getFullPath(), false);
		p1.setRawIncludepath(newCP, null);

		waitForAutoBuild(); // wait for cycle markers to be created
		cycleMarkerCount = 0;
		for (int i = 0; i < projects.length; i++){
			cycleMarkerCount += numberOfCycleMarkers(projects[i]);
		}
		assertEquals("Unexpected number of projects involved in a classpath cycle", 3, cycleMarkerCount);
		
	} finally {
		// cleanup  
		deleteProjects(new String[] {"P1", "P2", "P3"});
	}
}
/**
 * Ensures that the default classpath locations are correct.
 * The default classpath should be the root of the project.
 */
public void testDefaultClasspathLocation() throws CoreException {
	try {
		IJavaScriptProject project = this.createJavaProject("P", new String[] {""});
		IIncludePathEntry[] classpath = project.getRawIncludepath();
		assertTrue("Incorrect default classpath; to many entries", classpath.length == 2);
		assertTrue("Incorrect default classpath: " + classpath[0], classpath[0].getPath().equals(project.getUnderlyingResource().getFullPath()));
	} finally {
		this.deleteProject("P");
	}
}

/**
 * Setting the classpath to empty should result in no entries,
 * and a delta with removed roots.
 */
public void testEmptyClasspath() throws CoreException {
	IJavaScriptProject project = this.createJavaProject("P", new String[] {""});
	try {
		startDeltas();
		setClasspath(project, new IIncludePathEntry[] {});
		IIncludePathEntry[] cp= project.getRawIncludepath();
		assertTrue("classpath should have no entries", cp.length == 0);

		// ensure the deltas are correct
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN | CONTENT | CLASSPATH CHANGED}\n" + 
			"	<project root>[*]: {REMOVED FROM CLASSPATH}\n" + 
			"  D:\\WTPDevelopment\\junit-workspace\\.metadata\\.plugins\\org.eclipse.wst.jsdt.core\\libraries\\system.js[*]: {REORDERED}\n" +
			"	ResourceDelta(/P/.settings)[*]"
		);
	} finally {
		stopDeltas();
		this.deleteProject("P");
	}
}
/*
 * Ensures that a source folder that contains character that must be encoded can be written.
 * (regression test for bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=70193)
 */
public void testEncoding() throws CoreException {
	try {
		createJavaProject("P", new String[] {"src\u3400"});
		IFile file = getFile("/P/.settings/.jsdtscope");
		String encodedContents = new String (org.eclipse.wst.jsdt.internal.core.util.Util.getResourceContentsAsCharArray(file, "UTF-8"));
		encodedContents = Util.convertToIndependantLineDelimiter(encodedContents);
		assertEquals(
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<classpath>\n" +
			"	<classpathentry kind=\"src\" path=\"src\u3400\"/>\n" +
			" <classpathentry kind=\"con\" path=\"org.eclipse.wst.jsdt.launching.JRE_CONTAINER\"/>\n" +
			"	<classpathentry kind=\"output\" path=\"\"/>\n" +
			"</classpath>\n",
			encodedContents);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a source classpath entry can be encoded and decoded.
 */
public void testEncodeDecodeEntry01() {
	assertEncodeDecodeEntry(
		"P", 
		"<classpathentry kind=\"src\" path=\"src\"/>\n", 
		JavaScriptCore.newSourceEntry(new Path("/P/src"))
	);
}
/*
 * Ensures that a source classpath entry with all possible attributes can be encoded and decoded.
 */
public void testEncodeDecodeEntry02() {
	assertEncodeDecodeEntry(
		"P", 
		"<classpathentry excluding=\"**/X.js\" including=\"**/Y.js\" kind=\"src\" output=\"bin\" path=\"src\">\n" + 
		"	<attributes>\n" + 
		"		<attribute name=\"attrName\" value=\"some value\"/>\n" + 
		"	</attributes>\n" + 
		"</classpathentry>\n",
		JavaScriptCore.newSourceEntry(
			new Path("/P/src"), 
			new IPath[] {new Path("**/Y.js")},
			new IPath[] {new Path("**/X.js")},
			new Path("/P/bin"),
			new IIncludePathAttribute[] {JavaScriptCore.newIncludepathAttribute("attrName", "some value")})
	);
}
/*
 * Ensures that a project classpath entry can be encoded and decoded.
 */
public void testEncodeDecodeEntry03() {
	assertEncodeDecodeEntry(
		"P1", 
		"<classpathentry kind=\"src\" path=\"/P2\"/>\n",
		JavaScriptCore.newProjectEntry(new Path("/P2"))
	);
}
/*
 * Ensures that a library classpath entry can be encoded and decoded.
 */
public void testEncodeDecodeEntry04() {
	assertEncodeDecodeEntry(
		"P", 
		"<classpathentry exported=\"true\" kind=\"lib\" path=\"lib.jar\" rootpath=\"root\" sourcepath=\"src.zip\">\n" + 
		"	<attributes>\n" + 
		"		<attribute name=\"attr1\" value=\"val1\"/>\n" + 
		"	</attributes>\n" + 
		"	<accessrules>\n" + 
		"		<accessrule kind=\"accessible\" pattern=\"**/A*.js\"/>\n" + 
		"	</accessrules>\n" + 
		"</classpathentry>\n",
		JavaScriptCore.newLibraryEntry(
			new Path("/P/lib.jar"),
			new Path("/P/src.zip"),
			new Path("root"),
			new IAccessRule[] {JavaScriptCore.newAccessRule(new Path("**/A*.js"), IAccessRule.K_ACCESSIBLE)},
			new IIncludePathAttribute[] {JavaScriptCore.newIncludepathAttribute("attr1", "val1")},
			true)
	);
}
/*
 * Ensures that a library classpath entry can be encoded and decoded.
 */
public void testEncodeDecodeEntry05() {
	assertEncodeDecodeEntry(
		"P", 
		"<classpathentry exported=\"true\" kind=\"lib\" path=\"lib.jar\" rootpath=\"root\" sourcepath=\"src.zip\">\n" + 
		"	<attributes>\n" + 
		"		<attribute name=\"attr1\" value=\"val1\"/>\n" + 
		"	</attributes>\n" + 
		"	<accessrules>\n" + 
		"		<accessrule ignoreifbetter=\"true\" kind=\"accessible\" pattern=\"**/A*.js\"/>\n" + 
		"	</accessrules>\n" + 
		"</classpathentry>\n",
		JavaScriptCore.newLibraryEntry(
			new Path("/P/lib.jar"),
			new Path("/P/src.zip"),
			new Path("root"),
			new IAccessRule[] {JavaScriptCore.newAccessRule(new Path("**/A*.js"), IAccessRule.K_ACCESSIBLE | IAccessRule.IGNORE_IF_BETTER)},
			new IIncludePathAttribute[] {JavaScriptCore.newIncludepathAttribute("attr1", "val1")},
			true)
	);
}
/**
 * Ensures that adding an empty classpath container
 * generates the correct deltas.
 */
public void testEmptyContainer() throws CoreException {
	try {
		IJavaScriptProject proj = createJavaProject("P", new String[] {});

		startDeltas();

		// create container
		JavaScriptCore.setJsGlobalScopeContainer(
			new Path("container/default"), 
			new IJavaScriptProject[]{ proj },
			new IJsGlobalScopeContainer[] {
				new TestContainer(
					new Path("container/default"),
					new IIncludePathEntry[] {}) 
			}, 
			null);

		// set P's classpath with this container
		IIncludePathEntry container = JavaScriptCore.newContainerEntry(new Path("container/default"), true);
		proj.setRawIncludepath(new IIncludePathEntry[] {container}, null);

		assertDeltas(
			"Unexpected delta",
			"P[*]: {CONTENT | CLASSPATH CHANGED}\n" + 
			"   D:\\WTPDevelopment\\junit-workspace\\.metadata\\.plugins\\org.eclipse.wst.jsdt.core\\libraries\\system.js[*]: {REORDERED}\n" +
			"	ResourceDelta(/P/.classpath)[*]"
		);
	} finally {
		stopDeltas();
		this.deleteProject("P");
	}
}
/*
 * Ensure that a .classpath with an empty inclusion pattern is correctly handled
 * (regression test for bug 105581 Creating a Java project from existing source fails because of "Unhandled event loop exception":)
 */
public void testEmptyInclusionPattern() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {""});
		project.open(null);
		editFile(
			"/P/.settings/.jsdtscope",
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"  <classpathentry including=\"X.java|\" kind=\"src\" path=\"\"/>\n" + 
			"  <classpathentry kind=\"output\" path=\"bin\"/>\n" + 
			"</classpath>"
		);
		project.getProject().close(null);
		project.getProject().open(null);
		project.getPackageFragmentRoot(project.getProject()).open(null);
		IIncludePathEntry[] classpath = project.getRawIncludepath();
		assertClasspathEquals(
			classpath, 
			"/P[CPE_SOURCE][K_SOURCE][isExported:false][including:X.java]"
		);
	} finally {
		deleteProject("P");
	}
}
/**
 * Exporting a container should make it visible to its dependent project.
 * (regression test for bug 21749 Exported libraries and source folders)
 */
public void testExportContainer() throws CoreException {
	try {
		IJavaScriptProject p1 = this.createJavaProject("P1", new String[] {""});

		// create container
		JavaScriptCore.setJsGlobalScopeContainer(
			new Path("container/default"), 
			new IJavaScriptProject[]{ p1 },
			new IJsGlobalScopeContainer[] {
				new TestContainer(
					new Path("container/default"),
					new IIncludePathEntry[] {
						JavaScriptCore.newLibraryEntry(getExternalJCLPath(""), null, null)
					}) 
			}, 
			null);

		// set P1's classpath with this container
		IIncludePathEntry container = JavaScriptCore.newContainerEntry(new Path("container/default"), true);
		p1.setRawIncludepath(new IIncludePathEntry[] {container}, null);
		
		// create dependent project P2
		IJavaScriptProject  p2 = this.createJavaProject("P2", new String[] {}, new String[] {}, new String[] {"/P1"});
		IIncludePathEntry[] classpath = ((JavaProject)p2).getExpandedClasspath();
		
		// ensure container is exported to P2
		assertEquals("Unexpected number of classpath entries", 2, classpath.length);
		assertEquals("Unexpected first entry", "/P1", classpath[0].getPath().toString());
		assertEquals("Unexpected second entry", getSystemJsPathString(), classpath[1].getPath().toOSString());
	} finally {
		this.deleteProjects(new String[] {"P1", "P2"});
	}
}
/*
 * Ensures that setting 0 extra classpath attributes generates the correct .classpath file.
 */
public void testExtraAttributes1() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		IIncludePathEntry entry = JavaScriptCore.newSourceEntry(new Path("/P"), new IPath[0], new IPath[0], null, new IIncludePathAttribute[] {});
		project.setRawIncludepath(new IIncludePathEntry[] {entry}, null);
		String contents = new String (org.eclipse.wst.jsdt.internal.core.util.Util.getResourceContentsAsCharArray(getFile("/P/.settings/.jsdtscope")));
		assertSourceEquals(
			"Unexpected content", 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"	<classpathentry kind=\"src\" path=\"\"/>\n" + 
			"	<classpathentry kind=\"output\" path=\"\"/>\n" + 
			"</classpath>\n",
			contents);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that setting 1 extra classpath attributes generates the correct .classpath file.
 */
public void testExtraAttributes2() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		IIncludePathAttribute attribute = JavaScriptCore.newIncludepathAttribute("foo", "some value");
		IIncludePathEntry entry = JavaScriptCore.newSourceEntry(new Path("/P"), new IPath[0], new IPath[0], null, new IIncludePathAttribute[] {attribute});
		project.setRawIncludepath(new IIncludePathEntry[] {entry}, null);
		String contents = new String (org.eclipse.wst.jsdt.internal.core.util.Util.getResourceContentsAsCharArray(getFile("/P/.settings/.jsdtscope")));
		assertSourceEquals(
			"Unexpected content", 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"	<classpathentry kind=\"src\" path=\"\">\n" + 
			"		<attributes>\n" + 
			"			<attribute name=\"foo\" value=\"some value\"/>\n" + 
			"		</attributes>\n" + 
			"	</classpathentry>\n" + 
			"	<classpathentry kind=\"output\" path=\"\"/>\n" + 
			"</classpath>\n",
			contents);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that setting 2 extra classpath attributes generates the correct .classpath file.
 */
public void testExtraAttributes3() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		IIncludePathAttribute attribute1 = JavaScriptCore.newIncludepathAttribute("foo", "some value");
		IIncludePathAttribute attribute2 = JavaScriptCore.newIncludepathAttribute("bar", "other value");
		IIncludePathEntry entry = JavaScriptCore.newSourceEntry(new Path("/P"), new IPath[0], new IPath[0], null, new IIncludePathAttribute[] {attribute1, attribute2});
		project.setRawIncludepath(new IIncludePathEntry[] {entry}, null);
		String contents = new String (org.eclipse.wst.jsdt.internal.core.util.Util.getResourceContentsAsCharArray(getFile("/P/.settings/.jsdtscope")));
		assertSourceEquals(
			"Unexpected content", 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"	<classpathentry kind=\"src\" path=\"\">\n" + 
			"		<attributes>\n" + 
			"			<attribute name=\"foo\" value=\"some value\"/>\n" + 
			"			<attribute name=\"bar\" value=\"other value\"/>\n" + 
			"		</attributes>\n" + 
			"	</classpathentry>\n" + 
			"	<classpathentry kind=\"output\" path=\"\"/>\n" + 
			"</classpath>\n",
			contents);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that extra classpath attributes in a .classpath file are correctly read.
 */
public void testExtraAttributes4() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		editFile(
			"/P/.settings/.jsdtscope",
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"	<classpathentry kind=\"src\" path=\"\">\n" + 
			"		<attributes>\n" + 
			"			<attribute value=\"some value\" name=\"foo\"/>\n" + 
			"		</attributes>\n" + 
			"	</classpathentry>\n" + 
			"	<classpathentry kind=\"output\" path=\"\"/>\n" + 
			"</classpath>\n"
		);
		assertClasspathEquals(
			project.getRawIncludepath(),
			"/P[CPE_SOURCE][K_SOURCE][isExported:false][attributes:foo=some value]"
		);
	} finally {
		deleteProject("P");
	}
}
/**
 * Test IJavaScriptProject.hasClasspathCycle(IIncludePathEntry[]).
 */
public void testHasClasspathCycle() throws CoreException {
	try {
		IJavaScriptProject p1 = this.createJavaProject("P1", new String[] {""});
		IJavaScriptProject p2 = this.createJavaProject("P2", new String[] {""});
		this.createJavaProject("P3", new String[] {""}, new String[] {}, new String[] {"/P1"});
	
		IIncludePathEntry[] originalP1CP= p1.getRawIncludepath();
		IIncludePathEntry[] originalP2CP= p2.getRawIncludepath();
	
		// Ensure no cycle reported
		assertTrue("P1 should not have a cycle", !p1.hasIncludepathCycle(originalP1CP));

		// Ensure that adding NervousTest as a prerequesite of P2 doesn't report a cycle
		int length = originalP2CP.length;
		IIncludePathEntry[] newCP= new IIncludePathEntry[length + 1];
		System.arraycopy(originalP2CP, 0 , newCP, 0, length);
		newCP[length]= JavaScriptCore.newProjectEntry(p1.getProject().getFullPath(), false);
		assertTrue("P2 should not have a cycle", !p2.hasIncludepathCycle(newCP));
		p2.setRawIncludepath(newCP, null);

		// Ensure that adding P3 as a prerequesite of P1 reports a cycle
		length = originalP1CP.length;
		newCP= new IIncludePathEntry[length + 1];
		System.arraycopy(originalP1CP, 0 , newCP, 0, length);
		newCP[length]= JavaScriptCore.newProjectEntry(p2.getProject().getFullPath(), false);
		assertTrue("P3 should have a cycle", p2.hasIncludepathCycle(newCP));

		// Ensure a cycle is not reported through markers
		IWorkspace workspace = getJavaModel().getWorkspace();
		IMarker[] markers = workspace.getRoot().findMarkers(IJavaScriptModelMarker.TRANSIENT_PROBLEM, true, 1);
		boolean hasCycleMarker = false;
		for (int i = 0; i < markers.length; i++){
			if (markers[i].getAttribute(IJavaScriptModelMarker.CYCLE_DETECTED) != null) {
				hasCycleMarker = true;
				break;
			}
		}
	assertTrue("Should have no cycle markers", !hasCycleMarker);
		
	} finally {
		// cleanup  
		this.deleteProjects(new String[] {"P1", "P2", "P3"});
	}
}
/**
 * Ensures that a marker is created if editing the .classpath results in an invalid classpath.
 */
public void testInvalidClasspath1() throws CoreException {
	try {
		IJavaScriptProject project = this.createJavaProject("P", new String[] {"src"});
		this.editFile(
			"/P/.settings/.jsdtscope",
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<classpath>\n" +
			"    <classpathentry kind=\"src\" path=\"src\"/\n" + // missing closing >
			"    <classpathentry kind=\"output\" path=\"bin\"/>\n" +
			"</classpath>"
		);
		assertMarkers(
			"Unexpected markers",
			"XML format error in \'.classpath\' file of project P: Bad format",
			project);
	} finally {
		this.deleteProject("P");
	}
}
/**
 * Ensures that a marker is created if editing the .classpath results in an invalid classpath.
 */
public void testInvalidClasspath2() throws CoreException {
	try {
		IJavaScriptProject javaProject = this.createJavaProject("P", new String[] {"src"});
		this.editFile(
			"/P/.settings/.jsdtscope",
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<classpath>\n" +
			"    <classpathentry kind=\"src1\" path=\"src\"/>\n" + // invalid kind: src1
			"    <classpathentry kind=\"output\" path=\"bin\"/>\n" +
			"</classpath>"
		);
		assertMarkers(
			"Unexpected markers",
			"Illegal entry in \'.classpath\' of project P file: Unknown kind: \'src1\'",
			javaProject);
			
		// Verify that error marker is not removed after build
		// (regression test for bug 42366: Classpath validation error message removed while rebuilding a project.)
		IProject project = javaProject.getProject();
		project.build(IncrementalProjectBuilder.FULL_BUILD, null);
		waitForAutoBuild();
		assertMarkers(
			"Unexpected markers",
			"Illegal entry in \'.classpath\' of project P file: Unknown kind: \'src1\'",
			javaProject);
	} finally {
		this.deleteProject("P");
	}
}

/*
 * Ensures that a non existing source folder cannot be put on the classpath.
 * (regression test for bug 66512 Invalid classpath entry not rejected)
 */
public void testInvalidSourceFolder() throws CoreException {
	try {
		createJavaProject("P1");
		IJavaScriptProject proj = createJavaProject("P2", new String[] {}, new String[] {}, new String[] {"/P1/src1/src2"});
		assertMarkers(
			"Unexpected markers",
			"Project P2 is missing required source folder: \'/P1/src1/src2\'",
			proj);
	} finally {
		deleteProject("P1");
		deleteProject("P2");
	}
}
/**
 * Ensures that only one marker is created if building a project that is
 * missing its .classpath file multiple times.
 * (regression test for bug 39877 Rebuild All generates extra "Unable to read classpath" entry.)
 */
public void testMissingClasspath() throws CoreException {
	try {
		IJavaScriptProject javaProject = createJavaProject("P");
		IProject project = javaProject.getProject();
		project.close(null);
		deleteFile(new File(project.getLocation().toOSString(), ".settings/.jsdtscope"));
		waitForAutoBuild();
		project.open(null);
		waitForAutoBuild();
		project.build(IncrementalProjectBuilder.FULL_BUILD, null);
		waitForAutoBuild();
		project.build(IncrementalProjectBuilder.FULL_BUILD, null);
		waitForAutoBuild();
		assertMarkers(
			"Unexpected markers",
			"Unable to read \'.jsdtscope\' file of project P",
			javaProject);
	} finally {
		this.deleteProject("P");
	}
}
/**
 * Test that a marker is added when a project as a missing project in its classpath.
 */
public void testMissingPrereq1() throws CoreException {
	try {
		IJavaScriptProject javaProject = this.createJavaProject("A", new String[] {});
		IIncludePathEntry[] classpath = 
			new IIncludePathEntry[] {
				JavaScriptCore.newProjectEntry(new Path("/B"))
			};
		javaProject.setRawIncludepath(classpath, null);
		this.assertMarkers(
			"Unexpected markers",
			"Project A is missing required Java project: \'B\'",
			javaProject);
	} finally {
		this.deleteProject("A");
	}
}
/**
 * Test that a marker is added when a project as a missing project in its classpath.
 */
public void testMissingPrereq2() throws CoreException {
	try {
		IJavaScriptProject javaProject = 
			this.createJavaProject(
				"A", 
				new String[] {}, // source folders
				new String[] {}, // lib folders
				new String[] {"/B"});
		this.assertMarkers(
			"Unexpected markers",
			"Project A is missing required Java project: \'B\'",
			javaProject);
	} finally {
		this.deleteProject("A");
	}
}
/**
 * Test that a marker indicating a missing project is removed when the project is added.
 */
public void testMissingPrereq3() throws CoreException {
	try {
		IJavaScriptProject javaProject = 
			this.createJavaProject(
				"A", 
				new String[] {}, // source folders
				new String[] {}, // lib folders
				new String[] {"/B"});
		this.createJavaProject("B", new String[] {});
		this.assertMarkers("Unexpected markers", "", javaProject);
	} finally {
		this.deleteProjects(new String[] {"A", "B"});
	}
}
/**
 * Test that a marker indicating a cycle is removed when a project in the cycle is deleted
 * and replaced with a missing prereq marker.
 * (regression test for bug 15168 circular errors not reported)
 */
public void testMissingPrereq4() throws CoreException {
	try {
		IJavaScriptProject projectA =
			this.createJavaProject(
				"A", 
				new String[] {}, // source folders
				new String[] {}, // lib folders
				new String[] {"/B"});
		IJavaScriptProject projectB =
			this.createJavaProject(
				"B", 
				new String[] {}, // source folders
				new String[] {}, // lib folders
				new String[] {"/A"});
		this.assertMarkers(
			"Unexpected markers for project A",
			"A cycle was detected in the include path of project: A",
			projectA);
		this.assertMarkers(
			"Unexpected markers for project B",
			"A cycle was detected in the include path of project: B",
			projectB);
		
		// delete project B	
		this.deleteProject("B");
		this.assertMarkers(
			"Unexpected markers for project A after deleting of project B",
			"Project A is missing required Java project: \'B\'",
			projectA);
			
		// add project B back
		projectB =
			this.createJavaProject(
				"B", 
				new String[] {}, // source folders
				new String[] {}, // lib folders
				new String[] {"/A"});
		this.assertMarkers(
			"Unexpected markers for project A after adding project B back",
			"A cycle was detected in the build path of project: A",
			projectA);
		this.assertMarkers(
			"Unexpected markers for project B after adding project B back",
			"A cycle was detected in the build path of project: B",
			projectB);

	} finally {
		this.deleteProjects(new String[] {"A", "B"});
	}
}
/**
 * Setting the classpath to null should be the same as using the
 * default classpath.
 */
public void testNullClasspath() throws CoreException {
	try {
		IJavaScriptProject project = this.createJavaProject("P", new String[] {""});
		setClasspath(project, null);
		IIncludePathEntry[] cp= project.getRawIncludepath();
		assertTrue("classpath should have one root entry", cp.length == 1 && cp[0].getPath().equals(project.getUnderlyingResource().getFullPath()));
	} finally {
		this.deleteProject("P");
	}
}

/*
 * Ensures that setting the 'combineAccessRules' flag to false on a project entry generates the correct .classpath file.
 */
public void testCombineAccessRules1() throws CoreException {
	try {
		createJavaProject("P1");
		IJavaScriptProject project = createJavaProject("P2");
		IIncludePathEntry entry = JavaScriptCore.newProjectEntry(new Path("/P1"), (IAccessRule[]) null, false/*don't combine*/, new IIncludePathAttribute[] {}, false);
		project.setRawIncludepath(new IIncludePathEntry[] {entry}, null);
		String contents = new String (org.eclipse.wst.jsdt.internal.core.util.Util.getResourceContentsAsCharArray(getFile("/P2/.settings/.jsdtscope")));
		assertSourceEquals(
			"Unexpected content", 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"	<classpathentry combineaccessrules=\"false\" kind=\"src\" path=\"/P1\"/>\n" + 
			"	<classpathentry kind=\"output\" path=\"\"/>\n" + 
			"</classpath>\n",
			contents);
	} finally {
		deleteProject("P1");
		deleteProject("P2");
	}
}

/*
 * Ensures that setting the 'combineAccessRules' flag to true on a project entry generates the correct .classpath file.
 */
public void testCombineAccessRules2() throws CoreException {
	try {
		createJavaProject("P1");
		IJavaScriptProject project = createJavaProject("P2");
		IIncludePathEntry entry = JavaScriptCore.newProjectEntry(new Path("/P1"), (IAccessRule[]) null, true/*combine*/, new IIncludePathAttribute[] {}, false);
		project.setRawIncludepath(new IIncludePathEntry[] {entry}, null);
		String contents = new String (org.eclipse.wst.jsdt.internal.core.util.Util.getResourceContentsAsCharArray(getFile("/P2/.settings/.jsdtscope")));
		assertSourceEquals(
			"Unexpected content", 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"	<classpathentry kind=\"src\" path=\"/P1\"/>\n" + 
			"	<classpathentry kind=\"output\" path=\"\"/>\n" + 
			"</classpath>\n",
			contents);
	} finally {
		deleteProject("P1");
		deleteProject("P2");
	}
}

/*
 * Ensures that 'combineAccessRules' flag in a .classpath file is correctly read.
 */
public void testCombineAccessRules3() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P2");
		editFile(
			"/P2/.settings/.jsdtscope",
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"	<classpathentry kind=\"src\" combineaccessrules=\"false\" path=\"/P1\"/>\n" + 
			"	<classpathentry kind=\"output\" path=\"\"/>\n" + 
			"</classpath>\n"
		);
		assertClasspathEquals(
			project.getRawIncludepath(),
			"/P1[CPE_PROJECT][K_SOURCE][isExported:false][combine access rules:false]"
		);
	} finally {
		deleteProject("P2");
	}
}

/*
 * Ensures that the absence of 'combineAccessRules' flag in a .classpath file is correctly handled.
 */
public void testCombineAccessRules4() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P2");
		editFile(
			"/P2/.settings/.jsdtscope",
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"	<classpathentry kind=\"src\" path=\"/P1\"/>\n" + 
			"	<classpathentry kind=\"output\" path=\"\"/>\n" + 
			"</classpath>\n"
		);
		assertClasspathEquals(
			project.getRawIncludepath(),
			"/P1[CPE_PROJECT][K_SOURCE][isExported:false][combine access rules:true]"
		);
	} finally {
		deleteProject("P2");
	}
}

/*
 * Ensures that the absence of 'combineAccessRules' flag in a .classpath file is correctly handled.
 */
public void testCombineAccessRules5() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P2");
		editFile(
			"/P2/.settings/.jsdtscope",
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"	<classpathentry kind=\"src\" path=\"src\"/>\n" + 
			"	<classpathentry kind=\"output\" path=\"bin\"/>\n" + 
			"</classpath>\n"
		);
		assertClasspathEquals(
			project.getRawIncludepath(),
			"/P2/src[CPE_SOURCE][K_SOURCE][isExported:false]"
		);
	} finally {
		deleteProject("P2");
	}
}

public void testCycleDetection() throws CoreException {
	
	int max = 5;
	IJavaScriptProject[] p = new IJavaScriptProject[max];
	String[] projectNames = new String[max];
	try {
		for (int i = 0; i < max; i++) {
			projectNames[i] = "P"+i;
			p[i] = this.createJavaProject(projectNames[i], new String[] {""});
		}

		IIncludePathEntry[][] extraEntries = new IIncludePathEntry[][]{ 
			{ JavaScriptCore.newProjectEntry(p[1].getPath()), JavaScriptCore.newProjectEntry(p[3].getPath()) },
			{ JavaScriptCore.newProjectEntry(p[2].getPath()), JavaScriptCore.newProjectEntry(p[3].getPath()) },
			{ JavaScriptCore.newProjectEntry(p[1].getPath()) }, 
			{ JavaScriptCore.newProjectEntry(p[4].getPath())}, 
			{ JavaScriptCore.newProjectEntry(p[3].getPath()), JavaScriptCore.newProjectEntry(p[0].getPath()) } 
		}; 

		int[][] expectedCycleParticipants = new int[][] {
			{ 0, 0, 0, 0, 0 }, // after setting CP p[0]
			{ 0, 0, 0, 0, 0 }, // after setting CP p[1]
			{ 0, 1, 1, 0, 0 }, // after setting CP p[2]
			{ 0, 1, 1, 0, 0 }, // after setting CP p[3]
			{ 1, 1, 1, 1, 1 }, // after setting CP p[4]
		};
		
		for (int i = 0; i < p.length; i++){

			// append project references			
			IIncludePathEntry[] oldClasspath = p[i].getRawIncludepath();
			IIncludePathEntry[] newClasspath = new IIncludePathEntry[oldClasspath.length+extraEntries[i].length];
			System.arraycopy(oldClasspath, 0 , newClasspath, 0, oldClasspath.length);
			for (int j = 0; j < extraEntries[i].length; j++){
				newClasspath[oldClasspath.length+j] = extraEntries[i][j];
			}			
			// set classpath
			p[i].setRawIncludepath(newClasspath, null);

			// check cycle markers
			this.assertCycleMarkers(p[i], p, expectedCycleParticipants[i]);
		}
		//this.startDeltas();
		
	} finally {
		this.deleteProjects(projectNames);
	}
}

public void testCycleDetectionThroughContainers() throws CoreException {
	
	int max = 5;
	IJavaScriptProject[] p = new IJavaScriptProject[max];
	String[] projectNames = new String[max];
	try {
		for (int i = 0; i < max; i++) {
			projectNames[i] = "P"+i;
			p[i] = this.createJavaProject(projectNames[i], new String[] {""});
		}

		IJsGlobalScopeContainer[] containers = new IJsGlobalScopeContainer[]{ 
			new TestContainer(
				new Path("container0/default"), 
				new IIncludePathEntry[]{ JavaScriptCore.newProjectEntry(p[3].getPath()) }),
			new TestContainer(
				new Path("container1/default"), 
				new IIncludePathEntry[]{ JavaScriptCore.newProjectEntry(p[1].getPath()) }),
			new TestContainer(
				new Path("container2/default"), 
				new IIncludePathEntry[]{ JavaScriptCore.newProjectEntry(p[4].getPath()) }),
		};

		IIncludePathEntry[][] extraEntries = new IIncludePathEntry[][]{ 
			{ JavaScriptCore.newProjectEntry(p[1].getPath()), JavaScriptCore.newContainerEntry(containers[0].getPath()) },
			{ JavaScriptCore.newProjectEntry(p[2].getPath()), JavaScriptCore.newProjectEntry(p[3].getPath()) },
			{ JavaScriptCore.newContainerEntry(containers[1].getPath()) }, 
			{ JavaScriptCore.newContainerEntry(containers[2].getPath())}, 
			{ JavaScriptCore.newProjectEntry(p[3].getPath()), JavaScriptCore.newProjectEntry(p[0].getPath()) } 
		}; 

		int[][] expectedCycleParticipants = new int[][] {
			{ 0, 0, 0, 0, 0 }, // after setting CP p[0]
			{ 0, 0, 0, 0, 0 }, // after setting CP p[1]
			{ 0, 0, 0, 0, 0 }, // after setting CP p[2]
			{ 0, 0, 0, 0, 0 }, // after setting CP p[3]
			{ 1, 1, 1, 1, 1 }, // after setting CP p[4]
		};
		
		for (int i = 0; i < p.length; i++){

			// append project references			
			IIncludePathEntry[] oldClasspath = p[i].getRawIncludepath();
			IIncludePathEntry[] newClasspath = new IIncludePathEntry[oldClasspath.length+extraEntries[i].length];
			System.arraycopy(oldClasspath, 0 , newClasspath, 0, oldClasspath.length);
			for (int j = 0; j < extraEntries[i].length; j++){
				newClasspath[oldClasspath.length+j] = extraEntries[i][j];
			}			
			// set classpath
			p[i].setRawIncludepath(newClasspath, null);

			// update container paths
			if (i == p.length - 1){
				JavaScriptCore.setJsGlobalScopeContainer(
					containers[0].getPath(),
					new IJavaScriptProject[]{ p[0] },
					new IJsGlobalScopeContainer[] { containers[0] },
					null);

				JavaScriptCore.setJsGlobalScopeContainer(
					containers[1].getPath(),
					new IJavaScriptProject[]{ p[2] },
					new IJsGlobalScopeContainer[] { containers[1] },
					null);

				JavaScriptCore.setJsGlobalScopeContainer(
					containers[2].getPath(),
					new IJavaScriptProject[]{ p[3] },
					new IJsGlobalScopeContainer[] { containers[2] },
					null);
			}
			
			// check cycle markers
			this.assertCycleMarkers(p[i], p, expectedCycleParticipants[i]);
		}
		//this.startDeltas();
		
	} finally {
		//this.stopDeltas();
		this.deleteProjects(projectNames);
	}
}
public void testCycleDetectionThroughContainerVariants() throws CoreException {
	
	int max = 5;
	IJavaScriptProject[] p = new IJavaScriptProject[max];
	String[] projectNames = new String[max];
	try {
		for (int i = 0; i < max; i++) {
			projectNames[i] = "P"+i;
			p[i] = this.createJavaProject(projectNames[i], new String[] {""});
		}

		class LocalTestContainer implements IJsGlobalScopeContainer {
			IPath path;
			IIncludePathEntry[] entries;
			LocalTestContainer(IPath path, IIncludePathEntry[] entries){
				this.path = path;
				this.entries = entries;
			}
			public IPath getPath() { return this.path; }
			/**
			 * @deprecated Use {@link #getIncludepathEntries()} instead
			 */
			public IIncludePathEntry[] getClasspathEntries() {
				return getIncludepathEntries();
			}
			public IIncludePathEntry[] getIncludepathEntries() { return this.entries;	}
			public String getDescription() { return null; 	}
			public int getKind() { return 0; }
			/* (non-Javadoc)
			 * @see org.eclipse.wst.jsdt.core.IJsGlobalScopeContainer#resolvedLibraryImport(java.lang.String)
			 */
			public String[] resolvedLibraryImport(String a) {
				return new String[] {a};
			}
		}

		IJsGlobalScopeContainer[] containers = new IJsGlobalScopeContainer[]{ 
			new LocalTestContainer(
				new Path("container0/default"), 
				new IIncludePathEntry[]{ JavaScriptCore.newProjectEntry(p[3].getPath()) }),
			new LocalTestContainer(
				new Path("container0/default"), 
				new IIncludePathEntry[]{ JavaScriptCore.newProjectEntry(p[1].getPath()) }),
			new LocalTestContainer(
				new Path("container0/default"), 
				new IIncludePathEntry[]{ JavaScriptCore.newProjectEntry(p[4].getPath()) }),
		};

		IIncludePathEntry[][] extraEntries = new IIncludePathEntry[][]{ 
			{ JavaScriptCore.newProjectEntry(p[1].getPath()), JavaScriptCore.newContainerEntry(containers[0].getPath()) },
			{ JavaScriptCore.newProjectEntry(p[2].getPath()), JavaScriptCore.newProjectEntry(p[3].getPath()) },
			{ JavaScriptCore.newContainerEntry(containers[1].getPath()) }, 
			{ JavaScriptCore.newContainerEntry(containers[2].getPath())}, 
			{ JavaScriptCore.newProjectEntry(p[3].getPath()), JavaScriptCore.newProjectEntry(p[0].getPath()) } 
		}; 

		int[][] expectedCycleParticipants = new int[][] {
			{ 0, 0, 0, 0, 0 }, // after setting CP p[0]
			{ 0, 0, 0, 0, 0 }, // after setting CP p[1]
			{ 0, 0, 0, 0, 0 }, // after setting CP p[2]
			{ 0, 0, 0, 0, 0 }, // after setting CP p[3]
			{ 1, 1, 1, 1, 1 }, // after setting CP p[4]
		};
		
		for (int i = 0; i < p.length; i++){

			// append project references			
			IIncludePathEntry[] oldClasspath = p[i].getRawIncludepath();
			IIncludePathEntry[] newClasspath = new IIncludePathEntry[oldClasspath.length+extraEntries[i].length];
			System.arraycopy(oldClasspath, 0 , newClasspath, 0, oldClasspath.length);
			for (int j = 0; j < extraEntries[i].length; j++){
				newClasspath[oldClasspath.length+j] = extraEntries[i][j];
			}			
			// set classpath
			p[i].setRawIncludepath(newClasspath, null);

			// update same container path for multiple projects
			if (i == p.length - 1){
				JavaScriptCore.setJsGlobalScopeContainer(
					containers[0].getPath(),
					new IJavaScriptProject[]{ p[0], p[2], p[3] },
					new IJsGlobalScopeContainer[] { containers[0], containers[1], containers[2] },
					null);
			}
			
			// check cycle markers
			this.assertCycleMarkers(p[i], p, expectedCycleParticipants[i]);
		}
		//this.startDeltas();
		
	} finally {
		//this.stopDeltas();
		this.deleteProjects(projectNames);
	}
}
public void testCycleDetection2() throws CoreException {
	
	int max = 5;
	IJavaScriptProject[] p = new IJavaScriptProject[max];
	String[] projectNames = new String[max];
	try {
		for (int i = 0; i < max; i++) {
			projectNames[i] = "P"+i;
			p[i] = this.createJavaProject(projectNames[i], new String[] {""});
		}

		IIncludePathEntry[][] extraEntries = new IIncludePathEntry[][]{ 
			{ JavaScriptCore.newProjectEntry(p[1].getPath()), JavaScriptCore.newProjectEntry(p[3].getPath()) },
			{ JavaScriptCore.newProjectEntry(p[2].getPath()) },
			{ JavaScriptCore.newProjectEntry(p[0].getPath()) }, 
			{ JavaScriptCore.newProjectEntry(p[4].getPath())}, 
			{ JavaScriptCore.newProjectEntry(p[0].getPath()) } 
		}; 

		int[][] expectedCycleParticipants = new int[][] {
			{ 0, 0, 0, 0, 0 }, // after setting CP p[0]
			{ 0, 0, 0, 0, 0 }, // after setting CP p[1]
			{ 1, 1, 1, 0, 0 }, // after setting CP p[2]
			{ 1, 1, 1, 0, 0 }, // after setting CP p[3]
			{ 1, 1, 1, 1, 1 }, // after setting CP p[4]
		};
		
		for (int i = 0; i < p.length; i++){

			// append project references			
			IIncludePathEntry[] oldClasspath = p[i].getRawIncludepath();
			IIncludePathEntry[] newClasspath = new IIncludePathEntry[oldClasspath.length+extraEntries[i].length];
			System.arraycopy(oldClasspath, 0 , newClasspath, 0, oldClasspath.length);
			for (int j = 0; j < extraEntries[i].length; j++){
				newClasspath[oldClasspath.length+j] = extraEntries[i][j];
			}			
			// set classpath
			p[i].setRawIncludepath(newClasspath, null);

			// check cycle markers
			this.assertCycleMarkers(p[i], p, expectedCycleParticipants[i]);
		}
		//this.startDeltas();
		
	} finally {
		//this.stopDeltas();
		this.deleteProjects(projectNames);
	}
}

public void testCycleDetection3() throws CoreException {
	
	int max = 6;
	IJavaScriptProject[] p = new IJavaScriptProject[max];
	String[] projectNames = new String[max];
	try {
		for (int i = 0; i < max; i++) {
			projectNames[i] = "P"+i;
			p[i] = this.createJavaProject(projectNames[i], new String[] {""});
		}

		IIncludePathEntry[][] extraEntries = new IIncludePathEntry[][]{ 
			{ JavaScriptCore.newProjectEntry(p[2].getPath()), JavaScriptCore.newProjectEntry(p[4].getPath()) },
			{ JavaScriptCore.newProjectEntry(p[0].getPath()) },
			{ JavaScriptCore.newProjectEntry(p[3].getPath()) }, 
			{ JavaScriptCore.newProjectEntry(p[1].getPath())}, 
			{ JavaScriptCore.newProjectEntry(p[5].getPath()) }, 
			{ JavaScriptCore.newProjectEntry(p[1].getPath()) } 
		}; 

		int[][] expectedCycleParticipants = new int[][] {
			{ 0, 0, 0, 0, 0, 0 }, // after setting CP p[0]
			{ 0, 0, 0, 0, 0, 0 }, // after setting CP p[1]
			{ 0, 0, 0, 0, 0, 0 }, // after setting CP p[2]
			{ 1, 1, 1, 1, 0, 0 }, // after setting CP p[3]
			{ 1, 1, 1, 1, 0, 0 }, // after setting CP p[4]
			{ 1, 1, 1, 1, 1 , 1}, // after setting CP p[5]
		};
		
		for (int i = 0; i < p.length; i++){

			// append project references			
			IIncludePathEntry[] oldClasspath = p[i].getRawIncludepath();
			IIncludePathEntry[] newClasspath = new IIncludePathEntry[oldClasspath.length+extraEntries[i].length];
			System.arraycopy(oldClasspath, 0 , newClasspath, 0, oldClasspath.length);
			for (int j = 0; j < extraEntries[i].length; j++){
				newClasspath[oldClasspath.length+j] = extraEntries[i][j];
			}			
			// set classpath
			p[i].setRawIncludepath(newClasspath, null);

			// check cycle markers
			this.assertCycleMarkers(p[i], p, expectedCycleParticipants[i]);
		}
		//this.startDeltas();
		
	} finally {
		//this.stopDeltas();
		this.deleteProjects(projectNames);
	}
}
/*
 * Ensures that a cycle is detected if introduced during a post-change event.
 * (regression test for bug 113051 No classpath marker produced when cycle through PDE container)
 */
public void testCycleDetection4() throws CoreException {
	IResourceChangeListener listener = new IResourceChangeListener() {
		boolean containerNeedUpdate = true;
		public void resourceChanged(IResourceChangeEvent event) {
			if (containerNeedUpdate) {
				TestContainer container = new TestContainer(
					new Path("org.eclipse.wst.jsdt.core.tests.model.container/default"), 
					new IIncludePathEntry[] { JavaScriptCore.newProjectEntry(new Path("/P1")) });
				try {
					JavaScriptCore.setJsGlobalScopeContainer(container.getPath(), new IJavaScriptProject[] {getJavaProject("P2")}, new IJsGlobalScopeContainer[] {container}, null);
				} catch (JavaScriptModelException e) {
					e.printStackTrace();
				}
				containerNeedUpdate = false;
			}
		}
	};
	try {
		IJavaScriptProject p1 = createJavaProject("P1", new String[] {}, new String[] {}, new String[] {"/P2"});
		TestContainer container = new TestContainer(
			new Path("org.eclipse.wst.jsdt.core.tests.model.container/default"), 
			new IIncludePathEntry[] {});
		IJavaScriptProject p2 = createJavaProject("P2", new String[] {}, new String[] {container.getPath().toString()});
		JavaScriptCore.setJsGlobalScopeContainer(container.getPath(), new IJavaScriptProject[] {p2}, new IJsGlobalScopeContainer[] {container}, null);
		waitForAutoBuild();
		getWorkspace().addResourceChangeListener(listener, IResourceChangeEvent.POST_CHANGE);
		createFile("/P1/test.txt", "");
		assertCycleMarkers(p1, new IJavaScriptProject[] {p1, p2}, new int[] {1, 1});
	} finally {
		getWorkspace().removeResourceChangeListener(listener);
		deleteProjects(new String[] {"P1", "P2"});
	}
}
public void testPerfDenseCycleDetection1() throws CoreException {
	// each project prereqs all other projects
	denseCycleDetection(5);
}
public void testPerfDenseCycleDetection2() throws CoreException {
	// each project prereqs all other projects
	denseCycleDetection(10);
}
public void testPerfDenseCycleDetection3() throws CoreException {
	// each project prereqs all other projects
	denseCycleDetection(20);
}
/*
 * Create projects and set classpaths in one batch
 */
public void testNoCycleDetection1() throws CoreException {

	// each project prereqs all the previous ones
	noCycleDetection(5, false, false);
	noCycleDetection(10, false, false);
	noCycleDetection(20, false, false);

	// each project prereqs all the next ones
	noCycleDetection(5, true, false);
	noCycleDetection(10, true, false);
	noCycleDetection(20, true, false);
}
/*
 * Create projects first, then set classpaths
 */
public void testNoCycleDetection2() throws CoreException {

	// each project prereqs all the previous ones
	noCycleDetection(5, false, true);
	noCycleDetection(10, false, true);
	noCycleDetection(20, false, true);

	// each project prereqs all the next ones
	noCycleDetection(5, true, true);
	noCycleDetection(10, true, true);
	noCycleDetection(20, true, true);
}
/*
 * Ensures that the .classpath file is not written to disk when setting the raw classpath with no resource change.
 */
public void testNoResourceChange01() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src1"});
		IIncludePathEntry[] newClasspath = createClasspath("P", new String[] {"/P/src2", ""});
		project.setRawIncludepath(newClasspath, false/*cannot modify resources*/, null/*no progress*/);
		String contents = new String (org.eclipse.wst.jsdt.internal.core.util.Util.getResourceContentsAsCharArray(getFile("/P/.settings/.jsdtscope")));
		assertSourceEquals(
			"Unexpected content", 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"	<classpathentry kind=\"src\" path=\"src1\"/>\n" + 
			"   <classpathentry kind=\"con\" path=\"org.eclipse.wst.jsdt.launching.JRE_CONTAINER\"/>\n" +
			"	<classpathentry kind=\"output\" path=\"\"/>\n" + 
			"</classpath>\n",
			contents);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that the in-memory classpath is correct when setting the raw classpath with no resource change.
 */
public void testNoResourceChange02() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src1"});
		IIncludePathEntry[] newClasspath = createClasspath("P", new String[] {"/P/src2", ""});
		project.setRawIncludepath(newClasspath, false/*cannot modify resources*/, null/*no progress*/);
		assertClasspathEquals(
			project.getRawIncludepath(),
			"/P/src2[CPE_SOURCE][K_SOURCE][isExported:false]"
		);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a compilation unit on the old classpath doesn't exist after setting a new raw classpath with no resource change.
 */
public void testNoResourceChange03() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src1"});
		createFile(
			"/P/src1/X.js",
			"public class X {\n" +
			"}"
		);
		IJavaScriptUnit cu = getCompilationUnit("/P/src1/X.js");
		cu.open(null);
		IIncludePathEntry[] newClasspath = createClasspath("P", new String[] {"/P/src2", ""});
		project.setRawIncludepath(newClasspath, false/*cannot modify resources*/, null/*no progress*/);
		assertFalse("Compilation unit should not exist", cu.exists());
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that the delta is correct when setting a new raw classpath with no resource change.
 */
public void testNoResourceChange04() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src1"});
		createFolder("/P/src2");
		IIncludePathEntry[] newClasspath = createClasspath("P", new String[] {"/P/src2", ""});
		startDeltas();
		project.setRawIncludepath(newClasspath, false/*cannot modify resources*/, null/*no progress*/);
		assertDeltas(
			"Unexpected delta",
			"P[*]: {CHILDREN | CLASSPATH CHANGED}\n" + 
			"	src1[*]: {REMOVED FROM CLASSPATH}\n" + 
			"	src2[*]: {ADDED TO CLASSPATH}"
		);
	} finally {
		stopDeltas();
		deleteProject("P");
	}
}
/*
 * Ensures that the in-memory output is correct when setting the output with no resource change.
 */
public void testNoResourceChange05() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src1"});
		project.setRawIncludepath(project.getRawIncludepath(), false/*cannot modify resources*/, null/*no progress*/);
		IIncludePathEntry[] entries = project.readRawIncludepath(); // force to read classpath
		assertEquals(
			"/P/src2",
			entries[0].getPath().toString());
	} finally {
		deleteProject("P");
	}
}
/**
 * Ensures that a duplicate entry created by editing the .classpath is detected.
 * (regression test for bug 24498 Duplicate entries on classpath cause CP marker to no longer refresh)
 */
public void testDuplicateEntries() throws CoreException {
	try {
		IJavaScriptProject project = this.createJavaProject("P", new String[] {"src"});
		this.editFile(
			"/P/.settings/.jsdtscope",
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<classpath>\n" +
			"    <classpathentry kind=\"src\" path=\"src\"/>\n" +
			"    <classpathentry kind=\"src\" path=\"src\"/>\n" +
			"    <classpathentry kind=\"output\" path=\"bin\"/>\n" +
			"</classpath>"
		);
		assertMarkers(
			"Unexpected markers",
			"Build path contains duplicate entry: \'src\' for project P",
			project);
	} finally {
		this.deleteProject("P");
	}
}
private void denseCycleDetection(final int numberOfParticipants) throws CoreException {
	
	final IJavaScriptProject[] projects = new IJavaScriptProject[numberOfParticipants];
	final String[] projectNames  = new String[numberOfParticipants];
	final int[] allProjectsInCycle = new int[numberOfParticipants];

	try {
		JavaScriptCore.run(new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				for (int i = 0; i < numberOfParticipants; i++){
					projectNames[i] = "P"+i;
					projects[i] = createJavaProject(projectNames[i], new String[]{""});
					allProjectsInCycle[i] = 1;
				}		
				for (int i = 0; i < numberOfParticipants; i++){
					IIncludePathEntry[] extraEntries = new IIncludePathEntry[numberOfParticipants-1];
					int index = 0;
					for (int j = 0; j < numberOfParticipants; j++){
						if (i == j) continue;
						extraEntries[index++] = JavaScriptCore.newProjectEntry(projects[j].getPath());
					}
					// append project references			
					IIncludePathEntry[] oldClasspath = projects[i].getRawIncludepath();
					IIncludePathEntry[] newClasspath = new IIncludePathEntry[oldClasspath.length+extraEntries.length];
					System.arraycopy(oldClasspath, 0 , newClasspath, 0, oldClasspath.length);
					for (int j = 0; j < extraEntries.length; j++){
						newClasspath[oldClasspath.length+j] = extraEntries[j];
					}			
					// set classpath
					projects[i].setRawIncludepath(newClasspath, null);
				}
			}
		}, 
		null);
		
		for (int i = 0; i < numberOfParticipants; i++){
			// check cycle markers
			this.assertCycleMarkers(projects[i], projects, allProjectsInCycle);
		}
		
	} finally {
		this.deleteProjects(projectNames);
	}
}
/*
 * When using forward references, all projects prereq all of the ones which have a greater index, 
 * e.g. P0, P1, P2:  P0 prereqs {P1, P2}, P1 prereqs {P2}, P2 prereqs {}
 * When no using forward references (i.e. backward refs), all projects prereq projects with smaller index
 * e.g. P0, P1, P2:  P0 prereqs {}, P1 prereqs {P0}, P2 prereqs {P0, P1}
 */
private void noCycleDetection(final int numberOfParticipants, final boolean useForwardReferences, final boolean createProjectsFirst) throws CoreException {
	
	final IJavaScriptProject[] projects = new IJavaScriptProject[numberOfParticipants];
	final String[] projectNames  = new String[numberOfParticipants];
	final int[] allProjectsInCycle = new int[numberOfParticipants];
	
	final long[] start = new long[1];
	final long[] time = new long[1];
	
	try {
		if (createProjectsFirst) {
			JavaScriptCore.run(new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					for (int i = 0; i < numberOfParticipants; i++){
						projectNames[i] = "P"+i;
						projects[i] = createJavaProject(projectNames[i], new String[]{""});
						allProjectsInCycle[i] = 0;
					}
				}
			},
			null);
		}
		JavaScriptCore.run(new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				if (!createProjectsFirst) {
					for (int i = 0; i < numberOfParticipants; i++){
						projectNames[i] = "P"+i;
						projects[i] = createJavaProject(projectNames[i], new String[]{""});
						allProjectsInCycle[i] = 0;
					}		
				}
				for (int i = 0; i < numberOfParticipants; i++){
					IIncludePathEntry[] extraEntries = new IIncludePathEntry[useForwardReferences ? numberOfParticipants - i -1 : i];
					int index = 0;
					for (int j = useForwardReferences ? i+1 : 0; 
						useForwardReferences ? (j < numberOfParticipants) : (j < i); 
						j++){
						extraEntries[index++] = JavaScriptCore.newProjectEntry(projects[j].getPath());
					}
					// append project references			
					IIncludePathEntry[] oldClasspath = projects[i].getRawIncludepath();
					IIncludePathEntry[] newClasspath = new IIncludePathEntry[oldClasspath.length+extraEntries.length];
					System.arraycopy(oldClasspath, 0 , newClasspath, 0, oldClasspath.length);
					for (int j = 0; j < extraEntries.length; j++){
						newClasspath[oldClasspath.length+j] = extraEntries[j];
					}			
					// set classpath
					long innerStart = System.currentTimeMillis(); // time spent in individual CP setting
					projects[i].setRawIncludepath(newClasspath, null);
					time[0] += System.currentTimeMillis() - innerStart;
				}
				start[0] = System.currentTimeMillis(); // time spent in delta refresh
			}
		}, 
		null);
		time[0] += System.currentTimeMillis()-start[0];
		//System.out.println("No cycle check ("+numberOfParticipants+" participants) : "+ time[0]+" ms, "+ (useForwardReferences ? "forward references" : "backward references") + ", " + (createProjectsFirst ? "two steps (projects created first, then classpaths are set)" : "one step (projects created and classpaths set in one batch)"));
		
		for (int i = 0; i < numberOfParticipants; i++){
			// check cycle markers
			this.assertCycleMarkers(projects[i], projects, allProjectsInCycle);
		}
		
	} finally {
		this.deleteProjects(projectNames);
	}
}

/*
 * test for bug 32690
 * simulate checkout of project with invalid classpath
 */
public void testNestedSourceFolders() throws CoreException, IOException {
	try {
		// create project using Platform/Resources API
		final IProject project = getProject("P");
		IWorkspaceRunnable create = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				project.create(null, null);
				project.open(null);
			}
		};
		getWorkspace().run(create, null);
		
		// create folders src and src/src2 using java.io API
		File pro = project.getLocation().toFile();
		File src = createFolder(pro, "src");
		createFolder(src, "src2");
		
		// create .project using java.io API
		createFile(pro, ".project", 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<projectDescription>\n" +
			"	<name>org.eclipse.wst.jsdt.core</name>\n" +
			"	<comment></comment>\n" +
			"	<projects>\n" +
			"	</projects>\n" +
			"	<buildSpec>\n" +
			"		<buildCommand>\n" +
			"			<name>org.eclipse.wst.jsdt.core.javabuilder</name>\n" +
			"			<arguments>\n" +
			"			</arguments>\n" +
			"		</buildCommand>\n" +
			"	</buildSpec>\n" +
			"	<natures>\n" +
			"		<nature>org.eclipse.wst.jsdt.core.javanature</nature>\n" +
			"	</natures>\n" +
			"</projectDescription>");

		// create .classpath using java.io API
		createFile(pro, ".classpath",
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<classpath>\n" +
			"    <classpathentry kind=\"src\" path=\"src\"/>\n" +
			"    <classpathentry kind=\"src\" path=\"src/src2\"/>\n" +
			"    <classpathentry kind=\"output\" path=\"bin\"/>\n" +
			"</classpath>"
		);

		// refresh
		project.refreshLocal(IResource.DEPTH_INFINITE, null);
		
		assertMarkers(
			"Unexpected markers",
			"Cannot nest \'P/src/src2\' inside \'P/src\'. To enable the nesting exclude \'src2/\' from \'P/src\'",
			JavaScriptCore.create(project));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that no problems are reported for an optional source entry with no corresponding folder.
 */
public void testOptionalEntry1() throws CoreException {
	try {
		IJavaScriptProject javaProject = this.createJavaProject("A", new String[] {});
		IIncludePathAttribute attribute = JavaScriptCore.newIncludepathAttribute(IIncludePathAttribute.OPTIONAL, "true");
		IIncludePathEntry[] classpath = 
			new IIncludePathEntry[] {
				JavaScriptCore.newSourceEntry(new Path("/A/src"), new IPath[0], new IPath[0], new Path("/A/bin"), new IIncludePathAttribute[] {attribute})
			};
		javaProject.setRawIncludepath(classpath, null);
		assertMarkers(
			"Unexpected markers",
			"",
			javaProject);
	} finally {
		this.deleteProject("A");
	}
}
/*
 * Ensures that no problems are reported for an optional libary entry with no corresponding folder.
 */
public void testOptionalEntry2() throws CoreException {
	try {
		IJavaScriptProject javaProject = this.createJavaProject("A", new String[] {});
		IIncludePathAttribute attribute = JavaScriptCore.newIncludepathAttribute(IIncludePathAttribute.OPTIONAL, "true");
		IIncludePathEntry[] classpath = 
			new IIncludePathEntry[] {
				JavaScriptCore.newLibraryEntry(new Path("/A/lib"), null, null, null, new IIncludePathAttribute[] {attribute}, false)
			};
		javaProject.setRawIncludepath(classpath, null);
		assertMarkers(
			"Unexpected markers",
			"",
			javaProject);
	} finally {
		this.deleteProject("A");
	}
}
/*
 * Ensures that no problems are reported for an optional project entry with no corresponding project.
 */
public void testOptionalEntry3() throws CoreException {
	try {
		IJavaScriptProject javaProject = this.createJavaProject("A", new String[] {});
		IIncludePathAttribute attribute = JavaScriptCore.newIncludepathAttribute(IIncludePathAttribute.OPTIONAL, "true");
		IIncludePathEntry[] classpath = 
			new IIncludePathEntry[] {
				JavaScriptCore.newProjectEntry(new Path("/B"), null/*no access rules*/, false/*don't combine access rule*/, new IIncludePathAttribute[] {attribute}, false/*not exported*/)
			};
		javaProject.setRawIncludepath(classpath, null);
		assertMarkers(
			"Unexpected markers",
			"",
			javaProject);
	} finally {
		this.deleteProject("A");
	}
}

/**
 * Ensure classpath is refreshed when project is replaced (43670)
 */
public void testReplaceProject() throws CoreException {
	try {
		final IJavaScriptProject javaProject = createJavaProject("P", new String[] {"src"});

		ResourcesPlugin.getWorkspace().run(
			new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					IProjectDescription descr = javaProject.getProject().getDescription();
					descr.setComment("dummy"); // ensure it is changed
					javaProject.getProject().setDescription(descr, monitor);
					editFile(
						"/P/.settings/.jsdtscope",
						"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
						"<classpath>\n" +
						"    <classpathentry kind=\"src\" path=\"src2\"/>\n" +
						"</classpath>"
					);
				}
			},
			null);
		IIncludePathEntry[] classpath = javaProject.getRawIncludepath();
		assertEquals("classpath should have been refreshed", new Path("/P/src2"), classpath[0].getPath());
	} finally {
		deleteProject("P");
	}
}

/*
 * Ensures that unknown classpath attributes in a .classpath file are not lost when read and rewritten.
 * (regression test for bug 101425 Classpath persistence should be resilient with unknown attributes)
 */
public void testUnknownAttributes() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		editFile(
			"/P/.settings/.jsdtscope",
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"	<classpathentry unknown=\"test\" kind=\"src\" path=\"src1\"/>\n" + 
			"	<classpathentry kind=\"src\" path=\"src2\"/>\n" + 
			"	<classpathentry kind=\"output\" path=\"bin\"/>\n" + 
			"</classpath>\n"
		);
		IIncludePathEntry[] classpath = project.getRawIncludepath();
		
		// swap 2 entries
		IIncludePathEntry src1 = classpath[0];
		classpath[0] = classpath[1];
		classpath[1] = src1;
		project.setRawIncludepath(classpath, null);
		
		// check that .classpath has correct content
		String contents = new String (org.eclipse.wst.jsdt.internal.core.util.Util.getResourceContentsAsCharArray(getFile("/P/.settings/.jsdtscope")));
		assertSourceEquals(
			"Unexpected content", 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"	<classpathentry kind=\"src\" path=\"src2\"/>\n" + 
			"	<classpathentry kind=\"src\" path=\"src1\" unknown=\"test\"/>\n" + 
			"	<classpathentry kind=\"output\" path=\"bin\"/>\n" + 
			"</classpath>\n",
			contents);		
	} finally {
		deleteProject("P");
	}
}

/*
 * Ensures that unknown classpath elements in a .classpath file are not lost when read and rewritten.
 * (regression test for bug 101425 Classpath persistence should be resilient with unknown attributes)
 */
public void testUnknownElements1() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		editFile(
			"/P/.settings/.jsdtscope",
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"	<classpathentry kind=\"src\" path=\"src1\">\n" + 
			"		<unknown>\n" +
			"			<test kind=\"\"/>\n" +
			"		</unknown>\n" +
			"	</classpathentry>\n" +
			"	<classpathentry kind=\"src\" path=\"src2\"/>\n" + 
			"	<classpathentry kind=\"output\" path=\"bin\"/>\n" + 
			"</classpath>\n"
		);
		IIncludePathEntry[] classpath = project.getRawIncludepath();
		
		// swap 2 entries
		IIncludePathEntry src1 = classpath[0];
		classpath[0] = classpath[1];
		classpath[1] = src1;
		project.setRawIncludepath(classpath, null);
		
		// check that .classpath has correct content
		String contents = new String (org.eclipse.wst.jsdt.internal.core.util.Util.getResourceContentsAsCharArray(getFile("/P/.settings/.jsdtscope")));
		assertSourceEquals(
			"Unexpected content", 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"	<classpathentry kind=\"src\" path=\"src2\"/>\n" + 
			"	<classpathentry kind=\"src\" path=\"src1\">\n" + 
			"		<unknown>\n" +
			"			<test kind=\"\"/>\n" +
			"		</unknown>\n" +
			"	</classpathentry>\n" +
			"	<classpathentry kind=\"output\" path=\"bin\"/>\n" + 
			"</classpath>\n",			
			contents);		
	} finally {
		deleteProject("P");
	}
}

/*
 * Ensures that unknown classpath elements in a .classpath file are not lost when read and rewritten.
 * (regression test for bug 101425 Classpath persistence should be resilient with unknown attributes)
 */
public void testUnknownElements2() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		editFile(
			"/P/.settings/.jsdtscope",
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"	<classpathentry kind=\"src\" unknownattribute=\"abcde\" path=\"src1\">\n" + 
			"		<unknown1>\n" +
			"			<test kind=\"1\"/>\n" +
			"			<test kind=\"2\"/>\n" +
			"		</unknown1>\n" +
			"		<unknown2 attribute2=\"\">\n" +
			"			<test>\n" +
			"				<other a=\"b\"/>\n" +
			"			</test>\n" +
			"		</unknown2>\n" +
			"	</classpathentry>\n" +
			"	<classpathentry kind=\"src\" path=\"src2\"/>\n" + 
			"	<classpathentry kind=\"output\" path=\"bin\"/>\n" + 
			"</classpath>\n"
		);
		IIncludePathEntry[] classpath = project.getRawIncludepath();
		
		// swap 2 entries
		IIncludePathEntry src1 = classpath[0];
		classpath[0] = classpath[1];
		classpath[1] = src1;
		project.setRawIncludepath(classpath, null);
		
		// check that .classpath has correct content
		String contents = new String (org.eclipse.wst.jsdt.internal.core.util.Util.getResourceContentsAsCharArray(getFile("/P/.settings/.jsdtscope")));
		assertSourceEquals(
			"Unexpected content", 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<classpath>\n" + 
			"	<classpathentry kind=\"src\" path=\"src2\"/>\n" + 
			"	<classpathentry kind=\"src\" path=\"src1\" unknownattribute=\"abcde\">\n" + 
			"		<unknown1>\n" + 
			"			<test kind=\"1\"/>\n" + 
			"			<test kind=\"2\"/>\n" + 
			"		</unknown1>\n" + 
			"		<unknown2 attribute2=\"\">\n" + 
			"			<test>\n" + 
			"				<other a=\"b\"/>\n" + 
			"			</test>\n" + 
			"		</unknown2>\n" + 
			"	</classpathentry>\n" + 
			"	<classpathentry kind=\"output\" path=\"bin\"/>\n" + 
			"</classpath>\n",
			contents);		
	} finally {
		deleteProject("P");
	}
}

/**
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=55992
 * Check that Assert.AssertionFailedException exception is well catched
 * 	a) when verifying a classpath entry
 * 	b) when verifying whole classpath
 */
public void testBug55992a() throws CoreException {
	try {
		IJavaScriptProject proj =  this.createJavaProject("P", new String[] {});
	
		IPath path = getExternalJCLPath("");
		IPath sourceAttachmentPath = new Path("jclMin.zip");
		JavaScriptCore.setIncludepathVariables(
			new String[] {"TEST_LIB", "TEST_SRC"},
			new IPath[] {path, sourceAttachmentPath},
			null);

		ClasspathEntry cp = new ClasspathEntry(
			IPackageFragmentRoot.K_SOURCE,
			IIncludePathEntry.CPE_VARIABLE,
			new Path("TEST_LIB"),
			ClasspathEntry.INCLUDE_ALL, 
			ClasspathEntry.EXCLUDE_NONE, 
			new Path("TEST_SRC"),
			null,
			null, // specific output folder
			false,
			(IAccessRule[]) null,
			false,
			ClasspathEntry.NO_EXTRA_ATTRIBUTES);
		IJavaScriptModelStatus status = JavaScriptConventions.validateClasspathEntry(proj, cp, false);
		assertEquals(
			"assertion failed: Source attachment path \'jclMin.zip\' for IIncludePathEntry must be absolute",
			status.getMessage());
	} finally {
		this.deleteProject("P");
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=61214
 */
public void testRemoveDuplicates() throws CoreException {
	try {
		IJavaScriptProject p1 = this.createJavaProject("P1", new String[] {""});
		IIncludePathEntry[] p1ClasspathEntries = new IIncludePathEntry[]{JavaScriptCore.newLibraryEntry(getExternalJCLPath(""), null, null, true)};
		setClasspath(p1, p1ClasspathEntries);
		IJavaScriptProject  p2 = this.createJavaProject("P2", new String[] {""});
		IIncludePathEntry[] p2ClasspathEntries = new IIncludePathEntry[]{
				JavaScriptCore.newProjectEntry(new Path("/P1")),
				JavaScriptCore.newLibraryEntry(getExternalJCLPath(""), null, null, false)
		};
		setClasspath(p2, p2ClasspathEntries);
	
		IIncludePathEntry[] classpath = ((JavaProject)p2).getExpandedClasspath();
		assertEquals("Unexpected number of classpath entries", 2, classpath.length);
		assertEquals("Unexpected first entry", "/P1", classpath[0].getPath().toString());
		assertEquals("Unexpected second entry", getSystemJsPathString(), classpath[1].getPath().toOSString());
	} finally {
		this.deleteProjects(new String[] {"P1", "P2"});
	}
}
}
