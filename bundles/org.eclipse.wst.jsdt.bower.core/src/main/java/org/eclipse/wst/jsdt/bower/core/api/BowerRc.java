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

/**
 * This class will be used to hold the content of the file ".bowerrc". This file is used to store preferences
 * for bower.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class BowerRc {
	/**
	 * The directory where the bower dependencies will be stored.
	 */
	private String directory = ""; //$NON-NLS-1$

	/**
	 * Returns the directory.
	 *
	 * @return The directory
	 */
	public String getDirectory() {
		return this.directory;
	}

	/**
	 * Sets the directory.
	 *
	 * @param directory
	 *            The directory to set
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}
}
