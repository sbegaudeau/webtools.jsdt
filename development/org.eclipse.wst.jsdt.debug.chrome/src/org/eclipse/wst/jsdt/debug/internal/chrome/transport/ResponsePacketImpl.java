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
import java.util.HashMap;
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
	
	private String command = null;
	protected Number result = null;
	protected Map body = null;
	
	/**
	 * Constructor
	 * @param command
	 * @param tool the name of the tools service that issued this response
	 */
	public ResponsePacketImpl(String command, String tool) {
		super(RESPONSE, tool);
		if(command == null) {
			throw new IllegalArgumentException(Messages.cannot_create_response_null_command);
		}
		this.command = command;
	}
	
	/**
	 * Constructor
	 * @param json
	 */
	public ResponsePacketImpl(Map json) {
		super(json);
		this.command = (String) json.get(Attributes.COMMAND);
		if(command == null) {
			throw new IllegalArgumentException(Messages.no_command_in_json_response);
		}
		this.result = (Number) json.get(Attributes.RESULT);
		Object data = json.get(Attributes.DATA);
		if(data != null) {
			this.body = new HashMap();
			this.body.put(Attributes.DATA, data);
		}
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
		return -1;
	}

	/**
	 * Returns the result of the request that caused this response
	 * @see Attributes#OK
	 * @see Attributes#ILLEGAL_TAB_STATE
	 * @see Attributes#UNKNOWN_TAB
	 * @see Attributes#DEBUGGER_ERROR
	 * @see Attributes#UNKNOWN_COMMAND
	 * @return the value of the result
	 */
	public int getResult() {
		return this.result.intValue();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Response#getBody()
	 */
	public Map getBody() {
		if(this.body == null) {
			return Collections.EMPTY_MAP;
		}
		return this.body;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Response#isSuccess()
	 */
	public boolean isSuccess() {
		return this.result.intValue() == Attributes.OK;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Response#isRunning()
	 */
	public boolean isRunning() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.chrome.transport.PacketImpl#toJSON()
	 */
	public Map toJSON() {
		Map json = super.toJSON();
		json.put(Attributes.COMMAND, command);
		json.put(Attributes.RESULT, this.result);
		if(this.body != null) {
			json.put(Attributes.DATA, this.body.get(Attributes.DATA));
		}
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
