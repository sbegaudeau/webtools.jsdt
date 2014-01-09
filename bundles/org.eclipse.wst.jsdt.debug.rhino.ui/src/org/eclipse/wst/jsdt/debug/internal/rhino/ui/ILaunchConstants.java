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
 * Constants used for launching
 * 
 * @since 1.0
 */
public interface ILaunchConstants {

	/**
	 * The script attribute in a Rhino launch configuration
	 * <br><br>
	 * Value is: <code>script</code>
	 */
	public static final String ATTR_SCRIPT = "script";  //$NON-NLS-1$
	/**
	 * The log attribute in a Rhino launch configuration
	 * <br><br>
	 * Value is: <code>logexceptions</code>
	 */
	public static final String ATTR_LOG_INTERPRETER_EXCEPTIONS = "logexceptions"; //$NON-NLS-1$
	/**
	 * The EMCA version to interpret with attribute in a Rhino configuration
	 * <br><br>
	 * Value is: <code>emcaversion</code>
	 */
	public static final String ATTR_ECMA_VERSION = "ecmaversion"; //$NON-NLS-1$
	/**
	 * The Rhino optimization level attribute for a Rhino configuration
	 * <br><br>
	 * Value is: <code>optlevel</code>
	 */
	public static final String ATTR_OPT_LEVEL = "optlevel"; //$NON-NLS-1$
	/**
	 * The strict mode attribute in a Rhino configuration
	 * <br><br>
	 * Value is: <code>strictmode</code>
	 */
	public static final String ATTR_STRICT_MODE = "strictmode"; //$NON-NLS-1$
	/**
	 * The include path attribute in a Rhino configuration
	 * <br><br>
	 * Value is: <code>includepath</code>
	 */
	public static final String ATTR_INCLUDE_PATH = "includepath"; //$NON-NLS-1$
	/**
	 * The id for the Rhino launch configuration type
	 * <br><br>
	 * Value is: <code>org.eclipse.wst.jsdt.debug.rhino.ui.launch.config.type</code>
	 */
	public static final String LAUNCH_CONFIG_TYPE = "org.eclipse.wst.jsdt.debug.rhino.ui.launch.config.type"; //$NON-NLS-1$
	/**
	 * The empty string
	 */
	public static final String EMPTY_STRING = ""; //$NON-NLS-1$
	/**
	 * The ECMA version level for 1.7
	 * <br><br>
	 * Value is: <code>170</code>
	 */
	public static final String ECMA_170 = "170"; //$NON-NLS-1$
}
