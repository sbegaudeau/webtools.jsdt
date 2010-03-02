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
package org.eclipse.wst.jsdt.debug.core.model;

import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.DebugException;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptFunctionBreakpoint;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptLineBreakpoint;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptLoadBreakpoint;
import org.eclipse.wst.jsdt.debug.internal.core.breakpoints.JavaScriptFunctionBreakpoint;
import org.eclipse.wst.jsdt.debug.internal.core.breakpoints.JavaScriptLineBreakpoint;
import org.eclipse.wst.jsdt.debug.internal.core.breakpoints.JavaScriptLoadBreakpoint;

/**
 * The JavaScript debug model
 * 
 * @since 1.0
 */
public class JavaScriptDebugModel {

	/**
	 * Debug model identifier.<br>
	 * <br>
	 * Value is: <code>org.eclipse.wst.jsdt.debug.model</code>
	 */
	public static final String MODEL_ID = "org.eclipse.wst.jsdt.debug.model"; //$NON-NLS-1$
	
	/**
	 * Creates a new {@link IJavaScriptLineBreakpoint}
	 * 
	 * @param resource the resource to create the breakpoint on
	 * @param linenumber the line number to place the breakpoint on
	 * @param charstart the char start or -1
	 * @param charend the char end or -1
	 * @param attributes the optional map of attributes or <code>null</code>
	 * @param register if the breakpoint should be immediately registered or not
	 * 
	 * @return a new {@link IJavaScriptLineBreakpoint}
	 * @throws DebugException if breakpoint creation failed
	 */
	public static IJavaScriptLineBreakpoint createLineBreakpoint(final IResource resource, final int linenumber, final int charstart, final int charend, final Map attributes, final boolean register) throws DebugException {
		return new JavaScriptLineBreakpoint(resource, linenumber, charstart, charend, attributes, register);
	}
	
	/**
	 * Creates a new {@link IJavaScriptLoadBreakpoint}
	 * 
	 * @param resource the resource to associate the breakpoint with, or <code>null</code>
	 * @param charstart the char start or -1
	 * @param charend the char end or -1
	 * @param attributes the optional map of attributes or <code>null</code>
	 * @param register if the breakpoint should be immediately registered or not
	 * 
	 * @return a new {@link IJavaScriptLoadBreakpoint}
	 * @throws DebugException if breakpoint creation fails
	 */
	public static IJavaScriptLoadBreakpoint createScriptLoadBreakpoint(final IResource resource, final int charstart, final int charend, final Map attributes, final boolean register) throws DebugException {
		return new JavaScriptLoadBreakpoint(resource, charstart, charend, attributes, register);
	}
	
	/**
	 * Creates a new {@link IJavaScriptFunctionBreakpoint}
	 * 
	 * @param resource the resource to create the breakpoint on
	 * @param name the name of the function
	 * @param signature the signature of the function
	 * @param charstart the char start of the function or -1
	 * @param charend the char end of the function or -1
	 * @param attributes an optional mapping of attributes or <code>null</code>
	 * @param register if the breakpoint should immediately be registered or not
	 * 
	 * @return a new {@link IJavaScriptFunctionBreakpoint}
	 * @throws DebugException if breakpoint creation fails
	 */
	public static IJavaScriptFunctionBreakpoint createFunctionBreakpoint(final IResource resource, final String name, final String signature, final int charstart, final int charend, final Map attributes, final boolean register) throws DebugException {
		return new JavaScriptFunctionBreakpoint(resource, name, signature, charstart, charend, attributes, register);
	}
}
