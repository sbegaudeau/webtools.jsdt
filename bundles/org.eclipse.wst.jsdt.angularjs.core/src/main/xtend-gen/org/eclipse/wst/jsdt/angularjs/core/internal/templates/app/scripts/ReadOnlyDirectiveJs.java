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
 * Generator of an angular read-only directive.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@SuppressWarnings("all")
public class ReadOnlyDirectiveJs {
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
    _builder.append("  ");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("var readOnlyDirective = function () {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("var directive = {};");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("directive.link = function (scope, element, attributes) {");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("scope.$watch(attributes.directiveNgBind, function (newValue) {");
    _builder.newLine();
    _builder.append("      \t");
    _builder.append("var value = newValue;");
    _builder.newLine();
    _builder.append("      \t");
    _builder.append("if (value === undefined) {");
    _builder.newLine();
    _builder.append("      \t  ");
    _builder.append("value = \'\';");
    _builder.newLine();
    _builder.append("      \t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("element.text(value);");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("});");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("};");
    _builder.newLine();
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("return directive;");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("};");
    _builder.newLine();
    _builder.append("  ");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("var readOnlyDirectiveExport = {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("name: \'readOnlyDirective\',");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("declaration: [readOnlyDirective]");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("};");
    _builder.newLine();
    _builder.newLine();
    _builder.append("  ");
    _builder.append("module.exports = readOnlyDirectiveDeclaration;");
    _builder.newLine();
    _builder.append("})();");
    _builder.newLine();
    return _builder;
  }
}
