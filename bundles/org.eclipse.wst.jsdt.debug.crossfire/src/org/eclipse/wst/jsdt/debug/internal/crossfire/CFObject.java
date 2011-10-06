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
package org.eclipse.wst.jsdt.debug.internal.crossfire;

/**
 * Place-holding class for objects
 * 
 * @since 1.0
 */
public class CFObject {

	private String type = null;
	private String name = null;
	private Number handle = null;
	
	public CFObject(String name, String type, Number handle) {
		this.name = name;
		this.type = type;
		this.handle = handle;
	}
	
	/**
	 * @return the handle
	 */
	public Number getHandle() {
		return this.handle;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
}
