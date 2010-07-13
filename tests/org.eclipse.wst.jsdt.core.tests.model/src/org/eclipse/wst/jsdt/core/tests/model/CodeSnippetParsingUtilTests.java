/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.core.tests.model;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.IPackageFragmentRoot;
import org.eclipse.wst.jsdt.core.IProblemRequestor;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.LibrarySuperType;
import org.eclipse.wst.jsdt.core.WorkingCopyOwner;
import org.eclipse.wst.jsdt.core.compiler.IProblem;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.internal.core.JavaProject;
import org.eclipse.wst.jsdt.launching.JavaRuntime;

public class CodeSnippetParsingUtilTests extends TestCase {
	public static Test suite() {
		return new TestSuite(CodeSnippetParsingUtilTests.class);
	}

	/**
	 * 
	 */
	public CodeSnippetParsingUtilTests() {
	}

	/**
	 * @param name
	 */
	public CodeSnippetParsingUtilTests(String name) {
		super(name);
	}

	public void testErrorDetection() throws JavaScriptModelException, CoreException {
		String projectName = ".jsValidation";
		String fileName = "snippet.js";
		String contents = "var params = \"someBadString\".substring(1,2,3,4);\nparahnas.shift();";

		// Create the JavaScript project
		IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(projectName);
		description.setNatureIds(new String[]{JavaScriptCore.NATURE_ID});
		IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		iProject.create(description, new NullProgressMonitor());
		iProject.open(null);

		// Setup the JavaScript project
		IJavaScriptProject project = JavaScriptCore.create(iProject);
		project.setRawIncludepath(null, new NullProgressMonitor());
		LibrarySuperType superType = new LibrarySuperType(new Path(JavaRuntime.DEFAULT_SUPER_TYPE_LIBRARY), project, JavaRuntime.DEFAULT_SUPER_TYPE);
		((JavaProject) project).setCommonSuperType(superType);

		// Now create the IJavascriptUnit, and fill in its contents
		IPackageFragmentRoot root = project.getPackageFragmentRoot(iProject);
		IPackageFragment packageFragment = root.getPackageFragment(IPackageFragment.DEFAULT_PACKAGE_NAME);
		IJavaScriptUnit javaScriptUnit = packageFragment.getJavaScriptUnit(fileName);

		final List problems = new ArrayList();
		final IProblemRequestor requestor = new IProblemRequestor() {
			public void acceptProblem(IProblem problem) {
//				System.err.println(problem.getMessage());
				problems.add(problem);
			}

			public boolean isActive() {
				return true;
			}

			public void beginReporting() {
				problems.clear();
			}

			public void endReporting() {
			}
		};
		WorkingCopyOwner owner = new WorkingCopyOwner() {
			public IProblemRequestor getProblemRequestor(IJavaScriptUnit workingCopy) {
				return requestor;
			}
		};

		/*
		 * Using a "working copy" allows us to not have to create the file on
		 * disk
		 */
		IJavaScriptUnit workingCopy = javaScriptUnit.getWorkingCopy(owner, null);
		workingCopy.getBuffer().setContents(contents);

		/*
		 * Reconcile the working copy. Any problems found will be reported to
		 * the IProblemRequestor from the given WorkingCopyOwner.
		 */
		workingCopy.reconcile(AST.JLS3, true, true, owner, new NullProgressMonitor());

		// Clean up
		workingCopy.discardWorkingCopy();

		//FIXME need to enable semantic validation
		//assertFalse("no problems found", problems.isEmpty());
	}
}
