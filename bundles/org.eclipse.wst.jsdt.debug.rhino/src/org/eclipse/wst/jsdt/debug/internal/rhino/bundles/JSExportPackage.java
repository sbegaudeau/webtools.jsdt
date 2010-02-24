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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.wst.jsdt.debug.rhino.bundles.JSBundle;
import org.eclipse.wst.jsdt.debug.rhino.bundles.JSConstants;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.osgi.framework.Constants;
import org.osgi.framework.Version;

public class JSExportPackage {

	private JSBundle exportingBundle;
	private String name;
	private Version version = Version.emptyVersion;
	private Map attributes = new HashMap();
	private Map directives = new HashMap();
	private List mandatory = new ArrayList();

	public JSExportPackage(String header, JSBundle exportingBundle) {
		if (header == null)
			throw new IllegalArgumentException("header cannot be null"); //$NON-NLS-1$
		if (exportingBundle == null)
			throw new IllegalArgumentException("exportingBundle cannot be null"); //$NON-NLS-1$
		parseExport(header);
		this.exportingBundle = exportingBundle;
	}

	public JSBundle getExportingBundle() {
		return exportingBundle;
	}

	private void parseExport(String header) {
		StringTokenizer tokenizer = new StringTokenizer(header, JSConstants.PARAMETER_DELIMITER);
		this.name = tokenizer.nextToken().trim();
		while (tokenizer.hasMoreTokens()) {
			String token = (String) tokenizer.nextElement();
			if (token.indexOf(JSConstants.DIRECTIVE_EQUALS) != -1)
				parseDirective(token);
			else if (token.indexOf(JSConstants.ATTRIBUTE_EQUALS) != -1)
				parseAttribute(token);
		}
	}

	private void parseAttribute(String token) {
		int index = token.indexOf(JSConstants.ATTRIBUTE_EQUALS);
		String attributeName = token.substring(0, index).trim();
		if (attributeName.length() == 0)
			return;

		Object value = token.substring(index + JSConstants.ATTRIBUTE_EQUALS.length()).trim();

		if (attributeName.equals(Constants.VERSION_ATTRIBUTE))
			version = Version.parseVersion((String) value);
		attributes.put(attributeName, value);
	}

	private void parseDirective(String token) {
		int index = token.indexOf(JSConstants.DIRECTIVE_EQUALS);
		String directiveName = token.substring(0, index).trim();
		if (directiveName.length() == 0)
			return;

		String value = token.substring(index + JSConstants.DIRECTIVE_EQUALS.length()).trim();

		if (directiveName.equals(Constants.MANDATORY_DIRECTIVE))
			parseMandatory(value);

		directives.put(directiveName, value);
	}

	private void parseMandatory(String value) {
		StringTokenizer tokenizer = new StringTokenizer(value, JSConstants.MANDATORY_DELIMITER);
		while (tokenizer.hasMoreTokens()) {
			String token = (String) tokenizer.nextElement();
			token = token.trim();
			if (token.length() > 0)
				mandatory.add(token);
		}
	}

	public String getName() {
		return name;
	}

	public Version getVersion() {
		return version;
	}

	public String getBundleSymbolicName() {
		return exportingBundle.getSymbolicName();
	}

	public Version getBundleVersion() {
		return exportingBundle.getVersion();
	}

	public int getBundleId() {
		return exportingBundle.getBundleId();
	}

	public Map getAttributes() {
		return attributes;
	}

	public Map getDirectives() {
		return directives;
	}

	public List getMandatory() {
		return mandatory;
	}

	protected void addToScope(Scriptable scope) {
		Object value = exportingBundle.lookup(name);
		StringTokenizer tokenizer = new StringTokenizer(name, "."); //$NON-NLS-1$
		while (true) {
			String token = tokenizer.nextToken();
			Object current = scope.get(token, scope);
			if (!tokenizer.hasMoreTokens()) {
				if (current == Scriptable.NOT_FOUND) {
					if (value instanceof NativeObject) {
						Scriptable wrapped = Context.getCurrentContext().newObject(scope);
						wrapped.setPrototype((Scriptable) value);
						value = wrapped;
					}
					scope.put(token, scope, value);
					return;
				}
				throw new IllegalStateException("Resolve error: " + name + " already exists for " + this.toString()); //$NON-NLS-1$//$NON-NLS-2$				
			}
			if (current == Scriptable.NOT_FOUND) {
				current = ScriptableObject.getProperty(scope, token);
				if (current == Scriptable.NOT_FOUND)
					current = Context.getCurrentContext().newObject(scope);
				else if (current instanceof NativeObject) {
					// we need to wrap this object from the prototype
					Scriptable wrapped = Context.getCurrentContext().newObject(scope);
					wrapped.setPrototype((Scriptable) current);
					current = wrapped;
				} else
					throw new IllegalStateException("Resolve error: " + name + "-" + token + " already exists for " + this.toString()); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				scope.put(token, scope, current);
			}
			scope = (Scriptable) current;
		}
	}
}
