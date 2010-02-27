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
package org.eclipse.wst.jsdt.debug.internal.rhino.jsdi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame;
import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.DisconnectedException;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.TimeoutException;
import org.eclipse.wst.jsdt.debug.internal.rhino.json.JSONConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Request;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Response;

/**
 * Rhino implementation of {@link ThreadReference}
 * 
 * @since 1.0
 */
public class ThreadReferenceImpl extends MirrorImpl implements ThreadReference {

	private Number threadId;
	private List frames;
	private int status = THREAD_STATUS_RUNNING;
	private boolean suspended;
	private boolean atBreakpoint = false;
	private String name;
	private String step;

	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param jsonThread
	 */
	public ThreadReferenceImpl(VirtualMachineImpl vm, Map jsonThread) {
		super(vm);
		this.threadId = (Number) jsonThread.get(JSONConstants.THREAD_ID);
		this.name = (String) jsonThread.get(JSONConstants.NAME);
		if (this.name == null) {
			// TODO NLS this
			this.name = "Rhino - " + threadId; //$NON-NLS-1$
		}
		this.suspended = JSONConstants.SUSPENDED.equals(jsonThread
				.get(JSONConstants.STATE));
	}

	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param threadId
	 */
	private ThreadReferenceImpl(VirtualMachineImpl vm, Long threadId) {
		super(vm);
		this.threadId = threadId;
		// TODO NLS this
		this.name = "Rhino - " + threadId; //$NON-NLS-1$
		this.status = THREAD_STATUS_ZOMBIE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.languages.javascript.jsdi.ThreadReference#frameCount()
	 */
	public synchronized int frameCount() {
		frames();
		return frames.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.ThreadReference#frame(int)
	 */
	public synchronized StackFrame frame(int index) {
		frames();
		return (StackFrame) frames.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.ThreadReference#frames()
	 */
	public synchronized List frames() {
		if (!suspended || status == THREAD_STATUS_ZOMBIE)
			return Collections.EMPTY_LIST;

		if (frames != null)
			return frames;

		Request request = new Request(JSONConstants.FRAMES);
		request.getArguments().put(JSONConstants.THREAD_ID, threadId);
		try {
			Response response = vm.sendRequest(request, 30000);
			List frameIds = (List) response.getBody().get(JSONConstants.FRAMES);
			if (frameIds.isEmpty()) {
				frames = Collections.EMPTY_LIST;
				return frames;
			}
			frames = new ArrayList();
			for (Iterator iterator = frameIds.iterator(); iterator.hasNext();) {
				Long frameId = new Long(((Number) iterator.next()).longValue());
				StackFrameReferenceImpl frame = createStackFrame(frameId);
				frames.add(frame);
			}
		} catch (DisconnectedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return frames;
	}

	/**
	 * Sends a request to make a new {@link StackFrameReference} in the current debugger.
	 * Returns a new {@link StackFrameReference} on success, <code>null</code> on failure
	 * 
	 * @param frameId
	 * @return a new {@link StackFrameReference} or <code>null</code>
	 */
	private StackFrameReferenceImpl createStackFrame(Long frameId) {
		Request request = new Request(JSONConstants.FRAME);
		request.getArguments().put(JSONConstants.THREAD_ID, threadId);
		request.getArguments().put(JSONConstants.FRAME_ID, frameId);
		try {
			Response response = vm.sendRequest(request, 30000);
			Map jsonFrame = (Map) response.getBody().get(JSONConstants.FRAME);
			return new StackFrameReferenceImpl(vm, jsonFrame);
		} catch (DisconnectedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.languages.javascript.jsdi.ThreadReference#isAtBreakpoint()
	 */
	public synchronized boolean isAtBreakpoint() {
		return suspended && atBreakpoint;
	}

	/**
	 * Marks the current thread reference as suspended
	 * 
	 * @param atBreakpoint
	 */
	public synchronized void markSuspended(boolean atBreakpoint) {
		this.suspended = true;
		this.atBreakpoint = atBreakpoint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.languages.javascript.jsdi.ThreadReference#isSuspended()
	 */
	public synchronized boolean isSuspended() {
		return suspended;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.ThreadReference#name()
	 */
	public String name() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.ThreadReference#resume()
	 */
	public synchronized void resume() {
		if (status == THREAD_STATUS_ZOMBIE) {
			return;
		}
		Request request = new Request(JSONConstants.CONTINUE);
		request.getArguments().put(JSONConstants.THREAD_ID, threadId);
		if (step != null) {
			request.getArguments().put(JSONConstants.STEP, step);
		}
		try {
			Response response = vm.sendRequest(request, 30000);
			if (response.isSuccess()) {
				step = null;
				frames = null;
				suspended = false;
				atBreakpoint = false;
			}
		} catch (DisconnectedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.ThreadReference#suspend()
	 */
	public void suspend() {
		if (status == THREAD_STATUS_ZOMBIE) {
			return;
		}
		Request request = new Request(JSONConstants.SUSPEND);
		request.getArguments().put(JSONConstants.THREAD_ID, threadId);
		try {
			vm.sendRequest(request, 30000);
		} catch (DisconnectedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.ThreadReference#interrupt()
	 */
	public void interrupt() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.ThreadReference#status()
	 */
	public synchronized int status() {
		return status;
	}

	/**
	 * Returns the step type
	 * 
	 * @return the step type
	 * @see JSONConstants#STEP_IN
	 * @see JSONConstants#STEP_NEXT
	 * @see JSONConstants#STEP_OUT
	 */
	public synchronized String getStep() {
		return step;
	}

	/**
	 * Sets the current step type
	 * 
	 * @param step
	 * @see JSONConstants#STEP_IN
	 * @see JSONConstants#STEP_NEXT
	 * @see JSONConstants#STEP_OUT
	 */
	public synchronized void setStep(String step) {
		this.step = step;
	}

	/**
	 * Creates a new thread that is in the zombie state - i.e. not running and
	 * not initialized
	 * 
	 * @param vm
	 * @param threadId
	 * @return a new {@link ThreadReference}
	 */
	public static ThreadReferenceImpl zombie(VirtualMachineImpl vm,
			Long threadId) {
		return new ThreadReferenceImpl(vm, threadId);
	}
}
