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
import org.eclipse.wst.jsdt.internal.compiler.lookup.CompilationUnitScope;

/**
 * Annotation
 */
public abstract class Annotation extends Expression {
	
	public char[][] tokens;
	public long[] sourcePositions;
	public int declarationSourceEnd;
	
	public StringBuffer printExpression(int indent, StringBuffer output) {
		output.append('@');
		for (int i = 0; i < tokens.length; i++) {
			if (i > 0) output.append('.');
			output.append(tokens[i]);
		}
		return output;
	}
	public abstract void traverse(ASTVisitor visitor, BlockScope scope);
	public abstract void traverse(ASTVisitor visitor, ClassScope scope);
	public abstract void traverse(ASTVisitor visitor, CompilationUnitScope scope);
}
