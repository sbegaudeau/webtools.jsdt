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
package org.eclipse.wst.jsdt.nodejs.ide.ui.internal.views.dependencies;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wst.jsdt.nodejs.core.api.commands.Npm;
import org.eclipse.wst.jsdt.nodejs.core.api.commands.Result;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.NodeJsIdeUiActivator;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.preferences.INodeJsPreferenceConstants;

/**
 * This view will display the dependencies managed by npm.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class NpmDependenciesView extends ViewPart {

	/**
	 * List all the dependencies.
	 */
	private static final String LIST = "list"; //$NON-NLS-1$

	/**
	 * Retrieve the result as a json object.
	 */
	private static final String JSON = "--json"; //$NON-NLS-1$

	/**
	 * This listener will be used to refresh the content of the view when the selected resource changes.
	 */
	private ISelectionListener listener;

	/**
	 * The treeviewer showing the dependencies of the currently selected project.
	 */
	private TreeViewer treeViewer;

	/**
	 * The constructor.
	 */
	public NpmDependenciesView() {
		this.setPartName("NPM Dependencies");
		this.setContentDescription("Displays a tree-based view of the npm dependencies");
		this.setTitleToolTip("Displays a tree-based view of the npm dependencies of the selected project");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		this.treeViewer.setContentProvider(new NpmDependenciesViewContentProvider());
		this.treeViewer.setLabelProvider(new NpmDependenciesViewLabelProvider());

		ISelection selection = this.getSite().getPage().getSelection();
		List<JsonObject> dependencies = this.getJsonDependencies(selection);
		this.treeViewer.setInput(dependencies);

		MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		Control control = this.treeViewer.getControl();
		Menu menu = menuManager.createContextMenu(control);
		control.setMenu(menu);

		this.getSite().getPage().addPostSelectionListener(this.createListener());
	}

	/**
	 * Creates or returns the selection listener used to change the input of the tree viewer based on the
	 * current selection.
	 *
	 * @return The selection listener
	 */
	private ISelectionListener createListener() {
		if (this.listener == null) {
			this.listener = new ISelectionListener() {

				@Override
				public void selectionChanged(IWorkbenchPart part, ISelection selection) {
					List<JsonObject> dependencies = NpmDependenciesView.this.getJsonDependencies(selection);
					NpmDependenciesView.this.treeViewer.setInput(dependencies);
				}
			};
		}

		return this.listener;
	}

	/**
	 * Returns the Json version of the dependencies of the current selection.
	 *
	 * @param selection
	 *            The selection
	 * @return The Json version of the dependencies
	 */
	private List<JsonObject> getJsonDependencies(ISelection selection) {
		List<JsonObject> jsonResults = new ArrayList<JsonObject>();

		List<Result> results = this.getDependencies(selection);
		for (Result result : results) {
			if (result != null && result.getExitCode() == 0) {
				JsonParser parser = new JsonParser();
				JsonElement jsonElement = parser.parse(result.getBody());
				if (jsonElement instanceof JsonObject) {
					jsonResults.add((JsonObject)jsonElement);
				}
			}
		}

		return jsonResults;
	}

	/**
	 * Returns the dependencies of the current selection.
	 *
	 * @param selection
	 *            The selection
	 * @return The dependencies of the current selection
	 */
	private List<Result> getDependencies(ISelection selection) {
		List<Result> results = new ArrayList<Result>();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection iStructuredSelection = (IStructuredSelection)selection;

			IProject project = null;

			Iterator<?> iterator = iStructuredSelection.iterator();
			while (iterator.hasNext()) {
				Object element = iterator.next();
				if (element instanceof IResource) {
					project = ((IResource)element).getProject();
				} else if (element != null) {
					Object adapted = Platform.getAdapterManager().getAdapter(element, IResource.class);
					if (adapted instanceof IResource) {
						project = ((IResource)adapted).getProject();
					}
				}

				if (project != null) {
					IPreferenceStore preferenceStore = NodeJsIdeUiActivator.getInstance()
							.getPreferenceStore();
					String nodeLocation = preferenceStore.getString(INodeJsPreferenceConstants.NODE_LOCATION);
					String npmLocation = preferenceStore.getString(INodeJsPreferenceConstants.NPM_LOCATION);

					Npm npm = new Npm(npmLocation, project.getLocation().toFile(), nodeLocation);
					Result result = npm.call(LIST, JSON);
					results.add(result);
				}
			}

		}
		return results;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		this.getSite().getPage().removePostSelectionListener(this.listener);
		super.dispose();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// do nothing since nothing in the part can use the focus
	}

}
