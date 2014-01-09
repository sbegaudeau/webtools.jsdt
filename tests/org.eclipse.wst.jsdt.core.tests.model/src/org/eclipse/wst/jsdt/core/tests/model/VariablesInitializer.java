/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.model;

import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.jsdt.core.JsGlobalScopeVariableInitializer;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;

public class VariablesInitializer extends JsGlobalScopeVariableInitializer {

	public static ITestInitializer initializer;
	
	public static interface ITestInitializer {
		public void initialize(String variable) throws JavaScriptModelException;
	}
	
	public static void reset() {
		initializer = null;
		String[] varNames = JavaScriptCore.getIncludepathVariableNames();
		try {
			JavaScriptCore.setIncludepathVariables(varNames, new IPath[varNames.length], null);
		} catch (JavaScriptModelException e) {
			e.printStackTrace();
		}
	}

	public static void setInitializer(ITestInitializer initializer) {
		VariablesInitializer.initializer = initializer;
	}
	
	public void initialize(String variable) {
		if (initializer == null) return;
		try {
			initializer.initialize(variable);
		} catch (JavaScriptModelException e) {
			e.printStackTrace();
		}
	}
}
