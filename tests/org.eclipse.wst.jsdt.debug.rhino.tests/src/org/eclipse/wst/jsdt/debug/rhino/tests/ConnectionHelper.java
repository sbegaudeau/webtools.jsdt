/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.rhino.tests;

import java.io.IOException;

import org.eclipse.wst.jsdt.debug.transport.Connection;
import org.eclipse.wst.jsdt.debug.transport.ListenerKey;
import org.eclipse.wst.jsdt.debug.transport.TransportService;



public class ConnectionHelper {

	private TransportService transportService;
	private String address;

	public ConnectionHelper(final TransportService transportService, final String address) {
		this.transportService = transportService;
		this.address = address;
	}

	public Connection getClientConnection() {
		try {
			return transportService.attach(address, 5000, 5000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Connection getServerConnection() {
		ListenerKey key = null;
		try {
			key = transportService.startListening(address);
			return transportService.accept(key, 5000, 5000);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (key != null)
				try {
					transportService.stopListening(key);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return null;
	}
}
