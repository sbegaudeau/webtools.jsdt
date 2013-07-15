/*******************************************************************************
 * Copyright (c) 2011, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.search;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchConstants;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchScope;
import org.eclipse.wst.jsdt.core.search.SearchEngine;
import org.eclipse.wst.jsdt.core.search.SearchMatch;
import org.eclipse.wst.jsdt.core.search.SearchParticipant;
import org.eclipse.wst.jsdt.core.search.SearchPattern;
import org.eclipse.wst.jsdt.core.search.SearchRequestor;

public class TestConstructorSearch extends AbstractSearchTest {
	

	/**
	 * @param limitTo
	 *            - IJavaScriptSearchConstants#DECLARATIONS or
	 *            IJavaScriptSearchConstants#REFERENCES or
	 *            IJavaScriptSearchConstants#ALL_OCCURRENCES
	 * @return number of matches in test's project
	 * @throws Exception
	 */
	private int runSearchTest(String testName, String[] fileNames, String[] fileSources, int limitTo) throws Exception {
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[]{setupMinimalProject(getRootProjectName()+testName, fileNames, fileSources)});

		final int resultCount[] = new int[]{0};
		SearchEngine searchEngine = new SearchEngine();
		final SearchPattern constructorPattern = SearchPattern.createPattern(testName, IJavaScriptSearchConstants.CONSTRUCTOR, limitTo, SearchPattern.R_EXACT_MATCH);

		SearchRequestor requestor = new SearchRequestor() {
			public void acceptSearchMatch(SearchMatch match) throws CoreException {
				resultCount[0]++;
			}
		};
		searchEngine.search(constructorPattern, new SearchParticipant[]{SearchEngine.getDefaultSearchParticipant()}, scope, requestor, new NullProgressMonitor());
		return resultCount[0];
	}

	public void testDeclarationSearch01() throws Exception {
		int count = runSearchTest(getName(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"function " + getName() + "() {\n" + 
			"\tthis.s = 5;\n" +
			"}\n",
			""
			}, 
			IJavaScriptSearchConstants.DECLARATIONS);
		assertEquals("wrong number of declarations found", 1, count);
	}

	public void testDeclarationSearch02() throws Exception {
		int count = runSearchTest(getName(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"function " + getName() + "() {\n" + 
			"\tthis.s = 5;\n" +
			"}\n",
			"var a = new " + getName() + "();\n",
			}, 
			IJavaScriptSearchConstants.DECLARATIONS);
		assertEquals("wrong number of declarations found", 1, count);
	}

	public void testDeclarationSearch03() throws Exception {
		int count = runSearchTest(getName(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"function " + getName() + "() {\n" + 
			"\tthis.s = 5;\n" +
			"}\n",
			"function " + getName() + "2() {\n" + 
			"\tthis.s = 5;\n" +
			"}\n"+
			getName() + "2.prototype = new " + getName() + "();\n"
			}, 
			IJavaScriptSearchConstants.DECLARATIONS);
		assertEquals("wrong number of declarations found", 1, count);
	}
	public void testDeclarationSearch04() throws Exception {
		int count = runSearchTest("pkg."+getName()+"", 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"pkg."+getName() + " = function() {\n" + 
			"\tthis.s = 5;\n" +
			"}\n",
			""
			}, 
			IJavaScriptSearchConstants.DECLARATIONS);
		assertEquals("wrong number of declarations found", 1, count);
	}

	public void testDeclarationSearch05() throws Exception {
		int count = runSearchTest("pkg."+getName(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"pkg."+getName() + " = function() {\n" + 
			"\tthis.s = 5;\n" +
			"}\n",
			"var a = new " + "pkg."+getName() + "();\n",
			}, 
			IJavaScriptSearchConstants.DECLARATIONS);
		assertEquals("wrong number of declarations found", 1, count);
	}

	public void testDeclarationSearch06() throws Exception {
		int count = runSearchTest("pkg."+getName(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"pkg."+getName() + " = function() {\n" + 
			"\tthis.s = 5;\n" +
			"}\n",
			"pkg2."+getName() + " = function() {\n" + 
			"\tthis.s = 5;\n" +
			"}\n"+
			"pkg2."+getName() + ".prototype = new " + "pkg."+getName() + "();\n"
			}, 
			IJavaScriptSearchConstants.DECLARATIONS);
		assertEquals("wrong number of declarations found", 1, count);
	}
	
	public void testDeclarationSearch07() throws Exception {
		int count = runSearchTest("PKG."+getName(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"pkg."+getName() + " = function() {\n" + 
			"\tthis.s = 5;\n" +
			"}\n",
			"var a = new " + "pkg."+getName() + "();\n",
			}, 
			IJavaScriptSearchConstants.DECLARATIONS);
		assertEquals("wrong number of declarations found", 1, count);
	}
	
	public void testDeclarationSearch08() throws Exception {
		int count = runSearchTest("pkg."+getName(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"PKG."+getName() + " = function() {\n" + 
			"\tthis.s = 5;\n" +
			"}\n",
			"var a = new " + "pkg."+getName() + "();\n",
			}, 
			IJavaScriptSearchConstants.DECLARATIONS);
		assertEquals("wrong number of declarations found", 1, count);
	}
	
	public void testDeclarationSearch09() throws Exception {
		int count = runSearchTest("pkg."+getName(), 
			new String[] {"X.js"},
			new String[] {
			"pkg."+ getName() + " = function() {\n" + 
			"\tthis.s = 5;\n" +
			"};\n" +
			"pkg."+ getName() + "2 = function() {\n" + 
			"\tthis.s = 5;\n" +
			"};\n"
			}, 
			IJavaScriptSearchConstants.DECLARATIONS);
		assertEquals("wrong number of declarations found", 1, count);
	}
	
	public void testDeclarationSearch10() throws Exception {
		SearchMatch[] results = runSearchTest(getName(), 
			"tDS", 
			new String[] {"X.js"},
			new String[] {
			"pkg." + getName() + " = function() {\n" + 
			"\tthis.s = 5;\n" +
			"};\n" +
			"pkg." + getName() + "2 = function() {\n" + 
			"\tthis.s = 5;\n" +
			"};\n" +
			"pkg." + getName() + "3 = function() {\n" + 
			"\tthis.s = 5;\n" +
			"};\n"
			},
			IJavaScriptSearchConstants.CONSTRUCTOR, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_CAMELCASE_MATCH);
		
		assertEquals("wrong number of files containing references found", 3, results.length);
	}
	
	public void testReferenceSearch01() throws Exception {
		int count = runSearchTest(getName(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"function " + getName() + "() {\n" + 
			"\tthis.s = 5;\n" +
			"}\n"+
			"function x() {\n"+
			" var a = new " + getName() + "();\n"+
			" var b = new " + getName() + "();\n"+
			"}",
			""
			}, 
			IJavaScriptSearchConstants.REFERENCES);
		assertEquals("wrong number of references found", 2, count);
	}

	public void testReferenceSearch02() throws Exception {
		int count = runSearchTest(getName(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"function " + getName() + "() {\n" + 
			"\tthis.s = 5;\n" +
			"}\n",
			"var a = new " + getName() + "();\n",
			}, 
			IJavaScriptSearchConstants.REFERENCES);
		assertEquals("wrong number of files containing references found", 1, count);
	}
	
	public void testReferenceSearch03() throws Exception {
		int count = runSearchTest("pkg." + getName(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"pkg." + getName() + " = function() {\n" + 
			"\tthis.s = 5;\n" +
			"}\n"+
			"function x() {\n"+
			" var a = new " + "pkg." + getName() + "();\n"+
			" var b = new " + "pkg." + getName() + "();\n"+
			"}",
			""
			}, 
			IJavaScriptSearchConstants.REFERENCES);
		assertEquals("wrong number of references found", 2, count);
	}

	public void testReferenceSearch04() throws Exception {
		int count = runSearchTest("pkg." + getName(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"pkg." + getName() + " = function() {\n" +
			"\tthis.s = 5;\n" +
			"}\n",
			"var a = new " + "pkg." + getName() + "();\n",
			}, 
			IJavaScriptSearchConstants.REFERENCES);
		assertEquals("wrong number of files containing references found", 1, count);
	}
	public void testReferenceSearch05() throws Exception {
		int count = runSearchTest("pkg." + getName(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"pkg." + getName() + " = function() {\n" + 
			"\tthis.s = 5;\n" +
			"}\n"+
			"function x() {\n"+
			" var a = new " + "pkg." + getName() + "();\n"+
			" var b = new " + "pkg." + getName() + "();\n"+
			"}",
			"c = new " + "pkg." + getName() + "();\n"
			}, 
			IJavaScriptSearchConstants.REFERENCES);
		assertEquals("wrong number of references found", 3, count);
	}
	public void testReferenceSearch06() throws Exception {
		SearchMatch[] results = runSearchTest("pkg." + getName(),
			getName().substring(0, 5) + "*", 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"pkg." + getName() + " = function() {\n" +
			"\tthis.s = 5;\n" +
			"}\n",
			"var a = new " + "pkg." + getName() + "();\n",
			}, 
			IJavaScriptSearchConstants.CONSTRUCTOR, IJavaScriptSearchConstants.REFERENCES, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}

	public void testReferenceSearch07() throws Exception {
		SearchMatch[] results = runSearchTest("pkg." + getName(),
			getName().substring(0, 7) + "*", 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"pkg." + getName() + " = function() {\n" +
			"\tthis.s = 5;\n" +
			"}\n",
			"var a = new " + "pkg." + getName() + "();\n",
			}, 
			IJavaScriptSearchConstants.CONSTRUCTOR, IJavaScriptSearchConstants.REFERENCES, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	public void testReferenceSearch08() throws Exception {
		SearchMatch[] results = runSearchTest("pkg." + getName(),
			"*." + getName(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"pkg." + getName() + " = function() {\n" +
			"\tthis.s = 5;\n" +
			"}\n" +
			"pkg2." + getName() + " = function() {\n" +
			"\tthis.s = 5;\n" +
			"}\n",
			"var a = new " + "pkg." + getName() + "();\n" +
			"var b = new " + "pkg2." + getName() + "();\n"
			}, 
			IJavaScriptSearchConstants.CONSTRUCTOR, IJavaScriptSearchConstants.REFERENCES, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 2, results.length);
	}
	
	public void testOccurrencesSearch01() throws Exception {
		SearchMatch[] results = runSearchTest("pkg." + getName(),
			getName().substring(0, 7) + "*", 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"function " + getName() + "() {\n" + 
			"\tthis.s = 5;\n" +
			"}\n",
			"var a = new " + getName() + "();\n",
			}, 
			IJavaScriptSearchConstants.CONSTRUCTOR, IJavaScriptSearchConstants.ALL_OCCURRENCES, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 2, results.length);
	}
	
	public void testOccurrencesSearch02() throws Exception {
		SearchMatch[] results = runSearchTest("pkg." + getName(),
			"p*.*", 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"pkg." + getName() + " = function() {\n" +
			"\tthis.s = 5;\n" +
			"}\n",
			"var a = new " + "pkg." + getName() + "();\n"
			}, 
			IJavaScriptSearchConstants.CONSTRUCTOR, IJavaScriptSearchConstants.ALL_OCCURRENCES, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 2, results.length);
	}
}
