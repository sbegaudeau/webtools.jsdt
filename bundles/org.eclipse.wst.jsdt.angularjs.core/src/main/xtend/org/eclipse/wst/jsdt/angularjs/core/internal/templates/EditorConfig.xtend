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
 * Generator for .editoconfig.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
class EditorConfig {
	/**
	 * This operation will return the content of the file.
	 */
	def generateFile() '''
		root = true
		
		[*]
		indent_style = space
		indent_size = 2
		end_of_line = lf
		charset = utf-8
		trim_trailing_whitespace = true
		insert_final_newline = true
	'''
}