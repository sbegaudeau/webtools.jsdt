/**
 * 
 */
package org.eclipse.wst.jsdt.core.ast;

import org.eclipse.wst.jsdt.internal.compiler.lookup.MethodScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;



/**
 * @author childsb
 *
 */
public interface IArgument extends ILocalDeclaration{

	public char[] getComment();
	public abstract void bind(MethodScope scope, TypeBinding typeBinding, boolean used);
	public abstract StringBuffer print(int indent, StringBuffer output);
}