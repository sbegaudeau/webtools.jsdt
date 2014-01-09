/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.ui.eval;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.jsdt.core.ICodeAssist;
import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.ToolFactory;
import org.eclipse.wst.jsdt.core.compiler.IScanner;
import org.eclipse.wst.jsdt.core.compiler.ITerminalSymbols;
import org.eclipse.wst.jsdt.core.compiler.InvalidInputException;
import org.eclipse.wst.jsdt.ui.JavaScriptUI;

/**
 * Utility class for aiding step into selection actions and hyper-linking
 * 
 * @since 1.1
 */
public class StepIntoSelectionUtils {

	/**
     * Returns the text editor associated with the given part or <code>null</code>
     * if none. In case of a multi-page editor, this method should be used to retrieve
     * the correct editor to perform the operation on.
     * 
     * @param part workbench part
     * @return text editor part or <code>null</code>
     */
    public static ITextEditor getTextEditor(IWorkbenchPart part) {
    	if (part instanceof ITextEditor) {
    		return (ITextEditor) part;
    	}
    	return (ITextEditor) part.getAdapter(ITextEditor.class);
    }	
	
	/**
     * gets the <code>IJavaScriptElement</code> from the editor input
     * @param input the current editor input
     * @return the corresponding <code>IJavaScriptElement</code>
     */
    public static IJavaScriptElement getJavaElement(IEditorInput input) {
    	IJavaScriptElement je = JavaScriptUI.getEditorInputJavaElement(input);
    	if(je != null) {
    		return je;
    	}
    	return JavaScriptUI.getWorkingCopyManager().getWorkingCopy(input);
    }
    
    /**
     * Returns the <code>IFuntion</code> from the given selection within the given <code>IJavaScriptElement</code>, 
     * or <code>null</code> if the selection does not container or is not an <code>IFunction</code>
     * @param selection
     * @param element
     * @return the corresponding <code>IFunction</code> from the selection within the provided <code>IJavaScriptElement</code>
     * @throws JavaScriptModelException
     */
    public static IFunction getFunction(ITextSelection selection, IJavaScriptElement element) throws JavaScriptModelException {
    	if(element != null && element instanceof ICodeAssist) {
    		return resolveFunction(selection.getOffset(), selection.getLength(), (ICodeAssist)element);
    	}
    	return null;
    }

	/**
	 * @param offset selection offset
	 * @param length selection length
	 * @param codeAssist context
	 * @return the function at the given position, or <code>null</code> if no function could be resolved 
	 * @throws JavaScriptModelException
	 */
	private static IFunction resolveFunction(int offset, int length, ICodeAssist codeAssist) throws JavaScriptModelException {
		IJavaScriptElement[] elements = codeAssist.codeSelect(offset, length);
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] instanceof IFunction) {
				return (IFunction)elements[i];
			}
		}
		return null;
	}
    
	/**
	 * @param offset
	 * @param activeEditor
	 * @param element
	 * @return the first function found at or after <code>offset</code> on the same line
	 * @throws JavaScriptModelException
	 */
	public static IFunction getFirstFunctionOnLine(int offset, IEditorPart activeEditor, IJavaScriptElement element) throws JavaScriptModelException {
		if (! (activeEditor instanceof ITextEditor) || ! (element instanceof ICodeAssist)) {
			return null;
		}
		ITextEditor editor = (ITextEditor)activeEditor;
		IDocumentProvider documentProvider = editor.getDocumentProvider();
		if (documentProvider == null) {
			return null;
		}
		IDocument document = documentProvider.getDocument(editor.getEditorInput());
		if (document == null) {
			return null;
		}
		try {
			IRegion lineInfo = document.getLineInformationOfOffset(offset);
			String line = document.get(lineInfo.getOffset(), lineInfo.getLength());
			IScanner scanner = ToolFactory.createScanner(false, false, false, null, JavaScriptCore.VERSION_1_5);
			scanner.setSource(line.toCharArray());
			scanner.resetTo(offset - lineInfo.getOffset(), lineInfo.getLength());
			int token = scanner.getNextToken();
			while (token != ITerminalSymbols.TokenNameEOF) {
				if (token == ITerminalSymbols.TokenNameIdentifier) {
					int methodStart = scanner.getCurrentTokenStartPosition();
					token = scanner.getNextToken();
					if (token == ITerminalSymbols.TokenNameLPAREN) {
						return resolveFunction(lineInfo.getOffset() + methodStart, 0, (ICodeAssist)element);
					}
				} 
				else {
					token = scanner.getNextToken();
				}
			}
		} 
		catch (BadLocationException e) {
			return null;
		} 
		catch (InvalidInputException e) {
			return null;
		}
		return null;
	}

}
