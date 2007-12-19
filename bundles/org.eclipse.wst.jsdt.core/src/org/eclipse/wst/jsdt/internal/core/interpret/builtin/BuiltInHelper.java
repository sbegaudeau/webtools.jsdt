package org.eclipse.wst.jsdt.internal.core.interpret.builtin;

import org.eclipse.wst.jsdt.internal.core.interpret.InterpreterContext;

public  class BuiltInHelper {

	public static void initializeBuiltinObjects(InterpreterContext context)
	{
		BuiltInObject.initializeContext(context);
		BuiltInString.initializeContext(context);
	}
}
