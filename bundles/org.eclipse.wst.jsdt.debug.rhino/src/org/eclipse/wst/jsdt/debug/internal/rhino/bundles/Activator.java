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
package org.eclipse.wst.jsdt.debug.internal.rhino.bundles;

import org.eclipse.wst.jsdt.debug.internal.rhino.RhinoDebugger;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSBundle;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSFramework;
import org.mozilla.javascript.ContextFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class Activator implements BundleActivator, ServiceTrackerCustomizer {

	private static PackageAdmin packageAdmin;
	private volatile static JSBundleTracker jsBundleTracker;
	private volatile static Bundle thisBundle;

	private ServiceTracker packageAdminTracker;
	private BundleContext context;
	private ServiceRegistration registration;
	private JSFrameworkImpl framework;
	private ServiceTracker debuggerTracker;

	public void start(BundleContext bundleContext) throws Exception {
		this.context = bundleContext;
		thisBundle = context.getBundle();
		
		packageAdminTracker = new ServiceTracker(context, PackageAdmin.class.getName(), this);
		packageAdminTracker.open();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		packageAdminTracker.close();
		packageAdminTracker = null;
		if (debuggerTracker != null) {
			debuggerTracker.close();
			debuggerTracker = null;
		}
		thisBundle = null;
		this.context = null;
	}

	public static synchronized JSBundle getJSBundle(Bundle bundle) {
		if (jsBundleTracker == null)
			throw new IllegalStateException("Not started"); //$NON-NLS-1$

		return jsBundleTracker.getJSBundle(bundle);
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

	public static synchronized Bundle getBundle(Class clazz) {
		if (packageAdmin == null)
			throw new IllegalStateException("Not started"); //$NON-NLS-1$

		return packageAdmin.getBundle(clazz);
	}

	public static Bundle[] getFragments(Bundle bundle) {
		if (packageAdmin == null)
			throw new IllegalStateException("Not started"); //$NON-NLS-1$

		return packageAdmin.getFragments(bundle);
	}

	public static Bundle getRhinoBundle() {
		Bundle bundle = getBundle(org.mozilla.javascript.Context.class);
		if (bundle != null)
			return bundle;

		if (thisBundle == null)
			throw new IllegalStateException("Not started"); //$NON-NLS-1$

		ExportedPackage[] exportedPackages = packageAdmin.getExportedPackages("org.apache.jasper.servlet"); //$NON-NLS-1$
		for (int i = 0; i < exportedPackages.length; i++) {
			Bundle[] importingBundles = exportedPackages[i].getImportingBundles();
			for (int j = 0; j < importingBundles.length; j++) {
				if (thisBundle.equals(importingBundles[j]))
					return exportedPackages[i].getExportingBundle();
			}
		}
		return null;
	}

	public Object addingService(ServiceReference reference) {
		Object service = context.getService(reference);
		synchronized (Activator.class) {
			if (service instanceof PackageAdmin)
				packageAdmin = (PackageAdmin) service;
		}
		
		if (packageAdmin != null) {
			ContextFactory contextFactory = new ContextFactory();
			String rhinoDebug = context.getProperty("rhino.debug"); 
			if ( rhinoDebug != null) {
				RhinoDebugger debugger = new RhinoDebugger(rhinoDebug);
				debugger.start();
				contextFactory.addListener(debugger);
			}
			
			framework = new JSFrameworkImpl(contextFactory);
			jsBundleTracker = new JSBundleTracker(context, framework);
			jsBundleTracker.open();
			registration = context.registerService(JSFramework.class.getName(), framework, null);
		}
		return service;
	}

	public void modifiedService(ServiceReference reference, Object service) {
		//do nothing
	}

	public void removedService(ServiceReference reference, Object service) {
		if (service instanceof PackageAdmin) {
			registration.unregister();
			jsBundleTracker.close();
			framework.shutdown();
			synchronized (Activator.class) {
				context.ungetService(reference);
				packageAdmin = null;
			}
		}
	}
}
