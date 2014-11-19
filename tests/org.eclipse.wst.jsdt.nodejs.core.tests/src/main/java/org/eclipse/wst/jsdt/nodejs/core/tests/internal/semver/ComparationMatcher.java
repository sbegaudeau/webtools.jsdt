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
package org.eclipse.wst.jsdt.nodejs.core.tests.internal.semver;

import org.eclipse.wst.jsdt.nodejs.core.api.semver.Version;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

/**
 * This matcher will be used to compare two version numbers.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class ComparationMatcher extends BaseMatcher<Version> {

	/**
	 * This enumeration will be used to select the kind of operation to test.
	 *
	 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
	 */
	private enum Comparator {
		/**
		 * The other version number must be lower than the tested one.
		 */
		GREATER,
		/**
		 * The other version number must be lower than or equals to the tested one.
		 */
		GREATER_OR_EQUALS,
		/**
		 * The other version number must be equals to the tested one.
		 */
		EQUALS,
		/**
		 * The other version number must be greater than or equals to the tested one.
		 */
		LOWER_OR_EQUALS,
		/**
		 * The other version number must be greater than the tested one.
		 */
		LOWER;
	}

	/**
	 * The other version number.
	 */
	private Version otherVersion;

	/**
	 * The type of comparison to run.
	 */
	private Comparator comparator;

	/**
	 * The constructor.
	 *
	 * @param comparator
	 * @param otherVersion
	 */
	public ComparationMatcher(Comparator comparator, Version otherVersion) {
		this.comparator = comparator;
		this.otherVersion = otherVersion;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.hamcrest.Matcher#matches(java.lang.Object)
	 */
	@Override
	public boolean matches(Object item) {
		boolean isMatching = false;
		if (item instanceof Version) {
			Version version = (Version)item;
			switch (this.comparator) {
				case GREATER:
					isMatching = version.compareTo(this.otherVersion) > 0;
					break;
				case GREATER_OR_EQUALS:
					isMatching = version.compareTo(this.otherVersion) >= 0;
					break;
				case EQUALS:
					isMatching = version.compareTo(this.otherVersion) == 0;
					break;
				case LOWER_OR_EQUALS:
					isMatching = version.compareTo(this.otherVersion) <= 0;
					break;
				case LOWER:
					isMatching = version.compareTo(this.otherVersion) < 0;
					break;
				default:
					isMatching = false;
					break;
			}
		}
		return isMatching;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
	 */
	@Override
	public void describeTo(Description description) {
		switch (this.comparator) {
			case GREATER:
				description.appendText("greater than ").appendText(otherVersion.toString()); //$NON-NLS-1$
				break;
			case GREATER_OR_EQUALS:
				description.appendText("greater than or equals to ").appendText(otherVersion.toString()); //$NON-NLS-1$
				break;
			case EQUALS:
				description.appendText("equals to ").appendText(otherVersion.toString()); //$NON-NLS-1$
				break;
			case LOWER_OR_EQUALS:
				description.appendText("lower than or equals to ").appendText(otherVersion.toString()); //$NON-NLS-1$
				break;
			case LOWER:
				description.appendText("lower than ").appendText(otherVersion.toString()); //$NON-NLS-1$
				break;
			default:
				break;
		}
	}

	/**
	 * Returns a matcher testing if a version number is greater than another one.
	 *
	 * @param otherVersion
	 *            The other version number
	 * @return The matcher
	 */
	@Factory
	public static Matcher<Version> greaterThan(Version otherVersion) {
		return new ComparationMatcher(Comparator.GREATER, otherVersion);
	}

	/**
	 * Returns a matcher testing if a version number is greater than or equals to another one.
	 *
	 * @param otherVersion
	 *            The other version number
	 * @return The matcher
	 */
	@Factory
	public static Matcher<Version> greaterThanOrEqualsTo(Version otherVersion) {
		return new ComparationMatcher(Comparator.GREATER_OR_EQUALS, otherVersion);
	}

	/**
	 * Returns a matcher testing if a version number is equals to another one.
	 *
	 * @param otherVersion
	 *            The other version number
	 * @return The matcher
	 */
	@Factory
	public static Matcher<Version> equalTo(Version otherVersion) {
		return new ComparationMatcher(Comparator.EQUALS, otherVersion);
	}

	/**
	 * Returns a matcher testing if a version number is lower than or equals to another one.
	 *
	 * @param otherVersion
	 *            The other version number
	 * @return The matcher
	 */
	@Factory
	public static Matcher<Version> lowerThanOrEqualsTo(Version otherVersion) {
		return new ComparationMatcher(Comparator.LOWER_OR_EQUALS, otherVersion);
	}

	/**
	 * Returns a matcher testing if a version number is lower than another one.
	 *
	 * @param otherVersion
	 *            The other version number
	 * @return The matcher
	 */
	@Factory
	public static Matcher<Version> lowerThan(Version otherVersion) {
		return new ComparationMatcher(Comparator.LOWER, otherVersion);
	}
}
