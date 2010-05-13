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
package org.eclipse.wst.jsdt.debug.internal.crossfire.event;

import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.LocatableEvent;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequest;

/**
 * Default implementation of {@link LocatableEvent} for Crossfire
 * 
 * @sicne 1.0
 */
public class CFLocatableEvent extends CFEvent implements LocatableEvent {

	private ThreadReference thread = null;
	private Location location = null;
	
	/**
	 * Constructor
	 * @param vm
	 * @param request
	 */
	public CFLocatableEvent(VirtualMachine vm, EventRequest request, ThreadReference thread, Location location) {
		super(vm, request);
		this.thread = thread;
		this.location = location;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Locatable#location()
	 */
	public Location location() {
		return location;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.event.LocatableEvent#thread()
	 */
	public ThreadReference thread() {
		return thread;
	}
}
