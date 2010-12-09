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
package org.eclipse.wst.jsdt.debug.internal.chrome.jsdi;

import java.util.List;

import org.eclipse.wst.jsdt.debug.core.jsdi.StackFrame;
import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;

/**
 *
 */
public class ThreadImpl extends MirrorImpl implements ThreadReference {

	Number id = null;
	String url = null;
	
	/**
	 * Constructor
	 * @param vm
	 * @param id
	 * @param url
	 */
	public ThreadImpl(VMImpl vm, Number id, String url) {
		super(vm);
		this.id = id;
		this.url = url;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#frameCount()
	 */
	public int frameCount() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#frame(int)
	 */
	public StackFrame frame(int index) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#frames()
	 */
	public List frames() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#interrupt()
	 */
	public void interrupt() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#resume()
	 */
	public void resume() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#suspend()
	 */
	public void suspend() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#status()
	 */
	public int status() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#isAtBreakpoint()
	 */
	public boolean isAtBreakpoint() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#isSuspended()
	 */
	public boolean isSuspended() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference#name()
	 */
	public String name() {
		return url;
	}
	
	public Number id() {
		return id;
	}
}
