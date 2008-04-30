/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.launching;


import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.jsdt.core.IAccessRule;
import org.eclipse.wst.jsdt.core.IIncludePathAttribute;
import org.eclipse.wst.jsdt.core.IJsGlobalScopeContainer;
import org.eclipse.wst.jsdt.core.IIncludePathEntry;
import org.eclipse.wst.jsdt.core.JavaScriptCore;

import com.ibm.icu.text.MessageFormat;

/** 
 * JRE Container - resolves a classpath container variable to a JRE
 */
public class JREContainer implements IJsGlobalScopeContainer {

	/**
	 * Corresponding JRE
	 */
	private IVMInstall fVMInstall = null;
	
	/**
	 * Container path used to resolve to this JRE
	 */
	private IPath fPath = null;
	
	/**
	 * Cache of classpath entries per VM install. Cleared when a VM changes.
	 */
	private static Map fgClasspathEntries = null;
	
	private static IAccessRule[] EMPTY_RULES = new IAccessRule[0];
	
	/**
	 * Returns the classpath entries associated with the given VM.
	 * 
	 * @param vm
	 * @return classpath entries
	 */
	private static IIncludePathEntry[] getClasspathEntries(IVMInstall vm) {
		if (fgClasspathEntries == null) {
			fgClasspathEntries = new HashMap(10);
			// add a listener to clear cached value when a VM changes or is removed
//			IVMInstallChangedListener listener = new IVMInstallChangedListener() {
//				public void defaultVMInstallChanged(IVMInstall previous, IVMInstall current) {
//				}
//
//				public void vmChanged(PropertyChangeEvent event) {
//					if (event.getSource() != null) {
//						fgClasspathEntries.remove(event.getSource());
//					}
//				}
//
//				public void vmAdded(IVMInstall newVm) {
//				}
//
//				public void vmRemoved(IVMInstall removedVm) {
//					fgClasspathEntries.remove(removedVm);
//				}
//			}; 
//			JavaRuntime.addVMInstallChangedListener(listener);
		}
		IIncludePathEntry[] entries = (IIncludePathEntry[])fgClasspathEntries.get(vm);
		if (entries == null) {
			entries = computeClasspathEntries(vm);
			fgClasspathEntries.put(vm, entries);
		}
		return entries;
	}
	
	/**
	 * Computes the classpath entries associated with a VM - one entry per library.
	 * 
	 * @param vm
	 * @return classpath entries
	 */
	private static IIncludePathEntry[] computeClasspathEntries(IVMInstall vm) {
		LibraryLocation[] libs = vm.getLibraryLocations();
		boolean overrideJavaDoc = false;
		if (libs == null) {
			libs = JavaRuntime.getLibraryLocations(vm);
			overrideJavaDoc = true;
		}
		List entries = new ArrayList(libs.length);
		for (int i = 0; i < libs.length; i++) {
			if (!libs[i].getSystemLibraryPath().isEmpty()) {
				IPath sourcePath = libs[i].getSystemLibrarySourcePath();
				if (sourcePath.isEmpty()) {
					sourcePath = null;
				}
				IPath rootPath = libs[i].getPackageRootPath();
				if (rootPath.isEmpty()) {
					rootPath = null;
				}
				URL javadocLocation = libs[i].getJavadocLocation();
				if (overrideJavaDoc && javadocLocation == null) {
					javadocLocation = vm.getJavadocLocation();
				}
				IIncludePathAttribute[] attributes = null;
				if (javadocLocation == null) {
					attributes = new IIncludePathAttribute[0];
				} else {
					attributes = new IIncludePathAttribute[]{JavaScriptCore.newIncludepathAttribute(IIncludePathAttribute.JSDOC_LOCATION_ATTRIBUTE_NAME, javadocLocation.toExternalForm())};
				}
				entries.add(JavaScriptCore.newLibraryEntry(libs[i].getSystemLibraryPath(), sourcePath, rootPath, EMPTY_RULES, attributes, false));
			}
		}
		return (IIncludePathEntry[])entries.toArray(new IIncludePathEntry[entries.size()]);		
	}
	
	/**
	 * Constructs a JRE classpath conatiner on the given VM install
	 * 
	 * @param vm vm install - cannot be <code>null</code>
	 * @param path container path used to resolve this JRE
	 */
	public JREContainer(IVMInstall vm, IPath path) {
		fVMInstall = vm;
		fPath = path;
	}
	
	/**
	 * @see IJsGlobalScopeContainer#getClasspathEntries()
	 * @deprecated Use {@link #getIncludepathEntries()} instead
	 */
	public IIncludePathEntry[] getClasspathEntries() {
		return getIncludepathEntries();
	}

	/**
	 * @see IJsGlobalScopeContainer#getIncludepathEntries()
	 */
	public IIncludePathEntry[] getIncludepathEntries() {
		return getClasspathEntries(fVMInstall);
	}

	/**
	 * @see IJsGlobalScopeContainer#getDescription()
	 */
	public String getDescription() {
//		String environmentId = JavaRuntime.getExecutionEnvironmentId(getPath());
//		String tag = null;
//		if (environmentId == null) {
//			tag = fVMInstall.getName();
//		} else {
//			tag = environmentId;
//		}
		return MessageFormat.format(LaunchingMessages.JREContainer_JRE_System_Library_1, new String[]{});
	}

	/**
	 * @see IJsGlobalScopeContainer#getKind()
	 */
	public int getKind() {
		return IJsGlobalScopeContainer.K_DEFAULT_SYSTEM;
	}

	/**
	 * @see IJsGlobalScopeContainer#getPath()
	 */
	public IPath getPath() {
		return fPath;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.IJsGlobalScopeContainer#resolvedLibraryImport(java.lang.String)
	 */
	public String[] resolvedLibraryImport(String a) {
		
		return null;
	}

}
