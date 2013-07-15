/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.search;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchConstants;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchScope;
import org.eclipse.wst.jsdt.core.search.SearchEngine;
import org.eclipse.wst.jsdt.core.search.SearchMatch;
import org.eclipse.wst.jsdt.core.search.SearchPattern;
import org.eclipse.wst.jsdt.core.search.TypeNameMatch;
import org.eclipse.wst.jsdt.core.search.TypeNameMatchRequestor;

/**
 * <p>Tests for TypeDeclarationPattern.</p>
 */
public class TestTypeDeclarationPattern extends AbstractSearchTest {
	
	protected TypeNameMatch[] runTypeSearchTest(String projectQualifier, String queryString, String[] fileNames, String[] fileSources, int searchFor, int matchRule) throws Exception {
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[]{setupMinimalProject(getRootProjectName()+projectQualifier, fileNames, fileSources)});

		final List results = new ArrayList();
		SearchEngine searchEngine = new SearchEngine();

		TypeNameMatchRequestor requestor = new TypeNameMatchRequestor() {
			public void acceptTypeNameMatch(TypeNameMatch match) {
				results.add(match);
			}
		};
		
		searchEngine.searchAllTypeNames(
				queryString.toCharArray(),
				matchRule,
				searchFor,
				scope,
				requestor,
				IJavaScriptSearchConstants.WAIT_UNTIL_READY_TO_SEARCH,
				new NullProgressMonitor()
		);
		return (TypeNameMatch[])results.toArray(new TypeNameMatch[results.size()]);
	}

	public void testTypeDeclarationPatternMatch01() throws Exception {
		TypeNameMatch[] results = runTypeSearchTest(getName(), 
				"at*", 
				new String[] {"X.js"},
				new String[] {
				"aTest1" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest2" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest3" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.bTest" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n"
				},
				IJavaScriptSearchConstants.TYPE, 
				SearchPattern.R_PATTERN_MATCH);
			
		assertEquals("wrong number of files containing references found", 3, results.length);
	}
	
	public void testTypeDeclarationPatternMatch02() throws Exception {
		TypeNameMatch[] results = runTypeSearchTest(getName(), 
				"apack*", 
				new String[] {"X.js"},
				new String[] {
				"aTest1" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest2" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest3" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.bTest" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n"
				},
				IJavaScriptSearchConstants.TYPE, 
				SearchPattern.R_PATTERN_MATCH);
			
		assertEquals("wrong number of files containing references found", 3, results.length);
	}
	
	public void testTypeDeclarationPatternMatch03() throws Exception {
		TypeNameMatch[] results = runTypeSearchTest(getName(), 
				"a*e.b*", 
				new String[] {"X.js"},
				new String[] {
				"aTest1" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest2" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest3" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.bTest" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n"
				},
				IJavaScriptSearchConstants.TYPE, 
				SearchPattern.R_PATTERN_MATCH);
			
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	public void testTypeDeclarationPatternMatch04() throws Exception {
		TypeNameMatch[] results = runTypeSearchTest(getName(), 
				"a*.a*", 
				new String[] {"X.js"},
				new String[] {
				"aTest1" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest2" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest3" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.bTest" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n"
				},
				IJavaScriptSearchConstants.TYPE, 
				SearchPattern.R_PATTERN_MATCH);
			
		assertEquals("wrong number of files containing references found", 2, results.length);
	}
	
	public void testTypeDeclarationPatternMatch05() throws Exception {
		TypeNameMatch[] results = runTypeSearchTest(getName(), 
				"a*.s*", 
				new String[] {"X.js"},
				new String[] {
				"aTest1" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest2" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.sTest" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.subpackage.aTest3" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n"
				},
				IJavaScriptSearchConstants.TYPE, 
				SearchPattern.R_PATTERN_MATCH);
			
		assertEquals("wrong number of files containing references found", 2, results.length);
	}

	public void testTypeDeclarationPatternMatch06() throws Exception {
		TypeNameMatch[] results = runTypeSearchTest(getName(), 
				"a*.s*.*t", 
				new String[] {"X.js"},
				new String[] {
				"aTest1" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.sTest" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.subpackage.bTest" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.subpackage.aTest2" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n"
				},
				IJavaScriptSearchConstants.TYPE, 
				SearchPattern.R_PATTERN_MATCH);
			
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	public void testTypeDeclarationPatternMatch07() throws Exception {
		TypeNameMatch[] results = runTypeSearchTest(getName(), 
				"*.A*", 
				new String[] {"X.js"},
				new String[] {
				"aTest1" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest2" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest3" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.bTest" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n"
				},
				IJavaScriptSearchConstants.TYPE, 
				SearchPattern.R_PATTERN_MATCH);
			
		assertEquals("wrong number of files containing references found", 2, results.length);
	}
	
	public void testTypeDeclarationPatternMatch8() throws Exception {
		TypeNameMatch[] results = runTypeSearchTest(getName(), 
				"APACK*", 
				new String[] {"X.js"},
				new String[] {
				"aTest1" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest2" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest3" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.bTest" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n"
				},
				IJavaScriptSearchConstants.TYPE, 
				SearchPattern.R_PATTERN_MATCH);
			
		assertEquals("wrong number of files containing references found", 3, results.length);
	}
	
	public void testTypeDeclarationPatternMatch09() throws Exception {
		TypeNameMatch[] results = runTypeSearchTest(getName(), 
				"apack.a", 
				new String[] {"X.js"},
				new String[] {
				"aTest1" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest2" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest3" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.bTest" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n"
				},
				IJavaScriptSearchConstants.TYPE, 
				SearchPattern.R_PREFIX_MATCH);
			
		assertEquals("wrong number of files containing references found", 0, results.length);
	}
	
	public void testTypeDeclarationPatternMatch10() throws Exception {
		TypeNameMatch[] results = runTypeSearchTest(getName(), 
				"apackage.a", 
				new String[] {"X.js"},
				new String[] {
				"aTest1" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest2" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest3" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.bTest" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n"
				},
				IJavaScriptSearchConstants.TYPE, 
				SearchPattern.R_PREFIX_MATCH);
			
		assertEquals("wrong number of files containing references found", 2, results.length);
	}
	
	public void testTypeDeclarationPatternMatch11() throws Exception {
		TypeNameMatch[] results = runTypeSearchTest(getName(), 
				"APACK", 
				new String[] {"X.js"},
				new String[] {
				"aTest1" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest2" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest3" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.bTest" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n"
				},
				IJavaScriptSearchConstants.TYPE, 
				SearchPattern.R_PREFIX_MATCH);
			
		assertEquals("wrong number of files containing references found", 3, results.length);
	}
	
	public void testTypeDeclarationPatternMatch12() throws Exception {
		TypeNameMatch[] results = runTypeSearchTest(getName(), 
				"apackage.a", 
				new String[] {"X.js"},
				new String[] {
				"aTest1" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"APACKAGE.ATEST2" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"APACKAGE.aTest3" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"APACKAGE.bTest" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n"
				},
				IJavaScriptSearchConstants.TYPE, 
				SearchPattern.R_PREFIX_MATCH);
			
		assertEquals("wrong number of files containing references found", 2, results.length);
	}
	
	public void testTypeDeclarationSearch01() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
				"apack*", 
				new String[] {"X.js"},
				new String[] {
				"aTest1" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest2" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest3" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.bTest" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n"
				},
				IJavaScriptSearchConstants.TYPE,
				IJavaScriptSearchConstants.DECLARATIONS,
				SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 3, results.length);
	}
	
	public void testTypeDeclarationSearch02() throws Exception {
		SearchMatch[] results = runSearchTest(getName(), 
				"*.a*", 
				new String[] {"X.js"},
				new String[] {
				"aTest1" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest2" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest3" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.bTest" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n"
				},
				IJavaScriptSearchConstants.TYPE, 
				IJavaScriptSearchConstants.DECLARATIONS,
				SearchPattern.R_PATTERN_MATCH);
			
		assertEquals("wrong number of files containing references found", 2, results.length);
	}
	
	public void testTypeDeclarationSearch03() throws Exception {
		SearchMatch[] results = runSearchTest(getName(), 
				"APACKAGE.a*", 
				new String[] {"X.js"},
				new String[] {
				"aTest1" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"aTest2" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest3" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"APACKAGE.aTest4" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n"
				},
				IJavaScriptSearchConstants.TYPE, 
				IJavaScriptSearchConstants.DECLARATIONS,
				SearchPattern.R_PATTERN_MATCH | SearchPattern.R_CASE_SENSITIVE);
			
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	public void testTypeDeclarationSearch04() throws Exception {
		SearchMatch[] results = runSearchTest(getName(), 
				"at*", 
				new String[] {"X.js"},
				new String[] {
				"aTest1" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest2" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n" +
				"apackage.aTest3" + " = function() {\n" + 
				"\tthis.s = 5;\n" +
				"}\n"
				},
				IJavaScriptSearchConstants.TYPE, 
				IJavaScriptSearchConstants.DECLARATIONS,
				SearchPattern.R_PATTERN_MATCH);
			
		assertEquals("wrong number of files containing references found", 3, results.length);
	}
	
	public void testTypeDeclarationSearch05() throws Exception {
		SearchMatch[] results = runSearchTest(getName(), 
			"TTD", 
			new String[] {"X.js"},
			new String[] {
			"apackage.TestTypeDeclaration1" + " = function() {\n" + 
			"\tthis.s = 5;\n" +
			"}\n" +
			"apackage.TestTypeDeclaration2" + " = function() {\n" + 
			"\tthis.s = 5;\n" +
			"}\n" +
			"apackage.TestTypeDeclaration3" + " = function() {\n" + 
			"\tthis.s = 5;\n" +
			"}\n"
			},
			IJavaScriptSearchConstants.TYPE, 
			IJavaScriptSearchConstants.DECLARATIONS,
			SearchPattern.R_CAMELCASE_MATCH);
		
		assertEquals("wrong number of files containing references found", 3, results.length);
	}
	
}
