/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.wizards;

import org.eclipse.osgi.util.NLS;

public class FirefoxMessages extends NLS {
	
	private static final String BUNDLE_NAME= "org.eclipse.wst.jsdt.internal.ui.wizards.FirefoxMessages";//$NON-NLS-1$

	private FirefoxMessages() {
		// Do not instantiate
	}
	
	public static String FirefoxLibraryWizardPage_title;
	public static String FirefoxLibraryWizardPage_FirefoxLibraryAdded;
	public static String FirefoxLibraryWizardPage_BrowserSupport;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, FirefoxMessages.class);
	}

}
