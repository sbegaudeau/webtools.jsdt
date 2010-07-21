/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.chrome.transport;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.transport.packet.Request;

/**
 * Request packet
 * 
 * @since 1.0
 */
public class RequestPacketImpl extends PacketImpl implements Request {

	/**
	 * The type of this packet
	 */
	public static final String REQUEST = "request"; //$NON-NLS-1$
	private static int next_seq = 0;
	private final String command;
	private final int seq;
	private Map args = Collections.synchronizedMap(new HashMap(4));
	
	/**
	 * Constructor
	 * 
	 * @param command
	 * @param tool the name of the tools service expected to handle the request
	 */
	public RequestPacketImpl(String command, String tool) {
		super(REQUEST, tool);
		if(command == null) {
			throw new IllegalArgumentException(Messages.cannot_create_request_null_command);
		}
		this.command = command;
		this.seq = ++next_seq;
	}
	
	/**
	 * Constructor
	 * @param json
	 */
	public RequestPacketImpl(Map json) {
		super(json);
		this.command = (String) json.get(Attributes.COMMAND);
		if(command == null) {
			throw new IllegalArgumentException(Messages.no_command_in_request_json);
		}
		this.seq = ((Number)json.get(Attributes.SEQ)).intValue();
		Map pargs = (Map) json.get(Attributes.ARGUMENTS);
		if(pargs != null) {
			args.putAll(pargs);
		}
	}

	/**
	 * Sets the given argument in the JSON map.
	 * 
	 * @param key the key for the attribute, <code>null</code> is not accepted
	 * @param argument the value for the argument, <code>null</code> is not accepted
	 */
	public void setArgument(String key, Object argument) {
		if(key == null) {
			throw new IllegalArgumentException("The argument key cannot be null"); //$NON-NLS-1$
		}
		if(argument == null) {
			throw new IllegalArgumentException("A null argument is not allowed"); //$NON-NLS-1$
		}
		args.put(key, argument);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Request#getCommand()
	 */
	public String getCommand() {
		return command;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Request#getSequence()
	 */
	public int getSequence() {
		return seq;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Request#getArguments()
	 */
	public Map getArguments() {
		return args;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFPacket#toJSON()
	 */
	public Map toJSON() {
		Map json = super.toJSON();
		//json.put(Attributes.SEQ, new Integer(seq));
		json.put(Attributes.COMMAND, command);
		if(!args.isEmpty()) {
			json.put(Attributes.ARGUMENTS, args);
		}
		return json;
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
