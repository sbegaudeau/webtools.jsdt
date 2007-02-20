/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.formatter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.ASTVisitor;
import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.MethodInvocation;
import org.eclipse.wst.jsdt.core.dom.SuperMethodInvocation;

class MethodInvocationFragmentBuilder
	extends ASTVisitor {
		
	ArrayList fragmentsList;

	MethodInvocationFragmentBuilder() {
		this.fragmentsList = new ArrayList();
	}

	public List fragments() {
		return this.fragmentsList;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.compiler.ASTVisitor#visit(org.eclipse.wst.jsdt.internal.compiler.ast.MessageSend, org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope)
	 */
	public boolean visit(MethodInvocation methodInvocation) {
		final Expression expression = methodInvocation.getExpression();
		if (expression != null) {
			switch(expression.getNodeType()) {
				case ASTNode.METHOD_INVOCATION :
				case ASTNode.SUPER_METHOD_INVOCATION :
					expression.accept(this);
					break;
				default:
					this.fragmentsList.add(expression);
			}
		}
		this.fragmentsList.add(methodInvocation);
		return false;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.compiler.ASTVisitor#visit(org.eclipse.wst.jsdt.internal.compiler.ast.MessageSend, org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope)
	 */
	public boolean visit(SuperMethodInvocation methodInvocation) {
		this.fragmentsList.add(methodInvocation);
		return false;
	}
}
