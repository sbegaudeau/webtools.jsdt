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
package org.eclipse.wst.jsdt.debug.internal.core.launching;

import java.util.HashMap;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;

/**
 * General {@link IProcess} for JavaScript
 * 
 * @since 1.1
 */
public class JavaScriptProcess extends PlatformObject implements IProcess {

	private HashMap attributes = new HashMap();
	private ILaunch launch = null;
	private boolean terminated = false;
	private String name = null;
	
	/**
	 * Constructor
	 * @param config
	 * @param name
	 */
	public JavaScriptProcess(ILaunch launch, String name) {
		this.launch = launch;
		this.name = name;
		fireEvent(new DebugEvent(this, DebugEvent.CREATE));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if(adapter == IProcess.class) {
			return this;
		}
		if(adapter == ILaunch.class) {
			return launch;
		}
		if (adapter.equals(IDebugTarget.class)) {
			IDebugTarget[] targets = launch.getDebugTargets();
			for (int i = 0; i < targets.length; i++) {
				if (this.equals(targets[i].getProcess())) {
					return targets[i];
				}
			}
			return null;
		}
		return super.getAdapter(adapter);
	}

	/**
	 * Fires the given debug event.
	 * 
	 * @param event debug event to fire
	 */
	void fireEvent(DebugEvent event) {
		DebugPlugin dp = DebugPlugin.getDefault();
		if (dp != null) {
			dp.fireDebugEventSet(new DebugEvent[]{event});
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.ITerminate#canTerminate()
	 */
	public boolean canTerminate() {
		synchronized (this) {
			return !terminated;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.ITerminate#isTerminated()
	 */
	public boolean isTerminated() {
		synchronized (this) {
			return terminated;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.ITerminate#terminate()
	 */
	public void terminate() throws DebugException {
		synchronized (this) {
			if(!terminated) {
				fireEvent(new DebugEvent(this, DebugEvent.TERMINATE));
				terminated = true;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IProcess#getLabel()
	 */
	public String getLabel() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IProcess#getLaunch()
	 */
	public ILaunch getLaunch() {
		return launch;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IProcess#getStreamsProxy()
	 */
	public IStreamsProxy getStreamsProxy() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IProcess#setAttribute(java.lang.String, java.lang.String)
	 */
	public void setAttribute(String key, String value) {
		if(key != null) {
			attributes.put(key, value);
			fireEvent(new DebugEvent(this, DebugEvent.CHANGE));
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IProcess#getAttribute(java.lang.String)
	 */
	public String getAttribute(String key) {
		return (String) attributes.get(key);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IProcess#getExitValue()
	 */
	public int getExitValue() throws DebugException {
		return 0;
	}
}
