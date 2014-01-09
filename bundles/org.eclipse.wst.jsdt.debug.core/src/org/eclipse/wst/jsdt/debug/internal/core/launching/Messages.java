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
package org.eclipse.wst.jsdt.debug.internal.core.launching;

import org.eclipse.osgi.util.NLS;

/**
 * 
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.wst.jsdt.debug.internal.core.launching.messages"; //$NON-NLS-1$
	public static String acquiring_connector;
	public static String argument_map_null;
	public static String attaching_to_vm;
	public static String connect_thread;
	public static String could_not_locate_connector;
	public static String creating_debug_target;
	public static String javascript_process_name;
	public static String launching_js_debug_delegate;
	public static String waiting_for_vm_to_connect;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
