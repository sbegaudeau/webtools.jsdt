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

import org.eclipse.wst.jsdt.debug.core.jsdi.PrimitiveValue;

/**
 * Default {@link PrimitiveValue} Opera implementation
 * 
 * @since 0.1
 */
public abstract class PrimitiveValueImpl extends MirrorImpl implements PrimitiveValue {

	public PrimitiveValueImpl(VirtualMachineImpl vm) {
		super(vm);
	}
}
