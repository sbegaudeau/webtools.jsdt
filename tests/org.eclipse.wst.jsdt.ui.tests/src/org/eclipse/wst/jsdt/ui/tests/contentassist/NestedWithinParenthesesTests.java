/*******************************************************************************
 * Copyright (c) 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     
 *******************************************************************************/
package org.eclipse.wst.jsdt.ui.tests.contentassist;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.wst.jsdt.ui.tests.utils.TestProjectSetup;

public class NestedWithinParenthesesTests extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test Insert Proposals Within Parentheses";

	/**
	 * <p>
	 * Test project setup for this test.
	 * </p>
	 */
	private static TestProjectSetup fTestProjectSetup;
	
	/**
	 * <p>
	 * Default constructor
	 * <p>
	 * <p>
	 * Use {@link #suite()}
	 * </p>
	 * 
	 * @see #suite()
	 */
	public NestedWithinParenthesesTests() {
		super(TEST_NAME);
	}

	/**
	 * <p>
	 * Constructor that takes a test name.
	 * </p>
	 * <p>
	 * Use {@link #suite()}
	 * </p>
	 * 
	 * @param name
	 *            The name this test run should have.
	 * 
	 * @see #suite()
	 */
	public NestedWithinParenthesesTests(String name) {
		super(name);
	}

	/**
	 * <p>
	 * Use this method to add these tests to a larger test suite so set up and tear down can be
	 * performed
	 * </p>
	 * 
	 * @return a {@link TestSetup} that will run all of the tests in this class
	 *         with set up and tear down.
	 */

	public static Test suite() {
		TestSuite ts = new TestSuite(NestedWithinParenthesesTests.class, TEST_NAME);
		
		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false) {
			/**
			 * @see org.eclipse.wst.jsdt.ui.tests.contentassist.ContentAssistTestUtilities.ContentAssistTestsSetup#additionalSetUp()
			 */
			public void additionalSetUp() throws Exception {
			}
		};
		
		return fTestProjectSetup;
	}
	
	public void testInsertToEditor_315660_1() throws Exception {
		String expectedResult = 
				"var data1;\n" +
				"addEventListener(type, listener, useCapture)" ;
		ContentAssistTestUtilities.runProposalandInertTest(fTestProjectSetup, "validateTest_315660_1.js", 1, 0, expectedResult);
	}
	
	public void testInsertToEditor_315660_2() throws Exception {
		String expectedResult = 
				"var data1;\n" +
				"var data2;\n" +
				"(data1)";
		ContentAssistTestUtilities.runProposalandInertTest(fTestProjectSetup, "validateTest_315660_2.js", 2, 5, expectedResult);
	}
	
	public void testInsertToEditor_315660_3() throws Exception {
		String expectedResult = 
				"var myData1;\n"+
				"var myData2;\n"+
				"(\n"+
				"	// myData\n"+
				"	(\n"+
				"        myData1	\n"+
				"	)\n"+
				");";
		ContentAssistTestUtilities.runProposalandInertTest(fTestProjectSetup, "validateTest_315660_3.js", 5, 12, expectedResult);
	}

	public void testInsertToEditor_315660_4() throws Exception {
		String expectedResult = 
				"var data1;\n" +
				"var data2;\n" +
				"((10+data1));";
		ContentAssistTestUtilities.runProposalandInertTest(fTestProjectSetup, "validateTest_315660_4.js", 2, 7, expectedResult);
	}
	
	public void testInsertToEditor_315660_5() throws Exception {
		String expectedResult = 
				"var point = new Object();\n"+
				"point.x = 2.3;\n"+
				"point.y = -1.2;\n"+
				"var square = {upperLeft: {x:point.x, y:point.y}, upperRight: {x:(parseFloat(s))} };";
		ContentAssistTestUtilities.runProposalandInertTest(fTestProjectSetup, "validateTest_315660_5.js", 3, 66, expectedResult);
	}

}
