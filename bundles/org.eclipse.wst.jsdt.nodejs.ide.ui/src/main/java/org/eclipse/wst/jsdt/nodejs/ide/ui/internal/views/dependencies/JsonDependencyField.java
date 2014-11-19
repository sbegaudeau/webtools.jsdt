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

import com.google.gson.JsonObject;

/**
 * This class represents a field in a JsonObject.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class JsonDependencyField {
	/**
	 * The parent.
	 */
	private Object parent;

	/**
	 * The name.
	 */
	private String name;

	/**
	 * The value.
	 */
	private JsonObject value;

	/**
	 * The constructor.
	 *
	 * @param parent
	 *            The parent
	 * @param name
	 *            The name
	 * @param value
	 *            The value
	 */
	public JsonDependencyField(Object parent, String name, JsonObject value) {
		this.parent = parent;
		this.name = name;
		this.value = value;
	}

	/**
	 * Returns the parent.
	 *
	 * @return The parent
	 */
	public Object getParent() {
		return this.parent;
	}

	/**
	 * Returns the name.
	 *
	 * @return The name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the value.
	 *
	 * @return The value
	 */
	public JsonObject getValue() {
		return this.value;
	}
}
