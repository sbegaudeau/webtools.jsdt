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
package org.eclipse.wst.jsdt.debug.internal.crossfire.connect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.AttachingConnector;
import org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi.CFVirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFTransportService;
import org.eclipse.wst.jsdt.debug.transport.Connection;
import org.eclipse.wst.jsdt.debug.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.transport.TransportService;

/**
 * Attaching connector for Crossfire
 * 
 * @since 1.0
 */
public class CrossfireAttachingConnector implements AttachingConnector {

	public static final String CROSSFIRE_REMOTE_ATTACH_CONNECTOR_ID = "crossfire.remote.attach.connector"; //$NON-NLS-1$

	/**
	 * Constructor
	 */
	public CrossfireAttachingConnector() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#defaultArguments()
	 */
	public Map defaultArguments() {
		HashMap args = new HashMap(5);
		args.put(HostArgument.HOST, new HostArgument(null));
		args.put(PortArgument.PORT, new PortArgument(5000));
		args.put(TimeoutArgument.TIMEOUT, new TimeoutArgument());
		args.put(ConsoleArgument.CONSOLE, new ConsoleArgument());
		args.put(DOMArgument.DOM, new DOMArgument());
		args.put(InspectorArgument.INSPECTOR, new InspectorArgument());
		args.put(NetArgument.NET, new NetArgument());
		args.put(TraceArgument.TRACE, new TraceArgument());
		//XXX hack because there is no good way to find the Firefox executable on Win
		if(!Platform.OS_WIN32.equals(Platform.getOS())) {
			args.put(BrowserArgument.BROWSER, new BrowserArgument());
		}
		return args;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#description()
	 */
	public String description() {
		return Messages.attach_connector_desc;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#name()
	 */
	public String name() {
		return Messages.crossfire_remote_attach;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#id()
	 */
	public String id() {
		return CROSSFIRE_REMOTE_ATTACH_CONNECTOR_ID;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.AttachingConnector#attach
	 * (java.util.Map)
	 */
	public VirtualMachine attach(Map arguments) throws IOException {
		String str = (String) arguments.get(BrowserArgument.BROWSER);
		//XXX hack because there is no good way to find the Firefox executable on Win
		boolean browser = Boolean.valueOf(str).booleanValue() && !Platform.OS_WIN32.equals(Platform.getOS());
		if (browser && !HostArgument.isLocalhost((String) arguments.get(HostArgument.HOST))) {
			// we cannot auto launch the browser on a different host
			throw new IOException(Messages.cannot_launch_browser_not_localhost);
		}
		Connection c = null;
		if (browser) {
			c = launchForBrowser(arguments);
		} else {
			c = launch(arguments);
		}
		DebugSession session = new DebugSession(c);
		return new CFVirtualMachine(session);
	}

	/**
	 * Launches the browser and connects to it. This method will poll for the
	 * browser to be launched but only for a fixed timeout.
	 * 
	 * @param arguments
	 * @return the created connection or <code>null</code> if the attempt to
	 *         connect times out, the browser process terminates before we can
	 *         connect
	 * @throws IOException
	 */
	Connection launchForBrowser(final Map arguments) throws IOException {
		String host = (String) arguments.get(HostArgument.HOST);
		String port = (String) arguments.get(PortArgument.PORT);
		StringBuffer buffer = new StringBuffer("firefox -ProfileManager -load-fb-modules -crossfire-server-port ").append(port); //$NON-NLS-1$
		Process p = Runtime.getRuntime().exec(buffer.toString());
		TransportService service = new CFTransportService(getToolArgs(arguments));
		String timeoutstr = (String) arguments.get(TimeoutArgument.TIMEOUT);
		int timeout = Integer.parseInt(timeoutstr);
		buffer = new StringBuffer();
		buffer.append(host).append(':').append(Integer.parseInt(port));
		long timer = System.currentTimeMillis() + 20000;
		Connection c = null;
		while (p != null && System.currentTimeMillis() < timer && c == null) {
			try {
				c = service.attach(buffer.toString(), timeout,timeout);
			} catch (IOException ioe) {
				// ignore while pinging to connect
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					//do nothing
				}
			}
		}
		if (c == null) {
			throw new IOException(NLS.bind(Messages.failed_to_attach_to_auto_browser, new String[] {host, port }));
		}
		return c;
	}

	/**
	 * Tries to connect to the given
	 * 
	 * @param arguments
	 * @return the {@link Connection} or throws an exception
	 * @throws IOException
	 */
	Connection launch(Map arguments) throws IOException {
		TransportService service = new CFTransportService(getToolArgs(arguments));
		String host = (String) arguments.get(HostArgument.HOST);
		String port = (String) arguments.get(PortArgument.PORT);
		String timeoutstr = (String) arguments.get(TimeoutArgument.TIMEOUT);
		int timeout = Integer.parseInt(timeoutstr);
		StringBuffer buffer = new StringBuffer();
		buffer.append(host).append(':').append(Integer.parseInt(port));
		return service.attach(buffer.toString(), timeout, timeout);
	}
	
	String[] getToolArgs(Map arguments) {
		ArrayList tools = new ArrayList();
		String value = (String) arguments.get(ConsoleArgument.CONSOLE);
		if(Boolean.valueOf(value).booleanValue()) {
			tools.add(ConsoleArgument.CONSOLE);
		}
		value = (String) arguments.get(DOMArgument.DOM);
		if(Boolean.valueOf(value).booleanValue()) {
			tools.add(DOMArgument.DOM);
		}
		value = (String) arguments.get(InspectorArgument.INSPECTOR);
		if(Boolean.valueOf(value).booleanValue()) {
			tools.add(InspectorArgument.INSPECTOR);
		}
		value = (String) arguments.get(NetArgument.NET);
		if(Boolean.valueOf(value).booleanValue()) {
			tools.add(NetArgument.NET);
		}
		value = (String) arguments.get(TraceArgument.TRACE);
		if(Boolean.valueOf(value).booleanValue()) {
			tools.add(TraceArgument.TRACE);
		}
		return (String[]) tools.toArray(new String[tools.size()]);
	}
}
