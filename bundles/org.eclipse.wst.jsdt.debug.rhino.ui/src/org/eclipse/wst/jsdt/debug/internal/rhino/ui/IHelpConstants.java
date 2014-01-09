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
package org.eclipse.wst.jsdt.debug.internal.rhino.ui;

/**
 * Collection of help constants
 * 
 * @since 1.0
 */
public interface IHelpConstants {
	
	/**
	 * The help context id for the Rhino main tab in the launch dialog
	 * <br><br>
	 * Value is: <code>org.eclipse.wst.jsdt.debug.rhino.ui.rhino_main_tab_context</code>
	 */
	public static final String MAIN_TAB_CONTEXT = RhinoUIPlugin.PLUGIN_ID + ".rhino_main_tab_context"; //$NON-NLS-1$
	/**
	 * The help context id for the Rhino include tab in the launch dialog
	 * <br><br>
	 * Value is: <code>org.eclipse.wst.jsdt.debug.rhino.ui.rhino_include_tab_context</code>
	 */
	public static final String INCLUDE_TAB_CONTEXT = RhinoUIPlugin.PLUGIN_ID + ".rhino_include_tab_context"; //$NON-NLS-1$
	/**
	 * The help context id for the Rhino source tab in the launch dialog
	 * <br><br>
	 * Value is: <code>org.eclipse.wst.jsdt.debug.rhino.ui.rhino_source_tab_context</code>
	 */
	public static final String SOURCE_TAB_CONTEXT = RhinoUIPlugin.PLUGIN_ID + ".rhino_source_tab_context"; //$NON-NLS-1$
	/**
	 * The help context id for the Rhino common tab in the launch dialog
	 * <br><br>
	 * Value is: <code>org.eclipse.wst.jsdt.debug.rhino.ui.rhino_common_tab_context</code>
	 */
	public static final String COMMON_TAB_CONTEXT = RhinoUIPlugin.PLUGIN_ID + ".rhino_common_tab_context"; //$NON-NLS-1$
	/**
	 * The help context id for the Rhino preference page
	 * <br><br>
	 * The value is: <code>org.eclipse.wst.jsdt.debug.rhino.ui.rhino_preference_page</code>
	 */
	public static final String RHINO_PREFERENCE_PAGE = RhinoUIPlugin.PLUGIN_ID + ".rhino_preference_page"; //$NON-NLS-1$
}
