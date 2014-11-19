/*******************************************************************************
 * Copyright (c) 2014 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.nodejs.ide.ui.internal.projects;

import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.utils.I18n;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.utils.I18nKeys;

/**
 * The first page of the wizard where the user will select the name of the project along with its location.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MEANProjectWizardPage extends WizardNewProjectCreationPage {
	/**
	 * The constructor.
	 */
	public MEANProjectWizardPage() {
		super(I18n.getString(I18nKeys.MEAN_PROJECT_PAGE_NAME));
		this.setTitle(I18n.getString(I18nKeys.MEAN_PROJECT_PAGE_TITLE));
		this.setDescription(I18n.getString(I18nKeys.MEAN_PROJECT_PAGE_DESCRIPTION));
	}
}
