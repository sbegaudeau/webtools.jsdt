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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame;
import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.crossfire.Tracing;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Attributes;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFRequestPacket;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFResponsePacket;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Commands;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.JSON;

/**
 * Default implementation of {@link ThreadReference} for Crossfire
 * 
 * @since 1.0
 */
public class CFThreadReference extends CFMirror implements ThreadReference {
	
	static final int RUNNING = 0;
	static final int SUSPENDED = 1;
	static final int TERMINATED = 2;
	
	private String id = null;
	private String href = null;
	private int state = RUNNING;
	private ArrayList frames = null;
	
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
		this.id = (String) json.get(Attributes.CROSSFIRE_ID);
		if(this.id == null) {
			this.id = (String) json.get(Attributes.CONTEXT_ID);
		}
		this.href = (String) json.get(Attributes.HREF);
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
			CFRequestPacket request = new CFRequestPacket(Commands.BACKTRACE, id);
			request.setArgument(Attributes.FROM_FRAME, new Integer(0));
			request.setArgument(Attributes.INCLUDE_SCOPES, Boolean.TRUE);
			CFResponsePacket response = crossfire().sendRequest(request);
			if(response.isSuccess()) {
				frames = new ArrayList();
				ArrayList frms = (ArrayList) response.getBody().get(Attributes.FRAMES);
				for (int i = 0; i < frms.size(); i++) {
					frames.add(new CFStackFrame(virtualMachine(), this, (Map) frms.get(i)));
				}
			}
			else {
				if(TRACE) {
					Tracing.writeString("STACKFRAME [backtrace request failed]: "+JSON.serialize(request)); //$NON-NLS-1$
				}
				return Collections.EMPTY_LIST;
			}
		}
		return frames;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#interrupt()
	 */
	public void interrupt() {
		try {
			resume();
		}
		finally {
			state = TERMINATED;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#resume()
	 */
	public void resume() {
		CFRequestPacket request = new CFRequestPacket(Commands.CONTINUE, id);
		CFResponsePacket response = crossfire().sendRequest(request);
		if(response.isSuccess()) {
			clearFrames();
			state = RUNNING;
		}
		else if(TRACE) {
			Tracing.writeString("THREAD [failed continue request]: "+JSON.serialize(request)); //$NON-NLS-1$
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#suspend()
	 */
	public void suspend() {
		CFRequestPacket request = new CFRequestPacket(Commands.SUSPEND, id);
		CFResponsePacket response = crossfire().sendRequest(request);
		if(response.isSuccess()) {
			//XXX catch in case the last resume failed
			clearFrames();
			state = SUSPENDED;
		}
		else if(TRACE) {
			Tracing.writeString("THREAD [failed suspend request]: "+JSON.serialize(request)); //$NON-NLS-1$
		}
	}

	/**
	 * Clears out any stale stack frames
	 */
	void clearFrames() {
		if(frames != null) {
			frames.clear();
			frames = null;
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
		return NLS.bind(Messages.thread_name, new Object[] {id, href});
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
	
	/**
	 * Marks the thread as suspended or not. This is a call-back from the 
	 * VM when suspending a VM.
	 * 
	 * @param suspended
	 */
	public void markSuspended(boolean suspended) {
		//XXX catch - this causes a state change
		clearFrames();
		state = suspended ? SUSPENDED : RUNNING;
	}
}
