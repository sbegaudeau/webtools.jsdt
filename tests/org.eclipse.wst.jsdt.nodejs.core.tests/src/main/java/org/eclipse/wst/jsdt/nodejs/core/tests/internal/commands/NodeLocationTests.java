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
package org.eclipse.wst.jsdt.nodejs.core.tests.internal.commands;

import org.eclipse.wst.jsdt.nodejs.core.api.INodeJsConstants;
import org.eclipse.wst.jsdt.nodejs.core.api.commands.NodeVersion;
import org.eclipse.wst.jsdt.webapp.core.api.commands.Which;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Tests of NodeLocation.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class NodeLocationTests {
	/**
	 * Returns the location of node.js on the file system.
	 */
	@Test
	public void testGetLocation() {
		String location = new Which(INodeJsConstants.NODE).call();
		assertNotNull(location);
		String version = new NodeVersion(location).call();
		assertNotNull(version);
	}
}
