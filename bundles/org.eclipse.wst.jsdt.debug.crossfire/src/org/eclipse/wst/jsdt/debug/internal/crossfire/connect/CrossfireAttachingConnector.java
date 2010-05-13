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
package org.eclipse.wst.jsdt.debug.internal.crossfire.connect;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.AttachingConnector;
import org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi.CFVirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Connection;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.SocketTransportService;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.TransportService;

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

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#defaultArguments()
	 */
	public Map defaultArguments() {
		Map args = new HashMap(4);
		args.put(HostArgument.HOST, new HostArgument(null));
		args.put(PortArgument.PORT, new PortArgument(5000));
		args.put(TimeoutArgument.TIMEOUT, new TimeoutArgument());
		args.put(BrowserArgument.BROWSER, new BrowserArgument());
		return args;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#description()
	 */
	public String description() {
		return Messages.attach_connector_desc;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#name()
	 */
	public String name() {
		return Messages.crossfire_remote_attach;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#id()
	 */
	public String id() {
		return CROSSFIRE_REMOTE_ATTACH_CONNECTOR_ID;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.AttachingConnector#attach(java.util.Map)
	 */
	public VirtualMachine attach(Map arguments) throws IOException {
		String str = (String)arguments.get(BrowserArgument.BROWSER);
		boolean browser = Boolean.valueOf(str).booleanValue();
		TransportService service = new SocketTransportService();
		String host = (String) arguments.get(HostArgument.HOST);
		String port = (String) arguments.get(PortArgument.PORT);
		if(browser && !host.equals(TransportService.LOCALHOST)) {
			//we cannot auto launch the browser on a different host
			return null;
		}
		if(browser) {
			launchBrowser(host, port);
		}
		String timeoutstr = (String) arguments.get(TimeoutArgument.TIMEOUT);
		int timeout = Integer.parseInt(timeoutstr);
		StringBuffer buffer = new StringBuffer();
		buffer.append(host).append(':').append(Integer.parseInt(port));
		Connection c = service.attach(buffer.toString(), timeout, timeout);
		DebugSession session = new DebugSession(c);
		return new CFVirtualMachine(session);
	}
	
	/**
	 * Launch the browser on the given host / port
	 * @param host
	 * @param port
	 * @throws IOException 
	 */
	void launchBrowser(final String host, final String port) throws IOException {
		StringBuffer buffer = new StringBuffer("firefox -P crossfire -no-remote -crossfire-host "); //$NON-NLS-1$
		buffer.append(host).append(" -crossfire-port ").append(port); //$NON-NLS-1$
	}
}
