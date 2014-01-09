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

/**
 * Interface for all the command constants
 * 
 * @since 1.0
 */
public interface Commands {

	/**
	 * The "attach" command
	 */
	public static final String ATTACH = "attach"; //$NON-NLS-1$
	/**
	 * The "closed" command
	 */
	public static final String CLOSED = "closed"; //$NON-NLS-1$
	/**
	 * The "debugger_command" command
	 */
	public static final String DEBUGGER_COMMAND = "debugger_command"; //$NON-NLS-1$
	/**
	 * The "detach" command
	 */
	public static final String DETACH = "detach"; //$NON-NLS-1$
	/**
	 * The "evaluate_javascript" command
	 */
	public static final String EVALUATE_JAVASCRIPT = "evaluate_javascript"; //$NON-NLS-1$
	/**
	 * The "list_tabs" command
	 */
	public static final String LIST_TABS = "list_tabs"; //$NON-NLS-1$
	/**
	 * The "navigated" command
	 */
	public static final String NAVIGATED = "navigated"; //$NON-NLS-1$
	/**
	 * The "ping" command
	 */
	public static final String PING = "ping"; //$NON-NLS-1$
	/**
	 * The "version" command
	 */
	public static final String VERSION = "version"; //$NON-NLS-1$

}
