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
 * Generator of an angular read-only directive.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
class ReadOnlyDirectiveJs {
	/**
	 * This operation will generate the content of the file.
	 */
	 def generateFile() '''
	 	(function () {
	 	  'use strict';
	 	  
	 	  var readOnlyDirective = function () {
	 	    var directive = {};
	 	
	 	    directive.link = function (scope, element, attributes) {
	 	      scope.$watch(attributes.directiveNgBind, function (newValue) {
	 	      	var value = newValue;
	 	      	if (value === undefined) {
	 	      	  value = '';
	 	      	}
	 	        element.text(value);
	 	      });
	 	    };
	 	
	 	  	return directive;
	 	  };
	 	  
	 	  var readOnlyDirectiveExport = {
	 	    name: 'readOnlyDirective',
	 	    declaration: [readOnlyDirective]
	 	  };
	 	
	 	  module.exports = readOnlyDirectiveDeclaration;
	 	})();
	 '''
}