/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.crossfire.transport;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.wst.jsdt.debug.transport.packet.Request;

/**
 * Default request implementation using JSON
 * 
 * @since 1.0
 */
public class CFRequestPacket extends CFPacket implements Request {

	/**
	 * The type of this packet
	 */
	public static final String REQUEST = "request"; //$NON-NLS-1$
	
	private final String command;
	private final Map arguments = Collections.synchronizedMap(new HashMap());
	private final Map params = Collections.synchronizedMap(new HashMap());
	private static int currentSequence = 0;
	private final int sequence;
	
	/**
	 * Constructor
	 * 
	 * @param command the command, <code>null</code> is not accepted
	 * @param context_id the id of the context to scope the request to
	 * @see http://getfirebug.com/wiki/index.php/Crossfire_Protocol_Reference for 
	 * requests that do not require a context id.
	 */
	public CFRequestPacket(String command, String context_id) {
		super(REQUEST, context_id);
		if(command == null) {
			throw new IllegalArgumentException("The request command kind cannot be null"); //$NON-NLS-1$
		}
		this.sequence = nextSequence();
		this.command = command.intern();
	}

	/**
	 * Constructor
	 * 
	 * @param json map of JSON attributes, <code>null</code> is not accepted
	 */
	public CFRequestPacket(Map json) {
		super(json);
		if(json == null) {
			throw new IllegalArgumentException("The JSON map for a request packet cannot be null"); //$NON-NLS-1$
		}
		String value = (String) json.get(Attributes.COMMAND);
		this.command = value.intern();
		Map packetArguments = (Map) json.get(Attributes.ARGUMENTS);
		arguments.putAll(packetArguments);
		Number packetSeq = (Number) json.get(Attributes.SEQ);
		this.sequence = packetSeq.intValue();
	}

	/**
	 * @return a next value for the sequence
	 */
	private static synchronized int nextSequence() {
		return ++currentSequence;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Request#getSequence()
	 */
	public int getSequence() {
		return sequence;
	}
	
	/**
	 * Allows additional parameters to be added to the request
	 * 
	 * @param key
	 * @param value
	 */
	public void addAdditionalParam(String key, Object value) {
		params.put(key, value);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Request#getCommand()
	 */
	public String getCommand() {
		return command;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Request#getArguments()
	 */
	public Map getArguments() {
		return arguments;
	}

	/**
	 * Sets the given argument in the JSON map.
	 * 
	 * @param key the key for the attribute, <code>null</code> is not accepted
	 * @param argument the value for the argument
	 */
	public void setArgument(String key, Object argument) {
		if(key == null) {
			throw new IllegalArgumentException("The argument key cannot be null"); //$NON-NLS-1$
		}
		arguments.put(key, argument);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFPacket#toJSON()
	 */
	public Map toJSON() {
		Map json = super.toJSON();
		json.put(Attributes.SEQ, new Integer(sequence));
		json.put(Attributes.COMMAND, command);
		if(!arguments.isEmpty()) {
			json.put(Attributes.ARGUMENTS, arguments);
		}
		Entry entry = null;
		for(Iterator iter = params.entrySet().iterator(); iter.hasNext();) {
			entry = (Entry) iter.next();
			json.put(entry.getKey(), entry.getValue());
		}
		return json;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		Object json = toJSON();
		buffer.append("CFRequestPacket: "); //$NON-NLS-1$
		JSON.writeValue(json, buffer);
		return buffer.toString();
	}
}
