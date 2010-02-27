/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.rhino.jsdi;

import org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame;
import org.eclipse.wst.jsdt.debug.core.jsdi.Variable;

public class VariableImpl extends PropertyImpl implements Variable {

	public VariableImpl(VirtualMachineImpl vm, StackFrameReferenceImpl frame, String name, Number ref) {
		super(vm, frame, name, ref);
	}

	public boolean isArgument() {
		return false;
	}

	public boolean isVisible(StackFrame frame) {
		StackFrameReferenceImpl frameImpl = (StackFrameReferenceImpl) frame;
		return frameImpl.isVisible(this);
	}
}
