package org.eclipse.wst.jsdt.internal.compiler.ast;

import org.eclipse.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;

public class ObjectLiteralField extends Expression {

	public Expression fieldName;
	public Expression initializer;
	public Javadoc  javaDoc;
	
	public ObjectLiteralField(Expression field, Expression value, int start, int end) {
		
		this.fieldName=field;
		this.initializer=value;
		this.sourceEnd=end;
		this.sourceStart=start;
	}
	public StringBuffer printExpression(int indent, StringBuffer output) {
		if (this.javaDoc!=null)
			this.javaDoc.print(indent, output);
		fieldName.printExpression(indent, output);
		output.append(" : ");
		initializer.printExpression(indent, output) ;
		return output;
	}

	public void traverse(ASTVisitor visitor, BlockScope scope) {
		if (visitor.visit(this, scope)) {
			
			if (javaDoc!=null)
				javaDoc.traverse(visitor, scope);
			if (fieldName!=null)
				fieldName.traverse(visitor, scope);
			if (initializer!=null)
				initializer.traverse(visitor, scope);
		}
		visitor.endVisit(this, scope);
	}


	public TypeBinding resolveType(BlockScope scope) {
		return initializer.resolveType(scope);
	}

}
