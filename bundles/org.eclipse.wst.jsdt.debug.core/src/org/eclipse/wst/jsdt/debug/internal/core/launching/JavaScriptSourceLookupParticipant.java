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

import org.eclipse.core.resources.IFile;
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
		String path = getSourcePath(object);
		if (path != null) {
			// TODO not sure if we should do a search for the member if the URI path is a miss
			IFile file = (IFile) ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(path), false);
			if (file != null) {
				return new IFile[] { file };
			}
			//try to find it using the source tab infos
			Object[] sources = super.findSourceElements(object);
			if(sources != null && sources.length > 0) {
				return sources;
			}
			//else show the temp source
			return showExternalSource(getSource(object), path);
		}
		return NO_SOURCE;
	}

	/**
	 * Returns the raw element source to use to display an external editor
	 * @param object
	 * @return the raw source or <code>null</code>
	 */
	String getSource(Object object) {
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
	 * Returns the path to use to look up source
	 * @param object
	 * @return the path or <code>null</code>
	 * @since 1.1
	 */
	String getSourcePath(Object object) {
		if(object instanceof IJavaScriptStackFrame) {
			IJavaScriptStackFrame jframe = (IJavaScriptStackFrame) object;
			return jframe.getSourcePath();
		}
		if(object instanceof IScript) {
			IScript script = (IScript) object;
			return script.sourceURI().getPath().toString();
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
	Object[] showExternalSource(String source, String path) {
		try {
			File tempdir = new File(System.getProperty("java.io.tmpdir")); //$NON-NLS-1$
			File file = new File(tempdir, formatExternalName(path));
			file.deleteOnExit();
			FileWriter writer = new FileWriter(file);
			writer.write(source);
			writer.close();
			return new Object[] {file};

		} catch (IOException e) {
			JavaScriptDebugPlugin.log(e);
			return NO_SOURCE;
		}
	}

	/**
	 * Formats the name of the external source to be:
	 * <code>[name]_[counter].js</code>.<br><br>
	 * If the given name is <code>null</code> the default
	 * name of <code>script_[counter].js</code> will be used
	 * @param basename
	 * @return the formatted external source name
	 */
	String formatExternalName(String basename) {
		IPath path = null;
		if(basename == null) {
			path = new Path("script"); //$NON-NLS-1$
		}
		else {
			path = new Path(basename);
		}
		path = path.removeFileExtension();
		StringBuffer buffer = new StringBuffer(path.toString());
		buffer.append(".js"); //$NON-NLS-1$
		return buffer.toString();
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
