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
package org.eclipse.wst.jsdt.debug.internal.chrome.connect;

import org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.StringArgument;
import org.eclipse.wst.jsdt.debug.transport.Constants;

/**
 * Implementation of a string argument that describes the host argument
 * 
 * @since 1.0
 */
public class HostArgument implements StringArgument {

	private static final long serialVersionUID = 3057403815318177030L;
	private String host;

	/**
	 * Host attribute name
	 */
	public static final String HOST = "host"; //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param host
	 */
	public HostArgument(String host) {
		setValue(host);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#description()
	 */
	public String description() {
		return Messages.host_arg_desc;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.StringArgument#isValid(java.lang.String)
	 */
	public boolean isValid(String value) {
		return value != null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#label()
	 */
	public String label() {
		return Messages.host_arg_label;
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
		return HOST;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#setValue(java.lang.String)
	 */
	public void setValue(String host) {
		if(host == null) {
			this.host = Constants.LOCALHOST;
		}
		else {
			if (!isValid(host)) {
				throw new IllegalArgumentException();
			}
			this.host = host;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument#value()
	 */
	public String value() {
		return host;
	}

	/**
	 * Returns if the given host is <code>localhost</code> or <code>127.0.0.1</code>
	 * @param host
	 * @return true if the given host it localhost (127.0.0.1) false otherwise
	 */
	public static boolean isLocalhost(String host) {
		return host.equals(Constants.LOCALHOST) || host.equals(Constants.LOCALHOST_IP);
	}
}