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
 * Generator for package.json.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
class PackageJson {
	
	/**
	 * This operation will return the content of the file.
	 */
	def generateFile() '''
		{
		  "name": "",
		  "version": "",
		  "description": "",
		  "authors": [],
		  "scripts": {},
		  "dependencies": {},
		  "devDependencies": {
		  	"connect": "~2.25.7",
		  	"connect-livereload": "~0.4.0",
		  	"opn": "~1.0.0",
		  	"gulp": "~3.8.7",
		  	"gulp-load-plugins": "~0.5.3",
		  	"gulp-livereload": "~2.1.0",
		  	"gulp-jshint": "~1.8.4",
		  	"jshint-stylish": "~0.4.0",
		  	"jshint-checkstyle-file-reporter": "0.0.1",
		  	"gulp-ruby-sass": "~0.7.1",
		  	"gulp-autoprefixer": "~0.0.9",
		  	"gulp-csso": "~0.2.9",
		  	"gulp-imagemin": "~1.0.1",
		  	"main-bower-files": "~2.0.0",
		  	"gulp-clean": "~0.3.1",
		  	"gulp-flatten": "~0.0.2",
		  	"gulp-filter": "~1.0.0",
		  	"gulp-uglify": "~0.3.2",
		  	"gulp-concat": "~2.3.4",
		  	"gulp-size": "~1.0.0",
		  	"chai": "~1.9.1",
		  	"mocha": "~1.21.4",
		  	"karma": "~0.12.22",
		  	"karma-chai": "~0.1.0",
		  	"karma-mocha": "~0.1.9",
		  	"karma-chrome-launcher": "~0.1.4",
		  	"karma-coverage": "~0.2.6"
		  },
		  "engines": {
		  	"node": ">= 0.10.0"
		  }
		}
	'''
	
}