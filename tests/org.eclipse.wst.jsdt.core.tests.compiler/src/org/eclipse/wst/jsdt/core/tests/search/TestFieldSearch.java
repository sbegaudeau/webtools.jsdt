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

import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchConstants;
import org.eclipse.wst.jsdt.core.search.SearchMatch;
import org.eclipse.wst.jsdt.core.search.SearchPattern;

public class TestFieldSearch extends AbstractSearchTest {

	public void testFieldDeclarationSearch01() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			getName(), 
			new String[] {"X.js"},
			new String[] {
			"var searchField = {\n" +
			"\t" + getName() + ": 5,\n" +
			"\t" + getName() + "XYZ: 10,\n" +
			"};\n"
			}, 
			IJavaScriptSearchConstants.FIELD, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_EXACT_MATCH);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	public void testFieldDeclarationSearch02() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"SearchConstructor." + getName(), 
			new String[] {"X.js"},
			new String[] {
			"function SearchConstructor() {\n" +
				"\tthis." + getName() + " = 5;\n" +	
				"\tthis." + getName() + "XYZ = 10;\n" +
			"};"
			}, 
			IJavaScriptSearchConstants.FIELD, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_EXACT_MATCH);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	public void testFieldDeclarationSearch03() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"SearchConstructor." + getName(), 
			new String[] {"X.js"},
			new String[] {
			"function SearchConstructor() {\n" +
				"\tthis." + getName() + " = 5;\n" +	
			"};\n" + 
			"function SearchConstructor2() {\n" +
				"\tthis." + getName() + " = 5;\n" +	
			"};"
			}, 
			IJavaScriptSearchConstants.FIELD, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_EXACT_MATCH);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	public void testFieldDeclarationSearch04() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			getName() + "XYZ", 
			new String[] {"X.js"},
			new String[] {
			"function SearchConstructor() {\n" +
				"\tthis." + getName() + " = 5;\n" +	
			"};\n" +
			"SearchConstructor." + getName().toUpperCase() + "XYZ = 10;\n"
			}, 
			IJavaScriptSearchConstants.FIELD, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_EXACT_MATCH);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	public void testFieldDeclarationSearch05() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"testField*", 
			new String[] {"X.js"},
			new String[] {
			"var searchField = {\n" +
			"\t" + getName() + ": 5,\n" +
			"\t" + getName() + "XYZ: 10,\n" +
			"};\n"
			}, 
			IJavaScriptSearchConstants.FIELD, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 2, results.length);
	}
	
	public void testFieldDeclarationSearch06() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"*." + getName().toUpperCase(), 
			new String[] {"X.js"},
			new String[] {
			"function SearchConstructor() {\n" +
				"\tthis." + getName() + " = 5;\n" +	
				"\tthis." + getName() + "XYZ = 10;\n" +
			"};"
			}, 
			IJavaScriptSearchConstants.FIELD, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	public void testFieldDeclarationSearch07() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"SearchConstructor.*",
			new String[] {"X.js"},
			new String[] {
			"SearchConstructor.prototype." + getName() + " = 5;\n" +
			"SearchConstructor.prototype." + getName() + "XYZ = 10;\n"
			}, 
			IJavaScriptSearchConstants.FIELD, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 2, results.length);
	}
	
	public void testFieldReferenceSearch01() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			getName(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"var searchField = {};\n" +
			"searchField." + getName() + " = 5;\n" +
			"searchField." + getName() + "XYZ = 10;\n",
			"var a = searchField." + getName() + ";\n"
			}, 
			IJavaScriptSearchConstants.FIELD, IJavaScriptSearchConstants.REFERENCES, SearchPattern.R_EXACT_MATCH);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	public void testFieldReferenceSearch02() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"testfield*", 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"function searchField() {\n" +
				"\tthis." + getName().toUpperCase() + " = 5;\n" +
				"\tthis." + getName() + "XYZ = 10;\n" +
			"};\n",
			"var a = searchField." + getName().toUpperCase() + ";\n" +
			"var b = searchField." + getName() + "XYZ;\n"
			}, 
			IJavaScriptSearchConstants.FIELD, IJavaScriptSearchConstants.REFERENCES, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 2, results.length);
	}
	
	public void testFieldReferenceSearch03() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"tFRS", 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"function searchField() {\n" +
				"\tthis." + getName().toUpperCase() + " = 5;\n" +
				"\tthis." + getName() + "XYZ = 10;\n" +
			"};\n",
			"var a = searchField." + getName().toUpperCase() + ";\n" +
			"var b = searchField." + getName() + "XYZ;\n"
			}, 
			IJavaScriptSearchConstants.FIELD, IJavaScriptSearchConstants.REFERENCES, SearchPattern.R_CAMELCASE_MATCH);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	public void testFieldOccurrencesSearch01() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"TESTFIELD*", 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"function searchField() {\n" +
				"\tthis." + getName().toUpperCase() + " = 5;\n" +
				"\tthis." + getName() + "XYZ = 10;\n" +
			"};\n",
			"var a = searchField." + getName().toUpperCase() + ";\n" +
			"var b = searchField." + getName() + "XYZ;\n"
			}, 
			IJavaScriptSearchConstants.FIELD, IJavaScriptSearchConstants.ALL_OCCURRENCES, SearchPattern.R_PATTERN_MATCH  | SearchPattern.R_CASE_SENSITIVE);
		assertEquals("wrong number of files containing references found", 3, results.length);
	}
	
	
}
