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

public class TestVarSearch extends AbstractSearchTest {

	public void testVarDeclarationSearch01() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			getName(), 
			new String[] {"X.js"},
			new String[] {
			"var " + getName() + " = {\n" +
			"\tsearchVar: 5,\n" +
			"};\n"
			}, 
			IJavaScriptSearchConstants.VAR, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_EXACT_MATCH);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	public void testVarDeclarationSearch02() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			getName().toUpperCase(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"var " + getName().toUpperCase() + " = {\n" +
			"\tsearchVar: 5,\n" +
			"};\n",
			"var " + getName() + "XYZ = function() {};"
			}, 
			IJavaScriptSearchConstants.VAR, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}

	public void testVarDeclarationSearch03() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"testVar*", 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"var " + getName() + " = {\n" +
			"\tsearchVar: 5,\n" +
			"};\n",
			"var " + getName() + "XYZ = function() {};"
			}, 
			IJavaScriptSearchConstants.VAR, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 2, results.length);
	}
	
	public void testVarReferenceSearch01() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			getName(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"var searchVar = {};\n" +
			"searchVar." + getName() + " = 5;\n",
			"var x = searchVar." + getName() + ";\n"
			}, 
			IJavaScriptSearchConstants.VAR, IJavaScriptSearchConstants.REFERENCES, SearchPattern.R_EXACT_MATCH);
		assertEquals("wrong number of files containing references found", 0, results.length);
	}
	
	public void testVarReferenceSearch02() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			getName(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"var " + getName() + " = {\n" +
			"\tx: 5,\n" +
			"};\n",
			"var searchVar = " + getName() + ".x;\n"
			}, 
			IJavaScriptSearchConstants.VAR, IJavaScriptSearchConstants.REFERENCES, SearchPattern.R_EXACT_MATCH);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	public void testVarReferenceSearch03() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"testvar*", 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"var " + getName() + " = {\n" +
			"\tx: 5,\n" +
			"};\n",
			"var " + getName() + "XYZ = " + getName() + ".x;\n" +
			"var searchVar = " + getName() + "XYZ;\n"
			}, 
			IJavaScriptSearchConstants.VAR, IJavaScriptSearchConstants.REFERENCES, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 2, results.length);
	}

	public void testVarOccurrencesSearch03() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"testvar*", 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"var " + getName() + " = {\n" +
			"\tx: 5,\n" +
			"};\n",
			"var " + getName() + "XYZ = " + getName() + ".x;\n" +
			"var searchVar = " + getName() + "XYZ;\n"
			}, 
			IJavaScriptSearchConstants.VAR, IJavaScriptSearchConstants.ALL_OCCURRENCES, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 4, results.length);
	}
	
}
