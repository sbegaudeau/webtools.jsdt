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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointListener;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptLineBreakpoint;
import org.eclipse.wst.jsdt.debug.core.jsdi.BooleanValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.NullValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.NumberValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.StringValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.UndefinedValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventQueue;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager;
import org.eclipse.wst.jsdt.debug.core.model.JavaScriptDebugModel;
import org.eclipse.wst.jsdt.debug.internal.core.JavaScriptDebugPlugin;
import org.eclipse.wst.jsdt.debug.internal.crossfire.Constants;
import org.eclipse.wst.jsdt.debug.internal.crossfire.CrossFirePlugin;
import org.eclipse.wst.jsdt.debug.internal.crossfire.Tracing;
import org.eclipse.wst.jsdt.debug.internal.crossfire.event.CFEventQueue;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Attributes;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFEventPacket;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFRequestPacket;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFResponsePacket;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Commands;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.JSON;
import org.eclipse.wst.jsdt.debug.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.transport.exception.DisconnectedException;
import org.eclipse.wst.jsdt.debug.transport.exception.TimeoutException;

/**
 * Default CrossFire implementation of {@link VirtualMachine}
 * 
 * @since 1.0
 */
public class CFVirtualMachine extends CFMirror implements VirtualMachine, IBreakpointListener {

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
		DebugPlugin.getDefault().getBreakpointManager().addBreakpointListener(this);
	}

	/**
	 * Collects all of the breakpoints 
	 */
	void initializeBreakpoints() {
		List threads = allThreads();
		for (Iterator i = threads.iterator(); i.hasNext();) {
			CFThreadReference thread = (CFThreadReference) i.next();
			CFRequestPacket request = new CFRequestPacket(Commands.GET_BREAKPOINTS, thread.id());
			CFResponsePacket response = sendRequest(request);
			if(response.isSuccess()) {
				//TODO init breakpoints?
			}
			else if(TRACE) {
				Tracing.writeString("VM [failed getbreakpoints request]: "+JSON.serialize(request)); //$NON-NLS-1$
			}
		}
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
			if(threads != null) {
				Entry entry = null;
				for (Iterator iter = threads.entrySet().iterator(); iter.hasNext();) {
					entry = (Entry) iter.next();
					CFThreadReference thread = (CFThreadReference) entry.getValue();
					if(thread.isSuspended()) {
						CFRequestPacket request = new CFRequestPacket(Commands.CONTINUE, thread.id());
						CFResponsePacket response = sendRequest(request);
						if(response.isSuccess()) {
							if(thread.isSuspended()) {
								thread.markSuspended(false);
							}
						}
						else if(TRACE) {
							Tracing.writeString("VM [failed continue request][context: "+thread.id()+"]: "+JSON.serialize(request)); //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#suspend()
	 */
	public void suspend() {
		if(ready()) {
			if(threads != null) {
				Entry entry = null;
				for (Iterator iter = threads.entrySet().iterator(); iter.hasNext();) {
					entry = (Entry) iter.next();
					CFThreadReference thread = (CFThreadReference) entry.getValue();
					if(thread.isRunning()) {
						CFRequestPacket request = new CFRequestPacket(Commands.SUSPEND, thread.id());
						CFResponsePacket response = sendRequest(request);
						if(response.isSuccess()) {
							if(!thread.isSuspended()) {
								thread.markSuspended(true);
							}
						}
						else if(TRACE) {
							Tracing.writeString("VM [failed suspend request]: "+JSON.serialize(request)); //$NON-NLS-1$
						}
					}
				}
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
		return Messages.vm_name;
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
			CFRequestPacket request = new CFRequestPacket(Commands.VERSION, null);
			CFResponsePacket response = sendRequest(request);
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
			CFRequestPacket request = new CFRequestPacket(Commands.LISTCONTEXTS, null);
			CFResponsePacket response = sendRequest(request);
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
				CFRequestPacket request = new CFRequestPacket(Commands.SCRIPTS, thread.id());
				request.setArgument(Attributes.INCLUDE_SOURCE, Boolean.FALSE);
				CFResponsePacket response = sendRequest(request);
				if(response.isSuccess()) {
					List scriptz = (List) response.getBody().get(Commands.SCRIPTS);
					for (Iterator iter2 = scriptz.iterator(); iter2.hasNext();) {
						Map smap = (Map) iter2.next();
						Map scriptjson = (Map) smap.get(Attributes.SCRIPT);
						if(scriptjson != null) {
							CFScriptReference script = new CFScriptReference(this, thread.id(), scriptjson); 
							scripts.put(script.id(), script);
						}
					}
				}
				else if(TRACE) {
					Tracing.writeString("VM [failed scripts request]: "+JSON.serialize(request)); //$NON-NLS-1$
				}
			}
			if(scripts.size() < 1) {
				scripts = null;
				return Collections.EMPTY_LIST;
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
		CFScriptReference script = null;
		if(scripts != null) {
			//the scripts collection can be null after a call to allScripts()
			//when the remote target had no scripts loaded. In this case
			//we do not keep the initialized collection so that any successive 
			//calls the this method or allScripts will cause the remote target 
			//to be asked for all of its scripts
			script = (CFScriptReference) scripts.get(id);
		}
		//if we find we have a script id that is not cached, we should try a lookup + add in the vm
		if(script == null) {
			if(TRACE) {
				Tracing.writeString("VM [failed to find script]: "+id); //$NON-NLS-1$
			}
		}
		return script;
	}
	
	/**
	 * Adds the given script to the listing
	 * 
	 * @param context_id
	 * @param json
	 * 
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
	 * Removes all {@link CFScriptReference}s from the cache when the associated context is destroyed
	 * 
	 * @param contextid
	 */
	public void removeScriptsForContext(String contextid) {
		if(scripts != null) {
			Entry e = null;
			for(Iterator i = scripts.entrySet().iterator(); i.hasNext();) {
				e = (Entry) i.next();
				if(contextid.equals(((CFScriptReference)e.getValue()).context())) {
					i.remove();
				}
			}
		}
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
	 * Receives an {@link CFEventPacket} from the underlying {@link DebugSession}, 
	 * waiting for the {@link VirtualMachine#DEFAULT_TIMEOUT}.
	 * 
	 * @return the next {@link CFEventPacket} never <code>null</code>
	 * @throws TimeoutException
	 * @throws DisconnectedException
	 */
	public CFEventPacket receiveEvent() throws TimeoutException, DisconnectedException {
		return (CFEventPacket) session.receive(CFEventPacket.EVENT, DEFAULT_TIMEOUT);
	}

	/**
	 * Receives an {@link CFEventPacket} from the underlying {@link DebugSession}, 
	 * waiting for the {@link VirtualMachine#DEFAULT_TIMEOUT}.
	 * @param timeout
	 * @return the next {@link CFEventPacket} never <code>null</code>
	 * @throws TimeoutException
	 * @throws DisconnectedException
	 */
	public CFEventPacket receiveEvent(int timeout) throws TimeoutException, DisconnectedException {
		return (CFEventPacket) session.receive(CFEventPacket.EVENT, timeout);
	}
	
	/**
	 * Sends a request to the underlying {@link DebugSession}, waiting
	 * for the {@link VirtualMachine#DEFAULT_TIMEOUT}.
	 * 
	 * @param request
	 * @return the {@link CFResponsePacket} for the request
	 */
	public CFResponsePacket sendRequest(CFRequestPacket request) {
		try {
			session.send(request);
			return (CFResponsePacket) session.receiveResponse(request.getSequence(), 3000);
		}
		catch(DisconnectedException de) {
			disconnectVM();
			handleException(de.getMessage(), (de.getCause() == null ? de : de.getCause()));
		}
		catch(TimeoutException te) {
			CrossFirePlugin.log(te);
		}
		return CFResponsePacket.FAILED;
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
			DebugPlugin.getDefault().getBreakpointManager().removeBreakpointListener(this);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.IBreakpointListener#breakpointAdded(org.eclipse.debug.core.model.IBreakpoint)
	 */
	public void breakpointAdded(IBreakpoint breakpoint) {
		//TODO this will never work unless we map original URL to workspace source
		if(JavaScriptDebugModel.MODEL_ID.equals(breakpoint.getModelIdentifier())) {
			if(breakpoint instanceof IJavaScriptLineBreakpoint) {
				IJavaScriptLineBreakpoint bp = (IJavaScriptLineBreakpoint) breakpoint;
				try {
					String path = bp.getScriptPath();
					String url = JavaScriptDebugPlugin.getExternalScriptPath(new Path(path));
					if(url == null) {
						url = path;
					}
					if(url != null) {
						CFScriptReference script = findScript(url);
						if(script != null) {
							CFRequestPacket request = new CFRequestPacket(Commands.SET_BREAKPOINT, script.context());
							request.setArgument(Attributes.LINE, new Integer(bp.getLineNumber()));
							request.setArgument(Attributes.TARGET, script.id());
							CFResponsePacket response = sendRequest(request);
							if(!response.isSuccess() && TRACE) {
								Tracing.writeString("[failed setbreakpoint request] "+JSON.serialize(request)); //$NON-NLS-1$
							}
						}
					}
				}
				catch(CoreException ce) {
					CrossFirePlugin.log(ce);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.IBreakpointListener#breakpointRemoved(org.eclipse.debug.core.model.IBreakpoint, org.eclipse.core.resources.IMarkerDelta)
	 */
	public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
		//TODO this will never work unless we map original URL to workspace source
		if(JavaScriptDebugModel.MODEL_ID.equals(breakpoint.getModelIdentifier())) {
			if(breakpoint instanceof IJavaScriptLineBreakpoint) {
				IJavaScriptLineBreakpoint bp = (IJavaScriptLineBreakpoint) breakpoint;
				try {
					String path = bp.getScriptPath();
					String url = JavaScriptDebugPlugin.getExternalScriptPath(new Path(path));
					if(url == null) {
						url = path;
					}
					if(url != null) {
						CFScriptReference script = findScript(url);
						if(script != null) {
							CFRequestPacket request = new CFRequestPacket(Commands.CLEAR_BREAKPOINT, script.context());
							request.setArgument(Attributes.LINE, new Integer(bp.getLineNumber()));
							request.setArgument(Attributes.TARGET, script.id());
							CFResponsePacket response = sendRequest(request);
							if(!response.isSuccess() && TRACE) {
								Tracing.writeString("[failed clearbreakpoint request] "+JSON.serialize(request)); //$NON-NLS-1$
							}
						}
					}
				}
				catch(CoreException ce) {
					CrossFirePlugin.log(ce);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.IBreakpointListener#breakpointChanged(org.eclipse.debug.core.model.IBreakpoint, org.eclipse.core.resources.IMarkerDelta)
	 */
	public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta) {
		//TODO this will never work unless we map original URL to workspace source
		if(delta.getKind() == IResourceDelta.CHANGED && JavaScriptDebugModel.MODEL_ID.equals(breakpoint.getModelIdentifier())) {
			IJavaScriptLineBreakpoint bp = (IJavaScriptLineBreakpoint) breakpoint;
			try {
				String path = bp.getScriptPath();
				String url = JavaScriptDebugPlugin.getExternalScriptPath(new Path(path));
				if(url == null) {
					url = path;
				}
				if(url != null) {
					CFScriptReference script = findScript(url);
					if(script != null) {
						CFRequestPacket request = new CFRequestPacket(Commands.CHANGE_BREAKPOINT, script.context());
						request.setArgument(Attributes.LINE, new Integer(bp.getLineNumber()));
						request.setArgument(Attributes.TARGET, script.id());
						String cn = bp.getCondition();
						request.setArgument(Attributes.CONDITION, (cn == null ? "" : cn)); //$NON-NLS-1$
						CFResponsePacket response = sendRequest(request);
						if(!response.isSuccess() && TRACE) {
							Tracing.writeString("[failed changebreakpoint request] "+JSON.serialize(request)); //$NON-NLS-1$
						}
					}
				}
			}
			catch(CoreException ce) {
				CrossFirePlugin.log(ce);
			}
		}
	}
}
