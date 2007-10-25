package org.eclipse.wst.jsdt.internal.infer;

import org.eclipse.wst.jsdt.internal.compiler.lookup.FieldBinding;

public class InferredAttribute extends InferredMember{

	public FieldBinding binding;
	public int initializationStart=-1;
	public InferredType type;

	public InferredAttribute(char [] name, InferredType inType, int start, int end)
	{
		this.name=name;
		this.inType = inType;
		this.sourceStart=start;
		this.sourceEnd=end;
	}


	public StringBuffer print(int indent, StringBuffer output)
	{
		String modifier=(isStatic)? "static ":"";
		printIndent(indent, output).append(modifier);
	   if (type!=null)
		   type.dumpReference(output);
	   else
		   output.append("??");
	   output.append(" ").append(name);
	   return output;
	}

}
