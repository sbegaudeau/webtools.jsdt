/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.rhino.ui.source;

import org.eclipse.debug.core.sourcelookup.AbstractSourceLookupDirector;
import org.eclipse.debug.core.sourcelookup.ISourceLookupParticipant;

/**
 * Rhino specific source lookup director
 * 
 * @since 1.0
 */
public class RhinoSourceDirector extends AbstractSourceLookupDirector {
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.sourcelookup.ISourceLookupDirector#initializeParticipants()
	 */
	public void initializeParticipants() {
		addParticipants(new ISourceLookupParticipant[] {new RhinoSourceLookupParticipant()});
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.sourcelookup.AbstractSourceLookupDirector#isFindDuplicates()
	 */
	public boolean isFindDuplicates() {
		return true;
	}
}
