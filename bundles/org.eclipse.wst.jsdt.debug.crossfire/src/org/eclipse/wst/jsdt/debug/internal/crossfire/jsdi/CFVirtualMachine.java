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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.jsdt.debug.core.jsdi.BooleanValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.NullValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.NumberValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.StringValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.UndefinedValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventQueue;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager;
import org.eclipse.wst.jsdt.debug.internal.crossfire.Constants;
import org.eclipse.wst.jsdt.debug.internal.crossfire.CrossFirePlugin;
import org.eclipse.wst.jsdt.debug.internal.crossfire.Tracing;
import org.eclipse.wst.jsdt.debug.internal.crossfire.event.CFEventQueue;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Attributes;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Commands;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.DisconnectedException;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Event;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.JSON;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Request;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Response;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.TimeoutException;

/**
 * Default CrossFire implementation of {@link VirtualMachine}
 * 
 * @since 1.0
 */
public class CFVirtualMachine extends CFMirror implements VirtualMachine {

	private final NullValue nullvalue = new CFNullValue(this);
	private final UndefinedValue undefinedvalue = new CFUndefinedValue(this);
	
	private final DebugSession session;
	private final CFEventRequestManager ermanager = new CFEventRequestManager(this);
	private final CFEventQueue queue = new CFEventQueue(this, ermanager);
	private boolean disconnected = false;
	
	private Map threads = null;
	private Map scripts = null;
	
	/**
	 * Constructor
	 * 
	 * @param session
	 */
	public CFVirtualMachine(DebugSession session) {
		super();
		this.session = session;
	}

	/**
	 * @return the 'readiness' of the VM - i.e. is it in a state to process requests, etc
	 */
	boolean ready() {
		return !disconnected;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#resume()
	 */
	public void resume() {
		if(ready()) {
			//TODO make this work
			Request request = new Request(Commands.CONTINUE, null);
			Response response = sendRequest(request);
			if(response.isSuccess()) {
				if(threads != null) {
					Entry entry = null;
					for (Iterator iter = threads.entrySet().iterator(); iter.hasNext();) {
						entry = (Entry) iter.next();
						CFThreadReference thread = (CFThreadReference) entry.getValue();
						if(thread.isSuspended()) {
							thread.markSuspended(false);
						}
					}
				}
			}
			else if(TRACE) {
				Tracing.writeString("VM [failed continue request]: "+JSON.serialize(request)); //$NON-NLS-1$
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#suspend()
	 */
	public void suspend() {
		if(ready()) {
			//TODO make this work
			Request request = new Request(Commands.SUSPEND, null);
			Response response = sendRequest(request);
			if(response.isSuccess()) {
				if(threads != null) {
					Entry entry = null;
					for (Iterator iter = threads.entrySet().iterator(); iter.hasNext();) {
						entry = (Entry) iter.next();
						CFThreadReference thread = (CFThreadReference) entry.getValue();
						if(!thread.isSuspended()) {
							thread.markSuspended(true);
						}
					}
				}
			}
			else if(TRACE) {
				Tracing.writeString("VM [failed suspend request]: "+JSON.serialize(request)); //$NON-NLS-1$
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#terminate()
	 */
	public void terminate() {
		if(ready()) {
			disconnectVM();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#name()
	 */
	public String name() {
		return NLS.bind(Messages.vm_name, version());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#description()
	 */
	public String description() {
		return Messages.crossfire_vm;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#version()
	 */
	public synchronized String version() {
		if(ready()) {
			Request request = new Request(Commands.VERSION, null);
			Response response = sendRequest(request);
			if(response.isSuccess()) {
				Map json = response.getBody();
				return (String) json.get(Commands.VERSION);
			}
			if(TRACE) {
				Tracing.writeString("VM [failed version request]: "+JSON.serialize(request)); //$NON-NLS-1$
			}
		}
		return Constants.UNKNOWN;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#allThreads()
	 */
	public synchronized List allThreads() {
		if(threads == null) {
			threads = new HashMap();
			Request request = new Request(Commands.LISTCONTEXTS, null);
			Response response = sendRequest(request);
			if(response.isSuccess()) {
				List contexts = (List) response.getBody().get(Attributes.CONTEXTS);
				for (Iterator iter = contexts.iterator(); iter.hasNext();) {
					Map json = (Map) iter.next();
					CFThreadReference thread = new CFThreadReference(this, json);
					threads.put(thread.id(), thread);
				}
			}
			else if(TRACE) {
				Tracing.writeString("VM [failed allthreads request]: "+JSON.serialize(request)); //$NON-NLS-1$
			}
		}
		return new ArrayList(threads.values());
	}

	/**
	 * Adds a thread to the listing
	 * 
	 * @param id
	 * @param href
	 * @return the new thread
	 */
	public CFThreadReference addThread(String id, String href) {
		if(threads == null) {
			allThreads();
		}
		CFThreadReference thread  = new CFThreadReference(this, id, href);
		threads.put(thread.id(), thread);
		return thread;
	}
	
	/**
	 * Removes the thread with the given id
	 * 
	 * @param id the id of the thread to remove
	 */
	public void removeThread(String id) {
		if(threads != null) {
			Object obj = threads.remove(id);
			if(TRACE && obj == null) {
				Tracing.writeString("VM [failed to remove thread]: "+id); //$NON-NLS-1$
			}
		}
	}
	
	/**
	 * Returns the thread with the given id or <code>null</code>
	 * 
	 * @param id
	 * @return the thread or <code>null</code>
	 */
	public synchronized CFThreadReference findThread(String id) {
		if(threads == null) {
			allThreads();
		}
		CFThreadReference thread = (CFThreadReference) threads.get(id);
		if(TRACE && thread == null) {
			Tracing.writeString("VM [failed to find thread]: "+id); //$NON-NLS-1$
		}
		return thread;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#allScripts()
	 */
	public synchronized List allScripts() {
		if(scripts == null) {
			scripts = new HashMap();
			List threads = allThreads();
			for (Iterator iter = threads.iterator(); iter.hasNext();) {
				CFThreadReference thread = (CFThreadReference) iter.next();
				Request request = new Request(Commands.SCRIPTS, thread.id());
				request.setArgument(Attributes.INCLUDE_SOURCE, Boolean.TRUE);
				Response response = sendRequest(request);
				if(response.isSuccess()) {
					List scriptz = (List) response.getBody().get(Commands.SCRIPTS);
					for (Iterator iter2 = scriptz.iterator(); iter2.hasNext();) {
						CFScriptReference script = new CFScriptReference(this, thread.id(), (Map) iter2.next()); 
						scripts.put(script.id(), script);
					}
				}
				else if(TRACE) {
					Tracing.writeString("VM [failed scripts request]: "+JSON.serialize(request)); //$NON-NLS-1$
				}
			}
		}
		return new ArrayList(scripts.values());
	}

	/**
	 * Returns the script with the given id or <code>null</code>
	 * 
	 * @param id
	 * @return the thread or <code>null</code>
	 */
	public synchronized CFScriptReference findScript(String id) {
		if(scripts == null) {
			allScripts();
		}
		CFScriptReference script = (CFScriptReference) scripts.get(id);
		//if we find we have a script id that is not cached, we should try a lookup + add in the vm
		if(TRACE && script == null) {
			Tracing.writeString("VM [failed to find script]: "+id); //$NON-NLS-1$
		}
		return script;
	}
	
	/**
	 * Adds the given script to the listing
	 * 
	 * @param context_id
	 * @param json
	 * @return the new script
	 */
	public CFScriptReference addScript(String context_id, Map json) {
		if(scripts == null) {
			allScripts();
		}
		CFScriptReference script = new CFScriptReference(this, context_id, json);
		scripts.put(script.id(), script);
		return script;
	}
	
	/**
	 * Removes the script with the given id form the listing
	 * 
	 * @param id the script to remove
	 */
	public void removeScript(String id) {
		if(scripts != null) {
			Object obj = scripts.remove(id);
			if(TRACE && obj == null) {
				Tracing.writeString("VM [failed to remove script]: "+id); //$NON-NLS-1$
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#dispose()
	 */
	public synchronized void dispose() {
		try {
			if(TRACE) {
				Tracing.writeString("VM [disposing]"); //$NON-NLS-1$
			}
			queue.dispose();
			ermanager.dispose();
		}
		finally {
			//fall-back in case the VM has been disposed but not disconnected
			disconnectVM();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#mirrorOfUndefined()
	 */
	public UndefinedValue mirrorOfUndefined() {
		return undefinedvalue;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#mirrorOfNull()
	 */
	public NullValue mirrorOfNull() {
		return nullvalue;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#mirrorOf(boolean)
	 */
	public BooleanValue mirrorOf(boolean bool) {
		return new CFBooleanValue(this, bool);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#mirrorOf(java.lang.Number)
	 */
	public NumberValue mirrorOf(Number number) {
		return new CFNumberValue(this, number);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#mirrorOf(java.lang.String)
	 */
	public StringValue mirrorOf(String string) {
		return new CFStringValue(this, string);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#eventRequestManager()
	 */
	public synchronized EventRequestManager eventRequestManager() {
		return ermanager;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#eventQueue()
	 */
	public synchronized EventQueue eventQueue() {
		return queue;
	}
	
	/**
	 * Receives an {@link Event} from the underlying {@link DebugSession}, 
	 * waiting for the {@link VirtualMachine#DEFAULT_TIMEOUT}.
	 * 
	 * @return the next {@link Event} never <code>null</code>
	 * @throws TimeoutException
	 * @throws DisconnectedException
	 */
	public Event receiveEvent() throws TimeoutException, DisconnectedException {
		return session.receiveEvent(DEFAULT_TIMEOUT);
	}

	/**
	 * Receives an {@link Event} from the underlying {@link DebugSession}, 
	 * waiting for the {@link VirtualMachine#DEFAULT_TIMEOUT}.
	 * @param timeout
	 * @return the next {@link Event} never <code>null</code>
	 * @throws TimeoutException
	 * @throws DisconnectedException
	 */
	public Event receiveEvent(int timeout) throws TimeoutException, DisconnectedException {
		return session.receiveEvent(timeout);
	}
	
	/**
	 * Sends a request to the underlying {@link DebugSession}, waiting
	 * for the {@link VirtualMachine#DEFAULT_TIMEOUT}.
	 * 
	 * @param request
	 * @return the {@link Response} for the request
	 */
	public Response sendRequest(Request request) {
		try {
			session.sendRequest(request);
			return session.receiveResponse(request.getSequence(), 3000);
		}
		catch(DisconnectedException de) {
			disconnectVM();
			handleException(de.getMessage(), de);
		}
		catch(TimeoutException te) {
			CrossFirePlugin.log(te);
		}
		return Response.FAILED;
	}
	
	/**
	 * disconnects the VM
	 */
	public synchronized void disconnectVM() {
		if (disconnected) {
			// no-op it is already disconnected
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
			this.queue.dispose();
			this.ermanager.dispose();
			this.session.dispose();
		} finally {
			disconnected = true;
		}
	}
}
