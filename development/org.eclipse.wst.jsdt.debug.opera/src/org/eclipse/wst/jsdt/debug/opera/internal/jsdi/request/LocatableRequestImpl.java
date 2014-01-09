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

import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.opera.internal.jsdi.VirtualMachineImpl;

/**
 * Default implementation of a request that has a location
 * 
 * @since 0.1
 */
public class LocatableRequestImpl extends EventRequestImpl {

	Location location = null;
	
	/**
	 * Constructor
	 * @param vm
	 * @param location
	 */
	public LocatableRequestImpl(VirtualMachineImpl vm, Location location) {
		super(vm);
		this.location = location;
	}

	/**
	 * @return the location
	 */
	public Location location() {
		return location;
	}
}
