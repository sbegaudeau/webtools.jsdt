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

import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.FunctionReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.Value;

/**
 * Default implementation of {@link FunctionReference} for Crossfire
 * 
 * @since 1.0
 */
public class CFFunctionReference extends CFObjectReference implements FunctionReference {

	/**
	 * The "function" type
	 */
	public static final String FUNCTION = "function"; //$NON-NLS-1$
	
	private String funcname = null;
	
	
	/**
	 * Constructor
	 * @param vm
	 * @param frame
	 * @param body
	 */
	public CFFunctionReference(CFVirtualMachine vm, CFStackFrame frame, Map body) {
		super(vm, frame, body);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.FunctionReference#functionName()
	 */
	public String functionName() {
		synchronized (frame()) {
			if(funcname == null) {
				Value val = frame().lookup(id());
				System.out.println(val);
			}
		}
		return funcname;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.FunctionReference#functionBody()
	 */
	public String functionBody() {
		return source();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi.CFObjectReference#valueString()
	 */
	public String valueString() {
		if(source() != null) {
			return source();
		}
		return FUNCTION;
	}
}
