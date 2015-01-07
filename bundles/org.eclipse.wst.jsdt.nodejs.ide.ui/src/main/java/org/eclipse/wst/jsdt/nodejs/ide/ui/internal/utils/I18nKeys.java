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
package org.eclipse.wst.jsdt.nodejs.ide.ui.internal.utils;

/**
 * Utility interface holding the keys of the internationalization.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public interface I18nKeys {
	/**
	 * The location of node.js is missing from the preferences.
	 */
	String NODEJS_LAUNCH_MISSING_NODEJS_LOCATION = "NodeJsLaunch.MissingNodeJsLocation"; //$NON-NLS-1$

	/**
	 * The location of the application is missing from the launch configuration.
	 */
	String NODEJS_LAUNCH_MISSING_APPLICATION_LOCATION = "NodeJsLaunch.MissingApplicationLocation"; //$NON-NLS-1$

	/**
	 * The name of the general node.js tab in the launch configuration.
	 */
	String NODEJS_GENERAL_TAB_NAME = "NodeJsGeneralTab.Name"; //$NON-NLS-1$

	/**
	 * The label of the application group.
	 */
	String NODEJS_GENERAL_APPLICATION_GROUP_LABEL = "NodeJsGeneralTab.ApplicationGroupLabel"; //$NON-NLS-1$

	/**
	 * The label of the application.
	 */
	String NODEJS_GENERAL_APPLICATION_LABEL = "NodeJsGeneralTab.ApplicationLabel"; //$NON-NLS-1$

	/**
	 * The label of the browse button.
	 */
	String NODEJS_GENERAL_APPLICATION_BROWSE_LABEL = "NodeJsGeneralTab.BrowseLabel"; //$NON-NLS-1$

	/**
	 * The title of the dialog used to search the node.js application to launch.
	 */
	String NODEJS_GENERAL_APPLICATION_DIALOG_TITLE = "NodeJsGeneralTab.ApplicationDialogTitle"; //$NON-NLS-1$

	/**
	 * The error message displayed when the node.js application is missing.
	 */
	String NODEJS_GENERAL_APPLICATION_MISSING = "NodeJsGeneralTab.ApplicationMissing"; //$NON-NLS-1$

	/**
	 * The description of the preference page.
	 */
	String PREFERENCE_PAGE_DESCRIPTION = "PreferencePage.Description"; //$NON-NLS-1$

	/**
	 * The label of the node location field.
	 */
	String PREFERENCES_PAGE_NODE_LOCATION = "PreferencePage.NodeLocation"; //$NON-NLS-1$

	/**
	 * The label of the npm location field.
	 */
	String PREFERENCES_PAGE_NPM_LOCATION = "PreferencePage.NpmLocation"; //$NON-NLS-1$

	/**
	 * The name of the MEAN project page.
	 */
	String MEAN_PROJECT_PAGE_NAME = "MEANProjectPage.Name"; //$NON-NLS-1$

	/**
	 * The title of the MEAN project page.
	 */
	String MEAN_PROJECT_PAGE_TITLE = "MEANProjectPage.Title"; //$NON-NLS-1$

	/**
	 * The description of the MEAN project page.
	 */
	String MEAN_PROJECT_PAGE_DESCRIPTION = "MEANProjectPage.Description"; //$NON-NLS-1$

	/**
	 * The main task of the progress monitor during the creation of a new project.
	 */
	String MEAN_PROJECT_WIZARD_MONITOR_NEW_MEAN_PROJECT = "MEANProjectWizard.MonitorNewMeanProject"; //$NON-NLS-1$

	/**
	 * The initialization task of the progress monitor during the creation of a new project.
	 */
	String MEAN_PROJECT_WIZARD_MONITOR_INITIALIZING_DATA = "MEANProjectWizard.MonitorInitializingData"; //$NON-NLS-1$

	/**
	 * The npm install task of the progress monitor during the creation of a new project.
	 */
	String MEAN_PROJECT_WIZARD_MONITOR_NPM_INSTALL = "MeanProjectWizard.MonitorNpmInstall"; //$NON-NLS-1$
}
