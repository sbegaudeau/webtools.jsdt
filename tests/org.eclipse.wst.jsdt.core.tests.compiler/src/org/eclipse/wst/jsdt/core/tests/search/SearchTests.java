/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.search;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>Test suite for all JSDT Search Tests.<p>
 */
public class SearchTests extends TestSuite {
	/**
	 * <p>Default constructor</p>
	 */
	public SearchTests() {
		this("JavaScript Search Tests");
	}

	/**
	 * <p>Constructor with specified test name.</p>
	 *
	 * @param testName of this test suite
	 */
	public SearchTests(String testName) {
		super(testName);
	}
	
	public static Test suite() {
		TestSuite all = new TestSuite("JavaScript Search Tests");
		
		all.addTestSuite(TestMethodPattern.class);
		all.addTestSuite(TestConstructorSearch.class);
		all.addTestSuite(TestTypeDeclarationPattern.class);
		all.addTestSuite(TestMethodSearch.class);
		all.addTestSuite(TestFunctionSearch.class);
		all.addTestSuite(TestFieldSearch.class);
		all.addTestSuite(TestVarSearch.class);
		all.addTestSuite(TestGetAllSubtypeNames.class);
		return all;
	}
}
