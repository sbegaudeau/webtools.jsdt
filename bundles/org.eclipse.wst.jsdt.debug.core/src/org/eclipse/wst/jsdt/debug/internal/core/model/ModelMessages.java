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
package org.eclipse.wst.jsdt.debug.internal.core.model;

import org.eclipse.osgi.util.NLS;

/**
 * Message declarations
 * 
 * @since 1.0
 */
public class ModelMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.wst.jsdt.debug.internal.core.model.modelmessages"; //$NON-NLS-1$
	public static String debug_target_name;
	public static String JavaScriptValue_object_value_label;
	public static String JSDIDebugTarget_jsdi_debug_target;
	public static String JSDIDebugTarget_not_support_disconnect;
	public static String JSDIDebugTarget_not_support_terminate;
	public static String JSDIDebugTarget_recieved_unknown_event;
	public static String JSDIDebugTarget_unsupported_operation;
	public static String thread_timed_out_trying_to_suspend;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, ModelMessages.class);
	}

	private ModelMessages() {
	}
}
