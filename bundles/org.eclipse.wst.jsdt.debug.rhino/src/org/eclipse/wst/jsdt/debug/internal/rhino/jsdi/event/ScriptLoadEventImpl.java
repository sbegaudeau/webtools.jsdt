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

import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.ScriptLoadEvent;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ScriptLoadRequest;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.ScriptReferenceImpl;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.VirtualMachineImpl;

public class ScriptLoadEventImpl extends EventImpl implements ScriptLoadEvent {

	private final ThreadReference thread;
	private final ScriptReferenceImpl script;

	public ScriptLoadEventImpl(VirtualMachineImpl vm, ThreadReference thread, ScriptReferenceImpl script, ScriptLoadRequest request) {
		super(vm, request);
		this.thread = thread;
		this.script = script;
	}

	public ScriptReference script() {
		return script;
	}

	public ThreadReference thread() {
		return thread;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Locatable#location()
	 */
	public Location location() {
		return null;
	}
}
