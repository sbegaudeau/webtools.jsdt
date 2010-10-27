/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.crossfire.transport;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.internal.crossfire.Tracing;
import org.eclipse.wst.jsdt.debug.transport.Connection;
import org.eclipse.wst.jsdt.debug.transport.packet.Packet;
import org.eclipse.wst.jsdt.debug.transport.socket.SocketConnection;

/**
 * A specialized {@link Connection} that communicates using {@link Socket}s
 * 
 * @since 1.0
 */
public class CFSocketConnection extends SocketConnection {

	/**
	 * Constructor
	 * 
	 * @param socket the underlying {@link Socket}, <code>null</code> is not accepted
	 * 
	 * @throws IOException
	 */
	public CFSocketConnection(Socket socket) throws IOException {
		super(socket);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.socket.SocketConnection#writePacket(org.eclipse.wst.jsdt.debug.transport.packet.Packet)
	 */
	public void writePacket(Packet packet) throws IOException {
		String serialized = JSON.serialize((CFPacket) packet);
		if(CFPacket.TRACE) {
			Tracing.writeString("WRITE PACKET: "+serialized); //$NON-NLS-1$
		}
		Writer writer = getWriter();
		writer.write(serialized);
		writer.flush();
	}

	/**
	 * Writes the standard handshake packet to connect
	 * 
	 * @param packet
	 * @throws IOException
	 */
	public void writeHandShake() throws IOException {
		if(CFPacket.TRACE) {
			Tracing.writeString("WRITE HANDSHAKE: "+HandShakePacket.getHandshake()); //$NON-NLS-1$
		}
		Writer writer = getWriter();
		writer.write(HandShakePacket.getHandshake());
		writer.flush();
	}
	
	/**
	 * Reads the {@link HandShakePacket} packet from the the stream
	 * 
	 * @return the {@link HandShakePacket}, never <code>null</code>
	 * @throws IOException
	 */
	public CFPacket readHandShake() throws IOException {
		StringBuffer buffer = new StringBuffer();
		//read the header first
		int c = 0;
		boolean r = false;
		Reader reader = getReader();
		while((c = reader.read()) > -1) {
			buffer.append((char)c);
			if(r) {
				if(c == '\n') {
					break;
				}
			}
			r = c == '\r';
		}
		if(buffer.toString().equals(HandShakePacket.getHandshake())) {
			HandShakePacket ack = new HandShakePacket();
			if(CFPacket.TRACE) {
				Tracing.writeString("ACK HANDSHAKE: "+ack); //$NON-NLS-1$
			}
			return ack;
		}		
		throw new IOException("Did not get correct CrossFire handshake"); //$NON-NLS-1$
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.socket.SocketConnection#readPacket()
	 */
	public Packet readPacket() throws IOException {
		StringBuffer buffer = new StringBuffer();
		StringBuffer raw = new StringBuffer();
		int c = -1;
		boolean len = false;
		Reader reader = getReader();
		while((c = reader.read()) > -1) {
			if(CFPacket.TRACE) {
				raw.append((char)c);
			}
			if(c == '\r') {
				break;
			}
			if(len) {
				buffer.append((char)c);
				continue;
			}
			len = c == ':';
		}
		int length = 0;
		try {
			length = Integer.parseInt(buffer.toString());
		} catch (NumberFormatException e) {
			throw new IOException("Failed to parse content length: " + buffer.toString()); //$NON-NLS-1$
		}
		char[] message = new char[length];
		int n = 0;
		int off = 0;
		c = reader.read();
		if(CFPacket.TRACE) {
			raw.append((char)c);
		}
		if(c != '\n') {
			throw new IOException("Failed to parse content length: " + buffer.toString() + "next char was not '\n'" + (char)c); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if(!JSON.PRE03) {
			//check for the new double \r\n introduced in CF 0.3
			c = reader.read();
			if(CFPacket.TRACE) {
				raw.append((char)c);
			}
			if(c == '\r') {
				//chew up the \n
				c = reader.read();
				if(CFPacket.TRACE) {
					raw.append((char) c);
				}
			}
			else {
				message[0] = (char) c;
				off = 1;
			}
		}
		while (n < length) {
			int count = reader.read(message, off + n, length - n);
			if (count < 0) {
				throw new EOFException();
			}
			n += count;
		}
		if(CFPacket.TRACE) {
			raw.append(message);
			Tracing.writeString("READ PACKET: " + raw.toString()); //$NON-NLS-1$
		}
		Map json = (Map) JSON.read(new String(message));
		String type = CFPacket.getType(json);
		if (CFEventPacket.EVENT.equals(type)) {
			return new CFEventPacket(json);
		}
		if (CFRequestPacket.REQUEST.equals(type)) {
			return new CFRequestPacket(json);
		}
		if (CFResponsePacket.RESPONSE.equals(type)) {
			return new CFResponsePacket(json);
		}
		throw new IOException("Unknown packet type: " + type); //$NON-NLS-1$
	}
}
