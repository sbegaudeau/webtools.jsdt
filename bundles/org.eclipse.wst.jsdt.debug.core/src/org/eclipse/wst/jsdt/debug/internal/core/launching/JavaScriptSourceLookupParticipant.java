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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
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
		URI sourceURI = getSourceURI(object);
		if (sourceURI != null) {
			IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
			URI workspaceURI = workspaceRoot.getRawLocationURI();			
			URI workspaceRelativeURI = workspaceURI.relativize(sourceURI);
			if (! workspaceRelativeURI.isAbsolute()) {
				IFile file = (IFile) ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(workspaceRelativeURI.getPath()), false);
				if (file != null) {
					return new IFile[] { file };
				}
			}

			//try to find it using the source tab infos
			Object[] sources = super.findSourceElements(object);
			if(sources != null && sources.length > 0) {
				return sources;
			}
			//else show the temp source
			return showExternalSource(getSource(object), sourceURI);
		}
		return NO_SOURCE;
	}

	/**
	 * Returns the raw element source to use to display an external editor
	 * @param object
	 * @return the raw source or <code>null</code>
	 */
	private String getSource(Object object) {
		if(object instanceof IJavaScriptStackFrame) {
			IJavaScriptStackFrame jframe = (IJavaScriptStackFrame) object;
			return jframe.getSource();
		}
		if(object instanceof IScript) {
			IScript script = (IScript) object;
			return script.source();
		}
		return null;
	}
	
	/**
	 * Returns the uri to use to look up source
	 * @param object
	 * @return the URI or <code>null</code>
	 * @since 1.1
	 */
	private URI getSourceURI(Object object) {
		if(object instanceof IJavaScriptStackFrame) {
			IJavaScriptStackFrame jframe = (IJavaScriptStackFrame) object;
			return URI.create(jframe.getSourcePath());
		}
		if(object instanceof IScript) {
			IScript script = (IScript) object;
			return script.sourceURI();
		}
		return null;
	}
	
	/**
	 * Shows the source in an external editor
	 * 
	 * @param source
	 * @param path
	 * @return the collection of files to show in external editors
	 */
	private Object[] showExternalSource(String source, URI uri) {
		try {
			File tempdir = new File(System.getProperty("java.io.tmpdir")); //$NON-NLS-1$
			File jsdt_debug = new File(tempdir, "jsdt_debug"); //$NON-NLS-1$
			jsdt_debug.mkdir();
			jsdt_debug.deleteOnExit();

			String fileName = URIUtil.lastSegment(uri);
			if (fileName.endsWith(".js")) //$NON-NLS-1$
				fileName = fileName.substring(0, fileName.length()-3);
			fileName +="(" + Integer.toString(uri.toString().hashCode() + source.hashCode()) + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			fileName += ".js"; //$NON-NLS-1$
			
			File file = new File(jsdt_debug, fileName);
			file.deleteOnExit();
			
			if (!file.exists()) {
				FileWriter writer = new FileWriter(file);
				writer.write(source);
				writer.close();				
			}
			
			if (JavaScriptDebugPlugin.getExternalScriptPath(fileName) == null)
				JavaScriptDebugPlugin.addExternalScriptPath(fileName, uri.toString());
				
			return new Object[] {file};

		} catch (IOException e) {
			JavaScriptDebugPlugin.log(e);
			return NO_SOURCE;
		}
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
