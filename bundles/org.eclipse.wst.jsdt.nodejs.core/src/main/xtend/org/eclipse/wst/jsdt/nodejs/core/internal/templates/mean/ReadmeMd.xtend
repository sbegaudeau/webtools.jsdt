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
 * Generator for README.md
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
class ReadmeMd {
	/**
	 * The path of the file to generate.
	 */
	public static val PATH = "README.md";
	
	/**
	 * This operation will return the content of the file.
	 */
	def generateFile() '''
		MEAN Server
		
		This server is based on the following technologies:
		* Node.js
		* Express.js
		* MongoDB
		* Mongoose
		* Angular.js
		* Jade
		* Passport
	'''
}