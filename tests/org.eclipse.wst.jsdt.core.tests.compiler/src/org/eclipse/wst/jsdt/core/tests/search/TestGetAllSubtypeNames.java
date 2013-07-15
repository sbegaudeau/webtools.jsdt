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

import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchConstants;
import org.eclipse.wst.jsdt.core.search.SearchEngine;

public class TestGetAllSubtypeNames extends AbstractSearchTest {
	public void testGetAllSubtypeNames01() throws Exception {
		// single file
		IJavaScriptProject project = setupMinimalProject(getRootProjectName() + getName(),
			new String[] {"X.js"},
			new String[] {
			"function " + getName() + "(){\n" +
			"\tthis.searchVar= 5;\n" +
			"}\n" +
			"sub.prototype = new "+getName()+"();\n" +
			"sub  = {\n" +
			"\tsearchVar: 5,\n" +
			"};\n"
			});

		char[][] allSubtypeNames = SearchEngine.getAllSubtypeNames(getName().toCharArray(), SearchEngine.createJavaSearchScope(new IJavaScriptElement[]{project}), IJavaScriptSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, null);
		assertEquals("wrong number of subtypes found", 2, allSubtypeNames.length);
		assertEquals("wrong subtype found", "sub", new String(allSubtypeNames[1]));
	}
	
	public void testGetAllSubtypeNames02() throws Exception {
		// two files
		IJavaScriptProject project = setupMinimalProject(getRootProjectName() + getName(),
			new String[] {"X.js","Y.js"},
			new String[] {
			"function " + getName() + "(){\n" +
			"\tthis.searchVar= 5;\n" +
			"}\n",
			"sub.prototype = new "+getName()+"();\n" +
			"sub  = {\n" +
			"\tsearchVar: 5,\n" +
			"};\n"
			});

		char[][] allSubtypeNames = SearchEngine.getAllSubtypeNames(getName().toCharArray(), SearchEngine.createJavaSearchScope(new IJavaScriptElement[]{project}), IJavaScriptSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, null);
		assertEquals("wrong number of subtypes found", 2, allSubtypeNames.length);
		assertEquals("wrong subtype found", "sub", new String(allSubtypeNames[1]));
	}
	
}
