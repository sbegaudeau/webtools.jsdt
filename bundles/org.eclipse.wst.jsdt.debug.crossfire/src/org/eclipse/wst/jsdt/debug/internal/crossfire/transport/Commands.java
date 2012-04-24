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
package org.eclipse.wst.jsdt.debug.internal.crossfire.transport;

/**
 * Interface for all the command constants
 * 
 * @since 1.0
 */
public interface Commands {

	/**
	 * The "backtrace" command
	 */
	public static final String BACKTRACE = "backtrace"; //$NON-NLS-1$
	/**
	 * The "changebreakpoint" command
	 */
	public static final String CHANGE_BREAKPOINT = "changeBreakpoint"; //$NON-NLS-1$
	/**
	 * The "deletebreakpoint" command
	 */
	public static final String DELETE_BREAKPOINTS = "deleteBreakpoints"; //$NON-NLS-1$
	/**
	 * The "continue" command
	 */
	public static final String CONTINUE = "continue"; //$NON-NLS-1$
	/**
	 * The "disabletools" command
	 */
	public static final String DISABLE_TOOLS = "disableTools"; //$NON-NLS-1$
	/**
	 * The "enabletools" command
	 */
	public static final String ENABLE_TOOLS = "enableTools"; //$NON-NLS-1$
	/**
	 * The "evaluate" command
	 */
	public static final String EVALUATE = "evaluate"; //$NON-NLS-1$
	/**
	 * The "frame" command
	 */
	public static final String FRAME = "frame"; //$NON-NLS-1$
	/**
	 * The "getbreakpoints" command
	 */
	public static final String GET_BREAKPOINTS = "getBreakpoints"; //$NON-NLS-1$
	/**
	 * The "gettools" command
	 */
	public static final String GET_TOOLS = "getTools"; //$NON-NLS-1$
	/**
	 * The "inspect" command
	 */
	public static final String INSPECT = "inspect"; //$NON-NLS-1$
	/**
	 * The "listcontexts" command
	 */
	public static final String LISTCONTEXTS = "listContexts"; //$NON-NLS-1$
	/**
	 * The "lookup" command
	 */
	public static final String LOOKUP = "lookup"; //$NON-NLS-1$
	/**
	 * The "scopes" command
	 */
	public static final String SCOPES = "scopes"; //$NON-NLS-1$
	/**
	 * The "scripts" command
	 */
	public static final String SCRIPTS = "scripts"; //$NON-NLS-1$
	/**
	 * The "setbreakpoint" command
	 */
	public static final String SET_BREAKPOINTS = "setBreakpoints"; //$NON-NLS-1$
	/**
	 * The "in" step action
	 */
	public static final String STEP_IN = "in"; //$NON-NLS-1$
	/**
	 * The "next" step action 
	 */
	public static final String STEP_NEXT = "next"; //$NON-NLS-1$
	/**
	 * The "out" step action
	 */
	public static final String STEP_OUT = "out"; //$NON-NLS-1$
	/**
	 * The "source" command
	 */
	public static final String SOURCE = "source"; //$NON-NLS-1$
	/**
	 * The "suspend" command
	 */
	public static final String SUSPEND = "suspend"; //$NON-NLS-1$
	/** 
	 * The "createcontext" command
	 */
	public static final String CREATE_CONTEXT = "createContext"; //$NON-NLS-1$
	/**
	 * The "version" command
	 */
	public static final String VERSION = "version"; //$NON-NLS-1$

}
