/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.sourcelookup.AbstractSourceLookupParticipant;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptStackFrame;
import org.eclipse.wst.jsdt.debug.core.model.IScript;
import org.eclipse.wst.jsdt.debug.internal.core.JavaScriptDebugPlugin;
import org.eclipse.wst.jsdt.debug.internal.core.model.JavaScriptDebugTarget;
import org.eclipse.wst.jsdt.debug.internal.core.model.JavaScriptStackFrame;
import org.eclipse.wst.jsdt.debug.internal.core.model.Script;

/**
 * Default source lookup participant
 * 
 * @since 1.0
 */
public class JavaScriptSourceLookupParticipant extends AbstractSourceLookupParticipant {

	static final Object[] NO_SOURCE = new Object[0];

	private HashMap sourcemap = new HashMap();
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.sourcelookup.ISourceLookupParticipant#getSourceName(java.lang.Object)
	 */
	public String getSourceName(Object object) throws CoreException {
		return SourceLookup.getSourceName(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.sourcelookup.AbstractSourceLookupParticipant#findSourceElements(java.lang.Object)
	 */
	public Object[] findSourceElements(Object object) throws CoreException {
		IFile file = resolveFile(object);
		if(file != null && file.exists()) {
			return new IFile[] {file};
		}
		ISourceContainer[] containers = getSourceContainers();
		if(containers != null && containers.length > 0) {
			String name = getSourceName(object);
			if(name != null) {
				Object[] sources = null;
				for (int i = 0; i < containers.length; i++) {
					sources = containers[i].findSourceElements(name);
					if(sources != null && sources.length > 0) {
						return sources;
					}
				}
			}
			//did not find anything in the source look up path, create the source
			//in the external source project and show it
			URI sourceURI = SourceLookup.getSourceURI(object);
			if (sourceURI != null) {
				file = (IFile) sourcemap.get(sourceURI);
				if(file != null && file.exists()) {
					return new IFile[] { file };
				}
				return showExternalSource(sourceURI, object);
			}
		}
		return NO_SOURCE;
	}

	/**
	 * Tries to resolve the workspace local {@link IFile}(s) that match the underlying {@link ScriptReference}
	 * for either an {@link IJavaScriptStackFrame} or an {@link IScript}
	 * 
	 * @param object the object to try and resolve the {@link IFile} from
	 * @return the corresponding {@link IFile} for the object or <code>null</code> if one could not be determined
	 * @since 1.4
	 */
	IFile resolveFile(Object object) {
		String uri = null;
		JavaScriptDebugTarget target = null;
		if(object instanceof JavaScriptStackFrame) {
			JavaScriptStackFrame frame = (JavaScriptStackFrame) object;
			uri = frame.getSourcePath();
			target = (JavaScriptDebugTarget) frame.getDebugTarget();
		}
		if(object instanceof Script) {
			Script script = (Script) object;
			uri = script.sourceURI().toString();
			target = (JavaScriptDebugTarget) script.getDebugTarget();
		}
		if(uri != null && target != null) {
			List scripts = target.getVM().allScripts();
			for (Iterator i = scripts.iterator(); i.hasNext();) {
				ScriptReference ref = (ScriptReference) i.next();
				if(uri.equals(ref.sourceURI().toString())) {
					return JavaScriptDebugPlugin.getResolutionManager().getFile(ref);
				}
			}
		}
		return null;
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
				sourcemap.put(sourceuri, file);
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