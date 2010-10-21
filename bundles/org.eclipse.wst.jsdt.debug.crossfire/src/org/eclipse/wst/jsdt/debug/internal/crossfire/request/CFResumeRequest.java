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
package org.eclipse.wst.jsdt.debug.internal.crossfire.request;

import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ResumeRequest;

/**
 * Crossfire implementation of {@link ResumeRequest}
 * 
 * @since 1.0
 */
public class CFResumeRequest extends CFThreadEventRequest implements ResumeRequest {

	/**
	 * Constructor
	 * @param vm
	 * @param thread
	 */
	public CFResumeRequest(VirtualMachine vm, ThreadReference thread) {
		super(vm);
		setThread(thread);
	}
}
