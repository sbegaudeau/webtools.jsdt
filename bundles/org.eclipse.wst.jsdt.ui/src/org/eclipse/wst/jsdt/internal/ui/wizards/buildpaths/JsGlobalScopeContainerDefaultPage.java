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

import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.core.IClasspathEntry;
import org.eclipse.wst.jsdt.core.IJavaProject;
import org.eclipse.wst.jsdt.core.JavaCore;
import org.eclipse.wst.jsdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.wst.jsdt.internal.ui.JavaPluginImages;
import org.eclipse.wst.jsdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.wst.jsdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.wst.jsdt.ui.wizards.IJsGlobalScopeContainerPage;
import org.eclipse.wst.jsdt.ui.wizards.IJsGlobalScopeContainerPageExtension;
import org.eclipse.wst.jsdt.ui.wizards.NewElementWizardPage;

/**
  */
public class JsGlobalScopeContainerDefaultPage extends NewElementWizardPage implements IJsGlobalScopeContainerPage, IJsGlobalScopeContainerPageExtension {

	private StringDialogField fEntryField;
	private ArrayList fUsedPaths;

	/**
	 * Constructor for JsGlobalScopeContainerDefaultPage.
	 */
	public JsGlobalScopeContainerDefaultPage() {
		super("JsGlobalScopeContainerDefaultPage"); //$NON-NLS-1$
		setTitle(NewWizardMessages.JsGlobalScopeContainerDefaultPage_title); 
		setDescription(NewWizardMessages.JsGlobalScopeContainerDefaultPage_description); 
		setImageDescriptor(JavaPluginImages.DESC_WIZBAN_ADD_LIBRARY);
		
		fUsedPaths= new ArrayList();
		
		fEntryField= new StringDialogField();
		fEntryField.setLabelText(NewWizardMessages.JsGlobalScopeContainerDefaultPage_path_label); 
		fEntryField.setDialogFieldListener(new IDialogFieldListener() {
			public void dialogFieldChanged(DialogField field) {
				validatePath();
			}
		});
		validatePath();
	}

	private void validatePath() {
		StatusInfo status= new StatusInfo();
		String str= fEntryField.getText();
		if (str.length() == 0) {
			status.setError(NewWizardMessages.JsGlobalScopeContainerDefaultPage_path_error_enterpath); 
		} else if (!Path.ROOT.isValidPath(str)) {
			status.setError(NewWizardMessages.JsGlobalScopeContainerDefaultPage_path_error_invalidpath); 
		} else {
			IPath path= new Path(str);
			if (path.segmentCount() == 0) {
				status.setError(NewWizardMessages.JsGlobalScopeContainerDefaultPage_path_error_needssegment); 
			} else if (fUsedPaths.contains(path)) {
				status.setError(NewWizardMessages.JsGlobalScopeContainerDefaultPage_path_error_alreadyexists); 
			}
		}
		updateStatus(status);
	}

	/* (non-Javadoc)
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite composite= new Composite(parent, SWT.NONE);
		GridLayout layout= new GridLayout();
		layout.numColumns= 1;
		composite.setLayout(layout);
		
		fEntryField.doFillIntoGrid(composite, 2);
		LayoutUtil.setHorizontalGrabbing(fEntryField.getTextControl(null));
		
		fEntryField.setFocus();
		
		setControl(composite);
		Dialog.applyDialogFont(composite);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(composite, IJavaHelpContextIds.CLASSPATH_CONTAINER_DEFAULT_PAGE);
	}

	/* (non-Javadoc)
	 * @see IJsGlobalScopeContainerPage#finish()
	 */
	public boolean finish() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see IJsGlobalScopeContainerPage#getSelection()
	 */
	public IClasspathEntry getSelection() {
		return JavaCore.newContainerEntry(new Path(fEntryField.getText()));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.ui.wizards.IJsGlobalScopeContainerPageExtension#initialize(org.eclipse.wst.jsdt.core.IJavaProject, org.eclipse.wst.jsdt.core.IClasspathEntry)
	 */
	public void initialize(IJavaProject project, IClasspathEntry[] currentEntries) {
		for (int i= 0; i < currentEntries.length; i++) {
			IClasspathEntry curr= currentEntries[i];
			if (curr.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
				fUsedPaths.add(curr.getPath());
			}
		}
	}		

	/* (non-Javadoc)
	 * @see IJsGlobalScopeContainerPage#setSelection(IClasspathEntry)
	 */
	public void setSelection(IClasspathEntry containerEntry) {
		if (containerEntry != null) {
			fUsedPaths.remove(containerEntry.getPath());
			fEntryField.setText(containerEntry.getPath().toString());
		} else {
			fEntryField.setText(""); //$NON-NLS-1$
		}
	}



}
