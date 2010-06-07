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
package org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi;

import org.eclipse.wst.jsdt.debug.core.jsdi.StringValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;

/**
 * Default implementation of {@link StringValue} for Crossfire
 * 
 * @since 1.0
 */
public class CFStringValue extends CFMirror implements StringValue {

	/**
	 * The "type" of this value
	 */
	public static final String STRING = "string"; //$NON-NLS-1$
	
	private String string = null;
	
	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param string
	 */
	public CFStringValue(VirtualMachine vm, String string) {
		super(vm);
		this.string = string;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Value#valueString()
	 */
	public String valueString() {
		return string;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.StringValue#value()
	 */
	public String value() {
		return string;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return string;
	}
}
