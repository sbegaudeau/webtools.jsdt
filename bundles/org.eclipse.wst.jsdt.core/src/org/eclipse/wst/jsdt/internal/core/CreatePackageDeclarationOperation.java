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
import org.eclipse.wst.jsdt.core.ICompilationUnit;
import org.eclipse.wst.jsdt.core.IImportDeclaration;
import org.eclipse.wst.jsdt.core.IJavaElement;
import org.eclipse.wst.jsdt.core.IJavaModelStatus;
import org.eclipse.wst.jsdt.core.IJavaModelStatusConstants;
import org.eclipse.wst.jsdt.core.IJavaProject;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.JavaConventions;
import org.eclipse.wst.jsdt.core.JavaCore;
import org.eclipse.wst.jsdt.core.JavaModelException;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.CompilationUnit;
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
public CreatePackageDeclarationOperation(String name, ICompilationUnit parentElement) {
	super(parentElement);
	this.name= name;
}
protected StructuralPropertyDescriptor getChildPropertyDescriptor(ASTNode parent) {
	return CompilationUnit.PACKAGE_PROPERTY;
}
protected ASTNode generateElementAST(ASTRewrite rewriter, IDocument document, ICompilationUnit cu) throws JavaModelException {
	//look for an existing package declaration
	IJavaElement[] children = getCompilationUnit().getChildren();
	for (int i = 0; i < children.length; i++) {
		if (children[i].getElementType() ==  IJavaElement.PACKAGE_DECLARATION && this.name.equals(children[i].getElementName())) {
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
protected IJavaElement generateResultHandle() {
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
		ICompilationUnit cu = getCompilationUnit();
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
	} catch (JavaModelException e) {
		// cu doesn't exist: ignore
	}
}
/**
 * Possible failures: <ul>
 *  <li>NO_ELEMENTS_TO_PROCESS - no compilation unit was supplied to the operation
 *  <li>INVALID_NAME - a name supplied to the operation was not a valid
 * 		package declaration name.
 * </ul>
 * @see IJavaModelStatus
 * @see JavaConventions
 */
public IJavaModelStatus verify() {
	IJavaModelStatus status = super.verify();
	if (!status.isOK()) {
		return status;
	}
	IJavaProject project = getParentElement().getJavaProject();
	if (JavaConventions.validatePackageName(this.name, project.getOption(JavaCore.COMPILER_SOURCE, true), project.getOption(JavaCore.COMPILER_COMPLIANCE, true)).getSeverity() == IStatus.ERROR) {
		return new JavaModelStatus(IJavaModelStatusConstants.INVALID_NAME, this.name);
	}
	return JavaModelStatus.VERIFIED_OK;
}
}
