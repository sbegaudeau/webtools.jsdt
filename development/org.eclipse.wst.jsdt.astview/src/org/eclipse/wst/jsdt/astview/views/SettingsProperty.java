/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.astview.views;

import org.eclipse.swt.graphics.Image;

import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;

public class SettingsProperty extends ASTAttribute {
	
	private final JavaScriptUnit fRoot;

	public SettingsProperty(JavaScriptUnit root) {
		fRoot= root;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.astview.views.ASTAttribute#getParent()
	 */
	public Object getParent() {
		return fRoot;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.astview.views.ASTAttribute#getChildren()
	 */
	public Object[] getChildren() {
		AST ast= fRoot.getAST();
		Object[] res= {
				new GeneralAttribute(this, "apiLevel", String.valueOf(ast.apiLevel())),
				new GeneralAttribute(this, "hasResolvedBindings", String.valueOf(ast.hasResolvedBindings())),
				new GeneralAttribute(this, "hasStatementsRecovery", String.valueOf(ast.hasStatementsRecovery())),
				new GeneralAttribute(this, "hasBindingsRecovery", String.valueOf(ast.hasBindingsRecovery())),
		};
		return res;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.astview.views.ASTAttribute#getLabel()
	 */
	public String getLabel() {
		return "> AST settings";  //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.astview.views.ASTAttribute#getImage()
	 */
	public Image getImage() {
		return null;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !obj.getClass().equals(getClass())) {
			return false;
		}
		return true;
	}
	
	/*
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return 19;
	}
}
