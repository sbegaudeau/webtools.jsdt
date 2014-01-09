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
package org.eclipse.wst.jsdt.debug.internal.core.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference;
import org.eclipse.wst.jsdt.debug.core.model.IScriptResolver;
import org.eclipse.wst.jsdt.debug.internal.core.Constants;

/**
 * Default implementation of the delegate class for all {@link IScriptResolver}s
 * 
 * @since 3.4
 */
public class ScriptResolverExtension implements IScriptResolver {

	private IConfigurationElement element = null;
	private IScriptResolver delegate = null;
	
	/**
	 * Constructor
	 * @param element the backing {@link IConfigurationElement} to lazily load and delegate to
	 */
	public ScriptResolverExtension(IConfigurationElement element) {
		this.element = element;
	}
	
	/**
	 * Returns the delegate {@link IScriptResolver} from the backing {@link IConfigurationElement}
	 * 
	 * @return the delegate {@link IScriptResolver}
	 * @throws CoreException thrown if the delegate class fails to be created
	 */
	synchronized IScriptResolver getDelegate() throws CoreException {
		if(delegate == null) {
			delegate = (IScriptResolver) element.createExecutableExtension(Constants.CLASS);
		}
		return delegate;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.model.IScriptPathResolver#matches(org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference, org.eclipse.core.runtime.IPath)
	 */
	public boolean matches(ScriptReference script, IPath path) {
		try {
			return getDelegate().matches(script, path);
		}
		catch(CoreException ce) {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.model.IScriptPathResolver#getFile(org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference)
	 */
	public IFile getFile(ScriptReference script) {
		try {
			return getDelegate().getFile(script);
		}
		catch(CoreException ce) {
			return null;
		}
	}
}