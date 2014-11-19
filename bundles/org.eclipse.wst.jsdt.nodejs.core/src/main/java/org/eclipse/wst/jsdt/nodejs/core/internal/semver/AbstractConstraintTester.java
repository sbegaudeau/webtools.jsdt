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
 * This class will be used to validate whether or not a version validates a constraint.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public abstract class AbstractConstraintTester {
	/**
	 * The reference version.
	 */
	protected Version version;

	/**
	 * Indicates if the tested version validates the constraint or not.
	 *
	 * @param testedVersion
	 *            The tested version
	 * @return <code>true</code> if the constraint is validated, <code>false</code> otherwise
	 */
	public abstract boolean apply(Version testedVersion);

	/**
	 * Sets the version.
	 *
	 * @param version
	 *            The version to set
	 */
	public void setVersion(Version version) {
		this.version = version;
	}
}
