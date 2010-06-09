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
package org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Path;
import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.internal.crossfire.CrossFirePlugin;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Attributes;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Commands;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.JSON;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Request;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Response;

/**
 * Default implementation of {@link ScriptReference} for Crossfire
 * 
 * @since 1.0
 */
public class CFScriptReference extends CFMirror implements ScriptReference {
	
	private String context_id = null;
	private String context_href = null;
	private String id = null;
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
		//try "href" first -> CF 0.2 support
		this.id = (String) json.get(Attributes.HREF);
		if(id == null) {
			//try "data" next -> CF 0.1a3 support
			this.id = (String) json.get(Attributes.DATA);
		}
		if(this.id == null) {
			//try "id" last -> CF 0.1 support
			this.id = (String) json.get(Attributes.ID);
		}
		this.context_href = (String) json.get(Attributes.CONTEXT_HREF);
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
	 * The id of the script
	 * @return the id
	 */
	public String id() {
		return id;
	}
	
	/**
	 * The HTTP context of the script, if any.
	 * <br><br>
	 * This method can return <code>null</code>
	 * 
	 * @return the HTTP context of the script or null
	 */
	public String hrefContext() {
		return context_href;
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
			Request request = new Request(Commands.SOURCE, context_id);
			request.addAdditionalParam(Attributes.ID, id);
			Response response = crossfire().sendRequest(request);
			if(response.isSuccess()) {
				//TODO ability to ask for only the source for one script and not all of them?
				ArrayList scripts = (ArrayList) response.getBody().get(Commands.SCRIPTS);
				for (Iterator iter = scripts.iterator(); iter.hasNext();) {
					Map json = (Map) ((Map) iter.next()).get(Attributes.SCRIPT);
					if(json.get(Attributes.ID).equals(id)) {
						initializeScript(json);
					}
				}
			}
			if(TRACE) {
				System.out.println("ScriptReference [failed source request]: "+JSON.serialize(request)); //$NON-NLS-1$
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
				sourceuri = URI.create(id);
			}
			catch(IllegalArgumentException iae) {
				try {
					sourceuri = CrossFirePlugin.fileURI(new Path(id));
				} catch (URISyntaxException e) {
					CrossFirePlugin.log(e);
				}
			}
		}
		return sourceuri;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ScriptReference: [context_id - ").append(context_id).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append(" [context_href - ").append(context_href).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append(" [id - ").append(id).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append(" [srclength - ").append(srclength).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append(" [linecount - ").append(linecount).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append(" [lineoffset - ").append(lineoffset).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append(" [coloffset - ").append(coloffset).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append("\nSource: \n").append(source); //$NON-NLS-1$
		return super.toString();
	}
}
