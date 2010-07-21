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
import java.util.Map;

import org.eclipse.wst.jsdt.debug.transport.packet.Response;

/**
 * Response packet
 * 
 * @since 1.0
 */
public class ResponsePacketImpl extends PacketImpl implements Response {

	/**
	 * The type of this packet
	 */
	public static final String RESPONSE = "response"; //$NON-NLS-1$
	
	private int rseq = 0;
	private String command = null;
	private Map body = null;
	private boolean success = false;
	private boolean running = false;
	
	/**
	 * Constructor
	 * @param requestSequence
	 * @param command
	 * @param tool the name of the tools service that issued this response
	 */
	public ResponsePacketImpl(int requestSequence, String command, String tool) {
		super(RESPONSE, tool);
		if(command == null) {
			throw new IllegalArgumentException(Messages.cannot_create_response_null_command);
		}
		this.rseq = requestSequence;
		this.command = command;
	}
	
	/**
	 * Constructor
	 * @param json
	 */
	public ResponsePacketImpl(Map json) {
		super(json);
		this.rseq = ((Integer)json.get(Attributes.REQUEST_SEQ)).intValue();
		this.command = (String) json.get(Attributes.COMMAND);
		if(command == null) {
			throw new IllegalArgumentException(Messages.no_command_in_json_response);
		}
		this.body = (Map) json.get(Attributes.BODY);
		this.success = ((Boolean)json.get(Attributes.SUCCESS)).booleanValue();
		this.running = ((Boolean)json.get(Attributes.RUNNING)).booleanValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Response#getCommand()
	 */
	public String getCommand() {
		return command;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Response#getRequestSequence()
	 */
	public int getRequestSequence() {
		return rseq;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Response#getBody()
	 */
	public Map getBody() {
		if(body == null) {
			return Collections.EMPTY_MAP;
		}
		return body;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Response#isSuccess()
	 */
	public boolean isSuccess() {
		return success;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Response#isRunning()
	 */
	public boolean isRunning() {
		return running;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.chrome.transport.PacketImpl#toJSON()
	 */
	public Map toJSON() {
		Map json = super.toJSON();
		json.put(Attributes.COMMAND, command);
		json.put(Attributes.REQUEST_SEQ, new Integer(rseq));
		if(body != null) {
			json.put(Attributes.BODY, body);
		}
		json.put(Attributes.SUCCESS, Boolean.valueOf(success));
		json.put(Attributes.RUNNING, Boolean.valueOf(running));
		return json;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		Object json = toJSON();
		buffer.append("ResponsePacketImpl: "); //$NON-NLS-1$
		JSON.writeValue(json, buffer);
		return buffer.toString();
	}
}
