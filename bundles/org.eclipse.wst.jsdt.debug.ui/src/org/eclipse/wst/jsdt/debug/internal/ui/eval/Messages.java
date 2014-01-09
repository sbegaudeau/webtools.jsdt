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
package org.eclipse.wst.jsdt.debug.internal.ui.eval;

import org.eclipse.osgi.util.NLS;

/**
 * 
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.wst.jsdt.debug.internal.ui.eval.messages"; //$NON-NLS-1$
	public static String cannot_find_debug_target;
	public static String cursor_position_not_valid;
	public static String empty_editor;
	public static String exception_running_to_line;
	public static String exe_did_not_enter__0__before_returning;
	public static String hyperlink_step_into;
	public static String missing_doc;
	public static String only_in_the_js_editor;
	public static String selected_line_not_valid;
	public static String step_into_only_top_frame;
	public static String step_into_selection;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
