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
package org.eclipse.wst.jsdt.debug.internal.chrome.jsdi;

import org.eclipse.wst.jsdt.debug.core.jsdi.StringValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;

/**
 *
 */
public class StringImpl extends MirrorImpl implements StringValue {

	private String string = null;
	
	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param string
	 */
	StringImpl(VirtualMachine vm, String string) {
		super(vm);
		this.string = string;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Value#valueString()
	 */
	public String valueString() {
		return string;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.StringValue#value()
	 */
	public String value() {
		return string;
	}

}
