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
 * The list of the constraint kind that can be applied to a versioned constraint.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public enum ConstraintKind {
	/**
	 * >.
	 */
	GREATER_THAN(new AbstractConstraintTester() {

		@Override
		public boolean apply(Version testedVersion) {
			return testedVersion.compareTo(this.version) > 0;
		}

	}, ">"), //$NON-NLS-1$

	/**
	 * >=.
	 */
	GREATER_THAN_OR_EQUALS_TO(new AbstractConstraintTester() {

		@Override
		public boolean apply(Version testedVersion) {
			return testedVersion.compareTo(this.version) >= 0;
		}

	}, ">="), //$NON-NLS-1$

	/**
	 * =.
	 */
	EQUALS_TO(new AbstractConstraintTester() {

		@Override
		public boolean apply(Version testedVersion) {
			return testedVersion.compareTo(this.version) == 0;
		}

	}, "="), //$NON-NLS-1$

	/**
	 * <=.
	 */
	LOWER_THAN_OR_EQUALS_TO(new AbstractConstraintTester() {

		@Override
		public boolean apply(Version testedVersion) {
			return testedVersion.compareTo(this.version) <= 0;
		}

	}, "<="), //$NON-NLS-1$

	/**
	 * <.
	 */
	LOWER_THAN(new AbstractConstraintTester() {

		@Override
		public boolean apply(Version testedVersion) {
			return testedVersion.compareTo(this.version) < 0;
		}

	}, "<"), //$NON-NLS-1$

	/**
	 *
	 */
	ANY(new AbstractConstraintTester() {

		@Override
		public boolean apply(Version testedVersion) {
			return true;
		}

	}, "*"); //$NON-NLS-1$

	/**
	 * The label of the constraint.
	 */
	private String label;

	/**
	 * The tester of the constraint.
	 */
	private AbstractConstraintTester constraintTester;

	/**
	 * The constructor.
	 *
	 * @param label
	 *            The label of the constraint
	 * @param constraintTester
	 *            The constraint tester
	 */
	ConstraintKind(AbstractConstraintTester constraintTester, String label) {
		this.label = label;
		this.constraintTester = constraintTester;
	}

	/**
	 * Returns the label.
	 *
	 * @return The label
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Returns the constraintTester.
	 *
	 * @return The constraintTester
	 */
	public AbstractConstraintTester getConstraintTester() {
		return this.constraintTester;
	}

}
