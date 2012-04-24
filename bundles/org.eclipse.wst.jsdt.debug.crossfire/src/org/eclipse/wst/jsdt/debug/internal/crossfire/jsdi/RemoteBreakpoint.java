/*******************************************************************************
 * Copyright (c) 2011, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Attributes;


/**
 * This class holds the description of a breakpoint from the crossfire server
 * 
 * @since 1.0
 */
public class RemoteBreakpoint implements Comparable {

	public static final String TYPE_LINE = "line"; //$NON-NLS-1$
	public static final String TYPE_HTML_ATTRIBUTE_CHANGE = "html_attribute_change"; //$NON-NLS-1$
	public static final String TYPE_HTML_CHILD_CHANGE = "html_child_change"; //$NON-NLS-1$
	public static final String TYPE_HTML_REMOVE = "html_remove"; //$NON-NLS-1$
	public static final String TYPE_HTML_TEXT = "html_text"; //$NON-NLS-1$
	public static final String TYPE_HTML_UNKNOWN = "html_unknown_type"; //$NON-NLS-1$
	
	CFVirtualMachine vm = null;
	Number handle = null;
	String type = null;
	Map location = null;
	Map attributes = null;
	
	/**
	 * Constructor
	 * @param vm
	 * @param handle
	 * @param location
	 * @param attributes
	 * @param type
	 */
	public RemoteBreakpoint(CFVirtualMachine vm, Number handle, Map location, Map attributes, String type) {
		this.vm = vm;
		this.handle = handle;
		this.location = location;
		this.attributes = attributes;
		this.type = type;
	}
	
	/**
	 * @return the handle
	 */
	public Number getHandle() {
		return handle;
	}
	
	/**
	 * @return the URL
	 */
	public String getUrl() {
		if(this.location != null) {
			return (String) this.location.get(Attributes.URL);
		}
		return null;
	}
	
	/**
	 * @return the line
	 */
	public int getLine() {
		if(this.location != null) {
			Number line = (Number) this.location.get(Attributes.LINE);
			if(line != null) {
				return line.intValue();
			}
		}
		return -1;
	}
	
	/**
	 * @return the condition
	 */
	public String getCondition() {
		if(this.attributes != null) {
			return (String) this.attributes.get(Attributes.CONDITION);
		}
		return null;
	}
	
	/**
	 * @param condition the condition to set
	 */
	public void setCondition(String condition) {
		if(this.attributes == null) {
			this.attributes = new HashMap();
		}
		if(condition != null) {
			this.attributes.put(Attributes.CONDITION, condition);
		}
		else {
			this.attributes.remove(Attributes.CONDITION);
		}
	}
	
	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		if(this.attributes != null) {
			Boolean bool = (Boolean) this.attributes.get(Attributes.ENABLED);
			if(bool != null) {
				return bool.booleanValue();
			}
		}
		return false;
	}
	
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		if(this.attributes == null) {
			this.attributes = new HashMap();
		}
		this.attributes.put(Attributes.ENABLED, Boolean.valueOf(enabled));
	}
	
	/**
	 * Returns the type of the breakpoint
	 * @return the type
	 * @see RemoteBreakpoint for type names
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Returns if this breakpoint is a line breakpoint
	 * @return <code>true</code> if the type of the breakpoint is <code>line</code>
	 */
	public boolean isLineBreakpoint() {
		return TYPE_LINE.equals(this.type);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if(o instanceof RemoteBreakpoint) {
			RemoteBreakpoint bp = (RemoteBreakpoint) o;
			return handle.equals(bp.handle) && mapsEqual(location, bp.location) && mapsEqual(attributes, bp.attributes) && type.equals(bp.type);
		}
		return false;
	}
	
	/**
	 * Returns if the given maps are equal. 
	 * <br><br>
	 * They are considered equal iff:
	 * <ul>
	 * <li>both maps are <code>null</code></li>
	 * <li>the maps have the same number of values and the those values are equal using the default {@link #equals(Object)} method</li>
	 * </ul>
	 * 
	 * @param m1
	 * @param m2
	 * @return <code>true</code> if the maps are equal <code>false</code> otherwise
	 */
	boolean mapsEqual(Map m1, Map m2) {
		if(m1 == null && m2 == null) {
			return true;
		}
		if(m1 == null ^ m2 == null) {
			return false;
		}
		if(m1.size() != m2.size()) {
			return false;
		}
		Entry entry = null;
		for (Iterator i = m1.entrySet().iterator(); i.hasNext();) {
			entry = (Entry) i.next();
			Object val = m2.get(entry.getKey());
			if(val == null) {
				return false;
			}
			if(!val.equals(entry.getValue())) {
				return false;
			}
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return handle.hashCode() + mapHashCode(location) + mapHashCode(attributes) + type.hashCode();
	}
	
	/**
	 * Computes the hash code for the given map
	 * @param m
	 * @return the hash code to use for the given map
	 */
	int mapHashCode(Map m) {
		int hashcode = 0;
		for (Iterator i = m.values().iterator(); i.hasNext();) {
			hashcode += i.next().hashCode();
		}
		return hashcode;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		if(o instanceof RemoteBreakpoint) {
			RemoteBreakpoint bp = (RemoteBreakpoint) o;
			return this.type.compareTo(bp.type);
		}
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buff = new StringBuffer("RemoteBreakpoint\n"); //$NON-NLS-1$
		buff.append("\t[handle: ").append(handle.toString()).append("]\n"); //$NON-NLS-1$ //$NON-NLS-2$
		return super.toString();
	}
	
	/**
	 * Helper method to get the {@link Attributes#ENABLED} value from the breakpoint JSON
	 * 
	 * @param json the JSON for the breakpoint
	 * @return <code>true</code> if the attribute is found and <code>true</code>, <code>false</code> otherwise
	 */
	public static boolean getEnabled(Map json) {
		Object val = json.get(Attributes.ATTRIBUTES);
		if(val instanceof Map) {
			Map map = (Map) val;
			val = map.get(Attributes.ENABLED);
			if(val instanceof Boolean) {
				return ((Boolean)val).booleanValue();
			}
		}
		return false;
	}
	
	/**
	 * Helper method to get the condition from the breakpoint JSON
	 * 
	 * @param json the JSON for the breakpoint
	 * @return the condition or <code>null</code>
	 */
	public static final String getCondition(Map json) {
		Object val = json.get(Attributes.ATTRIBUTES);
		if(val instanceof Map) {
			Map map = (Map) val;
			String condition = (String)map.get(Attributes.CONDITION);
			if(condition != null && condition.trim().length() > 0) {
				return condition;
			}
		}
		return null;
	}
	
	/**
	 * Helper method to get the 'set' value from the breakpoint JSON
	 * 
	 * @param json the JSON for the breakpoint
	 * @return <code>true</code> if the attribute is present and <code>true</code>, <code>false</code> otherwise
	 */
	public static final boolean isSet(Map json) {
		Object val = json.get(Attributes.SET);
		if(val instanceof Boolean) {
			return ((Boolean)val).booleanValue();
		}
		return false;
	}
}
