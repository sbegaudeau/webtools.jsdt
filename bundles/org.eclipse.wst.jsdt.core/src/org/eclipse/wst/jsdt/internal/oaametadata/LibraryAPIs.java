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

public class LibraryAPIs {
	public ClassData[] classes;
	
	public Method [] globalMethods;
	public Property[] globalVars;
	
	public String version;
	public String language;
	public String getterPattern;
	public String setterPattern;
	public String description;
	public String[] authors;
	public String fileName;
	
	
	public Property getGlobalVar(String name) {
		if (this.globalVars!=null)
			for (int i = 0; i < this.globalVars.length; i++) {
				if (name.equals(this.globalVars[i].name))
					return this.globalVars[i];
			}
			return null;
	}
	public ClassData getClass(String name) {
		if (this.classes!=null)
			for (int i = 0; i < this.classes.length; i++) {
				if (name.equals(this.classes[i].name) || name.equals(this.classes[i].type))
					return this.classes[i];
			}
			return null;
	}
	public Method getGlobalMethod(String name) {
		if (this.globalMethods!=null)
			for (int i = 0; i < this.globalMethods.length; i++) {
				if (name.equals(this.globalMethods[i].name))
					return this.globalMethods[i];
			}
			return null;
	}
}
