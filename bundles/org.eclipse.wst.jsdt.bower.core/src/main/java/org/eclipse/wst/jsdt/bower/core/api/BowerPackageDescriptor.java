/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.bower.core.api;

/**
 * This descriptor only contains the name of a bower package and its repository URL.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class BowerPackageDescriptor {
	/**
	 * The name of the package.
	 */
	private String name;

	/**
	 * The URL of the package.
	 */
	private String url;

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            The name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * Sets the url.
	 *
	 * @param url
	 *            The url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Returns the URL.
	 *
	 * @return The url
	 */
	public String getUrl() {
		return this.url;
	}

}
