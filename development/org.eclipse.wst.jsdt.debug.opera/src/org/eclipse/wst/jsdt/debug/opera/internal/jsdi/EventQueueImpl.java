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

import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventQueue;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventSet;

/**
 * Default {@link EventQueue} implementation for Opera
 * @since 0.1
 */
public class EventQueueImpl extends MirrorImpl implements EventQueue {

	/**
	 * Constructor
	 * @param vm
	 */
	public EventQueueImpl(VirtualMachineImpl vm) {
		super(vm);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.event.EventQueue#remove()
	 */
	public EventSet remove() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.event.EventQueue#remove(int)
	 */
	public EventSet remove(int timeout) {
		return null;
	}
}
