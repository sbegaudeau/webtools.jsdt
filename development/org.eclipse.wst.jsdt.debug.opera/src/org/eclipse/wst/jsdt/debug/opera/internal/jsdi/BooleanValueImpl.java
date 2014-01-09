/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.opera.internal.jsdi;

import org.eclipse.wst.jsdt.debug.core.jsdi.BooleanValue;

/**
 * Default {@link BooleanValue} Opera implementation
 * 
 * @since 0.1
 */
public class BooleanValueImpl extends PrimitiveValueImpl implements BooleanValue {

	private boolean value = false;
	
	/**
	 * Constructor
	 * @param vm
	 * @param value
	 */
	public BooleanValueImpl(VirtualMachineImpl vm, boolean value) {
		super(vm);
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.PrimitiveValue#intValue()
	 */
	public int intValue() {
		return (value ? 1 : 0);
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
