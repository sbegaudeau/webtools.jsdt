/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.ILaunchConstants;

public class ProjectChange extends RhinoChange {
	
	/**
	 * Constructor
	 * 
	 * @param configuration
	 * @param oldname
	 * @param newname
	 */
	public ProjectChange(ILaunchConfiguration configuration, String oldname, String newname) {
		super(configuration, oldname, newname);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#perform(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public Change perform(IProgressMonitor pm) throws CoreException {
		ILaunchConfigurationWorkingCopy copy = configuration.getWorkingCopy();
		//move it first
		String value = computeNewContainerName(configuration);
		if(value != null) {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
	        IWorkspaceRoot root = workspace.getRoot();
	        IProject project = root.getProject(newname);
	        IContainer cont = (IContainer) project.findMember(value);
	        copy.setContainer(cont);
		}
		//update the attributes
		value = computeNewScriptName(configuration);
		if(value != null) {
			copy.setAttribute(ILaunchConstants.ATTR_SCRIPT, value);
		}
		//update include entries
		updateIncludeEntries(copy);
		//rename it
		value = computeNewConfigurationName(configuration);
		if(value != null) {
			copy.rename(value);
		}
		//update resource mappings
		Refactoring.mapResources(copy);
		if(copy.isDirty()) {
			configuration = copy.doSave();
			return new ProjectChange(configuration, newname, oldname);
		}
		return null;
	}
}