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
 * Generator for .jshintrc.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@SuppressWarnings("all")
public class JSHintRc {
  /**
   * This operation will generate the content of the file.
   */
  public CharSequence generateFile() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"node\": true,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"browser\": true,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"esnext\": true,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"bitwise\": true,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"camelcase\": true,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"curly\": true,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"eqeqeq\": true,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"immed\": true,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"indent\": 2,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"latedef\": true,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"newcap\": true,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"noarg\": true,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"quotmark\": \"single\",");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"undef\": true,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"unused\": true,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"strict\": true,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"trailing\": true,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"smarttabs\": true,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"jquery\": true,");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"predef\": [\"angular\"]");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
