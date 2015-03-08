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
 * Generator for Gulpfile.js.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@SuppressWarnings("all")
public class GulpfileJs {
  /**
   * This operation will generate the content of the file.
   */
  public CharSequence generateFile() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\'use strict\';");
    _builder.newLine();
    _builder.newLine();
    _builder.append("var gulp = require(\'gulp\');");
    _builder.newLine();
    _builder.append("var $ = require(\'gulp-load-plugins\')();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("// Compilation of the SASS stylesheets");
    _builder.newLine();
    _builder.append("gulp.task(\'styles\', function () {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("return gulp.src(\'app/styles/emf.scss\')");
    _builder.newLine();
    _builder.append("    ");
    _builder.append(".pipe($.rubySass({");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("style: \'expanded\',");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("precision: 10");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}))");
    _builder.newLine();
    _builder.append("    ");
    _builder.append(".pipe($.autoprefixer(\'last 1 version\'))");
    _builder.newLine();
    _builder.append("    ");
    _builder.append(".pipe(gulp.dest(\'.tmp/styles/\'))");
    _builder.newLine();
    _builder.append("    ");
    _builder.append(".pipe($.size());");
    _builder.newLine();
    _builder.append("});");
    _builder.newLine();
    _builder.newLine();
    _builder.append("// Creates a node.js server hosting the content of the website");
    _builder.newLine();
    _builder.append("gulp.task(\'connect\', [\'styles\'], function () {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("var connect = require(\'connect\');");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("var app = connect()");
    _builder.newLine();
    _builder.append("    ");
    _builder.append(".use(require(\'connect-livereload\')({ port: 35729 }))");
    _builder.newLine();
    _builder.append("    ");
    _builder.append(".use(connect.static(\'app\'))");
    _builder.newLine();
    _builder.append("    ");
    _builder.append(".use(connect.static(\'.tmp\'))");
    _builder.newLine();
    _builder.append("    ");
    _builder.append(".use(connect.directory(\'app\'));");
    _builder.newLine();
    _builder.newLine();
    _builder.append("require(\'http\').createServer(app)");
    _builder.newLine();
    _builder.append("  ");
    _builder.append(".listen(9000)");
    _builder.newLine();
    _builder.append("  ");
    _builder.append(".on(\'listening\', function () {");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("console.log(\'Started connect web server on http://localhost:9000\');");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("});");
    _builder.newLine();
    _builder.append("});");
    _builder.newLine();
    _builder.newLine();
    _builder.append("// Watch the files to reload the server and process the files");
    _builder.newLine();
    _builder.append("gulp.task(\'watch\', function () {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("$.livereload.listen();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("  ");
    _builder.append("gulp.watch([");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\'app/*.html\',");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\'.tmp/styles/**/*.css\',");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\'app/*.js\',");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\'app/components/**/*\',");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\'app/sections/**/*\'");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("]).on(\'change\', function (file) {");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("$.livereload.changed(file.path);");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("});");
    _builder.newLine();
    _builder.newLine();
    _builder.append("  ");
    _builder.append("gulp.watch(\'app/styles/**/*.scss\', [\'styles\']);");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("gulp.watch(\'app/components/**/*.scss\', [\'styles\']);");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("gulp.watch(\'app/sections/**/*.js\', [\'scripts\']);");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("gulp.watch(\'app/components/**/*.js\', [\'scripts\']);");
    _builder.newLine();
    _builder.append("});");
    _builder.newLine();
    _builder.newLine();
    _builder.append("gulp.task(\'default\', [\'connect\', \'watch\'], function () {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("require(\'opn\')(\'http://localhost:9000\');");
    _builder.newLine();
    _builder.append("});");
    _builder.newLine();
    return _builder;
  }
}
