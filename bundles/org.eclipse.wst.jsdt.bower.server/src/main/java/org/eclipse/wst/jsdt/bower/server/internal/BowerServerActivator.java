/*******************************************************************************
 * Copyright (c) 2014 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.bower.server.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The activator in charge of the registration of bower related servlets to Jetty.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class BowerServerActivator implements BundleActivator {

	/**
	 * The HttpService tracker.
	 */
	private ServiceTracker<HttpService, HttpService> httpServiceTracker;

	/**
	 * {@inheritDoc}
	 *
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		HttpServiceTrackerCustomizer httpServiceTrackerCustomizer = new HttpServiceTrackerCustomizer(context);
		this.httpServiceTracker = new ServiceTracker<>(context, HttpService.class,
				httpServiceTrackerCustomizer);
		this.httpServiceTracker.open();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		this.httpServiceTracker.close();
	}

}
