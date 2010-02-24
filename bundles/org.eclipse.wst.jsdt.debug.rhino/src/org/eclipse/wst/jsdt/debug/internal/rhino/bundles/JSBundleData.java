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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.URIUtil;

public class JSBundleData {
	private final int bundleId;
	private final String location;
	private final Map headers;
	private final ClassLoader contextClassLoader;
	private URL base;
	private String relativepath = null;

	public JSBundleData(int bundleId, String location, Map headers, ClassLoader contextClassLoader) {
		this.bundleId = bundleId;
		this.location = location;
		this.headers = Collections.unmodifiableMap(new HashMap(headers));
		this.contextClassLoader = contextClassLoader;
	}

	private synchronized void initBase() throws MalformedURLException {
		if (base != null)
			return;

		base = new URL(location);
	}

	public int getBundleId() {
		return bundleId;
	}

	public String getLocation() {
		return location;
	}

	public Map getHeaders() {
		return headers;
	}

	/**
	 * Returns the relative path for this bundle data slicing off the 
	 * host information and the trailing <code>bundle.json</code> bits
	 * @return the relative path for this bundle data
	 */
	public String getRelativePath() {
		if(relativepath == null) {
			if(location == null) {
				return location;
			}
			try {
				URI uri = URIUtil.fromString(location);
				IPath path = new Path(uri.getPath()).removeLastSegments(1);
				relativepath = path.toString();
			}
			catch(URISyntaxException urise) {
				return location;
			}
		}
		return relativepath;
	}
	
	public URL getEntry(String path) {
		try {
			initBase();
			return new URL(base, path);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public ClassLoader getContextClassLoader() {
		return contextClassLoader;
	}
}
