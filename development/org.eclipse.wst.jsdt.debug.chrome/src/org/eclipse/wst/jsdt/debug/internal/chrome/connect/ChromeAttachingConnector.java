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
		args.put(PortArgument.PORT, new PortArgument(5000));
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
		boolean browser = Boolean.valueOf(str).booleanValue();
		TransportService service = new ChromeTransportService();
		String host = (String) arguments.get(HostArgument.HOST);
		String port = (String) arguments.get(PortArgument.PORT);
		if(browser) {
			launchBrowser(host, port);
		}
		Connection c = service.attach(host + ":" + Integer.parseInt(port), 10000, 10000); //$NON-NLS-1$
		DebugSession session = new DebugSession(c);
		return new VMImpl(session);
	}
	
	/**
	 * Launch the browser on the given host / port
	 * @param host
	 * @param port
	 * @throws IOException 
	 */
	void launchBrowser(final String host, final String port) throws IOException {
		Thread thread = new Thread() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					ChromePlugin.log(e);
				}
				StringBuffer buffer = new StringBuffer("/opt/google/chrome/chrome --remote-shell-port= "); //$NON-NLS-1$
				buffer.append(port);
				try {
					Runtime.getRuntime().exec(buffer.toString());
				} catch (IOException e) {
					ChromePlugin.log(e);
				}
			};
		};
		thread.start();
	}
}
