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
package org.eclipse.wst.jsdt.jseview;


import org.eclipse.core.runtime.Assert;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.texteditor.ITextEditor;

import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IOpenable;
import org.eclipse.wst.jsdt.ui.JavaScriptUI;

/**
 *
 */
public class EditorUtility {
	private EditorUtility() {
		super();
	}

	public static IEditorPart getActiveEditor() {
		IWorkbenchWindow window= JEViewPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			IWorkbenchPage page= window.getActivePage();
			if (page != null) {
				return page.getActiveEditor();
			}
		}
		return null;
	}
	
	
	public static IOpenable getJavaInput(IEditorPart part) {
		IEditorInput editorInput= part.getEditorInput();
		if (editorInput != null) {
			IJavaScriptElement input= javaUIgetEditorInputJavaElement(editorInput);
			if (input instanceof IOpenable) {
				return (IOpenable) input;
			}
		}
		return null;	
	}

	/**
	 * Note: This is an inlined version of {@link JavaUI#getEditorInputJavaElement(IEditorInput)},
	 * which is not available in 3.1.
	 */
	private static IJavaScriptElement javaUIgetEditorInputJavaElement(IEditorInput editorInput) {
		Assert.isNotNull(editorInput);
		IJavaScriptElement je= JavaScriptUI.getWorkingCopyManager().getWorkingCopy(editorInput); 
		if (je != null)
			return je;
		
		/*
		 * This needs works, see https://bugs.eclipse.org/bugs/show_bug.cgi?id=120340
		 */
		return (IJavaScriptElement)editorInput.getAdapter(IJavaScriptElement.class);
	}
	
	public static void selectInEditor(ITextEditor editor, int offset, int length) {
		IEditorPart active = getActiveEditor();
		if (active != editor) {
			editor.getSite().getPage().activate(editor);
		}
		editor.selectAndReveal(offset, length);
	}
}
