/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.request;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequest;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.VirtualMachineImpl;

/**
 * Rhino implementation of {@link EventRequest}
 * 
 * @since 1.0
 */
public class EventRequestImpl implements EventRequest {

	private boolean deleted = false;
	protected boolean enabled = false;
	protected final VirtualMachineImpl vm;

	public EventRequestImpl(VirtualMachineImpl vm) {
		this.vm = vm;
	}

	public synchronized boolean isEnabled() {
		return enabled;
	}

	public synchronized void setEnabled(boolean enabled) {
		checkDeleted();
		this.enabled = enabled;
	}

	protected void checkDeleted() {
		if (deleted) {
			throw new IllegalStateException("deleted"); //$NON-NLS-1$
		}
	}

	public VirtualMachine virtualMachine() {
		return vm;
	}

	public synchronized void delete() {
		setEnabled(false);
		deleted = true;
	}
}
