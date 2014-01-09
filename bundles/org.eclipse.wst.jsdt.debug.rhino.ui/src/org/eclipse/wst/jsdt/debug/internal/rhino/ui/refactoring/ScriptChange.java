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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.ILaunchConstants;

/**
 * Change for a script being renamed / moved
 * 
 * @since 1.0
 */
public class ScriptChange extends RhinoChange {
	
	/**
	 * Constructor
	 */
	public ScriptChange(ILaunchConfiguration configuration, String oldname, String newname) {
		super(configuration, oldname, newname);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#perform(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public Change perform(IProgressMonitor pm) throws CoreException {
		ILaunchConfigurationWorkingCopy copy = configuration.getWorkingCopy();
		//update the script only if matches
		//this check is in case the configuration has include path-only updates
		String oldscript = copy.getAttribute(ILaunchConstants.ATTR_SCRIPT, (String)null);
		if(oldname.equals(oldscript)) {
			copy.setAttribute(ILaunchConstants.ATTR_SCRIPT, newname);
		}
		//update include entries
		updateIncludeEntries(copy);
		//rename it
		String value = computeNewConfigurationName(configuration);
		if(value != null) {
			copy.rename(value);
		}
		//update resource mappings
		Refactoring.mapResources(copy);
		if(copy.isDirty()) {
			configuration = copy.doSave();
			return new ScriptChange(configuration, newname, oldname);
		}
		return null;
	}
}
