/*****************************************************************************
 * Copyright (c) 2004,2009 IBM Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies  this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 * 
 ****************************************************************************/

package org.eclipse.wst.jsdt.unittests.internal;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.jsdt.web.core.tests.AllWebCoreTests;
import org.eclipse.wst.jsdt.web.ui.tests.AllWebUITests;
import org.osgi.framework.Bundle;

public class MasterJSDTTestSuite extends TestSuite {
	private static final Bundle BUNDLE = Activator.getDefault().getBundle();
	private static final String EXTENSION_POINT_ID = BUNDLE.getSymbolicName() + ".additionalTests";
	private static final String CLASS = "class";

	public MasterJSDTTestSuite() {
		super("JSDT Tests");

		System.setProperty("wtp.autotest.noninteractive", "true");

//		addTest(JSDTCompilerTests.suite());
//		addTest(RunJSDTCoreTests.suite());
		addTest(AllWebCoreTests.suite());
		addTest(AllWebUITests.suite());
//		addTest(JSDTUITests.suite());

		IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_POINT_ID);
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].getName().equals("suite")) {
				TestSuite suite;
				try {
					suite = (TestSuite) elements[i].createExecutableExtension(CLASS);
					addTestSuite(suite.getClass());
					System.err.println("Adding TestSuite " + suite.getClass().getName());
				}
				catch (CoreException e) {
					e.printStackTrace(System.err);
					Platform.getLog(BUNDLE).log(e.getStatus());
				}
			}
			else if (elements[i].getName().equals("test")) {
				Test test;
				try {
					test = (Test) elements[i].createExecutableExtension(CLASS);
					addTest(new TestSuite(test.getClass()));
					System.err.println("Adding TestCase " + test.getClass().getName());
				}
				catch (CoreException e) {
					e.printStackTrace(System.err);
					Platform.getLog(BUNDLE).log(e.getStatus());
				}
			}
		}
	}

	public void testAll() {
		// this method needs to exist, but doesn't really do anything
		// other than to signal to create an instance of this class.
		// The rest it automatic from the tests added in constructor.
	}
}
