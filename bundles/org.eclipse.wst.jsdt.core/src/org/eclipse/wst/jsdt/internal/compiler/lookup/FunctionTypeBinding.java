package org.eclipse.wst.jsdt.internal.compiler.lookup;

import org.eclipse.wst.jsdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.wst.jsdt.internal.infer.InferredType;

public class FunctionTypeBinding extends SourceTypeBinding {


	public MethodBinding functionBinding;
	SourceTypeBinding functionTypeBinding;
	
	
	public FunctionTypeBinding(MethodBinding function, Scope scope) {
		super(TypeConstants.FUNCTION, null, scope);

		this.functionTypeBinding=(SourceTypeBinding)scope.getJavaLangFunction();
		this.functionBinding=function;
		this.fPackage=this.functionTypeBinding.fPackage;
	
		this.compoundName=this.functionTypeBinding.compoundName;
		this.sourceName=this.functionTypeBinding.sourceName;
		this.modifiers=this.functionTypeBinding.modifiers;
		this.fileName=this.functionTypeBinding.fileName;
		this.constantPoolName=this.functionTypeBinding.constantPoolName;
		this.signature=this.functionTypeBinding.signature;
		this.tagBits=this.functionTypeBinding.tagBits;
		this.id=this.functionTypeBinding.id;

	}


	public void addMethod(MethodBinding binding) {
		functionTypeBinding.addMethod(binding);
	}


	public void cleanup() {
		functionTypeBinding.cleanup();
	}


	public FieldBinding[] fields() {
		return functionTypeBinding.fields();
	}


	public char[] genericSignature() {
		return functionTypeBinding.genericSignature();
	}


	public char[] genericTypeSignature() {
		return functionTypeBinding.genericTypeSignature();
	}


	public MethodBinding getExactConstructor(TypeBinding[] argumentTypes) {
		return functionTypeBinding.getExactConstructor(argumentTypes);
	}


	public MethodBinding getExactMethod(char[] selector, TypeBinding[] argumentTypes, CompilationUnitScope refScope) {
		return functionTypeBinding.getExactMethod(selector, argumentTypes, refScope);
	}


	public FieldBinding getField(char[] fieldName, boolean needResolve) {
		return functionTypeBinding.getField(fieldName, needResolve);
	}


	public InferredType getInferredType() {
		return functionTypeBinding.getInferredType();
	}


	public MethodBinding[] getMethods(char[] selector) {
		return functionTypeBinding.getMethods(selector);
	}


	public boolean hasMemberTypes() {
		return functionTypeBinding.hasMemberTypes();
	}


	public boolean isEquivalentTo(TypeBinding otherType) {
		return functionTypeBinding.isEquivalentTo(otherType);
	}


	public int kind() {
		return functionTypeBinding.kind();
	}


	public ReferenceBinding[] memberTypes() {
		return functionTypeBinding.memberTypes();
	}


	public MethodBinding[] methods() {
		return functionTypeBinding.methods();
	}


	public void setFields(FieldBinding[] fields) {
		functionTypeBinding.setFields(fields);
	}


	public void setMethods(MethodBinding[] methods) {
		functionTypeBinding.setMethods(methods);
	}


	public AbstractMethodDeclaration sourceMethod(MethodBinding binding) {
		return functionTypeBinding.sourceMethod(binding);
	}


	public ReferenceBinding superclass() {
		return functionTypeBinding.superclass();
	}


	public ReferenceBinding[] superInterfaces() {
		return functionTypeBinding.superInterfaces();
	}


	public String toString() {
		return functionTypeBinding.toString();
	}


	public TypeVariableBinding[] typeVariables() {
		return functionTypeBinding.typeVariables();
	}


	void verifyMethods(MethodVerifier verifier) {
		functionTypeBinding.verifyMethods(verifier);
	}
	
	
	public boolean isFunctionType()
	{
		return true;
	}
	
	
	
	
}
