package org.eclipse.wst.jsdt.internal.core.interpret;

public class BooleanValue extends Value {

	boolean value;
	public BooleanValue(boolean value) {
		super(BOOLEAN);
		this.value=value;
	}
	public boolean booleanValue() {
		return value;
	}
	public int numberValue() {
		return value? 1 : 0;
	}
	public String stringValue() {
		return value?"true":"false";
	}

}
