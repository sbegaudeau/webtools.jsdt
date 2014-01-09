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
package org.eclipse.wst.jsdt.debug.internal.chrome.jsdi;

import org.eclipse.wst.jsdt.debug.core.jsdi.UndefinedValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;

/**
 * Default implementation of {@link UndefinedValue} for Chrome.
 * 
 * @since 1.0
 */
public class UndefinedImpl extends MirrorImpl implements UndefinedValue {

	/**
	 * Textual representation of undefined<br><br>
	 * value is: <code>undefined</code>
	 */
	public static final String UNDEFINED = "undefined"; //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param vm the underlying {@link VirtualMachine}
	 */
	public UndefinedImpl(VirtualMachine vm) {
		super(vm);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Value#valueString()
	 */
	public String valueString() {
		return UNDEFINED;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return valueString();
	}	
}
