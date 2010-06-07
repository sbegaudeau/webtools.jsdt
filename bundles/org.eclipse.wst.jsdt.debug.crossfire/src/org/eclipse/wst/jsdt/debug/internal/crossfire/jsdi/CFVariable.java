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

import org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame;
import org.eclipse.wst.jsdt.debug.core.jsdi.Variable;

/**
 * Default implementation of a {@link Variable} for Crossfire
 * 
 * @since 1.0
 */
public class CFVariable extends CFProperty implements Variable {

	private boolean isarg = false;
	
	/**
	 * Constructor
	 * @param vm
	 * @param frame
	 * @param name
	 * @param ref
	 * @param isarg
	 */
	public CFVariable(CFVirtualMachine vm, CFStackFrame frame, String name, Number ref, boolean isarg) {
		super(vm, frame, name, ref);
		this.isarg = isarg;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Variable#isArgument()
	 */
	public boolean isArgument() {
		return this.isarg;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Variable#isVisible(org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame)
	 */
	public boolean isVisible(StackFrame frame) {
		if(frame instanceof CFStackFrame) {
			return ((CFStackFrame)frame).isVisible(this);
		}
		return false;
	}

}
