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
 * Generator for server.js
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@SuppressWarnings("all")
public class ServerJs {
  /**
   * The path of the file to generate.
   */
  public final static String PATH = "server.js";
  
  /**
   * This operation will return the content of the file.
   */
  public CharSequence generateFile() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\'use strict\';");
    _builder.newLine();
    _builder.newLine();
    _builder.append("var express        = require(\'express\');");
    _builder.newLine();
    _builder.append("var morgan         = require(\'morgan\');");
    _builder.newLine();
    _builder.append("var cookieParser   = require(\'cookie-parser\');");
    _builder.newLine();
    _builder.append("var bodyParser     = require(\'body-parser\');");
    _builder.newLine();
    _builder.append("var session        = require(\'express-session\');");
    _builder.newLine();
    _builder.append("var methodOverride = require(\'method-override\');");
    _builder.newLine();
    _builder.append("var mongoose       = require(\'mongoose\');");
    _builder.newLine();
    _builder.append("var passport       = require(\'mongoose\');");
    _builder.newLine();
    _builder.newLine();
    _builder.append("//var databaseConfiguration = require(\'./configuration/database\');");
    _builder.newLine();
    _builder.append("//mongoose.connect(databaseConfiguration.url);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("//var passportConfiguration = require(\'./configuration/passport\');");
    _builder.newLine();
    _builder.append("//passportConfiguration(passport);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("var app = express();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("app.use(express.static(__dirname + \'/public));");
    _builder.newLine();
    _builder.newLine();
    _builder.append("app.use(morgan(\'dev\'));");
    _builder.newLine();
    _builder.append("app.use(bodyParser.json());");
    _builder.newLine();
    _builder.append("app.use(cookieParser());");
    _builder.newLine();
    _builder.append("app.use(methodOverride());");
    _builder.newLine();
    _builder.newLine();
    _builder.append("//app.use(session({secret: \'secretkey\'}));");
    _builder.newLine();
    _builder.append("//app.use(passport.initialize());");
    _builder.newLine();
    _builder.append("//app.use(passport.session());");
    _builder.newLine();
    _builder.newLine();
    _builder.append("//var routes = require(\'./routes/routes\');");
    _builder.newLine();
    _builder.append("//routes(app, passport);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("//var serverConfiguration = require(\'./configuration/server\');");
    _builder.newLine();
    _builder.append("//app.listen(serverConfiguration.port);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("//console.log(\'Node.js server up and running on \' serverConfiguration.port);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("app.get(\'/\', function (req, res) {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("res.send(\'Hello World\');");
    _builder.newLine();
    _builder.append("});");
    _builder.newLine();
    _builder.append("app.listen(3000);");
    _builder.newLine();
    return _builder;
  }
}
