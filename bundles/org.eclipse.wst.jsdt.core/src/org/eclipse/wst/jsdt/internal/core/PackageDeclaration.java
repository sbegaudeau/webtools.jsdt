/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core;

import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IPackageDeclaration;

/**
 * @see IPackageDeclaration
 */

/* package */ class PackageDeclaration extends SourceRefElement implements IPackageDeclaration {

	String name;

protected PackageDeclaration(CompilationUnit parent, String name) {
	super(parent);
	this.name = name;
}
protected PackageDeclaration(ClassFile parent, String name) {
	super(parent);
	this.name = name;
}
public boolean equals(Object o) {
	if (!(o instanceof PackageDeclaration)) return false;
	return super.equals(o);
}
public String getElementName() {
	return this.name;
}
/**
 * @see IJavaScriptElement
 */
public int getElementType() {
	return PACKAGE_DECLARATION;
}
/**
 * @see JavaElement#getHandleMemento()
 */
protected char getHandleMementoDelimiter() {
	return JavaElement.JEM_PACKAGEDECLARATION;
}
/*
 * @see JavaElement#getPrimaryElement(boolean)
 */
public IJavaScriptElement getPrimaryElement(boolean checkOwner) {
	CompilationUnit cu = (CompilationUnit)getAncestor(JAVASCRIPT_UNIT);
	if (checkOwner && cu.isPrimary()) return this;
	return cu.getPackageDeclaration(this.name);
}
/**
 * @private Debugging purposes
 */
protected void toStringInfo(int tab, StringBuffer buffer, Object info, boolean showResolvedInfo) {
	buffer.append(this.tabString(tab));
	buffer.append("package "); //$NON-NLS-1$
	toStringName(buffer);
	if (info == null) {
		buffer.append(" (not open)"); //$NON-NLS-1$
	}
}
}
