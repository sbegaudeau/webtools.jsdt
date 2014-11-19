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
 * Generator for package.json.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
class PackageJson {
	/**
	 * The path of the file to generate.
	 */
	public static val PATH = "package.json";
	
	/**
	 * This operation will return the content of the file.
	 */
	def generateFile(String name, String version) '''
		{
		  "name": "«name»",
		  "version": "«version»",
		  "description": "",
		  "authors": [],
		  "scripts": {},
		  "dependencies": {
		    "express": "4.x",
		    "morgan": "1.x",
		    "body-parser": "1.x",
		    "cookie-parser": "~1.0.0",
		    "method-override": "1.x",
		    "express-session": "~1.0.0",
		    "mongoose": "3.x",
		    "passport": "~0.1.17",
		    "passport-local": "~0.1.6",
		    "passport-google-oauth": "~0.1.5",
		    "passport-twitter" : "~1.0.2",
		    "bcrypt-nodejs" : "~0.0.3"
		  },
		  "devDependencies": {
		  },
		  "engines": {
		    "node": ">= 0.10.0"
		  }
		}
	'''
}