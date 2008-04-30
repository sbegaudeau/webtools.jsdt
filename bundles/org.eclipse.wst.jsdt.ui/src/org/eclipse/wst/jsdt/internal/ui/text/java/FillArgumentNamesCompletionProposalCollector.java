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
package org.eclipse.wst.jsdt.internal.ui.text.java;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.wst.jsdt.core.CompletionProposal;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.Signature;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.ui.PreferenceConstants;
import org.eclipse.wst.jsdt.ui.text.java.CompletionProposalCollector;
import org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.wst.jsdt.ui.text.java.JavaContentAssistInvocationContext;

/**
 * Completion proposal collector which creates proposals with
 * filled in argument names.
 * <p>
 * This collector is used when
 * {@link PreferenceConstants#CODEASSIST_FILL_ARGUMENT_NAMES} is enabled.
 * <p/>
 */
public final class FillArgumentNamesCompletionProposalCollector extends CompletionProposalCollector {

	private final boolean fIsGuessArguments;

	public FillArgumentNamesCompletionProposalCollector(JavaContentAssistInvocationContext context) {
		super(context.getCompilationUnit());
		setInvocationContext(context);
		IPreferenceStore preferenceStore= JavaScriptPlugin.getDefault().getPreferenceStore();
		fIsGuessArguments= preferenceStore.getBoolean(PreferenceConstants.CODEASSIST_GUESS_METHOD_ARGUMENTS);
	}

	/*
	 * @see org.eclipse.wst.jsdt.internal.ui.text.java.ResultCollector#createJavaCompletionProposal(org.eclipse.wst.jsdt.core.CompletionProposal)
	 */
	protected IJavaCompletionProposal createJavaCompletionProposal(CompletionProposal proposal) {
		switch (proposal.getKind()) {
			case CompletionProposal.METHOD_REF:
				return createMethodReferenceProposal(proposal);
			case CompletionProposal.TYPE_REF:
				return createTypeProposal(proposal);
			default:
				return super.createJavaCompletionProposal(proposal);
		}
	}

	private IJavaCompletionProposal createMethodReferenceProposal(CompletionProposal methodProposal) {
		String completion= String.valueOf(methodProposal.getCompletion());
		// super class' behavior if this is not a normal completion or has no
		// parameters
		if ((completion.length() == 0) || ((completion.length() == 1) && completion.charAt(0) == ')') || Signature.getParameterCount(methodProposal.getSignature()) == 0 || getContext().isInJsdoc())
			return super.createJavaCompletionProposal(methodProposal);

		LazyJavaCompletionProposal proposal;
		IJavaScriptUnit compilationUnit= getCompilationUnit();
		if (compilationUnit != null && fIsGuessArguments)
			proposal= new ParameterGuessingProposal(methodProposal, getInvocationContext());
		else
			proposal= new FilledArgumentNamesMethodProposal(methodProposal, getInvocationContext());
		return proposal;
	}

	/*
	 * @see org.eclipse.wst.jsdt.internal.ui.text.java.ResultCollector#createTypeCompletion(org.eclipse.wst.jsdt.core.CompletionProposal)
	 */
	private IJavaCompletionProposal createTypeProposal(CompletionProposal typeProposal) {
		final IJavaScriptUnit cu= getCompilationUnit();
		if (cu == null || getContext().isInJsdoc())
			return super.createJavaCompletionProposal(typeProposal);

		IJavaScriptProject project= cu.getJavaScriptProject();
		if (!shouldProposeGenerics(project))
			return super.createJavaCompletionProposal(typeProposal);

		char[] completion= typeProposal.getCompletion();
		// don't add parameters for import-completions nor for proposals with an empty completion (e.g. inside the type argument list)
		if (completion.length > 0 && (completion[completion.length - 1] == ';' || completion[completion.length - 1] == '.'))
			return super.createJavaCompletionProposal(typeProposal);

		LazyJavaCompletionProposal newProposal= new LazyGenericTypeProposal(typeProposal, getInvocationContext());
		return newProposal;
	}

	/**
	 * Returns <code>true</code> if generic proposals should be allowed,
	 * <code>false</code> if not. Note that even though code (in a library)
	 * may be referenced that uses generics, it is still possible that the
	 * current source does not allow generics.
	 * 
	 * @param project the Java project 
	 * @return <code>true</code> if the generic proposals should be allowed,
	 *         <code>false</code> if not
	 */
	private final boolean shouldProposeGenerics(IJavaScriptProject project) {
		String sourceVersion;
		if (project != null)
			sourceVersion= project.getOption(JavaScriptCore.COMPILER_SOURCE, true);
		else
			sourceVersion= JavaScriptCore.getOption(JavaScriptCore.COMPILER_SOURCE);

		return sourceVersion != null && JavaScriptCore.VERSION_1_5.compareTo(sourceVersion) <= 0;
	}
}
