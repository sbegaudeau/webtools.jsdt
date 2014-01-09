/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.ITypeHierarchy;
import junit.framework.Test;

public class HierarchyOnWorkingCopiesTests extends WorkingCopyTests {

public HierarchyOnWorkingCopiesTests(String name) {
	super(name);
}

public static Test suite() {
	return buildModelTestSuite(HierarchyOnWorkingCopiesTests.class);
	/* NOTE: cannot use 'new Suite(HierarchyOnWorkingCopiesTests.class)' as this would include tests from super class
	TestSuite suite = new Suite(HierarchyOnWorkingCopiesTests.class.getName());

	suite.addTest(new HierarchyOnWorkingCopiesTests("testSimpleSuperTypeHierarchy"));
	suite.addTest(new HierarchyOnWorkingCopiesTests("testSimpleSubTypeHierarchy"));

	return suite;
	*/
}
/**
 */
public void testSimpleSubTypeHierarchy() throws CoreException {
	String newContents =
		"function A(){}\n" +
		"A.prototype = new B()\n";
	this.copy.getBuffer().setContents(newContents);
	this.copy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	
	IFile file = null;
	try {
		file = this.createFile(
			"P/src/x/y/B.js", 
			"function B() {}\n");
	
		IType type = this.getCompilationUnit("P/src/x/y/B.js").getType("B");
		ITypeHierarchy h = type.newTypeHierarchy(new IJavaScriptUnit[] {this.copy}, null);

		assertHierarchyEquals(
			"Focus: B [in B.js [in x.y [in src [in P]]]]\n" + 
			"Super types:\n" + 
			"  Object [in System.js [in java.lang [in "+ getSystemJsPathString() + " [in P]]]]\n" + 
			"Sub types:\n" + 
			"  A [in [Working copy] A.js [in x.y [in src [in P]]]]\n",
			h);
	} finally {
		if (file != null) {
			this.deleteResource(file);
		}
	}
}
/**
 */
public void testSimpleSuperTypeHierarchy() throws CoreException {
	String newContents =
		"function A() {this.a = 1;}\n" +
		"function B(this.b = 2;) {\n" +
		"}";
	this.copy.getBuffer().setContents(newContents);
	this.copy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	
	IFile file = null;
	try {
		file = this.createFile(
			"P/src/x/y/C.js", 
			"function C() {} {\n" +
			"C.prototype = new B();");
	
		IType type = this.getCompilationUnit("P/src/x/y/C.js").getType("C");
		ITypeHierarchy h = type.newSupertypeHierarchy(new IJavaScriptUnit[] {this.copy}, null);

		assertHierarchyEquals(
			"Focus: C [in C.js [in x.y [in src [in P]]]]\n" + 
			"Super types:\n" + 
			"  B [in [Working copy] A.js [in x.y [in src [in P]]]]\n" + 
			"    Object [in Object.class [in java.lang [in "+ getSystemJsPathString() + " [in P]]]]\n" + 
			"Sub types:\n",
			h);
	} finally {
		if (file != null) {
			this.deleteResource(file);
		}
	}
}

}
