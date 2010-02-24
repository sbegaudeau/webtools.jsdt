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
package org.eclipse.wst.jsdt.debug.rhino.bundles;

import java.util.Map;

import org.osgi.framework.Bundle;

public interface JSFramework {
	public JSBundle installBundle(String location) throws JSBundleException;

	public JSBundle installBundle(String location, Map headers) throws JSBundleException;
	
	public JSBundle installBundle(String location, Bundle bundle) throws JSBundleException;

	public JSBundle[] getBundles();

	public void refresh();

	public void resolve();

	public String getProperty(String name);

	public void setProperty(String name, String value);
}