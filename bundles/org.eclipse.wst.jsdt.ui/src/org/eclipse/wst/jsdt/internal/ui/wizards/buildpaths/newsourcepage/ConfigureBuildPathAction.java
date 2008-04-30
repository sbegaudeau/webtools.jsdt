/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Matt McCutchen - Bug 148313 [build path] "Configure Build Path" incorrectly appears for non-Java projects
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.wizards.buildpaths.newsourcepage;

import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.wst.jsdt.core.IIncludePathEntry;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IPackageFragmentRoot;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.internal.corext.util.JavaModelUtil;
import org.eclipse.wst.jsdt.internal.ui.JavaPluginImages;
import org.eclipse.wst.jsdt.internal.ui.packageview.JsGlobalScopeContainer;
import org.eclipse.wst.jsdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.wst.jsdt.internal.ui.preferences.BuildPathsPropertyPage;
import org.eclipse.wst.jsdt.internal.ui.wizards.NewWizardMessages;

//SelectedElements iff enabled: (IJavaScriptElement || JsGlobalScopeContainer || IAdaptable) && size == 1
public class ConfigureBuildPathAction extends BuildpathModifierAction {

	public ConfigureBuildPathAction(IWorkbenchSite site) {
		super(site, null, BuildpathModifierAction.CONFIGURE_BUILD_PATH);
		
		setText(NewWizardMessages.NewSourceContainerWorkbookPage_ToolBar_ConfigureBP_label);
		setImageDescriptor(JavaPluginImages.DESC_ELCL_CONFIGURE_BUILDPATH);
		setToolTipText(NewWizardMessages.NewSourceContainerWorkbookPage_ToolBar_ConfigureBP_tooltip);
		setDisabledImageDescriptor(JavaPluginImages.DESC_DLCL_CONFIGURE_BUILDPATH);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getDetailedDescription() {
	    return null;
	}
	
	public void run() {
		IProject project= null;
		Object firstElement= getSelectedElements().get(0);
		HashMap data= new HashMap();
		
		if (firstElement instanceof IJavaScriptElement) {
			IJavaScriptElement element= (IJavaScriptElement) firstElement;
			IPackageFragmentRoot root= (IPackageFragmentRoot) element.getAncestor(IJavaScriptElement.PACKAGE_FRAGMENT_ROOT);
			if (root != null) {
				try {
					data.put(BuildPathsPropertyPage.DATA_REVEAL_ENTRY, root.getRawIncludepathEntry());
				} catch (JavaScriptModelException e) {
					// ignore
				}
			}
			project= element.getJavaScriptProject().getProject();
		} else if (firstElement instanceof PackageFragmentRootContainer) {
			PackageFragmentRootContainer container= (PackageFragmentRootContainer) firstElement;
			project= container.getJavaProject().getProject();
			IIncludePathEntry entry= container instanceof JsGlobalScopeContainer ? ((JsGlobalScopeContainer) container).getClasspathEntry() : JavaScriptCore.newLibraryEntry(new Path("/x/y"), null, null); //$NON-NLS-1$
			data.put(BuildPathsPropertyPage.DATA_REVEAL_ENTRY, entry);
		} else {
			project= ((IResource) ((IAdaptable) firstElement).getAdapter(IResource.class)).getProject();
		}
		PreferencesUtil.createPropertyDialogOn(getShell(), project, BuildPathsPropertyPage.PROP_ID, null, data).open();
	}

	protected boolean canHandle(IStructuredSelection elements) {
		if (elements.size() != 1)
			return false;
	
		Object firstElement= elements.getFirstElement();
		
		if (firstElement instanceof IJavaScriptElement) {
			IJavaScriptElement element= (IJavaScriptElement) firstElement;
			IPackageFragmentRoot root= JavaModelUtil.getPackageFragmentRoot(element);
			if (root != null && root != element && root.isArchive()) {
				return false;
			}
			IJavaScriptProject project= element.getJavaScriptProject();
			if (project == null)
				return false;
			
			return project.getProject() != null;
		} else if (firstElement instanceof PackageFragmentRootContainer) {
			return true;
		} else if (firstElement instanceof IAdaptable) {
			IResource res= (IResource) ((IAdaptable) firstElement).getAdapter(IResource.class);
			if (res == null)
				return false;
			
			IProject project = res.getProject();
			if (project == null || !project.isOpen())
				return false;
			
			try {
				return project.hasNature(JavaScriptCore.NATURE_ID);
			} catch (CoreException e) {
				return false;
			}
		}
		return false;
	}

}
