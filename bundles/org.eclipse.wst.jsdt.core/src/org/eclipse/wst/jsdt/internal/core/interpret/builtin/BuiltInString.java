package org.eclipse.wst.jsdt.internal.core.interpret.builtin;

import org.eclipse.wst.jsdt.internal.core.interpret.InterpreterContext;
import org.eclipse.wst.jsdt.internal.core.interpret.InterpreterEngine;
import org.eclipse.wst.jsdt.internal.core.interpret.NativeFunction;
import org.eclipse.wst.jsdt.internal.core.interpret.NumberValue;
import org.eclipse.wst.jsdt.internal.core.interpret.ObjectValue;
import org.eclipse.wst.jsdt.internal.core.interpret.Value;

public class BuiltInString {

	
	public static ObjectValue prototype;

	public static void initializeContext(InterpreterContext context) {

		prototype = new ObjectValue(BuiltInObject.prototype);
		
		prototype.setValue("indexOf".toCharArray(), //$NON-NLS-1$
				new NativeFunction()	{

			public Value execute(InterpreterEngine interpreter,ObjectValue receiver,  Value[] arguments) {
				String str=receiver.stringValue();
				String searchStr="";
				int fromIndex=0;
				if (arguments.length>0)
					searchStr=arguments[0].stringValue();
				if (arguments.length>1)
					fromIndex=arguments[1].numberValue();
				int index = str.indexOf(searchStr, fromIndex);
				return new NumberValue(index);
			}
			
		});

		
		NativeFunction constructor = 	new NativeFunction()	{

			public Value execute(InterpreterEngine interpreter,ObjectValue receiver,  Value[] arguments) {
			
				return Value.UndefinedObjectValue;
			}
		};
		constructor.prototype=prototype;
		
		context.setValue(new char[]{'S','t','r','i','n','g'},constructor);


		
	}
}
