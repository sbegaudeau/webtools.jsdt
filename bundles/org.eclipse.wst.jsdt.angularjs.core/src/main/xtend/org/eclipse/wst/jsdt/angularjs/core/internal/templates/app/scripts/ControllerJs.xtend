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
package org.eclipse.wst.jsdt.angularjs.core.internal.templates.app.scripts

/**
 * Generator of an angular controller.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
class ControllerJs {
	/**
	 * This operation will generate the content of the file.
	 */
	 def generateFile() '''
	 	(function () {
	 	  'use strict';
	 	
	 	  var homepageController = function () {
	 	  	this.data = [
	 	  	  '',
	 	  	  '',
	 	  	  ''
	 	  	];
	 	
	 	    this.operation = function () {
	 	      this.data.push('');
	 	    };
	 	  };
	 	
	 	  var homepageControllerExport = {
	 	    name: 'homepageController',
	 	    declaration: [homepageController]
	 	  }
	 	
	 	  module.exports = homepageControllerDeclaration;
	 	})();
	 '''
}