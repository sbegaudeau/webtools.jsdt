/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.chrome.transport;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.internal.chrome.ChromePlugin;
import org.eclipse.wst.jsdt.debug.internal.chrome.Tracing;
import org.eclipse.wst.jsdt.debug.transport.Connection;
import org.eclipse.wst.jsdt.debug.transport.packet.Packet;
import org.eclipse.wst.jsdt.debug.transport.socket.SocketConnection;

/**
 * A specialized {@link Connection} that communicates using {@link Socket}s
 * 
 * @since 1.0
 */
public class ChromeSocketConnection extends SocketConnection {

	public static final String HANDSHAKE = "ChromeDevToolsHandshake\r\n"; //$NON-NLS-1$
	
	/**
	 * Constructor
	 * 
	 * @param socket the underlying {@link Socket}, <code>null</code> is not accepted
	 * 
	 * @throws IOException
	 */
	public ChromeSocketConnection(Socket socket) throws IOException {
		super(socket);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.socket.SocketConnection#writePacket(org.eclipse.wst.jsdt.debug.transport.packet.Packet)
	 */
	public void writePacket(Packet packet) throws IOException {
		String serialized = JSON.serialize(packet);
		if(PacketImpl.TRACE) {
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
		if(PacketImpl.TRACE) {
			Tracing.writeString("WRITE HANDSHAKE: "+HANDSHAKE); //$NON-NLS-1$
		}
		Writer writer = getWriter();
		writer.write(HANDSHAKE);
		writer.flush();
		//waitForReadyRead();
	}
	
	/**
	 * Method to wait for the socket reader to become ready after the handshake
	 * 
	 * @throws IOException
	 */
	void waitForReadyRead() throws IOException {
		long timeout = System.currentTimeMillis() + 1000;
		boolean timedout = System.currentTimeMillis() > timeout;
		Reader reader = getReader();
		while(!reader.ready() && !timedout) {
			try {
				Thread.sleep(100);
				timedout = System.currentTimeMillis() > timeout;
			} catch (InterruptedException e) {
				ChromePlugin.log(e);
			}
		}
		if(timedout) {
			if(PacketImpl.TRACE) {
				Tracing.writeString("HANDSHAKE: Timed out waiting for ready read from handshake"); //$NON-NLS-1$
			}
			//throw new IOException("Waiting for the socket to become available after receiving handshake timed out."); //$NON-NLS-1$
		}
	}
	
	/**
	 * Reads the {@link HandShakePacket} packet from the the stream
	 * 
	 * @return the {@link HandShakePacket}, never <code>null</code>
	 * @throws IOException
	 */
	public boolean readHandShake() throws IOException {
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
		return buffer.toString().equals(HANDSHAKE);	
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.socket.SocketConnection#readPacket()
	 */
	public Packet readPacket() throws IOException {
		StringBuffer buffer = new StringBuffer();
		int c = -1;
		boolean len = false;
		Reader reader = getReader();
		while((c = reader.read()) > -1) {
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
		c = reader.read();
		if(c != '\n') {
			throw new IOException("Failed to parse content length: " + buffer.toString() + "next char was not '\n'" + (char)c); //$NON-NLS-1$ //$NON-NLS-2$
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
		if(PacketImpl.TRACE) {
			Tracing.writeString("READ PACKET: [length - "+length+"]"+new String(message)); //$NON-NLS-1$ //$NON-NLS-2$
		}
		Map json = (Map) JSON.read(new String(message));
		String type = (String) json.get(Attributes.TYPE);
		if (EventPacketImpl.EVENT.equals(type)) {
			return new EventPacketImpl(json);
		}
		if (RequestPacketImpl.REQUEST.equals(type)) {
			return new RequestPacketImpl(json);
		}
		if (ResponsePacketImpl.RESPONSE.equals(type)) {
			return new ResponsePacketImpl(json);
		}
		throw new IOException("Unknown packet type: " + type); //$NON-NLS-1$
	}
}
