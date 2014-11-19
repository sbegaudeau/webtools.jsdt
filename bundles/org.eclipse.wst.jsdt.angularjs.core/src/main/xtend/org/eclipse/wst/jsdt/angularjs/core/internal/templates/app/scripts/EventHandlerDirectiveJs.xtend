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
 * Generator of an angular event-handler directive.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
class EventHandlerDirectiveJs {
	/**
	 * This operation will generate the content of the file.
	 */
	 def generateFile() '''
	 	(function () {
	 	  'use strict';
	 	
	 	  var eventHandlerDirective = function () {
	 	    var directive = {};
	 	
	 	    directive.link = function (scope, element, attributes) {
	 	      element.on('click', function () {
	 	        scope.$eval(attributes.directiveNgClick);
	 	        scope.$apply();
	 	      });
	 	    };
	 	
	 	    return directive;
	 	  };
	 	
	 	  var eventHandlerDirectiveExport = {
	 	    name: 'eventHandleDirective',
	 	    declaration: [eventHandlerDirective]
	 	  };
	 	
	 	  module.exports = eventHandlerDirectiveExport;
	 	})();
	 '''
}