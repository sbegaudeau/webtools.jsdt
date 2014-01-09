/*******************************************************************************
 * Copyright (c) 2011, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.core.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference;

/**
 * This is the default base class for all {@link IScriptResolver}s
 * <br><br>
 * Clients must extend this class and not directly implement the interface {@link IScriptResolver}
 * 
 * @since 2.1
 */
public abstract class ScriptResolver implements IScriptResolver {

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.model.IScriptResolver#matches(org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference, org.eclipse.core.runtime.IPath)
	 */
	public boolean matches(ScriptReference script, IPath path) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.model.IScriptResolver#getFile(org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference)
	 */
	public IFile getFile(ScriptReference script) {
		return null;
	}
}
