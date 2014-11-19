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
 * Generator for README.md
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@SuppressWarnings("all")
public class ReadmeMd {
  /**
   * The path of the file to generate.
   */
  public final static String PATH = "README.md";
  
  /**
   * This operation will return the content of the file.
   */
  public CharSequence generateFile() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("MEAN Server");
    _builder.newLine();
    _builder.newLine();
    _builder.append("This server is based on the following technologies:");
    _builder.newLine();
    _builder.append("* Node.js");
    _builder.newLine();
    _builder.append("* Express.js");
    _builder.newLine();
    _builder.append("* MongoDB");
    _builder.newLine();
    _builder.append("* Mongoose");
    _builder.newLine();
    _builder.append("* Angular.js");
    _builder.newLine();
    _builder.append("* Jade");
    _builder.newLine();
    _builder.append("* Passport");
    _builder.newLine();
    return _builder;
  }
}
