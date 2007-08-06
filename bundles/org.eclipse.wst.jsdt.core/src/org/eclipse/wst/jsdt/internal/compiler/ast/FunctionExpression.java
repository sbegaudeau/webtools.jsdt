package org.eclipse.wst.jsdt.internal.compiler.ast;

import org.eclipse.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.wst.jsdt.internal.compiler.flow.FlowContext;
import org.eclipse.wst.jsdt.internal.compiler.flow.FlowInfo;
import org.eclipse.wst.jsdt.internal.compiler.impl.Constant;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.MethodScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Scope;
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
		if (visitor.visit(this, scope))
			methodDeclaration.traverse(visitor, scope);
	}
	

	public TypeBinding resolveType(BlockScope scope) {
		constant = Constant.NotAConstant;
		this.methodDeclaration.scope=new MethodScope(scope,this.methodDeclaration,false);
		this.methodDeclaration.resolve(scope);
		return scope.getJavaLangFunction();
	}
	
	public int nullStatus(FlowInfo flowInfo) {
			return FlowInfo.NON_NULL; // constant expression cannot be null
	}
	
	public FlowInfo analyseCode(
			BlockScope classScope,
			FlowContext initializationContext,
			FlowInfo flowInfo) {
		return this.methodDeclaration.analyseCode(classScope, initializationContext, flowInfo);
	}
	
}
