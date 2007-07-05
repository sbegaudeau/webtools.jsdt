/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     IBM Corporation - added support for requesting updates of a particular
 *                       container for generic container operations.
 * 						 - canUpdateClasspathContainer(IPath, IJavaProject)
 * 						 - requestClasspathContainerUpdate(IPath, IJavaProject, IClasspathContainer)
 *     IBM Corporation - allow initializers to provide a readable description
 *                       of a container reference, ahead of actual resolution.
 * 						 - getDescription(IPath, IJavaProject)
 *******************************************************************************/
package org.eclipse.wst.jsdt.core;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.jsdt.core.compiler.libraries.LibraryLocation;

/**
 * Abstract base implementation of all classpath container initializer.
 * Classpath variable containers are used in conjunction with the
 * "org.eclipse.wst.jsdt.core.classpathContainerInitializer" extension point.
 * <p>
 * Clients should subclass this class to implement a specific classpath
 * container initializer. The subclass must have a public 0-argument
 * constructor and a concrete implementation of <code>initialize</code>.
 * <p>
 * Multiple classpath containers can be registered, each of them declares
 * the container ID they can handle, so as to narrow the set of containers they
 * can resolve, in other words, a container initializer is guaranteed to only be 
 * activated to resolve containers which match the ID they registered onto.
 * <p>
 * In case multiple container initializers collide on the same container ID, the first
 * registered one will be invoked.
 * 
 * @see IClasspathEntry
 * @see IClasspathContainer
 * @since 2.0
 */
public abstract class ClasspathContainerInitializer implements IClasspathContainerInitialzer,  IClasspathContainer {
	
   /**
     * Creates a new classpath container initializer.
     */
    public ClasspathContainerInitializer() {
    	// a classpath container initializer must have a public 0-argument constructor
    }

	public void initialize(IPath containerPath, IJavaProject project) throws CoreException {
		JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project }, new IClasspathContainer[] { getContainer(containerPath, project) }, null);
	}
	
	protected IClasspathContainer getContainer(IPath containerPath, IJavaProject project) {
		return this;
	}
    /* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.IClasspathContainerInitialzer#canUpdateClasspathContainer(org.eclipse.core.runtime.IPath, org.eclipse.wst.jsdt.core.IJavaProject)
	 */
    public boolean canUpdateClasspathContainer(IPath containerPath, IJavaProject project) {
    	if(project==null || containerPath==null) return true;
    	LibrarySuperType superType = project.getCommonSuperType();
    	return superType!=null && superType.getRawContainerPath().equals(getPath());
    }

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.IClasspathContainerInitialzer#requestClasspathContainerUpdate(org.eclipse.core.runtime.IPath, org.eclipse.wst.jsdt.core.IJavaProject, org.eclipse.wst.jsdt.core.IClasspathContainer)
	 */
    public void requestClasspathContainerUpdate(IPath containerPath, IJavaProject project, IClasspathContainer containerSuggestion) throws CoreException {

		// By default, classpath container initializers do not accept updating containers
    }

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.IClasspathContainerInitialzer#getDescription(org.eclipse.core.runtime.IPath, org.eclipse.wst.jsdt.core.IJavaProject)
	 */    
    public String getDescription(IPath containerPath, IJavaProject project) {
    	
    	// By default, a container path is the only available description
    	return containerPath.makeRelative().toString();
    }
    
    /* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.IClasspathContainerInitialzer#getFailureContainer(org.eclipse.core.runtime.IPath, org.eclipse.wst.jsdt.core.IJavaProject)
	 */
    public IClasspathContainer getFailureContainer(final IPath containerPath, IJavaProject project) {
    	final String description = getDescription(containerPath, project);
    	return 
    		new IClasspathContainer() {
				public IClasspathEntry[] getClasspathEntries() { 
					return new IClasspathEntry[0]; 
				}
				public String getDescription() { 
					return description;
				}
				public int getKind() { 
					return 0; 
				}
				public IPath getPath() { 
					return containerPath; 
				}
				public String toString() { 
					return getDescription(); 
				}
			};
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.IClasspathContainerInitialzer#getComparisonID(org.eclipse.core.runtime.IPath, org.eclipse.wst.jsdt.core.IJavaProject)
	 */
	public Object getComparisonID(IPath containerPath, IJavaProject project) {

		// By default, containers are identical if they have the same containerPath first segment,
		// but this may be refined by other container initializer implementations.
		if (containerPath == null) {
			return null;
		} else {
			return containerPath.segment(0);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.IClasspathContainerInitialzer#getHostPath(org.eclipse.core.runtime.IPath, org.eclipse.wst.jsdt.core.IJavaProject)
	 */
	public URI getHostPath(IPath path, IJavaProject project) {
		return null;
	}
	
	public boolean allowAttachJsDoc() { return true; };
	/*
	 * returns a String of all SuperTypes provided by this library.
	 */
	public String[] containerSuperTypes() {return new String[0];};
	
	public IClasspathEntry[] getClasspathEntries() {
		LibraryLocation libLocation =  getLibraryLocation();
		char[][] filesInLibs = libLocation.getLibraryFileNames();
		IClasspathEntry[] entries = new IClasspathEntry[filesInLibs.length];
		for (int i = 0; i < entries.length; i++) {
			IPath workingLibPath = new Path(libLocation.getLibraryPath(filesInLibs[i]));
			entries[i] = JavaCore.newLibraryEntry(workingLibPath.makeAbsolute(), null, null, new IAccessRule[0], new IClasspathAttribute[0], true);
		}
		return entries;
	}

	public String getDescription() {		
		return null;
	}

	public int getKind() {
		
		return 0;
	}

	public IPath getPath() {
		
		return null;
	}
	
}

