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
package org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame;
import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Request;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Response;

/**
 * Default implementation of {@link ThreadReference} for Crossfire
 * 
 * @since 1.0
 */
public class CFThreadReference extends CFMirror implements ThreadReference {

	/**
	 * The "frame" command
	 */
	public static final String CMD_FRAME = "frame"; //$NON-NLS-1$
	/**
	 * The "crossfire_id" attribute
	 */
	public static final String CROSSFIRE_ID = "crossfire_id"; //$NON-NLS-1$
	
	static final int RUNNING = 0;
	static final int SUSPENDED = 1;
	static final int TERMINATED = 2;
	
	private String id = null;
	private String href = null;
	private int state = RUNNING;
	private ArrayList frames = null;
	/**
	 * The "href" attribute
	 */
	public static final String HREF = "href"; //$NON-NLS-1$
	
	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param id
	 * @param href
	 */
	public CFThreadReference(VirtualMachine vm, String id, String href) {
		super(vm);
		this.id = id;
		this.href = href;
	}
	
	/**
	 * Constructor
	 * @param vm
	 * @param json
	 */
	public CFThreadReference(VirtualMachine vm, Map json) {
		super(vm);
		this.id = (String) json.get(CROSSFIRE_ID);
		this.href = (String) json.get(HREF);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#frameCount()
	 */
	public int frameCount() {
		return frames == null ? 0 : frames.size();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#frame(int)
	 */
	public synchronized StackFrame frame(int index) {
		if(frames == null || index < 0 || index > frames.size()) {
			return null;
		}
		return (StackFrame) frames.get(index);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#frames()
	 */
	public synchronized List frames() {
		//TODO we require some way to batch retrieve the frames from a given context
		//unless there is only ever one frame?
		if(frames == null) {
			frames = new ArrayList();
			Request request = new Request(CMD_FRAME, id);
			System.out.println(request);
			Response response = crossfire().sendRequest(request);
			System.out.println(response);
			if(response.isSuccess()) {
				frames.add(new CFStackFrame(virtualMachine(), response.getBody()));
			}
		}
		return frames;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#interrupt()
	 */
	public void interrupt() {
		try {
			
		}
		finally {
			state = TERMINATED;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#resume()
	 */
	public void resume() {
		try {
			if(frames != null) {
				frames.clear();
				frames = null;
			}
		}
		finally {
			state = RUNNING;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#suspend()
	 */
	public void suspend() {
		try {
			
		}
		finally {
			state = SUSPENDED;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#status()
	 */
	public int status() {
		return THREAD_STATUS_RUNNING;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#isAtBreakpoint()
	 */
	public boolean isAtBreakpoint() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#isSuspended()
	 */
	public boolean isSuspended() {
		return state == SUSPENDED;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#name()
	 */
	public String name() {
		String url = href;
		if(href.length() > 50) {
			url = href.substring(0, 17) + "..."; //$NON-NLS-1$
		}
		return NLS.bind(Messages.thread_name, new Object[] {id, url});
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ThreadReference: [crossfire_id - ").append(id).append("] [href - ").append(href).append("]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return buffer.toString();
	}
	
	/**
	 * Returns the Crossfire id for this thread
	 * 
	 * @return the id
	 */
	public String id() {
		return id;
	}
	
	/**
	 * Returns the href context for this thread
	 * 
	 * return the href context
	 */
	public String href() {
		return href;
	}
}
