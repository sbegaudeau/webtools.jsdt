package org.eclipse.wst.jsdt.internal.infer;

import org.eclipse.wst.jsdt.internal.compiler.ast.ASTNode;
import org.eclipse.wst.jsdt.internal.compiler.lookup.FieldBinding;

public class InferredAttribute  extends ASTNode{

	public char [] name;
	public InferredType type;
	public FieldBinding binding;
	public boolean isStatic;
	public int nameStart;
	public int initializationStart=-1;
	
	public InferredAttribute(char [] name, InferredType type, int start, int end)
	{
		this.name=name;
		this.type=type;
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
	
	public boolean isInferred()
	{
		return true;
	}

}
