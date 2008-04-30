/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.filters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.wst.jsdt.core.Flags;
import org.eclipse.wst.jsdt.core.IMember;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;

/**
 * Filters synthetic members
 * 
 * @since 3.1
 */
public class SyntheticMembersFilter extends ViewerFilter {
	public boolean select(Viewer viewer, Object parent, Object element) {
		if (!(element instanceof IMember))
			return true;
		IMember member= (IMember)element;
		if (!(member.isBinary()))
			return true;
		try {
			return !Flags.isSynthetic(member.getFlags());
		} catch (JavaScriptModelException e) {
			return true;
		}
	}
}