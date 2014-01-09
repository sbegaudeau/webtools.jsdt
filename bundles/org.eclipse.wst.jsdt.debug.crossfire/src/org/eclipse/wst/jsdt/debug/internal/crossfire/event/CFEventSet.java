/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.crossfire.event;

import java.util.ArrayList;

import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventSet;

/**
 * Default implementation of {@link EventSet} for Crossfire
 *  
 *  @since 1.0
 */
public class CFEventSet extends ArrayList implements EventSet {

	private VirtualMachine vm = null;
	private ThreadReference thread = null;
	
	/**
	 * Constructor
	 * @param vm
	 */
	public CFEventSet(VirtualMachine vm) {
		this.vm = vm;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.event.EventSet#suspended()
	 */
	public boolean suspended() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.event.EventSet#resume()
	 */
	public void resume() {
		if(thread != null) {
			thread.resume();
		}
		else {
			vm.resume();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Mirror#virtualMachine()
	 */
	public VirtualMachine virtualMachine() {
		return vm;
	}
	 
	/**
	 * Sets the thread context for the set
	 * @param thread
	 */
	public void setThread(ThreadReference thread) {
		this.thread = thread;
	}
}
