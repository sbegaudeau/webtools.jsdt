/*******************************************************************************
 * Copyright (c) 2010, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi;

import org.eclipse.wst.jsdt.debug.core.jsdi.NumberValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;

/**
 * Default implementation of {@link NumberValue} for Crossfire
 * 
 * @since 1.0
 */
public class CFNumberValue extends CFMirror implements NumberValue {
	
	/**
	 * Object representing 'Not a Number'
	 */
	public static final Double NAN_OBJ = new Double(Double.NaN);
	/**
	 * Object representing '-Infinity'
	 */
	public static final Double NEG_INFINITY_OBJ = new Double(Double.NEGATIVE_INFINITY);
	/**
	 * Object representing 'Infinity'
	 */
	public static final Double INFINITY_OBJ = new Double(Double.POSITIVE_INFINITY);
	
	/**
	 * The type "number"
	 */
	public static final String NUMBER = "number"; //$NON-NLS-1$

	private Number number = null;
	
	/**
	 * Constructor
	 * @param vm
	 * @param number
	 */
	public CFNumberValue(VirtualMachine vm, Number number) {
		super(vm);
		if(number != null) {
			this.number = number;
		}
		else {
			this.number = NAN_OBJ;
		}
	}

	/**
	 * Constructor
	 * @param vm the backing {@link VirtualMachine}
	 * @param number the name of the number
	 * @see #INFINITY
	 * @see #NEG_INFINTY
	 */
	public CFNumberValue(VirtualMachine vm, String number) {
		super(vm);
		if(INFINITY.equals(number)) {
			this.number = INFINITY_OBJ;
		}
		else if(NEG_INFINITY.equals(number)) {
			this.number = NEG_INFINITY_OBJ;
		}
		if(this.number == null) {
			this.number = NAN_OBJ;
		}
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
		return number.intValue() > 0 ? true : false;
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
		return Double.isNaN(this.number.doubleValue());
	}
}
