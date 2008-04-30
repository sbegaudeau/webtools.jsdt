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
package org.eclipse.wst.jsdt.internal.ui.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.IPackageFragmentRoot;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchConstants;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchScope;
import org.eclipse.wst.jsdt.core.search.SearchEngine;
import org.eclipse.wst.jsdt.core.search.SearchMatch;
import org.eclipse.wst.jsdt.core.search.SearchPattern;
import org.eclipse.wst.jsdt.core.search.SearchRequestor;
import org.eclipse.wst.jsdt.internal.corext.util.JavaModelUtil;
import org.eclipse.wst.jsdt.internal.corext.util.SearchUtils;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.ui.IJavaScriptElementSearchConstants;

public class MainMethodSearchEngine{
	
	private static class MethodCollector extends SearchRequestor {
			private List fResult;
			private int fStyle;

			public MethodCollector(List result, int style) {
				Assert.isNotNull(result);
				fResult= result;
				fStyle= style;
			}

			private boolean considerExternalJars() {
				return (fStyle & IJavaScriptElementSearchConstants.CONSIDER_EXTERNAL_JARS) != 0;
			}
					
			private boolean considerBinaries() {
				return (fStyle & IJavaScriptElementSearchConstants.CONSIDER_BINARIES) != 0;
			}		
			
			/* (non-Javadoc)
			 * @see org.eclipse.wst.jsdt.core.search.SearchRequestor#acceptSearchMatch(org.eclipse.wst.jsdt.core.search.SearchMatch)
			 */
			public void acceptSearchMatch(SearchMatch match) throws CoreException {
				Object enclosingElement= match.getElement();
				if (enclosingElement instanceof IFunction) { // defensive code
					try {
						IFunction curr= (IFunction) enclosingElement;
						if (curr.isMainMethod()) {
							if (!considerExternalJars()) {
								IPackageFragmentRoot root= JavaModelUtil.getPackageFragmentRoot(curr);
								if (root == null || root.isArchive()) {
									return;
								}
							}
							if (!considerBinaries() && curr.isBinary()) {
								return;
							}
							fResult.add(curr.getDeclaringType());
						}
					} catch (JavaScriptModelException e) {
						JavaScriptPlugin.log(e.getStatus());
					}
				}
			}
	}

	/**
	 * Searches for all main methods in the given scope.
	 * Valid styles are IJavaScriptElementSearchConstants.CONSIDER_BINARIES and
	 * IJavaScriptElementSearchConstants.CONSIDER_EXTERNAL_JARS
	 */	
	public IType[] searchMainMethods(IProgressMonitor pm, IJavaScriptSearchScope scope, int style) throws CoreException {
		List typesFound= new ArrayList(200);
		
		SearchPattern pattern= SearchPattern.createPattern("main(String[]) void", //$NON-NLS-1$
				IJavaScriptSearchConstants.METHOD, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE);
		SearchRequestor requestor= new MethodCollector(typesFound, style);
		new SearchEngine().search(pattern, SearchUtils.getDefaultSearchParticipants(), scope, requestor, pm);
			
		return (IType[]) typesFound.toArray(new IType[typesFound.size()]);
	}
	
	
	
	/**
	 * Searches for all main methods in the given scope.
	 * Valid styles are IJavaScriptElementSearchConstants.CONSIDER_BINARIES and
	 * IJavaScriptElementSearchConstants.CONSIDER_EXTERNAL_JARS
	 */
	public IType[] searchMainMethods(IRunnableContext context, final IJavaScriptSearchScope scope, final int style) throws InvocationTargetException, InterruptedException  {
		int allFlags=  IJavaScriptElementSearchConstants.CONSIDER_EXTERNAL_JARS | IJavaScriptElementSearchConstants.CONSIDER_BINARIES;
		Assert.isTrue((style | allFlags) == allFlags);
		
		final IType[][] res= new IType[1][];
		
		IRunnableWithProgress runnable= new IRunnableWithProgress() {
			public void run(IProgressMonitor pm) throws InvocationTargetException {
				try {
					res[0]= searchMainMethods(pm, scope, style);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				}
			}
		};
		context.run(true, true, runnable);
		
		return res[0];
	}
			
}
