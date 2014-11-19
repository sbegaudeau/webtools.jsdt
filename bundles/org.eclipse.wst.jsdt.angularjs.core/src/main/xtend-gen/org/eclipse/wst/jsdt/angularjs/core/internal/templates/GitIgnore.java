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
 * Generator for .gitignore.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@SuppressWarnings("all")
public class GitIgnore {
  /**
   * This operation will generate the content of the file.
   */
  public CharSequence generateFile() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(".sass-cache");
    _builder.newLine();
    _builder.append(".tmp");
    _builder.newLine();
    _builder.append("node_modules");
    _builder.newLine();
    _builder.append("dist");
    _builder.newLine();
    _builder.append("app/bower_components");
    _builder.newLine();
    _builder.append("tests/unit/coverage");
    _builder.newLine();
    _builder.append("checkstyle.xml");
    _builder.newLine();
    return _builder;
  }
}
