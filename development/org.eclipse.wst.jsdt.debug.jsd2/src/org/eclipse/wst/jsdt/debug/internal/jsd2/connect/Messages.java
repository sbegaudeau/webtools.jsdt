/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.jsd2.connect;

import org.eclipse.osgi.util.NLS;

/**
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.wst.jsdt.debug.internal.jsd2.connect.messages"; //$NON-NLS-1$
	public static String host_arg_desc;
	public static String host_arg_name;
	public static String JSD2AttachingConnector_description;
	public static String JSD2AttachingConnector_name;
	public static String port_arg_desc;
	public static String port_arg_name;
	public static String timeout;
	public static String timeout_desc;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
