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
import java.util.Map;

import org.eclipse.wst.jsdt.debug.transport.packet.Response;

/**
 * Default {@link CFResponsePacket} implementation using JSON
 * 
 * @since 1.0
 */
public class CFResponsePacket extends CFPacket implements Response {

	/**
	 * The type of this packet
	 */
	public static final String RESPONSE = "response"; //$NON-NLS-1$
	
	static final Map failed_attributes;
	static {
		failed_attributes = new HashMap();
		Integer value = new Integer(-1);
		failed_attributes.put(Attributes.SEQ, value);
		failed_attributes.put(Attributes.TYPE, RESPONSE);
		failed_attributes.put(Attributes.REQUEST_SEQ, value);
		failed_attributes.put(Attributes.COMMAND, "failed"); //$NON-NLS-1$
		failed_attributes.put(Attributes.SUCCESS, Boolean.FALSE);
		failed_attributes.put(Attributes.RUNNING, Boolean.FALSE);
	}
	
	public static final CFResponsePacket FAILED = new CFResponsePacket(failed_attributes);
	
	private String command;
	private int requestSequence;
	private Map body = Collections.synchronizedMap(new HashMap());
	private volatile boolean success = true;
	private volatile boolean running = true;
	private volatile String message;

	/**
	 * Constructor
	 * 
	 * @param requestSequence the sequence
	 * @param command the command, <code>null</code> is not accepted
	 */
	public CFResponsePacket(int requestSequence, String command) {
		super(RESPONSE, null);
		if(command == null) {
			throw new IllegalArgumentException("The command string for a response packet cannot be null"); //$NON-NLS-1$
		}
		this.requestSequence = requestSequence;
		this.command = command.intern();
	}

	/**
	 * Constructor
	 * 
	 * @param json the JSON map for a response, <code>null</code> is not accepted
	 */
	public CFResponsePacket(Map json) {
		super(json);
		Number packetRequestSeq = (Number) json.get(Attributes.REQUEST_SEQ);
		requestSequence = packetRequestSeq.intValue();

		String packetCommand = (String) json.get(Attributes.COMMAND);
		command = packetCommand.intern();

		Object bdy = json.get(Attributes.BODY);
		if(bdy instanceof Map) {
			Map packetBody = (Map)bdy; 
			body.putAll(packetBody);
		}

		Boolean packetSuccess = (Boolean) json.get(Attributes.SUCCESS);
		success = packetSuccess.booleanValue();

		Boolean packetRunning = (Boolean) json.get(Attributes.RUNNING);
		running = packetRunning.booleanValue();

		message = (String) json.get(Attributes.MESSAGE);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Response#getRequestSequence()
	 */
	public int getRequestSequence() {
		return requestSequence;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Response#getCommand()
	 */
	public String getCommand() {
		return command;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Response#getBody()
	 */
	public Map getBody() {
		return body;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Response#isSuccess()
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * Set the success flag for the response
	 * 
	 * @param success the new success flag
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Response#isRunning()
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Sets the running state of the underlying command
	 * 
	 * @param running the new running state for the underlying command
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * Returns the status message for this {@link CFResponsePacket}.<br>
	 * <br>
	 * This method can return <code>null</code>
	 * 
	 * @return the status message for this {@link CFResponsePacket} or <code>null</code>
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set the status message for this {@link CFResponsePacket}
	 * 
	 * @param message the new message, <code>null</code> is accepted
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFPacket#toJSON()
	 */
	public Map toJSON() {
		Map json = super.toJSON();
		json.put(Attributes.REQUEST_SEQ, new Integer(requestSequence));
		json.put(Attributes.COMMAND, command);
		json.put(Attributes.BODY, body);
		json.put(Attributes.SUCCESS, new Boolean(success));
		json.put(Attributes.RUNNING, new Boolean(running));
		if (message != null) {
			json.put(Attributes.MESSAGE, message);
		}
		return json;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		Object json = toJSON();
		buffer.append("CFResponsePacket: "); //$NON-NLS-1$
		JSON.writeValue(json, buffer);
		return buffer.toString();
	}
}
