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
import org.eclipse.wst.jsdt.internal.compiler.flow.FlowInfo;
import org.eclipse.wst.jsdt.internal.compiler.impl.Constant;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;

public class UndefinedLiteral extends MagicLiteral {

	static final char[] source = {'u' , 'n' , 'd' , 'e', 'f', 'i', 'n', 'e', 'd'};

	public UndefinedLiteral(int s , int e) {

		super(s,e);
	}

	public void computeConstant() {

		constant = Constant.NotAConstant;
	}

	/**
	 * Code generation for the null literal
	 *
	 * @param currentScope org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.eclipse.wst.jsdt.internal.compiler.codegen.CodeStream
	 * @param valueRequired boolean
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
		int pc = codeStream.position;
		if (valueRequired) {
			codeStream.aconst_null();
			codeStream.generateImplicitConversion(this.implicitConversion);
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}
	public TypeBinding literalType(BlockScope scope) {
		return TypeBinding.UNDEFINED;
	}

	public int nullStatus(FlowInfo flowInfo) {
		return FlowInfo.NULL;
	}

	public Object reusableJSRTarget() {
		return TypeBinding.UNDEFINED;
	}

	public char[] source() {
		return source;
	}

	public void traverse(ASTVisitor visitor, BlockScope scope) {
		visitor.visit(this, scope);
		visitor.endVisit(this, scope);
	}
}
