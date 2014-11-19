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
package org.eclipse.wst.jsdt.angularjs.core.internal.templates.app.scripts;

import org.eclipse.xtend2.lib.StringConcatenation;

/**
 * Generator of an angular controller.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@SuppressWarnings("all")
public class ControllerJs {
  /**
   * This operation will generate the content of the file.
   */
  public CharSequence generateFile() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(function () {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\'use strict\';");
    _builder.newLine();
    _builder.newLine();
    _builder.append("  ");
    _builder.append("var homepageController = function () {");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("this.data = [");
    _builder.newLine();
    _builder.append("  \t  ");
    _builder.append("\'\',");
    _builder.newLine();
    _builder.append("  \t  ");
    _builder.append("\'\',");
    _builder.newLine();
    _builder.append("  \t  ");
    _builder.append("\'\'");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("];");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("this.operation = function () {");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("this.data.push(\'\');");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("};");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("};");
    _builder.newLine();
    _builder.newLine();
    _builder.append("  ");
    _builder.append("var homepageControllerExport = {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("name: \'homepageController\',");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("declaration: [homepageController]");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("  ");
    _builder.append("module.exports = homepageControllerDeclaration;");
    _builder.newLine();
    _builder.append("})();");
    _builder.newLine();
    return _builder;
  }
}
