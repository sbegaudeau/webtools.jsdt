/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.model;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.jsdt.core.JsGlobalScopeContainerInitializer;
import org.eclipse.wst.jsdt.core.IJsGlobalScopeContainer;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.compiler.libraries.LibraryLocation;

public class ContainerInitializer extends JsGlobalScopeContainerInitializer {
	public static ITestInitializer initializer;
	
	public static interface ITestInitializer {
		public void initialize(IPath containerPath, IJavaScriptProject project) throws CoreException;
		public boolean allowFailureContainer();
	}
	
	public static void setInitializer(ITestInitializer initializer) {
		ContainerInitializer.initializer = initializer;
	}
	
	public IJsGlobalScopeContainer getFailureContainer(IPath containerPath, IJavaScriptProject project) {
		if (initializer == null || !initializer.allowFailureContainer()) return null;
		return super.getFailureContainer(containerPath, project);
	}
	
	public void initialize(IPath containerPath, IJavaScriptProject project) throws CoreException {
		if (initializer == null) return;
		initializer.initialize(containerPath, project);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.IJsGlobalScopeContainerInitialzer#getLibraryLocation()
	 */
	public LibraryLocation getLibraryLocation() {
		return null;
	}
}
