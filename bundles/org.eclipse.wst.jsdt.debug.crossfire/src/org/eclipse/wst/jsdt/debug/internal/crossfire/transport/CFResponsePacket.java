/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others All rights reserved. This
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
	
	/**
	 * Response codes
	 */
	public static final int CODE_OK = 0;
	public static final int CODE_MALFORMED_PACKET = 1;
	public static final int CODE_MALFORMED_REQUEST = 2;
	public static final int CODE_COMMAND_NOT_IMPLEMENTED = 3;
	public static final int CODE_INVALID_ARGUMENTS = 4;
	public static final int CODE_UNEXPECTED_EXCEPTION = 5;
	public static final int CODE_COMMAND_FAILED = 6;
	public static final int CODE_INVALID_STATE = 7;

	static final Map failed_attributes;
	static {
		failed_attributes = new HashMap();
		Integer value = new Integer(-1);
		failed_attributes.put(Attributes.SEQ, value);
		failed_attributes.put(Attributes.TYPE, RESPONSE);
		failed_attributes.put(Attributes.REQUEST_SEQ, value);
		failed_attributes.put(Attributes.COMMAND, "failed"); //$NON-NLS-1$
		Map status = new HashMap();
		failed_attributes.put(Attributes.STATUS, status);
		status.put(Attributes.CODE, new Integer(CODE_UNEXPECTED_EXCEPTION));
		status.put(Attributes.RUNNING, Boolean.FALSE);
		status.put(Attributes.MESSAGE, "failed"); //$NON-NLS-1$
	}
	
	public static final CFResponsePacket FAILED = new CFResponsePacket(failed_attributes);
	
	private String command;
	private int requestSequence;
	private Map body = Collections.synchronizedMap(new HashMap());
	private volatile int code = 0;
	private volatile boolean running = true;
	private volatile String message;
	private Map stackTrace = Collections.synchronizedMap(new HashMap());

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

		Object status = json.get(Attributes.STATUS);
		if (status != null && status instanceof Map) {
			Map packetStatus = (Map)status;
			Object codeObj = packetStatus.get(Attributes.CODE);
			if (codeObj != null) {
				code = ((Number)codeObj).intValue();
			}
			Boolean runningObj = (Boolean)packetStatus.get(Attributes.RUNNING);
			if (runningObj != null) {
				running = runningObj.booleanValue();
			}
			message = (String)packetStatus.get(Attributes.MESSAGE);
			stackTrace = (Map)packetStatus.get(Attributes.STACKTRACE);
		}
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
		return code == CODE_OK;
	}

	/**
	 * Set the success code for the response
	 * 
	 * @param code the new success code
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * Get the success code for the response
	 * 
	 * return the success code
	 */
	public int getCode() {
		return code;
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
	
	/**
	 * Returns the stack trace for this {@link CFResponsePacket}.<br>
	 * <br>
	 * This method can return <code>null</code>
	 * 
	 * @return the stack trace for this {@link CFResponsePacket} or <code>null</code>
	 */
	public Map getStackTrace() {
		return stackTrace;
	}

	/**
	 * Set the stack trace for this {@link CFResponsePacket}
	 * 
	 * @param stackTrace the new stack trace, <code>null</code> is accepted
	 */
	public void setStackTrace(Map stackTrace) {
		this.stackTrace = stackTrace;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFPacket#toJSON()
	 */
	public Map toJSON() {
		Map json = super.toJSON();
		json.put(Attributes.REQUEST_SEQ, new Integer(requestSequence));
		json.put(Attributes.COMMAND, command);
		if(body != null && body.size() > 0) {
			json.put(Attributes.BODY, body);
		}
		Map status = new HashMap();
		json.put(Attributes.STATUS, status);
		status.put(Attributes.RUNNING, new Boolean(running));
		status.put(Attributes.CODE, new Integer(code));
		if (message != null) {
			status.put(Attributes.MESSAGE, message);
		}
		if (stackTrace != null) {
			status.put(Attributes.STACKTRACE, stackTrace);
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
