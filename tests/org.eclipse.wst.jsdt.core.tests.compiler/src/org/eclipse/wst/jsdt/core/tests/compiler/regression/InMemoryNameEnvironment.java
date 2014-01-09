/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import org.eclipse.wst.jsdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.wst.jsdt.internal.compiler.env.*;
import org.eclipse.wst.jsdt.internal.compiler.impl.ITypeRequestor;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Binding;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.internal.compiler.util.HashtableOfObject;

public class InMemoryNameEnvironment implements INameEnvironment {
	INameEnvironment[] classLibs;
	HashtableOfObject compilationUnits = new HashtableOfObject();
public InMemoryNameEnvironment(String[] compilationUnits, INameEnvironment[] classLibs) {
	this.classLibs = classLibs;
	for (int i = 0, length = compilationUnits.length - 1; i < length; i += 2) {
		String fileName = compilationUnits[i];
		char[] contents = compilationUnits[i + 1].toCharArray();
		String dirName = "";
		int lastSlash = -1;
		if ((lastSlash = fileName.lastIndexOf('/')) != -1) {
			dirName = fileName.substring(0, lastSlash);
		}
		char[] packageName = dirName.replace('/', '.').toCharArray();
		char[] cuName = fileName.substring(lastSlash == -1 ? 0 : lastSlash + 1, fileName.length() - 3).toCharArray(); // remove ".java"
		HashtableOfObject cus = (HashtableOfObject)this.compilationUnits.get(packageName);
		if (cus == null) {
			cus = new HashtableOfObject();
			this.compilationUnits.put(packageName, cus);
		}
		CompilationUnit unit = new CompilationUnit(contents, fileName, null);
		cus.put(cuName, unit);
	}
}
public NameEnvironmentAnswer findType(char[][] compoundTypeName, ITypeRequestor requestor) {
	return findType(
		compoundTypeName[compoundTypeName.length - 1],
		CharOperation.subarray(compoundTypeName, 0, compoundTypeName.length - 1),requestor);
}
public NameEnvironmentAnswer findBinding(char[] typeName, char[][] packageName, int type, ITypeRequestor requestor, boolean returnMultiple, String excludePath) {
  if ((type&Binding.COMPILATION_UNIT)!=0)
  {
			HashtableOfObject cus = (HashtableOfObject)this.compilationUnits.get(CharOperation.concatWith(packageName, '.'));
			if (cus!=null)
			{
				CompilationUnit unit = (CompilationUnit)cus.get(typeName);
				unit.packageName=packageName;
				if (unit != null) 
					return new NameEnvironmentAnswer(unit, null /*no access restriction*/);
				
			}
  }
	
	//	HashtableOfObject cus = (HashtableOfObject)this.compilationUnits.get(CharOperation.concatWith(packageName, '.'));
//	if (cus == null) {
		return this.findTypeFromClassLibs(typeName, packageName,type,requestor);
//	}
//	JavaScriptUnit unit = (JavaScriptUnit)cus.get(typeName);
//	if (unit == null) {
//		return this.findTypeFromClassLibs(typeName, packageName,type,requestor);
//	}
//	return new NameEnvironmentAnswer(unit, null /*no access restriction*/);
}

public NameEnvironmentAnswer findType(char[] typeName, char[][] packageName, ITypeRequestor requestor) {
	return findBinding(typeName, packageName, Binding.TYPE|Binding.PACKAGE,requestor, false, null);
//	HashtableOfObject cus = (HashtableOfObject)this.compilationUnits.get(CharOperation.concatWith(packageName, '.'));
//	if (cus == null) {
//		return this.findTypeFromClassLibs(typeName, packageName);
//	}
//	JavaScriptUnit unit = (JavaScriptUnit)cus.get(typeName);
//	if (unit == null) {
//		return this.findTypeFromClassLibs(typeName, packageName);
//	}
//	return new NameEnvironmentAnswer(unit, null /*no access restriction*/);
}
private NameEnvironmentAnswer findTypeFromClassLibs(char[] typeName, char[][] packageName, int type, ITypeRequestor requestor) {
	for (int i = 0; i < this.classLibs.length; i++) {
		NameEnvironmentAnswer answer = this.classLibs[i].findBinding(typeName, packageName, type,requestor, false, null);
		if (answer != null) {
			return answer;
		}
	}
	return null;
}
public boolean isPackage(char[][] parentPackageName, char[] packageName) {
	char[] pkg = CharOperation.concatWith(parentPackageName, packageName, '.');
	return 
		this.compilationUnits.get(pkg) != null || 
		this.isPackageFromClassLibs(parentPackageName, packageName);
}
public boolean isPackageFromClassLibs(char[][] parentPackageName, char[] packageName) {
	for (int i = 0; i < this.classLibs.length; i++) {
		if (this.classLibs[i].isPackage(parentPackageName, packageName)) {
			return true;
		}
	}
	return false;
} 
public void cleanup() {
	for (int i = 0, max = this.classLibs.length; i < max; i++) {
		this.classLibs[i].cleanup();
	}
	this.compilationUnits = new HashtableOfObject();
}
}
