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
 * Generator for models/users.js
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
class ModelsUsersJs {
	/**
	 * The path of the file to generate.
	 */
	public static val PATH = "models/users.js";
	
	/**
	 * This operation will return the content of the file.
	 */
	def generateFile() '''
		'use strict';
		
		var mongoose = require('mongoose');
		var bcrypt   = require('brcypt-nodejs');
		
		var userSchema = new mongoose.Schema({
		  local: {
		    email: String,
		    password: String
		  }
		});
		
		userSchema.methods.generateHash = function (password) {
		  return bcrypt.hashSync(password, bcrypt.genSaltSync(8), null);
		}
	'''
}