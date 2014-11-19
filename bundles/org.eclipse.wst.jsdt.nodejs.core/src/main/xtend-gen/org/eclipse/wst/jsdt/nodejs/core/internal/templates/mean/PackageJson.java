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
 * Generator for package.json.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@SuppressWarnings("all")
public class PackageJson {
  /**
   * The path of the file to generate.
   */
  public final static String PATH = "package.json";
  
  /**
   * This operation will return the content of the file.
   */
  public CharSequence generateFile(final String name, final String version) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"name\": \"");
    _builder.append(name, "  ");
    _builder.append("\",");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("\"version\": \"");
    _builder.append(version, "  ");
    _builder.append("\",");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("\"description\": \"\",");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"authors\": [],");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"scripts\": {},");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"dependencies\": {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"express\": \"4.x\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"morgan\": \"1.x\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"body-parser\": \"1.x\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"cookie-parser\": \"~1.0.0\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"method-override\": \"1.x\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"express-session\": \"~1.0.0\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"mongoose\": \"3.x\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"passport\": \"~0.1.17\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"passport-local\": \"~0.1.6\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"passport-google-oauth\": \"~0.1.5\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"passport-twitter\" : \"~1.0.2\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"bcrypt-nodejs\" : \"~0.0.3\"");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("},");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"devDependencies\": {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("},");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"engines\": {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"node\": \">= 0.10.0\"");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
