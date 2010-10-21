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
package org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.request;

import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ResumeRequest;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.VirtualMachineImpl;

/**
 * Rhino implementation of {@link ResumeRequest}
 * 
 * @since 1.1
 */
public class ResumeRequestImpl extends EventRequestImpl implements ResumeRequest {

	private final ThreadReference thread;
	
	/**
	 * Constructor
	 * @param vm
	 * @param thread
	 */
	public ResumeRequestImpl(VirtualMachineImpl vm, ThreadReference thread) {
		super(vm);
		this.thread = thread;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.ResumeRequest#thread()
	 */
	public ThreadReference thread() {
		return thread;
	}

}
