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

import org.eclipse.wst.jsdt.debug.core.jsdi.StringValue;

/**
 * Default {@link StringValue} implementation for Opera
 * 
 *  @since 0.1
 */
public class StringValueImpl extends MirrorImpl implements StringValue {

	String value = null;
	
	/**
	 * Constructor
	 */
	public StringValueImpl(VirtualMachineImpl vm, String value) {
		super(vm);
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Value#valueString()
	 */
	public String valueString() {
		return value;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.StringValue#value()
	 */
	public String value() {
		return value;
	}
}
