/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.wizards.buildpaths;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.jsdt.core.IIncludePathEntry;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.internal.ui.util.ExceptionHandler;
import org.eclipse.wst.jsdt.internal.ui.util.PixelConverter;
import org.eclipse.wst.jsdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.wst.jsdt.ui.wizards.IJsGlobalScopeContainerPage;
import org.eclipse.wst.jsdt.ui.wizards.IJsGlobalScopeContainerPageExtension;
import org.eclipse.wst.jsdt.ui.wizards.IJsGlobalScopeContainerPageExtension2;


/**
  */
public class JsGlobalScopeContainerWizard extends Wizard {

	private JsGlobalScopeContainerDescriptor fPageDesc;
	private IIncludePathEntry fEntryToEdit;

	private IIncludePathEntry[] fNewEntries;
	private IJsGlobalScopeContainerPage fContainerPage;
	private IJavaScriptProject fCurrProject;
	private IIncludePathEntry[] fCurrClasspath;
	
	private JsGlobalScopeContainerSelectionPage fSelectionWizardPage;

	/**
	 * Constructor for JsGlobalScopeContainerWizard.
	 * @param entryToEdit entry to edit
	 * @param currProject current project
	 * @param currEntries entries currently in classpath
	 */
	public JsGlobalScopeContainerWizard(IIncludePathEntry entryToEdit, IJavaScriptProject currProject, IIncludePathEntry[] currEntries) {
		this(entryToEdit, null, currProject, currEntries);
	}
	
	/**
	 * Constructor for JsGlobalScopeContainerWizard.
	 * @param pageDesc page description
	 * @param currProject current project
	 * @param currEntries entries currently in classpath
	 */
	public JsGlobalScopeContainerWizard(JsGlobalScopeContainerDescriptor pageDesc, IJavaScriptProject currProject, IIncludePathEntry[] currEntries) {
		this(null, pageDesc, currProject, currEntries);	
	}

	private JsGlobalScopeContainerWizard(IIncludePathEntry entryToEdit, JsGlobalScopeContainerDescriptor pageDesc, IJavaScriptProject currProject, IIncludePathEntry[] currEntries) {
		fEntryToEdit= entryToEdit;
		fPageDesc= pageDesc;
		fNewEntries= null;
		
		fCurrProject= currProject;
		fCurrClasspath= currEntries;
		
		String title;
		if (entryToEdit == null) {
			title= NewWizardMessages.JsGlobalScopeContainerWizard_new_title; 
		} else {
			title= NewWizardMessages.JsGlobalScopeContainerWizard_edit_title; 
		}
		setWindowTitle(title);
	}
		
	public IIncludePathEntry[] getNewEntries() {
		return fNewEntries;
	}

	/* (non-Javadoc)
	 * @see IWizard#performFinish()
	 */
	public boolean performFinish() {
		if (fContainerPage != null) {
			if (fContainerPage.finish()) {
				if (fEntryToEdit == null && fContainerPage instanceof IJsGlobalScopeContainerPageExtension2) {
					fNewEntries= ((IJsGlobalScopeContainerPageExtension2) fContainerPage).getNewContainers();
				} else {
					IIncludePathEntry entry= fContainerPage.getSelection();
					fNewEntries= (entry != null) ? new IIncludePathEntry[] { entry } : null;
				}
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see IWizard#addPages()
	 */
	public void addPages() {
		if (fPageDesc != null) {
			fContainerPage= getContainerPage(fPageDesc);
			addPage(fContainerPage);			
		} else if (fEntryToEdit == null) { // new entry: show selection page as first page
			JsGlobalScopeContainerDescriptor[] containers= JsGlobalScopeContainerDescriptor.getDescriptors();

			fSelectionWizardPage= new JsGlobalScopeContainerSelectionPage(containers);
			addPage(fSelectionWizardPage);

			// add as dummy, will not be shown
			fContainerPage= new JsGlobalScopeContainerDefaultPage();
			addPage(fContainerPage);
		} else { // fPageDesc == null && fEntryToEdit != null
			JsGlobalScopeContainerDescriptor[] containers= JsGlobalScopeContainerDescriptor.getDescriptors();
			JsGlobalScopeContainerDescriptor descriptor= findDescriptorPage(containers, fEntryToEdit);
			fContainerPage= getContainerPage(descriptor);
			addPage(fContainerPage);				
		}
		super.addPages();
	}
	
	private IJsGlobalScopeContainerPage getContainerPage(JsGlobalScopeContainerDescriptor pageDesc) {
		IJsGlobalScopeContainerPage containerPage= null;
		if (pageDesc != null) {
			IJsGlobalScopeContainerPage page= pageDesc.getPage();
			if (page != null) {
				return page; // if page is already created, avoid double initialization
			}
			try {
				containerPage= pageDesc.createPage();
			} catch (CoreException e) {
				handlePageCreationFailed(e);
				containerPage= null;
			}
		}

		if (containerPage == null)	{
			containerPage= new JsGlobalScopeContainerDefaultPage();
			if (pageDesc != null) {
				pageDesc.setPage(containerPage); // avoid creation next time
			}
		}

		if (containerPage instanceof IJsGlobalScopeContainerPageExtension) {
			((IJsGlobalScopeContainerPageExtension) containerPage).initialize(fCurrProject, fCurrClasspath);
		}

		containerPage.setSelection(fEntryToEdit);
		containerPage.setWizard(this);
		return containerPage;
	}
	
	/* (non-Javadoc)
	 * @see IWizard#getNextPage(IWizardPage)
	 */
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == fSelectionWizardPage) {

			JsGlobalScopeContainerDescriptor selected= fSelectionWizardPage.getSelected();
			fContainerPage= getContainerPage(selected);
			
			return fContainerPage;
		}
		return super.getNextPage(page);
	}
	
	private void handlePageCreationFailed(CoreException e) {
		String title= NewWizardMessages.JsGlobalScopeContainerWizard_pagecreationerror_title; 
		String message= NewWizardMessages.JsGlobalScopeContainerWizard_pagecreationerror_message; 
		ExceptionHandler.handle(e, getShell(), title, message);
	}
	
	
	private JsGlobalScopeContainerDescriptor findDescriptorPage(JsGlobalScopeContainerDescriptor[] containers, IIncludePathEntry entry) {
		for (int i = 0; i < containers.length; i++) {
			if (containers[i].canEdit(entry)) {
				return containers[i];
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#dispose()
	 */
	public void dispose() {
		if (fSelectionWizardPage != null) {
			JsGlobalScopeContainerDescriptor[] descriptors= fSelectionWizardPage.getContainers();
			for (int i= 0; i < descriptors.length; i++) {
				descriptors[i].dispose();
			}
		}
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see IWizard#canFinish()
	 */
	public boolean canFinish() {
		if (fSelectionWizardPage != null) {
			if (!fContainerPage.isPageComplete()) {
				return false;
			}
		}
		if (fContainerPage != null) {
			return fContainerPage.isPageComplete();
		}
		return false;
	}
	
	public static int openWizard(Shell shell, JsGlobalScopeContainerWizard wizard) {
		WizardDialog dialog= new WizardDialog(shell, wizard);
		PixelConverter converter= new PixelConverter(JFaceResources.getDialogFont());
		dialog.setMinimumPageSize(converter.convertWidthInCharsToPixels(70), converter.convertHeightInCharsToPixels(20));
		dialog.create();
		return dialog.open();
	}
	
}
