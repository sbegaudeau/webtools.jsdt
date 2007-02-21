package org.eclipse.wst.jsdt.internal.compiler.ast;


public class ObjectLiteral extends Expression {

	public ObjectLiteralField [] fields;
	
	public StringBuffer printExpression(int indent, StringBuffer output) {
		if (fields==null || fields.length==0)
		{
			output.append("{}");
		}
		else
		{
			output.append("{\n");
			printIndent(indent+1, output);
			for (int i = 0; i < fields.length; i++) {
				if (i>0)
				{
					output.append(",\n");
					printIndent(indent+1, output);
				}
				fields[i].printExpression(indent, output);
			}
			output.append("\n");
			printIndent(indent, output);
			output.append("}");
		}
		return output;
	}

}
