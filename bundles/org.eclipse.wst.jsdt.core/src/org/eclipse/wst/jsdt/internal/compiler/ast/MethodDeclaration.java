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

import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.ast.IASTNode;
import org.eclipse.wst.jsdt.core.ast.IFunctionDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.wst.jsdt.internal.compiler.CompilationResult;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.wst.jsdt.internal.compiler.flow.ExceptionHandlingFlowContext;
import org.eclipse.wst.jsdt.internal.compiler.flow.FlowContext;
import org.eclipse.wst.jsdt.internal.compiler.flow.FlowInfo;
import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ExtraCompilerModifiers;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Scope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TagBits;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.wst.jsdt.internal.compiler.parser.Parser;
import org.eclipse.wst.jsdt.internal.compiler.problem.AbortMethod;
import org.eclipse.wst.jsdt.internal.compiler.problem.ProblemSeverities;

public class MethodDeclaration extends AbstractMethodDeclaration implements IFunctionDeclaration {

	public TypeReference returnType;
	public TypeParameter[] typeParameters;

	/**
	 * FunctionDeclaration constructor comment.
	 */
	public MethodDeclaration(CompilationResult compilationResult) {
		super(compilationResult);
	}

	public FlowInfo analyseCode(
		Scope classScope,
		FlowContext initializationContext,
		FlowInfo flowInfo) {

		// starting of the code analysis for methods
		if (ignoreFurtherInvestigation)
			return flowInfo;
		try {
			if (binding == null)
				return flowInfo;

			if (!this.binding.isUsed() &&
					(this.binding.isPrivate()
						|| (((this.binding.modifiers & (ExtraCompilerModifiers.AccOverriding|ExtraCompilerModifiers.AccImplementing)) == 0) && this.binding.declaringClass.isLocalType()))) {
				if (!classScope.referenceCompilationUnit().compilationResult.hasSyntaxError) {
					scope.problemReporter().unusedPrivateMethod(this);
				}
			}

//			// skip enum implicit methods
//			if (binding.declaringClass.isEnum() && (this.selector == TypeConstants.VALUES || this.selector == TypeConstants.VALUEOF))
//				return flowInfo;

			// may be in a non necessary <clinit> for innerclass with static final constant fields
			if (binding.isAbstract() || binding.isNative())
				return flowInfo;

			ExceptionHandlingFlowContext methodContext =
				new ExceptionHandlingFlowContext(
					initializationContext,
					this,
					binding.thrownExceptions,
					scope,
					FlowInfo.DEAD_END);

			// tag parameters as being set
			if (this.arguments != null) {
				for (int i = 0, count = this.arguments.length; i < count; i++) {
					flowInfo.markAsDefinitelyAssigned(this.arguments[i].getBinding());
				}
			}
			// propagate to statements
			if (statements != null) {
				boolean didAlreadyComplain = false;
				for (int i = 0, count = statements.length; i < count; i++) {
					Statement stat = statements[i];
					if (!stat.complainIfUnreachable(flowInfo, scope, didAlreadyComplain)) {
						flowInfo = stat.analyseCode(scope, methodContext, flowInfo);
					} else {
						didAlreadyComplain = true;
					}
				}
			}
			// check for missing returning path
			TypeBinding returnTypeBinding = binding.returnType;
			boolean isJsDocInferredReturn = (binding.tagBits&TagBits.IsInferredJsDocType)!=0;
			if ((returnTypeBinding == TypeBinding.VOID) || isAbstract()) {
				this.needFreeReturn =
					(flowInfo.tagBits & FlowInfo.UNREACHABLE) == 0;
			} else {
				if (flowInfo != FlowInfo.DEAD_END) {
					if ((this.inferredMethod==null || !this.inferredMethod.isConstructor) &&
						!isJsDocInferredReturn)
						scope.problemReporter().shouldReturn(returnTypeBinding, this);
				}
			}
			this.scope.reportUnusedDeclarations();
			// check unreachable catch blocks
			if (JavaScriptCore.IS_ECMASCRIPT4)
				methodContext.complainIfUnusedExceptionHandlers(this);
		} catch (AbortMethod e) {
			this.ignoreFurtherInvestigation = true;
		}
		return flowInfo;
	}

	public boolean isMethod() {

		return true;
	}

	public void parseStatements(Parser parser, CompilationUnitDeclaration unit) {

		//fill up the method body with statement
		if (ignoreFurtherInvestigation)
			return;
		parser.parse(this, unit);
	}

	public StringBuffer printReturnType(int indent, StringBuffer output) {

		if (returnType == null) return output;
		return returnType.printExpression(0, output).append(' ');
	}

	public void resolveStatements() {

		// ========= abort on fatal error =============
		if (this.returnType != null && this.binding != null) {
			this.returnType.resolvedType = this.binding.returnType;
			// record the return type binding
		}
//		// check if method with constructor name
//		if (CharOperation.equals(this.scope.enclosingSourceType().sourceName, selector)) {
//			this.scope.problemReporter().methodWithConstructorName(this);
//		}

		if (this.typeParameters != null) {
			for (int i = 0, length = this.typeParameters.length; i < length; i++) {
				this.typeParameters[i].resolve(this.scope);
			}
		}

		// check @Override annotation
		final CompilerOptions compilerOptions = this.scope.compilerOptions();
		checkOverride: {
			if (this.binding == null) break checkOverride;
			long sourceLevel = compilerOptions.sourceLevel;
			if (sourceLevel < ClassFileConstants.JDK1_5) break checkOverride;
			int bindingModifiers = this.binding.modifiers;
			boolean hasOverrideAnnotation = (this.binding.tagBits & TagBits.AnnotationOverride) != 0;
			boolean isInterfaceMethod = this.binding.declaringClass.isInterface();
			if (hasOverrideAnnotation) {
				// no static method is considered overriding
				if (!isInterfaceMethod && (bindingModifiers & (ClassFileConstants.AccStatic|ExtraCompilerModifiers.AccOverriding)) == ExtraCompilerModifiers.AccOverriding)
					break checkOverride;
				//	in 1.5, strictly for overriding superclass method
				//	in 1.6 and above, also tolerate implementing interface method
				if (sourceLevel >= ClassFileConstants.JDK1_6
						&& ((bindingModifiers & (ClassFileConstants.AccStatic|ExtraCompilerModifiers.AccImplementing)) == ExtraCompilerModifiers.AccImplementing))
					break checkOverride;
				// claims to override, and doesn't actually do so
				this.scope.problemReporter().methodMustOverride(this);
			} else if (!isInterfaceMethod
						&& (bindingModifiers & (ClassFileConstants.AccStatic|ExtraCompilerModifiers.AccOverriding)) == ExtraCompilerModifiers.AccOverriding) {
				// actually overrides, but did not claim to do so
				this.scope.problemReporter().missingOverrideAnnotation(this);
			}
		}

		// by grammatical construction, interface methods are always abstract
//		switch (TypeDeclaration.kind(this.scope.referenceType().modifiers)) {
//			case TypeDeclaration.ENUM_DECL :
//				if (this.selector == TypeConstants.VALUES) break;
//				if (this.selector == TypeConstants.VALUEOF) break;
//			case TypeDeclaration.CLASS_DECL :
//				// if a method has an semicolon body and is not declared as abstract==>error
//				// native methods may have a semicolon body
//				if ((this.modifiers & ExtraCompilerModifiers.AccSemicolonBody) != 0) {
//					if ((this.modifiers & ClassFileConstants.AccNative) == 0)
//						if ((this.modifiers & ClassFileConstants.AccAbstract) == 0)
//							this.scope.problemReporter().methodNeedBody(this);
//				} else {
//					// the method HAS a body --> abstract native modifiers are forbiden
//					if (((this.modifiers & ClassFileConstants.AccNative) != 0) || ((this.modifiers & ClassFileConstants.AccAbstract) != 0))
//						this.scope.problemReporter().methodNeedingNoBody(this);
//				}
//		}
		super.resolveStatements();

		// TagBits.OverridingMethodWithSupercall is set during the resolveStatements() call
		if (compilerOptions.getSeverity(CompilerOptions.OverridingMethodWithoutSuperInvocation) != ProblemSeverities.Ignore) {
			if (this.binding != null) {
        		int bindingModifiers = this.binding.modifiers;
        		if ((bindingModifiers & (ExtraCompilerModifiers.AccOverriding|ExtraCompilerModifiers.AccImplementing)) == ExtraCompilerModifiers.AccOverriding
        				&& (this.bits & ASTNode.OverridingMethodWithSupercall) == 0) {
        			this.scope.problemReporter().overridesMethodWithoutSuperInvocation(this.binding);
        		}
			}
		}
	}

	public void traverse(
		ASTVisitor visitor,
		 Scope classScope) {

		if (visitor.visit(this, classScope)) {
			if (this.javadoc != null) {
				this.javadoc.traverse(visitor, scope);
			}
			if (this.annotations != null) {
				int annotationsLength = this.annotations.length;
				for (int i = 0; i < annotationsLength; i++)
					this.annotations[i].traverse(visitor, scope);
			}
			if (this.typeParameters != null) {
				int typeParametersLength = this.typeParameters.length;
				for (int i = 0; i < typeParametersLength; i++) {
					this.typeParameters[i].traverse(visitor, scope);
				}
			}
			if (returnType != null)
				returnType.traverse(visitor, scope);
			if (arguments != null) {
				int argumentLength = arguments.length;
				for (int i = 0; i < argumentLength; i++)
					arguments[i].traverse(visitor, scope);
			}
			if (thrownExceptions != null) {
				int thrownExceptionsLength = thrownExceptions.length;
				for (int i = 0; i < thrownExceptionsLength; i++)
					thrownExceptions[i].traverse(visitor, scope);
			}
			if (statements != null) {
				int statementsLength = statements.length;
				for (int i = 0; i < statementsLength; i++)
					statements[i].traverse(visitor, scope);
			}
		}
		visitor.endVisit(this, classScope);
	}
	public void traverse(
			ASTVisitor visitor,
			BlockScope blockScope) {

			if (visitor.visit(this, blockScope)) {
				if (this.annotations != null) {
					int annotationsLength = this.annotations.length;
					for (int i = 0; i < annotationsLength; i++)
						this.annotations[i].traverse(visitor, scope);
				}
				if (this.typeParameters != null) {
					int typeParametersLength = this.typeParameters.length;
					for (int i = 0; i < typeParametersLength; i++) {
						this.typeParameters[i].traverse(visitor, scope);
					}
				}
				if (returnType != null)
					returnType.traverse(visitor, scope);
				if (arguments != null) {
					int argumentLength = arguments.length;
					for (int i = 0; i < argumentLength; i++)
						arguments[i].traverse(visitor, scope);
				}
				if (thrownExceptions != null) {
					int thrownExceptionsLength = thrownExceptions.length;
					for (int i = 0; i < thrownExceptionsLength; i++)
						thrownExceptions[i].traverse(visitor, scope);
				}
				if (statements != null) {
					int statementsLength = statements.length;
					for (int i = 0; i < statementsLength; i++)
						statements[i].traverse(visitor, scope);
				}
			}
			visitor.endVisit(this, blockScope);
		}


	public TypeParameter[] typeParameters() {
	    return this.typeParameters;
	}
	public int getASTType() {
		return IASTNode.FUNCTION_DECLARATION;
	
	}
}
