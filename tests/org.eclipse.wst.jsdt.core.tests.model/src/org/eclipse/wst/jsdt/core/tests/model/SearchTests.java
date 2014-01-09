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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import junit.framework.Test;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.wst.jsdt.core.IField;
import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.IImportDeclaration;
import org.eclipse.wst.jsdt.core.IIncludePathEntry;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.ILocalVariable;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchConstants;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchScope;
import org.eclipse.wst.jsdt.core.search.SearchEngine;
import org.eclipse.wst.jsdt.core.search.SearchPattern;
import org.eclipse.wst.jsdt.core.search.TypeNameRequestor;
import org.eclipse.wst.jsdt.core.tests.model.Semaphore.TimeOutException;
import org.eclipse.wst.jsdt.core.tests.util.Util;
import org.eclipse.wst.jsdt.internal.core.JavaElement;
import org.eclipse.wst.jsdt.internal.core.JavaModelManager;
import org.eclipse.wst.jsdt.internal.core.LocalVariable;
import org.eclipse.wst.jsdt.internal.core.search.BasicSearchEngine;
import org.eclipse.wst.jsdt.internal.core.search.indexing.IndexManager;
import org.eclipse.wst.jsdt.internal.core.search.processing.IJob;

/*
 * Test indexing support.
 */
public class SearchTests extends ModifyingResourceTests implements IJavaScriptSearchConstants {
	/*
	 * Empty jar contents.
	 * Generated using the following code:
	 
	 	String filePath = "d:\\temp\\empty.jar";
		new JarOutputStream(new FileOutputStream(filePath), new Manifest()).close();
		byte[] contents = org.eclipse.wst.jsdt.internal.compiler.util.Util.getFileByteContent(new File(filePath));
		System.out.print("{");
		for (int i = 0, length = contents.length; i < length; i++) {
			System.out.print(contents[i]);
			System.out.print(", ");
		}
		System.out.print("}");
	 */
	static final byte[] EMPTY_JAR = {80, 75, 3, 4, 20, 0, 8, 0, 8, 0, 106, -100, 116, 46, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 4, 0, 77, 69, 84, 65, 45, 73, 78, 70, 47, 77, 65, 78, 73, 70, 69, 83, 84, 46, 77, 70, -2, -54, 0, 0, -29, -27, 2, 0, 80, 75, 7, 8, -84, -123, -94, 20, 4, 0, 0, 0, 2, 0, 0, 0, 80, 75, 1, 2, 20, 0, 20, 0, 8, 0, 8, 0, 106, -100, 116, 46, -84, -123, -94, 20, 4, 0, 0, 0, 2, 0, 0, 0, 20, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 77, 69, 84, 65, 45, 73, 78, 70, 47, 77, 65, 78, 73, 70, 69, 83, 84, 46, 77, 70, -2, -54, 0, 0, 80, 75, 5, 6, 0, 0, 0, 0, 1, 0, 1, 0, 70, 0, 0, 0, 74, 0, 0, 0, 0, 0, };
	class WaitUntilReadyMonitor implements IProgressMonitor {
		public Semaphore sem = new Semaphore();
		public void beginTask(String name, int totalWork) {
		}
		public void internalWorked(double work) {
		}
		public void done() {
		}
		public boolean isCanceled() {
			return false;
		}
		public void setCanceled(boolean value) {
		}
		public void setTaskName(String name) {
		}
		public void subTask(String name) {
			// concurrent job is signaling it is working
			this.sem.release();
		}
		public void worked(int work) {
		}
	}
	public static class SearchTypeNameRequestor extends TypeNameRequestor {
		Vector results = new Vector();
		public void acceptType(int modifiers, char[] packageName, char[] simpleTypeName, char[][] enclosingTypeNames, String path) {
			char[] typeName = 
				CharOperation.concat(
					CharOperation.concatWith(enclosingTypeNames, '$'), 
					simpleTypeName,
					'$');
			results.addElement(new String(CharOperation.concat(packageName, typeName, '.')));
		}
		public String toString(){
			int length = results.size();
			String[] strings = new String[length];
			results.toArray(strings);
			org.eclipse.wst.jsdt.internal.core.util.Util.sort(strings);
			StringBuffer buffer = new StringBuffer(100);
			for (int i = 0; i < length; i++){
				buffer.append(strings[i]);
				if (i != length-1) {
					buffer.append('\n');
				}
			}
			return buffer.toString();
		}
		public int size() {
			return this.results.size();
		}
	}
	class WaitingJob implements IJob {
		static final int MAX_WAIT = 30000; // wait 30s max
		Semaphore startingSem = new Semaphore();
		Semaphore runningSem = new Semaphore();
		public boolean belongsTo(String jobFamily) {
			return false;
		}
		public void cancel() {
		}
		public void ensureReadyToRun() {
		}
		public boolean execute(IProgressMonitor progress) {
			this.startingSem.release();
			try {
				this.runningSem.acquire(MAX_WAIT);
			} catch (TimeOutException e) {
				e.printStackTrace();
			}
			return true;
		}
	}
static {
//	TESTS_NAMES = new String[] { "testSearchPatternCreation25" };
}
public static Test suite() {
	return buildModelTestSuite(SearchTests.class);
}


public SearchTests(String name) {
	super(name);
}
protected void assertAllTypes(int waitingPolicy, IProgressMonitor progressMonitor, String expected) throws JavaScriptModelException {
	assertAllTypes("Unexpected all types", null/* no specific project*/, waitingPolicy, progressMonitor, expected);
}
protected void assertAllTypes(String expected) throws JavaScriptModelException {
	assertAllTypes(WAIT_UNTIL_READY_TO_SEARCH, null/* no progress monitor*/, expected);
}
protected void assertAllTypes(String message, IJavaScriptProject project, String expected) throws JavaScriptModelException {
	assertAllTypes(message, project, WAIT_UNTIL_READY_TO_SEARCH, null/* no progress monitor*/, expected);
}
protected void assertAllTypes(String message, IJavaScriptProject project, int waitingPolicy, IProgressMonitor progressMonitor, String expected) throws JavaScriptModelException {
	IJavaScriptSearchScope scope =
		project == null ?
			SearchEngine.createWorkspaceScope() :
			SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
	SearchEngine searchEngine = new SearchEngine();
	SearchTypeNameRequestor requestor = new SearchTypeNameRequestor();
	searchEngine.searchAllTypeNames(
		null,
		SearchPattern.R_EXACT_MATCH,
		null,
		SearchPattern.R_PATTERN_MATCH, // case insensitive
		TYPE,
		scope, 
		requestor,
		waitingPolicy,
		progressMonitor);
	String actual = requestor.toString();
	if (!expected.equals(actual)){
	 	System.out.println(Util.displayString(actual, 3));
	}
	assertEquals(
		message,
		expected,
		actual);
}
protected void assertPattern(String expected, SearchPattern actualPattern) {
	String actual = actualPattern == null ? null : actualPattern.toString();
	if (!expected.equals(actual)) {
		System.out.print(actual == null ? "null" : Util.displayString(actual, 2));
		System.out.println(",");
	}
	assertEquals(
		"Unexpected search pattern",
		expected,
		actual);
}
protected void assertValidMatchRule(String pattern, int rule) {
	assertValidMatchRule(pattern, rule, rule);
}
protected void assertValidMatchRule(String pattern, int rule, int expected) {
	int validated = SearchPattern.validateMatchRule(pattern, rule);
	String validatedRule = BasicSearchEngine.getMatchRuleString(validated);
	String expectedRule = BasicSearchEngine.getMatchRuleString(expected);
	if (!validatedRule.equals(expectedRule)) {
		System.out.println("Test "+getName());
		System.out.print("	assertValidMatchRule(\"");
		System.out.print(pattern);
		System.out.print("\", ");
		System.out.print(validatedRule);
		System.out.println(");");
		assertEquals(pattern+"' does not match expected match rule!", expectedRule, validatedRule);
	}
}
public void setUpSuite() throws Exception {
	super.setUpSuite();
	createJavaProject("P");
	createFolder("/P/x");
	createFile(
		"/P/x/Foo.js",
		"function foo1(){\n" +
		"}"
	);
	createFile(
		"/P/x/I.js",
		"var v;\n" +
		""
	);
}
public void tearDownSuite() throws Exception {
	deleteProject("P");
	super.tearDownSuite();
}
/*
 * Ensure that changing the classpath in the middle of reindexing
 * a project causes another request to reindex.
 */
public void testChangeClasspath() throws CoreException, TimeOutException {
	IndexManager indexManager = JavaModelManager.getJavaModelManager().getIndexManager();
	WaitingJob job = new WaitingJob();
	try {
		// setup: suspend indexing and create a project (prj=src) with one cu
		indexManager.disable();
		JavaScriptCore.run(new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				createJavaProject("P1");
				createFile(
						"/P1/X.js",
						"function bar() {\n" +
						"}"
					);
			}
		}, null);
		
		// add waiting job and wait for it to be executed
		indexManager.request(job);
		indexManager.enable();
		job.startingSem.acquire(30000); // wait for job to start (wait 30s max)
		
		// remove source folder from classpath
		IJavaScriptProject project = getJavaProject("P1");
		project.setRawIncludepath(
			new IIncludePathEntry[0], 
			null);
			
		// resume waiting job
		job.runningSem.release();
		
		assertAllTypes(
			"Unexpected all types after removing source folder",
			project,
			""
		);
	} finally {
		job.runningSem.release();
		deleteProject("P1");
		indexManager.enable();
	}
}
/*
 * Ensure that removing a project source folder and adding another source folder removes the existing cus from the index
 * (regression test for bug 73356 Index not updated after adding a source folder
 */
public void testChangeClasspath2() throws CoreException {
	try {
		final IJavaScriptProject project = createJavaProject("P1", new String[] {""});
		createFile(
				"/P1/X.js",
				"function bar() {\n" +
				"}"
			);
		assertAllTypes(
			"Unexpected types before changing the classpath",
			null, // workspace search
			"X\n" + 
			"java.io.Serializable\n" + 
			"java.lang.Class\n" + 
			"java.lang.CloneNotSupportedException\n" + 
			"java.lang.Error\n" + 
			"java.lang.Exception\n" + 
			"java.lang.IllegalMonitorStateException\n" + 
			"java.lang.InterruptedException\n" + 
			"java.lang.Object\n" + 
			"java.lang.RuntimeException\n" + 
			"java.lang.String\n" + 
			"java.lang.Throwable\n" + 
			"x.y.Foo\n" + 
			"x.y.I"
		);
		getWorkspace().run(new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				createFolder("/P1/src");
				project.setRawIncludepath(createClasspath(new String[] {"/P1/src"}, false, false), null);
			}
		}, null);
		assertAllTypes(
			"Unexpected types after changing the classpath",
			null, // workspace search
			"java.io.Serializable\n" + 
			"java.lang.Class\n" + 
			"java.lang.CloneNotSupportedException\n" + 
			"java.lang.Error\n" + 
			"java.lang.Exception\n" + 
			"java.lang.IllegalMonitorStateException\n" + 
			"java.lang.InterruptedException\n" + 
			"java.lang.Object\n" + 
			"java.lang.RuntimeException\n" + 
			"java.lang.String\n" + 
			"java.lang.Throwable\n" + 
			"x.y.Foo\n" + 
			"x.y.I"
		);
	} finally {
		deleteProject("P1");
	}
}
/*
 * Ensure that performing a concurrent job while indexing a jar doesn't use the old index.
 * (regression test for bug 35306 Index update request can be incorrectly handled)
 */
public void testConcurrentJob() throws CoreException, InterruptedException, IOException, TimeOutException {
	IndexManager indexManager = JavaModelManager.getJavaModelManager().getIndexManager();
	WaitingJob job = new WaitingJob();
	try {
		// setup: suspend indexing and create a project with one empty jar on its classpath
		indexManager.disable();
		JavaScriptCore.run(new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				createJavaProject("P1", new String[] {}, new String[] {"/P1/jclMin.jar"});
				createFile("/P1/jclMin.jar", EMPTY_JAR);
			}
		}, null);
		
		// add waiting job and wait for it to be executed
		indexManager.request(job);
		indexManager.enable();
		job.startingSem.acquire(30000); // wait for job to start (wait 30s max)
				
		final IJavaScriptProject project = getJavaProject("P1");
			
		// start concurrent job
		final boolean[] success = new boolean[1];
		final WaitUntilReadyMonitor monitor = new WaitUntilReadyMonitor();
		Thread thread = new Thread() {
			public void run() {
				try {
					assertAllTypes(
						"Unexpected all types",
						project,
						WAIT_UNTIL_READY_TO_SEARCH,
						monitor,
						"java.io.Serializable\n" + 
						"java.lang.Class\n" + 
						"java.lang.CloneNotSupportedException\n" + 
						"java.lang.Error\n" + 
						"java.lang.Exception\n" + 
						"java.lang.IllegalMonitorStateException\n" + 
						"java.lang.InterruptedException\n" + 
						"java.lang.Object\n" + 
						"java.lang.RuntimeException\n" + 
						"java.lang.String\n" + 
						"java.lang.Throwable"
					);
				} catch (JavaScriptModelException e) {
					e.printStackTrace();
					return;
				}
				success[0] = true;
			}
		};
		thread.setDaemon(true);
		thread.start();
			
		// wait for concurrent job to start
		monitor.sem.acquire(30000); // wait 30s max

		// change jar contents
		getFile("/P1/jclMin.jar").setContents(new FileInputStream(getSystemJsPathString()), IResource.NONE, null);
			
		// resume waiting job
		job.runningSem.release();
		
		// wait for concurrent job to finish
		thread.join(10000); // 10s max
		
		assertTrue("Failed to get all types", success[0]);
				
	} finally {
		job.runningSem.release();
		deleteProject("P1");
		indexManager.enable();
	}
}
/*
 * Ensures that passing a null progress monitor with a CANCEL_IF_NOT_READY_TO_SEARCH
 * waiting policy doesn't throw a NullPointerException but an OperationCanceledException.
 * (regression test for bug 33571 SearchEngine.searchAllTypeNames: NPE when passing null as progress monitor)
 */
 public void testNullProgressMonitor() throws CoreException, TimeOutException {
	IndexManager indexManager = JavaModelManager.getJavaModelManager().getIndexManager();
	WaitingJob job = new WaitingJob();
 	try {
 		// add waiting job and wait for it to be executed
		indexManager.disable();
		indexManager.request(job);
		indexManager.enable();
		job.startingSem.acquire(30000); // wait for job to start (wait 30s max)
		
		// query all type names with a null progress monitor
		boolean operationCanceled = false;
		try {
			assertAllTypes(
				CANCEL_IF_NOT_READY_TO_SEARCH,
				null, // null progress monitor
				"Should not get any type"
			);
		} catch (OperationCanceledException e) {
			operationCanceled = true;
		}
		assertTrue("Should throw an OperationCanceledException", operationCanceled);
 	} finally {
 		job.runningSem.release();
 	}
 }
 /*
  * Ensures that types are found if the project is a lib folder
  * (regression test for bug 83822 Classes at root of project not found in Open Type dialog)
  */
 public void _testProjectLib() throws CoreException { // TODO disabled due to transcient failures (see bug 84164)
 	try {
 		IJavaScriptProject javaProject = createJavaProject("P1", new String[0], new String[] {"/P1"});
 		createClassFile("/P1", "X.class", "public class X {}");
 		IProject project = javaProject.getProject();
 		project.close(null);
 		waitUntilIndexesReady();
 		project.open(null);
 		assertAllTypes(
 			"Unexpected types in P1",
 			javaProject,
 			"X"
 		);
 	} finally {
 		deleteProject("P1");
 	}
 }
/*
 * Ensure that removing the outer folder from the classpath doesn't remove cus in inner folder
 * from index
 * (regression test for bug 32607 Removing outer folder removes nested folder's cus from index)
 */
public void testRemoveOuterFolder() throws CoreException {
	try {
		// setup: one cu in a nested source folder
		JavaScriptCore.run(new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				IJavaScriptProject project = createJavaProject("P1");
				project.setRawIncludepath(
					createClasspath(new String[] {"/P1/src1", "src2/", "/P1/src1/src2", ""}, false/*no inclusion*/, true/*exclusion*/), 
					null);
				createFolder("/P1/src1/src2");
				createFile(
					"/P1/src1/src2/X.js",
					"public class X {\n" +
					"}"
				);
			}
		}, null);
		IJavaScriptProject project = getJavaProject("P1");
		assertAllTypes(
			"Unexpected all types after setup",
			project,
			"X"
		);
		
		// remove outer folder from classpath
		project.setRawIncludepath(
			createClasspath(new String[] {"/P1/src1/src2", ""}, false/*no inclusion*/, true/*exclusion*/), 
			null);
		assertAllTypes(
			"Unexpected all types after removing outer folder",
			project,
			"X"
		);
		
	} finally {
		deleteProject("P1");
	}
}
/**
 * Test pattern creation
 */
public void testSearchPatternCreation01() {

	SearchPattern searchPattern = createPattern(
			"main(*)", 
			IJavaScriptSearchConstants.METHOD,
			IJavaScriptSearchConstants.REFERENCES,
			true); // case sensitive
	
	assertPattern(
		"MethodReferencePattern: main(*), pattern match, case sensitive",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation02() {

	SearchPattern searchPattern = createPattern(
			"main(*) void", 
			IJavaScriptSearchConstants.METHOD,
			IJavaScriptSearchConstants.REFERENCES,
			true); // case sensitive
	
	assertPattern(
		"MethodReferencePattern: main(*) --> void, pattern match, case sensitive",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation03() {

	SearchPattern searchPattern = createPattern(
			"main(String*) void", 
			IJavaScriptSearchConstants.METHOD,
			IJavaScriptSearchConstants.REFERENCES,
			true); // case sensitive
	
	assertPattern(
		"MethodReferencePattern: main(String*) --> void, pattern match, case sensitive",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation04() {

	SearchPattern searchPattern = createPattern(
			"main(*[])", 
			IJavaScriptSearchConstants.METHOD,
			IJavaScriptSearchConstants.REFERENCES,
			true); // case sensitive
	
	assertPattern(
		"MethodReferencePattern: main(*[]), pattern match, case sensitive",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation05() {

	SearchPattern searchPattern = createPattern(
			"java.lang.*.main ", 
			IJavaScriptSearchConstants.METHOD,
			IJavaScriptSearchConstants.REFERENCES,
			true); // case sensitive
	
	assertPattern(
		"MethodReferencePattern: java.lang.*.main(...), pattern match, case sensitive",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation06() {

	SearchPattern searchPattern = createPattern(
			"java.lang.* ", 
			IJavaScriptSearchConstants.CONSTRUCTOR,
			IJavaScriptSearchConstants.REFERENCES,
			true); // case sensitive
	
	assertPattern(
		"ConstructorReferencePattern: java.lang.*(...), pattern match, case sensitive",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation07() {

	SearchPattern searchPattern = createPattern(
			"X(*,*)", 
			IJavaScriptSearchConstants.CONSTRUCTOR,
			IJavaScriptSearchConstants.REFERENCES,
			true); // case sensitive
	
	assertPattern(
		"ConstructorReferencePattern: X(*, *), pattern match, case sensitive",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation08() {

	SearchPattern searchPattern = createPattern(
			"main(String*,*) void", 
			IJavaScriptSearchConstants.METHOD,
			IJavaScriptSearchConstants.REFERENCES,
			true); // case sensitive
	
	assertPattern(
		"MethodReferencePattern: main(String*, *) --> void, pattern match, case sensitive",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation10() {

	SearchPattern searchPattern = createPattern(
			"x.y.z.Bar.field Foo", 
			IJavaScriptSearchConstants.FIELD,
			IJavaScriptSearchConstants.DECLARATIONS,
			true); // case sensitive
	
	assertPattern(
		"FieldDeclarationPattern: x.y.z.Bar.field --> Foo, exact match, case sensitive",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation12() {
	IField field = getCompilationUnit("/P/x/y/z/Foo.js").getType("Foo").getField("field");
	SearchPattern searchPattern = createPattern(
			field, 
			IJavaScriptSearchConstants.REFERENCES);
	
	assertPattern(
		"FieldReferencePattern: x.y.z.Foo.field --> int, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation13() {
	IField field = getCompilationUnit("/P/x/y/z/Foo.js").getType("Foo").getField("field");
	SearchPattern searchPattern = createPattern(
			field, 
			IJavaScriptSearchConstants.DECLARATIONS);
	
	assertPattern(
		"FieldDeclarationPattern: x.y.z.Foo.field --> int, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation14() {
	IField field = getCompilationUnit("/P/x/y/z/Foo.js").getType("Foo").getField("field");
	SearchPattern searchPattern = createPattern(
			field, 
			IJavaScriptSearchConstants.ALL_OCCURRENCES);
	
	assertPattern(
		"FieldCombinedPattern: x.y.z.Foo.field --> int, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation15() {
	IImportDeclaration importDecl = getCompilationUnit("/P/x/y/z/Foo.js").getImport("x.y.*");
	SearchPattern searchPattern = createPattern(
			importDecl, 
			IJavaScriptSearchConstants.REFERENCES);
	
	assertPattern(
		"PackageReferencePattern: <x.y>, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation16() {
	IFunction method = getCompilationUnit("/P/x/y/z/Foo.js").getType("Foo").getFunction("bar", new String[] {});
	SearchPattern searchPattern = createPattern(
			method, 
			IJavaScriptSearchConstants.DECLARATIONS);
	
	assertPattern(
		"MethodDeclarationPattern: x.y.z.Foo.bar() --> void, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation17() {
	IFunction method = getCompilationUnit("/P/x/y/z/Foo.js").getType("Foo").getFunction("bar", new String[] {});
	SearchPattern searchPattern = createPattern(
			method, 
			IJavaScriptSearchConstants.REFERENCES);
	
	assertPattern(
		"MethodReferencePattern: x.y.z.Foo.bar() --> void, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation18() {
	IFunction method = getCompilationUnit("/P/x/y/z/Foo.js").getType("Foo").getFunction("bar", new String[] {});
	SearchPattern searchPattern = createPattern(
			method, 
			IJavaScriptSearchConstants.ALL_OCCURRENCES);
	
	assertPattern(
		"MethodCombinedPattern: x.y.z.Foo.bar() --> void, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation19() {
	IType type = getCompilationUnit("/P/x/y/z/Foo.js").getType("Foo");
	SearchPattern searchPattern = createPattern(
			type, 
			IJavaScriptSearchConstants.DECLARATIONS);
	
	assertPattern(
		"TypeDeclarationPattern: pkg<x.y.z>, enclosing<>, type<Foo>, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation20() {
	IType type = getCompilationUnit("/P/x/y/z/Foo.js").getType("Foo");
	SearchPattern searchPattern = createPattern(
			type, 
			IJavaScriptSearchConstants.REFERENCES);
	
	assertPattern(
		"TypeReferencePattern: qualification<x.y.z>, type<Foo>, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation21() {
	IType type = getCompilationUnit("/P/x/y/z/I.js").getType("I");
	SearchPattern searchPattern = createPattern(
			type, 
			IJavaScriptSearchConstants.IMPLEMENTORS);
	
	assertPattern(
		"SuperInterfaceReferencePattern: <I>, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation22() {
	IType type = getCompilationUnit("/P/x/y/z/Foo.js").getType("Foo");
	SearchPattern searchPattern = createPattern(
			type, 
			IJavaScriptSearchConstants.ALL_OCCURRENCES);
	
	assertPattern(
		"TypeDeclarationPattern: pkg<x.y.z>, enclosing<>, type<Foo>, exact match, case sensitive, erasure only\n" + 
		"| TypeReferencePattern: qualification<x.y.z>, type<Foo>, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation24() {
	IPackageFragment pkg = getPackage("/P/x/y/z");
	SearchPattern searchPattern = createPattern(
			pkg, 
			IJavaScriptSearchConstants.REFERENCES);
	
	assertPattern(
		"PackageReferencePattern: <x.y.z>, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation25() {
	IImportDeclaration importDecl = getCompilationUnit("/P/x/y/z/Foo.js").getImport("java.util.Vector");
	SearchPattern searchPattern = createPattern(
			importDecl, 
			IJavaScriptSearchConstants.REFERENCES);
	
	assertPattern(
		"TypeReferencePattern: qualification<java.util>, type<Vector>, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation26() {
	IPackageFragment pkg = getPackage("/P/x/y/z");
	SearchPattern searchPattern = createPattern(
			pkg, 
			IJavaScriptSearchConstants.DECLARATIONS);
	
	assertPattern(
		"PackageDeclarationPattern: <x.y.z>, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation28() {
	IImportDeclaration importDecl = getCompilationUnit("/P/x/y/z/Foo.js").getImport("x.y.*");
	SearchPattern searchPattern = createPattern(
			importDecl, 
			IJavaScriptSearchConstants.DECLARATIONS);
	
	assertPattern(
		"PackageDeclarationPattern: <x.y>, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test pattern creation
 */
public void testSearchPatternCreation29() {
	IPackageFragment pkg = getPackage("/P/x/y/z");
	SearchPattern searchPattern = createPattern(
			pkg, 
			IJavaScriptSearchConstants.ALL_OCCURRENCES);
	
	assertPattern(
		"PackageDeclarationPattern: <x.y.z>, exact match, case sensitive, erasure only\n" +
		"| PackageReferencePattern: <x.y.z>, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test LocalVarDeclarationPattern creation
 */
public void testSearchPatternCreation30() {
	ILocalVariable localVar = new LocalVariable((JavaElement)getCompilationUnit("/P/X.js").getType("X").getFunction("foo", new String[0]),  "var", 1, 2, 3, 4, "Z");
	SearchPattern searchPattern = createPattern(
			localVar, 
			IJavaScriptSearchConstants.DECLARATIONS);
	
	assertPattern(
		"LocalVarDeclarationPattern: var [in foo() [in X [in X.js [in <default> [in <project root> [in P]]]]]], exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test LocalVarReferencePattern creation
 */
public void testSearchPatternCreation31() {
	ILocalVariable localVar = new LocalVariable((JavaElement)getCompilationUnit("/P/X.js").getType("X").getFunction("foo", new String[0]),  "var", 1, 2, 3, 4, "Z");
	SearchPattern searchPattern = createPattern(
			localVar, 
			IJavaScriptSearchConstants.REFERENCES);
	
	assertPattern(
		"LocalVarReferencePattern: var [in foo() [in X [in X.js [in <default> [in <project root> [in P]]]]]], exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test LocalVarCombinedPattern creation
 */
public void testSearchPatternCreation32() {
	ILocalVariable localVar = new LocalVariable((JavaElement)getCompilationUnit("/P/X.js").getType("X").getFunction("foo", new String[0]),  "var", 1, 2, 3, 4, "Z");
	SearchPattern searchPattern = createPattern(
			localVar, 
			IJavaScriptSearchConstants.ALL_OCCURRENCES);
	
	assertPattern(
		"LocalVarCombinedPattern: var [in foo() [in X [in X.js [in <default> [in <project root> [in P]]]]]], exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test TypeDeclarationPattern creation
 */
public void testSearchPatternCreation33() {
	IType localType = getCompilationUnit("/P/X.js").getType("X").getFunction("foo", new String[0]).getType("Y", 2);
	SearchPattern searchPattern = createPattern(
			localType, 
			IJavaScriptSearchConstants.DECLARATIONS);
	
	assertPattern(
		"TypeDeclarationPattern: pkg<>, enclosing<X.*>, type<Y>, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test TypeReferencePattern creation
 */
public void testSearchPatternCreation34() {
	IType localType = getCompilationUnit("/P/X.js").getType("X").getFunction("foo", new String[0]).getType("Y", 3);
	SearchPattern searchPattern = createPattern(
			localType, 
			IJavaScriptSearchConstants.REFERENCES);
	
	assertPattern(
		"TypeReferencePattern: qualification<X.*>, type<Y>, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test TypeDeclarationPattern creation
 */
public void testSearchPatternCreation35() {
	IType localType = getCompilationUnit("/P/X.js").getType("X").getInitializer(1).getType("Y", 2);
	SearchPattern searchPattern = createPattern(
			localType, 
			IJavaScriptSearchConstants.DECLARATIONS);
	
	assertPattern(
		"TypeDeclarationPattern: pkg<>, enclosing<X.*>, type<Y>, exact match, case sensitive, erasure only",
		searchPattern);
}

/**
 * Test TypeReferencePattern creation
 */
public void testSearchPatternCreation36() {
	IType localType = getCompilationUnit("/P/X.js").getType("X").getInitializer(2).getType("Y", 3);
	SearchPattern searchPattern = createPattern(
			localType, 
			IJavaScriptSearchConstants.REFERENCES);
	
	assertPattern(
		"TypeReferencePattern: qualification<X.*>, type<Y>, exact match, case sensitive, erasure only",
		searchPattern);
}
/**
 * Test CamelCase validation
 */
public void testSearchPatternValidMatchRule01() {
	assertValidMatchRule("foo", SearchPattern.R_EXACT_MATCH, SearchPattern.R_EXACT_MATCH);
	assertValidMatchRule("foo", SearchPattern.R_PREFIX_MATCH, SearchPattern.R_PREFIX_MATCH);
	assertValidMatchRule("foo", SearchPattern.R_PATTERN_MATCH, SearchPattern.R_EXACT_MATCH);
	assertValidMatchRule("foo", SearchPattern.R_PATTERN_MATCH|SearchPattern.R_PREFIX_MATCH, SearchPattern.R_PREFIX_MATCH);
	assertValidMatchRule("foo", SearchPattern.R_CAMELCASE_MATCH, SearchPattern.R_PREFIX_MATCH|SearchPattern.R_CASE_SENSITIVE);
}
public void testSearchPatternValidMatchRule02() {
	assertValidMatchRule("CP*P", SearchPattern.R_EXACT_MATCH, SearchPattern.R_PATTERN_MATCH);
	assertValidMatchRule("CP*P", SearchPattern.R_PREFIX_MATCH, SearchPattern.R_PATTERN_MATCH);
	assertValidMatchRule("CP*P", SearchPattern.R_PATTERN_MATCH, SearchPattern.R_PATTERN_MATCH);
	assertValidMatchRule("CP*P", SearchPattern.R_PATTERN_MATCH|SearchPattern.R_PREFIX_MATCH, SearchPattern.R_PATTERN_MATCH);
	assertValidMatchRule("CP*P", SearchPattern.R_CAMELCASE_MATCH, SearchPattern.R_PATTERN_MATCH);
}
public void testSearchPatternValidMatchRule03() {
	assertValidMatchRule("NPE", SearchPattern.R_CAMELCASE_MATCH);
	assertValidMatchRule("NPE",
		SearchPattern.R_CAMELCASE_MATCH|SearchPattern.R_PREFIX_MATCH|SearchPattern.R_CASE_SENSITIVE,
		SearchPattern.R_CAMELCASE_MATCH);
	assertValidMatchRule("nPE", SearchPattern.R_CAMELCASE_MATCH);
	assertValidMatchRule("NuPoEx", SearchPattern.R_CAMELCASE_MATCH);
	assertValidMatchRule("oF", SearchPattern.R_CAMELCASE_MATCH);
}
public void testSearchPatternValidMatchRule04() {
	assertValidMatchRule("Nu/Po/Ex",
		SearchPattern.R_CAMELCASE_MATCH,
		SearchPattern.R_PREFIX_MATCH|SearchPattern.R_CASE_SENSITIVE);
	assertValidMatchRule("Nu.Po.Ex",
		SearchPattern.R_CAMELCASE_MATCH|SearchPattern.R_PREFIX_MATCH,
		SearchPattern.R_PREFIX_MATCH);
}
public void testSearchPatternValidMatchRule05() {
	assertValidMatchRule("hashMap", SearchPattern.R_CAMELCASE_MATCH);
	assertValidMatchRule("Hashmap", SearchPattern.R_CAMELCASE_MATCH);
}
}
