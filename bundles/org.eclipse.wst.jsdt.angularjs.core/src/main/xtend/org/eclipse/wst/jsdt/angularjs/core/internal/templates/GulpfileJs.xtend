/*******************************************************************************
 * Copyright (c) 2014 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.angularjs.core.internal.templates

/**
 * Generator for Gulpfile.js.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
class GulpfileJs {
	/**
	 * This operation will generate the content of the file.
	 */
	 def generateFile() '''
	 	'use strict';
	 	
	 	var gulp = require('gulp');
	 	var $ = require('gulp-load-plugins')();
	 	
	 	// Compilation of the SASS stylesheets
	 	gulp.task('styles', function () {
	 	  return gulp.src('app/styles/emf.scss')
	 	    .pipe($.rubySass({
	 	      style: 'expanded',
	 	      precision: 10
	 	    }))
	 	    .pipe($.autoprefixer('last 1 version'))
	 	    .pipe(gulp.dest('.tmp/styles/'))
	 	    .pipe($.size());
	 	});
	 	
	 	// Creates a node.js server hosting the content of the website
	 	gulp.task('connect', ['styles'], function () {
	 	  var connect = require('connect');
	 	  var app = connect()
	 	    .use(require('connect-livereload')({ port: 35729 }))
	 	    .use(connect.static('app'))
	 	    .use(connect.static('.tmp'))
	 	    .use(connect.directory('app'));
	 	
	 	require('http').createServer(app)
	 	  .listen(9000)
	 	  .on('listening', function () {
	 	  	console.log('Started connect web server on http://localhost:9000');
	 	  });
	 	});
	 	
	 	// Watch the files to reload the server and process the files
	 	gulp.task('watch', function () {
	 	  $.livereload.listen();
	 	
	 	  gulp.watch([
	 	    'app/*.html',
	 	    '.tmp/styles/**/*.css',
	 	    'app/*.js',
	 	    'app/components/**/*',
	 	    'app/sections/**/*'
	 	  ]).on('change', function (file) {
	 	  	$.livereload.changed(file.path);
	 	  });
	 	
	 	  gulp.watch('app/styles/**/*.scss', ['styles']);
	 	  gulp.watch('app/components/**/*.scss', ['styles']);
	 	  gulp.watch('app/sections/**/*.js', ['scripts']);
	 	  gulp.watch('app/components/**/*.js', ['scripts']);
	 	});
	 	
	 	gulp.task('default', ['connect', 'watch'], function () {
	 	  require('opn')('http://localhost:9000');
	 	});
	 '''
}