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

import org.eclipse.wst.jsdt.debug.core.jsdi.ArrayReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.Value;

/**
 * Default implementation of {@link ArrayReference} for Crossfire
 * 
 * @since 1.0
 */
public class CFArrayReference extends CFObjectReference implements ArrayReference {

	/**
	 * The "array" value type
	 */
	public static final String ARRAY = "array"; //$NON-NLS-1$
	
	/**
	 * Constructor
	 * @param vm
	 * @param frame
	 * @param body
	 */
	public CFArrayReference(CFVirtualMachine vm, CFStackFrame frame, Map body) {
		super(vm, frame, body);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ArrayReference#length()
	 */
	public int length() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ArrayReference#getValue(int)
	 */
	public Value getValue(int index) throws IndexOutOfBoundsException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ArrayReference#getValues()
	 */
	public List getValues() {
		return null;
	}
}
