package org.eclipse.wst.jsdt.internal.infer;

import org.eclipse.wst.jsdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.lookup.MethodBinding;

public class InferredMethod extends InferredMember{

	public MethodDeclaration methodDeclaration;

	public boolean isConstructor;
	public MethodBinding methodBinding;
	public InferredMethod(char [] name, MethodDeclaration methodDeclaration, InferredType inType )
	{
		this.methodDeclaration=methodDeclaration;
		this.name=name;
		this.inType = inType;
		this.sourceStart=methodDeclaration.sourceStart;
		this.sourceEnd=methodDeclaration.sourceEnd;
	}


	public StringBuffer print(int indent, StringBuffer output)
	{
		String modifier=(isStatic)? "static ":"";
		printIndent(indent, output).append(modifier);
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
}
