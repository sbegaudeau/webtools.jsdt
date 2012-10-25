/*******************************************************************************
 * Copyright (c) 2010, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.rhino.ui.launching;

import org.eclipse.osgi.util.NLS;

/**
 * @since 1.0
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.wst.jsdt.debug.internal.rhino.ui.launching.messages"; //$NON-NLS-1$
	public static String _script;
	public static String add_ext_script_button;
	public static String add_script_button;
	public static String bro_wse;
	public static String computing_script_scope;
	public static String config_name;
	public static String configuring_rhino_debugger;
	public static String connect_thread;
	public static String creating_js_debug_target;
	public static String creating_rhino_vm;
	public static String defaults_button;
	public static String down_button;
	public static String ecma_version_to_interpret_with;
	public static String failed_to_compute_scope;
	public static String include_path;
	public static String include_tab_name;
	public static String launch_script;
	public static String launching__;
	public static String log_interpreter_exceptions;
	public static String main;
	public static String options_group_name;
	public static String process_label;
	public static String provide_script_for_project;
	public static String remove_button;
	public static String rhino_opt_level;
	public static String RhinoMainTab_1;
	public static String script_not_a_file;
	public static String script_not_accessible;
	public static String script_not_in_workspace;
	public static String script_selection;
	public static String select_existing_config;
	public static String select_rhino_config;
	public static String select_scripts_to_add;
	public static String starting_rhino_interpreter;
	public static String starting_rhino_process;
	public static String strict_mode;
	public static String up_button;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
