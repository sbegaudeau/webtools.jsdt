/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.jsd2.connect;

import org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.IntegerArgument;

/**
 * Argument used to specify a timeout (in ms)
 * 
 * @since 1.0
 */
public class TimeoutArgument implements IntegerArgument {
	
	/**
	 * Argument to specify a timeout
	 */
	public static final String TIMEOUT = "timeout"; //$NON-NLS-1$
	/**
	 * default connecting timeout
	 */
	public static final Integer CONNECT_TIMEOUT = new Integer(30000);
	
	/**
	 * The timeout
	 */
	private int timeout = 0;
	
	/**
	 * Constructor
	 */
	public TimeoutArgument() {
		setValue(CONNECT_TIMEOUT.intValue());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#description()
	 */
	public String description() {
		return Messages.timeout_desc;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#label()
	 */
	public String label() {
		return Messages.timeout;
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
		return TIMEOUT;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#setValue(java.lang.String)
	 */
	public void setValue(String value) {
		try {
			timeout = Integer.parseInt(value);
		}
		catch(NumberFormatException nfe) {
			//do nothing the new value will not be set
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#value()
	 */
	public String value() {
		return Integer.toString(timeout);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.IntegerArgument#intValue()
	 */
	public int intValue() {
		return timeout;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.IntegerArgument#isValid(int)
	 */
	public boolean isValid(int intValue) {
		return intValue > 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.IntegerArgument#isValid(java.lang.String)
	 */
	public boolean isValid(String value) {
		try {
			return Integer.parseInt(value) > 0;
		}
		catch(NumberFormatException bfe) {
			//do nothing, just not valid
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.IntegerArgument#max()
	 */
	public int max() {
		return Integer.MAX_VALUE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.IntegerArgument#min()
	 */
	public int min() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.IntegerArgument#setValue(int)
	 */
	public void setValue(int intValue) {
		timeout = intValue;
	}

}
