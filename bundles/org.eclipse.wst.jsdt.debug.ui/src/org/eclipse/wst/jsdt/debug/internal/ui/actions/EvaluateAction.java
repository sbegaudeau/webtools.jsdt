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
package org.eclipse.wst.jsdt.debug.internal.ui.actions;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptStackFrame;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptValue;
import org.eclipse.wst.jsdt.debug.internal.ui.eval.EvaluationManager;
import org.eclipse.wst.jsdt.internal.ui.text.JavaWordFinder;

/**
 * Action for evaluating selected source
 * 
 * @since 1.1
 */
public abstract class EvaluateAction implements IEditorActionDelegate, IObjectActionDelegate, IActionDelegate2, IViewActionDelegate, IWorkbenchWindowActionDelegate, IHandler {

	private IRegion region = null;
	private IWorkbenchPart targetpart = null;
	private IWorkbenchWindow window = null;
	private IAction action = null;
	private Object selection = null;
	private IJavaScriptValue value = null;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate2#dispose()
	 */
	public void dispose() {
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate2#runWithEvent(org.eclipse.jface.action.IAction, org.eclipse.swt.widgets.Event)
	 */
	public void runWithEvent(IAction action, Event event) {
		run(action);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		IJavaScriptStackFrame frame = getStackFrameContext();
		if(frame != null) {
			resolveSelection();
			//XXX hack, if we want to support evaluations from the variables view this will not work
			setValue(frame.evaluate(getSelection().toString()));
			showResult(getValue());
		}
	}
	
	/**
	 * Shows the result of the evaluation to the user. Sub-classes must implement. 
	 * 
	 * @param value the {@link IJavaScriptValue} to show to the user
	 */
	protected abstract void showResult(IJavaScriptValue value);
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		setAction(action);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate2#init(org.eclipse.jface.action.IAction)
	 */
	public void init(IAction action) {
		setAction(action);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart view) {
		setTargetpart(view);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		IWorkbenchPage page = window.getActivePage();
		if(page != null) {
			setTargetpart(window.getActivePage().getActivePart());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		setTargetpart(targetPart);
		setAction(action);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorActionDelegate#setActiveEditor(org.eclipse.jface.action.IAction, org.eclipse.ui.IEditorPart)
	 */
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		setTargetpart(targetEditor);
		setAction(action);
	}
	
	/**
	 * Resolves the selected object from the target part
	 */
	protected void resolveSelection() {
		ISelection sel = getTargetSelection();
		setRegion(null);
		if(sel instanceof ITextSelection) {
			ITextSelection ts = (ITextSelection)sel;
			String text= ts.getText();
			if (text.trim().length() > 0) {
				setSelection(text);
				setRegion(new Region(ts.getOffset(), ts.getLength()));
			} else if (getTargetPart() instanceof IEditorPart) {
				IEditorPart editor= (IEditorPart)getTargetPart();
				if (editor instanceof ITextEditor) {
					setSelection(resolveText(null, ts, editor));
				}
			}
		}
		else if(sel instanceof IStructuredSelection) {
			
		}
	}
	
	/**
	 * Resolve the word from the given text selection. If no new word infos can be resolved 
	 * the original string is returned.
	 * @param text the text
	 * @param ts the current text selection
	 * @param editor the current editor
	 * @return the new resolved text for the whole word of the selection
	 */
	String resolveText(String text, ITextSelection ts, IEditorPart editor) {
		ITextEditor textEditor= (ITextEditor) editor;
		IDocument doc= textEditor.getDocumentProvider().getDocument(editor.getEditorInput());
		region = JavaWordFinder.findWord(doc, ts.getOffset());
		if (region != null) {
			try {
				return doc.get(region.getOffset(), region.getLength());
			} 
			catch (BadLocationException e) {
				//ignore
			}
		}
		return text;
	}
	
	/**
	 * Returns the select from the {@link IWorkbenchSite} of the {@link #getTargetPart()} {@link IWorkbenchPart}
	 * @return the selection or <code>null</code>
	 */
	protected ISelection getTargetSelection() {
		IWorkbenchPart part = getTargetPart();
		if(part != null) {
			ISelectionProvider provider = part.getSite().getSelectionProvider();
			if (provider != null) {
				return provider.getSelection();
			}
		}
		return null;
	}
	
	/**
	 * Finds the currently selected {@link IJavaScriptStackFrame} in the UI.
	 */
	protected IJavaScriptStackFrame getStackFrameContext() {
		IWorkbenchPart part = getTargetPart();
		IJavaScriptStackFrame frame = null;
		if (part == null) {
			frame = EvaluationManager.getManager().getEvaluationContext(getWindow());
		} else {
			frame = EvaluationManager.getManager().getEvaluationContext(part);
		}		
		return frame;
	}
	
	/**
	 * Allows the result of the evaluation to be set
	 * 
	 * @param value the {@link IJavaScriptValue} of the evaluation
	 */
	protected void setValue(IJavaScriptValue value) {
		this.value = value;
	}
	
	/**
	 * Returns the {@link IJavaScriptValue} result of the evaluation
	 * 
	 * @return the {@link IJavaScriptValue} result or <code>null</code>
	 */
	protected IJavaScriptValue getValue() {
		return value;
	}
	
	/**
	 * Allows the resolved selection to be set
	 * 
	 * @param obj
	 */
	protected void setSelection(Object obj) {
		selection = obj;
	}
	
	/**
	 * Returns the resolved selection or <code>null</code>. If <code>null</code>
	 * is returned attempts can be made to re-resolve the selection using {@link #resolveSelection()}
	 * 
	 * @return the resolved selection or <code>null</code>
	 */
	protected Object getSelection() {
		return selection;
	}
	
	/**
	 * Returns the current target {@link IWorkbenchPart}
	 * @return the {@link IWorkbenchPart} target or <code>null</code>
	 */
	protected IWorkbenchPart getTargetPart() {
		return targetpart;
	}
	
	/**
	 * Allows the target {@link IWorkbenchPart} to be set
	 * @param targetpart the targetpart to set
	 */
	protected void setTargetpart(IWorkbenchPart targetpart) {
		this.targetpart = targetpart;
	}
	
	/**
	 * Returns the current active {@link IWorkbenchWindow}
	 * 
	 * @return the current active {@link IWorkbenchWindow}
	 */
	protected IWorkbenchWindow getWindow() {
		return window;
	}
	
	/**
	 * Allows the current {@link IWorkbenchWindow} context to be set
	 * 
	 * @param window the {@link IWorkbenchWindow} context
	 */
	protected void setWindow(IWorkbenchWindow window) {
		this.window = window;
	}
	
	/**
	 * Returns the selected text {@link IRegion}, or <code>null</code> if none.
	 * 
	 * @return the selected text {@link IRegion} or <code>null</code>
	 */
	protected IRegion getRegion() {
		return region;
	}
	
	/**
	 * Allows the selected {@link IRegion} to be set
	 * 
	 * @param region the {@link IRegion} to set
	 */
	protected void setRegion(IRegion region) {
		this.region = region;
	}
	
	/**
	 * Returns the {@link IAction} context or <code>null</code>
	 * 
	 * @return the backing {@link IAction} or <code>null</code>
	 */
	protected IAction getAction() {
		return action;
	}
	
	/**
	 * Allows the backing {@link IAction} to be set
	 * 
	 * @param action the action to set
	 */
	protected void setAction(IAction action) {
		this.action = action;
	}
	
	/**
	 * Returns the styled text widget associated with the given part
	 * or <code>null</code> if none.
	 * 
	 * @param part workbench part
	 * @return associated style text widget or <code>null</code>
	 */
	protected StyledText getStyledText(IWorkbenchPart part) {
		ITextViewer viewer = (ITextViewer)part.getAdapter(ITextViewer.class);
		StyledText textWidget = null;
		if (viewer == null) {
			Control control = (Control) part.getAdapter(Control.class);
			if (control instanceof StyledText) {
				textWidget = (StyledText) control;
			}
		} else {
			textWidget = viewer.getTextWidget();
		}
		return textWidget;
	}
	
	/**
	 * Returns an anchor point for a popup dialog on top of a styled text
	 * or <code>null</code> if none.
	 * 
	 * @param part or <code>null</code>
	 * @return anchor point or <code>null</code>
	 */
	protected Point getPopupAnchor(StyledText textWidget) {
		if (textWidget != null) {
	        Point docRange = textWidget.getSelectionRange();
	        int midOffset = docRange.x + (docRange.y / 2);
	        Point point = textWidget.getLocationAtOffset(midOffset);
	        point = textWidget.toDisplay(point);
	
	        GC gc = new GC(textWidget);
	        gc.setFont(textWidget.getFont());
	        int height = gc.getFontMetrics().getHeight();
	        gc.dispose();
	        point.y += height;
	        return point;
		}
		return null;
	}
}
