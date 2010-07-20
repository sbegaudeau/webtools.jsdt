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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.transport.packet.Packet;

/**
 * Basic packet
 * 
 * @since 1.0
 */
public class PacketImpl implements Packet {

	/**
	 * Debugging flag
	 */
	public static boolean TRACE = false;
	
	private String type = null;
	
	/**
	 * Constructor
	 * 
	 * @param type
	 */
	public PacketImpl(String type) {
		if(type == null) {
			throw new IllegalArgumentException(Messages.packet_type_cannot_be_null);
		}
		this.type = type;
	}
	
	/**
	 * Constructor
	 * 
	 * @param existing JSON map to create a packet from
	 */
	public PacketImpl(Map json) {
		if(json == null) {
			throw new IllegalArgumentException(Messages.cannot_create_pakcet_null_json);
		}
		type = (String) json.get(Attributes.TYPE);
		if(type == null) {
			throw new IllegalArgumentException(Messages.no_packet_type_in_json);
		}
	}
	
	/**
	 * Sets if packet transfer should be traced
	 * @param tracing
	 */
	public static void setTracing(boolean tracing) {
		TRACE = tracing;
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
		return json;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		Object json = toJSON();
		buffer.append("PacketImpl: "); //$NON-NLS-1$
		JSON.writeValue(json, buffer);
		return buffer.toString();
	}
}
