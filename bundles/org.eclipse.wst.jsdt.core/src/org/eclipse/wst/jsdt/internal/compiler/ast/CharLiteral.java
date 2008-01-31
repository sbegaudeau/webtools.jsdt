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
package org.eclipse.wst.jsdt.internal.compiler.ast;

import org.eclipse.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.wst.jsdt.internal.compiler.impl.StringConstant;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;

public class CharLiteral extends NumberLiteral {
	public char value;
public CharLiteral(char[] token, int s, int e) {
	super(token, s, e);
//	computeValue();
}
public void computeConstant() {
	//The source is a  char[3] first and last char are '
	//This is true for both regular char AND unicode char
	//BUT not for escape char like '\b' which are char[4]....

	constant = StringConstant.fromValue(String.valueOf(source));
}
public TypeBinding literalType(BlockScope scope) {
	return scope.getJavaLangString();
}
public void traverse(ASTVisitor visitor, BlockScope blockScope) {
	visitor.visit(this, blockScope);
	visitor.endVisit(this, blockScope);
}
}
