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
package org.eclipse.wst.jsdt.nodejs.ide.ui.internal.projects;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.wst.jsdt.nodejs.core.api.projects.MEANProject;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.NodeJsIdeUiActivator;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.console.NpmConsole;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.utils.I18n;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.utils.I18nKeys;

/**
 * The wizard used to create a new MEAN project.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MEANProjectWizard extends Wizard implements IWorkbenchWizard, IExecutableExtension {
	/**
	 * The workbench.
	 */
	private IWorkbench workbench;

	/**
	 * The first page of the wizard.
	 */
	private MEANProjectWizardPage meanProjectWizardPage;

	/**
	 * The constructor.
	 */
	public MEANProjectWizard() {
		this.setNeedsProgressMonitor(true);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench iWorkbench, IStructuredSelection selection) {
		this.workbench = iWorkbench;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement,
	 *      java.lang.String, java.lang.Object)
	 */
	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		this.meanProjectWizardPage = new MEANProjectWizardPage();
		super.addPage(this.meanProjectWizardPage);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		try {
			IWizardContainer iWizardContainer = this.getContainer();

			IRunnableWithProgress projectCreation = new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) {
					MEANProjectWizard.this.createProject(monitor);
				}
			};
			iWizardContainer.run(false, false, projectCreation);

			// Update the perspective.
			return true;
		} catch (InvocationTargetException e) {
			NodeJsIdeUiActivator.log(e, true);
		} catch (InterruptedException e) {
			NodeJsIdeUiActivator.log(e, true);
		}
		return false;
	}

	/**
	 * Creates the project.
	 *
	 * @param monitor
	 *            The progress monitor
	 */
	private void createProject(IProgressMonitor monitor) {
		try {
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(
					this.meanProjectWizardPage.getProjectName());
			IPath location = this.meanProjectWizardPage.getLocationPath();
			if (!project.exists()) {
				monitor.beginTask(I18n.getString(I18nKeys.MEAN_PROJECT_WIZARD_MONITOR_NEW_MEAN_PROJECT), 100);

				IProjectDescription desc = project.getWorkspace().newProjectDescription(
						this.meanProjectWizardPage.getProjectName());
				if (ResourcesPlugin.getWorkspace().getRoot().getLocation().equals(location)) {
					location = null;
				}
				desc.setLocation(location);
				project.create(desc, new SubProgressMonitor(monitor, 5));
				project.open(new SubProgressMonitor(monitor, 5));

				monitor.subTask(I18n.getString(I18nKeys.MEAN_PROJECT_WIZARD_MONITOR_INITIALIZING_DATA));

				MEANProject meanProject = new MEANProject(project.getLocation().toFile(), project.getName(),
						"1.0.0");
				meanProject.initialize();
				monitor.worked(20);

				monitor.subTask(I18n.getString(I18nKeys.MEAN_PROJECT_WIZARD_MONITOR_NPM_INSTALL));
				NpmConsole npmConsole = new NpmConsole(project.getLocation().toFile());
				npmConsole.call("install");

				monitor.worked(65);

				project.refreshLocal(IResource.DEPTH_INFINITE, new SubProgressMonitor(monitor, 5));

				monitor.done();

				IWorkingSet[] workingSets = this.meanProjectWizardPage.getSelectedWorkingSets();
				this.workbench.getWorkingSetManager().addToWorkingSets(project, workingSets);
			}
		} catch (CoreException e) {
			NodeJsIdeUiActivator.log(e, true);
		}
	}
}
