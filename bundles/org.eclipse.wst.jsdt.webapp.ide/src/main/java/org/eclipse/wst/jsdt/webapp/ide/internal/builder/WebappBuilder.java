/*******************************************************************************
 * Copyright (c) 2014 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.webapp.ide.internal.builder;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * The webapp builder.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class WebappBuilder extends IncrementalProjectBuilder {
	/**
	 * The identifier of the builder.
	 */
	public static String BUILDER_ID = "org.eclipse.wst.jsdt.webapp.ide.builder"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.resources.IncrementalProjectBuilder#build(int, java.util.Map,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor)
			throws CoreException {
		switch (kind) {
			case CLEAN_BUILD:
				this.clean(monitor);
				break;
			case AUTO_BUILD:
				this.incrementalBuild(monitor);
				break;
			case INCREMENTAL_BUILD:
				this.incrementalBuild(monitor);
				break;
			case FULL_BUILD:
				this.fullBuild(monitor);
				break;
			default:
				this.fullBuild(monitor);
				break;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.resources.IncrementalProjectBuilder#clean(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		super.clean(monitor);
	}

	/**
	 * Launches an incremental build of the project.
	 *
	 * @param monitor
	 *            The progress monitor
	 */
	private void incrementalBuild(IProgressMonitor monitor) {
		System.out.println("WebappBuilder.incrementalBuild()");
	}

	/**
	 * Launches a full build of the project.
	 *
	 * @param monitor
	 *            The progress monitor
	 */
	private void fullBuild(IProgressMonitor monitor) {
		System.out.println("WebappBuilder.fullBuild()");
	}

}
