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
 * This class will be used to configure and run bower-related operations. This is the main entry point to be
 * used by clients.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class Bower {
	/**
	 * Creates an install command used to download all the dependencies of a bower project.
	 *
	 * @return An {@link InstallCommand}.
	 */
	public static InstallCommand install() {
		return new InstallCommand();
	}
}
