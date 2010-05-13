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
package org.eclipse.wst.jsdt.debug.internal.crossfire.transport;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link Packet} for replying to the Crossfire handshake request
 * 
 * @since 1.0
 */
public class HandShake extends Packet {
	
	/**
	 * handshake
	 */
	private static String CROSSFIRE_HANDSHAKE;
	public static final String HANDSHAKE = "handshake"; //$NON-NLS-1$

	static {
		String hs = "CrossfireHandshake\r\n"; //$NON-NLS-1$
		try {
			CROSSFIRE_HANDSHAKE = new String(hs.getBytes(), "utf-8"); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			CROSSFIRE_HANDSHAKE = hs;
		} 
	}
	
	/**
	 * Constructor
	 * @param type
	 */
	protected HandShake() {
		super(Response.RESPONSE);
	}

	/**
	 * @return the handshake
	 */
	public static String getHandshake() {
		return CROSSFIRE_HANDSHAKE;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Packet#toString()
	 */
	public String toString() {
		return CROSSFIRE_HANDSHAKE;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Packet#toJSON()
	 */
	public Map toJSON() {
		Map json = new HashMap(1);
		json.put(HANDSHAKE, CROSSFIRE_HANDSHAKE);
		return json;
	}
}
