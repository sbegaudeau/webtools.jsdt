/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.core.jsdi;

/**
 * Abstract representation of a value.
 * 
 * @see Mirror
 * @since 1.0
 * @noextend This interface is not intended to be extended by clients.
 */
public interface Value extends Mirror {

	/**
	 * Returns the name of the type of this value. <br>
	 * <br>
	 * For example the name <code>function</code> could be the type name to describe a {@link FunctionReference}.<br>
	 * <br>
	 * This method cannot return <code>null</code>
	 * 
	 * @return the name of the type of this {@link Value} never <code>null</code>
	 */
	public String getValueTypeName();

	/**
	 * Returns the string representation of the value.<br>
	 * <br>
	 * This method can return <code>null</code>
	 * 
	 * @return the string representation of the value or <code>null</code>
	 */
	public String valueString();
}
