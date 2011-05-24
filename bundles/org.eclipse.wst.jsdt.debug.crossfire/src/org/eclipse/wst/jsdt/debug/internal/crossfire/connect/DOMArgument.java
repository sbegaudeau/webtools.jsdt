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
package org.eclipse.wst.jsdt.debug.internal.crossfire.connect;

import org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.BooleanArgument;

/**
 * An {@link Argument} to enable DOM tooling support in the crossfire server
 * 
 * @since 1.0
 */
public class DOMArgument implements BooleanArgument {

	public static final String DOM = "dom"; //$NON-NLS-1$
	
	private boolean fValue = true;
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#description()
	 */
	public String description() {
		return Messages.dom_arg_description;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#label()
	 */
	public String label() {
		return Messages.dom_arg_label;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#mustSpecify()
	 */
	public boolean mustSpecify() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#name()
	 */
	public String name() {
		return DOM;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#setValue(java.lang.String)
	 */
	public void setValue(String value) {
		fValue = Boolean.valueOf(value).booleanValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#value()
	 */
	public String value() {
		return Boolean.toString(fValue);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.BooleanArgument#booleanValue()
	 */
	public boolean booleanValue() {
		return fValue;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.BooleanArgument#isValid(java.lang.String)
	 */
	public boolean isValid(String value) {
		return value != null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.BooleanArgument#setValue(boolean)
	 */
	public void setValue(boolean booleanValue) {
		fValue = booleanValue;
	}
}