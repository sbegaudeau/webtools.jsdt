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
package org.eclipse.wst.jsdt.debug.internal.rhino.jsdi;

import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference;

/**
 * Rhino implementation of {@link Location}
 * 
 * @since 1.0
 */
public class LocationImpl extends MirrorImpl implements Location {
	private String functionName;
	private int lineNumber;
	private ScriptReferenceImpl scriptReference;

	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param functionName
	 * @param lineNumber
	 * @param scriptReference
	 */
	public LocationImpl(VirtualMachineImpl vm, String functionName, int lineNumber, ScriptReferenceImpl scriptReference) {
		super(vm);
		this.functionName = functionName;
		this.lineNumber = lineNumber;
		this.scriptReference = scriptReference;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Location#functionName()
	 */
	public String functionName() {
		return functionName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Location#lineNumber()
	 */
	public int lineNumber() {
		return lineNumber;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Location#scriptReference()
	 */
	public ScriptReference scriptReference() {
		return scriptReference;
	}
}
