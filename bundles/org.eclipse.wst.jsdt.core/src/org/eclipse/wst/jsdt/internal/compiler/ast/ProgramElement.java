package org.eclipse.wst.jsdt.internal.compiler.ast;

import org.eclipse.wst.jsdt.core.ast.IASTNode;
import org.eclipse.wst.jsdt.core.ast.IProgramElement;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Scope;

public abstract class ProgramElement extends ASTNode implements IProgramElement {

	public abstract StringBuffer printStatement(int indent, StringBuffer output);

	public void resolve(BlockScope scope)
	{
		if (this instanceof AbstractMethodDeclaration)
			((AbstractMethodDeclaration)this).resolve((Scope)scope);
		else
			//TODO: implement
			throw new org.eclipse.wst.jsdt.core.UnimplementedException();
	}
	public int getASTType() {
		return IASTNode.PROGRAM_ELEMENT;
	
	}

}
