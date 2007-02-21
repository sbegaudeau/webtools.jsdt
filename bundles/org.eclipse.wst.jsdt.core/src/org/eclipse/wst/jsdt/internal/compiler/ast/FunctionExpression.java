package org.eclipse.wst.jsdt.internal.compiler.ast;

import org.eclipse.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;

public class FunctionExpression extends Expression {


	public MethodDeclaration methodDeclaration;
	
	public FunctionExpression(MethodDeclaration methodDeclaration)
	{
		this.methodDeclaration=methodDeclaration;
	}
	
	public StringBuffer printExpression(int indent, StringBuffer output) {
		return methodDeclaration.print(indent, output);
	}

	public void traverse(ASTVisitor visitor, BlockScope scope) {
		methodDeclaration.traverse(visitor, scope);
	}
	

	public TypeBinding resolveType(BlockScope scope) {
		return scope.getJavaLangFunction();
	}
}
