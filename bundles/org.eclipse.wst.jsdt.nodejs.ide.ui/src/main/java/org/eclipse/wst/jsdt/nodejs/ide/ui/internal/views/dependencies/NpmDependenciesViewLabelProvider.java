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
import com.google.gson.JsonPrimitive;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.NodeJsIdeUiActivator;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.utils.INodeJsIcons;

/**
 * The label provider of the NPM dependencies view.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class NpmDependenciesViewLabelProvider extends StyledCellLabelProvider {
	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.viewers.StyledCellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
	 */
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		if (element instanceof JsonObject) {
			JsonObject jsonObject = (JsonObject)element;
			JsonElement nameJsonElement = jsonObject.get("name");
			JsonElement versionJsonELement = jsonObject.get("version");
			if (nameJsonElement instanceof JsonPrimitive && versionJsonELement instanceof JsonPrimitive) {
				JsonPrimitive nameJsonPrimitive = (JsonPrimitive)nameJsonElement;
				String name = nameJsonPrimitive.getAsString();

				JsonPrimitive versionJsonPrimitive = (JsonPrimitive)versionJsonELement;
				String version = versionJsonPrimitive.getAsString();

				StyledString text = new StyledString();
				text.append(name);
				text.append(" ");
				text.append("[" + version + "]", StyledString.COUNTER_STYLER);

				cell.setText(text.toString());
				cell.setStyleRanges(text.getStyleRanges());
				cell.setImage(NodeJsIdeUiActivator.getInstance().getImage(INodeJsIcons.PROJECT_16x16));
			}
		} else if (element instanceof JsonDependencyField) {
			JsonDependencyField jsonDependencyField = (JsonDependencyField)element;
			String name = jsonDependencyField.getName();

			JsonElement versionJsonElement = jsonDependencyField.getValue().get("version");
			if (versionJsonElement instanceof JsonPrimitive) {
				JsonPrimitive versionJsonPrimitive = (JsonPrimitive)versionJsonElement;
				String version = versionJsonPrimitive.getAsString();

				StyledString text = new StyledString();
				text.append(name);
				text.append(" ");
				text.append("[" + version + "]", StyledString.COUNTER_STYLER);

				cell.setText(text.toString());
				cell.setStyleRanges(text.getStyleRanges());
				cell.setImage(NodeJsIdeUiActivator.getInstance().getImage(INodeJsIcons.FILE_16x16));
			}
		}
	}
}
