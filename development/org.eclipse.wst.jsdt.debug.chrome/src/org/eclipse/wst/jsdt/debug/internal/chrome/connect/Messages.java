/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.chrome.connect;

import org.eclipse.osgi.util.NLS;

/**
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.wst.jsdt.debug.internal.chrome.connect.messages"; //$NON-NLS-1$
	public static String attach_to_google_chrome;
	public static String browser_arg_desc;
	public static String browser_arg_label;
	public static String google_chrome_attach;
	public static String host_arg_desc;
	public static String host_arg_label;
	public static String port_arg_desc;
	public static String port_arg_label;
	public static String timeout_arg_desc;
	public static String timeout_arg_label;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
