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
package org.eclipse.wst.jsdt.debug.internal.rhino.jsdi;

import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.BooleanValue;
import org.eclipse.wst.jsdt.debug.rhino.transport.JSONConstants;

/**
 * Rhino implementation of {@link BooleanValue}
 * 
 * @since 1.0
 */
public class BooleanValueImpl extends MirrorImpl implements BooleanValue {

	private Boolean value;

	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param body
	 */
	public BooleanValueImpl(VirtualMachineImpl vm, Map body) {
		this(vm, (Boolean) body.get(JSONConstants.VALUE));
	}

	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param bool
	 */
	public BooleanValueImpl(VirtualMachineImpl vm, Boolean bool) {
		super(vm);
		this.value = bool;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.BooleanValue#value()
	 */
	public boolean value() {
		return value.booleanValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Value#getValueTypeName()
	 */
	public String getValueTypeName() {
		return JSONConstants.BOOLEAN;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Value#valueString()
	 */
	public String valueString() {
		return value.toString();
	}
}
