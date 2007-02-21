/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.compiler.ast;

import org.eclipse.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.wst.jsdt.internal.compiler.codegen.CodeStream;
import org.eclipse.wst.jsdt.internal.compiler.impl.StringConstant;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;

public class RegExLiteral extends Literal {

	char[] source;

	public RegExLiteral(char[] token, int start, int end) {

		this(start,end);
		this.source = token;
	}

	public RegExLiteral(int s, int e) {

		super(s,e);
	}

	public void computeConstant() {
	
		constant = StringConstant.fromValue(String.valueOf(source));
	}

	/**
	 * Code generation for string literal
	 */ 
	public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {

		int pc = codeStream.position;
		if (valueRequired)
			codeStream.ldc(constant.stringValue());
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}

	public TypeBinding literalType(BlockScope scope) {

		return scope.getJavaLangString();
	}

	public StringBuffer printExpression(int indent, StringBuffer output) {
	
		// handle some special char.....
		for (int i = 0; i < source.length; i++) {
			switch (source[i]) {
//				case '\b' :
//					output.append("\\b"); //$NON-NLS-1$
//					break;
//				case '\t' :
//					output.append("\\t"); //$NON-NLS-1$
//					break;
//				case '\n' :
//					output.append("\\n"); //$NON-NLS-1$
//					break;
//				case '\f' :
//					output.append("\\f"); //$NON-NLS-1$
//					break;
//				case '\r' :
//					output.append("\\r"); //$NON-NLS-1$
//					break;
//				case '\"' :
//					output.append("\\\""); //$NON-NLS-1$
//					break;
//				case '\'' :
//					output.append("\\'"); //$NON-NLS-1$
//					break;
//				case '\\' : //take care not to display the escape as a potential real char
//					output.append("\\\\"); //$NON-NLS-1$
//					break;
				default :
					output.append(source[i]);
			}
		}
		return output;
	}

	public char[] source() {

		return source;
	}

	public void traverse(ASTVisitor visitor, BlockScope scope) {
		visitor.visit(this, scope);
		visitor.endVisit(this, scope);
	}
}
