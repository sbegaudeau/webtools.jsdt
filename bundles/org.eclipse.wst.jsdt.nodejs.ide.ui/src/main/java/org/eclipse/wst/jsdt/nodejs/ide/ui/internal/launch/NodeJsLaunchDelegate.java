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
package org.eclipse.wst.jsdt.nodejs.ide.ui.internal.launch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.RuntimeProcess;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.NodeJsIdeUiActivator;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.preferences.INodeJsPreferenceConstants;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.utils.I18n;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.utils.I18nKeys;

/**
 * The launch configuration.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class NodeJsLaunchDelegate implements ILaunchConfigurationDelegate2 {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate2#getLaunch(org.eclipse.debug.core.ILaunchConfiguration,
	 *      java.lang.String)
	 */
	@Override
	public ILaunch getLaunch(ILaunchConfiguration configuration, String mode) throws CoreException {
		return new Launch(configuration, mode, null);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate2#buildForLaunch(org.eclipse.debug.core.ILaunchConfiguration,
	 *      java.lang.String, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public boolean buildForLaunch(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		return false;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate2#preLaunchCheck(org.eclipse.debug.core.ILaunchConfiguration,
	 *      java.lang.String, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public boolean preLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		return true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate2#finalLaunchCheck(org.eclipse.debug.core.ILaunchConfiguration,
	 *      java.lang.String, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public boolean finalLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		return configuration.hasAttribute(INodeJsLaunchConfigurationAttributes.APPLICATION_PATH);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org.eclipse.debug.core.ILaunchConfiguration,
	 *      java.lang.String, org.eclipse.debug.core.ILaunch, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch,
			IProgressMonitor monitor) throws CoreException {
		if (launch != null) {
			Map<String, String> attributes = new HashMap<String, String>();

			IPreferenceStore preferenceStore = NodeJsIdeUiActivator.getInstance().getPreferenceStore();
			String nodeJsLocation = preferenceStore.getString(INodeJsPreferenceConstants.NODE_LOCATION);
			String applicationPath = configuration.getAttribute(
					INodeJsLaunchConfigurationAttributes.APPLICATION_PATH, ""); //$NON-NLS-1$

			if (nodeJsLocation == null || nodeJsLocation.length() == 0) {
				IStatus status = new Status(IStatus.ERROR, NodeJsIdeUiActivator.PLUGIN_ID, I18n
						.getString(I18nKeys.NODEJS_LAUNCH_MISSING_NODEJS_LOCATION));
				throw new CoreException(status);
			}
			if (applicationPath == null || applicationPath.length() == 0) {
				IStatus status = new Status(IStatus.ERROR, NodeJsIdeUiActivator.PLUGIN_ID, I18n
						.getString(I18nKeys.NODEJS_LAUNCH_MISSING_APPLICATION_LOCATION));
				throw new CoreException(status);
			}

			ProcessBuilder processBuilder = new ProcessBuilder(nodeJsLocation, applicationPath);
			processBuilder.redirectErrorStream(true);
			try {
				Process nodeJsProcess = processBuilder.start();
				IProcess process = new RuntimeProcess(launch, nodeJsProcess, mode, attributes);
				launch.addProcess(process);
			} catch (IOException e) {
				NodeJsIdeUiActivator.log(e, true);
			}
		}
	}

}
