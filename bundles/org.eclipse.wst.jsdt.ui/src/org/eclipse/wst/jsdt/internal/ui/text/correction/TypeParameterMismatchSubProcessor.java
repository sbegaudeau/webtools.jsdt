/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.text.correction;

import java.util.Collection;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.ParameterizedType;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.wst.jsdt.internal.corext.dom.ASTNodes;
import org.eclipse.wst.jsdt.internal.ui.JavaPluginImages;
import org.eclipse.wst.jsdt.ui.text.java.IInvocationContext;
import org.eclipse.wst.jsdt.ui.text.java.IProblemLocation;


public class TypeParameterMismatchSubProcessor {

	public static void getTypeParameterMismatchProposals(IInvocationContext context, IProblemLocation problem, Collection proposals) {
		JavaScriptUnit astRoot= context.getASTRoot();
		ASTNode selectedNode= problem.getCoveredNode(astRoot);
		if (!(selectedNode instanceof SimpleName)) {
			return;
		}

		ASTNode normalizedNode= ASTNodes.getNormalizedNode(selectedNode);
		if (!(normalizedNode instanceof ParameterizedType)) {
			return;
		}
		// waiting for result of https://bugs.eclipse.org/bugs/show_bug.cgi?id=81544


	}
	
	public static void removeMismatchedParameters(IInvocationContext context, IProblemLocation problem, Collection proposals){
		IJavaScriptUnit cu= context.getCompilationUnit();
		ASTNode selectedNode= problem.getCoveredNode(context.getASTRoot());
		if (!(selectedNode instanceof SimpleName)) {
			return;
		}
		
		ASTNode normalizedNode=ASTNodes.getNormalizedNode(selectedNode);
		if (normalizedNode instanceof ParameterizedType) {
			ASTRewrite rewrite = ASTRewrite.create(normalizedNode.getAST());
			ParameterizedType pt = (ParameterizedType) normalizedNode;
			ASTNode mt = rewrite.createMoveTarget(pt.getType());
			rewrite.replace(pt, mt, null);
			String label= CorrectionMessages.TypeParameterMismatchSubProcessor_removeTypeParameter;
			Image image= JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_CHANGE);
			ASTRewriteCorrectionProposal proposal= new ASTRewriteCorrectionProposal(label, cu, rewrite, 6, image);
			proposals.add(proposal);
		}
	}
	
}
