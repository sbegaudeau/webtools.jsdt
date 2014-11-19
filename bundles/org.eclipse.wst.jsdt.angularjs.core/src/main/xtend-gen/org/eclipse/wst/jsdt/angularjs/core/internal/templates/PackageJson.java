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
 * Generator for package.json.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@SuppressWarnings("all")
public class PackageJson {
  /**
   * This operation will return the content of the file.
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
    _builder.append("\"description\": \"\",");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"authors\": [],");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"scripts\": {},");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"dependencies\": {},");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"devDependencies\": {");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"connect\": \"~2.25.7\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"connect-livereload\": \"~0.4.0\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"opn\": \"~1.0.0\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"gulp\": \"~3.8.7\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"gulp-load-plugins\": \"~0.5.3\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"gulp-livereload\": \"~2.1.0\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"gulp-jshint\": \"~1.8.4\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"jshint-stylish\": \"~0.4.0\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"jshint-checkstyle-file-reporter\": \"0.0.1\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"gulp-ruby-sass\": \"~0.7.1\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"gulp-autoprefixer\": \"~0.0.9\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"gulp-csso\": \"~0.2.9\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"gulp-imagemin\": \"~1.0.1\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"main-bower-files\": \"~2.0.0\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"gulp-clean\": \"~0.3.1\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"gulp-flatten\": \"~0.0.2\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"gulp-filter\": \"~1.0.0\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"gulp-uglify\": \"~0.3.2\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"gulp-concat\": \"~2.3.4\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"gulp-size\": \"~1.0.0\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"chai\": \"~1.9.1\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"mocha\": \"~1.21.4\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"karma\": \"~0.12.22\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"karma-chai\": \"~0.1.0\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"karma-mocha\": \"~0.1.9\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"karma-chrome-launcher\": \"~0.1.4\",");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"karma-coverage\": \"~0.2.6\"");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("},");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\"engines\": {");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("\"node\": \">= 0.10.0\"");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
