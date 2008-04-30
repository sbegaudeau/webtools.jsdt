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
package org.eclipse.wst.jsdt.internal.compiler.ast;

import org.eclipse.wst.jsdt.core.ast.IASTNode;
import org.eclipse.wst.jsdt.core.ast.ILongLiteralMinValue;
import org.eclipse.wst.jsdt.internal.compiler.impl.Constant;
import org.eclipse.wst.jsdt.internal.compiler.impl.LongConstant;

public class LongLiteralMinValue extends LongLiteral implements ILongLiteralMinValue {

	final static char[] CharValue = new char[]{'-', '9','2','2','3','3','7','2','0','3','6','8','5','4','7','7','5','8','0','8','L'};
	final static Constant MIN_VALUE = LongConstant.fromValue(Long.MIN_VALUE) ;

public LongLiteralMinValue(){
	super(CharValue,0,0);
	constant = MIN_VALUE;
}
public void computeConstant() {

	/*precomputed at creation time*/}
public int getASTType() {
	return IASTNode.LONG_LITERAL_MIN_VALUE;

}
}
