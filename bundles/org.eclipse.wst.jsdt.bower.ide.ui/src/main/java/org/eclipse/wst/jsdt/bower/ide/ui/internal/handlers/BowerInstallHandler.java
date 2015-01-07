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
import org.eclipse.wst.jsdt.bower.core.api.Bower;
import org.eclipse.wst.jsdt.bower.core.api.BowerJson;
import org.eclipse.wst.jsdt.bower.ide.api.EclipseGitProgressTransformer;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.BowerUiPlugin;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.preferences.IPreferenceConstants;

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
	protected void doExecute(IProgressMonitor monitor) {
		if (this.getBowerJson().isPresent()) {
			BowerJson bowerJson = this.getBowerJson().get();
			File outputDirectory = this.getOutputDirectory().getLocation().toFile();

			String serverUrl = BowerUiPlugin.getInstance().getPreferenceStore().getString(
					IPreferenceConstants.BOWER_SERVER_URL);

			Bower.install().setMonitor(new EclipseGitProgressTransformer(monitor)).setOutputDirectory(
					outputDirectory).setBowerJson(bowerJson).setBowerServerURL(serverUrl).call();
		}
	}
}
