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
package org.eclipse.wst.jsdt.debug.opera.internal.launching;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.ListeningConnector;

/**
 * Default listening connector
 */
public class OperaListeningConnector implements ListeningConnector {

	/**
	 * Constructor
	 */
	public OperaListeningConnector() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#defaultArguments()
	 */
	public Map defaultArguments() {
		HashMap args = new HashMap();
		args.put(HostArgument.HOST, new HostArgument(null));
		args.put(PortArgument.PORT, new PortArgument(7001));
		return args;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#description()
	 */
	public String description() {
		return Messages.connector_desc;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#name()
	 */
	public String name() {
		return Messages.connector_name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector#id()
	 */
	public String id() {
		return Messages.connector_id;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.ListeningConnector#accept(java.util.Map)
	 */
	public VirtualMachine accept(Map arguments) throws IOException {
//		DefaultListener listener = new DefaultListener();
//		ExceptionListener el = new ExceptionListener() {
//			public void exceptionThrown(Exception e) {
//				e.printStackTrace();
//			}
//		};
//		String host = (String) arguments.get(HostArgument.HOST);
//		String port = (String) arguments.get(PortArgument.PORT);
//		if(host == null || port == null) {
//			throw new IOException("The host or port arguments cannot be null"); //$NON-NLS-1$
//		}
//		int portnum = -1;
//		try {
//			portnum = Integer.parseInt(port);
//		}
//		catch(NumberFormatException nfe) {
//			throw new IOException("The given port was not an integer"); //$NON-NLS-1$
//		}
//		ScopeSDK scope = new ScopeSDK(listener, el, host, portnum, -1, null);
//		try {
//			scope.start();
//			ScopeClient client = listener.waitForClient(50000, TimeUnit.MILLISECONDS);
//			if(client != null) {
//				VirtualMachineImpl vm = new VirtualMachineImpl(client);
//				return vm;
//			}
//			throw new IOException("Could not start the ScopeSDK builder"); //$NON-NLS-1$
//		}
//		catch(IOException ioe) {
//			scope.stop();
//			throw ioe;
//		}
		return null;
	}
}
