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
package org.eclipse.wst.jsdt.debug.internal.rhino.ui.refactoring;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.ILaunchConstants;

/**
 * Change for a folder being renamed
 * 
 * @since 1.0
 */
public class FolderChange extends RhinoChange {

	/**
	 * Constructor
	 * 
	 * @param configuration
	 * @param oldname the old folder name
	 * @param newname the new folder name
	 */
	public FolderChange(ILaunchConfiguration configuration, String oldname, String newname) {
		super(configuration, oldname, newname);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#perform(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public Change perform(IProgressMonitor pm) throws CoreException {
		ILaunchConfigurationWorkingCopy copy = configuration.getWorkingCopy();
		//move it first
		if(!configuration.isLocal()) {
	        IContainer cont = (IContainer) ResourcesPlugin.getWorkspace().getRoot().findMember(newname);
	        copy.setContainer(cont);
		}
		//update the script attribute
		String script = computeNewScriptName(configuration);
		if(script != null) {
			copy.setAttribute(ILaunchConstants.ATTR_SCRIPT, script);
		}
		//update the include path
		updateIncludeEntries(copy);
		//update resource mappings
		Refactoring.mapResources(copy);
		if(copy.isDirty()) {
			configuration = copy.doSave();
			return new FolderChange(configuration, newname, oldname);
		}
		return null;
	}
}
