/*******************************************************************************
 * Copyright (c) 2010, 2012 IBM Corporation and others.
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
import java.util.Arrays;
import java.util.Collections;
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

	private int index = -1;
	private String scriptUrl = null;
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
		scriptUrl = (String) json.get(Attributes.URL);
		funcname = (String) json.get(Attributes.FUNCTION_NAME);
		
		parseLocals((Map) json.get(Attributes.LOCALS));
		parseScopes((List) json.get(Attributes.SCOPES));
	}

	/**
	 * Parses the scopes object node, if there is one
	 * 
	 * @param list the list of scopes
	 */
	void parseScopes(List list) {
		if(list != null) {
			if(vars == null) {
				vars = new ArrayList(list.size());
			}
			for (Iterator i = list.iterator(); i.hasNext();) {
				Map map = (Map) i.next();
				String name = (String) map.get(Attributes.NAME);
				if(name == null) {
					name = Messages.CFStackFrame_0;
				}
				Map scope = (Map)map.get(Attributes.SCOPE);
				if(scope != null) {
					vars.add(0, new CFVariable(crossfire(), this, name, (Number) scope.get(Attributes.HANDLE), scope));
				}
			}
		}
	}
	
	/**
	 * Read the local variable information from the JSON mapping
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
			Map thismap = (Map) json.get(CFObjectReference.THIS);
			thisvar = new CFVariable(crossfire(), this, CFObjectReference.THIS, null, thismap);
		}
	}
	
	void parseVariables(Map map, List varcollector) {
		Entry entry = null;
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			entry = (Entry) iter.next();
			if(entry.getValue() instanceof Map) {
				Map info  = (Map) entry.getValue();
				Object obj = info.get(Attributes.HANDLE);
				Number ref = null;
				if(obj instanceof Number) {
					ref = (Number) obj;
				}
				//not an initialized object, try to see if the map has a type (or not null)
				varcollector.add(new CFVariable(crossfire(), this, (String) entry.getKey(), ref, info));
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
			CFScriptReference script = crossfire().findScript(scriptUrl); 
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
			request.setArgument(Attributes.HANDLES, Arrays.asList(new Number[] {ref}));
			request.setArgument(Attributes.INCLUDE_SOURCE, Boolean.TRUE);
			CFResponsePacket response = crossfire().sendRequest(request);
			if(response.isSuccess()) {
				List list = (List)response.getBody().get(Attributes.VALUES);
				if (list != null && list.size() > 0) {
					return createValue(list.get(0));
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
		if(CFNumberValue.NUMBER.equals(type)) {
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
		request.setArgument(Attributes.FRAME_INDEX, new Integer(index));
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
	void scope(int frameindex, int scopeindex) {
		CFRequestPacket request = new CFRequestPacket(Commands.SCOPES, thread.id());
		request.setArgument(Attributes.FRAME_INDEX, new Integer(frameindex));
		request.setArgument(Attributes.SCOPE_INDEXES, Arrays.asList(new Number[] {new Integer(scopeindex)}));
		CFResponsePacket response = crossfire().sendRequest(request);
		if(response.isSuccess()) {
			if(vars == null) {
				vars = new ArrayList();
			}
			List list = (List)response.getBody().get(Attributes.SCOPES);
			if (list != null && list.size() > 0) {
				Map scope = ((Map)list.get(0));
				vars.add(0, new CFVariable(crossfire(), this, "Enclosing Scope", (Number) scope.get(Attributes.HANDLE), scope)); //$NON-NLS-1$
			}
		}
		else if(TRACE) {
			Tracing.writeString("VM [failed scopes request]: "+JSON.serialize(request)); //$NON-NLS-1$
		}
	}
}
