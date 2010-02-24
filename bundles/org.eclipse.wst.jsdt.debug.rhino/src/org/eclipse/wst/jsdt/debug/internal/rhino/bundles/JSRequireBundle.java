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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import org.eclipse.wst.jsdt.debug.rhino.bundles.JSConstants;
import org.osgi.framework.Constants;

public class JSRequireBundle {
	private String name;
	private Map attributes = new HashMap();
	private Map directives = new HashMap();
	private boolean optional = false;
	private JSVersionRange bundleVersionRange = JSVersionRange.emptyRange;
	private JSBundleImpl wiredBundle;

	public JSRequireBundle(String header) {
		if (header == null)
			throw new IllegalArgumentException("header cannot be null"); //$NON-NLS-1$
		parseRequire(header);
	}

	private void parseRequire(String header) {
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

		if (attributeName.equals(Constants.BUNDLE_VERSION_ATTRIBUTE))
			bundleVersionRange = new JSVersionRange((String) value);

		attributes.put(attributeName, value);
	}

	private void parseDirective(String token) {
		int index = token.indexOf(JSConstants.DIRECTIVE_EQUALS);
		String directiveName = token.substring(0, index).trim();
		if (directiveName.length() == 0)
			return;

		String value = token.substring(index + JSConstants.DIRECTIVE_EQUALS.length()).trim();
		if (directiveName.equals(Constants.RESOLUTION_DIRECTIVE))
			optional = Constants.RESOLUTION_OPTIONAL.equals(value);
		directives.put(directiveName, value);
	}

	public boolean isOptional() {
		return optional;
	}

	public String getName() {
		return name;
	}

	public JSVersionRange getBundleVersionRange() {
		return bundleVersionRange;
	}

	public Map getAttributes() {
		return attributes;
	}

	public Map getDirectives() {
		return directives;
	}

	public boolean wire(JSBundleImpl candidate) {
		if (!name.equals(candidate.getSymbolicName()))
			return false;

		for (Iterator iterator = attributes.entrySet().iterator(); iterator.hasNext();) {
			Entry entry = (Entry) iterator.next();
			String key = (String) entry.getKey();
			if (key.equals(Constants.BUNDLE_VERSION_ATTRIBUTE)) {
				if (!bundleVersionRange.isIncluded(candidate.getVersion()))
					return false;
			}
		}
		wiredBundle = candidate;
		return true;
	}

	public JSBundleImpl getWiredBundle() {
		return wiredBundle;
	}

	public void unwire() {
		wiredBundle = null;
	}
}
