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
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.wst.jsdt.debug.core.jsdi.json.JSONUtil;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSBundle;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSConstants;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.framework.Version;

public class JSBundleImpl implements JSBundle {
	private static final String ACTIVATOR_STOP_METHOD = "stop"; //$NON-NLS-1$
	private static final String ACTIVATOR_START_METHOD = "start"; //$NON-NLS-1$
	private String symbolicName;
	private Version version;
	private List imports;
	private List requires;
	private List exports;
	private boolean singleton = false;
	private boolean markedStarted = false;

	private int state;
	Scriptable scope;
	private JSFrameworkImpl framework;
	private JSBundleData bundleData;
	JSBundleContext bundleContext;
	Scriptable activator;

	public JSBundleImpl(JSFrameworkImpl framework, JSBundleData bundleData) {
		this.framework = framework;
		this.bundleData = bundleData;
		state = INSTALLED;

		Map headers = bundleData.getHeaders();
		parseSymbolicName((String) headers.get(Constants.BUNDLE_SYMBOLICNAME));
		parseVersion((String) headers.get(Constants.BUNDLE_VERSION));
		parseImports((String) headers.get(Constants.IMPORT_PACKAGE));
		parseExports((String) headers.get(Constants.EXPORT_PACKAGE));
		parseRequires((String) headers.get(Constants.REQUIRE_BUNDLE));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.internal.javascript.JSBundle#getSymbolicName()
	 */
	public String getSymbolicName() {
		return symbolicName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.internal.javascript.JSBundle#getVersion()
	 */
	public Version getVersion() {
		return version;
	}

	public List getImports() {
		return imports;
	}

	public List getRequires() {
		return requires;
	}

	public List getExports() {
		return exports;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbolicName == null) ? 0 : symbolicName.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JSBundleImpl other = (JSBundleImpl) obj;
		if (symbolicName == null) {
			if (other.symbolicName != null)
				return false;
		} else if (!symbolicName.equals(other.symbolicName))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	private void parseSymbolicName(String symbolicNameHeader) {
		if (symbolicNameHeader == null)
			throw new IllegalArgumentException("Bundle-SymbolicName cannot be null");
		StringTokenizer tokenizer = new StringTokenizer(symbolicNameHeader, JSConstants.PARAMETER_DELIMITER);
		symbolicName = tokenizer.nextToken().trim();
		if (symbolicName.length() == 0)
			throw new IllegalArgumentException("Illegal Bundle-SymbolicName");
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (token.indexOf(JSConstants.DIRECTIVE_EQUALS) != -1) {
				int index = token.indexOf(JSConstants.DIRECTIVE_EQUALS);
				String directiveName = token.substring(0, index).trim();
				if (directiveName.length() == 0)
					throw new IllegalArgumentException("bad syntax: " + token + " in " + symbolicNameHeader); //$NON-NLS-1$//$NON-NLS-2$

				if (!directiveName.equals(Constants.SINGLETON_DIRECTIVE))
					continue;
				String value = token.substring(index + JSConstants.DIRECTIVE_EQUALS.length()).trim();
				if (value.equalsIgnoreCase(Boolean.TRUE.toString()))
					singleton = true;
			} else
				throw new IllegalArgumentException("bad syntax: " + token + " in " + symbolicNameHeader); //$NON-NLS-1$//$NON-NLS-2$
		}
	}

	private void parseVersion(String header) {
		version = Version.parseVersion(header);
	}

	private void parseRequires(String header) {
		if (header == null) {
			requires = Collections.EMPTY_LIST;
			return;
		}

		requires = new ArrayList();
		StringTokenizer tokenizer = new StringTokenizer(header, JSConstants.CLAUSE_DELIMITER);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			JSRequireBundle jsRequire = new JSRequireBundle(token);
			if (jsRequire != null)
				requires.add(jsRequire);
		}
	}

	private void parseExports(String header) {
		if (header == null) {
			exports = Collections.EMPTY_LIST;
			return;
		}
		exports = new ArrayList();
		StringTokenizer tokenizer = new StringTokenizer(header, JSConstants.CLAUSE_DELIMITER);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			JSExportPackage jsExport = new JSExportPackage(token, this);
			if (jsExport != null)
				exports.add(jsExport);
		}
	}

	private void parseImports(String header) {
		if (header == null) {
			imports = Collections.EMPTY_LIST;
			return;
		}

		imports = new ArrayList();
		StringTokenizer tokenizer = new StringTokenizer(header, JSConstants.CLAUSE_DELIMITER);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			JSImportPackage jsImport = new JSImportPackage(token);
			if (jsImport != null)
				imports.add(jsImport);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.internal.javascript.JSBundle#getBundleId()
	 */
	public int getBundleId() {
		return bundleData.getBundleId();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.internal.javascript.JSBundle#getLocation()
	 */
	public String getLocation() {
		return bundleData.getLocation();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.internal.javascript.JSBundle#getHeaders()
	 */
	public Map getHeaders() {
		return bundleData.getHeaders();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.internal.javascript.JSBundle#getState()
	 */
	public int getState() {
		return state;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.internal.javascript.JSBundle#uninstall()
	 */
	public void uninstall() {
		stop();
		state = UNINSTALLED;
	}

	protected void resolve() {
		if (state != INSTALLED)
			return;

		Map namedExports = new HashMap();

		// process imports (first)
		for (Iterator iterator = imports.iterator(); iterator.hasNext();) {
			JSImportPackage jsImport = (JSImportPackage) iterator.next();
			JSExportPackage jsExport = jsImport.getWiredExport();
			if (!namedExports.containsKey(jsExport.getName()))
				namedExports.put(jsExport.getName(), jsExport);
		}

		// process requires
		for (Iterator iterator = requires.iterator(); iterator.hasNext();) {
			JSRequireBundle jsRequire = (JSRequireBundle) iterator.next();
			JSBundleImpl wiredBundle = jsRequire.getWiredBundle();
			List wiredBundleExports = wiredBundle.getExports();
			for (Iterator exportIterator = wiredBundleExports.iterator(); exportIterator.hasNext();) {
				JSExportPackage jsExport = (JSExportPackage) exportIterator.next();
				if (!namedExports.containsKey(jsExport.getName()))
					namedExports.put(jsExport.getName(), jsExport);
			}
		}

		ArrayList names = new ArrayList(namedExports.keySet());
		// this sorts the set of names we'll be importing alphabetically and
		// perhaps more importantly will allow us to create the shorter/parent dotted entries first
		Collections.sort(names);

		Scriptable newScope = framework.createScope(bundleData.getContextClassLoader());
		for (Iterator iterator = names.iterator(); iterator.hasNext();) {
			String exportName = (String) iterator.next();
			JSExportPackage jsExport = (JSExportPackage) namedExports.get(exportName);
			jsExport.addToScope(newScope);
		}

		evalScript(newScope);
		scope = newScope;
		state = RESOLVED;
	}

	private void evalScript(Scriptable scriptScope) {
		Context context = Context.getCurrentContext();

		String scriptPath = (String) bundleData.getHeaders().get(JSConstants.BUNDLE_SCRIPTPATH);
		if (scriptPath == null)
			scriptPath = JSConstants.SCRIPT_PATH_DOT;

		StringTokenizer tokenizer = new StringTokenizer(scriptPath, JSConstants.SCRIPT_PATH_DELIMITER);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken().trim();
			if (token.equals(JSConstants.SCRIPT_PATH_DOT)) {
				String script = (String) bundleData.getHeaders().get(JSConstants.BUNDLE_SCRIPT);
				if (script != null)
					context.evaluateString(scriptScope, script, "dot.js", 0, null); //$NON-NLS-1$
				continue;
			}

			Reader reader = null;
			try {
				URL scriptURL = bundleData.getEntry(token);
				reader = new InputStreamReader(scriptURL.openStream());
				
				Map scriptProperties = new HashMap();
				scriptProperties.put("name", token);
				scriptProperties.put("base", bundleData.getLocation());
				scriptProperties.put("path", bundleData.getRelativePath());
				scriptProperties.put(Constants.BUNDLE_SYMBOLICNAME, symbolicName);
				scriptProperties.put(Constants.BUNDLE_VERSION, version.toString());
				if (bundleData.getContextClassLoader() instanceof RhinoClassLoader) {
					RhinoClassLoader rhinoClassLoader = (RhinoClassLoader) bundleData.getContextClassLoader();
					Bundle bundle = rhinoClassLoader.getBundle();
					if (bundle != null) {
						scriptProperties.put("OSGi." + Constants.BUNDLE_SYMBOLICNAME, bundle.getSymbolicName());
						scriptProperties.put("OSGi." + Constants.BUNDLE_VERSION, bundle.getVersion().toString());
					}
				}
			
				context.evaluateReader(scriptScope, reader, JSONUtil.write(scriptProperties), 0, null);
			} catch (IOException e) {
				throw new IllegalStateException("Error reading script for " + this.toString() + ": " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
			} finally {
				try {
					if (reader != null)
						reader.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	protected void unresolve() {
		if (state == ACTIVE) {
			stop();
			markedStarted = true;
		}
		scope = null;

		if (state == RESOLVED)
			state = INSTALLED;
	}

	public String toString() {
		return "JavaScriptBundle{symbolic-name=" + symbolicName + ", version=" + version.toString() + "}"; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.internal.javascript.JSBundle#getScope()
	 */
	public Scriptable getScope() {
		return scope;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.internal.javascript.JSBundle#start()
	 */
	public void start() {
		markedStarted = true;
		if (state != RESOLVED)
			return;

		state = STARTING;
		bundleContext = framework.getBundleContext(this);
		activator = createActivatorInstance();
		if (activator != null) {
			final Callable startMethod = (Callable) activator.get(ACTIVATOR_START_METHOD, activator);
			framework.getContextFactory().call(new ContextAction() {
				public Object run(Context cx) {
					startMethod.call(cx, activator, activator, new Object[] {bundleContext});
					return null;
				}
			});

		}
		state = ACTIVE;
	}

	private Scriptable createActivatorInstance() {
		final String activatorName = (String) getHeaders().get(Constants.BUNDLE_ACTIVATOR);
		if (activatorName == null)
			return null;

		Object value = lookup(activatorName);
		if (!(value instanceof Function))
			throw new IllegalStateException(activatorName + " is not a function."); //$NON-NLS-1$

		final Function constructorFunction = (Function) value;
		return (Scriptable) framework.getContextFactory().call(new ContextAction() {
			public Object run(Context cx) {
				return constructorFunction.construct(cx, scope, null);
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.internal.javascript.JSBundle#stop()
	 */
	public void stop() {
		markedStarted = false;
		if (state != ACTIVE)
			return;

		state = STOPPING;
		if (activator != null) {
			final Callable stopMethod = (Callable) activator.get(ACTIVATOR_STOP_METHOD, activator);
			framework.getContextFactory().call(new ContextAction() {
				public Object run(Context cx) {
					stopMethod.call(Context.getCurrentContext(), activator, activator, new Object[] {bundleContext});
					return null;
				}
			});
		}
		state = RESOLVED;
	}

	public boolean isSingleton() {
		return singleton;
	}

	public boolean isMarkedStarted() {
		return markedStarted;
	}

	public Object call(final ContextAction action) {
		final ClassLoader bundleClassLoader = bundleData.getContextClassLoader();
		if (bundleClassLoader == null)
			return framework.getContextFactory().call(action);

		return framework.getContextFactory().call(new ContextAction() {
			public Object run(Context context) {
				ClassLoader current = context.getApplicationClassLoader();
				context.setApplicationClassLoader(bundleClassLoader);
				try {
					return action.run(context);
				} finally {
					context.setApplicationClassLoader(current);
				}
			}
		});
	}

	public Object lookup(String name) {
		Scriptable currentScope = scope;
		StringTokenizer tokenizer = new StringTokenizer(name, "."); //$NON-NLS-1$
		while (true) {
			String token = tokenizer.nextToken();
			Object value = currentScope.get(token, currentScope);
			if (!tokenizer.hasMoreTokens())
				return value;

			if (value instanceof Scriptable)
				currentScope = (Scriptable) value;
			else
				throw new RuntimeException("Not Found: " + name + " in " + this.toString()); //$NON-NLS-1$//$NON-NLS-2$
		}
	}
}
