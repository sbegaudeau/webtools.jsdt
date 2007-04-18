package org.eclipse.wst.jsdt.internal.compiler.lookup;

public class IndirectMethodBinding extends MethodBinding {
	TypeBinding receiverType;
	
	public IndirectMethodBinding(int modifiers, TypeBinding receiverType, TypeBinding[] parameters,ReferenceBinding declaringClass) 
	{
		super(modifiers,null,TypeBinding.ANY,parameters,ReferenceBinding.NO_EXCEPTIONS,declaringClass);
		this.receiverType=receiverType;
	}
	
}
