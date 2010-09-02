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
package org.eclipse.wst.jsdt.debug.internal.rhino.ui.source;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.ISourcePathComputerDelegate;
import org.eclipse.debug.core.sourcelookup.containers.DirectorySourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.FolderSourceContainer;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.ILaunchConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.launching.IncludeEntry;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.refactoring.Refactoring;

/**
 * Source computer delegate for Rhino
 * 
 * @since 1.0
 */
public class RhinoSourcePathComputer implements ISourcePathComputerDelegate {

	static final ISourceContainer[] NO_CONTAINERS = new ISourceContainer[0];
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.sourcelookup.ISourcePathComputerDelegate#computeSourceContainers(org.eclipse.debug.core.ILaunchConfiguration, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public ISourceContainer[] computeSourceContainers(ILaunchConfiguration configuration, IProgressMonitor monitor)	throws CoreException {
		HashSet containers = new HashSet();
		List includes = configuration.getAttribute(ILaunchConstants.ATTR_INCLUDE_PATH, (List)null); 
		if(includes != null) {
			String entry = null, name = null;
			for (Iterator i = includes.iterator(); i.hasNext();) {
				entry = (String) i.next();
				int kind = Integer.parseInt(entry.substring(0, 1));
				name = entry.substring(1);
				switch(kind) {
					case IncludeEntry.LOCAL_SCRIPT: {
						IFile file = (IFile) ResourcesPlugin.getWorkspace().getRoot().findMember(name);
						if(file != null) {
							containers.add(new FolderSourceContainer(file.getParent(), false));
						}
						continue;
					}
					case IncludeEntry.EXT_SCRIPT: {
						File file = new File(name);
						if(file.exists()) {
							containers.add(new DirectorySourceContainer(file.getParentFile(), false));
						}
						continue;
					}
				}
			}
		}
		//make sure the folder containing the original script is included
		IFile script = Refactoring.getScript(configuration);
		if(script != null) {
			containers.add(new FolderSourceContainer(script.getParent(), false));
		}
		return (ISourceContainer[]) containers.toArray(new ISourceContainer[containers.size()]);
	}
}
