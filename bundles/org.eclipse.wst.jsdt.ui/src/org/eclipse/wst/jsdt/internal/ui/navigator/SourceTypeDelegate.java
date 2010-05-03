/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.internal.ui.navigator;

import java.io.InputStream;
import java.net.URI;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.wst.jsdt.core.CompletionRequestor;
import org.eclipse.wst.jsdt.core.IClassFile;
import org.eclipse.wst.jsdt.core.IField;
import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.IInitializer;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptModel;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IOpenable;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.ISourceRange;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.ITypeHierarchy;
import org.eclipse.wst.jsdt.core.ITypeRoot;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.LibrarySuperType;
import org.eclipse.wst.jsdt.core.WorkingCopyOwner;
import org.eclipse.wst.jsdt.internal.core.JavaElement;
import org.eclipse.wst.jsdt.internal.core.NameLookup;
import org.eclipse.wst.jsdt.internal.core.SearchableEnvironment;
import org.eclipse.wst.jsdt.internal.core.SourceType;

public class SourceTypeDelegate extends SourceType implements ITypeDelegate {
	private SourceType fDelegateType;

	public SourceTypeDelegate(SourceType delegate) {
		super((JavaElement)delegate.getParent(), delegate.getElementName());
		fDelegateType = delegate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#codeComplete(char[], int, int,
	 * char[][], char[][], int[], boolean,
	 * org.eclipse.wst.jsdt.core.CompletionRequestor)
	 */
	public void codeComplete(char[] snippet, int insertion, int position, char[][] localVariableTypeNames, char[][] localVariableNames, int[] localVariableModifiers, boolean isStatic, CompletionRequestor requestor) throws JavaScriptModelException {
		fDelegateType.codeComplete(snippet, insertion, position, localVariableTypeNames, localVariableNames, localVariableModifiers, isStatic, requestor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#codeComplete(char[], int, int,
	 * char[][], char[][], int[], boolean,
	 * org.eclipse.wst.jsdt.core.CompletionRequestor,
	 * org.eclipse.wst.jsdt.core.WorkingCopyOwner)
	 */
	public void codeComplete(char[] snippet, int insertion, int position, char[][] localVariableTypeNames, char[][] localVariableNames, int[] localVariableModifiers, boolean isStatic, CompletionRequestor requestor, WorkingCopyOwner owner) throws JavaScriptModelException {
		fDelegateType.codeComplete(snippet, insertion, position, localVariableTypeNames, localVariableNames, localVariableModifiers, isStatic, requestor, owner);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.ISourceManipulation#copy(org.eclipse.wst.
	 * jsdt.core.IJavaScriptElement,
	 * org.eclipse.wst.jsdt.core.IJavaScriptElement, java.lang.String,
	 * boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void copy(IJavaScriptElement container, IJavaScriptElement sibling, String rename, boolean replace, IProgressMonitor monitor) throws JavaScriptModelException {
		fDelegateType.copy(container, sibling, rename, replace, monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#createField(java.lang.String,
	 * org.eclipse.wst.jsdt.core.IJavaScriptElement, boolean,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IField createField(String contents, IJavaScriptElement sibling, boolean force, IProgressMonitor monitor) throws JavaScriptModelException {
		return fDelegateType.createField(contents, sibling, force, monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#createMethod(java.lang.String,
	 * org.eclipse.wst.jsdt.core.IJavaScriptElement, boolean,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IFunction createMethod(String contents, IJavaScriptElement sibling, boolean force, IProgressMonitor monitor) throws JavaScriptModelException {
		return fDelegateType.createMethod(contents, sibling, force, monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#createType(java.lang.String,
	 * org.eclipse.wst.jsdt.core.IJavaScriptElement, boolean,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IType createType(String contents, IJavaScriptElement sibling, boolean force, IProgressMonitor monitor) throws JavaScriptModelException {
		return fDelegateType.createType(contents, sibling, force, monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.ISourceManipulation#delete(boolean,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void delete(boolean force, IProgressMonitor monitor) throws JavaScriptModelException {
		fDelegateType.delete(force, monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJavaScriptElement#exists()
	 */
	public boolean exists() {
		return fDelegateType.exists();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.IType#findMethods(org.eclipse.wst.jsdt.core
	 * .IFunction)
	 */
	public IFunction[] findMethods(IFunction method) {
		return fDelegateType.findMethods(method);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return fDelegateType.getAdapter(adapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJavaScriptElement#getAncestor(int)
	 */
	public IJavaScriptElement getAncestor(int ancestorType) {
		return fDelegateType.getAncestor(ancestorType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.IJavaScriptElement#getAttachedJavadoc(org
	 * .eclipse.core.runtime.IProgressMonitor)
	 */
	public String getAttachedJavadoc(IProgressMonitor monitor) throws JavaScriptModelException {
		return fDelegateType.getAttachedJavadoc(monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IMember#getCategories()
	 */
	public String[] getCategories() throws JavaScriptModelException {
		return fDelegateType.getCategories();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IParent#getChildren()
	 */
	public IJavaScriptElement[] getChildren() throws JavaScriptModelException {
		return fDelegateType.getChildren();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.IType#getChildrenForCategory(java.lang.String
	 * )
	 */
	public IJavaScriptElement[] getChildrenForCategory(String category) throws JavaScriptModelException {
		return fDelegateType.getChildrenForCategory(category);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IMember#getClassFile()
	 */
	public IClassFile getClassFile() {
		return fDelegateType.getClassFile();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJavaScriptElement#getCommonSuperType()
	 */
	public LibrarySuperType getCommonSuperType() {
		return fDelegateType.getCommonSuperType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.IJavaScriptElement#getCorrespondingResource()
	 */
	public IResource getCorrespondingResource() throws JavaScriptModelException {
		return fDelegateType.getCorrespondingResource();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IMember#getDeclaringType()
	 */
	public IType getDeclaringType() {
		return fDelegateType.getDeclaringType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJavaScriptElement#getDisplayName()
	 */
	public String getDisplayName() {
		return fDelegateType.getDisplayName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJavaScriptElement#getElementName()
	 */
	public String getElementName() {
		return fDelegateType.getElementName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJavaScriptElement#getElementType()
	 */
	public int getElementType() {
		return fDelegateType.getElementType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#getField(java.lang.String)
	 */
	public IField getField(String name) {
		return fDelegateType.getField(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#getFields()
	 */
	public IField[] getFields() throws JavaScriptModelException {
		return fDelegateType.getFields();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IMember#getFlags()
	 */
	public int getFlags() throws JavaScriptModelException {
		return fDelegateType.getFlags();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#getFullyQualifiedName()
	 */
	public String getFullyQualifiedName() {
		return fDelegateType.getFullyQualifiedName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#getFullyQualifiedName(char)
	 */
	public String getFullyQualifiedName(char enclosingTypeSeparator) {
		return fDelegateType.getFullyQualifiedName(enclosingTypeSeparator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.IType#getFullyQualifiedParameterizedName()
	 */
	public String getFullyQualifiedParameterizedName() throws JavaScriptModelException {
		return fDelegateType.getFullyQualifiedParameterizedName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#getFunction(java.lang.String,
	 * java.lang.String[])
	 */
	public IFunction getFunction(String name, String[] parameterTypeSignatures) {
		return getFunction(name, parameterTypeSignatures);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#getFunctions()
	 */
	public IFunction[] getFunctions() throws JavaScriptModelException {
		return getFunctions();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJavaScriptElement#getHandleIdentifier()
	 */
	public String getHandleIdentifier() {
		return fDelegateType.getHandleIdentifier();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJavaScriptElement#getHostPath()
	 */
	public URI getHostPath() {
		return fDelegateType.getHostPath();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#getInitializer(int)
	 */
	public IInitializer getInitializer(int occurrenceCount) {
		return fDelegateType.getInitializer(occurrenceCount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#getInitializers()
	 */
	public IInitializer[] getInitializers() throws JavaScriptModelException {
		return fDelegateType.getInitializers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJavaScriptElement#getJavaScriptModel()
	 */
	public IJavaScriptModel getJavaScriptModel() {
		return fDelegateType.getJavaScriptModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.IJavaScriptElement#getJavaScriptProject()
	 */
	public IJavaScriptProject getJavaScriptProject() {
		return fDelegateType.getJavaScriptProject();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IMember#getJavaScriptUnit()
	 */
	public IJavaScriptUnit getJavaScriptUnit() {
		return fDelegateType.getJavaScriptUnit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IMember#getJSdocRange()
	 */
	public ISourceRange getJSdocRange() throws JavaScriptModelException {
		return fDelegateType.getJSdocRange();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#getKey()
	 */
	public String getKey() {
		return fDelegateType.getKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IMember#getNameRange()
	 */
	public ISourceRange getNameRange() throws JavaScriptModelException {
		return fDelegateType.getNameRange();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IMember#getOccurrenceCount()
	 */
	public int getOccurrenceCount() {
		return fDelegateType.getOccurrenceCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJavaScriptElement#getOpenable()
	 */
	public IOpenable getOpenable() {
		return fDelegateType.getOpenable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#getPackageFragment()
	 */
	public IPackageFragment getPackageFragment() {
		return fDelegateType.getPackageFragment();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJavaScriptElement#getParent()
	 */
	public IJavaScriptElement getParent() {
		return fDelegateType.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJavaScriptElement#getPath()
	 */
	public IPath getPath() {
		return fDelegateType.getPath();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJavaScriptElement#getPrimaryElement()
	 */
	public IJavaScriptElement getPrimaryElement() {
		return fDelegateType.getPrimaryElement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJavaScriptElement#getResource()
	 */
	public IResource getResource() {
		return fDelegateType.getResource();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJavaScriptElement#getSchedulingRule()
	 */
	public ISchedulingRule getSchedulingRule() {
		return fDelegateType.getSchedulingRule();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.ISourceReference#getSource()
	 */
	public String getSource() throws JavaScriptModelException {
		return fDelegateType.getSource();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.ISourceReference#getSourceRange()
	 */
	public ISourceRange getSourceRange() throws JavaScriptModelException {
		return fDelegateType.getSourceRange();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#getSuperclassName()
	 */
	public String getSuperclassName() throws JavaScriptModelException {
		return fDelegateType.getSuperclassName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#getSuperclassTypeSignature()
	 */
	public String getSuperclassTypeSignature() throws JavaScriptModelException {
		return fDelegateType.getSuperclassTypeSignature();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#getType(java.lang.String)
	 */
	public IType getType(String name) {
		return fDelegateType.getType(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IMember#getType(java.lang.String, int)
	 */
	public IType getType(String name, int occurrenceCount) {
		return fDelegateType.getType(name, occurrenceCount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#getTypeQualifiedName()
	 */
	public String getTypeQualifiedName() {
		return fDelegateType.getTypeQualifiedName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#getTypeQualifiedName(char)
	 */
	public String getTypeQualifiedName(char enclosingTypeSeparator) {
		return fDelegateType.getTypeQualifiedName(enclosingTypeSeparator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IMember#getTypeRoot()
	 */
	public ITypeRoot getTypeRoot() {
		return fDelegateType.getTypeRoot();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#getTypes()
	 */
	public IType[] getTypes() throws JavaScriptModelException {
		return fDelegateType.getTypes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.IJavaScriptElement#getUnderlyingResource()
	 */
	public IResource getUnderlyingResource() throws JavaScriptModelException {
		return fDelegateType.getUnderlyingResource();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IParent#hasChildren()
	 */
	public boolean hasChildren() throws JavaScriptModelException {
		return fDelegateType.hasChildren();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#isAnonymous()
	 */
	public boolean isAnonymous() {
		return fDelegateType.isAnonymous();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IMember#isBinary()
	 */
	public boolean isBinary() {
		return fDelegateType.isBinary();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#isClass()
	 */
	public boolean isClass() throws JavaScriptModelException {
		return fDelegateType.isClass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#isLocal()
	 */
	public boolean isLocal() {
		return fDelegateType.isLocal();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#isMember()
	 */
	public boolean isMember() {
		return fDelegateType.isMember();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJavaScriptElement#isReadOnly()
	 */
	public boolean isReadOnly() {
		return fDelegateType.isReadOnly();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#isResolved()
	 */
	public boolean isResolved() {
		return fDelegateType.isResolved();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJavaScriptElement#isStructureKnown()
	 */
	public boolean isStructureKnown() throws JavaScriptModelException {
		return fDelegateType.isStructureKnown();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJavaScriptElement#isVirtual()
	 */
	public boolean isVirtual() {
		return fDelegateType.isVirtual();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.IType#loadTypeHierachy(java.io.InputStream,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	public ITypeHierarchy loadTypeHierachy(InputStream input, IProgressMonitor monitor) throws JavaScriptModelException {
		return fDelegateType.loadTypeHierachy(input, monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.ISourceManipulation#move(org.eclipse.wst.
	 * jsdt.core.IJavaScriptElement,
	 * org.eclipse.wst.jsdt.core.IJavaScriptElement, java.lang.String,
	 * boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void move(IJavaScriptElement container, IJavaScriptElement sibling, String rename, boolean replace, IProgressMonitor monitor) throws JavaScriptModelException {
		fDelegateType.move(container, sibling, rename, replace, monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.ILookupScope#newNameLookup(org.eclipse.wst
	 * .jsdt.core.IJavaScriptUnit[])
	 */
	public NameLookup newNameLookup(IJavaScriptUnit[] workingCopies) throws JavaScriptModelException {
		return fDelegateType.newNameLookup(workingCopies);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.ILookupScope#newNameLookup(org.eclipse.wst
	 * .jsdt.core.WorkingCopyOwner)
	 */
	public NameLookup newNameLookup(WorkingCopyOwner owner) throws JavaScriptModelException {
		return fDelegateType.newNameLookup(owner);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.ILookupScope#newSearchableNameEnvironment
	 * (org.eclipse.wst.jsdt.core.IJavaScriptUnit[])
	 */
	public SearchableEnvironment newSearchableNameEnvironment(IJavaScriptUnit[] workingCopies) throws JavaScriptModelException {
		return fDelegateType.newSearchableNameEnvironment(workingCopies);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.ILookupScope#newSearchableNameEnvironment
	 * (org.eclipse.wst.jsdt.core.WorkingCopyOwner)
	 */
	public SearchableEnvironment newSearchableNameEnvironment(WorkingCopyOwner owner) throws JavaScriptModelException {
		return fDelegateType.newSearchableNameEnvironment(owner);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.IType#newSupertypeHierarchy(org.eclipse.wst
	 * .jsdt.core.IJavaScriptUnit[],
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	public ITypeHierarchy newSupertypeHierarchy(IJavaScriptUnit[] workingCopies, IProgressMonitor monitor) throws JavaScriptModelException {
		return fDelegateType.newSupertypeHierarchy(workingCopies, monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.IType#newSupertypeHierarchy(org.eclipse.core
	 * .runtime.IProgressMonitor)
	 */
	public ITypeHierarchy newSupertypeHierarchy(IProgressMonitor monitor) throws JavaScriptModelException {
		return fDelegateType.newSupertypeHierarchy(monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.IType#newSupertypeHierarchy(org.eclipse.wst
	 * .jsdt.core.WorkingCopyOwner, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public ITypeHierarchy newSupertypeHierarchy(WorkingCopyOwner owner, IProgressMonitor monitor) throws JavaScriptModelException {
		return fDelegateType.newSupertypeHierarchy(owner, monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.IType#newTypeHierarchy(org.eclipse.wst.jsdt
	 * .core.IJavaScriptProject, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public ITypeHierarchy newTypeHierarchy(IJavaScriptProject project, IProgressMonitor monitor) throws JavaScriptModelException {
		return fDelegateType.newTypeHierarchy(project, monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.IType#newTypeHierarchy(org.eclipse.wst.jsdt
	 * .core.IJavaScriptProject, org.eclipse.wst.jsdt.core.WorkingCopyOwner,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	public ITypeHierarchy newTypeHierarchy(IJavaScriptProject project, WorkingCopyOwner owner, IProgressMonitor monitor) throws JavaScriptModelException {
		return fDelegateType.newTypeHierarchy(project, owner, monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.IType#newTypeHierarchy(org.eclipse.wst.jsdt
	 * .core.IJavaScriptUnit[], org.eclipse.core.runtime.IProgressMonitor)
	 */
	public ITypeHierarchy newTypeHierarchy(IJavaScriptUnit[] workingCopies, IProgressMonitor monitor) throws JavaScriptModelException {
		return fDelegateType.newTypeHierarchy(workingCopies, monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.IType#newTypeHierarchy(org.eclipse.core.runtime
	 * .IProgressMonitor)
	 */
	public ITypeHierarchy newTypeHierarchy(IProgressMonitor monitor) throws JavaScriptModelException {
		return fDelegateType.newTypeHierarchy(monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.IType#newTypeHierarchy(org.eclipse.wst.jsdt
	 * .core.WorkingCopyOwner, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public ITypeHierarchy newTypeHierarchy(WorkingCopyOwner owner, IProgressMonitor monitor) throws JavaScriptModelException {
		return fDelegateType.newTypeHierarchy(owner, monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.jsdt.core.ISourceManipulation#rename(java.lang.String,
	 * boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void rename(String name, boolean replace, IProgressMonitor monitor) throws JavaScriptModelException {
		fDelegateType.rename(name, replace, monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#resolveType(java.lang.String)
	 */
	public String[][] resolveType(String typeName) throws JavaScriptModelException {
		return fDelegateType.resolveType(typeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IType#resolveType(java.lang.String,
	 * org.eclipse.wst.jsdt.core.WorkingCopyOwner)
	 */
	public String[][] resolveType(String typeName, WorkingCopyOwner owner) throws JavaScriptModelException {
		return null;
	}

	public String toString() {
		return "SourceTypeDelegate: " + fDelegateType.getDisplayName(); //$NON-NLS-1$
	}

	public IType getDelegate() {
		return fDelegateType;
	}
}
