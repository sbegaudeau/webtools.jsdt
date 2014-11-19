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
 * Generator for .editoconfig.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@SuppressWarnings("all")
public class EditorConfig {
  /**
   * This operation will return the content of the file.
   */
  public CharSequence generateFile() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("root = true");
    _builder.newLine();
    _builder.newLine();
    _builder.append("[*]");
    _builder.newLine();
    _builder.append("indent_style = space");
    _builder.newLine();
    _builder.append("indent_size = 2");
    _builder.newLine();
    _builder.append("end_of_line = lf");
    _builder.newLine();
    _builder.append("charset = utf-8");
    _builder.newLine();
    _builder.append("trim_trailing_whitespace = true");
    _builder.newLine();
    _builder.append("insert_final_newline = true");
    _builder.newLine();
    return _builder;
  }
}
