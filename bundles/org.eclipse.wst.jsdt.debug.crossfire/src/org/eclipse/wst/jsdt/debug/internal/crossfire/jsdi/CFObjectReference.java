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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.wst.jsdt.debug.core.jsdi.ObjectReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame;
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
	private Number constref = null;
	private Number protoref = null;
	private Value constructor = null;
	private Value prototype = null;
	private List properties = null;
	private String source = null;

	/**
	 * The "this" attribute
	 */
	public static final String THIS = "this"; //$NON-NLS-1$
	
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
		source = (String) body.get(Attributes.SOURCE);
		//init properties - we are dealing with evaluation results
		Map props = (Map) body.get(Attributes.RESULT);
		if(props == null) {
			Object o = body.get(Attributes.VALUE);
			if(o instanceof Map) {
				props = (Map) body.get(Attributes.VALUE);
			}
			else if(this.handle == null) {
				props = body;
			}
		}
		if(props != null) {
			if(properties == null) {
				properties = new ArrayList(props.size());
			}
			Entry entry = null;
			String name = null;
			Map json = null;
			Number ref = null;
			for(Iterator i = props.entrySet().iterator(); i.hasNext();) {
				entry = (Entry) i.next();
				name = (String)entry.getKey();
				if(entry.getValue() instanceof Map) {
					json = (Map) entry.getValue();
					Object o = json.get(Attributes.HANDLE);
					//prevent http://code.google.com/p/fbug/issues/detail?id=4635
					if(o instanceof Number) {
						ref = (Number) o;
						//don't add constructor and proto to the properties heap
						//they are requested specially
						if(Attributes.CONSTRUCTOR.equals(name)) {
							constref = ref;
							continue;
						}
						else if(Attributes.PROTO.equals(name)) {
							protoref = ref;
							continue;
						}
					}
				}
				properties.add(new CFVariable(crossfire(), frame, name, ref, json));
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Value#valueString()
	 */
	public String valueString() {
		if(source != null) {
			return source;
		}
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
				if(constref == null) {
					constructor = crossfire().mirrorOfUndefined();
				}
				constructor = frame.lookup(constref);
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
				if(protoref == null) {
					prototype = crossfire().mirrorOfUndefined();
				}
				prototype = frame.lookup(protoref);
			}
		}
		return prototype;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ObjectReference#properties()
	 */
	public List properties() {
		return properties;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ObjectReference#handle()
	 */
	public Number id() {
		return handle;
	}
	
	/**
	 * @return the backing {@link StackFrame}
	 */
	protected CFStackFrame frame() {
		return this.frame;
	}
	
	/**
	 * @return the source for the body of the object, or <code>null</code>
	 */
	protected String source() {
		return this.source;
	}
}
