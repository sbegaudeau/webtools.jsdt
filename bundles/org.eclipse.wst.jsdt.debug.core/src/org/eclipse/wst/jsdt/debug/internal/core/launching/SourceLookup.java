/*******************************************************************************
 * Copyright (c) 2010, 2012 IBM Corporation and others.
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
import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptStackFrame;
import org.eclipse.wst.jsdt.debug.core.model.IScript;
import org.eclipse.wst.jsdt.debug.internal.core.Constants;
import org.eclipse.wst.jsdt.debug.internal.core.JavaScriptDebugPlugin;

/**
 * Utility class to help with looking up source
 * 
 * @since 1.1
 */
public final class SourceLookup {

	public static final QualifiedName SCRIPT_URL = new QualifiedName(JavaScriptCore.PLUGIN_ID, "scriptURL"); //$NON-NLS-1$
	public static final IPath TOP_LEVEL_PATH = new Path("/"); //$NON-NLS-1$
	/**
	 * Returns the name of the source object to lookup or <code>null</code>
	 * if the object is not a {@link IJavaScriptStackFrame} or an {@link IScript}
	 * 
	 * @param object the object to look up source for
	 * @return the name of the source element to look up or <code>null</code>
	 * @since 1.1
	 */
	public static String getSourceName(Object object) {
		String name = null;
		if (object instanceof IJavaScriptStackFrame) {
			name = ((IJavaScriptStackFrame) object).getSourceName();
		}
		if(object instanceof IScript) {
			name = URIUtil.lastSegment(((IScript)object).sourceURI());
		}
		if(name != null) {
			if(new Path(name).getFileExtension() == null) {
				//append .js, there is no case where we would look up a file with no extension from a script node
				StringBuffer buf = new StringBuffer(name.length()+3);
				buf.append(name).append('.').append(Constants.JS_EXTENSION);
				return buf.toString();
			}
			return name;
		}
		return null;
	}
	
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
				try {
					return new URI(jframe.getSourcePath());
				}
				catch (URISyntaxException use) {
					return URIUtil.fromString(jframe.getSourcePath());
				}
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
	 * project and create it and the source {@link IFile} if necessary.
	 * 
	 * @param sourceuri the {@link URI} to get the source for
	 * @param sourceobj the backing object we want to get the source for
	 * @throws CoreException 
	 */
	public static IFile getExternalSource(URI sourceuri, Object sourceobj) throws CoreException {
		String source = getSource(sourceobj);
		if(source != null) {
			IProject project = JavaScriptDebugPlugin.getExternalSourceProject(true);
			IPath path = getSourcePath(sourceuri);
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
				file = doFormat(file, source);
			}
			else {
				file = doFormat(file, source);
			}
			file.setPersistentProperty(SCRIPT_URL, sourceuri.toString());
			return file;
		}
		return null;
	}
	
	/**
	 * Returns the source path from the given source {@link URI}. 
	 * <br>
	 * This path may be altered as follows:
	 * <ul>
	 * <li>if the URI path is only a root ('/') than the returned path is changed to 'page.js'</li>
	 * <li>if the source URI has a host specification, it is added to the beginning of the source path</li>
	 * <li>if the total character count of any one segment of the path exceeds 15 chars, the path is pruned to avoid 
	 * going over the windows 255 char path limit</li>
	 * </ul>
	 * @param sourceuri
	 * @return the {@link IPath} to use to represent the given source {@link URI}
	 */
	public static IPath getSourcePath(URI sourceuri) {
		String uripath = sourceuri.getPath();
		if(uripath == null) {
			return null;
		}
		if(uripath.trim().equals("/")) { //$NON-NLS-1$
			uripath = "index.htm"; //$NON-NLS-1$
		}
		String host = sourceuri.getHost();
		IPath path = null;
		if(host != null) {
			path = new Path(host);	
		}
		if(path == null) {
			path = new Path(uripath);
		}
		else {
			path = path.append(uripath);
		}
		return adjustPath(path);
	}
	
	/**
	 * Formats the given source into the given file handle
	 * @param handle
	 * @param source
	 * @return the file handle
	 * @throws CoreException
	 */
	static IFile doFormat(IFile handle, String source) throws CoreException {
		/*CodeFormatter formatter = ToolFactory.createCodeFormatter(null);
		TextEdit edit = formatter.format(CodeFormatter.K_JAVASCRIPT_UNIT, source, 0, source.length(), 0, null);
		String localsrc = source;
		if(edit != null) {
			Document doc = new Document(source);
			try {
				edit.apply(doc);
				localsrc = doc.get();
			} catch (Exception e) {
				JavaScriptDebugPlugin.log(e);
			} 
		}*/
		if(handle.isAccessible()) {
			handle.setContents(new ByteArrayInputStream(source.getBytes()), IResource.FORCE, null);
		}
		else {
			handle.create(new ByteArrayInputStream(source.getBytes()), true, null);
		}
		return handle;
	}
	
	/**
	 * Makes some sanity adjustments to the path prior to trying to create it.
	 * <ul>
	 * <li>make sure no one segment is super long, the current threshold is 15 characters per segment</li>
	 * <li>make sure no segment following the host part has '.js' in it. This prevents name collisions like:
	 * www.domain.org/scripts/myscript.js/eval/1.js and www.domain.org/scripts/myscript.js - where myscript.js will cause a 
	 * collision trying to create a file resource, as it could already exist as a folder and vice versa 
	 * </li>
	 * <li>if the script has no extension add .js to make sure Eclipse opens the correct editor for the content type</li>
	 * <ul>
	 * @param path
	 * @return the adjusted path
	 * @since 1.1
	 */
	static IPath adjustPath(IPath path) {
		String segment = null;
		ArrayList segments = new ArrayList(path.segmentCount());
		for (int i = 0; i < path.segments().length; i++) {
			segment = path.segment(i);
			if(i > 0) {
				if(segment.length() > 15) {
					segment = segment.substring(0, 1) + segment.charAt(segment.length()-1);
				}
				if(i < path.segments().length-1) {
					segment = segment.replaceAll("\\.js", "\\_js"); //$NON-NLS-1$ //$NON-NLS-2$
					segment = segment.replaceAll("\\.html", "\\_html"); //$NON-NLS-1$ //$NON-NLS-2$
					//segments can never end in '.' it is reserved on all platforms
					if(segment.endsWith(".")) { //$NON-NLS-1$
						segment = segment.substring(0, segment.length()-1) + '_';
					}
				}
			}
			segments.add(segment);
		}
		IPath newpath = new Path((String) segments.get(0));
		for (int i = 1; i < segments.size(); i++) {
			newpath = newpath.append((String) segments.get(i));
		}
		String ext = newpath.getFileExtension(); 
		if(ext == null && !Constants.JS_EXTENSION.equals(ext)) {
			newpath = newpath.addFileExtension(Constants.JS_EXTENSION);
		}
		return newpath; 
	}
}
