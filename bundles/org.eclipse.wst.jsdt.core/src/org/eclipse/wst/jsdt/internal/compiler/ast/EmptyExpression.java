package org.eclipse.wst.jsdt.internal.compiler.ast;

import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;

public class EmptyExpression extends Expression {

	
	
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

}
