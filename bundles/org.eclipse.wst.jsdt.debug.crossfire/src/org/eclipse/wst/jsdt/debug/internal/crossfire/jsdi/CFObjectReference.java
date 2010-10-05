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

import org.eclipse.wst.jsdt.debug.core.jsdi.ObjectReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.Value;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Attributes;

/**
 * Default implementation of {@link ObjectReference} for Crossfire
 * 
 * @since 1.0
 */
public class CFObjectReference extends CFMirror implements ObjectReference {
	
	/**
	 * The "type" of this mirror element
	 */
	public static final String OBJECT = "object"; //$NON-NLS-1$
	
	private CFStackFrame frame = null;
	private String classname = null;
	private Number handle = null;
	private Value constructor = null;
	private Value prototype = null;
	private List properties = null;
	
	/**
	 * Constructor
	 * @param vm
	 * @param frame
	 * @param body
	 */
	public CFObjectReference(CFVirtualMachine vm, CFStackFrame frame, Map body) {
		super(vm);
		this.frame = frame;
		handle = (Number) body.get(Attributes.HANDLE);
		if(handle == null) {
			handle = new Integer(-1);
	 		//init properties - we are dealing with evaluation results
			Map props = (Map) body.get(Attributes.RESULT);
			if(props == null) {
				Object o = body.get(Attributes.VALUE);
				if(o instanceof Map) {
					props = (Map) body.get(Attributes.VALUE);
				}
				else {
					props = body;
				}
			}
			if(props != null) {
				if(props.containsKey(Attributes.VALUE)) {
					/*properties = new ArrayList(props.size());
					properties.add(frame.createValue(props));*/
				}
				else {
					if(properties == null) {
						properties = new ArrayList(props.size());
					}
					Entry entry = null;
					for(Iterator i = props.entrySet().iterator(); i.hasNext();) {
						entry = (Entry) i.next();
						properties.add(new CFProperty(crossfire(), frame, (String)entry.getKey(), frame.createValue(entry.getValue())));
					}
				}
			}
			
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Value#valueString()
	 */
	public String valueString() {
		return OBJECT;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ObjectReference#className()
	 */
	public String className() {
		return classname;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ObjectReference#constructor()
	 */
	public Value constructor() {
		synchronized (frame) {
			if(constructor == null) {
				return crossfire().mirrorOfUndefined();
			}
		}
		return constructor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ObjectReference#prototype()
	 */
	public Value prototype() {
		synchronized (frame) {
			if(prototype == null) {
				return crossfire().mirrorOfUndefined();
			}
		}
		return prototype;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ObjectReference#properties()
	 */
	public List properties() {
		synchronized (frame) {
			if(properties == null) {
				return Collections.EMPTY_LIST;
			}
		}
		return properties;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ObjectReference#id()
	 */
	public Number id() {
		return handle;
	}
}
