/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.crossfire.transport;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link CFPacket} for replying to the Crossfire handshake request
 * 
 * @since 1.0
 */
public class HandShakePacket extends CFPacket {
	
	/**
	 * handshake String
	 */
	private static String CROSSFIRE_HANDSHAKE = "CrossfireHandshake\r\n"; //$NON-NLS-1$
	
	
	/**
	 * Constructor
	 * @param type
	 */
	protected HandShakePacket() {
		super(CFResponsePacket.RESPONSE, null);
	}

	/**
	 * Creates a handshake object with the given set of tools
	 * 
	 * @param tools the {@link String} array of tools to enable
	 * @return the handshake
	 */
	public static String getHandshake(String[] tools) {
		StringBuffer buffer = new StringBuffer(CROSSFIRE_HANDSHAKE);
		if(tools != null) {
			for (int i = 0; i < tools.length; i++) {
				if(tools[i] != null) {
					buffer.append(tools[i]);
				}
				if(i < tools.length -1) {
					buffer.append(',');
				}
			}
		}
		return buffer.append(JSON.LINE_FEED).toString();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFPacket#toString()
	 */
	public String toString() {
		return CROSSFIRE_HANDSHAKE;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFPacket#toJSON()
	 */
	public Map toJSON() {
		Map json = new HashMap(1);
		json.put(Attributes.HANDSHAKE, CROSSFIRE_HANDSHAKE);
		return json;
	}
}
