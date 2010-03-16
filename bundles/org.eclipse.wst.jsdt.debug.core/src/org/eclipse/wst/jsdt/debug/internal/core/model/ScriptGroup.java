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

import java.util.List;

import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptDebugTarget;
import org.eclipse.wst.jsdt.debug.core.model.IScriptGroup;


/**
 * Container for all scripts from a target
 * 
 * @since 1.0
 */
public class ScriptGroup extends JavaScriptDebugElement implements IScriptGroup {

	/**
	 * Constructor
	 */
	public ScriptGroup(IJavaScriptDebugTarget target) {
		super(target);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.model.IScriptGroup#allScripts()
	 */
	public List allScripts() {
		return getJavaScriptDebugTarget().allScripts();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(obj instanceof ScriptGroup) {
			return ((ScriptGroup)obj).getJavaScriptDebugTarget().equals(getJavaScriptDebugTarget());
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getJavaScriptDebugTarget().hashCode();
	}
}
