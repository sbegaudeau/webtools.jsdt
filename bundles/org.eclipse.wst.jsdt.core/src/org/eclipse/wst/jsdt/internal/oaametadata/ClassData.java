/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.oaametadata;

import java.util.ArrayList;

public class ClassData extends DocumentedElement{

	public Ancestor [] ancestors;
	public String name;
	public String type;
	public String superclass;
	public String visibility; 

	public Method [] constructors;
	public Event [] events;
	public Method [] methods;
	public Property [] fields;
	
	public Mix [] mixes;
	
	
	public Property [] getFields()
	{
		ArrayList list = new ArrayList();
		if (this.fields!=null)
			for (int i = 0; i < this.fields.length; i++) {
				if (this.fields[i].isField)
					list.add(this.fields[i]);
		}
		return (Property [] )list.toArray(new Property[list.size()]);
	}
	
	public Property [] getProperties()
	{
		ArrayList list = new ArrayList();
		if (this.fields!=null)
			for (int i = 0; i < this.fields.length; i++) {
				if (!this.fields[i].isField)
					list.add(this.fields[i]);
		}
		return (Property [] )list.toArray(new Property[list.size()]);
	}
	

	public Method getMethod(String name)
	{
		if (this.methods!=null)
			for (int i = 0; i < this.methods.length; i++) {
				if (this.methods[i].name.equals(name))
					return this.methods[i];
				
			}
		return null;
	}
}
