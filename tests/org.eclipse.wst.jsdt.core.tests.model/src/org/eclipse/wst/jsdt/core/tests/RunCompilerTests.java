/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Runs all compiler tests (including parser tests) See AbstractCompilerTests
 * for more details.
 */
public class RunCompilerTests extends TestCase {

	public RunCompilerTests(String name) {
		super(name);
	}

	public static Class[] getAllTestClasses() {
		return new Class[] {
				org.eclipse.wst.jsdt.core.tests.compiler.regression.TestAll.class};
	}

	public static Test suite() {
		TestSuite ts = new TestSuite(RunCompilerTests.class.getName());

		Class[] testClasses = getAllTestClasses();
		for (int i = 0; i < testClasses.length; i++) {
			Class testClass = testClasses[i];

			// call the suite() method and add the resulting suite to the suite
			try {
				Method suiteMethod = testClass.getDeclaredMethod(
						"suite", new Class[0]); //$NON-NLS-1$
				Test suite = (Test) suiteMethod.invoke(null, new Object[0]);
				ts.addTest(suite);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.getTargetException().printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return ts;
	}
}
