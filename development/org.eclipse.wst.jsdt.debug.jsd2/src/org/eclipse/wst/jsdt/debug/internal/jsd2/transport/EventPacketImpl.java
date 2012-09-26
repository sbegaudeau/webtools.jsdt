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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.wst.jsdt.debug.transport.packet.Event;

/**
 * Default event implementation for JSD2
 * 
 * @since 1.0
 */
public class EventPacketImpl extends PacketImpl implements Event {

	/**
	 * The type of this packet
	 */
	public static final String EVENT = "event"; //$NON-NLS-1$
	/**
	 * The name of the event
	 */
	final String fEvent;
	/**
	 * The body of the event
	 */
	final Map fBody;
	
	/**
	 * Constructor
	 */
	public EventPacketImpl(String event) {
		super(EVENT);
		fEvent = event;
		fBody = Collections.synchronizedMap(new HashMap());
	}

	/**
	 * Constructor
	 * 
	 * @param json
	 */
	public EventPacketImpl(Map json) {
		super(json);
		fEvent = (String) json.get(EVENT);
		Assert.isNotNull(fEvent, Messages.EventPacketImpl_cannot_create_event_packet_null_event_name);
		//TODO collect the body from the JSON properly
		fBody = Collections.synchronizedMap(new HashMap());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Event#getEvent()
	 */
	public String getEvent() {
		return fEvent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Event#getBody()
	 */
	public Map getBody() {
		return fBody;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFPacket#toJSON()
	 */
	public Map toJSON() {
		Map json = new HashMap();
		json.put(EVENT, fEvent);
		if(fBody != null && fBody.size() > 0) {
			//TODO add the body spec
		}
		return json;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		Object json = toJSON();
		buffer.append("EventPacketImpl: "); //$NON-NLS-1$
		JSON.writeValue(json, buffer);
		return buffer.toString();
	}
}
