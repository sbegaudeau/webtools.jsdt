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
package org.eclipse.wst.jsdt.core.tests.dom;

import junit.framework.Assert;
import org.eclipse.wst.jsdt.core.dom.ASTVisitor;
import org.eclipse.wst.jsdt.core.dom.ClassInstanceCreation;
import org.eclipse.wst.jsdt.core.dom.IFunctionBinding;

class GetKeyVisitor extends ASTVisitor {
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#visit(org.eclipse.wst.jsdt.core.dom.SimpleName)
	 */
	public boolean visit(ClassInstanceCreation node) {
		IFunctionBinding constructorBinding = node.resolveConstructorBinding();
		Assert.assertNotNull(constructorBinding);
		Assert.assertNotNull(constructorBinding.getKey());
		return super.visit(node);
	}
}
