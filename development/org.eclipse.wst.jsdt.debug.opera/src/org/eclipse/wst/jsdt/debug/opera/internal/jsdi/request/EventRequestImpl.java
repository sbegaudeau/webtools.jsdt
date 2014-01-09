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

import org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequest;
import org.eclipse.wst.jsdt.debug.opera.internal.jsdi.MirrorImpl;
import org.eclipse.wst.jsdt.debug.opera.internal.jsdi.VirtualMachineImpl;

/**
 * Default {@link EventRequest} implementation for Opera
 * 
 * @since 0.1
 */
public class EventRequestImpl extends MirrorImpl implements EventRequest {

	/**
	 * Constructor
	 * @param vm
	 */
	public EventRequestImpl(VirtualMachineImpl vm) {
		super(vm);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequest#isEnabled()
	 */
	public boolean isEnabled() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequest#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
	}

}
