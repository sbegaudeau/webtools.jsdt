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

import org.eclipse.wst.jsdt.debug.core.jsdi.Mirror;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;

/**
 * Default implementation for a {@link Mirror} for Crossfire
 * 
 * @since 1.0
 */
public class CFMirror implements Mirror {

	public static boolean TRACE = false;
	
	private VirtualMachine vm = null;
	
	/**
	 * Constructor
	 * For use by the {@link CFVirtualMachine} only
	 * @noreference This constructor is not intended to be referenced by clients.
	 */
	public CFMirror() {
		vm = (VirtualMachine) this;
	}
	
	/**
	 * Constructor
	 */
	public CFMirror(VirtualMachine vm) {
		this.vm = vm;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Mirror#virtualMachine()
	 */
	public VirtualMachine virtualMachine() {
		return this.vm;
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
	 * Returns the handle to the {@link CFVirtualMachine}
	 * 
	 * @return the {@link CFVirtualMachine} handle
	 */
	protected CFVirtualMachine crossfire() {
		return (CFVirtualMachine) vm;
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
