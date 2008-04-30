/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.ui.actions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.wst.jsdt.internal.ui.JavaPluginImages;
import org.eclipse.wst.jsdt.internal.ui.actions.ActionMessages;
import org.eclipse.wst.jsdt.internal.ui.wizards.NewSourceFolderCreationWizard;

/**
 * <p>Action that opens the new source folder wizard. The action initializes the wizard with the
 * selection as configured by {@link #setSelection(org.eclipse.jface.viewers.IStructuredSelection)} or the selection of
 * the active workbench window.</p>
 * 
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 *  
 *
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
 * (repeatedly) as the API evolves.
 */
public class OpenNewSourceFolderWizardAction extends AbstractOpenWizardAction {
	
	/**
	 * Creates an instance of the <code>OpenNewSourceFolderWizardAction</code>.
	 */
	public OpenNewSourceFolderWizardAction() {
		setText(ActionMessages.OpenNewSourceFolderWizardAction_text); 
		setDescription(ActionMessages.OpenNewSourceFolderWizardAction_description); 
		setToolTipText(ActionMessages.OpenNewSourceFolderWizardAction_tooltip); 
		setImageDescriptor(JavaPluginImages.DESC_WIZBAN_NEWSRCFOLDR);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJavaHelpContextIds.OPEN_SOURCEFOLDER_WIZARD_ACTION);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.ui.actions.AbstractOpenWizardAction#createWizard()
	 */
	protected final INewWizard createWizard() throws CoreException {
		return new NewSourceFolderCreationWizard();
	}
}
