/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.rhino;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.json.JSONConstants;
import org.eclipse.wst.jsdt.debug.internal.core.jsdi.connect.EventPacket;
import org.mozilla.javascript.debug.DebugFrame;

/**
 * Rhino implementation of {@link ContextData}
 * 
 * @since 1.0
 */
public class ContextData {
	private static final int CONTEXT_RUNNING = 0;
	private static final int CONTEXT_SUSPENDED = 1;

	private static final int STEP_CONTINUE = 0;
	private static final int STEP_IN = 1;
	private static final int STEP_NEXT = 2;
	private static final int STEP_OUT = 4;

	private final Long threadId;
	private final Long contextId;
	private final RhinoDebugger debugger;

	private final LinkedList frames = new LinkedList();

	private int contextState = CONTEXT_RUNNING;
	private int stepState = STEP_CONTINUE;
	private DebugFrameImpl stepFrame;

	/**
	 * Constructor
	 * 
	 * @param threadId
	 * @param contextId
	 * @param debugger
	 */
	public ContextData(Long threadId, Long contextId, RhinoDebugger debugger) {
		this.threadId = threadId;
		this.contextId = contextId;
		this.debugger = debugger;
	}

	/**
	 * Returns the unique id of this context data
	 * 
	 * @return the unique id
	 */
	public Long getId() {
		return contextId;
	}

	/**
	 * Returns the live list of {@link DebugFrame}s from this context
	 * 
	 * @return the live list of {@link DebugFrame}s
	 */
	public synchronized List getFrameIds() {
		List result = new ArrayList();
		for (Iterator iterator = frames.iterator(); iterator.hasNext();) {
			result.add(((DebugFrameImpl) iterator.next()).getId());
		}
		return result;
	}

	/**
	 * Returns the {@link DebugFrame} with the given id or <code>null</code> if no such {@link DebugFrame} exists
	 * 
	 * @param frameId
	 * @return the {@link DebugFrame} with the given id or <code>null</code>
	 */
	public synchronized DebugFrameImpl getFrame(Long frameId) {
		DebugFrameImpl frame = null;
		for (Iterator iterator = frames.iterator(); iterator.hasNext();) {
			frame = (DebugFrameImpl) iterator.next();
			if (frame.getId().equals(frameId)) {
				return frame;
			}
		}
		return null;
	}

	/**
	 * Adds the given frame to the top of the frame stack and sends out a break event as needed
	 * 
	 * @param frame
	 * @param script
	 * @param lineNumber
	 * @param functionName
	 */
	public synchronized void pushFrame(DebugFrameImpl frame, ScriptImpl script, Integer lineNumber, String functionName) {
		frames.addFirst(frame);
		Collection breakpoints = script.getBreakpoints(functionName, lineNumber, frame);
		if (functionName == null) {
			functionName = "(toplevel)"; //$NON-NLS-1$
		}
		boolean isStepBreak = stepBreak(STEP_IN);
		if (isStepBreak || !breakpoints.isEmpty()) {
			if (sendBreakEvent(script, frame.getLineNumber(), functionName, breakpoints, isStepBreak, false)) {
				suspendState();
			}
		}
	}

	/**
	 * Returns if the step operation should cause a break
	 * 
	 * @param step
	 * @return true if the operation should break false otherwise
	 */
	private boolean stepBreak(int step) {
		return ((0 != (step & stepState)) && (stepFrame == null || stepFrame == frames.getFirst()));
	}

	/**
	 * Suspends the state via {@link #wait()}
	 */
	private void suspendState() {
		contextState = CONTEXT_SUSPENDED;
		while (contextState == CONTEXT_SUSPENDED) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO log this
				e.printStackTrace();
			}
		}
	}

	/**
	 * Removes the first {@link DebugFrame} from the current stack
	 * 
	 * @param byThrow
	 * @param resultOrException
	 */
	public synchronized void popFrame(boolean byThrow, Object resultOrException) {
		frames.removeFirst();
		if (frames.isEmpty()) {
			return;
		}
		boolean isStepBreak = stepBreak(STEP_OUT);
		if (isStepBreak) {
			DebugFrameImpl frame = (DebugFrameImpl) frames.getFirst();
			if (sendBreakEvent(frame.getScript(), frame.getLineNumber(), null, Collections.EMPTY_LIST, isStepBreak, false)) {
				suspendState();
			}
		}
	}

	/**
	 * Resume the state with the given step type
	 * 
	 * @param stepType
	 */
	public synchronized void resume(String stepType) {
		if (stepType == null) {
			stepState = STEP_CONTINUE;
			stepFrame = null;
		} else if (stepType.equals(JSONConstants.STEP_IN)) {
			stepState = STEP_IN;
			stepFrame = null;
		} else if (stepType.equals(JSONConstants.STEP_NEXT)) {
			stepState = STEP_NEXT;
			stepFrame = (DebugFrameImpl) frames.getFirst();
		} else if (stepType.equals(JSONConstants.STEP_OUT)) {
			if (frames.size() > 1) {
				stepState = STEP_OUT;
				stepFrame = (DebugFrameImpl) frames.get(1);
			} else {
				stepState = STEP_CONTINUE;
				stepFrame = null;
			}
		} else if (stepType.equals(JSONConstants.STEP_ANY)) {
			stepState = STEP_IN | STEP_OUT | STEP_NEXT;
			stepFrame = null;
		} else {
			// TODO NLS this
			throw new IllegalStateException("bad stepType: " + stepType); //$NON-NLS-1$
		}
		contextState = CONTEXT_RUNNING;
		notifyAll();
	}

	/**
	 * Set the step state to the suspend equivalent
	 */
	public synchronized void suspend() {
		stepState = STEP_IN | STEP_NEXT | STEP_OUT;
		stepFrame = null;
	}

	/**
	 * Handles a <code>debugger;</code> statement
	 * 
	 * @param script
	 * @param lineNumber
	 */
	public synchronized void debuggerStatement(ScriptImpl script, Integer lineNumber) {
		DebugFrameImpl frame = (DebugFrameImpl) frames.getFirst();
		Collection breakpoints = script.getBreakpoints(null, lineNumber, frame);
		boolean isStepBreak = stepBreak(STEP_IN | STEP_NEXT);
		if (sendBreakEvent(script, lineNumber, null, breakpoints, isStepBreak, true)) {
			suspendState();
		}
	}

	/**
	 * Handles a line change
	 * 
	 * @param script
	 * @param lineNumber
	 */
	public synchronized void lineChange(ScriptImpl script, Integer lineNumber) {
		DebugFrameImpl frame = (DebugFrameImpl) frames.getFirst();
		Collection breakpoints = script.getBreakpoints(null, lineNumber, frame);
		boolean isStepBreak = stepBreak(STEP_IN | STEP_NEXT);
		if (isStepBreak || !breakpoints.isEmpty()) {
			if (sendBreakEvent(script, lineNumber, null, breakpoints, isStepBreak, false)) {
				suspendState();
			}
		}
	}

	/**
	 * Handles forwarding an exception event
	 * 
	 * @param ex
	 */
	public synchronized void exceptionThrown(Throwable ex) {
		DebugFrameImpl frame = (DebugFrameImpl) frames.getFirst();
		if (sendExceptionEvent(frame.getScript(), frame.getLineNumber(), ex)) {
			suspendState();
		}
	}

	/**
	 * Sends a JSON message for an exception that has occurred
	 * 
	 * @param script
	 * @param lineNumber
	 * @param ex
	 * @return true if the message was sent successfully, false otherwise
	 */
	private boolean sendExceptionEvent(ScriptImpl script, Integer lineNumber, Throwable ex) {
		EventPacket exceptionEvent = new EventPacket(JSONConstants.EXCEPTION);
		Map body = exceptionEvent.getBody();
		body.put(JSONConstants.CONTEXT_ID, contextId);
		body.put(JSONConstants.THREAD_ID, threadId);
		body.put(JSONConstants.SCRIPT_ID, script.getId());
		body.put(JSONConstants.LINE_NUMBER, lineNumber);
		body.put(JSONConstants.MESSAGE, ex.getMessage());
		return debugger.sendEvent(exceptionEvent);
	}

	/**
	 * Sends a JSON message for a break event
	 * 
	 * @param script
	 * @param lineNumber
	 * @param functionName
	 * @param breakpoints
	 * @param isStepBreak
	 * @param isDebuggerStatement
	 * @return true if the message was sent successfully, false otherwise
	 */
	private boolean sendBreakEvent(ScriptImpl script, Integer lineNumber, String functionName, Collection breakpoints, boolean isStepBreak, boolean isDebuggerStatement) {
		EventPacket breakEvent = new EventPacket(JSONConstants.BREAK);
		Map body = breakEvent.getBody();
		body.put(JSONConstants.THREAD_ID, threadId);
		body.put(JSONConstants.CONTEXT_ID, contextId);
		body.put(JSONConstants.SCRIPT_ID, script.getId());
		if (functionName != null) {
			body.put(JSONConstants.FUNCTION_NAME, functionName);
		}
		body.put(JSONConstants.LINE_NUMBER, lineNumber);
		if (!breakpoints.isEmpty()) {
			body.put(JSONConstants.BREAKPOINTS, breakpoints);
		}

		if (isStepBreak) {
			String stepType;
			if (stepState == STEP_IN) {
				stepType = JSONConstants.STEP_IN;
			} else if (stepState == STEP_NEXT) {
				stepType = JSONConstants.STEP_NEXT;
			} else if (stepState == STEP_OUT) {
				stepType = JSONConstants.STEP_OUT;
			} else {
				stepType = JSONConstants.SUSPEND;
			}
			body.put(JSONConstants.STEP, stepType);
			stepState = 0;
		}

		body.put(JSONConstants.DEBUGGER_STATEMENT, Boolean.valueOf(isDebuggerStatement));
		return debugger.sendEvent(breakEvent);
	}

	/**
	 * Handles a script load event
	 * 
	 * @param script
	 */
	public synchronized void scriptLoaded(ScriptImpl script) {
		if (sendScriptEvent(script)) {
			suspendState();
		}
	}

	/**
	 * Send a JSON message for a script event
	 * 
	 * @param script
	 * @return
	 */
	private boolean sendScriptEvent(ScriptImpl script) {
		EventPacket scriptEvent = new EventPacket(JSONConstants.SCRIPT);
		Map body = scriptEvent.getBody();
		body.put(JSONConstants.THREAD_ID, threadId);
		body.put(JSONConstants.CONTEXT_ID, contextId);
		body.put(JSONConstants.SCRIPT_ID, script.getId());
		return debugger.sendEvent(scriptEvent);
	}

	/**
	 * Returns the string representation of the state
	 * 
	 * @return the state text
	 */
	public String getState() {
		return contextState == CONTEXT_RUNNING ? JSONConstants.RUNNING : JSONConstants.SUSPENDED;
	}

	/**
	 * Returns the underlying thread id
	 * 
	 * @return the underlying thread id
	 */
	public Long getThreadId() {
		return threadId;
	}
}
