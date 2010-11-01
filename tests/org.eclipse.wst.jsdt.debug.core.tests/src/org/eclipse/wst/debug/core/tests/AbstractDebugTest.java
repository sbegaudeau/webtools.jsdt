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

import java.io.InputStream;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.progress.WorkbenchJob;

/**
 * Abstract test for JSDT debug
 *  
 *  @since 1.0
 */
public abstract class AbstractDebugTest extends TestCase {
	
	protected static final String SRC = "testsource"; //$NON-NLS-1$
	
	/**
	 * Constructor
	 * @param name
	 */
	public AbstractDebugTest(String name) {
		super(name);
		SafeRunnable.setIgnoreErrors(true);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		switchToPerspective(IDebugUIConstants.ID_DEBUG_PERSPECTIVE);
	}
	/**
	 * Checks for the existence of a test project with the given name and creates one
	 * if not found
	 * 
	 * @param name
	 * @throws Exception
	 */
	protected void assertTestProject(String name) throws Exception {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
		if(!project.exists()) {
			project.create(null);
		}
		if(!project.isAccessible()) {
			project.open(null);
		}
	}
	
	/**
	 * Ensures the script specified by <code>projectname/path/scriptname</code> exists in the testing workspace.
	 * <br><br>
	 * If it does not it is looked up from the default test source location in <code>container</code> and copied 
	 * into the test workspace to <code>projectname/path/scriptname</code>.
	 * @param projectname
	 * @param path
	 * @param scriptname
	 * @param container
	 * @throws Exception
	 */
	protected IFile loadTestSource(String projectname, String path, String scriptname, String container) throws Exception {
		IPath fpath = new Path(projectname).append(path).append(scriptname);
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(fpath);
		if(!file.exists()) {
			InputStream stream = Util.getSourceStream(container, scriptname);
			try {
				file.create(stream, true, null);
			}
			finally {
				if(stream != null) {
					stream.close();
				}
			}
		}
		return file;
	}
	
	/**
	 * Returns the launch manager
	 * 
	 * @return launch manager
	 */
	protected ILaunchManager getLaunchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}
	
	/**
	 * Returns the breakpoint manager
	 * 
	 * @return breakpoint manager
	 */
	protected IBreakpointManager getBreakpointManager() {
		return DebugPlugin.getDefault().getBreakpointManager();
	}	
	
	/**
	 * Forces marker deltas to be sent based on breakpoint creation.
	 * 
	 * @param breakpoint
	 */
	protected void forceDeltas(IProject project) throws CoreException {
		if (project != null && project.isAccessible()) {
			project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
		}
	}
	
	/**
	 * Switches to the perspective with the given id in a synExec or does nothing if the id is not a valid perspective
	 * @param id
	 */
	protected void switchToPerspective(final String id) {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
            public void run() {
                IWorkbench workbench = PlatformUI.getWorkbench();
                IPerspectiveDescriptor descriptor = workbench.getPerspectiveRegistry().findPerspectiveWithId(id);
                IWorkbenchPage activePage = workbench.getActiveWorkbenchWindow().getActivePage();
				activePage.setPerspective(descriptor);
            }
        });
    }
	
    /**
     * Wait for builds to complete
     */
    public static void waitForBuild() {
        boolean wasInterrupted = false;
        do {
            try {
                Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, null);
                Job.getJobManager().join(ResourcesPlugin.FAMILY_MANUAL_BUILD, null);
                wasInterrupted = false;
            } catch (OperationCanceledException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                wasInterrupted = true;
            }
        } while (wasInterrupted);
    }	

	/**
	 * Opens and returns an editor on the given file or <code>null</code>
	 * if none. The editor will be activated.
	 * 
	 * @param file
	 * @return editor or <code>null</code>
	 */
	protected IEditorPart openEditor(final IFile file) throws PartInitException, InterruptedException {
		Display display = DebugUIPlugin.getStandardDisplay();
		if (Thread.currentThread().equals(display.getThread())) {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			return IDE.openEditor(page, file, true);
		} 
		final IEditorPart[] parts = new IEditorPart[1];
		WorkbenchJob job = new WorkbenchJob(display, "open editor") { //$NON-NLS-1$
			public IStatus runInUIThread(IProgressMonitor monitor) {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					parts[0] = IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
					return e.getStatus();
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();
		job.join();
		return parts[0];
	}
	
	/**
	 * Closes all editors in the active workbench page.
	 */
	protected void closeAllEditors() {
	    Runnable closeAll = new Runnable() {
            public void run() {
                IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                activeWorkbenchWindow.getActivePage().closeAllEditors(false);
            }
        };
        Display display = DebugUIPlugin.getStandardDisplay();
        display.syncExec(closeAll);
	}
}

