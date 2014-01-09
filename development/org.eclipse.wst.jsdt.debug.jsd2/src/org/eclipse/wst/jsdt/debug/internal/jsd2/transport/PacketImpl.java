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

import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.wst.jsdt.debug.transport.packet.Packet;

/**
 * Basic implementation of a packet for JSD2
 * 
 * @since 1.0
 */
public abstract class PacketImpl implements Packet {

	String fType = null;
	
	/**
	 * Constructor
	 * 
	 * @param type the type of the packet
	 */
	public PacketImpl(String type) {
		fType = type;
	}

	/**
	 * Constructor
	 * 
	 * @param json the JSON from the connection
	 */
	public PacketImpl(Map json) {
		Assert.isNotNull(json, Messages.PacketImpl_cannot_create_packet_null_json);
		fType = getType(json);
		Assert.isNotNull(fType, Messages.PacketImpl_cannot_create_packet_null_type);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Packet#getType()
	 */
	public String getType() {
		return fType;
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
		String type = (String) json.get(Attributes.TO);
		if(type == null) {
			type = (String) json.get(Attributes.FROM);
		}
		if(type == null) {
		}
		return type;
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
