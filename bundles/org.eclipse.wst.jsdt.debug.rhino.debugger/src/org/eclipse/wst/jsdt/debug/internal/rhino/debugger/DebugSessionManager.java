/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.rhino.debugger;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Connection;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.DebugSession;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.DisconnectedException;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.EventPacket;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.JSONConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Request;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Response;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.SocketTransportService;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.TimeoutException;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.TransportService;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.TransportService.ListenerKey;

/**
 * Delegate for {@link DebugSession} communication
 * 
 * @since 1.1
 */
public class DebugSessionManager implements Runnable {

	private static final String ADDRESS = "address"; //$NON-NLS-1$
	private static final String SOCKET = "socket"; //$NON-NLS-1$
	private static final String TRANSPORT = "transport"; //$NON-NLS-1$
	
	private final TransportService transportService;
	private final String address;
	private final boolean startSuspended;
	
	private ListenerKey listenerKey;
	private Connection connection;
	private DebugSession debugSession;
	
	private Thread debuggerThread;

	private volatile boolean shutdown = false;
	private RequestHandler requestHandler;
	
	/**
	 * Constructor
	 * 
	 * @param transportService
	 * @param address
	 * @param startSuspended
	 */
	public DebugSessionManager(TransportService transportService, String address, boolean startSuspended) {
		this.transportService = transportService;
		this.address = address;
		this.startSuspended = startSuspended;
		prettyPrintHeader();
	}
	
	/**
	 * Creates a new session manager
	 * @param configString
	 * @return
	 */
	static DebugSessionManager create(String configString) {
		Map config = parseConfigString(configString);
		String transport = (String) config.get(TRANSPORT);
		if (! SOCKET.equals(transport)) {
			throw new IllegalArgumentException("Transport service must be 'socket': "+ transport); //$NON-NLS-1$
		}
		TransportService parsedTransportService = new SocketTransportService();
		String parsedAddress = (String) config.get(ADDRESS);
		String suspend = (String) config.get(JSONConstants.SUSPEND);
		boolean parsedStartSuspended = false;
		if(suspend != null) {
			parsedStartSuspended = (Boolean.valueOf(suspend).booleanValue() || suspend.trim().equalsIgnoreCase("y")); //$NON-NLS-1$
		}
		return new DebugSessionManager(parsedTransportService, parsedAddress, parsedStartSuspended);
	}
	
	/**
	 * Parses the command line configuration string
	 * 
	 * @param configString
	 * @return the map of command line arguments
	 */
	private static Map parseConfigString(String configString) {
		Map config = new HashMap();
		StringTokenizer tokenizer = new StringTokenizer(configString, ","); //$NON-NLS-1$
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			int equalsIndex = token.indexOf('=');
			if (equalsIndex == -1)
				config.put(token, null);
			else
				config.put(token.substring(0, equalsIndex), token.substring(equalsIndex + 1));
		}
		return config;
	}
	
	/**
	 * Pretty print the header for the debugger
	 * @since 1.1
	 */
	private void prettyPrintHeader() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Rhino attaching debugger\n"); //$NON-NLS-1$
		buffer.append("Start at time: ").append(getStartAtDate()); //$NON-NLS-1$
		buffer.append("\nListening to "); //$NON-NLS-1$
		buffer.append(this.transportService instanceof SocketTransportService ? "socket on " : "transport service on "); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append("port: ").append(this.address); //$NON-NLS-1$
		if(this.startSuspended) {
			buffer.append("\nStarted suspended - waiting for client resume..."); //$NON-NLS-1$
		}
		System.out.println(buffer.toString());
	}
	
	/**
	 * Returns the formatted date
	 * @return the formatted date
	 * @see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4981314
	 * @since 1.1
	 */
	String getStartAtDate() {
		try {		
			return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(Calendar.getInstance().getTime());
		}
		catch(Throwable t) {
			return "<unknown>"; //$NON-NLS-1$
		}
	}
	
	/**
	 * @return true if the <code>suspend=true</code> command line argument is set
	 */
	public boolean isStartSuspended() {
		return startSuspended;
	}

	/**
	 * Returns if a {@link DebugSession} has successfully connected to this debugger.
	 * 
	 * @return <code>true</code> if the debugger has a connected {@link DebugSession} <code>false</code> otherwise
	 */
	public synchronized boolean isConnected() {
		return debugSession != null;
	}
	
	/**
	 * Starts the debugger
	 */
	public synchronized void start(RhinoDebuggerImpl debugger) {
		try {
			requestHandler = new RequestHandler(debugger);
			if (listenerKey == null) {
				listenerKey = transportService.startListening(address);
			}
			if(startSuspended) {
				acceptConnection(300000);
			}
			debuggerThread =  new Thread(this, "RhinoDebugger - Request Handler"); //$NON-NLS-1$
			debuggerThread.start();
		} catch (IOException e) {
			sendDeathEvent();
			/*e.printStackTrace();*/
		}
	}

	/**
	 * Stops the debugger
	 */
	public synchronized void stop() {
		shutdown = true;
		try {
			debuggerThread.interrupt();
			if (debuggerThread.isAlive())
				wait();
			debuggerThread.join();
		} catch (InterruptedException e) {
			/*e.printStackTrace();*/
		}
		try {
			transportService.stopListening(listenerKey);
		} catch (IOException e) {
			sendDeathEvent();
			/*e.printStackTrace();*/
		}
		requestHandler = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			while (!shutdown) {
				try {
					acceptConnection(10000);
				} catch (IOException e) {
					if (connection == null) {
						continue;
					}
				}
				while (!shutdown && connection.isOpen()) {
					try {
						Request request = debugSession.receiveRequest(1000);
						Response response = requestHandler.handleRequest(request);
						debugSession.sendResponse(response);
					} catch (TimeoutException e) {
						// ignore
					} catch (DisconnectedException e) {
						break;
					}
				}
				closeConnection();
			}

		} catch (IOException e) {
			sendDeathEvent();
			/*e.printStackTrace();*/
		}
	}

	/**
	 * Close the active connection
	 * 
	 * @throws IOException
	 */
	private synchronized void closeConnection() throws IOException {
		if (connection != null) {
			debugSession.dispose();
			debugSession = null;
			connection.close();
			connection = null;
			notify();
		}
	}

	/**
	 * Waits for a connection for the given timeout
	 * 
	 * @param timeout
	 * @throws IOException
	 */
	private synchronized void acceptConnection(long timeout) throws IOException {
		if (connection == null) {
			connection = transportService.accept(listenerKey, timeout, timeout);
			debugSession = new DebugSession(connection);
		}
	}
	
	
	/**
	 * Sends the given {@link EventPacket} using the underlying {@link DebugRuntime} and returns if it was sent successfully
	 * 
	 * @param event
	 * @return true if the event was sent successfully, false otherwise
	 */
	public synchronized boolean sendEvent(EventPacket event) {
		try {
			if (debugSession != null) {
				debugSession.sendEvent(event);
				return true;
			}
		} catch (DisconnectedException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Sends out an event that the debugger has died in an unexpected way. Debugger death can result from:
	 * <ul>
	 * <li>an {@link IOException} while the debugger is running</li>
	 * <li>an {@link InterruptedException} processing I/O</li>
	 * </ul>
	 */
	private void sendDeathEvent() {
		EventPacket event = new EventPacket(JSONConstants.VMDEATH);
		sendEvent(event);
	}
}
