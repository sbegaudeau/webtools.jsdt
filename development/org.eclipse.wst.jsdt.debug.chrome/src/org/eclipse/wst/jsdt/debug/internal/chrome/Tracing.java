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
package org.eclipse.wst.jsdt.debug.internal.chrome;

import org.eclipse.wst.jsdt.debug.internal.chrome.transport.JSON;

/**
 * Helper class for common tracing functions
 * 
 * @since 1.0
 */
public class Tracing {

	public static final String PRINTABLE_LINE_FEED = "\\\\r\\\\n"; //$NON-NLS-1$
	
	/**
	 * Writes the string to system out cleaning it of control chars before printing it
	 * 
	 * @param string
	 */
	public static void writeString(String string) {
		System.out.println("[CHROME] " +string.replaceAll(JSON.LINE_FEED, PRINTABLE_LINE_FEED)); //$NON-NLS-1$
	}
	
}
