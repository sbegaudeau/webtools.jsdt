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

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptStackFrame;
import org.eclipse.wst.jsdt.debug.core.model.IScript;
import org.eclipse.wst.jsdt.debug.internal.core.Constants;
import org.eclipse.wst.jsdt.debug.internal.core.JavaScriptDebugPlugin;
import org.eclipse.wst.jsdt.debug.internal.core.model.Script;

/**
 * Utility class to help with looking up source
 * 
 * @since 1.1
 */
public final class SourceLookup {

	/**
	 * Returns the raw element source to use to display an external editor. This method 
	 * will make a request from the backing {@link VirtualMachine}.
	 * 
	 * @param sourceobj the object to get the raw source from
	 * @return the raw source or <code>null</code>
	 */
	public static String getSource(Object sourceobj) {
		if(sourceobj instanceof IJavaScriptStackFrame) {
			IJavaScriptStackFrame jframe = (IJavaScriptStackFrame) sourceobj;
			return jframe.getSource();
		}
		if(sourceobj instanceof IScript) {
			IScript script = (IScript) sourceobj;
			return script.source();
		}
		return null;
	}

	/**
	 * Returns the {@link URI} to use to look up source
	 * 
	 * @param sourceobj the object to get the source {@link URI} for
	 * @return the {@link URI} or <code>null</code>
	 */
	public static URI getSourceURI(Object sourceobj) {
		if(sourceobj instanceof IJavaScriptStackFrame) {
			IJavaScriptStackFrame jframe = (IJavaScriptStackFrame) sourceobj;
			try {
				return URIUtil.fromString(jframe.getSourcePath());
			} catch (URISyntaxException e) {
				JavaScriptDebugPlugin.log(e);
			}
		}
		if(sourceobj instanceof IScript) {
			IScript script = (IScript) sourceobj;
			return script.sourceURI();
		}
		return null;
	}

	/**
	 * Returns the external source from the given {@link URI}. This method queries the <code>External JavaScript Source</code>
	 * project and create it and the source {@link IFile} is necessary.
	 * 
	 * @param sourceuri the {@link URI} to get the source for
	 * @param sourceobj the backing object we want to get the source for
	 * @throws CoreException 
	 */
	public static IFile getExternalSource(URI sourceuri, Object sourceobj) throws CoreException {
		String source = getSource(sourceobj);
		if(source != null) {
			IProject project = JavaScriptDebugPlugin.getExternalSourceProject(true);
			String filename = Script.resolveName(sourceuri);
			String uriHash =  Integer.toString(sourceuri.toString().hashCode());
			String sourceHash = Integer.toString(source.hashCode());
			IPath path = new Path(uriHash).append(sourceHash).append(filename);
			String ext = path.getFileExtension(); 
			if(ext == null || !Constants.JS_EXTENSION.equals(ext)) {
				path = path.addFileExtension(Constants.JS_EXTENSION);
			}
			IFile file = project.getFile(path);
			if(!file.isAccessible()) {
				IContainer folder = project;
				for (int i = 0; i < path.segmentCount()-1; i++) {
					IFolder f = folder.getFolder(new Path(path.segment(i)));
					if(!f.exists()) {
						f.create(true, true, null);
					}
					folder = f;
				}
				file.create(new ByteArrayInputStream(source.getBytes()), true, null);
			}
			return file;
		}
		return null;
	}
}
