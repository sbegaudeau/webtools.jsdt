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
import java.util.HashSet;
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
	
	private static final HashSet EVENTS;
	
	static {
		EVENTS = new HashSet();
		EVENTS.add(Commands.CLOSED);
		EVENTS.add(Commands.NAVIGATED);
	}
	
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
		String serialized = JSON.serialize((PacketImpl) packet);
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
		r = false;
		while(reader.ready() && (c = reader.read()) > -1) {
			if(r) {
				if(c == '\n') {
					break;
				}
			}
			r = c == '\r';
		}
		if(PacketImpl.TRACE) {
			Tracing.writeString("READ HANDSHAKE: "+buffer.toString()); //$NON-NLS-1$
		}
		return buffer.toString().equals(HANDSHAKE);	
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.socket.SocketConnection#readPacket()
	 */
	public Packet readPacket() throws IOException {
		StringBuffer buffer = new StringBuffer();
		int c = -1;
		Reader reader = getReader();
		String dest = null;
		String tool = null;
		String len = null;
		boolean r = false;
		while((c = reader.read()) > -1) {
			if(r) {
				if(c == '\n') {
					String str = buffer.toString();
					if(str.startsWith(JSON.DESTINATION_HEADER)) {
						dest = grabAttrib(str);
					}
					else if(str.startsWith(JSON.TOOL_HEADER)) {
						tool = grabAttrib(str);
					}
					else if(str.startsWith(JSON.CONTENT_LENGTH)) {
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
			throw new IOException("Failed to parse content length: " + len); //$NON-NLS-1$
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
			Tracing.writeString("READ PACKET: [destination - "+dest+"] [tool - "+tool+"] [length - "+length+"]"+new String(message)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		Map json = (Map) JSON.read(new String(message));
		json.put(Attributes.TOOL, tool);
		json.put(Attributes.DESTINATION, dest);
		if(json.containsKey(Attributes.RESULT)) {
			if(EVENTS.contains(json.get(Attributes.COMMAND))) {
				json.put(Attributes.TYPE, EventPacketImpl.EVENT);
				return new EventPacketImpl(json);
			}
			json.put(Attributes.TYPE, ResponsePacketImpl.RESPONSE);
			return new ResponsePacketImpl(json);
		}
		json.put(Attributes.TYPE, RequestPacketImpl.REQUEST);
		return new RequestPacketImpl(json);
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
