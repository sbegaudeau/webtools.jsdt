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

package org.eclipse.wst.jsdt.internal.ui.text.correction;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IPackageDeclaration;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.ISourceRange;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.internal.corext.codemanipulation.StubUtility;
import org.eclipse.wst.jsdt.internal.corext.util.Messages;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.JavaPluginImages;
import org.eclipse.wst.jsdt.ui.text.java.IProblemLocation;

public class CorrectPackageDeclarationProposal extends CUCorrectionProposal {

	private IProblemLocation fLocation;

	public CorrectPackageDeclarationProposal(IJavaScriptUnit cu, IProblemLocation location, int relevance) {
		super(CorrectionMessages.CorrectPackageDeclarationProposal_name, cu, relevance,
			JavaPluginImages.get(JavaPluginImages.IMG_OBJS_PACKDECL));
		fLocation= location;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.ui.text.correction.CUCorrectionProposal#addEdits(org.eclipse.wst.jsdt.internal.corext.textmanipulation.TextBuffer)
	 */
	protected void addEdits(IDocument doc, TextEdit root) throws CoreException {
		super.addEdits(doc, root);

		IJavaScriptUnit cu= getCompilationUnit();

		IPackageFragment parentPack= (IPackageFragment) cu.getParent();
		IPackageDeclaration[] decls= cu.getPackageDeclarations();

		if (parentPack.isDefaultPackage() && decls.length > 0) {
			for (int i= 0; i < decls.length; i++) {
				ISourceRange range= decls[i].getSourceRange();
				root.addChild(new DeleteEdit(range.getOffset(), range.getLength()));
			}
			return;
		}
		if (!parentPack.isDefaultPackage() && decls.length == 0) {
			String lineDelim= StubUtility.getLineDelimiterUsed(cu);
			String str= "package " + parentPack.getElementName() + ';' + lineDelim + lineDelim; //$NON-NLS-1$
			root.addChild(new InsertEdit(0, str));
			return;
		}

		root.addChild(new ReplaceEdit(fLocation.getOffset(), fLocation.getLength(), parentPack.getElementName()));
	}

	/*
	 * @see ICompletionProposal#getDisplayString()
	 */
	public String getDisplayString() {
		IJavaScriptUnit cu= getCompilationUnit();
		IPackageFragment parentPack= (IPackageFragment) cu.getParent();
		try {
			IPackageDeclaration[] decls= cu.getPackageDeclarations();
			if (parentPack.isDefaultPackage() && decls.length > 0) {
				return Messages.format(CorrectionMessages.CorrectPackageDeclarationProposal_remove_description, decls[0].getElementName());
			}
			if (!parentPack.isDefaultPackage() && decls.length == 0) {
				return (Messages.format(CorrectionMessages.CorrectPackageDeclarationProposal_add_description,  parentPack.getElementName()));
			}
		} catch(JavaScriptModelException e) {
			JavaScriptPlugin.log(e);
		}
		return (Messages.format(CorrectionMessages.CorrectPackageDeclarationProposal_change_description, parentPack.getElementName()));
	}
}
