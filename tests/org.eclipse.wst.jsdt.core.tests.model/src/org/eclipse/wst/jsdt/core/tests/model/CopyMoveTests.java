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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.jsdt.core.*;

abstract public class CopyMoveTests extends ModifyingResourceTests {
public CopyMoveTests(String name) {
	super(name);
}
/**
 * Attempts to copy the element with optional 
 * forcing. The operation should fail with the failure code.
 */
public void copyNegative(IJavaScriptElement element, IJavaScriptElement destination, IJavaScriptElement sibling, String rename, boolean force, int failureCode) {
	try {
		((ISourceManipulation)element).copy(destination, sibling, rename, force, null);
	} catch (JavaScriptModelException jme) {
		assertTrue("Code not correct for JavaScriptModelException: " + jme, jme.getStatus().getCode() == failureCode);
		return;
	}
	assertTrue("The copy should have failed for: " + element, false);
	return;
}
/**
 * Attempts to copy the elements with optional 
 * forcing. The operation should fail with the failure code.
 */
public void copyNegative(IJavaScriptElement[] elements, IJavaScriptElement[] destinations, IJavaScriptElement[] siblings, String[] renames, boolean force, int failureCode) {
	try {
		getJavaModel().copy(elements, destinations, siblings, renames, force, null);
	} catch (JavaScriptModelException jme) {
		assertTrue("Code not correct for JavaScriptModelException: " + jme, jme.getStatus().getCode() == failureCode);
		return;
	}
	assertTrue("The move should have failed for for multiple elements: ", false);
	return;
}
/**
 * Copies the element to the container with optional rename
 * and forcing. The operation should succeed, so any exceptions
 * encountered are thrown.
 */
public IJavaScriptElement copyPositive(IJavaScriptElement element, IJavaScriptElement container, IJavaScriptElement sibling, String rename, boolean force) throws JavaScriptModelException {
	// if forcing, ensure that a name collision exists
	if (force) {
		IJavaScriptElement collision = generateHandle(element, rename, container);
		assertTrue("Collision does not exist", collision.exists());
	}

	IJavaScriptElement copy;
	try {
		startDeltas();
		
		// copy
	 	((ISourceManipulation) element).copy(container, sibling, rename, force, null);
	
		// ensure the original element still exists
		assertTrue("The original element must still exist", element.exists());
	
		// generate the new element	handle
		copy = generateHandle(element, rename, container);
		assertTrue("Copy should exist", copy.exists());
	
		//ensure correct position
		if (element.getElementType() > IJavaScriptElement.JAVASCRIPT_UNIT) {
			ensureCorrectPositioning((IParent) container, sibling, copy);
		}
		if (copy.getElementType() == IJavaScriptElement.IMPORT_DECLARATION)
			container = ((IJavaScriptUnit) container).getImportContainer();
		IJavaScriptElementDelta destDelta = getDeltaFor(container, true);
		assertTrue("Destination container not changed", destDelta != null && destDelta.getKind() == IJavaScriptElementDelta.CHANGED);
		IJavaScriptElementDelta[] deltas = destDelta.getAddedChildren();
		assertTrue("Added children not correct for element copy", deltas[0].getElement().equals(copy));
	} finally {
		stopDeltas();
	}
	return copy;
}
/**
 * Generates a new handle to the original element in
 * its new container.
 */
public IJavaScriptElement generateHandle(IJavaScriptElement original, String rename, IJavaScriptElement container) {
	String name = original.getElementName();
	if (rename != null) {
		name = rename;
	}
	switch (container.getElementType()) {
		case IJavaScriptElement.PACKAGE_FRAGMENT_ROOT :
			switch (original.getElementType()) {
				case IJavaScriptElement.PACKAGE_FRAGMENT :
					return ((IPackageFragmentRoot) container).getPackageFragment(name);
				default :
					assertTrue("illegal child type", false);
					break;
			}
			break;
		case IJavaScriptElement.PACKAGE_FRAGMENT :
			switch (original.getElementType()) {
				case IJavaScriptElement.JAVASCRIPT_UNIT :
					return ((IPackageFragment) container).getJavaScriptUnit(name);
				default :
					assertTrue("illegal child type", false);
					break;
			}
			break;
		case IJavaScriptElement.JAVASCRIPT_UNIT :
			switch (original.getElementType()) {
				case IJavaScriptElement.IMPORT_DECLARATION :
					return ((IJavaScriptUnit) container).getImport(name);
				case IJavaScriptElement.TYPE :
					if (isMainType(original, container)) {
						//the cu has been renamed as well
						container = ((IPackageFragment) container.getParent()).getJavaScriptUnit(name + ".js");
					}
					return ((IJavaScriptUnit) container).getType(name);
				default :
					assertTrue("illegal child type", false);
					break;
			}
			break;
		case IJavaScriptElement.TYPE :
			switch (original.getElementType()) {
				case IJavaScriptElement.METHOD :
					if (name.equals(original.getParent().getElementName())) {
						//method is a constructor
						return ((IType) container).getFunction(container.getElementName(), ((IFunction) original).getParameterTypes());
					}
					return ((IType) container).getFunction(name, ((IFunction) original).getParameterTypes());
				case IJavaScriptElement.FIELD :
					return ((IType) container).getField(name);
				case IJavaScriptElement.TYPE :
					return ((IType) container).getType(name);
				case IJavaScriptElement.INITIALIZER :
					//hack to return the first initializer
					return ((IType) container).getInitializer(1);
				default :
					assertTrue("illegal child type", false);
					break;
			}
			break;
		default :
			assertTrue("unsupported container type", false);
			break;
	}
	assertTrue("should not get here", false);
	return null;
}
/**
 * Returns true if this element is the main type of its compilation unit.
 */
protected boolean isMainType(IJavaScriptElement element, IJavaScriptElement parent) {
	if (parent instanceof IJavaScriptUnit) {
		if (element instanceof IType) {
			IJavaScriptUnit cu= (IJavaScriptUnit)parent;
			String typeName = cu.getElementName();
			typeName = typeName.substring(0, typeName.length() - 5);
			return element.getElementName().equals(typeName) && element.getParent().equals(cu);
		}
	}
	return false;
}
/**
 * Attempts to move the element with optional 
 * forcing. The operation should fail with the failure code.
 */
public void moveNegative(IJavaScriptElement element, IJavaScriptElement destination, IJavaScriptElement sibling, String rename, boolean force, int failureCode) {
	try {
		((ISourceManipulation)element).move(destination, sibling, rename, force, null);
	} catch (JavaScriptModelException jme) {
		assertTrue("Code not correct for JavaScriptModelException: " + jme, jme.getStatus().getCode() == failureCode);
		return;
	}
	assertTrue("The move should have failed for: " + element, false);
	return;
}
/**
 * Attempts to move the element with optional 
 * forcing. The operation should fail with the failure code.
 */
public void moveNegative(IJavaScriptElement[] elements, IJavaScriptElement[] destinations, IJavaScriptElement[] siblings, String[] renames, boolean force, int failureCode) {
	try {
		getJavaModel().move(elements, destinations, siblings, renames, force, null);
	} catch (JavaScriptModelException jme) {
		assertTrue("Code not correct for JavaScriptModelException: " + jme, jme.getStatus().getCode() == failureCode);
		return;
	}
	assertTrue("The move should have failed for for multiple elements: ", false);
	return;
}
/**
 * Moves the element to the container with optional rename
 * and forcing. The operation should succeed, so any exceptions
 * encountered are thrown.
 */
public void movePositive(IJavaScriptElement element, IJavaScriptElement container, IJavaScriptElement sibling, String rename, boolean force) throws JavaScriptModelException {
	IJavaScriptElement[] siblings = new IJavaScriptElement[] {sibling};
	String[] renamings = new String[] {rename};
	if (sibling == null) {
		siblings = null;
	}
	if (rename == null) {
		renamings = null;
	}
	movePositive(new IJavaScriptElement[] {element}, new IJavaScriptElement[] {container}, siblings, renamings, force);
}
/**
 * Moves the elements to the containers with optional renaming
 * and forcing. The operation should succeed, so any exceptions
 * encountered are thrown.
 */
public void movePositive(IJavaScriptElement[] elements, IJavaScriptElement[] destinations, IJavaScriptElement[] siblings, String[] names, boolean force) throws JavaScriptModelException {
	movePositive(elements, destinations, siblings, names, force, null);
}
/**
 * Moves the elements to the containers with optional renaming
 * and forcing. The operation should succeed, so any exceptions
 * encountered are thrown.
 */
public void movePositive(IJavaScriptElement[] elements, IJavaScriptElement[] destinations, IJavaScriptElement[] siblings, String[] names, boolean force, IProgressMonitor monitor) throws JavaScriptModelException {
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

	try {
		startDeltas();
		
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
			// ensure the original element no longer exists, unless moving within the same container
			if (!destinations[i].equals(element.getParent())) {
				if (element.getElementType() == IJavaScriptElement.PACKAGE_FRAGMENT) {
					//moving a package fragment does not necessary mean that it no longer exists
					//it could have members that are not part of the Java Model
					try {
						IResource[] members = ((IContainer) element.getUnderlyingResource()).members();
						if (members.length == 0) {
							assertTrue("The original element must not exist", !element.exists());
						}
					} catch (CoreException ce) {
						throw new JavaScriptModelException(ce);
					}
				} else {
					assertTrue("The original element must not exist", !element.exists());
				}
			}
			assertTrue("Moved element should exist", moved.exists());
	
			//ensure correct position
			if (element.getElementType() > IJavaScriptElement.JAVASCRIPT_UNIT) {
				if (siblings != null && siblings.length > 0) {
					ensureCorrectPositioning((IParent) moved.getParent(), siblings[i], moved);
				}
			}
			IJavaScriptElementDelta destDelta = null;
			if (isMainType(element, destinations[i]) && names != null && names[i] != null) { //moved/renamed main type to same cu
				destDelta = getDeltaFor(moved.getParent());
				assertTrue("Renamed compilation unit as result of main type not added", destDelta != null && destDelta.getKind() == IJavaScriptElementDelta.ADDED);
				assertTrue("flag should be F_MOVED_FROM", (destDelta.getFlags() & IJavaScriptElementDelta.F_MOVED_FROM) > 0);
				assertTrue("moved from handle should be original", destDelta.getMovedFromElement().equals(element.getParent()));
			} else {
				destDelta = getDeltaFor(destinations[i], true);
				assertTrue("Destination container not changed", destDelta != null && destDelta.getKind() == IJavaScriptElementDelta.CHANGED);
				IJavaScriptElementDelta[] deltas = destDelta.getAddedChildren();
				assertTrue("Added children not correct for element copy", deltas[i].getElement().equals(moved));
				assertTrue("should be K_ADDED", deltas[i].getKind() == IJavaScriptElementDelta.ADDED);
				IJavaScriptElementDelta sourceDelta= getDeltaFor(element, false);
				assertTrue("should be K_REMOVED", sourceDelta.getKind() == IJavaScriptElementDelta.REMOVED);
			}
		}
	} finally {
		stopDeltas();
	}
}
}
