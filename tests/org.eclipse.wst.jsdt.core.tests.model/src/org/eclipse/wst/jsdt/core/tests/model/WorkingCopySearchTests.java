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
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchConstants;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchScope;
import org.eclipse.wst.jsdt.core.search.SearchEngine;
import org.eclipse.wst.jsdt.core.search.SearchParticipant;
import org.eclipse.wst.jsdt.core.search.SearchPattern;
import org.eclipse.wst.jsdt.core.search.TypeNameRequestor;
public class WorkingCopySearchTests extends JavaSearchTests {
	IJavaScriptUnit workingCopy;
	
	public WorkingCopySearchTests(String name) {
		super(name);
	}
	public static Test suite() {
		return buildModelTestSuite(WorkingCopySearchTests.class);
	}
	// Use this static initializer to specify subset for tests
	// All specified tests which do not belong to the class are skipped...
	static {
//		TESTS_PREFIX =  "testAllTypeNames";
//		TESTS_NAMES = new String[] { "testAllTypeNamesBug98684" };
//		TESTS_NUMBERS = new int[] { 8 };
//		TESTS_RANGE = new int[] { -1, -1 };
	}
	
	/**
	 * Get a new working copy.
	 */
	protected void setUp() throws Exception {
		super.setUp();
		try {
			this.workingCopy = this.getCompilationUnit("JSSearch", "src", "wc", "X.js").getWorkingCopy(null);
		} catch (JavaScriptModelException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Destroy the working copy.
	 */
	protected void tearDown() throws Exception {
		this.workingCopy.discardWorkingCopy();
		this.workingCopy = null;
		super.tearDown();
	}
	
	/**
	 * Hierarchy scope on a working copy test.
	 */
	public void testHierarchyScopeOnWorkingCopy() throws CoreException {
		IJavaScriptUnit unit = this. getCompilationUnit("JSSearch", "src", "a9", "A.js");
		IJavaScriptUnit copy = unit.getWorkingCopy(null);
		try {
			IType type = copy.getType("A");
			assertNotNull(type);
			IJavaScriptSearchScope scope = SearchEngine.createHierarchyScope(type);
			assertTrue("a9.A should be included in hierarchy scope", scope.encloses(type));
			assertTrue("a9.C should be included in hierarchy scope", scope.encloses(copy.getType("C")));
			assertTrue("a9.B should be included in hierarchy scope", scope.encloses(copy.getType("B")));
			IPath path = unit.getUnderlyingResource().getFullPath();
			assertTrue("a9/A.java should be included in hierarchy scope", scope.encloses(path.toString()));
		} finally {
			copy.discardWorkingCopy();
		}
	}
	
	/**
	 * Type declaration in a working copy test.
	 * A new type is added in the working copy only.
	 */
	public void testAddNewType() throws CoreException {
		this.workingCopy.createType(
			"function NewType(){}\n" +
			"NewType.prototype = new Object();\n",
			null,
			false,
			null);
		
		IJavaScriptSearchScope scope = 
			SearchEngine.createJavaSearchScope(
				new IJavaScriptElement[] {this.workingCopy.getParent()});
	//	JavaSearchResultCollector resultCollector = new JavaSearchResultCollector();
		SearchPattern pattern = SearchPattern.createPattern(
			"NewType",
			TYPE,
			DECLARATIONS, 
			SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE);
		new SearchEngine(new IJavaScriptUnit[] {this.workingCopy}).search(
			pattern, 
			new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()},
			scope, 
			resultCollector,
			null);
		assertSearchResults(
			"src/wc/X.js wc.NewType [NewType]", 
			resultCollector);
	}
	
	/*
	 * Search all type names in working copies test.
	 * (Regression test for bug 40793 Primary working copies: Type search does not find type in modified CU)
	 */
	public void testAllTypeNames1() throws CoreException {
		this.workingCopy.getBuffer().setContents(
			"Y.prototype = new Object();\n" +
			"Y.prototype.I = new Object();\n" +
			"Y.I2 = new Object();\n"
		);
		this.workingCopy.makeConsistent(null);
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {this.workingCopy.getParent()});
		SearchTests.SearchTypeNameRequestor requestor = new SearchTests.SearchTypeNameRequestor();
		new SearchEngine(new IJavaScriptUnit[] {this.workingCopy}).searchAllTypeNames(
			null,
			SearchPattern.R_EXACT_MATCH,
			null,
			SearchPattern.R_PATTERN_MATCH, // case insensitive
			TYPE,
			scope, 
			requestor,
			IJavaScriptSearchConstants.WAIT_UNTIL_READY_TO_SEARCH,
			null		
		);
		assertSearchResults(
			"Unexpected all type names",
			"Y",
			requestor);
	}
	
	/*
	 * Search all type names in working copies test (without reconciling working copies).
	 * (Regression test for bug 40793 Primary working copies: Type search does not find type in modified CU)
	 */
	public void testAllTypeNames2() throws CoreException {
		this.workingCopy.getBuffer().setContents(
			"Y.prototype = new Object();\n" +
			"Y.prototype.I = new Object();\n" +
			"Y.I2 = new Object();\n"
		);
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {this.workingCopy.getParent()});
		SearchTests.SearchTypeNameRequestor requestor = new SearchTests.SearchTypeNameRequestor();
		new SearchEngine(new IJavaScriptUnit[] {this.workingCopy}).searchAllTypeNames(
			null,
			SearchPattern.R_EXACT_MATCH,
			null,
			SearchPattern.R_PATTERN_MATCH, // case insensitive
			TYPE,
			scope, 
			requestor,
			IJavaScriptSearchConstants.WAIT_UNTIL_READY_TO_SEARCH,
			null		
		);
		assertSearchResults(
			"Unexpected all type names",
			"Y",
			requestor);
	}
	
	/*
	 * Search all type names with a prefix in a primary working copy.
	 * (regression test for bug 44884 Wrong list displayed while code completion)
	 */
	public void testAllTypeNames3() throws CoreException {
		IJavaScriptUnit wc = getCompilationUnit("/JSSearch/wc3/X44884.js");
		try {
			wc.becomeWorkingCopy(null);
			wc.getBuffer().setContents(
				"X44884.prototype = new Object();\n" +
				"I.prototype = new Object();\n"
			);
			wc.makeConsistent(null);
			
			IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {wc.getParent()});
			SearchTests.SearchTypeNameRequestor requestor = new SearchTests.SearchTypeNameRequestor();
			new SearchEngine().searchAllTypeNames(
				null,
				SearchPattern.R_EXACT_MATCH,
				"X".toCharArray(),
				SearchPattern.R_PREFIX_MATCH, // case insensitive
				TYPE,
				scope, 
				requestor,
				IJavaScriptSearchConstants.WAIT_UNTIL_READY_TO_SEARCH,
				null		
			);
			assertSearchResults(
				"Unexpected all type names",
				"X44884",
				requestor);
		} finally {
			wc.discardWorkingCopy();
		}
	}

	/*
	 * Search all type names with a prefix in a primary working copy (without reconciling it).
	 * (regression test for bug 44884 Wrong list displayed while code completion)
	 */
	public void testAllTypeNames4() throws CoreException {
		IJavaScriptUnit wc = getCompilationUnit("/JSSearch/wc3/X44884.js");
		try {
			wc.becomeWorkingCopy(null);
			wc.getBuffer().setContents(
				"X44884.prototype = new Object();\n" +
				"I.prototype = new Object();\n"
			);
			
			IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {wc.getParent()});
			SearchTests.SearchTypeNameRequestor requestor = new SearchTests.SearchTypeNameRequestor();
			new SearchEngine().searchAllTypeNames(
				null,
				SearchPattern.R_EXACT_MATCH,
				"X".toCharArray(),
				SearchPattern.R_PREFIX_MATCH, // case insensitive
				TYPE,
				scope, 
				requestor,
				IJavaScriptSearchConstants.WAIT_UNTIL_READY_TO_SEARCH,
				null		
			);
			assertSearchResults(
				"Unexpected all type names",
				"X44884",
				requestor);
		} finally {
			wc.discardWorkingCopy();
		}
	}

	/**
	 * Bug 99915: [search] Open Type: not yet saved types not found if case-sensitve name is entered
	 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=99915"
	 */
	public void testAllTypeNamesBug99915() throws CoreException {
		this.workingCopy.getBuffer().setContents(
			"X.prototype = new Object();\n" +
			"AAABBB.prototype = new Object();\n" +
			"BBBCCC.prototype = new Object();\n"
		);
		this.workingCopy.makeConsistent(null);
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {this.workingCopy.getParent()});
		SearchTests.SearchTypeNameRequestor requestor = new SearchTests.SearchTypeNameRequestor();
		new SearchEngine(new IJavaScriptUnit[] {this.workingCopy}).searchAllTypeNames(
			null,
			SearchPattern.R_EXACT_MATCH,
			"A*".toCharArray(),
			SearchPattern.R_PATTERN_MATCH, // case insensitive
			TYPE,
			scope, 
			requestor,
			IJavaScriptSearchConstants.WAIT_UNTIL_READY_TO_SEARCH,
			null		
		);
		assertSearchResults(
			"Unexpected all type names",
			"AAABBB",
			requestor);
	}

	/**
	 * Bug 98684: [search] Code assist shown inner types of unrelated project
	 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=98684"
	 */
	public void testAllTypeNamesBug98684() throws CoreException {
		try {
			IJavaScriptProject[] projects = new IJavaScriptProject[2];
			projects[0] = createJavaProject("P1");
			projects[1] = createJavaProject("P2");
			workingCopies = new IJavaScriptUnit[2];
			workingCopies[0] = getWorkingCopy("/P1/p1/A1.js",
				"Ca1.prototype = new Object()"
			);
			workingCopies[1] = getWorkingCopy("/P2/p2/A2.js",
				"Cb2.prototype = new Object()"
			);
			TypeNameRequestor requestor =  new SearchTests.SearchTypeNameRequestor();
			IJavaScriptSearchScope scope = 	SearchEngine.createJavaSearchScope(new IJavaScriptElement[] { projects[1] });
			new SearchEngine(this.workingCopies).searchAllTypeNames(
				null,
				SearchPattern.R_EXACT_MATCH,
				"C".toCharArray(),
				SearchPattern.R_PREFIX_MATCH,
				TYPE,
				scope,
				requestor,
				IJavaScriptSearchConstants.WAIT_UNTIL_READY_TO_SEARCH,
				null
			);
			assertSearchResults(
				"Unexpected all type names",
				"Cb2",
				requestor);
		}
		finally {
			deleteProject("P1");
			deleteProject("P2");
		}
	}

	/**
	 * Declaration of referenced types test.
	 * (Regression test for bug 5355 search: NPE in searchDeclarationsOfReferencedTypes  )
	 */
	public void testDeclarationOfReferencedTypes() throws CoreException {
		IFunction method = this.workingCopy.getType("X").createMethod(
			"public void foo() {\n" +
			"  X x = new X();\n" +
			"}",
			null,
			true,
			null);
	//	JavaSearchResultCollector resultCollector = new JavaSearchResultCollector();
		searchDeclarationsOfReferencedTypes(
			method, 
			resultCollector
		);
		assertSearchResults(
			"src/wc/X.java wc.X [X]", 
			resultCollector);
	}
	
	/**
	 * Type declaration in a working copy test.
	 * A type is moved from one working copy to another.
	 */
	public void testMoveType() throws CoreException {
		
		// move type X from working copy in one package to a working copy in another package
		IJavaScriptUnit workingCopy1 = getCompilationUnit("JSSearch", "src", "wc1", "X.js").getWorkingCopy(null);
		IJavaScriptUnit workingCopy2 = getCompilationUnit("JSSearch", "src", "wc2", "Y.js").getWorkingCopy(null);
		
		try {
			workingCopy1.getType("X").move(workingCopy2, null, null, true, null);
			
			SearchEngine searchEngine = new SearchEngine(new IJavaScriptUnit[] {workingCopy1, workingCopy2});
			
			// type X should not be visible in old package
			IJavaScriptSearchScope scope1 = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {workingCopy1.getParent()});
	//		JavaSearchResultCollector resultCollector = new JavaSearchResultCollector();
			
			SearchPattern pattern = SearchPattern.createPattern(
				"X",
				TYPE,
				DECLARATIONS, 
				SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE);
			searchEngine.search(
				pattern, 
				new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()},
				scope1, 
				resultCollector,
				null);
			assertEquals(
				"", 
				resultCollector.toString());
			
			// type X should be visible in new package
			IJavaScriptSearchScope scope2 = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {workingCopy2.getParent()});
			resultCollector = new JavaSearchResultCollector();
			searchEngine.search(
				pattern, 
				new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()},
				scope2, 
				resultCollector,
				null);
			assertSearchResults(
				"src/wc2/Y.js wc2.X [X]", 
				resultCollector);
		} finally {
			workingCopy1.discardWorkingCopy();
			workingCopy2.discardWorkingCopy();
		}
	}
	
	/**
	 * Type declaration in a working copy test.
	 * A type is removed from the working copy only.
	 */
	public void testRemoveType() throws CoreException {
		this.workingCopy.getType("X").delete(true, null);
		
		IJavaScriptSearchScope scope = 
			SearchEngine.createJavaSearchScope(
				new IJavaScriptElement[] {this.workingCopy.getParent()});
		
		// type X should not be visible when working copy hides it
	//	JavaSearchResultCollector resultCollector = new JavaSearchResultCollector();
		SearchPattern pattern = SearchPattern.createPattern(
			"X",
			TYPE,
			DECLARATIONS, 
			SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE);
		new SearchEngine(new IJavaScriptUnit[] {this.workingCopy}).search(
			pattern, 
			new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()},
			scope, 
			resultCollector,
			null);
		assertSearchResults(
			"", 
			resultCollector);
			
		// ensure the type is still present in the compilation unit
		resultCollector = new JavaSearchResultCollector();
		new SearchEngine().search(
			pattern, 
			new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()},
			scope, 
			resultCollector,
			null);
		assertSearchResults(
			"src/wc/X.java wc.X [X]", 
			resultCollector);
	
	}
	
}
