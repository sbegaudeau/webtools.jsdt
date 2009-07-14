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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.jsdt.core.compiler.IProblem;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.internal.core.JavaModelManager;
import org.eclipse.wst.jsdt.internal.core.search.indexing.IndexManager;
import org.eclipse.wst.jsdt.core.*;

public class ReconcilerStatementsRecoveryTests extends ModifyingResourceTests {
	
	protected IJavaScriptUnit workingCopy;
	protected ProblemRequestor problemRequestor;
	
	/* A problem requestor that auto-cancels on first problem */
	class CancelingProblemRequestor extends ProblemRequestor {
		IProgressMonitor progressMonitor = new IProgressMonitor() {
			boolean isCanceled = false;
			public void beginTask(String name, int totalWork) {}
			public void done() {}
			public void internalWorked(double work) {}
			public boolean isCanceled() {
				return this.isCanceled;
			}
			public void setCanceled(boolean value) {
				this.isCanceled = value;
			}
			public void setTaskName(String name) {}
			public void subTask(String name) {}
			public void worked(int work) {}
		};
	
		boolean isCanceling = false;
		public void acceptProblem(IProblem problem) {
			if (isCanceling) this.progressMonitor.setCanceled(true); // auto-cancel on first problem
			super.acceptProblem(problem);
		}		
	}

/**
 */
public ReconcilerStatementsRecoveryTests(String name) {
	super(name);
}
// Use this static initializer to specify subset for tests
// All specified tests which do not belong to the class are skipped...
static {
//	JavaModelManager.VERBOSE = true;
//	org.eclipse.wst.jsdt.internal.core.search.BasicSearchEngine.VERBOSE = true;
//	TESTS_PREFIX = "testIgnoreIfBetterNonAccessibleRule";
//	TESTS_NAMES = new String[] { "testExternal" };
//	TESTS_NUMBERS = new int[] { 118823 };
//	TESTS_RANGE = new int[] { 16, -1 };
}
public static Test suite() {
	return buildModelTestSuite(ReconcilerStatementsRecoveryTests.class);
}
protected void assertProblems(String message, String expected) {
	assertProblems(message, expected, this.problemRequestor);
}
// Expect no error as soon as indexing is finished
protected void assertNoProblem(char[] source, IJavaScriptUnit unit) throws InterruptedException, JavaScriptModelException {
	IndexManager indexManager = JavaModelManager.getJavaModelManager().getIndexManager();
	if (this.problemRequestor.problemCount > 0) {
		// If errors then wait for indexes to finish
		while (indexManager.awaitingJobsCount() > 0) {
			Thread.sleep(100);
		}
		// Reconcile again to see if error goes away
		this.problemRequestor.initialize(source);
		unit.getBuffer().setContents(source); // need to set contents again to be sure that following reconcile will be really done
		unit.reconcile(AST.JLS3,
			true, // force problem detection to see errors if any
			null,	// do not use working copy owner to not use working copies in name lookup
			null);
		if (this.problemRequestor.problemCount > 0) {
			assertEquals("Working copy should NOT have any problem!", "", this.problemRequestor.problems.toString());
		}
	}
}
protected void addClasspathEntries(IIncludePathEntry[] entries, boolean enableForbiddenReferences) throws JavaScriptModelException {
	IJavaScriptProject project = getJavaProject("Reconciler");
	IIncludePathEntry[] oldClasspath = project.getRawIncludepath();
	int oldLength = oldClasspath.length;
	int length = entries.length;
	IIncludePathEntry[] newClasspath = new IIncludePathEntry[oldLength+length];
	System.arraycopy(oldClasspath, 0, newClasspath, 0, oldLength);
	System.arraycopy(entries, 0, newClasspath, oldLength, length);
	project.setRawIncludepath(newClasspath, null);
	
	if (enableForbiddenReferences) {
		project.setOption(JavaScriptCore.COMPILER_PB_FORBIDDEN_REFERENCE, JavaScriptCore.ERROR);
	}
}
protected void removeClasspathEntries(IIncludePathEntry[] entries) throws JavaScriptModelException {
	IJavaScriptProject project = getJavaProject("Reconciler");
	IIncludePathEntry[] oldClasspath = project.getRawIncludepath();
	int oldLength = oldClasspath.length;
	int length = entries.length;
	IIncludePathEntry[] newClasspath = new IIncludePathEntry[oldLength-length];
	System.arraycopy(oldClasspath, 0, newClasspath, 0, oldLength-length);
	project.setRawIncludepath(newClasspath, null);
}
/**
 * Setup for the next test.
 */
public void setUp() throws Exception {
	super.setUp();
	this.problemRequestor =  new ProblemRequestor();
	this.workingCopy = getCompilationUnit("Reconciler/src/p1/X.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
	this.problemRequestor.initialize(this.workingCopy.getSource().toCharArray());
	startDeltas();
}
public void setUpSuite() throws Exception {
	super.setUpSuite();

	// Create project with 1.4 compliance
	IJavaScriptProject project14 = createJavaProject("Reconciler", new String[] {"src"}, new String[] {"JCL_LIB"});
	createFolder("/Reconciler/src/p1");
	createFolder("/Reconciler/src/p2");
	createFile(
		"/Reconciler/src/p1/X.js", 
		"  function foo() {\n" +
		"}"
	);
	project14.setOption(JavaScriptCore.COMPILER_PB_UNUSED_LOCAL, JavaScriptCore.IGNORE);
	project14.setOption(JavaScriptCore.COMPILER_PB_INVALID_JAVADOC, JavaScriptCore.WARNING);

	// Create project with 1.5 compliance
	IJavaScriptProject project15 = createJavaProject("Reconciler15", new String[] {"src"}, new String[] {"JCL15_LIB"}, "bin", "1.5");
	addLibrary(
		project15, 
		"lib15.jar", 
		"lib15src.zip", 
		new String[] {
			"java/util/List.js",
			"package java.util;\n" +
			"public class List<T> {\n" +
			"}",
			"java/util/Stack.js",
			"package java.util;\n" +
			"public class Stack<T> {\n" +
			"}",
			"java/util/Map.js",
			"package java.util;\n" +
			"public interface Map<K,V> {\n" +
			"}",
			"java/lang/annotation/Annotation.js",
			"package java.lang.annotation;\n" +
			"public interface Annotation {\n" +
			"}",
			"java/lang/Deprecated.js",
			"package java.lang;\n" +
			"public @interface Deprecated {\n" +
			"}",
			"java/lang/SuppressWarnings.js",
			"package java.lang;\n" +
			"public @interface SuppressWarnings {\n" +
			"   String[] value();\n" +
			"}"
		}, 
		JavaScriptCore.VERSION_1_5
	);
	project15.setOption(JavaScriptCore.COMPILER_PB_UNUSED_LOCAL, JavaScriptCore.IGNORE);
}
//private void setUp15WorkingCopy() throws JavaScriptModelException {
//	setUp15WorkingCopy("Reconciler15/src/p1/X.js", new WorkingCopyOwner() {});
//}
//private void setUp15WorkingCopy(String path, WorkingCopyOwner owner) throws JavaScriptModelException {
//	String contents = this.workingCopy.getSource();
//	setUpWorkingCopy(path, contents, owner);
//}
//private void setUpWorkingCopy(String path, String contents) throws JavaScriptModelException {
//	setUpWorkingCopy(path, contents, new WorkingCopyOwner() {});
//}
//private void setUpWorkingCopy(String path, String contents, WorkingCopyOwner owner) throws JavaScriptModelException {
//	this.workingCopy.discardWorkingCopy();
//	this.workingCopy = getCompilationUnit(path).getWorkingCopy(owner, this.problemRequestor, null);
//	setWorkingCopyContents(contents);
//	this.workingCopy.makeConsistent(null);
//}
void setWorkingCopyContents(String contents) throws JavaScriptModelException {
	this.workingCopy.getBuffer().setContents(contents);
	this.problemRequestor.initialize(contents.toCharArray());
}
/**
 * Cleanup after the previous test.
 */
public void tearDown() throws Exception {
	TestvalidationParticipant.PARTICIPANT = null;
	if (this.workingCopy != null) {
		this.workingCopy.discardWorkingCopy();
	}
	stopDeltas();
	super.tearDown();
}
public void tearDownSuite() throws Exception {
	deleteProject("Reconciler");
	deleteProject("Reconciler15");
	super.tearDownSuite();
}
/*
 * No ast and no statements recovery
 */
public void testStatementsRecovery01() throws CoreException {
	// Introduce syntax error
	setWorkingCopyContents(
		"package p1;\n" +
		"import p2.*;\n" +
		"public class X {\n" +
		"  public void foo() {\n" +
		"     UnknownType name\n" +
		"  }\n" +
		"}");
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, false, null, null);
	assertDeltas(
		"Unexpected delta after syntax error", 
		"[Working copy] X.js[*]: {CONTENT | FINE GRAINED}"
	);
	assertProblems(
		"Unexpected problems",
		"----------\n" + 
		"1. ERROR in /Reconciler/src/p1/X.js (at line 5)\n" + 
		"	UnknownType name\n" + 
		"	            ^^^^\n" + 
		"Syntax error, insert \";\" to complete BlockStatements\n" + 
		"----------\n"
	);
	
	clearDeltas();
}
/*
 * Ast and no statements recovery
 */
public void testStatementsRecovery02() throws CoreException {
	// Introduce syntax error
	setWorkingCopyContents(
		"package p1;\n" +
		"import p2.*;\n" +
		"public class X {\n" +
		"  public void foo() {\n" +
		"     UnknownType name\n" +
		"  }\n" +
		"}");
	this.workingCopy.reconcile(AST.JLS3, false, false, null, null);
	assertDeltas(
		"Unexpected delta after syntax error", 
		"[Working copy] X.js[*]: {CONTENT | FINE GRAINED | AST AFFECTED}"
	);
	assertProblems(
		"Unexpected problems",
		"----------\n" + 
		"1. ERROR in /Reconciler/src/p1/X.js (at line 5)\n" + 
		"	UnknownType name\n" + 
		"	            ^^^^\n" + 
		"Syntax error, insert \";\" to complete BlockStatements\n" + 
		"----------\n"
	);
	
	clearDeltas();
}
/*
 * No ast, statements recovery
 */
public void testStatementsRecovery03() throws CoreException {
	// Introduce syntax error
	setWorkingCopyContents(
		"package p1;\n" +
		"import p2.*;\n" +
		"public class X {\n" +
		"  public void foo() {\n" +
		"     UnknownType name\n" +
		"  }\n" +
		"}");
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, true, null, null);
	assertDeltas(
		"Unexpected delta after syntax error", 
		"[Working copy] X.js[*]: {CONTENT | FINE GRAINED}"
	);
	assertProblems(
		"Unexpected problems",
		"----------\n" + 
		"1. ERROR in /Reconciler/src/p1/X.js (at line 5)\n" + 
		"	UnknownType name\n" + 
		"	^^^^^^^^^^^\n" + 
		"UnknownType cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in /Reconciler/src/p1/X.js (at line 5)\n" + 
		"	UnknownType name\n" + 
		"	            ^^^^\n" + 
		"Syntax error, insert \";\" to complete BlockStatements\n" + 
		"----------\n"
	);
	
	clearDeltas();
}
/*
 * Ast, statements recovery
 */
public void testStatementsRecovery04() throws CoreException {
	// Introduce syntax error
	setWorkingCopyContents(
		"package p1;\n" +
		"import p2.*;\n" +
		"public class X {\n" +
		"  public void foo() {\n" +
		"     UnknownType name\n" +
		"  }\n" +
		"}");
	this.workingCopy.reconcile(AST.JLS3, false, true, null, null);
	assertDeltas(
		"Unexpected delta after syntax error", 
		"[Working copy] X.js[*]: {CONTENT | FINE GRAINED | AST AFFECTED}"
	);
	assertProblems(
		"Unexpected problems",
		"----------\n" + 
		"1. ERROR in /Reconciler/src/p1/X.js (at line 5)\n" + 
		"	UnknownType name\n" + 
		"	^^^^^^^^^^^\n" + 
		"UnknownType cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in /Reconciler/src/p1/X.js (at line 5)\n" + 
		"	UnknownType name\n" + 
		"	            ^^^^\n" + 
		"Syntax error, insert \";\" to complete BlockStatements\n" + 
		"----------\n"
	);
	
	clearDeltas();
}
}
