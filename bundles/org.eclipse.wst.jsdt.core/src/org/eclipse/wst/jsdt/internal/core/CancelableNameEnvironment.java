/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.wst.jsdt.core.JavaModelException;
import org.eclipse.wst.jsdt.core.WorkingCopyOwner;
import org.eclipse.wst.jsdt.internal.codeassist.ISearchRequestor;
import org.eclipse.wst.jsdt.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.wst.jsdt.internal.compiler.impl.ITypeRequestor;
import org.eclipse.wst.jsdt.internal.compiler.problem.AbortCompilation;


public class CancelableNameEnvironment extends SearchableEnvironment {
	public IProgressMonitor monitor;

	public CancelableNameEnvironment(JavaProject project, WorkingCopyOwner owner, IProgressMonitor monitor) throws JavaModelException {
		super(project, owner);
		this.monitor = monitor;
	}

	private void checkCanceled() {
		if (this.monitor != null && this.monitor.isCanceled()) {
			if (NameLookup.VERBOSE)
				System.out.println(Thread.currentThread() + " CANCELLING LOOKUP "); //$NON-NLS-1$
			throw new AbortCompilation(true/*silent*/, new OperationCanceledException());
		}
	}

	public void findPackages(char[] prefix, ISearchRequestor requestor) {
		checkCanceled();
		super.findPackages(prefix, requestor);
	}

	public NameEnvironmentAnswer findType(char[] name, char[][] packageName) {
		checkCanceled();
		return super.findType(name, packageName);
	}

	public NameEnvironmentAnswer findBinding(char[] typeName, char[][] packageName, int type, ITypeRequestor requestor) {
		checkCanceled();
		return super.findBinding(typeName, packageName, type, requestor);
	}
	
	public NameEnvironmentAnswer findType(char[][] compoundTypeName,  ITypeRequestor requestor) {
		checkCanceled();
		return super.findType(compoundTypeName,requestor);
	}

	public void findTypes(char[] prefix, boolean findMembers, boolean camelCaseMatch, int searchFor, ISearchRequestor storage) {
		checkCanceled();
		super.findTypes(prefix, findMembers, camelCaseMatch, searchFor, storage);
	}
}
