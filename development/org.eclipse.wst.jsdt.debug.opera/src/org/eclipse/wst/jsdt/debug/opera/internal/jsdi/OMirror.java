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

import org.eclipse.wst.jsdt.debug.core.jsdi.Mirror;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;

/**
 * Default {@link Mirror} object for Opera
 * 
 * @since 0.1
 */
public class OMirror implements Mirror {

	private OVirtualMachine vm = null;
	
	/**
	 * Constructor
	 * <br><br>
	 * This constructor is only to be used when the {@link Mirror} is in fact the {@link VirtualMachine} object
	 * 
	 * @noreference This constructor is not intended to be referenced by clients.
	 */
	public OMirror() {
		vm = (OVirtualMachine) this;
	}
	
	/**
	 * Constructor
	 */
	public OMirror(OVirtualMachine vm) {
		this.vm = vm;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Mirror#virtualMachine()
	 */
	public VirtualMachine virtualMachine() {
		return this.vm;
	}
}
