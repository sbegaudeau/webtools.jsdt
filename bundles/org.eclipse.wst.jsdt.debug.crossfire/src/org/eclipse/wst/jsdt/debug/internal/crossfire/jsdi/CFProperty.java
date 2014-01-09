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

import org.eclipse.wst.jsdt.debug.core.jsdi.NullValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.Property;
import org.eclipse.wst.jsdt.debug.core.jsdi.Value;

/**
 * Default implementation of {@link Property} for Crossfire
 * 
 * @since 1.0
 */
public class CFProperty extends CFMirror implements Property {

	private String name = null;
	private Number ref = null;
	private CFStackFrame frame = null;
	private Value value = null;
	
	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param frame
	 * @param name
	 * @param ref
	 */
	public CFProperty(CFVirtualMachine vm, CFStackFrame frame, String name, Number ref) {
		super(vm);
		this.frame = frame;
		this.name = name;
		this.ref = ref;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Property#name()
	 */
	public String name() {
		return this.name;
	}
	
	/**
	 * Allows the value to be set. Passing in <code>null</code> for <code>newvalue</code> will result
	 * in {@link #value} being set to {@link NullValue}
	 * 
	 * @param newvalue
	 */
	protected void setValue(Value newvalue) {
		if(newvalue == null) {
			this.value = crossfire().mirrorOfNull();
		}
		else {
			this.value = newvalue;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Property#value()
	 */
	public synchronized Value value() {
		if(this.value == null) {
			this.value = frame.lookup(this.ref);
			if(this.value == null) {
				this.value = crossfire().mirrorOfUndefined();
			}
		}
		if(this.value == null) {
			return crossfire().mirrorOfUndefined();
		}
		return this.value;
	}
}
