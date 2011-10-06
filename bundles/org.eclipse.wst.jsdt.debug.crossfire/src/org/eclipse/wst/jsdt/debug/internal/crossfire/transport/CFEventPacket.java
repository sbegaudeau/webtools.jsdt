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
import java.util.List;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.transport.packet.Event;


/**
 * An {@link CFEventPacket} is a specialized {@link CFPacket}
 * that only handles <code>event</code> data.
 * 
 * @since 1.0
 */
public class CFEventPacket extends CFPacket implements Event {

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
	 * The "onBreak" event kind
	 */
	public static final String ON_BREAK = "onBreak"; //$NON-NLS-1$
	/**
	 * The "onConsoleDebug" event kind
	 */
	public static final String ON_CONSOLE_DEBUG = "onConsoleDebug"; //$NON-NLS-1$
	/**
	 * The "onConsoleError" event kind
	 */
	public static final String ON_CONSOLE_ERROR = "onConsoleError"; //$NON-NLS-1$
	/**
	 * The "onConsoleInfo" event kind
	 */
	public static final String ON_CONSOLE_INFO = "onConsoleInfo"; //$NON-NLS-1$
	/**
	 * The "onConsoleLog" event kind
	 */
	public static final String ON_CONSOLE_LOG = "onConsoleLog"; //$NON-NLS-1$
	/**
	 * The "onConsoleWarn" event kind
	 */
	public static final String ON_CONSOLE_WARN = "onConsoleWarn"; //$NON-NLS-1$
	/**
	 * The "onError" event kind
	 */
	public static final String ON_ERROR = "onError"; //$NON-NLS-1$
	/**
	 * The "onInspectNode" event kind
	 */
	public static final String ON_INSPECT_NODE = "onInspectNode"; //$NON-NLS-1$
	/**
	 * The "onResume" event kind
	 */
	public static final String ON_RESUME = "onResume"; //$NON-NLS-1$
	/**
	 * The "onToggleBreakpoint" event kind
	 */
	public static final String ON_TOGGLE_BREAKPOINT = "onToggleBreakpoint"; //$NON-NLS-1$
	/**
	 * The "onContextSelected" event kind
	 */
	public static final String ON_CONTEXT_SELECTED = "onContextSelected"; //$NON-NLS-1$
	/**
	 * The "onContextDestroyed" event kind
	 */
	public static final String ON_CONTEXT_DESTROYED = "onContextDestroyed"; //$NON-NLS-1$
	/**
	 * The "onContextCreated" event kind
	 */
	public static final String ON_CONTEXT_CREATED = "onContextCreated"; //$NON-NLS-1$
	/**
	 * The "onContextLoaded" event kind
	 */
	public static final String ON_CONTEXT_LOADED = "onContextLoaded"; //$NON-NLS-1$
	
	private final String event;
	private final Map body = Collections.synchronizedMap(new HashMap());
	
	/**
	 * Constructor
	 * @param event
	 */
	public CFEventPacket(String event) {
		super(EVENT, null);
		this.event = event.intern();
	}

	/**
	 * Constructor
	 * @param json
	 */
	public CFEventPacket(Map json) {
		super(json);
		String packetEvent = (String) json.get(EVENT);
		event = packetEvent.intern();
		Object data = json.get(Attributes.BODY);
		if(data instanceof Map) {
			body.putAll((Map) data);
		}
		else if(data instanceof String ||
				data instanceof List) {
			body.put(Attributes.BODY, data);
		}
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFPacket#getContextId()
	 */
	public String getContextId() {
		String id = super.getContextId();
		if(id == null) {
			id = (String) body.get(Attributes.CONTEXT_ID);
		}
		return id;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Event#getEvent()
	 */
	public String getEvent() {
		return event;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.packet.Event#getBody()
	 */
	public Map getBody() {
		return body;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFPacket#toJSON()
	 */
	public Map toJSON() {
		Map json = super.toJSON();
		json.put(EVENT, event);
		if(body.size() > 0) {
			json.put(Attributes.BODY, body);
		}
		return json;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		Object json = toJSON();
		buffer.append("CFEventPacket: "); //$NON-NLS-1$
		JSON.writeValue(json, buffer);
		return buffer.toString();
	}
}
