/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Mickael Istria (Red Hat Inc.) - 426209 Java 6 + Warnings cleanup
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.jsdt.core.tests.junit.extension.TestCase;

public class RunJavaSearchTests extends junit.framework.TestCase {

	public final static List<Class<? extends TestCase>> TEST_CLASSES = new ArrayList<Class<? extends TestCase>>();
	static {
		TEST_CLASSES.add(JavaSearchTests.class);
		TEST_CLASSES.add(WorkingCopySearchTests.class);
		TEST_CLASSES.add(JavaSearchJavadocTests.class);
	}

	public static Class<? extends TestCase>[] getTestClasses() {
		return TEST_CLASSES.toArray(new Class[TEST_CLASSES.size()]);
	}

	public RunJavaSearchTests(String name) {
		super(name);
	}

	public static Test suite() {
		TestSuite ts = new TestSuite(RunJavaSearchTests.class.getName());

		// Store test classes with same "JavaSearch"project
		AbstractJavaSearchTests.JAVA_SEARCH_SUITES = new ArrayList<Class<? extends TestCase>>(TEST_CLASSES);

		// Get all classes
		List<Class<? extends TestCase>> allClasses = new ArrayList<Class<? extends TestCase>>(TEST_CLASSES);
		allClasses.add(JavaSearchBugsTests.class);
		allClasses.add(JavaSearchMultipleProjectsTests.class);
		allClasses.add(SearchTests.class);
		allClasses.add(JavaSearchScopeTests.class);

		// Reset forgotten subsets of tests
		TestCase.TESTS_PREFIX = null;
		TestCase.TESTS_NAMES = null;
		TestCase.TESTS_NUMBERS = null;
		TestCase.TESTS_RANGE = null;
		TestCase.RUN_ONLY_ID = null;

		// Add all tests suite of tests
		for (Class<? extends TestCase> testClass : allClasses) {
			// call the suite() method and add the resulting suite to the suite
			try {
				Method suiteMethod = testClass.getDeclaredMethod("suite", new Class[0]); //$NON-NLS-1$
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
