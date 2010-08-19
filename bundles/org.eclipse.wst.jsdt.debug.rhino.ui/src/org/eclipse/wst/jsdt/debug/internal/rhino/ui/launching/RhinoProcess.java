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
package org.eclipse.wst.jsdt.debug.internal.rhino.ui.launching;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.debug.internal.core.StreamsProxy;
import org.eclipse.wst.jsdt.debug.internal.core.launching.JavaScriptProcess;
import org.eclipse.wst.jsdt.debug.rhino.debugger.RhinoDebugger;

/**
 * Specialization to terminate the instantiated {@link RhinoDebugger}
 * 
 * @since 1.0
 */
public class RhinoProcess extends JavaScriptProcess {

	public static final String TYPE = "rhino"; //$NON-NLS-1$
	
	private Process process = null;
	
	private StreamsProxy streamproxy = null;
	
	/**
	 * Constructor
	 * 
	 * @param launch the {@link ILaunch} this {@link IProcess} is associated with
	 * @param p the underlying {@link Process}
	 * @param name the human readable name for the process
	 */
	public RhinoProcess(ILaunch launch, Process p, String name) {
		super(launch, name);
		this.process = p;
		String capture = launch.getAttribute(DebugPlugin.ATTR_CAPTURE_OUTPUT);
		boolean needsstreams = capture == null ? true : Boolean.valueOf(capture).booleanValue();
		if(needsstreams) {
			streamproxy = new StreamsProxy(p, launch.getAttribute(DebugPlugin.ATTR_CONSOLE_ENCODING));
		}
		setAttribute(IProcess.ATTR_PROCESS_TYPE, TYPE);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.core.launching.JavaScriptProcess#terminate()
	 */
	public void terminate() throws DebugException {
		process.destroy();
		if(streamproxy != null) {
			streamproxy.close();
		}
		super.terminate();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.core.launching.JavaScriptProcess#getStreamsProxy()
	 */
	public IStreamsProxy getStreamsProxy() {
		return streamproxy;
	}
}
