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

/**
 * This handler will install all the bower dependencies of the currently selected bower.json file.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class BowerInstallHandler extends AbstractBowerHandler {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.wst.jsdt.bower.ide.ui.internal.handlers.AbstractBowerHandler#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus doExecute(IProgressMonitor monitor) {
		IStatus status = Status.OK_STATUS;
		if (this.getBowerJson().isPresent()) {
			BowerJson bowerJson = this.getBowerJson().get();
			File outputDirectory = this.getOutputDirectory().getLocation().toFile();

			String serverUrl = BowerUiPlugin.getInstance().getPreferenceStore().getString(
					IPreferenceConstants.BOWER_SERVER_URL);

			if (!monitor.isCanceled()) {
				Bower.install().setMonitor(new EclipseGitProgressTransformer(monitor)).setOutputDirectory(
						outputDirectory).setBowerJson(bowerJson).setBowerServerURL(serverUrl).call();
			} else {
				status = Status.CANCEL_STATUS;
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
		return I18n.getString(I18nKeys.BOWER_INSTALL_JOB_NAME);
	}
}
