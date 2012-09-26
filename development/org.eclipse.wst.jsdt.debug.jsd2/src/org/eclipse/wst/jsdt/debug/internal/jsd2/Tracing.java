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
package org.eclipse.wst.jsdt.debug.internal.jsd2;

import org.eclipse.wst.jsdt.debug.internal.jsd2.transport.JSON;

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
		String s = string.replaceAll(JSON.LINE_FEED, PRINTABLE_LINE_FEED);
		s = s.replaceAll("\r", "\\\\r");  //$NON-NLS-1$//$NON-NLS-2$
		s = s.replaceAll("\n", "\\\\n");  //$NON-NLS-1$//$NON-NLS-2$
		System.out.println("[CROSSFIRE]" + s); //$NON-NLS-1$
	}
}
