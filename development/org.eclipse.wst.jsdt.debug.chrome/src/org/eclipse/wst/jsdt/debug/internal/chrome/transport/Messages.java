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
package org.eclipse.wst.jsdt.debug.internal.chrome.transport;

import org.eclipse.osgi.util.NLS;

/**
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.wst.jsdt.debug.internal.chrome.transport.messages"; //$NON-NLS-1$
	public static String cannot_create_packet_with_no_event;
	public static String cannot_create_pakcet_null_json;
	public static String cannot_create_request_null_command;
	public static String cannot_create_response_null_command;
	public static String no_command_in_json_response;
	public static String no_command_in_request_json;
	public static String no_event_found_in_json;
	public static String no_packet_type_in_json;
	public static String no_tool_found_in_packet_json;
	public static String packet_tools_service_name_cannot_be_null;
	public static String packet_type_cannot_be_null;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
