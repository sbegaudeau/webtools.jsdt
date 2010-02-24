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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.rhino.bundles.JSBundle;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSBundleException;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSConstants;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;

public class JSBundleTracker extends BundleTracker {
	private JSFrameworkImpl framework;
	private Map jsBundleMap = new HashMap();

	public JSBundleTracker(BundleContext context, JSFrameworkImpl framework) {
		super(context, Bundle.RESOLVED | Bundle.ACTIVE | Bundle.STARTING | Bundle.STOPPING, null);
		this.framework = framework;
	}

	public JSBundle getJSBundle(Bundle bundle) {
		return (JSBundle) jsBundleMap.get(bundle);
	}

	public Object addingBundle(Bundle bundle, BundleEvent event) {
		String jsBundleHeader = (String) bundle.getHeaders().get(JSConstants.JAVASCRIPT_BUNDLE);
		if (jsBundleHeader == null)
			return null;

		URL jsBundleEntry = bundle.getEntry(jsBundleHeader.trim());
		if (jsBundleEntry == null)
			return null;

		try {
			JSBundle jsBundle = framework.installBundle(jsBundleEntry.toString(), bundle);
			jsBundleMap.put(bundle, jsBundle);
			framework.resolve();
			if (event == null && bundle.getState() == Bundle.ACTIVE)
				jsBundle.start();
			return jsBundle;
		} catch (JSBundleException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {
		if (event == null)
			return;

		JSBundle jsBundle = (JSBundle) object;
		switch (event.getType()) {
			case BundleEvent.STARTED :
				jsBundle.start();
				break;
			case BundleEvent.STOPPING :
				jsBundle.stop();
		}
	}

	public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
		JSBundle jsBundle = (JSBundle) object;
		jsBundle.uninstall();
		jsBundleMap.remove(bundle);
		framework.refresh();
	}

}
