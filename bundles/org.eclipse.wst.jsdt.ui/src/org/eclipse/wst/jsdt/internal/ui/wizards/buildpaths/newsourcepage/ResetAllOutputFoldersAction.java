/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.wizards.buildpaths.newsourcepage;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.wst.jsdt.core.IIncludePathEntry;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IPackageFragmentRoot;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.internal.corext.buildpath.BuildpathDelta;
import org.eclipse.wst.jsdt.internal.corext.buildpath.ClasspathModifier;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.wst.jsdt.internal.ui.wizards.buildpaths.CPListElement;
import org.eclipse.wst.jsdt.internal.ui.wizards.buildpaths.CPListElementAttribute;

public class ResetAllOutputFoldersAction extends BuildpathModifierAction {
	
	private final IRunnableContext fContext;
	private final IJavaScriptProject fJavaProject;

	
	public ResetAllOutputFoldersAction(IRunnableContext context, IJavaScriptProject project, ISetSelectionTarget selectionTarget) {
		this(null, selectionTarget, context, project);
    }

	public ResetAllOutputFoldersAction(IWorkbenchSite site, ISetSelectionTarget selectionTarget, IRunnableContext context, IJavaScriptProject javaProject) {
		super(site, selectionTarget, BuildpathModifierAction.RESET_ALL_OUTPUT_FOLDERS);
		
		fContext= context;
		fJavaProject= javaProject;
		
		setText(NewWizardMessages.NewSourceContainerWorkbookPage_ToolBar_Reset_tooltip);
		setToolTipText(NewWizardMessages.NewSourceContainerWorkbookPage_ToolBar_Reset_tooltip);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public String getDetailedDescription() {
		return NewWizardMessages.PackageExplorerActionGroup_FormText_Default_ResetAllOutputFolders;
	}

	/**
	 * {@inheritDoc}
	 */
	public void run() {
		final IRunnableWithProgress runnable= new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				try {
					resetOutputFolders(fJavaProject, monitor);					
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				}
			}
		};
		try {
	        fContext.run(false, false, runnable);
        } catch (InvocationTargetException e) {
        	if (e.getCause() instanceof CoreException) {
				showExceptionDialog((CoreException)e.getCause(), NewWizardMessages.RemoveFromBuildpathAction_ErrorTitle);
			} else {
				JavaScriptPlugin.log(e);
			}
        } catch (InterruptedException e) {
        }
	}
	
	private List resetOutputFolders(IJavaScriptProject project, IProgressMonitor monitor) throws JavaScriptModelException {
		if (monitor == null)
			monitor= new NullProgressMonitor();
		try {
			IPackageFragmentRoot[] roots= project.getPackageFragmentRoots();
			monitor.beginTask(NewWizardMessages.ClasspathModifier_Monitor_ResetOutputFolder, roots.length + 10); 
			List entries= new ArrayList();
			for (int i= 0; i < roots.length; i++) {
				monitor.worked(1);
				if (roots[i].isArchive())
					continue;
				IIncludePathEntry entry= roots[i].getRawIncludepathEntry();
				CPListElement element= CPListElement.createFromExisting(entry, project);
				CPListElementAttribute outputFolder= new CPListElementAttribute(element, CPListElement.OUTPUT, element.getAttribute(CPListElement.OUTPUT), true);
				entries.add(outputFolder);
			}
			return reset(entries, project, new SubProgressMonitor(monitor, 10));
		} finally {
			monitor.done();
		}
	}
	
	private List reset(List selection, IJavaScriptProject project, IProgressMonitor monitor) throws JavaScriptModelException {
	    if (monitor == null)
        	monitor= new NullProgressMonitor();
        try {
        	monitor.beginTask(NewWizardMessages.ClasspathModifier_Monitor_Resetting, selection.size()); 
        	List entries= ClasspathModifier.getExistingEntries(project);
        	List result= new ArrayList();
        	for (int i= 0; i < selection.size(); i++) {
        		Object element= selection.get(i);
        		if (element instanceof IJavaScriptElement) {
        			IJavaScriptElement javaElement= (IJavaScriptElement) element;
        			IPackageFragmentRoot root;
        			if (element instanceof IJavaScriptProject)
        				root= project.getPackageFragmentRoot(project.getResource());
        			else
        				root= (IPackageFragmentRoot) element;
        			CPListElement entry= ClasspathModifier.getClasspathEntry(entries, root);
        			ClasspathModifier.resetFilters(javaElement, entry, project, new SubProgressMonitor(monitor, 1));
        			result.add(javaElement);
        		} else {
        			CPListElement selElement= ((CPListElementAttribute) element).getParent();
        			CPListElement entry= ClasspathModifier.getClasspathEntry(entries, selElement);
        			CPListElementAttribute outputFolder= ClasspathModifier.resetOutputFolder(entry, project);
        			result.add(outputFolder);
        		}
        	}
        
        	ClasspathModifier.commitClassPath(entries, project, null);
        	
        	BuildpathDelta delta= new BuildpathDelta(getToolTipText());
        	delta.setNewEntries((CPListElement[])entries.toArray(new CPListElement[entries.size()]));
        	informListeners(delta);
        	
        	return result;
        } finally {
        	monitor.done();
        }
    }
	
	protected boolean canHandle(IStructuredSelection elements) {
		return true;
	}
}
