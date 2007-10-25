package org.eclipse.wst.jsdt.internal.core.interpret;

public class NumberValue extends Value{

	int intValue;
	
	public NumberValue(int value)
	{
		super(NUMBER);
		this.intValue=value;
	}


	public  int numberValue()
	{
		return intValue;
	}
	public  String stringValue() 
	{
		return String.valueOf(intValue);
	}
	
	public  boolean booleanValue() { return (intValue==0) ?  false:true;}
	

}
