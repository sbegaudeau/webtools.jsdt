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

import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;

/**
 * Default implementation of {@link Location} for Crossfire
 * 
 * @since 1.0
 */
public class CFLocation extends CFMirror implements Location {

	private CFScriptReference script = null;
	private String function = null;
	private int line = -1;
	
	/**
	 * Constructor
	 * @param vm
	 * @param script
	 * @param function
	 * @param line
	 */
	public CFLocation(VirtualMachine vm, CFScriptReference script, String function, int line) {
		super(vm);
		this.script = script;
		this.function = function;
		this.line = line;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Location#scriptReference()
	 */
	public ScriptReference scriptReference() {
		return script;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Location#lineNumber()
	 */
	public int lineNumber() {
		return line;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Location#functionName()
	 */
	public String functionName() {
		return function;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("LocationImpl: "); //$NON-NLS-1$
		buffer.append("[script - ").append(script.sourceURI()).append("] "); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append("[function - ").append(function).append("] "); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append("[line - ").append(line).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
		return buffer.toString();
	}
}
