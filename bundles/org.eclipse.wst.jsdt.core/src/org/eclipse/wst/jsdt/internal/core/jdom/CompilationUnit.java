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
package org.eclipse.wst.jsdt.internal.core.jdom;

import org.eclipse.wst.jsdt.internal.compiler.env.ICompilationUnit;

/**
 * Implements a very simple version of the ICompilationUnit.
 *
 * <p>Please do not use outside of jdom.</p>
 */
public class CompilationUnit implements ICompilationUnit {
	protected char[] fContents;
	protected char[] fFileName;
	protected char[] fMainTypeName;
	protected char [][]fPackageName;
public CompilationUnit(char[] contents, char[] filename,char [][]packageName) {
	fContents = contents;
	fFileName = filename;
	fPackageName=packageName;

	String file = new String(filename);
	int start = file.lastIndexOf("/") + 1; //$NON-NLS-1$
	if (start == 0 || start < file.lastIndexOf("\\")) //$NON-NLS-1$
		start = file.lastIndexOf("\\") + 1; //$NON-NLS-1$

	int end = file.lastIndexOf("."); //$NON-NLS-1$
	if (end == -1)
		end = file.length();

	fMainTypeName = file.substring(start, end).toCharArray();
}
public CompilationUnit(char[] contents, char[] filename) {
	this(contents,filename,null);
}

public char[] getContents() {
	return fContents;
}
/**
 * @see org.eclipse.wst.jsdt.internal.compiler.env.IDependent#getFileName()
 */
public char[] getFileName() {
	return fFileName;
}
public char[] getMainTypeName() {
	return fMainTypeName;
}
public char[][] getPackageName() {
	return fPackageName;
}
public String toString() {
	return "CompilationUnit[" + new String(fFileName) + "]";  //$NON-NLS-2$ //$NON-NLS-1$
}
}
