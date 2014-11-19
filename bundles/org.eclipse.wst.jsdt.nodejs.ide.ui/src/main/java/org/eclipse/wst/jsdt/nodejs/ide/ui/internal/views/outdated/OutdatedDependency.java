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

/**
 * This POJO will store the properties of a dependency for the outdated view.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class OutdatedDependency {
	/**
	 * The name.
	 */
	private String name;

	/**
	 * The current.
	 */
	private String current;

	/**
	 * The wanted.
	 */
	private String wanted;

	/**
	 * The latest.
	 */
	private String latest;

	/**
	 * The location.
	 */
	private String location;

	/**
	 * The constructor.
	 *
	 * @param name
	 *            The name
	 * @param current
	 *            The current
	 * @param wanted
	 *            The wanted
	 * @param latest
	 *            The latest
	 * @param location
	 *            The location
	 */
	public OutdatedDependency(String name, String current, String wanted, String latest, String location) {
		this.name = name;
		this.current = current;
		this.wanted = wanted;
		this.latest = latest;
		this.location = location;
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
	 * Returns the current.
	 *
	 * @return The current
	 */
	public String getCurrent() {
		return this.current;
	}

	/**
	 * Returns the wanted.
	 *
	 * @return The wanted
	 */
	public String getWanted() {
		return this.wanted;
	}

	/**
	 * Returns the latest.
	 *
	 * @return The latest
	 */
	public String getLatest() {
		return this.latest;
	}

	/**
	 * Returns the location.
	 *
	 * @return The location
	 */
	public String getLocation() {
		return this.location;
	}

}
