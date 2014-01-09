/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.debug.core.tests;

import org.eclipse.wst.debug.core.tests.breakpoints.LineBreakpointTests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The root {@link TestSuite} for the test bundle
 * 
 * @since 1.1
 */
public class JavaScriptDebugTestSuite extends DebugSuite {

	/**
	 * Returns the suite of tests
	 * 
	 * @return the suite of tests
	 */
	public static Test suite() {
		return new JavaScriptDebugTestSuite();
	}
	
	/**
	 * Constructor
	 * 
	 * Composes the suite of tests
	 */
	public JavaScriptDebugTestSuite() {
		addTest(new TestSuite(LineBreakpointTests.class));
	}
	
}
