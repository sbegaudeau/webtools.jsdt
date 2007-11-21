package org.eclipse.wst.jsdt.internal.core.interpret;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.jsdt.internal.compiler.util.HashtableOfObject;
import org.eclipse.wst.jsdt.internal.core.interpret.builtin.BuiltInHelper;

public class InterpreterContext implements ValueReference{

	protected HashtableOfObject values=new HashtableOfObject();
	InterpreterContext parent;
	InterpreterContext lastReference;
	public Value returnValue;
	ValueReference thisObject;
	public int returnCode;
	
	public InterpreterContext() {
		this(null,null);
	}
	
	public InterpreterContext(InterpreterContext parent,ObjectValue thisObject) {
		this.parent=parent;
		this.thisObject= (thisObject!=null) ? thisObject : (ValueReference)globalContext();
		
	}
	
	
	
	public Value getValue(char [] name) {
		
		InterpreterContext checkContext=this;
		do {
			this.lastReference=checkContext;
			Value value=(Value) checkContext.values.get(name);
			if (value!=null)
			{
				return value;
			}
			checkContext=checkContext.parent;
		} while (checkContext!=null);
		return Value.UndefinedObjectValue;
	}

	public void setValue(char [] name, Value value) {
		values.put(name,  value);
		
	}

	public ValueReference getReference(char [] name) {
		if (parent==null || values.containsKey(name))
			return this;
		return parent.getReference(name);
	}

	public void initailizeBuiltins() {
		BuiltInHelper.initializeBuiltinObjects(this);
		
	}

	public InterpreterContext globalContext()
	{
		InterpreterContext checkContext=this;
		while (checkContext.parent!=null)
			checkContext=checkContext.parent;
		return checkContext;
		
	}
	
	public List getContextStack()
	{
		ArrayList list=new ArrayList();
		InterpreterContext currentContext=this;
		while (currentContext!=null)
		{
			list.add(0, currentContext);
			currentContext=currentContext.parent;
		}
		return list;
	}

	public ValueReference getThisObject() {
		return thisObject;
	}
}
