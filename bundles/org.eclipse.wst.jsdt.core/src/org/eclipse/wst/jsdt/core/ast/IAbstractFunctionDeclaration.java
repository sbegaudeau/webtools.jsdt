/**
 * 
 */
package org.eclipse.wst.jsdt.core.ast;

import org.eclipse.wst.jsdt.core.infer.InferredMethod;
import org.eclipse.wst.jsdt.core.infer.InferredType;



/**
 * @author childsb
 *
 */
public interface IAbstractFunctionDeclaration extends IStatement{

	
	public void setArguments( IArgument[] args);

	public IArgument[] getArguments();
	IJsDoc getJsDoc();

	IProgramElement[] getStatements();

	char[] getName();

	void setInferredType(InferredType type);

	InferredMethod getInferredMethod();

	InferredType getInferredType();

}