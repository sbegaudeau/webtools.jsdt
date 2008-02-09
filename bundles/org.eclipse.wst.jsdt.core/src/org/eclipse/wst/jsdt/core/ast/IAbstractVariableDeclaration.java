/**
 * 
 */
package org.eclipse.wst.jsdt.core.ast;

import org.eclipse.wst.jsdt.core.infer.InferredType;




/**
 * @author childsb
 *
 */
public interface IAbstractVariableDeclaration extends IStatement{
	public void setInferredType(InferredType type);
	public InferredType getInferredType();
	public IAnnotation[] getAnnotation();
	public char[] getName();
	public IExpression getInitialization();
	public IJsDoc getJsDoc();

}