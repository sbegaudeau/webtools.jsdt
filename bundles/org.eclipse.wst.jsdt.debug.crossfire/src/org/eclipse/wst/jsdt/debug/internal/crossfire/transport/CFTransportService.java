/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.crossfire.transport;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.transport.Connection;
import org.eclipse.wst.jsdt.debug.transport.ListenerKey;
import org.eclipse.wst.jsdt.debug.transport.packet.Packet;
import org.eclipse.wst.jsdt.debug.transport.socket.SocketConnection;
import org.eclipse.wst.jsdt.debug.transport.socket.SocketTransportService;


/**
 * Implementation of a transport service that using a {@link Socket} for communication
 * 
 * @since 1.0
 */
public class CFTransportService extends SocketTransportService {

	static final Class serverSocketClass = ServerSocket.class; // temporary used to pre-load the ServerSocket.class

	/**
	 * Map of {@link ListenerKey} to {@link ServerSocket}s
	 */
	Map listeners = new HashMap();
	
	String[] tools = null;
	
	/**
	 * Constructor
	 */
	public CFTransportService(String[] tools) {
		this.tools = tools;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.socket.SocketTransportService#getConnection(java.net.Socket)
	 */
	public SocketConnection getConnection(Socket socket) throws IOException {
		return new CFSocketConnection(socket);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.socket.SocketTransportService#handleAccept(org.eclipse.wst.jsdt.debug.transport.Connection)
	 */
	public void handleAccept(Connection connection) throws IOException {
		if(connection instanceof CFSocketConnection) {
			CFSocketConnection cfconn = (CFSocketConnection) connection;
			Packet packet = cfconn.readHandShake();
			if (!(packet instanceof HandShakePacket)) {
				throw new IOException("failure establishing connection"); //$NON-NLS-1$
			}
			cfconn.writeHandShake(this.tools);
			return;
		}
		throw new IOException("failure establishing connection"); //$NON-NLS-1$
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.socket.SocketTransportService#handleAttach(org.eclipse.wst.jsdt.debug.transport.Connection)
	 */
	public void handleAttach(Connection connection) throws IOException {
		if(connection instanceof CFSocketConnection) {
			CFSocketConnection cfconn = (CFSocketConnection) connection;
			cfconn.writeHandShake(this.tools);
			Packet packet = cfconn.readHandShake();
			if (!(packet instanceof HandShakePacket)) {
				throw new IOException("failure establishing connection"); //$NON-NLS-1$
			}
			return;
		}
		throw new IOException("failure establishing connection"); //$NON-NLS-1$
	}
}
