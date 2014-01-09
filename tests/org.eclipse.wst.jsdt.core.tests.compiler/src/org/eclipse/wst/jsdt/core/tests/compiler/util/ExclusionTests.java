/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler.util;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.jsdt.internal.core.util.Util;

public class ExclusionTests extends TestCase {
	
	/** name of the test project */
	private static final String PROJECT_NAME = "ExclusionTests";
	
	/** the test project */
	private static IProject fProject;
	
	/**
	 * <p>Default constructor<p>
	 * <p>Use {@link #suite()}</p>
	 * 
	 * @see #suite()
	 */
	public ExclusionTests() {
		super("Exclusion Tests");
	}
	
	/**
	 * <p>Constructor that takes a test name.</p>
	 * <p>Use {@link #suite()}</p>
	 * 
	 * @param name The name this test run should have.
	 * 
	 * @see #suite()
	 */
	public ExclusionTests(String name) {
		super(name);
	}
	
	/**
	 * <p>Use this method to add these tests to a larger test suite so set up
	 * and tear down can be performed</p>
	 * 
	 * @return a {@link TestSetup} that will run all of the tests in this class
	 * with set up and tear down.
	 */
	public static Test suite() {
		TestSuite ts = new TestSuite(ExclusionTests.class, "Exclusion Tests");
		return new ExclusionTestsSetup(ts);
	}
	
	public void testExcluded_DirPathWorkspaceRelative_DirExcludedWorkspaceRelative() {
		IPath resourcePath = new Path(PROJECT_NAME + "/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_DirPathWorkspaceRelativeLeadingSeperator_DirExcludedWorkspaceRelative() {
		IPath resourcePath = new Path(IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_DirPathWorkspaceRelative_DirExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = new Path(PROJECT_NAME + "/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_DirPathWorkspaceRelativeLeadingSeperator_DirExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = new Path(IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathWorkspaceRelative_DirExcludedWorkspaceRelative() {
		IPath resourcePath = new Path("foo/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathWorkspaceRelativeLeadingSeperator_DirExcludedWorkspaceRelative() {
		IPath resourcePath = new Path(IPath.SEPARATOR + "foo/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathWorkspaceRelative_DirExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = new Path("foo/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathWorkspaceRelativeLeadingSeperator_DirExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = new Path(IPath.SEPARATOR + "foo/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_FilePathWorkspaceRelative_DirExcludedWorkspaceRelative() {
		IPath resourcePath = new Path(PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_FilePathWorkspaceRelativeLeadingSeperator_DirExcludedWorkspaceRelative() {
		IPath resourcePath = new Path(IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_FilePathWorkspaceRelative_DirExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = new Path(PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_FilePathWorkspaceRelativeLeadingSeperator_DirExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = new Path(IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_FilePathWorkspaceRelative_DirExcludedWorkspaceRelative() {
		IPath resourcePath = new Path("foo/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_FilePathWorkspaceRelativeLeadingSeperator_DirExcludedWorkspaceRelative() {
		IPath resourcePath = new Path(IPath.SEPARATOR + "foo/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_FilePathWorkspaceRelative_DirExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = new Path("foo/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_FilePathWorkspaceRelativeLeadingSeperator_DirExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = new Path(IPath.SEPARATOR + "foo/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_DirPathAbsolute_DirExcludedWorkspaceRelative() {
		IPath resourcePath = fProject.getLocation().append("/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_DirPathAbsolute_DirExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = fProject.getLocation().append("/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathAbsolute_DirExcludedWorkspaceRelative() {
		IPath resourcePath = fProject.getLocation().append("foo/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathAbsolute_DirExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = fProject.getLocation().append("foo/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_FilePathAbsolute_DirExcludedWorkspaceRelative() {
		IPath resourcePath = fProject.getLocation().append("/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_FilePathAbsolute_DirExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = fProject.getLocation().append("/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_FilePathAbsolute_DirExcludedWorkspaceRelative() {
		IPath resourcePath = fProject.getLocation().append("foo/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_FilePathAbsolute_DirExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = fProject.getLocation().append("foo/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}

	public void testExcluded_DirPathAbsolute_DirExcludedAbsolute() {
		IPath resourcePath = fProject.getLocation().append("/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		
		exclusionPatterns[0] = fProject.getLocation().append("/WebContent/").toPortableString().toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathAbsolute_DirExcludedAbsolute() {
		IPath resourcePath = fProject.getLocation().append("foo/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = fProject.getLocation().append("/WebContent/").toPortableString().toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_FilePathAbsolute_DirExcludedAbsolute() {
		IPath resourcePath = fProject.getLocation().append("/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = fProject.getLocation().append("/WebContent/").toPortableString().toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_FilePathAbsolute_DirExcludedAbsolute() {
		IPath resourcePath = fProject.getLocation().append("foo/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = fProject.getLocation().append("/WebContent/").toPortableString().toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}

	public void testNotExcluded_DirPathWorkspaceRelative_FileExcludedWorkspaceRelative() {
		IPath resourcePath = new Path(PROJECT_NAME + "/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathWorkspaceRelative_FileExcludedWorkspaceRelative_2() {
		IPath resourcePath = new Path("foo/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathWorkspaceRelativeLeadingSeperator_FileExcludedWorkspaceRelative() {
		IPath resourcePath = new Path(IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathWorkspaceRelativeLeadingSeperator_FileExcludedWorkspaceRelative_2() {
		IPath resourcePath = new Path(IPath.SEPARATOR + "foo/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathWorkspaceRelative_FileExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = new Path(PROJECT_NAME + "/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathWorkspaceRelative_FileExcludedWorkspaceRelativeLeadingSeperator_2() {
		IPath resourcePath = new Path("foo/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathWorkspaceRelativeLeadingSeperator_FileExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = new Path(IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathWorkspaceRelativeLeadingSeperator_FileExcludedWorkspaceRelativeLeadingSeperator_2() {
		IPath resourcePath = new Path(IPath.SEPARATOR + "foo/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_FilePathWorkspaceRelative_FileExcludedWorkspaceRelative() {
		IPath resourcePath = new Path(PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_FilePathWorkspaceRelativeLeadingSeperator_FileExcludedWorkspaceRelative() {
		IPath resourcePath = new Path(IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_FilePathWorkspaceRelative_FileExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = new Path(PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_FilePathWorkspaceRelativeLeadingSeperator_FileExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = new Path(IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_FilePathWorkspaceRelative_FileExcludedWorkspaceRelative() {
		IPath resourcePath = new Path("foo/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_FilePathWorkspaceRelativeLeadingSeperator_FileExcludedWorkspaceRelative() {
		IPath resourcePath = new Path(IPath.SEPARATOR + "foo/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_FilePathWorkspaceRelative_FileExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = new Path("foo/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_FilePathWorkspaceRelativeLeadingSeperator_FileExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = new Path(IPath.SEPARATOR + "foo/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathAbsolute_FileExcludedWorkspaceRelative() {
		IPath resourcePath = fProject.getLocation().append("/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathAbsolute_FileExcludedWorkspaceRelative_2() {
		IPath resourcePath = fProject.getLocation().append("foo/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathAbsolute_FileExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = fProject.getLocation().append("/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathAbsolute_FileExcludedWorkspaceRelativeLeadingSeperator_2() {
		IPath resourcePath = fProject.getLocation().append("foo/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_FilePathAbsolute_FileExcludedWorkspaceRelative() {
		IPath resourcePath = fProject.getLocation().append("/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_FilePathAbsolute_FileExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = fProject.getLocation().append("/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_FilePathAbsolute_FileExcludedWorkspaceRelative() {
		IPath resourcePath = fProject.getLocation().append("foo/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_FilePathAbsolute_FileExcludedWorkspaceRelativeLeadingSeperator() {
		IPath resourcePath = fProject.getLocation().append("foo/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (IPath.SEPARATOR + PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js").toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}

	public void testNotExcluded_DirPathAbsolute_FileExcludedAbsolute() {
		IPath resourcePath = fProject.getLocation().append("/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		
		exclusionPatterns[0] = fProject.getLocation().append("/WebContent/dojo/dijit/_Widget.js").toPortableString().toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_DirPathAbsolute_FileExcludedAbsolute_2() {
		IPath resourcePath = fProject.getLocation().append("foo/WebContent/dojo/dijit/");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = fProject.getLocation().append("/WebContent/dojo/dijit/_Widget.js").toPortableString().toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_FilePathAbsolute_FileExcludedAbsolute() {
		IPath resourcePath = fProject.getLocation().append("/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = fProject.getLocation().append("/WebContent/dojo/dijit/_Widget.js").toPortableString().toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testNotExcluded_FilePathAbsolute_FileExcludedAbsolute() {
		IPath resourcePath = fProject.getLocation().append("foo/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = fProject.getLocation().append("/WebContent/dojo/dijit/_Widget.js").toPortableString().toCharArray();
		
		assertFalse(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_DirPathWorkspaceRelative_DirExcludedWorkspaceRelativeStarWildCard() {
		IPath resourcePath = new Path(PROJECT_NAME + "/WebContent/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/*/dijit/").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	public void testExcluded_DirPathWorkspaceRelative_DirExcludedWorkspaceRelativeDoubleStarWildCard() {
		IPath resourcePath = new Path(PROJECT_NAME + "/WebContent/foo/dojo/dijit/_Widget.js");
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = new char[1][0];
		exclusionPatterns[0] = (PROJECT_NAME + "/WebContent/**/dijit/").toCharArray();
		
		assertTrue(Util.isExcluded(resourcePath, inclusionPatterns, exclusionPatterns));
	}
	
	/**
	 * <p>This inner class is used to do set up and tear down before and
	 * after (respectively) all tests in the inclosing class have run.</p>
	 */
	private static class ExclusionTestsSetup extends TestSetup {
		private static final String WTP_AUTOTEST_NONINTERACTIVE = "wtp.autotest.noninteractive";
		private static String previousWTPAutoTestNonInteractivePropValue = null;
		
		/**
		 * Default constructor
		 * 
		 * @param test do setup for the given test
		 */
		public ExclusionTestsSetup(Test test) {
			super(test);
		}

		/**
		 * <p>This is run once before all of the tests</p>
		 * 
		 * @see junit.extensions.TestSetup#setUp()
		 */
		public void setUp() throws Exception {
			//project setup
			fProject = createProject(PROJECT_NAME, null, null);
			
			//set non-interactive
			String noninteractive = System.getProperty(WTP_AUTOTEST_NONINTERACTIVE);
			if (noninteractive != null) {
				previousWTPAutoTestNonInteractivePropValue = noninteractive;
			} else {
				previousWTPAutoTestNonInteractivePropValue = "false";
			}
			System.setProperty(WTP_AUTOTEST_NONINTERACTIVE, "true");
		}

		/**
		 * <p>This is run once after all of the tests have been run</p>
		 * 
		 * @see junit.extensions.TestSetup#tearDown()
		 */
		public void tearDown() throws Exception {
			//reset non-interactive
			if (previousWTPAutoTestNonInteractivePropValue != null) {
				System.setProperty(WTP_AUTOTEST_NONINTERACTIVE, previousWTPAutoTestNonInteractivePropValue);
			}
		}
	}
	
	private static IProject createProject(String name, IPath location, String[] natureIds) {
		IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(name);
		if (location != null) {
			description.setLocation(location);
		}
		if (natureIds != null) {
			description.setNatureIds(natureIds);
		}
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
		try {
			project.create(description, new NullProgressMonitor());
			project.open(new NullProgressMonitor());
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
		return project;
	}
}
