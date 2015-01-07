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
package org.eclipse.wst.jsdt.nodejs.core.api;

/**
 * Utility interface holding some constants.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public interface INodeJsConstants {
	/**
	 * The name of the node executable.
	 */
	String NODE = "node"; //$NON-NLS-1$

	/**
	 * The name of the npm executable.
	 */
	String NPM = "npm"; //$NON-NLS-1$

	/**
	 * The name of the PATH environment variable.
	 */
	String PATH = "PATH"; //$NON-NLS-1$

	/**
	 * The separator of the different entries in the PATH.
	 */
	String PATH_SEPARATOR = ":"; //$NON-NLS-1$
}
