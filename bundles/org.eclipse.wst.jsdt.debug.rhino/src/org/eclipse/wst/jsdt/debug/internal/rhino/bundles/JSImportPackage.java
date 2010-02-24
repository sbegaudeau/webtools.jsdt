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

public class JSImportPackage {
	private String name;
	private JSVersionRange versionRange = JSVersionRange.emptyRange;
	private String bundleSymbolicName;
	private JSVersionRange bundleVersionRange = JSVersionRange.emptyRange;
	private boolean optional = false;
	private Map attributes = new HashMap();
	private Map directives = new HashMap();
	private JSExportPackage wiredExport;

	public JSImportPackage(String header) {
		if (header == null)
			throw new IllegalArgumentException("header cannot be null"); //$NON-NLS-1$
		parseImport(header);
	}

	private void parseImport(String header) {
		StringTokenizer tokenizer = new StringTokenizer(header, JSConstants.PARAMETER_DELIMITER);
		this.name = tokenizer.nextToken().trim();
		while (tokenizer.hasMoreTokens()) {
			String token = (String) tokenizer.nextElement();
			if (token.indexOf(JSConstants.DIRECTIVE_EQUALS) != -1)
				parseDirective(token);
			else if (token.indexOf(JSConstants.ATTRIBUTE_EQUALS) != -1)
				parseAttribute(token);
			else
				throw new IllegalArgumentException("bad import syntax: " + token + " in " + header); //$NON-NLS-1$//$NON-NLS-2$
		}
	}

	private void parseAttribute(String token) {
		int index = token.indexOf(JSConstants.ATTRIBUTE_EQUALS);
		String attributeName = token.substring(0, index).trim();
		if (attributeName.length() == 0)
			return;

		Object value = token.substring(index + JSConstants.ATTRIBUTE_EQUALS.length()).trim();

		if (attributeName.equals(Constants.VERSION_ATTRIBUTE))
			versionRange = new JSVersionRange((String) value);
		else if (attributeName.equals(Constants.BUNDLE_SYMBOLICNAME_ATTRIBUTE))
			bundleSymbolicName = (String) value;
		else if (attributeName.equals(Constants.BUNDLE_VERSION_ATTRIBUTE))
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

	public String getName() {
		return name;
	}

	public JSVersionRange getVersionRange() {
		return versionRange;
	}

	public String getBundleSymbolicName() {
		return bundleSymbolicName;
	}

	public JSVersionRange getBundleVersionRange() {
		return bundleVersionRange;
	}

	public boolean isOptional() {
		return optional;
	}

	public Map getAttributes() {
		return attributes;
	}

	public Map getDirectives() {
		return directives;
	}

	public boolean wire(JSExportPackage candidate) {
		if (!name.equals(candidate.getName()))
			return false;

		for (Iterator iterator = attributes.entrySet().iterator(); iterator.hasNext();) {
			Entry entry = (Entry) iterator.next();
			String key = (String) entry.getKey();

			if (key.equals(Constants.VERSION_ATTRIBUTE)) {
				if (!versionRange.isIncluded(candidate.getVersion()))
					return false;
			} else if (key.equals(Constants.BUNDLE_SYMBOLICNAME_ATTRIBUTE)) {
				if (!bundleSymbolicName.equals(candidate.getBundleSymbolicName()))
					return false;
			} else if (key.equals(Constants.BUNDLE_VERSION_ATTRIBUTE)) {
				if (!bundleVersionRange.isIncluded(candidate.getBundleVersion()))
					return false;
			} else {
				Object value = entry.getValue();
				Object attributeValue = candidate.getAttributes().get(key);
				if (attributeValue == null || !attributeValue.equals(value))
					return false;
			}
		}

		for (Iterator iterator = candidate.getMandatory().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			Object mandatoryValue = candidate.getAttributes().get(key);
			Object value = attributes.get(key);
			if (value == null || !value.equals(mandatoryValue))
				return false;
		}

		wiredExport = candidate;
		return true;
	}

	public JSExportPackage getWiredExport() {
		return wiredExport;
	}

	public void unwire() {
		wiredExport = null;
	}

}
