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
package org.eclipse.wst.jsdt.nodejs.core.internal.templates.mean

/**
 * Generator for CONTRIBUTING.md.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
class ContributingMd {
	/**
	 * The path of the file to generate.
	 */
	public static val PATH = "CONTRIBUTING.md";
	
	/**
	 * This operation will return the content of the file.
	 */
	def generateFile() '''
		In order to contribute to this project, you will need to install the following tools:
		
		* Node.js
		* SASS
		* MongoDB
	'''
}