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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
	}
	
	HashMap scopes = new HashMap();
	
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
		Number value = getNumber(json, Attributes.INDEX);
		if(value != null) {
			index = value.intValue();
		}
		value = getNumber(json, Attributes.LINE);
		if(value != null) {
			linenumber = value.intValue();
		}
		scriptid = (String) json.get(Attributes.SCRIPT);
		funcname = (String) json.get(Attributes.FUNC);
		
		parseScopes((List) json.get(Attributes.SCOPES));
		parseLocals((Map) json.get(Attributes.LOCALS));
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
				Scope s = new Scope(getNumber(map, Attributes.INDEX), getNumber(map, Attributes.FRAME_INDEX), getNumber((Map)map.get(Attributes.OBJECT),Attributes.HANDLE));
				scopes.put(new Integer(index), s);
			}
		}
	}
	
	/**
	 * Tries to look up a {@link Number} value from the given map with the given name. This method
	 * will try to convert a string value to a number if found mapped to the given attribute name.
	 * <br><br>
	 * If a {@link Number} cannot be computed <code>null</code> is returned
	 * @param map
	 * @param attrib
	 * @return the {@link Number} mapped to the given attribute or <code>null</code>
	 */
	Number getNumber(Map map, String attrib) {
		Object o = map.get(attrib);
		if(o instanceof Number) {
			return (Number) o;
		}
		else if(o instanceof String) {
			try {
				return new BigDecimal((String)o);
			}
			catch(NumberFormatException nfe) {}
		}
		return null;
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
			if(thismap != null) {
				thisvar = new CFVariable(crossfire(), this, Attributes.THIS, null, thismap);
			}
		}
	}
	
	void parseVariables(Map map, List varcollector) {
		Entry entry = null;
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			entry = (Entry) iter.next();
			if(entry.getValue() instanceof Map) {
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
				varcollector.add(new CFVariable(crossfire(), this, name, ref, info));
			}
			else {
				varcollector.add(new CFVariable(crossfire(), this, (String) entry.getKey(), null, null));
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
		scope();
		CFRequestPacket request = new CFRequestPacket(Commands.EVALUATE, thread.id());
		request.setArgument(Attributes.FRAME, new Integer(index));
		request.setArgument(Attributes.EXPRESSION, expression);
		CFResponsePacket response = crossfire().sendRequest(request);
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
			CFRequestPacket request = new CFRequestPacket(Commands.LOOKUP, thread.id());
			request.setArgument(Attributes.HANDLE, ref);
			CFResponsePacket response = crossfire().sendRequest(request);
			if(response.isSuccess()) {
				Map value = (Map) response.getBody().get(Attributes.VALUE);
				if(value != null) {
					Number handle = (Number) value.get(Attributes.HANDLE);
					if(handle == null) {
						value.put(Attributes.HANDLE, ref);
					}
					return createValue(value);
				}
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
			Object o = values.get(Attributes.RESULT);
			if(o == null) {
				String type = (String) values.get(Attributes.TYPE);
				if(type != null) {
					return createTypeValue(type, values);
				}
			}
			if(o instanceof Map){
				return new CFObjectReference(crossfire(), this, (Map) o);
			}
			if(o instanceof String) {
				return crossfire().mirrorOf(o.toString());
			}
			else if(o instanceof Number) {
				return crossfire().mirrorOf((Number)o);
			}
		}
		else if(val instanceof Map) {
			return new CFObjectReference(crossfire(), this, (Map) val);
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
		if(CFNullValue.NULL.equals(type) || type == null) {
			return crossfire().mirrorOfNull();
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
		if(!response.isSuccess() && TRACE) {
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
			Map scope = (Map) response.getBody().get(Attributes.OBJECT);
			if(scope != null) {
				String ref = (String) scope.get(Attributes.HANDLE);
				if(ref != null) {
					//TODO what to do with this:
				}
			}
		}
		else if(TRACE) {
			Tracing.writeString("VM [failed scopes request]: "+JSON.serialize(request)); //$NON-NLS-1$
		}
	}
}
