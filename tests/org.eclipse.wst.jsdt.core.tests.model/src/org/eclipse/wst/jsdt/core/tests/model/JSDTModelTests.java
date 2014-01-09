/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.tests.junit.extension.TestCase;

	/**
	 * Run all java model tests.
	 */
	public class JSDTModelTests extends TestSuite {
	static {
		JavaScriptCore.getPlugin().getPluginPreferences().setValue("semanticValidation", true);
	}
	
	public JSDTModelTests() {
		this("JavaScript Model Tests");
	}
		
	public JSDTModelTests(String name) {
		super(name);
		Class[] classes = getAllTestClasses();
		for (int i = 0; i < classes.length; i++) {
			addTestSuite(classes[i]);
		}
	}

private static Class[] getAllTestClasses() {
	Class[] classes = new Class[] {
	
		// Enter each test here, grouping the tests that are related
			
		// Binding key tests
		BindingKeyTests.class,

		// Working copy tests
		WorkingCopyTests.class,

		// IBuffer tests
		BufferTests.class,
	
		// Java-like extensions tests
		JavaScriptLikeExtensionsTests.class,
		
		// Code snipper parsing util tests
		CodeSnippetParsingUtilTests.class
	};
	
	int classesLength = classes.length;
	Class[] result = new Class[classesLength];
	System.arraycopy(classes, 0, result, 0, classesLength);
	
	return result;
}

public static Test suite() {
	TestSuite suite = new TestSuite("JSDT Model Tests");

	// Hack to load all classes before computing their suite of test cases
	// this allow to reset test cases subsets while running all Java Model tests...
	Class[] classes = getAllTestClasses();

	// Reset forgotten subsets of tests
	TestCase.TESTS_PREFIX = null;
	TestCase.TESTS_NAMES = null;
	TestCase.TESTS_NUMBERS = null;
	TestCase.TESTS_RANGE = null;
	TestCase.RUN_ONLY_ID = null;
	
	for (int i = 0, length = classes.length; i < length; i++) {
		Class clazz = classes[i];
		Method suiteMethod;
		try {
			suiteMethod = clazz.getDeclaredMethod("suite", new Class[0]);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			continue;
		}
		Object test;
		try {
			test = suiteMethod.invoke(null, new Object[0]);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			continue;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			continue;
		}
		suite.addTest((Test) test);
	}

	return suite;
}

}
