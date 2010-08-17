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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.ILaunchConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.RhinoUIPlugin;

/**
 * Utilities to help with refactoring
 * 
 * @since 1.0
 */
public class Refactoring {

	public static ILaunchConfiguration[] NO_CONFIGS = new ILaunchConfiguration[0];
	
	/**
	 * Creates one or more {@link Change}s required after an {@link IProject} rename
	 * 
	 * @param project the renamed {@link IProject}
	 * @param newname the new name
	 * @return a new {@link Change}
	 */
	public static Change createChangesForProjectRename(IProject project, String newname) {
		ILaunchConfiguration[] configs = getConfigurationsForProject(project.getName());
		if(configs.length < 1) {
			return null;
		}
		ArrayList changes = new ArrayList(configs.length);
		for (int i = 0; i < configs.length; i++) {
			changes.add(new ProjectChange(configs[i], project.getName(), newname));
		}
		return createChangeFromList(changes, Messages.multi_updates);
	}
	
	/**
	 * Create one or more {@link Change}s after a script {@link IFile} rename
	 * 
	 * @param script the renamed {@link IFile}
	 * @param newname the new name
	 * @return a new {@link Change}
	 */
	public static Change createChangesForScriptRename(IFile script, String newname) {
		String scriptname = script.getFullPath().makeAbsolute().toOSString();
		ILaunchConfiguration[] configs = getConfigurationsForScript(scriptname);
		if(configs.length < 1) {
			return null;
		}
		ArrayList changes = new ArrayList(configs.length);
		for (int i = 0; i < configs.length; i++) {
			changes.add(new ScriptChange(configs[i], scriptname, newname));
		}
		return createChangeFromList(changes, Messages.multi_updates);
	}
	
	/**
	 * Create one or more {@link Change}s after an {@link IFolder} rename
	 * 
	 * @param folder the renamed {@link IFolder}
	 * @param newname the new name
	 * @return a new {@link Change}
	 */
	public static Change createChangesForFolderRename(IFolder folder, String newname) {
		ILaunchConfiguration[] configs = getRhinoConfigurations();
		if(configs.length < 1) {
			return null;
		}
		ArrayList changes = new ArrayList(configs.length);
		String script = null;
		String foldername = folder.getFullPath().makeAbsolute().toOSString();
		for (int i = 0; i < configs.length; i++) {
			try {
				script = configs[i].getAttribute(ILaunchConstants.ATTR_SCRIPT, ILaunchConstants.EMPTY_STRING);
				if(!ILaunchConstants.EMPTY_STRING.equals(script)) {
					if(script.startsWith(foldername)) {
						changes.add(new FolderChange(configs[i], foldername, newname));
					}
				}
			}
			catch(CoreException ce) {}
		}
		return createChangeFromList(changes, Messages.multi_updates);
	}
	
	/**
	 * Collects the {@link IResource}s associated with the given Rhino launch configuration
	 * 
	 * @param configuration
	 * @return the array of associated {@link IResource}s or an empty array, never <code>null</code>
	 * @throws CoreException
	 */
	public static IResource[] getResources(ILaunchConfiguration configuration) {
		ArrayList resources = new ArrayList(2);
		try {
			IProject project = null;
			String name = configuration.getAttribute(ILaunchConstants.ATTR_PROJECT, ILaunchConstants.EMPTY_STRING);
			if(!ILaunchConstants.EMPTY_STRING.equals(name)) {
				project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
				resources.add(project);
			}
			IFile script = getScript(configuration, project);
			if(script != null) {
				resources.add(script);
			}
		}
		catch(CoreException ce) {
			RhinoUIPlugin.log(ce);
		}
		return (IResource[]) resources.toArray(new IResource[resources.size()]);
	}
	
	/**
	 * Fetches the project attribute as an {@link IProject}. This method does not check the existence
	 * or accessibility of the {@link IProject}.
	 * 
	 * @param configuration
	 * @return the {@link IProject} resource handle for the project attribute
	 * @throws CoreException
	 */
	public static IProject getProject(ILaunchConfiguration configuration) throws CoreException {
		String name = configuration.getAttribute(ILaunchConstants.ATTR_PROJECT, ILaunchConstants.EMPTY_STRING);
		if(!ILaunchConstants.EMPTY_STRING.equals(name)) {
			return ResourcesPlugin.getWorkspace().getRoot().getProject(name);
		}
		return null;
	}
	
	/**
	 * Fetches the script attribute as an {@link IFile} or returns <code>null</code> if it could not be found
	 * 
	 * @param configuration
	 * @param project the project context to search in or <code>null</code>
	 * @return the {@link IFile} backing the script attribute or <code>null</code> if not found
	 * @throws CoreException
	 */
	public static IFile getScript(ILaunchConfiguration configuration, IProject project) throws CoreException {
		String name = configuration.getAttribute(ILaunchConstants.ATTR_SCRIPT, ILaunchConstants.EMPTY_STRING);
		if(!ILaunchConstants.EMPTY_STRING.equals(name)) {
			IResource script = null;
			IPath spath = new Path(name); 
			if(project != null) {
				script = project.findMember(spath.removeFirstSegments(1));
			}
			else {
				script = ResourcesPlugin.getWorkspace().getRoot().findMember(spath);
			}
			if(script != null && script.getType() == IResource.FILE) {
				return (IFile) script;
			}
		}
		return null;
	}
	
	/**
	 * Delegate method to map {@link IResource}s in the given working copy. This method does not save the 
	 * working and if no resources can be computed any existing resource mapping will be removed
	 * 
	 * @param configuration
	 * @throws CoreException
	 */
	public static void mapResources(ILaunchConfigurationWorkingCopy configuration) {
		IResource[] resources = getResources(configuration);
		if(resources.length < 1) {
			configuration.setMappedResources(null);
		}
		else {
			configuration.setMappedResources(resources);
		}
	}
	
	/**
	 * Take a list of Changes, and return a unique Change, a CompositeChange, or null.
	 */
	public static Change createChangeFromList(List changes, String changeLabel) {
		int count = changes.size();
		if (count == 0) {
			return null;
		} 
		else if (count == 1) {
			return (Change) changes.get(0);
		} 
		else {
			return new CompositeChange(changeLabel, (Change[])changes.toArray(new Change[changes.size()]));
		}
	}
	
	/**
	 * Returns the complete known listing of Rhino launch configurations
	 * 
	 * @return the complete listing of Rhino launch configurations or an empty array, never <code>null</code>
	 */
	public static ILaunchConfiguration[] getRhinoConfigurations() {
		ILaunchManager lm = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = lm.getLaunchConfigurationType(ILaunchConstants.LAUNCH_CONFIG_TYPE);
		if(type != null) {
			try {
				return lm.getLaunchConfigurations(type);
			} catch (CoreException e) {
				//ignore just return an empty array
			}
		}
		return NO_CONFIGS;
	}
	
	/**
	 * Returns all of the Rhino launch configurations that have the project attribute set to the given project name.
	 * 
	 * @param name
	 * @return all of the Rhino launch configurations that reference the given project name or an empty array, never <code>null</code>
	 */
	public static ILaunchConfiguration[] getConfigurationsForProject(String name) {
		return getConfigurationsFor(ILaunchConstants.ATTR_PROJECT, name);
	}
	
	/**
	 * Returns all of the Rhino launch configurations that have the script attribute set to the given name
	 * 
	 * @param name
	 * @return all of the Rhino launch configurations that reference the given script name or an empty array, never <code>null</code>
	 */
	public static ILaunchConfiguration[] getConfigurationsForScript(String name) {
		return getConfigurationsFor(ILaunchConstants.ATTR_SCRIPT, name);
	}
	
	/**
	 * Delegate method to get all configurations where the given name matches the stored value of the given attribute name
	 * 
	 * @param attribute
	 * @param name
	 * @return the list of matching configurations or an empty array, never <code>null</code>
	 */
	static ILaunchConfiguration[] getConfigurationsFor(String attribute, String name) {
		if(name == null) {
			return NO_CONFIGS;
		}
		ILaunchConfiguration[] configs = getRhinoConfigurations();
		if(configs.length < 1) {
			return NO_CONFIGS;
		}
		HashSet cfgs = new HashSet();
		for (int i = 0; i < configs.length; i++) {
			try {
				String pname = configs[i].getAttribute(attribute, ILaunchConstants.EMPTY_STRING);
				if(name.equals(pname)) {
					cfgs.add(configs[i]);
				}
			}
			catch(CoreException ce) {
				//ignore
			}
		}
		return (ILaunchConfiguration[]) cfgs.toArray(new ILaunchConfiguration[cfgs.size()]);
	}
}
