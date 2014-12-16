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
import org.junit.Test;

import static org.eclipse.wst.jsdt.nodejs.core.tests.internal.semver.ComparationMatcher.equalTo;
import static org.eclipse.wst.jsdt.nodejs.core.tests.internal.semver.ComparationMatcher.greaterThan;
import static org.eclipse.wst.jsdt.nodejs.core.tests.internal.semver.ComparationMatcher.greaterThanOrEqualsTo;
import static org.eclipse.wst.jsdt.nodejs.core.tests.internal.semver.ComparationMatcher.lowerThan;
import static org.eclipse.wst.jsdt.nodejs.core.tests.internal.semver.ComparationMatcher.lowerThanOrEqualsTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Unit tests of the version numbers.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class VersionTests {
	/**
	 * Test the string serialization of some version numbers.
	 */
	@Test
	public void testVersionToString() {
		assertThat(Version.fromString("v1").toString(), is("1.0.0")); //$NON-NLS-1$ //$NON-NLS-2$
		assertThat(Version.fromString("v1.2").toString(), is("1.2.0")); //$NON-NLS-1$ //$NON-NLS-2$
		assertThat(Version.fromString("v1.2.3").toString(), is("1.2.3")); //$NON-NLS-1$ //$NON-NLS-2$
		assertThat(Version.fromString("1.2.3").toString(), is("1.2.3")); //$NON-NLS-1$ //$NON-NLS-2$

		assertThat(Version.fromString("1.2.3-beta").toString(), is("1.2.3-beta")); //$NON-NLS-1$ //$NON-NLS-2$
		assertThat(Version.fromString("v1.2.3-beta").toString(), is("1.2.3-beta")); //$NON-NLS-1$ //$NON-NLS-2$
		assertThat(Version.fromString("v1.2-beta").toString(), is("1.2.0-beta")); //$NON-NLS-1$ //$NON-NLS-2$
		assertThat(Version.fromString("v1-beta").toString(), is("1.0.0-beta")); //$NON-NLS-1$ //$NON-NLS-2$
		assertThat(Version.fromString("v1-beta2.5").toString(), is("1.0.0-beta2.5")); //$NON-NLS-1$ //$NON-NLS-2$

		assertThat(Version.fromString("1.2.3+build2014").toString(), is("1.2.3+build2014")); //$NON-NLS-1$ //$NON-NLS-2$
		assertThat(Version.fromString("v1.2.3+build2014").toString(), is("1.2.3+build2014")); //$NON-NLS-1$ //$NON-NLS-2$
		assertThat(Version.fromString("v1.2+build2014").toString(), is("1.2.0+build2014")); //$NON-NLS-1$ //$NON-NLS-2$
		assertThat(Version.fromString("v1+build2014").toString(), is("1.0.0+build2014")); //$NON-NLS-1$ //$NON-NLS-2$

		assertThat(Version.fromString("v1.25-beta2.5+build2014-06-11").toString(), //$NON-NLS-1$
				is("1.25.0-beta2.5+build2014-06-11")); //$NON-NLS-1$
	}

	/**
	 * Test that if a version number starts with a "v", this prefix gets trimmed.
	 */
	@Test
	public void testPrefixTrimming() {
		assertThat(Version.fromString("v1.0.0"), is(equalTo(Version.fromString("1.0.0")))); //$NON-NLS-1$ //$NON-NLS-2$
		assertThat(Version.fromString("v1.0"), is(equalTo(Version.fromString("1.0.0")))); //$NON-NLS-1$ //$NON-NLS-2$
		assertThat(Version.fromString("v1"), is(equalTo(Version.fromString("1.0.0")))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Test the comparison of version number for the major part.
	 */
	@Test
	public void testMajorVersionNumbersComparison() {
		// greater
		assertThat(Version.fromString("1.0.0"), is(greaterThan(Version.fromString("0.99.999")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1.0"), is(greaterThan(Version.fromString("0.99.999")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1"), is(greaterThan(Version.fromString("0.99.999")))); //$NON-NLS-1$//$NON-NLS-2$

		// greater or equals
		assertThat(Version.fromString("1.0.0"), is(greaterThanOrEqualsTo(Version.fromString("1.0.0")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1.0.0"), is(greaterThanOrEqualsTo(Version.fromString("0.99.999")))); //$NON-NLS-1$//$NON-NLS-2$

		assertThat(Version.fromString("1.0"), is(greaterThanOrEqualsTo(Version.fromString("1.0.0")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1.0"), is(greaterThanOrEqualsTo(Version.fromString("0.99.999")))); //$NON-NLS-1$//$NON-NLS-2$

		assertThat(Version.fromString("1"), is(greaterThanOrEqualsTo(Version.fromString("1.0.0")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1"), is(greaterThanOrEqualsTo(Version.fromString("0.99.999")))); //$NON-NLS-1$//$NON-NLS-2$

		// equals
		assertThat(Version.fromString("1.0.0"), is(equalTo(Version.fromString("1.0.0")))); //$NON-NLS-1$ //$NON-NLS-2$
		assertThat(Version.fromString("1.0"), is(equalTo(Version.fromString("1.0.0")))); //$NON-NLS-1$ //$NON-NLS-2$
		assertThat(Version.fromString("1"), is(equalTo(Version.fromString("1.0.0")))); //$NON-NLS-1$ //$NON-NLS-2$

		// lower or equals
		assertThat(Version.fromString("1.0.0"), is(lowerThanOrEqualsTo(Version.fromString("1.0.0")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1.0.0"), is(lowerThanOrEqualsTo(Version.fromString("9.99.999")))); //$NON-NLS-1$//$NON-NLS-2$

		assertThat(Version.fromString("1.0"), is(lowerThanOrEqualsTo(Version.fromString("1.0.0")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1.0"), is(lowerThanOrEqualsTo(Version.fromString("9.99.999")))); //$NON-NLS-1$//$NON-NLS-2$

		assertThat(Version.fromString("1"), is(lowerThanOrEqualsTo(Version.fromString("1.0.0")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1"), is(lowerThanOrEqualsTo(Version.fromString("9.99.999")))); //$NON-NLS-1$//$NON-NLS-2$

		// lower
		assertThat(Version.fromString("1.0.0"), is(lowerThan(Version.fromString("9.99.999")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1.0"), is(lowerThan(Version.fromString("9.99.999")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1"), is(lowerThan(Version.fromString("9.99.999")))); //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * Test the comparison of version numbers for the minor part.
	 */
	@Test
	public void testMinorVersionNumbersComparison() {
		assertThat(Version.fromString("1.1.0"), is(greaterThan(Version.fromString("1.0.0")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1.1"), is(greaterThan(Version.fromString("1.0.0")))); //$NON-NLS-1$//$NON-NLS-2$

		assertThat(Version.fromString("1.1.0"), is(greaterThanOrEqualsTo(Version.fromString("1.1.0")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1.1"), is(greaterThanOrEqualsTo(Version.fromString("1.1.0")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1.1.0"), is(greaterThanOrEqualsTo(Version.fromString("1.0.0")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1.1"), is(greaterThanOrEqualsTo(Version.fromString("1.0.0")))); //$NON-NLS-1$//$NON-NLS-2$

		assertThat(Version.fromString("1.1.0"), is(equalTo(Version.fromString("1.1.0")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1.1.0"), is(equalTo(Version.fromString("1.1")))); //$NON-NLS-1$//$NON-NLS-2$

		assertThat(Version.fromString("1.2.0"), is(lowerThanOrEqualsTo(Version.fromString("1.2.0")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1.2"), is(lowerThanOrEqualsTo(Version.fromString("1.2.0")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1.2.0"), is(lowerThanOrEqualsTo(Version.fromString("1.3.0")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1.2"), is(lowerThanOrEqualsTo(Version.fromString("1.3.0")))); //$NON-NLS-1$//$NON-NLS-2$

		assertThat(Version.fromString("1.1.0"), is(lowerThan(Version.fromString("1.2.0")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1.1"), is(lowerThan(Version.fromString("1.2.0")))); //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * Test the comparison of version numbers for the patch part.
	 */
	@Test
	public void testPatchVersionNumbersComparison() {
		assertThat(Version.fromString("1.1.1"), is(greaterThan(Version.fromString("1.1.0")))); //$NON-NLS-1$//$NON-NLS-2$

		assertThat(Version.fromString("1.1.1"), is(greaterThanOrEqualsTo(Version.fromString("1.1.1")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1.1.1"), is(greaterThanOrEqualsTo(Version.fromString("1.1.0")))); //$NON-NLS-1$//$NON-NLS-2$

		assertThat(Version.fromString("1.1.1"), is(equalTo(Version.fromString("1.1.1")))); //$NON-NLS-1$//$NON-NLS-2$

		assertThat(Version.fromString("1.1.1"), is(lowerThanOrEqualsTo(Version.fromString("1.1.1")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1.1.1"), is(lowerThanOrEqualsTo(Version.fromString("1.1.2")))); //$NON-NLS-1$//$NON-NLS-2$

		assertThat(Version.fromString("1.1.1"), is(lowerThan(Version.fromString("1.1.2")))); //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * Test the comparison of version numbers with a build part.
	 */
	@Test
	public void testBuildVersionNumbersComparison() {
		assertThat(Version.fromString("1.1.1"), is(equalTo(Version.fromString("1.1.1+build2014")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1.1"), is(equalTo(Version.fromString("1.1.0+build2014")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("1"), is(equalTo(Version.fromString("v1.0.0+build2014")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("v1"), is(equalTo(Version.fromString("v1.0.0+build2014")))); //$NON-NLS-1$//$NON-NLS-2$

		assertThat(Version.fromString("v1+build2014"), is(equalTo(Version.fromString("v1.0.0+build2014")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("v1.0+build2014"), is(equalTo(Version.fromString("v1.0.0+build2014")))); //$NON-NLS-1$//$NON-NLS-2$
		assertThat(Version.fromString("v1.0.0+build2014"), //$NON-NLS-1$
				is(equalTo(Version.fromString("v1.0.0+build2014")))); //$NON-NLS-1$

		assertThat(Version.fromString("v1+build2014"), //$NON-NLS-1$
				is(not(equalTo(Version.fromString("v1.0.0+build2015"))))); //$NON-NLS-1$
		assertThat(Version.fromString("v1+build2014"), //$NON-NLS-1$
				is(lowerThan(Version.fromString("v1.0.0+build2015")))); //$NON-NLS-1$
		assertThat(Version.fromString("v1+build2014"), //$NON-NLS-1$
				is(greaterThan(Version.fromString("v1.0.0+build2013")))); //$NON-NLS-1$
	}

	/**
	 * Test the comparison of version numbers with a qualifier part.
	 */
	@Test
	public void testQualifierVersionNumbersComparison() {
		assertThat(Version.fromString("v1.1-beta"), //$NON-NLS-1$
				is(lowerThan(Version.fromString("v1.1.0")))); //$NON-NLS-1$
		assertThat(Version.fromString("v1.1-beta"), //$NON-NLS-1$
				is(greaterThan(Version.fromString("v1.0.9")))); //$NON-NLS-1$
		assertThat(Version.fromString("v1.1-beta"), //$NON-NLS-1$
				is(greaterThan(Version.fromString("v1.1-alpha")))); //$NON-NLS-1$
	}
}
