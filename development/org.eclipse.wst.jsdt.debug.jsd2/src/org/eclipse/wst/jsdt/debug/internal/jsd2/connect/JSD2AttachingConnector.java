/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.jsd2.connect;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.AttachingConnector;
import org.eclipse.wst.jsdt.debug.internal.jsd2.jsdi.VirtualMachineImpl;
import org.eclipse.wst.jsdt.debug.transport.Connection;
import org.eclipse.wst.jsdt.debug.transport.DebugSession;

/**
 * An attaching connector for JSD2
 * 
 * @see https://wiki.mozilla.org/Remote_Debugging_Protocol
 * @see https://wiki.mozilla.org/DevTools/Features/Debugger
 */
public class JSD2AttachingConnector implements AttachingConnector {

	/**
	 * The id of the connector as defined in plugin.xml
	 */
	public static final String CONNECTR_ID = "org.eclipse.wst.jsdt.debug.jsd2.attaching.connector"; //$NON-NLS-1$
	
	/**
	 * Constructor
	 */
	public JSD2AttachingConnector() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#defaultArguments()
	 */
	public Map defaultArguments() {
		HashMap args = new HashMap(5);
		args.put(HostArgument.HOST, new HostArgument(null));
		args.put(PortArgument.PORT, new PortArgument(5000));
		args.put(TimeoutArgument.TIMEOUT, new TimeoutArgument());
		return args;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#description()
	 */
	public String description() {
		return Messages.JSD2AttachingConnector_description;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#name()
	 */
	public String name() {
		return Messages.JSD2AttachingConnector_name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#id()
	 */
	public String id() {
		return CONNECTR_ID;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.AttachingConnector#attach(java.util.Map)
	 */
	public VirtualMachine attach(Map arguments) throws IOException {
		Connection c = null;
		//TODO make it connect
		DebugSession session = new DebugSession(c);
		return new VirtualMachineImpl(session);
	}
}
