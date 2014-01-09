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

import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.StepRequest;
import org.eclipse.wst.jsdt.debug.opera.internal.jsdi.VirtualMachineImpl;

/**
 * Default {@link StepRequest} implementation for Opera
 * 
 * @since 0.1
 */
public class StepRequestImpl extends EventRequestImpl implements StepRequest {

	ThreadReference thread = null;
	int stepkind = -1;
	
	/**
	 * Constructor
	 * @param vm
	 */
	public StepRequestImpl(VirtualMachineImpl vm, ThreadReference thread, int kind) {
		super(vm);
		this.thread = thread;
		this.stepkind = kind;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.StepRequest#step()
	 */
	public int step() {
		return stepkind;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.StepRequest#thread()
	 */
	public ThreadReference thread() {
		return thread;
	}
}