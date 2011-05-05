/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.rhino.tests;

import org.eclipse.wst.jsdt.debug.internal.rhino.transport.RhinoRequest;
import org.eclipse.wst.jsdt.debug.transport.exception.DisconnectedException;
import org.eclipse.wst.jsdt.debug.transport.exception.TimeoutException;
import org.eclipse.wst.jsdt.debug.transport.packet.Response;

public class RequestBadCommandTest extends RequestTest {

	public void testBadCommand() throws DisconnectedException, TimeoutException {
		RhinoRequest request = new RhinoRequest("bad_command"); //$NON-NLS-1$
		debugSession.send(request);
		Response response = debugSession.receiveResponse(request.getSequence(), 30000);
		assertFalse(response.isSuccess());
	}
}
