/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.rhino.tests.connect;

import java.util.Collection;

import org.eclipse.wst.jsdt.debug.core.jsdi.connect.DisconnectedException;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.TimeoutException;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Request;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.Response;

public class RequestScriptsTest extends RequestTest {

	public void testScriptsWithNoScripts() throws DisconnectedException, TimeoutException {
		Request request = new Request("scripts");
		debugSession.sendRequest(request);
		Response response = debugSession.receiveResponse(request.getSequence(), 30000);
		assertTrue(response.isSuccess());
		Collection scripts = (Collection) response.getBody().get("scripts");
		assertNotNull(scripts);
		assertTrue(scripts.isEmpty());
	}
}
