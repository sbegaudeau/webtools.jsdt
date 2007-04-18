/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Dmitry Stalnov (dstalnov@fusionone.com) - contributed fixes for:
 *       o Allow 'this' constructor to be inlined  
 *         (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=38093)
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.corext.refactoring.code;

import java.util.List;

import org.eclipse.core.runtime.Assert;

import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.ConstructorInvocation;
import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.IMethodBinding;
import org.eclipse.wst.jsdt.core.dom.MethodInvocation;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.SuperMethodInvocation;

public class Invocations {

	public static List getArguments(ASTNode invocation) {
		switch(invocation.getNodeType()) {
			case ASTNode.METHOD_INVOCATION:
				return ((MethodInvocation)invocation).arguments();
			case ASTNode.SUPER_METHOD_INVOCATION:
				return ((SuperMethodInvocation)invocation).arguments();
			case ASTNode.CONSTRUCTOR_INVOCATION:
				return ((ConstructorInvocation)invocation).arguments();
			default:
				Assert.isTrue(false, "Should not happen."); //$NON-NLS-1$
				return null;
		}
	}

	public static Expression getExpression(ASTNode invocation) {
		switch(invocation.getNodeType()) {
			case ASTNode.METHOD_INVOCATION:
				return ((MethodInvocation)invocation).getExpression();
			case ASTNode.SUPER_METHOD_INVOCATION:
			case ASTNode.CONSTRUCTOR_INVOCATION:
				return null;
			default:
				Assert.isTrue(false, "Should not happen."); //$NON-NLS-1$
				return null;
		}
	}
	
	public static boolean isInvocation(ASTNode node) {
		int type= node.getNodeType();
		return type == ASTNode.METHOD_INVOCATION || type == ASTNode.SUPER_METHOD_INVOCATION || 
			type == ASTNode.CONSTRUCTOR_INVOCATION;
	}
	
	public static IMethodBinding resolveBinding(ASTNode invocation) {
		switch(invocation.getNodeType()) {
			case ASTNode.METHOD_INVOCATION:
				SimpleName name = ((MethodInvocation)invocation).getName();
				if (name!=null)
				return (IMethodBinding)name.resolveBinding();
				else
					return ((MethodInvocation)invocation).resolveMethodBinding();
			case ASTNode.SUPER_METHOD_INVOCATION:
				return (IMethodBinding)((SuperMethodInvocation)invocation).getName().resolveBinding();
			case ASTNode.CONSTRUCTOR_INVOCATION:
				return ((ConstructorInvocation)invocation).resolveConstructorBinding();
			default:
				Assert.isTrue(false, "Should not happen."); //$NON-NLS-1$
				return null;
		}
	}
}
