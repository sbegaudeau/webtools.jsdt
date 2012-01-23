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
package org.eclipse.wst.jsdt.debug.core.model;

import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference;

/**
 * This resolver allows contributors to plug-in to the process of resolving a {@link ScriptReference}'s 
 * {@link URI} to a workspace-local {@link IFile}.
 * <br><br>
 * This interface is not intended to be directly implemented. Instead clients must extend the base class {@link ScriptResolver}
 * 
 * @see ScriptResolver
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 * @since 2.1
 */
public interface IScriptResolver {

	/**
	 * This method determines if the given {@link IPath} matches the {@link URI} from {@link ScriptReference#sourceURI()}.

	 * @param script the {@link ScriptReference} to compare the source {@link URI} from - never <code>null</code>
	 * @param path the path to match against the source {@link URI} - never <code>null</code>
	 * 
	 * @return <code>true</code> if the source {@link URI} of the given {@link ScriptReference} is considered to match the given {@link IPath}, <code>false</code> otherwise.
	 */
	public boolean matches(ScriptReference script, IPath path);
	
	/**
	 * This method is used to find the workspace {@link IFile} that corresponds to the source {@link URI} from the given {@link ScriptReference}.
	 * <br><br>
	 * If no file can be determined the method must return <code>null</code>. The result will be checked for existence.
	 * 
	 * @param script the {@link ScriptReference} to find the {@link IFile} for
	 * @return the {@link IFile} for the {@link ScriptReference} or <code>null</code>
	 */
	public IFile getFile(ScriptReference script);
}
