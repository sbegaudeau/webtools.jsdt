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
 * Interface for all the attribute constants
 * 
 * @since 1.0
 */
public interface Attributes {

	/**
	 * Result meaning everything is OK
	 */
	public static final int OK = 0;
	/**
	 * Result meaning that tab is in a state that cannot accept the sent request
	 */
	public static final int ILLEGAL_TAB_STATE = 1;
	/**
	 * Result meaning the given tab id does not exist
	 */
	public static final int UNKNOWN_TAB = 2;
	/**
	 * Result meaning the debugger encountered an error
	 */
	public static final int DEBUGGER_ERROR = 3;
	/**
	 * Result meaning the request is unknown
	 */
	public static final int UNKNOWN_COMMAND = 4;
	
	/**
	 * The name for the development tools service
	 * <br><br>
	 * Value is: <code>DevToolsService</code>
	 */
	public static final String TOOL_DEVTOOLSRVC = "DevToolsService"; //$NON-NLS-1$
	/**
	 * The name for the V8 debugger service
	 * <br><br>
	 * Value is: <code>V8Debugger</code>
	 */
	public static final String TOOL_V8DEBUGGER = "V8Debugger"; //$NON-NLS-1$
	/**
	 * The "command" attribute
	 */
	public static final String COMMAND = "command"; //$NON-NLS-1$
	/**
	 * The "data" attribute
	 */
	public static final String DATA = "data"; //$NON-NLS-1$
	/**
	 * The "dest" attribute
	 */
	public static final String DESTINATION = "dest"; //$NON-NLS-1$
	/**
	 * The "handshake" attribute
	 */
	public static final String HANDSHAKE = "handshake"; //$NON-NLS-1$
	/**
	 * The "result" attribute
	 */
	public static final String RESULT = "result"; //$NON-NLS-1$
	/**
	 * The "type" attribute
	 */
	public static final String TYPE = "type"; //$NON-NLS-1$
	/**
	 * the "tool" attribute
	 */
	public static final String TOOL = "tool"; //$NON-NLS-1$
	/**
	 * The "value" attribute
	 */
	public static final String VALUE = "value"; //$NON-NLS-1$
}
