/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.crossfire.transport;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract description of a packet for sending / receiving information to the debug client
 * using JSON
 *  
 *  @since 1.0
 */
public abstract class CFPacket implements org.eclipse.wst.jsdt.debug.transport.packet.Packet {

	/**
	 * Debugging flag
	 */
	public static boolean TRACE = false;
	
	private final String type;
	private final String context_id;
	
	/**
	 * Constructor
	 * 
	 * @param type the type for the {@link CFPacket} <code>null</code> is not accepted
	 */
	protected CFPacket(String type, String context_id) {
		if(type == null) {
			throw new IllegalArgumentException("The type for a packet cannot be null"); //$NON-NLS-1$
		}
		this.type = type.intern();
		this.context_id = context_id;
	}

	/**
	 * Constructor
	 * 
	 * @param json the pre-composed map of attributes for the packet, <code>null</code> is not accepted
	 */
	protected CFPacket(Map json) {
		if(json == null) {
			throw new IllegalArgumentException("The JSON map for a packet cannot be null"); //$NON-NLS-1$
		}
		String packetType = (String) json.get(Attributes.TYPE);
		this.type = packetType.intern();
		this.context_id = (String) json.get(Attributes.CONTEXT_ID);
	}

	/**
	 * @return the context id or <code>null</code>
	 */
	public String getContextId() {
		return context_id;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Packet#getType()
	 */
	public String getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Packet#toJSON()
	 */
	public Map toJSON() {
		Map json = new HashMap();
		json.put(Attributes.TYPE, type);
		if(context_id != null) {
			json.put(Attributes.CONTEXT_ID, context_id);
		}
		return json;
	}

	/**
	 * Returns the type from the given JSON map.<br>
	 * <br>
	 * This method can return <code>null</code> if the map is not correctly
	 * formed.
	 * 
	 * @param json the JSON map, <code>null</code> is not accepted
	 * @return the type from the JSON map or <code>null</code>
	 */
	public static String getType(Map json) {
		if(json == null) {
			throw new IllegalArgumentException("A null JSON map is not allowed when trying to get the packet type"); //$NON-NLS-1$
		}
		return (String) json.get(Attributes.TYPE);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		Object json = toJSON();
		buffer.append("CFPacket: "); //$NON-NLS-1$
		JSON.writeValue(json, buffer);
		return buffer.toString();
	}
	
	/**
	 * Sets if packet transfer should be traced
	 * @param tracing
	 */
	public static void setTracing(boolean tracing) {
		TRACE = tracing;
	}
}
