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
package org.eclipse.wst.jsdt.debug.opera.internal.jsdi.request;

import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.BreakpointRequest;
import org.eclipse.wst.jsdt.debug.opera.internal.jsdi.VirtualMachineImpl;

/**
 * Default {@link BreakpointRequest} implementation for Opera
 * 
 * @since 0.1
 */
public class BreakpointRequestImpl extends LocatableRequestImpl implements BreakpointRequest {

	ThreadReference thread = null;
	String condition = null;
	int hitcount = -1;
	
	/**
	 * Constructor
	 * @param vm
	 * @param location
	 */
	public BreakpointRequestImpl(VirtualMachineImpl vm, Location location) {
		super(vm, location);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.BreakpointRequest#addThreadFilter(org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference)
	 */
	public void addThreadFilter(ThreadReference thread) {
		this.thread = thread;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.BreakpointRequest#addConditionFilter(java.lang.String)
	 */
	public void addConditionFilter(String condition) {
		this.condition = condition;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.BreakpointRequest#addHitCountFilter(int)
	 */
	public void addHitCountFilter(int hitcount) {
		this.hitcount = hitcount;
	}
}
