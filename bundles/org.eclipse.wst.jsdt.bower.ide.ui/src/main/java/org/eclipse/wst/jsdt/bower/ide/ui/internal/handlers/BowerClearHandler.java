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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.utils.I18n;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.utils.I18nKeys;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.utils.IBowerIdeUiConstants;
import org.eclipse.wst.jsdt.nodejs.core.api.utils.ILogger;

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
	protected IStatus doExecute(IProgressMonitor monitor) {
		IStatus status = Status.OK_STATUS;
		if (this.getBowerJson().isPresent()) {
			IFolder outputDirectory = this.getOutputDirectory();
			if (outputDirectory != null && outputDirectory.exists()) {
				if (!monitor.isCanceled()) {
					try {
						outputDirectory.delete(true, monitor);
					} catch (OperationCanceledException e) {
						// do not log
						status = Status.CANCEL_STATUS;
					} catch (CoreException e) {
						logger.log(IBowerIdeUiConstants.BOWER_IDE_UI_BUNDLE_ID, ILogger.ERROR, e);
					}
				} else {
					status = Status.CANCEL_STATUS;
				}
			}
		}
		return status;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.wst.jsdt.bower.ide.ui.internal.handlers.AbstractBowerHandler#getJobName()
	 */
	@Override
	protected String getJobName() {
		return I18n.getString(I18nKeys.BOWER_CLEAR_JOB_NAME);
	}
}
