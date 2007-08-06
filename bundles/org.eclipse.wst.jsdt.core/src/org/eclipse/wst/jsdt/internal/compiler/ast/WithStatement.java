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
import org.eclipse.wst.jsdt.internal.compiler.impl.*;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.wst.jsdt.internal.compiler.codegen.*;
import org.eclipse.wst.jsdt.internal.compiler.flow.*;
import org.eclipse.wst.jsdt.internal.compiler.lookup.*;

public class WithStatement extends Statement {
	
	public Expression condition;
	public Statement action;

	public WithStatement(Expression condition, Statement action, int s, int e) {

		this.condition = condition;
		this.action = action;
		// remember useful empty statement
		if (action instanceof EmptyStatement) action.bits |= IsUsefulEmptyStatement;
		sourceStart = s;
		sourceEnd = e;
	}

	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {

		flowInfo =
			condition.analyseCode(currentScope, flowContext, flowInfo);

		if (this.action != null) {
			// Save info for code gen
//			thenInitStateIndex =
//				currentScope.methodScope().recordInitializationStates(thenFlowInfo);
			if (!action.complainIfUnreachable(flowInfo, currentScope, false)) {
				flowInfo =
					this.action.analyseCode(currentScope, flowContext, flowInfo);
			}
		}

		
		return flowInfo;
	}

	/**
	 * While code generation
	 *
	 * @param currentScope org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.eclipse.wst.jsdt.internal.compiler.codegen.CodeStream
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {

	}

	public void resolve(BlockScope parentScope) {

		TypeBinding type = condition.resolveTypeExpecting(parentScope, TypeBinding.ANY);
        BlockScope scope = (type instanceof ReferenceBinding)?
        		new WithScope(parentScope,(ReferenceBinding)type) : parentScope; 
		if (action != null)
			action.resolve(scope);
	}

	public StringBuffer printStatement(int tab, StringBuffer output) {

		printIndent(tab, output).append("with ("); //$NON-NLS-1$
		condition.printExpression(0, output).append(")\n");
		if (action == null)
			output.append(';');
		else
			action.printStatement(tab + 1, output); 
		return output;
	}

	public void traverse(
		ASTVisitor visitor,
		BlockScope blockScope) {

		if (visitor.visit(this, blockScope)) {
			condition.traverse(visitor, blockScope);
			if (action != null)
				action.traverse(visitor, blockScope);
		}
		visitor.endVisit(this, blockScope);
	}
}
