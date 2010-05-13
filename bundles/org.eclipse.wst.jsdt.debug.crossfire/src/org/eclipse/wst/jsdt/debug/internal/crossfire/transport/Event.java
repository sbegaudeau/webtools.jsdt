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


/**
 * An {@link Event} is a specialized {@link Packet}
 * that only handles <code>event</code> data.
 * 
 * @since 1.0
 */
public class Event extends Packet {

	/**
	 * The type of this packet
	 */
	public static final String EVENT = "event"; //$NON-NLS-1$
	/**
	 * The "closed" event kind
	 */
	public static final String CLOSED = "closed"; //$NON-NLS-1$
	/**
	 * The "onScript" event kind
	 */
	public static final String ON_SCRIPT = "onScript"; //$NON-NLS-1$
	/**
	 * The "onContextCreated" event kind
	 */
	public static final String ON_CONTEXT_CREATED = "onContextCreated"; //$NON-NLS-1$
	/**
	 * The "onContextDestroyed" event kind
	 */
	public static final String ON_CONTEXT_DESTROYED = "onContextDestroyed"; //$NON-NLS-1$
	/**
	 * The "data" attribute
	 */
	public static final String DATA = "data"; //$NON-NLS-1$
	
	private final String event;
	private final Map body = Collections.synchronizedMap(new HashMap());
	
	
	/**
	 * Constructor
	 * @param event
	 */
	public Event(String event) {
		super(EVENT);
		this.event = event.intern();
	}

	/**
	 * Constructor
	 * @param json
	 */
	public Event(Map json) {
		super(json);
		String packetEvent = (String) json.get(EVENT);
		event = packetEvent.intern();
		Object data = json.get(DATA);
		if(data instanceof Map) {
			body.putAll((Map) data);
		}
		if(data instanceof String) {
			body.put(DATA, data);
		}
	}

	/**
	 * Returns the underlying event data
	 * @return the event data
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * Returns the underlying body of the event packet
	 * @return the body of the packet
	 */
	public Map getBody() {
		return body;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.core.jsdi.connect.Packet#toJSON()
	 */
	public Map toJSON() {
		Map json = super.toJSON();
		json.put(EVENT, event);
		json.put(BODY, body);
		return json;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Event: ").append(JSON.serialize(this)); //$NON-NLS-1$
		return buffer.toString();
	}
}
