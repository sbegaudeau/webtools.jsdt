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

import java.util.Optional;

import javax.servlet.ServletException;

import org.eclipse.wst.jsdt.bower.server.internal.servlets.PackagesServlet;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class HttpServiceTrackerCustomizer implements ServiceTrackerCustomizer<HttpService, HttpService> {

	/**
	 * The root of the HTTP service.
	 */
	private static final String PACKAGES = "/packages"; //$NON-NLS-1$

	/**
	 * The bundle context.
	 */
	private BundleContext context;

	/**
	 * The HTTP service.
	 */
	private Optional<HttpService> httpService = Optional.empty();

	/**
	 * The constructor.
	 *
	 * @param context
	 *            The bundle context
	 */
	public HttpServiceTrackerCustomizer(BundleContext context) {
		this.context = context;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#addingService(org.osgi.framework.ServiceReference)
	 */
	@Override
	public HttpService addingService(ServiceReference<HttpService> reference) {
		HttpService service = this.context.getService(reference);
		this.httpService = Optional.ofNullable(service);
		return service;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#modifiedService(org.osgi.framework.ServiceReference,
	 *      java.lang.Object)
	 */
	@Override
	public void modifiedService(ServiceReference<HttpService> reference, HttpService service) {
		if (this.httpService.isPresent()) {
			this.httpService.get().unregister(PACKAGES);
		}

		HttpService newHttpService = this.context.getService(reference);
		this.httpService = Optional.ofNullable(newHttpService);
		if (this.httpService.isPresent()) {
			try {
				this.httpService.get().registerServlet(PACKAGES, new PackagesServlet(), null, null);
			} catch (ServletException | NamespaceException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#removedService(org.osgi.framework.ServiceReference,
	 *      java.lang.Object)
	 */
	@Override
	public void removedService(ServiceReference<HttpService> reference, HttpService service) {
		if (this.httpService.isPresent()) {
			this.httpService.get().unregister(PACKAGES);
		}
	}

}
