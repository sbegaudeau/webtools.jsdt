/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.eval;

import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.internal.compiler.ClassFile;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFormatException;
import org.eclipse.wst.jsdt.internal.compiler.env.IBinaryType;
import org.eclipse.wst.jsdt.internal.compiler.env.INameEnvironment;
import org.eclipse.wst.jsdt.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.wst.jsdt.internal.compiler.impl.ITypeRequestor;

/**
 * An environment that wraps the client's name environment.
 * This wrapper always considers the wrapped environment then if the name is
 * not found, it search in the code snippet support. This includes the super class
 * org.eclipse.wst.jsdt.internal.eval.target.CodeSnippet as well as the global variable classes.
 */
public class CodeSnippetEnvironment implements INameEnvironment, EvaluationConstants {
	INameEnvironment env;
	EvaluationContext context;
/**
 * Creates a new wrapper for the given environment.
 */
public CodeSnippetEnvironment(INameEnvironment env, EvaluationContext context) {
	this.env = env;
	this.context = context;
}
/**
 * @see INameEnvironment#findType(char[][])
 */
public NameEnvironmentAnswer findType(char[][] compoundTypeName, ITypeRequestor requestor) {
	NameEnvironmentAnswer result = this.env.findType(compoundTypeName,requestor);
	if (result != null) {
		return result;
	}
	if (CharOperation.equals(compoundTypeName, ROOT_COMPOUND_NAME)) {
		IBinaryType binary = this.context.getRootCodeSnippetBinary();
		if (binary == null) {
			return null;
		} else {
			return new NameEnvironmentAnswer(binary, null /*no access restriction*/);
		}
	}
	VariablesInfo installedVars = this.context.installedVars;
	ClassFile[] classFiles = installedVars.classFiles;
	for (int i = 0; i < classFiles.length; i++) {
		ClassFile classFile = classFiles[i];
		if (CharOperation.equals(compoundTypeName, classFile.getCompoundName())) {
			ClassFileReader binary = null;
			try {
				binary = new ClassFileReader(classFile.getBytes(), null);
			} catch (ClassFormatException e) {
				e.printStackTrace();  // Should never happen since we compiled this type
				return null;
			}
			return new NameEnvironmentAnswer(binary, null /*no access restriction*/);
		}
	}
	return null;
}

public NameEnvironmentAnswer findBinding(char[] typeName, char[][] packageName, int type, ITypeRequestor requestor, boolean returnMultiple, String excludePath) {
	//TODO: implement
	throw new org.eclipse.wst.jsdt.core.UnimplementedException();
//	return findType(typeName,packageName);
}
/**
 * @see INameEnvironment#findType(char[], char[][])
 */
public NameEnvironmentAnswer findType(char[] typeName, char[][] packageName, ITypeRequestor requestor) {
	NameEnvironmentAnswer result = this.env.findType(typeName, packageName,requestor);
	if (result != null) {
		return result;
	}
	return findType(CharOperation.arrayConcat(packageName, typeName),requestor);
}
/**
 * @see INameEnvironment#isPackage(char[][], char[])
 */
public boolean isPackage(char[][] parentPackageName, char[] packageName) {
	return this.env.isPackage(parentPackageName, packageName);
}
public void cleanup() {
	this.env.cleanup();
}
}
