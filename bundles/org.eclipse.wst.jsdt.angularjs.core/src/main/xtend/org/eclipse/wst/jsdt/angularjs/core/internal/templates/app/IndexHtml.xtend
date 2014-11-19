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
package org.eclipse.wst.jsdt.angularjs.core.internal.templates.app

/**
 * Generator for index.html.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
class IndexHtml {
	/**
	 * This operation will generate the content of the file.
	 */
	 def generateFile() '''
	 	<!DOCTYPE html>
	 	<html ng-app="">
	 	  <head>
	 	    <meta charset="utf-8">
	 	    <meta name="description" content="">
	 	    <meta name="viewport" content="width=device-width, initial-scale=1">
	 	
	 	    <title></title>
	 	
	 	    <link rel="stylesheet" href="styles/style.css">
	 	  </head>
	 	  <body>
	 	    <!--[if lt IE 10]>
	 	      <p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
	 	    <![endif]-->
	 	    
	 	    <div id="wrapper">
	 	      <div ng-view></div>
	 	    </div>
	 	
	 	    <script src="bower_components/angular/angular.js"></script>
	 	    <script src="bower_components/angular-route/angular-route.js"></script>
	 	
	 	    <script src="scripts/app.js"></script>
	 	  </body>
	 	</html>
	 '''
}