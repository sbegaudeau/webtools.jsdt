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

import org.eclipse.wst.jsdt.debug.core.jsdi.BooleanValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;

/**
 * Default implementation of {@link BooleanValue} for Chrome
 * 
 * @since 1.0
 */
public class BooleanImpl extends MirrorImpl implements BooleanValue {

	private boolean value = false;
	
	/**
	 * Constructor
	 * 
	 * @param vm the underlying {@link VirtualMachine}
	 * @param bool the boolean value
	 */
	public BooleanImpl(VirtualMachine vm, boolean bool) {
		super(vm);
		value = bool;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.PrimitiveValue#intValue()
	 */
	public int intValue() {
		if(value) {
			return 1;
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.PrimitiveValue#doubleValue()
	 */
	public double doubleValue() {
		return intValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.PrimitiveValue#booleanValue()
	 */
	public boolean booleanValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.PrimitiveValue#stringValue()
	 */
	public String stringValue() {
		return Boolean.toString(value);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Value#valueString()
	 */
	public String valueString() {
		return stringValue();
	}
}
