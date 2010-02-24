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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import org.eclipse.wst.jsdt.debug.core.jsdi.json.JSONConstants;
import org.eclipse.wst.jsdt.debug.core.jsdi.json.JSONUtil;
import org.mozilla.javascript.debug.DebuggableScript;

/**
 * Rhino script implementation
 * 
 * @since 1.0
 */
public class ScriptImpl {

	private static final String TOP_NAME = ""; //$NON-NLS-1$

	/**
	 * struct for a script handle
	 */
	static class ScriptRecord {
		String name = null;
		int firstLine = 1;
	}

	private final Long scriptId;
	private final String location;
	private final Map sourceProperties;
	private final String source;
	private final Boolean generated;
	private final Collection functionNames = new HashSet();
	private final Collection lineNumbers = new HashSet();
	private final Map scriptRecords = new HashMap();

	private Collection breakpoints = new ArrayList();

	/**
	 * Constructor
	 * 
	 * @param scriptId
	 * @param debuggableScript
	 * @param source
	 */
	public ScriptImpl(Long scriptId, DebuggableScript debuggableScript, String source) {
		this.scriptId = scriptId;

		String sourceName = debuggableScript.getSourceName();
		this.sourceProperties = parseSourceProperties(sourceName);
		this.location = (sourceProperties == null) ? sourceName : (String) sourceProperties.get(JSONConstants.NAME);

		this.generated = Boolean.valueOf(debuggableScript.isGeneratedScript());
		this.source = source;

		int[] scriptLineNumbers = debuggableScript.getLineNumbers();
		for (int i = 0; i < scriptLineNumbers.length; i++) {
			lineNumbers.add(new Integer(scriptLineNumbers[i] + 1));
		}
		scriptRecords.put(debuggableScript, new ScriptRecord());

		expandFunctions(TOP_NAME, debuggableScript);
	}

	private static Map parseSourceProperties(String sourceName) {
		if (sourceName != null && sourceName.charAt(0) == '{') {
			try {
				Object json = JSONUtil.read(sourceName);
				if (json instanceof Map) {
					return (Map) json;
				}
			} catch (RuntimeException e) {
				// ignore
			}
		}
		return null;
	}

	/**
	 * Expands the function name
	 * 
	 * @param prefix
	 * @param debuggableScript
	 */
	private void expandFunctions(String prefix, DebuggableScript debuggableScript) {
		int functionCount = debuggableScript.getFunctionCount();
		for (int i = 0; i < functionCount; i++) {
			DebuggableScript functionScript = debuggableScript.getFunction(i);

			ScriptRecord record = new ScriptRecord();
			scriptRecords.put(functionScript, record);

			String functionName = functionScript.getFunctionName();
			if ((functionName == null) || functionName.equals(TOP_NAME)) {
				functionName = "%" + i; //$NON-NLS-1$
			}
			record.name = prefix + functionName;
			functionNames.add(record.name);

			int[] scriptLineNumbers = functionScript.getLineNumbers();
			if (scriptLineNumbers != null && scriptLineNumbers.length != 0) {
				record.firstLine = scriptLineNumbers[0] + 1;
				for (int j = 0; j < scriptLineNumbers.length; j++) {
					int currentLine = scriptLineNumbers[j] + 1;
					if (currentLine < record.firstLine) {
						record.firstLine = currentLine;
					}
					lineNumbers.add(new Integer(currentLine));
				}
			}
			expandFunctions(record.name + ".", functionScript); //$NON-NLS-1$
		}
	}

	/**
	 * @return a new JSON map
	 */
	public Object toJSON() {
		HashMap result = new HashMap();
		result.put(JSONConstants.SCRIPT_ID, scriptId);
		result.put(JSONConstants.LOCATION, location);
		result.put(JSONConstants.PROPERTIES, sourceProperties);
		result.put(JSONConstants.SOURCE, source);
		result.put(JSONConstants.GENERATED, generated);
		result.put(JSONConstants.LINES, new TreeSet(lineNumbers));
		result.put(JSONConstants.FUNCTIONS, new TreeSet(functionNames));
		return result;
	}

	/**
	 * @return the id for this script object
	 */
	public Long getId() {
		return scriptId;
	}

	/**
	 * Returns the complete collection of breakpoints that match the given attributes or an empty collection, never <code>null</code>
	 * 
	 * @param functionName
	 * @param lineNumber
	 * @param frame
	 * @return all breakpoints that match the given attributes or an empty collection
	 */
	public synchronized Collection getBreakpoints(String functionName, Integer lineNumber, DebugFrameImpl frame) {
		ArrayList result = new ArrayList();
		for (Iterator iterator = breakpoints.iterator(); iterator.hasNext();) {
			BreakpointImpl breakpoint = (BreakpointImpl) iterator.next();
			if (breakpoint.matches(functionName, lineNumber, frame)) {
				result.add(breakpoint.getId());
			}
		}
		return result;
	}

	/**
	 * Returns if the line number or the function is valid wrt this script
	 * 
	 * @param lineNumber
	 * @param functionName
	 * @return true if the line number or function name is valid wrt this script
	 */
	public boolean isValid(Integer lineNumber, String functionName) {
		if (lineNumber != null) {
			return lineNumbers.contains(lineNumber);
		}
		if (functionName != null) {
			return functionNames.contains(functionName);
		}
		return functionName == null;
	}

	/**
	 * Adds a breakpoint to this script
	 * 
	 * @param breakpoint
	 */
	public synchronized void addBreakpoint(BreakpointImpl breakpoint) {
		breakpoints.add(breakpoint);
	}

	/**
	 * Removes a breakpoint from this script
	 * 
	 * @param breakpoint
	 */
	public synchronized void removeBreakpoint(BreakpointImpl breakpoint) {
		breakpoints.remove(breakpoint);
	}

	/**
	 * @return the string location of this script
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Returns the {@link ScriptRecord} for the debuggable script or <code>null</code>
	 * 
	 * @param debuggableScript
	 * @return the {@link ScriptRecord} for the given {@link DebuggableScript} or <code>null</code>
	 */
	public ScriptRecord getRecord(DebuggableScript debuggableScript) {
		return (ScriptRecord) scriptRecords.get(debuggableScript);
	}
}
