package org.eclipse.wst.jsdt.core.infer;

import org.eclipse.wst.jsdt.core.ast.IFunctionDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.lookup.MethodBinding;

public class InferredMethod extends InferredMember{

	private MethodDeclaration methodDeclaration;

	public boolean isConstructor;
	public MethodBinding methodBinding;
	public InferredMethod(char [] name, IFunctionDeclaration functionDeclaration, InferredType inType )
	{
		this.methodDeclaration=(MethodDeclaration)functionDeclaration;
		this.name=name;
		this.inType = inType;
		this.sourceStart=methodDeclaration.sourceStart;
		this.sourceEnd=methodDeclaration.sourceEnd;
	}

	public IFunctionDeclaration getFunctionDeclaration()
	{
		return methodDeclaration;
	}
	
	public StringBuffer print(int indent, StringBuffer output)
	{
		String modifier=(isStatic)? "static ":""; //$NON-NLS-1$ //$NON-NLS-2$
		printIndent(indent, output).append(modifier);
		if (!isConstructor)
		{
		 if (methodDeclaration.inferredType!=null)
			 methodDeclaration.inferredType.dumpReference(output);
		else
			output.append("??"); //$NON-NLS-1$
		output.append(" "); //$NON-NLS-1$
		}

		output.append(name).append("("); //$NON-NLS-1$
		   if (methodDeclaration.arguments!=null)
			   for (int i = 0; i < methodDeclaration.arguments.length; i++) {
				   if (i>0)
					   output.append(", "); //$NON-NLS-1$
				  InferredType argumentType = methodDeclaration.arguments[i].inferredType;
				  if (argumentType!=null )
				  {
					  output.append(argumentType.name).append(" "); //$NON-NLS-1$
				  }
				   output.append(methodDeclaration.arguments[i].name);
			   }
		   output.append(")"); //$NON-NLS-1$

		   return output;
	}
}
