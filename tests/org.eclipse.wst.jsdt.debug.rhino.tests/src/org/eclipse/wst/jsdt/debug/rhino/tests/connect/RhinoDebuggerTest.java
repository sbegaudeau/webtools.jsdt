/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.rhino.tests.connect;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.internal.rhino.bundles.RhinoClassLoader;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSBundle;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSBundleException;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSConstants;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.osgi.framework.Constants;

public class RhinoDebuggerTest extends RequestTest {

	public static String TEST = "test";

	public void testDebugSanityInstallResolveBundle() throws JSBundleException {
		Map headers = new HashMap();
		headers.put(Constants.BUNDLE_SYMBOLICNAME, "test");
		headers.put(Constants.BUNDLE_VERSION, "1.8");
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

		headers.put(JSConstants.BUNDLE_SCRIPT, script);

		framework.installBundle("testloc", headers, new RhinoClassLoader(Activator.getBundleContext().getBundle()));
		assertEquals(1, framework.getBundles().length);
		JSBundle jsBundle = framework.getBundles()[0];

		assertEquals(JSBundle.INSTALLED, jsBundle.getState());
		framework.resolve();
		assertEquals(JSBundle.RESOLVED, jsBundle.getState());
		Scriptable scope = jsBundle.getScope();
		Object obj = scope.get("test", scope);
		assertNotSame(Scriptable.NOT_FOUND, obj);
		assertTrue(obj instanceof Callable);
		Callable test = (Callable) obj;
		contextFactory.enterContext();
		try {
			Object result = test.call(Context.getCurrentContext(), scope, scope, new Object[0]);
			Object javaResult = Context.jsToJava(result, String.class);
			assertEquals("test", javaResult);
		} finally {
			Context.exit();
		}
	}
}
