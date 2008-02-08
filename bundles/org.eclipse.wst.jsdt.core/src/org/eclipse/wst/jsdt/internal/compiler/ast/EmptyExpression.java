package org.eclipse.wst.jsdt.internal.compiler.ast;

import org.eclipse.wst.jsdt.core.ast.IASTNode;
import org.eclipse.wst.jsdt.core.ast.IEmptyExpression;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;

public class EmptyExpression extends Expression implements IEmptyExpression {

	
	
	public EmptyExpression(int startPosition, int endPosition) {
		this.sourceStart = startPosition;
		this.sourceEnd = endPosition;
	}

	
	public StringBuffer printExpression(int indent, StringBuffer output) {
		return output;
	}

	public void resolve(BlockScope scope) {
	}
	
	public TypeBinding resolveType(BlockScope scope) {
		return TypeBinding.ANY;
	}
	public int getASTType() {
		return IASTNode.EMPTY_EXPRESSION;
	
	}
}
