package org.eclipse.wst.jsdt.internal.core.interpret;

public interface ValueReference {

	Value getValue(char [] name);
	void setValue(char [] name, Value value);
	
}
