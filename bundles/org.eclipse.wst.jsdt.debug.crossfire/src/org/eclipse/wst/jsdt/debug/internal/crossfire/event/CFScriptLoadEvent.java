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
package org.eclipse.wst.jsdt.debug.internal.crossfire.event;

import org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.ScriptLoadEvent;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequest;

/**
 * Default implementation of {@link ScriptLoadEvent} for Crossfire
 * 
 * @since 1.0
 */
public class CFScriptLoadEvent extends CFLocatableEvent implements ScriptLoadEvent {

	private ScriptReference script = null;
	
	/**
	 * Constructor
	 * @param vm
	 * @param request
	 */
	public CFScriptLoadEvent(VirtualMachine vm, EventRequest request, ThreadReference thread, ScriptReference script) {
		super(vm, request, thread, null);
		this.script = script;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.event.ScriptLoadEvent#script()
	 */
	public ScriptReference script() {
		return script;
	}

}
