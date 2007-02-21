package org.eclipse.wst.jsdt.internal.infer;

import org.eclipse.wst.jsdt.internal.compiler.ast.Expression;
import org.eclipse.wst.jsdt.internal.compiler.ast.MessageSend;
import org.eclipse.wst.jsdt.internal.compiler.ast.StringLiteral;

public class DojoInferEngine extends InferEngine {

	public DojoInferEngine() {
		super();
	}

	protected boolean handleFunctionCall(MessageSend messageSend) {
		if (isFunction(messageSend,"dojo.provide"))
		{
			char [] className=null;
			Expression expression =messageSend.arguments[0];
			if (expression instanceof StringLiteral) {
				StringLiteral strLit = (StringLiteral) expression;
				className=strLit.source();
			}
			if (className!=null)
			{
				InferredType type = addType(className);
				type.isDefinition=true;
			}
			return false;
		}
		return true;
	}


}
