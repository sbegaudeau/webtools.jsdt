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
package org.eclipse.wst.debug.core.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointListener;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.ui.actions.ToggleBreakpointAction;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.progress.WorkbenchJob;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptFunctionBreakpoint;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptLineBreakpoint;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptLoadBreakpoint;
import org.eclipse.wst.jsdt.debug.core.model.JavaScriptDebugModel;

/**
 * Abstract test that has delegate methods to create various kinds of breakpoints with the ability
 * to auto remove them after tests complete
 * 
 * @since 1.0
 */
public abstract class AbstractBreakpointTest extends AbstractDebugTest {

	/**
	 * Test project name for testing breakpoints
	 */
	protected static final String BP_PROJECT = "BpProject"; //$NON-NLS-1$
	
	private ArrayList breakpoints = new ArrayList();
	
	/**
	 * Constructor
	 * @param name
	 */
	public AbstractBreakpointTest(String name) {
		super(name);
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		try {
			if(breakpoints.size() > 0) {
				getBreakpointManager().removeBreakpoints((IBreakpoint[]) breakpoints.toArray(new IBreakpoint[breakpoints.size()]), true);
				breakpoints.clear();
			}
		}
		finally {
			super.tearDown();
		}
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		assertTestProject(AbstractBreakpointTest.BP_PROJECT);
	}
	
	/**
	 * Creates a line breakpoint in the given script at the given line number.
	 * 
	 * @param scriptpath the absolute path to the script to create the breakpoint in
	 * @param line line number to install the breakpoint at
	 * @param condition the optional condition for the breakpoint
	 * @return line breakpoint
	 * @throws Exception
	 */
	protected IJavaScriptLineBreakpoint createLineBreakpoint(String scriptpath, int line, String condition) throws Exception {
		assertNotNull("script path cannot be null creating a line breakpoint", scriptpath); //$NON-NLS-1$
		assertTrue("the line number must be greater than -1 to create a line breakpoint", line > -1); //$NON-NLS-1$
		IResource res = getBreakpointResource(scriptpath);
		assertNotNull("failed creating line breakpoint: the breakpoint resource could not be found for ["+scriptpath+"]", res); //$NON-NLS-1$ //$NON-NLS-2$
		IJavaScriptUnit element = (IJavaScriptUnit) JavaScriptCore.create(res);
		assertNotNull("failed creating line breakpoint: the javascript element could not be computed for ["+scriptpath+"]", element); //$NON-NLS-1$ //$NON-NLS-2$
		Document doc = new Document(element.getSource());
		IRegion region = doc.getLineInformation(line);
		HashMap attr = new HashMap();
		int offset = region.getOffset();
		IJavaScriptLineBreakpoint bp = JavaScriptDebugModel.createLineBreakpoint(res, line, offset, offset+region.getLength(), attr, true);
		bp.setCondition(condition);
		breakpoints.add(bp);
		forceDeltas(res.getProject());
		return bp;
	}
	
	/**
	 * Creates JavaScript line breakpoints on the given array of lines
	 * @param resource
	 * @param lines
	 * @return the list of created breakpoints
	 * @throws Exception
	 */
	protected List createLineBreakpoints(String path, int[] lines) throws Exception {
		List bps = new ArrayList(lines.length);
		for (int i = 0; i < lines.length; i++) {
			IBreakpoint bp = createLineBreakpoint(path, lines[i], null);
			bps.add(bp);
			breakpoints.add(bp);
		}
		return bps;
	}
	    
	/**
	 * Creates a new function breakpoint
	 * 
	 * @param scriptpath the full absolute workspace path to the script to create a function breakpoint for
	 * @param fname function name
	 * @param sig function signature 
	 * @param condition the optional condition for the breakpoint
	 * @param entry whether to break on entry
	 * @param exit whether to break on exit
	 */
	protected IJavaScriptFunctionBreakpoint createFunctionBreakpoint(String scriptpath, String fname, String sig, String condition, boolean entry, boolean exit) throws Exception {
		assertNotNull("script path cannot be null creating a function breakpoint", scriptpath); //$NON-NLS-1$
		assertNotNull("function name cannot be null creating a function breakpoint", fname); //$NON-NLS-1$
		assertNotNull("function signature cannot be null creating a function breakpoint", sig); //$NON-NLS-1$
		IResource res = getBreakpointResource(scriptpath);
		assertNotNull("failed creating function breakpoint: the breakpoint resource could not be found for ["+scriptpath+"]", res); //$NON-NLS-1$ //$NON-NLS-2$
		IJavaScriptElement element = JavaScriptCore.create(res);
		assertNotNull("failed creating line breakpoint: the javascript element could not be computed for ["+scriptpath+"]", element); //$NON-NLS-1$ //$NON-NLS-2$
		//TODO find the function
		HashMap attr = new HashMap();
		IJavaScriptFunctionBreakpoint bp = JavaScriptDebugModel.createFunctionBreakpoint(res, fname, sig, -1, -1, attr, false);
		bp.setEntry(entry);
		bp.setExit(exit);
		bp.setCondition(condition);
		breakpoints.add(bp);
		forceDeltas(res.getProject());
		return bp;
	}	
	
	/**
	 * Creates a script load breakpoint for the given script path
	 * 
	 * @param scriptpath the full absolute workspace path to the script to create a load breakpoint for
	 * 
	 * @return class prepare breakpoint
	 * @throws Exception
	 */
	protected IJavaScriptLoadBreakpoint createScriptloadBreakpoint(String scriptpath) throws Exception {
		assertNotNull("script path cannot be null creating a script load breakpoint", scriptpath); //$NON-NLS-1$
		IResource res = getBreakpointResource(scriptpath);
		assertNotNull("failed creating script load breakpoint: the breakpoint resource could not be found for ["+scriptpath+"]", res); //$NON-NLS-1$ //$NON-NLS-2$
		IJavaScriptElement element = JavaScriptCore.create(res);
		assertNotNull("failed creating line breakpoint: the javascript element could not be computed for ["+scriptpath+"]", element); //$NON-NLS-1$ //$NON-NLS-2$
		HashMap attr = new HashMap();
		IJavaScriptLoadBreakpoint bp = JavaScriptDebugModel.createScriptLoadBreakpoint(res, -1, -1, attr, false);
		breakpoints.add(bp);
		forceDeltas(res.getProject());
		return bp;
	}
	
	/**
	 * Returns the workspace resource for the given script path or <code>null</code>
	 * @param scriptpath the absolute workspace path
	 * 
	 * @return the {@link IResource} backing the path or <code>null</code>
	 */
	protected IResource getBreakpointResource(String scriptpath) {
		return ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(scriptpath));
	}
	
	/**
	 * Deletes all existing breakpoints
	 * 
	 * @throws Exception
	 */
	protected void removeAllBreakpoints() throws Exception {
		IBreakpoint[] bps = getBreakpointManager().getBreakpoints();
		if(bps.length > 0) {
			getBreakpointManager().removeBreakpoints(bps, true);
		}
	}
	
	/**
	 * Toggles a breakpoint in the editor at the given line number returning the breakpoint
	 * or <code>null</code> if none.
	 * 
	 * @param editor
	 * @param lineNumber
	 * @return returns the created breakpoint or <code>null</code> if none.
	 * @throws InterruptedException
	 */
	protected IBreakpoint toggleBreakpoint(final IEditorPart editor, int lineNumber) throws InterruptedException {
		final IVerticalRulerInfo info = new VerticalRulerInfoStub(lineNumber-1); // sub 1, as the doc lines start at 0
		WorkbenchJob job = new WorkbenchJob("toggle javascript breakpoint") { //$NON-NLS-1$
			public IStatus runInUIThread(IProgressMonitor monitor) {
				ToggleBreakpointAction action = new ToggleBreakpointAction(editor, null, info);
				action.run();
				return Status.OK_STATUS;
			}
		};
		final Object lock = new Object();
		final IBreakpoint[] bps = new IBreakpoint[1];
		IBreakpointListener listener = new IBreakpointListener() {
			public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
				synchronized (lock) {
					lock.notifyAll();
				}
			}
			public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta) {
			}
			public void breakpointAdded(IBreakpoint breakpoint) {
				synchronized (lock) {
					bps[0] = breakpoint;
					lock.notifyAll();
				}
			}
		};
		IBreakpointManager manager = DebugPlugin.getDefault().getBreakpointManager();
		manager.addBreakpointListener(listener);
		synchronized (lock) {
			job.setPriority(Job.INTERACTIVE);
			job.schedule();
			lock.wait(10000);
		}
		manager.removeBreakpointListener(listener);
		if(bps[0] != null) {
			breakpoints.add(bps[0]);
		}
		return bps[0];
	}
}
