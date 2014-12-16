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
package org.eclipse.wst.jsdt.nodejs.core.tests;

import org.eclipse.wst.jsdt.nodejs.core.tests.internal.semver.RangeTests;
import org.eclipse.wst.jsdt.nodejs.core.tests.internal.semver.VersionTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Launches all the tests.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@RunWith(Suite.class)
@SuiteClasses({VersionTests.class, RangeTests.class })
public class AllTests {
	/**
	 * The constructor.
	 */
	private AllTests() {
		// prevent instantiation
	}
}
