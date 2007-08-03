package org.eclipse.wst.jsdt.internal.compiler.ast;

import org.eclipse.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.wst.jsdt.internal.compiler.flow.FlowInfo;
import org.eclipse.wst.jsdt.internal.compiler.impl.Constant;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;


public class ObjectLiteral extends Expression {

	public ObjectLiteralField [] fields;
	
	public StringBuffer printExpression(int indent, StringBuffer output) {
		if (fields==null || fields.length==0)
		{
			output.append("{}");
		}
		else
		{
			output.append("{\n");
			printIndent(indent+1, output);
			for (int i = 0; i < fields.length; i++) {
				if (i>0)
				{
					output.append(",\n");
					printIndent(indent+1, output);
				}
				fields[i].printExpression(indent, output);
			}
			output.append("\n");
			printIndent(indent, output);
			output.append("}");
		}
		return output;
	}
	public void traverse(ASTVisitor visitor, BlockScope scope) {
		if (visitor.visit(this, scope)) {
			if (fields!=null)
				for (int i = 0; i < fields.length; i++) {
					fields[i].traverse(visitor, scope);
				} 
		}
		visitor.endVisit(this, scope);
	}
	

	public TypeBinding resolveType(BlockScope scope) {
		if (this.fields!=null)
			for (int i = 0; i < this.fields.length; i++) {
				this.fields[i].resolveType(scope);
			}
		return null;
	}
	
	public int nullStatus(FlowInfo flowInfo) {
			return FlowInfo.NON_NULL; // constant expression cannot be null
	}
}
