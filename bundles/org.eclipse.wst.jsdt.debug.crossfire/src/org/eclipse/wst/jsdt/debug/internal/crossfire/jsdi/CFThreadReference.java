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
package org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame;
import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.StepRequest;
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
	static final int EVENT_RESUME = 3;
	
	private String id = null;
	private String url = null;
	private boolean current = false;
	private int state = RUNNING;
	private ArrayList frames = null;
	private int stepkind = -1;
	
	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param id
	 * @param url
	 */
	public CFThreadReference(VirtualMachine vm, String id, String url) {
		super(vm);
		this.id = id;
		this.url = url;
	}
	
	/**
	 * Constructor
	 * @param vm
	 * @param json
	 */
	public CFThreadReference(VirtualMachine vm, Map json) {
		super(vm);
		this.id = (String) json.get(Attributes.CONTEXT_ID);
		this.url = (String) json.get(Attributes.URL);
		Boolean bool = (Boolean) json.get(Attributes.CURRENT);
		if(bool != null) {
			this.current = bool.booleanValue();
		}
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
		if(frames == null) {
			CFRequestPacket request = new CFRequestPacket(Commands.BACKTRACE, id);
			request.setArgument(Attributes.FROM_FRAME, new Integer(0));
			request.setArgument(Attributes.INCLUDE_SCOPES, Boolean.TRUE);
			CFResponsePacket response = crossfire().sendRequest(request);
			if(response.isSuccess()) {
				frames = new ArrayList();
				List frms = (List) response.getBody().get(Attributes.FRAMES);
				if(frms != null) {
					Map fmap = null;
					for (int i = 0; i < frms.size(); i++) {
						fmap = (Map) frms.get(i);
						//XXX hack to prevent http://code.google.com/p/fbug/issues/detail?id=4203
						if(fmap.containsKey(Attributes.URL)) {
							frames.add(new CFStackFrame(virtualMachine(), this, fmap));
						}
						else if(TRACE) {
							Tracing.writeString("STACKFRAME [got bogus stackframe infos]: "+fmap.values().toString()); //$NON-NLS-1$
						}
					}
				}
			}
			else {
				if(TRACE) {
					Tracing.writeString("STACKFRAME [backtrace request failed]: "+JSON.serialize(request)); //$NON-NLS-1$
				}
				if(frames != null) {
					frames.clear();
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

	/**
	 * The thread has been resumed by an event
	 */
	public void eventResume() {
		state = EVENT_RESUME;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#resume()
	 */
	public void resume() {
		if(isSuspended()) {
			try {
				if(state != EVENT_RESUME) {
					//XXX only send an request if we were not resumed by an event
					CFRequestPacket request = new CFRequestPacket(Commands.CONTINUE, id);
					String step = resolveStepKind();
					if(step != null) {
						request.setArgument(Attributes.STEPACTION, step);
					}
				
					CFResponsePacket response = crossfire().sendRequest(request);
					if(response.isSuccess()) {
						state = RUNNING;
					}
					else if(TRACE) {
						Tracing.writeString("THREAD [failed continue request] "+JSON.serialize(request)); //$NON-NLS-1$
					}
				}
			}
			finally {
				clearFrames();
				state = RUNNING;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#suspend()
	 */
	public void suspend() {
		if(isRunning()) {
			CFRequestPacket request = new CFRequestPacket(Commands.SUSPEND, id);
			try {
				CFResponsePacket response = crossfire().sendRequest(request);
				if(response.isSuccess()) {
					//XXX catch in case the last resume failed
					state = SUSPENDED;
				}
				else if(TRACE) {
					Tracing.writeString("THREAD [failed suspend request]: "+JSON.serialize(request)); //$NON-NLS-1$
				}
			}
			finally {
				clearFrames();
			}
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

	/**
	 * @return if the thread is in a running state
	 */
	public boolean isRunning() {
		return state == RUNNING;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#name()
	 */
	public String name() {
		return NLS.bind(Messages.thread_name, new Object[] {id, url});
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ThreadReference: [contextId - ").append(id).append("] [url - ").append(url).append("]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
	 * Returns the URL for this thread
	 * 
	 * @return the URL
	 */
	public String url() {
		return url;
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
	
	/**
	 * Sets the current step kind kind to perform, or -1 to remove the kind
	 * @param stepkind
	 */
	public void setStep(int step) {
		this.stepkind = step;
	}
	
	/**
	 * @return the step kind to use in the continue request or <code>null</code>
	 */
	String resolveStepKind() {
		if(stepkind != -1) {
			switch(stepkind) {
				case StepRequest.STEP_INTO: return Commands.STEP_IN;
				case StepRequest.STEP_OUT: return Commands.STEP_OUT;
				case StepRequest.STEP_OVER: return Commands.STEP_NEXT;
			}
		}
		return null;
	}
	
	/**
	 * Returns if the this thread is the current (focus) context in the browser 
	 * 
	 * @return <code>true</code> if this thread is the current (focus) context
	 */
	public boolean isCurrent() {
		return this.current;
	}
	
	/**
	 * Allows the current (focus) status of the thread to be set
	 * @param current the new current state
	 */
	public void setCurrent(boolean current) {
		this.current = current;
	}
}