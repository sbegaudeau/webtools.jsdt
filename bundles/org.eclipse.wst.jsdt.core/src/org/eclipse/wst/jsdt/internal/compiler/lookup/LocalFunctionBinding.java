package org.eclipse.wst.jsdt.internal.compiler.lookup;

public class LocalFunctionBinding extends MethodBinding {
	final static char[] LocalFunctionPrefix = { '$', 'L', 'o', 'c', 'a', 'l', 'f', 'u', 'n', 'c', '$' };


	public LocalFunctionBinding(int modifiers, char[] selector,
			TypeBinding returnType, TypeBinding[] parameters,
			ReferenceBinding[] thrownExceptions, ReferenceBinding declaringClass) {
		super(modifiers, selector, returnType, parameters, thrownExceptions,
				declaringClass);
	}



}
