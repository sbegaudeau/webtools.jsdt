/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.internal.corext.refactoring.generics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints2.CastVariable2;
import org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints2.CollectionElementVariable2;

public class InferTypeArgumentsUpdate {
	public static class CuUpdate {
		private List fDeclarations= new ArrayList();
		private List fCastsToRemove= new ArrayList();

		public List/*<CollectionElementVariable2>*/ getDeclarations() {
			return fDeclarations;
		}
		
		public List/*<CastVariable2>*/ getCastsToRemove() {
			return fCastsToRemove;
		}
	}
	
	private HashMap/*<IJavaScriptUnit, CuUpdate>*/ fUpdates= new HashMap();
	
	public HashMap/*<IJavaScriptUnit, CuUpdate>*/ getUpdates() {
		return fUpdates;
	}
	
	public void addDeclaration(CollectionElementVariable2 elementCv) {
		IJavaScriptUnit cu= elementCv.getCompilationUnit();
		if (cu == null)
			return;
		CuUpdate update= getUpdate(cu);
		update.fDeclarations.add(elementCv);
	}

	public void addCastToRemove(CastVariable2 castCv) {
		IJavaScriptUnit cu= castCv.getCompilationUnit();
		CuUpdate update= getUpdate(cu);
		update.fCastsToRemove.add(castCv);
	}

	private CuUpdate getUpdate(IJavaScriptUnit cu) {
		Assert.isNotNull(cu);
		Object obj= fUpdates.get(cu);
		CuUpdate update;
		if (obj == null) {
			update= new CuUpdate();
			fUpdates.put(cu, update);
		} else {
			update= (CuUpdate) obj;
		}
		return update;
	}
	
}
