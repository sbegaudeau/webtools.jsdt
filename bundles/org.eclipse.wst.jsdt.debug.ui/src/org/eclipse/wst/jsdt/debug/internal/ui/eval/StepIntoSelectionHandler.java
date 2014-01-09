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
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventFilter;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptDebugTarget;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptStackFrame;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptThread;
import org.eclipse.wst.jsdt.debug.internal.ui.JavaScriptDebugUIPlugin;

/**
 * Handles stepping into a selected function, for a specific thread.
 * 
 * @since 1.0
 */
public class StepIntoSelectionHandler implements IDebugEventFilter {
	
	/**
	 * The function to step into
	 */
	private IFunction function;
	
	/**
	 * Resolved signature of the function to step into
	 */
	private String signature;
	
	/**
	 * The thread in which to step
	 */
	private IJavaScriptThread thread;

	/**
	 * The initial stack frame
	 */
	private String origname;
	private int origdepth;
	
	/**
	 * Whether this is the first step into.
	 */
	private boolean firststep = true;
	
	/**
	 * Expected event kind
	 */
	private int eventkind = -1;
	
	/**
	 * Expected event detail
	 */
	private int eventdetail = -1;

	/**
	 * Constructs a step handler to step into the given function in the given thread
	 * starting from the given stack frame.
	 * 
	 * @param thread
	 * @param frame
	 * @param func
	 */
	public StepIntoSelectionHandler(IJavaScriptThread thread, IJavaScriptStackFrame frame, IFunction func) {
		function = func;
		this.thread = thread;
		try {
			origname = frame.getName();
			signature = func.getSignature();
		} catch (CoreException e) {
			JavaScriptDebugUIPlugin.log(e);
		}
	}
	
	/**
	 * Returns the target thread for the step.
	 * 
	 * @return the target thread for the step
	 */
	protected IJavaScriptThread getThread() {
		return thread;
	}
	
	protected IJavaScriptDebugTarget getDebugTarget() {
		return (IJavaScriptDebugTarget)getThread().getDebugTarget();
	}
	
	/**
	 * Returns the function to step into
	 * 
	 * @return the function to step into
	 */
	protected IFunction getMethod() {
		return function;
	}
	
	/**
	 * Returns the resolved signature of the function to step into
	 * 
	 * @return the resolved signature of the function to step into
	 */
	protected String getSignature() {
		return signature;
	}	
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.IDebugEventFilter#filterDebugEvents(org.eclipse.debug.core.DebugEvent[])
	 */
	public DebugEvent[] filterDebugEvents(DebugEvent[] events) {
		// we only expect one event from our thread - find the event
		DebugEvent event = null;
		int index = -1;
		int threadEvents = 0;
		for (int i = 0; i < events.length; i++) {
			DebugEvent e = events[i];
			if (isExpectedEvent(e)) {
				event = e;
				index = i;
				threadEvents++;
			} else if (e.getSource() == getThread()) {
				threadEvents++;
			} 
		}
		if (event == null) {
			// nothing to process in this event set
			return events;
		}
		// create filtered event set
		DebugEvent[] filtered = new DebugEvent[events.length - 1];
		if (filtered.length > 0) {
			int j = 0;
			for (int i = 0; i < events.length; i++) {
				if (i != index) {
					filtered[j] = events[i];
					j++;
				}
			}
		}
		// if more than one event in our thread, abort (filtering our event)
		if (threadEvents > 1) {
			cleanup();
			return filtered;
		}
		// we have the one expected event - process it
		switch (event.getKind()) {
			case DebugEvent.RESUME:
				// next, we expect a step end
				setExpectedEvent(DebugEvent.SUSPEND, DebugEvent.STEP_END);
				if (firststep) {
					firststep = false;
					return events; // include the first resume event
				}
				// secondary step - filter the event
				return filtered;			
			case DebugEvent.SUSPEND:
				// compare location to desired location
				try {
					final IJavaScriptStackFrame frame = (IJavaScriptStackFrame)getThread().getTopStackFrame();
					int stackDepth = frame.getThread().getStackFrames().length;
					String name = frame.getName();
					if (name.equals(getMethod().getElementName())) {
						// hit
						cleanup();
						return events;
					}
					// step again
					Runnable r = null;
					if (stackDepth > origdepth) {
						r = new Runnable() {
							public void run() {
								try {
									setExpectedEvent(DebugEvent.RESUME, DebugEvent.STEP_RETURN);
									frame.stepReturn();
								} catch (DebugException e) {
									JavaScriptDebugUIPlugin.log(e);
									cleanup();
									DebugPlugin.getDefault().fireDebugEventSet(new DebugEvent[]{new DebugEvent(getDebugTarget(), DebugEvent.CHANGE)});
								}
							}
						};
					} else if (stackDepth == origdepth){
						// we should be back in the original stack frame - if not, abort
						if (!frame.getName().equals(origname)) {
							missed();
							return events;
						}
						r = new Runnable() {
							public void run() {
								try {
									setExpectedEvent(DebugEvent.RESUME, DebugEvent.STEP_INTO);
									frame.stepInto();	
								} catch (DebugException e) {
									JavaScriptDebugUIPlugin.log(e);
									cleanup();
									DebugPlugin.getDefault().fireDebugEventSet(new DebugEvent[]{new DebugEvent(getDebugTarget(), DebugEvent.CHANGE)});
								}
							}
						};																
					} else {
						// we returned from the original frame - never hit the desired method
						missed();
						return events;								
					}
					DebugPlugin.getDefault().asyncExec(r);
					// filter the events
						return filtered;
				} catch (CoreException e) {
					// abort
					JavaScriptDebugUIPlugin.log(e);
					cleanup();
					return events;
				}			
		}
		// execution should not reach here
		return events;
		 
	}
	
	/** 
	 * Called when stepping returned from the original frame without entering the desired method.
	 */
	protected void missed() {
		cleanup();
		Runnable r = new Runnable() {
			public void run() {
				String methodName = null;
				try {
					methodName = org.eclipse.wst.jsdt.core.Signature.toString(getMethod().getSignature(), getMethod().getElementName(), getMethod().getParameterNames(), false, false);
				} catch (JavaScriptModelException e) {
					methodName = getMethod().getElementName();
				}
				new MessageDialog(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),  
						Messages.step_into_selection, 
						null, 
						NLS.bind(Messages.exe_did_not_enter__0__before_returning, new String[]{methodName}), 
						MessageDialog.INFORMATION, new String[] {IDialogConstants.OK_LABEL}, 0).open();    
			}
		};
		JavaScriptDebugUIPlugin.getStandardDisplay().asyncExec(r);		
	}

	/**
	 * Performs the step.
	 */
	public void step() {
		// add event filter and turn off step filters
		DebugPlugin.getDefault().addDebugEventFilter(this);
		try {
			origdepth = getThread().getStackFrames().length;
			setExpectedEvent(DebugEvent.RESUME, DebugEvent.STEP_INTO);
			getThread().stepInto();
		} catch (DebugException e) {
			JavaScriptDebugUIPlugin.log(e);
			cleanup();
			DebugPlugin.getDefault().fireDebugEventSet(new DebugEvent[]{new DebugEvent(getDebugTarget(), DebugEvent.CHANGE)});			
		}
	}
	
	/**
	 * Cleans up when the step is complete/aborted.
	 */
	protected void cleanup() {
		DebugPlugin.getDefault().removeDebugEventFilter(this);
	}
	
	/**
	 * Sets the expected debug event kind and detail we are waiting for next.
	 * 
	 * @param kind event kind
	 * @param detail event detail
	 */
	private void setExpectedEvent(int kind, int detail) {
		eventkind = kind;
		eventdetail = detail;
	}
	
	/**
	 * Returns whether the given event is what we expected.
	 * 
	 * @param event fire event
	 * @return whether the event is what we expected
	 */
	protected boolean isExpectedEvent(DebugEvent event) {
		return event.getSource().equals(getThread()) &&
			event.getKind() == eventkind &&
			event.getDetail() == eventdetail;
	}
}
