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
package org.eclipse.wst.jsdt.angularjs.core.internal.templates.app;

import org.eclipse.xtend2.lib.StringConcatenation;

/**
 * Generator for index.html.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@SuppressWarnings("all")
public class IndexHtml {
  /**
   * This operation will generate the content of the file.
   */
  public CharSequence generateFile() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<!DOCTYPE html>");
    _builder.newLine();
    _builder.append("<html ng-app=\"\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<head>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<meta charset=\"utf-8\">");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<meta name=\"description\" content=\"\">");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<title></title>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<link rel=\"stylesheet\" href=\"styles/style.css\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</head>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<body>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<!--[if lt IE 10]>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<p class=\"browsehappy\">You are using an <strong>outdated</strong> browser. Please <a href=\"http://browsehappy.com/\">upgrade your browser</a> to improve your experience.</p>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<![endif]-->");
    _builder.newLine();
    _builder.append("    ");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<div id=\"wrapper\">");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<div ng-view></div>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<script src=\"bower_components/angular/angular.js\"></script>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<script src=\"bower_components/angular-route/angular-route.js\"></script>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<script src=\"scripts/app.js\"></script>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</body>");
    _builder.newLine();
    _builder.append("</html>");
    _builder.newLine();
    return _builder;
  }
}
