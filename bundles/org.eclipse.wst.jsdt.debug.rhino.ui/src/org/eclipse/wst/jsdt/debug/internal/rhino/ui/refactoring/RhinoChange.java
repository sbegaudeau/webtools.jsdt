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

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.ILaunchConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.launching.IncludeEntry;

/**
 * Abstract {@link Change} for Rhino configurations.
 * <br><br>
 * By default this change returns the backing {@link ILaunchConfiguration} from the {@link #getModifiedElement()}
 * method and considers the {@link Change} to be valid if the backing {@link ILaunchConfiguration} exists.
 * 
 * @since 1.0
 */
public abstract class RhinoChange extends Change {

	protected String oldname = null;
	protected String newname = null;
	protected ILaunchConfiguration configuration = null;
	
	/**
	 * Constructor
	 * 
	 * @param configuration the backing {@link ILaunchConfiguration}
	 * @param oldname the existing name of the element being refactored
	 * @param newname the computed new name of the element being refactored
	 */
	public RhinoChange(ILaunchConfiguration configuration, String oldname, String newname) {
		this.configuration = configuration;
		this.oldname = oldname;
		this.newname = newname;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#getName()
	 */
	public String getName() {
		return NLS.bind(Messages.update_attributes, new String[] {configuration.getName(), newname});
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#initializeValidationData(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void initializeValidationData(IProgressMonitor pm) {
		//do nothing
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#isValid(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public RefactoringStatus isValid(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		if(!configuration.exists()) {
			return RefactoringStatus.createErrorStatus(NLS.bind(Messages.config_no_longer_exists, configuration.getName()));
		}
		return new RefactoringStatus();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#getModifiedElement()
	 */
	public Object getModifiedElement() {
		return configuration;
	}
	
	/**
	 * Computes a new container name for the configuration or <code>null</code>
	 * 
	 * @param configuration
	 * @return the new container name for the configuration or <code>null</code>
	 */
	protected String computeNewContainerName(ILaunchConfiguration configuration) {
		IFile file = configuration.getFile();
		if(file != null) {
			return file.getParent().getProjectRelativePath().toString();
		}
		return null;
	}
	
	/**
	 * Computes a new name for the configuration or <code>null</code> if the name does not need updating
	 * 
	 * @param configuration
	 * @return the new name for the configuration or <code>null</code>
	 */
	protected String computeNewConfigurationName(ILaunchConfiguration configuration) {
		String name = configuration.getName();
		IPath old = new Path(oldname);
		String nametofind = (old.segmentCount() > 1 ? old.lastSegment() : oldname);
		int idx = name.indexOf(nametofind);
		if(idx > -1) {
			IPath neww = new Path(newname);
			String replacement = (neww.segmentCount() > 1 ? neww.lastSegment() : newname);
			return name.replaceAll(nametofind, replacement);
		}
		return null;
	}
	
	/**
	 * Computes the new name for the script attribute or <code>null</code> if an update is not needed
	 * 
	 * @param configuration
	 * @return the new name or <code>null</code>
	 */
	protected String computeNewScriptName(ILaunchConfiguration configuration) {
		try {
			String attr = configuration.getAttribute(ILaunchConstants.ATTR_SCRIPT, ILaunchConstants.EMPTY_STRING);
			return computeNewName(attr);
		}
		catch(CoreException ce) {
			//ignore
		}
		return null;
	}
	
	/**
	 * Updates any include entries with matching elements to the {@link #oldname}
	 * 
	 * @param copy
	 * @throws CoreException
	 */
	protected void updateIncludeEntries(ILaunchConfigurationWorkingCopy copy) throws CoreException {
		//update the include path
		List includes = copy.getAttribute(ILaunchConstants.ATTR_INCLUDE_PATH, (List)null);
		if(includes != null) {
			IncludeEntry[] entries = Refactoring.findIncludeEntries(includes, oldname);
			String sname = null;
			for (int i = 0; i < entries.length; i++) {
				sname = computeNewName(entries[i].getPath());
				if(sname != null && !oldname.equals(sname)) {
					int idx = includes.indexOf(entries[i].string());
					if(idx > -1) {
						includes.remove(idx);
						includes.add(idx, new IncludeEntry(entries[i].getKind(), sname).string());
					}
				}
			}
			copy.setAttribute(ILaunchConstants.ATTR_INCLUDE_PATH, includes);
		}
	}
	
	/**
	 * Computes a new element name using {@link #newname} iff: (1) the given name
	 * has the {@link #oldname} as a prefix or (2) the old name appears in the given name.
	 * <br><br>
	 * The computed new name is not guaranteed to exist in any way.
	 * 
	 * @param name
	 * @return the new name to use or <code>null</code>
	 */
	protected String computeNewName(String name) {
		IPath path = new Path(name);
		IPath old = new Path(oldname);
		if(old.isPrefixOf(path)) {
			path = path.removeFirstSegments(old.segmentCount());
			path = new Path(newname).append(path);
			return path.makeAbsolute().toString();
		}
		return path.toString().replaceAll(oldname, newname);
	}
}
