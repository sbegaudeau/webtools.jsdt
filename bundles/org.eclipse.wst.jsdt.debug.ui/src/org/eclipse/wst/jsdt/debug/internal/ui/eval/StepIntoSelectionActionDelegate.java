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


import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.ui.actions.IRunToLineTarget;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.texteditor.IEditorStatusLine;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptStackFrame;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptThread;
import org.eclipse.wst.jsdt.debug.internal.ui.JavaScriptDebugUIPlugin;

/**
 * Steps into the selected function.
 * 
 * @since 1.0
 */
public class StepIntoSelectionActionDelegate implements IEditorActionDelegate, IWorkbenchWindowActionDelegate {
	
	private IEditorPart editor = null;
	private IWorkbenchWindow window = null;
	private IRegion region = null;
	
	/**
	 * Default constructor
	 */
	public StepIntoSelectionActionDelegate() {}
	
	/**
	 * Constructor
	 * @param region
	 */
	public StepIntoSelectionActionDelegate(IRegion region) {
		this.region = region;
	}

	/**
	 * The line number being "run to."
	 */
	private int line = -1;
	
	/**
	 * The debug event list listener used to know when a run to line has finished.
	 * @see StepIntoSelectionActionDelegate#runToLineBeforeStepIn(ITextSelection, IJavaScriptStackFrame, IFunction)
	 */
	private IDebugEventSetListener listener = null;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		IJavaScriptStackFrame frame = getStackFrame();
		if (frame == null || !frame.isSuspended()) {
			// no longer suspended - unexpected
			return;
		}
		ITextSelection textSelection = getTextSelection();
		try {
			IEditorPart activeEditor = getActiveEditor();
			IJavaScriptElement javaElement= StepIntoSelectionUtils.getJavaElement(activeEditor.getEditorInput());
			IFunction function = StepIntoSelectionUtils.getFunction(textSelection, javaElement);
			if (function == null) {
				function = StepIntoSelectionUtils.getFirstFunctionOnLine(textSelection.getOffset(), activeEditor, javaElement);
			}
			int lineNumber = frame.getLineNumber();
			if (textSelection.getStartLine() == (lineNumber - 1)) {
                doStepIn(frame, function);
			} else {
				runToLineBeforeStepIn(textSelection, frame.getThread(), function);
				return;
			}
		} 
		catch (DebugException e) {
			showErrorMessage(e.getStatus().getMessage());
			return;
		}
		catch(JavaScriptModelException jme) {
			showErrorMessage(jme.getStatus().getMessage());
			return;
		}
	}
	
	/**
	 * Steps into the given function in the given stack frame
	 * @param frame the frame in which the step should begin
	 * @param function the function to step into
	 * @throws DebugException
	 */
	private void doStepIn(IJavaScriptStackFrame frame, IFunction function) throws DebugException {
		// ensure top stack frame
		IStackFrame tos = frame.getThread().getTopStackFrame();
		if (tos == null) {
			return; 
		}		
		if (!tos.equals(frame)) {
			showErrorMessage(Messages.step_into_only_top_frame); 
			return;
		}
		StepIntoSelectionHandler handler = new StepIntoSelectionHandler((IJavaScriptThread)frame.getThread(), frame, function);
		handler.step();
	}
	
	/**
	 * When the user chooses to "step into selection" on a line other than
	 * the currently executing one, first perform a "run to line" to get to
	 * the desired location, then perform a "step into selection."
	 */
	private void runToLineBeforeStepIn(ITextSelection textSelection, final IThread thread, final IFunction function) {
		line = textSelection.getStartLine() + 1;
		if (line == -1) {
			return;
		}
		// see bug 65489 - get the run-to-line adapter from the editor
		IRunToLineTarget runToLineAction = null;
		IEditorPart ed = getActiveEditor();
		if (ed != null) {
			runToLineAction  = (IRunToLineTarget) ed.getAdapter(IRunToLineTarget.class);
			if (runToLineAction == null) {
				IAdapterManager adapterManager = Platform.getAdapterManager();
				if (adapterManager.hasAdapter(ed, IRunToLineTarget.class.getName())) { 
					runToLineAction = (IRunToLineTarget) adapterManager.loadAdapter(ed,IRunToLineTarget.class.getName()); 
				}
			}
		}	
		// if no adapter exists, use the Java adapter
		if (runToLineAction == null) {
		  runToLineAction = new RunToLineAdapter();
		}
		listener = new IDebugEventSetListener() {

			/* (non-Javadoc)
			 * @see org.eclipse.debug.core.IDebugEventSetListener#handleDebugEvents(org.eclipse.debug.core.DebugEvent[])
			 */
			public void handleDebugEvents(DebugEvent[] events) {
				for (int i = 0; i < events.length; i++) {
					DebugEvent event = events[i];
					switch (event.getKind()) {
						case DebugEvent.SUSPEND :
							handleSuspendEvent(event);
							break;
						case DebugEvent.TERMINATE :
							handleTerminateEvent(event);
							break;
						default :
							break;
					}
				}
			}
			/**
			 * Listen for the completion of the "run to line." When the thread
			 * suspends at the correct location, perform a "step into selection"
			 * 
			 * @param event the debug event
			 */
			private void handleSuspendEvent(DebugEvent event) {
				Object source = event.getSource();
				if (source instanceof IJavaScriptThread) {
					try {
						final IJavaScriptStackFrame frame= (IJavaScriptStackFrame) ((IJavaScriptThread) source).getTopStackFrame();
						if (isExpectedFrame(frame)) {
							DebugPlugin plugin = DebugPlugin.getDefault();
							plugin.removeDebugEventListener(listener);
							plugin.asyncExec(new Runnable() {
								public void run() {
									try {
										doStepIn(frame, function);
									} catch (DebugException e) {
										showErrorMessage(e.getStatus().getMessage());
									}
								}
							});
						}
					} catch (DebugException e) {
						return;
					}
				}
			}
			/**
			 * Returns whether the given frame is the frame that this action is expecting.
			 * This frame is expecting a stack frame for the suspension of the "run to line".
			 * @param frame the given stack frame or <code>null</code>
			 * @return whether the given stack frame is the expected frame
			 * @throws DebugException
			 */
			private boolean isExpectedFrame(IJavaScriptStackFrame frame) throws DebugException {
				return frame != null && line == frame.getLineNumber();
			}
			/**
			 * When the debug target we're listening for terminates, stop listening
			 * to debug events.
			 * @param event the debug event
			 */
			private void handleTerminateEvent(DebugEvent event) {
				Object source = event.getSource();
				if (thread.getDebugTarget() == source) {
					DebugPlugin.getDefault().removeDebugEventListener(listener);
				}
			}
		};
		DebugPlugin.getDefault().addDebugEventListener(listener);
		try {
			runToLineAction.runToLine(getActiveEditor(), textSelection, thread);
		} catch (CoreException e) {
			DebugPlugin.getDefault().removeDebugEventListener(listener);
			showErrorMessage(Messages.exception_running_to_line); 
			JavaScriptDebugUIPlugin.log(e.getStatus());
		}
	}
	
	/**
	 * Gets the current text selection from the currently active editor
	 * @return the current text selection
	 */
	private ITextSelection getTextSelection() {
		IEditorPart part = getActiveEditor();
		if (part instanceof ITextEditor) { 
			ITextEditor editor = (ITextEditor)part;
			if(region != null) {
				IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
				if(document != null) {
					return new TextSelection(document, region.getOffset(), region.getLength());
				}
			}
			else {
				return (ITextSelection)editor.getSelectionProvider().getSelection();
			}
		}
		showErrorMessage(Messages.only_in_the_js_editor); 
		return null;
	}
	
	/**
	 * Displays an error message in the status area
	 * 
	 * @param message
	 */
	protected void showErrorMessage(String message) {	
		if (getActiveEditor() != null) {
			IEditorStatusLine statusLine= (IEditorStatusLine) getActiveEditor().getAdapter(IEditorStatusLine.class);
			if (statusLine != null) {
				statusLine.setMessage(true, message, null);
			}
		}		
		JavaScriptDebugUIPlugin.getStandardDisplay().beep();		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorActionDelegate#setActiveEditor(org.eclipse.jface.action.IAction, org.eclipse.ui.IEditorPart)
	 */
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		editor = targetEditor;
	}
	
	/**
	 * Returns the active editor or <code>null</code>.
	 * 
	 * @return active editor or <code>null</code>
	 */
	protected IEditorPart getActiveEditor() {
		if (window != null) {
			// global action
			return window.getActivePage().getActiveEditor();
		}
		// pop-up action
		return editor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * Returns the current stack frame context, or <code>null</code> if none.
	 * 
	 * @return the current stack frame context, or <code>null</code> if none
	 */
	protected IJavaScriptStackFrame getStackFrame() {
		return EvaluationManager.getManager().getEvaluationContext(getActiveEditor());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

}
