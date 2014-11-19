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
package org.eclipse.wst.jsdt.nodejs.ide.ui.internal.views.outdated;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wst.jsdt.nodejs.core.api.commands.Npm;
import org.eclipse.wst.jsdt.nodejs.core.api.commands.Result;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.NodeJsIdeUiActivator;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.preferences.INodeJsPreferenceConstants;

/**
 * This view will show the state of the dependencies of the currently selected project.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class NpmOutdatedView extends ViewPart {

	/**
	 * List all the outdated dependencies.
	 */
	private static final String OUTDATED = "outdated"; //$NON-NLS-1$

	/**
	 * Retrieve the result as a json object.
	 */
	private static final String JSON = "--json"; //$NON-NLS-1$

	/**
	 * This listener will be used to refresh the content of the view when the selected resource changes.
	 */
	private ISelectionListener listener;

	/**
	 * The table viewer showing the state of the dependencies of the currently selected project.
	 */
	private TableViewer tableViewer;

	/**
	 * The constructor.
	 */
	public NpmOutdatedView() {
		this.setPartName("NPM Outdated");
		this.setContentDescription("Displays a table-based view of the state of the dependencies of the project");
		this.setTitleToolTip("Displays a table-based view of the state of the dependencies of the project");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.tableViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.FULL_SELECTION | SWT.BORDER);
		this.tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		Table table = this.tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableViewerColumn packageColumn = new TableViewerColumn(this.tableViewer, SWT.NONE);
		packageColumn.getColumn().setText("Package");
		packageColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof OutdatedDependency) {
					return ((OutdatedDependency)element).getName();
				}
				return super.getText(element);
			}
		});

		TableViewerColumn currentVersionColumn = new TableViewerColumn(this.tableViewer, SWT.NONE);
		currentVersionColumn.getColumn().setText("Current Version");
		currentVersionColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof OutdatedDependency) {
					return ((OutdatedDependency)element).getCurrent();
				}
				return super.getText(element);
			}
		});

		TableViewerColumn wantedVersionColumn = new TableViewerColumn(this.tableViewer, SWT.NONE);
		wantedVersionColumn.getColumn().setText("Wanted");
		wantedVersionColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof OutdatedDependency) {
					return ((OutdatedDependency)element).getWanted();
				}
				return super.getText(element);
			}
		});

		TableViewerColumn latestVersionColumn = new TableViewerColumn(this.tableViewer, SWT.NONE);
		latestVersionColumn.getColumn().setText("Lastest");
		latestVersionColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof OutdatedDependency) {
					return ((OutdatedDependency)element).getLatest();
				}
				return super.getText(element);
			}
		});

		TableViewerColumn locationColumn = new TableViewerColumn(this.tableViewer, SWT.NONE);
		locationColumn.getColumn().setText("Location");
		locationColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof OutdatedDependency) {
					return ((OutdatedDependency)element).getLocation();
				}
				return super.getText(element);
			}
		});

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
					List<OutdatedDependency> dependencies = NpmOutdatedView.this
							.getOutdatedDependencies(selection);
					NpmOutdatedView.this.tableViewer.setInput(dependencies);
				}
			};
		}

		return this.listener;
	}

	private String getStringValue(JsonElement jsonElement, String fieldName) {
		String value = "";
		if (jsonElement instanceof JsonObject) {
			JsonObject jsonObject = (JsonObject)jsonElement;
			JsonPrimitive jsonPrimitive = jsonObject.getAsJsonPrimitive(fieldName);
			if (jsonPrimitive != null && jsonPrimitive.isString()) {
				value = jsonPrimitive.getAsString();
			}
		}
		return value;
	}

	/**
	 * Returns the outdated dependencies of the current selection.
	 *
	 * @param selection
	 *            The selection
	 * @return The outdated dependencies of the current selection
	 */
	private List<OutdatedDependency> getOutdatedDependencies(ISelection selection) {
		List<OutdatedDependency> dependencies = new ArrayList<OutdatedDependency>();

		Result result = this.getDependencies(selection);
		if (result != null && result.getExitCode() == 0) {
			JsonParser parser = new JsonParser();
			JsonElement jsonElement = parser.parse(result.getBody());
			if (jsonElement instanceof JsonObject) {
				JsonObject jsonObject = (JsonObject)jsonElement;
				Set<Entry<String, JsonElement>> entries = jsonObject.entrySet();
				for (Entry<String, JsonElement> entry : entries) {
					String name = entry.getKey();
					JsonElement element = entry.getValue();
					String current = this.getStringValue(element, "current");
					String wanted = this.getStringValue(element, "wanted");
					String latest = this.getStringValue(element, "latest");
					String location = this.getStringValue(element, "location");
					OutdatedDependency outdatedDependency = new OutdatedDependency(name, current, wanted,
							latest, location);
					dependencies.add(outdatedDependency);
				}
			}
		}

		return dependencies;
	}

	/**
	 * Returns the dependencies of the current selection.
	 *
	 * @param selection
	 *            The selection
	 * @return The dependencies of the current selection
	 */
	private Result getDependencies(ISelection selection) {
		Result result = null;
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection iStructuredSelection = (IStructuredSelection)selection;

			IProject project = null;

			Iterator<?> iterator = iStructuredSelection.iterator();
			if (iterator.hasNext()) {
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
					result = npm.call(OUTDATED, JSON);
				}
			}

		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// do nothing for now
	}

}
