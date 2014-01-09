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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.ListeningConnector;
import org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi.CFVirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFTransportService;
import org.eclipse.wst.jsdt.debug.transport.Connection;
import org.eclipse.wst.jsdt.debug.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.transport.TransportService;

/**
 * Default launching connector for CrossFire
 * 
 * @since 1.0
 */
public class CrossfireListeningConnector implements ListeningConnector {
	
	/**
	 * The id of the connector
	 */
	public static final String CROSSFIRE_REMOTE_ATTACH_CONNECTOR_ID = "crossfire.remote.listen.connector"; //$NON-NLS-1$

	/**
	 * Constructor
	 */
	public CrossfireListeningConnector() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#defaultArguments()
	 */
	public Map defaultArguments() {
		HashMap args = new HashMap(5);
		args.put(HostArgument.HOST, new HostArgument(null));
		args.put(PortArgument.PORT, new PortArgument(5000));
		args.put(TimeoutArgument.TIMEOUT, new TimeoutArgument());
		args.put(ConsoleArgument.CONSOLE, new ConsoleArgument());
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
		return Messages.attach_connector_name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#id()
	 */
	public String id() {
		return CROSSFIRE_REMOTE_ATTACH_CONNECTOR_ID;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.ListeningConnector#accept(java.util.Map)
	 */
	public VirtualMachine accept(Map arguments) throws IOException {
		TransportService service = new CFTransportService(null);
		String host = (String) arguments.get(HostArgument.HOST);
		String port = (String) arguments.get(PortArgument.PORT);
		String timeoutstr = (String) arguments.get(TimeoutArgument.TIMEOUT);
		int timeout = Integer.parseInt(timeoutstr);
		StringBuffer buffer = new StringBuffer();
		buffer.append(host).append(':').append(Integer.parseInt(port));
		Connection c = service.accept(service.startListening(buffer.toString()), timeout, timeout);
		DebugSession session = new DebugSession(c);
		return new CFVirtualMachine(session);
	}
}
