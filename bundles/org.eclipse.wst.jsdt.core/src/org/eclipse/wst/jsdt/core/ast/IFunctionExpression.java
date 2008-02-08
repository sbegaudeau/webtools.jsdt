/**
 * 
 */
package org.eclipse.wst.jsdt.core.ast;

import org.eclipse.wst.jsdt.internal.compiler.ast.MethodDeclaration;



/**
 * @author childsb
 *
 */
public interface IFunctionExpression extends IExpression{

	public void setMethodDeclaration(MethodDeclaration methodDeclaration);
	public MethodDeclaration getMethodDeclaration();
	
}