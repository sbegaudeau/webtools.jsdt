/*******************************************************************************
 * Copyright (c) 2010, 2012 IBM Corporation and others.
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpoint;
import org.eclipse.wst.jsdt.debug.core.jsdi.BooleanValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.NullValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.NumberValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.StringValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.UndefinedValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventQueue;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager;
import org.eclipse.wst.jsdt.debug.internal.rhino.Constants;
import org.eclipse.wst.jsdt.debug.internal.rhino.RhinoDebugPlugin;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.event.EventQueueImpl;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.request.EventRequestManagerImpl;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.EventPacket;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.JSONConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.RhinoRequest;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.RhinoResponse;
import org.eclipse.wst.jsdt.debug.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.transport.exception.DisconnectedException;
import org.eclipse.wst.jsdt.debug.transport.exception.TimeoutException;

/**
 * Rhino implementation of {@link VirtualMachine}
 * 
 * @since 1.0
 */
public class VirtualMachineImpl extends MirrorImpl implements VirtualMachine {

	public final UndefinedValueImpl undefinedValue = new UndefinedValueImpl(this);
	public final NullValueImpl nullValue = new NullValueImpl(this);

	private final Map scripts = new HashMap();
	private final DebugSession session;
	private EventRequestManagerImpl eventRequestManager = new EventRequestManagerImpl(this);
	private final EventQueue eventQueue = new EventQueueImpl(this, eventRequestManager);
	private Map threads = new HashMap();
	private boolean disconnected = false;
	private String name = null;
	private String version = null;
	Object lock = new Object();

	/**
	 * Constructor
	 * 
	 * @param debugSession
	 */
	public VirtualMachineImpl(DebugSession debugSession) {
		super(Constants.SPACE);
		this.session = debugSession;
		initalizeScripts();
	}

	/**
	 * loads the scripts
	 */
	private void initalizeScripts() {
		RhinoRequest request = new RhinoRequest(JSONConstants.SCRIPTS);
		try {
			RhinoResponse response = sendRequest(request);
			List scriptIds = (List) response.getBody().get(JSONConstants.SCRIPTS);
			for (Iterator iterator = scriptIds.iterator(); iterator.hasNext();) {
				Long scriptId = new Long(((Number) iterator.next()).longValue());
				ScriptReferenceImpl script = createScriptReference(scriptId);
				if(script != null) {
					scripts.put(scriptId, script);
				}
			}
		} catch (DisconnectedException e) {
			disconnectVM();
			handleException(e.getMessage(), e);
		} catch (TimeoutException e) {
			RhinoDebugPlugin.log(e);
		}
	}

	/**
	 * Called via reflection to determine if the vm supports suspend on script loads
	 * @return <code>true</code> if this VM can suspend when a script loads <code>false</code> otherwise
	 */
	public boolean supportsSuspendOnScriptLoads() {
		return true;
	}
	
	/**
	 * Creates a new {@link ScriptReferenceImpl} for the given ID
	 * @param scriptId
	 * @return the new {@link ScriptReferenceImpl} or <code>null</code>
	 */
	private ScriptReferenceImpl createScriptReference(Long scriptId) {
		RhinoRequest request = new RhinoRequest(JSONConstants.SCRIPT);
		request.getArguments().put(JSONConstants.SCRIPT_ID, scriptId);
		try {
			RhinoResponse response = sendRequest(request, DEFAULT_TIMEOUT);
			Map jsonScript = (Map) response.getBody().get(JSONConstants.SCRIPT);
			return new ScriptReferenceImpl(this, jsonScript);
		} catch (DisconnectedException e) {
			disconnectVM();
			handleException(e.getMessage(), e);
		} catch (TimeoutException e) {
			RhinoDebugPlugin.log(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#allScripts()
	 */
	public synchronized List allScripts() {
		return new ArrayList(scripts.values());
	}

	/**
	 * Adds a new script to the {@link VirtualMachine} with the given ID and returns
	 * it.
	 * @param scriptId
	 * @param script
	 */
	public synchronized ScriptReferenceImpl addScript(Long scriptId) {
		ScriptReferenceImpl script = createScriptReference(scriptId);
		if (script != null) {
			scripts.put(scriptId, script);
		}
		return script;
	}

	/**
	 * Returns the {@link ScriptReferenceImpl} for the given ID or <code>null</code>
	 * if no such {@link ScriptReferenceImpl} exists.
	 * @param scriptId
	 * @return the {@link ScriptReferenceImpl} for the given ID or <code>null</code>
	 */
	public synchronized ScriptReferenceImpl getScript(Long scriptId) {
		return (ScriptReferenceImpl) scripts.get(scriptId);
	}

	/**
	 * @param timeout
	 * @return
	 * @throws TimeoutException
	 * @throws DisconnectedException
	 */
	public EventPacket receiveEvent(int timeout) throws TimeoutException, DisconnectedException {
		return (EventPacket) session.receive(EventPacket.TYPE, timeout);
	}

	/**
	 * Sends a request to the backing {@link DebugSession}
	 * 
	 * @param request
	 * @param timeout
	 * @return the {@link RhinoResponse} for the request
	 * @throws TimeoutException
	 * @throws DisconnectedException
	 */
	public RhinoResponse sendRequest(RhinoRequest request, int timeout) throws TimeoutException, DisconnectedException {
		session.send(request);
		return (RhinoResponse) session.receiveResponse(request.getSequence(), timeout);
	}

	/**
	 * Sends the given {@link RhinoRequest} using the default timeout
	 * @param request
	 * @return the {@link RhinoResponse} from the request
	 * @throws TimeoutException
	 * @throws DisconnectedException
	 */
	public RhinoResponse sendRequest(RhinoRequest request) throws TimeoutException, DisconnectedException {
		return sendRequest(request, DEFAULT_TIMEOUT);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#allThreads()
	 */
	public synchronized List allThreads() {
		RhinoRequest request = new RhinoRequest(JSONConstants.THREADS);
		try {
			RhinoResponse response = sendRequest(request);
			List threadIds = (List) response.getBody().get(JSONConstants.THREADS);
			HashMap allThreads = new HashMap(threadIds.size());
			for (Iterator iterator = threadIds.iterator(); iterator.hasNext();) {
				Long threadId = new Long(((Number) iterator.next()).longValue());
				ThreadReferenceImpl thread = (ThreadReferenceImpl) threads.get(threadId);
				if (thread == null) {
					thread = createThreadReference(threadId);
				}
				if(thread != null) {
					allThreads.put(threadId, thread);
				}
			}
			threads = allThreads;
		} catch (DisconnectedException e) {
			disconnectVM();
			handleException(e.getMessage(), e);
		} catch (TimeoutException e) {
			RhinoDebugPlugin.log(e);
		}
		return new ArrayList(threads.values());
	}

	/**
	 * Removes the thread with the given ID
	 * @param threadId
	 * @return the {@link ThreadReferenceImpl} that has been removed
	 */
	public synchronized ThreadReferenceImpl removeThread(Long threadId) {
		return (ThreadReferenceImpl) threads.remove(threadId);
	}

	/**
	 * Creates a new {@link ThreadReferenceImpl} with the given ID
	 * @param threadId
	 * @return the new {@link ThreadReferenceImpl} or <code>null</code>
	 */
	private ThreadReferenceImpl createThreadReference(Long threadId) {
		RhinoRequest request = new RhinoRequest(JSONConstants.THREAD);
		request.getArguments().put(JSONConstants.THREAD_ID, threadId);
		try {
			RhinoResponse response = sendRequest(request, DEFAULT_TIMEOUT);
			Map jsonThread = (Map) response.getBody().get(JSONConstants.THREAD);
			if (jsonThread == null) {
				return null;
			}
			return new ThreadReferenceImpl(this, jsonThread);
		} catch (DisconnectedException e) {
			disconnectVM();
			handleException(e.getMessage(), e);
		} catch (TimeoutException e) {
			RhinoDebugPlugin.log(e);
		}
		return null;
	}

	/**
	 * Returns the {@link ThreadReferenceImpl} with the given ID or 
	 * create and return a new one if it does not exist
	 * @param threadId
	 * @return the {@link ThreadReferenceImpl} for the given ID or <code>null</code>
	 * if the request to create a new one fails
	 */
	public synchronized ThreadReferenceImpl getThread(Long threadId) {
		ThreadReferenceImpl thread = (ThreadReferenceImpl) threads.get(threadId);
		if (thread == null) {
			thread = createThreadReference(threadId);
			if (thread != null) {
				threads.put(threadId, thread);
			}
		}
		return thread;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#description()
	 */
	public String description() {
		RhinoRequest request = new RhinoRequest(JSONConstants.VERSION);
		try {
			RhinoResponse response = sendRequest(request);
			StringBuffer buffer = new StringBuffer();
			buffer.append((String) response.getBody().get(JSONConstants.VM_VENDOR)).append(Constants.SPACE);
			buffer.append(response.getBody().get(JSONConstants.VM_NAME)).append(Constants.SPACE);
			buffer.append(response.getBody().get(JSONConstants.VM_VERSION));
			return buffer.toString();
		} catch (DisconnectedException e) {
			disconnectVM();
			handleException(e.getMessage(), e);
		} catch (TimeoutException e) {
			RhinoDebugPlugin.log(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#dispose()
	 */
	public void dispose() {
		RhinoRequest request = new RhinoRequest(JSONConstants.DISPOSE);
		try {
			sendRequest(request);
		} catch (DisconnectedException e) {
			disconnectVM();
			handleException(e.getMessage(), e);
		} catch (TimeoutException e) {
			RhinoDebugPlugin.log(e);
		} finally {
			disconnectVM();
			this.eventRequestManager.createVMDeathRequest();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#name()
	 */
	public String name() {
		synchronized (lock) {
			if(this.name == null) {
	 			RhinoRequest request = new RhinoRequest(JSONConstants.VERSION);
				try {
					RhinoResponse response = sendRequest(request);
					name = (String) response.getBody().get(JSONConstants.VM_NAME);
				} catch (DisconnectedException e) {
					disconnectVM();
					handleException(e.getMessage(), e);
				} catch (TimeoutException e) {
					RhinoDebugPlugin.log(e);
				}
			}
		}
		return this.name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#resume()
	 */
	public void resume() {
		RhinoRequest request = new RhinoRequest(JSONConstants.CONTINUE);
		try {
			sendRequest(request);
		} catch (DisconnectedException e) {
			disconnectVM();
			handleException(e.getMessage(), e);
		} catch (TimeoutException e) {
			RhinoDebugPlugin.log(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#suspend()
	 */
	public void suspend() {
		RhinoRequest request = new RhinoRequest(JSONConstants.SUSPEND);
		try {
			sendRequest(request);
		} catch (DisconnectedException e) {
			disconnectVM();
			handleException(e.getMessage(), e);
		} catch (TimeoutException e) {
			RhinoDebugPlugin.log(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#terminate()
	 */
	public void terminate() {
		dispose();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#version()
	 */
	public String version() {
		synchronized (lock) {
			if(this.version == null) {
				RhinoRequest request = new RhinoRequest(JSONConstants.VERSION);
				try {
					RhinoResponse response = sendRequest(request);
					this.version = (String) response.getBody().get(JSONConstants.VM_VERSION);
				} catch (DisconnectedException e) {
					disconnectVM();
					handleException(e.getMessage(), e);
				} catch (TimeoutException e) {
					RhinoDebugPlugin.log(e);
				}
			}
		}
		return this.version;
	}

	/**
	 * disconnects the VM and creates the correct requests
	 */
	public void disconnectVM() {
		if (this.disconnected) {
			// no-op it is already disconnected
			return;
		}
		try {
			this.session.dispose();
		} finally {
			this.disconnected = true;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#mirrorOf(boolean)
	 */
	public BooleanValue mirrorOf(boolean bool) {
		return new BooleanValueImpl(this, Boolean.valueOf(bool));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#mirrorOf(java.lang.Number)
	 */
	public NumberValue mirrorOf(Number number) {
		return new NumberValueImpl(this, number);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#mirrorOf(java.lang.String)
	 */
	public StringValue mirrorOf(String string) {
		return new StringValueImpl(this, string);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#mirrorOfNull()
	 */
	public NullValue mirrorOfNull() {
		return nullValue;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#mirrorOfUndefined()
	 */
	public UndefinedValue mirrorOfUndefined() {
		return undefinedValue;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#eventRequestManager()
	 */
	public EventRequestManager eventRequestManager() {
		return eventRequestManager;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#eventQueue()
	 */
	public EventQueue eventQueue() {
		return eventQueue;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#canUpdateBreakpoints()
	 */
	public boolean canUpdateBreakpoints() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#updateBreakpoint(org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpoint)
	 */
	public void updateBreakpoint(IJavaScriptBreakpoint breakpoint) {
	}
}
