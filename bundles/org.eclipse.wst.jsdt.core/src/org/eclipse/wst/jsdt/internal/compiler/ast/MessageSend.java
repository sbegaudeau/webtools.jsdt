/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Nick Teryaev - fix for bug (https://bugs.eclipse.org/bugs/show_bug.cgi?id=40752)
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.compiler.ast;

import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.ast.IASTNode;
import org.eclipse.wst.jsdt.core.ast.IExpression;
import org.eclipse.wst.jsdt.core.ast.IFunctionCall;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.wst.jsdt.internal.compiler.flow.FlowContext;
import org.eclipse.wst.jsdt.internal.compiler.flow.FlowInfo;
import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.wst.jsdt.internal.compiler.impl.Constant;
import org.eclipse.wst.jsdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Binding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ExtraCompilerModifiers;
import org.eclipse.wst.jsdt.internal.compiler.lookup.IndirectMethodBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.InvocationSite;
import org.eclipse.wst.jsdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ProblemMethodBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.wst.jsdt.internal.compiler.lookup.RawTypeBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Scope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.wst.jsdt.internal.compiler.problem.ProblemSeverities;

public class MessageSend extends Expression implements InvocationSite, IFunctionCall {

	public Expression receiver;
	public char[] selector;
	public Expression[] arguments;
	public MethodBinding binding;							// exact binding resulting from lookup
//	protected FunctionBinding codegenBinding;		// actual binding used for code generation (if no synthetic accessor)
//	FunctionBinding syntheticAccessor;						// synthetic accessor for inner-emulation
	public TypeBinding expectedType;					// for generic method invocation (return type inference)

	public long nameSourcePosition ; //(start<<32)+end

	public TypeBinding actualReceiverType;
//	public TypeBinding receiverGenericCast; // extra reference type cast to perform on generic receiver
//	public TypeBinding valueCast; // extra reference type cast to perform on method returned value
	public TypeReference[] typeArguments;
	public TypeBinding[] genericTypeArguments;

	
	public char[] getSelector() {
		return this.selector;
	}
	
	public IExpression getReciever() {
		return this.receiver;
	}
	
	public IExpression[] getArguments() {
		return this.arguments;
	}
	
	
public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {

	boolean nonStatic = binding==null || !binding.isStatic();
	if (receiver!=null)
	{
		flowInfo = receiver.analyseCode(currentScope, flowContext, flowInfo, nonStatic).unconditionalInits();
		if (nonStatic) {
			receiver.checkNPE(currentScope, flowContext, flowInfo);
		}
	}

	if (arguments != null) {
		int length = arguments.length;
		for (int i = 0; i < length; i++) {
			flowInfo = arguments[i].analyseCode(currentScope, flowContext, flowInfo).unconditionalInits();
		}
	}
	ReferenceBinding[] thrownExceptions;
	if (JavaScriptCore.IS_ECMASCRIPT4)
	{
		if ((thrownExceptions = binding.thrownExceptions) != Binding.NO_EXCEPTIONS) {
		// must verify that exceptions potentially thrown by this expression are caught in the method
			flowContext.checkExceptionHandlers(thrownExceptions, this, flowInfo.copy(), currentScope);
		// TODO (maxime) the copy above is needed because of a side effect into
		//               checkExceptionHandlers; consider protecting there instead of here;
		//               NullReferenceTest#test0510
		}
	}
//	manageSyntheticAccessIfNecessary(currentScope, flowInfo);
	return flowInfo;
}
/**
 * @see org.eclipse.wst.jsdt.internal.compiler.ast.Expression#computeConversion(org.eclipse.wst.jsdt.internal.compiler.lookup.Scope, org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding, org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding)
 */
public void computeConversion(Scope scope, TypeBinding runtimeTimeType, TypeBinding compileTimeType) {
//	if (runtimeTimeType == null || compileTimeType == null)
//		return;
//	// set the generic cast after the fact, once the type expectation is fully known (no need for strict cast)
//	if (this.binding != null && this.binding.isValidBinding()) {
//		FunctionBinding originalBinding = this.binding.original();
//		TypeBinding originalType = originalBinding.returnType;
//	    // extra cast needed if method return type is type variable
//		if (originalBinding != this.binding
//				&& originalType != this.binding.returnType
//				&& runtimeTimeType.id != T_JavaLangObject
//				&& (originalType.tagBits & TagBits.HasTypeVariable) != 0) {
//	    	TypeBinding targetType = (!compileTimeType.isBaseType() && runtimeTimeType.isBaseType())
//	    		? compileTimeType  // unboxing: checkcast before conversion
//	    		: runtimeTimeType;
//	        this.valueCast = originalType.genericCast(targetType);
//		} 	else if (this.actualReceiverType!=null && this.actualReceiverType.isArrayType()
//						&& runtimeTimeType.id != T_JavaLangObject
//						&& this.binding.parameters == Binding.NO_PARAMETERS
//						&& scope.compilerOptions().complianceLevel >= ClassFileConstants.JDK1_5
//						&& CharOperation.equals(this.binding.selector, CLONE)) {
//					// from 1.5 compliant mode on, array#clone() resolves to array type, but codegen to #clone()Object - thus require extra inserted cast
//			this.valueCast = runtimeTimeType;
//		}
//	}
//	super.computeConversion(scope, runtimeTimeType, compileTimeType);
}

/**
 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.InvocationSite#genericTypeArguments()
 */
public TypeBinding[] genericTypeArguments() {
	return this.genericTypeArguments;
}

public boolean isSuperAccess() {
	return receiver!=null && receiver.isSuper();
}
public boolean isTypeAccess() {
	return receiver != null && receiver.isTypeReference();
}
public void manageSyntheticAccessIfNecessary(BlockScope currentScope, FlowInfo flowInfo){

	if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) != 0)	return;

	// if method from parameterized type got found, use the original method at codegen time
//	this.codegenBinding = this.binding.original();
	if (this.binding.isPrivate()){

//		// depth is set for both implicit and explicit access (see FunctionBinding#canBeSeenBy)
//		if (currentScope.enclosingSourceType() != this.codegenBinding.declaringClass){
//
//			syntheticAccessor = ((SourceTypeBinding)this.codegenBinding.declaringClass).addSyntheticMethod(this.codegenBinding, isSuperAccess());
//			currentScope.problemReporter().needToEmulateMethodAccess(this.codegenBinding, this);
//			return;
//		}

	} else if (receiver instanceof QualifiedSuperReference){ // qualified super

		// qualified super need emulation always
//		SourceTypeBinding destinationType = (SourceTypeBinding)(((QualifiedSuperReference)receiver).currentCompatibleType);
//		syntheticAccessor = destinationType.addSyntheticMethod(this.codegenBinding, isSuperAccess());
//		currentScope.problemReporter().needToEmulateMethodAccess(this.codegenBinding, this);
		return;

	} else if (binding.isProtected()){

//		SourceTypeBinding enclosingSourceType;
//		if (((bits & DepthMASK) != 0)
//				&& this.codegenBinding.declaringClass.getPackage()
//					!= (enclosingSourceType = currentScope.enclosingSourceType()).getPackage()){
//
//			SourceTypeBinding currentCompatibleType = (SourceTypeBinding)enclosingSourceType.enclosingTypeAt((bits & DepthMASK) >> DepthSHIFT);
//			syntheticAccessor = currentCompatibleType.addSyntheticMethod(this.codegenBinding, isSuperAccess());
//			currentScope.problemReporter().needToEmulateMethodAccess(this.codegenBinding, this);
//			return;
//		}
	}

	// if the binding declaring class is not visible, need special action
	// for runtime compatibility on 1.2 VMs : change the declaring class of the binding
	// NOTE: from target 1.2 on, method's declaring class is touched if any different from receiver type
	// and not from Object or implicit static method call.
//	if (this.binding.declaringClass != this.actualReceiverType
//			&& this.receiverGenericCast == null
//			&& !this.actualReceiverType.isArrayType()) {
//		CompilerOptions options = currentScope.compilerOptions();
//		if ((options.targetJDK >= ClassFileConstants.JDK1_2
//				&& (options.complianceLevel >= ClassFileConstants.JDK1_4 || !(receiver.isImplicitThis() && this.codegenBinding.isStatic()))
//				&& this.binding.declaringClass.id != T_JavaLangObject) // no change for Object methods
//			|| !this.binding.declaringClass.canBeSeenBy(currentScope)) {
//
//			this.codegenBinding = currentScope.enclosingSourceType().getUpdatedMethodBinding(
//			        										this.codegenBinding, (ReferenceBinding) this.actualReceiverType.erasure());
//		}
//		// Post 1.4.0 target, array clone() invocations are qualified with array type
//		// This is handled in array type #clone method binding resolution (see Scope and UpdatedMethodBinding)
//	}
}
public int nullStatus(FlowInfo flowInfo) {
	return FlowInfo.UNKNOWN;
}

/**
 * @see org.eclipse.wst.jsdt.internal.compiler.ast.Expression#postConversionType(Scope)
 */
public TypeBinding postConversionType(Scope scope) {
	TypeBinding convertedType = this.resolvedType;
//	if (this.valueCast != null)
//		convertedType = this.valueCast;
	int runtimeType = (this.implicitConversion & IMPLICIT_CONVERSION_MASK) >> 4;
	switch (runtimeType) {
		case T_boolean :
			convertedType = TypeBinding.BOOLEAN;
			break;
		case T_byte :
			convertedType = TypeBinding.BYTE;
			break;
		case T_short :
			convertedType = TypeBinding.SHORT;
			break;
		case T_char :
			convertedType = TypeBinding.CHAR;
			break;
		case T_int :
			convertedType = TypeBinding.INT;
			break;
		case T_float :
			convertedType = TypeBinding.FLOAT;
			break;
		case T_long :
			convertedType = TypeBinding.LONG;
			break;
		case T_double :
			convertedType = TypeBinding.DOUBLE;
			break;
		default :
	}
	if ((this.implicitConversion & BOXING) != 0) {
		convertedType = scope.environment().computeBoxingType(convertedType);
	}
	return convertedType;
}

public StringBuffer printExpression(int indent, StringBuffer output){

	if (receiver!=null && !receiver.isImplicitThis())
	{
		receiver.printExpression(0, output);
		if (selector!=null)
			output.append('.');
	}
	if (selector!=null)
		output.append(selector);
	output.append('(') ;
	if (arguments != null) {
		for (int i = 0; i < arguments.length ; i ++) {
			if (i > 0) output.append(", "); //$NON-NLS-1$
			arguments[i].printExpression(0, output);
		}
	}
	return output.append(')');
}

public TypeBinding resolveType(BlockScope scope) {
	// Answer the signature return type
	// Base type promotion

	constant = Constant.NotAConstant;
//	boolean receiverCast = false, argsContainCast = false;
//	if (this.receiver instanceof CastExpression) {
//		this.receiver.bits |= DisableUnnecessaryCastCheck; // will check later on
//		receiverCast = true;
//	}
	this.actualReceiverType = (receiver!=null) ?receiver.resolveType(scope):null;
	boolean receiverIsType = (receiver instanceof NameReference || receiver instanceof FieldReference)
		&& ( receiver.bits & Binding.TYPE) != 0;
//	if (receiverCast && this.actualReceiverType != null) {
//		 // due to change of declaring class with receiver type, only identity cast should be notified
//		if (((CastExpression)this.receiver).expression.resolvedType == this.actualReceiverType) {
//			scope.problemReporter().unnecessaryCast((CastExpression)this.receiver);
//		}
//	}
//	// resolve type arguments (for generic constructor call)
//	if (this.typeArguments != null) {
//		int length = this.typeArguments.length;
//		boolean argHasError = false; // typeChecks all arguments
//		this.genericTypeArguments = new TypeBinding[length];
//		for (int i = 0; i < length; i++) {
//			TypeReference typeReference = this.typeArguments[i];
//			if ((this.genericTypeArguments[i] = typeReference.resolveType(scope, true /* check bounds*/)) == null) {
//				argHasError = true;
//			}
//			if (argHasError && typeReference instanceof Wildcard) {
//				scope.problemReporter().illegalUsageOfWildcard(typeReference);
//			}
//		}
//		if (argHasError) {
//			return null;
//		}
//	}
	// will check for null after args are resolved
	TypeBinding[] argumentTypes = Binding.NO_PARAMETERS;
	if (arguments != null) {
		boolean argHasError = false; // typeChecks all arguments
		int length = arguments.length;
		argumentTypes = new TypeBinding[length];
		for (int i = 0; i < length; i++){
			Expression argument = arguments[i];
//			if (argument instanceof CastExpression) {
//				argument.bits |= DisableUnnecessaryCastCheck; // will check later on
//				argsContainCast = true;
//			}
			if ((argumentTypes[i] = argument.resolveType(scope)) == null){
				argHasError = true;
			}
		}
		if (argHasError) {
			if (actualReceiverType instanceof ReferenceBinding) {
				//  record a best guess, for clients who need hint about possible method match
				TypeBinding[] pseudoArgs = new TypeBinding[length];
				for (int i = length; --i >= 0;)
					pseudoArgs[i] = argumentTypes[i] == null ? TypeBinding.NULL  : argumentTypes[i]; // replace args with errors with receiver
				if (selector==null)
					this.binding=new IndirectMethodBinding(0,this.actualReceiverType,argumentTypes,scope.compilationUnitScope().referenceContext.compilationUnitBinding);
				else
				this.binding =
					receiver.isImplicitThis()
						? scope.getImplicitMethod(selector, pseudoArgs, this)
						: scope.findMethod((ReferenceBinding) actualReceiverType, selector, pseudoArgs, this);
				if (binding != null && !binding.isValidBinding()) {
					MethodBinding closestMatch = ((ProblemMethodBinding)binding).closestMatch;
					// record the closest match, for clients who may still need hint about possible method match
					if (closestMatch != null) {
						if (closestMatch.original().typeVariables != Binding.NO_TYPE_VARIABLES) { // generic method
							// shouldn't return generic method outside its context, rather convert it to raw method (175409)
							closestMatch = scope.environment().createParameterizedGenericMethod(closestMatch.original(), (RawTypeBinding)null);
						}
						this.binding = closestMatch;
						MethodBinding closestMatchOriginal = closestMatch.original();
						if ((closestMatchOriginal.isPrivate() || closestMatchOriginal.declaringClass.isLocalType()) && !scope.isDefinedInMethod(closestMatchOriginal)) {
							// ignore cases where method is used from within inside itself (e.g. direct recursions)
							closestMatchOriginal.original().modifiers |= ExtraCompilerModifiers.AccLocallyUsed;
						}
					}
				}
			}
			return null;
		}
	}
//	if (this.actualReceiverType == null) {
//		return null;
//	}
	// base type cannot receive any message
//	if (this.actualReceiverType!=null && this.actualReceiverType.isBaseType()) {
//		scope.problemReporter().errorNoMethodFor(this, this.actualReceiverType, argumentTypes);
//		return null;
//	}
	if (selector==null)
		this.binding=new IndirectMethodBinding(0,this.actualReceiverType,argumentTypes,scope.compilationUnitScope().referenceContext.compilationUnitBinding);
	else
	{
		if (receiver==null || receiver.isImplicitThis())
			this.binding =scope.getImplicitMethod(selector, argumentTypes, this);
		else
		{
			this.binding =scope.getMethod(this.actualReceiverType, selector, argumentTypes, this);
			//  if receiver type was function, try using binding from receiver  
			if (!binding.isValidBinding() && (this.actualReceiverType!=null && this.actualReceiverType.isFunctionType()))
			{
			   Binding alternateBinding = receiver.alternateBinding();
			   if (alternateBinding instanceof TypeBinding)
			   {
				   this.actualReceiverType=(TypeBinding)alternateBinding;
				   this.binding=scope.getMethod(this.actualReceiverType, selector, argumentTypes, this);
				   receiverIsType=true;
			   }
			}
		
		}
		if (argumentTypes.length!=this.binding.parameters.length)
			scope.problemReporter().wrongNumberOfArguments(this, this.binding);
	}

	if (!binding.isValidBinding() && !(this.actualReceiverType==TypeBinding.ANY || this.actualReceiverType==TypeBinding.UNKNOWN)) {
		if (binding.declaringClass == null) {
			if (this.actualReceiverType==null || this.actualReceiverType instanceof ReferenceBinding) {
				binding.declaringClass = (ReferenceBinding) this.actualReceiverType;
			} else {
				scope.problemReporter().errorNoMethodFor(this, this.actualReceiverType, argumentTypes);
				return null;
			}
		}
		scope.problemReporter().invalidMethod(this, binding);
		MethodBinding closestMatch = ((ProblemMethodBinding)binding).closestMatch;
		switch (this.binding.problemId()) {
			case ProblemReasons.Ambiguous :
				break; // no resilience on ambiguous
			case ProblemReasons.NotVisible :
			case ProblemReasons.NonStaticReferenceInConstructorInvocation :
			case ProblemReasons.NonStaticReferenceInStaticContext :
			case ProblemReasons.ReceiverTypeNotVisible :
			case ProblemReasons.ParameterBoundMismatch :
				// only steal returnType in cases listed above
				if (closestMatch != null) this.resolvedType = closestMatch.returnType;
			default :
		}
		// record the closest match, for clients who may still need hint about possible method match
		if (closestMatch != null) {
			this.binding = closestMatch;
			MethodBinding closestMatchOriginal = closestMatch.original();
			if ((closestMatchOriginal.isPrivate() || closestMatchOriginal.declaringClass.isLocalType()) && !scope.isDefinedInMethod(closestMatchOriginal)) {
				// ignore cases where method is used from within inside itself (e.g. direct recursions)
				closestMatchOriginal.original().modifiers |= ExtraCompilerModifiers.AccLocallyUsed;
			}
		}
		return this.resolvedType;
	}
	final CompilerOptions compilerOptions = scope.compilerOptions();
	if (!binding.isStatic()) {
		// the "receiver" must not be a type, in other words, a NameReference that the TC has bound to a Type
		if (receiverIsType && binding.isValidBinding()) {
			scope.problemReporter().mustUseAStaticMethod(this, binding);
			if (this.actualReceiverType.isRawType()
					&& (this.receiver.bits & IgnoreRawTypeCheck) == 0
					&& compilerOptions.getSeverity(CompilerOptions.RawTypeReference) != ProblemSeverities.Ignore) {
				scope.problemReporter().rawTypeReference(this.receiver, this.actualReceiverType);
			}
		} else {
			if (receiver!=null)
				receiver.computeConversion(scope, this.actualReceiverType, this.actualReceiverType);
			// compute generic cast if necessary
//			TypeBinding receiverErasure = this.actualReceiverType.erasure();
//			if (receiverErasure instanceof ReferenceBinding) {
//				if (receiverErasure.findSuperTypeWithSameErasure(this.binding.declaringClass) == null) {
//					this.receiverGenericCast = this.binding.declaringClass; // handle indirect inheritance thru variable secondary bound
//				}
//			}
		}
	} else {
		if (receiver!=null) {
			// static message invoked through receiver? legal but unoptimal (optional warning).
			if (!(receiver.isImplicitThis() || receiver.isSuper() || receiverIsType)) {
				scope.problemReporter().nonStaticAccessToStaticMethod(this,
						binding);
			}
			if (!receiver.isImplicitThis()
					&& binding.declaringClass != actualReceiverType) {
				//			scope.problemReporter().indirectAccessToStaticMethod(this, binding);
			}
		}
	}
//	checkInvocationArguments(scope, this.receiver, actualReceiverType, binding, this.arguments, argumentTypes, argsContainCast, this);

	//-------message send that are known to fail at compile time-----------
	if (binding.isAbstract()) {
		if (receiver.isSuper()) {
			scope.problemReporter().cannotDireclyInvokeAbstractMethod(this, binding);
		}
		// abstract private methods cannot occur nor abstract static............
	}
	if (isMethodUseDeprecated(binding, scope, true))
		scope.problemReporter().deprecatedMethod(binding, this);

	// from 1.5 compliance on, array#clone() returns the array type (but binding still shows Object)
	if (actualReceiverType!=null && actualReceiverType.isArrayType()
			&& this.binding.parameters == Binding.NO_PARAMETERS
			&& compilerOptions.complianceLevel >= ClassFileConstants.JDK1_5
			&& CharOperation.equals(this.binding.selector, CLONE)) {
		this.resolvedType = actualReceiverType;
	} else {
		TypeBinding returnType = this.binding.returnType;
		if (returnType != null)
			returnType = returnType.capture(scope, this.sourceEnd);
		else
			returnType=TypeBinding.UNKNOWN;
		this.resolvedType = returnType;
	}
	if (receiver!=null && receiver.isSuper() && compilerOptions.getSeverity(CompilerOptions.OverridingMethodWithoutSuperInvocation) != ProblemSeverities.Ignore) {
		final ReferenceContext referenceContext = scope.methodScope().referenceContext;
		if (referenceContext instanceof AbstractMethodDeclaration) {
			final AbstractMethodDeclaration abstractMethodDeclaration = (AbstractMethodDeclaration) referenceContext;
			MethodBinding enclosingMethodBinding = abstractMethodDeclaration.binding;
			if (enclosingMethodBinding.isOverriding()
					&& CharOperation.equals(this.binding.selector, enclosingMethodBinding.selector)
					&& this.binding.areParametersEqual(enclosingMethodBinding)) {
				abstractMethodDeclaration.bits |= ASTNode.OverridingMethodWithSupercall;
			}
		}
	}
	return this.resolvedType;
}

public void setActualReceiverType(ReferenceBinding receiverType) {
	if (receiverType == null) return; // error scenario only
	this.actualReceiverType = receiverType;
}
public void setDepth(int depth) {
	bits &= ~DepthMASK; // flush previous depth if any
	if (depth > 0) {
		bits |= (depth & 0xFF) << DepthSHIFT; // encoded on 8 bits
	}
}

/**
 * @see org.eclipse.wst.jsdt.internal.compiler.ast.Expression#setExpectedType(org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding)
 */
public void setExpectedType(TypeBinding expectedType) {
    this.expectedType = expectedType;
}
public void setFieldIndex(int depth) {
	// ignore for here
}

public void traverse(ASTVisitor visitor, BlockScope blockScope) {
	if (visitor.visit(this, blockScope)) {
		if (receiver!=null)
			receiver.traverse(visitor, blockScope);
		if (this.typeArguments != null) {
			for (int i = 0, typeArgumentsLength = this.typeArguments.length; i < typeArgumentsLength; i++) {
				this.typeArguments[i].traverse(visitor, blockScope);
			}
		}
		if (arguments != null) {
			int argumentsLength = arguments.length;
			for (int i = 0; i < argumentsLength; i++)
				arguments[i].traverse(visitor, blockScope);
		}
	}
	visitor.endVisit(this, blockScope);
}
public int getASTType() {
	return IASTNode.FUNCTION_CALL;

}

public IExpression getReceiver() {
	return this.receiver;
}
}
