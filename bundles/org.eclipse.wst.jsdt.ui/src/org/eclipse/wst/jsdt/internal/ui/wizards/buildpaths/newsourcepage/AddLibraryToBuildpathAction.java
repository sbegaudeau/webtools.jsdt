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

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.wst.jsdt.core.IIncludePathEntry;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.internal.corext.buildpath.BuildpathDelta;
import org.eclipse.wst.jsdt.internal.corext.buildpath.ClasspathModifier;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.JavaPluginImages;
import org.eclipse.wst.jsdt.internal.ui.actions.WorkbenchRunnableAdapter;
import org.eclipse.wst.jsdt.internal.ui.packageview.JsGlobalScopeContainer;
import org.eclipse.wst.jsdt.internal.ui.util.PixelConverter;
import org.eclipse.wst.jsdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.wst.jsdt.internal.ui.wizards.buildpaths.CPListElement;
import org.eclipse.wst.jsdt.internal.ui.wizards.buildpaths.JsGlobalScopeContainerWizard;

//SelectedElements: IJavaScriptProject && size == 1
public class AddLibraryToBuildpathAction extends BuildpathModifierAction {

	public AddLibraryToBuildpathAction(IWorkbenchSite site) {
		this(site, null, PlatformUI.getWorkbench().getProgressService());
	}
	
	public AddLibraryToBuildpathAction(IRunnableContext context, ISetSelectionTarget selectionTarget) {
		this(null, selectionTarget, context);
    }

	private AddLibraryToBuildpathAction(IWorkbenchSite site, ISetSelectionTarget selectionTarget, IRunnableContext context) {
		super(site, selectionTarget, BuildpathModifierAction.ADD_LIB_TO_BP);

		setText(NewWizardMessages.NewSourceContainerWorkbookPage_ToolBar_AddLibCP_label);
		setImageDescriptor(JavaPluginImages.DESC_OBJS_LIBRARY);
		setToolTipText(NewWizardMessages.NewSourceContainerWorkbookPage_ToolBar_AddLibCP_tooltip);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public String getDetailedDescription() {
	    return NewWizardMessages.PackageExplorerActionGroup_FormText_Default_toBuildpath_library;
	}

	/**
	 * {@inheritDoc}
	 */
	public void run() {
		final IJavaScriptProject project= (IJavaScriptProject)getSelectedElements().get(0);

		Shell shell= getShell();
		if (shell == null) {
			shell= JavaScriptPlugin.getActiveWorkbenchShell();
		}

		IIncludePathEntry[] classpath;
		try {
			classpath= project.getRawIncludepath();
		} catch (JavaScriptModelException e1) {
			showExceptionDialog(e1, NewWizardMessages.AddLibraryToBuildpathAction_ErrorTitle);
			return;
		}

		JsGlobalScopeContainerWizard wizard= new JsGlobalScopeContainerWizard((IIncludePathEntry) null, project, classpath) {

			/**
			 * {@inheritDoc}
			 */
			public boolean performFinish() {
				if (super.performFinish()) {
					IWorkspaceRunnable op= new IWorkspaceRunnable() {
						public void run(IProgressMonitor monitor) throws CoreException, OperationCanceledException {
							try {
								finishPage(monitor);
							} catch (InterruptedException e) {
								throw new OperationCanceledException(e.getMessage());
							}
						}
					};
					try {
						ISchedulingRule rule= null;
						Job job= Job.getJobManager().currentJob();
						if (job != null)
							rule= job.getRule();
						IRunnableWithProgress runnable= null;
						if (rule != null)
							runnable= new WorkbenchRunnableAdapter(op, rule, true);
						else
							runnable= new WorkbenchRunnableAdapter(op, ResourcesPlugin.getWorkspace().getRoot());
						getContainer().run(false, true, runnable);
					} catch (InvocationTargetException e) {
						JavaScriptPlugin.log(e);
						return false;
					} catch  (InterruptedException e) {
						return false;
					}
					return true;
				} 
				return false;
			}

			private void finishPage(IProgressMonitor pm) throws InterruptedException {
				IIncludePathEntry[] selected= getNewEntries();
				if (selected != null) {
					try {
						pm.beginTask(NewWizardMessages.ClasspathModifier_Monitor_AddToBuildpath, 4); 

						List addedEntries= new ArrayList();
						for (int i= 0; i < selected.length; i++) {
							addedEntries.add(new CPListElement(project, IIncludePathEntry.CPE_CONTAINER, selected[i].getPath(), null));
						}

						pm.worked(1);
						if (pm.isCanceled())
							throw new InterruptedException();

						List existingEntries= ClasspathModifier.getExistingEntries(project);
						ClasspathModifier.setNewEntry(existingEntries, addedEntries, project, new SubProgressMonitor(pm, 1));
						if (pm.isCanceled())
							throw new InterruptedException();

						ClasspathModifier.commitClassPath(existingEntries, project, new SubProgressMonitor(pm, 1));
						
			        	BuildpathDelta delta= new BuildpathDelta(getToolTipText());
			        	delta.setNewEntries((CPListElement[])existingEntries.toArray(new CPListElement[existingEntries.size()]));
			        	informListeners(delta);

						List result= new ArrayList(addedEntries.size());
						for (int i= 0; i < addedEntries.size(); i++) {
							result.add(new JsGlobalScopeContainer(project, selected[i]));
						}
						selectAndReveal(new StructuredSelection(result));

						pm.worked(1);
					} catch (CoreException e) {
						showExceptionDialog(e, NewWizardMessages.AddLibraryToBuildpathAction_ErrorTitle);
					} finally {
						pm.done();
					}
				}
			}
		};
		wizard.setNeedsProgressMonitor(true);

		WizardDialog dialog= new WizardDialog(shell, wizard);
		PixelConverter converter= new PixelConverter(shell);
		dialog.setMinimumPageSize(converter.convertWidthInCharsToPixels(70), converter.convertHeightInCharsToPixels(20));
		dialog.create();
		dialog.open();
	}

	protected boolean canHandle(IStructuredSelection selection) {
		if (selection.size() != 1)
			return false;
		
		if (!(selection.getFirstElement() instanceof IJavaScriptProject))
			return false;
			
		return true;
	}

}
