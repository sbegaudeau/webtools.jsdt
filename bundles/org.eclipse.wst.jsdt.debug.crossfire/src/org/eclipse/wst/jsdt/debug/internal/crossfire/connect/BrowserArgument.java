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
package org.eclipse.wst.jsdt.debug.internal.crossfire.connect;

import org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.BooleanArgument;

/**
 * Option to automatically launch the browser and connect to it
 * 
 * @since 1.0
 */
public class BrowserArgument implements BooleanArgument {

	/**
	 * name of the argument
	 */
	public static final String BROWSER = "browser"; //$NON-NLS-1$
	
	private boolean doit = false; 
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#description()
	 */
	public String description() {
		return Messages.auto_attach_desc;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#label()
	 */
	public String label() {
		return Messages.auto_attach_label;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#mustSpecify()
	 */
	public boolean mustSpecify() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#name()
	 */
	public String name() {
		return BROWSER;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#setValue(java.lang.String)
	 */
	public void setValue(String value) {
		doit = Boolean.valueOf(value).booleanValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#value()
	 */
	public String value() {
		return Boolean.toString(doit);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.BooleanArgument#booleanValue()
	 */
	public boolean booleanValue() {
		return doit;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.BooleanArgument#isValid(java.lang.String)
	 */
	public boolean isValid(String value) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.BooleanArgument#setValue(boolean)
	 */
	public void setValue(boolean booleanValue) {
		doit = booleanValue;
	}
}
