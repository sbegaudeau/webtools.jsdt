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
package org.eclipse.wst.jsdt.nodejs.core.api.semver;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.wst.jsdt.nodejs.core.internal.semver.ConstraintKind;
import org.eclipse.wst.jsdt.nodejs.core.internal.semver.VersionedConstraint;

/**
 * A range is created from an expression describing a set of versions to use.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class Range {

	/**
	 * The array of all the constraints to consider for the creation of a range. /!\ Warning the order of the
	 * values in this array matters!!! /!\.
	 */
	private static final ConstraintKind[] constraintKinds = new ConstraintKind[] {
			ConstraintKind.GREATER_THAN_OR_EQUALS_TO, ConstraintKind.GREATER_THAN, ConstraintKind.EQUALS_TO,
			ConstraintKind.LOWER_THAN_OR_EQUALS_TO, ConstraintKind.LOWER_THAN };

	/**
	 * The list of constraints of the range.
	 */
	private List<VersionedConstraint> constraints = new ArrayList<VersionedConstraint>();

	/**
	 * Adds the versioned constraint to the range.
	 *
	 * @param versionedConstraint
	 *            The versioned constraint
	 */
	private void addVersionedConstraint(VersionedConstraint versionedConstraint) {
		this.constraints.add(versionedConstraint);
	}

	/**
	 * Returns the constraints.
	 *
	 * @return The constraints
	 */
	public List<VersionedConstraint> getConstraints() {
		return this.constraints;
	}

	/**
	 * Create a range from the given expression.
	 *
	 * @param rangeExpression
	 * @return The range
	 */
	public static Range fromString(String rangeExpression) {
		Range range = new Range();

		StringTokenizer tokenizer = new StringTokenizer(rangeExpression);
		while (tokenizer.hasMoreTokens()) {
			String nextToken = tokenizer.nextToken();
			nextToken = nextToken.trim();

			for (ConstraintKind constraintKind : Range.constraintKinds) {
				if (nextToken.startsWith(constraintKind.getLabel())) {
					Version version = Version.fromString(nextToken.substring(constraintKind.getLabel()
							.length()));
					range.addVersionedConstraint(new VersionedConstraint(version, constraintKind));
					break;
				}
			}
		}

		return range;
	}

}
