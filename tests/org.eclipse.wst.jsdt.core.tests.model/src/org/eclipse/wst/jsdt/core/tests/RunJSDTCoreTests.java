/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.wst.jsdt.core.tests.utils.SequenceReaderTests;

/**
 * Runs all JDT Core tests.
 */
public class RunJSDTCoreTests extends TestCase {
public RunJSDTCoreTests(String name) {
	super(name);
}
public static Test suite() {
	TestSuite suite = new TestSuite("JSDT 'Model' Tests");
	suite.addTest(RunDOMTests.suite());
	suite.addTest(RunFormatterTests.suite());
	suite.addTest(RunModelTests.suite());
	suite.addTestSuite(SequenceReaderTests.class);
	return suite;
}
}

