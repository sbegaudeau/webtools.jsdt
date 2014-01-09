/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.opera.internal.jsdi;

import org.eclipse.wst.jsdt.debug.core.jsdi.NullValue;

/**
 * Default {@link NullValue} Opera implementation
 * 
 * @since 0.1
 */
public class NullValueImpl extends MirrorImpl implements NullValue {

	public static String VALUE = "null"; //$NON-NLS-1$
	
	public NullValueImpl(VirtualMachineImpl vm) {
		super(vm);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Value#valueString()
	 */
	public String valueString() {
		return VALUE;
	}
}
