/*******************************************************************************
 * Copyright (c) 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dawid Pakula - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import junit.framework.Test;

public class SwitchTest extends AbstractRegressionTest {
	public SwitchTest(String name) {
		super(name);
	}
	
	public static Test suite() {
		return buildAllCompliancesTestSuite(testClass());
	}
	
	public static Class testClass()  {
		return SwitchTest.class;
	}
	
	public void test001() {
		this.runConformTest(new String[] {
			"p/X.js",
			"switch(0) {\n" + 
			"case 0.5 :\n" + 
			"case 1 :\n" + 
			"case 2 :\n" + 
			"}\n" 
		}, "SUCCESS");
	}
	
	public void test002() {
		this.runConformTest(new String[] {
			"p/X.js",
			"var n = 1; \n" +
			"switch(n) {\n" +
			"case 1/2: \n" +
			"case 1/3: \n" +
			"default: \n" +
			"}\n"
		}, "SUCCESS");
	}
			
	public void test003() {
		this.runConformTest(new String[] {
			"p/X.js",
			"var n = '1'; \n" +
			"switch (n) {\n" +
			"case 1: \n" +
			"case 2: \n" +
			"}"
		}, "SUCCESS");
	}
	
	public void test004() {
		this.runConformTest(new String[] {
			"p/X.js",
			"var n = 1.5; \n" +
			"switch (n) {\n" +
			"case 1/2: \n" + 
			"case 1/3: \n" +
			"}"
		}, "SUCCESS");
	}
	
	public void test005() {
		this.runNegativeTest(new String[] {
			"p/X.js",
			"switch (n) {\n" +
			"case 1:\n" +
			"case 1:\n" +
			"case 2:\n" +
 			"}",
			},
			"----------\n" + 
			"1. ERROR in p\\X.js (at line 2)\n" + 
			"	case 1:\n" + 
			"	^^^^^^\n" + 
			"Duplicate case\n" + 
			"----------\n" + 
			"2. ERROR in p\\X.js (at line 3)\n" + 
			"	case 1:\n" + 
			"	^^^^^^\n" + 
			"Duplicate case\n" + 
			"----------\n"  
		);
	}
			
	public void test006() {
		this.runNegativeTest(new String[] {
			"p/X.js",
			"var n = \"hi\"; \n" +
			"switch(n) {\n" +
			"case 'hi':\n" +
			"case 'bye':\n" +
			"case \"hi\":\n" +
			"}"
			},
	 		"----------\n" + 
			"1. ERROR in p\\X.js (at line 3)\n" +
			"	case 'hi':\n" + 
			"	^^^^^^^^^\n" + 
			"Duplicate case\n" +
	 		"----------\n" + 
			"2. ERROR in p\\X.js (at line 5)\n" +
			"	case \"hi\":\n" + 
			"	^^^^^^^^^\n" + 
	 		"Duplicate case\n" + 
	 		"----------\n"
		);
	}
		
	public void test007() {
		this.runConformTest(new String[] {
			"p/X.js",
			"function f(k) {\n" +
			"	switch(k) {\n" +
			"	case 1/2:\n" +
			"   case 1/3: \n" +
			"	}\n" +
			"}"
		});
	}
	
	public void test008() {
		this.runConformTest(new String[] {
			"p/X.js",
			"/** @param Int k **/" +
			"function f(k) {\n" +
			"	switch(k) {\n" +
			"	case 1/2:\n" +
			"   case 1/3: \n" +
			"	}\n" +
			"}"
		});
	}
	
	public void test009() {
		this.runConformTest(new String[] {
			"p/X.js",
			"/** @param Number k **/" +
			"function f(k) {\n" +
			"	switch(k) {\n" +
			"	case 1/2:\n" +
			"   case 1/3: \n" +
			"	}\n" +
			"}"
		});
	}
	
	public void test010() {
		this.runConformTest(new String[] {
			"p/X.js",
			"/**\n" +
			" * @param String k \n" +
			" **/" +
			"function f(k) {\n" +
			"	switch(k) {\n" +
			"	case 1/2:\n" +
			"   case 1/3: \n" +
			"	}\n" +
			"}"
		});
	}
}
