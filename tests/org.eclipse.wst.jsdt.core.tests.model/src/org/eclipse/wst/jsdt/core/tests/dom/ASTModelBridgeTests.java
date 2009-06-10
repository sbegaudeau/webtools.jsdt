/*******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.dom;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.jsdt.core.*;
import org.eclipse.wst.jsdt.core.compiler.IProblem;
import org.eclipse.wst.jsdt.core.dom.*;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchConstants;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchScope;
import org.eclipse.wst.jsdt.core.search.SearchEngine;
import org.eclipse.wst.jsdt.core.tests.model.AbstractJavaSearchTests;

import junit.framework.Test;

/*
 * Test the bridge between the DOM AST and the Java model.
 */
public class ASTModelBridgeTests extends AbstractASTTests {
	
	IJavaScriptUnit workingCopy;

	public ASTModelBridgeTests(String name) {
		super(name);
	}

	public static Test suite() {
		return buildModelTestSuite(ASTModelBridgeTests.class);
	}
	
	// Use this static initializer to specify subset for tests
	// All specified tests which do not belong to the class are skipped...
	static {
//		TESTS_PREFIX =  "testBug86380";
//		TESTS_NAMES = new String[] { "testCreateBindings19" };
//		TESTS_NUMBERS = new int[] { 83230 };
//		TESTS_RANGE = new int[] { 83304, -1 };
		}

	/*
	 * Removes the marker comments "*start*" and "*end*" from the given contents,
	 * builds an AST from the resulting source, and returns the AST node that was delimited
	 * by "*start*" and "*end*".
	 */
	private ASTNode buildAST(String contents) throws JavaScriptModelException {
		return buildAST(contents, this.workingCopy);
	}
	
	private IBinding[] createBindings(String contents, IJavaScriptElement element) throws JavaScriptModelException {
		this.workingCopy.getBuffer().setContents(contents);
		this.workingCopy.makeConsistent(null);
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setProject(getJavaProject("P"));
		IJavaScriptElement[] elements = new IJavaScriptElement[] {element};
		return parser.createBindings(elements, null);
	}
	
	private IBinding[] createBinaryBindings(String contents, IJavaScriptElement element) throws CoreException {
		createClassFile("/P/lib", "A.class", contents);
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setProject(getJavaProject("P"));
		IJavaScriptElement[] elements = new IJavaScriptElement[] {element};
		return parser.createBindings(elements, null);
	}
	
	public void setUpSuite() throws Exception {
		super.setUpSuite();
		setUpJavaProject();
	}

	private void setUpJavaProject() throws CoreException, IOException, JavaScriptModelException {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"}, new String[] {"JCL15_LIB,JCL15_SRC", "/P/lib"}, "bin", "1.5");
		project.setOption(JavaScriptCore.COMPILER_PB_UNUSED_LOCAL, JavaScriptCore.IGNORE);
		project.setOption(JavaScriptCore.COMPILER_PB_UNUSED_PRIVATE_MEMBER, JavaScriptCore.IGNORE);
		project.setOption(JavaScriptCore.COMPILER_PB_FIELD_HIDING, JavaScriptCore.IGNORE);
		project.setOption(JavaScriptCore.COMPILER_PB_LOCAL_VARIABLE_HIDING, JavaScriptCore.IGNORE);
		project.setOption(JavaScriptCore.COMPILER_PB_TYPE_PARAMETER_HIDING, JavaScriptCore.IGNORE);
		project.setOption(JavaScriptCore.COMPILER_PB_RAW_TYPE_REFERENCE, JavaScriptCore.IGNORE);
		addLibrary(
			project, 
			"lib.jar",
			"libsrc.zip", 
			new String[] {
				"p/Y.js",
				"package p;\n" +
				"public class Y<T> {\n" +
				"  public Y(T t) {\n" +
				"  }\n" +
				"}",
				"p/Z.js",
				"package p;\n" +
				"public class Z {\n" +
				"  /*start*/class Member {\n" +
				"  }/*end*/\n" +
				"  void foo() {\n" +
				"    new Member() {};\n" +
				"  }\n" +
				"}",
				"p/W.js",
				"package p;\n" +
				"public class W {\n" +
				"  class Member {\n" +
				"    /*start*/Member(String s) {\n" +
				"    }/*end*/\n" +
				"  }\n" +
				"}",
				"p/ABC.js",
				"package p;\n" +
				"public class ABC {\n" +
				"}",
				"Z.js",
				"public class Z {\n" +
				"  /*start*/class Member {\n" +
				"  }/*end*/\n" +
				"  void foo() {\n" +
				"    new Member() {};\n" +
				"  }\n" +
				"}"
			}, 
			"1.5");
		this.workingCopy = getCompilationUnit("/P/src/X.js").getWorkingCopy(
			new WorkingCopyOwner() {}, 
			new IProblemRequestor() {
				public void acceptProblem(IProblem problem) {}
				public void beginReporting() {}
				public void endReporting() {}
				public boolean isActive() {
					return true;
				}
			}, 
			null/*no progress*/);
	}
	
	public void tearDownSuite() throws Exception {
		tearDownJavaProject();
		super.tearDownSuite();
	}

	private void tearDownJavaProject() throws JavaScriptModelException, CoreException {
		if (this.workingCopy != null)
			this.workingCopy.discardWorkingCopy();
		deleteProject("P");
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing an anonymous type is correct.
	 */
	public void testAnonymousType() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"public class X {\n" +
			"  Object foo() {\n" +
			"    return new Object() /*start*/{\n" +
			"    }/*end*/;\n" +
			"  }\n" +
			"}"
		);
		IBinding binding = ((AnonymousClassDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"<anonymous #1> [in foo() [in X [in [Working copy] X.java [in <default> [in src [in P]]]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	public void testAnonymousType2() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"public class X {\n" + 
			"	public void foo() {\n" + 
			"		new Y(0/*c*/) /*start*/{\n" + 
			"			Object field;\n" + 
			"		}/*end*/;\n" + 
			"	}\n" + 
			"}\n" + 
			"class Y {\n" + 
			"	Y(int i) {\n" + 
			"	}\n" + 
			"}"
		);
		IBinding binding = ((AnonymousClassDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"<anonymous #1> [in foo() [in X [in [Working copy] X.java [in <default> [in src [in P]]]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing an array type is correct.
	 */
	public void testArrayType1() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"public class X {\n" +
			"  /*start*/Object[]/*end*/ field;\n" +
			"}"
		);
		IBinding binding = ((ArrayType) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"Object [in Object.class [in java.lang [in "+ getExternalJCLPathString("1.5") + "]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing an array type of base type null.
	 * (regression test for bug 100142
	  	CCE when calling ITypeBinding#getJavaElement() on char[][]
	 */
	public void testArrayType2() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"public class X {\n" +
			"  /*start*/char[][]/*end*/ field;\n" +
			"}"
		);
		IBinding binding = ((ArrayType) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"<null>",
			element
		);
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a method coming from a class file is correct.
	 * (regression test for bug 91445 IFunctionBinding.getJavaElement() returns an "unopen" IFunction)
	 */
	public void testBinaryMethod() throws JavaScriptModelException {
		IClassFile classFile = getClassFile("P", getExternalJCLPathString("1.5"), "java.lang", "Enum.class");
		String source = classFile.getSource();
		MarkerInfo markerInfo = new MarkerInfo(source);
		markerInfo.astStarts = new int[] {source.indexOf("protected Enum")};
		markerInfo.astEnds = new int[] {source.indexOf('}', markerInfo.astStarts[0]) + 1};
		ASTNode node = buildAST(markerInfo, classFile);
		IBinding binding = ((FunctionDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"Enum(java.lang.String, int) [in Enum [in Enum.class [in java.lang [in "+ getExternalJCLPathString("1.5") + "]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}
	
	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a constructor of a binary member type is correct.
	 * (regression test for bug 119249 codeResolve, search, etc. don't work on constructor of binary inner class)
	 */
	public void testBinaryMemberTypeConstructor() throws JavaScriptModelException {
		IClassFile classFile = getClassFile("P", "/P/lib.jar", "p", "W$Member.class");
		String source = classFile.getSource();
		MarkerInfo markerInfo = new MarkerInfo(source);
		markerInfo.astStarts = new int[] {source.indexOf("/*start*/") + "/*start*/".length()};
		markerInfo.astEnds = new int[] {source.indexOf("/*end*/")};
		ASTNode node = buildAST(markerInfo, classFile);
		IBinding binding = ((FunctionDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"Member(p.W, java.lang.String) [in Member [in W$Member.class [in p [in lib.jar [in P]]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}
	
	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a type coming from a class file is correct.
	 */
	public void testBinaryType() throws JavaScriptModelException {
		IClassFile classFile = getClassFile("P", getExternalJCLPathString("1.5"), "java.lang", "String.class");
		String source = classFile.getSource();
		MarkerInfo markerInfo = new MarkerInfo(source);
		markerInfo.astStarts = new int[] {source.indexOf("public")};
		markerInfo.astEnds = new int[] {source.lastIndexOf('}') + 1};
		ASTNode node = buildAST(markerInfo, classFile);
		IBinding binding = ((TypeDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"String [in String.class [in java.lang [in "+ getExternalJCLPathString("1.5") + "]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}
	
	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a type coming from a class file is correct
	 * after searching for references to this type.
	 * (regression test for bug 136016 [refactoring] CCE during Use Supertype refactoring)
	 */
	public void testBinaryType2() throws CoreException {
		IClassFile classFile = getClassFile("P", "lib.jar", "p", "ABC.class"); // class with no references
		
		// ensure classfile is open
		classFile.open(null);
		
		//search for references to p.ABC after adding references in exactly 1 file
		try {
			createFile(
				"/P/src/Test.js",
				"import p.ABC;\n" +
				"public class Test extends ABC {\n" +
				"}"
				);
			IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {getPackageFragmentRoot("/P/src")});
			search(classFile.getType(), IJavaScriptSearchConstants.REFERENCES, scope, new AbstractJavaSearchTests.JavaSearchResultCollector());
		} finally {
			deleteFile("/P/src/Test.js");
		}
		
		String source = classFile.getSource();
		MarkerInfo markerInfo = new MarkerInfo(source);
		markerInfo.astStarts = new int[] {source.indexOf("public")};
		markerInfo.astEnds = new int[] {source.lastIndexOf('}') + 1};
		ASTNode node = buildAST(markerInfo, classFile);
		IBinding binding = ((TypeDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"ABC [in ABC.class [in p [in lib.jar [in P]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}
	
	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a type in a jar is correct after deleting the first project
	 * referencing it.
	 */
	public void testBinaryType3() throws CoreException, IOException {
		// force String to be put in the jar cache
		buildAST(
			"public class X {\n" + 
			"    /*start*/String/*end*/ field;\n" + 
			"}"
		);
		try {
			tearDownJavaProject();
			
			createJavaProject("P1", new String[] {""}, new String[] {"JCL15_LIB"}, "", "1.5");
			createFile(
				"/P1/X.js",
				"public class X {\n" + 
				"    /*start*/String/*end*/ field;\n" + 
				"}"
			);
			ASTNode node = buildAST(getCompilationUnit("/P1/X.js"));
			IBinding binding = ((Type) node).resolveBinding();
			IJavaScriptElement element = binding.getJavaElement();
			assertElementEquals(
				"Unexpected Java element",
				"String [in String.class [in java.lang [in "+ getExternalJCLPathString("1.5") + "]]]",
				element
			);
			assertTrue("Element should exist", element.exists());
		} finally {
			deleteProject("P1");
			setUpJavaProject();
		}
	}

	/*
	 * Ensures that the IJavaScriptElement for a binary member type coming from an anoumous class file is correct.
	 * (regression test for bug 100636 [model] Can't find overriden methods of protected nonstatic inner class.)
	 */
	public void testBinaryMemberTypeFromAnonymousClassFile1() throws JavaScriptModelException {
		IClassFile classFile = getClassFile("P", "/P/lib.jar", "p", "Z$1.class");
		String source = classFile.getSource();
		MarkerInfo markerInfo = new MarkerInfo(source);
		markerInfo.astStarts = new int[] {source.indexOf("/*start*/") + "/*start*/".length()};
		markerInfo.astEnds = new int[] {source.indexOf("/*end*/")};
		ASTNode node = buildAST(markerInfo, classFile);
		IBinding binding = ((TypeDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"Member [in Z$Member.class [in p [in lib.jar [in P]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}
	
	/*
	 * Ensures that the IJavaScriptElement for a binary member type coming from an anoumous class file is correct.
	 * (regression test for bug 100636 [model] Can't find overriden methods of protected nonstatic inner class.)
	 */
	public void testBinaryMemberTypeFromAnonymousClassFile2() throws JavaScriptModelException {
		IClassFile classFile = getClassFile("P", "/P/lib.jar", "", "Z$1.class");
		String source = classFile.getSource();
		MarkerInfo markerInfo = new MarkerInfo(source);
		markerInfo.astStarts = new int[] {source.indexOf("/*start*/") + "/*start*/".length()};
		markerInfo.astEnds = new int[] {source.indexOf("/*end*/")};
		ASTNode node = buildAST(markerInfo, classFile);
		IBinding binding = ((TypeDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"Member [in Z$Member.class [in <default> [in lib.jar [in P]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}
	
	/*
	 * Ensures that the correct IBindings are created for a given set of IJavaScriptElement
	 * (test several kinds of elements)
	 */
	public void testCreateBindings01() throws JavaScriptModelException {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setResolveBindings(true);
		parser.setProject(getJavaProject("P"));
		WorkingCopyOwner owner = new WorkingCopyOwner() {};
		this.workingCopies = new IJavaScriptUnit[3];
		this.workingCopies[0] = getWorkingCopy(
			"/P/src/X.js", 
			"public class X {\n" +
			"  public void foo(int i, String s) {\n" +
			"  }\n" +
			"}",
			owner, false);
		this.workingCopies[1] = getWorkingCopy(
			"/P/src/Y.js", 
			"public class Y extends X {\n" +
			"  void bar() {\n" +
			"    new Y() {};\n" +
			"  }\n" +
			"}",
			owner, false);
		this.workingCopies[2] = getWorkingCopy(
			"/P/src/I.js", 
			"public interface I {\n" +
			"  int BAR;\n" +
			"}",
			owner, false);
		IType typeX = this.workingCopies[0].getType("X");
		IJavaScriptElement[] elements = new IJavaScriptElement[] {
			typeX, 
			getClassFile("P", getExternalJCLPathString("1.5"), "java.lang", "Object.class").getType(),
			typeX.getFunction("foo", new String[] {"I", "QString;"}),
			this.workingCopies[2].getType("I").getField("BAR"),
			this.workingCopies[1].getType("Y").getFunction("bar", new String[0]).getType("", 1)
		};
		IBinding[] bindings = parser.createBindings(elements, null);
		assertBindingsEqual(
			"LX;\n" + 
			"Ljava/lang/Object;\n" + 
			"LX;.foo(ILjava/lang/String;)V\n" + 
			"LI;.BAR)I\n" + 
			"LY$50;",
			bindings);
	}
	
	/*
	 * Ensures that the correct IBindings are created for a given set of IJavaScriptElement
	 * (top level type)
	 */
	public void testCreateBindings02() throws JavaScriptModelException {
		IBinding[] bindings = createBindings(
			"public class X {\n" +
			"}",
			this.workingCopy.getType("X")
		);
		assertBindingsEqual(
			"LX;",
			bindings);
	}
	
	/*
	 * Ensures that the correct IBindings are created for a given set of IJavaScriptElement
	 * (member type)
	 */
	public void testCreateBindings03() throws JavaScriptModelException {
		IBinding[] bindings = createBindings(
			"public class X {\n" +
			"  public class Member {\n" +
			"  }\n" +
			"}",
			this.workingCopy.getType("X").getType("Member")
		);
		assertBindingsEqual(
			"LX$Member;",
			bindings);
	}
	
	/*
	 * Ensures that the correct IBindings are created for a given set of IJavaScriptElement
	 * (anonymous type)
	 */
	public void testCreateBindings04() throws JavaScriptModelException {
		IBinding[] bindings = createBindings(
			"public class X {\n" +
			"  void foo() {\n" +
			"    new X() {\n" +
			"    };\n" +
			"  }\n" +
			"}",
			this.workingCopy.getType("X").getFunction("foo", new String[0]).getType("", 1)
		);
		assertBindingsEqual(
			"LX$40;",
			bindings);
	}
	
	/*
	 * Ensures that the correct IBindings are created for a given set of IJavaScriptElement
	 * (local type)
	 */
	public void testCreateBindings05() throws JavaScriptModelException {
		IBinding[] bindings = createBindings(
			"public class X {\n" +
			"  void foo() {\n" +
			"    class Y {\n" +
			"    }\n" +
			"  }\n" +
			"}",
			this.workingCopy.getType("X").getFunction("foo", new String[0]).getType("Y", 1)
		);
		assertBindingsEqual(
			"LX$42$Y;",
			bindings);
	}
	
	/*
	 * Ensures that the correct IBindings are created for a given set of IJavaScriptElement
	 * (field)
	 */
	public void testCreateBindings06() throws JavaScriptModelException {
		IBinding[] bindings = createBindings(
			"public class X {\n" +
			"  int field;\n" +
			"}",
			this.workingCopy.getType("X").getField("field")
		);
		assertBindingsEqual(
			"LX;.field)I",
			bindings);
	}
	
	/*
	 * Ensures that the correct IBindings are created for a given set of IJavaScriptElement
	 * (method)
	 */
	public void testCreateBindings07() throws JavaScriptModelException {
		IBinding[] bindings = createBindings(
			"public class X {\n" +
			"  void foo() {}\n" +
			"}",
			this.workingCopy.getType("X").getFunction("foo", new String[0])
		);
		assertBindingsEqual(
			"LX;.foo()V",
			bindings);
	}
	
	/*
	 * Ensures that the correct IBindings are created for a given set of IJavaScriptElement
	 * (annotation declaration)
	 */
	public void testCreateBindings08() throws JavaScriptModelException {
		IBinding[] bindings = createBindings(
			"@interface X {\n" +
			"}",
			this.workingCopy.getType("X")
		);
		assertBindingsEqual(
			"LX;",
			bindings);
	}
	
	/*
	 * Ensures that the correct IBindings are created for a given set of IJavaScriptElement
	 * (enum declaration)
	 */
	public void testCreateBindings09() throws JavaScriptModelException {
		IBinding[] bindings = createBindings(
			"public enum X {\n" +
			"}",
			this.workingCopy.getType("X")
		);
		assertBindingsEqual(
			"LX;",
			bindings);
	}
	
	/*
	 * Ensures that the correct IBindings are created for a given set of IJavaScriptElement
	 * (annotation member declaration)
	 */
	public void testCreateBindings10() throws JavaScriptModelException {
		IBinding[] bindings = createBindings(
			"@interface X {\n" +
			"  int foo();\n" +
			"}",
			this.workingCopy.getType("X").getFunction("foo", new String[0])
		);
		assertBindingsEqual(
			"LX;.foo()I",
			bindings);
	}
	
	/*
	 * Ensures that the correct IBindings are created for a given set of IJavaScriptElement
	 * (enum constant)
	 */
	public void testCreateBindings11() throws JavaScriptModelException {
		IBinding[] bindings = createBindings(
			"public enum X {\n" +
			"  FOO;\n" +
			"}",
			this.workingCopy.getType("X").getField("FOO")
		);
		assertBindingsEqual(
			"LX;.FOO)LX;",
			bindings);
	}
	
	/*
	 * Ensures that the correct IBindings are created for a given set of IJavaScriptElement
	 * (import)
	 */
	public void testCreateBindings12() throws JavaScriptModelException {
		IBinding[] bindings = createBindings(
			"import java.io.*;\n" +
			"public class X implements Serializable {\n" +
			"  static final long serialVersionUID = 0;\n" +
			"}",
			this.workingCopy.getImport("java.io.*")
		);
		assertBindingsEqual(
			"java/io",
			bindings);
	}
	
	/*
	 * Ensures that the correct IBindings are created for a given set of IJavaScriptElement
	 * (import)
	 */
	public void testCreateBindings13() throws JavaScriptModelException {
		IBinding[] bindings = createBindings(
			"import java.io.Serializable;\n" +
			"public class X implements Serializable {\n" +
			"  static final long serialVersionUID = 0;\n" +
			"}",
			this.workingCopy.getImport("java.io.Serializable")
		);
		assertBindingsEqual(
			"Ljava/io/Serializable;",
			bindings);
	}
	
	/*
	 * Ensures that the correct IBindings are created for a given set of IJavaScriptElement
	 * (binary type)
	 */
	public void testCreateBindings15() throws CoreException {
		IBinding[] bindings = createBinaryBindings(
			"public class A {\n" +
			"}",
			getClassFile("/P/lib/A.class").getType()
		);
		assertBindingsEqual(
			"LA;",
			bindings);
	}
	
	/*
	 * Ensures that the correct IBindings are created for a given set of IJavaScriptElement
	 * (binary field)
	 */
	public void testCreateBindings16() throws CoreException {
		IBinding[] bindings = createBinaryBindings(
			"public class A {\n" +
			"  int field;\n" +
			"}",
			getClassFile("/P/lib/A.class").getType().getField("field")
		);
		assertBindingsEqual(
			"LA;.field)I",
			bindings);
	}
	
	/*
	 * Ensures that the correct IBindings are created for a given set of IJavaScriptElement
	 * (binary method)
	 */
	public void testCreateBindings17() throws CoreException {
		IBinding[] bindings = createBinaryBindings(
			"public class A {\n" +
			"  int foo(String s, boolean b) {\n" +
			"    return -1;\n" +
			"  }\n" +
			"}",
			getClassFile("/P/lib/A.class").getType().getFunction("foo", new String[] {"Ljava.lang.String;", "Z"})
		);
		assertBindingsEqual(
			"LA;.foo(Ljava/lang/String;Z)I",
			bindings);
	}
	
	/*
	 * Ensures that the correct IBindings are created for a given set of IJavaScriptElement
	 * (binary method)
	 * (regression test for bug 122650 ASTParser.createBindings(IJavaScriptElement[]) returns wrong element)
	 */
	public void testCreateBindings18() throws CoreException {
		IBinding[] bindings = createBinaryBindings(
			"public class A {\n" +
			"  <E> void foo(E e) {\n" +
			"  }\n" +
			"}",
			getClassFile("/P/lib/A.class").getType().getFunction("foo", new String[] {"TE;"})
		);
		assertBindingsEqual(
			"LA;.foo<E:Ljava/lang/Object;>(TE;)V",
			bindings);
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a field is correct.
	 */
	public void testField1() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"public class X {\n" +
			"  Object /*start*/field/*end*/;\n" +
			"}"
		);
		IBinding binding = ((VariableDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"field [in X [in [Working copy] X.java [in <default> [in src [in P]]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a field is correct.
	 */
	public void testField2() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"public class X {\n" +
			"  Object foo() {\n" +
			"    return new Object() {\n" +
			"      Object /*start*/field/*end*/;\n" +
			"    };\n" +
			"  }\n" +
			"}"
		);
		IBinding binding = ((VariableDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"field [in <anonymous #1> [in foo() [in X [in [Working copy] X.java [in <default> [in src [in P]]]]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a local type is correct.
	 */
	public void testLocalType() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"public class X {\n" +
			"  void foo() {\n" +
			"    /*start*/class Y {\n" +
			"    }/*end*/\n" +
			"  }\n" +
			"}"
		);
		IBinding binding = ((TypeDeclarationStatement) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"Y [in foo() [in X [in [Working copy] X.java [in <default> [in src [in P]]]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a local type
	 * and coming from a binding key resolution is correct.
	 */
	public void testLocalType2() throws CoreException {
		String filePath = "/P/src/Z.js";
		try {
			String contents = 
				"public class Z {\n" +
				"  void foo() {\n" +
				"    /*start*/class Local {\n" +
				"    }/*end*/\n" +
				"  }\n" +
				"}";
			createFile(filePath, contents);

			// Get the binding key
			ASTNode node = buildAST(contents, getCompilationUnit(filePath));
			IBinding binding = ((TypeDeclarationStatement) node).resolveBinding();
			String bindingKey = binding.getKey();
			
			// Resolve the binding key
			BindingRequestor requestor = new BindingRequestor();
			String[] bindingKeys = new String[] {bindingKey};
			resolveASTs(
				new IJavaScriptUnit[] {}, 
				bindingKeys,
				requestor,
				getJavaProject("P"),
				workingCopy.getOwner()
			);
			IBinding[] bindings = requestor.getBindings(bindingKeys);
			
			// Ensure the Java element is correct
			IJavaScriptElement element = bindings[0].getJavaElement();
			assertElementEquals(
				"Unexpected Java element",
				"Local [in foo() [in Z [in Z.java [in <default> [in src [in P]]]]]]",
				element
			);
			assertTrue("Element should exist", element.exists());
		} finally {
			deleteFile(filePath);
		}
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a local variable is correct.
	 * (regression test for bug 79610 IVariableBinding#getJavaElement() returns null for local variables)
	 */
	public void testLocalVariable1() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"public class X {\n" +
			"  void foo() {\n" +
			"    int /*start*/local/*end*/;\n" +
			"  }\n" +
			"}"
		);
		IBinding binding = ((VariableDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		IJavaScriptElement expected = getLocalVariable(this.workingCopy, "local", "local");
		assertEquals(
			"Unexpected Java element",
			expected,
			element
		);
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a local variable is correct.
	 * (regression test for bug 79610 IVariableBinding#getJavaElement() returns null for local variables)
	 */
	public void testLocalVariable2() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"public class X {\n" +
			"  void foo() {\n" +
			"    Object first, /*start*/second/*end*/, third;\n" +
			"  }\n" +
			"}"
		);
		IBinding binding = ((VariableDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		IJavaScriptElement expected = getLocalVariable(this.workingCopy, "second", "second");
		assertEquals(
			"Unexpected Java element",
			expected,
			element
		);
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a local variable is correct.
	 * (regression test for bug 80021 [1.5] CCE in VariableBinding.getJavaElement())
	 */
	public void testLocalVariable3() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"public class X {\n" +
			"  void foo(/*start*/int arg/*end*/) {\n" +
			"  }\n" +
			"}"
		);
		IBinding binding = ((VariableDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		IJavaScriptElement expected = getLocalVariable(this.workingCopy, "arg", "arg");
		assertEquals(
			"Unexpected Java element",
			expected,
			element
		);
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a member type is correct.
	 */
	public void testMemberType() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"public class X {\n" +
			"  /*start*/class Y {\n" +
			"  }/*end*/\n" +
			"}"
		);
		IBinding binding = ((TypeDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"Y [in X [in [Working copy] X.java [in <default> [in src [in P]]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a method is correct.
	 */
	public void testMethod01() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"public class X<K, V> {\n" +
			"  /*start*/void foo(int i, Object o, java.lang.String s, Class[] c, X<K, V> x) {\n" +
			"  }/*end*/\n" +
			"}"
		);
		IBinding binding = ((FunctionDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"foo(int, Object, java.lang.String, Class[], X<K,V>) [in X [in [Working copy] X.java [in <default> [in src [in P]]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a method is correct.
	 */
	public void testMethod02() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"public class X<K, V> {\n" +
			"  /*start*/void foo() {\n" +
			"  }/*end*/\n" +
			"}"
		);
		IBinding binding = ((FunctionDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"foo() [in X [in [Working copy] X.java [in <default> [in src [in P]]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a method is correct.
	 * (regression test for bug 78757 FunctionBinding.getJavaElement() returns null)
	 */
	public void testMethod03() throws JavaScriptModelException {
		IJavaScriptUnit otherWorkingCopy = null;
		try {
			otherWorkingCopy = getWorkingCopy(
				"/P/src/Y.js",
				"public class Y {\n" +
				"  void foo(int i, String[] args, java.lang.Class clazz) {}\n" +
				"}",
				this.workingCopy.getOwner(), 
				null
			);
			ASTNode node = buildAST(
				"public class X {\n" +
				"  void bar() {\n" +
				"    Y y = new Y();\n" +
				"    /*start*/y.foo(1, new String[0], getClass())/*end*/;\n" +
				"  }\n" +
				"}"
			);
			IBinding binding = ((FunctionInvocation) node).resolveMethodBinding();
			assertNotNull("No binding", binding);
			IJavaScriptElement element = binding.getJavaElement();
			assertElementEquals(
				"Unexpected Java element",
				"foo(int, String[], java.lang.Class) [in Y [in [Working copy] Y.java [in <default> [in src [in P]]]]]",
				element
			);
			assertTrue("Element should exist", element.exists());
		} finally {
			if (otherWorkingCopy != null)
				otherWorkingCopy.discardWorkingCopy();
		}
	}
	
	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a method is correct.
	 * (regression test for bug 81258 IFunctionBinding#getJavaElement() is null with inferred method parameterization)
	 */
	public void testMethod04() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"public class X {\n" + 
			"	void foo() {\n" + 
			"		/*start*/bar(new B<Object>())/*end*/;\n" + 
			"	}\n" + 
			"	<T extends Object> void bar(A<? extends T> arg) {\n" + 
			"	}\n" + 
			"}\n" + 
			"class A<T> {\n" + 
			"}\n" + 
			"class B<T> extends A<T> {	\n" + 
			"}"
		);
		IBinding binding = ((FunctionInvocation) node).resolveMethodBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"bar(A<? extends T>) [in X [in [Working copy] X.java [in <default> [in src [in P]]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a parameterized method is correct.
	 * (regression test for bug 82382 IFunctionBinding#getJavaElement() for method m(T t) in parameterized type Gen<T> is null)
	 */
	public void testMethod05() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"public class X<T> {\n" + 
			"    void m(T t) { }\n" + 
			"}\n" + 
			"\n" + 
			"class Y {\n" + 
			"    {\n" + 
			"        /*start*/new X<String>().m(\"s\")/*end*/;\n" + 
			"    }\n" + 
			"}"
		);
		IBinding binding = ((FunctionInvocation) node).resolveMethodBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"m(T) [in X [in [Working copy] X.java [in <default> [in src [in P]]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a method inside an annotation is correct.
	 * (regression test for bug 83300 [1.5] ClassCastException in #getJavaElement() on binding of annotation element)
	 */
	public void testMethod06() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"@X(/*start*/value/*end*/=\"Hello\", count=-1)\n" + 
			"@interface X {\n" + 
			"    String value();\n" + 
			"    int count();\n" + 
			"}"
		);
		IBinding binding = ((SimpleName) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"value() [in X [in [Working copy] X.java [in <default> [in src [in P]]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}
	
	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a method with array parameters is correct.
	 * (regression test for bug 88769 IFunctionBinding#getJavaElement() drops extra array dimensions and varargs
	 */
	public void testMethod07() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"public class X {\n" +
			"  /*start*/public int[] bar(int a[]) {\n" +
			"    return a;\n" +
			"  }/*end*/\n" +
			"}"
		);
		IBinding binding = ((FunctionDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"bar(int[]) [in X [in [Working copy] X.java [in <default> [in src [in P]]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a method with array parameters is correct.
	 * (regression test for bug 88769 IFunctionBinding#getJavaElement() drops extra array dimensions and varargs
	 */
	public void testMethod08() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"public class X {\n" +
			"  /*start*/public Object[] bar2(Object[] o[][]) [][] {\n" +
			"    return o;\n" +
			"  }/*end*/\n" +
			"}"
		);
		IBinding binding = ((FunctionDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"bar2(Object[][][]) [in X [in [Working copy] X.java [in <default> [in src [in P]]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a method with varargs parameters is correct.
	 * (regression test for bug 88769 IFunctionBinding#getJavaElement() drops extra array dimensions and varargs
	 */
	public void testMethod09() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"public class X {\n" +
			"  /*start*/public void bar3(Object... objs) {\n" +
			"  }/*end*/\n" +
			"}"
		);
		IBinding binding = ((FunctionDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"bar3(Object[]) [in X [in [Working copy] X.java [in <default> [in src [in P]]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	/*
	 * Ensures that getting the IJavaScriptElement of an IBinding representing a method in an anonymous type
	 * doesn't throw a ClassCastException if there is a syntax error.
	 * (regression test for bug 149853 CCE in IFunctionBinding#getJavaElement() for recovered anonymous type)
	 */
	public void testMethod10() throws CoreException {
		try {
			// use a compilation unit instead of a working copy to use the ASTParser instead of reconcile
			createFile(
				"/P/src/Test.js",
				"public class X {\n" + 
				"        void test() {\n" + 
				"                new Object() {\n" + 
				"                        /*start*/public void yes() {\n" + 
				"                                System.out.println(\"hello world\");\n" + 
				"                        }/*end*/\n" + 
				"                } // missing semicolon;\n" + 
				"        }\n" + 
				"}"
			);
			IJavaScriptUnit cu = getCompilationUnit("/P/src/Test.js");
			
			ASTNode node = buildAST(null/*use existing contents*/, cu, false/*don't report errors*/, true/*statement recovery*/);
			IBinding binding = ((FunctionDeclaration) node).resolveBinding();
			assertNotNull("No binding", binding);
			IJavaScriptElement element = binding.getJavaElement();
			assertElementEquals(
				"Unexpected Java element",
				"yes() [in <anonymous #1> [in test() [in X [in Test.java [in <default> [in src [in P]]]]]]]",
				element
			);
			assertTrue("Element should exist", element.exists());
		} finally {
			deleteFile("/P/src/Test.js");
		}
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a package is correct.
	 */
	public void testPackage1() throws CoreException {
		ASTNode node = buildAST(
			"public class X {\n" +
			"  /*start*/java.lang/*end*/.String field;\n" +
			"}"
		);
		IBinding binding = ((QualifiedName) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"java.lang [in "+ getExternalJCLPathString("1.5") + "]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a package is correct
	 * (case of default package)
	 */
	public void testPackage2() throws CoreException {
		ASTNode node = buildAST(
			"/*start*/public class X {\n" +
			"}/*end*/"
		);
		ITypeBinding typeBinding = ((TypeDeclaration) node).resolveBinding();
		assertNotNull("No binding", typeBinding);
		IPackageBinding binding = typeBinding.getPackage();
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"<default> [in src [in P]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}
	
	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a parameterized binary type is correct.
	 * (regression test for bug 78087 [dom] TypeBinding#getJavaElement() throws IllegalArgumentException for parameterized or raw reference to binary type)
	 */
	public void testParameterizedBinaryType() throws CoreException {
		ASTNode node = buildAST(
			"public class X {\n" +
			"  /*start*/Comparable<String>/*end*/ field;\n" +
			"}"
		);
		IBinding binding = ((Type) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"Comparable [in Comparable.class [in java.lang [in "+ getExternalJCLPathString("1.5") + "]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a parameterized binary method is correct.
	 * (regression test for bug 88892 [1.5] IFunctionBinding#getJavaElement() returns nonexistent IMethods (wrong parameter types))
	 */
	public void testParameterizedBinaryMethod() throws CoreException {
		ASTNode node = buildAST(
			"public class X extends p.Y<String> {\n" +
			"  public X(String s) {\n" +
			"    /*start*/super(s);/*end*/\n" +
			"  }\n" +
			"}"		
		);
		IBinding binding = ((SuperConstructorInvocation) node).resolveConstructorBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"Y(T) [in Y [in Y.class [in p [in lib.jar [in P]]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a raw binary type is correct.
	 * (regression test for bug 78087 [dom] TypeBinding#getJavaElement() throws IllegalArgumentException for parameterized or raw reference to binary type)
	 */
	public void testRawBinaryType() throws CoreException {
		ASTNode node = buildAST(
			"public class X {\n" +
			"  /*start*/Comparable/*end*/ field;\n" +
			"}"
		);
		IBinding binding = ((Type) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"Comparable [in Comparable.class [in java.lang [in "+ getExternalJCLPathString("1.5") + "]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}
	
	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a top level type is correct.
	 */
	public void testTopLevelType1() throws JavaScriptModelException {
		ASTNode node = buildAST(
			"/*start*/public class X {\n" +
			"}/*end*/"
		);
		IBinding binding = ((TypeDeclaration) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"X [in [Working copy] X.java [in <default> [in src [in P]]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a top level type is correct
	 * (the top level type being in another compilation unit)
	 */
	public void testTopLevelType2() throws CoreException {
		try {
			createFile(
				"/P/src/Y.js",
				"public class Y {\n" +
				"}"
			);
			ASTNode node = buildAST(
				"public class X extends /*start*/Y/*end*/ {\n" +
				"}"
			);
			IBinding binding = ((Type) node).resolveBinding();
			assertNotNull("No binding", binding);
			IJavaScriptElement element = binding.getJavaElement();
			assertElementEquals(
				"Unexpected Java element",
				"Y [in Y.java [in <default> [in src [in P]]]]",
				element
			);
			assertTrue("Element should exist", element.exists());
		} finally {
			deleteFile("/P/src/Y.js");
		}
	}

	/*
	 * Ensures that the IJavaScriptElement of an IBinding representing a top level type is correct
	 * (the top level type being in a jar)
	 */
	public void testTopLevelType3() throws CoreException {
		ASTNode node = buildAST(
			"public class X {\n" +
			"  /*start*/String/*end*/ field;\n" +
			"}"
		);
		IBinding binding = ((Type) node).resolveBinding();
		assertNotNull("No binding", binding);
		IJavaScriptElement element = binding.getJavaElement();
		assertElementEquals(
			"Unexpected Java element",
			"String [in String.class [in java.lang [in "+ getExternalJCLPathString("1.5") + "]]]",
			element
		);
		assertTrue("Element should exist", element.exists());
	}

	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=160637
	 */
	public void testCreateBindings19() throws CoreException {
		IBinding[] bindings = createBinaryBindings(
			"public class A {\n" +
			"  String foo(String s) {\n" +
			"		return null;\n" +
			"  }\n" +
			"}",
			getClassFile("/P/lib/A.class").getType().getFunction("foo", new String[] {"Ljava.lang.String;"})
		);
		assertNotNull("No bindings", bindings);
		assertEquals("Wrong size", 1, bindings.length);
		assertTrue("Not a method binding", bindings[0] instanceof IFunctionBinding);
		assertBindingsEqual(
			"LA;.foo(Ljava/lang/String;)Ljava/lang/String;",
			bindings);
	}
}
