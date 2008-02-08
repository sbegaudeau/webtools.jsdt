/**
 * 
 */
package org.eclipse.wst.jsdt.core.ast;


/**
 * @author childsb
 *
 */
public interface IFunctionCall extends IExpression{

	public IExpression getReciever();
	public char[] getSelector();
	public IExpression[] getArguments();
	
}