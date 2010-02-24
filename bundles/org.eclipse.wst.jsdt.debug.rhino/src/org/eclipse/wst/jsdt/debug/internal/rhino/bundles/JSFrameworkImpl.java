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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.core.jsdi.json.JSONUtil;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSBundle;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSBundleException;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSFramework;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.LazilyLoadedCtor;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.osgi.framework.Bundle;

public class JSFrameworkImpl implements JSFramework {

	private static final Comparator exportsComparator = new Comparator() {
		// sorts in descending version order, then ascending bundle id
		public int compare(Object arg0, Object arg1) {
			JSExportPackage export0 = (JSExportPackage) arg0;
			JSExportPackage export1 = (JSExportPackage) arg1;

			// order switched for descending order
			int result = export1.getVersion().compareTo(export0.getVersion());
			if (result == 0)
				result = export0.getBundleId() - export1.getBundleId();

			return result;
		}
	};

	private static final Comparator requireBundlesComparator = new Comparator() {
		// sorts in descending version order, then ascending bundle id
		public int compare(Object arg0, Object arg1) {
			JSBundle bundle0 = (JSBundle) arg0;
			JSBundle bundle1 = (JSBundle) arg1;

			// order switched for descending order
			int result = bundle1.getVersion().compareTo(bundle0.getVersion());
			if (result == 0)
				result = bundle0.getBundleId() - bundle1.getBundleId();

			return result;
		}
	};

	private List installOrderBundles = new ArrayList();
	private List resolveOrderBundles = new ArrayList();
	private Map exports = new HashMap();
	private Map requiredBundles = new HashMap();
	private int currentBundleId = 0;

	private final Scriptable frameworkScope;

	private final Map properties = new HashMap();

	private ContextFactory contextFactory;

	private static Map loadHeaders(String location) throws JSBundleException {
		String contents;
		try {
			URL url = new URL(location);
			contents = readContents(url);
		} catch (IOException e) {
			throw new JSBundleException("error reading contents from: " + location, e); //$NON-NLS-1$
		}

		try {
			Object headers = JSONUtil.read(contents);
			if (headers instanceof Map)
				return (Map) headers;
			throw new JSBundleException("invalid representation - JSON Object expected from: " + location); //$NON-NLS-1$
		} catch (RuntimeException e) {
			throw new JSBundleException("error parsing JSON contents from: " + location, e); //$NON-NLS-1$
		}
	}

	private static String readContents(URL url) throws IOException {
		Reader reader = new InputStreamReader(new BufferedInputStream(url.openStream()));
		try {
			StringBuffer buffer = new StringBuffer();
			int read = 0;
			char[] cbuf = new char[1024];
			while (-1 != (read = reader.read(cbuf))) {
				buffer.append(cbuf, 0, read);
			}
			return buffer.toString();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}

	public JSFrameworkImpl() {
		this(new ContextFactory());
	}

	public JSFrameworkImpl(ContextFactory contextFactory) {
		this.contextFactory = contextFactory;
		frameworkScope = (Scriptable) contextFactory.call(new ContextAction() {
			public Object run(Context cx) {
				return cx.initStandardObjects();
			}
		});
	}
	
	
	ContextFactory getContextFactory() {
		return contextFactory;
	}

	public JSBundle installBundle(String location) throws JSBundleException {
		Map headers = loadHeaders(location);
		return installBundle(location, headers);
	}

	public JSBundle installBundle(String location, Map headers) throws JSBundleException {
		return installBundle(location, headers, new RhinoClassLoader());
	}

	public JSBundle installBundle(String location, Bundle bundle) throws JSBundleException {
		Map headers = loadHeaders(location);
		return installBundle(location, headers, new RhinoClassLoader(bundle));
	}

	public JSBundle installBundle(String location, Map headers, Bundle bundle) throws JSBundleException {
		if (bundle == null)
			throw new IllegalArgumentException("Bundle cannot be null"); //$NON-NLS-1$
		return installBundle(location, headers, new RhinoClassLoader(bundle));
	}

	/**
	 * @throws JSBundleException  
	 */
	public JSBundle installBundle(String location, Map headers, RhinoClassLoader contextClassLoader) throws JSBundleException {
		if (location == null)
			throw new IllegalArgumentException("Location cannot be null"); //$NON-NLS-1$

		if (contextClassLoader == null)
			contextClassLoader = new RhinoClassLoader();

		JSBundleData bundleData = new JSBundleData(currentBundleId, location, headers, contextClassLoader);
		JSBundle jsBundle = new JSBundleImpl(this, bundleData);
		int index = installOrderBundles.indexOf(jsBundle);
		if (index != -1)
			return (JSBundle) installOrderBundles.get(index);

		currentBundleId++;
		installOrderBundles.add(jsBundle);
		return jsBundle;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.internal.javascript.JSFramework#getBundles()
	 */
	public JSBundle[] getBundles() {
		return (JSBundle[]) installOrderBundles.toArray(new JSBundle[installOrderBundles.size()]);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.internal.javascript.JSFramework#refresh()
	 */
	public void refresh() {
		boolean uninstalledBundleFound = false;
		for (Iterator iterator = resolveOrderBundles.iterator(); iterator.hasNext();) {
			JSBundleImpl bundle = (JSBundleImpl) iterator.next();
			if (bundle.getState() == JSBundle.UNINSTALLED) {
				uninstalledBundleFound = true;
				installOrderBundles.remove(bundle);
			}

			if (uninstalledBundleFound) {
				unresolveBundle(bundle);
				iterator.remove();
			}
		}
		if (uninstalledBundleFound)
			resolve();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.internal.javascript.JSFramework#resolve()
	 */
	public void resolve() {
		final List unresolvedBundles = new ArrayList();
		for (Iterator iterator = installOrderBundles.iterator(); iterator.hasNext();) {
			JSBundle bundle = (JSBundle) iterator.next();
			if (bundle.getState() == JSBundle.INSTALLED)
				unresolvedBundles.add(bundle);
		}

		contextFactory.call(new ContextAction() {
			public Object run(Context cx) {
				List resolvedBundles = new ArrayList();
				while (!unresolvedBundles.isEmpty()) {
					JSBundleImpl resolvedBundle = stepResolver(unresolvedBundles);
					if (resolvedBundle != null)
						resolvedBundles.add(resolvedBundle);
					else
						break;
				}
				for (Iterator iterator = resolvedBundles.iterator(); iterator.hasNext();) {
					JSBundleImpl resolvedBundle = (JSBundleImpl) iterator.next();
					if (resolvedBundle.isMarkedStarted())
						resolvedBundle.start();
				}
				return null;
			}
		});
	}

	JSBundleImpl stepResolver(List unresolved) {
		for (Iterator iterator = unresolved.iterator(); iterator.hasNext();) {
			JSBundleImpl bundle = (JSBundleImpl) iterator.next();
			if (resolveBundle(bundle)) {
				resolveOrderBundles.add(bundle);
				iterator.remove();

				return bundle;
			}
		}
		return null;
	}

	private boolean resolveBundle(JSBundleImpl jsBundle) {
		if (jsBundle.isSingleton() && requiredBundles.containsKey(jsBundle.getSymbolicName()))
			return false;

		if (wire(jsBundle)) {
			jsBundle.resolve();
			addExports(jsBundle);
			addRequiredBunde(jsBundle);
			return true;
		}
		unwire(jsBundle);
		return false;
	}

	private void unresolveBundle(JSBundleImpl jsBundle) {
		removeExports(jsBundle);
		removeRequiredBunde(jsBundle);
		jsBundle.unresolve();
		unwire(jsBundle);
	}

	private boolean wire(JSBundleImpl bundle) {
		return wireRequires(bundle) && wireImports(bundle);
	}

	private boolean wireRequires(JSBundleImpl bundle) {
		List requires = bundle.getRequires();
		for (Iterator iterator = requires.iterator(); iterator.hasNext();) {
			JSRequireBundle jsRequire = (JSRequireBundle) iterator.next();
			String name = jsRequire.getName();
			List candidates = (List) requiredBundles.get(name);
			if (candidates == null)
				return false;

			boolean satisfied = false;
			for (Iterator candidatesIterator = candidates.iterator(); candidatesIterator.hasNext();) {
				JSBundleImpl candidate = (JSBundleImpl) candidatesIterator.next();
				satisfied = jsRequire.wire(candidate);
				if (satisfied)
					break;
			}
			if (!satisfied && !jsRequire.isOptional())
				return false;
		}
		return true;
	}

	private boolean wireImports(JSBundleImpl bundle) {
		Collection imports = bundle.getImports();
		for (Iterator iterator = imports.iterator(); iterator.hasNext();) {
			JSImportPackage jsImport = (JSImportPackage) iterator.next();
			String name = jsImport.getName();
			List candidates = (List) exports.get(name);
			if (candidates == null)
				return false;

			boolean satisfied = false;
			for (Iterator candidatesIterator = candidates.iterator(); candidatesIterator.hasNext();) {
				JSExportPackage candidate = (JSExportPackage) candidatesIterator.next();
				satisfied = jsImport.wire(candidate);
				if (satisfied)
					break;
			}
			if (!satisfied && !jsImport.isOptional())
				return false;
		}
		return true;
	}

	private void addExports(JSBundleImpl bundle) {
		for (Iterator it = bundle.getExports().iterator(); it.hasNext();) {
			JSExportPackage jsExport = (JSExportPackage) it.next();
			String name = jsExport.getName();
			List namedExports = (List) exports.get(name);
			if (namedExports == null) {
				namedExports = new ArrayList();
				exports.put(name, namedExports);
			}
			namedExports.add(jsExport);
			Collections.sort(namedExports, exportsComparator);
		}
	}

	private void addRequiredBunde(JSBundle bundle) {
		String name = bundle.getSymbolicName();
		List namedBundles = (List) requiredBundles.get(name);
		if (namedBundles == null) {
			namedBundles = new ArrayList();
			requiredBundles.put(name, namedBundles);
		}
		namedBundles.add(bundle);
		Collections.sort(namedBundles, requireBundlesComparator);
	}

	private void unwire(JSBundleImpl jsBundle) {
		unwireImports(jsBundle);
		unwireRequires(jsBundle);
	}

	private void unwireImports(JSBundleImpl bundle) {
		Collection imports = bundle.getImports();
		for (Iterator iterator = imports.iterator(); iterator.hasNext();) {
			JSImportPackage jsImport = (JSImportPackage) iterator.next();
			jsImport.unwire();
		}
	}

	private void unwireRequires(JSBundleImpl bundle) {
		List requires = bundle.getRequires();
		for (Iterator iterator = requires.iterator(); iterator.hasNext();) {
			JSRequireBundle jsRequire = (JSRequireBundle) iterator.next();
			jsRequire.unwire();
		}
	}

	private void removeRequiredBunde(JSBundle bundle) {
		String name = bundle.getSymbolicName();
		List namedBundles = (List) requiredBundles.get(name);
		if (namedBundles == null)
			return;
		namedBundles.remove(bundle);
		if (namedBundles.isEmpty())
			requiredBundles.remove(name);
	}

	private void removeExports(JSBundleImpl bundle) {
		for (Iterator it = bundle.getExports().iterator(); it.hasNext();) {
			JSExportPackage jsExport = (JSExportPackage) it.next();
			String name = jsExport.getName();
			List namedExports = (List) exports.get(name);
			if (namedExports == null)
				continue;
			namedExports.remove(jsExport);
			if (namedExports.isEmpty())
				exports.remove(name);
		}
	}

	protected Scriptable createScope(ClassLoader classLoader) {
		Context cx = Context.getCurrentContext();
		ClassLoader current = cx.getApplicationClassLoader();
		cx.setApplicationClassLoader(classLoader);
		try {
			ScriptableObject scope = (ScriptableObject) cx.newObject(frameworkScope);
			//scope.setPrototype(frameworkScope);
			scope.setParentScope(frameworkScope);
			// We want the Packages and other NativeJavaTopPackage(s) to be per class loader.
			new LazilyLoadedCtor(scope, "Packages", "org.mozilla.javascript.NativeJavaTopPackage", false); //$NON-NLS-1$//$NON-NLS-2$
			// this is done to prime the NativeJavaTopPackage (named Packages) with the correct class loader
			scope.get("Packages", scope); //$NON-NLS-1$
			return scope;
		} finally {
			cx.setApplicationClassLoader(current);
		}
	}

	public JSBundleContext getBundleContext(JSBundle jsBundle) {
		return new JSBundleContext(jsBundle, this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.internal.javascript.JSFramework#getProperty(java.lang.String)
	 */
	public String getProperty(String name) {
		return (String) properties.get(name);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.internal.javascript.JSFramework#setProperty(java.lang.String, java.lang.String)
	 */
	public void setProperty(String name, String value) {
		properties.put(name, value);
	}

	public void shutdown() {
		List reversed = new ArrayList(installOrderBundles);
		Collections.reverse(reversed);
		for (Iterator iterator = reversed.iterator(); iterator.hasNext();) {
			JSBundle jsBundle = (JSBundle) iterator.next();
			jsBundle.stop();
			jsBundle.uninstall();
		}
		refresh();
	}
}
