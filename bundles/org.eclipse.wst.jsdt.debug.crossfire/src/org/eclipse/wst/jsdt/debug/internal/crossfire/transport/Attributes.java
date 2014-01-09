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
	 * The "attributes" attribute
	 */
	public static final String ATTRIBUTES = "attributes"; //$NON-NLS-1$
	/**
	 * The "body" attribute
	 */
	public static final String BODY = "body"; //$NON-NLS-1$
	/**
	 * The "breakpoint" attribute
	 */
	public static final String BREAKPOINT = "breakpoint"; //$NON-NLS-1$
	/**
	 * The "breakpoints" attribute
	 */
	public static final String BREAKPOINTS = "breakpoints"; //$NON-NLS-1$
	/**
	 * The "callee attribute"
	 */
	public static final String CALLEE = "callee"; //$NON-NLS-1$
	/**
	 * The "callingFrame" attribute
	 */
	public static final String CALLING_FRAME = "callingFrame"; //$NON-NLS-1$
	/**
	 * The "category" attribute
	 */
	public static final String CATEGORY = "category"; //$NON-NLS-1$
	/**
	 * The code attribute for this packet
	 */
	public static final String CODE = "code"; //$NON-NLS-1$
	/**
	 * The "columnNumber" attribute
	 */
	public static final String COLUMN_NUMBER = "columnNumber"; //$NON-NLS-1$
	/**
	 * The "columnOffset" attribute
	 */
	public static final String COLUMN_OFFSET = "columnOffset"; //$NON-NLS-1$
	/**
	 * The "command" attribute
	 */
	public static final String COMMAND = "command"; //$NON-NLS-1$
	/**
	 * The "condition" argument
	 */
	public static final String CONDITION = "condition"; //$NON-NLS-1$
	/**
	 * the "constructor" attribute
	 */
	public static final String CONSTRUCTOR = "constructor"; //$NON-NLS-1$
	/**
	 * The "contextId" attribute
	 */
	public static final String CONTEXT_ID = "contextId"; //$NON-NLS-1$
	/**
	 * The "contexts" attribute
	 */
	public static final String CONTEXTS = "contexts"; //$NON-NLS-1$
	/**
	 * the "current" attribute
	 */
	public static final String CURRENT = "current"; //$NON-NLS-1$
	/**
	 * The "enabled" argument
	 */
	public static final String ENABLED = "enabled"; //$NON-NLS-1$
	/**
	 * The "error" attribute for an onError event
	 */
	public static final String ERROR = "error"; //$NON-NLS-1$
	/**
	 * The "errorMessage" attribute
	 */
	public static final String ERROR_MESSAGE = "errorMessage"; //$NON-NLS-1$
	/**
	 * The "executionContext" attribute
	 */
	public static final String EXECUTION_CONTEXT = "executionContext"; //$NON-NLS-1$
	/**
	 * The "expression" attribute
	 */
	public static final String EXPRESSION = "expression"; //$NON-NLS-1$
	/**
	 * The "fileName" attribute
	 */
	public static final String FILE_NAME = "fileName"; //$NON-NLS-1$
	/**
	 * The "flags" attributes
	 */
	public static final String FLAGS = "flags"; //$NON-NLS-1$
	/**
	 * The "frame" argument
	 */
	public static final String FRAME = "frame"; //$NON-NLS-1$
	/**
	 * The "frameIndex" argument
	 */
	public static final String FRAME_INDEX = "frameIndex"; //$NON-NLS-1$
	/**
	 * The "frames" attribute
	 */
	public static final String FRAMES = "frames"; //$NON-NLS-1$
	/**
	 * The "fromFrame" attribute
	 */
	public static final String FROM_FRAME = "fromFrame"; //$NON-NLS-1$
	/**
	 * The type "function"
	 */
	public static final String FUNCTION = "function"; //$NON-NLS-1$
	/**
	 * The "functionName" attribute
	 */
	public static final String FUNCTION_NAME = "functionName"; //$NON-NLS-1$
	/**
	 * The "handle" attribute
	 */
	public static final String HANDLE = "handle"; //$NON-NLS-1$
	/**
	 * The "handles" attribute
	 */
	public static final String HANDLES = "handles"; //$NON-NLS-1$
	/**
	 * The "handshake" attribute
	 */
	public static final String HANDSHAKE = "handshake"; //$NON-NLS-1$
	/**
	 * The "hitCount" attribute
	 */
	public static final String HIT_COUNT = "hitCount"; //$NON-NLS-1$
	/**
	 * The "includeScopes" attribute
	 */
	public static final String INCLUDE_SCOPES = "includeScopes"; //$NON-NLS-1$
	/**
	 * The "includeSource" attribute
	 */
	public static final String INCLUDE_SOURCE = "includeSource"; //$NON-NLS-1$
	/**
	 * The "index" attribute
	 */
	public static final String INDEX = "index"; //$NON-NLS-1$
	/**
	 * the "isDebugger" attribute
	 */
	public static final String IS_DEBUGGER = "isDebugger"; //$NON-NLS-1$
	/**
	 * the "isValid" attribute
	 */
	public static final String IS_VALID = "isValid"; //$NON-NLS-1$
	/**
	 * The "line" attribute
	 */
	public static final String LINE = "line"; //$NON-NLS-1$
	/**
	 * The "lineCount" attribute
	 */
	public static final String LINE_COUNT = "lineCount"; //$NON-NLS-1$
	/**
	 * The "lineNo" attribute
	 */
	public static final String LINE_NO = "lineNo"; //$NON-NLS-1$
	/**
	 * The "lineNumber" attribute
	 */
	public static final String LINE_NUMBER = "lineNumber"; //$NON-NLS-1$
	/**
	 * The "lineOffset" attribute
	 */
	public static final String LINE_OFFSET = "lineOffset"; //$NON-NLS-1$
	/**
	 * The "locals" attribute
	 */
	public static final String LOCALS = "locals"; //$NON-NLS-1$
	/**
	* The "location" attribute
	*/
	public static final String LOCATION = "location"; //$NON-NLS-1$
	/**
	 * The message attribute for this packet
	 */
	public static final String MESSAGE = "message"; //$NON-NLS-1$
	/**
	 * The "name" attribute
	 */
	public static final String NAME = "name"; //$NON-NLS-1$
	/**
	 * The type "proto"
	 */
	public static final String PROTO = "proto"; //$NON-NLS-1$
	/**
	 * The "ref" attribute / type
	 */
	public static final String REF = "ref"; //$NON-NLS-1$
	/**
	 * The "requestSeq" attribute
	 */
	public static final String REQUEST_SEQ = "requestSeq"; //$NON-NLS-1$
	/**
	 * The "result" attribute
	 */
	public static final String RESULT = "result"; //$NON-NLS-1$
	/**
	 * The running attribute for this packet
	 */
	public static final String RUNNING = "running"; //$NON-NLS-1$
	/**
	 * The "scope" attribute
	 */
	public static final String SCOPE = "scope"; //$NON-NLS-1$
	/**
	 * The "scopeIndexes" attribute
	 */
	public static final String SCOPE_INDEXES = "scopeIndexes"; //$NON-NLS-1$
	/**
	 * The "scopes" attribute
	 */
	public static final String SCOPES = "scopes"; //$NON-NLS-1$
	/**
	 * The "script" attribute
	 */
	public static final String SCRIPT = "script"; //$NON-NLS-1$
	/**
	 * The "scripts" attribute
	 */
	public static final String SCRIPTS = "scripts"; //$NON-NLS-1$
	/**
	 * The "seq" attribute
	 */
	public static final String SEQ = "seq"; //$NON-NLS-1$
	/**
	 * The "set" attribute
	 */
	public static final String SET = "set"; //$NON-NLS-1$
	/**
	 * The "source" attribute
	 */
	public static final String SOURCE = "source"; //$NON-NLS-1$
	/**
	 * The "sourceLength" attribute
	 */
	public static final String SOURCE_LENGTH = "sourceLength"; //$NON-NLS-1$
	/**
	 * The "sourceName" attribute
	 */
	public static final String SOURCE_NAME = "sourceName"; //$NON-NLS-1$
	/**
	 * The "stacktrace" attribute
	 */
	public static final String STACKTRACE = "stackTrace"; //$NON-NLS-1$
	/**
	 * The "status" attribute
	 */
	public static final String STATUS = "status"; //$NON-NLS-1$
	/**
	 * The "stepaction" attribute
	 */
	public static final String STEPACTION = "stepAction"; //$NON-NLS-1$
	/**
	 * The thisValue" attribute
	 */
	public static final String THIS_VALUE = "thisValue"; //$NON-NLS-1$
	/**
	 * The "tools" attribute
	 */
	public static final String TOOLS = "tools"; //$NON-NLS-1$
	/**
	 * The "type" attribute
	 */
	public static final String TYPE = "type"; //$NON-NLS-1$
	/**
	 * The "url" attribute
	 */
	public static final String URL = "url"; //$NON-NLS-1$
	/**
	 * The "urls" attribute
	 */
	public static final String URLS = "urls"; //$NON-NLS-1$
	/**
	 * The "value" attribute
	 */
	public static final String VALUE = "value"; //$NON-NLS-1$
	/**
	 * The "values" attribute
	 */
	public static final String VALUES = "values"; //$NON-NLS-1$
}
