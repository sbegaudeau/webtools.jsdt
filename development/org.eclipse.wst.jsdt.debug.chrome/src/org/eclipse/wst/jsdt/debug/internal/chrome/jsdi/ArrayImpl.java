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

import java.util.List;

import org.eclipse.wst.jsdt.debug.core.jsdi.ArrayReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.Value;

/**
 *
 */
public class ArrayImpl extends ObjectImpl implements ArrayReference {

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
