/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
import org.eclipse.wst.jsdt.core.ast.IAbstractFunctionDeclaration;
import org.eclipse.wst.jsdt.core.ast.IArgument;
import org.eclipse.wst.jsdt.core.ast.IJsDoc;
import org.eclipse.wst.jsdt.core.ast.IProgramElement;
import org.eclipse.wst.jsdt.core.compiler.CategorizedProblem;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.core.infer.InferredMethod;
import org.eclipse.wst.jsdt.core.infer.InferredType;
import org.eclipse.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.wst.jsdt.internal.compiler.CompilationResult;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.wst.jsdt.internal.compiler.flow.FlowContext;
import org.eclipse.wst.jsdt.internal.compiler.flow.FlowInfo;
import org.eclipse.wst.jsdt.internal.compiler.impl.ReferenceContext;

import org.eclipse.wst.jsdt.internal.compiler.lookup.AnnotationBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Binding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.CompilationUnitBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ExtraCompilerModifiers;
import org.eclipse.wst.jsdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.MethodScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Scope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TagBits;
import org.eclipse.wst.jsdt.internal.compiler.parser.Parser;
import org.eclipse.wst.jsdt.internal.compiler.problem.AbortCompilation;
import org.eclipse.wst.jsdt.internal.compiler.problem.AbortCompilationUnit;
import org.eclipse.wst.jsdt.internal.compiler.problem.AbortMethod;
import org.eclipse.wst.jsdt.internal.compiler.problem.AbortType;
import org.eclipse.wst.jsdt.internal.compiler.problem.ProblemSeverities;


public abstract class AbstractMethodDeclaration
	extends Statement
	implements IAbstractFunctionDeclaration,  ProblemSeverities, ReferenceContext {

	public MethodScope scope;
	//it is not relevent for constructor but it helps to have the name of the constructor here
	//which is always the name of the class.....parsing do extra work to fill it up while it do not have to....
	public char[] selector;
	public int declarationSourceStart;
	public int declarationSourceEnd;
	public int modifiers;
	public int modifiersSourceStart;
	public Annotation[] annotations;
	public Argument[] arguments;
	public TypeReference[] thrownExceptions;
	public Statement[] statements;
	public int explicitDeclarations;
	public MethodBinding binding;
	public boolean ignoreFurtherInvestigation = false;
	public boolean needFreeReturn = false;

	public Javadoc javadoc;

	public int bodyStart;
	public int bodyEnd = -1;
	public CompilationResult compilationResult;

	public InferredType inferredType;
	public InferredMethod inferredMethod;

	public boolean errorInSignature = false;
	public int exprStackPtr;

	AbstractMethodDeclaration(CompilationResult compilationResult){
		this.compilationResult = compilationResult;
	}

	public void setArguments( IArgument[] args) {
		if(args instanceof Argument[]) this.arguments = (Argument[])args;
	}
	
	public IArgument[] getArguments() {
		return this.arguments;
	}
	
	/*
	 *	We cause the compilation task to abort to a given extent.
	 */
	public void abort(int abortLevel, CategorizedProblem problem) {

		switch (abortLevel) {
			case AbortCompilation :
				throw new AbortCompilation(this.compilationResult, problem);
			case AbortCompilationUnit :
				throw new AbortCompilationUnit(this.compilationResult, problem);
			case AbortType :
				throw new AbortType(this.compilationResult, problem);
			default :
				throw new AbortMethod(this.compilationResult, problem);
		}
	}

	public FlowInfo analyseCode(BlockScope classScope, FlowContext initializationContext, FlowInfo info)
	{
	 return this.analyseCode((Scope)classScope, initializationContext, info);
	}
	public abstract FlowInfo analyseCode(Scope classScope, FlowContext initializationContext, FlowInfo info);

		/**
	 * Bind and add argument's binding into the scope of the method
	 */
	public void bindArguments() {

		if (this.arguments != null) {
			// by default arguments in abstract/native methods are considered to be used (no complaint is expected)
			if (this.binding == null) {
				for (int i = 0, length = this.arguments.length; i < length; i++) {
					this.arguments[i].bind(this.scope, null, true);
				}
				return;
			}
			if (this.arguments.length>0 && this.binding.parameters.length==0)  // types not set yet
			{
				ReferenceBinding declaringClass = this.binding.declaringClass;
				if (declaringClass instanceof CompilationUnitBinding) {
					CompilationUnitBinding compilationUnitBinding = (CompilationUnitBinding) declaringClass;
					compilationUnitBinding.resolveTypesFor(this.binding,this);
				}
			}
			boolean used = this.binding.isAbstract() || this.binding.isNative();
			AnnotationBinding[][] paramAnnotations = null;
			for (int i = 0, length = this.arguments.length; i < length && i < this.binding.parameters.length; i++) {
				IArgument argument = this.arguments[i];
				argument.bind(this.scope, this.binding.parameters[i], used);
//				if (argument.getAnnotation() != null) {
//					this.binding.tagBits |= TagBits.HasParameterAnnotations;
//					if (paramAnnotations == null)
//						paramAnnotations = new AnnotationBinding[length][];
//					paramAnnotations[i] = argument.getBinding().getAnnotations();
//				}
			}
			if (paramAnnotations != null)
				this.binding.setParameterAnnotations(paramAnnotations);
		}
	}

	/**
	 * Record the thrown exception type bindings in the corresponding type references.
	 */
	public void bindThrownExceptions() {

		if (this.thrownExceptions != null
			&& this.binding != null
			&& this.binding.thrownExceptions != null) {
			int thrownExceptionLength = this.thrownExceptions.length;
			int length = this.binding.thrownExceptions.length;
			if (length == thrownExceptionLength) {
				for (int i = 0; i < length; i++) {
					this.thrownExceptions[i].resolvedType = this.binding.thrownExceptions[i];
				}
			} else {
				int bindingIndex = 0;
				for (int i = 0; i < thrownExceptionLength && bindingIndex < length; i++) {
					TypeReference thrownException = this.thrownExceptions[i];
					ReferenceBinding thrownExceptionBinding = this.binding.thrownExceptions[bindingIndex];
					char[][] bindingCompoundName = thrownExceptionBinding.compoundName;
					if (bindingCompoundName == null) continue; // skip problem case
					if (thrownException instanceof SingleTypeReference) {
						// single type reference
						int lengthName = bindingCompoundName.length;
						char[] thrownExceptionTypeName = thrownException.getTypeName()[0];
						if (CharOperation.equals(thrownExceptionTypeName, bindingCompoundName[lengthName - 1])) {
							thrownException.resolvedType = thrownExceptionBinding;
							bindingIndex++;
						}
					} else {
						// qualified type reference
						if (CharOperation.equals(thrownException.getTypeName(), bindingCompoundName)) {
							thrownException.resolvedType = thrownExceptionBinding;
							bindingIndex++;
						}
					}
				}
			}
		}
	}

	public CompilationResult compilationResult() {

		return this.compilationResult;
	}



	public boolean hasErrors() {
		return this.ignoreFurtherInvestigation;
	}

	public boolean isAbstract() {

		if (this.binding != null)
			return this.binding.isAbstract();
		return (this.modifiers & ClassFileConstants.AccAbstract) != 0;
	}

	public boolean isAnnotationMethod() {

		return false;
	}

	public boolean isClinit() {

		return false;
	}

	public boolean isConstructor() {

		return false;
	}

	public boolean isDefaultConstructor() {

		return false;
	}

	public boolean isInitializationMethod() {

		return false;
	}

	public boolean isMethod() {

		return false;
	}

	public boolean isNative() {

		if (this.binding != null)
			return this.binding.isNative();
		return (this.modifiers & ClassFileConstants.AccNative) != 0;
	}

	public boolean isStatic() {

		if (this.binding != null)
			return this.binding.isStatic();
		return (this.modifiers & ClassFileConstants.AccStatic) != 0;
	}

	/**
	 * Fill up the method body with statement
	 * @param parser
	 * @param unit
	 */
	public abstract void parseStatements(
		Parser parser,
		CompilationUnitDeclaration unit);

	public StringBuffer printStatement(int indent, StringBuffer output)
	{
		return print(indent,output);
	}

	public StringBuffer print(int tab, StringBuffer output) {

		if (this.javadoc != null) {
			this.javadoc.print(tab, output);
		}
		printIndent(tab, output);
		printModifiers(this.modifiers, output);
//		if (this.annotations != null) printAnnotations(this.annotations, output);

//		TypeParameter[] typeParams = typeParameters();
//		if (typeParams != null) {
//			output.append('<');
//			int max = typeParams.length - 1;
//			for (int j = 0; j < max; j++) {
//				typeParams[j].print(0, output);
//				output.append(", ");//$NON-NLS-1$
//			}
//			typeParams[max].print(0, output);
//			output.append('>');
//		}
		output.append("function "); //$NON-NLS-1$
		if (this.selector!=null)
			output.append(this.selector);
		output.append('(');
		if (this.arguments != null) {
			for (int i = 0; i < this.arguments.length; i++) {
				if (i > 0) output.append(", "); //$NON-NLS-1$
				this.arguments[i].print(0, output);
			}
		}
		output.append(')');
//		if (this.thrownExceptions != null) {
//			output.append(" throws "); //$NON-NLS-1$
//			for (int i = 0; i < this.thrownExceptions.length; i++) {
//				if (i > 0) output.append(", "); //$NON-NLS-1$
//				this.thrownExceptions[i].print(0, output);
//			}
//		}
		printBody(tab + 1, output);
		return output;
	}

	public StringBuffer printBody(int indent, StringBuffer output) {

		if (isAbstract() || (this.modifiers & ExtraCompilerModifiers.AccSemicolonBody) != 0)
			return output.append(';');

		output.append(" {"); //$NON-NLS-1$
		if (this.statements != null) {
			for (int i = 0; i < this.statements.length; i++) {
				output.append('\n');
				this.statements[i].printStatement(indent, output);
			}
		}
		output.append('\n');
		printIndent(indent == 0 ? 0 : indent - 1, output).append('}');
		return output;
	}

	public StringBuffer printReturnType(int indent, StringBuffer output) {

		return output;
	}

	public void resolve(Scope upperScope) {


		if (this.scope==null )
		{
			this.scope = new MethodScope(upperScope,this, false);
			if (this.selector!=null) {
				SourceTypeBinding compilationUnitBinding = upperScope
						.enclosingCompilationUnit();
				MethodBinding methodBinding = scope.createMethod(this,
						this.selector, compilationUnitBinding, false, true);
				if (methodBinding != null) {
					this.binding = methodBinding;
					methodBinding = compilationUnitBinding
							.resolveTypesFor(methodBinding,this);
					if (methodBinding != null) {
						MethodScope enclosingMethodScope = upperScope
								.enclosingMethodScope();
						if (enclosingMethodScope != null)
							enclosingMethodScope.addLocalMethod(methodBinding);
						else {
							compilationUnitBinding.addMethod(methodBinding);
							upperScope.environment().defaultPackage.addBinding(
									methodBinding, methodBinding.selector,
									Binding.METHOD);
						}
					}
				}
			}

		}

		if (this.binding == null) {


			this.ignoreFurtherInvestigation = true;
		}

		try {
			bindArguments();
			if (JavaScriptCore.IS_ECMASCRIPT4)
				bindThrownExceptions();
			resolveJavadoc();
//			if (JavaScriptCore.IS_ECMASCRIPT4)
//				resolveAnnotations(scope, this.annotations, this.binding);
			resolveStatements();
			// check @Deprecated annotation presence
			if (this.binding != null
					&& (this.binding.getAnnotationTagBits() & TagBits.AnnotationDeprecated) == 0
					&& (this.binding.modifiers & ClassFileConstants.AccDeprecated) != 0
					&& this.scope.compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5) {
				this.scope.problemReporter().missingDeprecatedAnnotationForMethod(this);
			}
		} catch (AbortMethod e) {	// ========= abort on fatal error =============
			this.ignoreFurtherInvestigation = true;
		}
	}

	public void resolveJavadoc() {

		if (this.binding == null) return;
		if (this.javadoc != null) {
			this.javadoc.resolve(this.scope);
			return;
		}
		if (this.binding.declaringClass != null && !this.binding.declaringClass.isLocalType()) {
			this.scope.problemReporter().javadocMissing(this.sourceStart, this.sourceEnd, this.binding.modifiers);
		}
	}

	public void resolveStatements() {

		if (this.statements != null) {
			for (int i = 0, length = this.statements.length; i < length; i++) {
				this.statements[i].resolve(this.scope);
			}
		} else if ((this.bits & UndocumentedEmptyBlock) != 0) {
			this.scope.problemReporter().undocumentedEmptyBlock(this.bodyStart-1, this.bodyEnd+1);
		}
	}

	public void tagAsHavingErrors() {

		this.ignoreFurtherInvestigation = true;
	}

	public void traverse(
		ASTVisitor visitor,
		Scope classScope) {
		// default implementation: subclass will define it
	}

	public TypeParameter[] typeParameters() {
	    return null;
	}




	public void resolve(BlockScope scope) {
		this.resolve((Scope)scope);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.compiler.ast.ASTNode#isInferred()
	 */
	public boolean isInferred() {
		return this.inferredMethod!=null;
	}
	public int getASTType() {
		return IASTNode.ABSTRACT_FUNCTION_DECLARATION;
	
	}
	
 
	public IJsDoc getJsDoc()
	{
		return this.javadoc;
	}

	public IProgramElement[] getStatements()
	{
		return this.statements;
	}

	public char[] getName()
	{
		return this.selector;
	}

	public void setInferredType(InferredType type)
	{
		this.inferredType=type;
	}

	public InferredMethod getInferredMethod()
	{
		return this.inferredMethod;
	}

	public InferredType getInferredType()
	{
		return this.inferredType;
	}
	
	public char [] getSafeName()
	{
		if (this.selector!=null)
			return this.selector;
		if (this.inferredMethod!=null && this.inferredMethod.name!=null)
			return this.inferredMethod.name;
		return new char []{};
			
	}
}
