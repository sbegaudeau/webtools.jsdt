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

import org.eclipse.wst.jsdt.debug.core.jsdi.NumberValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;

/**
 * Default implementation of {@link NumberValue} for Chrome
 * 
 * @since 1.0
 */
public class NumberImpl extends MirrorImpl implements NumberValue {

	private Number number = null;
	
	/**
	 * Constructor
	 * 
	 * @param vm the underlying {@link VirtualMachine}
	 * @param number the number
	 */
	public NumberImpl(VirtualMachine vm, Number number) {
		super(vm);
		if(number == null) {
			throw new IllegalArgumentException(Messages.cannot_mirror_null_number);
		}
		this.number = number;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.PrimitiveValue#intValue()
	 */
	public int intValue() {
		return number.intValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.PrimitiveValue#doubleValue()
	 */
	public double doubleValue() {
		return number.doubleValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.PrimitiveValue#booleanValue()
	 */
	public boolean booleanValue() {
		if(number.intValue() < 1) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.PrimitiveValue#stringValue()
	 */
	public String stringValue() {
		return number.toString();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Value#valueString()
	 */
	public String valueString() {
		return stringValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.NumberValue#value()
	 */
	public Number value() {
		return number;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.NumberValue#isNaN()
	 */
	public boolean isNaN() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return valueString();
	}
}
