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
package org.eclipse.wst.jsdt.debug.internal.chrome.jsdi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpoint;
import org.eclipse.wst.jsdt.debug.core.jsdi.BooleanValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.NullValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.NumberValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.StringValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.UndefinedValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventQueue;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager;
import org.eclipse.wst.jsdt.debug.internal.chrome.ChromePlugin;
import org.eclipse.wst.jsdt.debug.internal.chrome.Tracing;
import org.eclipse.wst.jsdt.debug.internal.chrome.event.EventQueueImpl;
import org.eclipse.wst.jsdt.debug.internal.chrome.request.EventReqManager;
import org.eclipse.wst.jsdt.debug.internal.chrome.transport.Attributes;
import org.eclipse.wst.jsdt.debug.internal.chrome.transport.EventPacketImpl;
import org.eclipse.wst.jsdt.debug.internal.chrome.transport.RequestPacketImpl;
import org.eclipse.wst.jsdt.debug.internal.chrome.transport.Commands;
import org.eclipse.wst.jsdt.debug.internal.chrome.transport.JSON;
import org.eclipse.wst.jsdt.debug.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.transport.exception.DisconnectedException;
import org.eclipse.wst.jsdt.debug.transport.exception.TimeoutException;
import org.eclipse.wst.jsdt.debug.transport.packet.Event;
import org.eclipse.wst.jsdt.debug.transport.packet.Request;
import org.eclipse.wst.jsdt.debug.transport.packet.Response;

/**
 * Default implementation of a {@link VirtualMachine} for Chrome
 * 
 * @since 1.0
 */
public class VMImpl extends MirrorImpl implements VirtualMachine {

	public static int RUNNING = 1;
	public static int SUSPENDED = 2;
	public static int TERMINATED = 3;
	public static int DISPOSED = 4;
	public static int DISCONNECTED = 5;
	
	/**
	 * The current state
	 */
	private int state = 0;
	/**
	 * The singleton {@link NullValue}
	 */
	private static NullValue nullValue = null;
	/**
	 * The singleton {@link UndefinedValue}
	 */
	private static UndefinedValue undefinedValue = null;
	
	private EventRequestManager ermanager = new EventReqManager(this);
	private EventQueue queue = new EventQueueImpl(this, ermanager);
	private final DebugSession session;
	
	private Map threads = null;
	private Map scripts = null;
	private String version = null;
	
	/**
	 * Constructor
	 * 
	 * @param session the backing {@link DebugSession}
	 */
	public VMImpl(DebugSession session) {
		super();
		this.session = session;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#resume()
	 */
	public synchronized void resume() {
		if(state == SUSPENDED) {
			//TODO
			state = RUNNING;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#suspend()
	 */
	public synchronized void suspend() {
		if(state == RUNNING) {
			//TODO
			state = SUSPENDED;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#terminate()
	 */
	public synchronized void terminate() {
		if(state != TERMINATED) {
			//TODO
			state = TERMINATED;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#name()
	 */
	public String name() {
		return NLS.bind(Messages.chrome_vm, version());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#description()
	 */
	public String description() {
		return Messages.vm_description;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#version()
	 */
	public synchronized String version() {
		if(version == null) {
			RequestPacketImpl request = new RequestPacketImpl(Commands.VERSION, Attributes.TOOL_DEVTOOLSRVC, null);
			Response response = sendRequest(request);
			if(response.isSuccess()) {
				version = (String) response.getBody().get(Attributes.DATA);
			}
			else if(TRACE) {
				Tracing.writeString("VM [failed version request]" + JSON.serialize(request)); //$NON-NLS-1$
			}
		}
		return version;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#allThreads()
	 */
	public synchronized List allThreads() {
		if(threads == null) {
			threads = Collections.synchronizedMap(new HashMap(4));
			RequestPacketImpl request = new RequestPacketImpl(Commands.LIST_TABS, Attributes.TOOL_DEVTOOLSRVC, null);
			Response response = sendRequest(request);
			if(response.isSuccess()) {
				List tabs = (List) response.getBody().get(Attributes.DATA);
				if(tabs != null) {
					ArrayList values = null;
					for(Iterator i = tabs.iterator(); i.hasNext();) {
						values = (ArrayList) i.next();
						String url = (String)values.get(1);
						Number id = (Number)values.get(0);
						ThreadImpl thread = new ThreadImpl(this, id, url);
						threads.put(id, thread);
						attach(thread);
					}
				}
			}
			else if(TRACE) {
				Tracing.writeString("VM [failed all_tabs request]" + JSON.serialize(request)); //$NON-NLS-1$
			}
		}
		return new ArrayList(threads.values());
	}

	/**
	 * Try to attach the debugger to the v8 debugger service for the given thread (tab)
	 * 
	 * @param thread
	 */
	void attach(ThreadImpl thread) {
		RequestPacketImpl request = new RequestPacketImpl(Commands.ATTACH, Attributes.TOOL_DEVTOOLSRVC, thread.id());
		Response response = sendRequest(request);
		if(response.isSuccess()) {
			
		}
		else if(TRACE) {
			Tracing.writeString("VM [failed attach request] " + JSON.serialize(request)); //$NON-NLS-1$
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#allScripts()
	 */
	public synchronized List allScripts() {
		if(this.scripts == null) {
			this.scripts = Collections.synchronizedMap(new HashMap(4));
		}
		return new ArrayList(scripts.values());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#dispose()
	 */
	public synchronized void dispose() {
		if(state != DISPOSED) {
			//TODO
			try {
				if(threads != null) {
					threads.clear();
					threads = null;
				}
				if(scripts != null) {
					scripts.clear();
					scripts = null;
				}
			}
			finally {
				state = DISPOSED;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#mirrorOfUndefined()
	 */
	public UndefinedValue mirrorOfUndefined() {
		synchronized (this) {
			if(undefinedValue == null) {
				undefinedValue = new UndefinedImpl(this);
			}
		}
		return undefinedValue;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#mirrorOfNull()
	 */
	public NullValue mirrorOfNull() {
		synchronized (this) {
			if(nullValue == null) {
				nullValue = new NullImpl(this);
			}
		}
		return nullValue;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#mirrorOf(boolean)
	 */
	public BooleanValue mirrorOf(boolean bool) {
		return new BooleanImpl(this, bool);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#mirrorOf(java.lang.Number)
	 */
	public NumberValue mirrorOf(Number number) {
		return new NumberImpl(this, number);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#mirrorOf(java.lang.String)
	 */
	public StringValue mirrorOf(String string) {
		return new StringImpl(this, string);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#eventRequestManager()
	 */
	public EventRequestManager eventRequestManager() {
		return ermanager;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#eventQueue()
	 */
	public EventQueue eventQueue() {
		return queue;
	}
	
	/**
	 * Sends a request to the underlying {@link DebugSession}, waiting
	 * for the {@link VirtualMachine#DEFAULT_TIMEOUT}.
	 * 
	 * @param request
	 * @return the {@link CFResponse} for the request
	 */
	public Response sendRequest(Request request) {
		try {
			session.send(request);
			return session.receiveResponse(request.getSequence(), 3000);
		}
		catch(DisconnectedException de) {
			disconnectVM();
			handleException(de.getMessage(), de.getCause());
		}
		catch(TimeoutException te) {
			ChromePlugin.log(te);
		}
		return null;
	}
	
	/**
	 * Receives an {@link EventPacket} from the underlying {@link DebugSession}, 
	 * waiting for the {@link VirtualMachine#DEFAULT_TIMEOUT}.
	 * 
	 * @return the next {@link EventPacket} never <code>null</code>
	 * @throws TimeoutException
	 * @throws DisconnectedException
	 */
	public Event receiveEvent() throws TimeoutException, DisconnectedException {
		return (Event) session.receive(EventPacketImpl.EVENT, DEFAULT_TIMEOUT);
	}

	/**
	 * Receives an {@link EventPacket} from the underlying {@link DebugSession}, 
	 * waiting for the {@link VirtualMachine#DEFAULT_TIMEOUT}.
	 * @param timeout
	 * @return the next {@link EventPacket} never <code>null</code>
	 * @throws TimeoutException
	 * @throws DisconnectedException
	 */
	public Event receiveEvent(int timeout) throws TimeoutException, DisconnectedException {
		return (Event) session.receive(EventPacketImpl.EVENT, timeout);
	}
	
	/**
	 * disconnects the VM
	 */
	public synchronized void disconnectVM() {
		if (state == DISCONNECTED) {
			if(TRACE) {
				Tracing.writeString("VM [already disconnected]"); //$NON-NLS-1$
			}
			return;
		}
		if(TRACE) {
			Tracing.writeString("VM [disconnecting]"); //$NON-NLS-1$
		}
		try {
			if(threads != null) {
				threads.clear();
			}
			if(scripts != null) {
				scripts.clear();
			}
			this.session.dispose();
		} finally {
			state = DISCONNECTED;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#canUpdateBreakpoints()
	 */
	public boolean canUpdateBreakpoints() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#updateBreakpoint(org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpoint)
	 */
	public void updateBreakpoint(IJavaScriptBreakpoint breakpoint) {
	}
}
