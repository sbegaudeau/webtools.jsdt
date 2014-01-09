/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.jseview.properties;

import org.eclipse.ui.views.properties.PropertyDescriptor;

import org.eclipse.wst.jsdt.core.JavaScriptModelException;

public abstract class GenericProperty<T> {
	
	private final Class<T> fType;
	private final String fName;
	private final String fId;
	private final PropertyDescriptor fDescriptor;
	
	public GenericProperty(Class<T> type, String name) {
		fType= type;
		fName= name;
		fId= "org.eclipse.wst.jsdt.jseview." + type.getSimpleName() + "." + name;
		fDescriptor= new PropertyDescriptor(fId, fName);
		fDescriptor.setAlwaysIncompatible(true);
		fDescriptor.setCategory(type.getSimpleName());
	}

	public abstract Object compute(T element) throws JavaScriptModelException;

	
	public Class<T> getType() {
		return fType;
	}
	
	
	public String getName() {
		return fName;
	}
	
	public String getId() {
		return fId;
	}
	
	public PropertyDescriptor getDescriptor() {
		return fDescriptor;
	}
	
}