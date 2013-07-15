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

public class TestMethodSearch extends AbstractSearchTest {

	public void testDeclarationSearch01() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			getName(), 
			new String[] {"X.js"},
			new String[] {
			"var searchVar = {\n" +
			"\t" + getName() + ": function() {},\n" +
			"\t" + getName() + "XYZ: function() {},\n" +
			"};\n"
			}, 
			IJavaScriptSearchConstants.METHOD, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_EXACT_MATCH);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	public void testDeclarationSearch02() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"searchVar." + getName(), 
			new String[] {"X.js"},
			new String[] {
			"var searchVar = function() {\n" +
				"\tthis." + getName() + " = function() {};\n" +	
				"\tthis." + getName() + "XYZ = function() {};\n" +
			"};"
			}, 
			IJavaScriptSearchConstants.METHOD, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_EXACT_MATCH);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	public void testDeclarationSearch03() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"SearchConstructor." + getName(),
			new String[] {"X.js"},
			new String[] {
			"SearchConstructor.prototype." + getName() + " = function() {};\n" +
			"SearchConstructor2.prototype." + getName() + " = function() {};\n"
			}, 
			IJavaScriptSearchConstants.METHOD, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_EXACT_MATCH);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	public void testDeclarationSearch04() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"*." + getName(), 
			new String[] {"X.js"},
			new String[] {
			"var searchVar = function() {\n" +
				"\tthis." + getName() + " = function() {};\n" +	
				"\tthis." + getName() + "XYZ = function() {};\n" +
			"};"
			}, 
			IJavaScriptSearchConstants.METHOD, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	public void testDeclarationSearch05() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"testdec*", 
			new String[] {"X.js"},
			new String[] {
			"var searchVar = {\n" +
			"\t" + getName() + ": function() {},\n" +
			"\t" + getName() + "XYZ: function() {},\n" +
			"};\n"
			}, 
			IJavaScriptSearchConstants.METHOD, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 2, results.length);
	}
	
	public void testDeclarationSearch06() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"SearchConstructor.*",
			new String[] {"X.js"},
			new String[] {
			"SearchConstructor.prototype." + getName() + " = function() {};\n" +
			"SearchConstructor.prototype." + getName() + "XYZ = function() {};\n"
			}, 
			IJavaScriptSearchConstants.METHOD, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 2, results.length);
	}
	
	public void testReferencesSearch01() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			getName() + "xyz", 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"var searchVar = {\n" +
			"\t" + getName() + ": function() {},\n" +
			"\t" + getName() + "XYZ: function() {},\n" +
			"};\n",
			"searchVar." + getName() + "();\n" +
			"searchVar." + getName() + "XYZ();\n"
			}, 
			IJavaScriptSearchConstants.METHOD, IJavaScriptSearchConstants.REFERENCES, SearchPattern.R_EXACT_MATCH);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	public void testReferencesSearch02() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"testref*", 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"var searchVar = function() {\n" +
				"\tthis." + getName() + " = function() {};\n" +	
				"\tthis." + getName() + "XYZ = function() {};\n" +
			"};",
			"searchVar." + getName() + "();\n" +
			"searchVar." + getName() + "XYZ();\n"
			}, 
			IJavaScriptSearchConstants.METHOD, IJavaScriptSearchConstants.REFERENCES, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 2, results.length);
	}
	
	public void testReferencesSearch03() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"TESTREF*", 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"var searchVar = function() {\n" +
				"\tthis." + getName() + " = function() {};\n" +	
				"\tthis." + getName() + "XYZ = function() {};\n" +
			"};",
			"searchVar." + getName() + "();\n" +
			"searchVar." + getName() + "XYZ();\n"
			}, 
			IJavaScriptSearchConstants.METHOD, IJavaScriptSearchConstants.REFERENCES, SearchPattern.R_PATTERN_MATCH | SearchPattern.R_CASE_SENSITIVE);
		assertEquals("wrong number of files containing references found", 0, results.length);
	}
	
	public void testOccurrencesSearch01() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			getName(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"var searchVar = {\n" +
			"\t" + getName() + ": function() {}\n" + 
			"};\n",
			"searchVar." + getName() + "();"
			}, 
			IJavaScriptSearchConstants.METHOD, IJavaScriptSearchConstants.ALL_OCCURRENCES, SearchPattern.R_EXACT_MATCH);
		assertEquals("wrong number of files containing references found", 2, results.length);
	}
	
	public void testOccurrencesSearch02() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"testocc*", 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"var searchVar = function() {\n" +
				"\tthis." + getName() + " = function() {};\n" +	
				"\tthis." + getName() + "XYZ = function() {};\n" +
			"};",
			"searchVar." + getName() + "();\n" +
			"searchVar." + getName() + "XYZ();\n"
			}, 
			IJavaScriptSearchConstants.METHOD, IJavaScriptSearchConstants.ALL_OCCURRENCES, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 4, results.length);
	}
	
}

