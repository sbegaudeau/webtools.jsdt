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

import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.ThreadExitEvent;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequest;

/**
 * Default implementation of {@link ThreadExitEvent} for Crossfire
 * 
 * @since 1.0
 */
public class CFThreadExitEvent extends CFLocatableEvent implements ThreadExitEvent {

	/**
	 * Constructor
	 * @param vm
	 * @param request
	 * @param thread
	 */
	public CFThreadExitEvent(VirtualMachine vm, EventRequest request, ThreadReference thread) {
		super(vm, request, thread, null);
	}
}
