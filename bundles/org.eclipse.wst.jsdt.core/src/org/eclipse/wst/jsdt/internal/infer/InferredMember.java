package org.eclipse.wst.jsdt.internal.infer;

import org.eclipse.wst.jsdt.internal.compiler.ast.ASTNode;

public abstract class InferredMember extends ASTNode{

	public char [] name;
	public InferredType inType;
	public int nameStart;
	public boolean isStatic = false;

	public boolean isInferred()
	{
		return true;
	}
}
