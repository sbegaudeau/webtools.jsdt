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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The tree viewer content provider of the NPM dependencies view.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class NpmDependenciesViewContentProvider implements ITreeContentProvider {

	/**
	 * The dependencies field name.
	 */
	private static final String DEPENDENCIES = "dependencies"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object element) {
		if (element instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>)element;
			return collection.toArray(new Object[collection.size()]);
		}
		return new Object[] {};
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		List<Object> children = new ArrayList<Object>();

		JsonObject jsonObject = null;

		if (parentElement instanceof JsonObject) {
			// We have the root of the dependencies tree
			jsonObject = (JsonObject)parentElement;
		} else if (parentElement instanceof JsonDependencyField) {
			// We have a child of the dependencies tree
			jsonObject = ((JsonDependencyField)parentElement).getValue();
		}

		if (jsonObject != null) {
			JsonObject dependencies = jsonObject.getAsJsonObject(DEPENDENCIES);
			if (dependencies != null) {
				Set<Entry<String, JsonElement>> entries = dependencies.entrySet();
				for (Entry<String, JsonElement> entry : entries) {
					if (entry.getValue() instanceof JsonObject) {
						children.add(new JsonDependencyField(parentElement, entry.getKey(), (JsonObject)entry
								.getValue()));
					}
				}
			}

		}

		return children.toArray(new Object[children.size()]);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object element) {
		if (element instanceof JsonDependencyField) {
			return ((JsonDependencyField)element).getParent();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		boolean result = false;

		JsonObject jsonObject = null;

		if (element instanceof JsonObject) {
			jsonObject = (JsonObject)element;
		} else if (element instanceof JsonDependencyField) {
			jsonObject = ((JsonDependencyField)element).getValue();
		}

		if (jsonObject != null) {
			JsonObject dependencies = jsonObject.getAsJsonObject(DEPENDENCIES);
			if (dependencies != null) {
				result = dependencies.entrySet().size() > 0;
			}
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		// do nothing
	}
}
