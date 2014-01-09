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
package org.eclipse.wst.jsdt.debug.internal.crossfire.request;

import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;

/**
 * Abstract notion of an event that knows about a thread
 * 
 * @since 1.0
 */
public class CFThreadEventRequest extends CFEventRequest {

	private ThreadReference thread = null;
	
	/**
	 * Constructor
	 * @param vm
	 */
	public CFThreadEventRequest(VirtualMachine vm) {
		super(vm);
	}
	
	/**
	 * Sets the backing {@link ThreadReference} for the request
	 * @param thread
	 */
	public void setThread(ThreadReference thread) {
		this.thread = thread;
	}
	
	/**
	 * Returns the underlying {@link ThreadReference} this request is filtered to.
	 * @return the thread filter
	 */
	public ThreadReference thread() {
		return this.thread;
	}
}
