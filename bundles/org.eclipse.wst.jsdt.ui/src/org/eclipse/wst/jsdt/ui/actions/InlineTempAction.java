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
package org.eclipse.wst.jsdt.ui.actions;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.jface.viewers.IStructuredSelection;

import org.eclipse.jface.text.ITextSelection;

import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PlatformUI;

import org.eclipse.wst.jsdt.core.ICompilationUnit;
import org.eclipse.wst.jsdt.core.JavaModelException;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.CompilationUnit;

import org.eclipse.wst.jsdt.internal.corext.refactoring.RefactoringAvailabilityTester;
import org.eclipse.wst.jsdt.internal.corext.refactoring.RefactoringExecutionStarter;
import org.eclipse.wst.jsdt.internal.corext.refactoring.util.RefactoringASTParser;

import org.eclipse.wst.jsdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.wst.jsdt.internal.ui.JavaPlugin;
import org.eclipse.wst.jsdt.internal.ui.actions.ActionUtil;
import org.eclipse.wst.jsdt.internal.ui.actions.SelectionConverter;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.JavaTextSelection;
import org.eclipse.wst.jsdt.internal.ui.refactoring.RefactoringMessages;
import org.eclipse.wst.jsdt.internal.ui.util.ExceptionHandler;

/**
 * Inlines the value of a local variable at all places where a read reference
 * is used.
 * 
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 * 
 * @since 2.0
 */
public class InlineTempAction extends SelectionDispatchAction {

	private JavaEditor fEditor;

	/**
	 * Note: This constructor is for internal use only. Clients should not call this constructor.
	 * 
	 * @param editor the java editor
	 */
	public InlineTempAction(JavaEditor editor) {
		this(editor.getEditorSite());
		fEditor= editor;
		setEnabled(SelectionConverter.canOperateOn(fEditor));
	}
	
	/* package */ InlineTempAction(IWorkbenchSite site) {
		super(site);
		setText(RefactoringMessages.InlineTempAction_label);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJavaHelpContextIds.INLINE_ACTION);
	}
	
	//---- text selection ----------------------------------------------------------

	/* (non-Javadoc)
	 * Method declared on SelectionDispatchAction
	 */		
	public void selectionChanged(ITextSelection selection) {
		setEnabled(true);
	}
	
	/**
	 * Note: This method is for internal use only. Clients should not call this method.
	 */
	public void selectionChanged(JavaTextSelection selection) {
		try {
			setEnabled(RefactoringAvailabilityTester.isInlineTempAvailable(selection));
		} catch (JavaModelException e) {
			setEnabled(false);
		}
	}
	
	/* (non-Javadoc)
	 * Method declared on SelectionDispatchAction
	 */		
	public void run(ITextSelection selection) {
		try{
			ICompilationUnit input= SelectionConverter.getInputAsCompilationUnit(fEditor);
			if (!ActionUtil.isEditable(fEditor))
				return;
			RefactoringExecutionStarter.startInlineTempRefactoring(input, new RefactoringASTParser(AST.JLS3).parse(input, true), selection, getShell(), true);
		} catch (JavaModelException e){
			ExceptionHandler.handle(e, RefactoringMessages.InlineTempAction_inline_temp, RefactoringMessages.NewTextRefactoringAction_exception); 
		}	
	}

	/*
	 * @see org.eclipse.wst.jsdt.ui.actions.SelectionDispatchAction#run(org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void run(IStructuredSelection selection) {
		//do nothing
	}

	/*
	 * @see org.eclipse.wst.jsdt.ui.actions.SelectionDispatchAction#selectionChanged(org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void selectionChanged(IStructuredSelection selection) {
		setEnabled(false);
	}

	/* package */ boolean tryInlineTemp(ICompilationUnit unit, CompilationUnit node, ITextSelection selection, Shell shell) {
		try {
			if (RefactoringExecutionStarter.startInlineTempRefactoring(unit, node, selection, shell, false)) {
				run(selection);
				return true;
			}
		} catch (JavaModelException exception) {
			JavaPlugin.log(exception);
		}
		return false;
	}
}