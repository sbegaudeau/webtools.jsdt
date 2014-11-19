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
 * Generator of app.js.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@SuppressWarnings("all")
public class AppJs {
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
    _builder.append("var angular = require(\'angular\');");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("angular.module(\'\', []);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("  ");
    _builder.append("var homepageCtrl = require(\'../sections/homepage/HomepageCtrl\');");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("angular.module(\'\').controller(homepageCtrl.name, homepageCtrl.declaration);");
    _builder.newLine();
    _builder.append("})();");
    _builder.newLine();
    return _builder;
  }
}
