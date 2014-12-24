/*******************************************************************************
 * Copyright (c) 2014 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.nodejs.core.internal.semver;

import org.eclipse.wst.jsdt.nodejs.core.api.semver.Version;

/**
 * Utility class holding a version and its constraint.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class VersionedConstraint {

	/**
	 * The version.
	 */
	private Version version;

	/**
	 * The constraint.
	 */
	private ConstraintKind constraintKind;

	/**
	 * The constructor.
	 *
	 * @param version
	 *            The version
	 * @param constraintKind
	 *            The kind of constraint
	 */
	public VersionedConstraint(Version version, ConstraintKind constraintKind) {
		this.version = version;
		this.constraintKind = constraintKind;
	}

	/**
	 * Indicates if the tested version validates the constraint or not.
	 *
	 * @param testedVersion
	 *            The tested version
	 * @return <code>true</code> if the constraint is validated, <code>false</code> otherwise
	 */
	public boolean isValid(Version testedVersion) {
		AbstractConstraintTester constraintTester = this.constraintKind.getConstraintTester();
		constraintTester.setVersion(this.version);
		return constraintTester.apply(testedVersion);
	}
}
