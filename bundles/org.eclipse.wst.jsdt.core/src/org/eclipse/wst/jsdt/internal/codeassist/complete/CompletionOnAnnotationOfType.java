/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.codeassist.complete;

import org.eclipse.wst.jsdt.internal.compiler.CompilationResult;
import org.eclipse.wst.jsdt.internal.compiler.ast.ASTNode;
import org.eclipse.wst.jsdt.internal.compiler.ast.TypeDeclaration;

public class CompletionOnAnnotationOfType extends TypeDeclaration {
	public ASTNode potentialAnnotatedNode;
	// During recovery a parameter can be parsed as a FieldDeclaration instead of Argument.
	// 'isParameter' is set to true in this case.
	public boolean isParameter;
		public CompletionOnAnnotationOfType(char[] typeName, CompilationResult compilationResult){
		super(compilationResult);
		this.name = typeName;
	}

	public StringBuffer print(int indent, StringBuffer output) {
		return new StringBuffer();
	}
}
