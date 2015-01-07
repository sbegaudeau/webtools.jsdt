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

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.NodeJsIdeUiActivator;

/**
 * Launches the selected node.js application.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class NodeJsLaunchShortcut implements ILaunchShortcut {

	/**
	 * The launch configuration type.
	 */
	private static final String LAUNCH_CONFIGURATION_TYPE = "org.eclipse.wst.jsdt.nodejs.ide.ui.launchconfigurationtype.nodejs"; //$NON-NLS-1$

	/**
	 * Node.js application to launch.
	 */
	private IFile file;

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.jface.viewers.ISelection,
	 *      java.lang.String)
	 */
	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			List<?> list = ((IStructuredSelection)selection).toList();
			for (Object object : list) {
				if (object instanceof IFile && ((IFile)object).getFileExtension() != null
						&& "js".equals(((IFile)object).getFileExtension())) { //$NON-NLS-1$
					this.file = (IFile)object;
					break;
				}
			}
		}

		if (this.file != null) {
			launchApplication(mode);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.ui.IEditorPart, java.lang.String)
	 */
	@Override
	public void launch(IEditorPart editor, String mode) {
		IEditorInput input = editor.getEditorInput();
		IFile iFile = (IFile)input.getAdapter(IFile.class);
		if (iFile != null && iFile.getFileExtension() != null && "js".equals(iFile.getFileExtension())) { //$NON-NLS-1$
			this.file = iFile;
		}

		if (this.file != null) {
			launchApplication(mode);
		}
	}

	/**
	 * Launches the application.
	 *
	 * @param mode
	 *            The application mode
	 */
	private void launchApplication(String mode) {
		ILaunchConfiguration launchConfiguration = this.findLaunchConfiguration();
		if (launchConfiguration == null) {
			launchConfiguration = this.createConfiguration();
		}
	}

	/**
	 * Returns a newly created launch configuration for the available ".js" file.
	 *
	 * @return A newly created launch configuration for the available ".js" file.
	 */
	protected ILaunchConfiguration createConfiguration() {
		ILaunchConfiguration config = null;
		ILaunchConfigurationWorkingCopy wc = null;
		try {
			String applicationPath = this.file.getLocation().toString();

			ILaunchConfigurationType configType = DebugPlugin.getDefault().getLaunchManager()
					.getLaunchConfigurationType(LAUNCH_CONFIGURATION_TYPE);
			wc = configType.newInstance(null, DebugPlugin.getDefault().getLaunchManager()
					.generateLaunchConfigurationName(this.file.getName()));
			wc.setAttribute(INodeJsLaunchConfigurationAttributes.APPLICATION_PATH, applicationPath);
			wc.setMappedResources(new IResource[] {this.file });
			config = wc.doSave();

			IStructuredSelection selection;
			if (config == null) {
				selection = new StructuredSelection();
			} else {
				selection = new StructuredSelection(config);
			}
			DebugUITools.openLaunchConfigurationDialogOnGroup(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), selection, IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
		} catch (CoreException e) {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if (window != null) {
				MessageDialog.openError(window.getShell(), "", e.getStatus().getMessage()); //$NON-NLS-1$
			} else {
				NodeJsIdeUiActivator.log(e, true);
			}
		}
		return config;
	}

	/**
	 * Returns the first launch configuration using all the selected ".js" file.
	 *
	 * @return The first launch configuration using all the selected ".js" file.
	 */
	protected ILaunchConfiguration findLaunchConfiguration() {
		String computedApplicationPath = this.file.getLocation().toString();

		ILaunchConfigurationType configurationType = DebugPlugin.getDefault().getLaunchManager()
				.getLaunchConfigurationType(LAUNCH_CONFIGURATION_TYPE);
		try {
			ILaunchConfiguration[] launchConfigurations = DebugPlugin.getDefault().getLaunchManager()
					.getLaunchConfigurations(configurationType);
			for (ILaunchConfiguration iLaunchConfiguration : launchConfigurations) {
				String applicationPath = iLaunchConfiguration.getAttribute(
						INodeJsLaunchConfigurationAttributes.APPLICATION_PATH, ""); //$NON-NLS-1$

				if (applicationPath != null && applicationPath.equals(computedApplicationPath)) {
					return iLaunchConfiguration;
				}
			}
		} catch (CoreException e) {
			NodeJsIdeUiActivator.log(e, true);
		}
		return null;
	}

}
