/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core;

import org.eclipse.wst.jsdt.internal.compiler.env.ISourceMethod;

/**
 * Element info for IFunction elements.
 */
public abstract class SourceMethodElementInfo extends MemberElementInfo implements ISourceMethod {

	/**
	 * For a source method (that is, a method contained in a compilation unit)
	 * this is a collection of the names of the parameters for this method,
	 * in the order the parameters are delcared. For a binary method (that is,
	 * a method declared in a binary type), these names are invented as
	 * "arg"i where i starts at 1. This is an empty array if this method
	 * has no parameters.
	 */
	protected char[][] argumentNames;

	/**
	 * A collection of type names of the exceptions this
	 * method throws, or an empty collection if this method
	 * does not declare to throw any exceptions. A name is a simple
	 * name or a qualified, dot separated name.
	 * For example, Hashtable or java.util.Hashtable.
	 */
	protected char[][] exceptionTypes;


public char[][] getArgumentNames() {
	return this.argumentNames;
}
public char[][] getExceptionTypeNames() {
	return this.exceptionTypes;
}
public abstract char[] getReturnTypeName();

public abstract boolean isConstructor();
protected void setArgumentNames(char[][] names) {
	this.argumentNames = names;
}
protected void setExceptionTypeNames(char[][] types) {
	this.exceptionTypes = types;
}
protected abstract void setReturnType(char[] type);
}
