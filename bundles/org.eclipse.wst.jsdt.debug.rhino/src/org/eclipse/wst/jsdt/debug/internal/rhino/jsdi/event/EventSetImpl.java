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
package org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.event;

import java.util.HashSet;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventSet;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.ThreadReferenceImpl;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.VirtualMachineImpl;

public class EventSetImpl extends HashSet implements EventSet {

	private static final long serialVersionUID = 8149294222889427503L;
	private VirtualMachineImpl vm;
	private ThreadReferenceImpl thread;

	public EventSetImpl(VirtualMachineImpl vm) {
		this.vm = vm;
	}

	public void resume() {
		if (thread == null)
			vm.resume();
		else
			thread.resume();
	}

	public boolean suspended() {
		return true;
	}

	public VirtualMachine virtualMachine() {
		return this.vm;
	}

	public void setThread(ThreadReferenceImpl thread) {
		this.thread = thread;
	}
}
