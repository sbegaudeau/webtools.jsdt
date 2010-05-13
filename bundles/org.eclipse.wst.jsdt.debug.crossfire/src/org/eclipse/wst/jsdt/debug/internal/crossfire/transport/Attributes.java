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
package org.eclipse.wst.jsdt.debug.internal.crossfire.transport;

/**
 * Interface for all the attribute constants
 * 
 * @since 1.0
 */
public interface Attributes {

	/**
	 * The "arguments" attribute
	 */
	public static final String ARGUMENTS = "arguments"; //$NON-NLS-1$
	/**
	 * The "body" attribute
	 */
	public static final String BODY = "body"; //$NON-NLS-1$
	/**
	 * The "command" attribute
	 */
	public static final String COMMAND = "command"; //$NON-NLS-1$
	/**
	 * The "context_id" attribute
	 */
	public static final String CONTEXT_ID = "context_id"; //$NON-NLS-1$
	/**
	 * The "contexts" attribute
	 */
	public static final String CONTEXTS = "contexts"; //$NON-NLS-1$
	/**
	 * The "data" attribute
	 */
	public static final String DATA = "data"; //$NON-NLS-1$
	/**
	 * The "handshake" attribute
	 */
	public static final String HANDSHAKE = "handshake"; //$NON-NLS-1$
	/**
	 * The message attribute for this packet
	 */
	public static final String MESSAGE = "message"; //$NON-NLS-1$
	/**
	 * The "request_seq" attribute
	 */
	public static final String REQUEST_SEQ = "request_seq"; //$NON-NLS-1$
	/**
	 * The running attribute for this packet
	 */
	public static final String RUNNING = "running"; //$NON-NLS-1$
	/**
	 * The "seq" attribute
	 */
	public static final String SEQ = "seq"; //$NON-NLS-1$
	/**
	 * The success attribute for this packet
	 */
	public static final String SUCCESS = "success"; //$NON-NLS-1$
	/**
	 * The "type" attribute
	 */
	public static final String TYPE = "type"; //$NON-NLS-1$
}
