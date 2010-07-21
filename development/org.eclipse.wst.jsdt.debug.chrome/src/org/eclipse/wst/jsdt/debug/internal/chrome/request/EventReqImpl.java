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

import org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequest;
import org.eclipse.wst.jsdt.debug.internal.chrome.jsdi.MirrorImpl;
import org.eclipse.wst.jsdt.debug.internal.chrome.jsdi.VMImpl;

/**
 * Impl of {@link EventRequest}
 * 
 * @since 1.0
 */
public class EventReqImpl extends MirrorImpl implements EventRequest {

	private boolean enabled = false;
	
	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param enabled
	 */
	public EventReqImpl(VMImpl vm, boolean enabled) {
		super(vm);
		this.enabled = enabled;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequest#isEnabled()
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequest#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
