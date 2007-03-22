/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.infer;

import java.util.HashMap;
import java.util.Map;

public class InferOptions {
	
	public static final String OPTION_UseAssignments = "org.eclipse.wst.jsdt.core.infer.useAssignments"; //$NON-NLS-1$
	public static final String OPTION_UseInitMethod = "org.eclipse.wst.jsdt.core.infer.useInitMethod"; //$NON-NLS-1$

	
	
	// tags used to recognize tasks in comments
	public char[][] systemClassMethod = null;

	public boolean useAssignments=true;
	
	public boolean useInitMethod;
	public String engineClass;
	
	
	/** 
	 * Initializing the compiler options with defaults
	 */
	public InferOptions(){
		// use default options
		setDefaultOptions();
	}

	/** 
	 * Initializing the compiler options with external settings
	 * @param settings
	 */
	public InferOptions(Map settings){

		if (settings == null) return;
		set(settings);		
	}
	
	public void setDefaultOptions()
	{
		this.useAssignments=true;
		this.useInitMethod=true;
	}
 
	public Map getMap() {
		Map optionsMap = new HashMap(30);
		optionsMap.put(OPTION_UseAssignments, this.useAssignments ? "true":"false"); 
		optionsMap.put(OPTION_UseInitMethod, this.useInitMethod ? "true":"false"); 
		return optionsMap;		
	}
	
 
	public void set(Map optionsMap) {

		Object optionValue;
		if ((optionValue = optionsMap.get(OPTION_UseAssignments)) != null) {
			this.useAssignments="true".equals(optionValue) ;
		}
		if ((optionValue = optionsMap.get(OPTION_UseInitMethod)) != null) {
			this.useInitMethod="true".equals(optionValue) ;
		}
	}

	public String toString() {
	
		StringBuffer buf = new StringBuffer("InferOptions:"); //$NON-NLS-1$
		buf.append("\n\t- use assignments: ").append( this.useAssignments ? "ON" : " OFF"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		buf.append("\n\t- use initialization method : ").append( this.useInitMethod ? "ON" : " OFF"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return buf.toString();
	}

	
	public InferEngine createEngine()
	{
		if (engineClass!=null)
		{
			try {
				InferEngine engine= (InferEngine) Class.forName(engineClass).newInstance();
				engine.inferOptions=this;
				return engine;
			} catch (Exception ex)
			{
				ex.printStackTrace();
				//TODO: implement something
			}
		}
		return new InferEngine(this);
	}
}
