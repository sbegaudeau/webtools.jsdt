/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.rhino.bundles;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.rhino.bundles.JSBundle;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSBundleException;
import org.mozilla.javascript.Scriptable;

public class JSBundleContext {

	private final JSBundle jsBundle;
	private final JSFrameworkImpl framework;

	public JSBundleContext(JSBundle jsBundle, JSFrameworkImpl framework) {
		this.jsBundle = jsBundle;
		this.framework = framework;
	}

	public JSBundle getBundle() {
		return jsBundle;
	}

	public JSBundle[] getBundles() {
		return framework.getBundles();
	}

	public String getProperty(String name) {
		return framework.getProperty(name);
	}

	public JSBundle installBundle(String location) throws JSBundleException {
		return framework.installBundle(location);
	}

	public JSBundle installBundle(String location, Scriptable object) throws JSBundleException {
		Map headers = new HashMap();
		Object[] ids = object.getIds();
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] instanceof String) {
				String key = (String) ids[i];
				Object value = object.get(key, object);
				if (value instanceof String)
					headers.put(key, value);
			}
		}
		return framework.installBundle(location, headers);
	}
}
