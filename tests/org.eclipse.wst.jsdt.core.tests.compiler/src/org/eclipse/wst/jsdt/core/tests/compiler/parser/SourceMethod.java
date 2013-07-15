/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler.parser;

import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.wst.jsdt.internal.compiler.env.ISourceMethod;

public class SourceMethod implements ISourceMethod {
	private int modifiers;
	private int declarationStart;
	private int declarationEnd;
	private char[] returnTypeName;
	private char[] selector;
	private int nameSourceStart;
	private int nameSourceEnd;
	private char[][] argumentTypeNames;
	private char[][] argumentNames;
	private char[] source;
	private String explicitConstructorCall;
	private int numberOfMemberMethods;
	private SourceMethod[] memberMethods;
	SourceMethod parent;
	
public SourceMethod(
	int declarationStart,
	int modifiers,
	char[] returnTypeName,
	char[] selector,
	int nameSourceStart,
	int nameSourceEnd,
	char[][] argumentTypeNames,
	char[][] argumentNames,
	char[] source) {

	this.declarationStart = declarationStart;
	this.modifiers = modifiers;
	this.returnTypeName = returnTypeName;
	this.selector = selector;
	this.nameSourceStart = nameSourceStart;
	this.nameSourceEnd = nameSourceEnd;
	this.argumentTypeNames = argumentTypeNames;
	this.argumentNames = argumentNames;
	this.source = source;
}
public String displayModifiers() {
	StringBuffer buffer = new StringBuffer();

	if (this.modifiers == 0)
		return null;
	if ((this.modifiers & ClassFileConstants.AccPublic) != 0)
		buffer.append("public ");
	if ((this.modifiers & ClassFileConstants.AccProtected) != 0)
		buffer.append("protected ");
	if ((this.modifiers & ClassFileConstants.AccPrivate) != 0)
		buffer.append("private ");
	if ((this.modifiers & ClassFileConstants.AccFinal) != 0)
		buffer.append("final ");
	if ((this.modifiers & ClassFileConstants.AccStatic) != 0)
		buffer.append("static ");
	if ((this.modifiers & ClassFileConstants.AccAbstract) != 0)
		buffer.append("abstract ");
	if ((this.modifiers & ClassFileConstants.AccNative) != 0)
		buffer.append("native ");
	if (buffer.toString().trim().equals(""))
		return null;
	return buffer.toString().trim();
}
protected void addMemberMethod(SourceMethod sourceMemberMethod) {
	if(memberMethods == null) {
		memberMethods = new SourceMethod[4];
	}

	if(numberOfMemberMethods == memberMethods.length) {
		System.arraycopy(memberMethods, 0, memberMethods = new SourceMethod[numberOfMemberMethods * 2], 0, numberOfMemberMethods);
	}
	memberMethods[numberOfMemberMethods++] = sourceMemberMethod;
}

public SourceMethod[] getMemberMethods() {
	if (memberMethods != null && memberMethods.length != numberOfMemberMethods) {
		System.arraycopy(
				memberMethods, 
			0, 
			memberMethods = new SourceMethod[numberOfMemberMethods], 
			0, 
			numberOfMemberMethods); 
	}
	return memberMethods;
}

public String getActualName() {
	StringBuffer buffer = new StringBuffer();
	buffer.append(source, nameSourceStart, nameSourceEnd - nameSourceStart + 1);
	return buffer.toString();
}
public char[][] getArgumentNames() {
	return argumentNames;
}
public char[][] getArgumentTypeNames() {
	return argumentTypeNames;
}
public int getDeclarationSourceEnd() {
	return declarationEnd;
}
public int getDeclarationSourceStart() {
	return declarationStart;
}
public int getModifiers() {
	return modifiers;
}
public int getNameSourceEnd() {
	return nameSourceEnd;
}
public int getNameSourceStart() {
	return nameSourceStart;
}
public char[] getReturnTypeName() {
	return returnTypeName;
}
public char[] getSelector() {
	return selector;
}
public boolean isConstructor() {
	return returnTypeName == null;
}
protected void setDeclarationSourceEnd(int position) {
	declarationEnd = position;
}
protected void setExplicitConstructorCall(String s) {
	explicitConstructorCall = s;
}
public String tabString(int tab) {
	/*slow code*/

	String s = "";
	for (int i = tab; i > 0; i--)
		s = s + "\t";
	return s;
}
public String toString() {
	return toString(0);
}
public String toString(int tab) {
	StringBuffer buffer = new StringBuffer();
	buffer.append(tabString(tab));
	String displayModifiers = displayModifiers();
	if (displayModifiers != null) {
		buffer.append(displayModifiers).append(" ");
	}
	if (returnTypeName != null) {
		buffer.append(returnTypeName).append(" ");
	}
	buffer.append("function ").append(selector).append("(");
	if (argumentTypeNames != null) {
		for (int i = 0, max = argumentTypeNames.length; i < max; i++) {
			buffer.append(argumentTypeNames[i]).append(" ").append(
				argumentNames[i]).append(
				", "); 
		}
	}
	buffer.append(") ");
	buffer.append("{");
	if (explicitConstructorCall != null) {
		buffer.append("\n").append(tabString(tab+1)).append(explicitConstructorCall).append(tabString(tab)).append("}");
	}
	if (this.numberOfMemberMethods>0)
	{
		for (int i = 0; i < numberOfMemberMethods; i++) {
			buffer.append("\n").append(memberMethods[i].toString(tab+1));
		}
		buffer.append("\n").append(tabString(tab));
	}
	buffer.append("}");
	return buffer.toString();
}
}
