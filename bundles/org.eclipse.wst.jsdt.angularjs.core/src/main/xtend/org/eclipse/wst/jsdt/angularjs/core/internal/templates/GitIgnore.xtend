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
 * Generator for .gitignore.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
class GitIgnore {
	/**
	 * This operation will generate the content of the file.
	 */
	 def generateFile() '''
	 	.sass-cache
	 	.tmp
	 	node_modules
	 	dist
	 	app/bower_components
	 	tests/unit/coverage
	 	checkstyle.xml
	 '''
}