/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.rhino.tests;

import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class RhinoDebuggerTest extends RequestTest {

	public static String TEST = "test";

	public void testDebugSanityInstallResolveBundle(){
		String script = "";
		script += "\r\n";
		script += "var test = function() {";
		script += "\r\n";
		script += "var clazz = Packages." + this.getClass().getName() + ";";
		script += "return clazz.TEST;";
		script += "};\r\n";
		script += "var v = test();\r\n";
		script += "\r\n";
		script += "\r\n";
		script += "var y = test();\r\n";
		script += "\r\n";
		script += "\r\n";
		script += "var z = test();\r\n";
		script += "\r\n";
		script += "\r\n";
		
		Scriptable scope = null;
		Context context = contextFactory.enterContext();
		ClassLoader current = context.getApplicationClassLoader();		
		context.setApplicationClassLoader(this.getClass().getClassLoader());
		try {
			scope = context.initStandardObjects();
			context.evaluateString(scope, script, "script", 0, null);
		} finally {
			context.setApplicationClassLoader(current);
			Context.exit();
		}
		Object obj = scope.get("test", scope);
		assertNotSame(Scriptable.NOT_FOUND, obj);
		assertTrue(obj instanceof Callable);
		Callable test = (Callable) obj;
		
		context = contextFactory.enterContext();
		current = context.getApplicationClassLoader();		
		context.setApplicationClassLoader(this.getClass().getClassLoader());
		try {
			Object result = test.call(Context.getCurrentContext(), scope, scope, new Object[0]);
			Object javaResult = Context.jsToJava(result, String.class);
			assertEquals("test", javaResult);
		} finally {
			context.setApplicationClassLoader(current);
			Context.exit();
		}
	}
}
