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
package org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.NullValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame;
import org.eclipse.wst.jsdt.debug.core.jsdi.Value;
import org.eclipse.wst.jsdt.debug.core.jsdi.Variable;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.crossfire.Tracing;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Attributes;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFRequestPacket;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFResponsePacket;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Commands;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.JSON;

/**
 * Default implementation of {@link StackFrame} for Crossfire
 * 
 * @since 1.0
 */
public class CFStackFrame extends CFMirror implements StackFrame {

	/**
	 * Describes a scope
	 */
	class Scope {
		Number idx = null;
		Number fidx = null;
		Number ref = null;
		public Scope(Number idx, Number fidx, Number ref) {
			this.idx = idx;
			this.fidx = fidx;
			this.ref = ref;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return idx.hashCode() + fidx.hashCode() + ref.hashCode();
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object obj) {
			if(obj instanceof Scope) {
				Scope s = (Scope) obj;
				return idx.equals(s.idx) && fidx.equals(s.fidx) && ref.equals(s.ref);
			}
			return false;
		}
	}
	
	HashSet scopes = new HashSet();
	
	private int index = -1;
	private String scriptid = null;
	private String funcname = null;
	private int linenumber = -1;
	private List vars = null;
	private Variable thisvar = null;
	private CFLocation loc = null;
	private CFThreadReference thread = null;
	
	/**
	 * Constructor
	 * @param vm
	 * @param json
	 */
	public CFStackFrame(VirtualMachine vm, CFThreadReference thread, Map json) {
		super(vm);
		this.thread = thread;
		Number value = (Number) json.get(Attributes.INDEX);
		if(value != null) {
			index = value.intValue();
		}
		value = (Number) json.get(Attributes.LINE);
		if(value != null) {
			linenumber = value.intValue();
		}
		scriptid = (String) json.get(Attributes.URL);
		funcname = (String) json.get(Attributes.FUNCTION_NAME);
		
		parseScopes((List) json.get(Attributes.SCOPES));
		parseLocals((Map) json.get(Attributes.LOCALS));
		//scope();
		//allScopes();
	}

	/**
	 * Parses the scopes object node, if there is one
	 * 
	 * @param list the list of scopes
	 */
	void parseScopes(List list) {
		if(list != null) {
			for (Iterator i = list.iterator(); i.hasNext();) {
				Map map = (Map) i.next();
				Scope s = new Scope(
						(Number)map.get(Attributes.INDEX), 
						(Number)map.get(Attributes.FRAME_INDEX), 
						(Number) ((Map)map.get(Attributes.OBJECT)).get(Attributes.HANDLE));
				scopes.add(s);
			}
		}
	}
	
	/**
	 * Read the local variable information from the json mapping
	 * 
	 * @param json
	 */
	void parseLocals(Map json) {
		if(json != null) {
			Object val = json.get(Attributes.VALUE);
			if(val instanceof Map) {
				Map locals = (Map) json.get(Attributes.VALUE); 
				if(locals != null) {
					vars = new ArrayList(locals.size());
					parseVariables(locals, vars);
				}
			}
			else {
				vars = new ArrayList();
			}
			Map thismap = (Map) json.get(Attributes.THIS); 
			thisvar = new CFVariable(crossfire(), this, Attributes.THIS, null, (thismap == null ? new HashMap(0) : thismap));
		}
	}
	
	void parseVariables(Map map, List varcollector) {
		Entry entry = null;
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			entry = (Entry) iter.next();
			if(entry.getValue() instanceof Map) {
				Map info  = (Map) entry.getValue();
				varcollector.add(
						new CFVariable(
								crossfire(), 
								this, 
								(String) entry.getKey(), 
								(Number) info.get(Attributes.HANDLE), 
								info));
			}
			else {
				varcollector.add(
						new CFVariable(
								crossfire(), 
								this, 
								(String) entry.getKey(), 
								null, 
								null));
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
		CFRequestPacket request = new CFRequestPacket(Commands.EVALUATE, thread.id());
		request.setArgument(Attributes.FRAME_INDEX, new Integer(index));
		request.setArgument(Attributes.EXPRESSION, expression);
		CFResponsePacket response = crossfire().sendRequest(request);
		if(response.isSuccess()) {
			return createValue(response.getBody().get(Attributes.RESULT));
		}
		else if(TRACE) {
			Tracing.writeString("STACKFRAME [failed evaluate request]: "+JSON.serialize(request)); //$NON-NLS-1$
		}
		return virtualMachine().mirrorOfNull();
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
			CFRequestPacket request = new CFRequestPacket(Commands.LOOKUP, thread.id());
			request.setArgument(Attributes.HANDLE, ref);
			request.setArgument(Attributes.INCLUDE_SOURCE, Boolean.TRUE);
			CFResponsePacket response = crossfire().sendRequest(request);
			if(response.isSuccess()) {
				return createValue(response.getBody());
			}
			else if(TRACE) {
				Tracing.writeString("STACKFRAME [request for value lookup failed]: "+JSON.serialize(request)); //$NON-NLS-1$
			}
		}
		return crossfire().mirrorOfNull();
	}
	
	/**
	 * Creates the correct type if {@link Value} from the given json mapping
	 * @param json
	 * @return the new {@link Value} or <code>null</code> if one could not be created
	 */
	Value createValue(Object val) {
		//resolve the smallest type from the crossfire insanity
		if(val instanceof Map) {
			Map values = (Map) val;
			String type = (String) values.get(Attributes.TYPE);
			if(type != null) {
				return createTypeValue(type, values);
			}
		}
		else if(val instanceof String) {
			String str = (String) val;
			if(CFUndefinedValue.UNDEFINED.equals(str)) {
				return crossfire().mirrorOfUndefined();
			}
			return crossfire().mirrorOf((String) val); 
		}
		else if(val instanceof Number) {
			return crossfire().mirrorOf((Number) val);
		}
		return crossfire().mirrorOfNull();
	}
	
	/**
	 * Create a new {@link Value} based on the given type
	 * 
	 * @param type the type
	 * @param map the map of value information
	 * @return the new {@link Value} for the given type or {@link NullValue} if a value cannot be computed
	 */
	Value createTypeValue(String type, Map map) {
		if(CFUndefinedValue.UNDEFINED.equals(type)) {
			return crossfire().mirrorOfUndefined();
		}
		if(Attributes.NUMBER.equals(type)) {
			//could be NaN, Infinity or -Infinity, check for strings
			Object o = map.get(Attributes.VALUE);
			if(o instanceof Number) {
				return crossfire().mirrorOf((Number)o);
			}
			if(o instanceof String) {
				return crossfire().mirrorOf((String)o);
			}
		}
		if(CFStringValue.STRING.equals(type)) {
			return crossfire().mirrorOf(map.get(Attributes.VALUE).toString());
		}
		if(CFObjectReference.OBJECT.equals(type) || Attributes.REF.equals(type)) {
			return new CFObjectReference(crossfire(), this, map);
		}
		if(CFArrayReference.ARRAY.equals(type)) {
			return new CFArrayReference(crossfire(), this, map);
		}
		if(CFFunctionReference.FUNCTION.equals(type)) {
			return new CFFunctionReference(crossfire(), this, map);
		}
		return crossfire().mirrorOfNull();
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
	
	/**
	 * Gets all of the scopes from Firebug
	 */
	void allScopes() {
		CFRequestPacket request = new CFRequestPacket(Commands.SCOPES, thread.id());
		request.setArgument(Attributes.FRAME_NUMBER, new Integer(index));
		CFResponsePacket response = crossfire().sendRequest(request);
		if(response.isSuccess()) {
			List list = (List) response.getBody().get(Attributes.SCOPES);
			if(list != null) {
				parseScopes(list);
			}
		}
		else if(TRACE) {
			Tracing.writeString("VM [failed scopes request]: "+JSON.serialize(request)); //$NON-NLS-1$
		}
	}
	
	/**
	 * Gets the scope for this frame
	 */
	void scope() {
		CFRequestPacket request = new CFRequestPacket(Commands.SCOPE, thread.id());
		request.setArgument(Attributes.FRAME_NUMBER, new Integer(index));
		request.setArgument(Attributes.NUMBER, new Integer(0));
		CFResponsePacket response = crossfire().sendRequest(request);
		if(response.isSuccess()) {
			Scope s = new Scope(
					(Number)response.getBody().get(Attributes.INDEX), 
					(Number)response.getBody().get(Attributes.FRAME_INDEX),
					(Number) ((Map)response.getBody().get(Attributes.OBJECT)).get(Attributes.HANDLE));
			scopes.add(s);
		}
		else if(TRACE) {
			Tracing.writeString("VM [failed scopes request]: "+JSON.serialize(request)); //$NON-NLS-1$
		}
	}
}
