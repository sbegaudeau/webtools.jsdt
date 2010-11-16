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
package org.eclipse.wst.jsdt.debug.internal.chrome.connect;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.AttachingConnector;
import org.eclipse.wst.jsdt.debug.internal.chrome.ChromePlugin;
import org.eclipse.wst.jsdt.debug.internal.chrome.jsdi.VMImpl;
import org.eclipse.wst.jsdt.debug.internal.chrome.transport.ChromeTransportService;
import org.eclipse.wst.jsdt.debug.transport.Connection;
import org.eclipse.wst.jsdt.debug.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.transport.TransportService;


/**
 * Default attaching connector
 * 
 * @since 1.0
 */
public class ChromeAttachingConnector implements AttachingConnector {

	public static final String ID = "org.eclipse.wst.jsdt.debug.chrome.connector.attach"; //$NON-NLS-1$
	
	/**
	 * Constructor
	 */
	public ChromeAttachingConnector() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#defaultArguments()
	 */
	public Map defaultArguments() {
		Map args = new HashMap();
		args.put(HostArgument.HOST, new HostArgument(null));
		args.put(PortArgument.PORT, new PortArgument(9222));
		args.put(TimeoutArgument.TIMEOUT, new TimeoutArgument());
		args.put(BrowserArgument.BROWSER, new BrowserArgument());
		return args;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#description()
	 */
	public String description() {
		return Messages.attach_to_google_chrome;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#name()
	 */
	public String name() {
		return Messages.google_chrome_attach;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#id()
	 */
	public String id() {
		return ID;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.AttachingConnector#attach(java.util.Map)
	 */
	public VirtualMachine attach(Map arguments) throws IOException {
		String str = (String)arguments.get(BrowserArgument.BROWSER);
		Connection c = null;
		if(Boolean.valueOf(str).booleanValue()) {
			c = launchForBrowser(arguments);
		}
		else {
			c = launch(arguments);
		}
		DebugSession session = new DebugSession(c);
		return new VMImpl(session);
	}
	
	/**
	 * Launches the browser and connects to it. This method will poll for the browser to be launched
	 * but only for a fixed timeout. 
	 * @param arguments
	 * @return the created connection or <code>null</code> if the attempt to connect times out, the browser process
	 * terminates before we can connect  
	 * @throws IOException 
	 */
	Connection launchForBrowser(Map arguments) throws IOException {
		TransportService service = new ChromeTransportService();
		String host = (String) arguments.get(HostArgument.HOST);
		String port = (String) arguments.get(PortArgument.PORT);
		StringBuffer buffer = new StringBuffer("/opt/google/chrome/chrome --remote-shell-port="); //$NON-NLS-1$
		buffer.append(port);
		Process proc = null;
		try {
			proc = Runtime.getRuntime().exec(buffer.toString());
		} catch (IOException e) {
			ChromePlugin.log(e);
		}
		String timeoutstr = (String) arguments.get(TimeoutArgument.TIMEOUT);
		int timeout = Integer.parseInt(timeoutstr);
		buffer = new StringBuffer();
		buffer.append(host).append(':').append(Integer.parseInt(port));
		Connection c = null;
		long timer = System.currentTimeMillis() + 60000;
		while(proc != null && System.currentTimeMillis() < timer && c == null) {
			try {
				c = service.attach(buffer.toString(), timeout, timeout);
			}
			catch(IOException ioe) {
				//ignore while pinging to connect
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if(c == null) {
			throw new IOException(NLS.bind("Failed to attach to debugger at {0} on port {1}", new String[] {host, port})); //$NON-NLS-1$
		}
		return c;
	}
	
	/**
	 * Tries to connect to the given 
	 * @param arguments
	 * @return the {@link Connection} or throws an exception
	 * @throws IOException
	 */
	Connection launch(Map arguments) throws IOException {
		TransportService service = new ChromeTransportService();
		String host = (String) arguments.get(HostArgument.HOST);
		String port = (String) arguments.get(PortArgument.PORT);
		String timeoutstr = (String) arguments.get(TimeoutArgument.TIMEOUT);
		int timeout = Integer.parseInt(timeoutstr);
		StringBuffer buffer = new StringBuffer();
		buffer.append(host).append(':').append(Integer.parseInt(port));
		return service.attach(buffer.toString(), timeout, timeout);
	}
}
