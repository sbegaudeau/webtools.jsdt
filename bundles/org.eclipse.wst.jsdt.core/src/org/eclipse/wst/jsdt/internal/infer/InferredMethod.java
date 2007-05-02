package org.eclipse.wst.jsdt.internal.infer;

import org.eclipse.wst.jsdt.internal.compiler.ast.ASTNode;
import org.eclipse.wst.jsdt.internal.compiler.ast.Argument;
import org.eclipse.wst.jsdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.lookup.MethodBinding;

public class InferredMethod extends ASTNode{
  
	public MethodDeclaration methodDeclaration;
	public char [] name;
	public InferredType inferredType;
	public boolean isConstructor;
	public MethodBinding methodBinding;
	public int nameStart;

	public InferredMethod(char [] name, MethodDeclaration methodDeclaration, InferredType inClass)
	{
		this.methodDeclaration=methodDeclaration;
		this.name=name;
		inferredType=inClass;
		this.sourceStart=methodDeclaration.sourceStart;
		this.sourceEnd=methodDeclaration.sourceEnd;
	}
	
	
	public StringBuffer print(int indent, StringBuffer output)  
	{
		printIndent(indent, output);
		if (!isConstructor)
		{
		 if (methodDeclaration.inferredType!=null)
			 methodDeclaration.inferredType.dumpReference(output);
		else
			output.append("??");
		output.append(" ");
		}
			
		output.append(name).append("(");	
		   if (methodDeclaration.arguments!=null)
			   for (int i = 0; i < methodDeclaration.arguments.length; i++) {
				   if (i>0)
					   output.append(", ");
				  InferredType argumentType = methodDeclaration.arguments[i].inferredType;
				  if (argumentType!=null )
				  {
					  output.append(argumentType.name).append(" ");
				  }
				   output.append(methodDeclaration.arguments[i].name);
			   }
		   output.append(")");	

		   return output;
	}
	
	public boolean isInferred()
	{
		return true;
	}

}
