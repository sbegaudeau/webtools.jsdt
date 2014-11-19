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
 * Generator for server.js
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
class ServerJs {
	/**
	 * The path of the file to generate.
	 */
	public static val PATH = "server.js";
	
	/**
	 * This operation will return the content of the file.
	 */
	def generateFile() '''
		'use strict';
		
		var express        = require('express');
		var morgan         = require('morgan');
		var cookieParser   = require('cookie-parser');
		var bodyParser     = require('body-parser');
		var session        = require('express-session');
		var methodOverride = require('method-override');
		var mongoose       = require('mongoose');
		var passport       = require('mongoose');
		
		//var databaseConfiguration = require('./configuration/database');
		//mongoose.connect(databaseConfiguration.url);
		
		//var passportConfiguration = require('./configuration/passport');
		//passportConfiguration(passport);
		
		var app = express();
		
		app.use(express.static(__dirname + '/public));
		
		app.use(morgan('dev'));
		app.use(bodyParser.json());
		app.use(cookieParser());
		app.use(methodOverride());
		
		//app.use(session({secret: 'secretkey'}));
		//app.use(passport.initialize());
		//app.use(passport.session());
		
		//var routes = require('./routes/routes');
		//routes(app, passport);
		
		//var serverConfiguration = require('./configuration/server');
		//app.listen(serverConfiguration.port);
		
		//console.log('Node.js server up and running on ' serverConfiguration.port);
		
		app.get('/', function (req, res) {
		  res.send('Hello World');
		});
		app.listen(3000);
	'''
}