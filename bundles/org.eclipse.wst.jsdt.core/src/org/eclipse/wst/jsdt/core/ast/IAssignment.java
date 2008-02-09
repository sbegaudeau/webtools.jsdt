/**
 * 
 */
package org.eclipse.wst.jsdt.core.ast;





/**
 * @author childsb
 *
 */
public interface IAssignment extends IExpression {

	IExpression getExpression();

	IExpression getLeftHandSide();

}