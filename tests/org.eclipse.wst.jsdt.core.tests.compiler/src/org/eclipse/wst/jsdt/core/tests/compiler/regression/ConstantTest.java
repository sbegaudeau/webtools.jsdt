/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests exceptions with Literal#computeConstant() via batch compilation
 */
public class ConstantTest extends AbstractRegressionTest {
	
public ConstantTest(String name) {
	super(name);
}

public static Test suite() {
	return new TestSuite(ConstantTest.class);
}
public void test002() {
	this.runConformTest(new String[] {
		"p/X.js",
		"  if(55!=00000000000000000000055) {\n" + 
		"    print(\"55!=00000000000000000000055\");\n" + 
		"  }\n" + 
		"  else {\n" + 
		"    print(\"55==00000000000000000000055\");\n" + 
		"  }\n",
	},"55!=00000000000000000000055\n");
}

public void test003() {
	this.runConformTest(new String[] {
		"p/X.js",
		"  if(55e2!=550e1) {\n" + 
		"    print(\"55e2!=550e1\");\n" + 
		"  }\n" + 
		"  else {\n" + 
		"    print(\"55e2==550e1\");\n" + 
		"  }\n",
	},"55e2==550e1\n");
}

public void test004() {
	this.runConformTest(new String[] {
		"p/X.js",
		"  if(5.5e2!=5.50e1) {\n" + 
		"    print(\"5.5e2!=5.50e1\");\n" + 
		"  }\n" + 
		"  else {\n" + 
		"    print(\"5.5e2==5.50e1\");\n" + 
		"  }\n",
	},"5.5e2!=5.50e1\n");
}

public void test005() {
	this.runConformTest(new String[] {
		"p/X.js",
		"  if(5.5e-2!=0.550e-1) {\n" + 
		"    print(\"5.5e-2!=0.550e-1\");\n" + 
		"  }\n" + 
		"  else {\n" + 
		"    print(\"5.5e-2==00.55e-1\");\n" + 
		"  }\n"
	},
	"5.5e-2==00.55e-1\n");
}
public void test006() {
	this.runConformTest(new String[] {
		"p/X.js",
		"  if(0x5a!=0x5A) {\n" + 
		"    print(\"0x5a != 0x5A\");\n" + 
		"  }\n" + 
		"  else {\n" + 
		"    print(\"0x5a == 0x5A\");\n" + 
		"  }\n",
	},
	"0x5a == 0x5A\n");
}

/*
 * null is not a constant
 * http://bugs.eclipse.org/bugs/show_bug.cgi?id=26585
 */
public void test007() {
	this.runConformTest(
		new String[] {
			"X.js",
			"var F = false;	\n"+
			"var Str = F ? \"dummy\" : null;	\n"+
			"function main(args) {	\n"+
			"   if (Str == null)\n"+
			"      print(\"SUCCESS\");\n"+
			"   else\n"+
			"      print(\"FAILED\");\n"+
			"}\n"+
			"main();"
		},
		"SUCCESS\n");
}

/*
 * null is not a constant
 * http://bugs.eclipse.org/bugs/show_bug.cgi?id=26138
 */
public void test008() {
	this.runConformTest(
		new String[] {
			"X.js",
			"function X() {	\n"+
			"    function main(args) {	\n"+
			"      	print(\"SUCCESS\");	\n"+
			"	} 	\n"+
			"	function foo(){	\n"+
			"		while (null == null);	//not an inlinable constant\n"+
			"		print(\"unreachable but shouldn't be flagged\");	\n" +
			"	}	\n"+
			"}	\n",
		});
}

/*
 * null is not a constant
 * http://bugs.eclipse.org/bugs/show_bug.cgi?id=26138
 */
public void test009() {
	this.runConformTest(
		new String[] {
			"X.js",
			"       if (null == null) print(\"1\");	\n" +
			"       if ((null==null ? null:null) == (null==null ? null:null))	\n" +
			"        	print(\"2\");	\n" +
			"		var b = (\"[\" + null + \"]\") == \"[null]\";  // cannot inline	\n" +
			"		print(\"3\");	\n" +
			"		var s = null;	\n" +
			"		if (s == null) print(\"4\");	\n" +
			"		var s2 = \"aaa\";	\n" +
			"		if (s2 == \"aaa\") print(\"5\");	\n"
		},
		"1\n2\n3\n4\n5\n");
		
}

/*
 * null is not a constant
 * http://bugs.eclipse.org/bugs/show_bug.cgi?id=26138
 */
public void test010() {
	this.runConformTest(
		new String[] {
			"X.js",
			"function main(args) {\n"+
			"       if (null == null) {\n"+
			"			print(\"SUCCESS\");	\n" +
			"			return;	\n" +
			"		}	\n" +
			"		print(\"SHOULDN'T BE GENERATED\");	\n" +
			"}	\n" +
			"main();\n"
		},
		"SUCCESS\n");
		
}
public void test001() {
	this.runNegativeTest(
		new String[] {
			"X.js",
			"       if (null == null) {\n"+
			"			print(\"SUCCESS\");	\n" +
			"			return;	\n" +
			"		}	\n" +
			"		print(\"SHOULDN'T BE GENERATED\");	\n"
		},
		"----------\n" + 
		"1. ERROR in X.js (at line 3)\n" + 
		"	return;	\n" + 
		"	^^^^^^^\n" + 
		"Cannot return from outside a function or method.\n" + 
		"----------\n");
		
}

public void test011() {
	this.runConformTest(
		new String[] {
			"X.js",
			"function main(args) {\n"+
			"       if (\"a\" == \"a\") {\n"+
			"			print(\"SUCCESS\");	\n" +
			"			return;	\n" +
			"		}	\n" +
			"		print(\"FAIL\");	\n" +
			"}	\n" +
			"main();\n"
		},
		"SUCCESS\n");
		
}
public void test012() {
	this.runConformTest(
		new String[] {
			"X.js",
			"function main(args) {\n"+
			"       if (true == true) {\n"+
			"			print(\"SUCCESS\");	\n" +
			"			return;	\n" +
			"		}	\n" +
			"		print(\"FAIL\");	\n" +
			"}	\n" +
			"main();\n"
		},
		"SUCCESS\n");
		
}
public void test013() {
	this.runConformTest(
		new String[] {
			"X.js",
			"function main(args) {\n"+
			"       if (5) {\n"+
			"			print(\"SUCCESS\");	\n" +
			"			return;	\n" +
			"		}	\n" +
			"		print(\"FAIL\");	\n" +
			"}	\n" +
			"main();\n"
		},
		"SUCCESS\n");
		
}
public void test014() {
	this.runConformTest(
		new String[] {
			"X.js",
			"function main(args) {\n"+
			"       var a = true;\n" +
			"       if (a == true) {\n"+
			"			print(\"SUCCESS\");	\n" +
			"			return;	\n" +
			"		}	\n" +
			"		print(\"FAIL\");	\n" +
			"}	\n" +
			"main();\n"
		},
		"SUCCESS\n");
		
}
public static Class testClass() {
	return ConstantTest.class;
}
}
