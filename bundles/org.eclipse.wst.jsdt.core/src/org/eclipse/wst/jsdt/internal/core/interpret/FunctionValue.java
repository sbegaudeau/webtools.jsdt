package org.eclipse.wst.jsdt.internal.core.interpret;

import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.internal.compiler.ast.MethodDeclaration;

public class FunctionValue extends ObjectValue {

	MethodDeclaration method;
	public ObjectValue prototype=new ObjectValue();
	
	protected FunctionValue(MethodDeclaration method) {
		super();
		this.type=Value.FUNCTION;
		this.method=method;
	}

	
	public Value getValue(char[] name) {
		if (CharOperation.equals(Contants.PROTOTYPE_ARR, name))
			return prototype;
		return super.getValue(name);
	}


	public void setValue(char[] name, Value value) {
		if (CharOperation.equals(Contants.PROTOTYPE_ARR, name))
		{
			if (value.type!=OBJECT)
				throw new InterpretException("invalid prototype"); //$NON-NLS-1$
			this.prototype=(ObjectValue)value;
		}
		super.setValue(name, value);
	}


	public Value execute(InterpreterEngine interpreter,ObjectValue receiver, Value[] arguments)
	{
		InterpreterContext context = interpreter.context=new InterpreterContext(interpreter.context,receiver);
		MethodDeclaration method=this.method;
		if (method.arguments!=null)
		  for (int i=0;i<method.arguments.length;i++)
		{
			Value value=(i<arguments.length) ? arguments[i]:Value.NullObjectValue;
			if (i<arguments.length)
				context.setValue(method.arguments[i].name, value);
		} 
		//TODO: crate "arguments" Array here
		interpreter.execBlock(method.statements);
		Value returnValue=context.returnValue;
		if (returnValue==null)
			returnValue=Value.UndefinedObjectValue;
		interpreter.context=context.parent;
		return returnValue;
	}
}
