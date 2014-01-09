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

import org.eclipse.wst.jsdt.debug.transport.packet.Response;

/**
 * Default response packet implementation for JSD2
 * 
 * @since 1.0
 */
public class ResponsePacketImpl extends PacketImpl implements Response {

	/**
	 * The name of the command from the original request
	 */
	String fCommand;
	
	/**
	 * Constructor
	 * 
	 * @param command
	 */
	public ResponsePacketImpl(String command) {
		super(Attributes.FROM);
		fCommand = command;
	}

	/**
	 * Constructor
	 * @param json
	 */
	public ResponsePacketImpl(Map json) {
		super(json);
		fCommand = (String) json.get(Attributes.TYPE);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Response#getCommand()
	 */
	public String getCommand() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Response#getRequestSequence()
	 */
	public int getRequestSequence() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Response#getBody()
	 */
	public Map getBody() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Response#isSuccess()
	 */
	public boolean isSuccess() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Response#isRunning()
	 */
	public boolean isRunning() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Packet#toJSON()
	 */
	public Map toJSON() {
		return null;
	}
}
