/*******************************************************************************
 * Copyright (c) 2011, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.opera.internal.jsdi;

import java.util.Collections;
import java.util.List;

import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpoint;
import org.eclipse.wst.jsdt.debug.core.jsdi.BooleanValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.NullValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.NumberValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.StringValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.UndefinedValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventQueue;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequestManager;

/**
 * {@link VirtualMachine} for Opera support
 * 
 * @since 0.1
 */
public class VirtualMachineImpl extends MirrorImpl implements VirtualMachine {

	private static UndefinedValue undefinedmirror = null;
	private static NullValue nullmirror = null;
	private static Object rmilock = new Object();
	private static Object eqlock = new Object();
	
//	private ScopeClient client = null;
	private String name = null;
	private RequestManagerImpl rmi = null;
	private EventQueueImpl eqi = null;
	
	/**
	 * Constructor
	 * @param client
	 */
//	public VirtualMachineImpl(ScopeClient client) {
//		super();
//		this.client = client;
//	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#resume()
	 */
	public void resume() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#suspend()
	 */
	public void suspend() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#terminate()
	 */
	public void terminate() {
//		client.getClientLock().release();
//		client.close();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#name()
	 */
	public synchronized String name() {
		if(name == null) {
			name = description();
		}
		return name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#description()
	 */
	public String description() {
		return Messages.vm_desc;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#version()
	 */
	public String version() {
		StringBuffer buff = new StringBuffer("stp: "); //$NON-NLS-1$
//		ScopeProtos.HostInfo.Builder b = ScopeProtos.HostInfo.newBuilder(); 
//		buff.append(Integer.toString(b.getStpVersion())).append(" "); //$NON-NLS-1$
//		buff.append("core: ").append(b.getCoreVersion()); //$NON-NLS-1$
//		buff.append("agent: ").append(b.getUserAgent()); //$NON-NLS-1$
//		buff.append("plat: ").append(b.getPlatform()); //$NON-NLS-1$
//		buff.append("os: ").append(b.getOperatingSystem()); //$NON-NLS-1$
		return buff.toString();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#allThreads()
	 */
	public List allThreads() {
		//TODO send threads request
		return Collections.EMPTY_LIST;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#allScripts()
	 */
	public List allScripts() {
//		MessageCallback callback = new MessageCallback() {
//			public void onMessageReceived(Object t) {
//				System.out.println("got runtimes"); //$NON-NLS-1$
//			}
//
//			public void onError(ScopeErrorException ex) {
//				System.err.println("got exception"); //$NON-NLS-1$
//			}
//		};
//		Command.Builder command = Command.newBuilder();
//		command.setCommandID(ESDebuggerCommand.LIST_RUNTIMES.getId());
//		command.setFormat(ProtocolFormat.JSON.getValue().intValue());
//		
//		command.setPayload(ByteString.EMPTY);
//		command.setService(ESDebuggerCommand.LIST_RUNTIMES.getName());
//        command.setTag(client.getNextTag());
//		client.sendCommand(command.build(), callback);
		return Collections.EMPTY_LIST;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#dispose()
	 */
	public void dispose() {
//		client.close();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#mirrorOfUndefined()
	 */
	public synchronized UndefinedValue mirrorOfUndefined() {
		if(undefinedmirror == null) {
			undefinedmirror = new UndefinedImpl(this);
		}
		return undefinedmirror;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#mirrorOfNull()
	 */
	public synchronized NullValue mirrorOfNull() {
		if(nullmirror == null) {
			nullmirror = new NullValueImpl(this);
		}
		return nullmirror;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#mirrorOf(boolean)
	 */
	public BooleanValue mirrorOf(boolean bool) {
		return new BooleanValueImpl(this, bool);
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
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#eventRequestManager()
	 */
	public EventRequestManager eventRequestManager() {
		synchronized (rmilock) {
			if(rmi == null) {
				rmi = new RequestManagerImpl(this);
			}
		}
		return rmi;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine#eventQueue()
	 */
	public EventQueue eventQueue() {
		synchronized (eqlock) {
			if(eqi == null) {
				eqi = new EventQueueImpl(this);
			}
		}
		return eqi;
	}

	@Override
	public boolean canUpdateBreakpoints() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateBreakpoint(IJavaScriptBreakpoint breakpoint) {
		// TODO Auto-generated method stub
		
	}
}