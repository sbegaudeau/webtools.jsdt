/**
 * 
 */
package org.eclipse.wst.jsdt.core.ast;


/**
 * @author childsb
 *
 */
public interface IAbstractFunctionDeclaration extends IStatement{

	
	public void setArguments( IArgument[] args);

	public IArgument[] getArguments();
}