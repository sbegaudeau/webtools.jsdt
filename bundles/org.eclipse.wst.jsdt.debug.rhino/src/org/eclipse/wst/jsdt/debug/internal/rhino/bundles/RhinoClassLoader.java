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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.osgi.framework.Bundle;

/**
 * This Class loader looks up as follows:
 * 1) Thread-ContextClassLoader (top - parent) -- see ContextFinder
 * 2) Rhino Bundle
 * 3) The Bundle referenced at Script creation
 */
public class RhinoClassLoader extends BundleProxyClassLoader {

	private static final Bundle RHINOBUNDLE = Activator.getRhinoBundle();
	private static final ClassLoader EMPTY_CLASSLOADER = new ClassLoader() {
		public URL getResource(String name) {
			return null;
		}

		/**
		 * Note: documented to avoid warning
		 * @throws IOException  
		 */
		public Enumeration findResources(String name) throws IOException {
			return new Enumeration() {
				public boolean hasMoreElements() {
					return false;
				}

				public Object nextElement() {
					return null;
				}
			};
		}

		public Class loadClass(String name) throws ClassNotFoundException {
			throw new ClassNotFoundException(name);
		}
	};
	private final Bundle bundle;

	public RhinoClassLoader() {
		super(RHINOBUNDLE, new RhinoContextFinder(EMPTY_CLASSLOADER));
		this.bundle = null;
	}

	public RhinoClassLoader(Bundle bundle) {
		super(bundle, new BundleProxyClassLoader(RHINOBUNDLE, new RhinoContextFinder(EMPTY_CLASSLOADER)));
		this.bundle = bundle;
	}

	public Bundle getBundle() {
		return bundle;
	}
}
