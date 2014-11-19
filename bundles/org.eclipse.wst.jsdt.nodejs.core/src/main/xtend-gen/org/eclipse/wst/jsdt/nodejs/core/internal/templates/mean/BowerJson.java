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
 * Generator for bower.json.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@SuppressWarnings("all")
public class BowerJson {
  /**
   * The path of the file to generate.
   */
  public final static String PATH = "bower.json";
  
  /**
   * This operation will return the content of the file.
   */
  public CharSequence generateFile(final String name) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"name\": \"");
    _builder.append(name, "  ");
    _builder.append("\",");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("\"description\": \"\",");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"dependencies\": {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"jquery\": \"~2.1.0\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"bootstrap-sass-official\": \"~3.1.0\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"angular\": \"~1.2.0\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"angular-cookies\": \"~1.2.0\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"angular-route\": \"~1.2.0\"");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("},");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"devDependencies\": {}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
