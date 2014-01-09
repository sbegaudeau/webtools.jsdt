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
import org.eclipse.wst.jsdt.debug.core.jsdi.request.StepRequest;
import org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi.CFThreadReference;

/**
 * Default implementation of {@link StepRequest} for Crossfire
 * 
 * @since 1.0
 */
public class CFStepRequest extends CFThreadEventRequest implements StepRequest {

	private int stepkind = 0;
	
	/**
	 * Constructor
	 * @param vm
	 */
	public CFStepRequest(VirtualMachine vm, ThreadReference thread, int step) {
		super(vm);
		setThread(thread);
		this.stepkind = step;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.StepRequest#step()
	 */
	public int step() {
		return stepkind;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.crossfire.request.CFEventRequest#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		((CFThreadReference)thread()).setStep((enabled ? stepkind : -1));
	}
}
