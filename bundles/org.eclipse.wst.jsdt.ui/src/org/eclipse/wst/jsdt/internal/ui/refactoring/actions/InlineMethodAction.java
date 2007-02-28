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
package org.eclipse.wst.jsdt.internal.ui.refactoring.actions;

import org.eclipse.core.runtime.Assert;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.jface.viewers.IStructuredSelection;

import org.eclipse.jface.text.ITextSelection;

import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PlatformUI;

import org.eclipse.wst.jsdt.core.IMethod;
import org.eclipse.wst.jsdt.core.ISourceRange;
import org.eclipse.wst.jsdt.core.ITypeRoot;
import org.eclipse.wst.jsdt.core.JavaModelException;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.CompilationUnit;

import org.eclipse.wst.jsdt.internal.corext.refactoring.RefactoringAvailabilityTester;
import org.eclipse.wst.jsdt.internal.corext.refactoring.RefactoringExecutionStarter;
import org.eclipse.wst.jsdt.internal.corext.refactoring.util.JavaElementUtil;
import org.eclipse.wst.jsdt.internal.corext.refactoring.util.RefactoringASTParser;
import org.eclipse.wst.jsdt.internal.corext.util.JavaModelUtil;

import org.eclipse.wst.jsdt.ui.actions.SelectionDispatchAction;

import org.eclipse.wst.jsdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.wst.jsdt.internal.ui.JavaPlugin;
import org.eclipse.wst.jsdt.internal.ui.actions.ActionUtil;
import org.eclipse.wst.jsdt.internal.ui.actions.SelectionConverter;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.JavaTextSelection;
import org.eclipse.wst.jsdt.internal.ui.refactoring.RefactoringMessages;
import org.eclipse.wst.jsdt.internal.ui.util.ExceptionHandler;

/**
 * Inlines a method.
 * 
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 */
public class InlineMethodAction extends SelectionDispatchAction {

	private JavaEditor fEditor;
	
	/**
	 * Note: This constructor is for internal use only. Clients should not call this constructor.
	 * @param editor the java editor
	 */
	public InlineMethodAction(JavaEditor editor) {
		this(editor.getEditorSite());
		fEditor= editor;
		setEnabled(SelectionConverter.canOperateOn(fEditor));
	}

	public InlineMethodAction(IWorkbenchSite site) {
		super(site);
		setText(RefactoringMessages.InlineMethodAction_inline_Method); 
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJavaHelpContextIds.INLINE_ACTION);
	}

	//---- structured selection ----------------------------------------------
	
	/*
	 * @see SelectionDispatchAction#selectionChanged(IStructuredSelection)
	 */
	public void selectionChanged(IStructuredSelection selection) {
		try {
			setEnabled(RefactoringAvailabilityTester.isInlineMethodAvailable(selection));
		} catch (JavaModelException e) {
			if (JavaModelUtil.isExceptionToBeLogged(e))
				JavaPlugin.log(e);
		}
	}

	/*
	 * @see SelectionDispatchAction#run(IStructuredSelection)
	 */
	public void run(IStructuredSelection selection) {
		try {
			Assert.isTrue(RefactoringAvailabilityTester.isInlineMethodAvailable(selection));
			IMethod method= (IMethod) selection.getFirstElement();
			ISourceRange nameRange= method.getNameRange();
			run(nameRange.getOffset(), nameRange.getLength(), method.getTypeRoot());
		} catch (JavaModelException e) {
			ExceptionHandler.handle(e, getShell(), RefactoringMessages.InlineMethodAction_dialog_title, RefactoringMessages.InlineMethodAction_unexpected_exception); 
		}
	}

	/*
	 * @see SelectionDispatchAction#selectionChanged(ITextSelection)
	 */
	public void selectionChanged(ITextSelection selection) {
		setEnabled(true);
	}
	
	/**
	 * Note: This method is for internal use only. Clients should not call this method.
	 */
	public void selectionChanged(JavaTextSelection selection) {
		try {
			setEnabled(RefactoringAvailabilityTester.isInlineMethodAvailable(selection));
		} catch (JavaModelException e) {
			setEnabled(false);
		}
	}
	
	/* (non-Javadoc)
	 * Method declared on SelectionDispatchAction
	 */		
	public void run(ITextSelection selection) {
		ITypeRoot typeRoot= SelectionConverter.getInputAsTypeRoot(fEditor);
		if (typeRoot == null)
			return;
		if (! JavaElementUtil.isSourceAvailable(typeRoot))
			return;
		run(selection.getOffset(), selection.getLength(), typeRoot);
	}

	private void run(int offset, int length, ITypeRoot typeRoot) {
		if (!ActionUtil.isEditable(fEditor, getShell(), typeRoot))
			return;
		try {
			RefactoringASTParser parser= new RefactoringASTParser(AST.JLS3);
			CompilationUnit compilationUnit= parser.parse(typeRoot, true);
			RefactoringExecutionStarter.startInlineMethodRefactoring(typeRoot, compilationUnit, offset, length, getShell(), true);
		} catch (JavaModelException e) {
			ExceptionHandler.handle(e, getShell(), RefactoringMessages.InlineMethodAction_dialog_title, RefactoringMessages.InlineMethodAction_unexpected_exception); 
		}
	}

	public boolean tryInlineMethod(ITypeRoot typeRoot, CompilationUnit node, ITextSelection selection, Shell shell) {
		try {
			if (RefactoringExecutionStarter.startInlineMethodRefactoring(typeRoot, node, selection.getOffset(), selection.getLength(), shell, false)) {
				run(selection);
				return true;
			}
		} catch (JavaModelException exception) {
			JavaPlugin.log(exception);
		}
		return false;
	}
}
