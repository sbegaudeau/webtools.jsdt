package org.eclipse.wst.jsdt.internal.compiler.ast;

public class ObjectLiteralField extends Expression {

	public Expression fieldName;
	public Expression initializer;
	public ObjectLiteralField(Expression field, Expression value, int start, int end) {
		
		this.fieldName=field;
		this.initializer=value;
		this.sourceEnd=start;
		this.sourceStart=end;
	}
	public StringBuffer printExpression(int indent, StringBuffer output) {
		fieldName.printExpression(indent, output);
		output.append(" : ");
		initializer.printExpression(indent, output) ;
		return output;
	}

}
