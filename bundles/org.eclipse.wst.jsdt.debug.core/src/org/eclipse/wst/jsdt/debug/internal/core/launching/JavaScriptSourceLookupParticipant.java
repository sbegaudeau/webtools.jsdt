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
package org.eclipse.wst.jsdt.debug.internal.core.launching;

import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.debug.core.sourcelookup.AbstractSourceLookupParticipant;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptStackFrame;
import org.eclipse.wst.jsdt.debug.core.model.IScript;
import org.eclipse.wst.jsdt.debug.internal.core.JavaScriptDebugPlugin;

/**
 * Default source lookup participant
 * 
 * @since 1.0
 */
public class JavaScriptSourceLookupParticipant extends AbstractSourceLookupParticipant {

	static final Object[] NO_SOURCE = new Object[0];

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.sourcelookup.ISourceLookupParticipant#getSourceName(java.lang.Object)
	 */
	public String getSourceName(Object object) throws CoreException {
		if (object instanceof IJavaScriptStackFrame) {
			return ((IJavaScriptStackFrame) object).getSourceName();
		}
		if(object instanceof IScript) {
			return URIUtil.lastSegment(((IScript)object).sourceURI());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.sourcelookup.AbstractSourceLookupParticipant#findSourceElements(java.lang.Object)
	 */
	public Object[] findSourceElements(Object object) throws CoreException {
		URI sourceURI = SourceLookup.getSourceURI(object);
		if (sourceURI != null) {
			if (!sourceURI.isAbsolute() || "file".equals(sourceURI.getScheme())) {//$NON-NLS-1$			
				IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
				URI workspaceURI = workspaceRoot.getRawLocationURI();			
				URI workspaceRelativeURI = workspaceURI.relativize(sourceURI);
				if (! workspaceRelativeURI.isAbsolute()) {
					IFile file = (IFile) workspaceRoot.findMember(new Path(workspaceRelativeURI.getPath()), false);
					if (file != null) {
						return new IFile[] { file };
					}
				}
			}
			//else show the temp source
			return showExternalSource(sourceURI, object);
		}
		return NO_SOURCE;
	}

	/**
	 * Shows the source in an external editor
	 * 
	 * @param sourceuri
	 * @param sourceobj
	 * 
	 * @return the collection of files to show in external editors
	 */
	private Object[] showExternalSource(URI sourceuri, Object sourceobj) {
		try {
			IFile file = SourceLookup.getExternalSource(sourceuri, sourceobj);
			if(file != null) {
				IPath path = file.getProjectRelativePath();
				if (JavaScriptDebugPlugin.getExternalScriptPath(path) == null) {
					JavaScriptDebugPlugin.addExternalScriptPath(path, sourceuri.toString());
				}	
				return new Object[] {file};
			}

		} catch (CoreException ce) {
			JavaScriptDebugPlugin.log(ce);
			return NO_SOURCE;
		}
		return NO_SOURCE;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.sourcelookup.AbstractSourceLookupParticipant#isFindDuplicates()
	 */
	public boolean isFindDuplicates() {
		return true;
	}
}
