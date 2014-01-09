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

import junit.framework.Test;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.wst.jsdt.core.*;
import org.eclipse.wst.jsdt.internal.core.util.Util;

public class CopyMoveResourcesTests extends CopyMoveTests {
/**
 */
public CopyMoveResourcesTests(String name) {
	super(name);
}
/**
 * Copies the element to the container with optional rename
 * and forcing. The operation should succeed, so any exceptions
 * encountered are thrown.
 */
public IJavaScriptElement copyPositive(IJavaScriptElement element, IJavaScriptElement container, IJavaScriptElement sibling, String rename, boolean force) throws JavaScriptModelException {
	try {
		startDeltas();
		
		// if forcing, ensure that a name collision exists
		if (force) {
			IJavaScriptElement collision = generateHandle(element, rename, container);
			assertTrue("Collision does not exist", collision.exists());
		}
	
		// copy
	 	((ISourceManipulation) element).copy(container, sibling, rename, force, null);
	
		// ensure the original element still exists
		assertTrue("The original element must still exist", element.exists());
	
		// generate the new element	handle
		IJavaScriptElement copy = generateHandle(element, rename, container);
		assertTrue("Copy should exist", copy.exists());
		//ensure correct position
		if (element.getElementType() > IJavaScriptElement.JAVASCRIPT_UNIT) {
			ensureCorrectPositioning((IParent) container, sibling, copy);
		}
		IJavaScriptElementDelta destDelta = getDeltaFor(container, true);
		assertTrue("Destination container not changed", destDelta != null && destDelta.getKind() == IJavaScriptElementDelta.CHANGED);
		IJavaScriptElementDelta[] deltas = destDelta.getAddedChildren();
		// FIXME: not strong enough
		boolean found = false;
		for (int i = 0; i < deltas.length; i++) {
			if (deltas[i].getElement().equals(copy))
				found = true;
		}
		assertTrue("Added children not correct for element copy", found);
		return copy;
	} finally {
		stopDeltas();
	}
}

/**
 * Moves the elements to the containers with optional renaming
 * and forcing. The operation should succeed, so any exceptions
 * encountered are thrown.
 */
public void movePositive(IJavaScriptElement[] elements, IJavaScriptElement[] destinations, IJavaScriptElement[] siblings, String[] names, boolean force, IProgressMonitor monitor) throws JavaScriptModelException {
	try {
		startDeltas();
		
		// if forcing, ensure that a name collision exists
		int i;
		if (force) {
			for (i = 0; i < elements.length; i++) {
				IJavaScriptElement e = elements[i];
				IJavaScriptElement collision = null;
				if (names == null) {
					collision = generateHandle(e, null, destinations[i]);
				} else {
					collision = generateHandle(e, names[i], destinations[i]);
				}
				assertTrue("Collision does not exist", collision.exists());
			}
		}
	
		// move
		getJavaModel().move(elements, destinations, siblings, names, force, monitor);
	
		for (i = 0; i < elements.length; i++) {
			IJavaScriptElement element = elements[i];
			IJavaScriptElement moved = null;
			if (names == null) {
				moved = generateHandle(element, null, destinations[i]);
			} else {
				moved = generateHandle(element, names[i], destinations[i]);
			}
			// ensure the original element no longer exists, unless moving within the same container, or moving a primary working copy
			if (!destinations[i].equals(element.getParent())) {
				if (element.getElementType() != IJavaScriptElement.JAVASCRIPT_UNIT || !((IJavaScriptUnit) element).isWorkingCopy())
					assertTrue("The original element must not exist", !element.exists());
			}
			assertTrue("Moved element should exist", moved.exists());

			IJavaScriptElementDelta destDelta = null;
			if (isMainType(element, destinations[i]) && names != null && names[i] != null) { //moved/renamed main type to same cu
				destDelta = getDeltaFor(moved.getParent());
				assertTrue("Renamed compilation unit as result of main type not added", destDelta != null && destDelta.getKind() == IJavaScriptElementDelta.ADDED);
				IJavaScriptElementDelta[] deltas = destDelta.getAddedChildren();
				assertTrue("Added children not correct for element copy", deltas[0].getElement().equals(moved));
				assertTrue("flag should be F_MOVED_FROM", (deltas[0].getFlags() & IJavaScriptElementDelta.F_MOVED_FROM) > 0);
				assertTrue("moved from handle should be original", deltas[0].getMovedFromElement().equals(element));
			} else {
				destDelta = getDeltaFor(destinations[i], true);
				assertTrue("Destination container not changed", destDelta != null && destDelta.getKind() == IJavaScriptElementDelta.CHANGED);
				IJavaScriptElementDelta[] deltas = destDelta.getAddedChildren();
				for (int j = 0; j < deltas.length - 1; j++) {
					// side effect packages added
					IJavaScriptElement pkg = deltas[j].getElement();
					assertTrue("Side effect child should be a package fragment", pkg.getElementType() == IJavaScriptElement.PACKAGE_FRAGMENT);
					assertTrue("Side effect child should be an enclosing package", element.getElementName().startsWith(pkg.getElementName()));
				}
				IJavaScriptElementDelta pkgDelta = deltas[deltas.length - 1];
				assertTrue("Added children not correct for element copy", pkgDelta.getElement().equals(moved));
				assertTrue("flag should be F_MOVED_FROM", (pkgDelta.getFlags() & IJavaScriptElementDelta.F_MOVED_FROM) > 0);
				assertTrue("moved from handle shoud be original", pkgDelta.getMovedFromElement().equals(element));
				IJavaScriptElementDelta sourceDelta = getDeltaFor(element, true);
				assertTrue("moved to handle should be original", sourceDelta.getMovedToElement().equals(moved));
			}
		}
	} finally {
		stopDeltas();
	}
}
/**
 * Setup for the next test.
 */
public void setUp() throws Exception {
	super.setUp();
	
	this.createJavaProject("P", new String[] {"src", "src2"});
}
static {
//	TESTS_NAMES = new String[] { "testCopyWorkingCopyDestination"};
}
public static Test suite() {
	return buildModelTestSuite(CopyMoveResourcesTests.class);
}
/**
 * Cleanup after the previous test.
 */
public void tearDown() throws Exception {
	this.deleteProject("P");
	
	super.tearDown();
}
/**
 * This operation should fail as copying a CU and a CU member at the
 * same time is not supported.
 */
public void testCopyCUAndType() throws CoreException {
	this.createFolder("/P/src/p1");
	this.createFile(
		"/P/src/p1/X.js",
		"package p1;\n" +
		"public class X {\n" +
		"}"
	);
	IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");

	copyNegative(
		new IJavaScriptElement[]{cuSource, cuSource.getType("X")}, 
		new IJavaScriptElement[]{cuSource.getParent(), cuSource}, 
		null, 
		new String[]{"Y.js", "Y"}, 
		false, 
		IJavaScriptModelStatusConstants.INVALID_ELEMENT_TYPES);
}
/**
 * Ensures that a CU can be copied to a different package, replacing an existing CU.
 */
public void testCopyCUForce() throws CoreException {
	this.createFolder("/P/src/p1");
	this.createFile(
		"/P/src/p1/X.js",
		"package p1;\n" +
		"public class X {\n" +
		"}"
	);
	IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");

	this.createFolder("/P/src/p2");
	this.createFile(
		"/P/src/p2/X.js",
		"package p2;\n" +
		"public class X {\n" +
		"}"
	);
	IPackageFragment pkgDest = getPackage("/P/src/p2");

	copyPositive(cuSource, pkgDest, null, null, true);
}
/**
 * Ensures that a CU can be copied to a different package,
 * and be renamed.
 */
public void testCopyCURename() throws CoreException {
	this.createFolder("/P/src/p1");
	this.createFile(
		"/P/src/p1/X.js",
		"package p1;\n" +
		"public class X {\n" +
		"}"
	);
	IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");

	this.createFolder("/P/src/p2");
	IPackageFragment pkgDest = getPackage("/P/src/p2");

	copyPositive(cuSource, pkgDest, null, "Y.js", false);
}
/**
 * Ensures that a read-only CU can be copied to a different package.
 */
public void testCopyCUReadOnly() throws CoreException {
	if (!Util.isReadOnlySupported()) {
		// Do not test if file system does not support read-only attribute
		return;
	}
	IFile file = null;
	IFile file2 = null;
	try {
		this.createFolder("/P/src/p1");
		file = this.createFile(
			"/P/src/p1/X.js",
			"package p1;\n" +
			"public class X {\n" +
			"}"
		);
		Util.setReadOnly(file, true);
		IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");
	
		this.createFolder("/P/src/p2");
		IPackageFragment pkgDest = getPackage("/P/src/p2");
	
		copyPositive(cuSource, pkgDest, null, null, false);
		
		file2 = getFile("/P/src/p2/X.js");
		assertTrue("Destination cu should be read-only", file2.isReadOnly());
	} finally {
		if (file != null) {
			Util.setReadOnly(file, false);
		}
		if (file2 != null) {
			Util.setReadOnly(file2, false);
		}
		deleteFolder("/P/src/p1");
		deleteFolder("/P/src/p2");
	}
}
/**
 * Ensures that a CU can be copied to a different package,
 * and be renamed, overwriting an existing CU
 */
public void testCopyCURenameForce() throws CoreException {
	this.createFolder("/P/src/p1");
	this.createFile(
		"/P/src/p1/X.js",
		"package p1;\n" +
		"public class X {\n" +
		"}"
	);
	IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");

	this.createFolder("/P/src/p2");
	this.createFile(
		"/P/src/p2/Y.js",
		"package p2;\n" +
		"public class Y {\n" +
		"}"
	);
	IPackageFragment pkgDest = getPackage("/P/src/p2");

	copyPositive(cuSource, pkgDest, null, "Y.js", true);
}
/**
 * Ensures that a CU cannot be copied to a different package,over an existing CU when no force.
 */
public void testCopyCUWithCollision() throws CoreException {
	this.createFolder("/P/src/p1");
	this.createFile(
		"/P/src/p1/X.js",
		"package p1;\n" +
		"public class X {\n" +
		"}"
	);
	IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");

	this.createFolder("/P/src/p2");
	this.createFile(
		"/P/src/p2/X.js",
		"package p2;\n" +
		"public class X {\n" +
		"}"
	);
	IPackageFragment pkgDest = getPackage("/P/src/p2");

	copyNegative(cuSource, pkgDest, null, null, false, IJavaScriptModelStatusConstants.NAME_COLLISION);
}
/**
 * Ensures that a CU cannot be copied to an invalid destination
 */
public void testCopyCUWithInvalidDestination() throws CoreException {
	this.createFolder("/P/src/p1");
	this.createFile(
		"/P/src/p1/X.js",
		"package p1;\n" +
		"public class X {\n" +
		"}"
	);
	IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");

	copyNegative(cuSource, cuSource, null, null, false, IJavaScriptModelStatusConstants.INVALID_DESTINATION);
}
/**
 * Ensures that a CU can be copied to a null container
 */
public void testCopyCUWithNullContainer() throws CoreException {
	this.createFolder("/P/src/p1");
	this.createFile(
		"/P/src/p1/X.js",
		"package p1;\n" +
		"public class X {\n" +
		"}"
	);
	IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");

	try {
		cuSource.copy(null, null, null, false, null);
	} catch (IllegalArgumentException iae) {
		return;
	}
	assertTrue("Should not be able to move a cu to a null container", false);
}
/**
 * Ensures that a CU can be copied to along with its server properties.
 * (Regression test for PR #1G56QT9)
 */
public void testCopyCUWithServerProperties() throws CoreException {
	this.createFolder("/P/src/p1");
	this.createFile(
		"/P/src/p1/X.js",
		"package p1;\n" +
		"public class X {\n" +
		"}"
	);
	IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");

	QualifiedName qualifiedName = new QualifiedName("x.y.z", "a property");
	cuSource.getUnderlyingResource().setPersistentProperty(
		qualifiedName,
		"some value");

	this.createFolder("/P/src/p2");
	IPackageFragment pkgDest = getPackage("/P/src/p2");

	copyPositive(cuSource, pkgDest, null, null, false);
	IJavaScriptUnit cu= pkgDest.getJavaScriptUnit("X.js");
	String propertyValue = cu.getUnderlyingResource().getPersistentProperty(qualifiedName);
	assertEquals(
		"Server property should be copied with cu",
		"some value",
		propertyValue
	);
}
/**
 * Ensures that a package fragment can be copied to a different package fragment root.
 */
public void testCopyPackageFragment() throws CoreException {
	this.createFolder("/P/src/p1");
	this.createFile(
		"/P/src/p1/X.js",
		"package p1;\n" +
		"public class X {\n" +
		"}"
	);
	IPackageFragment pkgSource = getPackage("/P/src/p1");

	IPackageFragmentRoot rootDest= getPackageFragmentRoot("P", "src2");

	copyPositive(pkgSource, rootDest, null, null, false);
}
/**
 * Ensures that a package fragment can be copied to a different package fragment root.
 */
public void testCopyReadOnlyPackageFragment() throws CoreException {
	if (!Util.isReadOnlySupported()) {
		// Do not test if file system does not support read-only attribute
		return;
	}
	IPackageFragment pkgSource = null;
	IPackageFragment pkg2 = null;
	try {
		this.createFolder("/P/src/p1/p2/p3");
		this.createFile(
			"/P/src/p1/p2/p3/X.js",
			"package p1.p2.p3;\n" +
			"public class X {\n" +
			"}"
		);
		Util.setReadOnly(getFile("/P/src/p1/p2/p3/X.js"), true);
		pkgSource = getPackage("/P/src/p1");
		Util.setReadOnly(pkgSource.getResource(), true);
		pkg2 = getPackage("/P/src/p1/p2/p3");
		Util.setReadOnly(pkg2.getResource(), true);
	
		IPackageFragmentRoot rootDest= getPackageFragmentRoot("P", "src2");
	
		copyPositive(pkg2, rootDest, null, null, false);
		
		assertTrue("Not readOnly", Util.isReadOnly(getPackage("/P/src2/p1").getResource()));
		assertTrue("Is readOnly", !Util.isReadOnly(getPackage("/P/src2/p1/p2").getResource()));
		assertTrue("Not readOnly", Util.isReadOnly(getPackage("/P/src2/p1/p2/p3").getResource()));
		assertTrue("Is readOnly", Util.isReadOnly(getFile("/P/src2/p1/p2/p3/X.js")));
	} finally {
		IFile xSrcFile = getFile("/P/src/p1/p2/p3/X.js");
		if (xSrcFile != null) {
			Util.setReadOnly(xSrcFile, false);
		}
		if (pkg2 != null) {
			Util.setReadOnly(pkg2.getResource(), false);
		}
		if (pkgSource != null) {
			Util.setReadOnly(pkgSource.getResource(), false);
		}
		IPackageFragment p1Fragment = getPackage("/P/src2/p1");
		if (p1Fragment != null) {
			Util.setReadOnly(p1Fragment.getResource(), false);
		}
		IPackageFragment p3Fragment = getPackage("/P/src2/p1/p2/p3");
		if (p3Fragment != null) {
			Util.setReadOnly(p3Fragment.getResource(), false);
		}
		IFile xFile = getFile("/P/src2/p1/p2/p3/X.js");
		if (xFile != null) {
			Util.setReadOnly(xFile, false);
		}
		deleteFolder("/P/src/p1");
	}
}
/**
 * Ensures that a WorkingCopy can be copied to a different package.
 */
public void testCopyWorkingCopy() throws CoreException {
	IJavaScriptUnit copy = null;
	try {
		this.createFolder("/P/src/p1");
		this.createFile(
			"/P/src/p1/X.js",
			"package p1;\n" +
			"public class X {\n" +
			"}"
		);
		IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");
		copy = cuSource.getWorkingCopy(null);
	
		this.createFolder("/P/src/p2");
		IPackageFragment pkgDest = getPackage("/P/src/p2");
	
		copyPositive(copy, pkgDest, null, null, false);
	} finally {
		if (copy != null) copy.discardWorkingCopy();
	}
}
/*
 * Ensures that a CU can be copied over an existing primary working copy in a different package.
 * (regression test for bug 117282 Package declaration inserted on wrong CU while copying class if names collide and editor opened)
 */
public void testCopyWorkingCopyDestination() throws CoreException {
	IJavaScriptUnit copy = null;
	try {
		createFolder("/P/src/p1");
		createFile(
			"/P/src/p1/X.js",
			"package p1;\n" +
			"public class X {\n" +
			"  void foo() {}\n" +
			"}"
		);
		IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");
	
		createFolder("/P/src/p2");
		IPackageFragment pkgDest = getPackage("/P/src/p2");
		createFile(
			"/P/src/p2/X.js",
			"\n" +
			"package p1;\n" +
			"public class X {\n" +
			"}"
		);
		copy = getCompilationUnit("/P/src/p2/X.js");
		copy.becomeWorkingCopy(null);
	
		copyPositive(cuSource, pkgDest, null, null, true/*force*/);
	} finally {
		if (copy != null) copy.discardWorkingCopy();
	}
}
/**
 * Ensures that a WorkingCopy can be copied to a different package, replacing an existing WorkingCopy.
 */
public void testCopyWorkingCopyForce() throws CoreException {
	IJavaScriptUnit copy = null;
	try {
		this.createFolder("/P/src/p1");
		this.createFile(
			"/P/src/p1/X.js",
			"package p1;\n" +
			"public class X {\n" +
			"}"
		);
		IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");
		copy = cuSource.getWorkingCopy(null);
	
		this.createFolder("/P/src/p2");
		this.createFile(
			"/P/src/p2/X.js",
			"package p2;\n" +
			"public class X {\n" +
			"}"
		);
		IPackageFragment pkgDest = getPackage("/P/src/p2");
	
		copyPositive(copy, pkgDest, null, null, true);
	} finally {
		if (copy != null) copy.discardWorkingCopy();
	}
}
/**
 * Ensures that a WorkingCopy can be copied to a different package,
 * and be renamed.
 */
public void testCopyWorkingCopyRename() throws CoreException {
	IJavaScriptUnit copy = null;
	try {
		this.createFolder("/P/src/p1");
		this.createFile(
			"/P/src/p1/X.js",
			"package p1;\n" +
			"public class X {\n" +
			"}"
		);
		IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");
		copy = cuSource.getWorkingCopy(null);
	
		this.createFolder("/P/src/p2");
		IPackageFragment pkgDest = getPackage("/P/src/p2");
	
		copyPositive(copy, pkgDest, null, "Y.js", false);
	} finally {
		if (copy != null) copy.discardWorkingCopy();
	}
}
/**
 * Ensures that a WorkingCopy can be copied to a different package,
 * and be renamed, overwriting an existing WorkingCopy
 */
public void testCopyWorkingCopyRenameForce() throws CoreException {
	IJavaScriptUnit copy = null;
	try {
		this.createFolder("/P/src/p1");
		this.createFile(
			"/P/src/p1/X.js",
			"package p1;\n" +
			"public class X {\n" +
			"}"
		);
		IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");
		copy = cuSource.getWorkingCopy(null);
	
		this.createFolder("/P/src/p2");
		this.createFile(
			"/P/src/p2/Y.js",
			"package p2;\n" +
			"public class Y {\n" +
			"}"
		);
		IPackageFragment pkgDest = getPackage("/P/src/p2");
	
		copyPositive(copy, pkgDest, null, "Y.js", true);
	} finally {
		if (copy != null) copy.discardWorkingCopy();
	}
}
/**
 * Ensures that a WorkingCopy cannot be copied to a different package,over an existing WorkingCopy when no force.
 */
public void testCopyWorkingCopyWithCollision() throws CoreException {
	IJavaScriptUnit copy = null;
	try {
		this.createFolder("/P/src/p1");
		this.createFile(
			"/P/src/p1/X.js",
			"package p1;\n" +
			"public class X {\n" +
			"}"
		);
		IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");
		copy = cuSource.getWorkingCopy(null);
	
		this.createFolder("/P/src/p2");
		this.createFile(
			"/P/src/p2/X.js",
			"package p2;\n" +
			"public class X {\n" +
			"}"
		);
		IPackageFragment pkgDest = getPackage("/P/src/p2");
	
		copyNegative(copy, pkgDest, null, null, false, IJavaScriptModelStatusConstants.NAME_COLLISION);
	} finally {
		if (copy != null) copy.discardWorkingCopy();
	}
}
/**
 * Ensures that a WorkingCopy cannot be copied to an invalid destination
 */
public void testCopyWorkingCopyWithInvalidDestination() throws CoreException {
	IJavaScriptUnit copy = null;
	try {
		this.createFolder("/P/src/p1");
		this.createFile(
			"/P/src/p1/X.js",
			"package p1;\n" +
			"public class X {\n" +
			"}"
		);
		IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");
		copy = cuSource.getWorkingCopy(null);
	
		copyNegative(copy, cuSource, null, null, false, IJavaScriptModelStatusConstants.INVALID_DESTINATION);
	} finally {
		if (copy != null) copy.discardWorkingCopy();
	}
}
/**
 * This operation should fail as moving a CU and a CU member at the
 * same time is not supported.
 */
public void testMoveCUAndType() throws CoreException {
	this.createFolder("/P/src/p1");
	this.createFile(
		"/P/src/p1/X.js",
		"package p1;\n" +
		"public class X {\n" +
		"}"
	);
	IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");

	moveNegative(
		new IJavaScriptElement[]{cuSource, cuSource.getType("X")}, 
		new IJavaScriptElement[]{cuSource.getParent(), cuSource}, 
		null, 
		new String[]{"Y.js", "Y"}, 
		false, 
		IJavaScriptModelStatusConstants.INVALID_ELEMENT_TYPES);
}
/**
 * Ensures that a CU can be moved to a different package, replacing an
 * existing CU.
 */
public void testMoveCUForce() throws CoreException {
	this.createFolder("/P/src/p1");
	this.createFile(
		"/P/src/p1/X.js",
		"package p1;\n" +
		"public class X {\n" +
		"}"
	);
	IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");

	this.createFolder("/P/src/p2");
	this.createFile(
		"/P/src/p2/X.js",
		"package p2;\n" +
		"public class X {\n" +
		"}"
	);
	IPackageFragment pkgDest = getPackage("/P/src/p2");

	movePositive(cuSource, pkgDest, null, null, true);
}
/**
 * Ensures that a CU can be moved to a different package,
 * be renamed
 */
public void testMoveCURename() throws CoreException {
	this.createFolder("/P/src/p1");
	this.createFile(
		"/P/src/p1/X.js",
		"package p1;\n" +
		"public class X {\n" +
		"}"
	);
	IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");

	this.createFolder("/P/src/p2");
	IPackageFragment pkgDest = getPackage("/P/src/p2");

	movePositive(cuSource, pkgDest, null, "Y.js", false);
}
/**
 * Ensures that a CU can be moved to a different package,
 * be renamed, overwriting an existing resource.
 */
public void testMoveCURenameForce() throws CoreException {
	this.createFolder("/P/src/p1");
	this.createFile(
		"/P/src/p1/X.js",
		"package p1;\n" +
		"public class X {\n" +
		"}"
	);
	IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");

	this.createFolder("/P/src/p2");
	this.createFile(
		"/P/src/p2/Y.js",
		"package p2;\n" +
		"public class Y {\n" +
		"}"
	);
	IPackageFragment pkgDest = getPackage("/P/src/p2");

	movePositive(cuSource, pkgDest, null, "Y.js", true);
}
/**
 * Ensures that a CU cannot be moved to a different package, replacing an
 * existing CU when not forced.
 */
public void testMoveCUWithCollision() throws CoreException {
	this.createFolder("/P/src/p1");
	this.createFile(
		"/P/src/p1/X.js",
		"package p1;\n" +
		"public class X {\n" +
		"}"
	);
	IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");

	this.createFolder("/P/src/p2");
	this.createFile(
		"/P/src/p2/X.js",
		"package p2;\n" +
		"public class X {\n" +
		"}"
	);
	IPackageFragment pkgDest = getPackage("/P/src/p2");

	moveNegative(cuSource, pkgDest, null, null, false, IJavaScriptModelStatusConstants.NAME_COLLISION);
}
/**
 * Ensures that a CU cannot be moved to an invalid destination.
 */
public void testMoveCUWithInvalidDestination() throws CoreException {
	this.createFolder("/P/src/p1");
	this.createFile(
		"/P/src/p1/X.js",
		"package p1;\n" +
		"public class X {\n" +
		"}"
	);
	IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");

	moveNegative(cuSource, cuSource, null, null, false, IJavaScriptModelStatusConstants.INVALID_DESTINATION);
}
/**
 * Ensures that a CU cannot be moved to a null container
 */
public void testMoveCUWithNullContainer() throws CoreException {
	this.createFolder("/P/src/p1");
	this.createFile(
		"/P/src/p1/X.js",
		"package p1;\n" +
		"public class X {\n" +
		"}"
	);
	IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");

	try {
		cuSource.move(null, null, null, false, null);
	} catch (IllegalArgumentException iae) {
		return;
	}
	assertTrue("Should not be able to move a cu to a null container", false);
}
/**
 * Ensures that a package fragment can be moved to a different package fragment root.
 */
public void testMovePackageFragment() throws CoreException {
	this.createFolder("/P/src/p1");
	this.createFile(
		"/P/src/p1/X.js",
		"package p1;\n" +
		"public class X {\n" +
		"}"
	);
	IPackageFragment pkgSource = getPackage("/P/src/p1");

	IPackageFragmentRoot rootDest= getPackageFragmentRoot("P", "src2");

	movePositive(pkgSource, rootDest, null, null, false);
}
/**
 * Ensures that a package fragment can be copied to a different package fragment root.
 */
public void testMoveReadOnlyPackageFragment() throws CoreException {
	if (!Util.isReadOnlySupported()) {
		// Do not test if file system does not support read-only attribute
		return;
	}
	IPackageFragment pkgSource = null;
	IPackageFragment pkg2 = null;
	try {
		this.createFolder("/P/src/p1/p2/p3");
		this.createFile(
			"/P/src/p1/p2/p3/X.js",
			"package p1.p2.p3;\n" +
			"public class X {\n" +
			"}"
		);
		Util.setReadOnly(getFile("/P/src/p1/p2/p3/X.js"), true);
		pkgSource = getPackage("/P/src/p1");
		Util.setReadOnly(pkgSource.getResource(), true);
		pkg2 = getPackage("/P/src/p1/p2/p3");
		Util.setReadOnly(pkg2.getResource(), true);
	
		IPackageFragmentRoot rootDest= getPackageFragmentRoot("P", "src2");
	
		movePositive(pkg2, rootDest, null, null, false);
		
		assertTrue("Not readOnly", Util.isReadOnly(getPackage("/P/src2/p1").getResource()));
		assertTrue("Is readOnly", !Util.isReadOnly(getPackage("/P/src2/p1/p2").getResource()));
		assertTrue("Not readOnly", Util.isReadOnly(getPackage("/P/src2/p1/p2/p3").getResource()));
		assertTrue("Is readOnly", Util.isReadOnly(getFile("/P/src2/p1/p2/p3/X.js")));
	} finally {
		IFile xSrcFile = getFile("/P/src/p1/p2/p3/X.js");
		if (xSrcFile != null) {
			Util.setReadOnly(xSrcFile, false);
		}
		if (pkg2 != null) {
			Util.setReadOnly(pkg2.getResource(), false);
		}
		if (pkgSource != null) {
			Util.setReadOnly(pkgSource.getResource(), false);
		}
		IPackageFragment p1Fragment = getPackage("/P/src2/p1");
		if (p1Fragment != null) {
			Util.setReadOnly(p1Fragment.getResource(), false);
		}
		IPackageFragment p3Fragment = getPackage("/P/src2/p1/p2/p3");
		if (p3Fragment != null) {
			Util.setReadOnly(p3Fragment.getResource(), false);
		}
		IFile xFile = getFile("/P/src2/p1/p2/p3/X.js");
		if (xFile != null) {
			Util.setReadOnly(xFile, false);
		}
		deleteFolder("/P/src/p1");
	}
}
/**
 * Ensures that a WorkingCopy cannot be moved to a different package.
 */
public void testMoveWorkingCopy() throws CoreException {
	IJavaScriptUnit copy = null;
	try {
		this.createFolder("/P/src/p1");
		this.createFile(
			"/P/src/p1/X.js",
			"package p1;\n" +
			"public class X {\n" +
			"}"
		);
		IJavaScriptUnit cuSource = getCompilationUnit("/P/src/p1/X.js");
		copy = cuSource.getWorkingCopy(null);
	
		this.createFolder("/P/src/p2");
		IPackageFragment pkgDest = getPackage("/P/src/p2");
	
		moveNegative(copy, pkgDest, null, null, false, IJavaScriptModelStatusConstants.INVALID_ELEMENT_TYPES);
	} finally {
		if (copy != null) copy.discardWorkingCopy();
	}
}

/*
 * Ensures that a primary working copy can be moved to a different package
 * and that its buffer doesn't contain unsaved changed after the move.
 * (regression test for bug 83599 CU dirty after move refactoring)
 */
public void testMoveWorkingCopy2() throws CoreException {
	IJavaScriptUnit copy = null;
	try {
		this.createFolder("/P/src/p1");
		this.createFile(
			"/P/src/p1/X.js",
			"package p1;\n" +
			"public class X {\n" +
			"}"
		);
		copy = getCompilationUnit("/P/src/p1/X.js");
		copy.becomeWorkingCopy(null);
	
		this.createFolder("/P/src/p2");
		IPackageFragment pkgDest = getPackage("/P/src/p2");
	
		movePositive(copy, pkgDest, null, null, false);
		assertTrue("Should not have unsaved changes", !copy.getBuffer().hasUnsavedChanges());
	} finally {
		if (copy != null) copy.discardWorkingCopy();
	}
}
}
