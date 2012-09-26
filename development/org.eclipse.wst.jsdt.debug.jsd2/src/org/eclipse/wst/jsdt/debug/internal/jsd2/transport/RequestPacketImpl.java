/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.jsd2.transport;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.wst.jsdt.debug.transport.packet.Request;

/**
 * Default request implementation for JSD2
 * 
 * @since 1.0
 */
public class RequestPacketImpl extends PacketImpl implements Request {

	/**
	 * The overall sequence number for all request packets
	 */
	static int currentSequence = 0;
	/**
	 * the current sequence for this request packet
	 */
	final int fSequence;
	/**
	 * The command for this request
	 */
	String fCommand = null; 
	
	/**
	 * Constructor
	 * @param command
	 */
	public RequestPacketImpl(String command) {
		super(Attributes.TO);
		fCommand = command;
		fSequence = incSeqence();
	}

	/**
	 * Constructor
	 * 
	 * @param json
	 */
	public RequestPacketImpl(Map json) {
		super(json);
		fCommand = (String) json.get(Attributes.TYPE);
		Assert.isNotNull(fCommand, Messages.RequestPacketImpl_cannot_create_request_packet_null_command);
		//TODO collect this from the packet if we end up storing it in there
		fSequence = incSeqence();
	}
	
	/**
	 * Creates a new sequence number for all request packets
	 * @return the new request identifier
	 */
	static int incSeqence() {
		return ++currentSequence;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Packet#toJSON()
	 */
	public Map toJSON() {
		Map json = new HashMap();
		json.put(Attributes.TYPE, fCommand);
		//TODO add all supported JSD2 attributes
		//TODO also decide if we want to pass the sequence along 
		return json;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Request#getCommand()
	 */
	public String getCommand() {
		return fCommand;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Request#getSequence()
	 */
	public int getSequence() {
		return fSequence;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Request#getArguments()
	 */
	public Map getArguments() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		Object json = toJSON();
		buffer.append("RequestPacketImpl: "); //$NON-NLS-1$
		JSON.writeValue(json, buffer);
		return buffer.toString();
	}
}
