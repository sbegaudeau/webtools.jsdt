/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.rhino.tests;

import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.wst.jsdt.debug.rhino.transport.Connection;
import org.eclipse.wst.jsdt.debug.rhino.transport.PipedTransportService;
import org.eclipse.wst.jsdt.debug.rhino.transport.SocketTransportService;
import org.eclipse.wst.jsdt.debug.rhino.transport.TransportService;
import org.eclipse.wst.jsdt.debug.rhino.transport.TransportService.ListenerKey;

public class TransportTest extends TestCase {

	public void testSocketStartStopListening() throws IOException {
		TransportService service = new SocketTransportService();
		ListenerKey key = service.startListening("9000");
		assertNotNull(key);
		service.stopListening(key);
	}

	public void testSocketAcceptAttach() throws IOException, InterruptedException {
		final TransportService service = new SocketTransportService();
		final ListenerKey key = service.startListening("9000");

		Thread t = new Thread() {
			public void run() {
				Connection c = null;
				try {
					c = service.accept(key, 5000, 5000);
					assertNotNull(c);
					c.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					fail();
				}
			}
		};
		t.start();

		Connection c = service.attach("9000", 5000, 5000);
		assertNotNull(c);
		c.close();
		t.join(5000);
		service.stopListening(key);
	}

	public void testPipedStartStopListening() throws IOException {
		TransportService service = new PipedTransportService();
		ListenerKey key = service.startListening("9000");
		assertNotNull(key);
		service.stopListening(key);
	}

	public void testPipedAcceptAttach() throws IOException, InterruptedException {
		final TransportService service = new PipedTransportService();
		final ListenerKey key = service.startListening("9000");

		Thread t = new Thread() {
			public void run() {
				Connection c = null;
				try {
					c = service.accept(key, 5000, 5000);
					assertNotNull(c);
					c.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					fail();
				}
			}
		};
		t.start();

		Connection c = service.attach("9000", 5000, 5000);
		assertNotNull(c);
		c.close();
		t.join(5000);
		service.stopListening(key);
	}

}
