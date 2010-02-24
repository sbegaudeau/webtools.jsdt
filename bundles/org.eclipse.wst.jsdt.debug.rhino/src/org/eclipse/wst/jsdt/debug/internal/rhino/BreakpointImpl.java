/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.rhino;

import java.util.HashMap;

import org.eclipse.core.runtime.Assert;
import org.eclipse.wst.jsdt.debug.core.jsdi.json.JSONConstants;


/**
 * Implementation of a breakpoint wrt Rhino debugging
 * 
 * @since 1.0
 */
public class BreakpointImpl {

	private final Long breakpointId;
	private final ScriptImpl script;
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
	public BreakpointImpl(Long breakpointId, ScriptImpl script, Integer lineNumber, String functionName, String condition, Long threadId) {
		Assert.isNotNull(script);
		this.breakpointId = breakpointId;
		this.script = script;
		this.lineNumber = lineNumber;
		this.functionName = functionName;
		this.condition = condition;
		this.threadId = threadId;
	}

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

	public Long getId() {
		return breakpointId;
	}

	public ScriptImpl getScript() {
		return script;
	}

	public boolean matches(String functionName, Integer lineNumber, DebugFrameImpl frame) {
		if (this.lineNumber == null) {
			if (functionName == null)
				return lineNumber.intValue() == 1 && this.functionName == null && checkThread(frame) && checkCondition(frame);

			return functionName.equals(this.functionName) && checkThread(frame) && checkCondition(frame);
		}
		return this.lineNumber.equals(lineNumber) && checkThread(frame) && checkCondition(frame);
	}

	private boolean checkThread(DebugFrameImpl frame) {
		if (threadId == null)
			return true;

		return frame.getThreadId().equals(threadId);
	}

	private boolean checkCondition(DebugFrameImpl frame) {
		if (condition == null)
			return true;

		return frame.evaluateCondition(condition);
	}

}
