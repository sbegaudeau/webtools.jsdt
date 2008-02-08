/**
 * 
 */
package org.eclipse.wst.jsdt.core.ast;

import org.eclipse.wst.jsdt.core.infer.InferredType;



/**
 * @author childsb
 *
 */
public interface IObjectLiteral {
	public InferredType getInferredType();
	public void setInferredType(InferredType type);
	public IObjectLiteralField[] getFields();
	
}