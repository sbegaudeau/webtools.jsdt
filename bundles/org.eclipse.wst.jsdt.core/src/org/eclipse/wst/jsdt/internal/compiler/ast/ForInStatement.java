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
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.wst.jsdt.internal.compiler.codegen.BranchLabel;
import org.eclipse.wst.jsdt.internal.compiler.codegen.CodeStream;
import org.eclipse.wst.jsdt.internal.compiler.flow.FlowContext;
import org.eclipse.wst.jsdt.internal.compiler.flow.FlowInfo;
import org.eclipse.wst.jsdt.internal.compiler.flow.LoopingFlowContext;
import org.eclipse.wst.jsdt.internal.compiler.flow.UnconditionalFlowInfo;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;

public class ForInStatement extends Statement {

	public Statement  iterationVariable;
	public Expression collection;
	public Statement action;

	//when there is no local declaration, there is no need of a new scope
	//scope is positionned either to a new scope, or to the "upper"scope (see resolveType)
	public boolean neededScope;
	public BlockScope scope;

	private BranchLabel breakLabel, continueLabel;

	// for local variables table attributes
	int preCondInitStateIndex = -1;
	int condIfTrueInitStateIndex = -1;
	int mergedInitStateIndex = -1;

	public ForInStatement(
		Statement  iterationVariable,
		Expression collection,
		Statement action,
		boolean neededScope,
		int s,
		int e) {

		this.sourceStart = s;
		this.sourceEnd = e;
		this.iterationVariable = iterationVariable;
		this.collection = collection;
		this.action = action;
		// remember useful empty statement
		if (action instanceof EmptyStatement) action.bits |= IsUsefulEmptyStatement;
		this.neededScope = neededScope;
	}

	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {

		breakLabel = new BranchLabel();
		continueLabel = new BranchLabel();

		// process the element variable and collection
		this.collection.checkNPE(currentScope, flowContext, flowInfo);
		flowInfo = this.iterationVariable.analyseCode(scope, flowContext, flowInfo);
		FlowInfo condInfo = this.collection.analyseCode(scope, flowContext, flowInfo.copy());

		LocalVariableBinding iterationVariableBinding=null;
		if (this.iterationVariable instanceof LocalDeclaration)
			iterationVariableBinding=((LocalDeclaration)this.iterationVariable).binding;
		else if (this.iterationVariable instanceof SingleNameReference)
		{
			SingleNameReference singleNameReference =(SingleNameReference)this.iterationVariable;
			if (singleNameReference.binding instanceof LocalVariableBinding)
				iterationVariableBinding=(LocalVariableBinding)singleNameReference.binding;
		}


		// element variable will be assigned when iterating
		if (iterationVariableBinding!=null)
		condInfo.markAsDefinitelyAssigned(iterationVariableBinding);

//		this.postCollectionInitStateIndex = currentScope.methodScope().recordInitializationStates(condInfo);

		// process the action
		LoopingFlowContext loopingContext =
			new LoopingFlowContext(flowContext, flowInfo, this, breakLabel,
				continueLabel, scope);
		UnconditionalFlowInfo actionInfo =
			condInfo.nullInfoLessUnconditionalCopy();
		if (iterationVariableBinding!=null)
			actionInfo.markAsDefinitelyUnknown(iterationVariableBinding);
		FlowInfo exitBranch;
		if (!(action == null || (action.isEmptyBlock()
		        	&& currentScope.compilerOptions().complianceLevel <= ClassFileConstants.JDK1_3))) {

			if (!this.action.complainIfUnreachable(actionInfo, scope, false)) {
				actionInfo = action.
					analyseCode(scope, loopingContext, actionInfo).
					unconditionalCopy();
			}

			// code generation can be optimized when no need to continue in the loop
			exitBranch = flowInfo.unconditionalCopy().
				addInitializationsFrom(condInfo.initsWhenFalse());
			// TODO (maxime) no need to test when false: can optimize (same for action being unreachable above)
			if ((actionInfo.tagBits & loopingContext.initsOnContinue.tagBits &
					FlowInfo.UNREACHABLE) != 0) {
				continueLabel = null;
			} else {
				actionInfo = actionInfo.mergedWith(loopingContext.initsOnContinue);
				loopingContext.complainOnDeferredFinalChecks(scope, actionInfo);
				exitBranch.addPotentialInitializationsFrom(actionInfo);
			}
		} else {
			exitBranch = condInfo.initsWhenFalse();
		}

		// we need the variable to iterate the collection even if the
		// element variable is not used
		final boolean hasEmptyAction = this.action == null
				|| this.action.isEmptyBlock()
				|| ((this.action.bits & IsUsefulEmptyStatement) != 0);


		//end of loop
		loopingContext.complainOnDeferredNullChecks(currentScope, actionInfo);

		FlowInfo mergedInfo = FlowInfo.mergedOptimizedBranches(
				(loopingContext.initsOnBreak.tagBits &
					FlowInfo.UNREACHABLE) != 0 ?
					loopingContext.initsOnBreak :
					flowInfo.addInitializationsFrom(loopingContext.initsOnBreak), // recover upstream null info
				false,
				exitBranch,
				false,
				true /*for(;;){}while(true); unreachable(); */);
//		mergedInitStateIndex = currentScope.methodScope().recordInitializationStates(mergedInfo);
		return mergedInfo;
	}

	/**
	 * For statement code generation
	 *
	 * @param currentScope org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.eclipse.wst.jsdt.internal.compiler.codegen.CodeStream
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {

//		if ((bits & IsReachable) == 0) {
//			return;
//		}
//		int pc = codeStream.position;
//
//		// generate the initializations
//		if (initializations != null) {
//			for (int i = 0, max = initializations.length; i < max; i++) {
//				initializations[i].generateCode(scope, codeStream);
//			}
//		}
//		Constant cst = this.condition == null ? null : this.condition.optimizedBooleanConstant();
//		boolean isConditionOptimizedFalse = cst != null && (cst != Constant.NotAConstant && cst.booleanValue() == false);
//		if (isConditionOptimizedFalse) {
//			condition.generateCode(scope, codeStream, false);
//			// May loose some local variable initializations : affecting the local variable attributes
//			if (neededScope) {
//				codeStream.exitUserScope(scope);
//			}
//			if (mergedInitStateIndex != -1) {
//				codeStream.removeNotDefinitelyAssignedVariables(currentScope, mergedInitStateIndex);
//				codeStream.addDefinitelyAssignedVariables(currentScope, mergedInitStateIndex);
//			}
//			codeStream.recordPositionsFrom(pc, this.sourceStart);
//			return;
//		}
//
//		// label management
//		BranchLabel actionLabel = new BranchLabel(codeStream);
//		actionLabel.tagBits |= BranchLabel.USED;
//		BranchLabel conditionLabel = new BranchLabel(codeStream);
//		breakLabel.initialize(codeStream);
//		if (this.continueLabel != null) {
//			this.continueLabel.initialize(codeStream);
//		}
//		// jump over the actionBlock
//		if ((condition != null)
//			&& (condition.constant == Constant.NotAConstant)
//			&& !((action == null || action.isEmptyBlock()) && (increments == null))) {
//			conditionLabel.tagBits |= BranchLabel.USED;
//			int jumpPC = codeStream.position;
//			codeStream.goto_(conditionLabel);
//			codeStream.recordPositionsFrom(jumpPC, condition.sourceStart);
//		}
//		// generate the loop action
//		if (action != null) {
//			// Required to fix 1PR0XVS: LFRE:WINNT - Compiler: variable table for method appears incorrect
//			if (condIfTrueInitStateIndex != -1) {
//				// insert all locals initialized inside the condition into the action generated prior to the condition
//				codeStream.addDefinitelyAssignedVariables(
//					currentScope,
//					condIfTrueInitStateIndex);
//			}
//			actionLabel.place();
//			action.generateCode(scope, codeStream);
//		} else {
//			actionLabel.place();
//		}
//		// continuation point
//		if (continueLabel != null) {
//			continueLabel.place();
//			// generate the increments for next iteration
//			if (increments != null) {
//				for (int i = 0, max = increments.length; i < max; i++) {
//					increments[i].generateCode(scope, codeStream);
//				}
//			}
//		}
//
//		// May loose some local variable initializations : affecting the local variable attributes
//		if (preCondInitStateIndex != -1) {
//			codeStream.removeNotDefinitelyAssignedVariables(currentScope, preCondInitStateIndex);
//		}
//
//		// generate the condition
//		conditionLabel.place();
//		if ((condition != null) && (condition.constant == Constant.NotAConstant)) {
//			condition.generateOptimizedBoolean(scope, codeStream, actionLabel, null, true);
//		} else {
//			if (continueLabel != null) {
//				codeStream.goto_(actionLabel);
//			}
//		}
//
//		// May loose some local variable initializations : affecting the local variable attributes
//		if (neededScope) {
//			codeStream.exitUserScope(scope);
//		}
//		if (mergedInitStateIndex != -1) {
//			codeStream.removeNotDefinitelyAssignedVariables(currentScope, mergedInitStateIndex);
//			codeStream.addDefinitelyAssignedVariables(currentScope, mergedInitStateIndex);
//		}
//		breakLabel.place();
//		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}

	public StringBuffer printStatement(int tab, StringBuffer output) {

		printIndent(tab, output).append("for ("); //$NON-NLS-1$
		//inits
		if (iterationVariable != null) {
			if (iterationVariable instanceof AbstractVariableDeclaration) {
				AbstractVariableDeclaration variable = (AbstractVariableDeclaration) iterationVariable;
				variable.printAsExpression(0, output);
			}
			else
				iterationVariable.print(0, output);
		}
		output.append(" in "); //$NON-NLS-1$
		//cond
		if (collection != null) collection.printExpression(0, output);
		output.append(") "); //$NON-NLS-1$
		//block
		if (action == null)
			output.append(';');
		else {
			output.append('\n');
			action.printStatement(tab + 1, output);
		}
		return output;
	}

	public void resolve(BlockScope upperScope) {

		// use the scope that will hold the init declarations
		scope = neededScope ? new BlockScope(upperScope) : upperScope;
		if (iterationVariable != null)
		{
			if (iterationVariable instanceof Expression) {
				Expression expression = (Expression) iterationVariable;
				expression.resolveType(scope, true, null);
// TODO: show a warning message here saying this var is at global scope
			}
			else
				iterationVariable.resolve(scope);
		}
		if (collection != null) {
			TypeBinding type = collection.resolveTypeExpecting(scope, TypeBinding.BOOLEAN);
			collection.computeConversion(scope, type, type);
		}
		if (action != null)
			action.resolve(scope);
	}

	public void traverse(
		ASTVisitor visitor,
		BlockScope blockScope) {

		if (visitor.visit(this, blockScope)) {
			if (iterationVariable != null) {
				iterationVariable.traverse(visitor, scope);
			}

			if (collection != null)
				collection.traverse(visitor, scope);


			if (action != null)
				action.traverse(visitor, scope);
		}
		visitor.endVisit(this, blockScope);
	}
}
