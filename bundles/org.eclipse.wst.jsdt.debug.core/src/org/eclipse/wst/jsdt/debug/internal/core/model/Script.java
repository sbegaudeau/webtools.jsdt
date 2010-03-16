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
package org.eclipse.wst.jsdt.debug.internal.core.model;

import java.net.URI;

import org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference;
import org.eclipse.wst.jsdt.debug.core.model.IScript;

/**
 * Wrapper for a {@link ScriptReference}
 * 
 * @since 1.0
 */
public class Script extends JavaScriptDebugElement implements IScript {

	private ScriptReference script = null;
	
	/**
	 * Constructor
	 */
	public Script(JavaScriptDebugTarget target, ScriptReference script) {
		super(target);
		this.script  = script;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.model.IScript#source()
	 */
	public String source() {
		return this.script.source();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.model.IScript#sourceURI()
	 */
	public URI sourceURI() {
		return this.script.sourceURI();
	}
}
