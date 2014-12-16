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
package org.eclipse.wst.jsdt.bower.ide.ui.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.jsdt.nodejs.core.api.utils.ILogger;
import org.osgi.framework.BundleContext;

/**
 * The activator of the Bower IDE UI bundle.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class BowerUiPlugin extends AbstractUIPlugin {
	/**
	 * The sole instance of the activator.
	 */
	private static BowerUiPlugin instance;

	/**
	 * Returns the sole instance of the Bower IDE UI activator.
	 *
	 * @return The sole instance
	 */
	public static final BowerUiPlugin getInstance() {
		return instance;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		instance = null;
		super.stop(context);
	}

	/**
	 * Returns the logger.
	 *
	 * @return The logger
	 */
	public ILogger getLogger() {
		return new BowerUiLog(this.getLog());
	}
}
