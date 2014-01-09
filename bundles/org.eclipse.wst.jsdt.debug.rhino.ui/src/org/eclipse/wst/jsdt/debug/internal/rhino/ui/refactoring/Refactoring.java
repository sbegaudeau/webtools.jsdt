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
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.launching.IncludeEntry;

/**
 * Utilities to help with refactoring
 * 
 * @since 1.0
 */
public class Refactoring {

	public static ILaunchConfiguration[] NO_CONFIGS = new ILaunchConfiguration[0];
	public static IncludeEntry[] NO_ENTRIES = new IncludeEntry[0];
	
	/**
	 * Creates one or more {@link Change}s required after an {@link IProject} rename
	 * 
	 * @param project the renamed {@link IProject}
	 * @param newname the new name
	 * @return a new {@link Change}
	 */
	public static Change createChangesForProjectRename(IProject project, String newname) {
		ILaunchConfiguration[] configs = getConfigurationsScopedTo(project.getFullPath().makeAbsolute().toString());
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
		String scriptname = script.getFullPath().makeAbsolute().toString();
		ILaunchConfiguration[] configs = getConfigurationsScopedTo(scriptname);
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
		String foldername = folder.getFullPath().makeAbsolute().toString();
		ILaunchConfiguration[] configs = getConfigurationsScopedTo(foldername);
		if(configs.length < 1) {
			return null;
		}
		ArrayList changes = new ArrayList(configs.length);
		for (int i = 0; i < configs.length; i++) {
			changes.add(new FolderChange(configs[i], foldername, newname));
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
			IFile script = getScript(configuration);
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
	 * Fetches the script attribute as an {@link IFile} or returns <code>null</code> if it could not be found
	 * 
	 * @param configuration
	 * @return the {@link IFile} backing the script attribute or <code>null</code> if not found
	 * @throws CoreException
	 */
	public static IFile getScript(ILaunchConfiguration configuration) throws CoreException {
		String name = configuration.getAttribute(ILaunchConstants.ATTR_SCRIPT, ILaunchConstants.EMPTY_STRING);
		if(!ILaunchConstants.EMPTY_STRING.equals(name)) {
			IPath spath = new Path(name); 
			IResource script = ResourcesPlugin.getWorkspace().getRoot().findMember(spath);
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
	 * Returns the complete listing of Rhino {@link ILaunchConfiguration}s that reference the given path in some way.
	 * The supported reference kinds are (1) via the script attribute, and (2) via the include path where it could be an exact
	 * match for a workspace script or a partial match for a local file system script
	 *  
	 * @param elementpath the absolute path of the element to find configuration for
	 * @return the complete listing of Rhino configurations that reference the given path or an empty array, never <code>null</code>
	 */
	public static ILaunchConfiguration[] getConfigurationsScopedTo(String elementpath) {
		if(elementpath == null) {
			return NO_CONFIGS;
		}
		ILaunchConfiguration[] configs = getRhinoConfigurations();
		ArrayList scoped = new ArrayList(4);
		List includes = null;
		String script = null;
		for (int i = 0; i < configs.length; i++) {
			try {
				script = configs[i].getAttribute(ILaunchConstants.ATTR_SCRIPT, ILaunchConstants.EMPTY_STRING);
				if(elementpath.equals(script) || script.indexOf(elementpath) > -1) {
					scoped.add(configs[i]);
				}
				else {
					includes = configs[i].getAttribute(ILaunchConstants.ATTR_INCLUDE_PATH, (List)null);
					if(includes != null && hasIncludeEntryFor(includes, elementpath)) {
						scoped.add(configs[i]);
					}
				}
			}
			catch(CoreException ce) {
				//ignore, just don't report the configuration
			}
		}
		return (ILaunchConfiguration[]) scoped.toArray(new ILaunchConfiguration[scoped.size()]);
	}
	
	/**
	 * Returns <code>true</code> if the given set of include path entries contains an entry that matches
	 * the given script path.
	 * 
	 * @param includes
	 * @param elementpath
	 * @return <code>true</code> if a matching include path entry is found
	 */
	public static boolean hasIncludeEntryFor(List includes, String elementpath) {
		if(includes == null || elementpath == null) {
			return false;
		}
		String entry = null;
		String path = null;
		for (int i = 0; i < includes.size(); i++) {
			entry = (String) includes.get(i);
			path = entry.substring(1);
			if(elementpath.equals(path) || path.indexOf(elementpath) > -1) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the {@link IncludeEntry}'s for the given absolute script path or an empty array.
	 * <br><br>
	 * This method avoids creating {@link IncludeEntry} objects unless potential matches are found.
	 * 
	 * @param includes the current list of includes to search
	 * @param elementpath the absolute path of the element to check for
	 * @return the {@link IncludeEntry}'s of the given script in the include path listing or an empty array, never <code>null</code>
	 */
	public static final IncludeEntry[] findIncludeEntries(List includes, String elementpath) {
		if(includes == null || elementpath == null) {
			return NO_ENTRIES;
		}
		String entry = null;
		String path = null;
		ArrayList list = new ArrayList();
		for (int i = 0; i < includes.size(); i++) {
			entry = (String) includes.get(i);
			path = entry.substring(1);
			if(elementpath.equals(path) || path.indexOf(elementpath) > -1) {
				try {
					list.add(new IncludeEntry(Integer.parseInt(entry.substring(0, 1)), path));
				}
				catch(NumberFormatException nfe) {
					//ignore but keep looking
				}
			}
		}
		return (IncludeEntry[]) list.toArray(new IncludeEntry[list.size()]);
	}
	
	/**
	 * Returns the complete include path from the given {@link ILaunchConfiguration} as a list of {@link IncludeEntry}s
	 * in the order they are defined in the configuration.
	 * 
	 * @param configuration
	 * @return the ordered listing of {@link IncludeEntry}s or an empty array, never <code>null</code>
	 */
	public static final IncludeEntry[] getIncludeEntries(ILaunchConfiguration configuration) {
		try {
			List includes = configuration.getAttribute(ILaunchConstants.ATTR_INCLUDE_PATH, (List)null);
			if(includes != null) {
				ArrayList entries = new ArrayList(includes.size());
				String entry = null;
				for (int i = 0; i < includes.size(); i++) {
					entry = (String) includes.get(i);
					try {
						entries.add(new IncludeEntry(Integer.parseInt(entry.substring(0, 1)), entry.substring(1)));
					}
					catch(NumberFormatException nfe) {
						//ignore, keep going
					}
				}
				return (IncludeEntry[]) entries.toArray(new IncludeEntry[entries.size()]);
			}
		}
		catch(CoreException ce) {
			//do nothing return none
		}
		return NO_ENTRIES;
	}
}
