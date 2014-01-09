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
package org.eclipse.wst.jsdt.debug.internal.rhino.ui.launching;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * Describes an entry in the include path for a Rhino {@link ILaunchConfiguration}
 * 
 * @since 1.1
 */
public class IncludeEntry {
	
	public static final int LOCAL_SCRIPT = 1;
	public static final int EXT_SCRIPT = 2;
	
	int kind = -1;
	String path = null;
	
	
	/**
	 * Constructor
	 * 
	 * @param kind
	 * @param path
	 * @param index
	 */
	public IncludeEntry(int kind, String path) {
		this.kind = kind;
		this.path = path;
	}
	
	/**
	 * @return the kind
	 */
	public int getKind() {
		return kind;
	}
	
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * @return the combined entry for an {@link ILaunchConfiguration} memento
	 */
	public String string() {
		return kind+path;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(obj instanceof IncludeEntry) {
			IncludeEntry entry = (IncludeEntry) obj;
			return kind == entry.kind && path.equals(entry.path);
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return kind + path.hashCode();
	}
}