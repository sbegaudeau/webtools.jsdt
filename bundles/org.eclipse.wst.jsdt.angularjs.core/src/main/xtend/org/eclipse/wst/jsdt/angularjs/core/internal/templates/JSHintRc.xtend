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
 * Generator for .jshintrc.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
class JSHintRc {
	/**
	 * This operation will generate the content of the file.
	 */
	 def generateFile() '''
	 	{
	 	  "node": true,
	 	  "browser": true,
	 	  "esnext": true,
	 	  "bitwise": true,
	 	  "camelcase": true,
	 	  "curly": true,
	 	  "eqeqeq": true,
	 	  "immed": true,
	 	  "indent": 2,
	 	  "latedef": true,
	 	  "newcap": true,
	 	  "noarg": true,
	 	  "quotmark": "single",
	 	  "undef": true,
	 	  "unused": true,
	 	  "strict": true,
	 	  "trailing": true,
	 	  "smarttabs": true,
	 	  "jquery": true,
	 	  "predef": ["angular"]
	 	}
	 '''
}