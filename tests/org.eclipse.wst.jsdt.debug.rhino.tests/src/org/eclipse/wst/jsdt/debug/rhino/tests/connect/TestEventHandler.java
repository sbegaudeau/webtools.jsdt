/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.rhino.tests.connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.connect.DisconnectedException;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.TimeoutException;
import org.eclipse.wst.jsdt.debug.internal.rhino.json.JSONUtil;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.EventPacket;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Request;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Response;

public class TestEventHandler implements Runnable {

	public interface Subhandler {
		boolean handleEvent(DebugSession debugSession, EventPacket event);
	}

	public static Subhandler debugScriptSubhandler = new Subhandler() {

		public boolean handleEvent(DebugSession debugSession, EventPacket event) {
			if (event.getEvent().equals("script")) {
				Object scriptId = event.getBody().get("scriptId");
				Request request = new Request("script");
				request.getArguments().put("scriptId", scriptId);
				try {
					debugSession.sendRequest(request);
					Response response = debugSession.receiveResponse(request.getSequence(), 10000);
					System.out.println(JSONUtil.write(response.toJSON()));
					String lines = (String) ((Map) response.getBody().get("script")).get("source");
					BufferedReader reader = new BufferedReader(new StringReader(lines));
					try {
						int lineNumber = 1;
						String line = reader.readLine();
						while (line != null) {
							System.out.println("[" + lineNumber++ + "]: " + line);
							line = reader.readLine();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (DisconnectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return false;
		}

	};

	private DebugSession debugSession;
	private Thread thread;
	private final ArrayList subhandlers = new ArrayList();
	private volatile boolean shutdown = false;

	private int eventCount = 0;

	public TestEventHandler(DebugSession debugSession) {
		this.debugSession = debugSession;
		this.thread = new Thread(this, "TestEventHandler");
	}

	public void start() {
		thread.start();
	}

	public void stop() {
		shutdown = true;
		thread.interrupt();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run() {
		while (!shutdown) {
			try {
				EventPacket event = debugSession.receiveEvent(1000);
				handleEvent(event);
			} catch (TimeoutException e) {
				// ignore
			} catch (DisconnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public synchronized void addSubhandler(Subhandler subhandler) {
		subhandlers.add(subhandler);
	}

	public synchronized void removeSubhandler(Subhandler subhandler) {
		subhandlers.remove(subhandler);
	}

	private synchronized void handleEvent(EventPacket event) {
		// System.out.println(JSONUtil.write(event.toJSON()));
		for (Iterator iterator = subhandlers.iterator(); iterator.hasNext();) {
			Subhandler subhandler = (Subhandler) iterator.next();
			try {
				if (subhandler.handleEvent(debugSession, event)) {
					eventCount++;
					notifyAll();
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		if (!event.getEvent().equals("thread")) {
			sendContinue(event, null);
		}

	}

	protected void sendContinue(EventPacket event, String step) {
		Number threadId = (Number) event.getBody().get("threadId");

		Request request = new Request("continue");
		request.getArguments().put("threadId", threadId);
		request.getArguments().put("step", step);
		try {
			debugSession.sendRequest(request);
			debugSession.receiveResponse(request.getSequence(), 1000);
		} catch (DisconnectedException e) {
			if (!shutdown)
				e.printStackTrace();
		} catch (TimeoutException e) {
			if (!shutdown)
				e.printStackTrace();
		}
	}

	public synchronized void waitForEvents(int count) {
		long timeout = System.currentTimeMillis() + 5000;
		while (eventCount < count && System.currentTimeMillis() < timeout) {
			try {
				wait(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (eventCount != count) {
			throw new IllegalStateException("eventcount was: " + eventCount + " expected: " + count);
		}
	}

	public int eventCount() {
		return eventCount;
	}
}
