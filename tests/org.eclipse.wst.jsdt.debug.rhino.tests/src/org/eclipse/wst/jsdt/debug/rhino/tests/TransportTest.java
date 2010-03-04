/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.rhino.tests;

import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.wst.jsdt.debug.core.jsdi.connect.SocketUtil;
import org.eclipse.wst.jsdt.debug.rhino.transport.Connection;
import org.eclipse.wst.jsdt.debug.rhino.transport.PipedTransportService;
import org.eclipse.wst.jsdt.debug.rhino.transport.SocketTransportService;
import org.eclipse.wst.jsdt.debug.rhino.transport.TransportService;
import org.eclipse.wst.jsdt.debug.rhino.transport.TransportService.ListenerKey;

/**
 * Tests the two provided transport services: {@link PipedTransportService} and {@link SocketTransportService}
 * 
 * @since 1.0
 */
public class TransportTest extends TestCase {

	/**
	 * Tests that a socket transport service can be started and stopped without fail 
	 * @throws Exception - pass up any failures to the test framework for reporting
	 */
	public void testSocketStartStopListening() throws Exception {
		TransportService service = new SocketTransportService();
		ListenerKey key = service.startListening(SocketUtil.findFreePortString());
		assertNotNull(key);
		service.stopListening(key);
	}

	/**
	 * Tests that a socket attach service can be started, joined and stopped without fail.
	 * This test can fail in the event that Java cannot find a free port to communicate on.
	 * @throws Exception - pass up any failures to the test framework for reporting
	 */
	public void testSocketAcceptAttach() throws Exception {
		final TransportService service = new SocketTransportService();
		final String port = SocketUtil.findFreePortString();
		assertTrue("A valid port could not be found on localhost", !"-1".equals(port));
		final ListenerKey key = service.startListening(port);

		Thread t = new Thread() {
			public void run() {
				Connection c = null;
				try {
					c = service.accept(key, 5000, 5000);
					assertNotNull(c);
					c.close();
				} catch (IOException e) {
					fail(e.getMessage());
				}
			}
		};
		t.start();

		Connection c = service.attach(port, 5000, 5000);
		assertNotNull(c);
		c.close();
		t.join(5000);
		service.stopListening(key);
	}

	/**
	 * Tests that a piped transport server can be started and stopped without fail
	 * @throws Exception - pass up any failures to the test framework for reporting
	 */
	public void testPipedStartStopListening() throws Exception {
		TransportService service = new PipedTransportService();
		ListenerKey key = service.startListening(SocketUtil.findFreePortString());
		assertNotNull(key);
		service.stopListening(key);
	}

	/**
	 * Tests that a piped connection can be attached to, joined and then closed without fail
	 * @throws Exception - pass up any failures to the test framework for reporting
	 */
	public void testPipedAcceptAttach() throws Exception {
		final TransportService service = new PipedTransportService();
		final String port = SocketUtil.findFreePortString();
		assertTrue("A valid port could not be found on localhost", !"-1".equals(port));
		final ListenerKey key = service.startListening(port);
		
		Thread t = new Thread() {
			public void run() {
				Connection c = null;
				try {
					c = service.accept(key, 5000, 5000);
					assertNotNull(c);
					c.close();
				} catch (IOException e) {
					fail(e.getMessage());
				}
			}
		};
		t.start();

		Connection c = service.attach(port, 5000, 5000);
		assertNotNull(c);
		c.close();
		t.join(5000);
		service.stopListening(key);
	}

}
