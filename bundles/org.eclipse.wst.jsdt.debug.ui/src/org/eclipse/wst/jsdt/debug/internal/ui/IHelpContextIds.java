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
package org.eclipse.wst.jsdt.debug.internal.ui;

/**
 * Constants defining help context ids
 * 
 * @since 1.0
 */
public interface IHelpContextIds {

	/**
	 * Help constant for the line breakpoint editor
	 */
	public static final String STANDARD_BREAKPOINT_EDITOR = JavaScriptDebugUIPlugin.PLUGIN_ID + ".standard_breakpoint_editor"; //$NON-NLS-1$
	/**
	 * Help constant for the general breakpoint property page
	 */
	public static final String JAVASCRIPT_BREAKPOINT_PROPERTY_PAGE = JavaScriptDebugUIPlugin.PLUGIN_ID + ".breakpoint_property_page"; //$NON-NLS-1$
	/**
	 * Help constant for the connect tab
	 */
	public static final String CONNECT_TAB = JavaScriptDebugUIPlugin.PLUGIN_ID + ".connect_tab"; //$NON-NLS-1$
	/**
	 * Help constant for the Debug root preference page
	 * @since 1.1
	 */
	public static final String DEBUG_PREFERENCE_PAGE = JavaScriptDebugUIPlugin.PLUGIN_ID + ".debug_pref_page"; //$NON-NLS-1$
	/**
	 * Help constant for the Source tab
	 * @since 1.2
	 */
	public static final String SOURCE_LOOKUP_TAB = JavaScriptDebugUIPlugin.PLUGIN_ID + ".source_lookup_tab"; //$NON-NLS-1$
	/**
	 * Help constant for the Environment tab
	 * @since 1.2
	 */
	public static final String ENVIRONMENT_TAB = JavaScriptDebugUIPlugin.PLUGIN_ID + ".environment_tab"; //$NON-NLS-1$
	/**
	 * Help constant for the Common tab
	 * @since 1.2
	 */
	public static final String COMMON_TAB = JavaScriptDebugUIPlugin.PLUGIN_ID + ".common_tab"; //$NON-NLS-1$
}
