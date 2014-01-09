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

import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.sourcelookup.AbstractSourceLookupParticipant;
import org.eclipse.wst.jsdt.debug.internal.core.launching.SourceLookup;

/**
 * Rhino specific source lookup participant
 * 
 * @since 1.0
 */
public class RhinoSourceLookupParticipant extends AbstractSourceLookupParticipant {

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.sourcelookup.ISourceLookupParticipant#getSourceName(java.lang.Object)
	 */
	public String getSourceName(Object object) throws CoreException {
		return SourceLookup.getSourceName(object);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.sourcelookup.AbstractSourceLookupParticipant#findSourceElements(java.lang.Object)
	 */
	public Object[] findSourceElements(Object object) throws CoreException {
		Object[] source = super.findSourceElements(object);
		if(source.length < 1) {
			URI uri = SourceLookup.getSourceURI(object);
			if(uri != null) {
				IFile file = SourceLookup.getExternalSource(uri, object);
				if(file != null) {
					return new Object[] {file};
				}
			} 
		}
		else {
			return source;
		}
		return EMPTY;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.sourcelookup.AbstractSourceLookupParticipant#isFindDuplicates()
	 */
	public boolean isFindDuplicates() {
		return false;
	}
}