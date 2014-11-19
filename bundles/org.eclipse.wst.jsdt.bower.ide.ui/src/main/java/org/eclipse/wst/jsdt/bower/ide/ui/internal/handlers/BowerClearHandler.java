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
package org.eclipse.wst.jsdt.bower.ide.ui.internal.handlers;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * This handler will clear the content of the bower install folder.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class BowerClearHandler extends AbstractBowerHandler {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.wst.jsdt.bower.ide.ui.internal.handlers.AbstractBowerHandler#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void doExecute(IProgressMonitor monitor) {
		if (this.getBowerJson().isPresent()) {
			IFolder outputDirectory = this.getOutputDirectory();
			if (outputDirectory != null && outputDirectory.exists()) {
				try {
					outputDirectory.delete(true, monitor);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
