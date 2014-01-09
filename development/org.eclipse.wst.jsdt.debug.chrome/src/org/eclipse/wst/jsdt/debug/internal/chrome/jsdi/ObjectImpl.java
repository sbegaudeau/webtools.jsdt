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

import org.eclipse.wst.jsdt.debug.core.jsdi.ObjectReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.Value;

/**
 *
 */
public class ObjectImpl extends MirrorImpl implements ObjectReference {

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Value#valueString()
	 */
	public String valueString() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ObjectReference#className()
	 */
	public String className() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ObjectReference#constructor()
	 */
	public Value constructor() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ObjectReference#prototype()
	 */
	public Value prototype() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ObjectReference#properties()
	 */
	public List properties() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ObjectReference#id()
	 */
	public Number id() {
		return null;
	}

}
