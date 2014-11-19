/**
 * Copyright (c) 2014 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.wst.jsdt.nodejs.core.internal.templates.mean;

import org.eclipse.xtend2.lib.StringConcatenation;

/**
 * Generator for models/users.js
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@SuppressWarnings("all")
public class ModelsUsersJs {
  /**
   * The path of the file to generate.
   */
  public final static String PATH = "models/users.js";
  
  /**
   * This operation will return the content of the file.
   */
  public CharSequence generateFile() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\'use strict\';");
    _builder.newLine();
    _builder.newLine();
    _builder.append("var mongoose = require(\'mongoose\');");
    _builder.newLine();
    _builder.append("var bcrypt   = require(\'brcypt-nodejs\');");
    _builder.newLine();
    _builder.newLine();
    _builder.append("var userSchema = new mongoose.Schema({");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("local: {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("email: String,");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("password: String");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("});");
    _builder.newLine();
    _builder.newLine();
    _builder.append("userSchema.methods.generateHash = function (password) {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("return bcrypt.hashSync(password, bcrypt.genSaltSync(8), null);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
