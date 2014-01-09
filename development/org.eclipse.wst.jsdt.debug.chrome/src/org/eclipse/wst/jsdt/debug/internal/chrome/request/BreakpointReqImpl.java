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
package org.eclipse.wst.jsdt.debug.internal.chrome.request;

import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.BreakpointRequest;
import org.eclipse.wst.jsdt.debug.internal.chrome.jsdi.VMImpl;

/**
 * {@link BreakpointRequest} impl
 * 
 * @since 1.0
 */
public class BreakpointReqImpl extends EventReqImpl implements BreakpointRequest {

	private Location location = null;
	private ThreadReference thread = null;
	private int hitcount = 0;
	private String condition = null;
	
	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param location 
	 * @param enabled
	 */
	public BreakpointReqImpl(VMImpl vm, Location location, boolean enabled) {
		super(vm, enabled);
		this.location = location;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Locatable#location()
	 */
	public Location location() {
		return location;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.BreakpointRequest#addThreadFilter(org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference)
	 */
	public void addThreadFilter(ThreadReference thread) {
		this.thread = thread;
	}

	/**
	 * Returns the underlying {@link ThreadReference} this request applies to
	 * 
	 * @return the underlying {@link ThreadReference}
	 */
	public synchronized ThreadReference thread() {
		return this.thread;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.BreakpointRequest#addConditionFilter(java.lang.String)
	 */
	public void addConditionFilter(String condition) {
		this.condition = condition;
	}

	/**
	 * Returns the condition for this breakpoint
	 * 
	 * @return the condition
	 */
	public synchronized String condition() {
		return condition;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.BreakpointRequest#addHitCountFilter(int)
	 */
	public void addHitCountFilter(int hitcount) {
		this.hitcount = hitcount;
	}
	
	/**
	 * Returns the hit count for the breakpoint
	 * 
	 * @return the hit count for the breakpoint
	 */
	public synchronized int hitcount() {
		return this.hitcount;
	}
}
