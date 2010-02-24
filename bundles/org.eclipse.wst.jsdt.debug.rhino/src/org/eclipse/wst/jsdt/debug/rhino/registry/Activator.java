/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.rhino.registry;

import java.net.URL;

import org.eclipse.wst.jsdt.debug.rhino.bundles.JSBundle;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSConstants;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSFramework;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class Activator implements BundleActivator, ServiceTrackerCustomizer {
	public static final String ID = "org.eclipse.e4.internal.javascript.registry";
	private ServiceTracker packageAdminTracker;
	private static PackageAdmin packageAdmin;
	
	private ServiceTracker jsFrameworkTracker;
	private static JSFramework jsFramework;
	
	private BundleContext context;

	public void start(BundleContext context) throws Exception {
		this.context = context;
		packageAdminTracker = new ServiceTracker(context, PackageAdmin.class.getName(), this);
		jsFrameworkTracker = new ServiceTracker(context, JSFramework.class.getName(), this);
		packageAdminTracker.open();
		jsFrameworkTracker.open();
	}

	public void stop(BundleContext context) throws Exception {
		jsFrameworkTracker.close();
		packageAdminTracker.close();
		jsFrameworkTracker = null;
		packageAdminTracker = null;
		this.context = null;
	}

	public static synchronized Bundle getBundle(String symbolicName) {
		if (packageAdmin == null)
			throw new IllegalStateException("Not started"); //$NON-NLS-1$

		Bundle[] bundles = packageAdmin.getBundles(symbolicName, null);
		if (bundles == null)
			return null;
		//Return the first bundle that is not installed or uninstalled
		for (int i = 0; i < bundles.length; i++) {
			if ((bundles[i].getState() & (Bundle.INSTALLED | Bundle.UNINSTALLED)) == 0) {
				return bundles[i];
			}
		}
		return null;
	}

	public Object addingService(ServiceReference reference) {
		Object service = context.getService(reference);
		synchronized (Activator.class) {
			if (service instanceof PackageAdmin && packageAdmin == null)
				packageAdmin = (PackageAdmin) service;				
			else if (service instanceof JSFramework && jsFramework == null)
				jsFramework = (JSFramework)service;
		}
		return service;
	}

	public void modifiedService(ServiceReference reference, Object service) {
	}

	public void removedService(ServiceReference reference, Object service) {
		synchronized (Activator.class) {
			if (service == packageAdmin)
				packageAdmin = null;
			else if (service == jsFramework)
				jsFramework = null;
		}
		context.ungetService(reference);
	}

	public static synchronized JSBundle getJSBundle(Bundle bundle) {
		if (jsFramework == null)
			throw new IllegalStateException("Not started"); //$NON-NLS-1$
		
		String jsBundleHeader = (String) bundle.getHeaders().get(JSConstants.JAVASCRIPT_BUNDLE);
		if (jsBundleHeader == null)
			return null;

		URL jsBundleEntry = bundle.getEntry(jsBundleHeader.trim());
		if (jsBundleEntry == null)
			return null;
		
		String location = jsBundleEntry.toString();
		
		JSBundle[] jsBundles = jsFramework.getBundles();
		for (int i = 0; i < jsBundles.length; i++) {
			if (location.equals(jsBundles[i].getLocation()))
					return jsBundles[i];
		}
		
		return null;
	}
}
