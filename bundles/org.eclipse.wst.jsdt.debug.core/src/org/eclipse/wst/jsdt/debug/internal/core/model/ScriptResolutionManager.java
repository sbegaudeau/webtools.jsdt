/*******************************************************************************
 * Copyright (c) 2011, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.core.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference;
import org.eclipse.wst.jsdt.debug.core.model.IScriptResolver;
import org.eclipse.wst.jsdt.debug.internal.core.Constants;
import org.eclipse.wst.jsdt.debug.internal.core.JavaScriptDebugPlugin;
import org.eclipse.wst.jsdt.debug.internal.core.Messages;
import org.eclipse.wst.jsdt.debug.internal.core.launching.SourceLookup;

/**
 * Handles the collection of {@link ScriptResolverExtension}s and provide useful utilities for path / script handling
 * @since 3.4
 */
public final class ScriptResolutionManager {

	static IScriptResolver[] NO_RESOLVERS = new IScriptResolver[0];
	ListenerList resolvers = null;
	
	/**
	 * This is a convenience method that consults all of the registered {@link IScriptResolver}s.
	 * <br><br>
	 * This method will return <code>true</code> iff any one (or all) of the {@link IScriptResolver}s returns <code>true</code> or
	 * the default matching algorithm returns <code>true</code> - which is consulted after all extensions have been asked.
	 * @param script the script to check
	 * @param path the path to compare against
	 * @return <code>true</code> if any one (or all) of the {@link IScriptResolver}s return <code>true</code>, <code>false</code> otherwise
	 */
	public boolean matches(ScriptReference script, IPath path) {
		IScriptResolver[] res = getResolvers();
		for (int i = 0; i < res.length; i++) {
			if(res[i].matches(script, path)) {
				return true;
			}
		}
		//no extensions matched, fall-back to the old way
		return isMatch(script, path);
	}
	
	boolean isMatch(ScriptReference script, IPath path) {
		if(guessScriptMatches(script, path)) {
			return true;
		}
		//no luck, try an exact match
		URI sourceURI = script.sourceURI();
		if (URIUtil.isFileURI(sourceURI)) {
			IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
			URI workspaceURI = workspaceRoot.getRawLocationURI();			
			sourceURI = workspaceURI.relativize(sourceURI);
		}
		IPath spath = path;
		if(spath.segmentCount() > 0 && spath.segment(0).equals(Messages.external_javascript_source)) {
			spath = spath.removeFirstSegments(1).makeAbsolute();
		}
		IPath uripath = SourceLookup.getSourcePath(sourceURI);
		if(uripath != null) {
			uripath = uripath.makeAbsolute();
		}
		return spath.equals(uripath);
	}

	/**
	 * Guesses if the paths are considered equal by walking backward from the last segment of the paths and counting the matching segments. 
	 * The paths are guessed to be equal iff any two or more segments match in order.
	 * 
	 * @param script the {@link ScriptReference}
	 * @param path the path to compare
	 * @return <code>true</code> if the paths 'match', <code>false</code> otherwise
	 */
	boolean guessScriptMatches(ScriptReference script, IPath path) {
		IPath newpath = path.makeAbsolute();
		IPath uri = new Path(script.sourceURI().getPath());
		if(SourceLookup.TOP_LEVEL_PATH.equals(newpath) && SourceLookup.TOP_LEVEL_PATH.equals(uri)) {
			return true;
		}
		uri = uri.makeAbsolute();
		int matched_segments = 0;
		int last = uri.segmentCount()-1;
		for(int i  = newpath.segmentCount()-1; i > -1; i--) {
			if(last < 0) {
				break;
			}
			if(newpath.segment(i).equals(uri.segment(last))) {
				matched_segments++;
				last--;
			}
		}
		return matched_segments > 1;
	}
	
	/**
	 * Convenience method to consult all of the registered {@link IScriptResolver}s for the matching {@link IFile}.
	 * <br><br>
	 * This method will vote on the file to return, and return the {@link IFile} that has the most votes as follows:
	 * <ul>
	 * <li>If there is one contributor and it returns an {@link IFile} that one is chosen</li>
	 * <li>If there is &gt; one contributor the greatest number of matching {@link IFile}s returned is chosen. If there is a tie, the {@link IFile}
	 * that matches the one selected from the default algorithm will be chosen. In the event the default algorithm returns <code>null</code> the first {@link IFile}
	 * will be selected.</li>
	 * <li>If there are no contributors the default algorithm is consulted</li>
	 * </ul>
	 * @param script
	 * @return the matching {@link IFile} or <code>null</code>
	 */
	public IFile getFile(ScriptReference script) {
		IScriptResolver[] resolvers = getResolvers();
		ArrayList files = new ArrayList();
		IFile file = null;
		for (int i = 0; i < resolvers.length; i++) {
			file = resolvers[i].getFile(script);
			if(file != null) {
				if(files.contains(file)) {
					return file;
				}
				files.add(file);
			}
		}
		file = null;
		IPath p = SourceLookup.getSourcePath(script.sourceURI());
		IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(p.makeAbsolute());
		if(res == null) {
			IProject pj =JavaScriptDebugPlugin.getExternalSourceProject(true);
			res = pj.findMember(p);
		}
		if(res != null && res.getType() == IResource.FILE) {
			file = (IFile) res;
		}
		if(files.size() > 0) {
			if(files.contains(file)) {
				return file;
			}
			return (IFile) files.get(0);
		}
		return file;
	}
	
	/**
	 * Returns the complete listing of {@link IScriptResolver}s or an empty array, never <code>null</code>
	 * 
	 * @return the complete listing of {@link IScriptResolver}s
	 */
	public IScriptResolver[] getResolvers() {
		loadResolvers();
		if(resolvers.size() < 1) {
			return NO_RESOLVERS;
		}
		List res = Arrays.asList(resolvers.getListeners());
		return (IScriptResolver[]) res.toArray(new IScriptResolver[res.size()]);
	}
	
	/**
	 * load up all the extension points to the delegate listeners
	 */
	void loadResolvers() {
		if(resolvers == null) {
			resolvers = new ListenerList();
			IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(JavaScriptDebugPlugin.PLUGIN_ID, Constants.SCRIPT_RESOLVERS);
			IConfigurationElement[] elements = extensionPoint.getConfigurationElements();
			for (int i = 0; i < elements.length; i++) {
				resolvers.add(new ScriptResolverExtension(elements[i]));
			}
		}
	}
	
	/**
	 * Clean up
	 */
	public void dispose() {
		if(resolvers != null) {
			resolvers.clear();
			resolvers = null;
		}
	}
}
