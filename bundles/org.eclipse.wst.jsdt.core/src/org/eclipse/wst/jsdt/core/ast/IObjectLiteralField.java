/**
 * 
 */
package org.eclipse.wst.jsdt.core.ast;



/**
 * @author childsb
 *
 */
public interface IObjectLiteralField extends IExpression{
	public IExpression getFieldName();
	public IExpression getInitializer();
}