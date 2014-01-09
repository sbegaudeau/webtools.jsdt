/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.jseview.properties;

import java.util.ArrayList;

import org.eclipse.core.runtime.Path;

import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IOpenable;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.ITypeRoot;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.Signature;
import org.eclipse.wst.jsdt.core.WorkingCopyOwner;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.internal.compiler.util.SuffixConstants;
import org.eclipse.wst.jsdt.internal.core.BinaryType;
import org.eclipse.wst.jsdt.internal.core.JavaProject;
import org.eclipse.wst.jsdt.internal.core.util.BindingKeyParser;

public class JavaElementFinder extends BindingKeyParser {

	private JavaProject project;
	private WorkingCopyOwner owner;
	public IJavaScriptElement element;
	public JavaScriptModelException exception;
	private ArrayList types = new ArrayList();

	public JavaElementFinder(String key, JavaProject project, WorkingCopyOwner owner) {
		super(key);
		this.project = project;
		this.owner = owner;
	}

	private JavaElementFinder(BindingKeyParser parser, JavaProject project, WorkingCopyOwner owner) {
		super(parser);
		this.project = project;
		this.owner = owner;
	}

	public void consumeField(char[] fieldName) {
		if (!(this.element instanceof IType)) return;
		this.element = ((IType) this.element).getField(new String(fieldName));
	}

	public void consumeFullyQualifiedName(char[] fullyQualifiedName) {
		try {
			this.element = this.project.findType(new String(CharOperation.replaceOnCopy(fullyQualifiedName, '/', '.')), this.owner);
		} catch (JavaScriptModelException e) {
			this.exception = e;
		}
	}

	public void consumeLocalType(char[] uniqueKey) {
		if (this.element == null) return;
		if (this.element instanceof BinaryType) {
			int lastSlash = CharOperation.lastIndexOf('/', uniqueKey);
			int end = CharOperation.indexOf(';', uniqueKey, lastSlash+1);
			char[] localName = CharOperation.subarray(uniqueKey, lastSlash+1, end);
			IPackageFragment pkg = (IPackageFragment) this.element.getAncestor(IJavaScriptElement.PACKAGE_FRAGMENT);
			this.element = pkg.getClassFile(new String(localName) + SuffixConstants.SUFFIX_STRING_java);
		} else {
			int firstDollar = CharOperation.indexOf('$', uniqueKey);
			int end = CharOperation.indexOf('$', uniqueKey, firstDollar+1);
			if (end == -1)
				end = CharOperation.indexOf(';', uniqueKey, firstDollar+1);
			char[] sourceStart = CharOperation.subarray(uniqueKey, firstDollar+1, end);
			int position = Integer.parseInt(new String(sourceStart));
			try {
				this.element = ((ITypeRoot) this.element.getOpenable()).getElementAt(position);
			} catch (JavaScriptModelException e) {
				this.exception = e;
			}
		}
	}

	public void consumeMemberType(char[] simpleTypeName) {
		if (!(this.element instanceof IType)) return;
		this.element = ((IType) this.element).getType(new String(simpleTypeName));
	}

	public void consumeMethod(char[] selector, char[] signature) {
		if (!(this.element instanceof IType)) return;
		String[] parameterTypes = Signature.getParameterTypes(new String(signature));
		IType type = (IType) this.element;
		IFunction method = type.getFunction(new String(selector), parameterTypes);
		IFunction[] methods = type.findMethods(method);
		if (methods.length > 0)
			this.element = methods[0];
	}

	public void consumePackage(char[] pkgName) {
//		pkgName = CharOperation.replaceOnCopy(pkgName, '/', '.');
		try {
//			this.element = this.project.findPackageFragment(new String(pkgName));
			this.element = this.project.findPackageFragment(new Path(new String(pkgName)));
		} catch (JavaScriptModelException e) {
			this.exception = e;
		}
	}

	public void consumeParser(BindingKeyParser parser) {
		this.types.add(parser);
	}

	public void consumeSecondaryType(char[] simpleTypeName) {
		if (this.element == null) return;
		IOpenable openable = this.element.getOpenable();
		if (!(openable instanceof IJavaScriptUnit)) return;
		this.element = ((IJavaScriptUnit) openable).getType(new String(simpleTypeName));
	}

	public void consumeTypeVariable(char[] position, char[] typeVariableName) {
		if (this.element == null) return;
		switch (this.element.getElementType()) {
		case IJavaScriptElement.TYPE:
//			this.element = ((IType) this.element).getTypeParameter(new String(typeVariableName));
			break;
		case IJavaScriptElement.METHOD:
//			this.element = ((IMethod) this.element).getTypeParameter(new String(typeVariableName));
			break;
		}
	}

	public BindingKeyParser newParser() {
		return new JavaElementFinder(this, this.project, this.owner);
	}

}
