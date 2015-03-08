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

import org.eclipse.wst.jsdt.nodejs.core.api.semver.Range;
import org.eclipse.wst.jsdt.nodejs.core.api.semver.Version;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * In those tests, we will see if a specific version number matches a given range by respecting the Node.js
 * semantic versioning rules.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class RangeTests {

	/**
	 * Test some version numbers with exact ranges.
	 */
	@Test
	public void testExactRanges() {
		// valid
		assertTrue(Version.fromString("1.0.0").isIn(Range.fromString("1.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.0.0").isIn(Range.fromString("1.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.0.0").isIn(Range.fromString("1"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.0.0+build2012").isIn(Range.fromString("1.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$

		assertTrue(Version.fromString("1.0.0").isIn(Range.fromString("v1.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.0.0").isIn(Range.fromString("v1.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.0.0").isIn(Range.fromString("v1"))); //$NON-NLS-1$ //$NON-NLS-2$

		assertTrue(Version.fromString("1.0.0").isIn(Range.fromString("=1.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.0.0").isIn(Range.fromString("=1.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.0.0").isIn(Range.fromString("=1"))); //$NON-NLS-1$ //$NON-NLS-2$

		assertTrue(Version.fromString("1.0.0").isIn(Range.fromString("=v1.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.0.0").isIn(Range.fromString("=v1.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.0.0").isIn(Range.fromString("=v1"))); //$NON-NLS-1$ //$NON-NLS-2$

		// invalid
		assertFalse(Version.fromString("1.0.1").isIn(Range.fromString("1.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(Version.fromString("1.1.0").isIn(Range.fromString("1.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(Version.fromString("2.0.0").isIn(Range.fromString("1.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(Version.fromString("1.0.1").isIn(Range.fromString("=1.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(Version.fromString("0.9.9").isIn(Range.fromString("=1.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(Version.fromString("1.0.0-beta").isIn(Range.fromString("=1.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(Version.fromString("1.0.0-alpha").isIn(Range.fromString("=1.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(Version.fromString("1.0.0-alpha+build2014").isIn(Range.fromString("=v1.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Test some version numbers with approximate ranges.
	 */
	@Test
	public void testApproximateRanges() {
		// valid
		assertTrue(Version.fromString("1.0.0").isIn(Range.fromString(">=v1.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.0.0").isIn(Range.fromString(">=v1.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.0.0").isIn(Range.fromString(">=1"))); //$NON-NLS-1$ //$NON-NLS-2$

		assertTrue(Version.fromString("1.0.0").isIn(Range.fromString(">=0.0.1"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.0.0").isIn(Range.fromString(">=0.9"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.0.0").isIn(Range.fromString(">=0"))); //$NON-NLS-1$ //$NON-NLS-2$

		assertTrue(Version.fromString("1.0.7").isIn(Range.fromString(">v0.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.3.0").isIn(Range.fromString(">v1.2"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.4.2").isIn(Range.fromString(">v1.4.1"))); //$NON-NLS-1$ //$NON-NLS-2$

		assertTrue(Version.fromString("1.0.7").isIn(Range.fromString("<v2.0.8"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.3.0").isIn(Range.fromString("<v1.4"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.4.2").isIn(Range.fromString("<v1.4.11"))); //$NON-NLS-1$ //$NON-NLS-2$

		assertTrue(Version.fromString("1.0.7").isIn(Range.fromString(">v1.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.3.0").isIn(Range.fromString(">v1.2"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("1.4.2").isIn(Range.fromString(">v1.3.11"))); //$NON-NLS-1$ //$NON-NLS-2$

		assertTrue(Version.fromString("1.0.7-beta+build1234").isIn(Range.fromString(">v1.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("2.0.0-beta+build1234").isIn(Range.fromString(">v1.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("2.0.0-beta+build1234").isIn(Range.fromString("<=v2.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$

		// invalid
		assertFalse(Version.fromString("1.0.0").isIn(Range.fromString(">1.0.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(Version.fromString("1.0.0-beta").isIn(Range.fromString(">=v1.0"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(Version.fromString("1.1.1").isIn(Range.fromString(">=1.2"))); //$NON-NLS-1$ //$NON-NLS-2$

	}

	/**
	 * Test some version numbers with multiple ranges.
	 */
	@Test
	public void testMultipleRanges() {
		// valid
		assertTrue(Version.fromString("1.0.2").isIn(Range.fromString(">v0.9.3 <v1.1.8"))); //$NON-NLS-1$ //$NON-NLS-2$

		// invalid
		assertFalse(Version.fromString("1.0.2").isIn(Range.fromString(">v1.9.3 <v2.1.8"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertFalse(Version.fromString("1.0.2").isIn(Range.fromString(">v0.9.3 <v0.9.8"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Test some version numbers with any range.
	 */
	@Test
	public void testAnyRange() {
		// valid
		assertTrue(Version.fromString("1.0.7-beta+build1234").isIn(Range.fromString("*"))); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(Version.fromString("v8").isIn(Range.fromString("*"))); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
