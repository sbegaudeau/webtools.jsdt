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
package org.eclipse.wst.jsdt.core.tests.model;

import org.eclipse.wst.jsdt.core.IJavaProject;
import org.eclipse.wst.jsdt.core.compiler.ValidationParticipant;
import org.eclipse.wst.jsdt.core.compiler.ReconcileContext;

public class TestvalidationParticipant extends ValidationParticipant {
	
	public static ValidationParticipant PARTICIPANT;

	public boolean isActive(IJavaProject project) {
		return PARTICIPANT != null && PARTICIPANT.isActive(project);
	}
	
	public void reconcile(ReconcileContext context) {
		PARTICIPANT.reconcile(context);
	}
}
