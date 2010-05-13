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

import java.util.List;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame;
import org.eclipse.wst.jsdt.debug.core.jsdi.Value;
import org.eclipse.wst.jsdt.debug.core.jsdi.Variable;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;

/**
 * Default implementation of {@link StackFrame} for Crossfire
 * 
 * @since 1.0
 */
public class CFStackFrame extends CFMirror implements StackFrame {

	/**
	 * Constructor
	 * @param vm
	 * @param json
	 */
	public CFStackFrame(VirtualMachine vm, Map json) {
		super(vm);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame#thisObject()
	 */
	public Variable thisObject() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame#variables()
	 */
	public List variables() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame#location()
	 */
	public Location location() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame#evaluate(java.lang.String)
	 */
	public Value evaluate(String expression) {
		return null;
	}
}
