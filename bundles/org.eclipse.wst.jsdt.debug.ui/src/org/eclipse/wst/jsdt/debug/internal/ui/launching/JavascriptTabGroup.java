/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.ui.launching;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;
import org.eclipse.wst.jsdt.debug.internal.ui.IHelpContextIds;

/**
 * Default tab group for JavaScript debugging
 * 
 * @since 1.0
 */
public class JavascriptTabGroup extends AbstractLaunchConfigurationTabGroup {

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTabGroup#createTabs(org.eclipse.debug.ui.ILaunchConfigurationDialog, java.lang.String)
	 */
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		SourceLookupTab slt = new SourceLookupTab();
		slt.setHelpContextId(IHelpContextIds.SOURCE_LOOKUP_TAB);
		EnvironmentTab et = new EnvironmentTab();
		et.setHelpContextId(IHelpContextIds.ENVIRONMENT_TAB);
		CommonTab ct = new CommonTab();
		ct.setHelpContextId(IHelpContextIds.COMMON_TAB);
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				new JavaScriptConnectTab(),
				slt,
				et,
				ct
		};
		setTabs(tabs);
	}

}
