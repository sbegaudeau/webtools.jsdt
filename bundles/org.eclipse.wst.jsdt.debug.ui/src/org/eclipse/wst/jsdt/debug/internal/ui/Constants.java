/*******************************************************************************
 * Copyright (c) 2008, 2010 IBM Corporation and others.
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
 * Constants for the {@link JavaScriptDebugUIPlugin}
 * 
 * @since 1.1
 */
public interface Constants {

	/**
	 * Constant for the preference for showing / hiding functions from the variables view
	 * <br><br>
	 * Value is: <code>org.eclipse.wst.jsdt.debug.ui.show_functions</code>
	 */
	public static final String SHOW_FUNCTIONS = JavaScriptDebugUIPlugin.PLUGIN_ID + ".show_functions"; //$NON-NLS-1$
	/**
	 * Constant for the preference for showing / hiding prototypes from the variables view
	 * <br><br>
	 * Value is: <code>org.eclipse.wst.jsdt.debug.ui.show_protoypes</code>
	 */
	public static final String SHOW_PROTOTYPES = JavaScriptDebugUIPlugin.PLUGIN_ID + ".show_prototypes"; //$NON-NLS-1$
	/**
	 * Constant for the preference for showing / hiding the <code>this</code> variable from the variables view
	 * <br><br>
	 * Value is: <code>org.eclipse.wst.jsdt.debug.ui.show_this</code>
	 */
	public static final String SHOW_THIS = JavaScriptDebugUIPlugin.PLUGIN_ID + "show_this"; //$NON-NLS-1$
}
