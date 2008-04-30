/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.model;

import java.util.Hashtable;

import org.eclipse.wst.jsdt.core.*;
import org.eclipse.wst.jsdt.core.eval.IEvaluationContext;
import org.eclipse.wst.jsdt.internal.codeassist.CompletionEngine;
import org.eclipse.wst.jsdt.internal.codeassist.RelevanceConstants;

import junit.framework.*;

public class CompletionTests_1_5 extends AbstractJavaModelCompletionTests implements RelevanceConstants {
	static {
//		TESTS_NAMES = new String[]{"test0040"};
	}
public CompletionTests_1_5(String name) {
	super(name);
}
public void setUpSuite() throws Exception {
	if (COMPLETION_PROJECT == null)  {
		COMPLETION_PROJECT = setUpJavaProject("Completion", "1.5");
	} else {
		setUpProjectCompliance(COMPLETION_PROJECT, "1.5");
	}
	super.setUpSuite();
}

public void tearDownSuite() throws Exception {
	super.tearDownSuite();
}
public static Test suite() {
	return buildModelTestSuite(CompletionTests_1_5.class);
}
private IJavaScriptUnit[] getExternalQQTypes() throws JavaScriptModelException {
	IJavaScriptUnit[] units = new IJavaScriptUnit[6];
	
	units[0] = getWorkingCopy(
		"/Completion/src3/pkgstaticimport/QQType1.js",
		"package pkgstaticimport;\n"+
		"\n"+
		"public class QQType1 {\n"+
		"	public class Inner1 {}\n"+
		"	public static class Inner2 {}\n"+
		"	protected class Inner3 {}\n"+
		"	protected static class Inner4 {}\n"+
		"	private class Inner5 {}\n"+
		"	private static class Inner6 {}\n"+
		"	class Inner7 {}\n"+
		"	static class Inner8 {}\n"+
		"}");
	
	units[1] = getWorkingCopy(
		"/Completion/src3/pkgstaticimport/QQType3.js",
		"package pkgstaticimport;\n"+
		"\n"+
		"public class QQType3 extends QQType1 {\n"+
		"	\n"+
		"}");
	
	units[2] = getWorkingCopy(
		"/Completion/src3/pkgstaticimport/QQType4.js",
		"package pkgstaticimport;\n"+
		"\n"+
		"public class QQType4 {\n"+
		"	public int zzvarzz1;\n"+
		"	public static int zzvarzz2;\n"+
		"	protected int zzvarzz3;\n"+
		"	protected static int zzvarzz4;\n"+
		"	private int zzvarzz5;\n"+
		"	private static int zzvarzz6;\n"+
		"	int zzvarzz7;\n"+
		"	static int zzvarzz8;\n"+
		"}");
	
	units[3] = getWorkingCopy(
		"/Completion/src3/pkgstaticimport/QQType6.js",
		"package pkgstaticimport;\n"+
		"\n"+
		"public class QQType6 extends QQType4 {\n"+
		"	\n"+
		"}");
	
	units[4] = getWorkingCopy(
		"/Completion/src3/pkgstaticimport/QQType7.js",
		"package pkgstaticimport;\n"+
		"\n"+
		"public class QQType7 {\n"+
		"	public void zzfoozz1(){};\n"+
		"	public static void zzfoozz2(){};\n"+
		"	protected void zzfoozz3(){};\n"+
		"	protected static void zzfoozz4(){};\n"+
		"	private void zzfoozz5(){};\n"+
		"	private static void zzfoozz6(){};\n"+
		"	void zzfoozz7(){};\n"+
		"	static void zzfoozz8(){};\n"+
		"}");
	
	units[5] = getWorkingCopy(
		"/Completion/src3/pkgstaticimport/QQType9.js",
		"package pkgstaticimport;\n"+
		"\n"+
		"public class QQType9 extends QQType7 {\n"+
		"	\n"+
		"}");
	
	return units;
}
public void test0001() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0001", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "X<St";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("should have one class",
		"element:String    completion:String    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED),
		requestor.getResults());
}
public void test0002() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0002", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "X<Ob";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("should have one class",
		"element:Object    completion:Object    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED),
		requestor.getResults());
}
public void test0003() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0003", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "X<St";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("should have one class",
		"element:String    completion:String    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_EXACT_EXPECTED_TYPE + R_NON_RESTRICTED),
		requestor.getResults());
}
public void test0004() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0004", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "X<XZ";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("should have one class",
		"element:XZX    completion:XZX    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED)+"\n"+
		"element:XZXSuper    completion:XZXSuper    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_EXACT_EXPECTED_TYPE + R_NON_RESTRICTED),
		requestor.getResults());
}
public void test0005() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0005/Test.js",
            "package test0005;\n" +
            "\n" +
            "public class Test {\n" +
            "	void foo() {\n" +
            "		X<Object>.Y<St\n" +
            "	}\n" +
            "}\n" +
            "\n" +
            "class X<T> {\n" +
            "	public class Y<U> {\n" +
            "	}\n" +
            "}",
            "Y<St");
    
    assertResults(
            "expectedTypesSignatures={Ljava.lang.Object;}\n" +
            "expectedTypesKeys={Ljava/lang/Object;}",
            result.context);
    
    assertResults(
            "String[TYPE_REF]{String, java.lang, Ljava.lang.String;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED)+"}",
            result.proposals);
}
public void test0006() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0006/Test.js",
            "package test0006;\n" +
            "\n" +
            "public class Test {\n" +
            "	void foo() {\n" +
            "		X<String>.Y<Ob\n" +
            "	}\n" +
            "}\n" +
            "\n" +
            "class X<T> {\n" +
            "	public class Y<U> {\n" +
            "	}\n" +
            "}",
            "Y<Ob");
    
    assertResults(
            "expectedTypesSignatures={Ljava.lang.Object;}\n" +
            "expectedTypesKeys={Ljava/lang/Object;}",
            result.context);
    
    assertResults(
            "Object[TYPE_REF]{Object, java.lang, Ljava.lang.Object;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) +"}",
            result.proposals);
}
public void test0007() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0007/Test.js",
            "package test0007;\n" +
            "\n" +
            "public class Test {\n" +
            "	void foo() {\n" +
            "		X<Object>.Y<St\n" +
            "	}\n" +
            "}\n" +
            "\n" +
            "class X<T> {\n" +
            "	public class Y<U extends String> {\n" +
            "	}\n" +
            "}",
            "Y<St");
    
    assertResults(
            "expectedTypesSignatures={Ljava.lang.String;}\n" +
            "expectedTypesKeys={Ljava/lang/String;}",
            result.context);
    
    assertResults(
            "String[TYPE_REF]{String, java.lang, Ljava.lang.String;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_EXACT_EXPECTED_TYPE + R_NON_RESTRICTED) +"}",
            result.proposals);
}
public void test0008() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0008/Test.js",
            "package test0008;\n" +
            "\n" +
            "public class Test {\n" +
            "	void foo() {\n" +
            "		X<Object>.Y<XY\n" +
            "	}\n" +
            "}\n" +
            "\n" +
            "class X<T> {\n" +
            "	public class Y<U extends XYXSuper> {\n" +
            "	}\n" +
            "}\n" +
            "class XYX {\n" +
            "	\n" +
            "}\n" +
            "class XYXSuper {\n" +
            "	\n" +
            "}",
            "Y<XY");
    
    assertResults(
            "expectedTypesSignatures={Ltest0008.XYXSuper;}\n" +
            "expectedTypesKeys={Ltest0008/Test~XYXSuper;}",
            result.context);
    
    assertResults(
            "XYX[TYPE_REF]{XYX, test0008, Ltest0008.XYX;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED)+"}\n"+
			"XYXSuper[TYPE_REF]{XYXSuper, test0008, Ltest0008.XYXSuper;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_EXACT_EXPECTED_TYPE + R_NON_RESTRICTED)+"}",
			result.proposals);
}
public void test0009() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0009", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "/**/T_";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("should have one class",
		"element:T_1    completion:T_1    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED)+"\n"+
		"element:T_2    completion:T_2    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED),
		requestor.getResults());
}
public void test0010() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0010", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "/**/T_";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("should have one class",
		"element:T_1    completion:T_1    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED)+"\n"+
		"element:T_2    completion:T_2    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED)+"\n"+
		"element:T_3    completion:T_3    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED)+"\n"+
		"element:T_4    completion:T_4    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED),
		requestor.getResults());
}
public void test0011() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0011/Test.js",
            "package test0011;\n"+
            "\n"+
            "public class Test <T extends Z0011<Object>.Y001> {\n"+
            "\n"+
            "}\n"+
            "class Z0011<T0011> {\n"+
            "	public class Y0011 {\n"+
            "	}\n"+
            "}",
            ".Y001");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "Z0011<java.lang.Object>.Y0011[TYPE_REF]{Y0011, test0011, Ltest0011.Z0011<Ljava.lang.Object;>.Y0011;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED)+"}",
            result.proposals);
}
public void test0012() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0012/Test.js",
            "package test0012;\n"+
            "\n"+
            "public class Test {\n"+
            "	public Z0012<Object>.Y001\n"+
            "}\n"+
            "class Z0012<T0012> {\n"+
            "	public class Y0012 {\n"+
            "	}\n"+
            "}",
            ".Y001");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "Z0012<java.lang.Object>.Y0012[TYPE_REF]{Y0012, test0012, Ltest0012.Z0012<Ljava.lang.Object;>.Y0012;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) +"}",
            result.proposals);
}
public void test0013() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0013/Test.js",
            "package test0013;\n"+
            "\n"+
            "public class Test {\n"+
            "	public Z0013<Object>.Y001 foo() {}\n"+
            "}\n"+
            "class Z0013<T0013> {\n"+
            "	public class Y0013 {\n"+
            "	}\n"+
            "}",
            ".Y001");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "Z0013<java.lang.Object>.Y0013[TYPE_REF]{Y0013, test0013, Ltest0013.Z0013<Ljava.lang.Object;>.Y0013;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) +"}",
            result.proposals);
}
public void test0014() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0014/Test.js",
            "package test0014;\n" +
            "\n" +
            "public class Test extends Z0014<Object>.Y001 {\n" +
            "}\n" +
            "class Z0014<T0014> {\n" +
            "	public class Y0014 {\n" +
            "	}\n" +
            "}",
            ".Y001");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "Z0014<java.lang.Object>.Y0014[TYPE_REF]{Y0014, test0014, Ltest0014.Z0014<Ljava.lang.Object;>.Y0014;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_CLASS + R_NON_RESTRICTED) +"}",
            result.proposals);
}
public void test0015() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0015/Test.js",
            "package test0015;\n" +
            "\n" +
            "public class Test implements Z0015<Object>.Y001 {\n" +
            "}\n" +
            "class Z0015<T0015> {\n" +
            "	public class Y0015 {\n" +
            "	}\n" +
            "	public interface Y0015I {\n" +
            "	}\n" +
            "}",
            ".Y001");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "Z0015<java.lang.Object>.Y0015[TYPE_REF]{Y0015, test0015, Ltest0015.Z0015<Ljava.lang.Object;>.Y0015;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) +"}\n" +
            "Z0015<java.lang.Object>.Y0015I[TYPE_REF]{Y0015I, test0015, Ltest0015.Z0015<Ljava.lang.Object;>.Y0015I;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_INTERFACE+ R_NON_RESTRICTED) +"}",
            result.proposals);
}
public void test0016() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0016/Test.js",
            "package test0016;\n" +
            "\n" +
            "public class Test implements  {\n" +
            "	void foo(Z0016<Object>.Y001) {\n" +
            "		\n" +
            "	}\n" +
            "}\n" +
            "class Z0016<T0016> {\n" +
            "	public class Y0016 {\n" +
            "	}\n" +
            "}",
            ".Y001");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "Z0016<java.lang.Object>.Y0016[TYPE_REF]{Y0016, test0016, Ltest0016.Z0016<Ljava.lang.Object;>.Y0016;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) +"}",
            result.proposals);
}
public void test0017() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0017/Test.js",
            "package test0017;\n" +
            "\n" +
            "public class Test implements  {\n" +
            "	void foo() throws Z0017<Object>.Y001{\n" +
            "		\n" +
            "	}\n" +
            "}\n" +
            "class Z0017<T0017> {\n" +
            "	public class Y0017 {\n" +
            "	}\n" +
            "}",
            ".Y001");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "Z0017<java.lang.Object>.Y0017[TYPE_REF]{Y0017, test0017, Ltest0017.Z0017<Ljava.lang.Object;>.Y0017;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) +"}",
            result.proposals);
}
public void test0018() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0018/Test.js",
            "package test0018;\n" +
            "\n" +
            "public class Test {\n" +
            "	<T extends Z0018<Object>.Y001> void foo() {\n" +
            "		\n" +
            "	}\n" +
            "}\n" +
            "class Z0018<T0018> {\n" +
            "	public class Y0018 {\n" +
            "	}\n" +
            "}",
            ".Y001");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "Z0018<java.lang.Object>.Y0018[TYPE_REF]{Y0018, test0018, Ltest0018.Z0018<Ljava.lang.Object;>.Y0018;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) +"}",
            result.proposals);
}
public void test0019() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0019/Test.js",
            "package test0019;\n" +
            "\n" +
            "public class Test {\n" +
            "	<T extends Z0019<Object>.Y001\n" +
            "}\n" +
            "class Z0019<T0019> {\n" +
            "	public class Y0019 {\n" +
            "	}\n" +
            "}",
            ".Y001");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "Z0019<java.lang.Object>.Y0019[TYPE_REF]{Y0019, test0019, Ltest0019.Z0019<Ljava.lang.Object;>.Y0019;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) +"}",
            result.proposals);
}
public void test0020() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0020/Test.js",
            "package test0020;\n"+
            "\n"+
            "public class Test {\n"+
            "	void foo() {\n"+
            "		Z0020<Object>.Y002\n"+
            "	}\n"+
            "}\n"+
            "class Z0020<T0020> {\n"+
            "	public class Y0020 {\n"+
            "	}\n"+
            "}",
            ".Y002");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "Z0020<java.lang.Object>.Y0020[TYPE_REF]{Y0020, test0020, Ltest0020.Z0020<Ljava.lang.Object;>.Y0020;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) +"}",
            result.proposals);
}
public void test0021() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0021/Test.js",
		"package test0021;\n" +
		"\n" +
		"public class Test {\n" +
		"	<T extends Z0021Z> void foo() {\n" +
		"		this.<Z0021>foo();\n" +
		"	}\n" +
		"}\n" +
		"class Z0021Z {\n" +
		"	\n" +
		"}\n" +
		"class Z0021ZZ {\n" +
		"	\n" +
		"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "<Z0021";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
	
	assertResults(
			"Z0021Z[TYPE_REF]{Z0021Z, test0021, Ltest0021.Z0021Z;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
			"Z0021ZZ[TYPE_REF]{Z0021ZZ, test0021, Ltest0021.Z0021ZZ;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
public void test0022() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0022/Test.js",
		"package test0022;\n" +
		"\n" +
		"public class Test {\n" +
		"	void foo() {\n" +
		"		new Z0022<Z0022Z>foo();\n" +
		"	}\n" +
		"}\n" +
		"class Z0022<T extends Z0022ZZ> {\n" +
		"	\n" +
		"}\n" +
		"class Z0022ZZ {\n" +
		"	\n" +
		"}\n" +
		"class Z0022ZZZ {\n" +
		"	\n" +
		"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "<Z0022Z";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
	
	assertResults(
			"Z0022ZZZ[TYPE_REF]{Z0022ZZZ, test0022, Ltest0022.Z0022ZZZ;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
			"Z0022ZZ[TYPE_REF]{Z0022ZZ, test0022, Ltest0022.Z0022ZZ;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_EXACT_EXPECTED_TYPE + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
public void test0023() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0023", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "<St";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("should have one class",
		"",
		requestor.getResults());
}
public void test0024() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0024", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "<St";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("should have one class",
		"",
		requestor.getResults());
}
public void test0025() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0025", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "<St";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("should have one class",
		"element:String    completion:String    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED),
		requestor.getResults());
}
public void test0026() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0026/Test.js",
		"package test0026;\n" +
		"\n" +
		"public class Test {\n" +
		"	Z0026<String, String>.Z0026Z.Z0026ZZ<St, String> var;\n" +
		"}\n" +
		"class Z0026 <T1 extends String, T2 extends String>{\n" +
		"	public class Z0026Z {\n" +
		"		public class Z0026ZZ <T3, T4 extends String>{\n" +
		"			\n" +
		"		}\n" +
		"	} \n" +
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "Z<St";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
	
	assertResults(
			"String[TYPE_REF]{String, java.lang, Ljava.lang.String;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
public void test0027() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src3/test0026/Test.js",
		"package test0027;\n" +
		"\n" +
		"public class Test {\n" +
		"	Z0027<St, String>.Z0027Z.Z0027ZZ<String, String> var;\n" +
		"}\n" +
		"class Z0027 <T1, T2 extends String>{\n" +
		"	public class Z0027Z {\n" +
		"		public class Z0027ZZ <T3 extends String, T4 extends String>{\n" +
		"			\n" +
		"		}\n" +
		"	} \n" +
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "7<St";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
	
	assertResults(
			"String[TYPE_REF]{String, java.lang, Ljava.lang.String;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
			requestor.getResults());
	
	
}
public void test0028() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0028", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "<St";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("should have one class",
		"element:String    completion:String    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_EXACT_EXPECTED_TYPE + R_NON_RESTRICTED),
		requestor.getResults());
}
public void test0029() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0029/Test.js",
            "package test0029;\n"+
            "\n"+
            "public class Test {\n"+
            "	public class Inner {\n"+
            "		/**/Inner2<Inner2<Object>> stack= new Inner2<Inner2<Object>>();\n"+
            "	}\n"+
            "	class Inner2<T>{\n"+
            "	}\n"+
            "}",
            "/**/Inner2");
    
    assertResults(
            "Inner2[POTENTIAL_METHOD_DECLARATION]{Inner2, Ltest0029.Test$Inner;, ()V, Inner2, null, "+(R_DEFAULT + R_INTERESTING + R_NON_RESTRICTED)+"}\n"+
            "Test.Inner2<T>[TYPE_REF]{Inner2, test0029, Ltest0029.Test$Inner2<TT;>;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED + R_NON_RESTRICTED)+"}",
            result.proposals);
}
public void test0030() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0030", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "ZZ";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("unexpected result",
		"element:ZZX    completion:ZZX    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED)+ "\n" +
		"element:ZZY    completion:ZZY    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED),
		requestor.getResults());
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=72501
 */
public void test0031() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0031/Test.js",
            "package test0031;\n" +
            "\n" +
            "public class Test <T> {\n" +
            "	class Y {}\n" +
            "		void foo(){\n" +
            "			Test<T>.Y<Stri\n" +
            "		}\n" +
            "	}\n" +
            "}",
            "Stri");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "",
            result.proposals);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59082
 */
public void test0032() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0032", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "Stri";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("unexpected result",
		"element:String    completion:String    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED),
		requestor.getResults());
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59082
 */
public void test0033() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0033", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "Stri";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("unexpected result",
		"element:String    completion:String    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED),
		requestor.getResults());
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59082
 */
public void test0034() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0034", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "Stri";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("unexpected result",
		"",
		requestor.getResults());
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59082
 */
public void test0035() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0035", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "Stri";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("unexpected result",
		"",
		requestor.getResults());
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59082
 */
public void test0036() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0036", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "Stri";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("unexpected result",
		"",
		requestor.getResults());
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59082
 */
public void test0037() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0037", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "Stri";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("unexpected result",
		"",
		requestor.getResults());
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59082
 */
public void test0038() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0038", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "Stri";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("unexpected result",
		"",
		requestor.getResults());
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59082
 */
public void test0039() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0039", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "Stri";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("unexpected result",
		"",
		requestor.getResults());
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59082
 */
public void test0040() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0040/Test.js",
            "package test0040;\n" +
            "\n" +
            "public class Test <T> {\n" +
            "	public class Y {\n" +
            "		public class Z <U>{\n" +
            "			\n" +
            "		}\n" +
            "	}\n" +
            "	Test<Object>.Y.Z<Stri\n" +
            "}",
            "Stri");
    
    assertResults(
            "expectedTypesSignatures={Ljava.lang.Object;}\n" +
            "expectedTypesKeys={Ljava/lang/Object;}",
            result.context);
    
    assertResults(
            "String[TYPE_REF]{String, java.lang, Ljava.lang.String;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXPECTED_TYPE  + R_UNQUALIFIED + R_NON_RESTRICTED) +"}",
            result.proposals);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59082
 */
public void test0041() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0041/Test.js",
            "package test0041;\n" +
            "\n" +
            "public class Test <T> {\n" +
            "	public class Y {\n" +
            "		public class Z <U> {\n" +
            "			\n" +
            "		}\n" +
            "	}\n" +
            "	void foo() {\n" +
            "		Test<Object>.Y.Z<Stri\n" +
            "	}\n" +
            "}",
            "Stri");
    
    assertResults(
            "expectedTypesSignatures={Ljava.lang.Object;}\n" +
            "expectedTypesKeys={Ljava/lang/Object;}",
            result.context);
    
    assertResults(
            "String[TYPE_REF]{String, java.lang, Ljava.lang.String;, null, null, "+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXPECTED_TYPE + R_UNQUALIFIED  + R_NON_RESTRICTED) +"}",
            result.proposals);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59082
 */
public void test0042() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0042/Test.js",
            "package test0042;\n" +
            "\n" +
            "public class Test <T> {\n" +
            "	public class Y {\n" +
            "		public class Z {\n" +
            "			\n" +
            "		}\n" +
            "	}\n" +
            "	Test<Object>.Y.Z<Stri\n" +
            "}",
            "Stri");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "",
            result.proposals);

}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59082
 */
public void test0043() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0043/Test.js",
            "package test0043;\n" +
            "\n" +
            "public class Test <T> {\n" +
            "	public class Y {\n" +
            "		public class Z {\n" +
            "			\n" +
            "		}\n" +
            "	}\n" +
            "	void foo() {\n" +
            "		Test<Object>.Y.Z<Stri\n" +
            "	}\n" +
            "}",
            "Stri");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "",
            result.proposals);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59082
 */
public void test0044() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0044/Test.js",
            "package test0044;\n" +
            "\n" +
            "public class Test <T> {\n" +
            "	public class Y {\n" +
            "		public class Z <U>{\n" +
            "			\n" +
            "		}\n" +
            "	}\n" +
            "	Test<Object>.Y.Z<Object, Stri\n" +
            "}",
            "Stri");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "",
            result.proposals);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59082
 */
public void test0045() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0045/Test.js",
            "package test0045;\n" +
            "\n" +
            "public class Test <T> {\n" +
            "	public class Y {\n" +
            "		public class Z <U>{\n" +
            "			\n" +
            "		}\n" +
            "	}\n" +
            "	void foo() {\n" +
            "		Test<Object>.Y.Z<Object, Stri\n" +
            "	}\n" +
            "}",
            "Stri");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "",
            result.proposals);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59082
 */
public void test0046() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0046/Test.js",
            "package test0046;\n" +
            "\n" +
            "public class Test <T> {\n" +
            "	public class Y {\n" +
            "		public class Z <U>{\n" +
            "			\n" +
            "		}\n" +
            "	}\n" +
            "	Test<Object>.Y.Z<Object, Stri, Object> x;\n" +
            "}",
            "Stri");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "",
            result.proposals);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59082
 */
public void test0047() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0047/Test.js",
            "package test0047;\n" +
            "\n" +
            "public class Test <T> {\n" +
            "	public class Y {\n" +
            "		public class Z <U>{\n" +
            "			\n" +
            "		}\n" +
            "	}\n" +
            "	void foo() {\n" +
            "		Test<Object>.Y.Z<Object, Stri, Object> x;\n" +
            "	}\n" +
            "}",
            "Stri");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "",
            result.proposals);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=75455
 */
public void test0048() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0048", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "l.ba";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("unexpected result",
		"element:bar    completion:bar()    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_NON_STATIC + R_NON_RESTRICTED),
		requestor.getResults());
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=75455
 */
public void test0049() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0049", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "l.ba";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("unexpected result",
		"element:bar    completion:bar()    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_NON_STATIC + R_NON_RESTRICTED),
		requestor.getResults());
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74753
 */
public void test0050() throws JavaScriptModelException {
	CompletionTestsRequestor requestor = new CompletionTestsRequestor();
	IJavaScriptUnit cu = getCompilationUnit("Completion", "src3", "test0050", "Test.js");
	
	String str = cu.getSource();
	String completeBehind = "Test<T_0050";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);
	
	assertEquals("unexpected result",
		"element:T_0050    completion:T_0050    relevance:"+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED + R_NON_RESTRICTED),
		requestor.getResults());
}

public void test0051() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	IJavaScriptUnit[] qqTypes = null;
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		qqTypes = this.getExternalQQTypes();
		
		this.wc = getWorkingCopy(
				"/Completion/src3/test0051/Test.js",
				"package test0051;\n"+
				"import static pkgstaticimport.QQType1.*;\n"+
				"public class Test {\n"+
				"	void foo() {\n"+
				"		Inner\n"+
				"	}\n"+
				"}");
		
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
	
		String str = this.wc.getSource();
		String completeBehind = "Inner";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.wc.codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"QQType1.Inner1[TYPE_REF]{pkgstaticimport.QQType1.Inner1, pkgstaticimport, Lpkgstaticimport.QQType1$Inner1;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"QQType1.Inner2[TYPE_REF]{Inner2, pkgstaticimport, Lpkgstaticimport.QQType1$Inner2;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		this.discardWorkingCopies(qqTypes);
		
		JavaScriptCore.setOptions(oldOptions);
	}
}
public void test0052() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	IJavaScriptUnit[] qqTypes = null;
	IJavaScriptUnit qqType2 = null;
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		qqTypes = this.getExternalQQTypes();
		
		qqType2 = getWorkingCopy(
				"/Completion/src3/test0052/QQType2.js",
				"package test0052;\n"+
				"public class QQType2 {\n"+
				"	public class Inner1 {}\n"+
				"	public static class Inner2 {}\n"+
				"	protected class Inner3 {}\n"+
				"	protected static class Inner4 {}\n"+
				"	private class Inner5 {}\n"+
				"	private static class Inner6 {}\n"+
				"	class Inner7 {}\n"+
				"	static class Inner8 {}\n"+
				"}");
		
		this.wc = getWorkingCopy(
				"/Completion/src3/test0052/Test.js",
				"package test0052;\n"+
				"import static test0052.QQType2.*;\n"+
				"public class Test {\n"+
				"	void foo() {\n"+
				"		Inner\n"+
				"	}\n"+
				"}");
		
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
	
		String str = this.wc.getSource();
		String completeBehind = "Inner";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.wc.codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"QQType1.Inner1[TYPE_REF]{pkgstaticimport.QQType1.Inner1, pkgstaticimport, Lpkgstaticimport.QQType1$Inner1;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"QQType1.Inner2[TYPE_REF]{pkgstaticimport.QQType1.Inner2, pkgstaticimport, Lpkgstaticimport.QQType1$Inner2;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"QQType2.Inner1[TYPE_REF]{test0052.QQType2.Inner1, test0052, Ltest0052.QQType2$Inner1;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"QQType2.Inner3[TYPE_REF]{test0052.QQType2.Inner3, test0052, Ltest0052.QQType2$Inner3;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"QQType2.Inner7[TYPE_REF]{test0052.QQType2.Inner7, test0052, Ltest0052.QQType2$Inner7;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"QQType2.Inner2[TYPE_REF]{Inner2, test0052, Ltest0052.QQType2$Inner2;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"QQType2.Inner4[TYPE_REF]{Inner4, test0052, Ltest0052.QQType2$Inner4;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"QQType2.Inner8[TYPE_REF]{Inner8, test0052, Ltest0052.QQType2$Inner8;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		this.discardWorkingCopies(qqTypes);
		if(qqType2 != null) {
			qqType2.discardWorkingCopy();
		}
		
		JavaScriptCore.setOptions(oldOptions);
	}
}
public void test0053() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	IJavaScriptUnit[] qqTypes = null;
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		qqTypes = this.getExternalQQTypes();
		
		this.wc = getWorkingCopy(
				"/Completion/src3/test0053/Test.js",
				"package test0053;\n"+
				"import static pkgstaticimport.QQType1.*;\n"+
				"public class Test extends pkgstaticimport.QQType1 {\n"+
				"	void foo() {\n"+
				"		Inner\n"+
				"	}\n"+
				"}");
		
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
	
		String str = this.wc.getSource();
		String completeBehind = "Inner";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.wc.codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"QQType1.Inner1[TYPE_REF]{Inner1, pkgstaticimport, Lpkgstaticimport.QQType1$Inner1;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"QQType1.Inner2[TYPE_REF]{Inner2, pkgstaticimport, Lpkgstaticimport.QQType1$Inner2;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"QQType1.Inner3[TYPE_REF]{Inner3, pkgstaticimport, Lpkgstaticimport.QQType1$Inner3;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"QQType1.Inner4[TYPE_REF]{Inner4, pkgstaticimport, Lpkgstaticimport.QQType1$Inner4;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		this.discardWorkingCopies(qqTypes);
		
		JavaScriptCore.setOptions(oldOptions);
	}
}
public void test0054() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	IJavaScriptUnit[] qqTypes = null;
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		qqTypes = this.getExternalQQTypes();
		
		this.wc = getWorkingCopy(
				"/Completion/src3/test0054/Test.js",
				"package test0054;\n"+
				"import static pkgstaticimport.QQType1.Inner2;\n"+
				"public class Test {\n"+
				"	void foo() {\n"+
				"		Inner\n"+
				"	}\n"+
				"}");
		
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
	
		String str = this.wc.getSource();
		String completeBehind = "Inner";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.wc.codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"QQType1.Inner1[TYPE_REF]{pkgstaticimport.QQType1.Inner1, pkgstaticimport, Lpkgstaticimport.QQType1$Inner1;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"QQType1.Inner2[TYPE_REF]{Inner2, pkgstaticimport, Lpkgstaticimport.QQType1$Inner2;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		this.discardWorkingCopies(qqTypes);
		
		JavaScriptCore.setOptions(oldOptions);
	}
}
public void test0055() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	IJavaScriptUnit[] qqTypes = null;
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		qqTypes = this.getExternalQQTypes();
		
		this.wc = getWorkingCopy(
				"/Completion/src3/test0055/Test.js",
				"package test0055;\n"+
				"import static pkgstaticimport.QQType1.*;\n"+
				"import static pkgstaticimport.QQType1.Inner2;\n"+
				"public class Test {\n"+
				"	void foo() {\n"+
				"		Inner\n"+
				"	}\n"+
				"}");
		
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
	
		String str = this.wc.getSource();
		String completeBehind = "Inner";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.wc.codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"QQType1.Inner1[TYPE_REF]{pkgstaticimport.QQType1.Inner1, pkgstaticimport, Lpkgstaticimport.QQType1$Inner1;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"QQType1.Inner2[TYPE_REF]{Inner2, pkgstaticimport, Lpkgstaticimport.QQType1$Inner2;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		this.discardWorkingCopies(qqTypes);
		
		JavaScriptCore.setOptions(oldOptions);
	}
}
public void test0056() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	IJavaScriptUnit[] qqTypes = null;
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		qqTypes = this.getExternalQQTypes();
		
		this.wc = getWorkingCopy(
				"/Completion/src3/test0056/Test.js",
				"package test0056;\n"+
				"import static pkgstaticimport.QQType1.Inner2;\n"+
				"import static pkgstaticimport.QQType1.*;\n"+
				"public class Test {\n"+
				"	void foo() {\n"+
				"		Inner\n"+
				"	}\n"+
				"}");
		
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
	
		String str = this.wc.getSource();
		String completeBehind = "Inner";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.wc.codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"QQType1.Inner1[TYPE_REF]{pkgstaticimport.QQType1.Inner1, pkgstaticimport, Lpkgstaticimport.QQType1$Inner1;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"QQType1.Inner2[TYPE_REF]{Inner2, pkgstaticimport, Lpkgstaticimport.QQType1$Inner2;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		this.discardWorkingCopies(qqTypes);
		
		JavaScriptCore.setOptions(oldOptions);
	}
}
public void test0057() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	IJavaScriptUnit[] qqTypes = null;
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		qqTypes = this.getExternalQQTypes();
		
		this.wc = getWorkingCopy(
				"/Completion/src3/test0056/Test.js",
				"package test0057;\n"+
				"import static pkgstaticimport.QQType3.*;\n"+
				"public class Test {\n"+
				"	void foo() {\n"+
				"		Inner\n"+
				"	}\n"+
				"}");
		
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
	
		String str = this.wc.getSource();
		String completeBehind = "Inner";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.wc.codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"QQType1.Inner1[TYPE_REF]{pkgstaticimport.QQType1.Inner1, pkgstaticimport, Lpkgstaticimport.QQType1$Inner1;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"QQType1.Inner2[TYPE_REF]{Inner2, pkgstaticimport, Lpkgstaticimport.QQType1$Inner2;, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		this.discardWorkingCopies(qqTypes);
		
		JavaScriptCore.setOptions(oldOptions);
	}
}
public void test0058() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	IJavaScriptUnit[] qqTypes = null;
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		qqTypes = this.getExternalQQTypes();
		
		this.workingCopies = new IJavaScriptUnit[1];
		this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test0058/Test.js",
			"package test0058;\n"+
			"import static pkgstaticimport.QQType4.*;\n"+
			"public class Test {\n"+
			"	void foo() {\n"+
			"		zzvarzz\n"+
			"	}\n"+
			"}");

		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
		String str = this.workingCopies[0].getSource();
		String completeBehind = "zzvarzz";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

		assertResults(
				"zzvarzz2[FIELD_REF]{zzvarzz2, Lpkgstaticimport.QQType4;, I, zzvarzz2, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	
	} finally {
		this.discardWorkingCopies(qqTypes);
		
		JavaScriptCore.setOptions(oldOptions);
	}
}
public void test0059() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	IJavaScriptUnit[] qqTypes = null;
	IJavaScriptUnit qqType5 = null;
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		qqTypes = this.getExternalQQTypes();
		
		qqType5 = getWorkingCopy(
				"/Completion/src3/test0059/QQType5.js",
				"package test0059;\n"+
				"\n"+
				"public class QQType5 {\n"+
				"	public int zzvarzz1;\n"+
				"	public static int zzvarzz2;\n"+
				"	protected int zzvarzz3;\n"+
				"	protected static int zzvarzz4;\n"+
				"	private int zzvarzz5;\n"+
				"	private static int zzvarzz6;\n"+
				"	int zzvarzz7;\n"+
				"	static int zzvarzz8;\n"+
				"}");
	
		this.workingCopies = new IJavaScriptUnit[1];
		this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test0059/Test.js",
			"package test0059;\n"+
			"import static test0059.QQType5.*;\n"+
			"public class Test {\n"+
			"	void foo() {\n"+
			"		zzvarzz\n"+
			"	}\n"+
			"}");

		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
		String str = this.workingCopies[0].getSource();
		String completeBehind = "zzvarzz";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
		
		assertResults(
				"zzvarzz2[FIELD_REF]{zzvarzz2, Ltest0059.QQType5;, I, zzvarzz2, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzvarzz4[FIELD_REF]{zzvarzz4, Ltest0059.QQType5;, I, zzvarzz4, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzvarzz8[FIELD_REF]{zzvarzz8, Ltest0059.QQType5;, I, zzvarzz8, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		this.discardWorkingCopies(qqTypes);
		if(qqType5 != null) {
			qqType5.discardWorkingCopy();
		}
		JavaScriptCore.setOptions(oldOptions);
	}
}
public void test0060() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	IJavaScriptUnit[] qqTypes = null;
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		qqTypes = this.getExternalQQTypes();
		
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
		IJavaScriptUnit cu= getCompilationUnit("Completion", "src3", "test0060", "Test.js");
	
		String str = cu.getSource();
		String completeBehind = "zzvarzz";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		cu.codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"zzvarzz1[FIELD_REF]{zzvarzz1, Lpkgstaticimport.QQType4;, I, zzvarzz1, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzvarzz2[FIELD_REF]{zzvarzz2, Lpkgstaticimport.QQType4;, I, zzvarzz2, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzvarzz3[FIELD_REF]{zzvarzz3, Lpkgstaticimport.QQType4;, I, zzvarzz3, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzvarzz4[FIELD_REF]{zzvarzz4, Lpkgstaticimport.QQType4;, I, zzvarzz4, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		this.discardWorkingCopies(qqTypes);
		
		JavaScriptCore.setOptions(oldOptions);
	}
}
public void test0061() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	IJavaScriptUnit[] qqTypes = null;
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		qqTypes = this.getExternalQQTypes();
		
		this.workingCopies = new IJavaScriptUnit[1];
		this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test0061/Test.js",
			"package test0061;\n" +
			"import static pkgstaticimport.QQType4.zzvarzz2;\n" +
			"public class Test {\n" +
			"	void foo() {\n" +
			"		zzvarzz\n" +
			"	}\n" +
			"}");

		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
		String str = this.workingCopies[0].getSource();
		String completeBehind = "zzvarzz";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"zzvarzz2[FIELD_REF]{zzvarzz2, Lpkgstaticimport.QQType4;, I, zzvarzz2, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		this.discardWorkingCopies(qqTypes);
		
		JavaScriptCore.setOptions(oldOptions);
	}
}
public void test0062() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	IJavaScriptUnit[] qqTypes = null;
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		qqTypes = this.getExternalQQTypes();
		
		this.workingCopies = new IJavaScriptUnit[1];
		this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test0062/Test.js",
			"package test0062;\n" +
			"import static pkgstaticimport.QQType4.*;\n" +
			"import static pkgstaticimport.QQType4.zzvarzz2;\n" +
			"public class Test {\n" +
			"	void foo() {\n" +
			"		zzvarzz\n" +
			"	}\n" +
			"}");

		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
		String str = this.workingCopies[0].getSource();
		String completeBehind = "zzvarzz";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"zzvarzz2[FIELD_REF]{zzvarzz2, Lpkgstaticimport.QQType4;, I, zzvarzz2, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		this.discardWorkingCopies(qqTypes);
		
		JavaScriptCore.setOptions(oldOptions);
	}
}
public void test0063() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	IJavaScriptUnit[] qqTypes = null;
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		qqTypes = this.getExternalQQTypes();
		
		this.workingCopies = new IJavaScriptUnit[1];
		this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test0063/Test.js",
			"package test0063;\n" +
			"import static pkgstaticimport.QQType4.zzvarzz2;\n" +
			"import static pkgstaticimport.QQType4.*;\n" +
			"public class Test {\n" +
			"	void foo() {\n" +
			"		zzvarzz\n" +
			"	}\n" +
			"}");

		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
		String str = this.workingCopies[0].getSource();
		String completeBehind = "zzvarzz";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"zzvarzz2[FIELD_REF]{zzvarzz2, Lpkgstaticimport.QQType4;, I, zzvarzz2, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		this.discardWorkingCopies(qqTypes);
		
		JavaScriptCore.setOptions(oldOptions);
	}
}
public void test0064() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	IJavaScriptUnit[] qqTypes = null;
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		qqTypes = this.getExternalQQTypes();
		
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
		IJavaScriptUnit cu= getCompilationUnit("Completion", "src3", "test0064", "Test.js");
	
		String str = cu.getSource();
		String completeBehind = "zzvarzz";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		cu.codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"zzvarzz2[FIELD_REF]{zzvarzz2, Lpkgstaticimport.QQType4;, I, zzvarzz2, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		this.discardWorkingCopies(qqTypes);
		
		JavaScriptCore.setOptions(oldOptions);
	}
}
public void test0065() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	IJavaScriptUnit[] qqTypes = null;
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		qqTypes = this.getExternalQQTypes();
	
		this.workingCopies = new IJavaScriptUnit[1];
		this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test0065/Test.js",
			"package test0065;\n" +
			"import static pkgstaticimport.QQType7.*;\n" +
			"public class Test {\n" +
			"	void foo() {\n" +
			"		zzfoozz\n" +
			"	}\n" +
			"}");

		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
		String str = this.workingCopies[0].getSource();
		String completeBehind = "zzfoozz";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
		
		assertResults(
				"zzfoozz2[FUNCTION_REF]{zzfoozz2(), Lpkgstaticimport.QQType7;, ()V, zzfoozz2, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		this.discardWorkingCopies(qqTypes);
		
		JavaScriptCore.setOptions(oldOptions);
	}
}
public void test0066() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	IJavaScriptUnit[] qqTypes = null;
	IJavaScriptUnit qqType8 = null;
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		qqTypes = this.getExternalQQTypes();
		
		qqType8 = getWorkingCopy(
				"/Completion/src3/test0066/QQType8.js",
				"package test0066;\n"+
				"\n"+
				"public class QQType8 {\n"+
				"	public void zzfoozz1(){};\n"+
				"	public static void zzfoozz2(){};\n"+
				"	protected void zzfoozz3(){};\n"+
				"	protected static void zzfoozz4(){};\n"+
				"	private void zzfoozz5(){};\n"+
				"	private static void zzfoozz6(){};\n"+
				"	void zzfoozz7(){};\n"+
				"	static void zzfoozz8(){};\n"+
				"}");
	
		this.workingCopies = new IJavaScriptUnit[1];
		this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test0066/Test.js",
			"package test0066;\n" +
			"import static test0066.QQType8.*;\n" +
			"public class Test {\n" +
			"	void foo() {\n" +
			"		zzfoozz\n" +
			"	}\n" +
			"}");

		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
		String str = this.workingCopies[0].getSource();
		String completeBehind = "zzfoozz";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
		
		assertResults(
				"zzfoozz2[FUNCTION_REF]{zzfoozz2(), Ltest0066.QQType8;, ()V, zzfoozz2, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzfoozz4[FUNCTION_REF]{zzfoozz4(), Ltest0066.QQType8;, ()V, zzfoozz4, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzfoozz8[FUNCTION_REF]{zzfoozz8(), Ltest0066.QQType8;, ()V, zzfoozz8, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		this.discardWorkingCopies(qqTypes);
		if(qqType8 != null) {
			qqType8.discardWorkingCopy();
		}
		JavaScriptCore.setOptions(oldOptions);
	}
}
public void test0067() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	IJavaScriptUnit[] qqTypes = null;
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		qqTypes = this.getExternalQQTypes();
		
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
		IJavaScriptUnit cu= getCompilationUnit("Completion", "src3", "test0067", "Test.js");
	
		String str = cu.getSource();
		String completeBehind = "zzfoozz";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		cu.codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"zzfoozz1[FUNCTION_REF]{zzfoozz1(), Lpkgstaticimport.QQType7;, ()V, zzfoozz1, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzfoozz2[FUNCTION_REF]{zzfoozz2(), Lpkgstaticimport.QQType7;, ()V, zzfoozz2, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzfoozz3[FUNCTION_REF]{zzfoozz3(), Lpkgstaticimport.QQType7;, ()V, zzfoozz3, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzfoozz4[FUNCTION_REF]{zzfoozz4(), Lpkgstaticimport.QQType7;, ()V, zzfoozz4, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		this.discardWorkingCopies(qqTypes);
		
		JavaScriptCore.setOptions(oldOptions);
	}
}
public void test0068() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	IJavaScriptUnit[] qqTypes = null;
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		qqTypes = this.getExternalQQTypes();
		
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2();
		IJavaScriptUnit cu= getCompilationUnit("Completion", "src3", "test0068", "Test.js");
	
		String str = cu.getSource();
		String completeBehind = "zzfoozz";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		cu.codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"zzfoozz2[FUNCTION_REF]{zzfoozz2(), Lpkgstaticimport.QQType7;, ()V, zzfoozz2, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		this.discardWorkingCopies(qqTypes);
		
		JavaScriptCore.setOptions(oldOptions);
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74295
 */
public void test0069() throws JavaScriptModelException {
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	IJavaScriptUnit cu= getCompilationUnit("Completion", "src3", "test0069", "Test.js");

	String str = cu.getSource();
	String completeBehind = "icell.p";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	cu.codeComplete(cursorLocation, requestor);

	assertResults(
			"putValue[FUNCTION_REF]{putValue(), Ltest0069.Test<Ljava.lang.String;>;, (Ljava.lang.String;)V, putValue, (value), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_STATIC + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=77573
 */
public void test0070() throws JavaScriptModelException {
	IJavaScriptUnit importedClass = null;
	try {
		importedClass = getWorkingCopy(
				"/Completion/src3/test0070/p/ImportedClass.js",
				"package test0070.p;\n"+
				"\n"+
				"public class ImportedClass {\n"+
				"	\n"+
				"}");
		
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
		IJavaScriptUnit cu= getCompilationUnit("Completion", "src3", "test0070", "Test.js");
	
		String str = cu.getSource();
		String completeBehind = "test0070";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		cu.codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"test0070.p[PACKAGE_REF]{test0070.p., test0070.p, null, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"test0070[PACKAGE_REF]{test0070., test0070, null, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		if(importedClass != null) {
			importedClass.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=77573
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=94303
 */
public void test0071() throws JavaScriptModelException {
	IJavaScriptUnit importedClass = null;
	try {
		importedClass = getWorkingCopy(
				"/Completion/src3/test0071/p/ImportedClass.js",
				"package test0071.p;\n"+
				"\n"+
				"public class ImportedClass {\n"+
				"	\n"+
				"}");

		CompletionResult result = complete(
	            "/Completion/src3/test0071/Test.js",
	            "package test0071;\n" +
	            "\n" +
	            "import static test0071.p.Im\n" +
	            "\n" +
	            "public class Test {\n" +
	            "	\n" +
	            "}",
            	"test0071.p.Im");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	
		assertResults(
				"ImportedClass[TYPE_REF]{test0071.p.ImportedClass., test0071.p, Ltest0071.p.ImportedClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(importedClass != null) {
			importedClass.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=77573
 */
public void test0072() throws JavaScriptModelException {
	IJavaScriptUnit importedClass = null;
	try {
		importedClass = getWorkingCopy(
				"/Completion/src3/test0072/p/ImportedClass.js",
				"package test0072.p;\n"+
				"\n"+
				"public class ImportedClass {\n"+
				"	public static int ZZZ1;\n"+
				"	public static void ZZZ2() {}\n"+
				"	public static void ZZZ2(int i) {}\n"+
				"}");
		
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
		IJavaScriptUnit cu= getCompilationUnit("Completion", "src3", "test0072", "Test.js");
	
		String str = cu.getSource();
		String completeBehind = "test0072.p.ImportedClass.ZZ";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		cu.codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"ZZZ1[FIELD_REF]{test0072.p.ImportedClass.ZZZ1;, Ltest0072.p.ImportedClass;, I, ZZZ1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"ZZZ2[METHOD_IMPORT]{test0072.p.ImportedClass.ZZZ2;, Ltest0072.p.ImportedClass;, ()V, ZZZ2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"ZZZ2[METHOD_IMPORT]{test0072.p.ImportedClass.ZZZ2;, Ltest0072.p.ImportedClass;, (I)V, ZZZ2, (i), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		if(importedClass != null) {
			importedClass.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=77573
 */
public void test0073() throws JavaScriptModelException {
	IJavaScriptUnit importedClass = null;
	try {
		importedClass = getWorkingCopy(
				"/Completion/src3/test0073/p/ImportedClass.js",
				"package test0073.p;\n"+
				"\n"+
				"public class ImportedClass {\n"+
				"	public static class Inner {\n"+
				"		public static int ZZZ1;\n"+
				"		public static void ZZZ2() {}\n"+
				"		public static void ZZZ2(int i) {}\n"+
				"	}\n"+
				"}");
		
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
		IJavaScriptUnit cu= getCompilationUnit("Completion", "src3", "test0073", "Test.js");
	
		String str = cu.getSource();
		String completeBehind = "test0073.p.ImportedClass.Inner.ZZ";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		cu.codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"ZZZ1[FIELD_REF]{test0073.p.ImportedClass.Inner.ZZZ1;, Ltest0073.p.ImportedClass$Inner;, I, ZZZ1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"ZZZ2[METHOD_IMPORT]{test0073.p.ImportedClass.Inner.ZZZ2;, Ltest0073.p.ImportedClass$Inner;, ()V, ZZZ2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"ZZZ2[METHOD_IMPORT]{test0073.p.ImportedClass.Inner.ZZZ2;, Ltest0073.p.ImportedClass$Inner;, (I)V, ZZZ2, (i), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		if(importedClass != null) {
			importedClass.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=77573
 */
public void test0074() throws JavaScriptModelException {
	IJavaScriptUnit importedClass = null;
	try {
		importedClass = getWorkingCopy(
				"/Completion/src3/test0074/p/ImportedClass.js",
				"package test0074.p;\n"+
				"\n"+
				"public class ImportedClass {\n"+
				"	public class Inner {\n"+
				"		public static int ZZZ1;\n"+
				"		public static void ZZZ2() {}\n"+
				"		public static void ZZZ2(int i) {}\n"+
				"	}\n"+
				"}");
		
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
		IJavaScriptUnit cu= getCompilationUnit("Completion", "src3", "test0074", "Test.js");
	
		String str = cu.getSource();
		String completeBehind = "test0074.p.ImportedClass.Inner.ZZ";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		cu.codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"ZZZ1[FIELD_REF]{test0074.p.ImportedClass.Inner.ZZZ1;, Ltest0074.p.ImportedClass$Inner;, I, ZZZ1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"ZZZ2[METHOD_IMPORT]{test0074.p.ImportedClass.Inner.ZZZ2;, Ltest0074.p.ImportedClass$Inner;, ()V, ZZZ2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"ZZZ2[METHOD_IMPORT]{test0074.p.ImportedClass.Inner.ZZZ2;, Ltest0074.p.ImportedClass$Inner;, (I)V, ZZZ2, (i), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		if(importedClass != null) {
			importedClass.discardWorkingCopy();
		}
	}
}
public void test0075() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test0075/Test.js",
			"package test0075;\n" +
			"public @QQAnnot class Test {\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/pkgannotations/QQAnnotation.js",
		"package pkgannotations;"+
		"public @interface QQAnnotation {\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "@QQAnnot";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"QQAnnotation[TYPE_REF]{pkgannotations.QQAnnotation, pkgannotations, Lpkgannotations.QQAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_ANNOTATION + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
public void test0076() throws JavaScriptModelException {	
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test0076/Test.js",
			"package test0076;\n" +
			"public @QQAnnot class Test\n" +
			"");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/pkgannotations/QQAnnotation.js",
		"package pkgannotations;"+
		"public @interface QQAnnotation {\n"+
		"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "@QQAnnot";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"QQAnnotation[TYPE_REF]{pkgannotations.QQAnnotation, pkgannotations, Lpkgannotations.QQAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_ANNOTATION + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
public void test0077() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test0077/Test.js",
			"package test0077;\n" +
			"public @QQAnnot\n" +
			"");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/pkgannotations/QQAnnotation.js",
		"package pkgannotations;"+
		"public @interface QQAnnotation {\n"+
		"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "@QQAnnot";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"QQAnnotation[TYPE_REF]{pkgannotations.QQAnnotation, pkgannotations, Lpkgannotations.QQAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_ANNOTATION + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
public void test0078() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test0078/Test.js",
			"package test0078;\n" +
			"public class Test {\n" +
			"  public @QQAnnot void foo() {\n" +
			"  }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/pkgannotations/QQAnnotation.js",
		"package pkgannotations;"+
		"public @interface QQAnnotation {\n"+
		"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "@QQAnnot";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"QQAnnotation[TYPE_REF]{pkgannotations.QQAnnotation, pkgannotations, Lpkgannotations.QQAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_ANNOTATION + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
public void test0079() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test0078/Test.js",
			"package test0078;\n" +
			"public class Test {\n" +
			"  public @QQAnnot void foo(\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/pkgannotations/QQAnnotation.js",
		"package pkgannotations;"+
		"public @interface QQAnnotation {\n"+
		"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "@QQAnnot";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"QQAnnotation[TYPE_REF]{pkgannotations.QQAnnotation, pkgannotations, Lpkgannotations.QQAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_ANNOTATION + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
public void test0080() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test0078/Test.js",
			"package test0078;\n" +
			"public class Test {\n" +
			"  public @QQAnnot int var;\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/pkgannotations/QQAnnotation.js",
		"package pkgannotations;"+
		"public @interface QQAnnotation {\n"+
		"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "@QQAnnot";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"QQAnnotation[TYPE_REF]{pkgannotations.QQAnnotation, pkgannotations, Lpkgannotations.QQAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_ANNOTATION + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
public void test0081() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test0078/Test.js",
			"package test0078;\n" +
			"public class Test {\n" +
			"  public @QQAnnot int var\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/pkgannotations/QQAnnotation.js",
		"package pkgannotations;"+
		"public @interface QQAnnotation {\n"+
		"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "@QQAnnot";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"QQAnnotation[TYPE_REF]{pkgannotations.QQAnnotation, pkgannotations, Lpkgannotations.QQAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_ANNOTATION + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
public void test0082() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test0078/Test.js",
			"package test0078;\n" +
			"public class Test {\n" +
			"  void foo(@QQAnnot int i) {}\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/pkgannotations/QQAnnotation.js",
		"package pkgannotations;"+
		"public @interface QQAnnotation {\n"+
		"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "@QQAnnot";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"QQAnnotation[TYPE_REF]{pkgannotations.QQAnnotation, pkgannotations, Lpkgannotations.QQAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_ANNOTATION + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
public void test0083() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test0078/Test.js",
			"package test0078;\n" +
			"public class Test {\n" +
			"  void foo() {@QQAnnot int i}\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/pkgannotations/QQAnnotation.js",
		"package pkgannotations;"+
		"public @interface QQAnnotation {\n"+
		"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "@QQAnnot";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"QQAnnotation[TYPE_REF]{pkgannotations.QQAnnotation, pkgannotations, Lpkgannotations.QQAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_ANNOTATION + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
public void test0084() throws JavaScriptModelException {
	IJavaScriptUnit imported = null;
	try {
		imported = getWorkingCopy(
				"/Completion/src3/pkgstaticimport/MyClass0084.js",
				"package pkgstaticimport;\n" +
				"public class MyClass0084 {\n" +
				"   public static int foo() {return 0;}\n" +
				"   public static int foo(int i) {return 0;}\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0084/Test.js",
				"package test0084;\n" +
				"import static pkgstaticimport.MyClass0084.foo;\n" +
				"public class Test {\n" +
				"  void bar() {\n" +
				"    int i = foo\n" +
				"  }\n" +
				"}",
				"foo");
		
		assertResults(
				"foo[FUNCTION_REF]{foo(), Lpkgstaticimport.MyClass0084;, ()I, foo, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"foo[FUNCTION_REF]{foo(), Lpkgstaticimport.MyClass0084;, (I)I, foo, (i), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(imported != null) {
			imported.discardWorkingCopy();
		}
	}
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=85290
public void test0085() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0085/TestAnnotation.js",
			"package test0085;\n" +
			"public @interface TestAnnotation {\n" +
			"}\n" +
			"@TestAnnotati\n" +
			"class Test2 {\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "@TestAnnotati";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"TestAnnotation[TYPE_REF]{TestAnnotation, test0085, Ltest0085.TestAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_ANNOTATION + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=85290
public void test0086() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/TestAnnotation.js",
			"public @interface TestAnnotation {\n" +
			"}\n" +
			"@TestAnnotati\n" +
			"class Test2 {\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "@TestAnnotati";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"TestAnnotation[TYPE_REF]{TestAnnotation, , LTestAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_ANNOTATION + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=85402
public void test0087() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0087/TestAnnotation.js",
			"package test0087;\n" +
			"public @interface TestAnnotation {\n" +
			"}\n" +
			"@\n" +
			"class Test2 {\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "@";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	if(CompletionEngine.NO_TYPE_COMPLETION_ON_EMPTY_TOKEN) {
		assertResults(
				"",
				requestor.getResults());
	} else {
		assertResults(
				"TestAnnotation[TYPE_REF]{TestAnnotation, test0087, Ltest0087.TestAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_ANNOTATION + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
				requestor.getResults());
	}
}
public void test0088() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0088/TestAnnotation.js",
			"package test0088;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"@TestAnnotation(foo)\n" +
			"class Test2 {\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0088.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0089() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0089/TestAnnotation.js",
			"package test0089;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo)\n" +
			"  void bar(){}\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0089.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0090() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0090/TestAnnotation.js",
			"package test0090;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo)\n" +
			"  int var;\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0090.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0091() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0091/TestAnnotation.js",
			"package test0091;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar(){\n" +
			"    @TestAnnotation(foo)\n" +
			"    int var;\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0091.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0092() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0092/TestAnnotation.js",
			"package test0092;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar(int var1, @TestAnnotation(foo) int var2){\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0092.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0093() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0093/TestAnnotation.js",
			"package test0093;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo)\n" +
			"  Test2(){\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0093.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0094() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0094/TestAnnotation.js",
			"package test0094;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"@TestAnnotation(foo\n" +
			"class Test2 {\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0094.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0095() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0095/TestAnnotation.js",
			"package test0095;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo\n" +
			"  void bar(){}\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0095.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0096() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0096/TestAnnotation.js",
			"package test0096;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo\n" +
			"  int var;\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0096.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0097() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0097/TestAnnotation.js",
			"package test0097;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar(){\n" +
			"    @TestAnnotation(foo\n" +
			"    int var;\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0097.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0098() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0098/TestAnnotation.js",
			"package test0098;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar(int var1, @TestAnnotation(foo int var2){\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0098.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0099() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0099/TestAnnotation.js",
			"package test0099;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo\n" +
			"  Test2(){\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0099.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0100() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0100/TestAnnotation.js",
			"package test0100;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"@TestAnnotation(foo=\"\")\n" +
			"class Test2 {\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0100.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0101() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0101/TestAnnotation.js",
			"package test00101;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo=\"\")\n" +
			"  void bar(){}\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0101.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0102() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0102/TestAnnotation.js",
			"package test0102;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo=\"\")\n" +
			"  int var;\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0102.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0103() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0103/TestAnnotation.js",
			"package test00103;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar(){\n" +
			"    @TestAnnotation(foo=\"\")\n" +
			"    int var;\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0103.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0104() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0104/TestAnnotation.js",
			"package test0104;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar(int var1, @TestAnnotation(foo=\"\") int var2){\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0104.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0105() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0105/TestAnnotation.js",
			"package test0105;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo=\"\")\n" +
			"  Test2(){\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0105.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0106() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0106/TestAnnotation.js",
			"package test0106;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"@TestAnnotation(foo=\"\"\n" +
			"class Test2 {\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0106.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0107() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0107/TestAnnotation.js",
			"package test0107;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo=\"\"\n" +
			"  void bar(){}\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0107.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0108() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0108/TestAnnotation.js",
			"package test0108;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo=\"\"\n" +
			"  int var;\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0108.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0109() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0109/TestAnnotation.js",
			"package test0109;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar(){\n" +
			"    @TestAnnotation(foo=\"\"\n" +
			"    int var;\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0109.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0110() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0110/TestAnnotation.js",
			"package test0110;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar(int var1, @TestAnnotation(foo=\"\" int var2){\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0110.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0111() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0111/TestAnnotation.js",
			"package test0111;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo=\"\"\n" +
			"  Test2(){\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0111.TestAnnotation;, Ljava.lang.String;, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0112() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0112/TestAnnotation.js",
			"package test0112;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"@TestAnnotation(foo1=\"\", foo)\n" +
			"class Test2 {\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0112.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0113() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0113/TestAnnotation.js",
			"package test0113;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo1=\"\", foo)\n" +
			"  void bar(){}\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0113.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0114() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0114/TestAnnotation.js",
			"package test0114;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo1=\"\", foo)\n" +
			"  int var;\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0114.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0115() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0115/TestAnnotation.js",
			"package test0115;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar(){\n" +
			"    @TestAnnotation(foo1=\"\", foo)\n" +
			"    int var;\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0115.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0116() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0116/TestAnnotation.js",
			"package test0116;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar(int var1, @TestAnnotation(foo1=\"\", foo) int var2){\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0116.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0117() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0117/TestAnnotation.js",
			"package test0117;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo1=\"\", foo)\n" +
			"  Test2(){\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0117.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0118() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0118/TestAnnotation.js",
			"package test0118;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"@TestAnnotation(foo1=\"\", foo\n" +
			"class Test2 {\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0118.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0119() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0119/TestAnnotation.js",
			"package test0119;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo1=\"\", foo\n" +
			"  void bar(){}\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0119.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0120() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0120/TestAnnotation.js",
			"package test0120;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo1=\"\", foo\n" +
			"  int var;\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0120.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0121() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0121/TestAnnotation.js",
			"package test0121;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar(){\n" +
			"    @TestAnnotation(foo1=\"\", foo\n" +
			"    int var;\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0121.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0122() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0122/TestAnnotation.js",
			"package test0122;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar(int var1, @TestAnnotation(foo1=\"\", foo int var2){\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0122.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0123() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0123/TestAnnotation.js",
			"package test0123;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo1=\"\", foo\n" +
			"  Test2(){\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0123.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0124() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0124/TestAnnotation.js",
			"package test0124;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"@TestAnnotation(foo1=\"\", foo=\"\")\n" +
			"class Test2 {\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0124.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0125() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0125/TestAnnotation.js",
			"package test0125;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo1=\"\", foo=\"\")\n" +
			"  void bar(){}\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0125.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0126() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0126/TestAnnotation.js",
			"package test0126;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo1=\"\", foo=\"\")\n" +
			"  int var;\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0126.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0127() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0127/TestAnnotation.js",
			"package test0127;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar(){\n" +
			"    @TestAnnotation(foo1=\"\", foo=\"\")\n" +
			"    int var;\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0127.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0128() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0128/TestAnnotation.js",
			"package test0128;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar(int var1, @TestAnnotation(foo1=\"\", foo=\"\") int var2){\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0128.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0129() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0129/TestAnnotation.js",
			"package test0129;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo1=\"\", foo=\"\")\n" +
			"  Test2(){\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0129.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0130() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0130/TestAnnotation.js",
			"package test0130;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"@TestAnnotation(foo1=\"\", foo=\"\"\n" +
			"class Test2 {\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0130.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0131() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0131/TestAnnotation.js",
			"package test0131;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo1=\"\", foo=\"\"\n" +
			"  void bar(){}\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0131.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0132() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0132/TestAnnotation.js",
			"package test0132;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo1=\"\", foo=\"\"\n" +
			"  int var;\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0132.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0133() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0133/TestAnnotation.js",
			"package test0133;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar(){\n" +
			"    @TestAnnotation(foo1=\"\", foo=\"\"\n" +
			"    int var;\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0133.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0134() throws JavaScriptModelException {
	this.wc = getWorkingCopy(
			"/Completion/src3/test0134/TestAnnotation.js",
			"package test0134;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  void bar(int var1, @TestAnnotation(foo1=\"\", foo=\"\" int var2){\n" +
			"  }\n" +
			"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.wc.getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.wc.codeComplete(cursorLocation, requestor);

	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0134.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			requestor.getResults());
}
public void test0135() throws JavaScriptModelException {
	CompletionResult result = complete(
			"/Completion/src3/test0135/TestAnnotation.js",
			"package test0135;\n" +
			"public @interface TestAnnotation {\n" +
			"  String foo1();\n" +
			"  String foo2();\n" +
			"}\n" +
			"class Test2 {\n" +
			"  @TestAnnotation(foo1=\"\", foo=\"\"\n" +
			"  Test2(){\n" +
			"  }\n" +
			"}",
			"foo");
	
	assertResults(
			"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0135.TestAnnotation;, Ljava.lang.String;, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED + R_UNQUALIFIED) + "}",
			result.proposals);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=84554
public void test0136() throws JavaScriptModelException {
	IJavaScriptUnit enumeration = null;
	try {
		enumeration = getWorkingCopy(
				"/Completion/src3/test0136/Colors.js",
				"package test0136;\n" +
				"public enum Colors {\n" +
				"  RED, BLUE, WHITE;\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0136/Test.js",
				"package test0136;\n" +
				"public class Test {\n" +
				"  void bar(Colors c) {\n" +
				"    switch(c) {\n" + 
				"      case RED :\n" + 
				"        break;\n" + 
				"    }\n" + 
				"  }\n" +
				"}",
				"RED");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"RED[FIELD_REF]{RED, Ltest0136.Colors;, Ltest0136.Colors;, RED, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED + R_NON_RESTRICTED + R_ENUM_CONSTANT) + "}",
				result.proposals);
	} finally {
		if(enumeration != null) {
			enumeration.discardWorkingCopy();
		}
	}
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=84554
public void test0137() throws JavaScriptModelException {
	IJavaScriptUnit enumeration = null;
	try {
		enumeration = getWorkingCopy(
				"/Completion/src3/test0137/Colors.js",
				"package test0137;\n" +
				"public enum Colors {\n" +
				"  RED, BLUE, WHITE;\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0137/Test.js",
				"package test0137;\n" +
				"public class Test {\n" +
				"  void bar(Colors c) {\n" +
				"    switch(c) {\n" + 
				"      case BLUE :\n" + 
				"      case RED :\n" + 
				"        break;\n" + 
				"    }\n" + 
				"  }\n" +
				"}",
				"RED");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"RED[FIELD_REF]{RED, Ltest0137.Colors;, Ltest0137.Colors;, RED, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED + R_NON_RESTRICTED + R_ENUM_CONSTANT) + "}",
				result.proposals);
	} finally {
		if(enumeration != null) {
			enumeration.discardWorkingCopy();
		}
	}
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=84554
public void test0138() throws JavaScriptModelException {
	IJavaScriptUnit enumeration = null;
	try {
		enumeration = getWorkingCopy(
				"/Completion/src3/test0138/Colors.js",
				"package test0138;\n" +
				"public enum Colors {\n" +
				"  RED, BLUE, WHITE;\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0138/Test.js",
				"package test0138;\n" +
				"public class Test {\n" +
				"  void bar(Colors c) {\n" +
				"    switch(c) {\n" + 
				"      case BLUE :\n" + 
				"        break;\n" + 
				"      case RED :\n" + 
				"        break;\n" + 
				"    }\n" + 
				"  }\n" +
				"}",
				"RED");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"RED[FIELD_REF]{RED, Ltest0138.Colors;, Ltest0138.Colors;, RED, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED + R_NON_RESTRICTED + R_ENUM_CONSTANT) + "}",
				result.proposals);
	} finally {
		if(enumeration != null) {
			enumeration.discardWorkingCopy();
		}
	}
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=84554
public void test0139() throws JavaScriptModelException {
	IJavaScriptUnit enumeration = null;
	try {
		enumeration = getWorkingCopy(
				"/Completion/src3/test0139/Colors.js",
				"package test0139;\n" +
				"public enum Colors {\n" +
				"  RED, BLUE, WHITE;\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0139/Test.js",
				"package test0139;\n" +
				"public class Test {\n" +
				"  void bar(Colors c) {\n" +
				"    switch(c) {\n" + 
				"      case BLUE :\n" + 
				"        break;\n" + 
				"      case RED :\n" + 
				"    }\n" + 
				"  }\n" +
				"}",
				"RED");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"RED[FIELD_REF]{RED, Ltest0139.Colors;, Ltest0139.Colors;, RED, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED + R_NON_RESTRICTED + R_ENUM_CONSTANT) + "}",
				result.proposals);
	} finally {
		if(enumeration != null) {
			enumeration.discardWorkingCopy();
		}
	}
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=84554
public void test0140() throws JavaScriptModelException {
	IJavaScriptUnit enumeration = null;
	try {
		enumeration = getWorkingCopy(
				"/Completion/src3/test0140/Colors.js",
				"package test0140;\n" +
				"public enum Colors {\n" +
				"  RED, BLUE, WHITE;\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0140/Test.js",
				"package test0140;\n" +
				"public class Test {\n" +
				"  void bar(Colors c) {\n" +
				"    switch(c) {\n" + 
				"      case BLUE :\n" + 
				"        break;\n" + 
				"      case RED\n" + 
				"    }\n" + 
				"  }\n" +
				"}",
				"RED");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"RED[FIELD_REF]{RED, Ltest0140.Colors;, Ltest0140.Colors;, RED, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED + R_NON_RESTRICTED + R_ENUM_CONSTANT) + "}",
				result.proposals);
	} finally {
		if(enumeration != null) {
			enumeration.discardWorkingCopy();
		}
	}
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=84554
public void test0141() throws JavaScriptModelException {
	IJavaScriptUnit enumeration = null;
	try {
		enumeration = getWorkingCopy(
				"/Completion/src3/test0141/Colors.js",
				"package test0141;\n" +
				"public class Colors {\n" +
				"  public final static int RED = 0, BLUE = 1, WHITE = 3;\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0141/Test.js",
				"package test0141;\n" +
				"public class Test {\n" +
				"  void bar(Colors c) {\n" +
				"    switch(c) {\n" + 
				"      case BLUE :\n" + 
				"        break;\n" + 
				"      case RED :\n" + 
				"        break;\n" + 
				"    }\n" + 
				"  }\n" +
				"}",
				"RED");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"",
				result.proposals);
	} finally {
		if(enumeration != null) {
			enumeration.discardWorkingCopy();
		}
	}
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=84554
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=88295
public void test0142() throws JavaScriptModelException {
	IJavaScriptUnit enumeration = null;
	try {
		enumeration = getWorkingCopy(
				"/Completion/src3/test0142/Colors.js",
				"package test0142;\n" +
				"public enum Colors {\n" +
				"  RED, BLUE, WHITE;\n" +
				"  public static final int RED2 = 0;\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0142/Test.js",
				"package test0142;\n" +
				"public class Test {\n" +
				"  void bar(Colors REDc) {\n" +
				"    switch(REDc) {\n" + 
				"      case BLUE :\n" + 
				"        break;\n" + 
				"      case RED:\n" + 
				"        break;\n" +
				"    }\n" + 
				"  }\n" +
				"}",
				"RED");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"RED[FIELD_REF]{RED, Ltest0142.Colors;, Ltest0142.Colors;, RED, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED + R_NON_RESTRICTED + R_ENUM_CONSTANT) + "}",
				result.proposals);
	} finally {
		if(enumeration != null) {
			enumeration.discardWorkingCopy();
		}
	}
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=88756
public void test0143() throws JavaScriptModelException {
	Hashtable oldCurrentOptions = JavaScriptCore.getOptions();
	IJavaScriptUnit enumeration = null;
	try {
		Hashtable options = new Hashtable(oldCurrentOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.DISABLED);
		JavaScriptCore.setOptions(options);
		
		enumeration = getWorkingCopy(
				"/Completion/src3/test0143/Colors.js",
				"package test0143;\n" +
				"public enum Colors {\n" +
				"  RED, BLUE, WHITE;\n" +
				"  private Colors(){};\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0143/Test.js",
				"package test0143;\n" +
				"public class Test {\n" +
				"  void bar() {\n" +
				"    new Colors(\n" + 
				"  }\n" +
				"}",
				"Colors(");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"",
				result.proposals);
	} finally {
		if(enumeration != null) {
			enumeration.discardWorkingCopy();
		}
		JavaScriptCore.setOptions(oldCurrentOptions);
	}
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=88845
public void test0144() throws JavaScriptModelException {
	IJavaScriptUnit aClass = null;
	Hashtable oldCurrentOptions = JavaScriptCore.getOptions();
	try {
		Hashtable options = new Hashtable(oldCurrentOptions);
		options.put(JavaScriptCore.CODEASSIST_VISIBILITY_CHECK, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0144/X.js",
				"package test0144;\n" +
				"public class X {\n" +
				"  public class Y {}\n" +
				"  private class Y2 {}\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0144/Test.js",
				"package test0144;\n" +
				"public class Test extends X.\n" +
				"{}",
				"X.");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"X.Y[TYPE_REF]{Y, test0144, Ltest0144.X$Y;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_CLASS + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
		JavaScriptCore.setOptions(oldCurrentOptions);
	}
}
// complete annotation attribute value
public void test0145() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0145/ZZAnnotation.js",
				"package test0145;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0145/ZZClass.js",
				"package test0145;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0145/Test.js",
				"package test0145;\n" +
				"@ZZAnnotation(foo1=ZZ)\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={I}\n" +
				"expectedTypesKeys={I}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0145, Ltest0145.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0145, Ltest0145.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0146() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0146/ZZAnnotation.js",
				"package test0146;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0146/ZZClass.js",
				"package test0146;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0146/Test.js",
				"package test0146;\n" +
				"@ZZAnnotation(foo1= 0 + ZZ)\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={S,I,J,F,D,C,B,Ljava.lang.String;}\n" +
				"expectedTypesKeys={S,I,J,F,D,C,B,Ljava/lang/String;}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0146, Ltest0146.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0146, Ltest0146.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0147() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0147/ZZAnnotation.js",
				"package test0147;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int[] foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0147/ZZClass.js",
				"package test0147;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0147/Test.js",
				"package test0147;\n" +
				"@ZZAnnotation(foo1= {ZZ})\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0147, Ltest0147.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0147, Ltest0147.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0148() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0148/ZZAnnotation.js",
				"package test0148;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0148/ZZClass.js",
				"package test0148;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0148/Test.js",
				"package test0148;\n" +
				"@ZZAnnotation(foo1=ZZ\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={I}\n" +
				"expectedTypesKeys={I}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0148, Ltest0148.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0148, Ltest0148.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0149() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0149/ZZAnnotation.js",
				"package test0149;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0149/ZZClass.js",
				"package test0149;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0149/Test.js",
				"package test0149;\n" +
				"@ZZAnnotation(foo1= 0 + ZZ\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={S,I,J,F,D,C,B,Ljava.lang.String;}\n" +
				"expectedTypesKeys={S,I,J,F,D,C,B,Ljava/lang/String;}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0149, Ltest0149.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0149, Ltest0149.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0150() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0150/ZZAnnotation.js",
				"package test0150;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int[] foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0150/ZZClass.js",
				"package test0150;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0150/Test.js",
				"package test0150;\n" +
				"@ZZAnnotation(foo1= {ZZ}\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0150, Ltest0150.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0150, Ltest0150.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0151() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0151/ZZAnnotation.js",
				"package test0151;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int[] foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0151/ZZClass.js",
				"package test0151;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0151/Test.js",
				"package test0151;\n" +
				"@ZZAnnotation(foo1= {ZZ\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0151, Ltest0151.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0151, Ltest0151.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0152() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0152/ZZAnnotation.js",
				"package test0152;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0152/ZZClass.js",
				"package test0152;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0152/Test.js",
				"package test0152;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation(foo1=ZZ)\n" +
				"  void bar(){}\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={I}\n" +
				"expectedTypesKeys={I}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0152, Ltest0152.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0152, Ltest0152.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0152.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0153() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0153/ZZAnnotation.js",
				"package test0153;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0153/ZZClass.js",
				"package test0153;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0153/Test.js",
				"package test0153;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation(foo1= 0 + ZZ)\n" +
				"  void bar(){}\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={S,I,J,F,D,C,B,Ljava.lang.String;}\n" +
				"expectedTypesKeys={S,I,J,F,D,C,B,Ljava/lang/String;}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0153, Ltest0153.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0153, Ltest0153.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0153.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0154() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0154/ZZAnnotation.js",
				"package test0154;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int[] foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0154/ZZClass.js",
				"package test0154;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0154/Test.js",
				"package test0154;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation(foo1= {ZZ})\n" +
				"  void bar(){}\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"zzint[FIELD_REF]{zzint, Ltest0154.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0154, Ltest0154.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0154, Ltest0154.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0155() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0155/ZZAnnotation.js",
				"package test0155;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0155/ZZClass.js",
				"package test0155;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0155/Test.js",
				"package test0155;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation(foo1=ZZ\n" +
				"  void bar(){}\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={I}\n" +
				"expectedTypesKeys={I}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0155, Ltest0155.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0155, Ltest0155.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0155.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0156() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0156/ZZAnnotation.js",
				"package test0156;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0156/ZZClass.js",
				"package test0156;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0156/Test.js",
				"package test0156;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation(foo1= 0 + ZZ\n" +
				"  void bar(){}\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={S,I,J,F,D,C,B,Ljava.lang.String;}\n" +
				"expectedTypesKeys={S,I,J,F,D,C,B,Ljava/lang/String;}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0156, Ltest0156.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0156, Ltest0156.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0156.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0157() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0157/ZZAnnotation.js",
				"package test0157;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int[] foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0157/ZZClass.js",
				"package test0157;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0157/Test.js",
				"package test0157;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation(foo1= {ZZ}\n" +
				"  void bar(){}\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"zzint[FIELD_REF]{zzint, Ltest0157.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0157, Ltest0157.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0157, Ltest0157.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0158() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0158/ZZAnnotation.js",
				"package test0158;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int[] foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0158/ZZClass.js",
				"package test0158;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0158/Test.js",
				"package test0158;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation(foo1= {ZZ\n" +
				"  void bar(){}\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"zzint[FIELD_REF]{zzint, Ltest0158.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0158, Ltest0158.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0158, Ltest0158.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0159() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0159/ZZAnnotation.js",
				"package test0159;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0159/ZZClass.js",
				"package test0159;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0159/Test.js",
				"package test0159;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation(foo1=ZZ)\n" +
				"  int bar;\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={I}\n" +
				"expectedTypesKeys={I}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0159, Ltest0159.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0159, Ltest0159.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0159.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0160() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0160/ZZAnnotation.js",
				"package test0160;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0160/ZZClass.js",
				"package test0160;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0160/Test.js",
				"package test0160;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation(foo1= 0 + ZZ)\n" +
				"  int bar;\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={S,I,J,F,D,C,B,Ljava.lang.String;}\n" +
				"expectedTypesKeys={S,I,J,F,D,C,B,Ljava/lang/String;}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0160, Ltest0160.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0160, Ltest0160.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0160.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0161() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0161/ZZAnnotation.js",
				"package test0161;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int[] foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0161/ZZClass.js",
				"package test0161;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0161/Test.js",
				"package test0161;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation(foo1= {ZZ})\n" +
				"  int bar;\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"zzint[FIELD_REF]{zzint, Ltest0161.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0161, Ltest0161.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0161, Ltest0161.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0162() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0162/ZZAnnotation.js",
				"package test0162;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0162/ZZClass.js",
				"package test0162;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0162/Test.js",
				"package test0162;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation(foo1=ZZ\n" +
				"  int bar;\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={I}\n" +
				"expectedTypesKeys={I}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0162, Ltest0162.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0162, Ltest0162.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0162.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0163() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0163/ZZAnnotation.js",
				"package test0163;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0163/ZZClass.js",
				"package test0163;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0163/Test.js",
				"package test0163;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation(foo1= 0 + ZZ\n" +
				"  int bar;\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={S,I,J,F,D,C,B,Ljava.lang.String;}\n" +
				"expectedTypesKeys={S,I,J,F,D,C,B,Ljava/lang/String;}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0163, Ltest0163.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0163, Ltest0163.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0163.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0164() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0164/ZZAnnotation.js",
				"package test0164;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int[] foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0164/ZZClass.js",
				"package test0164;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0164/Test.js",
				"package test0164;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation(foo1= {ZZ}\n" +
				"  int bar;\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"zzint[FIELD_REF]{zzint, Ltest0164.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0164, Ltest0164.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0164, Ltest0164.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0165() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0165/ZZAnnotation.js",
				"package test0165;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int[] foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0165/ZZClass.js",
				"package test0165;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0165/Test.js",
				"package test0165;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation(foo1= {ZZ\n" +
				"  int bar;\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"zzint[FIELD_REF]{zzint, Ltest0165.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0165, Ltest0165.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0165, Ltest0165.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0166() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0166/ZZAnnotation.js",
				"package test0166;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0166/ZZClass.js",
				"package test0166;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0166/Test.js",
				"package test0166;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  void baz() {\n" +
				"    @ZZAnnotation(foo1=ZZ)\n" +
				"    int bar;\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={I}\n" +
				"expectedTypesKeys={I}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0166, Ltest0166.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0166, Ltest0166.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0166.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0167() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0167/ZZAnnotation.js",
				"package test0167;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0167/ZZClass.js",
				"package test0167;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0167/Test.js",
				"package test0167;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  void baz() {\n" +
				"    @ZZAnnotation(foo1= 0 + ZZ)\n" +
				"    int bar;\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={S,I,J,F,D,C,B,Ljava.lang.String;}\n" +
				"expectedTypesKeys={S,I,J,F,D,C,B,Ljava/lang/String;}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0167, Ltest0167.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0167, Ltest0167.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0167.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0168() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0168/ZZAnnotation.js",
				"package test0168;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int[] foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0168/ZZClass.js",
				"package test0168;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0168/Test.js",
				"package test0168;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  void baz() {\n" +
				"    @ZZAnnotation(foo1= {ZZ})\n" +
				"    int bar;\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"zzint[FIELD_REF]{zzint, Ltest0168.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0168, Ltest0168.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0168, Ltest0168.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0169() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0169/ZZAnnotation.js",
				"package test0169;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0169/ZZClass.js",
				"package test0169;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0169/Test.js",
				"package test0169;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  void baz() {\n" +
				"    @ZZAnnotation(foo1=ZZ\n" +
				"    int bar;\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={I}\n" +
				"expectedTypesKeys={I}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0169, Ltest0169.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0169, Ltest0169.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0169.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0170() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0170/ZZAnnotation.js",
				"package test0170;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0170/ZZClass.js",
				"package test0170;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0170/Test.js",
				"package test0170;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  void baz() {\n" +
				"    @ZZAnnotation(foo1= 0 + ZZ\n" +
				"    int bar;\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={S,I,J,F,D,C,B,Ljava.lang.String;}\n" +
				"expectedTypesKeys={S,I,J,F,D,C,B,Ljava/lang/String;}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0170, Ltest0170.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0170, Ltest0170.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0170.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0171() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0171/ZZAnnotation.js",
				"package test0171;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int[] foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0171/ZZClass.js",
				"package test0171;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0171/Test.js",
				"package test0171;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  void baz() {\n" +
				"    @ZZAnnotation(foo1= {ZZ}\n" +
				"    int bar;\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"zzint[FIELD_REF]{zzint, Ltest0171.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0171, Ltest0171.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0171, Ltest0171.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0172() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0172/ZZAnnotation.js",
				"package test0172;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int[] foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0172/ZZClass.js",
				"package test0172;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0172/Test.js",
				"package test0172;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  void baz() {\n" +
				"    @ZZAnnotation(foo1= {ZZ\n" +
				"    int bar;\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"zzint[FIELD_REF]{zzint, Ltest0172.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0172, Ltest0172.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0172, Ltest0172.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0173() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0173/ZZAnnotation.js",
				"package test0173;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0173/ZZClass.js",
				"package test0173;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0173/Test.js",
				"package test0173;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  void baz(@ZZAnnotation(foo1=ZZ) int bar) {\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={I}\n" +
				"expectedTypesKeys={I}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0173, Ltest0173.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0173, Ltest0173.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0173.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0174() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0174/ZZAnnotation.js",
				"package test0174;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0174/ZZClass.js",
				"package test0174;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0174/Test.js",
				"package test0174;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  void baz(@ZZAnnotation(foo1= 0 + ZZ) int bar) {\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={S,I,J,F,D,C,B,Ljava.lang.String;}\n" +
				"expectedTypesKeys={S,I,J,F,D,C,B,Ljava/lang/String;}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0174, Ltest0174.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0174, Ltest0174.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0174.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0175() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0175/ZZAnnotation.js",
				"package test0175;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int[] foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0175/ZZClass.js",
				"package test0175;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0175/Test.js",
				"package test0175;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  void baz(@ZZAnnotation(foo1= {ZZ}) int bar) {\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"zzint[FIELD_REF]{zzint, Ltest0175.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0175, Ltest0175.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0175, Ltest0175.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0176() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0176/ZZAnnotation.js",
				"package test0176;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0176/ZZClass.js",
				"package test0176;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0176/Test.js",
				"package test0176;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  void baz(@ZZAnnotation(foo1=ZZ int bar) {\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={I}\n" +
				"expectedTypesKeys={I}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0176, Ltest0176.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0176, Ltest0176.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0176.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0177() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0177/ZZAnnotation.js",
				"package test0177;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0177/ZZClass.js",
				"package test0177;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0177/Test.js",
				"package test0177;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  void baz(@ZZAnnotation(foo1= 0 + ZZ int bar) {\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={S,I,J,F,D,C,B,Ljava.lang.String;}\n" +
				"expectedTypesKeys={S,I,J,F,D,C,B,Ljava/lang/String;}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0177, Ltest0177.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0177, Ltest0177.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0177.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0178() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0178/ZZAnnotation.js",
				"package test0178;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int[] foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0178/ZZClass.js",
				"package test0178;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0178/Test.js",
				"package test0178;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  void baz(@ZZAnnotation(foo1= {ZZ} int bar) {\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"zzint[FIELD_REF]{zzint, Ltest0178.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0178, Ltest0178.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0178, Ltest0178.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0179() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0179/ZZAnnotation.js",
				"package test0179;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int[] foo1();\n" +
				"  int foo2();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0179/ZZClass.js",
				"package test0179;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0179/Test.js",
				"package test0179;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  void baz(@ZZAnnotation(foo1= {ZZ int bar) {\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"zzint[FIELD_REF]{zzint, Ltest0179.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0179, Ltest0179.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0179, Ltest0179.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0180() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0180/ZZAnnotation.js",
				"package test0180;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int value();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0180/ZZClass.js",
				"package test0180;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0180/Test.js",
				"package test0180;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation(ZZ)\n" +
				"  void bar() {\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={I}\n" +
				"expectedTypesKeys={I}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0180, Ltest0180.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0180, Ltest0180.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0180.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0181() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0181/ZZAnnotation.js",
				"package test0181;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int value();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0181/ZZClass.js",
				"package test0181;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0181/Test.js",
				"package test0181;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation(0 + ZZ)\n" +
				"  void bar() {\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={S,I,J,F,D,C,B,Ljava.lang.String;}\n" +
				"expectedTypesKeys={S,I,J,F,D,C,B,Ljava/lang/String;}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0181, Ltest0181.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0181, Ltest0181.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0181.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0182() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0182/ZZAnnotation.js",
				"package test0182;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int[] value();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0182/ZZClass.js",
				"package test0182;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0182/Test.js",
				"package test0182;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation({ZZ})\n" +
				"  void bar() {\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"zzint[FIELD_REF]{zzint, Ltest0182.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0182, Ltest0182.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0182, Ltest0182.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0183() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0183/ZZAnnotation.js",
				"package test0183;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int value();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0183/ZZClass.js",
				"package test0183;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0183/Test.js",
				"package test0183;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation(ZZ\n" +
				"  void bar() {\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={I}\n" +
				"expectedTypesKeys={I}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0183, Ltest0183.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0183, Ltest0183.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0183.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0184() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0184/ZZAnnotation.js",
				"package test0184;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int value();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0184/ZZClass.js",
				"package test0184;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0184/Test.js",
				"package test0184;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation(0 + ZZ\n" +
				"  void bar() {\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures={S,I,J,F,D,C,B,Ljava.lang.String;}\n" +
				"expectedTypesKeys={S,I,J,F,D,C,B,Ljava/lang/String;}",
				result.context);
		
		assertResults(
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0184, Ltest0184.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0184, Ltest0184.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"zzint[FIELD_REF]{zzint, Ltest0184.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0185() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0185/ZZAnnotation.js",
				"package test0185;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int[] value();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0185/ZZClass.js",
				"package test0185;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0185/Test.js",
				"package test0185;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation({ZZ}\n" +
				"  void bar() {\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"zzint[FIELD_REF]{zzint, Ltest0185.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0185, Ltest0185.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0185, Ltest0185.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// complete annotation attribute value
public void test0186() throws JavaScriptModelException {
	IJavaScriptUnit anAnnotation = null;
	IJavaScriptUnit aClass = null;
	try {
		anAnnotation = getWorkingCopy(
				"/Completion/src3/test0186/ZZAnnotation.js",
				"package test0186;\n" +
				"public @interface ZZAnnotation {\n" +
				"  int[] value();\n" +
				"}");
		
		aClass = getWorkingCopy(
				"/Completion/src3/test0186/ZZClass.js",
				"package test0186;\n" +
				"public class ZZClass {\n" +
				"}");
		
		CompletionResult result = complete(
				"/Completion/src3/test0186/Test.js",
				"package test0186;\n" +
				"public class Test {\n" +
				"  public static final int zzint = 0;\n" +
				"  @ZZAnnotation({ZZ\n" +
				"  void bar() {\n" +
				"  }\n" +
				"}",
				"ZZ");
		
		assertResults(
				"expectedTypesSignatures=null\n" +
				"expectedTypesKeys=null",
				result.context);
		
		assertResults(
				"zzint[FIELD_REF]{zzint, Ltest0186.Test;, I, zzint, null, " + (R_DEFAULT + R_INTERESTING + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZAnnotation[TYPE_REF]{ZZAnnotation, test0186, Ltest0186.ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"ZZClass[TYPE_REF]{ZZClass, test0186, Ltest0186.ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(anAnnotation != null) {
			anAnnotation.discardWorkingCopy();
		}
		if(aClass != null) {
			aClass.discardWorkingCopy();
		}
	}
}
// completion test with capture
public void test0187() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0187/Test.js",
            "package test0187;\n" +
            "public class Test<U> {\n" +
            "  void bar(ZZClass1<? extends U> var) {\n" +
            "    var.zzz\n" +
            "  }\n" +
            "}\n" +
            "abstract class ZZClass1<V> {\n" +
            "  ZZClass2<V>[] zzz1;\n"+
            "  abstract ZZClass2<V>[] zzz2();\n" +
            "}\n" +
            "abstract class ZZClass2<T> {\n" +
            "}",
            "var.zzz");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "zzz1[FIELD_REF]{zzz1, Ltest0187.ZZClass1<!+TU;>;, [Ltest0187.ZZClass2<!+TU;>;, zzz1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_STATIC + R_NON_RESTRICTED) + "}\n" +
            "zzz2[FUNCTION_REF]{zzz2(), Ltest0187.ZZClass1<!+TU;>;, ()[Ltest0187.ZZClass2<!+TU;>;, zzz2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_NON_STATIC + R_NON_RESTRICTED) + "}",
            result.proposals);
}
// completion test with capture
public void test0188() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0188/Test.js",
            "package test0188;\n" +
            "public class Test<U> {\n" +
            "  ZZClass1<? extends U> var1;\n" +
            "  void bar(ZZClass1<? extends U> var2) {\n" +
            "    var\n" +
            "  }\n" +
            "}\n" +
            "abstract class ZZClass1<V> {\n" +
            "  ZZClass2<V>[] zzz1;\n"+
            "  abstract ZZClass2<V>[] zzz2();\n" +
            "}\n" +
            "abstract class ZZClass2<T> {\n" +
            "}",
            "var");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "var1[FIELD_REF]{var1, Ltest0188.Test<TU;>;, Ltest0188.ZZClass1<+TU;>;, var1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED+ R_NON_RESTRICTED) + "}\n" +
            "var2[LOCAL_VARIABLE_REF]{var2, null, Ltest0188.ZZClass1<+TU;>;, var2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
            result.proposals);
}
// completion test with capture
public void test0189() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0189/Test.js",
            "package test0189;\n" +
            "public class Test<U> {\n" +
            "  void bar(ZZClass3 var) {\n" +
            "    var.zzz\n" +
            "  }\n" +
            "}\n" +
            "abstract class ZZClass2<T> {\n" +
            "}\n" +
            "class ZZClass3 {\n" +
             "  ZZClass2<? extends Object> zzz1;\n"+
            "}",
            "var.zzz");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "zzz1[FIELD_REF]{zzz1, Ltest0189.ZZClass3;, Ltest0189.ZZClass2<+Ljava.lang.Object;>;, zzz1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_NON_STATIC + R_NON_RESTRICTED) + "}",
            result.proposals);
}
// completion test with capture
public void test0190() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0190/Test.js",
            "package test0190;\n" +
            "public class Test<U> {\n" +
            "  ZZClass1<? extends U> var1\n" +
            "  void bar(ZZClass3<Object> var2) {\n" +
            "    var2.toto().zzz\n" +
            "  }\n" +
            "}\n" +
            "abstract class ZZClass1<V> {\n" +
            "  ZZClass2<V>[] zzz1;\n"+
            "  abstract ZZClass2<V>[] zzz2();\n" +
            "}\n" +
            "abstract class ZZClass2<T> {\n" +
            "}\n" +
            "abstract class ZZClass3<T> {\n" +
            "  ZZClass1<? extends T> toto() {\n" +
            "    return null;\n" +
            "  }\n" +
            "}",
            "toto().zzz");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "zzz1[FIELD_REF]{zzz1, Ltest0190.ZZClass1<!+Ljava.lang.Object;>;, [Ltest0190.ZZClass2<!+Ljava.lang.Object;>;, zzz1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_STATIC+ R_NON_RESTRICTED) + "}\n" +
            "zzz2[FUNCTION_REF]{zzz2(), Ltest0190.ZZClass1<!+Ljava.lang.Object;>;, ()[Ltest0190.ZZClass2<!+Ljava.lang.Object;>;, zzz2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_NON_STATIC + R_NON_RESTRICTED) + "}",
            result.proposals);
}
// completion test with capture
public void test0191() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0191/Test.js",
            "package test0191;\n" +
            "public class Test<U> {\n" +
            "  ZZClass1<? extends U> var1;\n" +
            "  void bar(ZZClass1<? extends U> zzzvar, ZZClass1<? extends U> var2) {\n" +
            "    zzzvar = var\n" +
            "  }\n" +
            "}\n" +
            "abstract class ZZClass1<V> {\n" +
            "  ZZClass2<V>[] zzz1;\n"+
            "  abstract ZZClass2<V>[] zzz2();\n" +
            "}\n" +
            "abstract class ZZClass2<T> {\n" +
            "}",
            "var");
    
    assertResults(
            "expectedTypesSignatures={Ltest0191.ZZClass1<+TU;>;}\n" +
            "expectedTypesKeys={Ltest0191/Test~ZZClass1<Ltest0191/Test~ZZClass1;+Ltest0191/Test;:TU;>;}",
            result.context);
    
    assertResults(
            "var1[FIELD_REF]{var1, Ltest0191.Test<TU;>;, Ltest0191.ZZClass1<+TU;>;, var1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_EXACT_EXPECTED_TYPE + R_NON_RESTRICTED) + "}\n" +
            "var2[LOCAL_VARIABLE_REF]{var2, null, Ltest0191.ZZClass1<+TU;>;, var2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + + R_UNQUALIFIED + R_EXACT_EXPECTED_TYPE + R_NON_RESTRICTED) + "}",
            result.proposals);
}
public void test0192() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0192/Test.js",
            "package test0192;\n" +     
            "class ZZClass1<X,Y> {\n" +
            "}\n" +
            "public class Test {\n" +
            "  ZZClass1<\n" +
            "}",
            "ZZClass1<");
    
    assertResults(
            "expectedTypesSignatures={Ljava.lang.Object;}\n" +
            "expectedTypesKeys={Ljava/lang/Object;}",
            result.context);
    
    assertResults(
            "ZZClass1<X,Y>[TYPE_REF]{, test0192, Ltest0192.ZZClass1<TX;TY;>;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXPECTED_TYPE + R_EXACT_NAME+ R_UNQUALIFIED + + R_NON_RESTRICTED) + "}",
            result.proposals);
}
public void test0193() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0193/Test.js",
            "package test0193;\n" +
            "class ZZClass1<X,Y> {\n" +
            "}\n" +
            "public class Test {\n" +
            "  void foo(){\n" +
            "    ZZClass1<\n" +
            "  }\n" +
            "}",
            "ZZClass1<");
    
    assertResults(
            "expectedTypesSignatures={Ljava.lang.Object;}\n" +
            "expectedTypesKeys={Ljava/lang/Object;}",
            result.context);
    
    assertResults(
            "ZZClass1<X,Y>[TYPE_REF]{, test0193, Ltest0193.ZZClass1<TX;TY;>;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXPECTED_TYPE + R_EXACT_NAME + R_UNQUALIFIED + + R_NON_RESTRICTED) + "}",
            result.proposals);
}
public void test0194() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0194/Test.js",
            "package test0194;\n" +     
            "class ZZClass1<X,Y> {\n" +
            "}\n" +
            "public class Test {\n" +
            "  ZZClass1<Object,\n" +
            "}",
            "ZZClass1<Object,");
    
    assertResults(
            "expectedTypesSignatures={Ljava.lang.Object;}\n" +
            "expectedTypesKeys={Ljava/lang/Object;}",
            result.context);
    
    assertResults(
            "ZZClass1<X,Y>[TYPE_REF]{, test0194, Ltest0194.ZZClass1<TX;TY;>;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXPECTED_TYPE + R_EXACT_NAME+ R_UNQUALIFIED + + R_NON_RESTRICTED) + "}",
            result.proposals);
}
public void test0195() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0195/Test.js",
            "package test0195;\n" +
            "class ZZClass1<X,Y> {\n" +
            "}\n" +
            "public class Test {\n" +
            "  void foo(){\n" +
            "    ZZClass1<Object,\n" +
            "  }\n" +
            "}",
            "ZZClass1<Object,");
    
    assertResults(
            "expectedTypesSignatures={Ljava.lang.Object;}\n" +
            "expectedTypesKeys={Ljava/lang/Object;}",
            result.context);
    
    assertResults(
            "ZZClass1<X,Y>[TYPE_REF]{, test0195, Ltest0195.ZZClass1<TX;TY;>;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXPECTED_TYPE + R_EXACT_NAME + R_UNQUALIFIED + + R_NON_RESTRICTED) + "}",
            result.proposals);
}
public void test0196() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0196/Test.js",
            "package test0196;\n" +
            "class ZZAnnot {\n" +
            "  int foo1();\n" +
            "  int foo2();\n" +
            "}\n" +
            "@ZZAnnot(\n" +
            "public class Test {\n" +
            "}",
            "@ZZAnnot(");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "",
            result.proposals);
}
public void test0196b() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0196/Test.js",
            "package test0196;\n" +
            "@interface ZZAnnot {\n" +
            "  int foo1();\n" +
            "  int foo2();\n" +
            "}\n" +
            "@ZZAnnot(\n" +
            "public class Test {\n" +
            "}",
            "@ZZAnnot(");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
    		"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0196.ZZAnnot;, I, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
            "foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0196.ZZAnnot;, I, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
            result.proposals);
}
public void test0197() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0197/Test.js",
            "package test0197;\n" +
            "class ZZAnnot {\n" +
            "  int foo1();\n" +
            "  int foo2();\n" +
            "}\n" +
            "public class Test {\n" +
            "  @ZZAnnot(\n" +
            "  void foo(){}\n" +
            "}",
            "@ZZAnnot(");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "",
            result.proposals);
}
public void test0197b() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0197/Test.js",
            "package test0197;\n" +
            "@interface ZZAnnot {\n" +
            "  int foo1();\n" +
            "  int foo2();\n" +
            "}\n" +
            "public class Test {\n" +
            "  @ZZAnnot(\n" +
            "  void foo(){}\n" +
            "}",
            "@ZZAnnot(");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
    		"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0197.ZZAnnot;, I, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
            "foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0197.ZZAnnot;, I, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
            result.proposals);
}
public void test0198() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0198/Test.js",
            "package test0198;\n" +
            "class ZZAnnot {\n" +
            "  int foo1();\n" +
            "  int foo2();\n" +
            "}\n" +
            
            "public class Test {\n" +
            "  @ZZAnnot(\n" +
            "}",
            "@ZZAnnot(");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "",
            result.proposals);
}
public void test0198b() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0198/Test.js",
            "package test0198;\n" +
            "@interface ZZAnnot {\n" +
            "  int foo1();\n" +
            "  int foo2();\n" +
            "}\n" +
            
            "public class Test {\n" +
            "  @ZZAnnot(\n" +
            "}",
            "@ZZAnnot(");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
    		"foo1[ANNOTATION_ATTRIBUTE_REF]{foo1, Ltest0198.ZZAnnot;, I, foo1, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
            "foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0198.ZZAnnot;, I, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
            result.proposals);
}
public void test0199() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0199/Test.js",
            "package test0199;\n" +
            "class ZZAnnot {\n" +
            "  int foo1();\n" +
            "  int foo2();\n" +
            "}\n" +
            "@ZZAnnot(foo1=0,\n" +
            "public class Test {\n" +
            "}",
            "@ZZAnnot(foo1=0,");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "",
            result.proposals);
}
public void test0199b() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0199/Test.js",
            "package test0199;\n" +
            "@interface ZZAnnot {\n" +
            "  int foo1();\n" +
            "  int foo2();\n" +
            "}\n" +
            "@ZZAnnot(foo1=0,\n" +
            "public class Test {\n" +
            "}",
            "@ZZAnnot(foo1=0,");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
    		"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0199.ZZAnnot;, I, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
            result.proposals);
}
public void test0200() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0200/Test.js",
            "package test0200;\n" +
            "class ZZAnnot {\n" +
            "  int foo1();\n" +
            "  int foo2();\n" +
            "}\n" +
            "public class Test {\n" +
            "  @ZZAnnot(foo1=0,\n" +
            "  void foo(){}\n" +
            "}",
            "@ZZAnnot(foo1=0,");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "",
            result.proposals);
}
public void test0200b() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0200/Test.js",
            "package test0200;\n" +
            "@interface ZZAnnot {\n" +
            "  int foo1();\n" +
            "  int foo2();\n" +
            "}\n" +
            "public class Test {\n" +
            "  @ZZAnnot(foo1=0,\n" +
            "  void foo(){}\n" +
            "}",
            "@ZZAnnot(foo1=0,");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
    		"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0200.ZZAnnot;, I, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
            result.proposals);
}
public void test0201() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0201/Test.js",
            "package test0201;\n" +
            "class ZZAnnot {\n" +
            "  int foo1();\n" +
            "  int foo2();\n" +
            "}\n" +
            
            "public class Test {\n" +
            "  @ZZAnnot(foo1=0,\n" +
            "}",
            "@ZZAnnot(foo1=0,");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
            "",
            result.proposals);
}
public void test0201b() throws JavaScriptModelException {
    CompletionResult result = complete(
            "/Completion/src3/test0201/Test.js",
            "package test0201;\n" +
            "@interface ZZAnnot {\n" +
            "  int foo1();\n" +
            "  int foo2();\n" +
            "}\n" +
            
            "public class Test {\n" +
            "  @ZZAnnot(foo1=0,\n" +
            "}",
            "@ZZAnnot(foo1=0,");
    
    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
    
    assertResults(
    		"foo2[ANNOTATION_ATTRIBUTE_REF]{foo2, Ltest0201.ZZAnnot;, I, foo2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
            result.proposals);
}
public void test0202() throws JavaScriptModelException {
	IJavaScriptUnit aType = null;
	try {
		aType = getWorkingCopy(
	            "/Completion/src3/p/ZZType.js",
	            "package p;\n" +
	            "public class ZZType {\n" +
	            "  public class ZZClass {" +
	            "  }" +
	            "  public interface ZZInterface {" +
	            "  }" +
	            "  public enum ZZEnum {" +
	            "  }" +
	            "  public @interface ZZAnnotation {" +
	            "  }" +
	            "}");
		
	    CompletionResult result = complete(
	            "/Completion/src3/test0202/Test.js",
	            "package test0202;\n" +
	            "public class Test {\n" +
	            "  public void foo() {" +
	            "    ZZ" +
	            "  }" +
	            "}",
            	"ZZ");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	    
	    assertResults(
	            "ZZType[TYPE_REF]{p.ZZType, p, Lp.ZZType;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"ZZType.ZZAnnotation[TYPE_REF]{p.ZZType.ZZAnnotation, p, Lp.ZZType$ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"ZZType.ZZClass[TYPE_REF]{p.ZZType.ZZClass, p, Lp.ZZType$ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"ZZType.ZZEnum[TYPE_REF]{p.ZZType.ZZEnum, p, Lp.ZZType$ZZEnum;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"ZZType.ZZInterface[TYPE_REF]{p.ZZType.ZZInterface, p, Lp.ZZType$ZZInterface;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
	            result.proposals);
	} finally {
		if(aType != null) {
			aType.discardWorkingCopy();
		}
	}
}
public void test0203() throws JavaScriptModelException {
	IJavaScriptUnit aType = null;
	try {
		aType = getWorkingCopy(
	            "/Completion/src3/p/ZZType.js",
	            "package p;\n" +
	            "public class ZZType {\n" +
	            "  public class ZZClass {" +
	            "  }" +
	            "  public interface ZZInterface {" +
	            "  }" +
	            "  public enum ZZEnum {" +
	            "  }" +
	            "  public @interface ZZAnnotation {" +
	            "  }" +
	            "}");
		
	    CompletionResult result = complete(
	            "/Completion/src3/test0203/Test.js",
	            "package test0203;\n" +
	            "public class Test extends ZZ{\n" +
	            "}",
            	"ZZ");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	    
	    assertResults(
	            "ZZType[TYPE_REF]{p.ZZType, p, Lp.ZZType;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_CLASS + R_NON_RESTRICTED) + "}\n" +
				"ZZType.ZZClass[TYPE_REF]{p.ZZType.ZZClass, p, Lp.ZZType$ZZClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_CLASS + R_NON_RESTRICTED) + "}",
	            result.proposals);
	} finally {
		if(aType != null) {
			aType.discardWorkingCopy();
		}
	}
}
public void test0204() throws JavaScriptModelException {
	IJavaScriptUnit aType = null;
	try {
		aType = getWorkingCopy(
	            "/Completion/src3/p/ZZType.js",
	            "package p;\n" +
	            "public class ZZType {\n" +
	            "  public class ZZClass {" +
	            "  }" +
	            "  public interface ZZInterface {" +
	            "  }" +
	            "  public enum ZZEnum {" +
	            "  }" +
	            "  public @interface ZZAnnotation {" +
	            "  }" +
	            "}");
		
	    CompletionResult result = complete(
	            "/Completion/src3/test0204/Test.js",
	            "package test0204;\n" +
	            "public interface Test extends ZZ{\n" +
	            "}",
            	"ZZ");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	    
	    assertResults(
	            "ZZType.ZZAnnotation[TYPE_REF]{p.ZZType.ZZAnnotation, p, Lp.ZZType$ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_INTERFACE + R_NON_RESTRICTED) + "}\n" +
				"ZZType.ZZInterface[TYPE_REF]{p.ZZType.ZZInterface, p, Lp.ZZType$ZZInterface;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_INTERFACE + R_NON_RESTRICTED) + "}",
	            result.proposals);
	} finally {
		if(aType != null) {
			aType.discardWorkingCopy();
		}
	}
}
public void test0205() throws JavaScriptModelException {
	IJavaScriptUnit aType = null;
	try {
		aType = getWorkingCopy(
	            "/Completion/src3/p/ZZType.js",
	            "package p;\n" +
	            "public class ZZType {\n" +
	            "  public class ZZClass {" +
	            "  }" +
	            "  public interface ZZInterface {" +
	            "  }" +
	            "  public enum ZZEnum {" +
	            "  }" +
	            "  public @interface ZZAnnotation {" +
	            "  }" +
	            "}");
		
	    CompletionResult result = complete(
	            "/Completion/src3/test0205/Test.js",
	            "package test0205;\n" +
	            "public class Test implements ZZ {\n" +
	            "}",
            	"ZZ");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	    
	    assertResults(
	            "ZZType.ZZAnnotation[TYPE_REF]{p.ZZType.ZZAnnotation, p, Lp.ZZType$ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_INTERFACE + R_NON_RESTRICTED) + "}\n" +
				"ZZType.ZZInterface[TYPE_REF]{p.ZZType.ZZInterface, p, Lp.ZZType$ZZInterface;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_INTERFACE + R_NON_RESTRICTED) + "}",
	            result.proposals);
	} finally {
		if(aType != null) {
			aType.discardWorkingCopy();
		}
	}
}
public void test0206() throws JavaScriptModelException {
	IJavaScriptUnit aType = null;
	try {
		aType = getWorkingCopy(
	            "/Completion/src3/p/ZZType.js",
	            "package p;\n" +
	            "public class ZZType {\n" +
	            "  public class ZZClass {" +
	            "  }" +
	            "  public interface ZZInterface {" +
	            "  }" +
	            "  public enum ZZEnum {" +
	            "  }" +
	            "  public @interface ZZAnnotation {" +
	            "  }" +
	            "}");
		
	    CompletionResult result = complete(
	            "/Completion/src3/test0206/Test.js",
	            "package test0206;\n" +
	            "@ZZ\n" +
	            "public class Test {\n" +
	            "}",
            	"ZZ");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	    
	    assertResults(
	            "ZZType.ZZAnnotation[TYPE_REF]{p.ZZType.ZZAnnotation, p, Lp.ZZType$ZZAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_ANNOTATION + R_NON_RESTRICTED) + "}",
	            result.proposals);
	} finally {
		if(aType != null) {
			aType.discardWorkingCopy();
		}
	}
}
// bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=93254
public void test0207() throws JavaScriptModelException {
	IJavaScriptUnit aType = null;
	try {
		aType = getWorkingCopy(
	            "/Completion/src3/p/Annot.js",
	            "package p;\n" +
	            "public @interface Annot {\n" +
	            "}");
		
	    CompletionResult result = complete(
	            "/Completion/src3/test0207/Test.js",
	            "package test0206;\n" +
	            "@p.Annot\n",
            	"@p.Annot");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	    
	    assertResults(
	            "Annot[TYPE_REF]{p.Annot, p, Lp.Annot;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_ANNOTATION + R_QUALIFIED + R_NON_RESTRICTED) + "}",
	            result.proposals);
	} finally {
		if(aType != null) {
			aType.discardWorkingCopy();
		}
	}
}
public void test0208() throws JavaScriptModelException {
	IJavaScriptUnit aType = null;
	try {
		aType = getWorkingCopy(
	            "/Completion/src3/p/Colors.js",
	            "package p;\n" +
	            "public enum Colors { BLACK, BLUE, WHITE, RED }\n");
		
	    CompletionResult result = complete(
	            "/Completion/src3/test0208/Test.js",
	            "package test0208;\n" +
	            "public class Test {\n" +
	            "  static final String BLANK = \"    \";\n" +
	            "  void foo(p.Colors color) {\n" +
	            "    switch (color) {\n" +
	            "      case BLUE:\n" +
	            "      case RED:\n" +
	            "        break;\n" +
	            "      case \n" +
	            "    }\n" +
	            "  }\n" +
	            "}",
            	"case ");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	    
	    assertResults(
	            "BLACK[FIELD_REF]{BLACK, Lp.Colors;, Lp.Colors;, BLACK, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_ENUM + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"WHITE[FIELD_REF]{WHITE, Lp.Colors;, Lp.Colors;, WHITE, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_ENUM + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
	            result.proposals);
	} finally {
		if(aType != null) {
			aType.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=94303
 */
public void test0209() throws JavaScriptModelException {
	IJavaScriptUnit importedClass = null;
	try {
		importedClass = getWorkingCopy(
				"/Completion/src3/test0209/p/ImportedClass.js",
				"package test0209.p;\n"+
				"\n"+
				"public class ImportedClass {\n"+
				"	public static class ImportedMember {\n"+
				"	}\n"+
				"}");

		CompletionResult result = complete(
	            "/Completion/src3/test0209/Test.js",
	            "package test0209;\n" +
	            "\n" +
	            "import static Imported\n" +
	            "\n" +
	            "public class Test {\n" +
	            "	\n" +
	            "}",
            	"Imported");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	
		assertResults(
				"ImportedClass[TYPE_REF]{test0209.p.ImportedClass., test0209.p, Ltest0209.p.ImportedClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"ImportedClass.ImportedMember[TYPE_REF]{test0209.p.ImportedClass.ImportedMember;, test0209.p, Ltest0209.p.ImportedClass$ImportedMember;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(importedClass != null) {
			importedClass.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=94303
 */
public void test0210() throws JavaScriptModelException {
	IJavaScriptUnit importedClass = null;
	try {
		importedClass = getWorkingCopy(
				"/Completion/src3/test0210/p/ImportedClass.js",
				"package test0210.p;\n"+
				"\n"+
				"public class ImportedClass {\n"+
				"	public class ImportedMember {\n"+
				"	}\n"+
				"}");

		CompletionResult result = complete(
	            "/Completion/src3/test0210/Test.js",
	            "package test0210;\n" +
	            "\n" +
	            "import static test0210.p.ImportedClass.Im\n" +
	            "\n" +
	            "public class Test {\n" +
	            "	\n" +
	            "}",
            	"test0210.p.ImportedClass.Im");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	
		assertResults(
				"",
				result.proposals);
	} finally {
		if(importedClass != null) {
			importedClass.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=94303
 */
public void test0211() throws JavaScriptModelException {
	IJavaScriptUnit importedClass = null;
	try {
		importedClass = getWorkingCopy(
				"/Completion/src3/test0211/p/ImportedClass.js",
				"package test0211.p;\n"+
				"\n"+
				"public class ImportedClass {\n"+
				"	public static class ImportedMember {\n"+
				"	}\n"+
				"}");

		CompletionResult result = complete(
	            "/Completion/src3/test0211/Test.js",
	            "package test0211;\n" +
	            "\n" +
	            "import static test0211.p.ImportedClass.Im\n" +
	            "\n" +
	            "public class Test {\n" +
	            "	\n" +
	            "}",
            	"test0211.p.ImportedClass.Im");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	
		assertResults(
				"ImportedClass.ImportedMember[TYPE_REF]{test0211.p.ImportedClass.ImportedMember;, test0211.p, Ltest0211.p.ImportedClass$ImportedMember;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(importedClass != null) {
			importedClass.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=94303
 */
public void test0212() throws JavaScriptModelException {
	IJavaScriptUnit importedClass = null;
	try {
		importedClass = getWorkingCopy(
				"/Completion/src3/test0212/p/ImportedClass.js",
				"package test0212.p;\n"+
				"\n"+
				"public class ImportedClass {\n"+
				"	public static class ImportedMember {\n"+
				"	}\n"+
				"}");

		CompletionResult result = complete(
	            "/Completion/src3/test0212/Test.js",
	            "package test0212;\n" +
	            "\n" +
	            "import test0212.p.Im\n" +
	            "\n" +
	            "public class Test {\n" +
	            "	\n" +
	            "}",
            	"test0212.p.Im");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	
		assertResults(
				"ImportedClass[TYPE_REF]{test0212.p.ImportedClass;, test0212.p, Ltest0212.p.ImportedClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(importedClass != null) {
			importedClass.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=94303
 */
public void test0213() throws JavaScriptModelException {
	IJavaScriptUnit importedClass = null;
	try {
		importedClass = getWorkingCopy(
				"/Completion/src3/test0213/p/ImportedClass.js",
				"package test0213.p;\n"+
				"\n"+
				"public class ImportedClass {\n"+
				"}");

		CompletionResult result = complete(
	            "/Completion/src3/test0213/Test.js",
	            "package test0213;\n" +
	            "\n" +
	            "import test0213.p.Im\n" +
	            "\n" +
	            "public class Test {\n" +
	            "	\n" +
	            "}",
            	"test0213.p.Im");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	
		assertResults(
				"ImportedClass[TYPE_REF]{test0213.p.ImportedClass;, test0213.p, Ltest0213.p.ImportedClass;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(importedClass != null) {
			importedClass.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=93249
 */
public void test0214() throws JavaScriptModelException {
	IJavaScriptUnit paramClass1 = null;
	IJavaScriptUnit paramClass2 = null;
	IJavaScriptUnit superClass = null;
	try {
		paramClass1 = getWorkingCopy(
				"/Completion/src3/test0214/AClass1.js",
				"package test0214;\n"+
				"\n"+
				"public class AClass1 {\n"+
				"}");
		
		paramClass2 = getWorkingCopy(
				"/Completion/src3/test0214/AClass2.js",
				"package test0214;\n"+
				"\n"+
				"public class AClass2 {\n"+
				"}");
		
		superClass = getWorkingCopy(
				"/Completion/src3/test0214/SuperClass.js",
				"package test0214;\n"+
				"\n"+
				"public class SuperClass<T> {\n"+
				"  public <M extends AClass1> void foo(M p1) {\n" +
				"  }\n" +
				"  public <M extends AClass2> void foo(M p2) {\n" +
				"  }\n" +
				"}");

		CompletionResult result = complete(
	            "/Completion/src3/test0214/Test.js",
	            "package test0214;\n" +
	            "\n" +
	            "public class Test<Z> extends SuperClass<Z>{\n" +
	            "	foo\n" +
	            "}",
            	"foo");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	
		assertResults(
				"foo[POTENTIAL_METHOD_DECLARATION]{foo, Ltest0214.Test<TZ;>;, ()V, foo, null, " + (R_DEFAULT + R_INTERESTING + R_NON_RESTRICTED) + "}\n" +
				"foo[FUNCTION_DECLARATION]{public <M extends AClass1> void foo(M p1), Ltest0214.SuperClass<TZ;>;, <M:Ltest0214.AClass1;>(TM;)V, foo, (p1), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_METHOD_OVERIDE + R_NON_RESTRICTED) + "}\n" +
				"foo[FUNCTION_DECLARATION]{public <M extends AClass2> void foo(M p2), Ltest0214.SuperClass<TZ;>;, <M:Ltest0214.AClass2;>(TM;)V, foo, (p2), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_METHOD_OVERIDE + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(paramClass1 != null) {
			paramClass1.discardWorkingCopy();
		}
		if(paramClass2 != null) {
			paramClass2.discardWorkingCopy();
		}
		if(superClass != null) {
			superClass.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=93249
 */
public void test0215() throws JavaScriptModelException {
	IJavaScriptUnit paramClass = null;
	IJavaScriptUnit superClass = null;
	try {
		paramClass = getWorkingCopy(
				"/Completion/src3/test0215/p/ParamClass.js",
				"package test0215.p;\n"+
				"\n"+
				"public class ParamClass {\n"+
				"  public class MemberParamClass<P2> {\n" +
				"  }\n" +
				"}");
		
		superClass = getWorkingCopy(
				"/Completion/src3/test0215/SuperClass.js",
				"package test0215;\n"+
				"\n"+
				"public class SuperClass<T> {\n"+
				"  public <M extends SuperClass<T>> SuperClass<?> foo(test0215.p.ParamClass.MemberParamClass<? super T> p1, int p2) throws Exception {\n" +
				"    return null;\n" +
				"  }\n" +
				"}");

		CompletionResult result = complete(
	            "/Completion/src3/test0215/Test.js",
	            "package test0215;\n" +
	            "\n" +
	            "public class Test<Z> extends SuperClass<Z>{\n" +
	            "	foo\n" +
	            "}",
            	"foo");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	
		assertResults(
				"foo[POTENTIAL_METHOD_DECLARATION]{foo, Ltest0215.Test<TZ;>;, ()V, foo, null, " + (R_DEFAULT + R_INTERESTING + R_NON_RESTRICTED) + "}\n" +
				"foo[FUNCTION_DECLARATION]{public <M extends test0215.SuperClass<Z>> test0215.SuperClass<?> foo(test0215.p.ParamClass.MemberParamClass<? super Z> p1, int p2) throws Exception, Ltest0215.SuperClass<TZ;>;, <M:Ltest0215.SuperClass<TZ;>;>(Ltest0215.p.ParamClass$MemberParamClass<-TZ;>;I)Ltest0215.SuperClass<*>;, foo, (p1, p2), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_METHOD_OVERIDE + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(paramClass != null) {
			paramClass.discardWorkingCopy();
		}
		if(superClass != null) {
			superClass.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=93249
 */
public void test0216() throws JavaScriptModelException {
	IJavaScriptUnit paramClass1 = null;
	IJavaScriptUnit paramClass2 = null;
	IJavaScriptUnit superClass = null;
	try {
		paramClass1 = getWorkingCopy(
				"/Completion/src3/test0216/p/ParamClass.js",
				"package test0216.p;\n"+
				"\n"+
				"public class ParamClass {\n"+
				"}");
		
		paramClass2 = getWorkingCopy(
				"/Completion/src3/test0216/q/ParamClass.js",
				"package test0216.q;\n"+
				"\n"+
				"public class ParamClass {\n"+
				"}");
		
		superClass = getWorkingCopy(
				"/Completion/src3/test0216/SuperClass.js",
				"package test0216;\n"+
				"\n"+
				"public class SuperClass<T> {\n"+
				"  public void foo(test0216.p.ParamClass p1) {\n" +
				"  }\n" +
				"  public void foo(test0216.q.ParamClass p2) {\n" +
				"  }\n" +
				"}");

		CompletionResult result = complete(
	            "/Completion/src3/test0216/Test.js",
	            "package test0216;\n" +
	            "\n" +
	            "public class Test<Z> extends SuperClass<Z>{\n" +
	            "	foo\n" +
	            "}",
            	"foo");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	
		assertResults(
				"foo[POTENTIAL_METHOD_DECLARATION]{foo, Ltest0216.Test<TZ;>;, ()V, foo, null, " + (R_DEFAULT + R_INTERESTING + R_NON_RESTRICTED) + "}\n" +
				"foo[FUNCTION_DECLARATION]{public void foo(test0216.p.ParamClass p1), Ltest0216.SuperClass<TZ;>;, (Ltest0216.p.ParamClass;)V, foo, (p1), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_METHOD_OVERIDE + R_NON_RESTRICTED) + "}\n" +
				"foo[FUNCTION_DECLARATION]{public void foo(test0216.q.ParamClass p2), Ltest0216.SuperClass<TZ;>;, (Ltest0216.q.ParamClass;)V, foo, (p2), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_METHOD_OVERIDE + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(paramClass1 != null) {
			paramClass1.discardWorkingCopy();
		}
		if(paramClass2 != null) {
			paramClass2.discardWorkingCopy();
		}
		if(superClass != null) {
			superClass.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=93119
 */
public void test0217() throws JavaScriptModelException {
	IJavaScriptUnit paramClass1 = null;
	try {
		paramClass1 = getWorkingCopy(
				"/Completion/src3/test0217/AType.js",
				"package test0217;\n"+
				"\n"+
				"public class AType<T> {\n"+
				"}");
		


		CompletionResult result = complete(
	            "/Completion/src3/test0217/Test.js",
	            "package test0217;\n" +
	            "\n" +
	            "public class Test {\n" +
	            "	AType<? ext\n" +
	            "}",
            	"ext");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	
		assertResults(
				"extends[KEYWORD]{extends, null, null, extends, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(paramClass1 != null) {
			paramClass1.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=93119
 */
public void test0218() throws JavaScriptModelException {
	IJavaScriptUnit paramClass1 = null;
	try {
		paramClass1 = getWorkingCopy(
				"/Completion/src3/test0218/AType.js",
				"package test0218;\n"+
				"\n"+
				"public class AType<T> {\n"+
				"}");
		


		CompletionResult result = complete(
	            "/Completion/src3/test0218/Test.js",
	            "package test0218;\n" +
	            "\n" +
	            "public class Test {\n" +
	            "	AType<? sup\n" +
	            "}",
            	"sup");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	
		assertResults(
				"super[KEYWORD]{super, null, null, super, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(paramClass1 != null) {
			paramClass1.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=93119
 */
public void test0219() throws JavaScriptModelException {
	IJavaScriptUnit paramClass1 = null;
	try {
		paramClass1 = getWorkingCopy(
				"/Completion/src3/test0219/AType.js",
				"package test0219;\n"+
				"\n"+
				"public class AType<T> {\n"+
				"}");
		


		CompletionResult result = complete(
	            "/Completion/src3/test0219/Test.js",
	            "package test0219;\n" +
	            "\n" +
	            "public class Test {\n" +
	            "	void foo() {\n" +
	            "	  AType<? ext\n" +
	            "	}\n" +
	            "}",
            	"ext");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	
		assertResults(
				"extends[KEYWORD]{extends, null, null, extends, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(paramClass1 != null) {
			paramClass1.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=93119
 */
public void test0220() throws JavaScriptModelException {
	IJavaScriptUnit paramClass1 = null;
	try {
		paramClass1 = getWorkingCopy(
				"/Completion/src3/test0220/AType.js",
				"package test0220;\n"+
				"\n"+
				"public class AType<T> {\n"+
				"}");
		


		CompletionResult result = complete(
	            "/Completion/src3/test0220/Test.js",
	            "package test0220;\n" +
	            "\n" +
	            "public class Test {\n" +
	            "	void foo() {\n" +
	            "	  AType<? sup\n" +
	            "	}\n" +
	            "}",
            	"sup");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	
		assertResults(
				"super[KEYWORD]{super, null, null, super, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(paramClass1 != null) {
			paramClass1.discardWorkingCopy();
		}
	}
}
public void test0221() throws JavaScriptModelException {
	IJavaScriptUnit paramClass1 = null;
	try {
		paramClass1 = getWorkingCopy(
				"/Completion/src3/test0221/AType.js",
				"package test0221;\n"+
				"\n"+
				"public class AType<T> {\n"+
				"}");
		


		CompletionResult result = complete(
	            "/Completion/src3/test0221/Test.js",
	            "package test0221;\n" +
	            "\n" +
	            "public class Test {\n" +
	            "  AType<? extends ATy\n"+
	            "}",
            	"ATy");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	
		assertResults(
				"AType[TYPE_REF]{AType, test0221, Ltest0221.AType;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(paramClass1 != null) {
			paramClass1.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=96918
 */
public void test0222() throws JavaScriptModelException {
	IJavaScriptUnit paramClass1 = null;
	try {
		paramClass1 = getWorkingCopy(
				"/Completion/src3/test0222/AType.js",
				"package test0222;\n"+
				"\n"+
				"public class AType<T> {\n"+
				"}");
		


		CompletionResult result = complete(
	            "/Completion/src3/test0222/Test.js",
	            "package test0222;\n" +
	            "\n" +
	            "public class Test {\n" +
	            "	void foo() {\n" +
	            "	  AType<? \n" +
	            "	}\n" +
	            "}",
            	"? ");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	
		assertResults(
				"extends[KEYWORD]{extends, null, null, extends, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
				"super[KEYWORD]{super, null, null, super, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(paramClass1 != null) {
			paramClass1.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=97307
 */
public void test0223() throws JavaScriptModelException {
	IJavaScriptUnit paramClass1 = null;
	try {
		paramClass1 = getWorkingCopy(
				"/Completion/src3/test0223/AType.js",
				"package test0223;\n"+
				"\n"+
				"public class AType {\n"+
				"  public static final int VAR = 0;\n"+
				"}");
		


		CompletionResult result = complete(
	            "/Completion/src3/test0223/Test.js",
	            "package test0223;\n" +
	            "\n" +
	            "import static test0223.AType.va\n" +
	            "\n" +
	            "public class Test {\n" +
	            "}",
	            true, // show positions
            	"AType.va");
	    
	
	    assertResults(
	            "expectedTypesSignatures=null\n" +
	            "expectedTypesKeys=null",
	            result.context);
	
	    int end = result.cursorLocation;
		int start = end - "test0223.AType.va".length();
		
		assertResults(
				"VAR[FIELD_REF]{test0223.AType.VAR;, Ltest0223.AType;, I, VAR, null, ["+start+", "+end+"], " + (R_DEFAULT + R_INTERESTING + R_NON_RESTRICTED) + "}",
				result.proposals);
	} finally {
		if(paramClass1 != null) {
			paramClass1.discardWorkingCopy();
		}
	}
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=85384
 */
public void test0224() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0224/Test.js",
            "package test0224;\n" +
            "\n" +
            "public class Test<T ext> {\n" +
            "}",
        	"ext");
    

    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
	
	assertResults(
			"extends[KEYWORD]{extends, null, null, extends, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
			result.proposals);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=85384
 */
public void test0225() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0225/Test.js",
            "package test0225;\n" +
            "\n" +
            "public class Test<T ext\n" +
            "",
        	"ext");
    

    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
	
	assertResults(
			"extends[KEYWORD]{extends, null, null, extends, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
			result.proposals);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=85384
 */
public void test0226() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0226/Test.js",
            "package test0226;\n" +
            "\n" +
            "public class Test {\n" +
            "  public <T ext> void foo() {}\n" +
            "}",
        	"ext");
    

    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
	
	assertResults(
			"extends[KEYWORD]{extends, null, null, extends, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
			result.proposals);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=85384
 */
public void test0227() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0227/Test.js",
            "package test0227;\n" +
            "\n" +
            "public class Test {\n" +
            "  public <T ext\n" +
            "}",
        	"ext");
    

    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);
	
	assertResults(
			"extends[KEYWORD]{extends, null, null, extends, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
			result.proposals);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=97801
 */
public void test0228() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0228/Test.js",
            "package test0228;\n" +
            "\n" +
            "public class Test {\n" +
            "	void foo() {\n" +
            "	  Test.clas \n" +
            "	}\n" +
            "}",
        	".clas");
    

    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);

	assertResults(
			"class[FIELD_REF]{class, null, Ljava.lang.Class<Ltest0228/Test;>;, class, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_INHERITED + R_NON_RESTRICTED) + "}",
			result.proposals);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=97801
 */
public void test0229() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0229/Test.js",
            "package test0229;\n" +
            "\n" +
            "public class Test<T> {\n" +
            "	void foo() {\n" +
            "	  Test.clas \n" +
            "	}\n" +
            "}",
        	".clas");
    

    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);

	assertResults(
			"class[FIELD_REF]{class, null, Ljava.lang.Class<Ltest0229/Test;>;, class, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_INHERITED + R_NON_RESTRICTED) + "}",
			result.proposals);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=96944
public void test0230() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0230/Test.js",
            "package test0230;\n" +
            "\n" +
            "public class Test<ZT> {\n" +
            "  void foo() {\n"+
            "    new ZT\n"+
            "  }\n"+
            "}",
        	"ZT");
    

    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);

	assertResults(
			"",
			result.proposals);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=96944
public void test0231() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0231/Test.js",
            "package test0231;\n" +
            "\n" +
            "public class Test<ZT> {\n" +
            "  void foo() {\n"+
            "    ZT var = new ZT\n"+
            "  }\n"+
            "}",
        	"ZT");
    

    assertResults(
            "expectedTypesSignatures={TZT;}\n" +
            "expectedTypesKeys={Ltest0231/Test;:TZT;}",
            result.context);

	assertResults(
			"",
			result.proposals);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=96944
public void test0232() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0232/Test.js",
            "package test0232;\n" +
            "\n" +
            "public class Test<ZT> {\n" +
            "  void foo() {\n"+
            "    ZT var = new \n"+
            "  }\n"+
            "}",
        	"new ");
    

    assertResults(
            "expectedTypesSignatures={TZT;}\n" +
            "expectedTypesKeys={Ltest0232/Test;:TZT;}",
            result.context);

    if(CompletionEngine.NO_TYPE_COMPLETION_ON_EMPTY_TOKEN) {
		assertResults(
				"",
				result.proposals);
    } else {
    	assertResults(
				"Test<ZT>[TYPE_REF]{Test, test0232, Ltest0232.Test<TZT;>;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				result.proposals);
    }
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=82560
public void test0233() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0233/Test0233Z.js",
            "package test0233;\n" +
            "\n" +
            "public class Test0233Z<ZT> {\n" +
            "  void bar() {\n"+
            "    zzz.<String>foo(new Test0233Z());\n"+
            "  }\n"+
            "  <T> void foo(Object o) {\n"+
            "  }\n"+
            "}",
        	"Test0233Z");
    

    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);

	assertResults(
			"Test0233Z<ZT>[TYPE_REF]{Test0233Z, test0233, Ltest0233.Test0233Z<TZT;>;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME+ R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
			result.proposals);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=97860
public void test0234() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0234/Test.js",
            "package test0234;\n" +
            "\n" +
            "public class Test<ZT> {\n" +
            "  void foo() {\n"+
            "    ZT.c\n"+
            "  }\n"+
            "}",
        	"ZT.c");
    

    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);

	assertResults(
			"",
			result.proposals);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=97860
public void test0235() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0235/Test.js",
            "package test0235;\n" +
            "\n" +
            "public class Test<ZT> {\n" +
            "  void foo() throws ZT.c {\n"+
            "  }\n"+
            "}",
        	"ZT.c");
    

    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);

	assertResults(
			"",
			result.proposals);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=94641
public void test0236() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0236/Test.js",
            "package test0236;\n" +
            "\n" +
            "public class Test<ZT> {\n" +
            "  void foo() {\n"+
            "    new Test<String>();\n"+
            "  }\n"+
            "}",
        	">(");
    

    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);

	assertResults(
			"Test[FUNCTION_REF<CONSTRUCTOR>]{, Ltest0236.Test<Ljava.lang.String;>;, ()V, Test, null, " + (R_DEFAULT + R_INTERESTING + R_NON_RESTRICTED) + "}\n" +
			"Test<java.lang.String>[ANONYMOUS_CLASS_DECLARATION]{, Ltest0236.Test<Ljava.lang.String;>;, ()V, null, null, " + (R_DEFAULT + R_INTERESTING + R_NON_RESTRICTED) + "}",
			result.proposals);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=94907
public void test0237() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0237/Test.js",
            "package test0237;\n" +
            "\n" +
            "public class Test<ZT> ext {\n" +
            "}",
        	"ext");
    

    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);

	assertResults(
			"extends[KEYWORD]{extends, null, null, extends, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
			result.proposals);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=94907
public void test0238() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0238/Test.js",
            "package test0238;\n" +
            "\n" +
            "public class Test<ZT> imp {\n" +
            "}",
        	"imp");
    

    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);

	assertResults(
			"implements[KEYWORD]{implements, null, null, implements, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
			result.proposals);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=94907
public void test0239() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0239/Test.js",
            "package test0239;\n" +
            "\n" +
            "public class Test<ZT> extends Object ext {\n" +
            "}",
        	"ext");
    

    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);

	assertResults(
			"",
			result.proposals);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=94907
public void test0240() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0204/Test.js",
            "package test0240;\n" +
            "\n" +
            "public class Test<ZT> extends Object imp {\n" +
            "}",
        	"imp");
    

    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);

	assertResults(
			"implements[KEYWORD]{implements, null, null, implements, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
			result.proposals);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=94907
public void test0241() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0241/Test.js",
            "package test0241;\n" +
            "\n" +
            "public interface Test<ZT> ext {\n" +
            "}",
        	"ext");
    

    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);

	assertResults(
			"extends[KEYWORD]{extends, null, null, extends, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
			result.proposals);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=94907
public void test0242() throws JavaScriptModelException {
	CompletionResult result = complete(
            "/Completion/src3/test0242/Test.js",
            "package test0242;\n" +
            "\n" +
            "public interface Test<ZT> imp {\n" +
            "}",
        	"imp");
    

    assertResults(
            "expectedTypesSignatures=null\n" +
            "expectedTypesKeys=null",
            result.context);

	assertResults(
			"",
			result.proposals);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=99686
	public void test0243() throws JavaScriptModelException {
		CompletionResult result = complete(
			"/Completion/src3/test0243/X.js",
			"package test0243;\n" + 
			"public class X {\n" + 
			"	void test() {\n" + 
			"		foo(new Object() {}).b\n" + 
			"	}\n" + 
			"	<T> Y<T> foo(T t) {\n" + 
			"		return null;\n" + 
			"	}\n" + 
			"}\n" + 
			"class Y<T> {\n" + 
			"	T bar() {\n" + 
			"		return null;\n" + 
			"	}\n" + 
			"}",
			"foo(new Object() {}).b");

		assertResults(
			"bar[FUNCTION_REF]{bar(), Ltest0243.Y<LObject;>;, ()LObject;, bar, null, 29}", 
			result.proposals);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=100009
public void test0244() throws JavaScriptModelException {
		CompletionResult result = complete(
			"/Completion/src3/test0244/X.js",
			"package test0244;\n" + 
			"import generics.*;\n" + 
			"public class X extends ZAGenericType {\n" + 
			"	foo\n" +  
			"}",
			"foo");

		assertResults(
			"foo[POTENTIAL_METHOD_DECLARATION]{foo, Ltest0244.X;, ()V, foo, null, " + (R_DEFAULT + R_INTERESTING + R_NON_RESTRICTED) + "}\n" +
			"foo[FUNCTION_DECLARATION]{public Object foo(Object t), Lgenerics.ZAGenericType;, (Ljava.lang.Object;)Ljava.lang.Object;, foo, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_METHOD_OVERIDE + R_NON_RESTRICTED) + "}\n" +
			"foo[FUNCTION_DECLARATION]{public Object foo(ZAGenericType var), Lgenerics.ZAGenericType;, (Lgenerics.ZAGenericType;)Ljava.lang.Object;, foo, (var), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_METHOD_OVERIDE + R_NON_RESTRICTED) + "}",
			result.proposals);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=101456
public void test0245() throws JavaScriptModelException {
    this.wc = getWorkingCopy(
            "/Completion/src/test/SnapshotImpl.js",
            "class SnapshotImpl extends AbstractSnapshot<SnapshotImpl, ProviderImpl> {}");
    getWorkingCopy(
            "/Completion/src/test/Snapshot.js",
            "public interface Snapshot<S extends Snapshot> {}");
    getWorkingCopy(
            "/Completion/src/test/SnapshotProvider.js",
            "interface SnapshotProvider<S extends Snapshot> {}");
    getWorkingCopy(
            "/Completion/src/test/AbstractSnapshot.js",
            "abstract class AbstractSnapshot<S extends Snapshot, P extends SnapshotProvider<S>> implements Snapshot<S> {}");
    getWorkingCopy(
            "/Completion/src/test/ProviderImpl.js",
            "class ProviderImpl implements SnapshotProvider<SnapshotImpl> {}");

    CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
    String str = this.wc.getSource();
    String completeBehind = "ProviderImp";
    int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
    this.wc.codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults("", requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=83005
public void test0246() throws JavaScriptModelException {
		CompletionResult result = complete(
			"/Completion/src3/test0245/X.js",
			"package test0245;\n" + 
			"public @interface X {\n" + 
			"	ann\n" +  
			"}",
			"ann");

		assertResults(
			"Annotation[TYPE_REF]{java.lang.annotation.Annotation, java.lang.annotation, Ljava.lang.annotation.Annotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_NON_RESTRICTED) + "}",
			result.proposals);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=102284
public void test0247() throws JavaScriptModelException {
		CompletionResult result = complete(
			"/Completion/src3/test0245/X.js",
			"package test0245;\n" + 
			"public class X {\n" + 
			"  void test() {\n" + 
			"    class Type<S, T> {\n" + 
			"      Type<String, String> t= new Type<String, String> ()\n" + 
			"    }\n" + 
			"  }\n" +  
			"}",
			"Type<String, String> (");

		assertResults(
			"Type[FUNCTION_REF<CONSTRUCTOR>]{, LType<Ljava.lang.String;Ljava.lang.String;>;, ()V, Type, null, " + (R_DEFAULT + R_INTERESTING + R_NON_RESTRICTED) + "}\n" +
			"Type<java.lang.String,java.lang.String>[ANONYMOUS_CLASS_DECLARATION]{, LType<Ljava.lang.String;Ljava.lang.String;>;, ()V, null, null, " + (R_DEFAULT + R_INTERESTING + R_NON_RESTRICTED) + "}",
			result.proposals);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=102572
public void test0248() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	try {
		Hashtable options = new Hashtable(oldOptions);
		options.put(JavaScriptCore.CODEASSIST_CAMEL_CASE_MATCH, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		this.workingCopies = new IJavaScriptUnit[2];
		this.workingCopies[0] = getWorkingCopy(
			"/Completion/src/camelcase/Test.js",
			"package camelcase;"+
			"import static camelcase.ImportedType.*;"+
			"public class Test {\n"+
			"  void foo() {\n"+
			"    oTT\n"+
			"  }\n"+
			"}");
		
		this.workingCopies[1] = getWorkingCopy(
			"/Completion/src/camelcase/ImportedType.js",
			"package camelcase;"+
			"public class ImportedType {\n"+
			"  public static void oneTwoThree(){}\n"+
			"  public static void oTTMethod(){}\n"+
			"}");
	
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
		String str = this.workingCopies[0].getSource();
		String completeBehind = "oTT";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"oneTwoThree[FUNCTION_REF]{oneTwoThree(), Lcamelcase.ImportedType;, ()V, oneTwoThree, null, " + (R_DEFAULT + R_INTERESTING + R_CAMEL_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"oTTMethod[FUNCTION_REF]{oTTMethod(), Lcamelcase.ImportedType;, ()V, oTTMethod, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		JavaScriptCore.setOptions(oldOptions);
	}
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=102572
public void test0249() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	try {
		Hashtable options = new Hashtable(oldOptions);
		options.put(JavaScriptCore.CODEASSIST_CAMEL_CASE_MATCH, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		this.workingCopies = new IJavaScriptUnit[2];
		this.workingCopies[0] = getWorkingCopy(
			"/Completion/src/camelcase/Test.js",
			"package camelcase;"+
			"import static camelcase.ImportedType.*;"+
			"public class Test {\n"+
			"  void foo() {\n"+
			"    oTT\n"+
			"  }\n"+
			"}");
		
		this.workingCopies[1] = getWorkingCopy(
			"/Completion/src/camelcase/ImportedType.js",
			"package camelcase;"+
			"public class ImportedType {\n"+
			"  public static int oneTwoThree;\n"+
			"  public static int oTTField;\n"+
			"}");
	
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
		String str = this.workingCopies[0].getSource();
		String completeBehind = "oTT";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"oneTwoThree[FIELD_REF]{oneTwoThree, Lcamelcase.ImportedType;, I, oneTwoThree, null, " + (R_DEFAULT + R_INTERESTING + R_CAMEL_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"oTTField[FIELD_REF]{oTTField, Lcamelcase.ImportedType;, I, oTTField, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		JavaScriptCore.setOptions(oldOptions);
	}
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=102572
public void test0250() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	try {
		Hashtable options = new Hashtable(oldOptions);
		options.put(JavaScriptCore.CODEASSIST_CAMEL_CASE_MATCH, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
	
		this.workingCopies = new IJavaScriptUnit[2];
		this.workingCopies[0] = getWorkingCopy(
			"/Completion/src/camelcase/Test.js",
			"package camelcase;"+
			"import static camelcase.ImportedType.oTT;"+
			"public class Test {\n"+
			"}");
		
		this.workingCopies[1] = getWorkingCopy(
			"/Completion/src/camelcase/ImportedType.js",
			"package camelcase;"+
			"public class ImportedType {\n"+
			"  public static void oneTwoThree(){}\n"+
			"  public static void oTTMethod(){}\n"+
			"}");
	
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
		String str = this.workingCopies[0].getSource();
		String completeBehind = "oTT";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"oneTwoThree[METHOD_IMPORT]{camelcase.ImportedType.oneTwoThree;, Lcamelcase.ImportedType;, ()V, oneTwoThree, null, " + (R_DEFAULT + R_INTERESTING + R_CAMEL_CASE + R_NON_RESTRICTED) + "}\n" +
				"oTTMethod[METHOD_IMPORT]{camelcase.ImportedType.oTTMethod;, Lcamelcase.ImportedType;, ()V, oTTMethod, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		JavaScriptCore.setOptions(oldOptions);
	}
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=102572
public void test0260() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	try {
		Hashtable options = new Hashtable(oldOptions);
		options.put(JavaScriptCore.CODEASSIST_CAMEL_CASE_MATCH, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		this.workingCopies = new IJavaScriptUnit[2];
		this.workingCopies[0] = getWorkingCopy(
			"/Completion/src/camelcase/Test.js",
			"package camelcase;"+
			"@Annot(oTT)"+
			"public class Test {\n"+
			"}");
		
		this.workingCopies[1] = getWorkingCopy(
			"/Completion/src/camelcase/Annot.js",
			"package camelcase;"+
			"public @interface Annot {\n"+
			"  String oneTwoThree() default \"\";\n"+
			"  String oTTAttribute() default \"\";\n"+
			"}");
	
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
		String str = this.workingCopies[0].getSource();
		String completeBehind = "oTT";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
	
		assertResults(
				"oneTwoThree[ANNOTATION_ATTRIBUTE_REF]{oneTwoThree, Lcamelcase.Annot;, Ljava.lang.String;, oneTwoThree, null, " + (R_DEFAULT + R_INTERESTING + R_CAMEL_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
				"oTTAttribute[ANNOTATION_ATTRIBUTE_REF]{oTTAttribute, Lcamelcase.Annot;, Ljava.lang.String;, oTTAttribute, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
				requestor.getResults());
	} finally {
		JavaScriptCore.setOptions(oldOptions);
	}
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=113945
public void test0261() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test<T extends SuperClass> {\n"+
		"  T foo() {\n"+
		"    foo().zz\n"+
		"  }\n"+
		"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/SuperClass.js",
		"package test;"+
		"public class SuperClass {\n"+
		"  public int zzfield;\n"+
		"  public void zzmethod(){}\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo().zz";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"zzfield[FIELD_REF]{zzfield, Ltest.SuperClass;, I, zzfield, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_STATIC + R_NON_RESTRICTED) + "}\n" +
			"zzmethod[FUNCTION_REF]{zzmethod(), Ltest.SuperClass;, ()V, zzmethod, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_STATIC + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=113945
public void test0262() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test<T extends SuperInterface> {\n"+
		"  T foo() {\n"+
		"    foo().zz\n"+
		"  }\n"+
		"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/SuperInterface.js",
		"package test;"+
		"public interface SuperInterface {\n"+
		"  public static int zzfield;\n"+
		"  public void zzmethod();\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo().zz";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"zzfield[FIELD_REF]{zzfield, Ltest.SuperInterface;, I, zzfield, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
			"zzmethod[FUNCTION_REF]{zzmethod(), Ltest.SuperInterface;, ()V, zzmethod, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_STATIC + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}

//https://bugs.eclipse.org/bugs/show_bug.cgi?id=113945
public void test0263() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[3];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test<T extends SuperClass & SuperInterface> {\n"+
		"  T foo() {\n"+
		"    foo().zz\n"+
		"  }\n"+
		"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/SuperClass.js",
		"package test;"+
		"public class SuperClass {\n"+
		"  public int zzfield;\n"+
		"  public void zzmethod();\n"+
		"}");
	
	this.workingCopies[2] = getWorkingCopy(
		"/Completion/src/test/SuperInterface.js",
		"package test;"+
		"public interface SuperInterface {\n"+
		"  public static int zzfield2;\n"+
		"  public void zzmethod2();\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo().zz";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"zzfield2[FIELD_REF]{zzfield2, Ltest.SuperInterface;, I, zzfield2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
			"zzfield[FIELD_REF]{zzfield, Ltest.SuperClass;, I, zzfield, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_STATIC + R_NON_RESTRICTED) + "}\n" +
			"zzmethod[FUNCTION_REF]{zzmethod(), Ltest.SuperClass;, ()V, zzmethod, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_STATIC + R_NON_RESTRICTED) + "}\n" +
			"zzmethod2[FUNCTION_REF]{zzmethod2(), Ltest.SuperInterface;, ()V, zzmethod2, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_STATIC + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=120522
public void test0264() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[3];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"@MyAnnot(MyEnum\n"+
		"public class Test {\n"+
		"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/MyEnum.js",
		"package test;"+
		"public enum MyEnum {\n"+
		"  AAA\n"+
		"}");
	
	this.workingCopies[2] = getWorkingCopy(
		"/Completion/src/test/MyAnnot.js",
		"package test;"+
		"public @interface MyAnnot {\n"+
		"  MyEnum[] value();\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	requestor.setIgnored(CompletionProposal.ANNOTATION_ATTRIBUTE_REF, true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "MyEnum";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"MyEnum[TYPE_REF]{MyEnum, test, Ltest.MyEnum;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=127323
public void test0265() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/enumbug/EnumBug.js",
		"package enumbug;\n"+
		"public class EnumBug {\n"+
		"  public static enum Foo {foo, bar, baz}\n"+
		"  public void bar(Foo f) {\n"+
		"    switch(f) {\n"+
		"      case Foo.baz:\n"+
		"      case  // <-- invoke context assist here!\n"+
		"    }\n"+
		"  }\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "case ";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"bar[FIELD_REF]{bar, Lenumbug.EnumBug$Foo;, Lenumbug.EnumBug$Foo;, bar, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_EXPECTED_TYPE + R_NON_RESTRICTED) + "}\n" +
			"baz[FIELD_REF]{baz, Lenumbug.EnumBug$Foo;, Lenumbug.EnumBug$Foo;, baz, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_EXPECTED_TYPE + R_NON_RESTRICTED) + "}\n" +
			"foo[FIELD_REF]{foo, Lenumbug.EnumBug$Foo;, Lenumbug.EnumBug$Foo;, foo, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_UNQUALIFIED + R_EXPECTED_TYPE + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=128169
public void test0266() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test<T, U, TU> extends SuperTest<T> {\n"+
		"  foo\n"+
		"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/SuperTest.js",
		"package test;\n"+
		"public class SuperTest<E> {\n"+
		"  public <T, U, TU> T foo(SuperTest<T> t, SuperTest<U> u, SuperTest<TU> tu, SuperTest<E> e) {return null;}\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"foo[POTENTIAL_METHOD_DECLARATION]{foo, Ltest.Test<TT;TU;TTU;>;, ()V, foo, null, " + (R_DEFAULT + R_INTERESTING + R_NON_RESTRICTED) + "}\n" +
			"foo[FUNCTION_DECLARATION]{public <V, W, TU2> V foo(test.SuperTest<V> t, test.SuperTest<W> u, test.SuperTest<TU2> tu, test.SuperTest<T> e), " +
				"Ltest.SuperTest<TT;>;, <V:Ljava.lang.Object;W:Ljava.lang.Object;TU2:Ljava.lang.Object;>(Ltest.SuperTest<TV;>;Ltest.SuperTest<TW;>;" +
				"Ltest.SuperTest<TTU2;>;Ltest.SuperTest<TT;>;)TV;, foo, (t, u, tu, e), " +
				(R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_METHOD_OVERIDE + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}

//https://bugs.eclipse.org/bugs/show_bug.cgi?id=128169
public void test0267() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test<T, U, TU> extends SuperTest {\n"+
		"  foo\n"+
		"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/SuperTest.js",
		"package test;\n"+
		"public class SuperTest<E> {\n"+
		"  public <T, U, TU> T foo(SuperTest<T> t, SuperTest<U> u, SuperTest<TU> tu, SuperTest<E> e) {return null;}\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"foo[POTENTIAL_METHOD_DECLARATION]{foo, Ltest.Test<TT;TU;TTU;>;, ()V, foo, null, " + (R_DEFAULT + R_INTERESTING + R_NON_RESTRICTED) + "}\n" +
			"foo[FUNCTION_DECLARATION]{public Object foo(SuperTest t, SuperTest u, SuperTest tu, SuperTest e), Ltest.SuperTest;, (Ltest.SuperTest;" +
				"Ltest.SuperTest;Ltest.SuperTest;Ltest.SuperTest;)Ljava.lang.Object;, foo, (t, u, tu, e), " +
				(R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_METHOD_OVERIDE + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=128169
public void test0268() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test<T, U, TU> extends SuperTest {\n"+
		"  foo\n"+
		"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/SuperTest.js",
		"package test;\n"+
		"public class SuperTest {\n"+
		"  public <T, U, TU> T foo(T t, U u, TU tu) {return null;}\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"foo[POTENTIAL_METHOD_DECLARATION]{foo, Ltest.Test<TT;TU;TTU;>;, ()V, foo, null, " + (R_DEFAULT + R_INTERESTING + R_NON_RESTRICTED) + "}\n" +
			"foo[FUNCTION_DECLARATION]{public <V, W, TU2> V foo(V t, W u, TU2 tu), Ltest.SuperTest;, <V:Ljava.lang.Object;W:Ljava.lang.Object;TU2:Ljava.lang.Object;>(TV;TW;TTU2;)TV;, foo, (t, u, tu), " +
				(R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_METHOD_OVERIDE + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=131681
public void test0269() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test extends SuperTest {\n"+
		"  foo\n"+
		"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/SuperTest.js",
		"package test;\n"+
		"public class SuperTest {\n"+
		"  public <T> void foo() {}\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"foo[POTENTIAL_METHOD_DECLARATION]{foo, Ltest.Test;, ()V, foo, null, " + (R_DEFAULT + R_INTERESTING + R_NON_RESTRICTED) + "}\n" +
			"foo[FUNCTION_DECLARATION]{public <T> void foo(), Ltest.SuperTest;, <T:Ljava.lang.Object;>()V, foo, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_METHOD_OVERIDE + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
public void test0270() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[3];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test270_2.js",
		"package test;\n"+
		"public class Test270_2 extends SuperTest<Test270> {\n"+
		"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/SuperTest.js",
		"package test;\n"+
		"public class SuperTest<T> {\n"+
		"}");
	
	this.workingCopies[2] = getWorkingCopy(
		"/Completion/src/test/Test270.js",
		"package test;\n"+
		"public class Test270 {\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "Test270";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"Test270_2[TYPE_REF]{Test270_2, test, Ltest.Test270_2;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
			"Test270[TYPE_REF]{Test270, test, Ltest.Test270;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXPECTED_TYPE + R_UNQUALIFIED + R_EXACT_NAME + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106450
public void test0271() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test {\n"+
		"	void foo() {\n"+
		"	  TestCollections.<Object>zzz\n"+
		"	}\n"+
		"}\n");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/TestCollections.js",
		"package test;\n"+
		"public class TestCollections {\n"+
		"  public <T> void zzz1(T t) {}\n"+
		"  public static <T> void zzz2(T t) {}\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "zzz";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"zzz2[FUNCTION_REF]{zzz2(), Ltest.TestCollections;, (Ljava.lang.Object;)V, zzz2, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_INHERITED + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106450
public void test0272() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test {\n"+
		"	void foo(TestCollections t) {\n"+
		"	  t.<Object>zzz\n"+
		"	}\n"+
		"}\n");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/TestCollections.js",
		"package test;\n"+
		"public class TestCollections {\n"+
		"  public <T> void zzz1(T t) {}\n"+
		"  public static <T> void zzz2(T t) {}\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "zzz";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"zzz2[FUNCTION_REF]{zzz2(), Ltest.TestCollections;, (Ljava.lang.Object;)V, zzz2, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
			"zzz1[FUNCTION_REF]{zzz1(), Ltest.TestCollections;, (Ljava.lang.Object;)V, zzz1, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_STATIC + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106450
public void test0273() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test {\n"+
		"	TestCollections bar() {\n"+
		"	  return null;\n"+
		"	}\n"+
		"	void foo() {\n"+
		"	  bar().<Object>zzz\n"+
		"	}\n"+
		"}\n");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/TestCollections.js",
		"package test;\n"+
		"public class TestCollections {\n"+
		"  public <T> void zzz1(T t) {}\n"+
		"  public static <T> void zzz2(T t) {}\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "zzz";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"zzz2[FUNCTION_REF]{zzz2(), Ltest.TestCollections;, (Ljava.lang.Object;)V, zzz2, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}\n" +
			"zzz1[FUNCTION_REF]{zzz1(), Ltest.TestCollections;, (Ljava.lang.Object;)V, zzz1, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_STATIC + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106450
public void test0274() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test {\n"+
		"	void foo() {\n"+
		"	  int.<Object>zzz\n"+
		"	}\n"+
		"}\n");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/TestCollections.js",
		"package test;\n"+
		"public class TestCollections {\n"+
		"  public <T> void zzz1(T t) {}\n"+
		"  public static <T> void zzz2(T t) {}\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "zzz";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106450
public void test0275() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test {\n"+
		"	void foo(int t) {\n"+
		"	  t.<Object>zzz\n"+
		"	}\n"+
		"}\n");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/TestCollections.js",
		"package test;\n"+
		"public class TestCollections {\n"+
		"  public <T> void zzz1(T t) {}\n"+
		"  public static <T> void zzz2(T t) {}\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "zzz";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106450
public void test0276() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test {\n"+
		"	int bar() {\n"+
		"	  return 0;\n"+
		"	}\n"+
		"	void foo() {\n"+
		"	  bar().<Object>zzz\n"+
		"	}\n"+
		"}\n");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/TestCollections.js",
		"package test;\n"+
		"public class TestCollections {\n"+
		"  public <T> void zzz1(T t) {}\n"+
		"  public static <T> void zzz2(T t) {}\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "zzz";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106450
public void test0277() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test {\n"+
		"	void foo(TestCollections[] o) {\n"+
		"	  o.<Object>zzz\n"+
		"	}\n"+
		"}\n");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/TestCollections.js",
		"package test;\n"+
		"public class TestCollections {\n"+
		"  public <T> void zzz1(T t) {}\n"+
		"  public static <T> void zzz2(T t) {}\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "zzz";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106450
public void test0278() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test {\n"+
		"  public <T> void zzz1(T t) {}\n"+
		"  public static <T> void zzz2(T t) {}\n"+
		"  void foo(TestCollections[] o) {\n"+
		"    this.<Object>zzz\n"+
		"  }\n"+
		"}\n");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "zzz";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"zzz2[FUNCTION_REF]{zzz2(), Ltest.Test;, (Ljava.lang.Object;)V, zzz2, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_INHERITED + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106450
public void test0279() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test extends TestCollections {\n"+
		"	void foo() {\n"+
		"	  super.<Object>zzz\n"+
		"	}\n"+
		"}\n");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/TestCollections.js",
		"package test;\n"+
		"public class TestCollections {\n"+
		"  public <T> void zzz1(T t) {}\n"+
		"  public static <T> void zzz2(T t) {}\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "zzz";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"zzz2[FUNCTION_REF]{zzz2(), Ltest.TestCollections;, (Ljava.lang.Object;)V, zzz2, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_INHERITED + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106450
public void test0280() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test {\n"+
		"	void foo() {\n"+
		"	  TestCollections.<Object, Object>zzz\n"+
		"	}\n"+
		"}\n");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/TestCollections.js",
		"package test;\n"+
		"public class TestCollections {\n"+
		"  public <T> void zzz1(T t) {}\n"+
		"  public static <T> void zzz2(T t) {}\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "zzz";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106450
public void test0281() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test {\n"+
		"	void foo() {\n"+
		"	  TestCollections.zzz\n"+
		"	}\n"+
		"}\n");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/TestCollections.js",
		"package test;\n"+
		"public class TestCollections {\n"+
		"  public <T> void zzz1(T t) {}\n"+
		"  public static <T> void zzz2(T t) {}\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "zzz";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"zzz2[FUNCTION_REF]{zzz2(), Ltest.TestCollections;, <T:Ljava.lang.Object;>(TT;)V, zzz2, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_INHERITED + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106450
public void test0282() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test {\n"+
		"  public <T> void zzz1(T t) {}\n"+
		"  public static <T> void zzz2(T t) {}\n"+
		"  void foo() {\n"+
		"    this.<Unknown>zzz\n"+
		"  }\n"+
		"}\n");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "zzz";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106450
public void test0283() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test {\n"+
		"  public <T, U> void zzz1(T t) {}\n"+
		"  public static <T> void zzz2(T t) {}\n"+
		"  void foo() {\n"+
		"    this.<Unknown, Object>zzz\n"+
		"  }\n"+
		"}\n");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "zzz";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106450
public void test0284() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test {\n"+
		"  public <T extends Test2> void zzz1(T t) {}\n"+
		"  public static <T extends Test2> void zzz2(T t) {}\n"+
		"  void foo() {\n"+
		"    this.<Object>zzz\n"+
		"  }\n"+
		"}\n");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/Test2.js",
		"package test;\n"+
		"public class Test2 {\n"+
		"}\n");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "zzz";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"zzz2[FUNCTION_REF]{zzz2(), Ltest.Test;, (Ljava.lang.Object;)V, zzz2, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_INHERITED + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=133491
public void test0285() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[3];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/other/Test.js",
		"package other;\n"+
		"import pack.*;\n"+
		"public class Test {\n"+
		"  @MyAnnotation(ZZZN\n"+
		"  public void hello() {\n"+
		"  }\n"+
		"}\n");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/pack/ZZZNeedsImportEnum.js",
		"package pack;\n"+
		"public enum ZZZNeedsImportEnum {\n"+
		"  HELLO;\n"+
		"}\n");
	
	this.workingCopies[2] = getWorkingCopy(
		"/Completion/src/pack/MyAnnotation.js",
		"package pack;\n"+
		"public @interface MyAnnotation {\n"+
		"  ZZZNeedsImportEnum value();\n"+
		"  boolean value2() default false;\n"+
		"}\n");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "ZZZN";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"ZZZNeedsImportEnum[TYPE_REF]{ZZZNeedsImportEnum, pack, Lpack.ZZZNeedsImportEnum;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=95829
public void test0286() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[3];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test {\n"+
		"  void bar(Test2<Object> t) {\n"+
		"    t.fo\n"+
		"  }\n"+
		"}\n");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/Test1.js",
		"package test;\n"+
		"public interface Test1<U> {\n"+
		"  <T> T[] foo(T[] t);\n"+
		"}\n");
	
	this.workingCopies[2] = getWorkingCopy(
		"/Completion/src/test/Test2.js",
		"package test;\n"+
		"public interface Test2<U> extends Test1<U> {\n"+
		"  <T> T[] foo(T[] t);\n"+
		"}\n");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "t.fo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.Test2<Ljava.lang.Object;>;, <T:Ljava.lang.Object;>([TT;)[TT;, foo, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_STATIC + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=95829
public void test0287() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[3];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;\n"+
		"public class Test implements Test2<Object>{\n"+
		"  fo\n"+
		"}\n");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test/Test1.js",
		"package test;\n"+
		"public interface Test1<U> {\n"+
		"  <T> T[] foo(T[] t);\n"+
		"}\n");
	
	this.workingCopies[2] = getWorkingCopy(
		"/Completion/src/test/Test2.js",
		"package test;\n"+
		"public interface Test2<U> extends Test1<U> {\n"+
		"  <T> T[] foo(T[] t);\n"+
		"}\n");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "fo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"fo[POTENTIAL_METHOD_DECLARATION]{fo, Ltest.Test;, ()V, fo, null, " + (R_DEFAULT + R_INTERESTING + R_NON_RESTRICTED) + "}\n" +
			"foo[FUNCTION_DECLARATION]{public <T> T[] foo(T[] t), Ltest.Test2<Ljava.lang.Object;>;, <T:Ljava.lang.Object;>([TT;)[TT;, foo, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_METHOD_OVERIDE + R_ABSTRACT_METHOD + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=97085
public void test0288() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;"+
		"import test0.tes"+
		"public class Test {\n"+
		"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test0/test1/X.js",
		"package test0/test1;"+
		"public class X {\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "test0.tes";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"test0.test1[PACKAGE_REF]{test0.test1.*;, test0.test1, null, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=97085
public void test0289() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;"+
		"import static test0.tes"+
		"public class Test {\n"+
		"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/test0/test1/X.js",
		"package test0/test1;"+
		"public class X {\n"+
		"}");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "test0.tes";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"test0.test1[PACKAGE_REF]{test0.test1., test0.test1, null, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=129983
public void test0290() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test0290/Test.js",
			"package test0290;\n" +
			"@\n" +
			"public class Test {\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
		"/Completion/src/pkgannotations/QQAnnotation.js",
		"package pkgannotations;"+
		"public @interface QQAnnotation {\n"+
		"}");
	
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "@";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"QQAnnotation[TYPE_REF]{pkgannotations.QQAnnotation, pkgannotations, Lpkgannotations.QQAnnotation;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_ANNOTATION + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=123225
public void test0291() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[5];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"  public void foo(){\n" +
			"    new Test2<Test4>().foo\n" +
			"  }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/Test1.js",
			"package test;\n" +
			"public class Test1<TTest1> {\n" +
			"  public void foo(TTest1 t){}\n" +
			"}");
	
	this.workingCopies[2] = getWorkingCopy(
			"/Completion/src3/test/Test2.js",
			"package test;\n" +
			"public class Test2<TTest2 extends Test3> extends Test1<TTest2> {\n" +
			"  public void foo(Test3 t){}\n" +
			"}");
	
	this.workingCopies[3] = getWorkingCopy(
			"/Completion/src3/test/Test3.js",
			"package test;\n" +
			"public class Test3 {\n" +
			"}");
	
	this.workingCopies[4] = getWorkingCopy(
			"/Completion/src3/test/Test4.js",
			"package test;\n" +
			"public class Test4 extends Test3 {\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = ".foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.Test2<Ltest.Test4;>;, (Ltest.Test3;)V, foo, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_STATIC + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=123225
public void test0292() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[6];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"  public void foo(){\n" +
			"    new Test5().foo\n" +
			"  }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/Test1.js",
			"package test;\n" +
			"public class Test1<TTest1> {\n" +
			"  public void foo(TTest1 t){}\n" +
			"}");
	
	this.workingCopies[2] = getWorkingCopy(
			"/Completion/src3/test/Test2.js",
			"package test;\n" +
			"public class Test2<TTest2 extends Test3> extends Test1<TTest2> {\n" +
			"  public void foo(Test3 t){}\n" +
			"}");
	
	this.workingCopies[3] = getWorkingCopy(
			"/Completion/src3/test/Test3.js",
			"package test;\n" +
			"public class Test3 {\n" +
			"}");
	
	this.workingCopies[4] = getWorkingCopy(
			"/Completion/src3/test/Test4.js",
			"package test;\n" +
			"public class Test4 extends Test3 {\n" +
			"}");
	
	this.workingCopies[5] = getWorkingCopy(
			"/Completion/src3/test/Test5.js",
			"package test;\n" +
			"public class Test5 extends Test2<Test4> {\n" +
			"  public void foo(Test4 t){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = ".foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.Test2<Ltest.Test4;>;, (Ltest.Test3;)V, foo, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_STATIC + R_NON_RESTRICTED) + "}\n" +
			"foo[FUNCTION_REF]{foo(), Ltest.Test5;, (Ltest.Test4;)V, foo, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_STATIC + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=123225
public void test0293() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[5];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test extends Test2<Test4> {\n" +
			"  public void foo(Test4 t){}\n" +
			"  public void bar(){\n" +
			"    foo\n" +
			"  }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/Test1.js",
			"package test;\n" +
			"public class Test1<TTest1> {\n" +
			"  public void foo(TTest1 t){}\n" +
			"}");
	
	this.workingCopies[2] = getWorkingCopy(
			"/Completion/src3/test/Test2.js",
			"package test;\n" +
			"public class Test2<TTest2 extends Test3> extends Test1<TTest2> {\n" +
			"  public void foo(Test3 t){}\n" +
			"}");
	
	this.workingCopies[3] = getWorkingCopy(
			"/Completion/src3/test/Test3.js",
			"package test;\n" +
			"public class Test3 {\n" +
			"}");
	
	this.workingCopies[4] = getWorkingCopy(
			"/Completion/src3/test/Test4.js",
			"package test;\n" +
			"public class Test4 extends Test3 {\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.Test2<Ltest.Test4;>;, (Ltest.Test3;)V, foo, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED + R_NON_RESTRICTED) + "}\n" +
			"foo[FUNCTION_REF]{foo(), Ltest.Test;, (Ltest.Test4;)V, foo, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=161557
public void test0294() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[3];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"  Test1<Test2> var[];\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/Test1.js",
			"package test;\n" +
			"public class Test1<TTest1> {\n" +
			"}");
	
	this.workingCopies[2] = getWorkingCopy(
			"/Completion/src3/test/Test2.js",
			"package test;\n" +
			"public class Test2 {\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "Test2";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"Test2[TYPE_REF]{Test2, test, Ltest.Test2;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=99928
public void test0295() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[5];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    void test(StringTest s, IntegerTest i) {\n" +
			"        combine(s, i).compareTo(null);\n" +
			"    }\n" +
			"    \n" +
			"    <T> T combine(T t1, T t2) {\n" +
			"        return null;\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/StringTest.js",
			"package test;\n" +
			"public class StringTest implements ComparableTest<StringTest>, SerializableTest {\n" +
			"    public int compareTo(StringTest s) {\n" +
			"        return 0;\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[2] = getWorkingCopy(
			"/Completion/src3/test/IntegerTest.js",
			"package test;\n" +
			"public class IntegerTest implements ComparableTest<IntegerTest>, SerializableTest {\n" +
			"    public int compareTo(IntegerTest i) {\n" +
			"        return 0;\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[3] = getWorkingCopy(
			"/Completion/src3/test/ComparableTest.js",
			"package test;\n" +
			"public interface ComparableTest<T> {\n" +
			"    public int compareTo(T t) ;\n" +
			"}");
	
	this.workingCopies[4] = getWorkingCopy(
			"/Completion/src3/test/SerializableTest.js",
			"package test;\n" +
			"public interface SerializableTest {\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "compare";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"compareTo[FUNCTION_REF]{compareTo, Ltest.ComparableTest<!*>;, (!*)I, compareTo, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_STATIC + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=99928
public void test0296() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[5];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"        public static void main(String[] args) {\n" +
			"                IntegerTest foo = null;\n" +
			"                StringTest bar = null;\n" +
			"                System.out.println((foo != null ? foo : bar).compare\n" +
			"        }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/StringTest.js",
			"package test;\n" +
			"public class StringTest implements ComparableTest<StringTest>, SerializableTest {\n" +
			"    public int compareTo(StringTest s) {\n" +
			"        return 0;\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[2] = getWorkingCopy(
			"/Completion/src3/test/IntegerTest.js",
			"package test;\n" +
			"public class IntegerTest implements ComparableTest<IntegerTest>, SerializableTest {\n" +
			"    public int compareTo(IntegerTest i) {\n" +
			"        return 0;\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[3] = getWorkingCopy(
			"/Completion/src3/test/ComparableTest.js",
			"package test;\n" +
			"public interface ComparableTest<T> {\n" +
			"    public int compareTo(T t) ;\n" +
			"}");
	
	this.workingCopies[4] = getWorkingCopy(
			"/Completion/src3/test/SerializableTest.js",
			"package test;\n" +
			"public interface SerializableTest {\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "compare";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"compareTo[FUNCTION_REF]{compareTo(), Ltest.ComparableTest<!*>;, (!*)I, compareTo, (t), " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_STATIC + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=154993
public void test0297() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    String description = \"Some description\";\n" +
			"    @Description(this.description)\n" +
			"    public void method() {\n" +
			"    }");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/Description.js",
			"package test;\n" +
			"public @interface Description {\n" +
			"    String value();\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "this.";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=164792
public void test0298() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public void method(ZZZ[] z) {\n" +
			"        ZZZ[] z2 = z.clon\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/ZZZ.js",
			"package test;\n" +
			"public class ZZZ {\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "clon";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"clone[FUNCTION_REF]{clone(), [Ltest.ZZZ;, ()[Ltest.ZZZ;, clone, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_STATIC + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=164792
public void test0299() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public void method(ZZZ z) {\n" +
			"        ZZZ z2 = z.clon\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/ZZZ.js",
			"package test;\n" +
			"public class ZZZ {\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "clon";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"clone[FUNCTION_REF]{clone(), Ljava.lang.Object;, ()Ljava.lang.Object;, clone, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_NON_STATIC + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=157584
public void test0300() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[3];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;"+
		"public class Test {\n" + 
		"	public void throwing() throws IZZAException, Top<Object>.IZZException {}\n" +
		"	public void foo() {\n" +
		"      try {\n" +
		"         throwing();\n" +
		"      }\n" +
		"      catch (IZZAException e) {\n" +
		"         bar();\n" +
		"      }\n" +
		"      catch (IZZ) {\n" +
		"      }\n" +
		"   }" +
		"}\n");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src/test/IZZAException.js",
			"package test;"+
			"public class IZZAException extends Exception {\n" + 
			"}\n");
	
	this.workingCopies[2] = getWorkingCopy(
			"/Completion/src/test/IZZException.js",
			"package test;"+
			"public class Top<T> {\n" + 
			"  public class IZZException extends Exception {\n" + 
			"  }\n" + 
			"}\n");

	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true);
	String str = this.workingCopies[0].getSource();
	String completeBehind = "IZZ";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"Top<java.lang.Object>.IZZException[TYPE_REF]{test.Top.IZZException, test, Ltest.Top<Ljava.lang.Object;>.IZZException;, null, null, " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXCEPTION + R_EXACT_EXPECTED_TYPE + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=153130
public void testEC001() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[1];
	this.workingCopies[0] = getWorkingCopy(
		"/Completion/src/test/Test.js",
		"package test;"+
		"public class Test<T> {\n"+
		"}");
	
	String start = "new test.Test<";
	IJavaScriptProject javaProject = getJavaProject("Completion");
	IEvaluationContext context = javaProject.newEvaluationContext();
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false);
	context.codeComplete(start, start.length(), requestor, this.wcOwner);
	
	int startOffset = start.length();
	int endOffset = startOffset;
	
	assertResults(
			"completion offset="+startOffset+"\n"+
			"completion range=["+startOffset+", "+(endOffset-1)+"]\n"+
			"completion token=\"\"\n"+
			"completion token kind=TOKEN_KIND_NAME\n"+
			"expectedTypesSignatures={Ljava.lang.Object;}\n"+
			"expectedTypesKeys={Ljava/lang/Object;}",
            requestor.getContext());
    
	assertResults(
			"Test<T>[TYPE_REF]{, test, Ltest.Test<TT;>;, null, null, ["+startOffset+", "+endOffset+"], "+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_EXPECTED_TYPE + R_UNQUALIFIED + R_NON_RESTRICTED)+"}",
			requestor.getResults());
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports001() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo;\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.*"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	int relevance1 = R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_RESTRICTED;
	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	int start2 = str.lastIndexOf("public class");
	int end2 = start2 + "".length();
	assertResults(
			"foo[FIELD_REF]{foo, Ltest.p.ZZZ;, I, foo, null, ["+start1+", "+end1+"], "+(relevance1)+"}\n" +
			"   foo[FIELD_IMPORT]{import static test.p.ZZZ.foo;\n, Ltest.p.ZZZ;, I, foo, null, ["+start2+", "+end2+"], " + (relevance1) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports002() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.*"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
	
	int relevance1 = R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_RESTRICTED;
	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	int start2 = str.lastIndexOf("public class");
	int end2 = start2 + "".length();
	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, ()I, foo, null, ["+start1+", "+end1+"], "+(relevance1)+"}\n" +
			"   foo[METHOD_IMPORT]{import static test.p.ZZZ.foo;\n, Ltest.p.ZZZ;, ()I, foo, null, ["+start2+", "+end2+"], " + (relevance1) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports003() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo;\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports004() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports005() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo;\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.foo"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
	
	int relevance1 = R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_RESTRICTED;
	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	int start2 = str.lastIndexOf("public class");
	int end2 = start2 + "".length();
	assertResults(
			"foo[FIELD_REF]{foo, Ltest.p.ZZZ;, I, foo, null, ["+start1+", "+end1+"], "+(relevance1)+"}\n" +
			"   foo[FIELD_IMPORT]{import static test.p.ZZZ.foo;\n, Ltest.p.ZZZ;, I, foo, null, ["+start2+", "+end2+"], " + (relevance1) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports006() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.foo"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	int relevance1 = R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_RESTRICTED;
	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	int start2 = str.lastIndexOf("public class");
	int end2 = start2 + "".length();
	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, ()I, foo, null, ["+start1+", "+end1+"], "+(relevance1)+"}\n" +
			"   foo[METHOD_IMPORT]{import static test.p.ZZZ.foo;\n, Ltest.p.ZZZ;, ()I, foo, null, ["+start2+", "+end2+"], " + (relevance1) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports007() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"import test.p.ZZZ.*;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.*"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	int relevance1 = R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_RESTRICTED;
	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	int start2 = str.lastIndexOf("public class");
	int end2 = start2 + "".length();
	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, ()I, foo, null, ["+start1+", "+end1+"], "+(relevance1)+"}\n" +
			"   foo[METHOD_IMPORT]{import static test.p.ZZZ.foo;\n, Ltest.p.ZZZ;, ()I, foo, null, ["+start2+", "+end2+"], " + (relevance1) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports008() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"import static test.p.ZZZ.*;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.*"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
	
	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, ()I, foo, null, ["+start1+", "+end1+"], "+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED +R_NON_RESTRICTED)+"}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports009() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"import test.p.ZZZ.*;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.foo"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	int relevance1 = R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_RESTRICTED;
	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	int start2 = str.lastIndexOf("public class");
	int end2 = start2 + "".length();
	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, ()I, foo, null, ["+start1+", "+end1+"], "+(relevance1)+"}\n" +
			"   foo[METHOD_IMPORT]{import static test.p.ZZZ.foo;\n, Ltest.p.ZZZ;, ()I, foo, null, ["+start2+", "+end2+"], " + (relevance1) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports010() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"import static test.p.ZZZ.*;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.foo"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, ()I, foo, null, ["+start1+", "+end1+"], "+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED +R_NON_RESTRICTED)+"}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports011() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"import test.p.ZZZ.foo;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.*"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	int relevance1 = R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_RESTRICTED;
	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	int start2 = str.lastIndexOf("public class");
	int end2 = start2 + "".length();
	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, ()I, foo, null, ["+start1+", "+end1+"], "+(relevance1)+"}\n" +
			"   foo[METHOD_IMPORT]{import static test.p.ZZZ.foo;\n, Ltest.p.ZZZ;, ()I, foo, null, ["+start2+", "+end2+"], " + (relevance1) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports012() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"import static test.p.ZZZ.foo;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.*"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, ()I, foo, null, ["+start1+", "+end1+"], "+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED +R_NON_RESTRICTED)+"}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports013() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"import test.p.ZZZ.foo;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.foo"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	int relevance1 = R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_RESTRICTED;
	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	int start2 = str.lastIndexOf("public class");
	int end2 = start2 + "".length();
	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, ()I, foo, null, ["+start1+", "+end1+"], "+(relevance1)+"}\n" +
			"   foo[METHOD_IMPORT]{import static test.p.ZZZ.foo;\n, Ltest.p.ZZZ;, ()I, foo, null, ["+start2+", "+end2+"], " + (relevance1) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports014() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"import static test.p.ZZZ.foo;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.foo"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, ()I, foo, null, ["+start1+", "+end1+"], "+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED +R_NON_RESTRICTED)+"}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports015() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[3];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"import static test.p.ZZZ.foo;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){}\n" +
			"}");
	
	this.workingCopies[2] = getWorkingCopy(
			"/Completion/src3/test/q/ZZZ2.js",
			"package test.q;\n" +
			"public class ZZZ2 {\n" +
			"    public static int foo(){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.q.ZZZ2.foo"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, ()I, foo, null, ["+start1+", "+end1+"], " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports016() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public class foo {\n" +
			"        public void method() {\n" +
			"            foo\n" +
			"        }\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.*"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
	
	int relevance1 = R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_RESTRICTED;
	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	int start2 = str.lastIndexOf("public class Test");
	int end2 = start2 + "".length();
	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, ()I, foo, null, ["+start1+", "+end1+"], " + (relevance1) + "}\n" +
			"   foo[METHOD_IMPORT]{import static test.p.ZZZ.foo;\n, Ltest.p.ZZZ;, ()I, foo, null, ["+start2+", "+end2+"], " + (relevance1) + "}\n" +
			"Test.foo[TYPE_REF]{foo, test, Ltest.Test$foo;, null, null, ["+start1+", "+end1+"], "+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED + R_NON_RESTRICTED)+"}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports017() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public void foo() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.*"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.Test;, ()V, foo, null, ["+start1+", "+end1+"], "+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED + R_NON_RESTRICTED)+"}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports018() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public int foo;\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.*"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	int relevance1 = R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_RESTRICTED;
	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	int start2 = str.lastIndexOf("public class");
	int end2 = start2 + "".length();
	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, ()I, foo, null, ["+start1+", "+end1+"], "+(relevance1)+"}\n" +
			"   foo[METHOD_IMPORT]{import static test.p.ZZZ.foo;\n, Ltest.p.ZZZ;, ()I, foo, null, ["+start2+", "+end2+"], " + (relevance1) + "}\n"+
			"foo[FIELD_REF]{foo, Ltest.Test;, I, foo, null, ["+start1+", "+end1+"], " + (R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED + R_NON_RESTRICTED) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports019() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        int foo = 0;\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.*"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	int relevance1 = R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_RESTRICTED;
	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	int start2 = str.lastIndexOf("public class");
	int end2 = start2 + "".length();
	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, ()I, foo, null, ["+start1+", "+end1+"], "+(relevance1)+"}\n" +
			"   foo[METHOD_IMPORT]{import static test.p.ZZZ.foo;\n, Ltest.p.ZZZ;, ()I, foo, null, ["+start2+", "+end2+"], " + (relevance1) + "}\n"+
			"foo[LOCAL_VARIABLE_REF]{foo, null, I, foo, null, ["+start1+", "+end1+"], "+(R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_UNQUALIFIED + R_NON_RESTRICTED)+"}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports020() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){}\n" +
			"    public static int foo(int i){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.foo"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	int relevance1 = R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_RESTRICTED;
	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	int start2 = str.lastIndexOf("public class");
	int end2 = start2 + "".length();
	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, ()I, foo, null, ["+start1+", "+end1+"], "+(relevance1)+"}\n" +
			"   foo[METHOD_IMPORT]{import static test.p.ZZZ.foo;\n, Ltest.p.ZZZ;, ()I, foo, null, ["+start2+", "+end2+"], " + (relevance1) + "}\n"+
			"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, (I)I, foo, (i), ["+start1+", "+end1+"], "+(relevance1)+"}\n" +
			"   foo[METHOD_IMPORT]{import static test.p.ZZZ.foo;\n, Ltest.p.ZZZ;, (I)I, foo, (i), ["+start2+", "+end2+"], " + (relevance1) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports021() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        <Object>foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static <T> int foo(){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.foo"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	int relevance1 = R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_RESTRICTED;
	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	int start2 = str.lastIndexOf("public class");
	int end2 = start2 + "".length();
	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, <T:Ljava.lang.Object;>()I, foo, null, ["+start1+", "+end1+"], "+(relevance1)+"}\n" +
			"   foo[METHOD_IMPORT]{import static test.p.ZZZ.foo;\n, Ltest.p.ZZZ;, <T:Ljava.lang.Object;>()I, foo, null, ["+start2+", "+end2+"], " + (relevance1) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports022() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo();\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){}\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.foo"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo(";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports023() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"/** */\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo;\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.*"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	int relevance1 = R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_RESTRICTED;
	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	int start2 = str.lastIndexOf("/** */");
	int end2 = start2 + "".length();
	assertResults(
			"foo[FIELD_REF]{foo, Ltest.p.ZZZ;, I, foo, null, ["+start1+", "+end1+"], "+(relevance1)+"}\n" +
			"   foo[FIELD_IMPORT]{import static test.p.ZZZ.foo;\n, Ltest.p.ZZZ;, I, foo, null, ["+start2+", "+end2+"], " + (relevance1) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports024() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public int foo;\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.*"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports025() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public int foo;\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.foo"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports026() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public int foo(){return 0;};\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.*"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports027() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public int foo(){return 0;};\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.foo"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	assertResults(
			"",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports029() throws JavaScriptModelException {
	this.workingCopies = new IJavaScriptUnit[2];
	this.workingCopies[0] = getWorkingCopy(
			"/Completion/src3/test/Test.js",
			"package test;\n" +
			"import test.p.ZZZ;\n" +
			"public class Test {\n" +
			"    public void method() {\n" +
			"        foo\n" +
			"    }\n" +
			"}");
	
	this.workingCopies[1] = getWorkingCopy(
			"/Completion/src3/test/p/ZZZ.js",
			"package test.p;\n" +
			"public class ZZZ {\n" +
			"    public static int foo(){return 0;};\n" +
			"}");
	
	CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
	requestor.allowAllRequiredProposals();
	requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.foo"});
	
	String str = this.workingCopies[0].getSource();
	String completeBehind = "foo";
	int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
	this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);

	int relevance1 = R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_RESTRICTED;
	int start1 = str.lastIndexOf("foo") + "".length();
	int end1 = start1 + "foo".length();
	int start2 = str.lastIndexOf("public class");
	int end2 = start2 + "".length();
	assertResults(
			"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, ()I, foo, null, ["+start1+", "+end1+"], "+(relevance1)+"}\n" +
			"   foo[METHOD_IMPORT]{import static test.p.ZZZ.foo;\n, Ltest.p.ZZZ;, ()I, foo, null, ["+start2+", "+end2+"], " + (relevance1) + "}",
			requestor.getResults());
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports030() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_SUGGEST_STATIC_IMPORTS, JavaScriptCore.DISABLED);
		JavaScriptCore.setOptions(options);
		
		this.workingCopies = new IJavaScriptUnit[2];
		this.workingCopies[0] = getWorkingCopy(
				"/Completion/src3/test/Test.js",
				"package test;\n" +
				"public class Test {\n" +
				"    public void method() {\n" +
				"        foo\n" +
				"    }\n" +
				"}");
		
		this.workingCopies[1] = getWorkingCopy(
				"/Completion/src3/test/p/ZZZ.js",
				"package test.p;\n" +
				"public class ZZZ {\n" +
				"    public static int foo(){}\n" +
				"}");
		
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
		requestor.allowAllRequiredProposals();
		requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.*"});
		
		String str = this.workingCopies[0].getSource();
		String completeBehind = "foo";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
		
		int relevance1 = R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_RESTRICTED;
		int start1 = str.lastIndexOf("foo") + "".length();
		int end1 = start1 + "foo".length();
		int start2 = str.lastIndexOf("public class");
		int end2 = start2 + "".length();
		assertResults(
				"foo[FUNCTION_REF]{ZZZ.foo(), Ltest.p.ZZZ;, ()I, foo, null, ["+start1+", "+end1+"], "+(relevance1)+"}\n" +
				"   ZZZ[TYPE_IMPORT]{import test.p.ZZZ;\n, test.p, Ltest.p.ZZZ;, null, null, ["+start2+", "+end2+"], " + (relevance1) + "}",
				requestor.getResults());
	} finally {
		JavaScriptCore.setOptions(oldOptions);
	}
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=152123
public void testFavoriteImports031() throws JavaScriptModelException {
	this.oldOptions = JavaScriptCore.getOptions();
	
	try {
		Hashtable options = new Hashtable(this.oldOptions);
		options.put(JavaScriptCore.CODEASSIST_SUGGEST_STATIC_IMPORTS, JavaScriptCore.ENABLED);
		JavaScriptCore.setOptions(options);
		
		this.workingCopies = new IJavaScriptUnit[2];
		this.workingCopies[0] = getWorkingCopy(
				"/Completion/src3/test/Test.js",
				"package test;\n" +
				"public class Test {\n" +
				"    public void method() {\n" +
				"        foo\n" +
				"    }\n" +
				"}");
		
		this.workingCopies[1] = getWorkingCopy(
				"/Completion/src3/test/p/ZZZ.js",
				"package test.p;\n" +
				"public class ZZZ {\n" +
				"    public static int foo(){}\n" +
				"}");
		
		CompletionTestsRequestor2 requestor = new CompletionTestsRequestor2(true, false, true, false, true);
		requestor.allowAllRequiredProposals();
		requestor.setFavoriteReferences(new String[]{"test.p.ZZZ.*"});
		
		String str = this.workingCopies[0].getSource();
		String completeBehind = "foo";
		int cursorLocation = str.lastIndexOf(completeBehind) + completeBehind.length();
		this.workingCopies[0].codeComplete(cursorLocation, requestor, this.wcOwner);
		
		int relevance1 = R_DEFAULT + R_INTERESTING + R_CASE + R_EXACT_NAME + R_NON_RESTRICTED;
		int start1 = str.lastIndexOf("foo") + "".length();
		int end1 = start1 + "foo".length();
		int start2 = str.lastIndexOf("public class");
		int end2 = start2 + "".length();
		assertResults(
				"foo[FUNCTION_REF]{foo(), Ltest.p.ZZZ;, ()I, foo, null, ["+start1+", "+end1+"], "+(relevance1)+"}\n" +
				"   foo[METHOD_IMPORT]{import static test.p.ZZZ.foo;\n, Ltest.p.ZZZ;, ()I, foo, null, ["+start2+", "+end2+"], " + (relevance1) + "}",
				requestor.getResults());
	} finally {
		JavaScriptCore.setOptions(oldOptions);
	}
}
}
