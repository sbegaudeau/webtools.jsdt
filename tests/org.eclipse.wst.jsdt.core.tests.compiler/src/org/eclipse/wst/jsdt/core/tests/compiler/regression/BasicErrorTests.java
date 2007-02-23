package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;

public class BasicErrorTests extends AbstractRegressionTest {

	public BasicErrorTests(String name) {
		super(name);
 
	}
	public void test001() {
		CompilationUnitDeclaration declaration = this.runParseTest(
				"function foo(){\n" + 
				"  var c;\n" + 
				"  var d;\n" + 
				"  c.\n" + 
			"}\n",
			"X.js",
			"function foo(){\n" + 
			"  var c;\n" + 
			"  var d;\n" + 
			"  c.\n" + 
			"\n"
			
		 );
	}

	public void test002() {
		CompilationUnitDeclaration declaration = this.runParseTest(
				"package p;\n" +
				"/**\n" +
				" * @category test\n" +
				" */\n" +
				"public class Y {\n" +
			"}\n",
			"X.js",
			"function foo(){\n" + 
			"  var c;\n" + 
			"  var d;\n" + 
			"  c.\n" + 
			"\n"
			
		 );
	}


}
