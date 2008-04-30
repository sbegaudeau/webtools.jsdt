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
package org.eclipse.wst.jsdt.internal.ui.text.correction;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.IBinding;
import org.eclipse.wst.jsdt.core.dom.ITypeBinding;
import org.eclipse.wst.jsdt.core.dom.Type;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.wst.jsdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.wst.jsdt.core.dom.rewrite.ListRewrite;
import org.eclipse.wst.jsdt.internal.corext.dom.Bindings;
import org.eclipse.wst.jsdt.internal.corext.util.Messages;
import org.eclipse.wst.jsdt.internal.ui.JavaPluginImages;

/**
 *
 */
public class ImplementInterfaceProposal extends LinkedCorrectionProposal {

	private IBinding fBinding;
	private JavaScriptUnit fAstRoot;
	private ITypeBinding fNewInterface;

	public ImplementInterfaceProposal(IJavaScriptUnit targetCU, ITypeBinding binding, JavaScriptUnit astRoot, ITypeBinding newInterface, int relevance) {
		super("", targetCU, null, relevance, JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_CHANGE)); //$NON-NLS-1$

		Assert.isTrue(binding != null && Bindings.isDeclarationBinding(binding));

		fBinding= binding;
		fAstRoot= astRoot;
		fNewInterface= newInterface;

		String[] args= { binding.getName(), Bindings.getRawName(newInterface) };
		setDisplayName(Messages.format(CorrectionMessages.ImplementInterfaceProposal_name, args));
	}

	protected ASTRewrite getRewrite() throws CoreException {
		ASTNode boundNode= fAstRoot.findDeclaringNode(fBinding);
		ASTNode declNode= null;
		JavaScriptUnit newRoot= fAstRoot;
		if (boundNode != null) {
			declNode= boundNode; // is same CU
		} else {
			newRoot= ASTResolving.createQuickFixAST(getCompilationUnit(), null);
			declNode= newRoot.findDeclaringNode(fBinding.getKey());
		}
		ImportRewrite imports= createImportRewrite(newRoot);
		
		if (declNode instanceof TypeDeclaration) {
			AST ast= declNode.getAST();
			ASTRewrite rewrite= ASTRewrite.create(ast);

			Type newInterface= imports.addImport(fNewInterface, ast);
			ListRewrite listRewrite= rewrite.getListRewrite(declNode, TypeDeclaration.SUPER_INTERFACE_TYPES_PROPERTY);
			listRewrite.insertLast(newInterface, null);

			// set up linked mode
			final String KEY_TYPE= "type"; //$NON-NLS-1$
			addLinkedPosition(rewrite.track(newInterface), true, KEY_TYPE);
				return rewrite;
		}
		return null;
	}


}
