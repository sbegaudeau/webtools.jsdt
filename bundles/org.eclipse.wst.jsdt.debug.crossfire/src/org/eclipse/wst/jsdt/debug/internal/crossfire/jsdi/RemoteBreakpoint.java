/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi;


/**
 * This class holds the description of a breakpoint from the crossfire server
 * 
 * @since 1.0
 */
public class RemoteBreakpoint implements Comparable {

	CFVirtualMachine vm = null;
	Number id = null;
	String url = null;
	boolean enabled = false;
	String condition = null;
	int line = -1;
	String kind = null;
	
	/**
	 * Constructor
	 * @param vm
	 * @param id
	 * @param url
	 * @param line
	 * @param enabled
	 * @param condition
	 * @param kind
	 */
	public RemoteBreakpoint(CFVirtualMachine vm, Number id, String url, int line, boolean enabled, String condition, String kind) {
		this.vm = vm;
		this.id = id;
		this.url = url;
		this.enabled = enabled;
		this.condition = condition;
		this.line = line;
		this.kind = kind;
	}
	
	/**
	 * @return the id
	 */
	public Number getId() {
		return id;
	}
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * @return the line
	 */
	public int getLine() {
		return line;
	}
	
	/**
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}
	
	/**
	 * @param condition the condition to set
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * @return the kind
	 */
	public String getKind() {
		return kind;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if(o instanceof RemoteBreakpoint) {
			RemoteBreakpoint bp = (RemoteBreakpoint) o;
			return id.equals(bp.id) && url.equals(bp.url) && line == bp.line && kind.equals(bp.kind);
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return id.hashCode() + url.hashCode() + line + kind.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		if(o instanceof RemoteBreakpoint) {
			RemoteBreakpoint bp = (RemoteBreakpoint) o;
			return this.url.compareTo(bp.url);
		}
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buff = new StringBuffer("RemoteBreakpoint\n"); //$NON-NLS-1$
		buff.append("\t[handle: ").append(id.toString()).append("]\n"); //$NON-NLS-1$ //$NON-NLS-2$
		buff.append("\t[url: ").append(url).append("]\n"); //$NON-NLS-1$ //$NON-NLS-2$
		buff.append("\t[line: ").append(line).append("]\n"); //$NON-NLS-1$ //$NON-NLS-2$
		buff.append("\t[enabled: ").append(enabled).append("]\n"); //$NON-NLS-1$ //$NON-NLS-2$
		buff.append("\t[condition: ").append(condition).append("]\n"); //$NON-NLS-1$ //$NON-NLS-2$
		return super.toString();
	}
}
