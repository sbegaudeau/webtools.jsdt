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

import org.eclipse.wst.jsdt.debug.core.jsdi.NumberValue;
import org.eclipse.wst.jsdt.debug.core.model.JavaScriptDebugModel;

/**
 * Default {@link NumberValue} implementation for Opera
 * 
 * @since 0.1
 */
public class NumberValueImpl extends PrimitiveValueImpl implements NumberValue {
	
	Number number = null;
	
	/**
	 * Constructor
	 * @param vm
	 * @param number
	 */
	public NumberValueImpl(VirtualMachineImpl vm, Number number) {
		super(vm);
		this.number = number;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.PrimitiveValue#intValue()
	 */
	public int intValue() {
		if(number == null) {
			return -1;
		}
		return number.intValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.PrimitiveValue#doubleValue()
	 */
	public double doubleValue() {
		if(number == null) {
			return -1;
		}
		return number.doubleValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.PrimitiveValue#booleanValue()
	 */
	public boolean booleanValue() {
		if(number == null) {
			return false;
		}
		return number.intValue() > 0 ? true : false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.PrimitiveValue#stringValue()
	 */
	public String stringValue() {
		return JavaScriptDebugModel.numberToString(number);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Value#valueString()
	 */
	public String valueString() {
		return JavaScriptDebugModel.numberToString(number);
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
		return number == null;
	}

}
