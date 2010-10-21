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
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ResumeRequest;
import org.eclipse.wst.jsdt.debug.internal.chrome.jsdi.VMImpl;

/**
 * {@link ResumeRequest} impl
 * 
 * @since 1.0
 */
public class ResumeReqImpl extends EventReqImpl implements ResumeRequest {

	private ThreadReference thread = null;
	
	/**
	 * Constructor
	 * @param vm
	 * @param enabled
	 */
	public ResumeReqImpl(VMImpl vm,ThreadReference thread, boolean enabled) {
		super(vm, enabled);
		this.thread = thread;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.SuspendRequest#thread()
	 */
	public ThreadReference thread() {
		return thread;
	}
}
