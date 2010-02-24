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

import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.Scriptable;
import org.osgi.framework.Version;

public interface JSBundle {

	public static final int UNINSTALLED = 1;
	public static final int INSTALLED = 2;
	public static final int RESOLVED = 4;
	public static final int STARTING = 8;
	public static final int STOPPING = 16;
	public static final int ACTIVE = 32;

	public String getSymbolicName();

	public Version getVersion();

	public int getBundleId();

	public String getLocation();

	public Map getHeaders();

	public int getState();

	public void uninstall();

	public void start();

	public void stop();

	public Object lookup(String name);

	public Scriptable getScope();

	public Object call(ContextAction action);
}