/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.interpret;

public class BasicInterpretTest extends InterpretTest {

	public BasicInterpretTest(String name) {
		super(name);
	}

	
	public void test0001() {
		interpetTest("1+2","3");
	}

	public void test0002() {
		interpetTest("a=10","a","10");
	}

	public void test0003() {
		interpetTest("a=10;b=a+11;","b","21");
	}
	
	public void test0004() {
		interpetTest("a={p:21}; b=a.p;","b","21");
	}

	public void test0005() {
		interpetTest("a = new Object(); a.hasProperty(\"c\");","false");
	}
	
	public void test0005a() {
		interpetTest("a = new Object(); a.c=1; a.hasProperty('c');","true");
	}
	
	public void test0007() {
		interpetTest("function box(w,h){this.width=w;this.height=h;}"+
				"box.prototype.area=function (){return this.width*this.height;}"+
				"b=new box(3,4); b.area();"
				,"12");
	}

	public void test0008() {
		interpetTest("var i=1;"+
				"if (i>0) i=3;"
				,"i","3");
	}

	public void test0008b() {
		interpetTest("var i=1;"+
				"if (i>1) i=3;"
				,"i","1");
	}


	public void test0009() {
		interpetTest("var i=1;"+
				"while (i<3) i++;"
				,"i","3");
	}

	public void test0009a() {
		interpetTest("var i=1;"+
				"while (i<3) {i++; if (i==2) break;}"
				,"i","2");
	}

	
	public void test0010() {
		interpetTest("a = \"abc\"; a.indexOf(\"b\");","1");
	}
	
}
