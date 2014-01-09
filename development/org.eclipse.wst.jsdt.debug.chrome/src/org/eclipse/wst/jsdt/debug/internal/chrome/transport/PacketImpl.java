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
	
	private final String type;
	private final String tool;
	
	/**
	 * Constructor
	 * 
	 * @param type the type of the packet
	 * @param tool the tools service expected to handle the packet
	 */
	public PacketImpl(String type, String tool) {
		if(type == null) {
			throw new IllegalArgumentException(Messages.packet_type_cannot_be_null);
		}
		if(tool == null) {
			throw new IllegalArgumentException(Messages.packet_tools_service_name_cannot_be_null);
		}
		this.type = type;
		this.tool = tool;
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
		tool = (String) json.get(Attributes.TOOL);
		if(tool == null) {
			throw new IllegalArgumentException(Messages.no_tool_found_in_packet_json);
		}
	}
	
	/**
	 * Sets if packet transfer should be traced
	 * @param tracing
	 */
	public static void setTracing(boolean tracing) {
		TRACE = tracing;
	}
	
	/**
	 * Returns the name of the tools service expected to handle this packet
	 * 
	 * @return the tools service name
	 */
	public String tool() {
		return tool;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Packet#getType()
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns the intended tab destination for the packet, default returns <code>null</code>
	 * 
	 * @return the tab id or <code>null</code>
	 */
	public Number destination() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Packet#toJSON()
	 */
	public Map toJSON() {
		Map json = new HashMap();
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
