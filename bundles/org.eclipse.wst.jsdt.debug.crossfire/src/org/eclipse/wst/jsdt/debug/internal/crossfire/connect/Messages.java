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
package org.eclipse.wst.jsdt.debug.internal.crossfire.connect;

import org.eclipse.osgi.util.NLS;

/**
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.wst.jsdt.debug.internal.crossfire.connect.messages"; //$NON-NLS-1$
	public static String attach_connector_desc;
	public static String attach_connector_name;
	public static String auto_attach_desc;
	public static String auto_attach_label;
	public static String cannot_launch_browser_not_localhost;
	public static String console_arg_description;
	public static String console_arg_label;
	public static String crossfire_remote_attach;
	public static String dom_arg_description;
	public static String dom_arg_label;
	public static String failed_to_attach_to_auto_browser;
	public static String host_arg_desc;
	public static String host_arg_name;
	public static String inspector_arg_description;
	public static String inspector_arg_label;
	public static String net_arg_description;
	public static String net_arg_label;
	public static String port_arg_desc;
	public static String port_arg_name;
	public static String timeout;
	public static String timeout_desc;
	public static String trace_arg_description;
	public static String trace_arg_label;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
