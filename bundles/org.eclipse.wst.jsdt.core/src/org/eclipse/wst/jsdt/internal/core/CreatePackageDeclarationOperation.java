/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IImportDeclaration;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptModelStatus;
import org.eclipse.wst.jsdt.core.IJavaScriptModelStatusConstants;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.JavaScriptConventions;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.Name;
import org.eclipse.wst.jsdt.core.dom.PackageDeclaration;
import org.eclipse.wst.jsdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.wst.jsdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.wst.jsdt.internal.core.util.Messages;

/**
 * <p>This operation adds/replaces a package declaration in an existing compilation unit.
 * If the compilation unit already includes the specified package declaration,
 * it is not generated (it does not generate duplicates).
 *
 * <p>Required Attributes:<ul>
 *  <li>Compilation unit element
 *  <li>Package name
 * </ul>
 */
public class CreatePackageDeclarationOperation extends CreateElementInCUOperation {
	/**
	 * The name of the package declaration being created
	 */
	protected String name = null;
/**
 * When executed, this operation will add a package declaration to the given compilation unit.
 */
public CreatePackageDeclarationOperation(String name, IJavaScriptUnit parentElement) {
	super(parentElement);
	this.name= name;
}
protected StructuralPropertyDescriptor getChildPropertyDescriptor(ASTNode parent) {
	return JavaScriptUnit.PACKAGE_PROPERTY;
}
protected ASTNode generateElementAST(ASTRewrite rewriter, IDocument document, IJavaScriptUnit cu) throws JavaScriptModelException {
	//look for an existing package declaration
	IJavaScriptElement[] children = getCompilationUnit().getChildren();
	for (int i = 0; i < children.length; i++) {
		if (children[i].getElementType() ==  IJavaScriptElement.PACKAGE_DECLARATION && this.name.equals(children[i].getElementName())) {
			//equivalent package declaration already exists
			this.creationOccurred = false;
			return null;
		}
	}
	AST ast = this.cuAST.getAST();
	PackageDeclaration pkgDeclaration = ast.newPackageDeclaration();
	Name astName = ast.newName(this.name);
	pkgDeclaration.setName(astName);
	return pkgDeclaration;
}
/**
 * Creates and returns the handle for the element this operation created.
 */
protected IJavaScriptElement generateResultHandle() {
	return getCompilationUnit().getPackageDeclaration(this.name);
}
/**
 * @see CreateElementInCUOperation#getMainTaskName()
 */
public String getMainTaskName(){
	return Messages.operation_createPackageProgress;
}
/**
 * Sets the correct position for new package declaration:<ul>
 * <li> before the first import
 * <li> if no imports, before the first type
 * <li> if no type - first thing in the CU
 * <li>
 */
protected void initializeDefaultPosition() {
	try {
		IJavaScriptUnit cu = getCompilationUnit();
		IImportDeclaration[] imports = cu.getImports();
		if (imports.length > 0) {
			createBefore(imports[0]);
			return;
		}
		IType[] types = cu.getTypes();
		if (types.length > 0) {
			createBefore(types[0]);
			return;
		}
	} catch (JavaScriptModelException e) {
		// cu doesn't exist: ignore
	}
}
/**
 * Possible failures: <ul>
 *  <li>NO_ELEMENTS_TO_PROCESS - no compilation unit was supplied to the operation
 *  <li>INVALID_NAME - a name supplied to the operation was not a valid
 * 		package declaration name.
 * </ul>
 * @see IJavaScriptModelStatus
 * @see JavaScriptConventions
 */
public IJavaScriptModelStatus verify() {
	IJavaScriptModelStatus status = super.verify();
	if (!status.isOK()) {
		return status;
	}
	IJavaScriptProject project = getParentElement().getJavaScriptProject();
	if (JavaScriptConventions.validatePackageName(this.name, project.getOption(JavaScriptCore.COMPILER_SOURCE, true), project.getOption(JavaScriptCore.COMPILER_COMPLIANCE, true)).getSeverity() == IStatus.ERROR) {
		return new JavaModelStatus(IJavaScriptModelStatusConstants.INVALID_NAME, this.name);
	}
	return JavaModelStatus.VERIFIED_OK;
}
}
