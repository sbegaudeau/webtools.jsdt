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

import org.eclipse.wst.jsdt.debug.core.jsdi.Mirror;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;

/**
 * Default impl for Chrome v8.
 * 
 * @since 1.0
 */
public class MirrorImpl implements Mirror {

	/**
	 * Flag for tracing
	 */
	static boolean TRACE = false;
	
	private VirtualMachine vm = null;
	
	/**
	 * Constructor
	 * 
	 * @param vm the underlying {@link VirtualMachine}
	 */
	public MirrorImpl(VirtualMachine vm) {
		this.vm = vm;
	}
	
	/**
	 * Constructor
	 * 
	 * Used for the {@link VMImpl} instantiation case
	 */
	protected MirrorImpl() {
		this.vm = (VirtualMachine) this;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Mirror#virtualMachine()
	 */
	public VirtualMachine virtualMachine() {
		return vm;
	}

	/**
	 * Returns the {@link VMImpl} backing this {@link Mirror} object
	 * 
	 * @return the backing {@link VMImpl}
	 */
	protected VMImpl chromeVM() {
		return (VMImpl)vm;
	}
	
	/**
	 * Re-throws the given exception as a {@link RuntimeException} with the given message
	 * @param message
	 * @param t
	 */
	protected void handleException(String message, Throwable t) {
		throw new RuntimeException(message, t);
	}
	
	/**
	 * Enables / Disables tracing in the all of the JSDI implementations
	 * 
	 * @param trace
	 */
	public static void setTracing(boolean trace) {
		TRACE = trace;
	}
}
