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

import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.jsdt.bower.core.api.Bower;
import org.eclipse.wst.jsdt.bower.core.api.BowerJson;
import org.eclipse.wst.jsdt.bower.ide.api.EclipseGitProgressTransformer;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.BowerUiPlugin;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.preferences.IPreferenceConstants;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.utils.I18n;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.utils.I18nKeys;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.utils.IBowerIdeUiConstants;
import org.eclipse.wst.jsdt.nodejs.core.api.utils.ILogger;

/**
 * This handler will update the bower dependencies of the project.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class BowerUpdateHandler extends AbstractBowerHandler {

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
				try {
					outputDirectory.delete(true, monitor);
				} catch (CoreException e) {
					logger.log(IBowerIdeUiConstants.BOWER_IDE_UI_BUNDLE_ID, ILogger.ERROR, e);
				}

				BowerJson bowerJson = this.getBowerJson().get();
				File directory = outputDirectory.getLocation().toFile();

				String serverUrl = BowerUiPlugin.getInstance().getPreferenceStore().getString(
						IPreferenceConstants.BOWER_SERVER_URL);

				if (!monitor.isCanceled()) {
					Bower.install().setMonitor(new EclipseGitProgressTransformer(monitor))
					.setOutputDirectory(directory).setBowerJson(bowerJson).setBowerServerURL(
							serverUrl).call();
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
		return I18n.getString(I18nKeys.BOWER_UPDATE_JOB_NAME);
	}

}
