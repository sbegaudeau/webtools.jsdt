/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others All rights reserved. This
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
	 * @param tools the {@link String} array of tools to enable by default
	 * @throws IOException
	 */
	public void writeHandShake(String[] tools) throws IOException {
		if(CFPacket.TRACE) {
			Tracing.writeString("WRITE HANDSHAKE: "+HandShakePacket.getHandshake(tools)); //$NON-NLS-1$
		}
		Writer writer = getWriter();
		writer.write(HandShakePacket.getHandshake(tools));
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
		//chew up the tool heading if there is one
		r = false;
		while(reader.ready() && (c = reader.read()) > -1) {
			if(r) {
				if(c == '\n') {
					buffer.append((char)c);
					break;
				}
			}
			r = c == '\r';
			if(r) {
				buffer.append((char)c);
			}
		}
		//XXX hack, we should be properly parsing and accounting for the tool header
		if(buffer.toString().equals(HandShakePacket.getHandshake(null))) {
			HandShakePacket ack = new HandShakePacket();
			if(CFPacket.TRACE) {
				Tracing.writeString("ACK HANDSHAKE: "+buffer.toString()); //$NON-NLS-1$
			}
			return ack;
		}	
		if(CFPacket.TRACE) {
			Tracing.writeString("Did not get correct CrossFire handshake: "+buffer.toString()); //$NON-NLS-1$
		}
		throw new IOException("Did not get correct CrossFire handshake: "+buffer.toString()); //$NON-NLS-1$
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.socket.SocketConnection#readPacket()
	 */
	public Packet readPacket() throws IOException {
		StringBuffer buffer = new StringBuffer();
		StringBuffer raw = new StringBuffer();
		int c = -1;
		boolean r = false;
		String len = null;
		Reader reader = getReader();
		while((c = reader.read()) > -1) {
			if(CFPacket.TRACE) {
				raw.append((char)c);
			}
			if(r) {
				if(c == '\n') {
					String str = buffer.toString();
					if(str.startsWith(JSON.CONTENT_LENGTH)) {
						len = grabAttrib(str);
					}
					else if(str.equals("\r")) { //$NON-NLS-1$
						break;
					}
					buffer = new StringBuffer();
					r = false;
				}
				continue;
			}
			buffer.append((char)c);
			r = c == '\r';
		}
		int length = 0;
		try {
			length = Integer.parseInt(len);
		} catch (NumberFormatException e) {
			if(CFPacket.TRACE) {
				Tracing.writeString("[SOCKET] failed to read content length: "+raw.toString()); //$NON-NLS-1$
			}
			throw new IOException("Failed to parse content length: " + raw.toString()); //$NON-NLS-1$
		}
		char[] message = new char[length];
		int n = 0;
		int off = 0;
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
	
	/**
	 * Grabs the attribute from the RHS of the header. Where all headers
	 * have the form <code>[name]:[value]</code>.
	 * 
	 * @param str the string to parse
	 * @return the <code>[value]</code> from the header
	 */
	String grabAttrib(String str) {
		if(str != null) {
			int idx = str.indexOf(':');
			if(idx > -1) {
				return str.substring(idx+1, str.length()-1);
			}
		}
		return null;
	}
}
