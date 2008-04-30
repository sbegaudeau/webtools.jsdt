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
package org.eclipse.wst.jsdt.internal.core;

import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptModelStatusConstants;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.wst.jsdt.internal.core.util.Messages;

/**
 * <p>This operation creates a initializer in a type.
 *
 * <p>Required Attributes:<ul>
 *  <li>Containing Type
 *  <li>The source code for the initializer. No verification of the source is
 *      performed.
 * </ul>
 */
public class CreateInitializerOperation extends CreateTypeMemberOperation {
	/**
	 * The current number of initializers in the parent type.
	 * Used to retrieve the handle of the newly created initializer.
	 */
	protected int numberOfInitializers= 1;
/**
 * When executed, this operation will create an initializer with the given name
 * in the given type with the specified source.
 *
 * <p>By default the new initializer is positioned after the last existing initializer
 * declaration, or as the first member in the type if there are no
 * initializers.
 */
public CreateInitializerOperation(IType parentElement, String source) {
	super(parentElement, source, false);
}
protected ASTNode generateElementAST(ASTRewrite rewriter, IDocument document, IJavaScriptUnit cu) throws JavaScriptModelException {
	ASTNode node = super.generateElementAST(rewriter, document, cu);
	if (node.getNodeType() != ASTNode.INITIALIZER)
		throw new JavaScriptModelException(new JavaModelStatus(IJavaScriptModelStatusConstants.INVALID_CONTENTS));
	return node;
}
/**
 * @see CreateElementInCUOperation#generateResultHandle
 */
protected IJavaScriptElement generateResultHandle() {
	try {
		//update the children to be current
		getType().getJavaScriptUnit().close();
		if (this.anchorElement == null) {
			return getType().getInitializer(this.numberOfInitializers);
		} else {
			IJavaScriptElement[] children = getType().getChildren();
			int count = 0;
			for (int i = 0; i < children.length; i++) {
				IJavaScriptElement child = children[i];
				if (child.equals(this.anchorElement)) {
					if (child .getElementType() == IJavaScriptElement.INITIALIZER && this.insertionPolicy == CreateElementInCUOperation.INSERT_AFTER) {
						count++;
					}
					return getType().getInitializer(count);
				} else
					if (child.getElementType() == IJavaScriptElement.INITIALIZER) {
						count++;
					}
			}
		}
	} catch (JavaScriptModelException e) {
		// type doesn't exist: ignore
	}
	return null;
}
/**
 * @see CreateElementInCUOperation#getMainTaskName()
 */
public String getMainTaskName(){
	return Messages.operation_createInitializerProgress;
}
protected SimpleName rename(ASTNode node, SimpleName newName) {
	return null; // intializer cannot be renamed
}
/**
 * By default the new initializer is positioned after the last existing initializer
 * declaration, or as the first member in the type if there are no
 * initializers.
 */
protected void initializeDefaultPosition() {
	IType parentElement = getType();
	try {
		IJavaScriptElement[] elements = parentElement.getInitializers();
		if (elements != null && elements.length > 0) {
			this.numberOfInitializers = elements.length;
			createAfter(elements[elements.length - 1]);
		} else {
			elements = parentElement.getChildren();
			if (elements != null && elements.length > 0) {
				createBefore(elements[0]);
			}
		}
	} catch (JavaScriptModelException e) {
		// type doesn't exist: ignore
	}
}
}
