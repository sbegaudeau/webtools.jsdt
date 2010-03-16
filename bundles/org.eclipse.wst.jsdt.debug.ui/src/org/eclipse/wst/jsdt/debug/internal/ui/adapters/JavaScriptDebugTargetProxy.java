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
package org.eclipse.wst.jsdt.debug.internal.ui.adapters;

import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.internal.ui.viewers.update.DebugEventHandler;
import org.eclipse.debug.internal.ui.viewers.update.DebugTargetEventHandler;
import org.eclipse.debug.internal.ui.viewers.update.DebugTargetProxy;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptDebugTarget;

/**
 * Model proxy for our {@link IJavaScriptDebugTarget}
 * 
 * @since 1.0
 */
public class JavaScriptDebugTargetProxy extends DebugTargetProxy {

	static DebugTargetEventHandler fgTargetHandle = null;
	JavaScriptThreadHandler threadHandler = null;
	
	/**
	 * Constructor
	 * @param target
	 */
	public JavaScriptDebugTargetProxy(IDebugTarget target) {
		super(target);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.viewers.update.DebugTargetProxy#createEventHandlers()
	 */
	protected DebugEventHandler[] createEventHandlers() {
		return new DebugEventHandler[] {getTargetHandler(), getThreadHandler()};
	}
	
	/**
	 * @return the singleton {@link DebugTargetEventHandler}
	 */
	synchronized DebugTargetEventHandler getTargetHandler() {
		if(fgTargetHandle == null) {
			fgTargetHandle = new DebugTargetEventHandler(this);
		}
		return fgTargetHandle;
	}

	/**
	 * @return the {@link JavaScriptThreadHandler}
	 */
	JavaScriptThreadHandler getThreadHandler() {
		threadHandler = new JavaScriptThreadHandler(this);
		return threadHandler;
	}
}
