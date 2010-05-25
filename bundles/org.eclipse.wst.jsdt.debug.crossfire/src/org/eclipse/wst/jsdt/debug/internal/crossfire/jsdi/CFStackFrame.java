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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame;
import org.eclipse.wst.jsdt.debug.core.jsdi.Value;
import org.eclipse.wst.jsdt.debug.core.jsdi.Variable;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Attributes;

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
		vars = parseLocals((Map) json.get(Attributes.LOCALS));
	}

	List parseLocals(Map json) {
		if(json != null) {
			return Collections.EMPTY_LIST;
		}
		return null;
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
			loc = new CFLocation(crossfire(), crossfire().findScript(scriptid), funcname, linenumber);
		}
		return loc;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame#evaluate(java.lang.String)
	 */
	public Value evaluate(String expression) {
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
}
