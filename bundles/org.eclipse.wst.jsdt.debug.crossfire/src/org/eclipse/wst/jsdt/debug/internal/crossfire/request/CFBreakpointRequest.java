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
package org.eclipse.wst.jsdt.debug.internal.crossfire.request;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.BreakpointRequest;
import org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi.CFScriptReference;
import org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi.CFVirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Attributes;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFRequestPacket;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFResponsePacket;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Commands;

/**
 * Default implementation of {@link BreakpointRequest} for Crossfire
 * 
 * @since 1.0
 */
public class CFBreakpointRequest extends CFThreadEventRequest implements BreakpointRequest {

	private String condition = null;
	private int hitcount = 0;
	private Location location = null;
	private Long bpid = null;
	
	/**
	 * Constructor
	 * @param vm
	 * @param location
	 */
	public CFBreakpointRequest(VirtualMachine vm, Location location) {
		super(vm);
		this.location = location;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Locatable#location()
	 */
	public Location location() {
		return location;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.BreakpointRequest#addThreadFilter(org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference)
	 */
	public void addThreadFilter(ThreadReference thread) {
		setThread(thread);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.BreakpointRequest#addConditionFilter(java.lang.String)
	 */
	public void addConditionFilter(String condition) {
		this.condition = condition;
	}

	/**
	 * Returns the {@link String} condition set for this request using {@link #addConditionFilter(String)} or <code>null</code>
	 * 
	 * @return the condition filter or <code>null</code>
	 */
	public String getConditionFilter() {
		return condition;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.BreakpointRequest#addHitCountFilter(int)
	 */
	public void addHitCountFilter(int hitcount) {
		this.hitcount = hitcount;
	}

	/**
	 * Returns the hit count set using {@link #addHitCountFilter(int)} or <code>0</code>
	 * 
	 * @return the specified hit count or <code>0</code>
	 */
	public int getHitCount() {
		return hitcount;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.crossfire.request.CFEventRequest#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if(enabled) {
			//send setbreakpoint request
			CFScriptReference script = (CFScriptReference) location.scriptReference();
			CFRequestPacket request = new CFRequestPacket(Commands.SET_BREAKPOINT, script.context());
			request.setArgument(Attributes.TYPE, Attributes.LINE);
			Map loc = new HashMap();
			loc.put(Attributes.LINE, new Integer(location.lineNumber()));
			loc.put(Attributes.URL, script.id());
			request.setArgument(Attributes.LOCATION, loc);
			if (condition != null) {
				request.setArgument(Attributes.CONDITION, condition);	
			}
			request.setArgument(Attributes.ENABLED, Boolean.TRUE);
			CFResponsePacket response = ((CFVirtualMachine)virtualMachine()).sendRequest(request);
			if(response.isSuccess()) {
				//process the response to get the id of the breakpoint
				Map bp = (Map) response.getBody().get(Attributes.BREAKPOINT);
				if(bp != null) {
					Number id = (Number) bp.get(Attributes.HANDLE);
					bpid = new Long(id.longValue());
				}
			}
		}
		else if(bpid != null) {
			//send clearbreakpoint request
			CFScriptReference script = (CFScriptReference) location.scriptReference();
			CFRequestPacket request = new CFRequestPacket(Commands.CLEAR_BREAKPOINT, script.context());
			request.getArguments().put(Attributes.HANDLE, bpid);
			CFResponsePacket response = ((CFVirtualMachine)virtualMachine()).sendRequest(request);
			if(response.isSuccess()) {
				bpid = null;
			}
		}
	}
}
