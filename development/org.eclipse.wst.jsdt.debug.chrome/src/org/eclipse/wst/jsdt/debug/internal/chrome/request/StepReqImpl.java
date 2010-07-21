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

import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.StepRequest;
import org.eclipse.wst.jsdt.debug.internal.chrome.jsdi.VMImpl;

/**
 * {@link StepRequest} impl
 * 
 * @since 1.0
 */
public class StepReqImpl extends EventReqImpl implements StepRequest {

	private int stepkind = 0;
	private ThreadReference thread = null;
	
	/**
	 * Constructor
	 * @param vm
	 * @param thread
	 * @param step
	 * @param enabled
	 */
	public StepReqImpl(VMImpl vm, ThreadReference thread, int step, boolean enabled) {
		super(vm, enabled);
		this.thread = thread;
		this.stepkind = step;
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
