/**
 * 
 */
package org.eclipse.wst.jsdt.core.ast;

import org.eclipse.wst.jsdt.internal.compiler.lookup.InvocationSite;
import org.eclipse.wst.jsdt.internal.compiler.lookup.LocalVariableBinding;



/**
 * @author childsb
 *
 */
public interface ILocalDeclaration extends InvocationSite, IAbstractVariableDeclaration {
	public void setBinding(LocalVariableBinding binding);
	public LocalVariableBinding getBinding();
}