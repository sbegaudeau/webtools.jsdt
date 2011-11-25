/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.ui.breakpoints;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.ui.IEditorInput;
import org.eclipse.wst.jsdt.debug.internal.ui.adapters.JavaScriptAdapterFactory;
import org.eclipse.wst.sse.ui.internal.provisional.extensions.ISourceEditingTextTools;
import org.eclipse.wst.sse.ui.internal.provisional.extensions.breakpoint.IBreakpointProvider;

/**
 * Provide the JavaScript breakpoint for the HTML editor
 * 
 * @since 3.4
 */
public class JavaScriptHtmlBreakpointProvider implements IBreakpointProvider {

	static final String SCRIPT_REGION = "org.eclipse.wst.html.SCRIPT"; //$NON-NLS-1$
	
	/**
	 * Constructor
	 */
	public JavaScriptHtmlBreakpointProvider() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.sse.ui.internal.provisional.extensions.breakpoint.IBreakpointProvider#addBreakpoint(org.eclipse.jface.text.IDocument, org.eclipse.ui.IEditorInput, int, int)
	 */
	public IStatus addBreakpoint(final IDocument document, IEditorInput input, final int lineNumber, final int offset) throws CoreException {
		final IResource resource = getResource(input);
		if(resource != null && offset > -1) {
			try {
				final ITypedRegion region = document.getPartition(document.getLineOffset(lineNumber));
				if (region != null && SCRIPT_REGION.equals(region.getType())) {
					Job j = new Job("Toggle JavaScript Line Breakpoint") { //$NON-NLS-1$
						protected IStatus run(IProgressMonitor monitor) {
							try {
								JavaScriptAdapterFactory.getToggleBreakpointAdapter().addBreakpoint(resource, document, lineNumber);
								return Status.OK_STATUS;
							}
							catch(CoreException ce) {
								return Status.CANCEL_STATUS;
							}
						}
					};
					j.setPriority(Job.INTERACTIVE);
					j.schedule();
				}
			}
			catch(BadLocationException ble) {}
		}
		return Status.CANCEL_STATUS;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.sse.ui.internal.provisional.extensions.breakpoint.IBreakpointProvider#getResource(org.eclipse.ui.IEditorInput)
	 */
	public IResource getResource(IEditorInput input) {
        IResource resource = (IResource) input.getAdapter(IFile.class);
        if (resource == null) {
            resource = ResourcesPlugin.getWorkspace().getRoot();
        }
        return resource;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.sse.ui.internal.provisional.extensions.breakpoint.IBreakpointProvider#setSourceEditingTextTools(org.eclipse.wst.sse.ui.internal.provisional.extensions.ISourceEditingTextTools)
	 */
	public void setSourceEditingTextTools(ISourceEditingTextTools tool) {
	}
}