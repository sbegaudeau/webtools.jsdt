/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.crossfire.CrossFirePlugin;
import org.eclipse.wst.jsdt.debug.internal.crossfire.Tracing;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Attributes;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Commands;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.JSON;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFRequestPacket;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.CFResponsePacket;

/**
 * Default implementation of {@link ScriptReference} for Crossfire
 * 
 * @since 1.0
 */
public class CFScriptReference extends CFMirror implements ScriptReference {
	
	private String context_id = null;
	private String url = null;
	private int srclength = 0;
	private int linecount = 0;
	private int coloffset = 0;
	private int lineoffset = 0;
	private String source = null;
	private URI sourceuri = null;
	
	private List linelocs = new ArrayList();
	
	/**
	 * Constructor
	 * @param vm
	 * @param context_id
	 * @param json
	 */
	public CFScriptReference(VirtualMachine vm, String context_id, Map json) {
		super(vm);
		this.context_id = context_id;
		this.url = (String) json.get(Attributes.URL);
		initializeScript(json);
	}

	/**
	 * Initialize the script from the given JSON map
	 * 
	 * @param json
	 */
	void initializeScript(Map json) {
		Number value = (Number) json.get(Attributes.SOURCE_LENGTH);
		if(value != null) {
			this.srclength = value.intValue();
		}
		value = (Number) json.get(Attributes.LINE_COUNT);
		if(value != null) {
			this.linecount = value.intValue();
		}
		value = (Number) json.get(Attributes.LINE_OFFSET);
		if(value != null) {
			this.lineoffset = value.intValue();
		}
		value = (Number) json.get(Attributes.COLUMN_OFFSET);
		if(value != null) {
			this.coloffset = value.intValue();
		}
		source = (String) json.get(Attributes.SOURCE);
		prepareLocations(linecount);
	}
	
	/**
	 * Creates the line locations
	 * 
	 * @param lines
	 */
	void prepareLocations(int lines) {
		linelocs.clear();  //remove old line infos
		for (int i = 1; i <= lines; i++) {
			linelocs.add(new CFLocation(virtualMachine(), this, null, i));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference#allLineLocations()
	 */
	public List allLineLocations() {
		return Collections.unmodifiableList(linelocs);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference#lineLocation(int)
	 */
	public Location lineLocation(int lineNumber) {
		if(lineNumber <= linelocs.size()) {
			return (Location) linelocs.get(lineNumber-1);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference#allFunctionLocations()
	 */
	public List allFunctionLocations() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference#functionLocation(java.lang.String)
	 */
	public Location functionLocation(String functionName) {
		return null;
	}

	/**
	 * The url of the script
	 * @return the url
	 */
	public String url() {
		return url;
	}
	
	/**
	 * Returns the context id of this script - this represents the id of the owning thread
	 * <br><br>
	 * This method can return <code>null</code>
	 * 
	 * @return the context id for the owning thread or <code>null</code>
	 */
	public String context() {
		return context_id;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference#source()
	 */
	public synchronized String source() {
		if(source == null) {
			CFRequestPacket request = new CFRequestPacket(Commands.SCRIPTS, context_id);
			request.setArgument(Attributes.INCLUDE_SOURCE, Boolean.TRUE);
			request.setArgument(Attributes.URLS, Arrays.asList(new String[] {url}));
			CFResponsePacket response = crossfire().sendRequest(request);
			if(response.isSuccess()) {
				List list = (List)response.getBody().get(Attributes.SCRIPTS);
				if (list != null && list.size() > 0) {
					initializeScript((Map)list.get(0));
				}
			}
			else if(TRACE) {
				Tracing.writeString("SCRIPTREF [failed source request]: "+JSON.serialize(request)); //$NON-NLS-1$
			}
		}
		return source;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference#sourceURI()
	 */
	public synchronized URI sourceURI() {
		if(sourceuri == null) {
			try {
				sourceuri = URIUtil.fromString(url);
			}
			catch(IllegalArgumentException iae) {
				try {
					sourceuri = CrossFirePlugin.fileURI(new Path(url));
				} catch (URISyntaxException e) {
					CrossFirePlugin.log(e);
				}
			}
			catch(URISyntaxException urise) {
				CrossFirePlugin.log(urise);
			}
		}
		return sourceuri;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Crossfire ScriptReference: [context_id - ").append(context_id).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append(" [url - ").append(url).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append(" [srclength - ").append(srclength).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append(" [linecount - ").append(linecount).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append(" [lineoffset - ").append(lineoffset).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append(" [coloffset - ").append(coloffset).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append("\nSource: \n").append(source); //$NON-NLS-1$
		return buffer.toString();
	}
}
