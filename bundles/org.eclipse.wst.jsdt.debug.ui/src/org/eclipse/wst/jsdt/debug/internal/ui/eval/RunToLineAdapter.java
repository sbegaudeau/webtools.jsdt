/*******************************************************************************
 *  Copyright (c) 2010 IBM Corporation and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.ui.eval;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugElement;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.ISuspendResume;
import org.eclipse.debug.ui.actions.IRunToLineTarget;
import org.eclipse.debug.ui.actions.RunToLineHandler;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTParser;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptDebugTarget;
import org.eclipse.wst.jsdt.debug.core.model.JavaScriptDebugModel;
import org.eclipse.wst.jsdt.debug.internal.ui.JavaScriptDebugUIPlugin;
import org.eclipse.wst.jsdt.debug.internal.ui.breakpoints.BreakpointLocationFinder;

/**
 * Run to line target for the JavaScript debugger
 * 
 * @since 1.0
 */
public class RunToLineAdapter implements IRunToLineTarget {
	
	/**
	 * Marker attribute used to denote a run to line breakpoint
	 */
	private static final String RUN_TO_LINE =  JavaScriptDebugUIPlugin.PLUGIN_ID + ".run_to_line"; //$NON-NLS-1$
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IRunToLineTarget#runToLine(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection, org.eclipse.debug.core.model.ISuspendResume)
	 */
	public void runToLine(IWorkbenchPart part, ISelection selection, ISuspendResume target) throws CoreException {
		ITextEditor textEditor = StepIntoSelectionUtils.getTextEditor(part);
		if (textEditor == null) {
			throw new CoreException(new Status(IStatus.ERROR, JavaScriptDebugUIPlugin.PLUGIN_ID, Messages.missing_doc, null));
		} 
		IEditorInput input = textEditor.getEditorInput();
		if (input == null) {
			throw new CoreException(new Status(IStatus.ERROR, JavaScriptDebugUIPlugin.PLUGIN_ID, Messages.empty_editor, null));
		} 
		final IDocument document= textEditor.getDocumentProvider().getDocument(input);
		if (document == null) {
			throw new CoreException(new Status(IStatus.ERROR, JavaScriptDebugUIPlugin.PLUGIN_ID, Messages.missing_doc, null));
		} 
		final int[] validLine = new int[1];
		final String[] typeName = new String[1];
		final int[] lineNumber = new int[1];
		final ITextSelection textSelection = (ITextSelection) selection;
		Runnable r = new Runnable() {
			public void run() {
				lineNumber[0] = textSelection.getStartLine() + 1;
				ASTParser parser = ASTParser.newParser(AST.JLS3);
				parser.setSource(document.get().toCharArray());
				JavaScriptUnit compilationUnit = (JavaScriptUnit)parser.createAST(null);
				BreakpointLocationFinder locator = new BreakpointLocationFinder(compilationUnit, lineNumber[0], false);
				compilationUnit.accept(locator);
				validLine[0] = locator.getLineNumber();		
				typeName[0] = locator.getFunctionName();
			}
		};
		BusyIndicator.showWhile(JavaScriptDebugUIPlugin.getStandardDisplay(), r);
		if (validLine[0] == lineNumber[0]) {
			IBreakpoint breakpoint= null;
			Map attributes = new HashMap(4);
			attributes.put(IBreakpoint.PERSISTED, Boolean.FALSE);
			attributes.put(RUN_TO_LINE, Boolean.TRUE);
			breakpoint = JavaScriptDebugModel.createLineBreakpoint(ResourcesPlugin.getWorkspace().getRoot(), lineNumber[0], -1, -1, attributes, false);
			if (target instanceof IAdaptable) {
				IDebugTarget debugTarget = (IDebugTarget) ((IAdaptable)target).getAdapter(IDebugTarget.class);
				if (debugTarget == null) {
					throw new CoreException(new Status(IStatus.ERROR, JavaScriptDebugUIPlugin.PLUGIN_ID, Messages.cannot_find_debug_target, null));
				}
                RunToLineHandler handler = new RunToLineHandler(debugTarget, target, breakpoint);
                handler.run(new NullProgressMonitor());
				return;
			}
		} else {
			// invalid line
			if (textSelection.getLength() > 0) {
				throw new CoreException(new Status(IStatus.ERROR, JavaScriptDebugUIPlugin.PLUGIN_ID, Messages.selected_line_not_valid, null));
			} 
			throw new CoreException(new Status(IStatus.ERROR, JavaScriptDebugUIPlugin.PLUGIN_ID, Messages.cursor_position_not_valid, null));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IRunToLineTarget#canRunToLine(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection, org.eclipse.debug.core.model.ISuspendResume)
	 */
	public boolean canRunToLine(IWorkbenchPart part, ISelection selection, ISuspendResume target) {
	    if (target instanceof IDebugElement) {
            IDebugElement element = (IDebugElement) target;
            IJavaScriptDebugTarget adapter = (IJavaScriptDebugTarget) element.getDebugTarget().getAdapter(IJavaScriptDebugTarget.class);
            return adapter != null;
        }
		return false;
	}
}
