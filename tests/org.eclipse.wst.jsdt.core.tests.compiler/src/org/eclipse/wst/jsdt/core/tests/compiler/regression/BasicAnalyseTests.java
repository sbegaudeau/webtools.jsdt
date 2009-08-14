package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import java.util.HashMap;
import java.util.Map;

public class BasicAnalyseTests extends AbstractRegressionTest {
	public BasicAnalyseTests(String name) {
		super(name);
	}
	
	public void testBug251374_1() {
		Map custom = new HashMap();
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.nullReference", "error");
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.potentialNullReference", "error");
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var b = null;\n" +
						"function boo() {\n" +
						"b.toString();\n" +
						"}\n" +
						"b = 2;\n" +
						"boo();" 
				},
				"", null, false, custom
		);
	}
	public void testBug251374_2() {
		Map custom = new HashMap();
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.nullReference", "error");
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.potentialNullReference", "error");
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var b = null;\n" +
						"function boo() {\n" +
						"b.toString();\n" +
						"}\n" +
						"boo();" 
				},
				"----------\n" + 
				"1. ERROR in X.js (at line 3)\n" + 
				"	b.toString();\n" + 
				"	^\n" + 
				"Null pointer access: The variable b can only be null at this location\n" + 
				"----------\n", null, false, custom
		);
	}
	
	public void testBug251374_3() {
		Map custom = new HashMap();
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.nullReference", "error");
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.potentialNullReference", "error");
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var b = null;\n" +
						"function boo() {\n" +
						"b = null\n" +
						"b.toString();\n" +
						"}\n" +
						"b = 2;\n" +
						"boo();" 
				},
				"----------\n" + 
				"1. ERROR in X.js (at line 4)\n" + 
				"	b.toString();\n" + 
				"	^\n" + 
				"Null pointer access: The variable b can only be null at this location\n" + 
				"----------\n", null, false, custom
		);
	}
	
	public void testBug251374_4() {
		Map custom = new HashMap();
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.nullReference", "error");
		custom.put("org.eclipse.wst.jsdt.core.compiler.problem.potentialNullReference", "error");
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var b = null;\n" +
						"function boo() {\n" +
						"b = null\n" +
						"b.toString();\n" +
						"}\n" +
						"b = 2;\n" +
						"boo();\n" +
						"b.toString();" 
				},
				"----------\n" + 
				"1. ERROR in X.js (at line 4)\n" + 
				"	b.toString();\n" + 
				"	^\n" + 
				"Null pointer access: The variable b can only be null at this location\n" + 
				"----------\n", null, false, custom
		);
	}
	
	public void testBug251374_5() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var b = null;\n" +
						"function boo() {\n" +
						"b = null\n" +
						"b.toString();\n" +
						"}\n" +
						"b = 2;\n" +
						"boo();\n" +
						"b.toString();" 
				},
				""
		);
	}
	
	public void testBug286029_1() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var sub;\n" +
						"if(!sub) sub = {};" 
				},
				""
		);
	}
	
	public void testBug286029_2() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function abc() {\n" +
						"var sub;\n" +
						"if(!sub) sub = {};\n" +
						"}"
				},
				"----------\n" + 
		"1. WARNING in X.js (at line 3)\n" + 
		"	if(!sub) sub = {};\n" + 
		"	    ^^^\n" + 
		"The local variable sub may not have been initialized\n" + 
		"----------\n"
		);
	}
	
	public void testBug286029_3() {
		this.runNegativeTest(
				new String[] {
						"X.js",
						"function abc() {\n" +
						"var sub; sub = {};\n" +
						"if(!sub) sub = {};\n" +
						"}"
				},
				""
		);
	}

}
