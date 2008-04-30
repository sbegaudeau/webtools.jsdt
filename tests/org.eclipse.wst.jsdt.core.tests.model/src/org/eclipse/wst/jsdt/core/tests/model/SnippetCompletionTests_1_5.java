/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.model;

import org.eclipse.wst.jsdt.core.IClassFile;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.internal.codeassist.RelevanceConstants;

import junit.framework.*;

public class SnippetCompletionTests_1_5 extends AbstractJavaModelTests implements RelevanceConstants {
	
public SnippetCompletionTests_1_5(String name) {
	super(name);
}
public void setUpSuite() throws Exception {
	super.setUpSuite();
	
	setUpJavaProject("SnippetCompletion", "1.5");
}
public void tearDownSuite() throws Exception {
	deleteProject("SnippetCompletion");
	
	super.tearDownSuite();
}

public static Test suite() {
	return buildModelTestSuite(SnippetCompletionTests_1_5.class);
}
protected void assertResults(String expected, String actual) {
	try {
		assertEquals(expected, actual);
	} catch(ComparisonFailure c) {
		System.out.println(actual);
		System.out.println();
		throw c;
	}
}
public void testCodeSnippetAssistForBug132665() throws JavaScriptModelException {
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
	IClassFile cf = getClassFile("SnippetCompletion", "class-folder", "bug132665", "Bug132665.class");
	IType type = cf.getType();
	
	String snippet = 
		"foo";

	char[][] typeNames = {};
	char[][] names = {};
	int[] modifiers = {};
	
	type.codeComplete(snippet.toCharArray(), -1, snippet.length(), typeNames, names, modifiers, false, requestor);
	
	assertResults(
		"foo1[FUNCTION_REF]{foo1(), Lbug132665.Bug132665;, ()V, foo1, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED)+"}\n"+
		"foo3[FUNCTION_REF]{foo3(), Lbug132665.Bug132665;, ()V, foo3, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED)+"}",
		requestor.getResults());
}
}
