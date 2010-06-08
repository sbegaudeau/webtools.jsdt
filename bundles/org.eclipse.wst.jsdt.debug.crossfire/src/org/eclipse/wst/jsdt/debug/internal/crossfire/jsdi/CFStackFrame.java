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
package org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame;
import org.eclipse.wst.jsdt.debug.core.jsdi.Value;
import org.eclipse.wst.jsdt.debug.core.jsdi.Variable;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.crossfire.Tracing;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Attributes;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Commands;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.JSON;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Request;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Response;

/**
 * Default implementation of {@link StackFrame} for Crossfire
 * 
 * @since 1.0
 */
public class CFStackFrame extends CFMirror implements StackFrame {

	private int index = -1;
	String context_id = null;
	private String scriptid = null;
	private String funcname = null;
	private int linenumber = -1;
	private List vars = null;
	private Variable thisvar = null;
	private CFLocation loc = null;
	
	/**
	 * Constructor
	 * @param vm
	 * @param json
	 */
	public CFStackFrame(VirtualMachine vm, Map json) {
		super(vm);
		context_id = (String) json.get(Attributes.CONTEXT_ID);
		Number value = (Number) json.get(Attributes.INDEX);
		if(value != null) {
			index = value.intValue();
		}
		value = (Number) json.get(Attributes.LINE);
		if(value != null) {
			linenumber = value.intValue();
		}
		scriptid = (String) json.get(Attributes.SCRIPT);
		funcname = (String) json.get(Attributes.FUNC);
		parseLocals((Map) json.get(Attributes.LOCALS));
	}

	/**
	 * Read the local variable information from the json mapping
	 * 
	 * @param json
	 */
	void parseLocals(Map json) {
		if(json != null) {
			Map locals = (Map) json.get(Attributes.VALUE); 
			if(locals != null) {
				locals = (Map) locals.get(Attributes.VALUE); 
				if(locals.size() < 1) {
					vars = Collections.EMPTY_LIST;
					return;
				}
				vars = new ArrayList(locals.size());
				Entry entry = null;
				for (Iterator iter = locals.entrySet().iterator(); iter.hasNext();) {
					entry = (Entry) iter.next();
					Map info  = (Map) entry.getValue();
					String name = (String) entry.getKey();
					Object handle = info.get(Attributes.HANDLE);
					Number ref = null;
					if(handle instanceof Number) {
						ref = (Number) handle;
					}
					else if(handle instanceof String) {	
						ref = new Integer((String)handle);
					}
					vars.add(new CFVariable(crossfire(), this, name, ref, false));
				}
			}
			Map thismap = (Map) json.get(Attributes.THIS); 
			if(thismap != null) {
				if(vars == null) {
					vars = new ArrayList(2);
				}
				thisvar = new CFVariable(crossfire(), this, Attributes.THIS, null, false);  
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame#thisObject()
	 */
	public Variable thisObject() {
		return thisvar;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame#variables()
	 */
	public synchronized List variables() {
		if(vars != null) {
			return vars;
		}
		return Collections.EMPTY_LIST; 
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame#location()
	 */
	public synchronized Location location() {
		if(loc == null) {
			CFScriptReference script = crossfire().findScript(scriptid); 
			if(script != null) {
				loc = new CFLocation(crossfire(), script, funcname, linenumber);
			}
		}
		return loc;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame#evaluate(java.lang.String)
	 */
	public Value evaluate(String expression) {
		Request request = new Request(Commands.EVALUATE, context_id);
		request.setArgument(Attributes.FRAME, new Integer(index));
		request.setArgument(Attributes.EXPRESSION, expression);
		Response response = crossfire().sendRequest(request);
		if(response.isSuccess()) {
			return createValue(response.getBody());
		}
		else if(TRACE) {
			Tracing.writeString("STACKFRAME [failed evaluate request]: "+JSON.serialize(request)); //$NON-NLS-1$
		}
		return null;
	}
	
	/**
	 * Returns the index of the frame in the stack
	 * 
	 * @return the frame index
	 */
	public int frameindex() {
		return index;
	}
	
	/**
	 * Looks up the value given its handle ref
	 * 
	 * @param ref
	 * @return the {@link Value} or <code>null</code>
	 */
	public Value lookup(Number ref) {
		if(ref != null) {
			Request request = new Request(Commands.LOOKUP, context_id);
			request.setArgument(Attributes.HANDLE, ref);
			Response response = crossfire().sendRequest(request);
			if(response.isSuccess()) {
				return createValue(response.getBody());
			}
			else if(TRACE) {
				Tracing.writeString("STACKFRAME [request for value lookup failed]: "+JSON.serialize(request)); //$NON-NLS-1$
			}
		}
		return null;
	}
	
	/**
	 * Creates the correct type if {@link Value} from the given json mapping
	 * @param json
	 * @return the new {@link Value} or <code>null</code> if one could not be created
	 */
	Value createValue(Map json) {
		//resolve the smallest type from the crossfire insanity
		Map smallest = json;
		Object o = json.get(Attributes.VALUE);
		if(o instanceof Map) {
			Map temp = smallest;
			while(temp != null) {
				temp = (Map) temp.get(Attributes.VALUE);
				if(temp != null && temp.containsKey(Attributes.VALUE)) {
					smallest = temp;
				}
			}
		}
		Object tobj = smallest.get(Attributes.TYPE);
		String type = null;
		if(tobj instanceof String) {
			type = (String) tobj;
		}
		else if(tobj instanceof Map) {
			type = (String) ((Map)tobj).get(Attributes.TYPE);
		}
		if(CFUndefinedValue.UNDEFINED.equals(type)) {
			return crossfire().mirrorOfUndefined();
		}
		if(CFNullValue.NULL.equals(type) || type == null) {
			return crossfire().mirrorOfNull();
		}
		if(CFStringValue.STRING.equals(type)) {
			//TODO
			return crossfire().mirrorOf(smallest.get(Attributes.VALUE).toString());
		}
		if(CFObjectReference.OBJECT.equals(type)) {
			return new CFObjectReference(crossfire(), this, json);
		}
		if(CFArrayReference.ARRAY.equals(type)) {
			return new CFArrayReference(crossfire(), this, json);
		}
		if(CFFunctionReference.FUNCTION.equals(type)) {
			return new CFFunctionReference(crossfire(), this, json);
		}
		return null;
	}
	
	/**
	 * Returns if this stack frame is visible
	 * 
	 * @param variable
	 * @return true if this frame is visible, false otherwise
	 */
	public synchronized boolean isVisible(CFVariable variable) {
		return vars != null && (thisvar == variable || vars.contains(variable));
	}
}
