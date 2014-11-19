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
package org.eclipse.wst.jsdt.angularjs.core.internal.templates;

import org.eclipse.xtend2.lib.StringConcatenation;

/**
 * Generator for bower.json.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@SuppressWarnings("all")
public class BowerJson {
  /**
   * This operation will generate the content of the file
   */
  public CharSequence generateFile() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"name\": \"\",");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"version\": \"\",");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"private\": true,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"authors\": [],");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"ignore\": [");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"**/.*\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"node_modules\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"app/bower_components\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"tests\"");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("],");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"dependencies\": {");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"jquery\": \"~1.11.0\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"bootstrap-sass-official\": \"~3.1.1\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"angular\": \"~1.2.22\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"angular-route\": \"~1.2.22\"");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("},");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"devDependencies\": {");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"chai\": \"~1.9.1\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"mocha\": \"~1.21.4\"");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
