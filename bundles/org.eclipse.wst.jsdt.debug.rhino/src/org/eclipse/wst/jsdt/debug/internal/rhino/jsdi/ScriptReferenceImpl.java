/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.rhino.jsdi;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.json.JSONConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.RhinoDebugPlugin;
import org.osgi.framework.Constants;

/**
 * Rhino implementation of {@link ScriptReference}
 * 
 * @see MirrorImpl
 * @see ScriptReference
 * @since 1.0
 */
public class ScriptReferenceImpl extends MirrorImpl implements ScriptReference {

	/**
	 * The id of the script - this can be used to look it up in the {@link VirtualMachineImpl}
	 */
	private final Long scriptId;
//	private String sourcePath = null;
	private URI sourceuri = null;
	private final String source;
	private final Boolean generated;
	private final List lineLocations = new ArrayList();
	private final List functionLocations = new ArrayList();
	private Map sourceProperties = null;

	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param jsonScript
	 */
	public ScriptReferenceImpl(VirtualMachineImpl vm, Map jsonScript) {
		super(vm);
		this.scriptId = new Long(((Number) jsonScript.get(JSONConstants.SCRIPT_ID)).longValue());
//		this.sourcePath = (String) jsonScript.get(JSONConstants.LOCATION);
		this.sourceProperties = (Map) jsonScript.get(JSONConstants.PROPERTIES);
		this.source = (String) jsonScript.get(JSONConstants.SOURCE);
		this.generated = (Boolean) jsonScript.get(JSONConstants.GENERATED);
		List lines = (List) jsonScript.get(JSONConstants.LINES);
		for (Iterator iterator = lines.iterator(); iterator.hasNext();) {
			Number lineNumber = (Number) iterator.next();
			Location location = new LocationImpl(vm, null, lineNumber.intValue(), this);
			lineLocations.add(location);
		}
		List functions = (List) jsonScript.get(JSONConstants.FUNCTIONS);
		for (Iterator iterator = functions.iterator(); iterator.hasNext();) {
			String functionName = (String) iterator.next();
			Location location = new LocationImpl(vm, functionName, 0, this);
			functionLocations.add(location);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference#allFunctionLocations()
	 */
	public List allFunctionLocations() {
		return Collections.unmodifiableList(functionLocations);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference#allLineLocations()
	 */
	public List allLineLocations() {
		return Collections.unmodifiableList(lineLocations);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference#functionLocation(java.lang.String)
	 */
	public Location functionLocation(String functionName) {
		for (Iterator iterator = functionLocations.iterator(); iterator.hasNext();) {
			Location location = (Location) iterator.next();
			if (location.functionName().equals(functionName))
				return location;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference#lineLocation(int)
	 */
	public Location lineLocation(int lineNumber) {
		for (Iterator iterator = lineLocations.iterator(); iterator.hasNext();) {
			Location location = (Location) iterator.next();
			if (location.lineNumber() == lineNumber)
				return location;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference#source()
	 */
	public String source() {
		return source;
	}

	/**
	 * @return the id of the {@link ScriptReference}
	 */
	public Long getScriptId() {
		return scriptId;
	}

	/**
	 * @return true if this {@link ScriptReference} was generated
	 */
	public Boolean isGenerated() {
		return generated;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference#sourceURI()
	 */
	public URI sourceURI() {
		synchronized (vm) {
			if (this.sourceuri == null) {
				try {
					IPath path = new Path((String) this.sourceProperties.get(Constants.BUNDLE_SYMBOLICNAME));
					path = path.append((String) this.sourceProperties.get(JSONConstants.PATH));
					path = path.append((String) this.sourceProperties.get(JSONConstants.NAME));
					this.sourceuri = new URI("file", null, path.makeAbsolute().toString(), null); //$NON-NLS-1$
				} catch (URISyntaxException urise) {
					RhinoDebugPlugin.log(urise);
				}
			}
		}
		return this.sourceuri;
	}
}
