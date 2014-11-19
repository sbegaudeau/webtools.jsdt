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

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.jsdt.bower.core.api.BowerJson;
import org.eclipse.wst.jsdt.bower.core.api.BowerRc;
import org.eclipse.wst.jsdt.bower.core.api.utils.IBowerConstants;

/**
 * This handler will initialize the files bower.json and .bowerrc.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class BowerInitHandler extends AbstractHandler {

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
			if (firstElement instanceof IContainer) {
				IContainer container = (IContainer)firstElement;
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().run(false, true,
							this.getRunnable(container));

				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					try {
						container.getProject().refreshLocal(IResource.DEPTH_INFINITE,
								new NullProgressMonitor());
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns the runnable which will realize the operation.
	 *
	 * @param container
	 *            The container in which we will create the file bower.json
	 * @return The runnable which will realize the operation
	 */
	private IRunnableWithProgress getRunnable(final IContainer container) {
		WorkspaceModifyOperation workspaceModifyOperation = new WorkspaceModifyOperation() {

			@Override
			protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException,
			InterruptedException {
				IFile bowerJsonFile = container.getFile(new Path(IBowerConstants.BOWER_JSON));
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				if (!bowerJsonFile.exists()) {
					BowerJson bowerJson = new BowerJson();
					bowerJson.setName(bowerJsonFile.getProject().getName());
					bowerJson.setVersion("0.0.1"); //$NON-NLS-1$
					bowerJson.setPrivate(true);
					bowerJson.setDependencies(Maps.<String, String> newHashMap());
					bowerJson.setDevDependencies(Maps.<String, String> newHashMap());

					byte[] bytes = gson.toJson(bowerJson).getBytes();
					InputStream inputStream = new ByteArrayInputStream(bytes);
					bowerJsonFile.create(inputStream, true, monitor);
				}

				IFile bowerRcFile = container.getFile(new Path(IBowerConstants.BOWER_RC));
				if (!bowerRcFile.exists()) {
					BowerRc bowerRc = new BowerRc();
					bowerRc.setDirectory(IBowerConstants.BOWER_COMPONENTS);
					byte[] bytes = gson.toJson(bowerRc).getBytes();
					InputStream inputStream = new ByteArrayInputStream(bytes);
					bowerRcFile.create(inputStream, true, monitor);
				}

				if (bowerJsonFile.exists()) {
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage();
					IDE.openEditor(page, bowerJsonFile);
				}
			}

			@Override
			public ISchedulingRule getRule() {
				return container.getProject();
			}
		};
		return workspaceModifyOperation;
	}

}
