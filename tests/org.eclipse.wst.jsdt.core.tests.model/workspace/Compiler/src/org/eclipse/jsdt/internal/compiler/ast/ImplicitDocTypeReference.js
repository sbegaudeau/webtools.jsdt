/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.compiler.ast;

import org.eclipse.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ClassScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Scope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;

public class ImplicitDocTypeReference extends TypeReference {
	
	public char[] token;

	public ImplicitDocTypeReference(char[] name, int pos) {
		super();
		this.token = name;
		this.sourceStart = pos;
		this.sourceEnd = pos;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.compiler.ast.TypeReference#copyDims(int)
	 */
	public TypeReference copyDims(int dim) {
		return null;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.compiler.ast.TypeReference#getTypeBinding(org.eclipse.wst.jsdt.internal.compiler.lookup.Scope)
	 */
	protected TypeBinding getTypeBinding(Scope scope) {
		this.constant = NotAConstant;
		return this.resolvedType = scope.enclosingSourceType();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.compiler.ast.TypeReference#getTypeName()
	 */
	public char[][] getTypeName() {
		if (this.token != null) {
			char[][] tokens = { this.token };
			return tokens;
		}
		return null;
	}
	public boolean isThis() {
		return true;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.compiler.ast.TypeReference#traverse(org.eclipse.wst.jsdt.internal.compiler.ASTVisitor, org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope)
	 */
	public void traverse(ASTVisitor visitor, BlockScope classScope) {
		// Do nothing
	}
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.compiler.ast.TypeReference#traverse(org.eclipse.wst.jsdt.internal.compiler.ASTVisitor, org.eclipse.wst.jsdt.internal.compiler.lookup.ClassScope)
	 */
	public void traverse(ASTVisitor visitor, ClassScope classScope) {
		// Do nothing
	}
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.compiler.ast.Expression#printExpression(int, java.lang.StringBuffer)
	 */
	public StringBuffer printExpression(int indent, StringBuffer output) {
		return new StringBuffer();
	}
}
