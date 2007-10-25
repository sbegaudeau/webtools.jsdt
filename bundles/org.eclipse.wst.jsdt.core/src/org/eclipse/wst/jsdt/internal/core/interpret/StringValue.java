package org.eclipse.wst.jsdt.internal.core.interpret;

public class StringValue extends Value {

	String stringValue;

	public StringValue(String value) {
		super(Value.STRING);
		this.stringValue=value;
	}


	public boolean booleanValue() {
		return stringValue.length()!=0;
	}

	public int numberValue() {
		return Integer.valueOf(stringValue).intValue();
	}

	public String stringValue() {
		return stringValue;
	}
	
}
