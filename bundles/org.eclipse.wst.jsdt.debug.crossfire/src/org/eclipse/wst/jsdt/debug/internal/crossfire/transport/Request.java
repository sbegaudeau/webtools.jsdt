/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others All rights reserved. This
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

/**
 * Default request implementation using JSON
 * 
 * @since 1.0
 */
public class Request extends Packet {

	/**
	 * The type of this packet
	 */
	public static final String REQUEST = "request"; //$NON-NLS-1$
	
	private final String command;
	private final Map arguments = Collections.synchronizedMap(new HashMap());
	private final Map params = Collections.synchronizedMap(new HashMap());
	
	
	/**
	 * Constructor
	 * 
	 * @param command the command, <code>null</code> is not accepted
	 * @param context_id the id of the context to scope the request to
	 * @see http://getfirebug.com/wiki/index.php/Crossfire_Protocol_Reference for 
	 * requests that do not require a context id.
	 */
	public Request(String command, String context_id) {
		super(REQUEST, context_id);
		if(command == null) {
			throw new IllegalArgumentException("The request command kind cannot be null"); //$NON-NLS-1$
		}
		this.command = command.intern();
	}

	/**
	 * Constructor
	 * 
	 * @param json map of JSON attributes, <code>null</code> is not accepted
	 */
	public Request(Map json) {
		super(json);
		if(json == null) {
			throw new IllegalArgumentException("The JSON map for a request packet cannot be null"); //$NON-NLS-1$
		}
		String value = (String) json.get(Attributes.COMMAND);
		this.command = value.intern();
		Map packetArguments = (Map) json.get(Attributes.ARGUMENTS);
		arguments.putAll(packetArguments);
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
	
	/**
	 * Returns the command that this {@link Request} will was created for.<br>
	 * <br>
	 * This method cannot return <code>null</code>
	 * 
	 * @return the underlying command, never <code>null</code>
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Returns the complete collection of JSON arguments in the {@link Request}.<br>
	 * <br>
	 * This method cannot return <code>null</code>, an empty map will be returned
	 * if there are no properties.
	 * 
	 * @return the arguments or an empty map never <code>null</code>
	 */
	public Map getArguments() {
		return arguments;
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
		arguments.put(key, argument);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.core.jsdi.connect.Packet#toJSON()
	 */
	public Map toJSON() {
		Map json = super.toJSON();
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
		buffer.append("Request: "); //$NON-NLS-1$
		JSON.writeValue(json, buffer);
		return buffer.toString();
	}
}
