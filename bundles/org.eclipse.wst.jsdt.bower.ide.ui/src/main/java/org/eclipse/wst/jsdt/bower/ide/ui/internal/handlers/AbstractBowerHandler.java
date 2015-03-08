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

import com.google.common.base.Optional;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.bower.core.api.BowerJson;
import org.eclipse.wst.jsdt.bower.core.api.BowerRc;
import org.eclipse.wst.jsdt.bower.core.api.BowerReaders;
import org.eclipse.wst.jsdt.bower.core.api.utils.IBowerConstants;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.BowerUiPlugin;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.utils.IBowerIdeUiConstants;
import org.eclipse.wst.jsdt.nodejs.core.api.utils.ILogger;

/**
 * Common superclass of the handlers that need the content of the bower.json file.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public abstract class AbstractBowerHandler extends AbstractHandler {

	/**
	 * The logger.
	 */
	protected ILogger logger = BowerUiPlugin.getInstance().getLogger();

	/**
	 * The content of bower.json.
	 */
	private Optional<BowerJson> bowerJson = Optional.absent();

	/**
	 * The content of .bowerrc.
	 */
	private Optional<BowerRc> bowerRc = Optional.absent();

	/**
	 * The output directory.
	 */
	private IFolder outputDirectory;

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService()
				.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection iStructuredSelection = (IStructuredSelection)selection;
			Object firstElement = iStructuredSelection.getFirstElement();
			if (firstElement instanceof IFile
					&& IBowerConstants.BOWER_JSON.equals(((IFile)firstElement).getName())
					&& ((IFile)firstElement).exists()) {
				final IFile bowerJsonFile = (IFile)firstElement;
				IFile bowerRcFile = bowerJsonFile.getParent().getFile(new Path(IBowerConstants.BOWER_RC));

				try {
					String directory = IBowerConstants.BOWER_COMPONENTS;

					this.bowerJson = Optional.fromNullable(BowerReaders.getBowerJson(bowerJsonFile
							.getContents(), logger));
					if (bowerRcFile.exists()) {
						this.bowerRc = Optional.fromNullable(BowerReaders.getBowerRc(bowerRcFile
								.getContents(), logger));
					}

					if (this.bowerRc.isPresent()) {
						directory = this.bowerRc.get().getDirectory();
					}

					this.outputDirectory = bowerJsonFile.getParent().getFolder(new Path(directory));
				} catch (CoreException e) {
					logger.log(IBowerIdeUiConstants.BOWER_IDE_UI_BUNDLE_ID, ILogger.ERROR, e);
				}

				Job job = new Job(this.getJobName()) {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						IStatus status = AbstractBowerHandler.this.doExecute(monitor);
						try {
							bowerJsonFile.getProject().refreshLocal(IResource.DEPTH_INFINITE,
									new NullProgressMonitor());
						} catch (CoreException e) {
							logger.log(IBowerIdeUiConstants.BOWER_IDE_UI_BUNDLE_ID, ILogger.ERROR, e);
						}
						return status;
					}
				};
				job.setUser(true);
				job.schedule();
			}

		}
		return null;
	}

	/**
	 * Returns the name of the job to display to the user.
	 *
	 * @return The name of the job
	 */
	protected abstract String getJobName();

	/**
	 * Execute the operation.
	 *
	 * @param monitor
	 *            The progress monitor
	 * @return The status of the execution
	 */
	protected abstract IStatus doExecute(IProgressMonitor monitor);

	/**
	 * Returns the bowerJson.
	 *
	 * @return The bowerJson
	 */
	public Optional<BowerJson> getBowerJson() {
		return this.bowerJson;
	}

	/**
	 * Returns the bowerRc.
	 *
	 * @return The bowerRc
	 */
	public Optional<BowerRc> getBowerRc() {
		return this.bowerRc;
	}

	/**
	 * Returns the outputDirectory.
	 *
	 * @return The outputDirectory
	 */
	public IFolder getOutputDirectory() {
		return this.outputDirectory;
	}
}
