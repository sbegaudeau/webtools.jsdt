package org.eclipse.wst.jsdt.internal.core.interpret;

public class InterpreterResult {

	InterpretException exception;
	Object result;
	
	public boolean hasErrors()
	{
		return exception!=null;
	}
	
	public Object getResult()
	{
		return result;
	}
}
