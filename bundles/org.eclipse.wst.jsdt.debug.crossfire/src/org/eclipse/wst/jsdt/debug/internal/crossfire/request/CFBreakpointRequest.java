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

import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.BreakpointRequest;

/**
 * Default implementation of {@link BreakpointRequest} for Crossfire
 * 
 * @since 1.0
 */
public class CFBreakpointRequest extends CFThreadEventRequest implements BreakpointRequest {

	private String condition = null;
	private int hitcount = 0;
	private Location location = null;
	
	/**
	 * Constructor
	 * @param vm
	 * @param location
	 */
	public CFBreakpointRequest(VirtualMachine vm, Location location) {
		super(vm);
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
		setThread(thread);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.BreakpointRequest#addConditionFilter(java.lang.String)
	 */
	public void addConditionFilter(String condition) {
		this.condition = condition;
	}

	/**
	 * Returns the {@link String} condition set for this request using {@link #addConditionFilter(String)} or <code>null</code>
	 * 
	 * @return the condition filter or <code>null</code>
	 */
	public String getConditionFilter() {
		return condition;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.BreakpointRequest#addHitCountFilter(int)
	 */
	public void addHitCountFilter(int hitcount) {
		this.hitcount = hitcount;
	}

	/**
	 * Returns the hit count set using {@link #addHitCountFilter(int)} or <code>0</code>
	 * 
	 * @return the specified hit count or <code>0</code>
	 */
	public int getHitCount() {
		return hitcount;
	}
}
