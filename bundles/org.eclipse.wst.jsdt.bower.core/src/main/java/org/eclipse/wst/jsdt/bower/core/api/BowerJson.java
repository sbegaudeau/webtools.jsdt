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
package org.eclipse.wst.jsdt.bower.core.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The bower.json file content. See <a href="https://github.com/bower/bower.json-spec">the bower.json
 * specification for more details</a>.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class BowerJson {
	/**
	 * The name.
	 */
	private String name = ""; //$NON-NLS-1$

	/**
	 * The description.
	 */
	private String description = ""; //$NON-NLS-1$

	/**
	 * The version.
	 */
	private String version = ""; //$NON-NLS-1$

	/**
	 * The main files.
	 */
	private List<String> main = new ArrayList<String>();

	/**
	 * The licenses
	 */
	private List<String> licenses = new ArrayList<String>();

	/**
	 * The files to ignore.
	 */
	private List<String> ignore = new ArrayList<String>();

	/**
	 * The authors.
	 */
	private List<String> authors = new ArrayList<String>();

	/**
	 * The homepage.
	 */
	private String homepage = ""; //$NON-NLS-1$

	/**
	 * The dependencies.
	 */
	private Map<String, String> dependencies = new HashMap<String, String>();

	/**
	 * The development dependencies.
	 */
	private Map<String, String> devDependencies = new HashMap<String, String>();

	/**
	 * Indicates if the package is private.
	 */
	@SerializedName("private")
	private boolean isPrivate;

	/**
	 * Returns the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 *
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the description.
	 *
	 * @return The description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description
	 *            The description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the version
	 *
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the version
	 *
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Returns the main.
	 *
	 * @return The main
	 */
	public List<String> getMain() {
		return this.main;
	}

	/**
	 * Sets the main.
	 *
	 * @param main
	 *            The main to set
	 */
	public void setMain(List<String> main) {
		this.main = main;
	}

	/**
	 * Returns the licenses.
	 *
	 * @return The licenses
	 */
	public List<String> getLicenses() {
		return this.licenses;
	}

	/**
	 * Sets the licenses.
	 *
	 * @param licenses
	 *            The licenses to set
	 */
	public void setLicenses(List<String> licenses) {
		this.licenses = licenses;
	}

	/**
	 * Returns the ignore.
	 *
	 * @return The ignore
	 */
	public List<String> getIgnore() {
		return this.ignore;
	}

	/**
	 * Sets the ignore.
	 *
	 * @param ignore
	 *            The ignore to set
	 */
	public void setIgnore(List<String> ignore) {
		this.ignore = ignore;
	}

	/**
	 * Returns the authors.
	 *
	 * @return The authors
	 */
	public List<String> getAuthors() {
		return this.authors;
	}

	/**
	 * Sets the authors.
	 *
	 * @param authors
	 *            The authors to set
	 */
	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	/**
	 * Returns the homepage.
	 *
	 * @return The homepage
	 */
	public String getHomepage() {
		return this.homepage;
	}

	/**
	 * Sets the homepage.
	 *
	 * @param homepage
	 *            The homepage to set
	 */
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	/**
	 * Returns the dependencies.
	 *
	 * @return the dependencies
	 */
	public Map<String, String> getDependencies() {
		return dependencies;
	}

	/**
	 * The dependencies to set.
	 *
	 * @param dependencies
	 *            the dependencies to set
	 */
	public void setDependencies(Map<String, String> dependencies) {
		this.dependencies = dependencies;
	}

	/**
	 * Returns the devDependencies.
	 *
	 * @return the devDependencies
	 */
	public Map<String, String> getDevDependencies() {
		return devDependencies;
	}

	/**
	 * The devDependencies to set.
	 *
	 * @param devDependencies
	 *            the devDependencies to set
	 */
	public void setDevDependencies(Map<String, String> devDependencies) {
		this.devDependencies = devDependencies;
	}

	/**
	 * Returns the isPrivate.
	 *
	 * @return The isPrivate
	 */
	public boolean isPrivate() {
		return this.isPrivate;
	}

	/**
	 * Sets the isPrivate.
	 *
	 * @param isPrivate
	 *            The isPrivate to set
	 */
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
}
