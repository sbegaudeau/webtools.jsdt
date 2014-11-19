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
package org.eclipse.wst.jsdt.angularjs.core.internal.templates

/**
 * Generator for bower.json.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
class BowerJson {
	/**
	 * This operation will generate the content of the file
	 */
	 def generateFile() '''
	 	{
	 	  "name": "",
	 	  "version": "",
	 	  "private": true,
	 	  "authors": [],
	 	  "ignore": [
	 	    "**/.*",
	 	    "node_modules",
	 	    "app/bower_components",
	 	    "tests"
	 	  ],
	 	  "dependencies": {
	 	  	"jquery": "~1.11.0",
	 	  	"bootstrap-sass-official": "~3.1.1",
	 	  	"angular": "~1.2.22",
	 	  	"angular-route": "~1.2.22"
	 	  },
	 	  "devDependencies": {
	 	  	"chai": "~1.9.1",
	 	  	"mocha": "~1.21.4"
	 	  }
	 	}
	 '''
}