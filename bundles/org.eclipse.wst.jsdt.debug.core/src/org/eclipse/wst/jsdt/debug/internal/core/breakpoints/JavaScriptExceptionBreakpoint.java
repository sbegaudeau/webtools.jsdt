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
package org.eclipse.wst.jsdt.debug.internal.core.breakpoints;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.Event;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventSet;
import org.eclipse.wst.jsdt.debug.core.model.JavaScriptDebugModel;
import org.eclipse.wst.jsdt.debug.internal.core.model.JavaScriptDebugTarget;


/**
 * Implementation of an exception breakpoint
 * 
 * @since 1.0
 */
public class JavaScriptExceptionBreakpoint extends JavaScriptBreakpoint {

	/**
	 * Constructor
	 * 
	 * Required for persistence / restore
	 */
	public JavaScriptExceptionBreakpoint() {}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IBreakpoint#getModelIdentifier()
	 */
	public String getModelIdentifier() {
		return JavaScriptDebugModel.MODEL_ID;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.core.breakpoints.JavaScriptBreakpoint#createRequest(org.eclipse.wst.jsdt.debug.internal.core.model.JavaScriptDebugTarget, org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference)
	 */
	protected boolean createRequest(JavaScriptDebugTarget target, ScriptReference script) throws CoreException {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.core.breakpoints.JavaScriptBreakpoint#handleEvent(org.eclipse.wst.jsdt.debug.core.jsdi.event.Event, org.eclipse.wst.jsdt.debug.internal.core.model.JavaScriptDebugTarget, boolean, org.eclipse.wst.jsdt.debug.core.jsdi.event.EventSet)
	 */
	public boolean handleEvent(Event event, JavaScriptDebugTarget target, boolean suspendVote, EventSet eventSet) {
		return false;
	}
}
