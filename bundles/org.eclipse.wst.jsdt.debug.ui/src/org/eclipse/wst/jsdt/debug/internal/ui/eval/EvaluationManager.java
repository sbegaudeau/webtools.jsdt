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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.contexts.DebugContextEvent;
import org.eclipse.debug.ui.contexts.IDebugContextListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptStackFrame;
import org.eclipse.wst.jsdt.debug.internal.ui.JavaScriptDebugUIPlugin;

/**
 * Manager to handle contexts for evaluations
 * 
 * @since 1.0
 */
public class EvaluationManager implements IDebugContextListener, IWindowListener {

	/**
	 * System property used to know if the debugger is active or not
	 */
	public static final String DEBUGGER_ACTIVE = JavaScriptDebugUIPlugin.PLUGIN_ID + ".jsdebuggerActive"; //$NON-NLS-1$
	
	private static EvaluationManager instance = null;
	
	private IWorkbenchWindow activewindow = null;
	private HashMap contextmap = null;
	
	/**
	 * Constructor
	 */
	private EvaluationManager() {
		//no instantiation
		DebugUITools.getDebugContextManager().addDebugContextListener(this);
	}
	
	/**
	 * Returns the singleton instance
	 * 
	 * @return the instance
	 */
	public static synchronized EvaluationManager getManager() {
		if(instance == null) {
			instance = new EvaluationManager();
		}
		return instance;
	}
	
	/**
	 * Start the manager
	 */
	public void start() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		for (int i = 0; i < windows.length; i++) {
			instance.windowOpened(windows[i]);	
		}
		workbench.addWindowListener(this);
		instance.activewindow = workbench.getActiveWorkbenchWindow();
	}
	
	/**
	 * Stop the manager and un-hook it as listeners
	 */
	public void stop() {
		DebugUITools.getDebugContextManager().removeDebugContextListener(this);
		PlatformUI.getWorkbench().removeWindowListener(this);
	}
	
	/**
	 * Returns the evaluation context for the given window, or <code>null</code> if none.
	 * The evaluation context corresponds to the selected stack frame in the following
	 * priority order:<ol>
	 * <li>stack frame in active page of the window</li>
	 * <li>stack frame in another page of the window</li>
	 * <li>stack frame in active page of another window</li>
	 * <li>stack frame in a page of another window</li>
	 * </ol>
	 * 
	 * @param window the window that the evaluation action was invoked from, or
	 *  <code>null</code> if the current window should be consulted
	 * @return the stack frame that supplies an evaluation context, or <code>null</code>
	 *   if none
	 * @return IJavaStackFrame
	 */
	public IJavaScriptStackFrame getEvaluationContext(IWorkbenchWindow window) {
		List otherwindows = new ArrayList();
		if (window == null) {
			return getEvaluationContext(activewindow, otherwindows);
		}
		return getEvaluationContext(window, otherwindows);
	}
	
	/**
	 * Returns the evaluation context for the given part, or <code>null</code> if none.
	 * The evaluation context corresponds to the selected stack frame in the following
	 * priority order:<ol>
	 * <li>stack frame in the same page</li>
	 * <li>stack frame in the same window</li>
	 * <li>stack frame in active page of other window</li>
	 * <li>stack frame in page of other windows</li>
	 * </ol>
	 * 
	 * @param part the part that the evaluation action was invoked from
	 * @return the stack frame that supplies an evaluation context, or <code>null</code>
	 *   if none
	 */
	public IJavaScriptStackFrame getEvaluationContext(IWorkbenchPart part) {
		IWorkbenchPage page = part.getSite().getPage();
		IJavaScriptStackFrame frame = getContext(page);
		if (frame == null) {
			return getEvaluationContext(page.getWorkbenchWindow());
		}
		return frame;
	}
	
	/**
	 * Looks up the current evaluation context for the given window. Tries all workbench windows
	 * if the active window does not have a context
	 * 
	 * @param window the window to look in
	 * @param otherwindows a listing of all other windows to try if the given window does not have a context
	 * @return the evaluation context or <code>null</code>
	 */
	IJavaScriptStackFrame getEvaluationContext(IWorkbenchWindow window, List otherwindows) {
		IWorkbenchPage activePage = window.getActivePage();
		IJavaScriptStackFrame frame = null;
		if (activePage != null) {
			frame = getContext(activePage);
		}
		if (frame == null) {
			IWorkbenchPage[] pages = window.getPages();
			for (int i = 0; i < pages.length; i++) {
				if (activePage != pages[i]) {
					frame = getContext(pages[i]);
					if (frame != null) {
						return frame;
					}
				}
			}
			otherwindows.add(window);
			IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
			for (int i = 0; i < windows.length; i++) {
				if (!otherwindows.contains(windows[i])) {
					frame = getEvaluationContext(windows[i], otherwindows);
					if (frame != null) {
						return frame;
					}
				}
			}
			return null;
		}
		return frame;
	}
	
	/**
	 * Looks up a context for the given page. Returns <code>null</code> if no mapping is found
	 * or if the context map has not been initialized
	 * 
	 * @param page the page to look up a context for
	 * 
	 * @return the {@link IJavaScriptStackFrame} context for the given page or <code>null</code>
	 */
	IJavaScriptStackFrame getContext(IWorkbenchPage page) {
		if (contextmap != null) {
			return (IJavaScriptStackFrame)contextmap.get(page);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.contexts.IDebugContextListener#debugContextChanged(org.eclipse.debug.ui.contexts.DebugContextEvent)
	 */
	public void debugContextChanged(DebugContextEvent event) {
		if ((event.getFlags() & DebugContextEvent.ACTIVATED) > 0) {
			IWorkbenchPart part = event.getDebugContextProvider().getPart();
			if (part != null) {
				IWorkbenchPage page = part.getSite().getPage();
				ISelection selection = event.getContext();
				if (selection instanceof IStructuredSelection) {
					IStructuredSelection ss = (IStructuredSelection)selection;
					if (ss.size() == 1) {
						Object element = ss.getFirstElement();
						if (element instanceof IAdaptable) {
							IJavaScriptStackFrame frame = (IJavaScriptStackFrame)((IAdaptable)element).getAdapter(IJavaScriptStackFrame.class);
							if (frame != null) {
								setContext(page, frame);
								return;
							}
						}
					}
				}
				// no context in the given view
				removeContext(page);
			}
		}
	}

	/**
	 * Sets the evaluation context for the given page, and notes that
	 * a valid execution context exists.
	 * 
	 * @param page
	 * @param frame
	 */
	private synchronized void setContext(IWorkbenchPage page, IJavaScriptStackFrame frame) {
		if (contextmap == null) {
			contextmap = new HashMap();
		}
		contextmap.put(page, frame);
		System.setProperty(DEBUGGER_ACTIVE, "true"); //$NON-NLS-1$
	}
	
	/**
	 * Removes an evaluation context for the given page, and determines if
	 * any valid execution context remain.
	 * 
	 * @param page
	 */
	private void removeContext(IWorkbenchPage page) {
		if (contextmap != null) {
			contextmap.remove(page);
			if (contextmap.isEmpty()) {
				System.setProperty(DEBUGGER_ACTIVE, "false"); //$NON-NLS-1$
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWindowListener#windowActivated(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void windowActivated(IWorkbenchWindow window) {
		activewindow = window;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWindowListener#windowDeactivated(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void windowDeactivated(IWorkbenchWindow window) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWindowListener#windowClosed(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void windowClosed(IWorkbenchWindow window) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWindowListener#windowOpened(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void windowOpened(IWorkbenchWindow window) {
	}
}
