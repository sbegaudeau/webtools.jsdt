/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.rhino.debugger;

import java.util.HashMap;

import org.eclipse.wst.jsdt.debug.rhino.transport.JSONConstants;


/**
 * Implementation of a breakpoint wrt Rhino debugging
 * 
 * @since 1.0
 */
public class Breakpoint {

	private final Long breakpointId;
	private final ScriptSource script;
	private final Integer lineNumber;
	private final Object functionName;
	private final String condition;
	private final Long threadId;

	/**
	 * Constructor
	 * 
	 * @param breakpointId the id for the breakpoint
	 * @param script the script the breakpoint applies to, <code>null</code> is not accepted
	 * @param lineNumber the line number for the breakpoint
	 * @param functionName the name of the function the breakpoint is set on, <code>null</code> is accepted
	 * @param condition the condition for the breakpoint, <code>null</code> is accepted
	 * @param threadId the id of the thread the breakpoint is for
	 */
	public Breakpoint(Long breakpointId, ScriptSource script, Integer lineNumber, String functionName, String condition, Long threadId) {
		if (script == null) {
			throw new IllegalArgumentException("The parent script cannot be null"); //$NON-NLS-1$
		}
		this.breakpointId = breakpointId;
		this.script = script;
		this.lineNumber = lineNumber;
		this.functionName = functionName;
		this.condition = condition;
		this.threadId = threadId;
	}

	/**
	 * Returns the breakpoint as a JSON entry
	 * @return
	 */
	public Object toJSON() {
		HashMap result = new HashMap();
		result.put(JSONConstants.BREAKPOINT_ID, breakpointId);
		result.put(JSONConstants.SCRIPT_ID, script.getId());
		if (lineNumber != null)
			result.put(JSONConstants.LINE, lineNumber);
		if (functionName != null)
			result.put(JSONConstants.FUNCTION, functionName);
		if (condition != null)
			result.put(JSONConstants.CONDITION, condition);
		if (threadId != null)
			result.put(JSONConstants.THREAD_ID, threadId);
		return result;
	}

	/**
	 * @return the id of this breakpoint
	 */
	public Long getId() {
		return breakpointId;
	}

	/**
	 * Deletes the breakpoint from the script it is associated with. Does
	 * not clear any of the handle infos so the deleted breakpoint can be 
	 * returned as an event if required.
	 */
	public void delete() {
		this.script.removeBreakpoint(this);
	}

	/**
	 * Returns if this breakpoint matches (cares about) the context that the given attributes are from
	 * 
	 * @param functionName the function name context - <code>null</code> is accepted
	 * @param lineNumber the line number - <code>null</code> is accepted
	 * @param frame the {@link StackFrame} context - <code>null</code> is not accepted
	 * @return <code>true</code> if this breakpoint cares, <code>false</code> otherwise
	 */
	public boolean matches(String functionName, Integer lineNumber, StackFrame frame) {
		if(frame == null) {
			throw new IllegalArgumentException("The stack frame context cannot be null"); //$NON-NLS-1$
		}
		if (this.lineNumber == null) {
			if (functionName == null) {
				return lineNumber.intValue() == 1 && this.functionName == null && checkThread(frame) && checkCondition(frame);
			}
			return functionName.equals(this.functionName) && checkThread(frame) && checkCondition(frame);
		}
		return this.lineNumber.equals(lineNumber) && checkThread(frame) && checkCondition(frame);
	}

	/**
	 * Returns if the thread id of the given {@link StackFrame} matches
	 * the id of the thread filter this breakpoint was created with
	 * 
	 * @param frame
	 * @return <code>true</code> if the thread ids match, <code>false</code> otherwise
	 */
	private boolean checkThread(StackFrame frame) {
		if (threadId == null) {
			return true;
		}
		return frame.getThreadId().equals(threadId);
	}

	/**
	 * evaluates the condition in the given {@link StackFrame} context
	 * @param frame
	 * @return <code>true</code> if the condition succeeds, <code>false</code> otherwise
	 */
	private boolean checkCondition(StackFrame frame) {
		if (condition == null) {
			return true;
		}
		return frame.evaluateCondition(condition);
	}
}
