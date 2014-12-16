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
package org.eclipse.wst.jsdt.bower.core.api.utils;

/**
 * Utility interface holding some of the constants of the plugin.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public interface IBowerConstants {
	/**
	 * The name of the file containing the description of a bower package.
	 */
	String BOWER_JSON = "bower.json"; //$NON-NLS-1$

	/**
	 * The name of the file in which the configuration of bower can be found.
	 */
	String BOWER_RC = ".bowerrc"; //$NON-NLS-1$

	/**
	 * The name of the default folder where bower packages will be downloaded.
	 */
	String BOWER_COMPONENTS = "bower_components"; //$NON-NLS-1$

	/**
	 * The URL of the default bower server.
	 */
	String DEFAULT_BOWER_SERVER_URL = "http://bower.herokuapp.com"; //$NON-NLS-1$

	/**
	 * The path on the bower server used to see the details of a package.
	 */
	String PACKAGES_PATH = "/packages/"; //$NON-NLS-1$

	/**
	 * The Git protocol prefix.
	 */
	String GIT_PREFIX = "git://"; //$NON-NLS-1$

	/**
	 * The SSH protocol prefix.
	 */
	String SSH_PREFIX = "ssh://"; //$NON-NLS-1$

	/**
	 * The HTTP protocol prefix.
	 */
	String HTTP_PREFIX = "http://"; //$NON-NLS-1$

	/**
	 * The HTTPS protocol prefix.
	 */
	String HTTPS_PREFIX = "https://"; //$NON-NLS-1$

	/**
	 * The git extension.
	 */
	String GIT_EXTENSION = ".git"; //$NON-NLS-1$

	/**
	 * The segment separator of an URL.
	 */
	String SEPARATOR = "/"; //$NON-NLS-1$

	/**
	 * Git refs tags prefix.
	 */
	String REFS_TAGS = "refs/tags/"; //$NON-NLS-1$

	/**
	 * The identifier of this bundle.
	 */
	String BOWER_CORE_BUNDLE_ID = "org.eclipse.wst.jsdt.bower.code"; //$NON-NLS-1$
}
