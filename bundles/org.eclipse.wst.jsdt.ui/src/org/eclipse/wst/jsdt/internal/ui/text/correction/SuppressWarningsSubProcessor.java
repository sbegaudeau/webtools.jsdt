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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.jsdt.core.CorrectionEngine;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.Annotation;
import org.eclipse.wst.jsdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.wst.jsdt.core.dom.ArrayInitializer;
import org.eclipse.wst.jsdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.EnumConstantDeclaration;
import org.eclipse.wst.jsdt.core.dom.EnumDeclaration;
import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.FieldDeclaration;
import org.eclipse.wst.jsdt.core.dom.ITypeBinding;
import org.eclipse.wst.jsdt.core.dom.Initializer;
import org.eclipse.wst.jsdt.core.dom.MemberValuePair;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.NormalAnnotation;
import org.eclipse.wst.jsdt.core.dom.SingleMemberAnnotation;
import org.eclipse.wst.jsdt.core.dom.SingleVariableDeclaration;
import org.eclipse.wst.jsdt.core.dom.StringLiteral;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationFragment;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.wst.jsdt.core.dom.rewrite.ListRewrite;
import org.eclipse.wst.jsdt.internal.corext.util.Messages;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.JavaPluginImages;
import org.eclipse.wst.jsdt.ui.text.java.IInvocationContext;
import org.eclipse.wst.jsdt.ui.text.java.IProblemLocation;

/**
 * 
 */
public class SuppressWarningsSubProcessor {
	
	private static final String ADD_SUPPRESSWARNINGS_ID= "org.eclipse.wst.jsdt.ui.correction.addSuppressWarnings"; //$NON-NLS-1$

	public static final boolean hasSuppressWarningsProposal(int problemId) {
		return CorrectionEngine.getWarningToken(problemId) != null; // Suppress warning annotations
	}
	
	
	public static void addSuppressWarningsProposals(IInvocationContext context, IProblemLocation problem, Collection proposals) {
		if (problem.isError()) {
			return;
		}
		String warningToken= CorrectionEngine.getWarningToken(problem.getProblemId());
		if (warningToken == null) {
			return;
		}
		for (Iterator iter= proposals.iterator(); iter.hasNext();) {
			Object element= iter.next();
			if (element instanceof SuppressWarningsProposal && warningToken.equals(((SuppressWarningsProposal) element).getWarningToken())) {
				return; // only one at a time
			}
		}
		
		ASTNode node= problem.getCoveringNode(context.getASTRoot());
		if (node == null) {
			return;
		}
		if (node.getLocationInParent() == VariableDeclarationFragment.NAME_PROPERTY) {
			ASTNode parent= node.getParent();
			if (parent.getLocationInParent() == VariableDeclarationStatement.FRAGMENTS_PROPERTY) {
				addSuppressWarningsProposal(context.getCompilationUnit(), parent.getParent(), warningToken, -2, proposals);
				return;
			}
		} else if (node.getLocationInParent() == SingleVariableDeclaration.NAME_PROPERTY) {
			addSuppressWarningsProposal(context.getCompilationUnit(), node.getParent(), warningToken, -2, proposals);
			return;
		} else if (node.getLocationInParent() == VariableDeclarationFragment.INITIALIZER_PROPERTY) {
			node= ASTResolving.findParentBodyDeclaration(node);
			if (node instanceof FieldDeclaration) {
				node= node.getParent();
			}
		}
		
		ASTNode target= ASTResolving.findParentBodyDeclaration(node);
		if (target instanceof Initializer) { 
			target= ASTResolving.findParentBodyDeclaration(target.getParent());
		}
		if (target != null) {
			addSuppressWarningsProposal(context.getCompilationUnit(), target, warningToken, -3, proposals);
		}
	}
	
	private static String getFirstFragmentName(List fragments) {
		if (fragments.size() > 0) {
			return ((VariableDeclarationFragment) fragments.get(0)).getName().getIdentifier();
		}
		return new String();
	}
	
	
	private static class SuppressWarningsProposal extends ASTRewriteCorrectionProposal {
		
		private final String fWarningToken;
		private final ASTNode fNode;
		private final ChildListPropertyDescriptor fProperty;

		public SuppressWarningsProposal(String warningToken, String label, IJavaScriptUnit cu, ASTNode node, ChildListPropertyDescriptor property, int relevance) {
			super(label, cu, null, relevance, JavaPluginImages.get(JavaPluginImages.IMG_OBJS_ANNOTATION));
			fWarningToken= warningToken;
			fNode= node;
			fProperty= property;
			setCommandId(ADD_SUPPRESSWARNINGS_ID);
		}
		
		/**
		 * @return Returns the warningToken.
		 */
		public String getWarningToken() {
			return fWarningToken;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.wst.jsdt.internal.ui.text.correction.ASTRewriteCorrectionProposal#getRewrite()
		 */
		protected ASTRewrite getRewrite() throws CoreException {
			AST ast= fNode.getAST();
			ASTRewrite rewrite= ASTRewrite.create(ast);
			
			StringLiteral newStringLiteral= ast.newStringLiteral();
			newStringLiteral.setLiteralValue(fWarningToken);
			
			Annotation existing= findExistingAnnotation((List) fNode.getStructuralProperty(fProperty));
			if (existing == null) {
				ListRewrite listRewrite= rewrite.getListRewrite(fNode, fProperty);
				
				SingleMemberAnnotation newAnnot= ast.newSingleMemberAnnotation();
				String importString= createImportRewrite((JavaScriptUnit) fNode.getRoot()).addImport("java.lang.SuppressWarnings"); //$NON-NLS-1$
				newAnnot.setTypeName(ast.newName(importString));

				newAnnot.setValue(newStringLiteral);
				
				listRewrite.insertFirst(newAnnot, null);
			} else if (existing instanceof SingleMemberAnnotation) {
				SingleMemberAnnotation annotation= (SingleMemberAnnotation) existing;
				Expression value= annotation.getValue();
				if (!addSuppressArgument(rewrite, value, newStringLiteral)) {
					rewrite.set(existing, SingleMemberAnnotation.VALUE_PROPERTY, newStringLiteral, null);
				}
			} else if (existing instanceof NormalAnnotation) {
				NormalAnnotation annotation= (NormalAnnotation) existing;
				Expression value= findValue(annotation.values());
				if (!addSuppressArgument(rewrite, value, newStringLiteral)) {
					ListRewrite listRewrite= rewrite.getListRewrite(annotation, NormalAnnotation.VALUES_PROPERTY);
					MemberValuePair pair= ast.newMemberValuePair();
					pair.setName(ast.newSimpleName("value")); //$NON-NLS-1$
					pair.setValue(newStringLiteral);
					listRewrite.insertFirst(pair, null);
				}	
			}
			return rewrite;
		}
		
		private static boolean addSuppressArgument(ASTRewrite rewrite, Expression value, StringLiteral newStringLiteral) {
			if (value instanceof ArrayInitializer) {
				ListRewrite listRewrite= rewrite.getListRewrite(value, ArrayInitializer.EXPRESSIONS_PROPERTY);
				listRewrite.insertLast(newStringLiteral, null);
			} else if (value instanceof StringLiteral) {
				ArrayInitializer newArr= rewrite.getAST().newArrayInitializer();
				newArr.expressions().add(rewrite.createMoveTarget(value));
				newArr.expressions().add(newStringLiteral);
				rewrite.replace(value, newArr, null);
			} else {
				return false;
			}
			return true;
		}
		
		private static Expression findValue(List keyValues) {
			for (int i= 0, len= keyValues.size(); i < len; i++) {
				MemberValuePair curr= (MemberValuePair) keyValues.get(i);
				if ("value".equals(curr.getName().getIdentifier())) { //$NON-NLS-1$
					return curr.getValue();
				}
			}
			return null;
		}
		
		private static Annotation findExistingAnnotation(List modifiers) {
			for (int i= 0, len= modifiers.size(); i < len; i++) {
				Object curr= modifiers.get(i);
				if (curr instanceof NormalAnnotation || curr instanceof SingleMemberAnnotation) {
					Annotation annotation= (Annotation) curr;
					ITypeBinding typeBinding= annotation.resolveTypeBinding();
					if (typeBinding != null) {
						if ("java.lang.SuppressWarnings".equals(typeBinding.getQualifiedName())) { //$NON-NLS-1$
							return annotation;
						}
					} else {
						String fullyQualifiedName= annotation.getTypeName().getFullyQualifiedName();
						if ("SuppressWarnings".equals(fullyQualifiedName) || "java.lang.SuppressWarnings".equals(fullyQualifiedName)) { //$NON-NLS-1$ //$NON-NLS-2$
							return annotation;
						}
					}
				}
			}
			return null;
		}
	}
	
	private static void addSuppressWarningsProposal(IJavaScriptUnit cu, ASTNode node, String warningToken, int relevance, Collection proposals) {

		ChildListPropertyDescriptor property= null;
		String name;
		switch (node.getNodeType()) {
			case ASTNode.SINGLE_VARIABLE_DECLARATION:
				property= SingleVariableDeclaration.MODIFIERS2_PROPERTY;
				name= ((SingleVariableDeclaration) node).getName().getIdentifier();
				break;
			case ASTNode.VARIABLE_DECLARATION_STATEMENT:
				property= VariableDeclarationStatement.MODIFIERS2_PROPERTY;
				name= getFirstFragmentName(((VariableDeclarationStatement) node).fragments());
				break;
			case ASTNode.TYPE_DECLARATION:
				property= TypeDeclaration.MODIFIERS2_PROPERTY;
				name= ((TypeDeclaration) node).getName().getIdentifier();
				break;
			case ASTNode.ANNOTATION_TYPE_DECLARATION:
				property= AnnotationTypeDeclaration.MODIFIERS2_PROPERTY;
				name= ((AnnotationTypeDeclaration) node).getName().getIdentifier();
				break;
			case ASTNode.ENUM_DECLARATION:
				property= EnumDeclaration.MODIFIERS2_PROPERTY;
				name= ((EnumDeclaration) node).getName().getIdentifier();
				break;
			case ASTNode.FIELD_DECLARATION:	
				property= FieldDeclaration.MODIFIERS2_PROPERTY;
				name= getFirstFragmentName(((FieldDeclaration) node).fragments());
				break;
			case ASTNode.INITIALIZER:
				property= Initializer.MODIFIERS2_PROPERTY;
				name= CorrectionMessages.SuppressWarningsSubProcessor_suppress_warnings_initializer_label;
				break;
			case ASTNode.FUNCTION_DECLARATION:
				property= FunctionDeclaration.MODIFIERS2_PROPERTY;
				name= ((FunctionDeclaration) node).getName().getIdentifier() + "()"; //$NON-NLS-1$
				break;
			case ASTNode.ANNOTATION_TYPE_MEMBER_DECLARATION:
				property= AnnotationTypeMemberDeclaration.MODIFIERS2_PROPERTY;
				name= ((AnnotationTypeMemberDeclaration) node).getName().getIdentifier() + "()"; //$NON-NLS-1$
				break;
			case ASTNode.ENUM_CONSTANT_DECLARATION:
				property= EnumConstantDeclaration.MODIFIERS2_PROPERTY;
				name= ((EnumConstantDeclaration) node).getName().getIdentifier();
				break;
			default:
				JavaScriptPlugin.logErrorMessage("SuppressWarning quick fix: wrong node kind: " + node.getNodeType()); //$NON-NLS-1$
				return;
		}
		
		String label= Messages.format(CorrectionMessages.SuppressWarningsSubProcessor_suppress_warnings_label, new String[] { warningToken, name });
		ASTRewriteCorrectionProposal proposal= new SuppressWarningsProposal(warningToken, label, cu, node, property, relevance);

		proposals.add(proposal);
	}

	public static void addUnknownSuppressWarningProposals(IInvocationContext context, IProblemLocation problem, Collection proposals) {

		ASTNode coveringNode= context.getCoveringNode();
		if (!(coveringNode instanceof StringLiteral))
			return;
		
		AST ast= coveringNode.getAST();
		StringLiteral literal= (StringLiteral) coveringNode;
		
		String literalValue= literal.getLiteralValue();
		String[] allWarningTokens= CorrectionEngine.getAllWarningTokens();
		for (int i= 0; i < allWarningTokens.length; i++) {
			String curr= allWarningTokens[i];
			if (NameMatcher.isSimilarName(literalValue, curr)) {
				StringLiteral newLiteral= ast.newStringLiteral();
				newLiteral.setLiteralValue(curr);
				ASTRewrite rewrite= ASTRewrite.create(ast);
				rewrite.replace(literal, newLiteral, null);
				String label= Messages.format(CorrectionMessages.SuppressWarningsSubProcessor_fix_suppress_token_label, new String[] { curr });
				Image image= JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_CHANGE);
				ASTRewriteCorrectionProposal proposal= new ASTRewriteCorrectionProposal(label, context.getCompilationUnit(), rewrite, 5, image);
				proposals.add(proposal);
			}
		}
	}

}
