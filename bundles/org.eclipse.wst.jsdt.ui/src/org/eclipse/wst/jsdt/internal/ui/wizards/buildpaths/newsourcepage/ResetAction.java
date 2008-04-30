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
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PlatformUI;
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

//Warning: This is unused and untested code. Images and descriptions are missing too.
//SelectedElements iff enabled: IJavaScriptProject || IPackageFragmentRoot || CPListElementAttribute
public class ResetAction extends BuildpathModifierAction {

	private final IRunnableContext fContext;

	public ResetAction(IWorkbenchSite site) {
		this(site, null, PlatformUI.getWorkbench().getProgressService());
	}
	
	public ResetAction(IRunnableContext context, ISetSelectionTarget selectionTarget) {
		this(null, selectionTarget, context);
    }

	public ResetAction(IWorkbenchSite site, ISetSelectionTarget selectionTarget, IRunnableContext context) {
		super(site, selectionTarget, BuildpathModifierAction.RESET);
		
		fContext= context;
		
		setText(NewWizardMessages.NewSourceContainerWorkbookPage_ToolBar_Reset_tooltip);
		setToolTipText(NewWizardMessages.NewSourceContainerWorkbookPage_ToolBar_Reset_tooltip);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public String getDetailedDescription() {
		if (!isEnabled())
			return null;
		
		Iterator iterator= getSelectedElements().iterator();
		Object p= iterator.next();
		while (iterator.hasNext()) {
			Object q= iterator.next();
			if (
					(p instanceof CPListElementAttribute && !(q instanceof CPListElementAttribute)) ||
					(q instanceof CPListElementAttribute && !(p instanceof CPListElementAttribute))
			) {
				return NewWizardMessages.PackageExplorerActionGroup_FormText_Default_Reset;
			}
			p= q;
		}
		if (p instanceof CPListElementAttribute) {
            return NewWizardMessages.PackageExplorerActionGroup_FormText_SetOutputToDefault;
		} else {
            return NewWizardMessages.PackageExplorerActionGroup_FormText_ResetFilters;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void run() {
		final IRunnableWithProgress runnable= new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				try {
					Object firstElement= getSelectedElements().get(0);
					IJavaScriptProject project= null;
					if (firstElement instanceof IJavaScriptProject) {
						project= (IJavaScriptProject)firstElement;
					} else if (firstElement instanceof IPackageFragmentRoot) {
						project= ((IPackageFragmentRoot)firstElement).getJavaScriptProject();
					} else {
						project= ((CPListElementAttribute)firstElement).getParent().getJavaProject();
					}
					
					List result= reset(getSelectedElements(), project, monitor);
					selectAndReveal(new StructuredSelection(result));					
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				}
			}
		};
		try {
	        fContext.run(false, false, runnable);
        } catch (InvocationTargetException e) {
        	if (e.getCause() instanceof CoreException) {
				showExceptionDialog((CoreException)e.getCause(), ""); //$NON-NLS-1$
			} else {
				JavaScriptPlugin.log(e);
			}
        } catch (InterruptedException e) {
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
		try {
	        for (Iterator iterator= elements.iterator(); iterator.hasNext();) {
	            Object element= iterator.next();
	            if (element instanceof IJavaScriptProject) {
	            	IJavaScriptProject project= (IJavaScriptProject)element;
	            	if (!project.isOnIncludepath(project))
	            		return false;
	            	
	            	IIncludePathEntry entry= ClasspathModifier.getClasspathEntryFor(project.getPath(), project, IIncludePathEntry.CPE_SOURCE);
	                if (entry.getInclusionPatterns().length == 0 && entry.getExclusionPatterns().length == 0)
	                    return false;
	            	
	        		return true;
	            } else if (element instanceof IPackageFragmentRoot) {
	            	if (ClasspathModifier.filtersSet((IPackageFragmentRoot)element))
	            		return true;
	            } else if (element instanceof CPListElementAttribute) {
	            	if (!ClasspathModifier.isDefaultOutputFolder((CPListElementAttribute)element))
	            		return true;
	            } else {
	            	return false;
	            }
	        }
        } catch (JavaScriptModelException e) {
	        return false;
        }
		return false;
	}
}