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
 * 						 - canUpdateJsGlobalScopeContainer(IPath, IJavaScriptProject)
 * 						 - requestJsGlobalScopeContainerUpdate(IPath, IJavaScriptProject, IJsGlobalScopeContainer)
 *     IBM Corporation - allow initializers to provide a readable description
 *                       of a container reference, ahead of actual resolution.
 * 						 - getDescription(IPath, IJavaScriptProject)
 *******************************************************************************/
package org.eclipse.wst.jsdt.core;

import java.net.URI;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.jsdt.core.compiler.libraries.LibraryLocation;
import org.eclipse.wst.jsdt.internal.core.JavaModelStatus;

/**
 * Abstract base implementation of all classpath container initializer.
 * Classpath variable containers are used in conjunction with the
 * "org.eclipse.wst.jsdt.core.JsGlobalScopeContainerInitializer" extension point.
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
 * @see IIncludePathEntry
 * @see IJsGlobalScopeContainer
 * @since 2.0
 */
public abstract class JsGlobalScopeContainerInitializer implements IJsGlobalScopeContainerInitializer,  IJsGlobalScopeContainer {

	/**
	 * Status code indicating that an attribute is not supported.
	 *
	 * @see #getAccessRulesStatus(IPath, IJavaScriptProject)
	 * @see #getAttributeStatus(IPath, IJavaScriptProject, String)
	 * @see #getSourceAttachmentStatus(IPath, IJavaScriptProject)
	 *
	 * @since 3.3
	 */
	public static final int ATTRIBUTE_NOT_SUPPORTED = 1;

	/**
	 * Status code indicating that an attribute is not modifiable.
	 *
	 * @see #getAccessRulesStatus(IPath, IJavaScriptProject)
	 * @see #getAttributeStatus(IPath, IJavaScriptProject, String)
	 * @see #getSourceAttachmentStatus(IPath, IJavaScriptProject)
	 *
	 * @since 3.3
	 */
	public static final int ATTRIBUTE_READ_ONLY = 2;

   /**
     * Creates a new classpath container initializer.
     */
    public JsGlobalScopeContainerInitializer() {
    	// a classpath container initializer must have a public 0-argument constructor
    }

	public void initialize(IPath containerPath, IJavaScriptProject project) throws CoreException {
		JavaScriptCore.setJsGlobalScopeContainer(containerPath, new IJavaScriptProject[] { project }, new IJsGlobalScopeContainer[] { getContainer(containerPath, project) }, null);
	}

	protected IJsGlobalScopeContainer getContainer(IPath containerPath, IJavaScriptProject project) {
		return this;
	}
    /* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.IJsGlobalScopeContainerInitialzer#canUpdateJsGlobalScopeContainer(org.eclipse.core.runtime.IPath, org.eclipse.wst.jsdt.core.IJavaScriptProject)
	 */
    public boolean canUpdateJsGlobalScopeContainer(IPath containerPath, IJavaScriptProject project) {
    	if(project==null || containerPath==null) return true;
    	LibrarySuperType superType = project.getCommonSuperType();
    	return superType!=null && superType.getRawContainerPath().equals(getPath());
    }

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.IJsGlobalScopeContainerInitialzer#requestJsGlobalScopeContainerUpdate(org.eclipse.core.runtime.IPath, org.eclipse.wst.jsdt.core.IJavaScriptProject, org.eclipse.wst.jsdt.core.IJsGlobalScopeContainer)
	 */
    public void requestJsGlobalScopeContainerUpdate(IPath containerPath, IJavaScriptProject project, IJsGlobalScopeContainer containerSuggestion) throws CoreException {

		// By default, classpath container initializers do not accept updating containers
    }

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.IJsGlobalScopeContainerInitialzer#getDescription(org.eclipse.core.runtime.IPath, org.eclipse.wst.jsdt.core.IJavaScriptProject)
	 */
    public String getDescription(IPath containerPath, IJavaScriptProject project) {

    	// By default, a container path is the only available description
    	return containerPath.makeRelative().toString();
    }

    /* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.IJsGlobalScopeContainerInitialzer#getFailureContainer(org.eclipse.core.runtime.IPath, org.eclipse.wst.jsdt.core.IJavaScriptProject)
	 */
    public IJsGlobalScopeContainer getFailureContainer(final IPath containerPath, IJavaScriptProject project) {
    	final String description = getDescription(containerPath, project);
    	return
    		new IJsGlobalScopeContainer() {
				/**
				 * @deprecated Use {@link #getIncludepathEntries()} instead
				 */
				public IIncludePathEntry[] getClasspathEntries() {
					return getIncludepathEntries();
				}
				public IIncludePathEntry[] getIncludepathEntries() {
					return new IIncludePathEntry[0];
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
				public String[] resolvedLibraryImport(String a) {
					return new String[] {a};
				}
			};
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.IJsGlobalScopeContainerInitialzer#getComparisonID(org.eclipse.core.runtime.IPath, org.eclipse.wst.jsdt.core.IJavaScriptProject)
	 */
	public Object getComparisonID(IPath containerPath, IJavaScriptProject project) {

		// By default, containers are identical if they have the same containerPath first segment,
		// but this may be refined by other container initializer implementations.
		if (containerPath == null) {
			return null;
		} else {
			return containerPath.segment(0);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.IJsGlobalScopeContainerInitialzer#getHostPath(org.eclipse.core.runtime.IPath, org.eclipse.wst.jsdt.core.IJavaScriptProject)
	 */
	public URI getHostPath(IPath path, IJavaScriptProject project) {
		return null;
	}

	public boolean allowAttachJsDoc() { return true; }
	/*
	 * returns a String of all SuperTypes provided by this library.
	 */
	public String[] containerSuperTypes() {return new String[0];}

	/* Return a string of imports to replace the real imports for.  necisary for toolkits and other t
	 * things that may have a certain import best for runtime but not best for building a model
	 *
	 */
	public String[] resolvedLibraryImport(String realImport) {
		return new String[] {realImport};
	}


	/**
	 * @deprecated Use {@link #getIncludepathEntries()} instead
	 */
	public IIncludePathEntry[] getClasspathEntries() {
		return getIncludepathEntries();
	}

	public IIncludePathEntry[] getIncludepathEntries() {
		LibraryLocation libLocation =  getLibraryLocation();
		char[][] filesInLibs = libLocation.getLibraryFileNames();
		IIncludePathEntry[] entries = new IIncludePathEntry[filesInLibs.length];
		for (int i = 0; i < entries.length; i++) {
			IPath workingLibPath = new Path(libLocation.getLibraryPath(filesInLibs[i]));
			entries[i] = JavaScriptCore.newLibraryEntry(workingLibPath.makeAbsolute(), null, null, new IAccessRule[0], new IIncludePathAttribute[0], true);
		}
		return entries;
	}

	public String getDescription() {
		return null;
	}

	public int getKind() {

		return K_APPLICATION;
	}

	public IPath getPath() {

		return null;
	}
	/**
	 * Returns the access rules attribute status according to this initializer.
	 * <p>
	 * The returned {@link IStatus status} can have one of the following severities:
	 * <ul>
	 * <li>{@link IStatus#OK OK}: means that the attribute is supported
	 * 	<strong>and</strong> is modifiable</li>
	 * <li>{@link IStatus#ERROR ERROR}: means that either the attribute
	 * 	is not supported or is not modifiable.<br>
	 * 	In this case, the {@link IStatus#getCode() code}will have
	 * 	respectively the {@link #ATTRIBUTE_NOT_SUPPORTED} value
	 * 	or the {@link #ATTRIBUTE_READ_ONLY} value.</li>
	 * </ul>
	 * </p><p>
	 * The status message can contain more information.
	 * </p><p>
	 * If the subclass does not override this method, then the default behavior is
	 * to return {@link IStatus#OK OK} if and only if the classpath container can
	 * be updated (see {@link #canUpdateJsGlobalScopeContainer(IPath, IJavaScriptProject)}).
	 * </p>
	 *
	 * @param containerPath the path of the container which requires to be
	 * 	updated
	 * @param project the project for which the container is to be updated
	 * @return returns the access rules attribute status
	 *
	 * @since 3.3
	 */
	public IStatus getAccessRulesStatus(IPath containerPath, IJavaScriptProject project) {

		if (canUpdateJsGlobalScopeContainer(containerPath, project)) {
			return Status.OK_STATUS;
		}
		return new JavaModelStatus(ATTRIBUTE_READ_ONLY);
	}

	/**
	 * Returns the extra attribute status according to this initializer.
	 * <p>
	 * The returned {@link IStatus status} can have one of the following severities:
	 * <ul>
	 * <li>{@link IStatus#OK OK}: means that the attribute is supported
	 * 	<strong>and</strong> is modifiable</li>
	 * <li>{@link IStatus#ERROR ERROR}: means that either the attribute
	 * 	is not supported or is not modifiable.<br>
	 * 	In this case, the {@link IStatus#getCode() code}will have
	 * 	respectively the {@link #ATTRIBUTE_NOT_SUPPORTED} value
	 * 	or the {@link #ATTRIBUTE_READ_ONLY} value.</li>
	 * </ul>
	 * </p><p>
	 * The status message can contain more information.
	 * </p><p>
	 * If the subclass does not override this method, then the default behavior is
	 * to return {@link IStatus#OK OK} if and only if the classpath container can
	 * be updated (see {@link #canUpdateJsGlobalScopeContainer(IPath, IJavaScriptProject)}).
	 * </p>
	 *
	 * @param containerPath the path of the container which requires to be
	 * 	updated
	 * @param project the project for which the container is to be updated
	 * @param attributeKey the key of the extra attribute
	 * @return returns the extra attribute status
	 * @see IIncludePathAttribute
	 *
	 * @since 3.3
	 */
	public IStatus getAttributeStatus(IPath containerPath, IJavaScriptProject project, String attributeKey) {

		if (canUpdateJsGlobalScopeContainer(containerPath, project)) {
			return Status.OK_STATUS;
		}
		return new JavaModelStatus(ATTRIBUTE_READ_ONLY);
	}

	/**
	 * Returns the source attachment attribute status according to this initializer.
	 * <p>
	 * The returned {@link IStatus status} can have one of the following severities:
	 * <ul>
	 * <li>{@link IStatus#OK OK}: means that the attribute is supported
	 * 	<strong>and</strong> is modifiable</li>
	 * <li>{@link IStatus#ERROR ERROR}: means that either the attribute
	 * 	is not supported or is not modifiable.<br>
	 * 	In this case, the {@link IStatus#getCode() code}will have
	 * 	respectively the {@link #ATTRIBUTE_NOT_SUPPORTED} value
	 * 	or the {@link #ATTRIBUTE_READ_ONLY} value.</li>
	 * </ul>
	 * </p><p>
	 * The status message can contain more information.
	 * </p><p>
	 * If the subclass does not override this method, then the default behavior is
	 * to return {@link IStatus#OK OK} if and only if the classpath container can
	 * be updated (see {@link #canUpdateJsGlobalScopeContainer(IPath, IJavaScriptProject)}).
	 * </p>
	 *
	 * @param containerPath the path of the container which requires to be
	 * 	updated
	 * @param project the project for which the container is to be updated
	 * @return returns the source attachment attribute status
	 *
	 * @since 3.3
	 */
	public IStatus getSourceAttachmentStatus(IPath containerPath, IJavaScriptProject project) {

		if (canUpdateJsGlobalScopeContainer(containerPath, project)) {
			return Status.OK_STATUS;
		}
		return new JavaModelStatus(ATTRIBUTE_READ_ONLY);
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.IJsGlobalScopeContainerInitialzer#getInferenceID()
	 */
	public String getInferenceID()
	{
		return null;
	}

	public void removeFromProject(IJavaScriptProject project) {}
	
}

